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
package net.sf.jasperreports.engine.xml;

import java.awt.Color;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;

import net.sf.jasperreports.charts.JRAreaPlot;
import net.sf.jasperreports.charts.JRBar3DPlot;
import net.sf.jasperreports.charts.JRBarPlot;
import net.sf.jasperreports.charts.JRBubblePlot;
import net.sf.jasperreports.charts.JRCandlestickPlot;
import net.sf.jasperreports.charts.JRCategoryDataset;
import net.sf.jasperreports.charts.JRCategorySeries;
import net.sf.jasperreports.charts.JRChartAxis;
import net.sf.jasperreports.charts.JRDataRange;
import net.sf.jasperreports.charts.JRGanttDataset;
import net.sf.jasperreports.charts.JRGanttSeries;
import net.sf.jasperreports.charts.JRHighLowDataset;
import net.sf.jasperreports.charts.JRHighLowPlot;
import net.sf.jasperreports.charts.JRItemLabel;
import net.sf.jasperreports.charts.JRLinePlot;
import net.sf.jasperreports.charts.JRMeterPlot;
import net.sf.jasperreports.charts.JRMultiAxisPlot;
import net.sf.jasperreports.charts.JRPie3DPlot;
import net.sf.jasperreports.charts.JRPieDataset;
import net.sf.jasperreports.charts.JRPiePlot;
import net.sf.jasperreports.charts.JRPieSeries;
import net.sf.jasperreports.charts.JRScatterPlot;
import net.sf.jasperreports.charts.JRThermometerPlot;
import net.sf.jasperreports.charts.JRTimePeriodDataset;
import net.sf.jasperreports.charts.JRTimePeriodSeries;
import net.sf.jasperreports.charts.JRTimeSeries;
import net.sf.jasperreports.charts.JRTimeSeriesDataset;
import net.sf.jasperreports.charts.JRTimeSeriesPlot;
import net.sf.jasperreports.charts.JRValueDataset;
import net.sf.jasperreports.charts.JRValueDisplay;
import net.sf.jasperreports.charts.JRXyDataset;
import net.sf.jasperreports.charts.JRXySeries;
import net.sf.jasperreports.charts.JRXyzDataset;
import net.sf.jasperreports.charts.JRXyzSeries;
import net.sf.jasperreports.charts.util.JRMeterInterval;
import net.sf.jasperreports.charts.xml.JRChartAxisFactory;
import net.sf.jasperreports.charts.xml.JRMeterPlotFactory;
import net.sf.jasperreports.charts.xml.JRThermometerPlotFactory;
import net.sf.jasperreports.crosstabs.JRCellContents;
import net.sf.jasperreports.crosstabs.JRCrosstab;
import net.sf.jasperreports.crosstabs.JRCrosstabBucket;
import net.sf.jasperreports.crosstabs.JRCrosstabCell;
import net.sf.jasperreports.crosstabs.JRCrosstabColumnGroup;
import net.sf.jasperreports.crosstabs.JRCrosstabDataset;
import net.sf.jasperreports.crosstabs.JRCrosstabMeasure;
import net.sf.jasperreports.crosstabs.JRCrosstabParameter;
import net.sf.jasperreports.crosstabs.JRCrosstabRowGroup;
import net.sf.jasperreports.crosstabs.design.JRDesignCrosstab;
import net.sf.jasperreports.crosstabs.fill.calculation.BucketDefinition;
import net.sf.jasperreports.crosstabs.xml.JRCellContentsFactory;
import net.sf.jasperreports.crosstabs.xml.JRCrosstabBucketFactory;
import net.sf.jasperreports.crosstabs.xml.JRCrosstabCellFactory;
import net.sf.jasperreports.crosstabs.xml.JRCrosstabColumnGroupFactory;
import net.sf.jasperreports.crosstabs.xml.JRCrosstabDatasetFactory;
import net.sf.jasperreports.crosstabs.xml.JRCrosstabFactory;
import net.sf.jasperreports.crosstabs.xml.JRCrosstabGroupFactory;
import net.sf.jasperreports.crosstabs.xml.JRCrosstabMeasureFactory;
import net.sf.jasperreports.crosstabs.xml.JRCrosstabParameterFactory;
import net.sf.jasperreports.crosstabs.xml.JRCrosstabRowGroupFactory;
import net.sf.jasperreports.engine.JRAnchor;
import net.sf.jasperreports.engine.JRBand;
import net.sf.jasperreports.engine.JRBreak;
import net.sf.jasperreports.engine.JRChart;
import net.sf.jasperreports.engine.JRChartDataset;
import net.sf.jasperreports.engine.JRChartPlot;
import net.sf.jasperreports.engine.JRChild;
import net.sf.jasperreports.engine.JRComponentElement;
import net.sf.jasperreports.engine.JRDataset;
import net.sf.jasperreports.engine.JRDatasetParameter;
import net.sf.jasperreports.engine.JRDatasetRun;
import net.sf.jasperreports.engine.JRElement;
import net.sf.jasperreports.engine.JRElementDataset;
import net.sf.jasperreports.engine.JRElementGroup;
import net.sf.jasperreports.engine.JREllipse;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.engine.JRFont;
import net.sf.jasperreports.engine.JRFrame;
import net.sf.jasperreports.engine.JRGenericElement;
import net.sf.jasperreports.engine.JRGenericElementParameter;
import net.sf.jasperreports.engine.JRGenericElementType;
import net.sf.jasperreports.engine.JRGraphicElement;
import net.sf.jasperreports.engine.JRGroup;
import net.sf.jasperreports.engine.JRHyperlink;
import net.sf.jasperreports.engine.JRHyperlinkHelper;
import net.sf.jasperreports.engine.JRHyperlinkParameter;
import net.sf.jasperreports.engine.JRImage;
import net.sf.jasperreports.engine.JRLine;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JRPropertiesHolder;
import net.sf.jasperreports.engine.JRPropertiesMap;
import net.sf.jasperreports.engine.JRPropertyExpression;
import net.sf.jasperreports.engine.JRQuery;
import net.sf.jasperreports.engine.JRRectangle;
import net.sf.jasperreports.engine.JRReport;
import net.sf.jasperreports.engine.JRReportFont;
import net.sf.jasperreports.engine.JRReportTemplate;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.JRScriptlet;
import net.sf.jasperreports.engine.JRSection;
import net.sf.jasperreports.engine.JRSortField;
import net.sf.jasperreports.engine.JRStaticText;
import net.sf.jasperreports.engine.JRStyle;
import net.sf.jasperreports.engine.JRSubreport;
import net.sf.jasperreports.engine.JRSubreportParameter;
import net.sf.jasperreports.engine.JRSubreportReturnValue;
import net.sf.jasperreports.engine.JRTextElement;
import net.sf.jasperreports.engine.JRTextField;
import net.sf.jasperreports.engine.JRVariable;
import net.sf.jasperreports.engine.JRChartPlot.JRSeriesColor;
import net.sf.jasperreports.engine.component.Component;
import net.sf.jasperreports.engine.component.ComponentKey;
import net.sf.jasperreports.engine.component.ComponentXmlWriter;
import net.sf.jasperreports.engine.component.ComponentsEnvironment;
import net.sf.jasperreports.engine.query.JRJdbcQueryExecuterFactory;
import net.sf.jasperreports.engine.type.FooterPositionEnum;
import net.sf.jasperreports.engine.type.PositionTypeEnum;
import net.sf.jasperreports.engine.util.JRXmlWriteHelper;
import net.sf.jasperreports.engine.util.XmlNamespace;

import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.time.Day;


/**
 * A writer that produces the JRXML representation of an in-memory report.
 * 
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @author Minor enhancements by Barry Klawans (bklawans@users.sourceforge.net)
 * @version $Id$
 */
public class JRXmlWriter extends JRXmlBaseWriter
{

	public static final XmlNamespace JASPERREPORTS_NAMESPACE = 
		new XmlNamespace(JRXmlConstants.JASPERREPORTS_NAMESPACE, null, JRXmlConstants.JASPERREPORT_XSD_SYSTEM_ID);

	/**
	 *
	 */
	private JRReport report = null;
	private String encoding = null;

	/**
	 *
	 */
	private Map fontsMap = new HashMap();

	private XmlWriterVisitor xmlWriterVisitor = new XmlWriterVisitor(this);


	/**
	 *
	 */
	protected JRXmlWriter(JRReport report, String encoding)
	{
		this.report = report;
		this.encoding = encoding;
	}


	/**
	 *
	 */
	public static String writeReport(JRReport report, String encoding)
	{
		JRXmlWriter writer = new JRXmlWriter(report, encoding);
		StringWriter buffer = new StringWriter();
		try
		{
			writer.writeReport(buffer);
		}
		catch (IOException e)
		{
			// doesn't actually happen
			throw new JRRuntimeException("Error writing report design.", e);
		}
		return buffer.toString();
	}


	/**
	 *
	 */
	public static void writeReport(
		JRReport report,
		String destFileName,
		String encoding
		) throws JRException
	{
		FileOutputStream fos = null;

		try
		{
			fos = new FileOutputStream(destFileName);
			Writer out = new OutputStreamWriter(fos, encoding);
			JRXmlWriter writer = new JRXmlWriter(report, encoding);
			writer.writeReport(out);
		}
		catch (IOException e)
		{
			throw new JRException("Error writing to file : " + destFileName, e);
		}
		finally
		{
			if (fos != null)
			{
				try
				{
					fos.close();
				}
				catch(IOException e)
				{
				}
			}
		}
	}


	/**
	 *
	 */
	public static void writeReport(
		JRReport report,
		OutputStream outputStream,
		String encoding
		) throws JRException
	{
		try
		{
			Writer out = new OutputStreamWriter(outputStream, encoding);
			JRXmlWriter writer = new JRXmlWriter(report, encoding);
			writer.writeReport(out);
		}
		catch (Exception e)
		{
			throw new JRException("Error writing to OutputStream : " + report.getName(), e);
		}
	}


	/**
	 *
	 */
	protected void writeReport(Writer out) throws IOException
	{
		useWriter(new JRXmlWriteHelper(out));

		writer.writeProlog(encoding);

		writer.startElement(XmlConstants.ELEMENT_jasperReport, getNamespace());
		writer.addEncodedAttribute(XmlConstants.ATTRIBUTE_name, report.getName());
		writer.addEncodedAttribute(XmlConstants.ATTRIBUTE_language, report.getLanguage(), JRReport.LANGUAGE_JAVA);
		writer.addAttribute(XmlConstants.ATTRIBUTE_columnCount, report.getColumnCount(), 1);
		writer.addAttribute(XmlConstants.ATTRIBUTE_printOrder, report.getPrintOrder(), JRXmlConstants.getPrintOrderMap(), JRReport.PRINT_ORDER_VERTICAL);
		writer.addAttribute(XmlConstants.ATTRIBUTE_pageWidth, report.getPageWidth());
		writer.addAttribute(XmlConstants.ATTRIBUTE_pageHeight, report.getPageHeight());
		writer.addAttribute(XmlConstants.ATTRIBUTE_orientation, report.getOrientation(), JRXmlConstants.getOrientationMap(), JRReport.ORIENTATION_PORTRAIT);
		writer.addAttribute(XmlConstants.ATTRIBUTE_whenNoDataType, report.getWhenNoDataType(), JRXmlConstants.getWhenNoDataTypeMap(), JRReport.WHEN_NO_DATA_TYPE_NO_PAGES);
		writer.addAttribute(XmlConstants.ATTRIBUTE_columnWidth, report.getColumnWidth());
		writer.addAttribute(XmlConstants.ATTRIBUTE_columnSpacing, report.getColumnSpacing(), 0);
		writer.addAttribute(XmlConstants.ATTRIBUTE_leftMargin, report.getLeftMargin());
		writer.addAttribute(XmlConstants.ATTRIBUTE_rightMargin, report.getRightMargin());
		writer.addAttribute(XmlConstants.ATTRIBUTE_topMargin, report.getTopMargin());
		writer.addAttribute(XmlConstants.ATTRIBUTE_bottomMargin, report.getBottomMargin());
		writer.addAttribute(XmlConstants.ATTRIBUTE_isTitleNewPage, report.isTitleNewPage(), false);
		writer.addAttribute(XmlConstants.ATTRIBUTE_isSummaryNewPage, report.isSummaryNewPage(), false);
		writer.addAttribute(XmlConstants.ATTRIBUTE_isSummaryWithPageHeaderAndFooter, report.isSummaryWithPageHeaderAndFooter(), false);
		writer.addAttribute(XmlConstants.ATTRIBUTE_isFloatColumnFooter, report.isFloatColumnFooter(), false);
		writer.addAttribute(XmlConstants.ATTRIBUTE_scriptletClass, report.getScriptletClass());
		writer.addAttribute(XmlConstants.ATTRIBUTE_formatFactoryClass, report.getFormatFactoryClass());
		writer.addEncodedAttribute(XmlConstants.ATTRIBUTE_resourceBundle, report.getResourceBundle());
		writer.addAttribute(XmlConstants.ATTRIBUTE_whenResourceMissingType, report.getWhenResourceMissingType(), JRXmlConstants.getWhenResourceMissingTypeMap(), JRReport.WHEN_RESOURCE_MISSING_TYPE_NULL);
		writer.addAttribute(XmlConstants.ATTRIBUTE_isIgnorePagination, report.isIgnorePagination(), false);

		writeProperties(report);

		/*   */
		String[] imports = report.getImports();
		if (imports != null && imports.length > 0)
		{
			for(int i = 0; i < imports.length; i++)
			{
				String value = imports[i];
				if (value != null)
				{
					writer.startElement(XmlConstants.ELEMENT_import);
					writer.addEncodedAttribute(XmlConstants.ATTRIBUTE_value, value);
					writer.closeElement();
				}
			}
		}

		writeTemplates();

		/*   */
		JRReportFont[] fonts = report.getFonts();
		if (fonts != null && fonts.length > 0)
		{
			for(int i = 0; i < fonts.length; i++)
			{
				fontsMap.put(fonts[i].getName(), fonts[i]);
				writeReportFont(fonts[i]);
			}
		}

		/*   */
		JRStyle[] styles = report.getStyles();
		if (styles != null && styles.length > 0)
		{
			for(int i = 0; i < styles.length; i++)
			{
				writeStyle(styles[i]);
			}
		}

		JRDataset[] datasets = report.getDatasets();
		if (datasets != null && datasets.length > 0)
		{
			for (int i = 0; i < datasets.length; ++i)
			{
				writeDataset(datasets[i]);
			}
		}

		writeDatasetContents(report.getMainDataset());

		if (report.getBackground() != null)
		{
			writer.startElement(XmlConstants.ELEMENT_background);
			writeBand(report.getBackground());
			writer.closeElement();
		}

		if (report.getTitle() != null)
		{
			writer.startElement(XmlConstants.ELEMENT_title);
			writeBand(report.getTitle());
			writer.closeElement();
		}

		if (report.getPageHeader() != null)
		{
			writer.startElement(XmlConstants.ELEMENT_pageHeader);
			writeBand(report.getPageHeader());
			writer.closeElement();
		}

		if (report.getColumnHeader() != null)
		{
			writer.startElement(XmlConstants.ELEMENT_columnHeader);
			writeBand(report.getColumnHeader());
			writer.closeElement();
		}

		JRSection detail = report.getDetailSection();
		if (detail != null)
		{
			writer.startElement(XmlConstants.ELEMENT_detail);
			writeSection(detail);
			writer.closeElement(true);
		}

		if (report.getColumnFooter() != null)
		{
			writer.startElement(XmlConstants.ELEMENT_columnFooter);
			writeBand(report.getColumnFooter());
			writer.closeElement();
		}

		if (report.getPageFooter() != null)
		{
			writer.startElement(XmlConstants.ELEMENT_pageFooter);
			writeBand(report.getPageFooter());
			writer.closeElement();
		}

		if (report.getLastPageFooter() != null)
		{
			writer.startElement(XmlConstants.ELEMENT_lastPageFooter);
			writeBand(report.getLastPageFooter());
			writer.closeElement();
		}

		if (report.getSummary() != null)
		{
			writer.startElement(XmlConstants.ELEMENT_summary);
			writeBand(report.getSummary());
			writer.closeElement();
		}

		if (report.getNoData() != null)
		{
			writer.startElement(XmlConstants.ELEMENT_noData);
			writeBand(report.getNoData());
			writer.closeElement();
		}

		writer.closeElement();

		out.flush();
	}


