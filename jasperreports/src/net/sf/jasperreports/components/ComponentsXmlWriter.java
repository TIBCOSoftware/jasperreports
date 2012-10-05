/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2011 Jaspersoft Corporation. All rights reserved.
 * http://www.jaspersoft.com
 *
 * Unless you have purchased a commercial license agreement from Jaspersoft,
 * the following license terms apply:
 *
 * This program is part of JasperReports.
 *
 * JasperReports is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * JasperReports is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with JasperReports. If not, see <http://www.gnu.org/licenses/>.
 */
package net.sf.jasperreports.components;

import java.io.IOException;
import java.util.List;

import net.sf.jasperreports.components.barbecue.BarbecueComponent;
import net.sf.jasperreports.components.barcode4j.BarcodeComponent;
import net.sf.jasperreports.components.barcode4j.BarcodeXmlWriter;
import net.sf.jasperreports.components.list.ListComponent;
import net.sf.jasperreports.components.list.ListContents;
import net.sf.jasperreports.components.map.MapComponent;
import net.sf.jasperreports.components.map.StandardMapXmlFactory;
import net.sf.jasperreports.components.map.type.MapImageTypeEnum;
import net.sf.jasperreports.components.map.type.MapScaleEnum;
import net.sf.jasperreports.components.map.type.MapTypeEnum;
import net.sf.jasperreports.components.sort.SortComponent;
import net.sf.jasperreports.components.sort.SortComponentXmlWriter;
import net.sf.jasperreports.components.spiderchart.SpiderChartComponent;
import net.sf.jasperreports.components.spiderchart.SpiderChartXmlWriter;
import net.sf.jasperreports.components.table.BaseColumn;
import net.sf.jasperreports.components.table.Cell;
import net.sf.jasperreports.components.table.Column;
import net.sf.jasperreports.components.table.ColumnGroup;
import net.sf.jasperreports.components.table.ColumnVisitor;
import net.sf.jasperreports.components.table.GroupCell;
import net.sf.jasperreports.components.table.TableComponent;
import net.sf.jasperreports.components.table.WhenNoDataTypeTableEnum;
import net.sf.jasperreports.engine.JRComponentElement;
import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRPropertiesUtil;
import net.sf.jasperreports.engine.JRReport;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.component.Component;
import net.sf.jasperreports.engine.component.ComponentKey;
import net.sf.jasperreports.engine.component.ComponentXmlWriter;
import net.sf.jasperreports.engine.type.EvaluationTimeEnum;
import net.sf.jasperreports.engine.util.JRXmlWriteHelper;
import net.sf.jasperreports.engine.util.VersionComparator;
import net.sf.jasperreports.engine.util.XmlNamespace;
import net.sf.jasperreports.engine.xml.JRXmlBaseWriter;
import net.sf.jasperreports.engine.xml.JRXmlConstants;
import net.sf.jasperreports.engine.xml.JRXmlWriter;

/**
 * XML writer for built-in component implementations.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id: ComponentsXmlHandler.java 5655 2012-09-12 13:25:21Z teodord $
 * @see ComponentsExtensionsRegistryFactory
 */
public class ComponentsXmlWriter implements ComponentXmlWriter
{
	/**
	 * 
	 */
	public static final String PROPERTY_COMPONENTS_PREFIX = JRPropertiesUtil.PROPERTY_PREFIX + "components.";

	/**
	 * 
	 */
	public static final String PROPERTY_COMPONENTS_VERSION_SUFFIX = ".version";

	private final JasperReportsContext jasperReportsContext;
	private final VersionComparator versionComparator;
	
	/**
	 * 
	 */
	public ComponentsXmlWriter(JasperReportsContext jasperReportsContext)
	{
		this.jasperReportsContext = jasperReportsContext;
		this.versionComparator = new VersionComparator();
	}

