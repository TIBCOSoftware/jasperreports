/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2009 Jaspersoft Corporation. All rights reserved.
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

import net.sf.jasperreports.charts.design.JRDesignCategorySeries;
import net.sf.jasperreports.charts.xml.JRCategorySeriesFactory;
import net.sf.jasperreports.components.barbecue.BarbecueComponent;
import net.sf.jasperreports.components.barbecue.StandardBarbecueComponent;
import net.sf.jasperreports.components.barcode4j.BarcodeComponent;
import net.sf.jasperreports.components.barcode4j.BarcodeXmlWriter;
import net.sf.jasperreports.components.barcode4j.CodabarComponent;
import net.sf.jasperreports.components.barcode4j.Code128Component;
import net.sf.jasperreports.components.barcode4j.Code39Component;
import net.sf.jasperreports.components.barcode4j.DataMatrixComponent;
import net.sf.jasperreports.components.barcode4j.EAN128Component;
import net.sf.jasperreports.components.barcode4j.EAN13Component;
import net.sf.jasperreports.components.barcode4j.EAN8Component;
import net.sf.jasperreports.components.barcode4j.Interleaved2Of5Component;
import net.sf.jasperreports.components.barcode4j.PDF417Component;
import net.sf.jasperreports.components.barcode4j.POSTNETComponent;
import net.sf.jasperreports.components.barcode4j.RoyalMailCustomerComponent;
import net.sf.jasperreports.components.barcode4j.UPCAComponent;
import net.sf.jasperreports.components.barcode4j.UPCEComponent;
import net.sf.jasperreports.components.barcode4j.USPSIntelligentMailComponent;
import net.sf.jasperreports.components.list.DesignListContents;
import net.sf.jasperreports.components.list.ListComponent;
import net.sf.jasperreports.components.list.ListContents;
import net.sf.jasperreports.components.list.StandardListComponent;
import net.sf.jasperreports.components.spiderchart.ChartSettings;
import net.sf.jasperreports.components.spiderchart.ChartSettingsXmlFactory;
import net.sf.jasperreports.components.spiderchart.SpiderChartComponent;
import net.sf.jasperreports.components.spiderchart.SpiderChartXmlFactory;
import net.sf.jasperreports.components.spiderchart.SpiderChartXmlWriter;
import net.sf.jasperreports.components.spiderchart.SpiderDataset;
import net.sf.jasperreports.components.spiderchart.SpiderDatasetXmlFactory;
import net.sf.jasperreports.components.spiderchart.SpiderPlot;
import net.sf.jasperreports.components.spiderchart.SpiderPlotXmlFactory;
import net.sf.jasperreports.components.table.BaseColumn;
import net.sf.jasperreports.components.table.Cell;
import net.sf.jasperreports.components.table.Column;
import net.sf.jasperreports.components.table.ColumnGroup;
import net.sf.jasperreports.components.table.ColumnVisitor;
import net.sf.jasperreports.components.table.DesignCell;
import net.sf.jasperreports.components.table.GroupCell;
import net.sf.jasperreports.components.table.StandardColumn;
import net.sf.jasperreports.components.table.StandardColumnGroup;
import net.sf.jasperreports.components.table.StandardGroupCell;
import net.sf.jasperreports.components.table.StandardTable;
import net.sf.jasperreports.components.table.TableComponent;
import net.sf.jasperreports.components.table.TableReportContextXmlRule;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRFont;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.component.Component;
import net.sf.jasperreports.engine.component.ComponentKey;
import net.sf.jasperreports.engine.component.ComponentXmlWriter;
import net.sf.jasperreports.engine.component.XmlDigesterConfigurer;
import net.sf.jasperreports.engine.design.JRDesignExpression;
import net.sf.jasperreports.engine.type.EvaluationTimeEnum;
import net.sf.jasperreports.engine.type.PrintOrderEnum;
import net.sf.jasperreports.engine.util.JRXmlWriteHelper;
import net.sf.jasperreports.engine.util.XmlNamespace;
import net.sf.jasperreports.engine.xml.JRElementDatasetFactory;
import net.sf.jasperreports.engine.xml.JRExpressionFactory;
import net.sf.jasperreports.engine.xml.JRFontFactory;
import net.sf.jasperreports.engine.xml.JRXmlWriter;
import net.sf.jasperreports.engine.xml.StyleContainerRule;
import net.sf.jasperreports.engine.xml.XmlConstantPropertyRule;
import net.sf.jasperreports.engine.xml.JRXmlConstants;