	private void writeProperties(JRPropertiesHolder propertiesHolder) throws IOException
	{
		if (propertiesHolder.hasProperties())
		{
			JRPropertiesMap propertiesMap = propertiesHolder.getPropertiesMap();
			String[] propertyNames = propertiesMap.getPropertyNames();
			if (propertyNames != null && propertyNames.length > 0)
			{
				for(int i = 0; i < propertyNames.length; i++)
				{
					writer.startElement(XmlConstants.ELEMENT_property);
					writer.addEncodedAttribute(XmlConstants.ATTRIBUTE_name, propertyNames[i]);
					String value = propertiesMap.getProperty(propertyNames[i]);
					if (value != null)
					{
						writer.addEncodedAttribute(XmlConstants.ATTRIBUTE_value, value);
					}
					writer.closeElement();
				}
			}
		}
	}


	protected void writeTemplates() throws IOException
	{
		JRReportTemplate[] templates = report.getTemplates();
		if (templates != null)
		{
			for (int i = 0; i < templates.length; i++)
			{
				JRReportTemplate template = templates[i];
				writeTemplate(template);
			}
		}
	}


	protected void writeTemplate(JRReportTemplate template) throws IOException
	{
		writer.writeExpression(XmlConstants.ELEMENT_template, template.getSourceExpression(),
				true, String.class.getName());
	}


	/**
	 *
	 */
	private void writeReportFont(JRReportFont font) throws IOException
	{
		writer.startElement(XmlConstants.ELEMENT_reportFont);
		writer.addEncodedAttribute(XmlConstants.ATTRIBUTE_name, font.getName());
		writer.addAttribute(XmlConstants.ATTRIBUTE_isDefault, font.isDefault());
		writer.addEncodedAttribute(XmlConstants.ATTRIBUTE_fontName, font.getOwnFontName());
		writer.addAttribute(XmlConstants.ATTRIBUTE_size, font.getOwnFontSize());
		writer.addAttribute(XmlConstants.ATTRIBUTE_isBold, font.isOwnBold());
		writer.addAttribute(XmlConstants.ATTRIBUTE_isItalic, font.isOwnItalic());
		writer.addAttribute(XmlConstants.ATTRIBUTE_isUnderline, font.isOwnUnderline());
		writer.addAttribute(XmlConstants.ATTRIBUTE_isStrikeThrough, font.isOwnStrikeThrough());
		writer.addEncodedAttribute(XmlConstants.ATTRIBUTE_pdfFontName, font.getOwnPdfFontName());
		writer.addEncodedAttribute(XmlConstants.ATTRIBUTE_pdfEncoding, font.getOwnPdfEncoding());
		writer.addAttribute(XmlConstants.ATTRIBUTE_isPdfEmbedded, font.isOwnPdfEmbedded());
		writer.closeElement();
	}


	/**
	 *
	 */
	private void writeScriptlet(JRScriptlet scriptlet) throws IOException
	{
		writer.startElement(XmlConstants.ELEMENT_scriptlet);
		writer.addEncodedAttribute(XmlConstants.ATTRIBUTE_name, scriptlet.getName());
		writer.addAttribute(XmlConstants.ATTRIBUTE_class, scriptlet.getValueClassName());

		writeProperties(scriptlet);

		writer.writeCDATAElement(XmlConstants.ELEMENT_scriptletDescription, scriptlet.getDescription());

		writer.closeElement();
	}


	/**
	 *
	 */
	private void writeParameter(JRParameter parameter) throws IOException
	{
		writer.startElement(XmlConstants.ELEMENT_parameter);
		writer.addEncodedAttribute(XmlConstants.ATTRIBUTE_name, parameter.getName());
		writer.addAttribute(XmlConstants.ATTRIBUTE_class, parameter.getValueClassName());
		writer.addAttribute(XmlConstants.ATTRIBUTE_nestedType, parameter.getNestedTypeName());
		writer.addAttribute(XmlConstants.ATTRIBUTE_isForPrompting, parameter.isForPrompting(), true);

		writeProperties(parameter);

		writer.writeCDATAElement(XmlConstants.ELEMENT_parameterDescription, parameter.getDescription());
		writer.writeExpression(XmlConstants.ELEMENT_defaultValueExpression, parameter.getDefaultValueExpression(), false);

		writer.closeElement();
	}


	/**
	 *
	 */
	private void writeQuery(JRQuery query) throws IOException
	{
		writer.startElement(XmlConstants.ELEMENT_queryString);
		writer.addEncodedAttribute(XmlConstants.ATTRIBUTE_language, query.getLanguage(), JRJdbcQueryExecuterFactory.QUERY_LANGUAGE_SQL);
		writer.writeCDATA(query.getText());
		writer.closeElement();
	}


	/**
	 *
	 */
	private void writeField(JRField field) throws IOException
	{
		writer.startElement(XmlConstants.ELEMENT_field);
		writer.addEncodedAttribute(XmlConstants.ATTRIBUTE_name, field.getName());
		writer.addAttribute(XmlConstants.ATTRIBUTE_class, field.getValueClassName());

		writeProperties(field);

		writer.writeCDATAElement(XmlConstants.ELEMENT_fieldDescription, field.getDescription());

		writer.closeElement();
	}


	/**
	 *
	 */
	private void writeSortField(JRSortField sortField) throws IOException
	{
		writer.startElement(XmlConstants.ELEMENT_sortField);
		writer.addEncodedAttribute(XmlConstants.ATTRIBUTE_name, sortField.getName());
		writer.addAttribute(XmlConstants.ATTRIBUTE_order, sortField.getOrder(), JRXmlConstants.getSortOrderMap(), JRSortField.SORT_ORDER_ASCENDING);
		writer.closeElement();
	}


	/**
	 *
	 */
	private void writeVariable(JRVariable variable) throws IOException
	{
		writer.startElement(XmlConstants.ELEMENT_variable);
		writer.addEncodedAttribute(XmlConstants.ATTRIBUTE_name, variable.getName());
		writer.addAttribute(XmlConstants.ATTRIBUTE_class, variable.getValueClassName());
		writer.addAttribute(XmlConstants.ATTRIBUTE_resetType, variable.getResetType(), JRXmlConstants.getResetTypeMap(), JRVariable.RESET_TYPE_REPORT);
		if (variable.getResetGroup() != null)
		{
			writer.addEncodedAttribute(XmlConstants.ATTRIBUTE_resetGroup, variable.getResetGroup().getName());
		}
		writer.addAttribute(XmlConstants.ATTRIBUTE_incrementType, variable.getIncrementType(), JRXmlConstants.getResetTypeMap(), JRVariable.RESET_TYPE_NONE);
		if (variable.getIncrementGroup() != null)
		{
			writer.addEncodedAttribute(XmlConstants.ATTRIBUTE_incrementGroup, variable.getIncrementGroup().getName());
		}
		writer.addAttribute(XmlConstants.ATTRIBUTE_calculation, variable.getCalculation(), JRXmlConstants.getCalculationMap(), JRVariable.CALCULATION_NOTHING);
		writer.addAttribute(XmlConstants.ATTRIBUTE_incrementerFactoryClass, variable.getIncrementerFactoryClassName());

		writer.writeExpression(XmlConstants.ELEMENT_variableExpression, variable.getExpression(), false);
		writer.writeExpression(XmlConstants.ELEMENT_initialValueExpression, variable.getInitialValueExpression(), false);

		writer.closeElement();
	}


	/**
	 *
	 */
	private void writeGroup(JRGroup group) throws IOException
	{
		writer.startElement(XmlConstants.ELEMENT_group);
		writer.addEncodedAttribute(XmlConstants.ATTRIBUTE_name, group.getName());
		writer.addAttribute(XmlConstants.ATTRIBUTE_isStartNewColumn, group.isStartNewColumn(), false);
		writer.addAttribute(XmlConstants.ATTRIBUTE_isStartNewPage, group.isStartNewPage(), false);
		writer.addAttribute(XmlConstants.ATTRIBUTE_isResetPageNumber, group.isResetPageNumber(), false);
		writer.addAttribute(XmlConstants.ATTRIBUTE_isReprintHeaderOnEachPage, group.isReprintHeaderOnEachPage(), false);
		writer.addAttributePositive(XmlConstants.ATTRIBUTE_minHeightToStartNewPage, group.getMinHeightToStartNewPage());
		writer.addAttribute(XmlConstants.ATTRIBUTE_footerPosition, group.getFooterPositionValue(), FooterPositionEnum.NORMAL);
		writer.addAttribute(XmlConstants.ATTRIBUTE_keepTogether, group.isKeepTogether(), false);

		writer.writeExpression(XmlConstants.ELEMENT_groupExpression, group.getExpression(), false);

		JRSection groupHeader = group.getGroupHeaderSection();
		if (groupHeader != null)
		{
			writer.startElement(XmlConstants.ELEMENT_groupHeader);
			writeSection(groupHeader);
			writer.closeElement(true);
		}

		JRSection groupFooter = group.getGroupFooterSection();
		if (groupFooter != null)
		{
			writer.startElement(XmlConstants.ELEMENT_groupFooter);
			writeSection(groupFooter);
			writer.closeElement(true);
		}

		writer.closeElement();
	}


	/**
	 *
	 */
	protected void writeSection(JRSection section) throws IOException
	{
		if (section != null)
		{
			JRBand[] bands = section.getBands();
			if (bands != null && bands.length > 0)
			{
				for(int i = 0; i < bands.length; i++)
				{
					writeBand(bands[i]);
				}
			}
		}
	}


	/**
	 *
	 */
	private void writeBand(JRBand band) throws IOException
	{
		writer.startElement(XmlConstants.ELEMENT_band);
		writer.addAttributePositive(XmlConstants.ATTRIBUTE_height, band.getHeight());
		writer.addAttribute(XmlConstants.ATTRIBUTE_splitType, band.getSplitType(), JRXmlConstants.getSplitTypeMap());

		writer.writeExpression(XmlConstants.ELEMENT_printWhenExpression, band.getPrintWhenExpression(), false);

		/*   */
		writeChildElements(band);

		writer.closeElement();
	}

	
	/**
	 * Writes the contents (child elements) of an element container.
	 * 
	 * @param elementContainer the element container
	 */
	public void writeChildElements(JRElementGroup elementContainer)
	{
		List children = elementContainer.getChildren();
		if (children != null && children.size() > 0)
		{
			for(int i = 0; i < children.size(); i++)
			{
				((JRChild) children.get(i)).visit(xmlWriterVisitor);
			}
		}
	}

	/**
	 *
	 */
	public void writeElementGroup(JRElementGroup elementGroup) throws IOException
	{
		writer.startElement(XmlConstants.ELEMENT_elementGroup, getNamespace());

		/*   */
		writeChildElements(elementGroup);

		writer.closeElement();
	}


	/**
	 *
	 */
	public void writeBreak(JRBreak breakElement) throws IOException
	{
		writer.startElement(XmlConstants.ELEMENT_break, getNamespace());
		writer.addAttribute(XmlConstants.ATTRIBUTE_type, breakElement.getType(), JRXmlConstants.getBreakTypeMap(), JRBreak.TYPE_PAGE);

		writeReportElement(breakElement);

		writer.closeElement();
	}


	/**
	 *
	 */
	public void writeLine(JRLine line) throws IOException
	{
		writer.startElement(XmlConstants.ELEMENT_line, getNamespace());
		writer.addAttribute(XmlConstants.ATTRIBUTE_direction, line.getDirection(), JRXmlConstants.getDirectionMap(), JRLine.DIRECTION_TOP_DOWN);

		writeReportElement(line);
		writeGraphicElement(line);

		writer.closeElement();
	}


	/**
	 *
	 */
	private void writeReportElement(JRElement element) throws IOException
	{
		writer.startElement(XmlConstants.ELEMENT_reportElement);
		writer.addEncodedAttribute(XmlConstants.ATTRIBUTE_key, element.getKey());
		writeStyleReferenceAttr(element);
		writer.addAttribute(XmlConstants.ATTRIBUTE_positionType, element.getPositionTypeValue(), PositionTypeEnum.FIX_RELATIVE_TO_TOP);
		writer.addAttribute(XmlConstants.ATTRIBUTE_stretchType, element.getStretchType(), JRXmlConstants.getStretchTypeMap(), JRElement.STRETCH_TYPE_NO_STRETCH);
		writer.addAttribute(XmlConstants.ATTRIBUTE_isPrintRepeatedValues, element.isPrintRepeatedValues(), true);
		writer.addAttribute(XmlConstants.ATTRIBUTE_mode, element.getOwnModeValue());

		writer.addAttribute(XmlConstants.ATTRIBUTE_x, element.getX());
		writer.addAttribute(XmlConstants.ATTRIBUTE_y, element.getY());
		writer.addAttribute(XmlConstants.ATTRIBUTE_width, element.getWidth());
		writer.addAttribute(XmlConstants.ATTRIBUTE_height, element.getHeight());
		writer.addAttribute(XmlConstants.ATTRIBUTE_isRemoveLineWhenBlank, element.isRemoveLineWhenBlank(), false);
		writer.addAttribute(XmlConstants.ATTRIBUTE_isPrintInFirstWholeBand, element.isPrintInFirstWholeBand(), false);
		writer.addAttribute(XmlConstants.ATTRIBUTE_isPrintWhenDetailOverflows, element.isPrintWhenDetailOverflows(), false);

		if (element.getPrintWhenGroupChanges() != null)
		{
			writer.addEncodedAttribute(XmlConstants.ATTRIBUTE_printWhenGroupChanges, element.getPrintWhenGroupChanges().getName());
		}

		writer.addAttribute(XmlConstants.ATTRIBUTE_forecolor, element.getOwnForecolor());
		writer.addAttribute(XmlConstants.ATTRIBUTE_backcolor, element.getOwnBackcolor());

		writeProperties(element);
		writePropertyExpressions(element.getPropertyExpressions());
		writer.writeExpression(XmlConstants.ELEMENT_printWhenExpression, element.getPrintWhenExpression(), false);
		writer.closeElement();
	}


	protected void writePropertyExpressions(
			JRPropertyExpression[] propertyExpressions) throws IOException
	{
		if (propertyExpressions != null)
		{
			for (int i = 0; i < propertyExpressions.length; i++)
			{
				writePropertyExpression(propertyExpressions[i]);
			}
		}
	}


	protected void writePropertyExpression(JRPropertyExpression propertyExpression) throws IOException
	{
		String expressionText = propertyExpression.getValueExpression().getText();
		writer.writeCDATAElement(XmlConstants.ELEMENT_propertyExpression, expressionText, 
				XmlConstants.ATTRIBUTE_name, propertyExpression.getName());
	}


	/**
	 *
	 */
	private void writeGraphicElement(JRGraphicElement element) throws IOException
	{
		writer.startElement(XmlConstants.ELEMENT_graphicElement);
		writer.addAttribute(XmlConstants.ATTRIBUTE_fill, element.getOwnFill(), JRXmlConstants.getFillMap());
		writePen(element.getLinePen());
		writer.closeElement(true);
	}


	/**
	 *
	 */
	public void writeRectangle(JRRectangle rectangle) throws IOException
	{
		writer.startElement(XmlConstants.ELEMENT_rectangle, getNamespace());
		writer.addAttribute(XmlConstants.ATTRIBUTE_radius, rectangle.getOwnRadius());

		writeReportElement(rectangle);
		writeGraphicElement(rectangle);

		writer.closeElement();
	}


	/**
	 *
	 */
	public void writeEllipse(JREllipse ellipse) throws IOException
	{
		writer.startElement(XmlConstants.ELEMENT_ellipse, getNamespace());

		writeReportElement(ellipse);
		writeGraphicElement(ellipse);

		writer.closeElement();
	}