	public void writeToXml(JRComponentElement componentElement, JRXmlWriter reportWriter) throws IOException
	{
		Component component = componentElement.getComponent();
		if (component instanceof ListComponent)
		{
			writeList(componentElement, reportWriter);
		}
		else if (component instanceof TableComponent)
		{
			writeTable(componentElement, reportWriter);
		}
		else if (component instanceof BarbecueComponent)
		{
			writeBarbecue(componentElement, reportWriter);
		}
		else if (component instanceof BarcodeComponent)
		{
			BarcodeXmlWriter barcodeWriter = new BarcodeXmlWriter(
													reportWriter, 
													componentElement, 
													getVersion(componentElement, reportWriter), 
													versionComparator);
			barcodeWriter.writeBarcode();
		}
		else if (component instanceof SpiderChartComponent)
		{
			SpiderChartXmlWriter spiderChartWriter = new SpiderChartXmlWriter(
															jasperReportsContext, 
															getVersion(componentElement, reportWriter), 
															versionComparator);
			spiderChartWriter.writeToXml(componentElement, reportWriter);
		}
		else if (component instanceof SortComponent)
		{
			SortComponentXmlWriter sortWriter = new SortComponentXmlWriter(
														jasperReportsContext, 
														getVersion(componentElement, reportWriter), 
														versionComparator);
			sortWriter.writeToXml(componentElement, reportWriter);
		}
		else if (component instanceof MapComponent)
		{
			writeMap(componentElement, reportWriter);
		}
		
	}

	protected void writeList(JRComponentElement componentElement, JRXmlWriter reportWriter) throws IOException
	{
		ListComponent list = (ListComponent) componentElement.getComponent();
		ComponentKey componentKey = componentElement.getComponentKey();
		JRXmlWriteHelper writer = reportWriter.getXmlWriteHelper();
		
		XmlNamespace namespace = new XmlNamespace(
				ComponentsExtensionsRegistryFactory.NAMESPACE, 
				componentKey.getNamespacePrefix(),
				ComponentsExtensionsRegistryFactory.XSD_LOCATION);
		
		writer.startElement("list", namespace);
		if(isNewerVersionOrEqual(componentElement, reportWriter, JRConstants.VERSION_3_6_1))
		{
			writer.addAttribute("printOrder", list.getPrintOrderValue());
		}
		writer.addAttribute("ignoreWidth", list.getIgnoreWidth()); 
		reportWriter.writeDatasetRun(list.getDatasetRun());
		
		ListContents contents = list.getContents();
		writer.startElement("listContents");
		writer.addAttribute("height", contents.getHeight());
		writer.addAttribute("width", contents.getWidth());
		reportWriter.writeChildElements(contents);
		writer.closeElement();
		
		writer.closeElement();
	}

	protected void writeBarbecue(JRComponentElement componentElement, JRXmlWriter reportWriter) throws IOException
	{
		Component component = componentElement.getComponent();
		BarbecueComponent barcode = (BarbecueComponent) component;
		JRXmlWriteHelper writer = reportWriter.getXmlWriteHelper();
		ComponentKey componentKey = componentElement.getComponentKey();
		
		XmlNamespace namespace = new XmlNamespace(
				ComponentsExtensionsRegistryFactory.NAMESPACE, 
				componentKey.getNamespacePrefix(),
				ComponentsExtensionsRegistryFactory.XSD_LOCATION);
		
		writer.startElement("barbecue", namespace);
		
		writer.addAttribute("type", barcode.getType());
		writer.addAttribute("drawText", barcode.isDrawText());
		writer.addAttribute("checksumRequired", barcode.isChecksumRequired());
		writer.addAttribute("barWidth", barcode.getBarWidth());
		writer.addAttribute("barHeight", barcode.getBarHeight());
		if (isNewerVersionOrEqual(componentElement, reportWriter, JRConstants.VERSION_4_0_0))
		{
			writer.addAttribute("rotation", barcode.getOwnRotation());
		}
		if (barcode.getEvaluationTimeValue() != EvaluationTimeEnum.NOW)
		{
			writer.addAttribute(JRXmlConstants.ATTRIBUTE_evaluationTime, 
					barcode.getEvaluationTimeValue());
		}
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_evaluationGroup, 
				barcode.getEvaluationGroup());

		writeExpression("codeExpression", barcode.getCodeExpression(), false, componentElement, reportWriter);
		writeExpression("applicationIdentifierExpression", barcode.getApplicationIdentifierExpression(), false, componentElement, reportWriter);
		