import org.apache.commons.digester.Digester;

/**
 * XML handler (digester + writer) for built-in component implementations.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 * @see ComponentsExtensionsRegistryFactory
 */
public class ComponentsXmlHandler implements XmlDigesterConfigurer, ComponentXmlWriter
{

	public void configureDigester(Digester digester)
	{
		addListRules(digester);
		addBarbecueRules(digester);
		addBarcode4jRules(digester);
		addTableRules(digester);
		addSpiderChartRules(digester);
	}

	protected void addListRules(Digester digester)
	{
		String listPattern = "*/componentElement/list";
		digester.addObjectCreate(listPattern, StandardListComponent.class);
		digester.addSetProperties(listPattern,
				//properties to be ignored by this rule
				new String[]{"printOrder"}, 
				new String[0]);
		digester.addRule(listPattern, new XmlConstantPropertyRule(
				"printOrder", "printOrderValue", PrintOrderEnum.values()));
		
		String listContentsPattern = listPattern + "/listContents";
		digester.addObjectCreate(listContentsPattern, DesignListContents.class);
		digester.addSetProperties(listContentsPattern);
		digester.addSetNext(listContentsPattern, "setContents");
	}

	protected void addBarbecueRules(Digester digester)
	{
		String barcodePattern = "*/componentElement/barbecue";
		digester.addObjectCreate(barcodePattern, StandardBarbecueComponent.class);
		digester.addSetProperties(barcodePattern,
				//properties to be ignored by this rule
				new String[]{JRXmlConstants.ATTRIBUTE_evaluationTime}, 
				new String[0]);
		digester.addRule(barcodePattern, 
				new XmlConstantPropertyRule(
						JRXmlConstants.ATTRIBUTE_evaluationTime, "evaluationTimeValue",
						EvaluationTimeEnum.values()));

		String barcodeExpressionPattern = barcodePattern + "/codeExpression";
		digester.addFactoryCreate(barcodeExpressionPattern, 
				JRExpressionFactory.StringExpressionFactory.class.getName());
		digester.addCallMethod(barcodeExpressionPattern, "setText", 0);
		digester.addSetNext(barcodeExpressionPattern, "setCodeExpression", 
				JRExpression.class.getName());

		String applicationIdentifierExpressionPattern = barcodePattern 
				+ "/applicationIdentifierExpression";
		digester.addFactoryCreate(applicationIdentifierExpressionPattern, 
				JRExpressionFactory.StringExpressionFactory.class.getName());
		digester.addCallMethod(applicationIdentifierExpressionPattern, "setText", 0);
		digester.addSetNext(applicationIdentifierExpressionPattern, 
				"setApplicationIdentifierExpression", 
				JRExpression.class.getName());
	}