	/**
	 *
	 */
	public void writeImage(JRImage image) throws IOException
	{
		writer.startElement(XmlConstants.ELEMENT_image, getNamespace());
		writer.addAttribute(XmlConstants.ATTRIBUTE_scaleImage, image.getOwnScaleImage(), JRXmlConstants.getScaleImageMap());
		writer.addAttribute(XmlConstants.ATTRIBUTE_hAlign, image.getOwnHorizontalAlignmentValue());
		writer.addAttribute(XmlConstants.ATTRIBUTE_vAlign, image.getOwnVerticalAlignmentValue());
		writer.addAttribute(XmlConstants.ATTRIBUTE_isUsingCache, image.isOwnUsingCache());
		writer.addAttribute(XmlConstants.ATTRIBUTE_isLazy, image.isLazy(), false);
		writer.addAttribute(XmlConstants.ATTRIBUTE_onErrorType, image.getOnErrorType(), JRXmlConstants.getOnErrorTypeMap(), JRImage.ON_ERROR_TYPE_ERROR);
		writer.addAttribute(XmlConstants.ATTRIBUTE_evaluationTime, image.getEvaluationTime(), JRXmlConstants.getEvaluationTimeMap(), JRExpression.EVALUATION_TIME_NOW);

		if (image.getEvaluationGroup() != null)
		{
			writer.addEncodedAttribute(XmlConstants.ATTRIBUTE_evaluationGroup, image.getEvaluationGroup().getName());
		}

		writer.addEncodedAttribute(XmlConstants.ATTRIBUTE_hyperlinkType, image.getLinkType(), JRHyperlinkHelper.HYPERLINK_TYPE_NONE);
		writer.addEncodedAttribute(XmlConstants.ATTRIBUTE_hyperlinkTarget, image.getLinkTarget(), JRHyperlinkHelper.HYPERLINK_TARGET_SELF);
		writer.addAttribute(XmlConstants.ATTRIBUTE_bookmarkLevel, image.getBookmarkLevel(), JRAnchor.NO_BOOKMARK);

		writeReportElement(image);
		writeBox(image.getLineBox());
		writeGraphicElement(image);

		//FIXME class is mandatory in verifier

		writer.writeExpression(XmlConstants.ELEMENT_imageExpression, image.getExpression(), true);
		writer.writeExpression(XmlConstants.ELEMENT_anchorNameExpression, image.getAnchorNameExpression(), false);
		writer.writeExpression(XmlConstants.ELEMENT_hyperlinkReferenceExpression, image.getHyperlinkReferenceExpression(), false);
		writer.writeExpression(XmlConstants.ELEMENT_hyperlinkAnchorExpression, image.getHyperlinkAnchorExpression(), false);
		writer.writeExpression(XmlConstants.ELEMENT_hyperlinkPageExpression, image.getHyperlinkPageExpression(), false);
		writer.writeExpression(XmlConstants.ELEMENT_hyperlinkTooltipExpression, image.getHyperlinkTooltipExpression(), false);
		writeHyperlinkParameters(image.getHyperlinkParameters());

		writer.closeElement();
	}


	/**
	 *
	 */
	public void writeStaticText(JRStaticText staticText) throws IOException
	{
		writer.startElement(XmlConstants.ELEMENT_staticText, getNamespace());

		writeReportElement(staticText);
		writeBox(staticText.getLineBox());
		writeTextElement(staticText);

		writer.writeCDATAElement(XmlConstants.ELEMENT_text, staticText.getText());

		writer.closeElement();
	}


	/**
	 *
	 */
	private void writeTextElement(JRTextElement textElement) throws IOException
	{
		writer.startElement(XmlConstants.ELEMENT_textElement);
		writer.addAttribute(XmlConstants.ATTRIBUTE_textAlignment, textElement.getOwnHorizontalAlignmentValue());
		writer.addAttribute(XmlConstants.ATTRIBUTE_verticalAlignment, textElement.getOwnVerticalAlignmentValue());
		writer.addAttribute(XmlConstants.ATTRIBUTE_rotation, textElement.getOwnRotation(), JRXmlConstants.getRotationMap());
		writer.addAttribute(XmlConstants.ATTRIBUTE_lineSpacing, textElement.getOwnLineSpacing(), JRXmlConstants.getLineSpacingMap());
		writer.addAttribute(XmlConstants.ATTRIBUTE_markup, textElement.getOwnMarkup());

		writeFont(textElement);

		writer.closeElement();
	}


	/**
	 *
	 */
	private void writeFont(JRFont font) throws IOException
	{
		if (font != null)
		{
			writer.startElement(XmlConstants.ELEMENT_font);
			if (font.getReportFont() != null)
			{
				JRFont baseFont =
					(JRFont)fontsMap.get(
						font.getReportFont().getName()
						);
				if(baseFont != null)
				{
					writer.addEncodedAttribute(XmlConstants.ATTRIBUTE_reportFont, font.getReportFont().getName());
				}
				else
				{
					throw
						new JRRuntimeException(
							"Referenced report font not found : "
							+ font.getReportFont().getName()
							);
				}
			}

			writer.addEncodedAttribute(XmlConstants.ATTRIBUTE_fontName, font.getOwnFontName());
			writer.addAttribute(XmlConstants.ATTRIBUTE_size, font.getOwnFontSize());
			writer.addAttribute(XmlConstants.ATTRIBUTE_isBold, font.isOwnBold());
			writer.addAttribute(XmlConstants.ATTRIBUTE_isItalic, font.isOwnItalic());
			writer.addAttribute(XmlConstants.ATTRIBUTE_isUnderline, font.isOwnUnderline());
			writer.addAttribute(XmlConstants.ATTRIBUTE_isStrikeThrough, font.isOwnStrikeThrough());
			writer.addEncodedAttribute(XmlConstants.ATTRIBUTE_pdfFontName, font.getOwnPdfFontName());
			writer.addEncodedAttribute(XmlConstants.ATTRIBUTE_pdfEncoding, font.getOwnPdfEncoding());
			writer.addAttribute(XmlConstants.ATTRIBUTE_isPdfEmbedded, font.isOwnPdfEmbedded());
			writer.closeElement(true);
		}
	}


	/**
	 *
	 */
	public void writeTextField(JRTextField textField) throws IOException
	{
		writer.startElement(XmlConstants.ELEMENT_textField, getNamespace());
		writer.addAttribute(XmlConstants.ATTRIBUTE_isStretchWithOverflow, textField.isStretchWithOverflow(), false);
		writer.addAttribute(XmlConstants.ATTRIBUTE_evaluationTime, textField.getEvaluationTime(), JRXmlConstants.getEvaluationTimeMap(), JRExpression.EVALUATION_TIME_NOW);

		if (textField.getEvaluationGroup() != null)
		{
			writer.addEncodedAttribute(XmlConstants.ATTRIBUTE_evaluationGroup, textField.getEvaluationGroup().getName());
		}

		writer.addEncodedAttribute(XmlConstants.ATTRIBUTE_pattern, textField.getOwnPattern());
		writer.addAttribute(XmlConstants.ATTRIBUTE_isBlankWhenNull, textField.isOwnBlankWhenNull());

		writer.addEncodedAttribute(XmlConstants.ATTRIBUTE_hyperlinkType, textField.getLinkType(), JRHyperlinkHelper.HYPERLINK_TYPE_NONE);
		writer.addEncodedAttribute(XmlConstants.ATTRIBUTE_hyperlinkTarget, textField.getLinkTarget(), JRHyperlinkHelper.HYPERLINK_TARGET_SELF);
		writer.addAttribute(XmlConstants.ATTRIBUTE_bookmarkLevel, textField.getBookmarkLevel(), JRAnchor.NO_BOOKMARK);

		writeReportElement(textField);
		writeBox(textField.getLineBox());
		writeTextElement(textField);

		writer.writeExpression(XmlConstants.ELEMENT_textFieldExpression, textField.getExpression(), true);

		writer.writeExpression(XmlConstants.ELEMENT_anchorNameExpression, textField.getAnchorNameExpression(), false);
		writer.writeExpression(XmlConstants.ELEMENT_hyperlinkReferenceExpression, textField.getHyperlinkReferenceExpression(), false);
		writer.writeExpression(XmlConstants.ELEMENT_hyperlinkAnchorExpression, textField.getHyperlinkAnchorExpression(), false);
		writer.writeExpression(XmlConstants.ELEMENT_hyperlinkPageExpression, textField.getHyperlinkPageExpression(), false);
		writer.writeExpression(XmlConstants.ELEMENT_hyperlinkTooltipExpression, textField.getHyperlinkTooltipExpression(), false);
		writeHyperlinkParameters(textField.getHyperlinkParameters());

		writer.closeElement();
	}


	/**
	 *
	 */
	public void writeSubreport(JRSubreport subreport) throws IOException
	{
		writer.startElement(XmlConstants.ELEMENT_subreport, getNamespace());
		writer.addAttribute(XmlConstants.ATTRIBUTE_isUsingCache, subreport.isOwnUsingCache());
		writer.addAttribute(XmlConstants.ATTRIBUTE_runToBottom, subreport.isRunToBottom());

		writeReportElement(subreport);

		writer.writeExpression(XmlConstants.ELEMENT_parametersMapExpression, subreport.getParametersMapExpression(), false);

		/*   */
		JRSubreportParameter[] parameters = subreport.getParameters();
		if (parameters != null && parameters.length > 0)
		{
			for(int i = 0; i < parameters.length; i++)
			{
				writeSubreportParameter(parameters[i]);
			}
		}

		writer.writeExpression(XmlConstants.ELEMENT_connectionExpression, subreport.getConnectionExpression(), false);
		writer.writeExpression(XmlConstants.ELEMENT_dataSourceExpression, subreport.getDataSourceExpression(), false);

		JRSubreportReturnValue[] returnValues = subreport.getReturnValues();
		if (returnValues != null && returnValues.length > 0)
		{
			for(int i = 0; i < returnValues.length; i++)
			{
				writeSubreportReturnValue(returnValues[i]);
			}
		}

		writer.writeExpression(XmlConstants.ELEMENT_subreportExpression, subreport.getExpression(), true);

		writer.closeElement();
	}


	/**
	 *
	 */
	private void writeSubreportParameter(JRSubreportParameter subreportParameter) throws IOException
	{
		writer.startElement(XmlConstants.ELEMENT_subreportParameter);
		writer.addEncodedAttribute(XmlConstants.ATTRIBUTE_name, subreportParameter.getName());

		writer.writeExpression(XmlConstants.ELEMENT_subreportParameterExpression, subreportParameter.getExpression(), false);

		writer.closeElement();
	}


	private void writeDatasetParameter(JRDatasetParameter datasetParameter) throws IOException
	{
		writer.startElement(XmlConstants.ELEMENT_datasetParameter);
		writer.addEncodedAttribute(XmlConstants.ATTRIBUTE_name, datasetParameter.getName());

		writer.writeExpression(XmlConstants.ELEMENT_datasetParameterExpression, datasetParameter.getExpression(), false);

		writer.closeElement();
	}


	/**
	 *
	 */
	private void writeChart(JRChart chart) throws IOException
	{
		writer.startElement(XmlConstants.ELEMENT_chart);
		writer.addAttribute(XmlConstants.ATTRIBUTE_isShowLegend, chart.getShowLegend());
		writer.addAttribute(XmlConstants.ATTRIBUTE_evaluationTime, chart.getEvaluationTime(), JRXmlConstants.getEvaluationTimeMap(), JRExpression.EVALUATION_TIME_NOW);

		if (chart.getEvaluationTime() == JRExpression.EVALUATION_TIME_GROUP)
		{
			writer.addEncodedAttribute(XmlConstants.ATTRIBUTE_evaluationGroup, chart.getEvaluationGroup().getName());
		}

		writer.addEncodedAttribute(XmlConstants.ATTRIBUTE_hyperlinkType, chart.getLinkType(), JRHyperlinkHelper.HYPERLINK_TYPE_NONE);
		writer.addEncodedAttribute(XmlConstants.ATTRIBUTE_hyperlinkTarget, chart.getLinkTarget(), JRHyperlinkHelper.HYPERLINK_TARGET_SELF);
		writer.addAttribute(XmlConstants.ATTRIBUTE_bookmarkLevel, chart.getBookmarkLevel(), JRAnchor.NO_BOOKMARK);
		writer.addAttribute(XmlConstants.ATTRIBUTE_customizerClass, chart.getCustomizerClass());
		writer.addEncodedAttribute(XmlConstants.ATTRIBUTE_renderType, chart.getRenderType());
		writer.addEncodedAttribute(XmlConstants.ATTRIBUTE_theme, chart.getTheme());

		writeReportElement(chart);
		writeBox(chart.getLineBox());

		// write title
		writer.startElement(XmlConstants.ELEMENT_chartTitle);
		writer.addAttribute(XmlConstants.ATTRIBUTE_position, chart.getTitlePositionByte(), JRXmlConstants.getChartEdgeMap());
		writer.addAttribute(XmlConstants.ATTRIBUTE_color, chart.getOwnTitleColor());
		writeFont(chart.getTitleFont());
		if (chart.getTitleExpression() != null)
		{
			writer.writeExpression(XmlConstants.ELEMENT_titleExpression, chart.getTitleExpression(), false);
		}
		writer.closeElement();

		// write subtitle
		writer.startElement(XmlConstants.ELEMENT_chartSubtitle);
		writer.addAttribute(XmlConstants.ATTRIBUTE_color, chart.getOwnSubtitleColor());
		writeFont(chart.getSubtitleFont());
		if (chart.getSubtitleExpression() != null)
		{
			writer.writeExpression(XmlConstants.ELEMENT_subtitleExpression, chart.getSubtitleExpression(), false);
		}
		writer.closeElement();

		// write chartLegend
		writer.startElement(XmlConstants.ELEMENT_chartLegend);
		if (chart.getOwnLegendColor() != null)
			writer.addAttribute(XmlConstants.ATTRIBUTE_textColor, chart.getOwnLegendColor());
		if (chart.getOwnLegendBackgroundColor() != null)
			writer.addAttribute(XmlConstants.ATTRIBUTE_backgroundColor, chart.getOwnLegendBackgroundColor());
		writer.addAttribute(XmlConstants.ATTRIBUTE_position, chart.getLegendPositionByte(), JRXmlConstants.getChartEdgeMap());
		writeFont(chart.getLegendFont());
		writer.closeElement();

		writer.writeExpression(XmlConstants.ELEMENT_anchorNameExpression, chart.getAnchorNameExpression(), false);
		writer.writeExpression(XmlConstants.ELEMENT_hyperlinkReferenceExpression, chart.getHyperlinkReferenceExpression(), false);
		writer.writeExpression(XmlConstants.ELEMENT_hyperlinkAnchorExpression, chart.getHyperlinkAnchorExpression(), false);
		writer.writeExpression(XmlConstants.ELEMENT_hyperlinkPageExpression, chart.getHyperlinkPageExpression(), false);
		writer.writeExpression(XmlConstants.ELEMENT_hyperlinkTooltipExpression, chart.getHyperlinkTooltipExpression(), false);
		writeHyperlinkParameters(chart.getHyperlinkParameters());

		writer.closeElement();
	}

	/**
	 * Writes the JRXML representation of an {@link JRElementDataset element dataset}.
	 * 
	 * <p>
	 * The method produces a <code>&lt;dataset&gt;</code> XML element.
	 * 
	 * @param dataset the element dataset
	 * @throws IOException any I/O exception that occurred while writing the
	 * XML output
	 */
	public void writeElementDataset(JRElementDataset dataset) throws IOException
	{
		writeElementDataset(dataset, true);
	}

