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
package net.sf.jasperreports.engine.util;

import java.awt.Color;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.HashSet;
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
import net.sf.jasperreports.charts.design.JRDesignGanttDataset;
import net.sf.jasperreports.charts.util.JRMeterInterval;
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
import net.sf.jasperreports.engine.JRAnchor;
import net.sf.jasperreports.engine.JRBand;
import net.sf.jasperreports.engine.JRBreak;
import net.sf.jasperreports.engine.JRChart;
import net.sf.jasperreports.engine.JRChartDataset;
import net.sf.jasperreports.engine.JRChartPlot;
import net.sf.jasperreports.engine.JRChild;
import net.sf.jasperreports.engine.JRComponentElement;
import net.sf.jasperreports.engine.JRConditionalStyle;
import net.sf.jasperreports.engine.JRDataset;
import net.sf.jasperreports.engine.JRDatasetParameter;
import net.sf.jasperreports.engine.JRDatasetRun;
import net.sf.jasperreports.engine.JRElement;
import net.sf.jasperreports.engine.JRElementDataset;
import net.sf.jasperreports.engine.JRElementGroup;
import net.sf.jasperreports.engine.JREllipse;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRExpressionChunk;
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
import net.sf.jasperreports.engine.JRLineBox;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JRPen;
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
import net.sf.jasperreports.engine.JRStyleContainer;
import net.sf.jasperreports.engine.JRSubreport;
import net.sf.jasperreports.engine.JRSubreportParameter;
import net.sf.jasperreports.engine.JRSubreportReturnValue;
import net.sf.jasperreports.engine.JRTextElement;
import net.sf.jasperreports.engine.JRTextField;
import net.sf.jasperreports.engine.JRVariable;
import net.sf.jasperreports.engine.component.Component;
import net.sf.jasperreports.engine.component.ComponentKey;
import net.sf.jasperreports.engine.design.JRDesignFrame;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.query.JRJdbcQueryExecuterFactory;
import net.sf.jasperreports.engine.xml.JRXmlWriter;

import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.time.Day;

/**
 * A writer that generates the Java code required to produce a given report template programmatically, using the JasperReports API.
 * 
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id: JRXmlWriter.java 3211 2009-11-23 17:34:54Z teodord $
 */
public class JRApiWriter
{

	/**
	 *
	 */
	private JRReport report;

	/**
	 *
	 */
	private Map stylesMap = new HashMap();

	/**
	 *
	 */
	private Map groupsMap = new HashMap();

	
	private Writer writer;
	
	private ApiWriterVisitor apiWriterVisitor = new ApiWriterVisitor(this);

	//TODO: remove this one below
	JasperDesign jasperDesign = null;

	private String indent;
	
	/**
	 *
	 */
	protected JRApiWriter(JRReport report)
	{
		this.report = report;
	}


	/**
	 *
	 */
	public static String writeReport(JRReport report)
	{
		JRApiWriter writer = new JRApiWriter(report);
		StringWriter buffer = new StringWriter();//FIXME use file buffered writer
		writer.writeReport(buffer);
		return buffer.toString();
	}