	protected void addBarcode4jRules(Digester digester)
	{
		addBaseBarcode4jRules(digester, 
				"*/componentElement/Codabar", 
				CodabarComponent.class);
		addBaseBarcode4jRules(digester, 
				"*/componentElement/Code128", 
				Code128Component.class);
		addBaseBarcode4jRules(digester, 
				"*/componentElement/EAN128", 
				EAN128Component.class);
		addBaseBarcode4jRules(digester, 
				"*/componentElement/DataMatrix", 
				DataMatrixComponent.class);
		addBaseBarcode4jRules(digester, 
				"*/componentElement/RoyalMailCustomer", 
				RoyalMailCustomerComponent.class);
		addBaseBarcode4jRules(digester, 
				"*/componentElement/USPSIntelligentMail", 
				USPSIntelligentMailComponent.class);
		addBaseBarcode4jRules(digester, 
				"*/componentElement/Code39", Code39Component.class);
		addBaseBarcode4jRules(digester, 
				"*/componentElement/Interleaved2Of5", Interleaved2Of5Component.class);
		addBaseBarcode4jRules(digester, 
				"*/componentElement/UPCA", UPCAComponent.class);
		addBaseBarcode4jRules(digester, 
				"*/componentElement/UPCE", UPCEComponent.class);
		addBaseBarcode4jRules(digester, 
				"*/componentElement/EAN13", EAN13Component.class);
		addBaseBarcode4jRules(digester, 
				"*/componentElement/EAN8", EAN8Component.class);
		addBaseBarcode4jRules(digester, 
				"*/componentElement/POSTNET", POSTNETComponent.class);
		addBaseBarcode4jRules(digester, 
				"*/componentElement/PDF417", PDF417Component.class);
	}
	
	protected void addBaseBarcode4jRules(Digester digester, 
			String barcodePattern, Class barcodeComponentClass)
	{
		digester.addObjectCreate(barcodePattern, barcodeComponentClass);
		digester.addSetProperties(barcodePattern,
				//properties to be ignored by this rule
				new String[]{JRXmlConstants.ATTRIBUTE_evaluationTime}, 
				new String[0]);
		//rule to set evaluation time
		digester.addRule(barcodePattern, 
				new XmlConstantPropertyRule(
						JRXmlConstants.ATTRIBUTE_evaluationTime, "evaluationTimeValue",
						EvaluationTimeEnum.values()));
		
		String codeExpressionPattern = barcodePattern + "/codeExpression";
		digester.addFactoryCreate(codeExpressionPattern, 
				JRExpressionFactory.StringExpressionFactory.class.getName());
		digester.addCallMethod(codeExpressionPattern, "setText", 0);
		digester.addSetNext(codeExpressionPattern, "setCodeExpression", 
				JRExpression.class.getName());
		
		String patternExpressionPattern = barcodePattern + "/patternExpression";
		digester.addFactoryCreate(patternExpressionPattern, 
				JRExpressionFactory.StringExpressionFactory.class.getName());
		digester.addCallMethod(patternExpressionPattern, "setText", 0);
		digester.addSetNext(patternExpressionPattern, "setPatternExpression", 
				JRExpression.class.getName());
	}

	protected void addTableRules(Digester digester)
	{
		String tablePattern = "*/componentElement/table";
		digester.addObjectCreate(tablePattern, StandardTable.class);
		
		String columnPattern = "*/column";
		digester.addObjectCreate(columnPattern, StandardColumn.class);
		digester.addSetNext(columnPattern, "addColumn");
		digester.addSetProperties(columnPattern);
		addExpressionRules(digester, columnPattern + "/printWhenExpression", 
				JRExpressionFactory.BooleanExpressionFactory.class, "setPrintWhenExpression",
				true);
		addTableCellRules(digester, columnPattern + "/tableHeader", "setTableHeader");
		addTableCellRules(digester, columnPattern + "/tableFooter", "setTableFooter");
		addTableGroupCellRules(digester, columnPattern + "/groupHeader", "addGroupHeader");
		addTableGroupCellRules(digester, columnPattern + "/groupFooter", "addGroupFooter");
		addTableCellRules(digester, columnPattern + "/columnHeader", "setColumnHeader");
		addTableCellRules(digester, columnPattern + "/columnFooter", "setColumnFooter");
		addTableCellRules(digester, columnPattern + "/detailCell", "setDetailCell");
		
		String columnGroupPattern = "*/columnGroup";
		digester.addObjectCreate(columnGroupPattern, StandardColumnGroup.class);
		digester.addSetNext(columnGroupPattern, "addColumn");
		digester.addSetProperties(columnGroupPattern);
		addExpressionRules(digester, columnGroupPattern + "/printWhenExpression", 
				JRExpressionFactory.BooleanExpressionFactory.class, "setPrintWhenExpression",
				true);
		addTableCellRules(digester, columnGroupPattern + "/tableHeader", "setTableHeader");
		addTableCellRules(digester, columnGroupPattern + "/tableFooter", "setTableFooter");
		addTableGroupCellRules(digester, columnGroupPattern + "/groupHeader", "addGroupHeader");
		addTableGroupCellRules(digester, columnGroupPattern + "/groupFooter", "addGroupFooter");
		addTableCellRules(digester, columnGroupPattern + "/columnHeader", "setColumnHeader");
		addTableCellRules(digester, columnGroupPattern + "/columnFooter", "setColumnFooter");
	}
	