	/**
	 * Writes the JRXML representation of an {@link JRElementDataset element dataset}.
	 * 
	 * <p>
	 * The method produces a <code>&lt;dataset&gt;</code> XML element.
	 * 
	 * @param dataset the element dataset
	 * @param skipIfEmpty if set, no output will be produced if the element dataset
	 * only has default attribute values
	 * @throws IOException any I/O exception that occurred while writing the
	 * XML output
	 */
	public void writeElementDataset(JRElementDataset dataset, boolean skipIfEmpty) throws IOException
	{
		writer.startElement(XmlConstants.ELEMENT_dataset, getNamespace());
		writer.addAttribute(XmlConstants.ATTRIBUTE_resetType, dataset.getResetType(), JRXmlConstants.getResetTypeMap(), JRVariable.RESET_TYPE_REPORT);

		if (dataset.getResetType() == JRVariable.RESET_TYPE_GROUP)
		{
			writer.addEncodedAttribute(XmlConstants.ATTRIBUTE_resetGroup, dataset.getResetGroup().getName());
		}
		writer.addAttribute(XmlConstants.ATTRIBUTE_incrementType, dataset.getIncrementType(), JRXmlConstants.getResetTypeMap(), JRVariable.RESET_TYPE_NONE);

		if (dataset.getIncrementType() == JRVariable.RESET_TYPE_GROUP)
		{
			writer.addEncodedAttribute(XmlConstants.ATTRIBUTE_incrementGroup, dataset.getIncrementGroup().getName());
		}

		writer.writeExpression(XmlConstants.ELEMENT_incrementWhenExpression, dataset.getIncrementWhenExpression(), false);

		JRDatasetRun datasetRun = dataset.getDatasetRun();
		if (datasetRun != null)
		{
			writeDatasetRun(datasetRun);
		}

		writer.closeElement(skipIfEmpty);
	}


	/**
	 *
	 */
	private void writeCategoryDataSet(JRCategoryDataset dataset) throws IOException
	{
		writer.startElement(XmlConstants.ELEMENT_categoryDataset);

		writeElementDataset(dataset);

		/*   */
		JRCategorySeries[] categorySeries = dataset.getSeries();
		if (categorySeries != null && categorySeries.length > 0)
		{
			for(int i = 0; i < categorySeries.length; i++)
			{
				writeCategorySeries(categorySeries[i]);
			}
		}

		writer.closeElement();
	}


	private void writeTimeSeriesDataset(JRTimeSeriesDataset dataset) throws IOException
	{
		writer.startElement(XmlConstants.ELEMENT_timeSeriesDataset);
		if (dataset.getTimePeriod() != null && !Day.class.getName().equals(dataset.getTimePeriod().getName()))
		{
			writer.addAttribute(XmlConstants.ATTRIBUTE_timePeriod, JRXmlConstants.getTimePeriodName(dataset.getTimePeriod()));
		}

		writeElementDataset( dataset );

		JRTimeSeries[] timeSeries = dataset.getSeries();
		if( timeSeries != null && timeSeries.length > 0 )
		{
			for( int i = 0; i < timeSeries.length; i++ )
		{
				writeTimeSeries( timeSeries[i] );
			}
		}

		writer.closeElement();
	}


	/**
	 * 
	 */
	private void writeGanttDataset(JRGanttDataset dataset) throws IOException
	{
		writer.startElement(XmlConstants.ELEMENT_ganttDataset);
		
		writeElementDataset(dataset);
		
		/*   */
		JRGanttSeries[] ganttSeries = dataset.getSeries();
		if (ganttSeries != null && ganttSeries.length > 0)
		{
			for(int i = 0; i < ganttSeries.length; i++)
			{
				writeGanttSeries(ganttSeries[i]);
			}
		}
		
		writer.closeElement();
	}


	private void writeTimePeriodDataset(JRTimePeriodDataset dataset) throws IOException
	{
		writer.startElement(XmlConstants.ELEMENT_timePeriodDataset);
		writeElementDataset(dataset);

		JRTimePeriodSeries[] timePeriodSeries = dataset.getSeries();
		if( timePeriodSeries != null && timePeriodSeries.length > 0 )
		{
			for( int i = 0; i < timePeriodSeries.length; i++ )
			{
				writeTimePeriodSeries(timePeriodSeries[i]);
			}
		}
		writer.closeElement();
	}


	/**
	 *
	 */
	private void writePieSeries(JRPieSeries pieSeries) throws IOException
	{
		writer.startElement(XmlConstants.ELEMENT_pieSeries);

		writer.writeExpression(XmlConstants.ELEMENT_keyExpression, pieSeries.getKeyExpression(), false);
		writer.writeExpression(XmlConstants.ELEMENT_valueExpression, pieSeries.getValueExpression(), false);
		writer.writeExpression(XmlConstants.ELEMENT_labelExpression, pieSeries.getLabelExpression(), false);
		writeHyperlink(XmlConstants.ELEMENT_sectionHyperlink, pieSeries.getSectionHyperlink());

		writer.closeElement();
	}

	/**
	 *
	 */
	private void writeCategorySeries(JRCategorySeries categorySeries) throws IOException
	{
		writer.startElement(XmlConstants.ELEMENT_categorySeries);

		writer.writeExpression(XmlConstants.ELEMENT_seriesExpression, categorySeries.getSeriesExpression(), false);
		writer.writeExpression(XmlConstants.ELEMENT_categoryExpression, categorySeries.getCategoryExpression(), false);
		writer.writeExpression(XmlConstants.ELEMENT_valueExpression, categorySeries.getValueExpression(), false);
		writer.writeExpression(XmlConstants.ELEMENT_labelExpression, categorySeries.getLabelExpression(), false);
		writeHyperlink(XmlConstants.ELEMENT_itemHyperlink, categorySeries.getItemHyperlink());

		writer.closeElement();
	}

	/**
	 *
	 */
	private void writeXyzDataset(JRXyzDataset dataset) throws IOException
	{
		writer.startElement(XmlConstants.ELEMENT_xyzDataset);
		writeElementDataset(dataset);

		JRXyzSeries[] series = dataset.getSeries();
		if( series != null && series.length > 0 )
		{
			for( int i = 0; i < series.length; i++ )
			{
				writeXyzSeries(series[i]);
			}
		}

		writer.closeElement();
	}


	/**
	 *
	 */
	private void writeXyzSeries(JRXyzSeries series) throws IOException
	{
		writer.startElement(XmlConstants.ELEMENT_xyzSeries);

		writer.writeExpression(XmlConstants.ELEMENT_seriesExpression, series.getSeriesExpression(), false);
		writer.writeExpression(XmlConstants.ELEMENT_xValueExpression, series.getXValueExpression(), false);
		writer.writeExpression(XmlConstants.ELEMENT_yValueExpression, series.getYValueExpression(), false);
		writer.writeExpression(XmlConstants.ELEMENT_zValueExpression, series.getZValueExpression(), false);
		writeHyperlink(XmlConstants.ELEMENT_itemHyperlink, series.getItemHyperlink());

		writer.closeElement();
	}

	/**
	 *
	 */
	private void writeXySeries(JRXySeries xySeries) throws IOException
	{
		writer.startElement(XmlConstants.ELEMENT_xySeries);

		writer.writeExpression(XmlConstants.ELEMENT_seriesExpression, xySeries.getSeriesExpression(), false);
		writer.writeExpression(XmlConstants.ELEMENT_xValueExpression, xySeries.getXValueExpression(), false);
		writer.writeExpression(XmlConstants.ELEMENT_yValueExpression, xySeries.getYValueExpression(), false);
		writer.writeExpression(XmlConstants.ELEMENT_labelExpression, xySeries.getLabelExpression(), false);
		writeHyperlink(XmlConstants.ELEMENT_itemHyperlink, xySeries.getItemHyperlink());

		writer.closeElement();
	}


	/**
	 *
	 */
	private void writeXyDataset(JRXyDataset dataset) throws IOException
	{
		writer.startElement(XmlConstants.ELEMENT_xyDataset);

		writeElementDataset(dataset);

		/*   */
		JRXySeries[] xySeries = dataset.getSeries();
		if (xySeries != null && xySeries.length > 0)
		{
			for(int i = 0; i < xySeries.length; i++)
			{
				writeXySeries(xySeries[i]);
			}
		}

		writer.closeElement();
	}


	/**
	 *
	 */
	private void writeTimeSeries(JRTimeSeries timeSeries) throws IOException
	{
		writer.startElement(XmlConstants.ELEMENT_timeSeries);

		writer.writeExpression(XmlConstants.ELEMENT_seriesExpression, timeSeries.getSeriesExpression(), false);
		writer.writeExpression(XmlConstants.ELEMENT_timePeriodExpression, timeSeries.getTimePeriodExpression(), false);
		writer.writeExpression(XmlConstants.ELEMENT_valueExpression, timeSeries.getValueExpression(), false);
		writer.writeExpression(XmlConstants.ELEMENT_labelExpression, timeSeries.getLabelExpression(), false);
		writeHyperlink(XmlConstants.ELEMENT_itemHyperlink, timeSeries.getItemHyperlink());

		writer.closeElement();
	}


	/**
	 * 
	 */
	private void writeGanttSeries(JRGanttSeries ganttSeries) throws IOException
	{
		writer.startElement(XmlConstants.ELEMENT_ganttSeries);
		
		writer.writeExpression(XmlConstants.ELEMENT_seriesExpression, ganttSeries.getSeriesExpression(), false);
		writer.writeExpression(XmlConstants.ELEMENT_taskExpression, ganttSeries.getTaskExpression(), false);
		writer.writeExpression(XmlConstants.ELEMENT_subtaskExpression, ganttSeries.getSubtaskExpression(), false);
		writer.writeExpression(XmlConstants.ELEMENT_startDateExpression, ganttSeries.getStartDateExpression(), false);
		writer.writeExpression(XmlConstants.ELEMENT_endDateExpression, ganttSeries.getEndDateExpression(), false);
		writer.writeExpression(XmlConstants.ELEMENT_percentExpression, ganttSeries.getPercentExpression(), false);
		writer.writeExpression(XmlConstants.ELEMENT_labelExpression, ganttSeries.getLabelExpression(), false);
		writeHyperlink(XmlConstants.ELEMENT_itemHyperlink, ganttSeries.getItemHyperlink());
		
		writer.closeElement();
	}


	private void writeTimePeriodSeries(JRTimePeriodSeries timePeriodSeries) throws IOException
	{
		writer.startElement(XmlConstants.ELEMENT_timePeriodSeries);

		writer.writeExpression(XmlConstants.ELEMENT_seriesExpression, timePeriodSeries.getSeriesExpression(), false);
		writer.writeExpression(XmlConstants.ELEMENT_startDateExpression, timePeriodSeries.getStartDateExpression(), false);
		writer.writeExpression(XmlConstants.ELEMENT_endDateExpression, timePeriodSeries.getEndDateExpression(), false);
		writer.writeExpression(XmlConstants.ELEMENT_valueExpression, timePeriodSeries.getValueExpression(), false);
		writer.writeExpression(XmlConstants.ELEMENT_labelExpression, timePeriodSeries.getLabelExpression(), false);
		writeHyperlink(XmlConstants.ELEMENT_itemHyperlink, timePeriodSeries.getItemHyperlink());

		writer.closeElement();
	}


	/**
	 *
	 */
	public void writePieDataset(JRPieDataset dataset) throws IOException
	{
		writer.startElement(XmlConstants.ELEMENT_pieDataset, getNamespace());

		writer.addAttribute(XmlConstants.ATTRIBUTE_maxCount, dataset.getMaxCount());
		writer.addAttribute(XmlConstants.ATTRIBUTE_minPercentage, dataset.getMinPercentage());

		writeElementDataset(dataset);

		/*   */
		JRPieSeries[] pieSeries = dataset.getSeries();
		if (pieSeries != null)
		{
			if (pieSeries.length > 1)
			{
				for(int i = 0; i < pieSeries.length; i++)
				{
					writePieSeries(pieSeries[i]);
				}
			}
			else
			{
				//preserve old syntax of single series pie datasets
				JRPieSeries ps = pieSeries[0];
				writer.writeExpression(XmlConstants.ELEMENT_keyExpression, ps.getKeyExpression(), false);
				writer.writeExpression(XmlConstants.ELEMENT_valueExpression, ps.getValueExpression(), false);
				writer.writeExpression(XmlConstants.ELEMENT_labelExpression, ps.getLabelExpression(), false);
				writeHyperlink(XmlConstants.ELEMENT_sectionHyperlink, ps.getSectionHyperlink());
			}
		}

		writer.writeExpression(XmlConstants.ELEMENT_otherKeyExpression, dataset.getOtherKeyExpression(), false);
		writer.writeExpression(XmlConstants.ELEMENT_otherLabelExpression, dataset.getOtherLabelExpression(), false);
		writeHyperlink(XmlConstants.ELEMENT_otherSectionHyperlink, dataset.getOtherSectionHyperlink());

		writer.closeElement();
	}

	/**
	 * Writes the description of a value dataset to the output stream.
	 * @param dataset the value dataset to persist
	 */
	public void writeValueDataset(JRValueDataset dataset) throws IOException
	{
		writer.startElement(XmlConstants.ELEMENT_valueDataset, getNamespace());

		writeElementDataset(dataset);

		writer.writeExpression(XmlConstants.ELEMENT_valueExpression, dataset.getValueExpression(), false);

		writer.closeElement();
	}


	/**
	 * Writes the description of how to display a value in a valueDataset.
	 *
	 * @param valueDisplay the description to save
	 */
	public void writeValueDisplay(JRValueDisplay valueDisplay) throws IOException
	{
		writer.startElement(XmlConstants.ELEMENT_valueDisplay, getNamespace());

		writer.addAttribute(XmlConstants.ATTRIBUTE_color, valueDisplay.getColor());
		writer.addAttribute(XmlConstants.ATTRIBUTE_mask, valueDisplay.getMask());

		writeFont(valueDisplay.getFont());

		writer.closeElement();
	}

	/**
	 * Writes the description of how to display item labels in a category plot.
	 *
	 * @param itemLabel the description to save
	 */
	public void writeItemLabel(JRItemLabel itemLabel) throws IOException
	{
		if(itemLabel != null)
		{
			writer.startElement(XmlConstants.ELEMENT_itemLabel, getNamespace());
	
			writer.addAttribute(XmlConstants.ATTRIBUTE_color, itemLabel.getColor());
			writer.addAttribute(XmlConstants.ATTRIBUTE_backgroundColor, itemLabel.getBackgroundColor());
	//		writer.addAttribute(XmlConstants.ATTRIBUTE_mask, itemLabel.getMask());
	
			writeFont(itemLabel.getFont());
	
			writer.closeElement();
		}
	}

	/**
	 * Writes a data range block to the output stream.
	 *
	 * @param dataRange the range to write
	 */
	public void writeDataRange(JRDataRange dataRange) throws IOException
	{
		writer.startElement(XmlConstants.ELEMENT_dataRange, getNamespace());

		writer.writeExpression(XmlConstants.ELEMENT_lowExpression, dataRange.getLowExpression(), false);
		writer.writeExpression(XmlConstants.ELEMENT_highExpression, dataRange.getHighExpression(), false);

		writer.closeElement();
	}


	/**
	 * Writes a meter interval description to the output stream.
	 *
	 * @param interval the interval to write
	 */
	private void writeMeterInterval(JRMeterInterval interval) throws IOException
	{
		writer.startElement(XmlConstants.ELEMENT_meterInterval);

		writer.addAttribute(XmlConstants.ATTRIBUTE_label, interval.getLabel());
		writer.addAttribute(XmlConstants.ATTRIBUTE_color, interval.getBackgroundColor());
		writer.addAttribute(XmlConstants.ATTRIBUTE_alpha, interval.getAlphaDouble());

		writeDataRange(interval.getDataRange());

		writer.closeElement();
	}

	/**
	 * Writes out the contents of a series colors block for a chart.  Assumes the caller
	 * has already written the <code>&lt;seriesColors&gt;</code> tag.
	 *
	 * @param seriesColors the colors to write
	 */
	private void writeSeriesColors(SortedSet seriesColors) throws IOException
	{
		if (seriesColors == null || seriesColors.size() == 0)
			return;

		JRSeriesColor[] colors = (JRSeriesColor[])seriesColors.toArray(new JRSeriesColor[0]);
		for (int i = 0; i < colors.length; i++)
		{
			writer.startElement(XmlConstants.ELEMENT_seriesColor);
			writer.addAttribute(XmlConstants.ATTRIBUTE_seriesOrder, colors[i].getSeriesOrder());
			writer.addAttribute(XmlConstants.ATTRIBUTE_color, colors[i].getColor());
			writer.closeElement();
		}
	}