	/**
	 *
	 */
	public static void writeReport(
		JRReport report,
		String destFileName
		) throws JRException
	{
		FileOutputStream fos = null;

		try
		{
			fos = new FileOutputStream(destFileName);
			String encoding = report.getProperty(JRExporterParameter.PROPERTY_CHARACTER_ENCODING) != null
			? report.getProperty(JRExporterParameter.PROPERTY_CHARACTER_ENCODING)
			: "UTF-8";//FIXME this is an export time config property
			Writer out = new OutputStreamWriter(fos, encoding);
			JRApiWriter writer = new JRApiWriter(report);
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
		OutputStream outputStream
		) throws JRException
	{
		try
		{
			String encoding = report.getProperty(JRExporterParameter.PROPERTY_CHARACTER_ENCODING) != null
			? report.getProperty(JRExporterParameter.PROPERTY_CHARACTER_ENCODING)
			: "UTF-8";
			
			Writer out = new OutputStreamWriter(outputStream, encoding);
			JRApiWriter writer = new JRApiWriter(report);
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
	protected void writeReport(Writer aWriter)
	{
		this.writer = aWriter;
		indent = "";

		write1("/*\n");
		write1(" * Generated by JasperReports - ");
		write1((new SimpleDateFormat()).format(new java.util.Date()));
		write1("\n");
		write1(" */\n");
		write1("import java.awt.Color;\n");
		write1("import java.io.IOException;\n");
		write1("import java.util.HashMap;\n");
		write1("import java.util.HashSet;\n");
		write1("import java.util.Iterator;\n");
		write1("import java.util.List;\n");
		write1("import java.util.Map;\n");
		write1("import java.util.Set;\n");
		write1("import java.util.SortedSet;\n");
		write1("import java.util.TreeSet;\n");
		write1("\n");
		write1("import org.jfree.chart.plot.PlotOrientation;\n");
		write1("\n");
		write1("import net.sf.jasperreports.charts.JRCategoryAxisFormat;\n");
		write1("import net.sf.jasperreports.charts.design.*;\n");
		write1("import net.sf.jasperreports.charts.util.JRAxisFormat;\n");
		write1("import net.sf.jasperreports.crosstabs.design.*;\n");
		write1("import net.sf.jasperreports.engine.JRException;\n");
		write1("import net.sf.jasperreports.engine.design.*;\n");
		write1("import net.sf.jasperreports.engine.util.ReportCreator;\n");
		write1("\n");
		
		
		/*   */
		String[] imports = report.getImports();
		if (imports != null && imports.length > 0)
		{
			for(int i = 0; i < imports.length; i++)
			{
				String value = imports[i];
				if (value != null)
				{
					write("import {0};\n", value);
				}
			}
		}
		write1("\n");
		write1("\n");
		write1("public class " + JRStringUtil.escapeJavaStringLiteral(report.getName()) + " implements ReportCreator");
		write1("{\n");
		write1("\n");
		indent += "  ";
		write1( "public JasperDesign create() throws JRException\n");
		write1( "{\n");
		indent += "  ";
		write1( "JasperDesign jasperDesign = new JasperDesign();\n");
		write1( "jasperDesign.setName(\"" + report.getName() + "\");\n");
		write( "jasperDesign.setLanguage(\"{0}\");\n", report.getLanguage());
		write( "jasperDesign.setColumnCount({0});\n", report.getColumnCount(), 1);
		write( "jasperDesign.setPrintOrder((byte){0});\n", report.getPrintOrder(), JRReport.PRINT_ORDER_VERTICAL);
		write( "jasperDesign.setPageWidth({0});\n", report.getPageWidth());
		write( "jasperDesign.setPageHeight({0});\n", report.getPageHeight());
		write( "jasperDesign.setOrientation((byte){0});\n", report.getOrientation(), JRReport.ORIENTATION_PORTRAIT);
		write( "jasperDesign.setWhenNoDataType((byte){0});\n", report.getWhenNoDataType(), JRReport.WHEN_NO_DATA_TYPE_NO_PAGES);
		write( "jasperDesign.setColumnWidth({0});\n", report.getColumnWidth());
		write( "jasperDesign.setColumnSpacing({0});\n", report.getColumnSpacing());
		write( "jasperDesign.setLeftMargin({0});\n", report.getLeftMargin());
		write( "jasperDesign.setRightMargin({0});\n", report.getRightMargin());
		write( "jasperDesign.setTopMargin({0});\n", report.getTopMargin());
		write( "jasperDesign.setBottomMargin({0});\n", report.getBottomMargin());
		write( "jasperDesign.setTitleNewPage({0});\n", report.isTitleNewPage(), false);
		write( "jasperDesign.setSummaryNewPage({0});\n", report.isSummaryNewPage(), false);
		write( "jasperDesign.setSummaryWithPageHeaderAndFooter({0});\n", report.isSummaryWithPageHeaderAndFooter(), false);
		write( "jasperDesign.setFloatColumnFooter({0});\n", report.isFloatColumnFooter(), false);
		write( "jasperDesign.setScriptletClass(\"{0}\");\n", report.getScriptletClass());
		write( "jasperDesign.setFormatFactoryClass(\"{0}\");\n", report.getFormatFactoryClass());
		write( "jasperDesign.setgetResourceBundle(\"{0}\");\n", JRStringUtil.escapeJavaStringLiteral(report.getResourceBundle()));
		write( "jasperDesign.setWhenResourceMissingType((byte){0});\n", report.getWhenResourceMissingType(), JRReport.WHEN_RESOURCE_MISSING_TYPE_NULL);
		write( "jasperDesign.setIgnorePagination({0});\n\n", report.isIgnorePagination(), false);

		writeProperties( report, "jasperDesign");
		write("\n");
		writeTemplates(indent);

		write("\n");
		
		JRReportFont[] fonts = report.getFonts();
		if (fonts != null && fonts.length > 0)
		{	
			write( "//report fonts\n\n");
			for(int i = 0; i < fonts.length; i++)
			{
				writeReportFont( fonts[i], "reportFontStyle"+i);
				write( "jasperDesign.addStyle(reportFontStyle" + i + ");\n\n");
				flush();
			}
		}

		JRStyle[] styles = report.getStyles();
		if (styles != null && styles.length > 0)
		{	
			write( "//styles\n");

			for(int i = 0; i < styles.length; i++)
			{
				writeStyle( styles[i], "reportStyle" + i);
				write( "jasperDesign.addStyle(reportStyle" + i + ");\n\n");

				if (toWriteConditionalStyles())
				{
					JRConditionalStyle[] conditionalStyles = styles[i].getConditionalStyles();
					if (!(styles[i] instanceof JRConditionalStyle) && conditionalStyles != null)
					{
						for (int j = 0; j < conditionalStyles.length; j++)
						{
							String conditionalStyleName = "reportStyle" + i + "Conditional" + j;
							writeConditionalStyle( conditionalStyles[j],conditionalStyleName);
							write( "reportStyle" + i + ".addConditionalStyle(" + conditionalStyleName + ");\n\n");
						}
						flush();
					}
				}
			}
			flush();
		}
		
		JRDataset[] datasets = report.getDatasets();
		if (datasets != null && datasets.length > 0)
		{
			write( "//datasets\n");
			for (int i = 0; i < datasets.length; ++i)
			{
				writeDataset( datasets[i], "reportDataset" + i);
				if(datasets[i] != null)
					write( "jasperDesign.addDataset(reportDataset" + i + ");\n");
			}
			write("\n");
			flush();
		}

		if(report.getMainDataset() != null)
		{
			writeDataset( report.getMainDataset(), "reportMainDataset");
			write( "jasperDesign.setMainDataset(reportMainDataset);\n");
		}

		if (report.getBackground() != null)
		{
			write( "//background\n\n");
			writeBand( report.getBackground(), "backgroundBand");
			write( "jasperDesign.setBackground(backgroundBand);\n\n");
		}

		if (report.getTitle() != null)
		{
			write( "//title\n\n");
			writeBand( report.getTitle(), "titleBand");
			write( "jasperDesign.setTitle(titleBand);\n\n");
		}

		if (report.getPageHeader() != null)
		{
			write( "//page header\n\n");
			writeBand( report.getPageHeader(), "pageHeaderBand");
			write( "jasperDesign.setPageHeader(pageHeaderBand);\n\n");
		}

		if (report.getColumnHeader() != null)
		{
			write( "//column header\n\n");
			writeBand( report.getColumnHeader(), "columnHeaderBand");
			write( "jasperDesign.setColumnHeader(columnHeaderBand);\n\n");
		}

		JRSection detail = report.getDetailSection();
		if (detail != null && detail.getBands() != null && detail.getBands().length > 0)
		{
			writeSection(
					detail, 
					"detailBand",
					indent + "((JRDesignSection)jasperDesign.getDetailSection()).getBandsList()"
					);
		}

		if (report.getColumnFooter() != null)
		{
			write( "//column footer\n\n");
			writeBand( report.getColumnFooter(), "columnFooterBand");
			write( "jasperDesign.setColumnFooter(columnFooterBand);\n\n");
		}

		if (report.getPageFooter() != null)
		{
			write( "//page footer\n\n");
			writeBand( report.getPageFooter(), "pageFooterBand");
			write( "jasperDesign.setPageFooter(pageFooterBand);\n\n");
		}

		if (report.getLastPageFooter() != null)
		{
			write( "//last page footer\n\n");
			writeBand( report.getLastPageFooter(), "lastPageFooterBand");
			write( "jasperDesign.setLastPageFooter(lastPageFooterBand);\n\n");
		}

		if (report.getSummary() != null)
		{
			write( "//summary\n\n");
			writeBand( report.getSummary(), "summaryBand");
			write( "jasperDesign.setSummary(summaryBand);\n\n");
		}

		if (report.getNoData() != null)
		{
			write( "//no data\n\n");
			writeBand( report.getNoData(), "noDataBand");
			write( "jasperDesign.setNoData(noDataBand);\n\n");
		}

		write( "return jasperDesign;\n");
		write( "}\n\n");
		indent = ""; 
		write("}\n");
		
		flush();//FIXME is this necessary?
		close();
	}


	/**
	 * 
	 *
	 */
	private void writeProperties( JRPropertiesHolder propertiesHolder, String propertiesHolderName)
	{
		if (propertiesHolder.hasProperties())
		{
			JRPropertiesMap propertiesMap = propertiesHolder.getPropertiesMap();
			String[] propertyNames = propertiesMap.getPropertyNames();
			if (propertyNames != null && propertyNames.length > 0)
			{
				write( "//properties\n");
				for(int i = 0; i < propertyNames.length; i++)
				{
					String value = propertiesMap.getProperty(propertyNames[i]);
					if (value != null)
					{
						write( propertiesHolderName + ".setProperty(\"" + propertyNames[i] + "\", \"" + JRStringUtil.escapeJavaStringLiteral(value) + "\");\n");
					}
				}
				write("\n");
			}
			flush();
		}
	}



	/**
	 * 
	 */
	protected void writeTemplates(String indent)
	{
		JRReportTemplate[] templates = report.getTemplates();
		if (templates != null)
		{
			for (int i = 0; i < templates.length; i++)
			{
				JRReportTemplate template = templates[i];
				writeTemplate( template, "reportTemplate" + i);
				write( "jasperDesign.addTemplate(reportTemplate" + i + ");\n");
			}
			write("\n");
			flush();
		}
	}


	/**
	 * 
	 */
	protected void writeTemplate( JRReportTemplate template, String templateName)
	{
		write( "JRDesignReportTemplate " + templateName + " = new JRDesignReportTemplate();\n");
		writeExpression( template.getSourceExpression(), templateName, "SourceExpression", String.class.getName());
		flush();
	}


	/**
	 *
	 */
	private void writeReportFont( JRReportFont font, String styleName)
	{
		if (font != null && stylesMap.get(font.getName()) == null)
		{
			write( styleName + ".setName(\"" + JRStringUtil.escapeJavaStringLiteral(font.getName()) + "\");\n");
			write( styleName + ".setDefault(" + font.isDefault() + ");\n");
			write( styleName + ".setFontName(\"" + JRStringUtil.escapeJavaStringLiteral(font.getFontName()) + "\");\n");
			write( styleName + ".setFontSize(" + font.getFontSize() + ");\n");
			write( styleName + ".setBold({0});\n", font.isOwnBold());
			write( styleName + ".setItalic(" + font.isItalic() + ");\n");
			write( styleName + ".setUnderline(" + font.isUnderline() + ");\n");
			write( styleName + ".setStrikeThrough(" + font.isStrikeThrough() + ");\n");
			write( styleName + ".setPdfFontName(\"" + JRStringUtil.escapeJavaStringLiteral(font.getPdfFontName()) + "\");\n");
			write( styleName + ".setPdfEncoding(\"" + font.getPdfEncoding() + "\");\n");
			write( styleName + ".setPdfEmbedded(" + font.isPdfEmbedded() + ");\n");
			stylesMap.put(font.getName(), styleName);
			flush();
		}
	}


	/**
	 *
	 */
	private void writeScriptlet( JRScriptlet scriptlet, String scriptletName)
	{
		if(scriptlet != null)
		{
			write( "JRDesignScriptlet " + scriptletName + " = new JRDesignScriptlet();\n");
			write( scriptletName + ".setDescription(\"" + JRStringUtil.escapeJavaStringLiteral(scriptlet.getDescription()) + "\");\n");
			write( scriptletName + ".setName(\"" + JRStringUtil.escapeJavaStringLiteral(scriptlet.getName()) + "\");\n");
			if(scriptlet.getValueClass() != null)
				write( scriptletName + ".setValueClass(Class.forName(\"" + JRStringUtil.escapeJavaStringLiteral(scriptlet.getValueClass().getName()) + "\"));\n");
			else if(scriptlet.getValueClassName() != null)
				write( scriptletName + ".setValueClassName(\"" + JRStringUtil.escapeJavaStringLiteral(scriptlet.getValueClassName()) + "\");\n");
	
			writeProperties( scriptlet, scriptletName);
			flush();
		}
	}


	/**
	 *
	 */
	private void writeParameter( JRParameter parameter,  String parameterName)
	{
		if(parameter != null)
		{
			write( "JRDesignParameter " + parameterName + " = new JRDesignParameter();\n");
			write( parameterName + ".setName(\"" + JRStringUtil.escapeJavaStringLiteral(parameter.getName()) + "\");\n");
			write( parameterName + ".setDescription(\"" + JRStringUtil.escapeJavaStringLiteral(parameter.getDescription()) + "\");\n");
			
			if(parameter.getValueClass() != null)
				write( parameterName + ".setValueClass(Class.forName(\"" + JRStringUtil.escapeJavaStringLiteral(parameter.getValueClass().getName()) + "\"));\n");
			else if(parameter.getValueClassName() != null)
				write( parameterName + ".setValueClassName(\"" + JRStringUtil.escapeJavaStringLiteral(parameter.getValueClassName()) + "\");\n");
			
			if(parameter.getNestedType() != null)
				write( parameterName + ".setNestedType(Class.forName(\"" + JRStringUtil.escapeJavaStringLiteral(parameter.getNestedType().getName()) + "\"));\n");
			else if(parameter.getNestedTypeName() != null)
				write( parameterName + ".setNestedTypeName(\"" + JRStringUtil.escapeJavaStringLiteral(parameter.getNestedTypeName()) + "\");\n");
			
			write( parameterName + ".setForPrompting(" + parameter.isForPrompting() + ");\n");
	
			writeProperties( parameter, parameterName);
	
			writeExpression( parameter.getDefaultValueExpression(), parameterName, "DefaultValueExpression");
			flush();
		}
	}


	/**
	 *
	 */
	private void writeQuery( JRQuery query, String queryName)
	{
		if(query != null)
		{
			write( "JRDesignQuery " + queryName + " = new JRDesignQuery();\n");
			write( queryName + ".setLanguage(\"" + JRStringUtil.escapeJavaStringLiteral(query.getLanguage() != null ? query.getLanguage() : JRJdbcQueryExecuterFactory.QUERY_LANGUAGE_SQL) + "\");\n");
			write( queryName + ".setText(\"" + JRStringUtil.escapeJavaStringLiteral(query.getText()) + "\");\n");
			flush();
		}
	}


	/**
	 *
	 */
	private void writeField( JRField field, String fieldName)
	{
		if(field != null)
		{
			write( "JRDesignField " + fieldName + " = new JRDesignField();\n");
			write( fieldName + ".setName(\"" + JRStringUtil.escapeJavaStringLiteral(field.getName()) + "\");\n");
			write( fieldName + ".setDescription(\"" + JRStringUtil.escapeJavaStringLiteral(field.getDescription()) + "\");\n");
			if(field.getValueClass() != null)
				write( fieldName + ".setValueClass(Class.forName(\"" + JRStringUtil.escapeJavaStringLiteral(field.getValueClass().getName()) + "\"));\n");
			else if(field.getValueClassName() != null)
				write( fieldName + ".setValueClassName(\"" + JRStringUtil.escapeJavaStringLiteral(field.getValueClassName()) + "\");\n");
			writeProperties( field, fieldName);
			flush();
		}
	}


	/**
	 *
	 */
	private void writeSortField( JRSortField sortField, String sortFieldName)
	{
		if(sortField != null)
		{
			write( "JRDesignSortField " + sortFieldName + " = new JRDesignSortField();\n");
			write( sortFieldName + ".setName(\"" + JRStringUtil.escapeJavaStringLiteral(sortField.getName()) + "\");\n");
			write( sortFieldName + ".setOrder((byte)" + sortField.getOrder() + ");\n");
			flush();
		}
	}


	/**
	 *
	 */
	private void writeVariable( JRVariable variable, String variableName)
	{
		if(variable != null)
		{
			String resetGroupName = getGroupName( variable.getResetGroup());
			String incrementGroupName = getGroupName( variable.getIncrementGroup());
			
			write( "JRDesignVariable " + variableName + " = new JRDesignVariable();\n");
			write( variableName + ".setName(\"" + JRStringUtil.escapeJavaStringLiteral(variable.getName()) + "\");\n");
			if(variable.getValueClass() != null)
				write( variableName + ".setValueClass(Class.forName(\"" + JRStringUtil.escapeJavaStringLiteral(variable.getValueClass().getName()) + "\"));\n");
			else if(variable.getValueClassName() != null)
				write( variableName + ".setValueClassName(\"" + JRStringUtil.escapeJavaStringLiteral(variable.getValueClassName()) + "\");\n");
			write( variableName + ".setResetType((byte)" + (variable.getResetType() < 1 ?  JRVariable.RESET_TYPE_REPORT : variable.getResetType()) + ");\n");
			if (resetGroupName != null)
			{
				write( variableName + ".setResetGroup(" + resetGroupName + ");\n");
				
			}
			write( variableName + ".setIncrementType((byte)" + (variable.getIncrementType() < 1 ?  JRVariable.RESET_TYPE_NONE : variable.getIncrementType()) + ");\n");
			if (incrementGroupName != null)
			{
				write( variableName + ".setResetGroup(" + incrementGroupName + ");\n");
			}

			write( variableName + ".setCalculation(" + (variable.getCalculation() < 0 ?  JRVariable.CALCULATION_NOTHING : variable.getCalculation()) + ");\n");
			write( variableName + ".setIncrementerFactoryClass(Class.forName(\"" + JRStringUtil.escapeJavaStringLiteral(variable.getIncrementerFactoryClassName()) + "\"));\n");
			writeExpression( variable.getExpression(), variableName, "Expression");
			writeExpression( variable.getInitialValueExpression(), variableName, "InitialValueExpression");
			flush();
		}
	}


	/**
	 *
	 */
	private void writeGroup( JRGroup group)
	{
		write( "JRDesignGroup " + group.getName() + "ResetGroup = new JRDesignGroup();\n");
		String groupName = group.getName();
		write( groupName + ".setName(\"" + JRStringUtil.escapeJavaStringLiteral(groupName) + "\");\n");
		write( groupName + ".setStartNewColumn(" + group.isStartNewColumn() + ");\n");
		write( groupName + ".setStartNewPage(" + group.isStartNewPage() + ");\n");
		write( groupName + ".setReprintHeaderOnEachPage(" + group.isReprintHeaderOnEachPage() + ");\n");
		write( groupName + ".setMinHeightToStartNewPage(" + group.getMinHeightToStartNewPage() + ");\n");
		write( groupName + ".setFooterPosition(" + (group.getFooterPosition() < 1 ? JRGroup.FOOTER_POSITION_NORMAL : group.getFooterPosition()) + ");\n");
		write( groupName + ".setKeepTogether(" + group.isKeepTogether() + ");\n");

		writeExpression( group.getExpression(), groupName, "Expression");

		JRSection groupHeader = group.getGroupHeaderSection();
		if (groupHeader != null)
		{
			writeSection(
					groupHeader, 
					groupName+"Header", 
					indent + "((JRDesignSection)" + groupName + ".getGroupHeaderSection()).getBandsList()"
					);
		}

		JRSection groupFooter = group.getGroupFooterSection();
		if (groupFooter != null)
		{
			writeSection(
					groupFooter, 
					groupName+"Footer", 
					indent + "((JRDesignSection)" + groupName + ".getGroupFooterSection()).getBandsList()"
					);
		}

		flush();
	}


	/**
	 *
	 */
	protected void writeSection( JRSection section, String sectionName, String sectionBandListGetterName)
	{
		if (section != null)
		{
			JRBand[] bands = section.getBands();
			if (bands != null && bands.length > 0)
			{
				write( "//" + sectionName + "\n\n");
				for(int i = 0; i < bands.length; i++)
				{
					writeBand( bands[i], sectionName + i);
					write( sectionBandListGetterName + ".add(" + i + ", " + sectionName + i + ");\n\n");
				}
			}
			flush();
		}
	}


	/**
	 *
	 */
	private void writeBand( JRBand band, String bandName)
	{
		if(band != null)
		{
			
			write( "//band name = " + bandName +"\n\n");
			write( "JRDesignBand " + bandName + " = new JRDesignBand();\n");
			write( bandName + ".setHeight(" + band.getHeight() + ");\n");
			write( bandName + ".setSplitType(Byte.valueOf(" + band.getSplitType() + "));\n");
			writeExpression( band.getPrintWhenExpression(), bandName, "PrintWhenExpression");

			writeChildElements( band, bandName);
	
			flush();
		}
	}

	
	/**
	 * Writes the contents (child elements) of an element container.
	 * 
	 * @param elementContainer the element container
	 */
	public void writeChildElements( JRElementGroup elementContainer, String parentName)
	{
		List children = elementContainer.getChildren();
		if (children != null && children.size() > 0)
		{
			for(int i = 0; i < children.size(); i++)
			{
				apiWriterVisitor.setName(parentName + i);
				((JRChild) children.get(i)).visit(apiWriterVisitor);
				write( parentName +".addElement(" + parentName + i + ");\n\n");
			}
		}
	}

	/**
	 *
	 */
	public void writeElementGroup( JRElementGroup elementGroup, String groupName)
	{ 
		if(elementGroup != null)
		{
			write( "JRDesignElementGroup " + groupName + " = new JRDesignElementGroup();\n");
			writeChildElements( elementGroup, groupName);
	
			flush();
		}
	}

	/**
	 *
	 */
	public void writeBreak( JRBreak breakElement, String breakName)
	{
		if(breakElement != null)
		{
			write( "JRDesignBreak " + breakName + " = new JRDesignBreak(jasperDesign);\n");
			write( breakName + ".setType((byte)" + (breakElement.getType() > 0 ? breakElement.getType() : JRBreak.TYPE_PAGE) + ");\n");
			writeReportElement( breakElement, breakName);
			flush();
		}
	}


	/**
	 *
	 */
	public void writeLine( JRLine line, String lineName)
	{
		if(line != null)
		{
			write( "JRDesignLine " + lineName + " = new JRDesignLine(jasperDesign);\n");
			write( lineName + ".setDirection((byte)" + (line.getDirection() > 0 ? line.getDirection() : JRLine.DIRECTION_TOP_DOWN) + ");\n");
			writeReportElement( line, lineName);
			writeGraphicElement( line, lineName);
			flush();
		}
	}


	/**
	 *
	 */
	private void writeReportElement( JRElement element, String elementName)
	{
		if(element != null)
		{
			if(element.getKey() != null)
				write( elementName + ".setKey(\"" + JRStringUtil.escapeJavaStringLiteral(element.getKey()) + "\");\n");
			writeStyleReferenceAttr( element, elementName);
			write( elementName + ".setPositionType((byte)" + (element.getPositionType() > 0 ? element.getPositionType() : JRElement.POSITION_TYPE_FIX_RELATIVE_TO_TOP) + ");\n");
			write( elementName + ".setStretchType((byte)" + (element.getStretchType() > -1 ? element.getStretchType() : JRElement.STRETCH_TYPE_NO_STRETCH) + ");\n");
			write( elementName + ".setPrintRepeatedValues(" + element.isPrintRepeatedValues() + ");\n");
			write( elementName + ".setMode((byte)" + element.getMode() + ");\n");
			write( elementName + ".setX(" + element.getX() + ");\n");
			write( elementName + ".setY(" + element.getY() + ");\n");
			write( elementName + ".setWidth(" + element.getWidth() + ");\n");
			write( elementName + ".setHeight(" + element.getHeight() + ");\n");
			write( elementName + ".setRemoveLineWhenBlank(" + element.isRemoveLineWhenBlank() + ");\n");
			write( elementName + ".setPrintInFirstWholeBand(" + element.isPrintInFirstWholeBand() + ");\n");
			write( elementName + ".setPrintWhenDetailOverflows(" + element.isPrintWhenDetailOverflows() + ");\n");

			if (element.getPrintWhenGroupChanges() != null)
			{
				String groupName = getGroupName( element.getPrintWhenGroupChanges());
				write( elementName + ".setPrintWhenGroupChanges(" + groupName + ");\n");
			}
			
			write( elementName + ".setForecolor(" + getColorText(element.getForecolor()) + ");\n");
			write( elementName + ".setBackcolor(" + getColorText(element.getBackcolor()) + ");\n");
	
			writeProperties( element, elementName + ".getPropertiesMap()");
			writePropertyExpressions( element.getPropertyExpressions(), elementName);
			writeExpression( element.getPrintWhenExpression(), elementName, "PrintWhenExpression");
			flush();
		}
	}


	/**
	 * 
	 */
	protected void writePropertyExpressions( JRPropertyExpression[] propertyExpressions, String propertyHolderName)
	{
		if (propertyExpressions != null && propertyExpressions.length > 0)
		{
			for (int i = 0; i < propertyExpressions.length; i++)
			{
				writePropertyExpression( propertyExpressions[i], propertyHolderName + "PropertyExpression" + i);
				write( propertyHolderName + ".addPropertyExpression(" + propertyHolderName + "PropertyExpression" + i +");\n");
			}
			flush();
		}
	}


	/**
	 * 
	 */
	protected void writePropertyExpression( JRPropertyExpression propertyExpression, String propertyExpressionName)
	{
		if(propertyExpression != null)
		{
			write( "JRDesignPropertyExpression " + propertyExpressionName + " = new JRDesignPropertyExpression();\n");
			write( propertyExpressionName + ".setName(\"" + JRStringUtil.escapeJavaStringLiteral(propertyExpression.getName()) + "\");\n");
			writeExpression( propertyExpression.getValueExpression(), propertyExpressionName, "ValueExpression");
			
			flush();
		}
	}


	/**
	 *
	 */
	private void writeGraphicElement( JRGraphicElement element, String elementName)
	{
		if(element != null)
		{
			write( elementName + ".setFill((byte)" + element.getFill() + ");\n");
			writePen( element.getLinePen(), elementName+".getLinePen()");
			flush();
		}
	}


	/**
	 *
	 */
	public void writeRectangle( JRRectangle rectangle, String rectangleName)
	{
		if(rectangle != null)
		{
			write( "JRDesignRectangle " + rectangleName + " = new JRDesignRectangle(jasperDesign);\n");
			write( rectangleName + ".setRadius(" + rectangle.getRadius() + ");\n");
			writeReportElement( rectangle, rectangleName);
			writeGraphicElement( rectangle, rectangleName);
			flush();
		}
	}


	/**
	 *
	 */
	public void writeEllipse( JREllipse ellipse, String ellipseName)
	{
		if(ellipse != null)
		{
			write( "JRDesignEllipse " + ellipseName + " = new JRDesignEllipse(jasperDesign);\n");
			writeReportElement( ellipse, ellipseName);
			writeGraphicElement( ellipse, ellipseName);
			flush();
		}
	}


	/**
	 *
	 */
	public void writeImage( JRImage image, String imageName)
	{
		if(image != null)
		{
			write( "JRDesignImage " + imageName + " = new JRDesignImage(jasperDesign);\n");
			write( imageName + ".setScaleImage(" + image.getScaleImage() + ");\n");
			write( imageName + ".setHorizontalAlignment((byte)" + image.getHorizontalAlignment() + ");\n");
			write( imageName + ".setVerticalAlignment((byte)" + image.getVerticalAlignment() + ");\n");
			write( imageName + ".setUsingCache(" + image.isUsingCache() + ");\n");
			write( imageName + ".setLazy(" + image.isLazy() + ");\n");
			write( imageName + ".setOnErrorType((byte)" + (image.getOnErrorType() > 0 ? image.getOnErrorType() : JRImage.ON_ERROR_TYPE_ERROR) + ");\n");
			write( imageName + ".setEvaluationTime((byte)" + (image.getEvaluationTime() > 0 ? image.getEvaluationTime() : JRExpression.EVALUATION_TIME_NOW) + ");\n");
			if (image.getEvaluationGroup() != null)
			{
				String groupName = getGroupName( image.getEvaluationGroup());
				write( imageName + ".setEvaluationGroup(" + groupName + ");\n");
			}
			write( imageName + ".setLinkType(\"" + JRStringUtil.escapeJavaStringLiteral(image.getLinkType() != null ? image.getLinkType() : JRHyperlinkHelper.HYPERLINK_TYPE_NONE) + "\");\n");
			write( imageName + ".setLinkTarget(\"" + JRStringUtil.escapeJavaStringLiteral(image.getLinkTarget() != null ? image.getLinkTarget() : JRHyperlinkHelper.HYPERLINK_TARGET_SELF) + "\");\n");
			write( imageName + ".setBookmarkLevel(" + (image.getBookmarkLevel() > 0 ? image.getBookmarkLevel() : JRAnchor.NO_BOOKMARK) + ");\n");
			
			writeReportElement( image, imageName);
			writeBox( image.getLineBox(), imageName + ".getLineBox()");
			writeGraphicElement( image, imageName);
			
			writeExpression( image.getExpression(), imageName, "Expression");
			writeExpression( image.getAnchorNameExpression(), imageName, "AnchorNameExpression");
			writeExpression( image.getHyperlinkReferenceExpression(), imageName, "HyperlinkReferenceExpression");
			writeExpression( image.getHyperlinkAnchorExpression(), imageName, "HyperlinkAnchorExpression");
			writeExpression( image.getHyperlinkPageExpression(), imageName, "HyperlinkPageExpression");
			writeExpression( image.getHyperlinkTooltipExpression(), imageName, "HyperlinkTooltipExpression");
			
			writeHyperlinkParameters( image.getHyperlinkParameters(), imageName);
			flush();
		}
	}


	/**
	 *
	 */
	public void writeStaticText( JRStaticText staticText, String staticTextName)
	{
		if(staticText != null)
		{
			write( "JRDesignStaticText " + staticTextName + " = new JRDesignStaticText(jasperDesign);\n");
			writeReportElement( staticText, staticTextName);
			writeBox( staticText.getLineBox(), staticTextName + ".getLineBox()");
			writeTextElement( staticText, staticTextName);
			write( staticTextName + ".setText(\"" + JRStringUtil.escapeJavaStringLiteral(staticText.getText()) + "\");\n");
			flush();
		}
	}


	/**
	 *
	 */
	private void writeTextElement( JRTextElement textElement, String textElementName)
	{
		if(textElement != null)
		{
			write( textElementName + ".setHorizontalAlignment((byte)" + textElement.getHorizontalAlignment() + ");\n");
			write( textElementName + ".setVerticalAlignment((byte)" + textElement.getVerticalAlignment() + ");\n");
			write( textElementName + ".setRotation((byte)" + textElement.getRotation() + ");\n");
			write( textElementName + ".setLineSpacing((byte)" + textElement.getLineSpacing() + ");\n");
			write( textElementName + ".setMarkup(\"" + JRStringUtil.escapeJavaStringLiteral(textElement.getMarkup()) + "\");\n");
			writeFont( textElement, textElementName);
			flush();
		}
	}


	/**
	 *
	 */
	private void writeFont( JRFont font, String fontHolderName)
	{
		if (font != null)
		{
			String reportFontStyle = null;
			
			if (font.getReportFont() != null)
			{
				reportFontStyle =	(String)stylesMap.get(font.getReportFont().getName());
				if(reportFontStyle != null)
				{
					write( fontHolderName + ".setStyle(" + reportFontStyle + ");\n");
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
			
			write( fontHolderName + ".setFontName(\"" + JRStringUtil.escapeJavaStringLiteral(font.getFontName()) + "\");\n");
			write( fontHolderName + ".setFontSize(" + font.getFontSize() + ");\n");
			write( fontHolderName + ".setBold(" + font.isBold() + ");\n");
			write( fontHolderName + ".setItalic(" + font.isItalic() + ");\n");
			write( fontHolderName + ".setUnderline(" + font.isUnderline() + ");\n");
			write( fontHolderName + ".setStrikeThrough(" + font.isStrikeThrough() + ");\n");
			write( fontHolderName + ".setPdfFontName(\"" + JRStringUtil.escapeJavaStringLiteral(font.getPdfFontName()) + "\");\n");
			write( fontHolderName + ".setPdfEncoding(\"" + JRStringUtil.escapeJavaStringLiteral(font.getPdfEncoding()) + "\");\n");
			write( fontHolderName + ".setPdfEmbedded(" + font.isPdfEmbedded() + ");\n");
			flush();
		}
	}

	/**
	 *
	 */
	private void writeStyle( JRStyle style, String styleName)
	{
		if (style != null && stylesMap.get(style.getName()) == null)
		{
			write( "JRDesignStyle " + styleName + " = new JRDesignStyle();\n");
			write( styleName + ".setName(\"" + JRStringUtil.escapeJavaStringLiteral(style.getName()) + "\");\n");
			writeStyleReferenceAttr( style, styleName);
			write( styleName + ".setDefault(" + style.isDefault() + ");\n");
			if(style.getMode() != null)
				write( styleName + ".setMode((byte)" + style.getMode().byteValue() + ");\n");
			
			write( styleName + ".setFontName(\"" + JRStringUtil.escapeJavaStringLiteral(style.getFontName()) + "\");\n");
			write( styleName + ".setFontSize(" + style.getFontSize() + ");\n");
			write( styleName + ".setBold(" + style.isBold() + ");\n");
			write( styleName + ".setItalic(" + style.isItalic() + ");\n");
			write( styleName + ".setUnderline(" + style.isUnderline() + ");\n");
			write( styleName + ".setStrikeThrough(" + style.isStrikeThrough() + ");\n");
			write( styleName + ".setPdfFontName(\"" + JRStringUtil.escapeJavaStringLiteral(style.getPdfFontName()) + "\");\n");
			write( styleName + ".setPdfEncoding(\"" + JRStringUtil.escapeJavaStringLiteral(style.getPdfEncoding()) + "\");\n");
			write( styleName + ".setPdfEmbedded(" + style.isPdfEmbedded() + ");\n");
			write( styleName + ".setForecolor(" + getColorText(style.getForecolor()) + ");\n");
			write( styleName + ".setBackcolor(" + getColorText(style.getBackcolor()) + ");\n");
			write( styleName + ".setFill((byte)" + style.getFill() + ");\n");
			write( styleName + ".setRadius(" + style.getRadius() + ");\n");
			write( styleName + ".setScaleImage((byte)" + style.getScaleImage() + ");\n");
			write( styleName + ".setHorizontalAlignment((byte)" + style.getHorizontalAlignment() + ");\n");
			write( styleName + ".setVerticalAlignment((byte)" + style.getVerticalAlignment() + ");\n");
			write( styleName + ".setRotation((byte)" + style.getRotation() + ");\n");
			write( styleName + ".setLineSpacing((byte)" + style.getLineSpacing() + ");\n");
			write( styleName + ".setMarkup(\"" + JRStringUtil.escapeJavaStringLiteral(style.getMarkup()) + "\");\n");
			if(style.getPattern() != null)
				write( styleName + ".setPattern(\"" + JRStringUtil.escapeJavaStringLiteral(style.getPattern()) + "\");\n");
			write( styleName + ".setBlankWhenNull(" + style.isBlankWhenNull() + ");\n");
//			writePen( style.getLinePen(), styleName + ".getLinePen()");
//			writeBox( style.getLineBox(),styleName + ".getLineBox()");
			stylesMap.put(style.getName(), styleName);
			flush();
		}
	}


	 /**
	 *
	 */
	public void writeTextField( JRTextField textField, String textFieldName)
	{
		if(textField != null)
		{
			write( "JRDesignTextField " + textFieldName + " = new JRDesignTextField(jasperDesign);\n");
			write( textFieldName + ".setStretchWithOverflow(" + textField.isStretchWithOverflow() + ");\n");
			write( textFieldName + ".setEvaluationTime((byte)" + (textField.getEvaluationTime() > 0 ? textField.getEvaluationTime() : JRExpression.EVALUATION_TIME_NOW) + ");\n");
			String evaluationGroupName = getGroupName( textField.getEvaluationGroup());
			if (evaluationGroupName != null)
			{
				write( textFieldName + ".setEvaluationGroup(" + evaluationGroupName + ");\n");
			}
	
			write( textFieldName + ".setPattern(\"" + JRStringUtil.escapeJavaStringLiteral(textField.getPattern()) + "\");\n");
			write( textFieldName + ".setBlankWhenNull(" + textField.isBlankWhenNull() + ");\n");
			write( textFieldName + ".setLinkType(\"" + JRStringUtil.escapeJavaStringLiteral(textField.getLinkType() != null ? textField.getLinkType() : JRHyperlinkHelper.HYPERLINK_TYPE_NONE) + "\");\n");
			write( textFieldName + ".setLinkTarget(\"" + JRStringUtil.escapeJavaStringLiteral(textField.getLinkTarget() != null ? textField.getLinkTarget() : JRHyperlinkHelper.HYPERLINK_TARGET_SELF) + "\");\n");
			write( textFieldName + ".setBookmarkLevel(" + (textField.getBookmarkLevel() > 0 ? textField.getBookmarkLevel() : JRAnchor.NO_BOOKMARK) + ");\n");
			
			writeReportElement( textField, textFieldName);
			writeBox( textField.getLineBox(), textFieldName + ".getLineBox");
			writeTextElement( textField, textFieldName);
	
			writeExpression( textField.getExpression(), textFieldName, "Expression");
	
			writeExpression( textField.getAnchorNameExpression(), textFieldName, "AnchorNameExpression");
	
			writeExpression( textField.getHyperlinkReferenceExpression(), textFieldName, "HyperlinkReferenceExpression");
	
			writeExpression( textField.getHyperlinkAnchorExpression(), textFieldName, "HyperlinkAnchorExpression");
	
			writeExpression( textField.getHyperlinkPageExpression(), textFieldName, "HyperlinkPageExpression");
	
			writeExpression( textField.getHyperlinkTooltipExpression(), textFieldName, "HyperlinkTooltipExpression");
	
			writeHyperlinkParameters( textField.getHyperlinkParameters(), textFieldName);
	
			flush();
		}
	}


	/**
	 *
	 */
	public void writeSubreport( JRSubreport subreport, String subreportName)
	{
		if(subreport != null)
		{
			write( "JRDesignSubreport " + subreportName + " = new JRDesignSubreport(jasperDesign);\n");
			write( subreportName + ".setUsingCache(" + subreport.isUsingCache() + ");\n");
			write( subreportName + ".setRunToBottom(" + subreport.isRunToBottom() + ");\n");
			writeReportElement( subreport, subreportName);
	
			writeExpression( subreport.getParametersMapExpression(), subreportName, "ParametersMapExpression");
	
			JRSubreportParameter[] parameters = subreport.getParameters();
			if (parameters != null && parameters.length > 0)
			{
				for(int i = 0; i < parameters.length; i++)
				{
					writeSubreportParameter( parameters[i], subreportName + "Parameter" + i);
					write( subreportName + ".addParameter(" + subreportName + "Parameter" + i + ");\n");
				}
			}

			writeExpression( subreport.getConnectionExpression(), subreportName, "ConnectionExpression");

			writeExpression( subreport.getDataSourceExpression(), subreportName, "DataSourceExpression");

			JRSubreportReturnValue[] returnValues = subreport.getReturnValues();
			if (returnValues != null && returnValues.length > 0)
			{
				for(int i = 0; i < returnValues.length; i++)
				{
					writeSubreportReturnValue( returnValues[i], subreportName + "ReturnValue" + i);
					write( subreportName + ".addReturnValue(" + subreportName + "ReturnValue" + i + ");\n");
				}
			}
	
			writeExpression( subreport.getExpression(), subreportName, "Expression");
	
			flush();
		}
	}


	/**
	 *
	 */
	private void writeSubreportParameter( JRSubreportParameter subreportParameter, String subreportParameterName)
	{
		if(subreportParameter != null)
		{
			write( "JRDesignSubreportParameter " + subreportParameterName + " = new JRDesignSubreportParameter();\n");
			write( subreportParameterName + ".setName(\"" + JRStringUtil.escapeJavaStringLiteral(subreportParameter.getName()) + "\");\n");
			writeExpression( subreportParameter.getExpression(), subreportParameterName, "Expression");
			flush();
		}
	}

	/**
	 *
	 */
	private void writeDatasetParameter( JRDatasetParameter datasetParameter, String datasetParameterName)
	{
		if(datasetParameter != null)
		{
			write( "JRDesignDatasetParameter " + datasetParameterName + " = new JRDesignSubreportParameter();\n");
			write( datasetParameterName + ".setName(\"" + JRStringUtil.escapeJavaStringLiteral(datasetParameter.getName()) + "\");\n");
			writeExpression( datasetParameter.getExpression(), datasetParameterName, "Expression");
			flush();
		}
	}
	
	/**
	 *
	 */
	private void writeChart( JRChart chart, String chartName)
	{
		if(chart != null)
		{
			write( chartName + ".setShowLegend(Boolean.valueOf(" + chart.getShowLegend() + "));\n");
			write( chartName + ".setEvaluationTime((byte)" + (chart.getEvaluationTime() > 0 ? chart.getEvaluationTime() : JRExpression.EVALUATION_TIME_NOW) + ");\n");

			
			if (chart.getEvaluationTime() == JRExpression.EVALUATION_TIME_GROUP)
			{
				String evaluationGroupName = getGroupName( chart.getEvaluationGroup());
				write( chartName + ".setEvaluationGroup(" + evaluationGroupName + ");\n");
			}
	
			write( chartName + ".setLinkType(\"" + JRStringUtil.escapeJavaStringLiteral(chart.getLinkType() != null ? chart.getLinkType() : JRHyperlinkHelper.HYPERLINK_TYPE_NONE) + "\");\n");
			write( chartName + ".setLinkTarget(\"" + JRStringUtil.escapeJavaStringLiteral(chart.getLinkTarget() != null ? chart.getLinkTarget() : JRHyperlinkHelper.HYPERLINK_TARGET_SELF) + "\");\n");
			write( chartName + ".setBookmarkLevel(" + (chart.getBookmarkLevel() > 0 ? chart.getBookmarkLevel() : JRAnchor.NO_BOOKMARK) + ");\n");

			if(chart.getCustomizerClass() != null)
			{
				write( chartName + ".setCustomizerClass(\"" + JRStringUtil.escapeJavaStringLiteral(chart.getCustomizerClass()) + "\");\n");
			}
			write( chartName + ".setRenderType(\"" + JRStringUtil.escapeJavaStringLiteral(chart.getRenderType()) + "\");\n");
			write( chartName + ".setTheme(\"" + JRStringUtil.escapeJavaStringLiteral(chart.getTheme()) + "\");\n");

			writeReportElement( chart, chartName);
			writeBox( chart.getLineBox(), chartName + ".getLineBox()");
	
			write( chartName + ".setTitlePosition(Byte.valueOf((byte)" + chart.getTitlePositionByte() + "));\n");
			if (chart.getSubtitleColor() != null)
				write( chartName + ".setTitleColor(" + getColorText(chart.getOwnTitleColor()) + ");\n");
			
			writeFont( chart.getTitleFont(), chartName + ".getTitleFont()");

			writeExpression( chart.getTitleExpression(), chartName, "TitleExpression");
	
			if (chart.getSubtitleColor() != null)
				write( chartName + ".setSubtitleColor(" + getColorText(chart.getOwnSubtitleColor()) + ");\n");
			
			writeFont( chart.getSubtitleFont(), chartName + ".getSubtitleFont()");

			writeExpression( chart.getSubtitleExpression(), chartName, "SubtitleExpression");
	
			if (chart.getOwnLegendColor() != null)
				write( chartName + ".setLegendColor(" + getColorText(chart.getOwnLegendColor()) + ");\n");
			if (chart.getOwnLegendBackgroundColor() != null)
				write( chartName + ".setLegendBackgroundColor(" + getColorText(chart.getOwnLegendBackgroundColor()) + ");\n");
			
			write( chartName + ".setLegendPosition(Byte.valueOf((byte)" + chart.getLegendPositionByte() + "));\n");

			writeFont( chart.getLegendFont(), chartName + ".getLegendFont()");
	
			writeExpression( chart.getAnchorNameExpression(), chartName, "AnchorNameExpression");
			writeExpression( chart.getHyperlinkReferenceExpression(), chartName, "HyperlinkReferenceExpression");
			writeExpression( chart.getHyperlinkAnchorExpression(), chartName, "HyperlinkAnchorExpression");
			writeExpression( chart.getHyperlinkPageExpression(), chartName, "HyperlinkPageExpression");
			writeExpression( chart.getHyperlinkTooltipExpression(), chartName, "HyperlinkTooltipExpression");
			writeHyperlinkParameters( chart.getHyperlinkParameters(), chartName);
	
			flush();
		}
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
	public void writeElementDataset( JRElementDataset dataset, String datasetName)
	{
		writeElementDataset( dataset, true, datasetName);
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
	public void writeElementDataset( JRElementDataset dataset, boolean skipIfEmpty, String datasetName)
	{
		if(dataset != null)
		{
			write( datasetName + ".setResetType((byte)" + (dataset.getResetType() > 0 ? dataset.getResetType() : JRVariable.RESET_TYPE_REPORT) + ");\n");
	
			if (dataset.getResetType() == JRVariable.RESET_TYPE_GROUP)
			{
				String resetGroupName = getGroupName(  dataset.getResetGroup());
				write( datasetName + ".setResetGroup(" + resetGroupName + ");\n");
			}
			write( datasetName + ".setIncrementType((byte)" + (dataset.getIncrementType() > 0 ? dataset.getIncrementType() : JRVariable.RESET_TYPE_NONE) + ");\n");
	
			if (dataset.getIncrementType() == JRVariable.RESET_TYPE_GROUP)
			{
				String incrementGroupName = getGroupName(  dataset.getIncrementGroup());
				write( datasetName + ".setIncrementGroup(" + incrementGroupName + ");\n");
			}
	
			writeExpression( dataset.getIncrementWhenExpression(), datasetName, "IncrementWhenExpression");
	
			JRDatasetRun datasetRun = dataset.getDatasetRun();
			if (datasetRun != null)
			{
				writeDatasetRun( datasetRun, datasetName);
			}
			flush();
		}
	}


	/**
	 *
	 */
	private void writeCategoryDataSet( JRCategoryDataset dataset, String parentName, String datasetNameSuffix)
	{
		if(dataset != null)
		{
			String datasetName = parentName + datasetNameSuffix;
			write( "JRDesignCategoryDataset " + datasetName + " = (JRDesignCategoryDataset)" + parentName + ".getDataset();\n");
	
			writeElementDataset( dataset, datasetName);
	
			JRCategorySeries[] categorySeries = dataset.getSeries();
			if (categorySeries != null && categorySeries.length > 0)
			{
				for(int i = 0; i < categorySeries.length; i++)
				{
					writeCategorySeries( categorySeries[i], datasetName, i);
				}
			}
	
			write( parentName + ".setDataset(" + datasetName + ");\n");
			flush();
		}
	}


	/**
	 * 
	 */
	private void writeTimeSeriesDataset( JRTimeSeriesDataset dataset, String parentName, String datasetNameSuffix)
	{
		if(dataset != null)
		{
			String datasetName = parentName + datasetNameSuffix;
			write( "JRDesignTimeSeriesDataset " + datasetName + " = (JRDesignTimeSeriesDataset)" + parentName + ".getDataset();\n");
			if (dataset.getTimePeriod() != null && !Day.class.getName().equals(dataset.getTimePeriod().getName()))
			{
				write( datasetName + ".setTimePeriod(Class.forName(\"" + dataset.getTimePeriod().getName() + "\"));\n");
			}
	
			writeElementDataset( dataset, datasetName);
	
			JRTimeSeries[] timeSeries = dataset.getSeries();
			if( timeSeries != null && timeSeries.length > 0 )
			{
				for( int i = 0; i < timeSeries.length; i++ )
			{
					writeTimeSeries( timeSeries[i], datasetName, i  );
				}
			}

			write( parentName + ".setDataset(" + datasetName + ");\n");
			flush();
		}
	}


	/**
	 * 
	 */
	private void writeGanttDataset( JRGanttDataset dataset, String parentName, String datasetNameSuffix)
	{
		if(dataset != null)
		{
			String datasetName = parentName + datasetNameSuffix;
			write( "JRDesignGanttDataset " + datasetName + " = (JRDesignGanttDataset)" + parentName + ".getDataset();\n");
			JRDesignGanttDataset d;
			
			writeElementDataset( dataset, datasetName);
	
			JRGanttSeries[] ganttSeries = dataset.getSeries();
			if (ganttSeries != null && ganttSeries.length > 0)
			{
				for(int i = 0; i < ganttSeries.length; i++)
				{
					writeGanttSeries( ganttSeries[i], datasetName, i);
				}
			}
			write( parentName + ".setDataset(" + datasetName + ");\n");
			flush();
		}
	}


	/**
	 * 
	 */
	private void writeTimePeriodDataset( JRTimePeriodDataset dataset, String parentName, String datasetNameSuffix)
	{
		if(dataset != null)
		{
			String datasetName = parentName + datasetNameSuffix;
			write( "JRDesignTimePeriodDataset " + datasetName + " = (JRDesignTimePeriodDataset)" + parentName + ".getDataset();\n");
			writeElementDataset( dataset, datasetName);
	
			JRTimePeriodSeries[] timePeriodSeries = dataset.getSeries();
			if( timePeriodSeries != null && timePeriodSeries.length > 0 )
			{
				for( int i = 0; i < timePeriodSeries.length; i++ )
				{
					writeTimePeriodSeries( timePeriodSeries[i], datasetName, i);
				}
			}
			write( parentName + ".setDataset(" + datasetName + ");\n");
			flush();
		}
	}


	/**
	 *
	 */
	private void writePieSeries( JRPieSeries pieSeries, String parentName, int index)
	{
		if(pieSeries != null)
		{
			String pieSeriesName = parentName + "PieSeries" + index;
			write( "JRDesignPieSeries " + pieSeriesName + " = " + parentName + ".getSeries()[" + index + "];\n");
	
			writeExpression( pieSeries.getKeyExpression(), pieSeriesName, "KeyExpression");
			writeExpression( pieSeries.getValueExpression(), pieSeriesName, "ValueExpression");
			writeExpression( pieSeries.getLabelExpression(), pieSeriesName, "LabelExpression");
			writeHyperlink( pieSeries.getSectionHyperlink(),pieSeriesName, "SectionHyperlink");
			write( parentName + ".addPieSeries(" + pieSeriesName + ");\n");
	
			flush();
		}
	}

	/**
	 *
	 */
	private void writeCategorySeries( JRCategorySeries categorySeries, String parentName, int index)
	{
		if(categorySeries != null)
		{
			String categorySeriesName = parentName + "CategorySeries" + index;
			//TODO: instantiate categorySeries
			write( "JRDesignCategorySeries " + categorySeriesName + " = " + parentName + ".getSeries()[" + index + "];\n");

			writeExpression( categorySeries.getSeriesExpression(), categorySeriesName, "SeriesExpression");
			writeExpression( categorySeries.getCategoryExpression(), categorySeriesName, "CategoryExpression");
			writeExpression( categorySeries.getValueExpression(), categorySeriesName, "ValueExpression");
			writeExpression( categorySeries.getLabelExpression(), categorySeriesName, "LabelExpression");
			writeHyperlink( categorySeries.getItemHyperlink(), categorySeriesName, "ItemHyperlink");
			write( parentName + ".addCategorySeries(" + categorySeriesName + ");\n");
			flush();
		}
	}

	/**
	 *
	 */
	private void writeXyzDataset( JRXyzDataset dataset, String parentName, String datasetNameSuffix)
	{
		if(dataset != null)
		{
			String datasetName = parentName + datasetNameSuffix;
			write( "JRDesignXyzDataset " + datasetName + " = (JRDesignXyzDataset)" + parentName + ".getDataset();\n");
	
			writeElementDataset( dataset, datasetName);
	
			JRXyzSeries[] series = dataset.getSeries();
			if( series != null && series.length > 0 )
			{
				for( int i = 0; i < series.length; i++ )
				{
					writeXyzSeries( series[i], datasetName, i);
				}
			}
			write( parentName + ".setDataset(" + datasetName + ");\n");
			flush();
		}
	}


	/**
	 *
	 */
	private void writeXyzSeries( JRXyzSeries series, String parentName, int index)
	{
		if(series != null)
		{
			String xyzSeriesName = parentName + "XyzSeries" + index;
			//TODO: instantiate xyzSeries
//			writer.startElement(JRApiConstants.ELEMENT_categorySeries);
			write( "JRDesignXyzSeries " + xyzSeriesName + " = " + parentName + ".getSeries()[" + index + "];\n");
	
			writeExpression( series.getSeriesExpression(), xyzSeriesName, "SeriesExpression");
			writeExpression( series.getXValueExpression(), xyzSeriesName, "XValueExpression");
			writeExpression( series.getYValueExpression(), xyzSeriesName, "YValueExpression");
			writeExpression( series.getZValueExpression(), xyzSeriesName, "ZValueExpression");
			writeHyperlink( series.getItemHyperlink(), xyzSeriesName, "ItemHyperlink");
			write( parentName + ".addXyzSeries(" + xyzSeriesName + ");\n");
			flush();
		}
	}


	/**
	 *
	 */
	private void writeXySeries( JRXySeries xySeries, String parentName, int index)
	{
		if(xySeries != null)
		{
			//TODO: instantiate xySeries
			String xySeriesName = parentName + "XySeries" + index;
			write( "JRDesignXySeries " + xySeriesName + " = " + parentName + ".getSeries()[" + index + "];\n");
	
			writeExpression( xySeries.getSeriesExpression(), xySeriesName, "SeriesExpression");
			writeExpression( xySeries.getXValueExpression(), xySeriesName, "XValueExpression");
			writeExpression( xySeries.getYValueExpression(), xySeriesName, "YValueExpression");
			writeExpression( xySeries.getLabelExpression(), xySeriesName, "LabelExpression");
			writeHyperlink( xySeries.getItemHyperlink(), xySeriesName, "ItemHyperlink");
			write( parentName + ".addXySeries(" + xySeriesName + ");\n");
			flush();
		}
	}


	/**
	 *
	 */
	private void writeXyDataset( JRXyDataset dataset, String parentName, String datasetNameSuffix)
	{
		if(dataset != null)
		{
			String datasetName = parentName + datasetNameSuffix;
			write( "JRDesignXyDataset " + datasetName + " = (JRDesignXyDataset)" + parentName + ".getDataset();\n");
	
			writeElementDataset( dataset, datasetName);
	
			JRXySeries[] xySeries = dataset.getSeries();
			if (xySeries != null && xySeries.length > 0)
			{
				for(int i = 0; i < xySeries.length; i++)
				{
					writeXySeries( xySeries[i], datasetName, i);
				}
			}
			write( parentName + ".setDataset(" + datasetName + ");\n");
			flush();
		}
	}

	/**
	 *
	 */
	private void writeTimeSeries( JRTimeSeries timeSeries, String parentName, int index)
	{
		if(timeSeries != null)
		{
			//TODO: instantiate categorySeries
			String timeSeriesName = parentName + "TimeSeries" + index;
			write( "JRDesignTimeSeries " + timeSeriesName + " = " + parentName + ".getSeries()[" + index + "];\n");
			writeExpression( timeSeries.getSeriesExpression(), timeSeriesName, "SeriesExpression");
			writeExpression( timeSeries.getTimePeriodExpression(), timeSeriesName, "TimePeriodExpression");
			writeExpression( timeSeries.getValueExpression(), timeSeriesName, "ValueExpression");
			writeExpression( timeSeries.getLabelExpression(), timeSeriesName, "LabelExpression");
			writeHyperlink( timeSeries.getItemHyperlink(), timeSeriesName, "ItemHyperlink");
			write( parentName + ".addTimeSeries(" + timeSeriesName + ");\n");
			flush();
		}
	}


	/**
	 * 
	 */
	private void writeGanttSeries( JRGanttSeries ganttSeries, String parentName, int index)
	{
		if(ganttSeries != null)
		{
			//TODO: instantiate ganttSeries
			String ganttSeriesName = parentName + "GanttSeries" + index;
			write( "JRDesignGanttSeries " + ganttSeriesName + " = " + parentName + ".getSeries()[" + index + "];\n");
			
			writeExpression( ganttSeries.getSeriesExpression(), ganttSeriesName, "SeriesExpression");
			writeExpression( ganttSeries.getTaskExpression(), ganttSeriesName, "TaskExpression");
			writeExpression( ganttSeries.getSubtaskExpression(), ganttSeriesName, "SubtaskExpression");
			writeExpression( ganttSeries.getStartDateExpression(), ganttSeriesName, "StartDateExpression");
			writeExpression( ganttSeries.getEndDateExpression(), ganttSeriesName, "EndDateExpression");
			writeExpression( ganttSeries.getPercentExpression(), ganttSeriesName, "PercentExpression");
			writeExpression( ganttSeries.getLabelExpression(), ganttSeriesName, "LabelExpression");
			writeHyperlink( ganttSeries.getItemHyperlink(), ganttSeriesName, "ItemHyperlink");
			write( parentName + ".addGanttSeries(" + ganttSeriesName + ");\n");
			flush();
		}
	}

	/**
	 * 
	 */
	private void writeTimePeriodSeries( JRTimePeriodSeries timePeriodSeries, String parentName, int index)
	{
		if(timePeriodSeries != null)
		{
			//TODO: instantiate timePeriodSeries
			String timePeriodSeriesName = parentName + "TimePeriodSeries" + index;
			write( "JRDesignTimePeriodSeries " + timePeriodSeriesName + " = " + parentName + ".getSeries()[" + index + "];\n");
			
			writeExpression( timePeriodSeries.getSeriesExpression(), timePeriodSeriesName, "SeriesExpression");
			writeExpression( timePeriodSeries.getStartDateExpression(), timePeriodSeriesName, "StartDateExpression");
			writeExpression( timePeriodSeries.getEndDateExpression(), timePeriodSeriesName, "EndDateExpression");
			writeExpression( timePeriodSeries.getValueExpression(), timePeriodSeriesName, "ValueExpression");
			writeExpression( timePeriodSeries.getLabelExpression(), timePeriodSeriesName, "LabelExpression");
			writeHyperlink( timePeriodSeries.getItemHyperlink(), timePeriodSeriesName, "ItemHyperlink");
			write( parentName + ".addTimePeriodSeries(" + timePeriodSeriesName + ");\n");
			flush();
		}
	}


	/**
	 *
	 */
	public void writePieDataset( JRPieDataset dataset, String parentName, String datasetNameSuffix)
	{
		if(dataset != null)
		{
			String datasetName = parentName + datasetNameSuffix;
			write( "JRDesignPieDataset " + datasetName + " = (JRDesignPieDataset)" + parentName + ".getDataset();\n");
			write( datasetName + ".setMaxCount(Integer.valueOf(" + dataset.getMaxCount() + "));\n");
			write( datasetName + ".setMinPercentage(Float.valueOf(" + dataset.getMinPercentage() + "f));\n");
	
			writeElementDataset( dataset, datasetName);
	
			JRPieSeries[] pieSeries = dataset.getSeries();
			if (pieSeries != null)
			{
				if (pieSeries.length > 1)
				{
					for(int i = 0; i < pieSeries.length; i++)
					{
						writePieSeries( pieSeries[i], datasetName, i);
					}
				}
				else
				{
					//preserve old syntax of single series pie datasets
					writePieSeries( pieSeries[0], datasetName, 0);
				}
			}
	
			writeExpression( dataset.getOtherKeyExpression(), datasetName, "OtherKeyExpression");
			writeExpression( dataset.getOtherLabelExpression(), datasetName, "OtherLabelExpression");
			writeHyperlink( dataset.getOtherSectionHyperlink(), datasetName, "OtherSectionHyperlink");
			write( parentName + ".setDataset(" + datasetName + ");\n");
			flush();
		}
	}

	/**
	 * Writes the description of a value dataset to the output stream.
	 * @param dataset the value dataset to persist
	 */
	public void writeValueDataset( JRValueDataset dataset, String parentName, String datasetNameSuffix)
	{
		if(dataset != null)
		{
			String datasetName = parentName + datasetNameSuffix;
			write( "JRDesignValueDataset " + datasetName + " = (JRDesignValueDataset)" +parentName + ".getDataset();\n");
			writeElementDataset( dataset, datasetName);
			writeExpression( dataset.getValueExpression(), datasetName, "ValueExpression");
			write( parentName + ".setDataset(" + datasetName + ");\n");
			flush();
		}
	}


	/**
	 * Writes the description of how to display a value in a valueDataset.
	 *
	 * @param valueDisplay the description to save
	 */
	public void writeValueDisplay( JRValueDisplay valueDisplay, String parentName)
	{
		if(valueDisplay != null)
		{
			String valueDisplayName = parentName + "ValueDisplay";
			//TODO: instantiate value display
			write( "JRDesignValueDataset " + valueDisplayName + " = " +parentName + ".getValueDisplay();\n");
			
			write( valueDisplayName + ".setColor(" + getColorText(valueDisplay.getColor()) + ");\n");
			if(valueDisplay.getMask() != null)
				write( valueDisplayName + ".setMask(\"" + valueDisplay.getMask() + "\");\n");
	
			writeFont( valueDisplay.getFont(), valueDisplayName + ".getFont()");
			write( parentName + "setValueDisplay(" + valueDisplayName + ");\n");
			
			flush();
		}
	}

	/**
	 * Writes the description of how to display item labels in a category plot.
	 *
	 * @param itemLabel the description to save
	 */
	public void writeItemLabel( JRItemLabel itemLabel, String parentName, String itemLabelSuffix)
	{
		if(itemLabel != null)
		{
			String itemLabelName = parentName + itemLabelSuffix;
			//TODO: instantiate itemLabel
			write( "JRDesignItemLabel " + itemLabelName + " = " + parentName + ".getItemLabel();\n");
			write( itemLabelName + ".setColor(" + getColorText(itemLabel.getColor()) + ");\n");
			write( itemLabelName + ".setBackgroundColor(" + getColorText(itemLabel.getBackgroundColor()) + ");\n");
			writeFont( itemLabel.getFont(), itemLabelName + ".getFont()");
			write( parentName + ".set" + itemLabelSuffix + "(" + itemLabelName + ");\n");
	
			flush();
		}
	}

	/**
	 * Writes a data range block to the output stream.
	 *
	 * @param dataRange the range to write
	 */
	public void writeDataRange( JRDataRange dataRange, String parentName, String dataRangeSuffix)
	{
		if(dataRange != null)
		{
			String dataRangeName = parentName + dataRangeSuffix;
			//TODO: instantiate dataRange
			write( "JRDesignDataRange " + dataRangeName + " = " + parentName + ".get" + dataRangeSuffix+ "();\n");
			writeExpression( dataRange.getLowExpression(), dataRangeName, "LowExpression");
			writeExpression( dataRange.getHighExpression(), dataRangeName, "HighExpression");
			write( parentName + ".set" + dataRangeSuffix + "(" + dataRangeName + ");\n");
			flush();
		}
	}


	/**
	 * Writes a meter interval description to the output stream.
	 *
	 * @param interval the interval to write
	 */
	private void writeMeterInterval( JRMeterInterval interval, String parentName, String meterIntervalName)
	{
		if(interval != null)
		{
			write( "JRMeterInterval " + meterIntervalName + " = new JRMeterInterval();\n");
			write( meterIntervalName + ".setLabel(\"" + JRStringUtil.escapeJavaStringLiteral(interval.getLabel()) + "\");\n");
			write( meterIntervalName + ".setBackgroundColor(" + getColorText(interval.getBackgroundColor()) + ");\n");
			write( meterIntervalName + ".setAlpha(Double.valueOf(" + interval.getAlphaDouble() + "));\n");
			writeDataRange( interval.getDataRange(), meterIntervalName, "DataRange");
			write( parentName + ".addInterval(" + meterIntervalName + ");\n");
			flush();
		}
	}

	/**
	 * Writes out the contents of a series colors block for a chart.  Assumes the caller
	 * has already written the <code>&lt;seriesColors&gt;</code> tag.
	 *
	 * @param seriesColors the colors to write
	 */
	private void writeSeriesColors( SortedSet seriesColors, String parentName)
	{
//		if (seriesColors == null || seriesColors.size() == 0)
//			return;
//		//TODO: instantiate series colors
//		JRSeriesColor[] colors = (JRSeriesColor[])seriesColors.toArray(new JRSeriesColor[0]);
//		for (int i = 0; i < colors.length; i++)
//		{
//			String seriesColorName = parentName + "SeriesColor" +i;
//			write( parentName + ".setSeriesColors(new TreeSet());\n");
//			write( "JRBaseChartPlot.JRBaseSeriesColor " + seriesColorName + " = new JRBaseChartPlot().JRBaseSeriesColor;\n");
//			//TODO: instantiate a SeriesColor
//			JRSeriesColor s; s.getSeriesOrder() 
//			write( seriesColorName + ".setPosition(Byte.valueOf((byte)" + chartAxis.getPositionByte() + "));\n");
//
//			writer.addAttribute(JRApiConstants.ATTRIBUTE_seriesOrder, colors[i].getSeriesOrder());
//			writer.addAttribute(JRApiConstants.ATTRIBUTE_color, colors[i].getColor());
//			JRDesignAreaPlot p;
//			p.addSeriesColor(seriesColor)
//			write( parentName + ".addSeriesColor(" + seriesColorName + ");\n");
//			flush();
//		}
	}

	/**
	 * Write the information about a the data and layout that make up one range axis in
	 * a multiple axis chart.
	 *
	 * @param chartAxis the axis being written
	 */
	private void writeChartAxis( JRChartAxis chartAxis, String parentName, String axisName)
	{
		if(chartAxis != null)
		{
			//TODO:instantiate
			write( "JRChartAxis " + axisName + " = new JRChartAxis();\n");
			write( axisName + ".setPosition(Byte.valueOf((byte)" + chartAxis.getPositionByte() + "));\n");
			// Let the nested chart describe itself
			writeChartTag( chartAxis.getChart(), axisName +"Chart");
			write( parentName + ".addAxis(" + axisName + ");\n");
			
			flush();
		}
	}

	/**
	 *
	 *
	 */
	private void writePlot( JRChartPlot plot, String plotName)
	{
		if(plot != null)
		{
			write( plotName + ".setBackcolor(" + getColorText(plot.getBackcolor()) + ");\n");
			String orientation = PlotOrientation.HORIZONTAL.equals(plot.getOrientation()) ? "PlotOrientation.HORIZONTAL" : "PlotOrientation.VERTICAL" ; 
			write( plotName + ".setOrientation(" + orientation + ");\n");
			write( plotName + ".setBackgroundAlpha(Float.valueOf(" + plot.getBackgroundAlphaFloat().floatValue() + "f));\n");
			write( plotName + ".setForegroundAlpha(Float.valueOf(" + plot.getForegroundAlphaFloat().floatValue() + "f));\n");
			writeSeriesColors( plot.getSeriesColors(), plotName);
	
			flush();
		}
	}


	/**
	 *
	 */
	public void writePieChart( JRChart chart, String chartName)
	{
		if(chart != null)
		{
			write( "JRDesignChart " + chartName + " = new JRDesignChart(jasperDesign, JRChart.CHART_TYPE_PIE);\n");
			writeChart( chart, chartName);
			writePieDataset( (JRPieDataset)chart.getDataset(), chartName, "PieDataset");
			// write plot
			JRPiePlot plot = (JRPiePlot) chart.getPlot();
			if(plot != null)
			{
				String plotName = chartName + "PiePlot";
				write( "JRDesignPiePlot " + plotName + " = (JRDesignPiePlot)" + chartName + ".getPlot();\n");
				write( plotName + ".setCircular(Boolean.valueOf(" + plot.getCircular() + "));\n");
				write( plotName + ".setLabelFormat(\"" + JRStringUtil.escapeJavaStringLiteral(plot.getLabelFormat()) + "\");\n");
				write( plotName + ".setLegendLabelFormat(\"" + JRStringUtil.escapeJavaStringLiteral(plot.getLegendLabelFormat()) + "\");\n");
				
				writePlot( plot, plotName);
				writeItemLabel( plot.getItemLabel(),plotName, "ItemLabel");
				flush();
			}
			flush();
		}
	}


	/**
	 *
	 */
	public void writePie3DChart( JRChart chart, String chartName)
	{
		if(chart != null)
		{
			write( "JRDesignChart " + chartName + " = new JRDesignChart(jasperDesign, JRChart.CHART_TYPE_PIE3D);\n");
			writeChart( chart, chartName);
			writePieDataset( (JRPieDataset)chart.getDataset(), chartName, "PieDataset");
			// write plot
			JRPie3DPlot plot = (JRPie3DPlot) chart.getPlot();
			if(plot != null)
			{
				String plotName = chartName + "Pie3DPlot";
				write( "JRDesignPie3DPlot " + plotName + " = (JRDesignPie3DPlot)" + chartName + ".getPlot();\n");
				write( plotName + ".setCircular(Boolean.valueOf(" + plot.getCircular() + "));\n");
				write( plotName + ".setLabelFormat(\"" + JRStringUtil.escapeJavaStringLiteral(plot.getLabelFormat()) + "\");\n");
				write( plotName + ".setLegendLabelFormat(\"" + JRStringUtil.escapeJavaStringLiteral(plot.getLegendLabelFormat()) + "\");\n");
				write( plotName + ".setDepthFactor(Double.valueOf(" + plot.getDepthFactorDouble() + "));\n");
				
				writePlot( plot, plotName);
				writeItemLabel( plot.getItemLabel(),plotName, "ItemLabel");
				flush();
			}
			flush();
		}
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
		String indent, 
		JRFont axisLabelFont, 
		Color axisLabelColor,
		JRFont axisTickLabelFont, 
		Color axisTickLabelColor,
		String axisTickLabelMask, 
		Boolean axisVerticalTickLabels, 
		Double labelRotation, 
		Color axisLineColor,
		String parentName,
		String axisNameSuffix
		) 
	{
		if (axisLabelFont == null && axisLabelColor == null &&
			axisTickLabelFont == null && axisTickLabelColor == null && axisLineColor == null)
			return;
		String axisName = parentName + axisNameSuffix;
		//TODO: instantiate categoryAxis
//		write( "JRCategoryAxisFormat " + axisName + " = new JRCategoryAxisFormat();\n");
		write( axisName + ".setCategoryAxisTickLabelRotation(Double.valueOf(" + labelRotation + "));\n");

		writeAxisFormat(
			indent,
			axisLabelFont, 
			axisLabelColor,
			axisTickLabelFont, 
			axisTickLabelColor,
			axisTickLabelMask, 
			axisVerticalTickLabels, 
			axisLineColor,
			parentName,
			axisNameSuffix,
			false
			);
		write( parentName + ".set" + axisNameSuffix + "(" + axisName + ");\n");
		
		flush();
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
		String indent,
		JRFont axisLabelFont, 
		Color axisLabelColor,
		JRFont axisTickLabelFont, 
		Color axisTickLabelColor,
		String axisTickLabelMask, 
		Boolean axisVerticalTickLabels, 
		Color axisLineColor,
		String parentName,
		String axisNameSuffix,
		boolean isToSet
		) 
	{
		if (axisLabelFont == null && axisLabelColor == null &&
				axisTickLabelFont == null && axisTickLabelColor == null && axisLineColor == null)
				return;
		String axisName = parentName + axisNameSuffix;
		if(isToSet)
			write( "JRAxisFormat " + axisName + " = new JRAxisFormat();\n");
		
		write( axisName + ".setLabelColor(" + getColorText(axisLabelColor) + ");\n");
		write( axisName + ".setTickLabelColor(" + getColorText(axisTickLabelColor) + ");\n");
		write( axisName + ".setLineColor(" + getColorText(axisLineColor) + ");\n");
		write( axisName + ".setTickLabelMask(\"" + JRStringUtil.escapeJavaStringLiteral(axisTickLabelMask)  + "\");\n");
		write( axisName + ".setVerticalTickLabel(Boolean.valueOf(" + axisVerticalTickLabels.booleanValue() + "));\n");
		
		if (axisLabelFont != null)
		{
			writeFont( axisLabelFont, axisName + ".getLabelFont()");
		}

		if (axisTickLabelFont != null)
		{
			writeFont( axisTickLabelFont, axisName + ".getTickLabelFont()");
		}
		if(isToSet)
			write( parentName + ".set" + axisNameSuffix + "(" + axisName + ");\n");

		flush();
	}

	/**
	 *
	 */
	private void writeBarPlot( JRBarPlot plot, String chartName)
	{
		if(plot != null)
		{
			String plotName = chartName + "BarPlot";
			write( "JRDesignBarPlot " + plotName + " = (JRDesignBarPlot)" + chartName + ".getPlot();\n");
			write( plotName + ".setShowLabels(Boolean.valueOf(" + plot.getShowLabels() + "));\n");
			write( plotName + ".setShowTickLabels(Boolean.valueOf(" + plot.getShowTickLabels() + "));\n");
			write( plotName + ".setShowTickMarks(Boolean.valueOf(" + plot.getShowTickMarks() + "));\n");
			writePlot( plot, plotName);
			
			writeItemLabel( plot.getItemLabel(), plotName, "ItemLabel");
			
			writeExpression( plot.getCategoryAxisLabelExpression(), plotName, "CategoryAxisLabelExpression");
			writeCategoryAxisFormat(
					indent,
					plot.getCategoryAxisLabelFont(), plot.getOwnCategoryAxisLabelColor(),
					plot.getCategoryAxisTickLabelFont(), plot.getOwnCategoryAxisTickLabelColor(),
					plot.getCategoryAxisTickLabelMask(), plot.getCategoryAxisVerticalTickLabels(), 
					plot.getCategoryAxisTickLabelRotation(), plot.getOwnCategoryAxisLineColor(),
					plotName, "CategoryAxisFormat"
					);
			
			writeExpression( plot.getValueAxisLabelExpression(), plotName, "ValueAxisLabelExpression");
			writeAxisFormat(
					indent, 
					plot.getValueAxisLabelFont(), plot.getOwnValueAxisLabelColor(),
					plot.getValueAxisTickLabelFont(), plot.getOwnValueAxisTickLabelColor(),
					plot.getValueAxisTickLabelMask(), plot.getValueAxisVerticalTickLabels(), 
					plot.getOwnValueAxisLineColor(),
					plotName, "ValueAxisFormat", true
					);
			writeExpression( plot.getDomainAxisMinValueExpression(), plotName, "DomainAxisMinValueExpression");
			writeExpression( plot.getDomainAxisMaxValueExpression(), plotName, "DomainAxisMaxValueExpression");
			writeExpression( plot.getRangeAxisMinValueExpression(), plotName, "RangeAxisMinValueExpression");
			writeExpression( plot.getRangeAxisMaxValueExpression(), plotName, "RangeAxisMaxValueExpression");
	
			flush();
		}
		
	}


	/**
	 *
	 */
	private void writeBubblePlot( JRBubblePlot plot, String chartName)
	{
		if(plot != null)
		{
			String plotName = chartName + "BubblePlot";
			write( "JRDesignBubblePlot " + plotName + " = (JRDesignBubblePlot)" + chartName + ".getPlot();\n");
			write( plotName + ".setScaleType(" + plot.getScaleTypeInteger() + ");\n");
			writePlot( plot, plotName);
			
			writeExpression( plot.getXAxisLabelExpression(), plotName, "XAxisLabelExpression");
			writeAxisFormat(
					indent, plot.getXAxisLabelFont(), plot.getOwnXAxisLabelColor(),
					plot.getXAxisTickLabelFont(), plot.getOwnXAxisTickLabelColor(),
					plot.getXAxisTickLabelMask(), plot.getXAxisVerticalTickLabels(), plot.getOwnXAxisLineColor(),
					plotName, "XAxisFormat", true
					);
			writeExpression( plot.getYAxisLabelExpression(), plotName, "YAxisLabelExpression");
			writeAxisFormat(
					indent, plot.getYAxisLabelFont(), plot.getOwnYAxisLabelColor(),
					plot.getYAxisTickLabelFont(), plot.getOwnYAxisTickLabelColor(),
					plot.getYAxisTickLabelMask(), plot.getYAxisVerticalTickLabels(), plot.getOwnYAxisLineColor(),
					plotName, "YAxisFormat", true
					);

			writeExpression( plot.getDomainAxisMinValueExpression(), plotName, "DomainAxisMinValueExpression");
			writeExpression( plot.getDomainAxisMaxValueExpression(), plotName, "DomainAxisMaxValueExpression");
			writeExpression( plot.getRangeAxisMinValueExpression(), plotName, "RangeAxisMinValueExpression");
			writeExpression( plot.getRangeAxisMaxValueExpression(), plotName, "RangeAxisMaxValueExpression");
	
			flush();
		}
	}


	/**
	 *
	 */
	private void writeLinePlot( JRLinePlot plot, String chartName)
	{
		if(plot != null)
		{
			String plotName = chartName + "LinePlot";
			write( "JRDesignLinePlot " + plotName + " = (JRDesignLinePlot)" + chartName + ".getPlot();\n");
			write( plotName + ".setShowLines(Boolean.valueOf(" + plot.getShowLines() + "));\n");
			write( plotName + ".setShowShapes(Boolean.valueOf(" + plot.getShowShapes() + "));\n");
			writePlot( plot, plotName);
			
			writeExpression( plot.getCategoryAxisLabelExpression(), plotName, "CategoryAxisLabelExpression");
			writeCategoryAxisFormat(
					indent,
					plot.getCategoryAxisLabelFont(), plot.getOwnCategoryAxisLabelColor(),
					plot.getCategoryAxisTickLabelFont(), plot.getOwnCategoryAxisTickLabelColor(),
					plot.getCategoryAxisTickLabelMask(), plot.getCategoryAxisVerticalTickLabels(), 
					plot.getCategoryAxisTickLabelRotation(), plot.getOwnCategoryAxisLineColor(),
					plotName, "CategoryAxisFormat"
					);
			
			writeExpression( plot.getValueAxisLabelExpression(), plotName, "ValueAxisLabelExpression");
			writeAxisFormat(
					indent, 
					plot.getValueAxisLabelFont(), plot.getOwnValueAxisLabelColor(),
					plot.getValueAxisTickLabelFont(), plot.getOwnValueAxisTickLabelColor(),
					plot.getValueAxisTickLabelMask(), plot.getValueAxisVerticalTickLabels(), 
					plot.getOwnValueAxisLineColor(),
					plotName, "ValueAxisFormat", true
					);
			writeExpression( plot.getDomainAxisMinValueExpression(), plotName, "DomainAxisMinValueExpression");
			writeExpression( plot.getDomainAxisMaxValueExpression(), plotName, "DomainAxisMaxValueExpression");
			writeExpression( plot.getRangeAxisMinValueExpression(), plotName, "RangeAxisMinValueExpression");
			writeExpression( plot.getRangeAxisMaxValueExpression(), plotName, "RangeAxisMaxValueExpression");
	
			flush();
		}
	}


	/**
	 * 
	 */
	private void writeTimeSeriesPlot( JRTimeSeriesPlot plot, String chartName)
	{
		if(plot != null)
		{
			String plotName = chartName + "TimeSeriesPlot";
			write( "JRDesignTimeSeriesPlot " + plotName + " = (JRDesignTimeSeriesPlot)" + chartName + ".getPlot();\n");
			write( plotName + ".setShowLines(Boolean.valueOf(" + plot.getShowLines() + "));\n");
			write( plotName + ".setShowShapes(Boolean.valueOf(" + plot.getShowShapes() + "));\n");
			writePlot( plot, plotName);
			
			writeExpression( plot.getTimeAxisLabelExpression(), plotName, "TimeAxisLabelExpression");
			writeAxisFormat(
					indent, plot.getTimeAxisLabelFont(), plot.getOwnTimeAxisLabelColor(),
					plot.getTimeAxisTickLabelFont(), plot.getOwnTimeAxisTickLabelColor(),
					plot.getTimeAxisTickLabelMask(), plot.getTimeAxisVerticalTickLabels(), 
					plot.getOwnTimeAxisLineColor(),
					plotName, "TimeAxisFormat", true
					);
			
			writeExpression( plot.getValueAxisLabelExpression(), plotName, "ValueAxisLabelExpression");
			writeAxisFormat(
					indent, 
					plot.getValueAxisLabelFont(), plot.getOwnValueAxisLabelColor(),
					plot.getValueAxisTickLabelFont(), plot.getOwnValueAxisTickLabelColor(),
					plot.getValueAxisTickLabelMask(), plot.getValueAxisVerticalTickLabels(), 
					plot.getOwnValueAxisLineColor(),
					plotName, "ValueAxisFormat", true
					);
			writeExpression( plot.getDomainAxisMinValueExpression(), plotName, "DomainAxisMinValueExpression");
			writeExpression( plot.getDomainAxisMaxValueExpression(), plotName, "DomainAxisMaxValueExpression");
			writeExpression( plot.getRangeAxisMinValueExpression(), plotName, "RangeAxisMinValueExpression");
			writeExpression( plot.getRangeAxisMaxValueExpression(), plotName, "RangeAxisMaxValueExpression");
	
			flush();
		}
	}

	/**
	 *
	 */
	public void writeBar3DPlot( JRBar3DPlot plot, String chartName)
	{
		if(plot != null)
		{
			String plotName = chartName + "Bar3DPlot";
			write( "JRDesignBar3DPlot " + plotName + " = (JRDesignBar3DPlot)" + chartName + ".getPlot();\n");
			write( plotName + ".setShowLabels(Boolean.valueOf(" + plot.getShowLabels() + "));\n");
			write( plotName + ".setXOffset(" + plot.getXOffsetDouble() + ");\n");
			write( plotName + ".setYOffset(" + plot.getYOffsetDouble() + ");\n");
			
			writePlot( plot, plotName);
			
			writeItemLabel( plot.getItemLabel(), plotName, "ItemLabel");
			
			writeExpression( plot.getCategoryAxisLabelExpression(), plotName, "CategoryAxisLabelExpression");
			writeCategoryAxisFormat(
					indent,
					plot.getCategoryAxisLabelFont(), plot.getOwnCategoryAxisLabelColor(),
					plot.getCategoryAxisTickLabelFont(), plot.getOwnCategoryAxisTickLabelColor(),
					plot.getCategoryAxisTickLabelMask(), plot.getCategoryAxisVerticalTickLabels(), 
					plot.getCategoryAxisTickLabelRotation(), plot.getOwnCategoryAxisLineColor(),
					plotName, "CategoryAxisFormat"
					);
			
			writeExpression( plot.getValueAxisLabelExpression(), plotName, "ValueAxisLabelExpression");
			writeAxisFormat(
					indent, 
					plot.getValueAxisLabelFont(), plot.getOwnValueAxisLabelColor(),
					plot.getValueAxisTickLabelFont(), plot.getOwnValueAxisTickLabelColor(),
					plot.getValueAxisTickLabelMask(), plot.getValueAxisVerticalTickLabels(), 
					plot.getOwnValueAxisLineColor(),
					plotName, "ValueAxisFormat", true
					);
			writeExpression( plot.getDomainAxisMinValueExpression(), plotName, "DomainAxisMinValueExpression");
			writeExpression( plot.getDomainAxisMaxValueExpression(), plotName, "DomainAxisMaxValueExpression");
			writeExpression( plot.getRangeAxisMinValueExpression(), plotName, "RangeAxisMinValueExpression");
			writeExpression( plot.getRangeAxisMaxValueExpression(), plotName, "RangeAxisMaxValueExpression");
	
			flush();
		}
	}


	/**
	 *
	 */
	public void writeBarChart( JRChart chart, String chartName)
	{
		if(chart != null)
		{
			write( "JRDesignChart " + chartName + " = new JRDesignChart(jasperDesign, JRChart.CHART_TYPE_BAR);\n");
			writeChart( chart, chartName);
			writeCategoryDataSet( (JRCategoryDataset) chart.getDataset(), chartName, "CategoryDataset");
			writeBarPlot( (JRBarPlot) chart.getPlot(), chartName);
			flush();
		}
	}


	/**
	 *
	 */
	public void writeBar3DChart( JRChart chart, String chartName)
	{
		if(chart != null)
		{
			write( "JRDesignChart " + chartName + " = new JRDesignChart(jasperDesign, JRChart.CHART_TYPE_BAR3D);\n");
			writeChart( chart, chartName);
			writeCategoryDataSet( (JRCategoryDataset) chart.getDataset(), chartName, "CategoryDataset");
			writeBar3DPlot( (JRBar3DPlot) chart.getPlot(), chartName);
			flush();
		}
	}


	/**
	 *
	 */
	public void writeBubbleChart( JRChart chart, String chartName)
	{
		if(chart != null)
		{
			write( "JRDesignChart " + chartName + " = new JRDesignChart(jasperDesign, JRChart.CHART_TYPE_BUBBLE);\n");
			writeChart( chart, chartName);
			writeXyzDataset( (JRXyzDataset) chart.getDataset(), chartName, "XyzDataset");
			writeBubblePlot( (JRBubblePlot) chart.getPlot(), chartName);
			flush();
		}
	}


	/**
	 *
	 */
	public void writeStackedBarChart( JRChart chart, String chartName)
	{
		if(chart != null)
		{
			write( "JRDesignChart " + chartName + " = new JRDesignChart(jasperDesign, JRChart.CHART_TYPE_STACKEDBAR);\n");
			writeChart( chart, chartName);
			writeCategoryDataSet( (JRCategoryDataset) chart.getDataset(), chartName, "CategoryDataset");
			writeBarPlot( (JRBarPlot) chart.getPlot(), chartName);
			flush();
		}
	}


	/**
	 *
	 */
	public void writeStackedBar3DChart( JRChart chart, String chartName)
	{
		if(chart != null)
		{
			write( "JRDesignChart " + chartName + " = new JRDesignChart(jasperDesign, JRChart.CHART_TYPE_STACKEDBAR3D);\n");
			writeChart( chart, chartName);
			writeCategoryDataSet( (JRCategoryDataset) chart.getDataset(), chartName, "CategoryDataset");
			writeBar3DPlot( (JRBar3DPlot) chart.getPlot(), chartName);
			flush();
		}
	}


	/**
	 *
	 */
	public void writeLineChart( JRChart chart, String chartName)
	{
		if(chart != null)
		{
			write( "JRDesignChart " + chartName + " = new JRDesignChart(jasperDesign, JRChart.CHART_TYPE_LINE);\n");
			writeChart( chart, chartName);
			writeCategoryDataSet( (JRCategoryDataset) chart.getDataset(), chartName, "CategoryDataset");
			writeLinePlot( (JRLinePlot) chart.getPlot(), chartName);
			flush();
		}
	}


	/**
	 * 
	 */
	public void writeTimeSeriesChart( JRChart chart, String chartName)
	{
		if(chart != null)
		{
			write( "JRDesignChart " + chartName + " = new JRDesignChart(jasperDesign, JRChart.CHART_TYPE_TIMESERIES);\n");
			writeChart( chart, chartName);
			writeTimeSeriesDataset( (JRTimeSeriesDataset) chart.getDataset(), chartName, "TimeSeriesDataset");
			writeTimeSeriesPlot( (JRTimeSeriesPlot) chart.getPlot(), chartName);
			flush();
		}
	}

	/**
	 * 
	 */
	public void writeHighLowDataset( JRHighLowDataset dataset, String parentName, String datasetNameSuffix)
	{
		if(dataset != null)
		{
			String datasetName = parentName + datasetNameSuffix;
			
			write( "JRDesignHighLowDataset " + datasetName + " = (JRDesignHighLowDataset)" + parentName + ".getDataset();\n");
	
			writeElementDataset( dataset, datasetName);
	
			writeExpression( dataset.getSeriesExpression(), datasetName, "SeriesExpression");
			writeExpression( dataset.getDateExpression(), datasetName, "DateExpression");
			writeExpression( dataset.getHighExpression(), datasetName, "HighExpression");
			writeExpression( dataset.getLowExpression(), datasetName, "LowExpression");
			writeExpression( dataset.getOpenExpression(), datasetName, "OpenExpression");
			writeExpression( dataset.getCloseExpression(), datasetName, "CloseExpression");
			writeExpression( dataset.getVolumeExpression(), datasetName, "VolumeExpression");
			writeHyperlink( dataset.getItemHyperlink(), datasetName, "ItemHyperlink");
			write( parentName + ".setDataset(" + datasetName + ");\n");
			flush();
		}
	}


	/**
	 * 
	 */
	public void writeHighLowChart( JRChart chart, String chartName)
	{
		if(chart != null)
		{
			write( "JRDesignChart " + chartName + " = new JRDesignChart(jasperDesign, JRChart.CHART_TYPE_HIGHLOW);\n");
			writeChart( chart, chartName);
			writeHighLowDataset( (JRHighLowDataset) chart.getDataset(), chartName, "HighLowDataset");
			
			JRHighLowPlot plot = (JRHighLowPlot) chart.getPlot();
			if(plot != null)
			{
				String plotName = chartName + "HighLowPlot";
				write( "JRDesignHighLowPlot " + plotName + " = (JRDesignHighLowPlot)" + chartName + ".getPlot();\n");
				write( plotName + ".setShowOpenTicks(Boolean.valueOf(" + plot.getShowOpenTicks() + "));\n");
				write( plotName + ".setShowCloseTicks(Boolean.valueOf(" + plot.getShowCloseTicks() + "));\n");

				writePlot( plot, plotName);
				writeExpression( plot.getTimeAxisLabelExpression(), plotName, "TimeAxisLabelExpression");
				writeAxisFormat(
						indent, plot.getTimeAxisLabelFont(), plot.getOwnTimeAxisLabelColor(),
						plot.getTimeAxisTickLabelFont(), plot.getOwnTimeAxisTickLabelColor(),
						plot.getTimeAxisTickLabelMask(), plot.getTimeAxisVerticalTickLabels(), plot.getOwnTimeAxisLineColor(),
						plotName, "TimeAxisFormat", true
						);
				
				writeExpression( plot.getValueAxisLabelExpression(), plotName, "ValueAxisLabelExpression");
				writeAxisFormat(
						indent, plot.getValueAxisLabelFont(), plot.getOwnValueAxisLabelColor(),
						plot.getValueAxisTickLabelFont(), plot.getOwnValueAxisTickLabelColor(),
						plot.getValueAxisTickLabelMask(), plot.getValueAxisVerticalTickLabels(), plot.getOwnValueAxisLineColor(),
						plotName, "ValueAxisFormat", true
						);
				writeExpression( plot.getDomainAxisMinValueExpression(), plotName, "DomainAxisMinValueExpression");
				writeExpression( plot.getDomainAxisMaxValueExpression(), plotName, "DomainAxisMaxValueExpression");
				writeExpression( plot.getRangeAxisMinValueExpression(), plotName, "RangeAxisMinValueExpression");
				writeExpression( plot.getRangeAxisMaxValueExpression(), plotName, "RangeAxisMaxValueExpression");
		
				flush();
			}
			flush();
		}
	}


	/**
	 * 
	 */
	public void writeGanttChart( JRChart chart, String chartName)
	{
		if(chart != null)
		{
			write( "JRDesignChart " + chartName + " = new JRDesignChart(jasperDesign, JRChart.CHART_TYPE_GANTT);\n");
			writeChart( chart, chartName);
			writeGanttDataset( (JRGanttDataset) chart.getDataset(), chartName, "GanttDataset");
			writeBarPlot( (JRBarPlot) chart.getPlot(), chartName);
			flush();
		}
	}


	/**
	 * 
	 */
	public void writeCandlestickChart( JRChart chart, String chartName)
	{
		if(chart != null)
		{
			write( "JRDesignChart " + chartName + " = new JRDesignChart(jasperDesign, JRChart.CHART_TYPE_CANDLESTICK);\n");
			writeChart( chart, chartName);
			writeHighLowDataset( (JRHighLowDataset) chart.getDataset(), chartName, "HighLowDataset");
			
			JRCandlestickPlot plot = (JRCandlestickPlot) chart.getPlot();
			if(plot != null)
			{
				String plotName = chartName + "CandlestickPlot";
				
				write( "JRDesignCandlestickPlot " + plotName + " = (JRDesignCandlestickPlot)" + chartName + ".getPlot();\n");
				write( plotName + ".setShowVolume(Boolean.valueOf(" + plot.getShowVolume() + "));\n");
				writePlot( plot, plotName);
				writeExpression( plot.getTimeAxisLabelExpression(), plotName, "TimeAxisLabelExpression");
				writeAxisFormat(
						indent, plot.getTimeAxisLabelFont(), plot.getOwnTimeAxisLabelColor(),
						plot.getTimeAxisTickLabelFont(), plot.getOwnTimeAxisTickLabelColor(),
						plot.getTimeAxisTickLabelMask(), plot.getTimeAxisVerticalTickLabels(), plot.getOwnTimeAxisLineColor(),
						plotName, "TimeAxisFormat", true
						);
				
				writeExpression( plot.getValueAxisLabelExpression(), plotName, "ValueAxisLabelExpression");
				writeAxisFormat(
						indent, plot.getValueAxisLabelFont(), plot.getOwnValueAxisLabelColor(),
						plot.getValueAxisTickLabelFont(), plot.getOwnValueAxisTickLabelColor(),
						plot.getValueAxisTickLabelMask(), plot.getValueAxisVerticalTickLabels(), plot.getOwnValueAxisLineColor(),
						plotName, "ValueAxisFormat", true
						);
				writeExpression( plot.getDomainAxisMinValueExpression(), plotName, "DomainAxisMinValueExpression");
				writeExpression( plot.getDomainAxisMaxValueExpression(), plotName, "DomainAxisMaxValueExpression");
				writeExpression( plot.getRangeAxisMinValueExpression(), plotName, "RangeAxisMinValueExpression");
				writeExpression( plot.getRangeAxisMaxValueExpression(), plotName, "RangeAxisMaxValueExpression");
		
				flush();
			}
			flush();
		}
	}

	/**
	 *
	 */
	private void writeAreaPlot( JRAreaPlot plot, String chartName)
	{
		if(plot != null)
		{
			String plotName = chartName + "AreaPlot";
			
			write( "JRDesignAreaPlot " + plotName + " = (JRDesignAreaPlot)" + chartName + ".getPlot();\n");
			writePlot( plot, plotName);
	
			writeExpression( plot.getCategoryAxisLabelExpression(), plotName, "CategoryAxisLabelExpression");
			writeCategoryAxisFormat(
					indent,
					plot.getCategoryAxisLabelFont(), plot.getOwnCategoryAxisLabelColor(),
					plot.getCategoryAxisTickLabelFont(), plot.getOwnCategoryAxisTickLabelColor(),
					plot.getCategoryAxisTickLabelMask(), plot.getCategoryAxisVerticalTickLabels(), 
					plot.getCategoryAxisTickLabelRotation(), plot.getOwnCategoryAxisLineColor(),
					plotName, "CategoryAxisFormat"
					);
			
			writeExpression( plot.getValueAxisLabelExpression(), plotName, "ValueAxisLabelExpression");
			writeAxisFormat(
					indent, 
					plot.getValueAxisLabelFont(), plot.getOwnValueAxisLabelColor(),
					plot.getValueAxisTickLabelFont(), plot.getOwnValueAxisTickLabelColor(),
					plot.getValueAxisTickLabelMask(), plot.getValueAxisVerticalTickLabels(), 
					plot.getOwnValueAxisLineColor(),
					plotName, "ValueAxisFormat", true
					);
			writeExpression( plot.getDomainAxisMinValueExpression(), plotName, "DomainAxisMinValueExpression");
			writeExpression( plot.getDomainAxisMaxValueExpression(), plotName, "DomainAxisMaxValueExpression");
			writeExpression( plot.getRangeAxisMinValueExpression(), plotName, "RangeAxisMinValueExpression");
			writeExpression( plot.getRangeAxisMaxValueExpression(), plotName, "RangeAxisMaxValueExpression");
	
			flush();
		}
	}


	/**
	 *
	 */
	public void writeAreaChart( JRChart chart, String chartName)
	{
		if(chart != null)
		{
			write( "JRDesignChart " + chartName + " = new JRDesignChart(jasperDesign, JRChart.CHART_TYPE_AREA);\n");
			writeChart( chart, chartName);
			writeCategoryDataSet( (JRCategoryDataset) chart.getDataset(), chartName, "CategoryDataset");
			writeAreaPlot( (JRAreaPlot) chart.getPlot(), chartName);
			flush();
		}
	}


	/**
	 *
	 */
	private void writeScatterPlot( JRScatterPlot plot, String chartName)
	{
		if(plot != null)
		{
			String plotName = chartName + "ScatterPlot";
			write( "JRDesignScatterPlot " + plotName + " = (JRDesignScatterPlot)" + chartName + ".getPlot();\n");
			write( plotName + ".setShowLines(Boolean.valueOf(" + plot.getShowLines() + "));\n");
			write( plotName + ".setShowShapes(Boolean.valueOf(" + plot.getShowShapes() + "));\n");
			writePlot( plot, plotName);
			
			writeExpression( plot.getXAxisLabelExpression(), plotName, "XAxisLabelExpression");
			writeAxisFormat(
					indent, 
					plot.getXAxisLabelFont(), plot.getOwnXAxisLabelColor(),
					plot.getXAxisTickLabelFont(), plot.getOwnXAxisTickLabelColor(),
					plot.getXAxisTickLabelMask(), plot.getXAxisVerticalTickLabels(), 
					plot.getOwnXAxisLineColor(),
					plotName, "XAxisFormat", true
					);
			
			writeExpression( plot.getYAxisLabelExpression(), plotName, "YAxisLabelExpression");
			writeAxisFormat(
					indent, 
					plot.getYAxisLabelFont(), plot.getOwnYAxisLabelColor(),
					plot.getYAxisTickLabelFont(), plot.getOwnYAxisTickLabelColor(),
					plot.getYAxisTickLabelMask(), plot.getYAxisVerticalTickLabels(), 
					plot.getOwnYAxisLineColor(),
					plotName, "YAxisFormat", true
					);
			writeExpression( plot.getDomainAxisMinValueExpression(), plotName, "DomainAxisMinValueExpression");
			writeExpression( plot.getDomainAxisMaxValueExpression(), plotName, "DomainAxisMaxValueExpression");
			writeExpression( plot.getRangeAxisMinValueExpression(), plotName, "RangeAxisMinValueExpression");
			writeExpression( plot.getRangeAxisMaxValueExpression(), plotName, "RangeAxisMaxValueExpression");
	
			flush();
		}
	}


	/**
	 *
	 */
	public void writeScatterChart( JRChart chart, String chartName)
	{
		if(chart != null)
		{
			write( "JRDesignChart " + chartName + " = new JRDesignChart(jasperDesign, JRChart.CHART_TYPE_SCATTER);\n");
			writeChart( chart, chartName);
			writeXyDataset( (JRXyDataset) chart.getDataset(), chartName, "XyDataset");
			writeScatterPlot( (JRScatterPlot) chart.getPlot(), chartName);
			flush();
		}
	}


	/**
	 *
	 */
	public void writeXyAreaChart( JRChart chart, String chartName)
	{
		if(chart != null)
		{
			write( "JRDesignChart " + chartName + " = new JRDesignChart(jasperDesign, JRChart.CHART_TYPE_XYAREA);\n");
			writeChart( chart, chartName);
			writeXyDataset( (JRXyDataset) chart.getDataset(), chartName, "XyDataset");
			writeAreaPlot( (JRAreaPlot) chart.getPlot(), chartName);
			flush();
		}
	}


	/**
	 *
	 */
	public void writeXyBarChart( JRChart chart, String chartName)
	{
		if(chart != null)
		{
			write( "JRDesignChart " + chartName + " = new JRDesignChart(jasperDesign, JRChart.CHART_TYPE_XYBAR);\n");
			writeChart( chart, chartName);
			JRChartDataset dataset = chart.getDataset();

			if( dataset.getDatasetType() == JRChartDataset.TIMESERIES_DATASET )
			{
				writeTimeSeriesDataset( (JRTimeSeriesDataset)dataset, chartName, "TimeSeriesDataset");
			}
			else if( dataset.getDatasetType() == JRChartDataset.TIMEPERIOD_DATASET ){
				writeTimePeriodDataset( (JRTimePeriodDataset)dataset, chartName, "XyDataset");
			}
			else if( dataset.getDatasetType() == JRChartDataset.XY_DATASET ){
				writeXyDataset( (JRXyDataset) chart.getDataset(), chartName, "XyDataset");
			}
			writeBarPlot( (JRBarPlot) chart.getPlot(), chartName);
			flush();
		}
	}


	/**
	 *
	 */
	public void writeXyLineChart( JRChart chart, String chartName)
	{
		if(chart != null)
		{
			write( "JRDesignChart " + chartName + " = new JRDesignChart(jasperDesign, JRChart.CHART_TYPE_XYLINE);\n");
			writeChart( chart, chartName);
			writeXyDataset( (JRXyDataset) chart.getDataset(), chartName, "XyDataset");
			writeLinePlot( (JRLinePlot) chart.getPlot(), chartName);
			flush();
		}
	}


	/**
	 * Writes the definition of a meter chart to the output stream.
	 *
	 * @param chart the meter chart to write
	 */
	public void writeMeterChart( JRChart chart, String chartName)
	{
		if(chart != null)
		{
			write( "JRDesignChart " + chartName + " = new JRDesignChart(jasperDesign, JRChart.CHART_TYPE_METER);\n");
			writeChart( chart, chartName);
			writeValueDataset( (JRValueDataset) chart.getDataset(), chartName, "ValueDataset");
			JRMeterPlot plot = (JRMeterPlot) chart.getPlot();
			if(plot != null)
			{
				String plotName = chartName + "MeterPlot";
				
				write( "JRDesignMeterPlot " + plotName + " = (JRDesignMeterPlot)" + chartName + ".getPlot();\n");
				write( plotName + ".setShape(Byte.valueOf((byte)" + plot.getShapeByte() + "));\n");
				write( plotName + ".setMeterAngle(Integer.valueOf(" + plot.getMeterAngleInteger() + "));\n");
				write( plotName + ".setUnits(\"" + JRStringUtil.escapeJavaStringLiteral(plot.getUnits()) + "\");\n");
				write( plotName + ".setTickInterval(Double.valueOf(" + plot.getTickIntervalDouble() + "));\n");
				write( plotName + ".setMeterBackgroundColor(" + getColorText(plot.getMeterBackgroundColor()) + ");\n");
				write( plotName + ".setNeedleColor(" + getColorText(plot.getNeedleColor()) + ");\n");
				write( plotName + ".setTickColor(" + getColorText(plot.getTickColor()) + ");\n");
				
				writePlot( plot, plotName);
				if (plot.getTickLabelFont() != null)
				{
					writeFont( plot.getTickLabelFont(), plotName + ".getTickLabelFont()");
					flush();
				}
				writeValueDisplay( plot.getValueDisplay(), plotName);
				writeDataRange( plot.getDataRange(), plotName, "DataRange");

				List intervals = plot.getIntervals();
				if (intervals != null && intervals.size() > 0)
				{
					for(int i = 0; i < intervals.size(); i++)
					{
						JRMeterInterval meterInterval = (JRMeterInterval) intervals.get(i);
						writeMeterInterval( meterInterval, plotName, plotName+"Interval"+i);
					}
				}
				flush();
				
			}
			flush();
		}
	}


	/**
	 * Writes the description of a thermometer chart to the output stream.
	 *
	 * @param chart the thermometer chart to write
	 */
	public void writeThermometerChart( JRChart chart, String chartName)
	{
		if(chart != null)
		{
			write( "JRDesignChart " + chartName + " = new JRDesignChart(jasperDesign, JRChart.CHART_TYPE_THERMOMETER);\n");
			writeChart( chart, chartName);
			writeValueDataset( (JRValueDataset) chart.getDataset(), chartName, "ValueDataset");
			JRThermometerPlot plot = (JRThermometerPlot) chart.getPlot();
			if(plot != null)
			{
				String plotName = chartName + "ThermometerPlot";
				write( "JRDesignThermometerPlot " + plotName + " = (JRDesignThermometerPlot)" + chartName + ".getPlot();\n");
				write( plotName + ".setValueLocation(Byte.valueOf((byte)" + plot.getValueLocationByte() + "));\n");
				write( plotName + ".setMercuryColor(" + getColorText(plot.getMercuryColor()) + ");\n");
				writePlot( plot, plotName);
				writeValueDisplay( plot.getValueDisplay(), plotName);
				writeDataRange( plot.getDataRange(), plotName, "DataRange");

				if (plot.getLowRange() != null)
				{
					writeDataRange( plot.getLowRange(), plotName, "LowRange");
				}

				if (plot.getMediumRange() != null)
				{
					writeDataRange( plot.getMediumRange(), plotName, "MediumRange");
				}

				if (plot.getHighRange() != null)
				{
					writeDataRange( plot.getHighRange(), plotName, "HighRange");
				}
				flush();
				
			}
			flush();
		}
	}


	/**
	 * Writes the definition of a multiple axis chart to the output stream.
	 *
	 * @param chart the multiple axis chart to write
	 */
	public void writeMultiAxisChart( JRChart chart, String chartName)
	{
		if(chart != null)
		{
			write( "JRDesignChart " + chartName + " = new JRDesignChart(jasperDesign, JRChart.CHART_TYPE_MULTI_AXIS);\n");
			writeChart( chart, chartName);
			JRMultiAxisPlot plot = (JRMultiAxisPlot) chart.getPlot();
			String plotName = chartName + "MultiAxisPlot";
			
			write( "JRDesignMultiAxisPlot " + plotName + " = (JRDesignMultiAxisPlot)" + chartName + ".getPlot();\n");
			writePlot( chart.getPlot(), plotName);
			List axes = plot.getAxes();
			if (axes != null && axes.size() > 0)
			{
				for (int i = 0; i < axes.size(); i++)
				{
					JRChartAxis chartAxis = (JRChartAxis) axes.get(i);
					writeChartAxis( chartAxis, plotName, plotName + "Axis" + i);
				}
			}
			flush();
		}
	}

	/**
	 *
	 */
	public void writeStackedAreaChart( JRChart chart, String chartName)
	{
		if(chart != null)
		{
			write( "JRDesignChart " + chartName + " = new JRDesignChart(jasperDesign, JRChart.CHART_TYPE_STACKEDAREA);\n");
			writeChart( chart, chartName);
			writeCategoryDataSet( (JRCategoryDataset) chart.getDataset(), chartName, "CategoryDataset");
			writeAreaPlot( (JRAreaPlot) chart.getPlot(), chartName);
			flush();
		}
	}


	/**
	 * 
	 */
	public void writeChartTag( JRChart chart, String chartName)
	{
		switch(chart.getChartType()) {
			case JRChart.CHART_TYPE_AREA:
				writeAreaChart( chart, chartName);
				break;
			case JRChart.CHART_TYPE_BAR:
				writeBarChart( chart, chartName);
				break;
			case JRChart.CHART_TYPE_BAR3D:
				writeBar3DChart( chart, chartName);
				break;
			case JRChart.CHART_TYPE_BUBBLE:
				writeBubbleChart( chart, chartName);
				break;
			case JRChart.CHART_TYPE_CANDLESTICK:
				writeCandlestickChart( chart, chartName);
				break;
			case JRChart.CHART_TYPE_HIGHLOW:
				writeHighLowChart( chart, chartName);
				break;
			case JRChart.CHART_TYPE_LINE:
				writeLineChart( chart, chartName);
				break;
			case JRChart.CHART_TYPE_METER:
				writeMeterChart( chart, chartName);
				break;
			case JRChart.CHART_TYPE_MULTI_AXIS:
				writeMultiAxisChart( chart, chartName);
				break;
			case JRChart.CHART_TYPE_PIE:
				writePieChart( chart, chartName);
				break;
			case JRChart.CHART_TYPE_PIE3D:
				writePie3DChart( chart, chartName);
				break;
			case JRChart.CHART_TYPE_SCATTER:
				writeScatterChart( chart, chartName);
				break;
			case JRChart.CHART_TYPE_STACKEDBAR:
				writeStackedBarChart( chart, chartName);
				break;
			case JRChart.CHART_TYPE_STACKEDBAR3D:
				writeStackedBar3DChart( chart, chartName);
				break;
			case JRChart.CHART_TYPE_THERMOMETER:
				writeThermometerChart( chart, chartName);
				break;
			case JRChart.CHART_TYPE_TIMESERIES:
				writeTimeSeriesChart( chart, chartName);
				break;
			case JRChart.CHART_TYPE_XYAREA:
				writeXyAreaChart( chart, chartName);
				break;
			case JRChart.CHART_TYPE_XYBAR:
				writeXyBarChart( chart, chartName);
				break;
			case JRChart.CHART_TYPE_XYLINE:
				writeXyLineChart( chart, chartName);
				break;
			case JRChart.CHART_TYPE_STACKEDAREA:
				writeStackedAreaChart( chart, chartName);
				break;
			case JRChart.CHART_TYPE_GANTT:
				writeGanttChart( chart, chartName);
				break;
			default:
				throw new JRRuntimeException("Chart type not supported.");
		}
	}


	/**
	 * 
	 */
	private void writeSubreportReturnValue( JRSubreportReturnValue returnValue, String returnValueName)
	{
		if(returnValue != null)
		{
			write( "JRDesignReturnValue " + returnValueName + " = new JRDesignReturnValue();\n");
			write( returnValueName + ".setSubreportVariable(\"" + JRStringUtil.escapeJavaStringLiteral(returnValue.getSubreportVariable()) + "\");\n");
			write( returnValueName + ".setToVariable(\"" + JRStringUtil.escapeJavaStringLiteral(returnValue.getToVariable()) + "\");\n");
			write( returnValueName + ".setCalculation((byte)" + (returnValue.getCalculation() > -1 ? returnValue.getCalculation() : JRVariable.CALCULATION_NOTHING) + ");\n");
			write( returnValueName + ".setIncrementerFactoryClassName(\"" + JRStringUtil.escapeJavaStringLiteral(returnValue.getIncrementerFactoryClassName()) + "\");\n");
			flush();
		}
	}

	/**
	 * 
	 */
	public void writeCrosstab( JRCrosstab crosstab, String crosstabName)
	{
		if(crosstab != null)
		{
			write( "JRDesignCrosstab " + crosstabName + " = new JRDesignCrosstab(jasperDesign);\n");
			write( crosstabName + ".setRepeatColumnHeaders(" + crosstab.isRepeatColumnHeaders() + ");\n");
			write( crosstabName + ".setRepeatRowHeaders(" + crosstab.isRepeatRowHeaders() + ");\n");
			write( crosstabName + ".setColumnBreakOffset(" + (crosstab.getColumnBreakOffset() > 0 ? crosstab.getColumnBreakOffset() : JRCrosstab.DEFAULT_COLUMN_BREAK_OFFSET) + ");\n");
			write( crosstabName + ".setRunDirection((byte)" + (crosstab.getRunDirection() > 0 ? crosstab.getRunDirection() : JRCrosstab.RUN_DIRECTION_LTR) + ");\n");
			write( crosstabName + ".setIgnoreWidth(" + crosstab.getIgnoreWidth().booleanValue() + ");\n");
	
			writeReportElement( crosstab, crosstabName);
	
			JRCrosstabParameter[] parameters = crosstab.getParameters();
			if (parameters != null)
			{
				for (int i = 0; i < parameters.length; i++)
				{
					if (!parameters[i].isSystemDefined())
					{
						writeCrosstabParameter( parameters[i], crosstabName + "Parameter" + i);
						write( crosstabName + ".addParameter(" + crosstabName + "Parameter" + i + ");\n");
						
					}
				}
			}

			writeExpression( crosstab.getParametersMapExpression(), crosstabName, "ParametersMapExpression");
	
			writeCrosstabDataset( crosstab, crosstabName);

			writeCrosstabHeaderCell( crosstab, crosstabName);
	
			JRCrosstabRowGroup[] rowGroups = crosstab.getRowGroups();
			for (int i = 0; i < rowGroups.length; i++)
			{
				writeCrosstabRowGroup( rowGroups[i], crosstabName + "RowGroup" + i);
				write( crosstabName + ".addRowGroup(" + crosstabName + "RowGroup" + i + ");\n");
			}
	
			JRCrosstabColumnGroup[] columnGroups = crosstab.getColumnGroups();
			for (int i = 0; i < columnGroups.length; i++)
			{
				writeCrosstabColumnGroup( columnGroups[i], crosstabName + "ColumnGroup" + i);
				write( crosstabName + ".addColumnGroup(" + crosstabName + "ColumnGroup" + i + ");\n");
			}
	
			JRCrosstabMeasure[] measures = crosstab.getMeasures();
			for (int i = 0; i < measures.length; i++)
			{
				writeCrosstabMeasure( measures[i], crosstabName + "Measure" + i);
				write( crosstabName + ".addMeasure(" + crosstabName + "Measure" + i + ");\n");
			}
	
			if (crosstab instanceof JRDesignCrosstab)
			{
				List cellsList = ((JRDesignCrosstab) crosstab).getCellsList();
				for (int i = 0; i < cellsList.size(); i++)
				{
					JRCrosstabCell cell = (JRCrosstabCell) cellsList.get(i);
					writeCrosstabCell( cell, crosstabName + "Cell" + i);
					write( crosstabName + ".addCell(" + crosstabName + "Cell" + i + ");\n");
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
							writeCrosstabCell( cell, crosstabName + "Cell" + i + "" + j);
							write( crosstabName + ".addCell(" + crosstabName + "Cell" + i + "" + j + ");\n");
						}
					}
				}
			}
	
			writeCrosstabWhenNoDataCell( crosstab, crosstabName + "NoDataCell");
			write( crosstabName + ".setWhenNoDataCell(" + crosstabName + "NoDataCell);\n");
			flush();
		}
	}


	/**
	 * 
	 */
	private void writeCrosstabDataset( JRCrosstab crosstab, String crosstabName)
	{
		if(crosstab != null)
		{
			String datasetName = crosstabName + "Dataset";
			JRCrosstabDataset dataset = crosstab.getDataset();
			write( "JRDesignCrosstabDataset " + datasetName + " = new JRDesignCrosstabDataset();\n");
			write( datasetName + ".setDataPreSorted(" + dataset.isDataPreSorted() + ");\n");
			writeElementDataset( dataset, datasetName);
			write( crosstabName + ".setDataset(" + datasetName + ");\n");
			
			flush();
		}
	}


	/**
	 * 
	 */
	private void writeCrosstabWhenNoDataCell( JRCrosstab crosstab, String cellName)
	{
		JRCellContents whenNoDataCell = crosstab.getWhenNoDataCell();
		if (whenNoDataCell != null)
		{
			writeCellContents( whenNoDataCell, cellName);
			flush();
		}
	}


	/**
	 * 
	 */
	private void writeCrosstabHeaderCell( JRCrosstab crosstab, String parentName)
	{
		JRCellContents headerCell = crosstab.getHeaderCell();
		if (headerCell != null)
		{
			writeCellContents( headerCell, parentName+"HeaderCellContents");
			write( parentName + ".setHeaderCell(" + parentName + "HeaderCellContents);\n");
			flush();
		}
	}


	/**
	 * 
	 */
	protected void writeCrosstabRowGroup( JRCrosstabRowGroup group, String groupName)
	{
		if(group != null)
		{
			write( "JRDesignCrosstabRowGroup " + groupName + " = new JRDesignCrosstabRowGroup();\n");
			write( groupName + ".setName(\"" + JRStringUtil.escapeJavaStringLiteral(group.getName()) + "\");\n");
			write( groupName + ".setWidth(" + group.getWidth() + ");\n");
			write( groupName + ".setTotalPosition((byte)" + (group.getTotalPosition() > 0 ? group.getTotalPosition() : BucketDefinition.TOTAL_POSITION_NONE) + ");\n");
			write( groupName + ".setPosition(" + ((byte)group.getPosition() > 0 ? group.getPosition() : JRCellContents.POSITION_Y_TOP) + ");\n");
	
			writeBucket( group.getBucket(), groupName);
	
			JRCellContents header = group.getHeader();
			writeCellContents( header, groupName + "HeaderContents");
			write( groupName + ".setHeader(" + groupName + "HeaderContents);\n");
	
			JRCellContents totalHeader = group.getTotalHeader();
			writeCellContents( totalHeader, groupName + "TotalHeaderContents");
			write( groupName + ".setTotalHeader(" + groupName + "TotalHeaderContents);\n");
			flush();

		}
	}


	/**
	 * 
	 */
	protected void writeCrosstabColumnGroup( JRCrosstabColumnGroup group, String groupName)
	{
		if(group != null)
		{
			write( "JRDesignColumnRowGroup " + groupName + " = new JRDesignCrosstabColumnGroup();\n");
			write( groupName + ".setName(\"" + JRStringUtil.escapeJavaStringLiteral(group.getName()) + "\");\n");
			write( groupName + ".setHeight(" + group.getHeight() + ");\n");
			write( groupName + ".setTotalPosition((byte)" + (group.getTotalPosition() > 0 ? group.getTotalPosition() : BucketDefinition.TOTAL_POSITION_NONE) + ");\n");
			write( groupName + ".setPosition((byte)" + (group.getPosition() > 0 ? group.getPosition() : JRCellContents.POSITION_X_LEFT) + ");\n");
	
			writeBucket( group.getBucket(), groupName);
	
			JRCellContents header = group.getHeader();
			writeCellContents( header, groupName + "HeaderContents");
			write( groupName + ".setHeader(" + groupName + "HeaderContents);\n");
	
			JRCellContents totalHeader = group.getTotalHeader();
			writeCellContents( totalHeader, groupName + "TotalHeaderContents");
			write( groupName + ".setTotalHeader(" + groupName + "TotalHeaderContents);\n");
			flush();

		}
	}


	/**
	 * 
	 */
	protected void writeBucket( JRCrosstabBucket bucket, String parentName)
	{
		if(bucket != null)
		{
			String bucketName = parentName + "Bucket";
			write( "JRDesignCrosstabBucket " + bucketName + " = new JRDesignCrosstabBucket();\n");
			write( bucketName + ".setOrder((byte)" + (bucket.getOrder() > 0 ? bucket.getOrder() : BucketDefinition.ORDER_ASCENDING) + ");\n");

			writeExpression( bucket.getExpression(), bucketName, "Expression");

			writeExpression( bucket.getComparatorExpression(), bucketName, "ComparatorExpression");

			writeExpression( bucket.getOrderByExpression(), bucketName, "OrderByExpression", Object.class.getName());
			flush();
		}
	}


	/**
	 * 
	 */
	protected void writeCrosstabMeasure( JRCrosstabMeasure measure, String measureName)
	{
		if(measure != null)
		{
			write( "JRDesignCrosstabMeasure " + measureName + " = new JRDesignCrosstabMeasure();\n");
			write( measureName + ".setName(\"" + JRStringUtil.escapeJavaStringLiteral(measure.getName()) + "\");\n");

			if(measure.getValueClass() != null)
				write( measureName + ".setValueClass(Class.forName(\"" + JRStringUtil.escapeJavaStringLiteral(measure.getValueClass().getName()) + "\"));\n");
			else if(measure.getValueClassName() != null)
				write( measureName + ".setValueClassName(\"" + JRStringUtil.escapeJavaStringLiteral(measure.getValueClassName()) + "\");\n");
			
			write( measureName + ".setCalculation((byte)" + (measure.getCalculation() > 0 ? measure.getCalculation() : JRVariable.CALCULATION_NOTHING) + ");\n");
			write( measureName + ".setPercentageOfType((byte)" + (measure.getPercentageOfType() > 0 ? measure.getPercentageOfType() : JRCrosstabMeasure.PERCENTAGE_TYPE_NONE) + ");\n");
			write( measureName + ".setPercentageCalculatorClassName(\"" + JRStringUtil.escapeJavaStringLiteral(measure.getPercentageCalculatorClassName()) + "\");\n");
			writeExpression( measure.getValueExpression(), measureName, "ValueExpression");
			
			flush();
		}
	}


	/**
	 * 
	 */
	protected void writeCrosstabCell( JRCrosstabCell cell, String cellName)
	{
		if(cell != null)
		{
			write( "JRDesignCrosstabCell " + cellName + " = new JRDesignCrosstabCell();\n");
			write( cellName + ".setWidth(" + cell.getWidth() + ");\n");
			write( cellName + ".setHeight(" + cell.getHeight() + ");\n");
			write( cellName + ".setRowTotalGroup(\"" + JRStringUtil.escapeJavaStringLiteral(cell.getRowTotalGroup()) + "\");\n");
			write( cellName + ".setColumnTotalGroup(\"" + JRStringUtil.escapeJavaStringLiteral(cell.getColumnTotalGroup()) + "\");\n");
		
			writeCellContents( cell.getContents(), cellName + "Contents");
	
			write( cellName + ".setContents(" + cellName + "Contents);\n");
			flush();
		}
	}


	/**
	 * 
	 */
	protected void writeCellContents( JRCellContents contents, String cellName)
	{
		if (contents != null)
		{
			write( "JRDesignCellContents " + cellName + " = new JRDesignCellContents();\n");
			write( cellName + ".setBackcolor(" + getColorText(contents.getBackcolor()) + ");\n");
			write( cellName + ".setMode((byte)" + contents.getMode() + ");\n");
			writeStyleReferenceAttr( contents, cellName);

			writeBox( contents.getLineBox(), cellName + ".getLineBox()");

			writeChildElements( contents, cellName);

			flush();
		}
	}


	/**
	 * 
	 */
	protected void writeCrosstabParameter( JRCrosstabParameter parameter, String parameterName)
	{
		if(parameter != null)
		{
			write( "JRDesignCrosstabParameter " + parameterName + " = new JRDesignCrosstabParameter();\n");
			write( parameterName + ".setDescription(" + parameter.getDescription() + ");\n");
			write( parameterName + ".setName(" + parameter.getName() + ");\n");
			
			if(parameter.getValueClass() != null)
				write( parameterName + ".setValueClass(Class.forName(\"" + JRStringUtil.escapeJavaStringLiteral(parameter.getValueClass().getName()) + "\"));\n");
			else if(parameter.getValueClassName() != null)
				write( parameterName + ".setValueClassName(\"" + JRStringUtil.escapeJavaStringLiteral(parameter.getValueClassName()) + "\");\n");
			else
				write( parameterName + ".setValueClassName(\"java.lang.String\");\n");
			
			writeExpression( parameter.getExpression(), parameterName, "Expression");
			flush();
		}
	}


	/**
	 * 
	 */
	public void writeDataset( JRDataset dataset, String datasetName)
	{
		if(dataset != null)
		{
			write( "JRDesignDataset " + datasetName + " = new JRDesignDataset(" + dataset.isMainDataset() + ");\n");	
			
			write( datasetName + ".setName(\"" + JRStringUtil.escapeJavaStringLiteral(dataset.getName()) + "\");\n");
			write( datasetName + ".setScriptletClass(\"" + JRStringUtil.escapeJavaStringLiteral(dataset.getScriptletClass()) + "\");\n");
			write( datasetName + ".setResourceBundle(\"" + JRStringUtil.escapeJavaStringLiteral(dataset.getResourceBundle()) + "\");\n");
			write( datasetName + ".setWhenResourceMissingType((byte)" + (dataset.getWhenResourceMissingType() < 1 ? JRReport.WHEN_RESOURCE_MISSING_TYPE_NULL : dataset.getWhenResourceMissingType()) + ");\n");
	
			writeProperties( dataset, datasetName);
	
			writeDatasetContents( dataset, datasetName);
			flush();
		}
		
	}

	/**
	 * 
	 */
	protected void writeDatasetContents( JRDataset dataset, String datasetName)
	{
		JRScriptlet[] scriptlets = dataset.getScriptlets();
		if (scriptlets != null && scriptlets.length > 0)
		{
			for(int i = 0; i < scriptlets.length; i++)
			{
				writeScriptlet( scriptlets[i], datasetName + "Scriptlet" + i);
				write( datasetName +".addScriptlet(" + datasetName + "Scriptlet" + i + ");\n");
			}
		}

		JRParameter[] parameters = dataset.getParameters();
		if (parameters != null && parameters.length > 0)
		{
			for(int i = 0; i < parameters.length; i++)
			{
				if (!parameters[i].isSystemDefined())
				{
					writeParameter( parameters[i], datasetName + "Parameter" + i);
					write( datasetName +".addParameter(" + datasetName + "Parameter" + i + ");\n");
				}
			}
		}

		if(dataset.getQuery() != null)
		{
			writeQuery( dataset.getQuery(), datasetName + "Query");
		}

		JRField[] fields = dataset.getFields();
		if (fields != null && fields.length > 0)
		{
			for(int i = 0; i < fields.length; i++)
			{
				writeField( fields[i], datasetName + "Field" + i);
				write( datasetName +".addField(" + datasetName + "Field" + i + ");\n");
			}
		}

		JRSortField[] sortFields = dataset.getSortFields();
		if (sortFields != null && sortFields.length > 0)
		{
			for(int i = 0; i < sortFields.length; i++)
			{
				writeSortField( sortFields[i], datasetName + "SortField" + i);
				write( datasetName +".addSortField(" + datasetName + "SortField" + i + ");\n");
			}
		}

		JRVariable[] variables = dataset.getVariables();
		if (variables != null && variables.length > 0)
		{
			for(int i = 0; i < variables.length; i++)
			{
				if (!variables[i].isSystemDefined())
				{
					writeVariable( variables[i], datasetName + "Variable" + i);
					write( datasetName +".addVariable(" + datasetName + "Variable" + i + ");\n");
				}
			}
		}

		writeExpression( dataset.getFilterExpression(), datasetName, "FilterExpression");

		JRGroup[] groups = dataset.getGroups();
		if (groups != null && groups.length > 0)
		{
			for(int i = 0; i < groups.length; i++)
			{
				String groupName = getGroupName( groups[i]);
				if(groupName != null)
				{
					write( datasetName +".addGroup(" + groupName + ");\n");
				}
			}
		}
		flush();
	}


	/**
	 * Outputs the XML representation of a subdataset run object.
	 * 
	 * @param datasetRun the subdataset run
	 * @throws IOException
	 */
	public void writeDatasetRun( JRDatasetRun datasetRun, String parentName)
	{
		if(datasetRun != null)
		{
			String runName = parentName + "Run";
			write( "JRDesignDatasetRun " + runName + " = new JRDesignDatasetRun();\n");
			write( runName + ".setDatasetName(\"" + JRStringUtil.escapeJavaStringLiteral(datasetRun.getDatasetName()) + "\");\n");
			writeExpression( datasetRun.getParametersMapExpression(), runName, "ParametersMapExpression");
	
			JRDatasetParameter[] parameters = datasetRun.getParameters();
			if (parameters != null && parameters.length > 0)
			{
				for(int i = 0; i < parameters.length; i++)
				{
					writeDatasetParameter( parameters[i], runName + "Parameter" + i);
				}
			}
	
			writeExpression( datasetRun.getConnectionExpression(), runName, "ConnectionExpression");

			writeExpression( datasetRun.getDataSourceExpression(), runName, "DataSourceExpression");
			write( parentName + ".setDatasetRun(" + runName + ");\n");
			flush();
		}
	}


	/**
	 * 
	 */
	public void writeFrame( JRFrame frame, String frameName)
	{
		if(frame != null)
		{
			JRDesignFrame f = new JRDesignFrame(jasperDesign);
			
			write( "JRDesignFrame " + frameName + " = new JRDesignFrame(jasperDesign);\n");
	
			writeReportElement( frame, frameName);
			writeBox( frame.getLineBox(), frameName + ".getLineBox()");
	
			writeChildElements( frame, frameName);
	
			flush();
		}
	}


	/**
	 * 
	 */
	protected void writeHyperlinkParameters( JRHyperlinkParameter[] parameters, String parentName)
	{
		if (parameters != null)
		{
			for (int i = 0; i < parameters.length; i++)
			{
				JRHyperlinkParameter parameter = parameters[i];
				writeHyperlinkParameter( parameter, parentName + "HyperlinkParameter" +i);
				write( parentName + ".addHyperlinkParameter(" + parentName + "HyperlinkParameter" + i + ");\n");
			}
			flush();
		}
	}


	/**
	 * 
	 */
	protected void writeHyperlinkParameter( JRHyperlinkParameter parameter, String parameterName)
	{
		if (parameter != null)
		{
			write( "JRDesignHyperlinkParameter " + parameterName + " = new JRDesignHyperlinkParameter();\n");
			write( parameterName + ".setName(\"" + JRStringUtil.escapeJavaStringLiteral(parameter.getName()) + "\");\n");
			writeExpression(	parameter.getValueExpression(), parameterName, "ValueExpression", String.class.getName());
			flush();
		}
	}

	/**
	 * 
	 *
	public void writeHyperlink( String tagName, JRHyperlink hyperlink, String hyperlinkName)
	{
		writeHyperlink( tagName, null, hyperlink, hyperlinkName);
	}

	/**
	 * 
	 *
	public void writeHyperlink( String tagName, XmlNamespace namespace, 
			JRHyperlink hyperlink, String hyperlinkName)
	{
		if (hyperlink != null)
		{
			writer.startElement(tagName, namespace);

			writer.addEncodedAttribute(JRApiConstants.ATTRIBUTE_hyperlinkType, hyperlink.getLinkType(), JRHyperlinkHelper.HYPERLINK_TYPE_NONE);
			writer.addEncodedAttribute(JRApiConstants.ATTRIBUTE_hyperlinkTarget, hyperlink.getLinkTarget(), JRHyperlinkHelper.HYPERLINK_TARGET_SELF);

			writer.writeExpression(JRApiConstants.ELEMENT_hyperlinkReferenceExpression, JASPERREPORTS_NAMESPACE,
					hyperlink.getHyperlinkReferenceExpression(), false);
			writer.writeExpression(JRApiConstants.ELEMENT_hyperlinkAnchorExpression, JASPERREPORTS_NAMESPACE,
					hyperlink.getHyperlinkAnchorExpression(), false);
			writer.writeExpression(JRApiConstants.ELEMENT_hyperlinkPageExpression, JASPERREPORTS_NAMESPACE,
					hyperlink.getHyperlinkPageExpression(), false);
			writer.writeExpression(JRApiConstants.ELEMENT_hyperlinkTooltipExpression, JASPERREPORTS_NAMESPACE,
					hyperlink.getHyperlinkTooltipExpression(), false);
			writeHyperlinkParameters(hyperlink.getHyperlinkParameters());

			flush();
		}
	}

	/**
	 * 
	 */
	public void writeHyperlink( JRHyperlink hyperlink, String parentName, String hyperlinkSuffix)
	{
		if (hyperlink != null)
		{
			String hyperlinkName = parentName + hyperlinkSuffix;
			write( "JRDesignHyperlink " + hyperlinkName + " = new JRDesignHyperlink();\n");

			write( hyperlinkName + ".setLinkType(\"" + JRStringUtil.escapeJavaStringLiteral(hyperlink.getLinkType() != null ? hyperlink.getLinkType() : JRHyperlinkHelper.HYPERLINK_TYPE_NONE) + "\");\n");
			write( hyperlinkName + ".setLinkTarget(\"" + JRStringUtil.escapeJavaStringLiteral(hyperlink.getLinkTarget() != null ? hyperlink.getLinkTarget() : JRHyperlinkHelper.HYPERLINK_TARGET_SELF) + "\");\n");

			writeExpression( hyperlink.getHyperlinkReferenceExpression(), hyperlinkName, "HyperlinkReferenceExpression");
			writeExpression( hyperlink.getHyperlinkAnchorExpression(), hyperlinkName, "HyperlinkAnchorExpression");
			writeExpression( hyperlink.getHyperlinkPageExpression(), hyperlinkName, "HyperlinkPageExpression");
			writeExpression( hyperlink.getHyperlinkTooltipExpression(), hyperlinkName, "HyperlinkTooltipExpression");
			writeHyperlinkParameters( hyperlink.getHyperlinkParameters(), hyperlinkName);
			write( parentName + ".set" + hyperlinkSuffix + "(" + hyperlinkName + ");\n");
			flush();
		}
	}


	/**
	 * 
	 */
	protected boolean toWriteConditionalStyles()
	{
		return true;
	}

	/**
	 * Writes a conditional style.
	 * 
	 * @param style the conditional style
	 * 
	 */
	protected void writeConditionalStyle( JRConditionalStyle style, String styleName)
	{
		if(style != null)
		{
			write( "JRDesignConditionalStyle " + styleName + " = new JRDesignConditionalStyle(jasperDesign);\n");
			writeExpression( style.getConditionExpression(), styleName, "ConditionExpression");
			write( styleName + ".setName(\"" + JRStringUtil.escapeJavaStringLiteral(style.getName()) + "\");\n");
			writeStyleReferenceAttr( style, styleName);
			write( styleName + ".setDefault(" + style.isDefault() + ");\n");
			write( styleName + ".setMode((byte)" + style.getMode() + ");\n");
			write( styleName + ".setFontName(\"" + JRStringUtil.escapeJavaStringLiteral(style.getFontName()) + "\");\n");
			write( styleName + ".setFontSize(" + style.getFontSize() + ");\n");
			write( styleName + ".setBold(" + style.isBold() + ");\n");
			write( styleName + ".setItalic(" + style.isItalic() + ");\n");
			write( styleName + ".setUnderline(" + style.isUnderline() + ");\n");
			write( styleName + ".setStrikeThrough(" + style.isStrikeThrough() + ");\n");
			write( styleName + ".setPdfFontName(\"" + JRStringUtil.escapeJavaStringLiteral(style.getPdfFontName()) + "\");\n");
			write( styleName + ".setPdfEncoding(\"" + JRStringUtil.escapeJavaStringLiteral(style.getPdfEncoding()) + "\");\n");
			write( styleName + ".setPdfEmbedded(" + style.isPdfEmbedded() + ");\n");
			write( styleName + ".setForecolor(" + getColorText(style.getForecolor()) + ");\n");
			write( styleName + ".setBackcolor(" + getColorText(style.getBackcolor()) + ");\n");
			write( styleName + ".setFill((byte)" + style.getFill() + ");\n");
			write( styleName + ".setRadius(" + style.getRadius() + ");\n");
			write( styleName + ".setScaleImage((byte)" + style.getScaleImage() + ");\n");
			write( styleName + ".setHorizontalAlignment((byte)" + style.getHorizontalAlignment() + ");\n");
			write( styleName + ".setVerticalAlignment((byte)" + style.getVerticalAlignment() + ");\n");
			write( styleName + ".setRotation((byte)" + style.getRotation() + ");\n");
			write( styleName + ".setLineSpacing((byte)" + style.getLineSpacing() + ");\n");
			write( styleName + ".setMarkup(\"" + JRStringUtil.escapeJavaStringLiteral(style.getMarkup()) + "\");\n");
			write( styleName + ".setPattern(\"" + JRStringUtil.escapeJavaStringLiteral(style.getPattern()) + "\");\n");
			write( styleName + ".setBlankWhenNull(" + style.isBlankWhenNull() + ");\n");

			flush();
		}
	}

	/**
	 * Returns the XML write helper used by this report writer.
	 * 
	 * The helper can be used to output XML elements and attributes.
	 * 
	 * @return the XML write helper used by this report writer
	 *
	public JRXmlWriteHelper getXmlWriteHelper()
	{
		return writer;
	}

	/**
	 * Returns the underlying stream to which this writer outputs to.
	 * 
	 * @return the underlying stream used by this writer
	 *
	public Writer getUnderlyingWriter()
	{
		return writer.getUnderlyingWriter();
	}

	/**
	 * 
	 */
	public void writeComponentElement( JRComponentElement componentElement, String componentName)
	{
		if(componentElement != null)
		{
			write( "JRDesignComponentElement " + componentName + " = new JRDesignComponentElement(jasperDesign);\n");
			writeReportElement( componentElement, componentName);
			
			ComponentKey componentKey = componentElement.getComponentKey();
			Component component = componentElement.getComponent();
			//TODO: component specific API writer
//			ComponentXmlWriter componentXmlWriter = ComponentsEnvironment.
//				getComponentManager(componentKey).getComponentXmlWriter();
//			componentXmlWriter.writeToXml(componentKey, component, this);
			
			flush();
		}
	}
	
	/**
	 * 
	 *
	protected XmlNamespace getNamespace()
	{
		return JASPERREPORTS_NAMESPACE;
	}


	/**
	 * 
	 */
	public void writeGenericElement( JRGenericElement element, String elementName)
	{
		write( "JRDesignGenericElement " + elementName + " = new JRDesignGenericElement(jasperDesign);\n");
		
		write( elementName + ".setEvaluationTime((byte)" + (element.getEvaluationTime() >0 ? element.getEvaluationTime() : JRExpression.EVALUATION_TIME_NOW) + ");\n");

		if (element.getEvaluationGroupName() != null)
		{
			write( elementName + ".setEvaluationGroupName(\"" + JRStringUtil.escapeJavaStringLiteral(element.getEvaluationGroupName()) + "\");\n");
		}

		writeReportElement( element, elementName);
		
		JRGenericElementType printKey = element.getGenericType();
		JRGenericElementType t = new JRGenericElementType(printKey.getNamespace(), printKey.getName());

		write( "JRGenericElementType " + elementName + "Type = new JRGenericElementType(\"" 
				+ JRStringUtil.escapeJavaStringLiteral(printKey.getNamespace()) 
				+ "\", \"" 
				+ JRStringUtil.escapeJavaStringLiteral(printKey.getName()) 
				+ "\");\n");
		write( elementName + ".setGenericType(" + elementName + "Type);\n");
		flush();//genericElementType

		JRGenericElementParameter[] params = element.getParameters();
		for (int i = 0; i < params.length; i++)
		{
			JRGenericElementParameter param = params[i];
			//TODO: constructor protected
//			JRBaseGenericElementParameter p = new JRBaseGenericElementParameter();

//			String paramName =  elementName + "Parameter" + i;
//			write( "JRBaseGenericElementParameter " + paramName + " = new JRBaseGenericElementParameter();\n");
//			write( paramName + ".setName(\"" + JRStringUtil.escapeJavaStringLiteral(params[i].getName()) + "\");\n");
//			writer.addAttribute(JRApiConstants.ATTRIBUTE_skipWhenNull, 
//					param.isSkipWhenEmpty(), false);
//			
//			JRExpression valueExpression = param.getValueExpression();
//			if (valueExpression != null)
//			{
//				writer.writeExpression(JRApiConstants.ELEMENT_genericElementParameter_valueExpression, 
//						valueExpression, true, Object.class.getName());
//			}
//			
//			flush();//genericElementParameter
		}
		
		flush();//genericElement
	}

	public String getColorText(Color color)
	{
		if (color == null)
			return null;
		
		return "new Color(" 
			+ color.getRed() 
			+ ", "
			+ color.getGreen() 
			+ ", "
			+ color.getBlue() 
			+ ", "
			+ color.getAlpha() 
			+ ")";
	}
	
	protected void writeStyleReferenceAttr( JRStyleContainer styleContainer, String styleName)
	{
		if (styleContainer.getStyle() != null)
		{
			write( styleName + ".setParentStyle(\"" + JRStringUtil.escapeJavaStringLiteral(styleContainer.getStyle().getName()) + "\");\n");
		}
		else if (styleContainer.getStyleNameReference() != null)
		{
			write( styleName + ".setParentStyleNameReference(" + styleContainer.getStyleNameReference() + ");\n");
		}
		flush();
	}
	
	/**
	 * 
	 * @param element
	 * @param pen
	 * @throws IOException
	 */
	private void writePen( JRPen pen, String penHolder)
	{
		if(pen != null)
		{
			write( penHolder + ".setLineWidth(" + pen.getLineWidth() + "f);\n");
			write( penHolder + ".setLineStyle((byte)" + pen.getLineStyle() + ");\n");
			write( penHolder + ".setLineColor(" + getColorText(pen.getLineColor()) + ");\n");
			flush();
		}
	}

	/**
	 *
	 */
	protected void writeBox( JRLineBox box, String boxHolder)
	{
		if (box != null)
		{
			write( boxHolder + ".setPadding(Integer.valueOf(" + box.getPadding().intValue() + "));\n");
			write( boxHolder + ".setTopPadding(Integer.valueOf(" + box.getTopPadding().intValue() + "));\n");
			write( boxHolder + ".setLeftPadding(Integer.valueOf(" + box.getLeftPadding().intValue() + "));\n");
			write( boxHolder + ".setBottomPadding(Integer.valueOf(" + box.getBottomPadding().intValue() + "));\n");
			write( boxHolder + ".setRightPadding(Integer.valueOf(" + box.getRightPadding().intValue() + "));\n");
			

			writePen( box.getPen(), boxHolder + ".getPen()");
			writePen( box.getTopPen(), boxHolder + ".getTopPen()");
			writePen( box.getLeftPen(), boxHolder + ".getLeftPen()");
			writePen( box.getBottomPen(), boxHolder + ".getBottomPen()");
			writePen( box.getRightPen(), boxHolder + ".getRightPen()");

			flush();
		}
	}
	
	public void writeExpression( JRExpression expression, String parentName, String expressionSuffix)
	{
		writeExpression( expression, parentName, expressionSuffix, null);
	}
	

	public void writeExpression(
			JRExpression expression, String parentName, String expressionSuffix, String defaultClassName)
	{
		if (expression != null)
		{
			String expressionName = parentName +  expressionSuffix;
			write( "JRDesignExpression " + expressionName + " = new JRDesignExpression();\n");
			write( expressionName + ".setId(" + expression.getId() + ");\n");
			write( expressionName + ".setText(\"" + JRStringUtil.escapeJavaStringLiteral(expression.getText()) + "\");\n");
			if(expression.getValueClass() != null)
				write( expressionName + ".setValueClass(Class.forName(\"" + (expression.getValueClass().getName()) + "\"));\n");
			else if(expression.getValueClassName() != null)
				write( expressionName + ".setValueClassName(\"" + JRStringUtil.escapeJavaStringLiteral(expression.getValueClassName()) + "\");\n");
			else if(defaultClassName != null)
				write( expressionName + ".setValueClassName(\"" + JRStringUtil.escapeJavaStringLiteral(defaultClassName) + "\");\n");

			JRExpressionChunk[] chunks = expression.getChunks();
			if(chunks != null && chunks.length >0)
			{
				String chunksName = "chunks_" + expressionName;
				write( "JRDesignExpressionChunk[] " + chunksName + " = new JRDesignExpressionChunk[" + chunks.length + "];\n");
				for(int i=0; i<chunks.length; i++)
				{
					write( chunksName + "[" + i + "] = new JRDesignExpressionChunk();\n");
					write( chunksName + "[" + i + "].setType((byte)" + chunks[i].getType() + ");\n");
					write( chunksName + "[" + i + "].setText(\"" + JRStringUtil.escapeJavaStringLiteral(chunks[i].getText()) + "\");\n");
					write( expressionName + ".addChunk(" +chunksName + "[" + i + "]);\n");
				}
			}
			write( parentName + ".set" + expressionSuffix + "(" + expressionName + ");\n");
			
			flush();
		}
	}

	private String getGroupName( JRGroup group)
	{
		if(group != null)
		{
			if(groupsMap.get(group.getName()) == null)
			{
				writeGroup( group);
				groupsMap.put(group.getName(), group.getName());
			}
			return group.getName();
		}
		return null;
	}


	/**
	 *
	 */
	protected void write(String text)
	{
		try
		{
			if(text.indexOf("null") < 0)
			{
				writer.write(indent + text);
			}
		}
		catch(IOException e)
		{
			throw new JRRuntimeException(e);
		}
	}

	
	/**
	 *
	 */
	protected void write1(String text)
	{
		try
		{
			writer.write(indent + text);
		}
		catch(IOException e)
		{
			throw new JRRuntimeException(e);
		}
	}

	
	/**
	 *
	 */
	protected void write(String pattern, Object value)
	{
		if (value != null)
		{
			MessageFormat.format(pattern, new Object[]{value});
		}
	}

	
	/**
	 *
	 */
	protected void write(String pattern, int value)
	{
		MessageFormat.format(pattern, new Object[]{new Integer(value)});
	}

	
	/**
	 *
	 */
	protected void write(String pattern, int value, int defaultValue)
	{
		if (value != defaultValue)
		{
			MessageFormat.format(pattern, new Object[]{new Integer(value)});
		}
	}

	
	/**
	 *
	 */
	protected void write(String pattern, boolean value, boolean defaultValue)
	{
		if (value != defaultValue)
		{
			MessageFormat.format(pattern, new Object[]{value ? Boolean.TRUE : Boolean.FALSE});
		}
	}

	
	/**
	 *
	 */
	protected void write(String pattern, byte value, byte defaultValue)
	{
		if (value != defaultValue)
		{
			MessageFormat.format(pattern, new Object[]{new Byte(value)});
		}
	}

	
	/**
	 *
	 */
	protected void close()
	{
		try
		{
			writer.close();
		}
		catch(IOException e)
		{
			throw new JRRuntimeException(e);
		}
	}

	
	/**
	 *
	 */
	protected void flush()
	{
		try
		{
			writer.flush();
		}
		catch(IOException e)
		{
			throw new JRRuntimeException(e);
		}
	}
	
	
	/**
	 * 
	 */
	public static void main(String[] args) 
	{
		if(args.length < 2)
		{
			System.out.println( "JRApiWriter usage:" );
			System.out.println( "\tjava JRApiWriter reportCreatorClassName file" );
			return;
		}
				
		String reportCreatorClassName = args[0];
		String destFileName = args[1];
		
		try
		{
			Class reportCreatorClass = Class.forName(reportCreatorClassName);
			ReportCreator reportCreator = (ReportCreator)reportCreatorClass.newInstance();
			JasperDesign jasperDesign = reportCreator.create();
			JRXmlWriter.writeReport(jasperDesign, destFileName, "UTF-8");
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	
}