	protected void addTableCellRules(Digester digester, String pattern, 
			String setNextMethod)
	{
		digester.addObjectCreate(pattern, DesignCell.class);
		digester.addSetNext(pattern, setNextMethod);
		// rule to set the context dataset name
		digester.addRule(pattern, new TableReportContextXmlRule());
		
		digester.addSetProperties(pattern,
				new String[]{JRXmlConstants.ATTRIBUTE_style}, 
				new String[0]);
		digester.addRule(pattern, new StyleContainerRule());
	}
	
	protected void addTableGroupCellRules(Digester digester, String pattern, 
			String setNextMethod)
	{
		digester.addObjectCreate(pattern, StandardGroupCell.class);
		digester.addSetProperties(pattern);
		addTableCellRules(digester, pattern + "/cell", "setCell");
		digester.addSetNext(pattern, setNextMethod);
	}

	protected void addExpressionRules(Digester digester, String expressionPattern,
			Class factoryClass, String setterMethod, boolean jrNamespace)
	{
		String originalNamespace = digester.getRuleNamespaceURI();
		if (jrNamespace)
		{
			digester.setRuleNamespaceURI(JRXmlWriter.JASPERREPORTS_NAMESPACE.getNamespaceURI());
		}
		
		digester.addFactoryCreate(expressionPattern, factoryClass);
		digester.addCallMethod(expressionPattern, "setText", 0);
		digester.addSetNext(expressionPattern, setterMethod,
				JRExpression.class.getName());
		
		if (jrNamespace)
		{
			digester.setRuleNamespaceURI(originalNamespace);
		}
	}
	
	public void writeToXml(ComponentKey componentKey, Component component,
			JRXmlWriter reportWriter) throws IOException
	{
		if (component instanceof ListComponent)
		{
			ListComponent list = (ListComponent) component;
			writeList(list, componentKey, reportWriter);
		}
		else if (component instanceof TableComponent)
		{
			TableComponent table = (TableComponent) component;
			writeTable(table, componentKey, reportWriter);
		}
		else if (component instanceof BarbecueComponent)
		{
			BarbecueComponent barcode = (BarbecueComponent) component;
			writeBarbecue(barcode, componentKey, reportWriter);
		}
		else if (component instanceof BarcodeComponent)
		{
			BarcodeComponent barcode = (BarcodeComponent) component;
			BarcodeXmlWriter barcodeWriter = new BarcodeXmlWriter(
					reportWriter, barcode, componentKey);
			barcodeWriter.writeBarcode();
		}
		else if (component instanceof SpiderChartComponent)
		{
			SpiderChartComponent spiderChart = (SpiderChartComponent) component;
			SpiderChartXmlWriter spiderChartWriter = new SpiderChartXmlWriter();
			spiderChartWriter.writeToXml(componentKey, spiderChart, reportWriter);
		}
	}