	/**
	 * Write the information about a the data and layout that make up one range axis in
	 * a multiple axis chart.
	 *
	 * @param chartAxis the axis being written
	 */
	private void writeChartAxis(JRChartAxis chartAxis) throws IOException
	{
		writer.startElement(JRChartAxisFactory.ELEMENT_axis);
		writer.addAttribute(JRChartAxisFactory.ATTRIBUTE_position, chartAxis.getPositionByte(), JRXmlConstants.getAxisPositionMap());

		// Let the nested chart describe itself
		writeChartTag(chartAxis.getChart());
		writer.closeElement();
	}

	/**
	 *
	 *
	 */
	private void writePlot(JRChartPlot plot) throws IOException
	{
		writer.startElement(XmlConstants.ELEMENT_plot);
		writer.addAttribute(XmlConstants.ATTRIBUTE_backcolor, plot.getOwnBackcolor());
		writer.addAttribute(XmlConstants.ATTRIBUTE_orientation, plot.getOrientation(), JRXmlConstants.getPlotOrientationMap(), PlotOrientation.VERTICAL);
		writer.addAttribute(XmlConstants.ATTRIBUTE_backgroundAlpha, plot.getBackgroundAlphaFloat());
		writer.addAttribute(XmlConstants.ATTRIBUTE_foregroundAlpha, plot.getForegroundAlphaFloat());
		writer.addAttribute(XmlConstants.ATTRIBUTE_labelRotation, plot.getLabelRotationDouble());
		writeSeriesColors(plot.getSeriesColors());

		writer.closeElement();
	}


	/**
	 *
	 */
	public void writePieChart(JRChart chart) throws IOException
	{
		writer.startElement(XmlConstants.ELEMENT_pieChart, getNamespace());
		writeChart(chart);
		writePieDataset((JRPieDataset) chart.getDataset());

		// write plot
		JRPiePlot plot = (JRPiePlot) chart.getPlot();
		writer.startElement(XmlConstants.ELEMENT_piePlot);
		writer.addAttribute(XmlConstants.ATTRIBUTE_isCircular, plot.getCircular());
		writer.addAttribute(XmlConstants.ATTRIBUTE_labelFormat, plot.getLabelFormat());
		writer.addAttribute(XmlConstants.ATTRIBUTE_legendLabelFormat, plot.getLegendLabelFormat());
		writePlot(chart.getPlot());
		writeItemLabel(plot.getItemLabel());
		writer.closeElement();

		writer.closeElement();
	}


	/**
	 *
	 */
	public void writePie3DChart(JRChart chart) throws IOException
	{
		writer.startElement(XmlConstants.ELEMENT_pie3DChart, getNamespace());
		writeChart(chart);
		writePieDataset((JRPieDataset) chart.getDataset());

		// write plot
		JRPie3DPlot plot = (JRPie3DPlot) chart.getPlot();
		writer.startElement(XmlConstants.ELEMENT_pie3DPlot);
		writer.addAttribute(XmlConstants.ATTRIBUTE_depthFactor, plot.getDepthFactorDouble());
		writer.addAttribute(XmlConstants.ATTRIBUTE_isCircular, plot.getCircular());
		writer.addAttribute(XmlConstants.ATTRIBUTE_labelFormat, plot.getLabelFormat());
		writer.addAttribute(XmlConstants.ATTRIBUTE_legendLabelFormat, plot.getLegendLabelFormat());
		writePlot(chart.getPlot());
		writeItemLabel(plot.getItemLabel());
		writer.closeElement();

		writer.closeElement();
	}


	/**
	 * Writes out the category axis format block.
	 *
	 * @param axisLabelFont font to use for the axis label
	 * @param axisLabelColor color to use for the axis label
	 * @param axisTickLabelFont font to use for the label of each tick mark
	 * @param axisTickLabelColor color to use for the label of each tick mark
	 * @param axisTickLabelMask formatting mask to use for the label of each tick mark
	 * @param axisVerticalTickLabels flag to render tick labels at 90 degrees
	 * @param labelRotation label rotation angle
	 * @param axisLineColor the color to use for the axis line and any tick marks
	 *
	 */
	public void writeCategoryAxisFormat(
		JRFont axisLabelFont, 
		Color axisLabelColor,
		JRFont axisTickLabelFont, 
		Color axisTickLabelColor,
		String axisTickLabelMask, 
		Boolean axisVerticalTickLabels, 
		Double labelRotation, 
		Color axisLineColor
		)  throws IOException
	{
		if (axisLabelFont == null && axisLabelColor == null &&
			axisTickLabelFont == null && axisTickLabelColor == null && axisLineColor == null)
			return;

		writer.startElement(XmlConstants.ELEMENT_categoryAxisFormat, getNamespace());
		
		writer.addAttribute(XmlConstants.ATTRIBUTE_labelRotation, labelRotation);

		writeAxisFormat(
			axisLabelFont, 
			axisLabelColor,
			axisTickLabelFont, 
			axisTickLabelColor,
			axisTickLabelMask, 
			axisVerticalTickLabels, 
			axisLineColor
			);
		
		writer.closeElement();
	}
	
	
	/**
	 * Writes out the axis format block for a chart axis.
	 *
	 * @param axisFormatElementName the name of the axis format element being written
	 * @param axisLabelFont font to use for the axis label
	 * @param axisLabelColor color to use for the axis label
	 * @param axisTickLabelFont font to use for the label of each tick mark
	 * @param axisTickLabelColor color to use for the label of each tick mark
	 * @param axisTickLabelMask formatting mask to use for the label of each tick mark
	 * @param axisVerticalTickLabels flag to render tick labels at 90 degrees
	 * @param axisLineColor the color to use for the axis line and any tick marks
	 *
	 */
	public void writeAxisFormat(
		String axisFormatElementName,
		JRFont axisLabelFont, 
		Color axisLabelColor,
		JRFont axisTickLabelFont, 
		Color axisTickLabelColor,
		String axisTickLabelMask, 
		Boolean axisVerticalTickLabels, 
		Color axisLineColor
		)  throws IOException
	{
		if (axisLabelFont == null && axisLabelColor == null &&
			axisTickLabelFont == null && axisTickLabelColor == null && axisLineColor == null)
			return;

		writer.startElement(axisFormatElementName, getNamespace());

		writeAxisFormat(
			axisLabelFont, 
			axisLabelColor,
			axisTickLabelFont, 
			axisTickLabelColor,
			axisTickLabelMask, 
			axisVerticalTickLabels, 
			axisLineColor
			);
		
		writer.closeElement();
	}
	
	
	/**
	 * Writes out the axis format block for a chart axis.
	 *
	 * @param axisLabelFont font to use for the axis label
	 * @param axisLabelColor color to use for the axis label
	 * @param axisTickLabelFont font to use for the label of each tick mark
	 * @param axisTickLabelColor color to use for the label of each tick mark
	 * @param axisTickLabelMask formatting mask to use for the label of each tick mark
	 * @param axisVerticalTickLabels flag to render tick labels at 90 degrees
	 * @param axisLineColor the color to use for the axis line and any tick marks
	 *
	 */
	public void writeAxisFormat(
		JRFont axisLabelFont, 
		Color axisLabelColor,
		JRFont axisTickLabelFont, 
		Color axisTickLabelColor,
		String axisTickLabelMask, 
		Boolean axisVerticalTickLabels, 
		Color axisLineColor
		)  throws IOException
	{
		writer.startElement(XmlConstants.ELEMENT_axisFormat);
		writer.addAttribute(XmlConstants.ATTRIBUTE_labelColor, axisLabelColor);
		writer.addAttribute(XmlConstants.ATTRIBUTE_tickLabelColor, axisTickLabelColor);
		writer.addAttribute(XmlConstants.ATTRIBUTE_tickLabelMask, axisTickLabelMask);
		writer.addAttribute(XmlConstants.ATTRIBUTE_verticalTickLabels, axisVerticalTickLabels);
		writer.addAttribute(XmlConstants.ATTRIBUTE_axisLineColor, axisLineColor);

		if (axisLabelFont != null)
		{
			writer.startElement(XmlConstants.ELEMENT_labelFont);
			writeFont(axisLabelFont);
			writer.closeElement();
		}

		if (axisTickLabelFont != null)
		{
			writer.startElement(XmlConstants.ELEMENT_tickLabelFont);
			writeFont(axisTickLabelFont);
			writer.closeElement();
		}

		writer.closeElement();
	}
	/**
	 *
	 */
	private void writeBarPlot(JRBarPlot plot) throws IOException
	{
		writer.startElement(XmlConstants.ELEMENT_barPlot);
		writer.addAttribute(XmlConstants.ATTRIBUTE_isShowLabels, plot.getShowLabels());
		writer.addAttribute(XmlConstants.ATTRIBUTE_isShowTickLabels, plot.getShowTickLabels());
		writer.addAttribute(XmlConstants.ATTRIBUTE_isShowTickMarks, plot.getShowTickMarks());

		writePlot(plot);
		writeItemLabel(plot.getItemLabel());
		
		writer.writeExpression(XmlConstants.ELEMENT_categoryAxisLabelExpression, plot.getCategoryAxisLabelExpression(), false);
		writeCategoryAxisFormat(plot.getCategoryAxisLabelFont(), plot.getOwnCategoryAxisLabelColor(),
						plot.getCategoryAxisTickLabelFont(), plot.getOwnCategoryAxisTickLabelColor(),
						plot.getCategoryAxisTickLabelMask(), plot.getCategoryAxisVerticalTickLabels(), 
						plot.getCategoryAxisTickLabelRotation(), plot.getOwnCategoryAxisLineColor());
		writer.writeExpression(XmlConstants.ELEMENT_valueAxisLabelExpression, plot.getValueAxisLabelExpression(), false);
		writeAxisFormat(XmlConstants.ELEMENT_valueAxisFormat, plot.getValueAxisLabelFont(), plot.getOwnValueAxisLabelColor(),
				plot.getValueAxisTickLabelFont(), plot.getOwnValueAxisTickLabelColor(),
				plot.getValueAxisTickLabelMask(), plot.getValueAxisVerticalTickLabels(), plot.getOwnValueAxisLineColor());
		writer.writeExpression(XmlConstants.ELEMENT_domainAxisMinValueExpression, plot.getDomainAxisMinValueExpression(), false);
		writer.writeExpression(XmlConstants.ELEMENT_domainAxisMaxValueExpression, plot.getDomainAxisMaxValueExpression(), false);
		writer.writeExpression(XmlConstants.ELEMENT_rangeAxisMinValueExpression, plot.getRangeAxisMinValueExpression(), false);
		writer.writeExpression(XmlConstants.ELEMENT_rangeAxisMaxValueExpression, plot.getRangeAxisMaxValueExpression(), false);


		writer.closeElement();
	}


	/**
	 *
	 */
	private void writeBubblePlot(JRBubblePlot plot) throws IOException
	{
		writer.startElement(XmlConstants.ELEMENT_bubblePlot);
		writer.addAttribute(XmlConstants.ATTRIBUTE_scaleType, plot.getScaleTypeInteger(), JRXmlConstants.getScaleTypeMap());
		writePlot(plot);

		writer.writeExpression(XmlConstants.ELEMENT_xAxisLabelExpression, plot.getXAxisLabelExpression(), false);
		writeAxisFormat(XmlConstants.ELEMENT_xAxisFormat, plot.getXAxisLabelFont(), plot.getOwnXAxisLabelColor(),
				plot.getXAxisTickLabelFont(), plot.getOwnXAxisTickLabelColor(),
				plot.getXAxisTickLabelMask(), plot.getXAxisVerticalTickLabels(), plot.getOwnXAxisLineColor());
		writer.writeExpression(XmlConstants.ELEMENT_yAxisLabelExpression, plot.getYAxisLabelExpression(), false);
		writeAxisFormat(XmlConstants.ELEMENT_yAxisFormat, plot.getYAxisLabelFont(), plot.getOwnYAxisLabelColor(),
				plot.getYAxisTickLabelFont(), plot.getOwnYAxisTickLabelColor(),
				plot.getYAxisTickLabelMask(), plot.getYAxisVerticalTickLabels(), plot.getOwnYAxisLineColor());
		writer.writeExpression(XmlConstants.ELEMENT_domainAxisMinValueExpression, plot.getDomainAxisMinValueExpression(), false);
		writer.writeExpression(XmlConstants.ELEMENT_domainAxisMaxValueExpression, plot.getDomainAxisMaxValueExpression(), false);
		writer.writeExpression(XmlConstants.ELEMENT_rangeAxisMinValueExpression, plot.getRangeAxisMinValueExpression(), false);
		writer.writeExpression(XmlConstants.ELEMENT_rangeAxisMaxValueExpression, plot.getRangeAxisMaxValueExpression(), false);

		writer.closeElement();
	}


	/**
	 *
	 */
	private void writeLinePlot(JRLinePlot plot) throws IOException
	{
		writer.startElement(XmlConstants.ELEMENT_linePlot);
		writer.addAttribute(XmlConstants.ATTRIBUTE_isShowLines, plot.getShowLines());
		writer.addAttribute(XmlConstants.ATTRIBUTE_isShowShapes, plot.getShowShapes());

		writePlot(plot);

		writer.writeExpression(XmlConstants.ELEMENT_categoryAxisLabelExpression, plot.getCategoryAxisLabelExpression(), false);
		writeCategoryAxisFormat(plot.getCategoryAxisLabelFont(), plot.getOwnCategoryAxisLabelColor(),
				plot.getCategoryAxisTickLabelFont(), plot.getOwnCategoryAxisTickLabelColor(),
				plot.getCategoryAxisTickLabelMask(), plot.getCategoryAxisVerticalTickLabels(), 
				plot.getCategoryAxisTickLabelRotation(), plot.getOwnCategoryAxisLineColor());
		writer.writeExpression(XmlConstants.ELEMENT_valueAxisLabelExpression, plot.getValueAxisLabelExpression(), false);
		writeAxisFormat(XmlConstants.ELEMENT_valueAxisFormat, plot.getValueAxisLabelFont(), plot.getOwnValueAxisLabelColor(),
				plot.getValueAxisTickLabelFont(), plot.getOwnValueAxisTickLabelColor(),
				plot.getValueAxisTickLabelMask(), plot.getValueAxisVerticalTickLabels(), plot.getOwnValueAxisLineColor());
		writer.writeExpression(XmlConstants.ELEMENT_domainAxisMinValueExpression, plot.getDomainAxisMinValueExpression(), false);
		writer.writeExpression(XmlConstants.ELEMENT_domainAxisMaxValueExpression, plot.getDomainAxisMaxValueExpression(), false);
		writer.writeExpression(XmlConstants.ELEMENT_rangeAxisMinValueExpression, plot.getRangeAxisMinValueExpression(), false);
		writer.writeExpression(XmlConstants.ELEMENT_rangeAxisMaxValueExpression, plot.getRangeAxisMaxValueExpression(), false);

		writer.closeElement();
	}