		writer.closeElement();
	}

	protected void writeMap(JRComponentElement componentElement, JRXmlWriter reportWriter) throws IOException
	{
		Component component = componentElement.getComponent();
		MapComponent map = (MapComponent) component;
		JRXmlWriteHelper writer = reportWriter.getXmlWriteHelper();
		ComponentKey componentKey = componentElement.getComponentKey();
		
		XmlNamespace namespace = new XmlNamespace(
			ComponentsExtensionsRegistryFactory.NAMESPACE, 
			componentKey.getNamespacePrefix(),
			ComponentsExtensionsRegistryFactory.XSD_LOCATION);
		
		writer.startElement("map", namespace);
		
		if (map.getEvaluationTime() != EvaluationTimeEnum.NOW)
		{
			writer.addAttribute(JRXmlConstants.ATTRIBUTE_evaluationTime, 
				map.getEvaluationTime());
		}
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_evaluationGroup, 
				map.getEvaluationGroup());

		if(isNewerVersionOrEqual(componentElement, reportWriter, JRConstants.VERSION_4_8_0))
		{
			writer.addAttribute(StandardMapXmlFactory.ATTRIBUTE_mapType, map.getMapType(), MapTypeEnum.ROADMAP);
			writer.addAttribute(StandardMapXmlFactory.ATTRIBUTE_mapScale, map.getMapScale(), MapScaleEnum.ONE);
			writer.addAttribute(StandardMapXmlFactory.ATTRIBUTE_imageType, map.getImageType(), MapImageTypeEnum.PNG);
		}

		writer.writeExpression("latitudeExpression", 
			map.getLatitudeExpression());
		writer.writeExpression("longitudeExpression", 
				map.getLongitudeExpression());
		writer.writeExpression("zoomExpression", 
				map.getZoomExpression());
		
		writer.closeElement();
	}

	protected void writeTable(final JRComponentElement componentElement, final JRXmlWriter reportWriter) throws IOException
	{
		Component component = componentElement.getComponent();
		TableComponent table = (TableComponent) component;
		final JRXmlWriteHelper writer = reportWriter.getXmlWriteHelper();
		ComponentKey componentKey = componentElement.getComponentKey();
		
		XmlNamespace namespace = new XmlNamespace(
				ComponentsExtensionsRegistryFactory.NAMESPACE, 
				componentKey.getNamespacePrefix(),
				ComponentsExtensionsRegistryFactory.XSD_LOCATION);
		
		writer.startElement("table", namespace);
		if (isNewerVersionOrEqual(componentElement, reportWriter, JRConstants.VERSION_4_1_1))
		{
			writer.addAttribute(JRXmlConstants.ATTRIBUTE_whenNoDataType, table.getWhenNoDataType(), WhenNoDataTypeTableEnum.BLANK);
		}
		reportWriter.writeDatasetRun(table.getDatasetRun());
		
		ColumnVisitor<Void> columnWriter = new ColumnVisitor<Void>()
		{
			public Void visitColumn(Column column)
			{
				try
				{
					writer.startElement("column");
					writer.addAttribute("width", column.getWidth());
					if(isNewerVersionOrEqual(componentElement, reportWriter, JRConstants.VERSION_4_6_0))
					{
						writer.addEncodedAttribute(JRXmlConstants.ATTRIBUTE_uuid, column.getUUID().toString());
						reportWriter.writeProperties(column);
						reportWriter.writePropertyExpressions(column.getPropertyExpressions());
					}
					writeExpression(JRXmlConstants.ELEMENT_printWhenExpression, 
							JRXmlWriter.JASPERREPORTS_NAMESPACE, 
							column.getPrintWhenExpression(),
							false, 
							componentElement,
							reportWriter);
					writeTableCell(componentElement, column.getTableHeader(), "tableHeader", reportWriter);
					writeTableCell(componentElement, column.getTableFooter(), "tableFooter", reportWriter);
					writeGroupCells(componentElement, column.getGroupHeaders(), "groupHeader", reportWriter);
					writeGroupCells(componentElement, column.getGroupFooters(), "groupFooter", reportWriter);
					writeTableCell(componentElement, column.getColumnHeader(), "columnHeader", reportWriter);
					writeTableCell(componentElement, column.getColumnFooter(), "columnFooter", reportWriter);
					writeTableCell(componentElement, column.getDetailCell(), "detailCell", reportWriter);
					writer.closeElement();
				}
				catch (IOException e)
				{
					throw new JRRuntimeException(e);
				}
				
				return null;
			}

			public Void visitColumnGroup(ColumnGroup columnGroup)
			{
				try
				{
					writer.startElement("columnGroup");
					writer.addAttribute("width", columnGroup.getWidth());
					if(isNewerVersionOrEqual(componentElement, reportWriter, JRConstants.VERSION_4_6_0))
					{
						writer.addEncodedAttribute(JRXmlConstants.ATTRIBUTE_uuid, columnGroup.getUUID().toString());
						reportWriter.writeProperties(columnGroup);
						reportWriter.writePropertyExpressions(columnGroup.getPropertyExpressions());
					}
					writeExpression(JRXmlConstants.ELEMENT_printWhenExpression, 
							JRXmlWriter.JASPERREPORTS_NAMESPACE, 
							columnGroup.getPrintWhenExpression(),
							false, 
							componentElement,
							reportWriter);
					writeTableCell(componentElement, columnGroup.getTableHeader(), "tableHeader", reportWriter);
					writeTableCell(componentElement, columnGroup.getTableFooter(), "tableFooter", reportWriter);
					writeGroupCells(componentElement, columnGroup.getGroupHeaders(), "groupHeader", reportWriter);
					writeGroupCells(componentElement, columnGroup.getGroupFooters(), "groupFooter", reportWriter);
					writeTableCell(componentElement, columnGroup.getColumnHeader(), "columnHeader", reportWriter);
					writeTableCell(componentElement, columnGroup.getColumnFooter(), "columnFooter", reportWriter);
					
					// deep
					for (BaseColumn column : columnGroup.getColumns())
					{
						column.visitColumn(this);
					}
					
					writer.closeElement();
				}
				catch (IOException e)
				{
					throw new JRRuntimeException(e);
				}
				
				return null;
			}
		};
		
		for (BaseColumn column : table.getColumns())
		{
			column.visitColumn(columnWriter);
		}
		
		writer.closeElement();
	}
	
	protected void writeGroupCells(JRComponentElement componentElement, List<GroupCell> cells, String name, 
			JRXmlWriter reportWriter) throws IOException
	{
		if (cells != null)
		{
			JRXmlWriteHelper writer = reportWriter.getXmlWriteHelper();
			for (GroupCell groupCell : cells)
			{
				writer.startElement(name);
				writer.addAttribute("groupName", groupCell.getGroupName());
				writeTableCell(componentElement, groupCell.getCell(), "cell", reportWriter);
				writer.closeElement();
			}
		}
	}
	
	protected void writeTableCell(JRComponentElement componentElement, Cell cell, String name, 
			JRXmlWriter reportWriter) throws IOException
	{
		if (cell != null)
		{
			JRXmlWriteHelper writer = reportWriter.getXmlWriteHelper();
			writer.startElement(name);
			reportWriter.writeStyleReferenceAttr(cell);
			writer.addAttribute("height", cell.getHeight());
			writer.addAttribute("rowSpan", cell.getRowSpan());
			
			if (isNewerVersionOrEqual(componentElement, reportWriter, JRConstants.VERSION_4_8_0))
			{
				reportWriter.writeProperties(cell);
			}
			reportWriter.writeBox(cell.getLineBox(), JRXmlWriter.JASPERREPORTS_NAMESPACE);
			reportWriter.writeChildElements(cell);
			
			writer.closeElement();//cell
		}
	}

	@Override
	public boolean isToWrite(JRComponentElement componentElement, JRXmlWriter reportWriter) 
	{
		ComponentKey componentKey = componentElement.getComponentKey();
		if (ComponentsExtensionsRegistryFactory.NAMESPACE.equals(componentKey.getNamespace()))
		{
			if(ComponentsExtensionsRegistryFactory.SORT_COMPONENT_NAME.equals(componentKey.getName())
					|| ComponentsExtensionsRegistryFactory.MAP_COMPONENT_NAME.equals(componentKey.getName()))
			{
				return isNewerVersionOrEqual(componentElement, reportWriter, JRConstants.VERSION_4_1_1);
			}
			else if(ComponentsExtensionsRegistryFactory.SPIDERCHART_COMPONENT_NAME.equals(componentKey.getName()))
			{
				return isNewerVersionOrEqual(componentElement, reportWriter, JRConstants.VERSION_3_7_4);
			}
			else if(ComponentsExtensionsRegistryFactory.TABLE_COMPONENT_NAME.equals(componentKey.getName()))
			{
				return isNewerVersionOrEqual(componentElement, reportWriter, JRConstants.VERSION_3_7_2);
			}
			else if(ComponentsExtensionsRegistryFactory.LIST_COMPONENT_NAME.equals(componentKey.getName()))
			{
				return isNewerVersionOrEqual(componentElement, reportWriter, JRConstants.VERSION_3_5_1);
			}
			else if(ComponentsExtensionsRegistryFactory.BARBECUE_COMPONENT_NAME.equals(componentKey.getName())
					|| isBarcode4jName(componentKey.getName()))
			{
				return isNewerVersionOrEqual(componentElement, reportWriter, JRConstants.VERSION_3_5_2);
			}
		}

		return true;
	}
	
	
	protected String getVersion(JRComponentElement componentElement, JRXmlWriter reportWriter) 
	{
		String version = null;

		ComponentKey componentKey = componentElement.getComponentKey();
		String versionProperty = PROPERTY_COMPONENTS_PREFIX + componentKey.getName() + PROPERTY_COMPONENTS_VERSION_SUFFIX;
		
		if (componentElement.getPropertiesMap().containsProperty(versionProperty))
		{
			version = componentElement.getPropertiesMap().getProperty(versionProperty);
		}
		else
		{
			JRReport report = reportWriter.getReport();
			version = JRPropertiesUtil.getInstance(jasperReportsContext).getProperty(report, versionProperty);
			
			if (version == null)
			{
				version = JRPropertiesUtil.getInstance(jasperReportsContext).getProperty(report, JRXmlBaseWriter.PROPERTY_REPORT_VERSION);
			}
		}
		
		return version;
	}

	protected boolean isNewerVersionOrEqual(JRComponentElement componentElement, JRXmlWriter reportWriter, String oldVersion) //FIXMEVERSION can we pass something else then reportWriter?
	{
		return versionComparator.compare(getVersion(componentElement, reportWriter), oldVersion) >= 0;
	}

	protected boolean isBarcode4jName(String name)
	{
		for (String barcode4jName : ComponentsExtensionsRegistryFactory.BARCODE4J_COMPONENT_NAMES)
		{
			if(barcode4jName.equals(name)){
				return true;
			}
		}
		return false;
	}
	
	@SuppressWarnings("deprecation")
	protected void writeExpression(String name, JRExpression expression, boolean writeClass, JRComponentElement componentElement, JRXmlWriter reportWriter)  throws IOException
	{
		JRXmlWriteHelper writer = reportWriter.getXmlWriteHelper();
		if(isNewerVersionOrEqual(componentElement, reportWriter, JRConstants.VERSION_4_1_1))
		{
			writer.writeExpression(name, expression);
		}
		else
		{
			writer.writeExpression(name, expression, writeClass);
		}
	}
	
	@SuppressWarnings("deprecation")
	protected void writeExpression(String name, XmlNamespace namespace, JRExpression expression, boolean writeClass, JRComponentElement componentElement, JRXmlWriter reportWriter)  throws IOException
	{
		JRXmlWriteHelper writer = reportWriter.getXmlWriteHelper();
		if(isNewerVersionOrEqual(componentElement, reportWriter, JRConstants.VERSION_4_1_1))
		{
			writer.writeExpression(name, namespace, expression);
		}
		else
		{
			writer.writeExpression(name, namespace, expression, writeClass);
		}
	}
}