	protected void writeList(ListComponent list, ComponentKey componentKey,
			JRXmlWriter reportWriter) throws IOException
	{
		JRXmlWriteHelper writer = reportWriter.getXmlWriteHelper();
		
		XmlNamespace namespace = new XmlNamespace(
				ComponentsExtensionsRegistryFactory.NAMESPACE, 
				componentKey.getNamespacePrefix(),
				ComponentsExtensionsRegistryFactory.XSD_LOCATION);
		
		writer.startElement("list", namespace);
		writer.addAttribute("printOrder", list.getPrintOrderValue());
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

	protected void writeBarbecue(BarbecueComponent barcode, ComponentKey componentKey,
			JRXmlWriter reportWriter) throws IOException
	{
		JRXmlWriteHelper writer = reportWriter.getXmlWriteHelper();
		
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
		if (barcode.getEvaluationTimeValue() != EvaluationTimeEnum.NOW)
		{
			writer.addAttribute(JRXmlConstants.ATTRIBUTE_evaluationTime, 
					barcode.getEvaluationTimeValue());
		}
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_evaluationGroup, 
				barcode.getEvaluationGroup());

		writer.writeExpression("codeExpression", 
				barcode.getCodeExpression(), false);
		writer.writeExpression("applicationIdentifierExpression", 
				barcode.getApplicationIdentifierExpression(), false);
		
		writer.closeElement();
	}