	private void writeTimeSeriesPlot(JRTimeSeriesPlot plot) throws IOException
	{
		writer.startElement(XmlConstants.ELEMENT_timeSeriesPlot);
		writer.addAttribute(XmlConstants.ATTRIBUTE_isShowLines, plot.getShowLines());
		writer.addAttribute(XmlConstants.ATTRIBUTE_isShowShapes, plot.getShowShapes());

		writePlot( plot );

		writer.writeExpression(XmlConstants.ELEMENT_timeAxisLabelExpression, plot.getTimeAxisLabelExpression(), false);
		writeAxisFormat(XmlConstants.ELEMENT_timeAxisFormat, plot.getTimeAxisLabelFont(), plot.getOwnTimeAxisLabelColor(),
				plot.getTimeAxisTickLabelFont(), plot.getOwnTimeAxisTickLabelColor(),
				plot.getTimeAxisTickLabelMask(), plot.getTimeAxisVerticalTickLabels(), plot.getOwnTimeAxisLineColor());
		writer.writeExpression(XmlConstants.ELEMENT_valueAxisLabelExpression, plot.getValueAxisLabelExpression(), false);
		writeAxisFormat(XmlConstants.ELEMENT_valueAxisFormat, plot.getValueAxisLabelFont(), plot.getOwnValueAxisLabelColor(),
				plot.getValueAxisTickLabelFont(), plot.getOwnValueAxisTickLabelColor(),
				plot.getValueAxisTickLabelMask(), plot.getValueAxisVerticalTickLabels(), plot.getOwnValueAxisLineColor());
		writer.writeExpression(XmlConstants.ELEMENT_domainAxisMinValueExpression, plot.getDomainAxisMinValueExpression(), false);
		writer.writeExpression(XmlConstants.ELEMENT_domainAxisMaxValueExpression, plot.getDomainAxisMaxValueExpression(), false);
		writer.writeExpression(XmlConstants.ELEMENT_rangeAxisMinValueExpression, plot.getRangeAxisMinValueExpression(), false);
		writer.writeExpression(XmlConstants.ELEMENT_rangeAxisMaxValueExpression, plot.getRangeAxisMaxValueExpression(), false);


		writer.closeElement();
	}


	/**
	 *
	 */
	public void writeBar3DPlot(JRBar3DPlot plot) throws IOException
	{
		writer.startElement(XmlConstants.ELEMENT_bar3DPlot, getNamespace());
		writer.addAttribute(XmlConstants.ATTRIBUTE_isShowLabels, plot.getShowLabels());
		writer.addAttribute(XmlConstants.ATTRIBUTE_xOffset, plot.getXOffsetDouble());
		writer.addAttribute(XmlConstants.ATTRIBUTE_yOffset, plot.getYOffsetDouble());

		writePlot(plot);
		writeItemLabel(plot.getItemLabel());
		
		writer.writeExpression(XmlConstants.ELEMENT_categoryAxisLabelExpression, plot.getCategoryAxisLabelExpression(), false);
		writeCategoryAxisFormat(plot.getCategoryAxisLabelFont(), plot.getOwnCategoryAxisLabelColor(),
				plot.getCategoryAxisTickLabelFont(), plot.getOwnCategoryAxisTickLabelColor(),
				plot.getCategoryAxisTickLabelMask(), plot.getCategoryAxisVerticalTickLabels(), 
				plot.getCategoryAxisTickLabelRotation(), plot.getOwnCategoryAxisLineColor());
		writer.writeExpression(XmlConstants.ELEMENT_valueAxisLabelExpression, plot.getValueAxisLabelExpression(), false);
		writeAxisFormat(XmlConstants.ELEMENT_valueAxisFormat, plot.getValueAxisLabelFont(), plot.getOwnValueAxisLabelColor(),
				plot.getValueAxisTickLabelFont(), plot.getOwnValueAxisTickLabelColor(),
				plot.getValueAxisTickLabelMask(), plot.getValueAxisVerticalTickLabels(), plot.getOwnValueAxisLineColor());
		writer.writeExpression(XmlConstants.ELEMENT_domainAxisMinValueExpression, plot.getDomainAxisMinValueExpression(), false);
		writer.writeExpression(XmlConstants.ELEMENT_domainAxisMaxValueExpression, plot.getDomainAxisMaxValueExpression(), false);
		writer.writeExpression(XmlConstants.ELEMENT_rangeAxisMinValueExpression, plot.getRangeAxisMinValueExpression(), false);
		writer.writeExpression(XmlConstants.ELEMENT_rangeAxisMaxValueExpression, plot.getRangeAxisMaxValueExpression(), false);


		writer.closeElement();
	}


	/**
	 *
	 */
	public void writeBarChart(JRChart chart) throws IOException
	{
		writer.startElement(XmlConstants.ELEMENT_barChart, getNamespace());

		writeChart(chart);
		writeCategoryDataSet((JRCategoryDataset) chart.getDataset());
		writeBarPlot((JRBarPlot) chart.getPlot());

		writer.closeElement();
	}


	/**
	 *
	 */
	public void writeBar3DChart(JRChart chart) throws IOException
	{
		writer.startElement(XmlConstants.ELEMENT_bar3DChart, getNamespace());

		writeChart(chart);
		writeCategoryDataSet((JRCategoryDataset) chart.getDataset());
		writeBar3DPlot((JRBar3DPlot) chart.getPlot());

		writer.closeElement();
	}


	/**
	 *
	 */
	public void writeBubbleChart(JRChart chart) throws IOException
	{
		writer.startElement(XmlConstants.ELEMENT_bubbleChart, getNamespace());
		writeChart(chart);
		writeXyzDataset((JRXyzDataset) chart.getDataset());
		writeBubblePlot((JRBubblePlot) chart.getPlot());
		writer.closeElement();
	}


	/**
	 *
	 */
	public void writeStackedBarChart(JRChart chart) throws IOException
	{
		writer.startElement(XmlConstants.ELEMENT_stackedBarChart, getNamespace());

		writeChart(chart);
		writeCategoryDataSet((JRCategoryDataset) chart.getDataset());
		writeBarPlot((JRBarPlot) chart.getPlot());

		writer.closeElement();
	}


	/**
	 *
	 */
	public void writeStackedBar3DChart(JRChart chart) throws IOException
	{
		writer.startElement(XmlConstants.ELEMENT_stackedBar3DChart, getNamespace());

		writeChart(chart);
		writeCategoryDataSet((JRCategoryDataset) chart.getDataset());
		writeBar3DPlot((JRBar3DPlot) chart.getPlot());
		writer.closeElement();
	}


	/**
	 *
	 */
	public void writeLineChart(JRChart chart) throws IOException
	{
		writer.startElement(XmlConstants.ELEMENT_lineChart, getNamespace());

		writeChart(chart);
		writeCategoryDataSet((JRCategoryDataset) chart.getDataset());
		writeLinePlot((JRLinePlot) chart.getPlot());
		writer.closeElement();
	}


	public void writeTimeSeriesChart(JRChart chart) throws IOException
	{
		writer.startElement(XmlConstants.ELEMENT_timeSeriesChart, getNamespace());
		writeChart(chart);
		writeTimeSeriesDataset((JRTimeSeriesDataset)chart.getDataset());
		writeTimeSeriesPlot((JRTimeSeriesPlot)chart.getPlot());
		writer.closeElement();
	}

	public void writeHighLowDataset(JRHighLowDataset dataset) throws IOException
	{
		writer.startElement(XmlConstants.ELEMENT_highLowDataset, getNamespace());

		writeElementDataset(dataset);

		writer.writeExpression(XmlConstants.ELEMENT_seriesExpression, dataset.getSeriesExpression(), false);
		writer.writeExpression(XmlConstants.ELEMENT_dateExpression, dataset.getDateExpression(), false);
		writer.writeExpression(XmlConstants.ELEMENT_highExpression, dataset.getHighExpression(), false);
		writer.writeExpression(XmlConstants.ELEMENT_lowExpression, dataset.getLowExpression(), false);
		writer.writeExpression(XmlConstants.ELEMENT_openExpression, dataset.getOpenExpression(), false);
		writer.writeExpression(XmlConstants.ELEMENT_closeExpression, dataset.getCloseExpression(), false);
		writer.writeExpression(XmlConstants.ELEMENT_volumeExpression, dataset.getVolumeExpression(), false);
		writeHyperlink(XmlConstants.ELEMENT_itemHyperlink, dataset.getItemHyperlink());

		writer.closeElement();
	}


	public void writeHighLowChart(JRChart chart) throws IOException
	{
		writer.startElement(XmlConstants.ELEMENT_highLowChart, getNamespace());

		writeChart(chart);
		writeHighLowDataset((JRHighLowDataset) chart.getDataset());

		JRHighLowPlot plot = (JRHighLowPlot) chart.getPlot();
		writer.startElement(XmlConstants.ELEMENT_highLowPlot);
		writer.addAttribute(XmlConstants.ATTRIBUTE_isShowOpenTicks, plot.getShowOpenTicks());
		writer.addAttribute(XmlConstants.ATTRIBUTE_isShowCloseTicks, plot.getShowCloseTicks());

		writePlot(plot);

		writer.writeExpression(XmlConstants.ELEMENT_timeAxisLabelExpression, plot.getTimeAxisLabelExpression(), false);
		writeAxisFormat(XmlConstants.ELEMENT_timeAxisFormat, plot.getTimeAxisLabelFont(), plot.getOwnTimeAxisLabelColor(),
				plot.getTimeAxisTickLabelFont(), plot.getOwnTimeAxisTickLabelColor(),
				plot.getTimeAxisTickLabelMask(), plot.getTimeAxisVerticalTickLabels(), plot.getOwnTimeAxisLineColor());
		writer.writeExpression(XmlConstants.ELEMENT_valueAxisLabelExpression, plot.getValueAxisLabelExpression(), false);
		writeAxisFormat(XmlConstants.ELEMENT_valueAxisFormat, plot.getValueAxisLabelFont(), plot.getOwnValueAxisLabelColor(),
				plot.getValueAxisTickLabelFont(), plot.getOwnValueAxisTickLabelColor(),
				plot.getValueAxisTickLabelMask(), plot.getValueAxisVerticalTickLabels(), plot.getOwnValueAxisLineColor());
		writer.writeExpression(XmlConstants.ELEMENT_domainAxisMinValueExpression, plot.getDomainAxisMinValueExpression(), false);
		writer.writeExpression(XmlConstants.ELEMENT_domainAxisMaxValueExpression, plot.getDomainAxisMaxValueExpression(), false);
		writer.writeExpression(XmlConstants.ELEMENT_rangeAxisMinValueExpression, plot.getRangeAxisMinValueExpression(), false);
		writer.writeExpression(XmlConstants.ELEMENT_rangeAxisMaxValueExpression, plot.getRangeAxisMaxValueExpression(), false);


		writer.closeElement();
		writer.closeElement();
	}


	/**
	 * 
	 */
	public void writeGanttChart(JRChart chart) throws IOException
	{
		writer.startElement(XmlConstants.ELEMENT_ganttChart, getNamespace());
		
		writeChart(chart);
		writeGanttDataset((JRGanttDataset) chart.getDataset());
		writeBarPlot((JRBarPlot) chart.getPlot());
		
		writer.closeElement();
	}


	public void writeCandlestickChart(JRChart chart) throws IOException
	{
		writer.startElement(XmlConstants.ELEMENT_candlestickChart, getNamespace());

		writeChart(chart);
		writeHighLowDataset((JRHighLowDataset) chart.getDataset());

		JRCandlestickPlot plot = (JRCandlestickPlot) chart.getPlot();
		writer.startElement(XmlConstants.ELEMENT_candlestickPlot);
		writer.addAttribute(XmlConstants.ATTRIBUTE_isShowVolume, plot.getShowVolume());

		writePlot(plot);

		writer.writeExpression(XmlConstants.ELEMENT_timeAxisLabelExpression, plot.getTimeAxisLabelExpression(), false);
		writeAxisFormat(XmlConstants.ELEMENT_timeAxisFormat, plot.getTimeAxisLabelFont(), plot.getOwnTimeAxisLabelColor(),
				plot.getTimeAxisTickLabelFont(), plot.getOwnTimeAxisTickLabelColor(),
				plot.getTimeAxisTickLabelMask(), plot.getTimeAxisVerticalTickLabels(), plot.getOwnTimeAxisLineColor());
		writer.writeExpression(XmlConstants.ELEMENT_valueAxisLabelExpression, plot.getValueAxisLabelExpression(), false);
		writeAxisFormat(XmlConstants.ELEMENT_valueAxisFormat, plot.getValueAxisLabelFont(), plot.getOwnValueAxisLabelColor(),
				plot.getValueAxisTickLabelFont(), plot.getOwnValueAxisTickLabelColor(),
				plot.getValueAxisTickLabelMask(), plot.getValueAxisVerticalTickLabels(), plot.getOwnValueAxisLineColor());
		writer.writeExpression(XmlConstants.ELEMENT_domainAxisMinValueExpression, plot.getDomainAxisMinValueExpression(), false);
		writer.writeExpression(XmlConstants.ELEMENT_domainAxisMaxValueExpression, plot.getDomainAxisMaxValueExpression(), false);
		writer.writeExpression(XmlConstants.ELEMENT_rangeAxisMinValueExpression, plot.getRangeAxisMinValueExpression(), false);
		writer.writeExpression(XmlConstants.ELEMENT_rangeAxisMaxValueExpression, plot.getRangeAxisMaxValueExpression(), false);


		writer.closeElement();
		writer.closeElement();
	}

	/**
	 *
	 */
	private void writeAreaPlot(JRAreaPlot plot) throws IOException
	{
		writer.startElement(XmlConstants.ELEMENT_areaPlot);
		writePlot(plot);

		writer.writeExpression(XmlConstants.ELEMENT_categoryAxisLabelExpression, plot.getCategoryAxisLabelExpression(), false);
		writeCategoryAxisFormat(plot.getCategoryAxisLabelFont(), plot.getOwnCategoryAxisLabelColor(),
				plot.getCategoryAxisTickLabelFont(), plot.getOwnCategoryAxisTickLabelColor(),
				plot.getCategoryAxisTickLabelMask(), plot.getCategoryAxisVerticalTickLabels(), 
				plot.getCategoryAxisTickLabelRotation(), plot.getOwnCategoryAxisLineColor());
		writer.writeExpression(XmlConstants.ELEMENT_valueAxisLabelExpression, plot.getValueAxisLabelExpression(), false);
		writeAxisFormat(XmlConstants.ELEMENT_valueAxisFormat, plot.getValueAxisLabelFont(), plot.getOwnValueAxisLabelColor(),
				plot.getValueAxisTickLabelFont(), plot.getOwnValueAxisTickLabelColor(),
				plot.getValueAxisTickLabelMask(), plot.getValueAxisVerticalTickLabels(), plot.getOwnValueAxisLineColor());
		writer.writeExpression(XmlConstants.ELEMENT_domainAxisMinValueExpression, plot.getDomainAxisMinValueExpression(), false);
		writer.writeExpression(XmlConstants.ELEMENT_domainAxisMaxValueExpression, plot.getDomainAxisMaxValueExpression(), false);
		writer.writeExpression(XmlConstants.ELEMENT_rangeAxisMinValueExpression, plot.getRangeAxisMinValueExpression(), false);
		writer.writeExpression(XmlConstants.ELEMENT_rangeAxisMaxValueExpression, plot.getRangeAxisMaxValueExpression(), false);


		writer.closeElement();
	}


	/**
	 *
	 */
	public void writeAreaChart(JRChart chart) throws IOException
	{
		writer.startElement(XmlConstants.ELEMENT_areaChart, getNamespace());

		writeChart(chart);
		writeCategoryDataSet((JRCategoryDataset) chart.getDataset());
		writeAreaPlot((JRAreaPlot) chart.getPlot());

		writer.closeElement();
	}


	/**
	 *
	 */
	private void writeScatterPlot(JRScatterPlot plot) throws IOException
	{
		writer.startElement(XmlConstants.ELEMENT_scatterPlot);
		writer.addAttribute(XmlConstants.ATTRIBUTE_isShowLines, plot.getShowLines());
		writer.addAttribute(XmlConstants.ATTRIBUTE_isShowShapes, plot.getShowShapes());

		writePlot(plot);

		writer.writeExpression(XmlConstants.ELEMENT_xAxisLabelExpression, plot.getXAxisLabelExpression(), false);
		writeAxisFormat(XmlConstants.ELEMENT_xAxisFormat, plot.getXAxisLabelFont(), plot.getOwnXAxisLabelColor(),
				plot.getXAxisTickLabelFont(), plot.getOwnXAxisTickLabelColor(),
				plot.getXAxisTickLabelMask(), plot.getXAxisVerticalTickLabels(), plot.getOwnXAxisLineColor());
		writer.writeExpression(XmlConstants.ELEMENT_yAxisLabelExpression, plot.getYAxisLabelExpression(), false);
		writeAxisFormat(XmlConstants.ELEMENT_yAxisFormat, plot.getYAxisLabelFont(), plot.getOwnYAxisLabelColor(),
				plot.getYAxisTickLabelFont(), plot.getOwnYAxisTickLabelColor(),
				plot.getYAxisTickLabelMask(), plot.getYAxisVerticalTickLabels(), plot.getOwnYAxisLineColor());
		writer.writeExpression(XmlConstants.ELEMENT_domainAxisMinValueExpression, plot.getDomainAxisMinValueExpression(), false);
		writer.writeExpression(XmlConstants.ELEMENT_domainAxisMaxValueExpression, plot.getDomainAxisMaxValueExpression(), false);
		writer.writeExpression(XmlConstants.ELEMENT_rangeAxisMinValueExpression, plot.getRangeAxisMinValueExpression(), false);
		writer.writeExpression(XmlConstants.ELEMENT_rangeAxisMaxValueExpression, plot.getRangeAxisMaxValueExpression(), false);

		writer.closeElement();
	}


