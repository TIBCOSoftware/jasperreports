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
import net.sf.jasperreports.engine.design.JRDesignDataset;
import net.sf.jasperreports.engine.design.JRDesignFrame;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.query.JRJdbcQueryExecuterFactory;

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
	private JRReport report = null;

	/**
	 *
	 */
	private Map stylesMap = new HashMap();

	/**
	 *
	 */
	private Map groupsMap = new HashMap();

	
	private Writer writer = null;
	
	private ApiWriterVisitor apiWriterVisitor = new ApiWriterVisitor(this);

	//TODO: remove this one below
	JasperDesign jasperDesign = null;

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
			: "UTF-8";
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

		write("package net.sf.jasperreports.engine.util;\n");
		write("\n");
		write("import java.awt.Color;\n");
		write("import java.io.IOException;\n");
		write("import java.util.HashMap;\n");
		write("import java.util.HashSet;\n");
		write("import java.util.Iterator;\n");
		write("import java.util.List;\n");
		write("import java.util.Map;\n");
		write("import java.util.Set;\n");
		write("import java.util.SortedSet;\n");
		write("import java.util.TreeSet;\n");
		write("\n");
		write("import org.jfree.chart.plot.PlotOrientation;\n");
		write("\n");
		write("import net.sf.jasperreports.charts.JRCategoryAxisFormat;\n");
		write("import net.sf.jasperreports.charts.design.*;\n");
		write("import net.sf.jasperreports.charts.util.JRAxisFormat;\n");
		write("import net.sf.jasperreports.crosstabs.design.*;\n");
		write("import net.sf.jasperreports.engine.JRException;\n");
		write("import net.sf.jasperreports.engine.design.*;\n");
		write("\n");
		
		
		/*   */
		String[] imports = report.getImports();
		if (imports != null && imports.length > 0)
		{
			for(int i = 0; i < imports.length; i++)
			{
				String value = imports[i];
				if (value != null)
				{
					write("import " + value + ";\n");
				}
			}
		}
		String indent = "";
		write("\n");
		write("\n");
		write("public class " + JRStringUtil.escapeJavaStringLiteral(report.getName()) + "_Generator\n");
		write("{\n");
		write("\n");
		indent += "  ";
		write(indent + "public JasperDesign getJasperDesign() throws JRException\n");
		write(indent + "{\n");
		indent += "  ";
		write(indent + "JasperDesign jasperDesign = new JasperDesign();\n");
		write(indent + "jasperDesign.setName(\"" + JRStringUtil.escapeJavaStringLiteral(report.getName()) + "\");\n");
		write(indent + "jasperDesign.setLanguage(\"" + JRStringUtil.escapeJavaStringLiteral(report.getLanguage()!= null ? report.getLanguage() : JRReport.LANGUAGE_JAVA) + "\");\n");
		write(indent + "jasperDesign.setColumnCount(" + (report.getColumnCount() > 0 ? report.getColumnCount() : 1) + ");\n");
		write(indent + "jasperDesign.setPrintOrder((byte)" + (report.getPrintOrder() > 0 ? report.getPrintOrder() : JRReport.PRINT_ORDER_VERTICAL) + ");\n");
		write(indent + "jasperDesign.setPageWidth(" + report.getPageWidth() + ");\n");
		write(indent + "jasperDesign.setPageHeight(" + report.getPageHeight() + ");\n");
		write(indent + "jasperDesign.setOrientation((byte)" + (report.getOrientation() > 0 ? report.getOrientation() : JRReport.ORIENTATION_PORTRAIT) + ");\n");
		write(indent + "jasperDesign.setWhenNoDataType((byte)" + (report.getWhenNoDataType() > 0 ? report.getWhenNoDataType() : JRReport.WHEN_NO_DATA_TYPE_NO_PAGES) + ");\n");
		write(indent + "jasperDesign.setColumnWidth(" + report.getColumnWidth() + ");\n");
		write(indent + "jasperDesign.setColumnSpacing(" + report.getColumnSpacing() + ");\n");
		write(indent + "jasperDesign.setLeftMargin(" + report.getLeftMargin() + ");\n");
		write(indent + "jasperDesign.setRightMargin(" + report.getRightMargin() + ");\n");
		write(indent + "jasperDesign.setTopMargin(" + report.getTopMargin() + ");\n");
		write(indent + "jasperDesign.setBottomMargin(" + report.getBottomMargin() + ");\n");
		write(indent + "jasperDesign.setTitleNewPage(" + (report.isTitleNewPage() ? report.isTitleNewPage() : false) + ");\n");
		write(indent + "jasperDesign.setSummaryNewPage(" + (report.isSummaryNewPage() ? report.isSummaryNewPage() : false) + ");\n");
		write(indent + "jasperDesign.setSummaryWithPageHeaderAndFooter(" + (report.isSummaryWithPageHeaderAndFooter() ? report.isSummaryWithPageHeaderAndFooter() : false) + ");\n");
		write(indent + "jasperDesign.setFloatColumnFooter(" + (report.isFloatColumnFooter() ? report.isFloatColumnFooter() : false) + ");\n");
		if(report.getScriptletClass() != null)
			write(indent + "jasperDesign.setScriptletClass(\"" + report.getScriptletClass() + "\");\n");
		if(report.getFormatFactoryClass() != null)
			write(indent + "jasperDesign.setFormatFactoryClass(\"" + report.getFormatFactoryClass() + "\");\n");
		if(report.getResourceBundle() != null)
			write(indent + "jasperDesign.setgetResourceBundle(\"" + JRStringUtil.escapeJavaStringLiteral(report.getResourceBundle()) + "\");\n");
		write(indent + "jasperDesign.setWhenResourceMissingType((byte)" + (report.getWhenResourceMissingType() > 0 ? report.getWhenResourceMissingType() : JRReport.WHEN_RESOURCE_MISSING_TYPE_NULL) + ");\n");
		write(indent + "jasperDesign.setIgnorePagination(" + (report.isIgnorePagination() ? report.isIgnorePagination() : false) + ");\n\n");

		writeProperties(indent, report, "jasperDesign");
		write("\n");
		writeTemplates(indent);

		write("\n");
		
		JRReportFont[] fonts = report.getFonts();
		if (fonts != null && fonts.length > 0)
		{	
			write(indent + "//report fonts\n\n");
			for(int i = 0; i < fonts.length; i++)
			{
				writeReportFont(indent, fonts[i], "reportFontStyle"+i);
				write(indent + "jasperDesign.addStyle(reportFontStyle" + i + ");\n\n");
				flush();
			}
		}

		JRStyle[] styles = report.getStyles();
		if (styles != null && styles.length > 0)
		{	
			write(indent + "//styles\n");

			for(int i = 0; i < styles.length; i++)
			{
				writeStyle(indent, styles[i], "reportStyle" + i);
				write(indent + "jasperDesign.addStyle(reportStyle" + i + ");\n\n");

				if (toWriteConditionalStyles())
				{
					JRConditionalStyle[] conditionalStyles = styles[i].getConditionalStyles();
					if (!(styles[i] instanceof JRConditionalStyle) && conditionalStyles != null)
					{
						for (int j = 0; j < conditionalStyles.length; j++)
						{
							String conditionalStyleName = "reportStyle" + i + "Conditional" + j;
							writeConditionalStyle(indent, conditionalStyles[j],conditionalStyleName);
							write(indent + "jasperDesign.addStyle(" + conditionalStyleName + ");\n\n");
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
			write(indent + "//datasets\n");
			for (int i = 0; i < datasets.length; ++i)
			{
				writeDataset(indent, datasets[i], "reportDataset" + i);
				if(datasets[i] != null)
					write(indent + "jasperDesign.addDataset(reportDataset" + i + ");\n");
			}
			write("\n");
			flush();
		}

		if(report.getMainDataset() != null)
		{
			writeDataset(indent, report.getMainDataset(), "reportMainDataset");
			write(indent + "jasperDesign.setMainDataset(reportMainDataset);\n");
		}

		if (report.getBackground() != null)
		{
			write(indent + "//background\n\n");
			writeBand(indent, report.getBackground(), "backgroundBand");
			write(indent + "jasperDesign.setBackground(backgroundBand);\n\n");
		}

		if (report.getTitle() != null)
		{
			write(indent + "//title\n\n");
			writeBand(indent, report.getTitle(), "titleBand");
			write(indent + "jasperDesign.setTitle(titleBand);\n\n");
		}

		if (report.getPageHeader() != null)
		{
			write(indent + "//page header\n\n");
			writeBand(indent, report.getPageHeader(), "pageHeaderBand");
			write(indent + "jasperDesign.setPageHeader(pageHeaderBand);\n\n");
		}

		if (report.getColumnHeader() != null)
		{
			write(indent + "//column header\n\n");
			writeBand(indent, report.getColumnHeader(), "columnHeaderBand");
			write(indent + "jasperDesign.setColumnHeader(columnHeaderBand);\n\n");
		}

		JRSection detail = report.getDetailSection();
		if (detail != null && detail.getBands() != null && detail.getBands().length > 0)
		{
			writeSection(
					indent, 
					detail, 
					"detailBand",
					indent + "((JRDesignSection)jasperDesign.getDetailSection()).getBandsList()"
					);
		}

		if (report.getColumnFooter() != null)
		{
			write(indent + "//column footer\n\n");
			writeBand(indent, report.getColumnFooter(), "columnFooterBand");
			write(indent + "jasperDesign.setColumnFooter(columnFooterBand);\n\n");
		}

		if (report.getPageFooter() != null)
		{
			write(indent + "//page footer\n\n");
			writeBand(indent, report.getPageFooter(), "pageFooterBand");
			write(indent + "jasperDesign.setPageFooter(pageFooterBand);\n\n");
		}

		if (report.getLastPageFooter() != null)
		{
			write(indent + "//last page footer\n\n");
			writeBand(indent, report.getLastPageFooter(), "lastPageFooterBand");
			write(indent + "jasperDesign.setLastPageFooter(lastPageFooterBand);\n\n");
		}

		if (report.getSummary() != null)
		{
			write(indent + "//summary\n\n");
			writeBand(indent, report.getSummary(), "summaryBand");
			write(indent + "jasperDesign.setSummary(summaryBand);\n\n");
		}

		if (report.getNoData() != null)
		{
			write(indent + "//no data\n\n");
			writeBand(indent, report.getNoData(), "noDataBand");
			write(indent + "jasperDesign.setNoData(noDataBand);\n\n");
		}

		write(indent + "return jasperDesign;\n");
		write("  }\n");
		write("\n");
		write("}\n");
		
		flush();//FIXME is this necessary?
		close();
	}


	/**
	 * 
	 *
	 */
	private void writeProperties(String indent, JRPropertiesHolder propertiesHolder, String propertiesHolderName)
	{
		if (propertiesHolder.hasProperties())
		{
			JRPropertiesMap propertiesMap = propertiesHolder.getPropertiesMap();
			String[] propertyNames = propertiesMap.getPropertyNames();
			if (propertyNames != null && propertyNames.length > 0)
			{
				write(indent + "//properties\n");
				for(int i = 0; i < propertyNames.length; i++)
				{
					String value = propertiesMap.getProperty(propertyNames[i]);
					if (value != null)
					{
						write(indent + propertiesHolderName + ".setProperty(\"" + propertyNames[i] + "\", \"" + JRStringUtil.escapeJavaStringLiteral(value) + "\");\n");
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
				writeTemplate(indent, template, "reportTemplate" + i);
				write(indent + "jasperDesign.addTemplate(reportTemplate" + i + ");\n");
			}
			write("\n");
			flush();
		}
	}


	/**
	 * 
	 */
	protected void writeTemplate(String indent, JRReportTemplate template, String templateName)
	{
		write(indent + "JRDesignReportTemplate " + templateName + " = new JRDesignReportTemplate();\n");
		writeExpression(indent, template.getSourceExpression(), templateName, "SourceExpression", String.class.getName());
		flush();
	}


	/**
	 *
	 */
	private void writeReportFont(String indent, JRReportFont font, String styleName)
	{
		if (font != null && stylesMap.get(font.getName()) == null)
		{
			write(indent + styleName + ".setName(\"" + JRStringUtil.escapeJavaStringLiteral(font.getName()) + "\");\n");
			write(indent + styleName + ".setDefault(" + font.isDefault() + ");\n");
			write(indent + styleName + ".setFontName(\"" + JRStringUtil.escapeJavaStringLiteral(font.getFontName()) + "\");\n");
			write(indent + styleName + ".setFontSize(" + font.getFontSize() + ");\n");
			write(indent + styleName + ".setBold(" + font.isBold() + ");\n");
			write(indent + styleName + ".setItalic(" + font.isItalic() + ");\n");
			write(indent + styleName + ".setUnderline(" + font.isUnderline() + ");\n");
			write(indent + styleName + ".setStrikeThrough(" + font.isStrikeThrough() + ");\n");
			write(indent + styleName + ".setPdfFontName(\"" + JRStringUtil.escapeJavaStringLiteral(font.getPdfFontName()) + "\");\n");
			write(indent + styleName + ".setPdfEncoding(\"" + font.getPdfEncoding() + "\");\n");
			write(indent + styleName + ".setPdfEmbedded(" + font.isPdfEmbedded() + ");\n");
			stylesMap.put(font.getName(), styleName);
			flush();
		}
	}


	/**
	 *
	 */
	private void writeScriptlet(String indent, JRScriptlet scriptlet, String scriptletName)
	{
		if(scriptlet != null)
		{
			write(indent + "JRDesignScriptlet " + scriptletName + " = new JRDesignScriptlet();\n");
			write(indent + scriptletName + ".setDescription(\"" + JRStringUtil.escapeJavaStringLiteral(scriptlet.getDescription()) + "\");\n");
			write(indent + scriptletName + ".setName(\"" + JRStringUtil.escapeJavaStringLiteral(scriptlet.getName()) + "\");\n");
			if(scriptlet.getValueClass() != null)
				write(indent + scriptletName + ".setValueClass(Class.forName(\"" + JRStringUtil.escapeJavaStringLiteral(scriptlet.getValueClass().getName()) + "\"));\n");
			else if(scriptlet.getValueClassName() != null)
				write(indent + scriptletName + ".setValueClassName(\"" + JRStringUtil.escapeJavaStringLiteral(scriptlet.getValueClassName()) + "\");\n");
	
			writeProperties(indent, scriptlet, scriptletName);
			flush();
		}
	}


	/**
	 *
	 */
	private void writeParameter(String indent, JRParameter parameter,  String parameterName)
	{
		if(parameter != null)
		{
			write(indent + "JRDesignParameter " + parameterName + " = new JRDesignParameter();\n");
			write(indent + parameterName + ".setName(\"" + JRStringUtil.escapeJavaStringLiteral(parameter.getName()) + "\");\n");
			write(indent + parameterName + ".setDescription(\"" + JRStringUtil.escapeJavaStringLiteral(parameter.getDescription()) + "\");\n");
			
			if(parameter.getValueClass() != null)
				write(indent + parameterName + ".setValueClass(Class.forName(\"" + JRStringUtil.escapeJavaStringLiteral(parameter.getValueClass().getName()) + "\"));\n");
			else if(parameter.getValueClassName() != null)
				write(indent + parameterName + ".setValueClassName(\"" + JRStringUtil.escapeJavaStringLiteral(parameter.getValueClassName()) + "\");\n");
			
			if(parameter.getNestedType() != null)
				write(indent + parameterName + ".setNestedType(Class.forName(\"" + JRStringUtil.escapeJavaStringLiteral(parameter.getNestedType().getName()) + "\"));\n");
			else if(parameter.getNestedTypeName() != null)
				write(indent + parameterName + ".setNestedTypeName(\"" + JRStringUtil.escapeJavaStringLiteral(parameter.getNestedTypeName()) + "\");\n");
			
			write(indent + parameterName + ".setForPrompting(" + parameter.isForPrompting() + ");\n");
	
			writeProperties(indent, parameter, parameterName);
	
			writeExpression(indent, parameter.getDefaultValueExpression(), parameterName, "DefaultValueExpression");
			flush();
		}
	}


	/**
	 *
	 */
	private void writeQuery(String indent, JRQuery query, String queryName)
	{
		if(query != null)
		{
			write(indent + "JRDesignQuery " + queryName + " = new JRDesignQuery();\n");
			write(indent + queryName + ".setLanguage(\"" + JRStringUtil.escapeJavaStringLiteral(query.getLanguage() != null ? query.getLanguage() : JRJdbcQueryExecuterFactory.QUERY_LANGUAGE_SQL) + "\");\n");
			write(indent + queryName + ".setText(\"" + JRStringUtil.escapeJavaStringLiteral(query.getText()) + "\");\n");
			flush();
		}
	}


	/**
	 *
	 */
	private void writeField(String indent, JRField field, String fieldName)
	{
		if(field != null)
		{
			write(indent + "JRDesignField " + fieldName + " = new JRDesignField();\n");
			write(indent + fieldName + ".setName(\"" + JRStringUtil.escapeJavaStringLiteral(field.getName()) + "\");\n");
			write(indent + fieldName + ".setDescription(\"" + JRStringUtil.escapeJavaStringLiteral(field.getDescription()) + "\");\n");
			if(field.getValueClass() != null)
				write(indent + fieldName + ".setValueClass(Class.forName(\"" + JRStringUtil.escapeJavaStringLiteral(field.getValueClass().getName()) + "\"));\n");
			else if(field.getValueClassName() != null)
				write(indent + fieldName + ".setValueClassName(\"" + JRStringUtil.escapeJavaStringLiteral(field.getValueClassName()) + "\");\n");
			writeProperties(indent, field, fieldName);
			flush();
		}
	}


	/**
	 *
	 */
	private void writeSortField(String indent, JRSortField sortField, String sortFieldName)
	{
		if(sortField != null)
		{
			write(indent + "JRDesignSortField " + sortFieldName + " = new JRDesignSortField();\n");
			write(indent + sortFieldName + ".setName(\"" + JRStringUtil.escapeJavaStringLiteral(sortField.getName()) + "\");\n");
			write(indent + sortFieldName + ".setOrder((byte)" + sortField.getOrder() + ");\n");
			flush();
		}
	}


	/**
	 *
	 */
	private void writeVariable(String indent, JRVariable variable, String variableName)
	{
		if(variable != null)
		{
			String resetGroupName = getGroupName(indent, variable.getResetGroup());
			String incrementGroupName = getGroupName(indent, variable.getIncrementGroup());
			
			write(indent + "JRDesignVariable " + variableName + " = new JRDesignVariable();\n");
			write(indent + variableName + ".setName(\"" + JRStringUtil.escapeJavaStringLiteral(variable.getName()) + "\");\n");
			if(variable.getValueClass() != null)
				write(indent + variableName + ".setValueClass(Class.forName(\"" + JRStringUtil.escapeJavaStringLiteral(variable.getValueClass().getName()) + "\"));\n");
			else if(variable.getValueClassName() != null)
				write(indent + variableName + ".setValueClassName(\"" + JRStringUtil.escapeJavaStringLiteral(variable.getValueClassName()) + "\");\n");
			write(indent + variableName + ".setResetType((byte)" + (variable.getResetType() < 1 ?  JRVariable.RESET_TYPE_REPORT : variable.getResetType()) + ");\n");
			if (resetGroupName != null)
			{
				write(indent + variableName + ".setResetGroup(" + resetGroupName + ");\n");
				
			}
			write(indent + variableName + ".setIncrementType((byte)" + (variable.getIncrementType() < 1 ?  JRVariable.RESET_TYPE_NONE : variable.getIncrementType()) + ");\n");
			if (incrementGroupName != null)
			{
				write(indent + variableName + ".setResetGroup(" + incrementGroupName + ");\n");
			}

			write(indent + variableName + ".setCalculation(" + (variable.getCalculation() < 0 ?  JRVariable.CALCULATION_NOTHING : variable.getCalculation()) + ");\n");
			write(indent + variableName + ".setIncrementerFactoryClass(Class.forName(\"" + JRStringUtil.escapeJavaStringLiteral(variable.getIncrementerFactoryClassName()) + "\"));\n");
			writeExpression(indent, variable.getExpression(), variableName, "Expression");
			writeExpression(indent, variable.getInitialValueExpression(), variableName, "InitialValueExpression");
			flush();
		}
	}


	/**
	 *
	 */
	private void writeGroup(String indent, JRGroup group)
	{
		write(indent + "JRDesignGroup " + group.getName() + "ResetGroup = new JRDesignGroup();\n");
		String groupName = group.getName();
		write(indent + groupName + ".setName(\"" + JRStringUtil.escapeJavaStringLiteral(groupName) + "\");\n");
		write(indent + groupName + ".setStartNewColumn(" + group.isStartNewColumn() + ");\n");
		write(indent + groupName + ".setStartNewPage(" + group.isStartNewPage() + ");\n");
		write(indent + groupName + ".setReprintHeaderOnEachPage(" + group.isReprintHeaderOnEachPage() + ");\n");
		write(indent + groupName + ".setMinHeightToStartNewPage(" + group.getMinHeightToStartNewPage() + ");\n");
		write(indent + groupName + ".setFooterPosition(" + (group.getFooterPosition() < 1 ? JRGroup.FOOTER_POSITION_NORMAL : group.getFooterPosition()) + ");\n");
		write(indent + groupName + ".setKeepTogether(" + group.isKeepTogether() + ");\n");

		writeExpression(indent, group.getExpression(), groupName, "Expression");

		JRSection groupHeader = group.getGroupHeaderSection();
		if (groupHeader != null)
		{
			writeSection(
					indent, 
					groupHeader, 
					groupName+"Header", 
					indent + "((JRDesignSection)" + groupName + ".getGroupHeaderSection()).getBandsList()"
					);
		}

		JRSection groupFooter = group.getGroupFooterSection();
		if (groupFooter != null)
		{
			writeSection(
					indent, 
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
	protected void writeSection(String indent, JRSection section, String sectionName, String sectionBandListGetterName)
	{
		if (section != null)
		{
			JRBand[] bands = section.getBands();
			if (bands != null && bands.length > 0)
			{
				write(indent + "//" + sectionName + "\n\n");
				for(int i = 0; i < bands.length; i++)
				{
					writeBand(indent, bands[i], sectionName + i);
					write(indent + sectionBandListGetterName + ".add(" + i + ", " + sectionName + i + ");\n\n");
				}
			}
			flush();
		}
	}


	/**
	 *
	 */
	private void writeBand(String indent, JRBand band, String bandName)
	{
		if(band != null)
		{
			
			write(indent + "//band name = " + bandName +"\n\n");
			write(indent + "JRDesignBand " + bandName + " = new JRDesignBand();\n");
			write(indent + bandName + ".setHeight(" + band.getHeight() + ");\n");
			write(indent + bandName + ".setSplitType(Byte.valueOf(" + band.getSplitType() + "));\n");
			writeExpression(indent, band.getPrintWhenExpression(), bandName, "PrintWhenExpression");

			writeChildElements(indent, band, bandName);
	
			flush();
		}
	}

	
	/**
	 * Writes the contents (child elements) of an element container.
	 * 
	 * @param elementContainer the element container
	 */
	public void writeChildElements(String indent, JRElementGroup elementContainer, String parentName)
	{
		List children = elementContainer.getChildren();
		if (children != null && children.size() > 0)
		{
			apiWriterVisitor.setIndent(indent);
			for(int i = 0; i < children.size(); i++)
			{
				apiWriterVisitor.setName(parentName + i);
				((JRChild) children.get(i)).visit(apiWriterVisitor);
				write(indent + parentName +".addElement(" + parentName + i + ");\n\n");
			}
		}
	}

	/**
	 *
	 */
	public void writeElementGroup(String indent, JRElementGroup elementGroup, String groupName)
	{ 
		if(elementGroup != null)
		{
			write(indent + "JRDesignElementGroup " + groupName + " = new JRDesignElementGroup();\n");
			writeChildElements(indent, elementGroup, groupName);
	
			flush();
		}
	}

	/**
	 *
	 */
	public void writeBreak(String indent, JRBreak breakElement, String breakName)
	{
		if(breakElement != null)
		{
			write(indent + "JRDesignBreak " + breakName + " = new JRDesignBreak(jasperDesign);\n");
			write(indent + breakName + ".setType((byte)" + (breakElement.getType() > 0 ? breakElement.getType() : JRBreak.TYPE_PAGE) + ");\n");
			writeReportElement(indent, breakElement, breakName);
			flush();
		}
	}


	/**
	 *
	 */
	public void writeLine(String indent, JRLine line, String lineName)
	{
		if(line != null)
		{
			write(indent + "JRDesignLine " + lineName + " = new JRDesignLine(jasperDesign);\n");
			write(indent + lineName + ".setDirection((byte)" + (line.getDirection() > 0 ? line.getDirection() : JRLine.DIRECTION_TOP_DOWN) + ");\n");
			writeReportElement(indent, line, lineName);
			writeGraphicElement(indent, line, lineName);
			flush();
		}
	}


	/**
	 *
	 */
	private void writeReportElement(String indent, JRElement element, String elementName)
	{
		if(element != null)
		{
			if(element.getKey() != null)
				write(indent + elementName + ".setKey(\"" + JRStringUtil.escapeJavaStringLiteral(element.getKey()) + "\");\n");
			writeStyleReferenceAttr(indent, element, elementName);
			write(indent + elementName + ".setPositionType((byte)" + (element.getPositionType() > 0 ? element.getPositionType() : JRElement.POSITION_TYPE_FIX_RELATIVE_TO_TOP) + ");\n");
			write(indent + elementName + ".setStretchType((byte)" + (element.getStretchType() > -1 ? element.getStretchType() : JRElement.STRETCH_TYPE_NO_STRETCH) + ");\n");
			write(indent + elementName + ".setPrintRepeatedValues(" + element.isPrintRepeatedValues() + ");\n");
			write(indent + elementName + ".setMode((byte)" + element.getMode() + ");\n");
			write(indent + elementName + ".setX(" + element.getX() + ");\n");
			write(indent + elementName + ".setY(" + element.getY() + ");\n");
			write(indent + elementName + ".setWidth(" + element.getWidth() + ");\n");
			write(indent + elementName + ".setHeight(" + element.getHeight() + ");\n");
			write(indent + elementName + ".setRemoveLineWhenBlank(" + element.isRemoveLineWhenBlank() + ");\n");
			write(indent + elementName + ".setPrintInFirstWholeBand(" + element.isPrintInFirstWholeBand() + ");\n");
			write(indent + elementName + ".setPrintWhenDetailOverflows(" + element.isPrintWhenDetailOverflows() + ");\n");

			if (element.getPrintWhenGroupChanges() != null)
			{
				String groupName = getGroupName(indent, element.getPrintWhenGroupChanges());
				write(indent + elementName + ".setPrintWhenGroupChanges(" + groupName + ");\n");
			}
			
			write(indent + elementName + ".setForecolor(" + getColorText(element.getForecolor()) + ");\n");
			write(indent + elementName + ".setBackcolor(" + getColorText(element.getBackcolor()) + ");\n");
	
			writeProperties(indent, element, elementName + ".getPropertiesMap()");
			writePropertyExpressions(indent, element.getPropertyExpressions(), elementName);
			writeExpression(indent, element.getPrintWhenExpression(), elementName, "PrintWhenExpression");
			flush();
		}
	}


	/**
	 * 
	 */
	protected void writePropertyExpressions(String indent, JRPropertyExpression[] propertyExpressions, String propertyHolderName)
	{
		if (propertyExpressions != null && propertyExpressions.length > 0)
		{
			for (int i = 0; i < propertyExpressions.length; i++)
			{
				writePropertyExpression(indent, propertyExpressions[i], propertyHolderName + "PropertyExpression" + i);
				write(indent + propertyHolderName + ".addPropertyExpression(" + propertyHolderName + "PropertyExpression" + i +");\n");
			}
			flush();
		}
	}


	/**
	 * 
	 */
	protected void writePropertyExpression(String indent, JRPropertyExpression propertyExpression, String propertyExpressionName)
	{
		if(propertyExpression != null)
		{
			write(indent + "JRDesignPropertyExpression " + propertyExpressionName + " = new JRDesignPropertyExpression();\n");
			write(indent + propertyExpressionName + ".setName(\"" + JRStringUtil.escapeJavaStringLiteral(propertyExpression.getName()) + "\");\n");
			writeExpression(indent, propertyExpression.getValueExpression(), propertyExpressionName, "ValueExpression");
			
			flush();
		}
	}


	/**
	 *
	 */
	private void writeGraphicElement( String indent, JRGraphicElement element, String elementName)
	{
		if(element != null)
		{
			write(indent + elementName + ".setFill((byte)" + element.getFill() + ");\n");
			writePen(indent, element.getLinePen(), elementName+".getLinePen()");
			flush();
		}
	}


	/**
	 *
	 */
	public void writeRectangle(String indent, JRRectangle rectangle, String rectangleName)
	{
		if(rectangle != null)
		{
			write(indent + "JRDesignRectangle " + rectangleName + " = new JRDesignRectangle(jasperDesign);\n");
			write(indent + rectangleName + ".setRadius(" + rectangle.getRadius() + ");\n");
			writeReportElement(indent, rectangle, rectangleName);
			writeGraphicElement(indent, rectangle, rectangleName);
			flush();
		}
	}


	/**
	 *
	 */
	public void writeEllipse(String indent, JREllipse ellipse, String ellipseName)
	{
		if(ellipse != null)
		{
			write(indent + "JRDesignEllipse " + ellipseName + " = new JRDesignEllipse(jasperDesign);\n");
			writeReportElement(indent, ellipse, ellipseName);
			writeGraphicElement(indent, ellipse, ellipseName);
			flush();
		}
	}


	/**
	 *
	 */
	public void writeImage(String indent, JRImage image, String imageName)
	{
		if(image != null)
		{
			write(indent + "JRDesignImage " + imageName + " = new JRDesignImage(jasperDesign);\n");
			write(indent + imageName + ".setScaleImage(" + image.getScaleImage() + ");\n");
			write(indent + imageName + ".setHorizontalAlignment((byte)" + image.getHorizontalAlignment() + ");\n");
			write(indent + imageName + ".setVerticalAlignment((byte)" + image.getVerticalAlignment() + ");\n");
			write(indent + imageName + ".setUsingCache(" + image.isUsingCache() + ");\n");
			write(indent + imageName + ".setLazy(" + image.isLazy() + ");\n");
			write(indent + imageName + ".setOnErrorType((byte)" + (image.getOnErrorType() > 0 ? image.getOnErrorType() : JRImage.ON_ERROR_TYPE_ERROR) + ");\n");
			write(indent + imageName + ".setEvaluationTime((byte)" + (image.getEvaluationTime() > 0 ? image.getEvaluationTime() : JRExpression.EVALUATION_TIME_NOW) + ");\n");
			if (image.getEvaluationGroup() != null)
			{
				String groupName = getGroupName(indent, image.getEvaluationGroup());
				write(indent + imageName + ".setEvaluationGroup(" + groupName + ");\n");
			}
			write(indent + imageName + ".setLinkType(\"" + JRStringUtil.escapeJavaStringLiteral(image.getLinkType() != null ? image.getLinkType() : JRHyperlinkHelper.HYPERLINK_TYPE_NONE) + "\");\n");
			write(indent + imageName + ".setLinkTarget(\"" + JRStringUtil.escapeJavaStringLiteral(image.getLinkTarget() != null ? image.getLinkTarget() : JRHyperlinkHelper.HYPERLINK_TARGET_SELF) + "\");\n");
			write(indent + imageName + ".setBookmarkLevel(" + (image.getBookmarkLevel() > 0 ? image.getBookmarkLevel() : JRAnchor.NO_BOOKMARK) + ");\n");
			
			writeReportElement(indent, image, imageName);
			writeBox(indent, image.getLineBox(), imageName + ".getLineBox()");
			writeGraphicElement(indent, image, imageName);
			
			writeExpression(indent, image.getExpression(), imageName, "Expression");
			writeExpression(indent, image.getAnchorNameExpression(), imageName, "AnchorNameExpression");
			writeExpression(indent, image.getHyperlinkReferenceExpression(), imageName, "HyperlinkReferenceExpression");
			writeExpression(indent, image.getHyperlinkAnchorExpression(), imageName, "HyperlinkAnchorExpression");
			writeExpression(indent, image.getHyperlinkPageExpression(), imageName, "HyperlinkPageExpression");
			writeExpression(indent, image.getHyperlinkTooltipExpression(), imageName, "HyperlinkTooltipExpression");
			
			writeHyperlinkParameters(indent, image.getHyperlinkParameters(), imageName);
			flush();
		}
	}


	/**
	 *
	 */
	public void writeStaticText(String indent, JRStaticText staticText, String staticTextName)
	{
		if(staticText != null)
		{
			write(indent + "JRDesignStaticText " + staticTextName + " = new JRDesignStaticText(jasperDesign);\n");
			writeReportElement(indent, staticText, staticTextName);
			writeBox(indent, staticText.getLineBox(), staticTextName + ".getLineBox()");
			writeTextElement(indent, staticText, staticTextName);
			write(indent + staticTextName + ".setText(\"" + JRStringUtil.escapeJavaStringLiteral(staticText.getText()) + "\");\n");
			flush();
		}
	}


	/**
	 *
	 */
	private void writeTextElement(String indent, JRTextElement textElement, String textElementName)
	{
		if(textElement != null)
		{
			write(indent + textElementName + ".setHorizontalAlignment((byte)" + textElement.getHorizontalAlignment() + ");\n");
			write(indent + textElementName + ".setVerticalAlignment((byte)" + textElement.getVerticalAlignment() + ");\n");
			write(indent + textElementName + ".setRotation((byte)" + textElement.getRotation() + ");\n");
			write(indent + textElementName + ".setLineSpacing((byte)" + textElement.getLineSpacing() + ");\n");
			write(indent + textElementName + ".setMarkup(\"" + JRStringUtil.escapeJavaStringLiteral(textElement.getMarkup()) + "\");\n");
			writeFont(indent, textElement, textElementName);
			flush();
		}
	}


	/**
	 *
	 */
	private void writeFont(String indent, JRFont font, String fontHolderName)
	{
		if (font != null)
		{
			String reportFontStyle = null;
			
			if (font.getReportFont() != null)
			{
				reportFontStyle =	(String)stylesMap.get(font.getReportFont().getName());
				if(reportFontStyle != null)
				{
					write(indent + fontHolderName + ".setStyle(" + reportFontStyle + ");\n");
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
			
			write(indent + fontHolderName + ".setFontName(\"" + JRStringUtil.escapeJavaStringLiteral(font.getFontName()) + "\");\n");
			write(indent + fontHolderName + ".setFontSize(" + font.getFontSize() + ");\n");
			write(indent + fontHolderName + ".setBold(" + font.isBold() + ");\n");
			write(indent + fontHolderName + ".setItalic(" + font.isItalic() + ");\n");
			write(indent + fontHolderName + ".setUnderline(" + font.isUnderline() + ");\n");
			write(indent + fontHolderName + ".setStrikeThrough(" + font.isStrikeThrough() + ");\n");
			write(indent + fontHolderName + ".setPdfFontName(\"" + JRStringUtil.escapeJavaStringLiteral(font.getPdfFontName()) + "\");\n");
			write(indent + fontHolderName + ".setPdfEncoding(\"" + JRStringUtil.escapeJavaStringLiteral(font.getPdfEncoding()) + "\");\n");
			write(indent + fontHolderName + ".setPdfEmbedded(" + font.isPdfEmbedded() + ");\n");
			flush();
		}
	}

	/**
	 *
	 */
	private void writeStyle(String indent, JRStyle style, String styleName)
	{
		if (style != null && stylesMap.get(style.getName()) == null)
		{
			write(indent + "JRDesignStyle " + styleName + " = new JRDesignStyle();\n");
			write(indent + styleName + ".setName(\"" + JRStringUtil.escapeJavaStringLiteral(style.getName()) + "\");\n");
			writeStyleReferenceAttr(indent, style, styleName);
			write(indent + styleName + ".setDefault(" + style.isDefault() + ");\n");
			write(indent + styleName + ".setMode((byte)" + style.getMode().byteValue() + ");\n");
			write(indent + styleName + ".setFontName(\"" + JRStringUtil.escapeJavaStringLiteral(style.getFontName()) + "\");\n");
			write(indent + styleName + ".setFontSize(" + style.getFontSize() + ");\n");
			write(indent + styleName + ".setBold(" + style.isBold() + ");\n");
			write(indent + styleName + ".setItalic(" + style.isItalic() + ");\n");
			write(indent + styleName + ".setUnderline(" + style.isUnderline() + ");\n");
			write(indent + styleName + ".setStrikeThrough(" + style.isStrikeThrough() + ");\n");
			write(indent + styleName + ".setPdfFontName(\"" + JRStringUtil.escapeJavaStringLiteral(style.getPdfFontName()) + "\");\n");
			write(indent + styleName + ".setPdfEncoding(\"" + JRStringUtil.escapeJavaStringLiteral(style.getPdfEncoding()) + "\");\n");
			write(indent + styleName + ".setPdfEmbedded(" + style.isPdfEmbedded() + ");\n");
			write(indent + styleName + ".setForecolor(" + getColorText(style.getForecolor()) + ");\n");
			write(indent + styleName + ".setBackcolor(" + getColorText(style.getBackcolor()) + ");\n");
			write(indent + styleName + ".setFill((byte)" + style.getFill() + ");\n");
			write(indent + styleName + ".setRadius(" + style.getRadius() + ");\n");
			write(indent + styleName + ".setScaleImage((byte)" + style.getScaleImage() + ");\n");
			write(indent + styleName + ".setHorizontalAlignment((byte)" + style.getHorizontalAlignment() + ");\n");
			write(indent + styleName + ".setVerticalAlignment((byte)" + style.getVerticalAlignment() + ");\n");
			write(indent + styleName + ".setRotation((byte)" + style.getRotation() + ");\n");
			write(indent + styleName + ".setLineSpacing((byte)" + style.getLineSpacing() + ");\n");
			write(indent + styleName + ".setMarkup(\"" + JRStringUtil.escapeJavaStringLiteral(style.getMarkup()) + "\");\n");
			if(style.getPattern() != null)
				write(indent + styleName + ".setPattern(\"" + JRStringUtil.escapeJavaStringLiteral(style.getPattern()) + "\");\n");
			write(indent + styleName + ".setBlankWhenNull(" + style.isBlankWhenNull() + ");\n");
//			writePen(indent, style.getLinePen(), styleName + ".getLinePen()");
//			writeBox(indent, style.getLineBox(),styleName + ".getLineBox()");
			stylesMap.put(style.getName(), styleName);
			flush();
		}
	}


	 /**
	 *
	 */
	public void writeTextField(String indent, JRTextField textField, String textFieldName)
	{
		if(textField != null)
		{
			write(indent + "JRDesignTextField " + textFieldName + " = new JRDesignTextField(jasperDesign);\n");
			write(indent + textFieldName + ".setStretchWithOverflow(" + textField.isStretchWithOverflow() + ");\n");
			write(indent + textFieldName + ".setEvaluationTime((byte)" + (textField.getEvaluationTime() > 0 ? textField.getEvaluationTime() : JRExpression.EVALUATION_TIME_NOW) + ");\n");
			String evaluationGroupName = getGroupName(indent, textField.getEvaluationGroup());
			if (evaluationGroupName != null)
			{
				write(indent + textFieldName + ".setEvaluationGroup(" + evaluationGroupName + ");\n");
			}
	
			write(indent + textFieldName + ".setPattern(\"" + JRStringUtil.escapeJavaStringLiteral(textField.getPattern()) + "\");\n");
			write(indent + textFieldName + ".setBlankWhenNull(" + textField.isBlankWhenNull() + ");\n");
			write(indent + textFieldName + ".setLinkType(\"" + JRStringUtil.escapeJavaStringLiteral(textField.getLinkType() != null ? textField.getLinkType() : JRHyperlinkHelper.HYPERLINK_TYPE_NONE) + "\");\n");
			write(indent + textFieldName + ".setLinkTarget(\"" + JRStringUtil.escapeJavaStringLiteral(textField.getLinkTarget() != null ? textField.getLinkTarget() : JRHyperlinkHelper.HYPERLINK_TARGET_SELF) + "\");\n");
			write(indent + textFieldName + ".setBookmarkLevel(" + (textField.getBookmarkLevel() > 0 ? textField.getBookmarkLevel() : JRAnchor.NO_BOOKMARK) + ");\n");
			
			writeReportElement(indent, textField, textFieldName);
			writeBox(indent, textField.getLineBox(), textFieldName + ".getLineBox");
			writeTextElement(indent, textField, textFieldName);
	
			writeExpression(indent, textField.getExpression(), textFieldName, "Expression");
	
			writeExpression(indent, textField.getAnchorNameExpression(), textFieldName, "AnchorNameExpression");
	
			writeExpression(indent, textField.getHyperlinkReferenceExpression(), textFieldName, "HyperlinkReferenceExpression");
	
			writeExpression(indent, textField.getHyperlinkAnchorExpression(), textFieldName, "HyperlinkAnchorExpression");
	
			writeExpression(indent, textField.getHyperlinkPageExpression(), textFieldName, "HyperlinkPageExpression");
	
			writeExpression(indent, textField.getHyperlinkTooltipExpression(), textFieldName, "HyperlinkTooltipExpression");
	
			writeHyperlinkParameters(indent, textField.getHyperlinkParameters(), textFieldName);
	
			flush();
		}
	}


	/**
	 *
	 */
	public void writeSubreport(String indent, JRSubreport subreport, String subreportName)
	{
		if(subreport != null)
		{
			write(indent + "JRDesignSubreport " + subreportName + " = new JRDesignSubreport(jasperDesign);\n");
			write(indent + subreportName + ".setUsingCache(" + subreport.isUsingCache() + ");\n");
			write(indent + subreportName + ".setRunToBottom(" + subreport.isRunToBottom() + ");\n");
			writeReportElement(indent, subreport, subreportName);
	
			writeExpression(indent, subreport.getParametersMapExpression(), subreportName, "ParametersMapExpression");
	
			JRSubreportParameter[] parameters = subreport.getParameters();
			if (parameters != null && parameters.length > 0)
			{
				for(int i = 0; i < parameters.length; i++)
				{
					writeSubreportParameter(indent, parameters[i], subreportName + "Parameter" + i);
					write(indent + subreportName + ".addParameter(" + subreportName + "Parameter" + i + ");\n");
				}
			}

			writeExpression(indent, subreport.getConnectionExpression(), subreportName, "ConnectionExpression");

			writeExpression(indent, subreport.getDataSourceExpression(), subreportName, "DataSourceExpression");

			JRSubreportReturnValue[] returnValues = subreport.getReturnValues();
			if (returnValues != null && returnValues.length > 0)
			{
				for(int i = 0; i < returnValues.length; i++)
				{
					writeSubreportReturnValue(indent, returnValues[i], subreportName + "ReturnValue" + i);
					write(indent + subreportName + ".addReturnValue(" + subreportName + "ReturnValue" + i + ");\n");
				}
			}
	
			writeExpression(indent, subreport.getExpression(), subreportName, "Expression");
	
			flush();
		}
	}


	/**
	 *
	 */
	private void writeSubreportParameter(String indent, JRSubreportParameter subreportParameter, String subreportParameterName)
	{
		if(subreportParameter != null)
		{
			write(indent + "JRDesignSubreportParameter " + subreportParameterName + " = new JRDesignSubreportParameter();\n");
			write(indent + subreportParameterName + ".setName(\"" + JRStringUtil.escapeJavaStringLiteral(subreportParameter.getName()) + "\");\n");
			writeExpression(indent, subreportParameter.getExpression(), subreportParameterName, "Expression");
			flush();
		}
	}

	/**
	 *
	 */
	private void writeDatasetParameter(String indent, JRDatasetParameter datasetParameter, String datasetParameterName)
	{
		if(datasetParameter != null)
		{
			write(indent + "JRDesignDatasetParameter " + datasetParameterName + " = new JRDesignSubreportParameter();\n");
			write(indent + datasetParameterName + ".setName(\"" + JRStringUtil.escapeJavaStringLiteral(datasetParameter.getName()) + "\");\n");
			writeExpression(indent, datasetParameter.getExpression(), datasetParameterName, "Expression");
			flush();
		}
	}
	
	/**
	 *
	 */
	private void writeChart(String indent, JRChart chart, String chartName)
	{
		if(chart != null)
		{
			write(indent + chartName + ".setShowLegend(Boolean.valueOf(" + chart.getShowLegend() + "));\n");
			write(indent + chartName + ".setEvaluationTime((byte)" + (chart.getEvaluationTime() > 0 ? chart.getEvaluationTime() : JRExpression.EVALUATION_TIME_NOW) + ");\n");

			
			if (chart.getEvaluationTime() == JRExpression.EVALUATION_TIME_GROUP)
			{
				String evaluationGroupName = getGroupName(indent, chart.getEvaluationGroup());
				write(indent + chartName + ".setEvaluationGroup(" + evaluationGroupName + ");\n");
			}
	
			write(indent + chartName + ".setLinkType(\"" + JRStringUtil.escapeJavaStringLiteral(chart.getLinkType() != null ? chart.getLinkType() : JRHyperlinkHelper.HYPERLINK_TYPE_NONE) + "\");\n");
			write(indent + chartName + ".setLinkTarget(\"" + JRStringUtil.escapeJavaStringLiteral(chart.getLinkTarget() != null ? chart.getLinkTarget() : JRHyperlinkHelper.HYPERLINK_TARGET_SELF) + "\");\n");
			write(indent + chartName + ".setBookmarkLevel(" + (chart.getBookmarkLevel() > 0 ? chart.getBookmarkLevel() : JRAnchor.NO_BOOKMARK) + ");\n");

			if(chart.getCustomizerClass() != null)
			{
				write(indent + chartName + ".setCustomizerClass(\"" + JRStringUtil.escapeJavaStringLiteral(chart.getCustomizerClass()) + "\");\n");
			}
			write(indent + chartName + ".setRenderType(\"" + JRStringUtil.escapeJavaStringLiteral(chart.getRenderType()) + "\");\n");
			write(indent + chartName + ".setTheme(\"" + JRStringUtil.escapeJavaStringLiteral(chart.getTheme()) + "\");\n");

			writeReportElement(indent, chart, chartName);
			writeBox(indent, chart.getLineBox(), chartName + ".getLineBox()");
	
			write(indent + chartName + ".setTitlePosition(Byte.valueOf((byte)" + chart.getTitlePositionByte() + "));\n");
			if (chart.getSubtitleColor() != null)
				write(indent + chartName + ".setTitleColor(" + getColorText(chart.getOwnTitleColor()) + ");\n");
			
			writeFont(indent, chart.getTitleFont(), chartName + ".getTitleFont()");

			writeExpression(indent, chart.getTitleExpression(), chartName, "TitleExpression");
	
			if (chart.getSubtitleColor() != null)
				write(indent + chartName + ".setSubtitleColor(" + getColorText(chart.getOwnSubtitleColor()) + ");\n");
			
			writeFont(indent, chart.getSubtitleFont(), chartName + ".getSubtitleFont()");

			writeExpression(indent, chart.getSubtitleExpression(), chartName, "SubtitleExpression");
	
			if (chart.getOwnLegendColor() != null)
				write(indent + chartName + ".setLegendColor(" + getColorText(chart.getOwnLegendColor()) + ");\n");
			if (chart.getOwnLegendBackgroundColor() != null)
				write(indent + chartName + ".setLegendBackgroundColor(" + getColorText(chart.getOwnLegendBackgroundColor()) + ");\n");
			
			write(indent + chartName + ".setLegendPosition(Byte.valueOf((byte)" + chart.getLegendPositionByte() + "));\n");

			writeFont(indent, chart.getLegendFont(), chartName + ".getLegendFont()");
	
			writeExpression(indent, chart.getAnchorNameExpression(), chartName, "AnchorNameExpression");
			writeExpression(indent, chart.getHyperlinkReferenceExpression(), chartName, "HyperlinkReferenceExpression");
			writeExpression(indent, chart.getHyperlinkAnchorExpression(), chartName, "HyperlinkAnchorExpression");
			writeExpression(indent, chart.getHyperlinkPageExpression(), chartName, "HyperlinkPageExpression");
			writeExpression(indent, chart.getHyperlinkTooltipExpression(), chartName, "HyperlinkTooltipExpression");
			writeHyperlinkParameters(indent, chart.getHyperlinkParameters(), chartName);
	
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
	public void writeElementDataset(String indent, JRElementDataset dataset, String datasetName)
	{
		writeElementDataset(indent, dataset, true, datasetName);
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
	public void writeElementDataset(String indent, JRElementDataset dataset, boolean skipIfEmpty, String datasetName)
	{
		if(dataset != null)
		{
			write(indent + datasetName + ".setResetType((byte)" + (dataset.getResetType() > 0 ? dataset.getResetType() : JRVariable.RESET_TYPE_REPORT) + ");\n");
	
			if (dataset.getResetType() == JRVariable.RESET_TYPE_GROUP)
			{
				String resetGroupName = getGroupName(indent,  dataset.getResetGroup());
				write(indent + datasetName + ".setResetGroup(" + resetGroupName + ");\n");
			}
			write(indent + datasetName + ".setIncrementType((byte)" + (dataset.getIncrementType() > 0 ? dataset.getIncrementType() : JRVariable.RESET_TYPE_NONE) + ");\n");
	
			if (dataset.getIncrementType() == JRVariable.RESET_TYPE_GROUP)
			{
				String incrementGroupName = getGroupName(indent,  dataset.getIncrementGroup());
				write(indent + datasetName + ".setIncrementGroup(" + incrementGroupName + ");\n");
			}
	
			writeExpression(indent, dataset.getIncrementWhenExpression(), datasetName, "IncrementWhenExpression");
	
			JRDatasetRun datasetRun = dataset.getDatasetRun();
			if (datasetRun != null)
			{
				writeDatasetRun(indent, datasetRun, datasetName);
			}
			flush();
		}
	}


	/**
	 *
	 */
	private void writeCategoryDataSet(String indent, JRCategoryDataset dataset, String parentName, String datasetNameSuffix)
	{
		if(dataset != null)
		{
			String datasetName = parentName + datasetNameSuffix;
			write(indent + "JRDesignCategoryDataset " + datasetName + " = (JRDesignCategoryDataset)" + parentName + ".getDataset();\n");
	
			writeElementDataset(indent, dataset, datasetName);
	
			JRCategorySeries[] categorySeries = dataset.getSeries();
			if (categorySeries != null && categorySeries.length > 0)
			{
				for(int i = 0; i < categorySeries.length; i++)
				{
					writeCategorySeries(indent, categorySeries[i], datasetName, i);
				}
			}
	
			write(indent + parentName + ".setDataset(" + datasetName + ");\n");
			flush();
		}
	}


	/**
	 * 
	 */
	private void writeTimeSeriesDataset(String indent, JRTimeSeriesDataset dataset, String parentName, String datasetNameSuffix)
	{
		if(dataset != null)
		{
			String datasetName = parentName + datasetNameSuffix;
			write(indent + "JRDesignTimeSeriesDataset " + datasetName + " = (JRDesignTimeSeriesDataset)" + parentName + ".getDataset();\n");
			if (dataset.getTimePeriod() != null && !Day.class.getName().equals(dataset.getTimePeriod().getName()))
			{
				write(indent + datasetName + ".setTimePeriod(Class.forName(\"" + dataset.getTimePeriod().getName() + "\"));\n");
			}
	
			writeElementDataset(indent, dataset, datasetName);
	
			JRTimeSeries[] timeSeries = dataset.getSeries();
			if( timeSeries != null && timeSeries.length > 0 )
			{
				for( int i = 0; i < timeSeries.length; i++ )
			{
					writeTimeSeries(indent, timeSeries[i], datasetName, i  );
				}
			}

			write(indent + parentName + ".setDataset(" + datasetName + ");\n");
			flush();
		}
	}


	/**
	 * 
	 */
	private void writeGanttDataset(String indent, JRGanttDataset dataset, String parentName, String datasetNameSuffix)
	{
		if(dataset != null)
		{
			String datasetName = parentName + datasetNameSuffix;
			write(indent + "JRDesignGanttDataset " + datasetName + " = (JRDesignGanttDataset)" + parentName + ".getDataset();\n");
			JRDesignGanttDataset d;
			
			writeElementDataset(indent, dataset, datasetName);
	
			JRGanttSeries[] ganttSeries = dataset.getSeries();
			if (ganttSeries != null && ganttSeries.length > 0)
			{
				for(int i = 0; i < ganttSeries.length; i++)
				{
					writeGanttSeries(indent, ganttSeries[i], datasetName, i);
				}
			}
			write(indent + parentName + ".setDataset(" + datasetName + ");\n");
			flush();
		}
	}


	/**
	 * 
	 */
	private void writeTimePeriodDataset(String indent, JRTimePeriodDataset dataset, String parentName, String datasetNameSuffix)
	{
		if(dataset != null)
		{
			String datasetName = parentName + datasetNameSuffix;
			write(indent + "JRDesignTimePeriodDataset " + datasetName + " = (JRDesignTimePeriodDataset)" + parentName + ".getDataset();\n");
			writeElementDataset(indent, dataset, datasetName);
	
			JRTimePeriodSeries[] timePeriodSeries = dataset.getSeries();
			if( timePeriodSeries != null && timePeriodSeries.length > 0 )
			{
				for( int i = 0; i < timePeriodSeries.length; i++ )
				{
					writeTimePeriodSeries(indent, timePeriodSeries[i], datasetName, i);
				}
			}
			write(indent + parentName + ".setDataset(" + datasetName + ");\n");
			flush();
		}
	}


	/**
	 *
	 */
	private void writePieSeries(String indent, JRPieSeries pieSeries, String parentName, int index)
	{
		if(pieSeries != null)
		{
			String pieSeriesName = parentName + "PieSeries" + index;
			write(indent + "JRDesignPieSeries " + pieSeriesName + " = " + parentName + ".getSeries()[" + index + "];\n");
	
			writeExpression(indent, pieSeries.getKeyExpression(), pieSeriesName, "KeyExpression");
			writeExpression(indent, pieSeries.getValueExpression(), pieSeriesName, "ValueExpression");
			writeExpression(indent, pieSeries.getLabelExpression(), pieSeriesName, "LabelExpression");
			writeHyperlink(indent, pieSeries.getSectionHyperlink(),pieSeriesName, "SectionHyperlink");
			write(indent + parentName + ".addPieSeries(" + pieSeriesName + ");\n");
	
			flush();
		}
	}

	/**
	 *
	 */
	private void writeCategorySeries(String indent, JRCategorySeries categorySeries, String parentName, int index)
	{
		if(categorySeries != null)
		{
			String categorySeriesName = parentName + "CategorySeries" + index;
			//TODO: instantiate categorySeries
			write(indent + "JRDesignCategorySeries " + categorySeriesName + " = " + parentName + ".getSeries()[" + index + "];\n");

			writeExpression(indent, categorySeries.getSeriesExpression(), categorySeriesName, "SeriesExpression");
			writeExpression(indent, categorySeries.getCategoryExpression(), categorySeriesName, "CategoryExpression");
			writeExpression(indent, categorySeries.getValueExpression(), categorySeriesName, "ValueExpression");
			writeExpression(indent, categorySeries.getLabelExpression(), categorySeriesName, "LabelExpression");
			writeHyperlink(indent, categorySeries.getItemHyperlink(), categorySeriesName, "ItemHyperlink");
			write(indent + parentName + ".addCategorySeries(" + categorySeriesName + ");\n");
			flush();
		}
	}

	/**
	 *
	 */
	private void writeXyzDataset(String indent, JRXyzDataset dataset, String parentName, String datasetNameSuffix)
	{
		if(dataset != null)
		{
			String datasetName = parentName + datasetNameSuffix;
			write(indent + "JRDesignXyzDataset " + datasetName + " = (JRDesignXyzDataset)" + parentName + ".getDataset();\n");
	
			writeElementDataset(indent, dataset, datasetName);
	
			JRXyzSeries[] series = dataset.getSeries();
			if( series != null && series.length > 0 )
			{
				for( int i = 0; i < series.length; i++ )
				{
					writeXyzSeries(indent, series[i], datasetName, i);
				}
			}
			write(indent + parentName + ".setDataset(" + datasetName + ");\n");
			flush();
		}
	}


	/**
	 *
	 */
	private void writeXyzSeries(String indent, JRXyzSeries series, String parentName, int index)
	{
		if(series != null)
		{
			String xyzSeriesName = parentName + "XyzSeries" + index;
			//TODO: instantiate xyzSeries
//			writer.startElement(JRXmlConstants.ELEMENT_categorySeries);
			write(indent + "JRDesignXyzSeries " + xyzSeriesName + " = " + parentName + ".getSeries()[" + index + "];\n");
	
			writeExpression(indent, series.getSeriesExpression(), xyzSeriesName, "SeriesExpression");
			writeExpression(indent, series.getXValueExpression(), xyzSeriesName, "XValueExpression");
			writeExpression(indent, series.getYValueExpression(), xyzSeriesName, "YValueExpression");
			writeExpression(indent, series.getZValueExpression(), xyzSeriesName, "ZValueExpression");
			writeHyperlink(indent, series.getItemHyperlink(), xyzSeriesName, "ItemHyperlink");
			write(indent + parentName + ".addXyzSeries(" + xyzSeriesName + ");\n");
			flush();
		}
	}


	/**
	 *
	 */
	private void writeXySeries(String indent, JRXySeries xySeries, String parentName, int index)
	{
		if(xySeries != null)
		{
			//TODO: instantiate xySeries
			String xySeriesName = parentName + "XySeries" + index;
			write(indent + "JRDesignXySeries " + xySeriesName + " = " + parentName + ".getSeries()[" + index + "];\n");
	
			writeExpression(indent, xySeries.getSeriesExpression(), xySeriesName, "SeriesExpression");
			writeExpression(indent, xySeries.getXValueExpression(), xySeriesName, "XValueExpression");
			writeExpression(indent, xySeries.getYValueExpression(), xySeriesName, "YValueExpression");
			writeExpression(indent, xySeries.getLabelExpression(), xySeriesName, "LabelExpression");
			writeHyperlink(indent, xySeries.getItemHyperlink(), xySeriesName, "ItemHyperlink");
			write(indent + parentName + ".addXySeries(" + xySeriesName + ");\n");
			flush();
		}
	}


	/**
	 *
	 */
	private void writeXyDataset(String indent, JRXyDataset dataset, String parentName, String datasetNameSuffix)
	{
		if(dataset != null)
		{
			String datasetName = parentName + datasetNameSuffix;
			write(indent + "JRDesignXyDataset " + datasetName + " = (JRDesignXyDataset)" + parentName + ".getDataset();\n");
	
			writeElementDataset(indent, dataset, datasetName);
	
			JRXySeries[] xySeries = dataset.getSeries();
			if (xySeries != null && xySeries.length > 0)
			{
				for(int i = 0; i < xySeries.length; i++)
				{
					writeXySeries(indent, xySeries[i], datasetName, i);
				}
			}
			write(indent + parentName + ".setDataset(" + datasetName + ");\n");
			flush();
		}
	}

	/**
	 *
	 */
	private void writeTimeSeries(String indent, JRTimeSeries timeSeries, String parentName, int index)
	{
		if(timeSeries != null)
		{
			//TODO: instantiate categorySeries
			String timeSeriesName = parentName + "TimeSeries" + index;
			write(indent + "JRDesignTimeSeries " + timeSeriesName + " = " + parentName + ".getSeries()[" + index + "];\n");
			writeExpression(indent, timeSeries.getSeriesExpression(), timeSeriesName, "SeriesExpression");
			writeExpression(indent, timeSeries.getTimePeriodExpression(), timeSeriesName, "TimePeriodExpression");
			writeExpression(indent, timeSeries.getValueExpression(), timeSeriesName, "ValueExpression");
			writeExpression(indent, timeSeries.getLabelExpression(), timeSeriesName, "LabelExpression");
			writeHyperlink(indent, timeSeries.getItemHyperlink(), timeSeriesName, "ItemHyperlink");
			write(indent + parentName + ".addTimeSeries(" + timeSeriesName + ");\n");
			flush();
		}
	}


	/**
	 * 
	 */
	private void writeGanttSeries(String indent, JRGanttSeries ganttSeries, String parentName, int index)
	{
		if(ganttSeries != null)
		{
			//TODO: instantiate ganttSeries
			String ganttSeriesName = parentName + "GanttSeries" + index;
			write(indent + "JRDesignGanttSeries " + ganttSeriesName + " = " + parentName + ".getSeries()[" + index + "];\n");
			
			writeExpression(indent, ganttSeries.getSeriesExpression(), ganttSeriesName, "SeriesExpression");
			writeExpression(indent, ganttSeries.getTaskExpression(), ganttSeriesName, "TaskExpression");
			writeExpression(indent, ganttSeries.getSubtaskExpression(), ganttSeriesName, "SubtaskExpression");
			writeExpression(indent, ganttSeries.getStartDateExpression(), ganttSeriesName, "StartDateExpression");
			writeExpression(indent, ganttSeries.getEndDateExpression(), ganttSeriesName, "EndDateExpression");
			writeExpression(indent, ganttSeries.getPercentExpression(), ganttSeriesName, "PercentExpression");
			writeExpression(indent, ganttSeries.getLabelExpression(), ganttSeriesName, "LabelExpression");
			writeHyperlink(indent, ganttSeries.getItemHyperlink(), ganttSeriesName, "ItemHyperlink");
			write(indent + parentName + ".addGanttSeries(" + ganttSeriesName + ");\n");
			flush();
		}
	}

	/**
	 * 
	 */
	private void writeTimePeriodSeries(String indent, JRTimePeriodSeries timePeriodSeries, String parentName, int index)
	{
		if(timePeriodSeries != null)
		{
			//TODO: instantiate timePeriodSeries
			String timePeriodSeriesName = parentName + "TimePeriodSeries" + index;
			write(indent + "JRDesignTimePeriodSeries " + timePeriodSeriesName + " = " + parentName + ".getSeries()[" + index + "];\n");
			
			writeExpression(indent, timePeriodSeries.getSeriesExpression(), timePeriodSeriesName, "SeriesExpression");
			writeExpression(indent, timePeriodSeries.getStartDateExpression(), timePeriodSeriesName, "StartDateExpression");
			writeExpression(indent, timePeriodSeries.getEndDateExpression(), timePeriodSeriesName, "EndDateExpression");
			writeExpression(indent, timePeriodSeries.getValueExpression(), timePeriodSeriesName, "ValueExpression");
			writeExpression(indent, timePeriodSeries.getLabelExpression(), timePeriodSeriesName, "LabelExpression");
			writeHyperlink(indent, timePeriodSeries.getItemHyperlink(), timePeriodSeriesName, "ItemHyperlink");
			write(indent + parentName + ".addTimePeriodSeries(" + timePeriodSeriesName + ");\n");
			flush();
		}
	}


	/**
	 *
	 */
	public void writePieDataset(String indent, JRPieDataset dataset, String parentName, String datasetNameSuffix)
	{
		if(dataset != null)
		{
			String datasetName = parentName + datasetNameSuffix;
			write(indent + "JRDesignPieDataset " + datasetName + " = (JRDesignPieDataset)" + parentName + ".getDataset();\n");
			write(indent + datasetName + ".setMaxCount(Integer.valueOf(" + dataset.getMaxCount() + "));\n");
			write(indent + datasetName + ".setMinPercentage(Float.valueOf(" + dataset.getMinPercentage() + "f));\n");
	
			writeElementDataset(indent, dataset, datasetName);
	
			JRPieSeries[] pieSeries = dataset.getSeries();
			if (pieSeries != null)
			{
				if (pieSeries.length > 1)
				{
					for(int i = 0; i < pieSeries.length; i++)
					{
						writePieSeries(indent, pieSeries[i], datasetName, i);
					}
				}
				else
				{
					//preserve old syntax of single series pie datasets
					writePieSeries(indent, pieSeries[0], datasetName, 0);
				}
			}
	
			writeExpression(indent, dataset.getOtherKeyExpression(), datasetName, "OtherKeyExpression");
			writeExpression(indent, dataset.getOtherLabelExpression(), datasetName, "OtherLabelExpression");
			writeHyperlink(indent, dataset.getOtherSectionHyperlink(), datasetName, "OtherSectionHyperlink");
			write(indent + parentName + ".setDataset(" + datasetName + ");\n");
			flush();
		}
	}

	/**
	 * Writes the description of a value dataset to the output stream.
	 * @param dataset the value dataset to persist
	 */
	public void writeValueDataset(String indent, JRValueDataset dataset, String parentName, String datasetNameSuffix)
	{
		if(dataset != null)
		{
			String datasetName = parentName + datasetNameSuffix;
			write(indent + "JRDesignValueDataset " + datasetName + " = (JRDesignValueDataset)" +parentName + ".getDataset();\n");
			writeElementDataset(indent, dataset, datasetName);
			writeExpression(indent, dataset.getValueExpression(), datasetName, "ValueExpression");
			write(indent + parentName + ".setDataset(" + datasetName + ");\n");
			flush();
		}
	}


	/**
	 * Writes the description of how to display a value in a valueDataset.
	 *
	 * @param valueDisplay the description to save
	 */
	public void writeValueDisplay(String indent, JRValueDisplay valueDisplay, String parentName)
	{
		if(valueDisplay != null)
		{
			String valueDisplayName = parentName + "ValueDisplay";
			//TODO: instantiate value display
			write(indent + "JRDesignValueDataset " + valueDisplayName + " = " +parentName + ".getValueDisplay();\n");
			
			write(indent + valueDisplayName + ".setColor(" + getColorText(valueDisplay.getColor()) + ");\n");
			if(valueDisplay.getMask() != null)
				write(indent + valueDisplayName + ".setMask(\"" + valueDisplay.getMask() + "\");\n");
	
			writeFont(indent, valueDisplay.getFont(), valueDisplayName + ".getFont()");
			write(indent + parentName + "setValueDisplay(" + valueDisplayName + ");\n");
			
			flush();
		}
	}

	/**
	 * Writes the description of how to display item labels in a category plot.
	 *
	 * @param itemLabel the description to save
	 */
	public void writeItemLabel(String indent, JRItemLabel itemLabel, String parentName, String itemLabelSuffix)
	{
		if(itemLabel != null)
		{
			String itemLabelName = parentName + itemLabelSuffix;
			//TODO: instantiate itemLabel
			write(indent + "JRDesignItemLabel " + itemLabelName + " = " + parentName + ".getItemLabel();\n");
			write(indent + itemLabelName + ".setColor(" + getColorText(itemLabel.getColor()) + ");\n");
			write(indent + itemLabelName + ".setBackgroundColor(" + getColorText(itemLabel.getBackgroundColor()) + ");\n");
			writeFont(indent, itemLabel.getFont(), itemLabelName + ".getFont()");
			write(indent + parentName + ".set" + itemLabelSuffix + "(" + itemLabelName + ");\n");
	
			flush();
		}
	}

	/**
	 * Writes a data range block to the output stream.
	 *
	 * @param dataRange the range to write
	 */
	public void writeDataRange(String indent, JRDataRange dataRange, String parentName, String dataRangeSuffix)
	{
		if(dataRange != null)
		{
			String dataRangeName = parentName + dataRangeSuffix;
			//TODO: instantiate dataRange
			write(indent + "JRDesignDataRange " + dataRangeName + " = " + parentName + ".get" + dataRangeSuffix+ "();\n");
			writeExpression(indent, dataRange.getLowExpression(), dataRangeName, "LowExpression");
			writeExpression(indent, dataRange.getHighExpression(), dataRangeName, "HighExpression");
			write(indent + parentName + ".set" + dataRangeSuffix + "(" + dataRangeName + ");\n");
			flush();
		}
	}


	/**
	 * Writes a meter interval description to the output stream.
	 *
	 * @param interval the interval to write
	 */
	private void writeMeterInterval(String indent, JRMeterInterval interval, String parentName, String meterIntervalName)
	{
		if(interval != null)
		{
			write(indent + "JRMeterInterval " + meterIntervalName + " = new JRMeterInterval();\n");
			write(indent + meterIntervalName + ".setLabel(\"" + JRStringUtil.escapeJavaStringLiteral(interval.getLabel()) + "\");\n");
			write(indent + meterIntervalName + ".setBackgroundColor(" + getColorText(interval.getBackgroundColor()) + ");\n");
			write(indent + meterIntervalName + ".setAlpha(Double.valueOf(" + interval.getAlphaDouble() + "));\n");
			writeDataRange(indent, interval.getDataRange(), meterIntervalName, "DataRange");
			write(indent + parentName + ".addInterval(" + meterIntervalName + ");\n");
			flush();
		}
	}

	/**
	 * Writes out the contents of a series colors block for a chart.  Assumes the caller
	 * has already written the <code>&lt;seriesColors&gt;</code> tag.
	 *
	 * @param seriesColors the colors to write
	 */
	private void writeSeriesColors(String indent, SortedSet seriesColors, String parentName)
	{
//		if (seriesColors == null || seriesColors.size() == 0)
//			return;
//		//TODO: instantiate series colors
//		JRSeriesColor[] colors = (JRSeriesColor[])seriesColors.toArray(new JRSeriesColor[0]);
//		for (int i = 0; i < colors.length; i++)
//		{
//			String seriesColorName = parentName + "SeriesColor" +i;
//			write(indent + parentName + ".setSeriesColors(new TreeSet());\n");
//			write(indent + "JRBaseChartPlot.JRBaseSeriesColor " + seriesColorName + " = new JRBaseChartPlot().JRBaseSeriesColor;\n");
//			//TODO: instantiate a SeriesColor
//			JRSeriesColor s; s.getSeriesOrder() 
//			write(indent + seriesColorName + ".setPosition(Byte.valueOf((byte)" + chartAxis.getPositionByte() + "));\n");
//
//			writer.addAttribute(JRXmlConstants.ATTRIBUTE_seriesOrder, colors[i].getSeriesOrder());
//			writer.addAttribute(JRXmlConstants.ATTRIBUTE_color, colors[i].getColor());
//			JRDesignAreaPlot p;
//			p.addSeriesColor(seriesColor)
//			write(indent + parentName + ".addSeriesColor(" + seriesColorName + ");\n");
//			flush();
//		}
	}

	/**
	 * Write the information about a the data and layout that make up one range axis in
	 * a multiple axis chart.
	 *
	 * @param chartAxis the axis being written
	 */
	private void writeChartAxis(String indent, JRChartAxis chartAxis, String parentName, String axisName)
	{
		if(chartAxis != null)
		{
			//TODO:instantiate
			write(indent + "JRChartAxis " + axisName + " = new JRChartAxis();\n");
			write(indent + axisName + ".setPosition(Byte.valueOf((byte)" + chartAxis.getPositionByte() + "));\n");
			// Let the nested chart describe itself
			writeChartTag(indent, chartAxis.getChart(), axisName +"Chart");
			write(indent + parentName + ".addAxis(" + axisName + ");\n");
			
			flush();
		}
	}

	/**
	 *
	 *
	 */
	private void writePlot(String indent, JRChartPlot plot, String plotName)
	{
		if(plot != null)
		{
			write(indent + plotName + ".setBackcolor(" + getColorText(plot.getBackcolor()) + ");\n");
			String orientation = PlotOrientation.HORIZONTAL.equals(plot.getOrientation()) ? "PlotOrientation.HORIZONTAL" : "PlotOrientation.VERTICAL" ; 
			write(indent + plotName + ".setOrientation(" + orientation + ");\n");
			write(indent + plotName + ".setBackgroundAlpha(Float.valueOf(" + plot.getBackgroundAlphaFloat().floatValue() + "f));\n");
			write(indent + plotName + ".setForegroundAlpha(Float.valueOf(" + plot.getForegroundAlphaFloat().floatValue() + "f));\n");
			writeSeriesColors(indent, plot.getSeriesColors(), plotName);
	
			flush();
		}
	}


	/**
	 *
	 */
	public void writePieChart(String indent, JRChart chart, String chartName)
	{
		if(chart != null)
		{
			write(indent + "JRDesignChart " + chartName + " = new JRDesignChart(jasperDesign, JRChart.CHART_TYPE_PIE);\n");
			writeChart(indent, chart, chartName);
			writePieDataset(indent, (JRPieDataset)chart.getDataset(), chartName, "PieDataset");
			// write plot
			JRPiePlot plot = (JRPiePlot) chart.getPlot();
			if(plot != null)
			{
				String plotName = chartName + "PiePlot";
				write(indent + "JRDesignPiePlot " + plotName + " = (JRDesignPiePlot)" + chartName + ".getPlot();\n");
				write(indent + plotName + ".setCircular(Boolean.valueOf(" + plot.getCircular() + "));\n");
				write(indent + plotName + ".setLabelFormat(\"" + JRStringUtil.escapeJavaStringLiteral(plot.getLabelFormat()) + "\");\n");
				write(indent + plotName + ".setLegendLabelFormat(\"" + JRStringUtil.escapeJavaStringLiteral(plot.getLegendLabelFormat()) + "\");\n");
				
				writePlot(indent, plot, plotName);
				writeItemLabel(indent, plot.getItemLabel(),plotName, "ItemLabel");
				flush();
			}
			flush();
		}
	}


	/**
	 *
	 */
	public void writePie3DChart(String indent, JRChart chart, String chartName)
	{
		if(chart != null)
		{
			write(indent + "JRDesignChart " + chartName + " = new JRDesignChart(jasperDesign, JRChart.CHART_TYPE_PIE3D);\n");
			writeChart(indent, chart, chartName);
			writePieDataset(indent, (JRPieDataset)chart.getDataset(), chartName, "PieDataset");
			// write plot
			JRPie3DPlot plot = (JRPie3DPlot) chart.getPlot();
			if(plot != null)
			{
				String plotName = chartName + "Pie3DPlot";
				write(indent + "JRDesignPie3DPlot " + plotName + " = (JRDesignPie3DPlot)" + chartName + ".getPlot();\n");
				write(indent + plotName + ".setCircular(Boolean.valueOf(" + plot.getCircular() + "));\n");
				write(indent + plotName + ".setLabelFormat(\"" + JRStringUtil.escapeJavaStringLiteral(plot.getLabelFormat()) + "\");\n");
				write(indent + plotName + ".setLegendLabelFormat(\"" + JRStringUtil.escapeJavaStringLiteral(plot.getLegendLabelFormat()) + "\");\n");
				write(indent + plotName + ".setDepthFactor(Double.valueOf(" + plot.getDepthFactorDouble() + "));\n");
				
				writePlot(indent, plot, plotName);
				writeItemLabel(indent, plot.getItemLabel(),plotName, "ItemLabel");
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
//		write(indent + "JRCategoryAxisFormat " + axisName + " = new JRCategoryAxisFormat();\n");
		write(indent + axisName + ".setCategoryAxisTickLabelRotation(Double.valueOf(" + labelRotation + "));\n");

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
		write(indent + parentName + ".set" + axisNameSuffix + "(" + axisName + ");\n");
		
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
			write(indent + "JRAxisFormat " + axisName + " = new JRAxisFormat();\n");
		
		write(indent + axisName + ".setLabelColor(" + getColorText(axisLabelColor) + ");\n");
		write(indent + axisName + ".setTickLabelColor(" + getColorText(axisTickLabelColor) + ");\n");
		write(indent + axisName + ".setLineColor(" + getColorText(axisLineColor) + ");\n");
		write(indent + axisName + ".setTickLabelMask(\"" + JRStringUtil.escapeJavaStringLiteral(axisTickLabelMask)  + "\");\n");
		write(indent + axisName + ".setVerticalTickLabel(Boolean.valueOf(" + axisVerticalTickLabels.booleanValue() + "));\n");
		
		if (axisLabelFont != null)
		{
			writeFont(indent, axisLabelFont, axisName + ".getLabelFont()");
		}

		if (axisTickLabelFont != null)
		{
			writeFont(indent, axisTickLabelFont, axisName + ".getTickLabelFont()");
		}
		if(isToSet)
			write(indent + parentName + ".set" + axisNameSuffix + "(" + axisName + ");\n");

		flush();
	}

	/**
	 *
	 */
	private void writeBarPlot(String indent, JRBarPlot plot, String chartName)
	{
		if(plot != null)
		{
			String plotName = chartName + "BarPlot";
			write(indent + "JRDesignBarPlot " + plotName + " = (JRDesignBarPlot)" + chartName + ".getPlot();\n");
			write(indent + plotName + ".setShowLabels(Boolean.valueOf(" + plot.getShowLabels() + "));\n");
			write(indent + plotName + ".setShowTickLabels(Boolean.valueOf(" + plot.getShowTickLabels() + "));\n");
			write(indent + plotName + ".setShowTickMarks(Boolean.valueOf(" + plot.getShowTickMarks() + "));\n");
			writePlot(indent, plot, plotName);
			
			writeItemLabel(indent, plot.getItemLabel(), plotName, "ItemLabel");
			
			writeExpression(indent, plot.getCategoryAxisLabelExpression(), plotName, "CategoryAxisLabelExpression");
			writeCategoryAxisFormat(
					indent,
					plot.getCategoryAxisLabelFont(), plot.getOwnCategoryAxisLabelColor(),
					plot.getCategoryAxisTickLabelFont(), plot.getOwnCategoryAxisTickLabelColor(),
					plot.getCategoryAxisTickLabelMask(), plot.getCategoryAxisVerticalTickLabels(), 
					plot.getCategoryAxisTickLabelRotation(), plot.getOwnCategoryAxisLineColor(),
					plotName, "CategoryAxisFormat"
					);
			
			writeExpression(indent, plot.getValueAxisLabelExpression(), plotName, "ValueAxisLabelExpression");
			writeAxisFormat(
					indent, 
					plot.getValueAxisLabelFont(), plot.getOwnValueAxisLabelColor(),
					plot.getValueAxisTickLabelFont(), plot.getOwnValueAxisTickLabelColor(),
					plot.getValueAxisTickLabelMask(), plot.getValueAxisVerticalTickLabels(), 
					plot.getOwnValueAxisLineColor(),
					plotName, "ValueAxisFormat", true
					);
			writeExpression(indent, plot.getDomainAxisMinValueExpression(), plotName, "DomainAxisMinValueExpression");
			writeExpression(indent, plot.getDomainAxisMaxValueExpression(), plotName, "DomainAxisMaxValueExpression");
			writeExpression(indent, plot.getRangeAxisMinValueExpression(), plotName, "RangeAxisMinValueExpression");
			writeExpression(indent, plot.getRangeAxisMaxValueExpression(), plotName, "RangeAxisMaxValueExpression");
	
			flush();
		}
		
	}


	/**
	 *
	 */
	private void writeBubblePlot(String indent, JRBubblePlot plot, String chartName)
	{
		if(plot != null)
		{
			String plotName = chartName + "BubblePlot";
			write(indent + "JRDesignBubblePlot " + plotName + " = (JRDesignBubblePlot)" + chartName + ".getPlot();\n");
			write(indent + plotName + ".setScaleType(" + plot.getScaleTypeInteger() + ");\n");
			writePlot(indent, plot, plotName);
			
			writeExpression(indent, plot.getXAxisLabelExpression(), plotName, "XAxisLabelExpression");
			writeAxisFormat(
					indent, plot.getXAxisLabelFont(), plot.getOwnXAxisLabelColor(),
					plot.getXAxisTickLabelFont(), plot.getOwnXAxisTickLabelColor(),
					plot.getXAxisTickLabelMask(), plot.getXAxisVerticalTickLabels(), plot.getOwnXAxisLineColor(),
					plotName, "XAxisFormat", true
					);
			writeExpression(indent, plot.getYAxisLabelExpression(), plotName, "YAxisLabelExpression");
			writeAxisFormat(
					indent, plot.getYAxisLabelFont(), plot.getOwnYAxisLabelColor(),
					plot.getYAxisTickLabelFont(), plot.getOwnYAxisTickLabelColor(),
					plot.getYAxisTickLabelMask(), plot.getYAxisVerticalTickLabels(), plot.getOwnYAxisLineColor(),
					plotName, "YAxisFormat", true
					);

			writeExpression(indent, plot.getDomainAxisMinValueExpression(), plotName, "DomainAxisMinValueExpression");
			writeExpression(indent, plot.getDomainAxisMaxValueExpression(), plotName, "DomainAxisMaxValueExpression");
			writeExpression(indent, plot.getRangeAxisMinValueExpression(), plotName, "RangeAxisMinValueExpression");
			writeExpression(indent, plot.getRangeAxisMaxValueExpression(), plotName, "RangeAxisMaxValueExpression");
	
			flush();
		}
	}


	/**
	 *
	 */
	private void writeLinePlot(String indent, JRLinePlot plot, String chartName)
	{
		if(plot != null)
		{
			String plotName = chartName + "LinePlot";
			write(indent + "JRDesignLinePlot " + plotName + " = (JRDesignLinePlot)" + chartName + ".getPlot();\n");
			write(indent + plotName + ".setShowLines(Boolean.valueOf(" + plot.getShowLines() + "));\n");
			write(indent + plotName + ".setShowShapes(Boolean.valueOf(" + plot.getShowShapes() + "));\n");
			writePlot(indent, plot, plotName);
			
			writeExpression(indent, plot.getCategoryAxisLabelExpression(), plotName, "CategoryAxisLabelExpression");
			writeCategoryAxisFormat(
					indent,
					plot.getCategoryAxisLabelFont(), plot.getOwnCategoryAxisLabelColor(),
					plot.getCategoryAxisTickLabelFont(), plot.getOwnCategoryAxisTickLabelColor(),
					plot.getCategoryAxisTickLabelMask(), plot.getCategoryAxisVerticalTickLabels(), 
					plot.getCategoryAxisTickLabelRotation(), plot.getOwnCategoryAxisLineColor(),
					plotName, "CategoryAxisFormat"
					);
			
			writeExpression(indent, plot.getValueAxisLabelExpression(), plotName, "ValueAxisLabelExpression");
			writeAxisFormat(
					indent, 
					plot.getValueAxisLabelFont(), plot.getOwnValueAxisLabelColor(),
					plot.getValueAxisTickLabelFont(), plot.getOwnValueAxisTickLabelColor(),
					plot.getValueAxisTickLabelMask(), plot.getValueAxisVerticalTickLabels(), 
					plot.getOwnValueAxisLineColor(),
					plotName, "ValueAxisFormat", true
					);
			writeExpression(indent, plot.getDomainAxisMinValueExpression(), plotName, "DomainAxisMinValueExpression");
			writeExpression(indent, plot.getDomainAxisMaxValueExpression(), plotName, "DomainAxisMaxValueExpression");
			writeExpression(indent, plot.getRangeAxisMinValueExpression(), plotName, "RangeAxisMinValueExpression");
			writeExpression(indent, plot.getRangeAxisMaxValueExpression(), plotName, "RangeAxisMaxValueExpression");
	
			flush();
		}
	}


	/**
	 * 
	 */
	private void writeTimeSeriesPlot(String indent, JRTimeSeriesPlot plot, String chartName)
	{
		if(plot != null)
		{
			String plotName = chartName + "TimeSeriesPlot";
			write(indent + "JRDesignTimeSeriesPlot " + plotName + " = (JRDesignTimeSeriesPlot)" + chartName + ".getPlot();\n");
			write(indent + plotName + ".setShowLines(Boolean.valueOf(" + plot.getShowLines() + "));\n");
			write(indent + plotName + ".setShowShapes(Boolean.valueOf(" + plot.getShowShapes() + "));\n");
			writePlot(indent, plot, plotName);
			
			writeExpression(indent, plot.getTimeAxisLabelExpression(), plotName, "TimeAxisLabelExpression");
			writeAxisFormat(
					indent, plot.getTimeAxisLabelFont(), plot.getOwnTimeAxisLabelColor(),
					plot.getTimeAxisTickLabelFont(), plot.getOwnTimeAxisTickLabelColor(),
					plot.getTimeAxisTickLabelMask(), plot.getTimeAxisVerticalTickLabels(), 
					plot.getOwnTimeAxisLineColor(),
					plotName, "TimeAxisFormat", true
					);
			
			writeExpression(indent, plot.getValueAxisLabelExpression(), plotName, "ValueAxisLabelExpression");
			writeAxisFormat(
					indent, 
					plot.getValueAxisLabelFont(), plot.getOwnValueAxisLabelColor(),
					plot.getValueAxisTickLabelFont(), plot.getOwnValueAxisTickLabelColor(),
					plot.getValueAxisTickLabelMask(), plot.getValueAxisVerticalTickLabels(), 
					plot.getOwnValueAxisLineColor(),
					plotName, "ValueAxisFormat", true
					);
			writeExpression(indent, plot.getDomainAxisMinValueExpression(), plotName, "DomainAxisMinValueExpression");
			writeExpression(indent, plot.getDomainAxisMaxValueExpression(), plotName, "DomainAxisMaxValueExpression");
			writeExpression(indent, plot.getRangeAxisMinValueExpression(), plotName, "RangeAxisMinValueExpression");
			writeExpression(indent, plot.getRangeAxisMaxValueExpression(), plotName, "RangeAxisMaxValueExpression");
	
			flush();
		}
	}

	/**
	 *
	 */
	public void writeBar3DPlot(String indent, JRBar3DPlot plot, String chartName)
	{
		if(plot != null)
		{
			String plotName = chartName + "Bar3DPlot";
			write(indent + "JRDesignBar3DPlot " + plotName + " = (JRDesignBar3DPlot)" + chartName + ".getPlot();\n");
			write(indent + plotName + ".setShowLabels(Boolean.valueOf(" + plot.getShowLabels() + "));\n");
			write(indent + plotName + ".setXOffset(" + plot.getXOffsetDouble() + ");\n");
			write(indent + plotName + ".setYOffset(" + plot.getYOffsetDouble() + ");\n");
			
			writePlot(indent, plot, plotName);
			
			writeItemLabel(indent, plot.getItemLabel(), plotName, "ItemLabel");
			
			writeExpression(indent, plot.getCategoryAxisLabelExpression(), plotName, "CategoryAxisLabelExpression");
			writeCategoryAxisFormat(
					indent,
					plot.getCategoryAxisLabelFont(), plot.getOwnCategoryAxisLabelColor(),
					plot.getCategoryAxisTickLabelFont(), plot.getOwnCategoryAxisTickLabelColor(),
					plot.getCategoryAxisTickLabelMask(), plot.getCategoryAxisVerticalTickLabels(), 
					plot.getCategoryAxisTickLabelRotation(), plot.getOwnCategoryAxisLineColor(),
					plotName, "CategoryAxisFormat"
					);
			
			writeExpression(indent, plot.getValueAxisLabelExpression(), plotName, "ValueAxisLabelExpression");
			writeAxisFormat(
					indent, 
					plot.getValueAxisLabelFont(), plot.getOwnValueAxisLabelColor(),
					plot.getValueAxisTickLabelFont(), plot.getOwnValueAxisTickLabelColor(),
					plot.getValueAxisTickLabelMask(), plot.getValueAxisVerticalTickLabels(), 
					plot.getOwnValueAxisLineColor(),
					plotName, "ValueAxisFormat", true
					);
			writeExpression(indent, plot.getDomainAxisMinValueExpression(), plotName, "DomainAxisMinValueExpression");
			writeExpression(indent, plot.getDomainAxisMaxValueExpression(), plotName, "DomainAxisMaxValueExpression");
			writeExpression(indent, plot.getRangeAxisMinValueExpression(), plotName, "RangeAxisMinValueExpression");
			writeExpression(indent, plot.getRangeAxisMaxValueExpression(), plotName, "RangeAxisMaxValueExpression");
	
			flush();
		}
	}


	/**
	 *
	 */
	public void writeBarChart(String indent, JRChart chart, String chartName)
	{
		if(chart != null)
		{
			write(indent + "JRDesignChart " + chartName + " = new JRDesignChart(jasperDesign, JRChart.CHART_TYPE_BAR);\n");
			writeChart(indent, chart, chartName);
			writeCategoryDataSet(indent, (JRCategoryDataset) chart.getDataset(), chartName, "CategoryDataset");
			writeBarPlot(indent, (JRBarPlot) chart.getPlot(), chartName);
			flush();
		}
	}


	/**
	 *
	 */
	public void writeBar3DChart(String indent, JRChart chart, String chartName)
	{
		if(chart != null)
		{
			write(indent + "JRDesignChart " + chartName + " = new JRDesignChart(jasperDesign, JRChart.CHART_TYPE_BAR3D);\n");
			writeChart(indent, chart, chartName);
			writeCategoryDataSet(indent, (JRCategoryDataset) chart.getDataset(), chartName, "CategoryDataset");
			writeBar3DPlot(indent, (JRBar3DPlot) chart.getPlot(), chartName);
			flush();
		}
	}


	/**
	 *
	 */
	public void writeBubbleChart(String indent, JRChart chart, String chartName)
	{
		if(chart != null)
		{
			write(indent + "JRDesignChart " + chartName + " = new JRDesignChart(jasperDesign, JRChart.CHART_TYPE_BUBBLE);\n");
			writeChart(indent, chart, chartName);
			writeXyzDataset(indent, (JRXyzDataset) chart.getDataset(), chartName, "XyzDataset");
			writeBubblePlot(indent, (JRBubblePlot) chart.getPlot(), chartName);
			flush();
		}
	}


	/**
	 *
	 */
	public void writeStackedBarChart(String indent, JRChart chart, String chartName)
	{
		if(chart != null)
		{
			write(indent + "JRDesignChart " + chartName + " = new JRDesignChart(jasperDesign, JRChart.CHART_TYPE_STACKEDBAR);\n");
			writeChart(indent, chart, chartName);
			writeCategoryDataSet(indent, (JRCategoryDataset) chart.getDataset(), chartName, "CategoryDataset");
			writeBarPlot(indent, (JRBarPlot) chart.getPlot(), chartName);
			flush();
		}
	}


	/**
	 *
	 */
	public void writeStackedBar3DChart(String indent, JRChart chart, String chartName)
	{
		if(chart != null)
		{
			write(indent + "JRDesignChart " + chartName + " = new JRDesignChart(jasperDesign, JRChart.CHART_TYPE_STACKEDBAR3D);\n");
			writeChart(indent, chart, chartName);
			writeCategoryDataSet(indent, (JRCategoryDataset) chart.getDataset(), chartName, "CategoryDataset");
			writeBar3DPlot(indent, (JRBar3DPlot) chart.getPlot(), chartName);
			flush();
		}
	}


	/**
	 *
	 */
	public void writeLineChart(String indent, JRChart chart, String chartName)
	{
		if(chart != null)
		{
			write(indent + "JRDesignChart " + chartName + " = new JRDesignChart(jasperDesign, JRChart.CHART_TYPE_LINE);\n");
			writeChart(indent, chart, chartName);
			writeCategoryDataSet(indent, (JRCategoryDataset) chart.getDataset(), chartName, "CategoryDataset");
			writeLinePlot(indent, (JRLinePlot) chart.getPlot(), chartName);
			flush();
		}
	}


	/**
	 * 
	 */
	public void writeTimeSeriesChart(String indent, JRChart chart, String chartName)
	{
		if(chart != null)
		{
			write(indent + "JRDesignChart " + chartName + " = new JRDesignChart(jasperDesign, JRChart.CHART_TYPE_TIMESERIES);\n");
			writeChart(indent, chart, chartName);
			writeTimeSeriesDataset(indent, (JRTimeSeriesDataset) chart.getDataset(), chartName, "TimeSeriesDataset");
			writeTimeSeriesPlot(indent, (JRTimeSeriesPlot) chart.getPlot(), chartName);
			flush();
		}
	}

	/**
	 * 
	 */
	public void writeHighLowDataset(String indent, JRHighLowDataset dataset, String parentName, String datasetNameSuffix)
	{
		if(dataset != null)
		{
			String datasetName = parentName + datasetNameSuffix;
			
			write(indent + "JRDesignHighLowDataset " + datasetName + " = (JRDesignHighLowDataset)" + parentName + ".getDataset();\n");
	
			writeElementDataset(indent, dataset, datasetName);
	
			writeExpression(indent, dataset.getSeriesExpression(), datasetName, "SeriesExpression");
			writeExpression(indent, dataset.getDateExpression(), datasetName, "DateExpression");
			writeExpression(indent, dataset.getHighExpression(), datasetName, "HighExpression");
			writeExpression(indent, dataset.getLowExpression(), datasetName, "LowExpression");
			writeExpression(indent, dataset.getOpenExpression(), datasetName, "OpenExpression");
			writeExpression(indent, dataset.getCloseExpression(), datasetName, "CloseExpression");
			writeExpression(indent, dataset.getVolumeExpression(), datasetName, "VolumeExpression");
			writeHyperlink(indent, dataset.getItemHyperlink(), datasetName, "ItemHyperlink");
			write(indent + parentName + ".setDataset(" + datasetName + ");\n");
			flush();
		}
	}


	/**
	 * 
	 */
	public void writeHighLowChart(String indent, JRChart chart, String chartName)
	{
		if(chart != null)
		{
			write(indent + "JRDesignChart " + chartName + " = new JRDesignChart(jasperDesign, JRChart.CHART_TYPE_HIGHLOW);\n");
			writeChart(indent, chart, chartName);
			writeHighLowDataset(indent, (JRHighLowDataset) chart.getDataset(), chartName, "HighLowDataset");
			
			JRHighLowPlot plot = (JRHighLowPlot) chart.getPlot();
			if(plot != null)
			{
				String plotName = chartName + "HighLowPlot";
				write(indent + "JRDesignHighLowPlot " + plotName + " = (JRDesignHighLowPlot)" + chartName + ".getPlot();\n");
				write(indent + plotName + ".setShowOpenTicks(Boolean.valueOf(" + plot.getShowOpenTicks() + "));\n");
				write(indent + plotName + ".setShowCloseTicks(Boolean.valueOf(" + plot.getShowCloseTicks() + "));\n");

				writePlot(indent, plot, plotName);
				writeExpression(indent, plot.getTimeAxisLabelExpression(), plotName, "TimeAxisLabelExpression");
				writeAxisFormat(
						indent, plot.getTimeAxisLabelFont(), plot.getOwnTimeAxisLabelColor(),
						plot.getTimeAxisTickLabelFont(), plot.getOwnTimeAxisTickLabelColor(),
						plot.getTimeAxisTickLabelMask(), plot.getTimeAxisVerticalTickLabels(), plot.getOwnTimeAxisLineColor(),
						plotName, "TimeAxisFormat", true
						);
				
				writeExpression(indent, plot.getValueAxisLabelExpression(), plotName, "ValueAxisLabelExpression");
				writeAxisFormat(
						indent, plot.getValueAxisLabelFont(), plot.getOwnValueAxisLabelColor(),
						plot.getValueAxisTickLabelFont(), plot.getOwnValueAxisTickLabelColor(),
						plot.getValueAxisTickLabelMask(), plot.getValueAxisVerticalTickLabels(), plot.getOwnValueAxisLineColor(),
						plotName, "ValueAxisFormat", true
						);
				writeExpression(indent, plot.getDomainAxisMinValueExpression(), plotName, "DomainAxisMinValueExpression");
				writeExpression(indent, plot.getDomainAxisMaxValueExpression(), plotName, "DomainAxisMaxValueExpression");
				writeExpression(indent, plot.getRangeAxisMinValueExpression(), plotName, "RangeAxisMinValueExpression");
				writeExpression(indent, plot.getRangeAxisMaxValueExpression(), plotName, "RangeAxisMaxValueExpression");
		
				flush();
			}
			flush();
		}
	}


	/**
	 * 
	 */
	public void writeGanttChart(String indent, JRChart chart, String chartName)
	{
		if(chart != null)
		{
			write(indent + "JRDesignChart " + chartName + " = new JRDesignChart(jasperDesign, JRChart.CHART_TYPE_GANTT);\n");
			writeChart(indent, chart, chartName);
			writeGanttDataset(indent, (JRGanttDataset) chart.getDataset(), chartName, "GanttDataset");
			writeBarPlot(indent, (JRBarPlot) chart.getPlot(), chartName);
			flush();
		}
	}


	/**
	 * 
	 */
	public void writeCandlestickChart(String indent, JRChart chart, String chartName)
	{
		if(chart != null)
		{
			write(indent + "JRDesignChart " + chartName + " = new JRDesignChart(jasperDesign, JRChart.CHART_TYPE_CANDLESTICK);\n");
			writeChart(indent, chart, chartName);
			writeHighLowDataset(indent, (JRHighLowDataset) chart.getDataset(), chartName, "HighLowDataset");
			
			JRCandlestickPlot plot = (JRCandlestickPlot) chart.getPlot();
			if(plot != null)
			{
				String plotName = chartName + "CandlestickPlot";
				
				write(indent + "JRDesignCandlestickPlot " + plotName + " = (JRDesignCandlestickPlot)" + chartName + ".getPlot();\n");
				write(indent + plotName + ".setShowVolume(Boolean.valueOf(" + plot.getShowVolume() + "));\n");
				writePlot(indent, plot, plotName);
				writeExpression(indent, plot.getTimeAxisLabelExpression(), plotName, "TimeAxisLabelExpression");
				writeAxisFormat(
						indent, plot.getTimeAxisLabelFont(), plot.getOwnTimeAxisLabelColor(),
						plot.getTimeAxisTickLabelFont(), plot.getOwnTimeAxisTickLabelColor(),
						plot.getTimeAxisTickLabelMask(), plot.getTimeAxisVerticalTickLabels(), plot.getOwnTimeAxisLineColor(),
						plotName, "TimeAxisFormat", true
						);
				
				writeExpression(indent, plot.getValueAxisLabelExpression(), plotName, "ValueAxisLabelExpression");
				writeAxisFormat(
						indent, plot.getValueAxisLabelFont(), plot.getOwnValueAxisLabelColor(),
						plot.getValueAxisTickLabelFont(), plot.getOwnValueAxisTickLabelColor(),
						plot.getValueAxisTickLabelMask(), plot.getValueAxisVerticalTickLabels(), plot.getOwnValueAxisLineColor(),
						plotName, "ValueAxisFormat", true
						);
				writeExpression(indent, plot.getDomainAxisMinValueExpression(), plotName, "DomainAxisMinValueExpression");
				writeExpression(indent, plot.getDomainAxisMaxValueExpression(), plotName, "DomainAxisMaxValueExpression");
				writeExpression(indent, plot.getRangeAxisMinValueExpression(), plotName, "RangeAxisMinValueExpression");
				writeExpression(indent, plot.getRangeAxisMaxValueExpression(), plotName, "RangeAxisMaxValueExpression");
		
				flush();
			}
			flush();
		}
	}

	/**
	 *
	 */
	private void writeAreaPlot(String indent, JRAreaPlot plot, String chartName)
	{
		if(plot != null)
		{
			String plotName = chartName + "AreaPlot";
			
			write(indent + "JRDesignAreaPlot " + plotName + " = (JRDesignAreaPlot)" + chartName + ".getPlot();\n");
			writePlot(indent, plot, plotName);
	
			writeExpression(indent, plot.getCategoryAxisLabelExpression(), plotName, "CategoryAxisLabelExpression");
			writeCategoryAxisFormat(
					indent,
					plot.getCategoryAxisLabelFont(), plot.getOwnCategoryAxisLabelColor(),
					plot.getCategoryAxisTickLabelFont(), plot.getOwnCategoryAxisTickLabelColor(),
					plot.getCategoryAxisTickLabelMask(), plot.getCategoryAxisVerticalTickLabels(), 
					plot.getCategoryAxisTickLabelRotation(), plot.getOwnCategoryAxisLineColor(),
					plotName, "CategoryAxisFormat"
					);
			
			writeExpression(indent, plot.getValueAxisLabelExpression(), plotName, "ValueAxisLabelExpression");
			writeAxisFormat(
					indent, 
					plot.getValueAxisLabelFont(), plot.getOwnValueAxisLabelColor(),
					plot.getValueAxisTickLabelFont(), plot.getOwnValueAxisTickLabelColor(),
					plot.getValueAxisTickLabelMask(), plot.getValueAxisVerticalTickLabels(), 
					plot.getOwnValueAxisLineColor(),
					plotName, "ValueAxisFormat", true
					);
			writeExpression(indent, plot.getDomainAxisMinValueExpression(), plotName, "DomainAxisMinValueExpression");
			writeExpression(indent, plot.getDomainAxisMaxValueExpression(), plotName, "DomainAxisMaxValueExpression");
			writeExpression(indent, plot.getRangeAxisMinValueExpression(), plotName, "RangeAxisMinValueExpression");
			writeExpression(indent, plot.getRangeAxisMaxValueExpression(), plotName, "RangeAxisMaxValueExpression");
	
			flush();
		}
	}


	/**
	 *
	 */
	public void writeAreaChart(String indent, JRChart chart, String chartName)
	{
		if(chart != null)
		{
			write(indent + "JRDesignChart " + chartName + " = new JRDesignChart(jasperDesign, JRChart.CHART_TYPE_AREA);\n");
			writeChart(indent, chart, chartName);
			writeCategoryDataSet(indent, (JRCategoryDataset) chart.getDataset(), chartName, "CategoryDataset");
			writeAreaPlot(indent, (JRAreaPlot) chart.getPlot(), chartName);
			flush();
		}
	}


	/**
	 *
	 */
	private void writeScatterPlot(String indent, JRScatterPlot plot, String chartName)
	{
		if(plot != null)
		{
			String plotName = chartName + "ScatterPlot";
			write(indent + "JRDesignScatterPlot " + plotName + " = (JRDesignScatterPlot)" + chartName + ".getPlot();\n");
			write(indent + plotName + ".setShowLines(Boolean.valueOf(" + plot.getShowLines() + "));\n");
			write(indent + plotName + ".setShowShapes(Boolean.valueOf(" + plot.getShowShapes() + "));\n");
			writePlot(indent, plot, plotName);
			
			writeExpression(indent, plot.getXAxisLabelExpression(), plotName, "XAxisLabelExpression");
			writeAxisFormat(
					indent, 
					plot.getXAxisLabelFont(), plot.getOwnXAxisLabelColor(),
					plot.getXAxisTickLabelFont(), plot.getOwnXAxisTickLabelColor(),
					plot.getXAxisTickLabelMask(), plot.getXAxisVerticalTickLabels(), 
					plot.getOwnXAxisLineColor(),
					plotName, "XAxisFormat", true
					);
			
			writeExpression(indent, plot.getYAxisLabelExpression(), plotName, "YAxisLabelExpression");
			writeAxisFormat(
					indent, 
					plot.getYAxisLabelFont(), plot.getOwnYAxisLabelColor(),
					plot.getYAxisTickLabelFont(), plot.getOwnYAxisTickLabelColor(),
					plot.getYAxisTickLabelMask(), plot.getYAxisVerticalTickLabels(), 
					plot.getOwnYAxisLineColor(),
					plotName, "YAxisFormat", true
					);
			writeExpression(indent, plot.getDomainAxisMinValueExpression(), plotName, "DomainAxisMinValueExpression");
			writeExpression(indent, plot.getDomainAxisMaxValueExpression(), plotName, "DomainAxisMaxValueExpression");
			writeExpression(indent, plot.getRangeAxisMinValueExpression(), plotName, "RangeAxisMinValueExpression");
			writeExpression(indent, plot.getRangeAxisMaxValueExpression(), plotName, "RangeAxisMaxValueExpression");
	
			flush();
		}
	}


	/**
	 *
	 */
	public void writeScatterChart(String indent, JRChart chart, String chartName)
	{
		if(chart != null)
		{
			write(indent + "JRDesignChart " + chartName + " = new JRDesignChart(jasperDesign, JRChart.CHART_TYPE_SCATTER);\n");
			writeChart(indent, chart, chartName);
			writeXyDataset(indent, (JRXyDataset) chart.getDataset(), chartName, "XyDataset");
			writeScatterPlot(indent, (JRScatterPlot) chart.getPlot(), chartName);
			flush();
		}
	}


	/**
	 *
	 */
	public void writeXyAreaChart(String indent, JRChart chart, String chartName)
	{
		if(chart != null)
		{
			write(indent + "JRDesignChart " + chartName + " = new JRDesignChart(jasperDesign, JRChart.CHART_TYPE_XYAREA);\n");
			writeChart(indent, chart, chartName);
			writeXyDataset(indent, (JRXyDataset) chart.getDataset(), chartName, "XyDataset");
			writeAreaPlot(indent, (JRAreaPlot) chart.getPlot(), chartName);
			flush();
		}
	}


	/**
	 *
	 */
	public void writeXyBarChart(String indent, JRChart chart, String chartName)
	{
		if(chart != null)
		{
			write(indent + "JRDesignChart " + chartName + " = new JRDesignChart(jasperDesign, JRChart.CHART_TYPE_XYBAR);\n");
			writeChart(indent, chart, chartName);
			JRChartDataset dataset = chart.getDataset();

			if( dataset.getDatasetType() == JRChartDataset.TIMESERIES_DATASET )
			{
				writeTimeSeriesDataset(indent, (JRTimeSeriesDataset)dataset, chartName, "TimeSeriesDataset");
			}
			else if( dataset.getDatasetType() == JRChartDataset.TIMEPERIOD_DATASET ){
				writeTimePeriodDataset(indent, (JRTimePeriodDataset)dataset, chartName, "XyDataset");
			}
			else if( dataset.getDatasetType() == JRChartDataset.XY_DATASET ){
				writeXyDataset(indent, (JRXyDataset) chart.getDataset(), chartName, "XyDataset");
			}
			writeBarPlot(indent, (JRBarPlot) chart.getPlot(), chartName);
			flush();
		}
	}


	/**
	 *
	 */
	public void writeXyLineChart(String indent, JRChart chart, String chartName)
	{
		if(chart != null)
		{
			write(indent + "JRDesignChart " + chartName + " = new JRDesignChart(jasperDesign, JRChart.CHART_TYPE_XYLINE);\n");
			writeChart(indent, chart, chartName);
			writeXyDataset(indent, (JRXyDataset) chart.getDataset(), chartName, "XyDataset");
			writeLinePlot(indent, (JRLinePlot) chart.getPlot(), chartName);
			flush();
		}
	}


	/**
	 * Writes the definition of a meter chart to the output stream.
	 *
	 * @param chart the meter chart to write
	 */
	public void writeMeterChart(String indent, JRChart chart, String chartName)
	{
		if(chart != null)
		{
			write(indent + "JRDesignChart " + chartName + " = new JRDesignChart(jasperDesign, JRChart.CHART_TYPE_METER);\n");
			writeChart(indent, chart, chartName);
			writeValueDataset(indent, (JRValueDataset) chart.getDataset(), chartName, "ValueDataset");
			JRMeterPlot plot = (JRMeterPlot) chart.getPlot();
			if(plot != null)
			{
				String plotName = chartName + "MeterPlot";
				
				write(indent + "JRDesignMeterPlot " + plotName + " = (JRDesignMeterPlot)" + chartName + ".getPlot();\n");
				write(indent + plotName + ".setShape(Byte.valueOf((byte)" + plot.getShapeByte() + "));\n");
				write(indent + plotName + ".setMeterAngle(Integer.valueOf(" + plot.getMeterAngleInteger() + "));\n");
				write(indent + plotName + ".setUnits(\"" + JRStringUtil.escapeJavaStringLiteral(plot.getUnits()) + "\");\n");
				write(indent + plotName + ".setTickInterval(Double.valueOf(" + plot.getTickIntervalDouble() + "));\n");
				write(indent + plotName + ".setMeterBackgroundColor(" + getColorText(plot.getMeterBackgroundColor()) + ");\n");
				write(indent + plotName + ".setNeedleColor(" + getColorText(plot.getNeedleColor()) + ");\n");
				write(indent + plotName + ".setTickColor(" + getColorText(plot.getTickColor()) + ");\n");
				
				writePlot(indent, plot, plotName);
				if (plot.getTickLabelFont() != null)
				{
					writeFont(indent, plot.getTickLabelFont(), plotName + ".getTickLabelFont()");
					flush();
				}
				writeValueDisplay(indent, plot.getValueDisplay(), plotName);
				writeDataRange(indent, plot.getDataRange(), plotName, "DataRange");

				List intervals = plot.getIntervals();
				if (intervals != null && intervals.size() > 0)
				{
					for(int i = 0; i < intervals.size(); i++)
					{
						JRMeterInterval meterInterval = (JRMeterInterval) intervals.get(i);
						writeMeterInterval(indent, meterInterval, plotName, plotName+"Interval"+i);
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
	public void writeThermometerChart(String indent, JRChart chart, String chartName)
	{
		if(chart != null)
		{
			write(indent + "JRDesignChart " + chartName + " = new JRDesignChart(jasperDesign, JRChart.CHART_TYPE_THERMOMETER);\n");
			writeChart(indent, chart, chartName);
			writeValueDataset(indent, (JRValueDataset) chart.getDataset(), chartName, "ValueDataset");
			JRThermometerPlot plot = (JRThermometerPlot) chart.getPlot();
			if(plot != null)
			{
				String plotName = chartName + "ThermometerPlot";
				write(indent + "JRDesignThermometerPlot " + plotName + " = (JRDesignThermometerPlot)" + chartName + ".getPlot();\n");
				write(indent + plotName + ".setValueLocation(Byte.valueOf((byte)" + plot.getValueLocationByte() + "));\n");
				write(indent + plotName + ".setMercuryColor(" + getColorText(plot.getMercuryColor()) + ");\n");
				writePlot(indent, plot, plotName);
				writeValueDisplay(indent, plot.getValueDisplay(), plotName);
				writeDataRange(indent, plot.getDataRange(), plotName, "DataRange");

				if (plot.getLowRange() != null)
				{
					writeDataRange(indent, plot.getLowRange(), plotName, "LowRange");
				}

				if (plot.getMediumRange() != null)
				{
					writeDataRange(indent, plot.getMediumRange(), plotName, "MediumRange");
				}

				if (plot.getHighRange() != null)
				{
					writeDataRange(indent, plot.getHighRange(), plotName, "HighRange");
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
	public void writeMultiAxisChart(String indent, JRChart chart, String chartName)
	{
		if(chart != null)
		{
			write(indent + "JRDesignChart " + chartName + " = new JRDesignChart(jasperDesign, JRChart.CHART_TYPE_MULTI_AXIS);\n");
			writeChart(indent, chart, chartName);
			JRMultiAxisPlot plot = (JRMultiAxisPlot) chart.getPlot();
			String plotName = chartName + "MultiAxisPlot";
			
			write(indent + "JRDesignMultiAxisPlot " + plotName + " = (JRDesignMultiAxisPlot)" + chartName + ".getPlot();\n");
			writePlot(indent, chart.getPlot(), plotName);
			List axes = plot.getAxes();
			if (axes != null && axes.size() > 0)
			{
				for (int i = 0; i < axes.size(); i++)
				{
					JRChartAxis chartAxis = (JRChartAxis) axes.get(i);
					writeChartAxis(indent, chartAxis, plotName, plotName + "Axis" + i);
				}
			}
			flush();
		}
	}

	/**
	 *
	 */
	public void writeStackedAreaChart(String indent, JRChart chart, String chartName)
	{
		if(chart != null)
		{
			write(indent + "JRDesignChart " + chartName + " = new JRDesignChart(jasperDesign, JRChart.CHART_TYPE_STACKEDAREA);\n");
			writeChart(indent, chart, chartName);
			writeCategoryDataSet(indent, (JRCategoryDataset) chart.getDataset(), chartName, "CategoryDataset");
			writeAreaPlot(indent, (JRAreaPlot) chart.getPlot(), chartName);
			flush();
		}
	}


	/**
	 * 
	 */
	public void writeChartTag(String indent, JRChart chart, String chartName)
	{
		switch(chart.getChartType()) {
			case JRChart.CHART_TYPE_AREA:
				writeAreaChart(indent, chart, chartName);
				break;
			case JRChart.CHART_TYPE_BAR:
				writeBarChart(indent, chart, chartName);
				break;
			case JRChart.CHART_TYPE_BAR3D:
				writeBar3DChart(indent, chart, chartName);
				break;
			case JRChart.CHART_TYPE_BUBBLE:
				writeBubbleChart(indent, chart, chartName);
				break;
			case JRChart.CHART_TYPE_CANDLESTICK:
				writeCandlestickChart(indent, chart, chartName);
				break;
			case JRChart.CHART_TYPE_HIGHLOW:
				writeHighLowChart(indent, chart, chartName);
				break;
			case JRChart.CHART_TYPE_LINE:
				writeLineChart(indent, chart, chartName);
				break;
			case JRChart.CHART_TYPE_METER:
				writeMeterChart(indent, chart, chartName);
				break;
			case JRChart.CHART_TYPE_MULTI_AXIS:
				writeMultiAxisChart(indent, chart, chartName);
				break;
			case JRChart.CHART_TYPE_PIE:
				writePieChart(indent, chart, chartName);
				break;
			case JRChart.CHART_TYPE_PIE3D:
				writePie3DChart(indent, chart, chartName);
				break;
			case JRChart.CHART_TYPE_SCATTER:
				writeScatterChart(indent, chart, chartName);
				break;
			case JRChart.CHART_TYPE_STACKEDBAR:
				writeStackedBarChart(indent, chart, chartName);
				break;
			case JRChart.CHART_TYPE_STACKEDBAR3D:
				writeStackedBar3DChart(indent, chart, chartName);
				break;
			case JRChart.CHART_TYPE_THERMOMETER:
				writeThermometerChart(indent, chart, chartName);
				break;
			case JRChart.CHART_TYPE_TIMESERIES:
				writeTimeSeriesChart(indent, chart, chartName);
				break;
			case JRChart.CHART_TYPE_XYAREA:
				writeXyAreaChart(indent, chart, chartName);
				break;
			case JRChart.CHART_TYPE_XYBAR:
				writeXyBarChart(indent, chart, chartName);
				break;
			case JRChart.CHART_TYPE_XYLINE:
				writeXyLineChart(indent, chart, chartName);
				break;
			case JRChart.CHART_TYPE_STACKEDAREA:
				writeStackedAreaChart(indent, chart, chartName);
				break;
			case JRChart.CHART_TYPE_GANTT:
				writeGanttChart(indent, chart, chartName);
				break;
			default:
				throw new JRRuntimeException("Chart type not supported.");
		}
	}


	/**
	 * 
	 */
	private void writeSubreportReturnValue(String indent, JRSubreportReturnValue returnValue, String returnValueName)
	{
		if(returnValue != null)
		{
			write(indent + "JRDesignReturnValue " + returnValueName + " = new JRDesignReturnValue();\n");
			write(indent + returnValueName + ".setSubreportVariable(\"" + JRStringUtil.escapeJavaStringLiteral(returnValue.getSubreportVariable()) + "\");\n");
			write(indent + returnValueName + ".setToVariable(\"" + JRStringUtil.escapeJavaStringLiteral(returnValue.getToVariable()) + "\");\n");
			write(indent + returnValueName + ".setCalculation((byte)" + (returnValue.getCalculation() > -1 ? returnValue.getCalculation() : JRVariable.CALCULATION_NOTHING) + ");\n");
			write(indent + returnValueName + ".setIncrementerFactoryClassName(\"" + JRStringUtil.escapeJavaStringLiteral(returnValue.getIncrementerFactoryClassName()) + "\");\n");
			flush();
		}
	}

	/**
	 * 
	 */
	public void writeCrosstab(String indent, JRCrosstab crosstab, String crosstabName)
	{
		if(crosstab != null)
		{
			write(indent + "JRDesignCrosstab " + crosstabName + " = new JRDesignCrosstab(jasperDesign);\n");
			write(indent + crosstabName + ".setRepeatColumnHeaders(" + crosstab.isRepeatColumnHeaders() + ");\n");
			write(indent + crosstabName + ".setRepeatRowHeaders(" + crosstab.isRepeatRowHeaders() + ");\n");
			write(indent + crosstabName + ".setColumnBreakOffset(" + (crosstab.getColumnBreakOffset() > 0 ? crosstab.getColumnBreakOffset() : JRCrosstab.DEFAULT_COLUMN_BREAK_OFFSET) + ");\n");
			write(indent + crosstabName + ".setRunDirection((byte)" + (crosstab.getRunDirection() > 0 ? crosstab.getRunDirection() : JRCrosstab.RUN_DIRECTION_LTR) + ");\n");
			write(indent + crosstabName + ".setIgnoreWidth(" + crosstab.getIgnoreWidth().booleanValue() + ");\n");
	
			writeReportElement(indent, crosstab, crosstabName);
	
			JRCrosstabParameter[] parameters = crosstab.getParameters();
			if (parameters != null)
			{
				for (int i = 0; i < parameters.length; i++)
				{
					if (!parameters[i].isSystemDefined())
					{
						writeCrosstabParameter(indent, parameters[i], crosstabName + "Parameter" + i);
						write(indent + crosstabName + ".addParameter(" + crosstabName + "Parameter" + i + ");\n");
						
					}
				}
			}

			writeExpression(indent, crosstab.getParametersMapExpression(), crosstabName, "ParametersMapExpression");
	
			writeCrosstabDataset(indent, crosstab, crosstabName);

			writeCrosstabHeaderCell(indent, crosstab, crosstabName);
	
			JRCrosstabRowGroup[] rowGroups = crosstab.getRowGroups();
			for (int i = 0; i < rowGroups.length; i++)
			{
				writeCrosstabRowGroup(indent, rowGroups[i], crosstabName + "RowGroup" + i);
				write(indent + crosstabName + ".addRowGroup(" + crosstabName + "RowGroup" + i + ");\n");
			}
	
			JRCrosstabColumnGroup[] columnGroups = crosstab.getColumnGroups();
			for (int i = 0; i < columnGroups.length; i++)
			{
				writeCrosstabColumnGroup(indent, columnGroups[i], crosstabName + "ColumnGroup" + i);
				write(indent + crosstabName + ".addColumnGroup(" + crosstabName + "ColumnGroup" + i + ");\n");
			}
	
			JRCrosstabMeasure[] measures = crosstab.getMeasures();
			for (int i = 0; i < measures.length; i++)
			{
				writeCrosstabMeasure(indent, measures[i], crosstabName + "Measure" + i);
				write(indent + crosstabName + ".addMeasure(" + crosstabName + "Measure" + i + ");\n");
			}
	
			if (crosstab instanceof JRDesignCrosstab)
			{
				List cellsList = ((JRDesignCrosstab) crosstab).getCellsList();
				for (int i = 0; i < cellsList.size(); i++)
				{
					JRCrosstabCell cell = (JRCrosstabCell) cellsList.get(i);
					writeCrosstabCell(indent, cell, crosstabName + "Cell" + i);
					write(indent + crosstabName + ".addCell(" + crosstabName + "Cell" + i + ");\n");
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
							writeCrosstabCell(indent, cell, crosstabName + "Cell" + i + "" + j);
							write(indent + crosstabName + ".addCell(" + crosstabName + "Cell" + i + "" + j + ");\n");
						}
					}
				}
			}
	
			writeCrosstabWhenNoDataCell(indent, crosstab, crosstabName + "NoDataCell");
			write(indent + crosstabName + ".setWhenNoDataCell(" + crosstabName + "NoDataCell);\n");
			flush();
		}
	}


	/**
	 * 
	 */
	private void writeCrosstabDataset(String indent, JRCrosstab crosstab, String crosstabName)
	{
		if(crosstab != null)
		{
			String datasetName = crosstabName + "Dataset";
			JRCrosstabDataset dataset = crosstab.getDataset();
			write(indent + "JRDesignCrosstabDataset " + datasetName + " = new JRDesignCrosstabDataset();\n");
			write(indent + datasetName + ".setDataPreSorted(" + dataset.isDataPreSorted() + ");\n");
			writeElementDataset(indent, dataset, datasetName);
			write(indent + crosstabName + ".setDataset(" + datasetName + ");\n");
			
			flush();
		}
	}


	/**
	 * 
	 */
	private void writeCrosstabWhenNoDataCell(String indent, JRCrosstab crosstab, String cellName)
	{
		JRCellContents whenNoDataCell = crosstab.getWhenNoDataCell();
		if (whenNoDataCell != null)
		{
			writeCellContents(indent, whenNoDataCell, cellName);
			flush();
		}
	}


	/**
	 * 
	 */
	private void writeCrosstabHeaderCell(String indent, JRCrosstab crosstab, String parentName)
	{
		JRCellContents headerCell = crosstab.getHeaderCell();
		if (headerCell != null)
		{
			writeCellContents(indent, headerCell, parentName+"HeaderCellContents");
			write(indent + parentName + ".setHeaderCell(" + parentName + "HeaderCellContents);\n");
			flush();
		}
	}


	/**
	 * 
	 */
	protected void writeCrosstabRowGroup(String indent, JRCrosstabRowGroup group, String groupName)
	{
		if(group != null)
		{
			write(indent + "JRDesignCrosstabRowGroup " + groupName + " = new JRDesignCrosstabRowGroup();\n");
			write(indent + groupName + ".setName(\"" + JRStringUtil.escapeJavaStringLiteral(group.getName()) + "\");\n");
			write(indent + groupName + ".setWidth(" + group.getWidth() + ");\n");
			write(indent + groupName + ".setTotalPosition((byte)" + (group.getTotalPosition() > 0 ? group.getTotalPosition() : BucketDefinition.TOTAL_POSITION_NONE) + ");\n");
			write(indent + groupName + ".setPosition(" + ((byte)group.getPosition() > 0 ? group.getPosition() : JRCellContents.POSITION_Y_TOP) + ");\n");
	
			writeBucket(indent, group.getBucket(), groupName);
	
			JRCellContents header = group.getHeader();
			writeCellContents(indent, header, groupName + "HeaderContents");
			write(indent + groupName + ".setHeader(" + groupName + "HeaderContents);\n");
	
			JRCellContents totalHeader = group.getTotalHeader();
			writeCellContents(indent, totalHeader, groupName + "TotalHeaderContents");
			write(indent + groupName + ".setTotalHeader(" + groupName + "TotalHeaderContents);\n");
			flush();

		}
	}


	/**
	 * 
	 */
	protected void writeCrosstabColumnGroup(String indent, JRCrosstabColumnGroup group, String groupName)
	{
		if(group != null)
		{
			write(indent + "JRDesignColumnRowGroup " + groupName + " = new JRDesignCrosstabColumnGroup();\n");
			write(indent + groupName + ".setName(\"" + JRStringUtil.escapeJavaStringLiteral(group.getName()) + "\");\n");
			write(indent + groupName + ".setHeight(" + group.getHeight() + ");\n");
			write(indent + groupName + ".setTotalPosition((byte)" + (group.getTotalPosition() > 0 ? group.getTotalPosition() : BucketDefinition.TOTAL_POSITION_NONE) + ");\n");
			write(indent + groupName + ".setPosition((byte)" + (group.getPosition() > 0 ? group.getPosition() : JRCellContents.POSITION_X_LEFT) + ");\n");
	
			writeBucket(indent, group.getBucket(), groupName);
	
			JRCellContents header = group.getHeader();
			writeCellContents(indent, header, groupName + "HeaderContents");
			write(indent + groupName + ".setHeader(" + groupName + "HeaderContents);\n");
	
			JRCellContents totalHeader = group.getTotalHeader();
			writeCellContents(indent, totalHeader, groupName + "TotalHeaderContents");
			write(indent + groupName + ".setTotalHeader(" + groupName + "TotalHeaderContents);\n");
			flush();

		}
	}


	/**
	 * 
	 */
	protected void writeBucket(String indent, JRCrosstabBucket bucket, String parentName)
	{
		if(bucket != null)
		{
			String bucketName = parentName + "Bucket";
			write(indent + "JRDesignCrosstabBucket " + bucketName + " = new JRDesignCrosstabBucket();\n");
			write(indent + bucketName + ".setOrder((byte)" + (bucket.getOrder() > 0 ? bucket.getOrder() : BucketDefinition.ORDER_ASCENDING) + ");\n");

			writeExpression(indent, bucket.getExpression(), bucketName, "Expression");

			writeExpression(indent, bucket.getComparatorExpression(), bucketName, "ComparatorExpression");

			writeExpression(indent, bucket.getOrderByExpression(), bucketName, "OrderByExpression", Object.class.getName());
			flush();
		}
	}


	/**
	 * 
	 */
	protected void writeCrosstabMeasure(String indent, JRCrosstabMeasure measure, String measureName)
	{
		if(measure != null)
		{
			write(indent + "JRDesignCrosstabMeasure " + measureName + " = new JRDesignCrosstabMeasure();\n");
			write(indent + measureName + ".setName(\"" + JRStringUtil.escapeJavaStringLiteral(measure.getName()) + "\");\n");

			if(measure.getValueClass() != null)
				write(indent + measureName + ".setValueClass(Class.forName(\"" + JRStringUtil.escapeJavaStringLiteral(measure.getValueClass().getName()) + "\"));\n");
			else if(measure.getValueClassName() != null)
				write(indent + measureName + ".setValueClassName(\"" + JRStringUtil.escapeJavaStringLiteral(measure.getValueClassName()) + "\");\n");
			
			write(indent + measureName + ".setCalculation((byte)" + (measure.getCalculation() > 0 ? measure.getCalculation() : JRVariable.CALCULATION_NOTHING) + ");\n");
			write(indent + measureName + ".setPercentageOfType((byte)" + (measure.getPercentageOfType() > 0 ? measure.getPercentageOfType() : JRCrosstabMeasure.PERCENTAGE_TYPE_NONE) + ");\n");
			write(indent + measureName + ".setPercentageCalculatorClassName(\"" + JRStringUtil.escapeJavaStringLiteral(measure.getPercentageCalculatorClassName()) + "\");\n");
			writeExpression(indent, measure.getValueExpression(), measureName, "ValueExpression");
			
			flush();
		}
	}


	/**
	 * 
	 */
	protected void writeCrosstabCell(String indent, JRCrosstabCell cell, String cellName)
	{
		if(cell != null)
		{
			write(indent + "JRDesignCrosstabCell " + cellName + " = new JRDesignCrosstabCell();\n");
			write(indent + cellName + ".setWidth(" + cell.getWidth() + ");\n");
			write(indent + cellName + ".setHeight(" + cell.getHeight() + ");\n");
			write(indent + cellName + ".setRowTotalGroup(\"" + JRStringUtil.escapeJavaStringLiteral(cell.getRowTotalGroup()) + "\");\n");
			write(indent + cellName + ".setColumnTotalGroup(\"" + JRStringUtil.escapeJavaStringLiteral(cell.getColumnTotalGroup()) + "\");\n");
		
			writeCellContents(indent, cell.getContents(), cellName + "Contents");
	
			write(indent + cellName + ".setContents(" + cellName + "Contents);\n");
			flush();
		}
	}


	/**
	 * 
	 */
	protected void writeCellContents(String indent, JRCellContents contents, String cellName)
	{
		if (contents != null)
		{
			write(indent + "JRDesignCellContents " + cellName + " = new JRDesignCellContents();\n");
			write(indent + cellName + ".setBackcolor(" + getColorText(contents.getBackcolor()) + ");\n");
			write(indent + cellName + ".setMode((byte)" + contents.getMode() + ");\n");
			writeStyleReferenceAttr(indent, contents, cellName);

			writeBox(indent, contents.getLineBox(), cellName + ".getLineBox()");

			writeChildElements(indent, contents, cellName);

			flush();
		}
	}


	/**
	 * 
	 */
	protected void writeCrosstabParameter(String indent, JRCrosstabParameter parameter, String parameterName)
	{
		if(parameter != null)
		{
			write(indent + "JRDesignCrosstabParameter " + parameterName + " = new JRDesignCrosstabParameter();\n");
			write(indent + parameterName + ".setDescription(" + parameter.getDescription() + ");\n");
			write(indent + parameterName + ".setName(" + parameter.getName() + ");\n");
			
			if(parameter.getValueClass() != null)
				write(indent + parameterName + ".setValueClass(Class.forName(\"" + JRStringUtil.escapeJavaStringLiteral(parameter.getValueClass().getName()) + "\"));\n");
			else if(parameter.getValueClassName() != null)
				write(indent + parameterName + ".setValueClassName(\"" + JRStringUtil.escapeJavaStringLiteral(parameter.getValueClassName()) + "\");\n");
			else
				write(indent + parameterName + ".setValueClassName(\"java.lang.String\");\n");
			
			writeExpression(indent, parameter.getExpression(), parameterName, "Expression");
			flush();
		}
	}


	/**
	 * 
	 */
	public void writeDataset(String indent, JRDataset dataset, String datasetName)
	{
		if(dataset != null)
		{
			write(indent + "JRDesignDataset " + datasetName + " = new JRDesignDataset(" + dataset.isMainDataset() + ");\n");	
			
			write(indent + datasetName + ".setName(\"" + JRStringUtil.escapeJavaStringLiteral(dataset.getName()) + "\");\n");
			write(indent + datasetName + ".setScriptletClass(\"" + JRStringUtil.escapeJavaStringLiteral(dataset.getScriptletClass()) + "\");\n");
			write(indent + datasetName + ".setResourceBundle(\"" + JRStringUtil.escapeJavaStringLiteral(dataset.getResourceBundle()) + "\");\n");
			write(indent + datasetName + ".setWhenResourceMissingType((byte)" + (dataset.getWhenResourceMissingType() < 1 ? JRReport.WHEN_RESOURCE_MISSING_TYPE_NULL : dataset.getWhenResourceMissingType()) + ");\n");
	
			writeProperties(indent, dataset, datasetName);
	
			writeDatasetContents(indent, dataset, datasetName);
			flush();
		}
		
	}

	/**
	 * 
	 */
	protected void writeDatasetContents(String indent, JRDataset dataset, String datasetName)
	{
		JRScriptlet[] scriptlets = dataset.getScriptlets();
		if (scriptlets != null && scriptlets.length > 0)
		{
			for(int i = 0; i < scriptlets.length; i++)
			{
				writeScriptlet(indent, scriptlets[i], datasetName + "Scriptlet" + i);
				write(indent + datasetName +".addScriptlet(" + datasetName + "Scriptlet" + i + ");\n");
			}
		}

		JRParameter[] parameters = dataset.getParameters();
		if (parameters != null && parameters.length > 0)
		{
			for(int i = 0; i < parameters.length; i++)
			{
				if (!parameters[i].isSystemDefined())
				{
					writeParameter(indent, parameters[i], datasetName + "Parameter" + i);
					write(indent + datasetName +".addParameter(" + datasetName + "Parameter" + i + ");\n");
				}
			}
		}

		if(dataset.getQuery() != null)
		{
			writeQuery(indent, dataset.getQuery(), datasetName + "Query");
		}

		JRField[] fields = dataset.getFields();
		if (fields != null && fields.length > 0)
		{
			for(int i = 0; i < fields.length; i++)
			{
				writeField(indent, fields[i], datasetName + "Field" + i);
				write(indent + datasetName +".addField(" + datasetName + "Field" + i + ");\n");
			}
		}

		JRSortField[] sortFields = dataset.getSortFields();
		if (sortFields != null && sortFields.length > 0)
		{
			for(int i = 0; i < sortFields.length; i++)
			{
				writeSortField(indent, sortFields[i], datasetName + "SortField" + i);
				write(indent + datasetName +".addSortField(" + datasetName + "SortField" + i + ");\n");
			}
		}

		JRVariable[] variables = dataset.getVariables();
		if (variables != null && variables.length > 0)
		{
			for(int i = 0; i < variables.length; i++)
			{
				if (!variables[i].isSystemDefined())
				{
					writeVariable(indent, variables[i], datasetName + "Variable" + i);
					write(indent + datasetName +".addVariable(" + datasetName + "Variable" + i + ");\n");
				}
			}
		}

		writeExpression(indent, dataset.getFilterExpression(), datasetName, "FilterExpression");

		JRGroup[] groups = dataset.getGroups();
		if (groups != null && groups.length > 0)
		{
			for(int i = 0; i < groups.length; i++)
			{
				String groupName = getGroupName(indent, groups[i]);
				if(groupName != null)
				{
					write(indent + datasetName +".addGroup(" + groupName + ");\n");
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
	public void writeDatasetRun(String indent, JRDatasetRun datasetRun, String parentName)
	{
		if(datasetRun != null)
		{
			String runName = parentName + "Run";
			write(indent + "JRDesignDatasetRun " + runName + " = new JRDesignDatasetRun();\n");
			write(indent + runName + ".setDatasetName(\"" + JRStringUtil.escapeJavaStringLiteral(datasetRun.getDatasetName()) + "\");\n");
			writeExpression(indent, datasetRun.getParametersMapExpression(), runName, "ParametersMapExpression");
	
			JRDatasetParameter[] parameters = datasetRun.getParameters();
			if (parameters != null && parameters.length > 0)
			{
				for(int i = 0; i < parameters.length; i++)
				{
					writeDatasetParameter(indent, parameters[i], runName + "Parameter" + i);
				}
			}
	
			writeExpression(indent, datasetRun.getConnectionExpression(), runName, "ConnectionExpression");

			writeExpression(indent, datasetRun.getDataSourceExpression(), runName, "DataSourceExpression");
			write(indent + parentName + ".setDatasetRun(" + runName + ");\n");
			flush();
		}
	}


	/**
	 * 
	 */
	public void writeFrame(String indent, JRFrame frame, String frameName)
	{
		if(frame != null)
		{
			JRDesignFrame f = new JRDesignFrame(jasperDesign);
			
			write(indent + "JRDesignFrame " + frameName + " = new JRDesignFrame(jasperDesign);\n");
	
			writeReportElement(indent, frame, frameName);
			writeBox(indent, frame.getLineBox(), frameName + ".getLineBox()");
	
			writeChildElements(indent, frame, frameName);
	
			flush();
		}
	}


	/**
	 * 
	 */
	protected void writeHyperlinkParameters(String indent, JRHyperlinkParameter[] parameters, String parentName)
	{
		if (parameters != null)
		{
			for (int i = 0; i < parameters.length; i++)
			{
				JRHyperlinkParameter parameter = parameters[i];
				writeHyperlinkParameter(indent, parameter, parentName + "HyperlinkParameter" +i);
				write(indent + parentName + ".addHyperlinkParameter(" + parentName + "HyperlinkParameter" + i + ");\n");
			}
			flush();
		}
	}


	/**
	 * 
	 */
	protected void writeHyperlinkParameter(String indent, JRHyperlinkParameter parameter, String parameterName)
	{
		if (parameter != null)
		{
			write(indent + "JRDesignHyperlinkParameter " + parameterName + " = new JRDesignHyperlinkParameter();\n");
			write(indent + parameterName + ".setName(\"" + JRStringUtil.escapeJavaStringLiteral(parameter.getName()) + "\");\n");
			writeExpression(indent,	parameter.getValueExpression(), parameterName, "ValueExpression", String.class.getName());
			flush();
		}
	}

	/**
	 * 
	 *
	public void writeHyperlink(String indent, String tagName, JRHyperlink hyperlink, String hyperlinkName)
	{
		writeHyperlink(indent, tagName, null, hyperlink, hyperlinkName);
	}

	/**
	 * 
	 *
	public void writeHyperlink(String indent, String tagName, XmlNamespace namespace, 
			JRHyperlink hyperlink, String hyperlinkName)
	{
		if (hyperlink != null)
		{
			writer.startElement(tagName, namespace);

			writer.addEncodedAttribute(JRXmlConstants.ATTRIBUTE_hyperlinkType, hyperlink.getLinkType(), JRHyperlinkHelper.HYPERLINK_TYPE_NONE);
			writer.addEncodedAttribute(JRXmlConstants.ATTRIBUTE_hyperlinkTarget, hyperlink.getLinkTarget(), JRHyperlinkHelper.HYPERLINK_TARGET_SELF);

			writer.writeExpression(JRXmlConstants.ELEMENT_hyperlinkReferenceExpression, JASPERREPORTS_NAMESPACE,
					hyperlink.getHyperlinkReferenceExpression(), false);
			writer.writeExpression(JRXmlConstants.ELEMENT_hyperlinkAnchorExpression, JASPERREPORTS_NAMESPACE,
					hyperlink.getHyperlinkAnchorExpression(), false);
			writer.writeExpression(JRXmlConstants.ELEMENT_hyperlinkPageExpression, JASPERREPORTS_NAMESPACE,
					hyperlink.getHyperlinkPageExpression(), false);
			writer.writeExpression(JRXmlConstants.ELEMENT_hyperlinkTooltipExpression, JASPERREPORTS_NAMESPACE,
					hyperlink.getHyperlinkTooltipExpression(), false);
			writeHyperlinkParameters(hyperlink.getHyperlinkParameters());

			flush();
		}
	}

	/**
	 * 
	 */
	public void writeHyperlink(String indent, JRHyperlink hyperlink, String parentName, String hyperlinkSuffix)
	{
		if (hyperlink != null)
		{
			String hyperlinkName = parentName + hyperlinkSuffix;
			write(indent + "JRDesignHyperlink " + hyperlinkName + " = new JRDesignHyperlink();\n");

			write(indent + hyperlinkName + ".setLinkType(\"" + JRStringUtil.escapeJavaStringLiteral(hyperlink.getLinkType() != null ? hyperlink.getLinkType() : JRHyperlinkHelper.HYPERLINK_TYPE_NONE) + "\");\n");
			write(indent + hyperlinkName + ".setLinkTarget(\"" + JRStringUtil.escapeJavaStringLiteral(hyperlink.getLinkTarget() != null ? hyperlink.getLinkTarget() : JRHyperlinkHelper.HYPERLINK_TARGET_SELF) + "\");\n");

			writeExpression(indent, hyperlink.getHyperlinkReferenceExpression(), hyperlinkName, "HyperlinkReferenceExpression");
			writeExpression(indent, hyperlink.getHyperlinkAnchorExpression(), hyperlinkName, "HyperlinkAnchorExpression");
			writeExpression(indent, hyperlink.getHyperlinkPageExpression(), hyperlinkName, "HyperlinkPageExpression");
			writeExpression(indent, hyperlink.getHyperlinkTooltipExpression(), hyperlinkName, "HyperlinkTooltipExpression");
			writeHyperlinkParameters(indent, hyperlink.getHyperlinkParameters(), hyperlinkName);
			write(indent + parentName + ".set" + hyperlinkSuffix + "(" + hyperlinkName + ");\n");
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
	protected void writeConditionalStyle(String indent, JRConditionalStyle style, String styleName)
	{
		if(style != null)
		{
			write(indent + "JRDesignConditionalStyle " + styleName + " = new JRDesignConditionalStyle(jasperDesign);\n");
			writeExpression(indent, style.getConditionExpression(), styleName, "ConditionExpression");
			write(indent + styleName + ".setName(\"" + JRStringUtil.escapeJavaStringLiteral(style.getName()) + "\");\n");
			writeStyleReferenceAttr(indent, style, styleName);
			write(indent + styleName + ".setDefault(" + style.isDefault() + ");\n");
			write(indent + styleName + ".setMode((byte)" + style.getMode() + ");\n");
			write(indent + styleName + ".setFontName(\"" + JRStringUtil.escapeJavaStringLiteral(style.getFontName()) + "\");\n");
			write(indent + styleName + ".setFontSize(" + style.getFontSize() + ");\n");
			write(indent + styleName + ".setBold(" + style.isBold() + ");\n");
			write(indent + styleName + ".setItalic(" + style.isItalic() + ");\n");
			write(indent + styleName + ".setUnderline(" + style.isUnderline() + ");\n");
			write(indent + styleName + ".setStrikeThrough(" + style.isStrikeThrough() + ");\n");
			write(indent + styleName + ".setPdfFontName(\"" + JRStringUtil.escapeJavaStringLiteral(style.getPdfFontName()) + "\");\n");
			write(indent + styleName + ".setPdfEncoding(\"" + JRStringUtil.escapeJavaStringLiteral(style.getPdfEncoding()) + "\");\n");
			write(indent + styleName + ".setPdfEmbedded(" + style.isPdfEmbedded() + ");\n");
			write(indent + styleName + ".setForecolor(" + getColorText(style.getForecolor()) + ");\n");
			write(indent + styleName + ".setBackcolor(" + getColorText(style.getBackcolor()) + ");\n");
			write(indent + styleName + ".setFill((byte)" + style.getFill() + ");\n");
			write(indent + styleName + ".setRadius(" + style.getRadius() + ");\n");
			write(indent + styleName + ".setScaleImage((byte)" + style.getScaleImage() + ");\n");
			write(indent + styleName + ".setHorizontalAlignment((byte)" + style.getHorizontalAlignment() + ");\n");
			write(indent + styleName + ".setVerticalAlignment((byte)" + style.getVerticalAlignment() + ");\n");
			write(indent + styleName + ".setRotation((byte)" + style.getRotation() + ");\n");
			write(indent + styleName + ".setLineSpacing((byte)" + style.getLineSpacing() + ");\n");
			write(indent + styleName + ".setMarkup(\"" + JRStringUtil.escapeJavaStringLiteral(style.getMarkup()) + "\");\n");
			write(indent + styleName + ".setPattern(\"" + JRStringUtil.escapeJavaStringLiteral(style.getPattern()) + "\");\n");
			write(indent + styleName + ".setBlankWhenNull(" + style.isBlankWhenNull() + ");\n");
			
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
	public void writeComponentElement(String indent, JRComponentElement componentElement, String componentName)
	{
		if(componentElement != null)
		{
			write(indent + "JRDesignComponentElement " + componentName + " = new JRDesignComponentElement(jasperDesign);\n");
			writeReportElement(indent, componentElement, componentName);
			
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
	public void writeGenericElement(String indent, JRGenericElement element, String elementName)
	{
		write(indent + "JRDesignGenericElement " + elementName + " = new JRDesignGenericElement(jasperDesign);\n");
		
		write(indent + elementName + ".setEvaluationTime((byte)" + (element.getEvaluationTime() >0 ? element.getEvaluationTime() : JRExpression.EVALUATION_TIME_NOW) + ");\n");

		if (element.getEvaluationGroupName() != null)
		{
			write(indent + elementName + ".setEvaluationGroupName(\"" + JRStringUtil.escapeJavaStringLiteral(element.getEvaluationGroupName()) + "\");\n");
		}

		writeReportElement(indent, element, elementName);
		
		JRGenericElementType printKey = element.getGenericType();
		JRGenericElementType t = new JRGenericElementType(printKey.getNamespace(), printKey.getName());

		write(indent + "JRGenericElementType " + elementName + "Type = new JRGenericElementType(\"" 
				+ JRStringUtil.escapeJavaStringLiteral(printKey.getNamespace()) 
				+ "\", \"" 
				+ JRStringUtil.escapeJavaStringLiteral(printKey.getName()) 
				+ "\");\n");
		write(indent + elementName + ".setGenericType(" + elementName + "Type);\n");
		flush();//genericElementType

		JRGenericElementParameter[] params = element.getParameters();
		for (int i = 0; i < params.length; i++)
		{
			JRGenericElementParameter param = params[i];
			//TODO: constructor protected
//			JRBaseGenericElementParameter p = new JRBaseGenericElementParameter();

//			String paramName =  elementName + "Parameter" + i;
//			write(indent + "JRBaseGenericElementParameter " + paramName + " = new JRBaseGenericElementParameter();\n");
//			write(indent + paramName + ".setName(\"" + JRStringUtil.escapeJavaStringLiteral(params[i].getName()) + "\");\n");
//			writer.addAttribute(JRXmlConstants.ATTRIBUTE_skipWhenNull, 
//					param.isSkipWhenEmpty(), false);
//			
//			JRExpression valueExpression = param.getValueExpression();
//			if (valueExpression != null)
//			{
//				writer.writeExpression(JRXmlConstants.ELEMENT_genericElementParameter_valueExpression, 
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
	
	protected void writeStyleReferenceAttr(String indent, JRStyleContainer styleContainer, String styleName)
	{
		if (styleContainer.getStyle() != null)
		{
			write(indent + styleName + ".setParentStyle(\"" + JRStringUtil.escapeJavaStringLiteral(styleContainer.getStyle().getName()) + "\");\n");
		}
		else if (styleContainer.getStyleNameReference() != null)
		{
			write(indent + styleName + ".setParentStyleNameReference(" + styleContainer.getStyleNameReference() + ");\n");
		}
		flush();
	}
	
	/**
	 * 
	 * @param element
	 * @param pen
	 * @throws IOException
	 */
	private void writePen(String indent, JRPen pen, String penHolder)
	{
		if(pen != null)
		{
			write(indent + penHolder + ".setLineWidth(" + pen.getLineWidth() + "f);\n");
			write(indent + penHolder + ".setLineStyle((byte)" + pen.getLineStyle() + ");\n");
			write(indent + penHolder + ".setLineColor(" + getColorText(pen.getLineColor()) + ");\n");
			flush();
		}
	}

	/**
	 *
	 */
	protected void writeBox(String indent, JRLineBox box, String boxHolder)
	{
		if (box != null)
		{
			write(indent + boxHolder + ".setPadding(Integer.valueOf(" + box.getPadding().intValue() + "));\n");
			write(indent + boxHolder + ".setTopPadding(Integer.valueOf(" + box.getTopPadding().intValue() + "));\n");
			write(indent + boxHolder + ".setLeftPadding(Integer.valueOf(" + box.getLeftPadding().intValue() + "));\n");
			write(indent + boxHolder + ".setBottomPadding(Integer.valueOf(" + box.getBottomPadding().intValue() + "));\n");
			write(indent + boxHolder + ".setRightPadding(Integer.valueOf(" + box.getRightPadding().intValue() + "));\n");
			

			writePen(indent, box.getPen(), boxHolder + ".getPen()");
			writePen(indent, box.getTopPen(), boxHolder + ".getTopPen()");
			writePen(indent, box.getLeftPen(), boxHolder + ".getLeftPen()");
			writePen(indent, box.getBottomPen(), boxHolder + ".getBottomPen()");
			writePen(indent, box.getRightPen(), boxHolder + ".getRightPen()");

			flush();
		}
	}
	
	public void writeExpression(String indent, JRExpression expression, String parentName, String expressionSuffix)
	{
		writeExpression(indent, expression, parentName, expressionSuffix, null);
	}
	

	public void writeExpression(String indent,
			JRExpression expression, String parentName, String expressionSuffix, String defaultClassName)
	{
		if (expression != null)
		{
			String expressionName = parentName +  expressionSuffix;
			write(indent + "JRDesignExpression " + expressionName + " = new JRDesignExpression();\n");
			write(indent + expressionName + ".setId(" + expression.getId() + ");\n");
			write(indent + expressionName + ".setText(\"" + JRStringUtil.escapeJavaStringLiteral(expression.getText()) + "\");\n");
			if(expression.getValueClass() != null)
				write(indent + expressionName + ".setValueClass(Class.forName(\"" + (expression.getValueClass().getName()) + "\"));\n");
			else if(expression.getValueClassName() != null)
				write(indent + expressionName + ".setValueClassName(\"" + JRStringUtil.escapeJavaStringLiteral(expression.getValueClassName()) + "\");\n");
			else if(defaultClassName != null)
				write(indent + expressionName + ".setValueClassName(\"" + JRStringUtil.escapeJavaStringLiteral(defaultClassName) + "\");\n");

			JRExpressionChunk[] chunks = expression.getChunks();
			if(chunks != null && chunks.length >0)
			{
				String chunksName = "chunks_" + expressionName;
				write(indent + "JRDesignExpressionChunk[] " + chunksName + " = new JRDesignExpressionChunk[" + chunks.length + "];\n");
				for(int i=0; i<chunks.length; i++)
				{
					write(indent + chunksName + "[" + i + "] = new JRDesignExpressionChunk();\n");
					write(indent + chunksName + "[" + i + "].setType((byte)" + chunks[i].getType() + ");\n");
					write(indent + chunksName + "[" + i + "].setText(\"" + JRStringUtil.escapeJavaStringLiteral(chunks[i].getText()) + "\");\n");
					write(indent + expressionName + ".addChunk(" +chunksName + "[" + i + "]);\n");
				}
			}
			write(indent + parentName + ".set" + expressionSuffix + "(" + expressionName + ");\n");
			
			flush();
		}
	}

	private String getGroupName(String indent, JRGroup group)
	{
		if(group != null)
		{
			if(groupsMap.get(group.getName()) == null)
			{
				writeGroup(indent, group);
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
			writer.write(text);
		}
		catch(IOException e)
		{
			throw new JRRuntimeException(e);
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
}