	protected void writeTable(TableComponent table, ComponentKey componentKey,
			final JRXmlWriter reportWriter) throws IOException
	{
		final JRXmlWriteHelper writer = reportWriter.getXmlWriteHelper();
		
		XmlNamespace namespace = new XmlNamespace(
				ComponentsExtensionsRegistryFactory.NAMESPACE, 
				componentKey.getNamespacePrefix(),
				ComponentsExtensionsRegistryFactory.XSD_LOCATION);
		
		writer.startElement("table", namespace);
		reportWriter.writeDatasetRun(table.getDatasetRun());
		
		ColumnVisitor<Void> columnWriter = new ColumnVisitor<Void>()
		{
			public Void visitColumn(Column column)
			{
				try
				{
					writer.startElement("column");
					writer.addAttribute("width", column.getWidth());
					writer.writeExpression(JRXmlConstants.ELEMENT_printWhenExpression, 
							JRXmlWriter.JASPERREPORTS_NAMESPACE, 
							column.getPrintWhenExpression(), false);
					writeTableCell(column.getTableHeader(), "tableHeader", reportWriter);
					writeTableCell(column.getTableFooter(), "tableFooter", reportWriter);
					writeGroupCells(column.getGroupHeaders(), "groupHeader", reportWriter);
					writeGroupCells(column.getGroupFooters(), "groupFooter", reportWriter);
					writeTableCell(column.getColumnHeader(), "columnHeader", reportWriter);
					writeTableCell(column.getColumnFooter(), "columnFooter", reportWriter);
					writeTableCell(column.getDetailCell(), "detailCell", reportWriter);
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
					writer.writeExpression(JRXmlConstants.ELEMENT_printWhenExpression, 
							JRXmlWriter.JASPERREPORTS_NAMESPACE, 
							columnGroup.getPrintWhenExpression(), false);
					writeTableCell(columnGroup.getTableHeader(), "tableHeader", reportWriter);
					writeTableCell(columnGroup.getTableFooter(), "tableFooter", reportWriter);
					writeGroupCells(columnGroup.getGroupHeaders(), "groupHeader", reportWriter);
					writeGroupCells(columnGroup.getGroupFooters(), "groupFooter", reportWriter);
					writeTableCell(columnGroup.getColumnHeader(), "columnHeader", reportWriter);
					writeTableCell(columnGroup.getColumnFooter(), "columnFooter", reportWriter);
					
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
	
	protected void writeGroupCells(List<GroupCell> cells, String name, 
			JRXmlWriter reportWriter) throws IOException
	{
		if (cells != null)
		{
			JRXmlWriteHelper writer = reportWriter.getXmlWriteHelper();
			for (GroupCell groupCell : cells)
			{
				writer.startElement(name);
				writer.addAttribute("groupName", groupCell.getGroupName());
				writeTableCell(groupCell.getCell(), "cell", reportWriter);
				writer.closeElement();
			}
		}
	}
	
	protected void writeTableCell(Cell cell, String name, 
			JRXmlWriter reportWriter) throws IOException
	{
		if (cell != null)
		{
			JRXmlWriteHelper writer = reportWriter.getXmlWriteHelper();
			writer.startElement(name);
			reportWriter.writeStyleReferenceAttr(cell);
			writer.addAttribute("height", cell.getHeight());
			writer.addAttribute("rowSpan", cell.getRowSpan());
			
			reportWriter.writeBox(cell.getLineBox(), JRXmlWriter.JASPERREPORTS_NAMESPACE);
			reportWriter.writeChildElements(cell);
			
			writer.closeElement();//cell
		}
	}
	
	protected void addSpiderChartRules(Digester digester)
	{
		String componentNamespace = digester.getRuleNamespaceURI();
		String jrNamespace = JRXmlConstants.JASPERREPORTS_NAMESPACE;
		
		String spiderChartPattern = "*/componentElement/spiderChart";
		digester.addFactoryCreate(spiderChartPattern, SpiderChartXmlFactory.class.getName());
		
		String chartSettingsPattern = spiderChartPattern + "/chartSettings";
		digester.addFactoryCreate(chartSettingsPattern, ChartSettingsXmlFactory.class.getName());
		digester.addSetNext(chartSettingsPattern, "setChartSettings", ChartSettings.class.getName());

		digester.setRuleNamespaceURI(jrNamespace);
		String chartTitlePattern = chartSettingsPattern + "/chartTitle";
		digester.addFactoryCreate(chartTitlePattern, ChartSettingsXmlFactory.ChartTitleFactory.class.getName());
		digester.addFactoryCreate(chartTitlePattern + "/font", JRFontFactory.ChartFontFactory.class.getName());
		digester.addSetNext(chartTitlePattern + "/font", "setTitleFont", JRFont.class.getName());
		digester.addFactoryCreate(chartTitlePattern + "/titleExpression", JRExpressionFactory.StringExpressionFactory.class);
		digester.addSetNext(chartTitlePattern + "/titleExpression", "setTitleExpression", JRDesignExpression.class.getName());
		digester.addCallMethod(chartTitlePattern + "/titleExpression", "setText", 0);
		
		String chartSubtitlePattern = chartSettingsPattern + "/chartSubtitle";
		digester.addFactoryCreate(chartSubtitlePattern, ChartSettingsXmlFactory.ChartSubtitleFactory.class.getName());
		digester.addFactoryCreate(chartSubtitlePattern + "/font", JRFontFactory.ChartFontFactory.class.getName());
		digester.addSetNext(chartSubtitlePattern + "/font", "setSubtitleFont", JRFont.class.getName());
		digester.addFactoryCreate(chartSubtitlePattern + "/subtitleExpression", JRExpressionFactory.StringExpressionFactory.class);
		digester.addSetNext(chartSubtitlePattern + "/subtitleExpression", "setSubtitleExpression", JRDesignExpression.class.getName());
		digester.addCallMethod(chartSubtitlePattern + "/subtitleExpression", "setText", 0);
		
		digester.addFactoryCreate(chartSettingsPattern + "/chartLegend", ChartSettingsXmlFactory.ChartLegendFactory.class.getName());
		digester.addFactoryCreate(chartSettingsPattern + "/chartLegend/font", JRFontFactory.ChartFontFactory.class.getName());
		digester.addSetNext(chartSettingsPattern + "/chartLegend/font", "setLegendFont", JRFont.class.getName());

		digester.setRuleNamespaceURI(componentNamespace);
		String spiderDatasetPattern = spiderChartPattern + "/spiderDataset";
		digester.addFactoryCreate(spiderDatasetPattern, SpiderDatasetXmlFactory.class.getName());
		digester.addSetNext(spiderDatasetPattern, "setDataset", SpiderDataset.class.getName());
		
		digester.setRuleNamespaceURI(jrNamespace);
		String datasetPattern = spiderDatasetPattern + "/dataset";
		digester.addFactoryCreate(datasetPattern, JRElementDatasetFactory.class.getName());

		String datasetIncrementWhenExpressionPath = datasetPattern + JRXmlConstants.ELEMENT_incrementWhenExpression;
		digester.addFactoryCreate(datasetIncrementWhenExpressionPath, JRExpressionFactory.BooleanExpressionFactory.class.getName());
		digester.addSetNext(datasetIncrementWhenExpressionPath, "setIncrementWhenExpression", JRExpression.class.getName());
		digester.addCallMethod(datasetIncrementWhenExpressionPath, "setText", 0);

		String seriesPattern = spiderDatasetPattern + "/categorySeries";
		digester.addFactoryCreate(seriesPattern, JRCategorySeriesFactory.class.getName());
		digester.addSetNext(seriesPattern, "addCategorySeries", JRDesignCategorySeries.class.getName());

		digester.addFactoryCreate(seriesPattern + "/seriesExpression", JRExpressionFactory.ComparableExpressionFactory.class);
		digester.addSetNext(seriesPattern + "/seriesExpression", "setSeriesExpression", JRDesignExpression.class.getName());
		digester.addCallMethod(seriesPattern + "/seriesExpression", "setText", 0);
		digester.addFactoryCreate(seriesPattern + "/categoryExpression", JRExpressionFactory.ComparableExpressionFactory.class);
		digester.addSetNext(seriesPattern + "/categoryExpression", "setCategoryExpression", JRDesignExpression.class.getName());
		digester.addCallMethod(seriesPattern + "/categoryExpression", "setText", 0);
		digester.addFactoryCreate(seriesPattern + "/labelExpression", JRExpressionFactory.StringExpressionFactory.class);
		digester.addSetNext(seriesPattern + "/labelExpression", "setLabelExpression", JRDesignExpression.class.getName());
		digester.addCallMethod(seriesPattern + "/labelExpression", "setText", 0);
		digester.addFactoryCreate(seriesPattern + "/valueExpression", JRExpressionFactory.NumberExpressionFactory.class);
		digester.addSetNext(seriesPattern + "/valueExpression", "setValueExpression", JRDesignExpression.class.getName());
		digester.addCallMethod(seriesPattern + "/valueExpression", "setText", 0);
		
		digester.setRuleNamespaceURI(componentNamespace);
		String plotPattern = spiderChartPattern + "/spiderPlot";
		digester.addFactoryCreate(plotPattern, SpiderPlotXmlFactory.class.getName());
		digester.addSetNext(plotPattern, "setPlot", SpiderPlot.class.getName());

		digester.setRuleNamespaceURI(jrNamespace);
		String labelFontPattern = plotPattern + "/labelFont/font";
		digester.addFactoryCreate(labelFontPattern, JRFontFactory.ChartFontFactory.class.getName());
		digester.addSetNext(labelFontPattern, "setLabelFont", JRFont.class.getName());

		digester.setRuleNamespaceURI(componentNamespace);
		String maxValueExpressionPattern = plotPattern + "/maxValueExpression";
		digester.addFactoryCreate(maxValueExpressionPattern, JRExpressionFactory.DoubleExpressionFactory.class);
		digester.addSetNext(maxValueExpressionPattern, "setMaxValueExpression", JRDesignExpression.class.getName() );
		digester.addCallMethod(maxValueExpressionPattern, "setText", 0 );
	}
	
}