	/**
	 *
	 */
	public void writeScatterChart(JRChart chart) throws IOException
	{
		writer.startElement(XmlConstants.ELEMENT_scatterChart, getNamespace());

		writeChart(chart);
		writeXyDataset((JRXyDataset) chart.getDataset());
		writeScatterPlot((JRScatterPlot) chart.getPlot());

		writer.closeElement();
	}


	/**
	 *
	 */
	public void writeXyAreaChart(JRChart chart) throws IOException
	{
		writer.startElement(XmlConstants.ELEMENT_xyAreaChart, getNamespace());

		writeChart(chart);
		writeXyDataset((JRXyDataset) chart.getDataset());
		writeAreaPlot((JRAreaPlot) chart.getPlot());

		writer.closeElement();
	}


	/**
	 *
	 */
	public void writeXyBarChart(JRChart chart) throws IOException
	{
		writer.startElement(XmlConstants.ELEMENT_xyBarChart, getNamespace());

		writeChart(chart);
		JRChartDataset dataset = chart.getDataset();

		if( dataset.getDatasetType() == JRChartDataset.TIMESERIES_DATASET ){
			writeTimeSeriesDataset( (JRTimeSeriesDataset)dataset );
		}
		else if( dataset.getDatasetType() == JRChartDataset.TIMEPERIOD_DATASET ){
			writeTimePeriodDataset( (JRTimePeriodDataset)dataset );
		}
		else if( dataset.getDatasetType() == JRChartDataset.XY_DATASET ){
			writeXyDataset( (JRXyDataset)dataset );
		}

		writeBarPlot((JRBarPlot) chart.getPlot());

		writer.closeElement();
	}


	/**
	 *
	 */
	public void writeXyLineChart(JRChart chart) throws IOException
	{
		writer.startElement(XmlConstants.ELEMENT_xyLineChart, getNamespace());

		writeChart(chart);
		writeXyDataset((JRXyDataset) chart.getDataset());
		writeLinePlot((JRLinePlot) chart.getPlot());

		writer.closeElement();
	}


	/**
	 * Writes the definition of a meter chart to the output stream.
	 *
	 * @param chart the meter chart to write
	 */
	public void writeMeterChart(JRChart chart) throws IOException
	{
		writer.startElement(XmlConstants.ELEMENT_meterChart, getNamespace());

		writeChart(chart);
		writeValueDataset((JRValueDataset) chart.getDataset());

		// write plot
		JRMeterPlot plot = (JRMeterPlot) chart.getPlot();
		writer.startElement(JRMeterPlotFactory.ELEMENT_meterPlot);
		writer.addAttribute(JRMeterPlotFactory.ATTRIBUTE_shape, plot.getShapeByte(), JRXmlConstants.getMeterShapeMap());
		writer.addAttribute(JRMeterPlotFactory.ATTRIBUTE_angle, plot.getMeterAngleInteger());
		writer.addAttribute(JRMeterPlotFactory.ATTRIBUTE_units, plot.getUnits());
		writer.addAttribute(JRMeterPlotFactory.ATTRIBUTE_tickInterval, plot.getTickIntervalDouble());
		writer.addAttribute(JRMeterPlotFactory.ATTRIBUTE_meterColor, plot.getMeterBackgroundColor());
		writer.addAttribute(JRMeterPlotFactory.ATTRIBUTE_needleColor, plot.getNeedleColor());
		writer.addAttribute(JRMeterPlotFactory.ATTRIBUTE_tickColor, plot.getTickColor());
		
		writePlot(chart.getPlot());
		if (plot.getTickLabelFont() != null)
		{
			writer.startElement(XmlConstants.ELEMENT_tickLabelFont);
			writeFont(plot.getTickLabelFont());
			writer.closeElement();
		}
		writeValueDisplay(plot.getValueDisplay());
		writeDataRange(plot.getDataRange());

		List intervals = plot.getIntervals();
		if (intervals != null)
		{
			Iterator iter = intervals.iterator();
			while (iter.hasNext())
			{
				JRMeterInterval meterInterval =
						  (JRMeterInterval) iter.next();
				writeMeterInterval(meterInterval);
			}
		}
		writer.closeElement();

		writer.closeElement();
	}


	/**
	 * Writes the description of a thermometer chart to the output stream.
	 *
	 * @param chart the thermometer chart to write
	 */
	public void writeThermometerChart(JRChart chart) throws IOException
	{
		writer.startElement(XmlConstants.ELEMENT_thermometerChart, getNamespace());

		writeChart(chart);
		writeValueDataset((JRValueDataset) chart.getDataset());

		// write plot
		JRThermometerPlot plot = (JRThermometerPlot) chart.getPlot();

		writer.startElement(JRThermometerPlotFactory.ELEMENT_thermometerPlot, getNamespace());

		writer.addAttribute(JRThermometerPlotFactory.ATTRIBUTE_valueLocation, plot.getValueLocationByte(), JRXmlConstants.getThermometerValueLocationMap());
		writer.addAttribute(JRThermometerPlotFactory.ATTRIBUTE_showValueLines, plot.isShowValueLines(), false);
		writer.addAttribute(JRThermometerPlotFactory.ATTRIBUTE_mercuryColor, plot.getMercuryColor());

		writePlot(chart.getPlot());

		writeValueDisplay(plot.getValueDisplay());
		writeDataRange(plot.getDataRange());

		if (plot.getLowRange() != null)
		{
			writer.startElement(JRThermometerPlotFactory.ELEMENT_lowRange);
			writeDataRange(plot.getLowRange());
			writer.closeElement();
		}

		if (plot.getMediumRange() != null)
		{
			writer.startElement(JRThermometerPlotFactory.ELEMENT_mediumRange);
			writeDataRange(plot.getMediumRange());
			writer.closeElement();
		}

		if (plot.getHighRange() != null)
		{
			writer.startElement(JRThermometerPlotFactory.ELEMENT_highRange);
			writeDataRange(plot.getHighRange());
			writer.closeElement();
		}


		writer.closeElement();

		writer.closeElement();
	}


	/**
	 * Writes the definition of a multiple axis chart to the output stream.
	 *
	 * @param chart the multiple axis chart to write
	 */
	public void writeMultiAxisChart(JRChart chart) throws IOException
	{
		writer.startElement(XmlConstants.ELEMENT_multiAxisChart, getNamespace());

		writeChart(chart);

		// write plot
		JRMultiAxisPlot plot = (JRMultiAxisPlot) chart.getPlot();
		writer.startElement(XmlConstants.ELEMENT_multiAxisPlot);

		writePlot(chart.getPlot());

		List axes = plot.getAxes();
		if (axes != null)
		{
			Iterator iter = axes.iterator();
			while (iter.hasNext())
			{
				JRChartAxis chartAxis =
						  (JRChartAxis) iter.next();
				writeChartAxis(chartAxis);
			}
		}
		writer.closeElement();

		writer.closeElement();
	}

	/**
	 *
	 */
	public void writeStackedAreaChart(JRChart chart) throws IOException
	{
		writer.startElement(XmlConstants.ELEMENT_stackedAreaChart, getNamespace());

		writeChart(chart);
		writeCategoryDataSet((JRCategoryDataset) chart.getDataset());
		writeAreaPlot((JRAreaPlot) chart.getPlot());

		writer.closeElement();
	}


	public void writeChartTag(JRChart chart) throws IOException
	{
		switch(chart.getChartType()) {
			case JRChart.CHART_TYPE_AREA:
				writeAreaChart(chart);
				break;
			case JRChart.CHART_TYPE_BAR:
				writeBarChart(chart);
				break;
			case JRChart.CHART_TYPE_BAR3D:
				writeBar3DChart(chart);
				break;
			case JRChart.CHART_TYPE_BUBBLE:
				writeBubbleChart(chart);
				break;
			case JRChart.CHART_TYPE_CANDLESTICK:
				writeCandlestickChart(chart);
				break;
			case JRChart.CHART_TYPE_HIGHLOW:
				writeHighLowChart(chart);
				break;
			case JRChart.CHART_TYPE_LINE:
				writeLineChart(chart);
				break;
			case JRChart.CHART_TYPE_METER:
				writeMeterChart(chart);
				break;
			case JRChart.CHART_TYPE_MULTI_AXIS:
				writeMultiAxisChart(chart);
				break;
			case JRChart.CHART_TYPE_PIE:
				writePieChart(chart);
				break;
			case JRChart.CHART_TYPE_PIE3D:
				writePie3DChart(chart);
				break;
			case JRChart.CHART_TYPE_SCATTER:
				writeScatterChart(chart);
				break;
			case JRChart.CHART_TYPE_STACKEDBAR:
				writeStackedBarChart(chart);
				break;
			case JRChart.CHART_TYPE_STACKEDBAR3D:
				writeStackedBar3DChart(chart);
				break;
			case JRChart.CHART_TYPE_THERMOMETER:
				writeThermometerChart(chart);
				break;
			case JRChart.CHART_TYPE_TIMESERIES:
				writeTimeSeriesChart( chart );
				break;
			case JRChart.CHART_TYPE_XYAREA:
				writeXyAreaChart(chart);
				break;
			case JRChart.CHART_TYPE_XYBAR:
				writeXyBarChart(chart);
				break;
			case JRChart.CHART_TYPE_XYLINE:
				writeXyLineChart(chart);
				break;
			case JRChart.CHART_TYPE_STACKEDAREA:
				writeStackedAreaChart(chart);
				break;
			case JRChart.CHART_TYPE_GANTT:
				writeGanttChart(chart);
				break;
			default:
				throw new JRRuntimeException("Chart type not supported.");
		}
	}


	private void writeSubreportReturnValue(JRSubreportReturnValue returnValue) throws IOException
	{
		writer.startElement(XmlConstants.ELEMENT_returnValue);
		writer.addEncodedAttribute(XmlConstants.ATTRIBUTE_subreportVariable, returnValue.getSubreportVariable());
		writer.addEncodedAttribute(XmlConstants.ATTRIBUTE_toVariable, returnValue.getToVariable());
		writer.addAttribute(XmlConstants.ATTRIBUTE_calculation, returnValue.getCalculation(), JRXmlConstants.getCalculationMap(), JRVariable.CALCULATION_NOTHING);
		writer.addAttribute(XmlConstants.ATTRIBUTE_incrementerFactoryClass, returnValue.getIncrementerFactoryClassName());
		writer.closeElement();
	}


	public void writeCrosstab(JRCrosstab crosstab) throws IOException
	{
		writer.startElement(JRCrosstabFactory.ELEMENT_crosstab, getNamespace());
		writer.addAttribute(JRCrosstabFactory.ATTRIBUTE_isRepeatColumnHeaders, crosstab.isRepeatColumnHeaders(), true);
		writer.addAttribute(JRCrosstabFactory.ATTRIBUTE_isRepeatRowHeaders, crosstab.isRepeatRowHeaders(), true);
		writer.addAttribute(JRCrosstabFactory.ATTRIBUTE_columnBreakOffset, crosstab.getColumnBreakOffset(), JRCrosstab.DEFAULT_COLUMN_BREAK_OFFSET);
		writer.addAttribute(XmlConstants.ATTRIBUTE_runDirection, crosstab.getRunDirection(), JRXmlConstants.getRunDirectionMap(), JRCrosstab.RUN_DIRECTION_LTR);
		writer.addAttribute(JRCrosstabFactory.ATTRIBUTE_ignoreWidth, crosstab.getIgnoreWidth());

		writeReportElement(crosstab);

		JRCrosstabParameter[] parameters = crosstab.getParameters();
		if (parameters != null)
		{
			for (int i = 0; i < parameters.length; i++)
			{
				if (!parameters[i].isSystemDefined())
				{
					writeCrosstabParameter(parameters[i]);
				}
			}
		}

		writer.writeExpression(JRCrosstabFactory.ELEMENT_parametersMapExpression, crosstab.getParametersMapExpression(), false);

		writeCrosstabDataset(crosstab);

		writeCrosstabHeaderCell(crosstab);

		JRCrosstabRowGroup[] rowGroups = crosstab.getRowGroups();
		for (int i = 0; i < rowGroups.length; i++)
		{
			writeCrosstabRowGroup(rowGroups[i]);
		}

		JRCrosstabColumnGroup[] columnGroups = crosstab.getColumnGroups();
		for (int i = 0; i < columnGroups.length; i++)
		{
			writeCrosstabColumnGroup(columnGroups[i]);
		}

		JRCrosstabMeasure[] measures = crosstab.getMeasures();
		for (int i = 0; i < measures.length; i++)
		{
			writeCrosstabMeasure(measures[i]);
		}

		if (crosstab instanceof JRDesignCrosstab)
		{
			List cellsList = ((JRDesignCrosstab) crosstab).getCellsList();
			for (Iterator it = cellsList.iterator(); it.hasNext();)
			{
				JRCrosstabCell cell = (JRCrosstabCell) it.next();
				writeCrosstabCell(cell);
			}
		}
		else
		{
			JRCrosstabCell[][] cells = crosstab.getCells();
			Set cellsSet = new HashSet();
			for (int i = cells.length - 1; i >= 0 ; --i)
			{
				for (int j = cells[i].length - 1; j >= 0 ; --j)
				{
					JRCrosstabCell cell = cells[i][j];
					if (cell != null && cellsSet.add(cell))
					{
						writeCrosstabCell(cell);
					}
				}
			}
		}

		writeCrosstabWhenNoDataCell(crosstab);

		writer.closeElement();
	}


	private void writeCrosstabDataset(JRCrosstab crosstab) throws IOException
	{
		JRCrosstabDataset dataset = crosstab.getDataset();
		writer.startElement(JRCrosstabDatasetFactory.ELEMENT_crosstabDataset);
		writer.addAttribute(JRCrosstabDatasetFactory.ATTRIBUTE_isDataPreSorted, dataset.isDataPreSorted(), false);
		writeElementDataset(dataset);
		writer.closeElement(true);
	}


	private void writeCrosstabWhenNoDataCell(JRCrosstab crosstab) throws IOException
	{
		JRCellContents whenNoDataCell = crosstab.getWhenNoDataCell();
		if (whenNoDataCell != null)
		{
			writer.startElement(JRCrosstabFactory.ELEMENT_whenNoDataCell);
			writeCellContents(whenNoDataCell);
			writer.closeElement();
		}
	}


	private void writeCrosstabHeaderCell(JRCrosstab crosstab) throws IOException
	{
		JRCellContents headerCell = crosstab.getHeaderCell();
		if (headerCell != null)
		{
			writer.startElement(JRCrosstabFactory.ELEMENT_crosstabHeaderCell);
			writeCellContents(headerCell);
			writer.closeElement();
		}
	}


	protected void writeCrosstabRowGroup(JRCrosstabRowGroup group) throws IOException
	{
		writer.startElement(JRCrosstabRowGroupFactory.ELEMENT_rowGroup);
		writer.addEncodedAttribute(JRCrosstabGroupFactory.ATTRIBUTE_name, group.getName());
		writer.addAttribute(JRCrosstabRowGroupFactory.ATTRIBUTE_width, group.getWidth());
		writer.addAttribute(JRCrosstabGroupFactory.ATTRIBUTE_totalPosition, group.getTotalPosition(), JRXmlConstants.getCrosstabTotalPositionMap(), BucketDefinition.TOTAL_POSITION_NONE);
		writer.addAttribute(JRCrosstabRowGroupFactory.ATTRIBUTE_headerPosition, group.getPosition(), JRXmlConstants.getCrosstabRowPositionMap(), JRCellContents.POSITION_Y_TOP);

		writeBucket(group.getBucket());

		JRCellContents header = group.getHeader();
		writer.startElement(JRCrosstabRowGroupFactory.ELEMENT_crosstabRowHeader);
		writeCellContents(header);
		writer.closeElement();

		JRCellContents totalHeader = group.getTotalHeader();
		writer.startElement(JRCrosstabRowGroupFactory.ELEMENT_crosstabTotalRowHeader);
		writeCellContents(totalHeader);
		writer.closeElement();

		writer.closeElement();
	}


	protected void writeCrosstabColumnGroup(JRCrosstabColumnGroup group) throws IOException
	{
		writer.startElement(JRCrosstabGroupFactory.ELEMENT_columnGroup);
		writer.addEncodedAttribute(JRCrosstabGroupFactory.ATTRIBUTE_name, group.getName());
		writer.addAttribute(JRCrosstabColumnGroupFactory.ATTRIBUTE_height, group.getHeight());
		writer.addAttribute(JRCrosstabGroupFactory.ATTRIBUTE_totalPosition, group.getTotalPosition(), JRXmlConstants.getCrosstabTotalPositionMap(), BucketDefinition.TOTAL_POSITION_NONE);
		writer.addAttribute(JRCrosstabColumnGroupFactory.ATTRIBUTE_headerPosition, group.getPosition(), JRXmlConstants.getCrosstabColumnPositionMap(), JRCellContents.POSITION_X_LEFT);

		writeBucket(group.getBucket());

		JRCellContents header = group.getHeader();
		writer.startElement(JRCrosstabGroupFactory.ELEMENT_crosstabColumnHeader);
		writeCellContents(header);
		writer.closeElement();

		JRCellContents totalHeader = group.getTotalHeader();
		writer.startElement(JRCrosstabGroupFactory.ELEMENT_crosstabTotalColumnHeader);
		writeCellContents(totalHeader);
		writer.closeElement();

		writer.closeElement();
	}


	protected void writeBucket(JRCrosstabBucket bucket) throws IOException
	{
		writer.startElement(JRCrosstabBucketFactory.ELEMENT_bucket);
		writer.addAttribute(JRCrosstabBucketFactory.ATTRIBUTE_order, bucket.getOrder(), JRXmlConstants.getCrosstabBucketOrderMap(), BucketDefinition.ORDER_ASCENDING);
		writer.writeExpression(JRCrosstabBucketFactory.ELEMENT_bucketExpression, bucket.getExpression(), true);
		writer.writeExpression(JRCrosstabBucketFactory.ELEMENT_orderByExpression, 
				bucket.getOrderByExpression(), true, Object.class.getName());
		writer.writeExpression(JRCrosstabBucketFactory.ELEMENT_comparatorExpression, bucket.getComparatorExpression(), false);
		writer.closeElement();
	}


	protected void writeCrosstabMeasure(JRCrosstabMeasure measure) throws IOException
	{
		writer.startElement(JRCrosstabMeasureFactory.ELEMENT_measure);
		writer.addEncodedAttribute(JRCrosstabMeasureFactory.ATTRIBUTE_name, measure.getName());
		writer.addAttribute(JRCrosstabMeasureFactory.ATTRIBUTE_class, measure.getValueClassName());
		writer.addAttribute(JRCrosstabMeasureFactory.ATTRIBUTE_calculation, measure.getCalculation(), JRXmlConstants.getCalculationMap(), JRVariable.CALCULATION_NOTHING);
		writer.addAttribute(JRCrosstabMeasureFactory.ATTRIBUTE_percentageOf, measure.getPercentageOfType(), JRXmlConstants.getCrosstabPercentageMap(), JRCrosstabMeasure.PERCENTAGE_TYPE_NONE);
		writer.addAttribute(JRCrosstabMeasureFactory.ATTRIBUTE_percentageCalculatorClass, measure.getPercentageCalculatorClassName());
		writer.writeExpression(JRCrosstabMeasureFactory.ELEMENT_measureExpression, measure.getValueExpression(), false);
		writer.closeElement();
	}


	protected void writeCrosstabCell(JRCrosstabCell cell) throws IOException
	{
		writer.startElement(JRCrosstabCellFactory.ELEMENT_crosstabCell);
		writer.addAttribute(JRCrosstabCellFactory.ATTRIBUTE_width, cell.getWidth());
		writer.addAttribute(JRCrosstabCellFactory.ATTRIBUTE_height, cell.getHeight());
		writer.addEncodedAttribute(JRCrosstabCellFactory.ATTRIBUTE_rowTotalGroup, cell.getRowTotalGroup());
		writer.addEncodedAttribute(JRCrosstabCellFactory.ATTRIBUTE_columnTotalGroup, cell.getColumnTotalGroup());

		writeCellContents(cell.getContents());

		writer.closeElement();
	}


	protected void writeCellContents(JRCellContents contents) throws IOException
	{
		if (contents != null)
		{
			writer.startElement(JRCellContentsFactory.ELEMENT_cellContents);
			writer.addAttribute(JRCellContentsFactory.ATTRIBUTE_backcolor, contents.getBackcolor());
			writer.addAttribute(JRCellContentsFactory.ATTRIBUTE_mode, contents.getModeValue());
			writeStyleReferenceAttr(contents);

			writeBox(contents.getLineBox());

			writeChildElements(contents);

			writer.closeElement();
		}
	}


	protected void writeCrosstabParameter(JRCrosstabParameter parameter) throws IOException
	{
		writer.startElement(JRCrosstabParameterFactory.ELEMENT_crosstabParameter);
		writer.addEncodedAttribute(JRCrosstabParameterFactory.ATTRIBUTE_name, parameter.getName());
		writer.addAttribute(JRCrosstabParameterFactory.ATTRIBUTE_class, parameter.getValueClassName(), "java.lang.String");
		writer.writeExpression(JRCrosstabParameterFactory.ELEMENT_parameterValueExpression, parameter.getExpression(), false);
		writer.closeElement();
	}


	public void writeDataset(JRDataset dataset) throws IOException
	{
		writer.startElement(XmlConstants.ELEMENT_subDataset, getNamespace());
		writer.addEncodedAttribute(XmlConstants.ATTRIBUTE_name, dataset.getName());
		writer.addAttribute(XmlConstants.ATTRIBUTE_scriptletClass, dataset.getScriptletClass());
		writer.addEncodedAttribute(XmlConstants.ATTRIBUTE_resourceBundle, dataset.getResourceBundle());
		writer.addAttribute(XmlConstants.ATTRIBUTE_whenResourceMissingType, dataset.getWhenResourceMissingType(), JRXmlConstants.getWhenResourceMissingTypeMap(), JRReport.WHEN_RESOURCE_MISSING_TYPE_NULL);

		writeProperties(dataset);

		writeDatasetContents(dataset);

		writer.closeElement();
	}

	protected void writeDatasetContents(JRDataset dataset) throws IOException
	{
		/*   */
		JRScriptlet[] scriptlets = dataset.getScriptlets();
		if (scriptlets != null && scriptlets.length > 0)
		{
			for(int i = 0; i < scriptlets.length; i++)
			{
				writeScriptlet(scriptlets[i]);
			}
		}

		/*   */
		JRParameter[] parameters = dataset.getParameters();
		if (parameters != null && parameters.length > 0)
		{
			for(int i = 0; i < parameters.length; i++)
			{
				if (!parameters[i].isSystemDefined())
				{
					writeParameter(parameters[i]);
				}
			}
		}

		/*   */
		if(dataset.getQuery() != null)
		{
			writeQuery(dataset.getQuery());
		}

		/*   */
		JRField[] fields = dataset.getFields();
		if (fields != null && fields.length > 0)
		{
			for(int i = 0; i < fields.length; i++)
			{
				writeField(fields[i]);
			}
		}

		/*   */
		JRSortField[] sortFields = dataset.getSortFields();
		if (sortFields != null && sortFields.length > 0)
		{
			for(int i = 0; i < sortFields.length; i++)
			{
				writeSortField(sortFields[i]);
			}
		}

		/*   */
		JRVariable[] variables = dataset.getVariables();
		if (variables != null && variables.length > 0)
		{
			for(int i = 0; i < variables.length; i++)
			{
				if (!variables[i].isSystemDefined())
				{
					writeVariable(variables[i]);
				}
			}
		}

		writer.writeExpression(XmlConstants.ELEMENT_filterExpression, dataset.getFilterExpression(), false);

		/*   */
		JRGroup[] groups = dataset.getGroups();
		if (groups != null && groups.length > 0)
		{
			for(int i = 0; i < groups.length; i++)
			{
				writeGroup(groups[i]);
			}
		}
	}


	/**
	 * Outputs the XML representation of a subdataset run object.
	 * 
	 * @param datasetRun the subdataset run
	 * @throws IOException
	 */
	public void writeDatasetRun(JRDatasetRun datasetRun) throws IOException
	{
		writer.startElement(XmlConstants.ELEMENT_datasetRun, getNamespace());
		writer.addEncodedAttribute(XmlConstants.ATTRIBUTE_subDataset, datasetRun.getDatasetName());

		writer.writeExpression(XmlConstants.ELEMENT_parametersMapExpression, datasetRun.getParametersMapExpression(), false);

		/*   */
		JRDatasetParameter[] parameters = datasetRun.getParameters();
		if (parameters != null && parameters.length > 0)
		{
			for(int i = 0; i < parameters.length; i++)
			{
				writeDatasetParameter(parameters[i]);
			}
		}

		writer.writeExpression(XmlConstants.ELEMENT_connectionExpression, datasetRun.getConnectionExpression(), false);
		writer.writeExpression(XmlConstants.ELEMENT_dataSourceExpression, datasetRun.getDataSourceExpression(), false);

		writer.closeElement();
	}


	public void writeFrame(JRFrame frame) throws IOException
	{
		writer.startElement(XmlConstants.ELEMENT_frame, getNamespace());

		writeReportElement(frame);
		writeBox(frame.getLineBox());

		writeChildElements(frame);

		writer.closeElement();
	}


	protected void writeHyperlinkParameters(JRHyperlinkParameter[] parameters) throws IOException
	{
		if (parameters != null)
		{
			for (int i = 0; i < parameters.length; i++)
			{
				JRHyperlinkParameter parameter = parameters[i];
				writeHyperlinkParameter(parameter);
			}
		}
	}


	protected void writeHyperlinkParameter(JRHyperlinkParameter parameter) throws IOException
	{
		if (parameter != null)
		{
			writer.startElement(XmlConstants.ELEMENT_hyperlinkParameter, JASPERREPORTS_NAMESPACE);
			writer.addEncodedAttribute(XmlConstants.ATTRIBUTE_name, parameter.getName());

			writer.writeExpression(XmlConstants.ELEMENT_hyperlinkParameterExpression,
					parameter.getValueExpression(), true, String.class.getName());

			writer.closeElement();
		}
	}

	public void writeHyperlink(String tagName, JRHyperlink hyperlink) throws IOException
	{
		writeHyperlink(tagName, null, hyperlink);
	}

	public void writeHyperlink(String tagName, XmlNamespace namespace, 
			JRHyperlink hyperlink) throws IOException
	{
		if (hyperlink != null)
		{
			writer.startElement(tagName, namespace);

			writer.addEncodedAttribute(XmlConstants.ATTRIBUTE_hyperlinkType, hyperlink.getLinkType(), JRHyperlinkHelper.HYPERLINK_TYPE_NONE);
			writer.addEncodedAttribute(XmlConstants.ATTRIBUTE_hyperlinkTarget, hyperlink.getLinkTarget(), JRHyperlinkHelper.HYPERLINK_TARGET_SELF);

			writer.writeExpression(XmlConstants.ELEMENT_hyperlinkReferenceExpression, JASPERREPORTS_NAMESPACE,
					hyperlink.getHyperlinkReferenceExpression(), false);
			writer.writeExpression(XmlConstants.ELEMENT_hyperlinkAnchorExpression, JASPERREPORTS_NAMESPACE,
					hyperlink.getHyperlinkAnchorExpression(), false);
			writer.writeExpression(XmlConstants.ELEMENT_hyperlinkPageExpression, JASPERREPORTS_NAMESPACE,
					hyperlink.getHyperlinkPageExpression(), false);
			writer.writeExpression(XmlConstants.ELEMENT_hyperlinkTooltipExpression, JASPERREPORTS_NAMESPACE,
					hyperlink.getHyperlinkTooltipExpression(), false);
			writeHyperlinkParameters(hyperlink.getHyperlinkParameters());

			writer.closeElement(true);
		}
	}


	protected boolean toWriteConditionalStyles()
	{
		return true;
	}

	/**
	 * Returns the XML write helper used by this report writer.
	 * 
	 * The helper can be used to output XML elements and attributes.
	 * 
	 * @return the XML write helper used by this report writer
	 */
	public JRXmlWriteHelper getXmlWriteHelper()
	{
		return writer;
	}

	/**
	 * Returns the underlying stream to which this writer outputs to.
	 * 
	 * @return the underlying stream used by this writer
	 */
	public Writer getUnderlyingWriter()
	{
		return writer.getUnderlyingWriter();
	}

	public void writeComponentElement(JRComponentElement componentElement) throws IOException
	{
		writer.startElement(XmlConstants.ELEMENT_componentElement, getNamespace());
		writeReportElement(componentElement);
		
		ComponentKey componentKey = componentElement.getComponentKey();
		Component component = componentElement.getComponent();
		ComponentXmlWriter componentXmlWriter = ComponentsEnvironment.
			getComponentManager(componentKey).getComponentXmlWriter();
		componentXmlWriter.writeToXml(componentKey, component, this);
		
		writer.closeElement();
	}
	
	protected XmlNamespace getNamespace()
	{
		return JASPERREPORTS_NAMESPACE;
	}


	public void writeGenericElement(JRGenericElement element) throws IOException
	{
		writer.startElement(XmlConstants.ELEMENT_genericElement, getNamespace());
		
		writer.addAttribute(XmlConstants.ATTRIBUTE_evaluationTime, 
				element.getEvaluationTime(), JRXmlConstants.getEvaluationTimeMap(),
				JRExpression.EVALUATION_TIME_NOW);
		if (element.getEvaluationGroupName() != null)
		{
			writer.addEncodedAttribute(XmlConstants.ATTRIBUTE_evaluationGroup, 
					element.getEvaluationGroupName());
		}

		writeReportElement(element);
		
		JRGenericElementType printKey = element.getGenericType();
		writer.startElement(XmlConstants.ELEMENT_genericElementType);
		writer.addEncodedAttribute(XmlConstants.ATTRIBUTE_namespace, 
				printKey.getNamespace());
		writer.addEncodedAttribute(XmlConstants.ATTRIBUTE_name, 
				printKey.getName());
		writer.closeElement();//genericElementType

		JRGenericElementParameter[] params = element.getParameters();
		for (int i = 0; i < params.length; i++)
		{
			JRGenericElementParameter param = params[i];
			writer.startElement(XmlConstants.ELEMENT_genericElementParameter);
			writer.addEncodedAttribute(XmlConstants.ATTRIBUTE_name, 
					param.getName());
			writer.addAttribute(XmlConstants.ATTRIBUTE_skipWhenNull, 
					param.isSkipWhenEmpty(), false);
			
			JRExpression valueExpression = param.getValueExpression();
			if (valueExpression != null)
			{
				writer.writeExpression(XmlConstants.ELEMENT_genericElementParameter_valueExpression, 
						valueExpression, true, Object.class.getName());
			}
			
			writer.closeElement();//genericElementParameter
		}
		
		writer.closeElement();//genericElement
	}
}
