/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2014 TIBCO Software Inc. All rights reserved.
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
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;
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
import net.sf.jasperreports.crosstabs.CrosstabColumnCell;
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
import net.sf.jasperreports.crosstabs.type.CrosstabColumnPositionEnum;
import net.sf.jasperreports.crosstabs.type.CrosstabPercentageEnum;
import net.sf.jasperreports.crosstabs.type.CrosstabRowPositionEnum;
import net.sf.jasperreports.crosstabs.type.CrosstabTotalPositionEnum;
import net.sf.jasperreports.engine.DefaultJasperReportsContext;
import net.sf.jasperreports.engine.ExpressionReturnValue;
import net.sf.jasperreports.engine.JRAnchor;
import net.sf.jasperreports.engine.JRBand;
import net.sf.jasperreports.engine.JRBreak;
import net.sf.jasperreports.engine.JRChart;
import net.sf.jasperreports.engine.JRChartDataset;
import net.sf.jasperreports.engine.JRChartPlot;
import net.sf.jasperreports.engine.JRChartPlot.JRSeriesColor;
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
import net.sf.jasperreports.engine.JRHyperlinkParameter;
import net.sf.jasperreports.engine.JRImage;
import net.sf.jasperreports.engine.JRLine;
import net.sf.jasperreports.engine.JRLineBox;
import net.sf.jasperreports.engine.JRParagraph;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JRPen;
import net.sf.jasperreports.engine.JRPropertiesHolder;
import net.sf.jasperreports.engine.JRPropertiesMap;
import net.sf.jasperreports.engine.JRPropertyExpression;
import net.sf.jasperreports.engine.JRQuery;
import net.sf.jasperreports.engine.JRRectangle;
import net.sf.jasperreports.engine.JRReport;
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
import net.sf.jasperreports.engine.JRVisitor;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.ReturnValue;
import net.sf.jasperreports.engine.TabStop;
import net.sf.jasperreports.engine.design.JRDesignChart;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.query.JRJdbcQueryExecuterFactory;
import net.sf.jasperreports.engine.type.BreakTypeEnum;
import net.sf.jasperreports.engine.type.CalculationEnum;
import net.sf.jasperreports.engine.type.EvaluationTimeEnum;
import net.sf.jasperreports.engine.type.FooterPositionEnum;
import net.sf.jasperreports.engine.type.HyperlinkTargetEnum;
import net.sf.jasperreports.engine.type.HyperlinkTypeEnum;
import net.sf.jasperreports.engine.type.IncrementTypeEnum;
import net.sf.jasperreports.engine.type.LineDirectionEnum;
import net.sf.jasperreports.engine.type.OnErrorTypeEnum;
import net.sf.jasperreports.engine.type.OrientationEnum;
import net.sf.jasperreports.engine.type.PrintOrderEnum;
import net.sf.jasperreports.engine.type.ResetTypeEnum;
import net.sf.jasperreports.engine.type.RunDirectionEnum;
import net.sf.jasperreports.engine.type.SortFieldTypeEnum;
import net.sf.jasperreports.engine.type.SortOrderEnum;
import net.sf.jasperreports.engine.type.StretchTypeEnum;
import net.sf.jasperreports.engine.type.TabStopAlignEnum;
import net.sf.jasperreports.engine.type.WhenNoDataTypeEnum;
import net.sf.jasperreports.engine.type.WhenResourceMissingTypeEnum;
import net.sf.jasperreports.engine.xml.JRXmlWriter;
import net.sf.jasperreports.export.WriterExporterOutput;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.time.Day;


/**
 * A writer that generates the Java code required to produce a given report template programmatically, using the JasperReports API.
 * 
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public class JRApiWriter
{
	private static final Log log = LogFactory.getLog(JRApiWriter.class);
	public static final String EXCEPTION_MESSAGE_KEY_OUTPUT_STREAM_WRITE_ERROR = "util.api.writer.output.stream.write.error";
	public static final String EXCEPTION_MESSAGE_KEY_FILE_WRITE_ERROR = "util.api.writer.file.write.error";

	/**
	 *
	 */
	private JasperReportsContext jasperReportsContext;

	/**
	 * @deprecated To be removed.
	 */
	private JRReport report;

	/**
	 *
	 */
	private Map<String, String> stylesMap = new HashMap<String, String>();

	/**
	 *
	 */
	private Map<String, String> groupsMap = new HashMap<String, String>();

	
	private Writer writer;
	
	private JRApiWriterVisitor apiWriterVisitor = new JRApiWriterVisitor(this);

	private String indent;
	
	
	/**
	 *
	 */
	public JRApiWriter(JasperReportsContext jasperReportsContext)
	{
		this.jasperReportsContext = jasperReportsContext;
	}

	
	/**
	 * @deprecated To be removed.
	 */
	protected JRApiWriter(JRReport report)
	{
		this.report = report;
	}


	/**
	 *
	 */
	public String write(JRReport report)
	{
		StringWriter buffer = new StringWriter();//FIXME use file buffered writer
		writeReport(report, buffer);
		return buffer.toString();
	}


	/**
	 *
	 */
	public void write(
		JRReport report,
		String destFileName
		) throws JRException
	{
		FileOutputStream fos = null;
		
		try
		{
			fos = new FileOutputStream(destFileName);
			String encoding = report.getProperty(WriterExporterOutput.PROPERTY_CHARACTER_ENCODING) != null
			? report.getProperty(WriterExporterOutput.PROPERTY_CHARACTER_ENCODING)
			: "UTF-8";//FIXME this is an export time config property
			Writer out = new OutputStreamWriter(fos, encoding);
			writeReport(report, out);
		}
		catch (IOException e)
		{
			throw 
				new JRException(
					EXCEPTION_MESSAGE_KEY_FILE_WRITE_ERROR,
					new Object[]{destFileName},
					e);
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
	public void write(
		JRReport report,
		OutputStream outputStream
		) throws JRException
	{
		try
		{
			String encoding = report.getProperty(WriterExporterOutput.PROPERTY_CHARACTER_ENCODING) != null
			? report.getProperty(WriterExporterOutput.PROPERTY_CHARACTER_ENCODING)
			: "UTF-8";
			
			Writer out = new OutputStreamWriter(outputStream, encoding);
			writeReport(report, out);
		}
		catch (Exception e)
		{
			throw 
				new JRException(
					EXCEPTION_MESSAGE_KEY_OUTPUT_STREAM_WRITE_ERROR,
					new Object[]{report.getName()},
					e);
		}
	}


	/**
	 * @see #write(JRReport)
	 */
	public static String writeReport(JRReport report)
	{
		return new JRApiWriter(DefaultJasperReportsContext.getInstance()).write(report);
	}


	/**
	 * @see #write(JRReport, String)
	 */
	public static void writeReport(
		JRReport report,
		String destFileName
		) throws JRException
	{
		new JRApiWriter(DefaultJasperReportsContext.getInstance()).write(report, destFileName);
	}


	/**
	 * @see #write(JRReport, OutputStream)
	 */
	public static void writeReport(
		JRReport report,
		OutputStream outputStream
		) throws JRException
	{
		new JRApiWriter(DefaultJasperReportsContext.getInstance()).write(report, outputStream);
	}


	/**
	 *
	 */
	protected void writeReport(JRReport report, Writer aWriter)
	{
		this.writer = aWriter;
		indent = "";

		write("/*\n");
		write(" * Generated by JasperReports - ");
		write((new SimpleDateFormat()).format(new java.util.Date()));
		write("\n");
		write(" */\n");
		write("import java.awt.Color;\n");
		write("\n");
		write("import org.jfree.chart.plot.PlotOrientation;\n");
		write("import org.jfree.chart.renderer.xy.XYBubbleRenderer;\n");
		write("\n");
		write("import net.sf.jasperreports.charts.*;\n");
		write("import net.sf.jasperreports.charts.design.*;\n");
		write("import net.sf.jasperreports.charts.util.*;\n");
		write("import net.sf.jasperreports.crosstabs.*;\n");
		write("import net.sf.jasperreports.crosstabs.design.*;\n");
		write("import net.sf.jasperreports.crosstabs.fill.calculation.BucketDefinition;\n");
		write("import net.sf.jasperreports.engine.*;\n");
		write("import net.sf.jasperreports.engine.base.JRBaseChartPlot.JRBaseSeriesColor;\n");
		write("import net.sf.jasperreports.engine.base.JRBaseFont;\n");
		write("import net.sf.jasperreports.engine.design.*;\n");
		write("import net.sf.jasperreports.engine.type.*;\n");
		write("import net.sf.jasperreports.engine.util.ReportCreator;\n");
		write("\n\n");
		write("public class {0} implements ReportCreator\n", JRStringUtil.escapeJavaStringLiteral(report.getName()));
		write("{\n\n");
		indent += "  ";
		write( "public JasperDesign create() throws JRException\n");
		write( "{\n");
		indent += "  ";
		write( "JasperDesign jasperDesign = new JasperDesign();\n");
		write( "jasperDesign.setName(\"{0}\");\n", JRStringUtil.escapeJavaStringLiteral(report.getName()));
		write( "jasperDesign.setLanguage(\"{0}\");\n", JRStringUtil.escapeJavaStringLiteral(report.getLanguage()));
		write( "jasperDesign.setColumnCount({0, number, #});\n", report.getColumnCount(), 1);
		write( "jasperDesign.setPrintOrder({0});\n", report.getPrintOrderValue(), PrintOrderEnum.VERTICAL);
		write( "jasperDesign.setColumnDirection({0});\n", report.getColumnDirection(), RunDirectionEnum.LTR);
		write( "jasperDesign.setPageWidth({0, number, #});\n", report.getPageWidth());
		write( "jasperDesign.setPageHeight({0, number, #});\n", report.getPageHeight());
		write( "jasperDesign.setOrientation({0});\n", report.getOrientationValue(), OrientationEnum.PORTRAIT);
		write( "jasperDesign.setWhenNoDataType({0});\n", report.getWhenNoDataTypeValue(), WhenNoDataTypeEnum.NO_PAGES);
		write( "jasperDesign.setColumnWidth({0, number, #});\n", report.getColumnWidth());
		write( "jasperDesign.setColumnSpacing({0, number, #});\n", report.getColumnSpacing());
		write( "jasperDesign.setLeftMargin({0, number, #});\n", report.getLeftMargin());
		write( "jasperDesign.setRightMargin({0, number, #});\n", report.getRightMargin());
		write( "jasperDesign.setTopMargin({0, number, #});\n", report.getTopMargin());
		write( "jasperDesign.setBottomMargin({0, number, #});\n", report.getBottomMargin());
		write( "jasperDesign.setTitleNewPage({0});\n", report.isTitleNewPage(), false);
		write( "jasperDesign.setSummaryNewPage({0});\n", report.isSummaryNewPage(), false);
		write( "jasperDesign.setSummaryWithPageHeaderAndFooter({0});\n", report.isSummaryWithPageHeaderAndFooter(), false);
		write( "jasperDesign.setFloatColumnFooter({0});\n", report.isFloatColumnFooter(), false);
		write( "jasperDesign.setScriptletClass(\"{0}\");\n", report.getScriptletClass());
		write( "jasperDesign.setFormatFactoryClass(\"{0}\");\n", report.getFormatFactoryClass());
		write( "jasperDesign.setResourceBundle(\"{0}\");\n", JRStringUtil.escapeJavaStringLiteral(report.getResourceBundle()));
		write( "jasperDesign.setWhenResourceMissingType({0});\n", report.getWhenResourceMissingTypeValue(), WhenResourceMissingTypeEnum.NULL);
		write( "jasperDesign.setIgnorePagination({0});\n\n", report.isIgnorePagination(), false);

		writeProperties( report, "jasperDesign");
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
					write("jasperDesign.addImport(\"{0}\");\n", value);
				}
			}
		}
		
		writeTemplates(report, indent);

		write("\n");
		
		JRStyle[] styles = report.getStyles();
		if (styles != null && styles.length > 0)
		{	
			write( "//styles\n");

			for(int i = 0; i < styles.length; i++)
			{
				String styleName = JRStringUtil.getJavaIdentifier(styles[i].getName());
				writeStyle( styles[i], styleName);
				write( "jasperDesign.addStyle(" + styleName + ");\n\n");

				if (toWriteConditionalStyles())
				{
					JRConditionalStyle[] conditionalStyles = styles[i].getConditionalStyles();
					if (!(styles[i] instanceof JRConditionalStyle) && conditionalStyles != null)
					{
						for (int j = 0; j < conditionalStyles.length; j++)
						{
							String conditionalStyleName = styleName + "Conditional" + j;
							writeConditionalStyle( conditionalStyles[j],conditionalStyleName);
							write( styles[i].getName() + ".addConditionalStyle(" + conditionalStyleName + ");\n\n");
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
				{
					write( "jasperDesign.addDataset(reportDataset" + i + ");\n");
				}
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
		indent = "  ";
		write( "}\n\n");
		indent = ""; 
		write("}\n");
		
		flush();//FIXME is this necessary?
		close();
	}


	/**
	 * @deprecated Replaced by {@link #writeReport(JRReport, Writer)}.
	 */
	protected void writeReport(Writer aWriter)
	{
		writeReport(report, aWriter);
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
	protected void writeTemplates(JRReport report, String indent)
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
	private void writeScriptlet( JRScriptlet scriptlet, String scriptletName)
	{
		if(scriptlet != null)
		{
			write( "JRDesignScriptlet " + scriptletName + " = new JRDesignScriptlet();\n");
			write( scriptletName + ".setDescription(\"{0}\");\n", JRStringUtil.escapeJavaStringLiteral(scriptlet.getDescription()));
			write( scriptletName + ".setName(\"{0}\");\n", JRStringUtil.escapeJavaStringLiteral(scriptlet.getName()));
			write( scriptletName + ".setValueClassName(\"{0}\");\n", scriptlet.getValueClassName());
	
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
			write( parameterName + ".setName(\"{0}\");\n", JRStringUtil.escapeJavaStringLiteral(parameter.getName()));
			write( parameterName + ".setDescription(\"{0}\");\n", JRStringUtil.escapeJavaStringLiteral(parameter.getDescription()));
			write( parameterName + ".setValueClassName(\"{0}\");\n", parameter.getValueClassName());
			
			write( parameterName + ".setNestedTypeName(\"{0}\");\n", JRStringUtil.escapeJavaStringLiteral(parameter.getNestedTypeName()));
			
			write( parameterName + ".setForPrompting({0});\n", parameter.isForPrompting(), true);
	
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
			write( queryName + ".setLanguage(\"{0}\");\n", JRStringUtil.escapeJavaStringLiteral(query.getLanguage()), JRJdbcQueryExecuterFactory.QUERY_LANGUAGE_SQL);
			write( queryName + ".setText(\"{0}\");\n", JRStringUtil.escapeJavaStringLiteral(query.getText()));
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
			write( fieldName + ".setName(\"{0}\");\n", JRStringUtil.escapeJavaStringLiteral(field.getName()));
			write( fieldName + ".setDescription(\"{0}\");\n", JRStringUtil.escapeJavaStringLiteral(field.getDescription()));
			write( fieldName + ".setValueClassName(\"{0}\");\n", field.getValueClassName());
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
			write( sortFieldName + ".setName(\"{0}\");\n", JRStringUtil.escapeJavaStringLiteral(sortField.getName()));
			write( sortFieldName + ".setOrder({0});\n", sortField.getOrderValue(), SortOrderEnum.ASCENDING);
			write( sortFieldName + ".setType({0});\n", sortField.getType(), SortFieldTypeEnum.FIELD);
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
			write( variableName + ".setName(\"{0}\");\n", JRStringUtil.escapeJavaStringLiteral(variable.getName()));
			write( variableName + ".setValueClassName(\"{0}\");\n", variable.getValueClassName());
			write( variableName + ".setResetType({0});\n", variable.getResetTypeValue(), ResetTypeEnum.REPORT);
			write( variableName + ".setResetGroup({0});\n", resetGroupName);
			write( variableName + ".setIncrementType({0});\n", variable.getIncrementTypeValue(), IncrementTypeEnum.NONE);
			write( variableName + ".setIncrementGroup({0});\n", incrementGroupName);
			
			write( variableName + ".setCalculation({0});\n", variable.getCalculationValue(), CalculationEnum.NOTHING);
			write( variableName + ".setIncrementerFactoryClass({0}.class);\n", JRStringUtil.escapeJavaStringLiteral(variable.getIncrementerFactoryClassName()));
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
		String groupName = group.getName();
		
		groupsMap.put(groupName, groupName);

		write( "JRDesignGroup " + groupName + " = new JRDesignGroup();\n");
		write( groupName + ".setName(\"" + JRStringUtil.escapeJavaStringLiteral(groupName) + "\");\n");
		write( groupName + ".setStartNewColumn({0});\n", group.isStartNewColumn(), false);
		write( groupName + ".setStartNewPage({0});\n", group.isStartNewPage(), false);
		write( groupName + ".setReprintHeaderOnEachPage({0});\n", group.isReprintHeaderOnEachPage(), false);
		write( groupName + ".setMinHeightToStartNewPage({0});\n", group.getMinHeightToStartNewPage());
		write( groupName + ".setFooterPosition({0});\n", group.getFooterPositionValue(), FooterPositionEnum.NORMAL);
		
		write( groupName + ".setKeepTogether({0});\n", group.isKeepTogether(), false);

		writeExpression( group.getExpression(), groupName, "Expression");

		JRSection groupHeader = group.getGroupHeaderSection();
		if (groupHeader != null)
		{
			writeSection(
					groupHeader, 
					groupName+"Header", 
					"((JRDesignSection)" + groupName + ".getGroupHeaderSection()).getBandsList()"
					);
		}

		JRSection groupFooter = group.getGroupFooterSection();
		if (groupFooter != null)
		{
			writeSection(
					groupFooter, 
					groupName+"Footer", 
					"((JRDesignSection)" + groupName + ".getGroupFooterSection()).getBandsList()"
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
			write( bandName + ".setHeight({0, number, #});\n", band.getHeight());
			write( bandName + ".setSplitType({0});\n", band.getSplitTypeValue());
			writeExpression( band.getPrintWhenExpression(), bandName, "PrintWhenExpression");

			writeChildElements( band, bandName);
	
			List<ExpressionReturnValue> returnValues = band.getReturnValues();
			if (returnValues != null && !returnValues.isEmpty())
			{
				for (ListIterator<ExpressionReturnValue> it = returnValues.listIterator(); it.hasNext();)
				{
					ExpressionReturnValue returnValue = it.next();
					String returnValueVarName = bandName + "ReturnValue" + it.previousIndex();
					writeReturnValue(returnValue, returnValueVarName);
					write(bandName + ".addReturnValue(" + returnValueVarName + ");\n");
				}
			}

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
		List<JRChild> children = elementContainer.getChildren();
		if (children != null && children.size() > 0)
		{
			for(int i = 0; i < children.size(); i++)
			{
				String childName = parentName + "_" + i;
				apiWriterVisitor.setName(childName);
				children.get(i).visit(apiWriterVisitor);
				if(children.get(i) instanceof JRElementGroup && !(children.get(i) instanceof JRElement))
				{
					write( parentName +".addElementGroup(" + childName + ");\n\n");
					
				}
				else if (!(children.get(i) instanceof JRComponentElement))//FIXME component
				{
					write( parentName +".addElement(" + childName + ");\n\n");
				}
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
			write( breakName + ".setType({0});\n", breakElement.getTypeValue(), BreakTypeEnum.PAGE);
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
			write( lineName + ".setDirection({0});\n", line.getDirectionValue(), LineDirectionEnum.TOP_DOWN);
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
			write( elementName + ".setKey(\"{0}\");\n", JRStringUtil.escapeJavaStringLiteral(element.getKey()));
			writeStyleReferenceAttr( element, elementName);
			write( elementName + ".setPositionType({0});\n", element.getPositionTypeValue());
			write( elementName + ".setStretchType({0});\n", element.getStretchTypeValue(), StretchTypeEnum.NO_STRETCH);
			write( elementName + ".setPrintRepeatedValues({0});\n", element.isPrintRepeatedValues(),true);
			write( elementName + ".setMode({0});\n", element.getOwnModeValue());
			write( elementName + ".setX({0, number, #});\n", element.getX());
			write( elementName + ".setY({0, number, #});\n", element.getY());
			write( elementName + ".setWidth({0, number, #});\n", element.getWidth());
			write( elementName + ".setHeight({0, number, #});\n", element.getHeight());
			write( elementName + ".setRemoveLineWhenBlank({0});\n", element.isRemoveLineWhenBlank(), false);
			write( elementName + ".setPrintInFirstWholeBand({0});\n", element.isPrintInFirstWholeBand(), false);
			write( elementName + ".setPrintWhenDetailOverflows({0});\n", element.isPrintWhenDetailOverflows(), false);

			if (element.getPrintWhenGroupChanges() != null)
			{
				String groupName = getGroupName( element.getPrintWhenGroupChanges());
				write( elementName + ".setPrintWhenGroupChanges(" + groupName + ");\n");
			}
			
			write( elementName + ".setForecolor({0});\n", element.getOwnForecolor());
			write( elementName + ".setBackcolor({0});\n", element.getOwnBackcolor());
	
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
			write( propertyExpressionName + ".setName(\"{0}\");\n", JRStringUtil.escapeJavaStringLiteral(propertyExpression.getName()));
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
			write( elementName + ".setFill({0});\n", element.getOwnFillValue());
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
			write( rectangleName + ".setRadius({0});\n", rectangle.getOwnRadius());
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
			write( imageName + ".setScaleImage({0});\n", image.getOwnScaleImageValue());
			write( imageName + ".setHorizontalImageAlign({0});\n", image.getOwnHorizontalImageAlign());
			write( imageName + ".setVerticalImageAlign({0});\n", image.getOwnVerticalImageAlign());
			write( imageName + ".setUsingCache({0});\n", image.getUsingCache());
			write( imageName + ".setLazy({0});\n", image.isLazy(), false);
			write( imageName + ".setOnErrorType({0});\n",image.getOnErrorTypeValue(),  OnErrorTypeEnum.ERROR);
			write( imageName + ".setEvaluationTime({0});\n", image.getEvaluationTimeValue(), EvaluationTimeEnum.NOW);
			write( imageName + ".setEvaluationGroup({0});\n", getGroupName(image.getEvaluationGroup()));

			if(image.getLinkType() != null)
			{
				write( imageName + ".setLinkType(\"{0}\");\n", JRStringUtil.escapeJavaStringLiteral(image.getLinkType()), HyperlinkTypeEnum.NONE.getName());
			}
			if(image.getLinkTarget() != null)
			{
				write( imageName + ".setLinkTarget(\"{0}\");\n", JRStringUtil.escapeJavaStringLiteral(image.getLinkTarget()), HyperlinkTargetEnum.SELF.getName());
			}
			write( imageName + ".setBookmarkLevel({0, number, #});\n", image.getBookmarkLevel(), JRAnchor.NO_BOOKMARK);
			
			writeReportElement( image, imageName);
			writeBox( image.getLineBox(), imageName + ".getLineBox()");
			writeGraphicElement( image, imageName);
			
			writeExpression( image.getExpression(), imageName, "Expression");
			writeExpression( image.getAnchorNameExpression(), imageName, "AnchorNameExpression");
			writeExpression( image.getHyperlinkReferenceExpression(), imageName, "HyperlinkReferenceExpression");
			writeExpression( image.getHyperlinkWhenExpression(), imageName, "HyperlinkWhenExpression");
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
			write( staticTextName + ".setText(\"{0}\");\n", JRStringUtil.escapeJavaStringLiteral(staticText.getText()));
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
			write( textElementName + ".setHorizontalTextAlign({0});\n", textElement.getOwnHorizontalTextAlign());
			write( textElementName + ".setVerticalTextAlign({0});\n", textElement.getOwnVerticalTextAlign());
			write( textElementName + ".setRotation({0});\n", textElement.getOwnRotationValue());
			write( textElementName + ".setMarkup(\"{0}\");\n", JRStringUtil.escapeJavaStringLiteral(textElement.getOwnMarkup()));
			writeFont( textElement, textElementName);
			writeParagraph( textElement.getParagraph(), textElementName);
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
			write( fontHolderName + ".setFontName(\"{0}\");\n", JRStringUtil.escapeJavaStringLiteral(font.getOwnFontName()));
			write( fontHolderName + ".setFontSize({0});\n", font.getOwnFontsize());
			write( fontHolderName + ".setBold({0});\n", font.isOwnBold());
			write( fontHolderName + ".setItalic({0});\n", font.isOwnItalic());
			write( fontHolderName + ".setUnderline({0});\n", font.isOwnUnderline());
			write( fontHolderName + ".setStrikeThrough({0});\n", font.isOwnStrikeThrough());
			write( fontHolderName + ".setPdfFontName(\"{0}\");\n", JRStringUtil.escapeJavaStringLiteral(font.getOwnPdfFontName()));
			write( fontHolderName + ".setPdfEncoding(\"{0}\");\n", JRStringUtil.escapeJavaStringLiteral(font.getOwnPdfEncoding()));
			write( fontHolderName + ".setPdfEmbedded({0});\n", font.isOwnPdfEmbedded());
			flush();
		}
	}

	
	/**
	 *
	 */
	private void writeCommonStyle(JRStyle style, String styleName)
	{
		write( styleName + ".setMode({0});\n", style.getOwnModeValue());
		write( styleName + ".setFontName(\"{0}\");\n", JRStringUtil.escapeJavaStringLiteral(style.getOwnFontName()));
		write( styleName + ".setFontSize({0});\n", style.getOwnFontsize());
		write( styleName + ".setBold({0});\n", style.isOwnBold());
		write( styleName + ".setItalic({0});\n", style.isOwnItalic());
		write( styleName + ".setUnderline({0});\n", style.isOwnUnderline());
		write( styleName + ".setStrikeThrough({0});\n", style.isOwnStrikeThrough());
		write( styleName + ".setPdfFontName(\"{0}\");\n", JRStringUtil.escapeJavaStringLiteral(style.getOwnPdfFontName()));
		write( styleName + ".setPdfEncoding(\"{0}\");\n", JRStringUtil.escapeJavaStringLiteral(style.getOwnPdfEncoding()));
		write( styleName + ".setPdfEmbedded({0});\n", style.isOwnPdfEmbedded());
		write( styleName + ".setForecolor({0});\n", style.getOwnForecolor());
		write( styleName + ".setBackcolor({0});\n", style.getOwnBackcolor());
		write( styleName + ".setFill({0});\n", style.getOwnFillValue());
		write( styleName + ".setRadius({0});\n", style.getOwnRadius());
		write( styleName + ".setScaleImage({0});\n", style.getOwnScaleImageValue());
		write( styleName + ".setHorizontalTextAlign({0});\n", style.getOwnHorizontalTextAlign());
		write( styleName + ".setHorizontalImageAlign({0});\n", style.getOwnHorizontalImageAlign());
		write( styleName + ".setVerticalTextAlign({0});\n", style.getOwnVerticalTextAlign());
		write( styleName + ".setVerticalImageAlign({0});\n", style.getOwnVerticalImageAlign());
		write( styleName + ".setRotation({0});\n", style.getOwnRotationValue());

		write( styleName + ".setMarkup(\"{0}\");\n", JRStringUtil.escapeJavaStringLiteral(style.getOwnMarkup()));
		write( styleName + ".setPattern(\"{0}\");\n", JRStringUtil.escapeJavaStringLiteral(style.getOwnPattern()));
		write( styleName + ".setBlankWhenNull({0});\n", style.isOwnBlankWhenNull());

		writePen( style.getLinePen(), styleName + ".getLinePen()");
		writeBox( style.getLineBox(), styleName + ".getLineBox()");
		writeParagraph( style.getParagraph(), styleName);
	}

	
	/**
	 *
	 */
	private void writeStyle( JRStyle style, String styleName)
	{
		if (style != null && stylesMap.get(style.getName()) == null)
		{
			write( "JRDesignStyle " + styleName + " = new JRDesignStyle();\n");

			write( styleName + ".setName(\"{0}\");\n", JRStringUtil.escapeJavaStringLiteral(style.getName()));

			if (style.getStyle() != null)//FIXME double check which one to use; style or styleNameReference?
			{
				write( styleName + ".setParentStyle({0});\n", JRStringUtil.escapeJavaStringLiteral(style.getStyle().getName()));
			}
			else if (style.getStyleNameReference() != null)
			{
				write( styleName + ".setParentStyleNameReference(\"{0}\");\n", JRStringUtil.escapeJavaStringLiteral(style.getStyleNameReference()));
			}
			
			write( styleName + ".setDefault({0});\n", style.isDefault(), false);

			writeCommonStyle(style, styleName);

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
			write( textFieldName + ".setBold({0});\n", textField.isOwnBold());
			write( textFieldName + ".setStretchWithOverflow({0});\n", textField.isStretchWithOverflow(), false);
			write( textFieldName + ".setEvaluationTime({0});\n", textField.getEvaluationTimeValue(), EvaluationTimeEnum.NOW);
			write( textFieldName + ".setEvaluationGroup({0});\n", getGroupName(textField.getEvaluationGroup()));

			write( textFieldName + ".setPattern(\"{0}\");\n", JRStringUtil.escapeJavaStringLiteral(textField.getOwnPattern()));
			write( textFieldName + ".setBlankWhenNull({0});\n", textField.isOwnBlankWhenNull());

			if(textField.getLinkType() != null)
			{
				write( textFieldName + ".setLinkType(\"{0}\");\n", JRStringUtil.escapeJavaStringLiteral(textField.getLinkType()), HyperlinkTypeEnum.NONE.getName());
			}
			if(textField.getLinkTarget() != null)
			{
				write( textFieldName + ".setLinkTarget(\"{0}\");\n", JRStringUtil.escapeJavaStringLiteral(textField.getLinkTarget()), HyperlinkTargetEnum.SELF.getName());
			}
			write( textFieldName + ".setBookmarkLevel({0, number, #});\n", textField.getBookmarkLevel(), JRAnchor.NO_BOOKMARK);

			writeReportElement( textField, textFieldName);
			writeBox( textField.getLineBox(), textFieldName + ".getLineBox()");
			writeTextElement( textField, textFieldName);
	
			writeExpression( textField.getExpression(), textFieldName, "Expression");
	
			writeExpression( textField.getPatternExpression(), textFieldName, "PatternExpression");
			
			writeExpression( textField.getAnchorNameExpression(), textFieldName, "AnchorNameExpression");
	
			writeExpression( textField.getHyperlinkReferenceExpression(), textFieldName, "HyperlinkReferenceExpression");
	
			writeExpression( textField.getHyperlinkWhenExpression(), textFieldName, "HyperlinkWhenExpression");
			
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
			write( subreportName + ".setUsingCache({0});\n", subreport.getUsingCache());
			write( subreportName + ".setRunToBottom({0});\n", subreport.isRunToBottom());
			write(subreportName + ".setOverflowType({0});\n", subreport.getOverflowType());
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
			write( subreportParameterName + ".setName(\"{0}\");\n", JRStringUtil.escapeJavaStringLiteral(subreportParameter.getName()));
			writeExpression( subreportParameter.getExpression(), subreportParameterName, "Expression");
			flush();
		}
	}

	/**
	 *
	 */
	private void writeDatasetParameter( JRDatasetParameter datasetParameter, String runName, String datasetParameterName)
	{
		if(datasetParameter != null)
		{
			write( "JRDesignDatasetParameter " + datasetParameterName + " = new JRDesignSubreportParameter();\n");
			write( datasetParameterName + ".setName(\"{0}\");\n", JRStringUtil.escapeJavaStringLiteral(datasetParameter.getName()));
			writeExpression( datasetParameter.getExpression(), datasetParameterName, "Expression");
			
			write( runName + ".addParameter(" + datasetParameterName + ");\n");
			
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
			write( chartName + ".setShowLegend({0});\n", getBooleanText(chart.getShowLegend()));
			write( chartName + ".setEvaluationTime({0});\n", chart.getEvaluationTimeValue(), EvaluationTimeEnum.NOW);
			write( chartName + ".setEvaluationGroup({0});\n", getGroupName(chart.getEvaluationGroup()));
	
			if(chart.getLinkType() != null)
			{
				write( chartName + ".setLinkType(\"{0}\");\n", JRStringUtil.escapeJavaStringLiteral(chart.getLinkType()), HyperlinkTypeEnum.NONE.getName());
			}
			if(chart.getLinkTarget() != null)
			{
				write( chartName + ".setLinkTarget(\"{0}\");\n", JRStringUtil.escapeJavaStringLiteral(chart.getLinkTarget()), HyperlinkTargetEnum.SELF.getName());
			}
			write( chartName + ".setBookmarkLevel({0, number, #});\n", chart.getBookmarkLevel(), JRAnchor.NO_BOOKMARK);

			if(chart.getCustomizerClass() != null)
			{
				write( chartName + ".setCustomizerClass(\"{0}\");\n", JRStringUtil.escapeJavaStringLiteral(chart.getCustomizerClass()));
			}
			write( chartName + ".setRenderType(\"{0}\");\n", JRStringUtil.escapeJavaStringLiteral(chart.getRenderType()));
			write( chartName + ".setTheme(\"{0}\");\n", JRStringUtil.escapeJavaStringLiteral(chart.getTheme()));

			writeReportElement( chart, chartName);
			writeBox( chart.getLineBox(), chartName + ".getLineBox()");
	
			write( chartName + ".setTitlePosition({0});\n", chart.getTitlePositionValue());
			write( chartName + ".setTitleColor({0});\n", chart.getOwnTitleColor());
			if(chart.getTitleFont() != null)
			{
				write( chartName + ".setTitleFont(new JRBaseFont());\n");
				writeFont( chart.getTitleFont(), chartName + ".getTitleFont()");
			}
			writeExpression( chart.getTitleExpression(), chartName, "TitleExpression");
			write( chartName + ".setSubtitleColor({0});\n", chart.getOwnSubtitleColor());
			
			if(chart.getSubtitleFont() != null)
			{
				write( chartName + ".setSubtitleFont(new JRBaseFont());\n");
				writeFont( chart.getSubtitleFont(), chartName + ".getSubtitleFont()");
			}

			writeExpression( chart.getSubtitleExpression(), chartName, "SubtitleExpression");
			write( chartName + ".setLegendColor({0});\n", chart.getOwnLegendColor());
			write( chartName + ".setLegendBackgroundColor({0});\n", chart.getOwnLegendBackgroundColor());
			write( chartName + ".setLegendPosition({0});\n", chart.getLegendPositionValue());

			if(chart.getLegendFont() != null)
			{
				write( chartName + ".setLegendFont(new JRBaseFont());\n");
				writeFont( chart.getLegendFont(), chartName + ".getLegendFont()");
			}
	
			writeExpression( chart.getAnchorNameExpression(), chartName, "AnchorNameExpression");
			writeExpression( chart.getHyperlinkReferenceExpression(), chartName, "HyperlinkReferenceExpression");
			writeExpression( chart.getHyperlinkWhenExpression(), chartName, "HyperlinkWhenExpression");//FIXMENOW can we reuse hyperlink write method?
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
	 */
	public void writeElementDataset( JRElementDataset dataset, boolean skipIfEmpty, String datasetName)
	{
		if(dataset != null)
		{
			write( datasetName + ".setResetType({0});\n", dataset.getResetTypeValue(), ResetTypeEnum.REPORT);
	
			if (dataset.getResetTypeValue() == ResetTypeEnum.GROUP)
			{
				String resetGroupName = getGroupName(  dataset.getResetGroup());
				write( datasetName + ".setResetGroup(" + resetGroupName + ");\n");
			}
			
			write( datasetName + ".setIncrementType({0});\n", dataset.getIncrementTypeValue(), IncrementTypeEnum.NONE);
	
			if (dataset.getIncrementTypeValue() == IncrementTypeEnum.GROUP)
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
			write( "JRDesignCategoryDataset " + datasetName + " = new JRDesignCategoryDataset("+ parentName + ".getDataset());\n");
	
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
			write( "JRDesignTimeSeriesDataset " + datasetName + " =  new JRDesignTimeSeriesDataset(" + parentName + ".getDataset());\n");

			if (dataset.getTimePeriod() != null && !Day.class.getName().equals(dataset.getTimePeriod().getName()))
			{
				write( datasetName + ".setTimePeriod({0}.class);\n", dataset.getTimePeriod().getName());
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
			write( "JRDesignGanttDataset " + datasetName + " = new JRDesignGanttDataset(" + parentName + ".getDataset());\n");
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
			write( "JRDesignTimePeriodDataset " + datasetName + " = new JRDesignTimePeriodDataset(" + parentName + ".getDataset());\n");
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
			write( "JRDesignPieSeries " + pieSeriesName + " = new JRDesignPieSeries();\n");
	
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

			write( "JRDesignCategorySeries " + categorySeriesName + " = new JRDesignCategorySeries();\n");

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
			write( "JRDesignXyzDataset " + datasetName + " = new JRDesignXyzDataset(" + parentName + ".getDataset());\n");
	
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

			write( "JRDesignXyzSeries " + xyzSeriesName + " = new JRDesignXyzSeries();\n");
	
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
			String xySeriesName = parentName + "XySeries" + index;
			write( "JRDesignXySeries " + xySeriesName + " = new JRDesignXySeries();\n");
			if(xySeries.getAutoSort() != null)
			{
				write( xySeriesName + ".setAutoSort({0});\n", xySeries.getAutoSort());
			}
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
			write( "JRDesignXyDataset " + datasetName + " = new JRDesignXyDataset(" + parentName + ".getDataset());\n");
	
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
			String timeSeriesName = parentName + "TimeSeries" + index;
			write( "JRDesignTimeSeries " + timeSeriesName + " = new JRDesignTimeSeries();\n");
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
			String ganttSeriesName = parentName + "GanttSeries" + index;
			write( "JRDesignGanttSeries " + ganttSeriesName + " = new JRDesignGanttSeries();\n");
			
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
			String timePeriodSeriesName = parentName + "TimePeriodSeries" + index;
			write( "JRDesignTimePeriodSeries " + timePeriodSeriesName + " = new JRDesignTimePeriodSeries();\n");
			
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
			write( "JRDesignPieDataset " + datasetName + " = new JRDesignPieDataset(" + parentName + ".getDataset());\n");
			write( datasetName + ".setMaxCount(Integer.valueOf({0, number, #}));\n", dataset.getMaxCount());
			write( datasetName + ".setMinPercentage({0});\n", dataset.getMinPercentage());
	
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
			write( "JRDesignValueDataset " + datasetName + " = new JRDesignValueDataset(" + parentName + ".getDataset());\n");
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

			write( "JRDesignValueDisplay " + valueDisplayName + " = new JRDesignValueDisplay(" +parentName + ".getValueDisplay(), " + parentName + ".getChart());\n");
			
			write( valueDisplayName + ".setColor({0});\n", valueDisplay.getColor());
			write( valueDisplayName + ".setMask(\"{0}\");\n", valueDisplay.getMask());
			write( valueDisplayName + ".setFont(new JRBaseFont());\n");
			if(valueDisplay.getFont() != null)
			{
				write( valueDisplayName + ".setFont(new JRBaseFont());\n");
				writeFont( valueDisplay.getFont(), valueDisplayName + ".getFont()");
			}
			
			write( parentName + ".setValueDisplay(" + valueDisplayName + ");\n");
			
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
			write( "JRDesignItemLabel " + itemLabelName + " = new JRDesignItemLabel("+ parentName + ".getItemLabel(), " + parentName + ".getChart());\n");
			write( itemLabelName + ".setColor({0});\n", itemLabel.getColor());
			write( itemLabelName + ".setBackgroundColor({0});\n", itemLabel.getBackgroundColor());
			if(itemLabel.getFont() != null)
			{
				write( itemLabelName + ".setFont(new JRBaseFont());\n");
				writeFont( itemLabel.getFont(), itemLabelName + ".getFont()");
			}
			
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
			write( "JRDesignDataRange " + dataRangeName + " = new JRDesignDataRange(" + parentName + ".get" + dataRangeSuffix + "());\n");
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
			write( meterIntervalName + ".setLabel(\"{0}\");\n", JRStringUtil.escapeJavaStringLiteral(interval.getLabel()));
			write( meterIntervalName + ".setBackgroundColor({0});\n", interval.getBackgroundColor());
			write( meterIntervalName + ".setAlpha({0});\n", interval.getAlphaDouble());
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
	private void writeSeriesColors( SortedSet<JRSeriesColor> seriesColors, String parentName)
	{
		if (seriesColors == null || seriesColors.size() == 0)
		{
			return;
		}
		//FIXME why do we need an array?
		JRSeriesColor[] colors = seriesColors.toArray(new JRSeriesColor[seriesColors.size()]);
		for (int i = 0; i < colors.length; i++)
		{
			String seriesColorName = parentName + "SeriesColor" +i;
			write( "JRBaseSeriesColor " + seriesColorName + " = new JRBaseSeriesColor(" + colors[i].getSeriesOrder() +", {0});\n", colors[i].getColor());
			write( parentName + ".addSeriesColor(" + seriesColorName + ");\n");
			flush();
		}
	}

	/**
	 * Write the information about a the data and layout that make up one range axis in
	 * a multiple axis chart.
	 *
	 * @param chartAxis the axis being written
	 */
	private void writeChartAxis( JRChartAxis chartAxis, String parentName, String axisName, String chartName)
	{
		if(chartAxis != null)
		{
			// Let the nested chart describe itself
			writeChartTag( chartAxis.getChart(), axisName +"Chart");
			
			write( "JRDesignChartAxis " + axisName + " = new JRDesignChartAxis(" + axisName +"Chart);\n");
			write( axisName + ".setPosition({0});\n", chartAxis.getPositionValue());
			write( axisName + ".setChart(" + axisName +"Chart);\n");
//			write( parentName + ".setChart(" + axisName +"Chart);\n");
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
			write( plotName + ".setBackcolor({0});\n", plot.getOwnBackcolor());

			if (plot.getOrientationValue() != null && plot.getOrientationValue().getOrientation() != PlotOrientation.VERTICAL)
			{
				write( plotName + ".setOrientation(PlotOrientation.{0});\n", plot.getOrientationValue());
			}

			write( plotName + ".setBackgroundAlpha({0});\n", plot.getBackgroundAlphaFloat());
			write( plotName + ".setForegroundAlpha({0});\n", plot.getForegroundAlphaFloat());
			//write( plotName + ".setLabelRotation({0});\n", plot.getLabelRotationDouble());//FIXMECHART check the deprecation of this method; looks incomplete
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
				write( plotName + ".setShowLabels({0});\n", getBooleanText(plot.getShowLabels()));
				write( plotName + ".setCircular({0});\n", getBooleanText(plot.getCircular()));
				write( plotName + ".setLabelFormat(\"{0}\");\n", JRStringUtil.escapeJavaStringLiteral(plot.getLabelFormat()));
				write( plotName + ".setLegendLabelFormat(\"{0}\");\n", JRStringUtil.escapeJavaStringLiteral(plot.getLegendLabelFormat()));
				
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
				write( plotName + ".setShowLabels({0});\n", getBooleanText(plot.getShowLabels()));
				write( plotName + ".setCircular({0});\n", getBooleanText(plot.getCircular()));
				write( plotName + ".setLabelFormat(\"{0}\");\n", JRStringUtil.escapeJavaStringLiteral(plot.getLabelFormat()));
				write( plotName + ".setLegendLabelFormat(\"{0}\");\n", JRStringUtil.escapeJavaStringLiteral(plot.getLegendLabelFormat()));
				write( plotName + ".setDepthFactor({0});\n", plot.getDepthFactorDouble());
				
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
		{
			return;
		}
		String axisName = parentName + axisNameSuffix;

		write( "JRCategoryAxisFormat " + axisName + " = " + parentName + ";\n");
		write( axisName + ".setCategoryAxisTickLabelRotation({0});\n", labelRotation);

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
		//write( parentName + ".set" + axisNameSuffix + "(" + axisName + ");\n");
		
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
		{
			return;
		}
		String axisName = parentName + axisNameSuffix;
		if(isToSet)
		{
			write( "JRAxisFormat " + axisName + " = new JRAxisFormat();\n");
		}
		write( axisName + ".setLabelColor({0});\n", axisLabelColor);
		write( axisName + ".setTickLabelColor({0});\n", axisTickLabelColor);
		write( axisName + ".setLineColor({0});\n", axisLineColor);
		write( axisName + ".setTickLabelMask(\"{0}\");\n", JRStringUtil.escapeJavaStringLiteral(axisTickLabelMask));
		write( axisName + ".setVerticalTickLabel({0});\n", getBooleanText(axisVerticalTickLabels));

		
		if (axisLabelFont != null)
		{
			write( axisName + ".setLabelFont(new JRBaseFont());\n");
			writeFont( axisLabelFont, axisName + ".getLabelFont()");
		}

		if (axisTickLabelFont != null)
		{
			write( axisName + ".setTickLabelFont(new JRBaseFont());\n");
			writeFont( axisTickLabelFont, axisName + ".getTickLabelFont()");
		}
		if(isToSet)//FIXMEAPIWRITER check this
		{
			write( parentName + ".set" + axisNameSuffix + "(" + axisName + ");\n");
		}

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
			write( plotName + ".setShowLabels({0});\n", getBooleanText(plot.getShowLabels()));
			write( plotName + ".setShowTickLabels({0});\n", getBooleanText(plot.getShowTickLabels()));
			write( plotName + ".setShowTickMarks({0});\n", getBooleanText(plot.getShowTickMarks()));
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
			write( plotName + ".setScaleType({0});\n", plot.getScaleTypeValue());
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
			write( plotName + ".setShowLines({0});\n", getBooleanText(plot.getShowLines()));
			write( plotName + ".setShowShapes({0});\n", getBooleanText(plot.getShowShapes()));
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
			write( plotName + ".setShowLines({0});\n", getBooleanText(plot.getShowLines()));
			write( plotName + ".setShowShapes({0});\n", getBooleanText(plot.getShowShapes()));
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
			write( plotName + ".setShowLabels({0});\n", getBooleanText(plot.getShowLabels()));
			write( plotName + ".setXOffset({0});\n", plot.getXOffsetDouble());
			write( plotName + ".setYOffset({0});\n", plot.getYOffsetDouble());
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
				write( plotName + ".setShowOpenTicks({0});\n", getBooleanText(plot.getShowOpenTicks()));
				write( plotName + ".setShowCloseTicks({0});\n", getBooleanText(plot.getShowCloseTicks()));

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
				write( plotName + ".setShowVolume({0});\n", getBooleanText(plot.getShowVolume()));
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
			write( plotName + ".setShowLines({0});\n", getBooleanText(plot.getShowLines()));
			write( plotName + ".setShowShapes({0});\n", getBooleanText(plot.getShowShapes()));
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
				write( plotName + ".setShape({0});\n", plot.getShapeValue());
				write( plotName + ".setMeterAngle(Integer.valueOf({0, number, #}));\n", plot.getMeterAngleInteger());
				
				write( plotName + ".setUnits(\"{0}\");\n", JRStringUtil.escapeJavaStringLiteral(plot.getUnits()));
				write( plotName + ".setTickInterval({0});\n", plot.getTickIntervalDouble());
				write( plotName + ".setMeterBackgroundColor({0});\n", plot.getMeterBackgroundColor());
				write( plotName + ".setNeedleColor({0});\n", plot.getNeedleColor());
				write( plotName + ".setTickColor({0});\n", plot.getTickColor());
				write( plotName + ".setTickCount({0});\n", plot.getTickCount());
				
				writePlot( plot, plotName);
				if (plot.getTickLabelFont() != null)
				{
					write( plotName + ".setTickLabelFont(new JRBaseFont());\n");
					writeFont( plot.getTickLabelFont(), plotName + ".getTickLabelFont()");
					flush();
				}
				writeValueDisplay( plot.getValueDisplay(), plotName);
				writeDataRange( plot.getDataRange(), plotName, "DataRange");

				List<JRMeterInterval> intervals = plot.getIntervals();
				if (intervals != null && intervals.size() > 0)
				{
					for(int i = 0; i < intervals.size(); i++)
					{
						JRMeterInterval meterInterval = intervals.get(i);
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
				write( plotName + ".setValueLocation({0});\n", plot.getValueLocationValue());
				write( plotName + ".setMercuryColor({0});\n", plot.getMercuryColor());
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
			write( plotName + ".setChart(" + chartName + ");\n");//FIXMECHART why is this needed since we get the plot from chart?
			writePlot( chart.getPlot(), plotName);
			List<JRChartAxis> axes = plot.getAxes();
			if (axes != null && axes.size() > 0)
			{
				for (int i = 0; i < axes.size(); i++)
				{
					JRChartAxis chartAxis = axes.get(i);
					writeChartAxis( chartAxis, plotName, plotName + "Axis" + i, chartName);
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
				throw 
					new JRRuntimeException(
						JRDesignChart.EXCEPTION_MESSAGE_KEY_UNSUPPORTED_CHART_TYPE,
						(Object[])null);
		}
	}


	/**
	 * 
	 */
	private void writeSubreportReturnValue(JRSubreportReturnValue returnValue, String returnValueName)
	{
		if(returnValue != null)
		{
			write( "JRDesignSubreportReturnValue " + returnValueName + " = new JRDesignSubreportReturnValue();\n");
			write( returnValueName + ".setSubreportVariable(\"{0}\");\n", JRStringUtil.escapeJavaStringLiteral(returnValue.getFromVariable()));
			write( returnValueName + ".setToVariable(\"{0}\");\n", JRStringUtil.escapeJavaStringLiteral(returnValue.getToVariable()));
			write( returnValueName + ".setCalculation({0});\n", returnValue.getCalculation(), CalculationEnum.NOTHING);
			write( returnValueName + ".setIncrementerFactoryClassName(\"{0}\");\n", JRStringUtil.escapeJavaStringLiteral(returnValue.getIncrementerFactoryClassName()));
			flush();
		}
	}

	private void writeReturnValue(ReturnValue returnValue, String returnValueName)
	{
		if(returnValue != null)
		{
			write("DesignReturnValue " + returnValueName + " = new DesignReturnValue();\n");
			write(returnValueName + ".setFromVariable(\"{0}\");\n", 
					JRStringUtil.escapeJavaStringLiteral(returnValue.getFromVariable()));
			write(returnValueName + ".setToVariable(\"{0}\");\n", 
					JRStringUtil.escapeJavaStringLiteral(returnValue.getToVariable()));
			write(returnValueName + ".setCalculation({0});\n", 
					returnValue.getCalculation(), CalculationEnum.NOTHING);
			write(returnValueName + ".setIncrementerFactoryClassName(\"{0}\");\n", 
					JRStringUtil.escapeJavaStringLiteral(returnValue.getIncrementerFactoryClassName()));
			flush();
		}
	}

	private void writeReturnValue(ExpressionReturnValue returnValue, String returnValueName)
	{
		if(returnValue != null)
		{
			write("DesignExpressionReturnValue " + returnValueName + " = new DesignExpressionReturnValue();\n");
			writeExpression( returnValue.getExpression(), returnValueName, "Expression");
			write(returnValueName + ".setToVariable(\"{0}\");\n", 
					JRStringUtil.escapeJavaStringLiteral(returnValue.getToVariable()));
			write(returnValueName + ".setCalculation({0});\n", 
					returnValue.getCalculation(), CalculationEnum.NOTHING);
			write(returnValueName + ".setIncrementerFactoryClassName(\"{0}\");\n", 
					JRStringUtil.escapeJavaStringLiteral(returnValue.getIncrementerFactoryClassName()));
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
			write( crosstabName + ".setRepeatColumnHeaders({0});\n", crosstab.isRepeatColumnHeaders(), true);
			write( crosstabName + ".setRepeatRowHeaders({0});\n", crosstab.isRepeatRowHeaders(), true);
			write( crosstabName + ".setColumnBreakOffset({0, number, #});\n", crosstab.getColumnBreakOffset(), JRCrosstab.DEFAULT_COLUMN_BREAK_OFFSET);
			write( crosstabName + ".setRunDirection({0});\n", crosstab.getRunDirectionValue(), RunDirectionEnum.LTR);
			write( crosstabName + ".setHorizontalPosition({0});\n", crosstab.getHorizontalPosition());
			write( crosstabName + ".setIgnoreWidth({0});\n", getBooleanText(crosstab.getIgnoreWidth()));
	
			writeReportElement( crosstab, crosstabName);
			writeBox( crosstab.getLineBox(), crosstabName + ".getLineBox()");
	
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

			writeCrosstabTitleCell(crosstab, crosstabName);
			
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
				List<JRCrosstabCell> cellsList = ((JRDesignCrosstab) crosstab).getCellsList();
				for (int i = 0; i < cellsList.size(); i++)
				{
					JRCrosstabCell cell = cellsList.get(i);
					writeCrosstabCell( cell, crosstabName + "Cell" + i);
					write( crosstabName + ".addCell(" + crosstabName + "Cell" + i + ");\n");
				}
			}
			else
			{
				JRCrosstabCell[][] cells = crosstab.getCells();
				Set<JRCrosstabCell> cellsSet = new HashSet<JRCrosstabCell>();
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
	
			writeCrosstabWhenNoDataCell( crosstab, crosstabName);

			flush();
		}
	}


	protected void writeCrosstabTitleCell(JRCrosstab crosstab, String crosstabName)
	{
		CrosstabColumnCell titleCell = crosstab.getTitleCell();
		if (titleCell != null)
		{
			String titleCellName = crosstabName + "TitleCell";
			write("DesignCrosstabColumnCell {0} = new DesignCrosstabColumnCell();\n", titleCellName);
			writePattern("{0}.setHeight({1, number, #});\n", titleCellName, titleCell.getHeight());
			writePattern("{0}.setContentsPosition({1});\n", titleCellName, titleCell.getContentsPosition());//default value for educative purposes
			
			JRCellContents cellContents = titleCell.getCellContents();
			if (cellContents != null)
			{
				String contentsName = titleCellName + "Contents";
				writeCellContents(cellContents, contentsName);
				writePattern("{0}.setCellContents({1});\n", titleCellName, contentsName);
			}
			
			writePattern("{0}.setTitleCell({1});\n", crosstabName, titleCellName);
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
			write( datasetName + ".setDataPreSorted({0});\n", dataset.isDataPreSorted(), false);
			writeElementDataset( dataset, datasetName);
			write( crosstabName + ".setDataset(" + datasetName + ");\n");
			
			flush();
		}
	}


	/**
	 * 
	 */
	private void writeCrosstabWhenNoDataCell( JRCrosstab crosstab, String parentName)
	{
		JRCellContents whenNoDataCell = crosstab.getWhenNoDataCell();
		if (whenNoDataCell != null)
		{
			writeCellContents( whenNoDataCell, parentName + "NoDataCell");
			write( parentName + ".setWhenNoDataCell(" + parentName + "NoDataCell);\n");
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
			writeCellContents( headerCell, parentName + "HeaderCellContents");
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
			write( groupName + ".setName(\"{0}\");\n", JRStringUtil.escapeJavaStringLiteral(group.getName()));
			write( groupName + ".setWidth({0, number, #});\n", group.getWidth());
			write( groupName + ".setTotalPosition({0});\n", group.getTotalPositionValue(), CrosstabTotalPositionEnum.NONE);
			write( groupName + ".setPosition({0});\n", group.getPositionValue(), CrosstabRowPositionEnum.TOP);
	
			writeBucket( group.getBucket(), groupName);
	
			JRCellContents header = group.getHeader();
			if(header != null)
			{
				writeCellContents( header, groupName + "HeaderContents");
				write( groupName + ".setHeader(" + groupName + "HeaderContents);\n");
			}
			
			JRCellContents totalHeader = group.getTotalHeader();
			if(totalHeader != null)
			{
				writeCellContents( totalHeader, groupName + "TotalHeaderContents");
				write( groupName + ".setTotalHeader(" + groupName + "TotalHeaderContents);\n");
			}
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
			write( "JRDesignCrosstabColumnGroup " + groupName + " = new JRDesignCrosstabColumnGroup();\n");

			write( groupName + ".setName(\"{0}\");\n", JRStringUtil.escapeJavaStringLiteral(group.getName()));
			write( groupName + ".setHeight({0, number, #});\n", group.getHeight());
			write( groupName + ".setTotalPosition({0});\n", group.getTotalPositionValue(), CrosstabTotalPositionEnum.NONE);
			write( groupName + ".setPosition({0});\n", group.getPositionValue(), CrosstabColumnPositionEnum.LEFT);
			
			writeBucket( group.getBucket(), groupName);
			
			JRCellContents crosstabHeader = group.getCrosstabHeader();
			if(crosstabHeader != null)
			{
				writeCellContents( crosstabHeader, groupName + "CrosstabHeaderContents");
				write( groupName + ".setCrosstabHeader(" + groupName + "CrosstabHeaderContents);\n");
			}
			
			JRCellContents header = group.getHeader();
			if(header != null)
			{
				writeCellContents( header, groupName + "HeaderContents");
				write( groupName + ".setHeader(" + groupName + "HeaderContents);\n");
			}
			
			JRCellContents totalHeader = group.getTotalHeader();
			if(totalHeader != null)
			{
				writeCellContents( totalHeader, groupName + "TotalHeaderContents");
				write( groupName + ".setTotalHeader(" + groupName + "TotalHeaderContents);\n");
			}
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
			write( bucketName + ".setValueClassName(\"{0}\");\n", bucket.getValueClassName());

			writeExpression( bucket.getExpression(), bucketName, "Expression");

			writeExpression( bucket.getComparatorExpression(), bucketName, "ComparatorExpression");

			writeExpression( bucket.getOrderByExpression(), bucketName, "OrderByExpression", Object.class.getName());

			write( parentName + ".setBucket(" + bucketName + ");\n");

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
			write( measureName + ".setName(\"{0}\");\n", JRStringUtil.escapeJavaStringLiteral(measure.getName()));

			write( measureName + ".setValueClassName(\"{0}\");\n", measure.getValueClassName());
			
			write( measureName + ".setCalculation({0});\n", measure.getCalculationValue(), CalculationEnum.NOTHING);
			write( measureName + ".setPercentageType({0});\n", measure.getPercentageType(), CrosstabPercentageEnum.NONE);
			write( measureName + ".setPercentageCalculatorClassName(\"{0}\");\n", JRStringUtil.escapeJavaStringLiteral(measure.getPercentageCalculatorClassName()));

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
			write( cellName + ".setWidth({0, number, #});\n", cell.getWidth());
			write( cellName + ".setHeight({0, number, #});\n", cell.getHeight());
			write( cellName + ".setRowTotalGroup(\"{0}\");\n", JRStringUtil.escapeJavaStringLiteral(cell.getRowTotalGroup()));
			write( cellName + ".setColumnTotalGroup(\"{0}\");\n", JRStringUtil.escapeJavaStringLiteral(cell.getColumnTotalGroup()));
		
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
			write( cellName + ".setBackcolor({0});\n", contents.getBackcolor());
			write( cellName + ".setMode({0});\n", contents.getModeValue());
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
			write( parameterName + ".setDescription(\"{0}\");\n", JRStringUtil.escapeJavaStringLiteral(parameter.getDescription()));
			write( parameterName + ".setName(\"{0}\");\n", JRStringUtil.escapeJavaStringLiteral(parameter.getName()));
			
			write( parameterName + ".setValueClassName(\"{0}\");\n", parameter.getValueClassName(), "java.lang.String");
			
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
			
			write( datasetName + ".setName(\"{0}\");\n", JRStringUtil.escapeJavaStringLiteral(dataset.getName()));
			write( datasetName + ".setScriptletClass(\"{0}\");\n", JRStringUtil.escapeJavaStringLiteral(dataset.getScriptletClass()));
			write( datasetName + ".setResourceBundle(\"{0}\");\n", JRStringUtil.escapeJavaStringLiteral(dataset.getResourceBundle()));
			write( datasetName + ".setWhenResourceMissingType({0});\n", dataset.getWhenResourceMissingTypeValue(), WhenResourceMissingTypeEnum.NULL);
	
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
			write( datasetName +".setQuery(" + datasetName + "Query);\n");
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
	 */
	public void writeDatasetRun( JRDatasetRun datasetRun, String parentName)
	{
		if(datasetRun != null)
		{
			String runName = parentName + "Run";
			write( "JRDesignDatasetRun " + runName + " = new JRDesignDatasetRun();\n");
			write( runName + ".setDatasetName(\"{0}\");\n", JRStringUtil.escapeJavaStringLiteral(datasetRun.getDatasetName()));
			writeExpression( datasetRun.getParametersMapExpression(), runName, "ParametersMapExpression");
	
			JRDatasetParameter[] parameters = datasetRun.getParameters();
			if (parameters != null && parameters.length > 0)
			{
				for(int i = 0; i < parameters.length; i++)
				{
					writeDatasetParameter( parameters[i], runName, runName + "Parameter" + i);
				}
			}
	
			writeExpression( datasetRun.getConnectionExpression(), runName, "ConnectionExpression");

			writeExpression( datasetRun.getDataSourceExpression(), runName, "DataSourceExpression");
			
			List<ReturnValue> returnValues = datasetRun.getReturnValues();
			if (returnValues != null && !returnValues.isEmpty())
			{
				for (ListIterator<ReturnValue> it = returnValues.listIterator(); it.hasNext();)
				{
					ReturnValue returnValue = it.next();
					String returnValueVarName = runName + "ReturnValue" + it.previousIndex();
					writeReturnValue(returnValue, returnValueVarName);
					write(runName + ".addReturnValue(" + returnValueVarName + ");\n");
				}
			}
			
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
			write( "JRDesignFrame " + frameName + " = new JRDesignFrame(jasperDesign);\n");
			write(frameName + ".setBorderSplitType({0});\n", frame.getBorderSplitType());
			
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
			write( parameterName + ".setName(\"{0}\");\n", JRStringUtil.escapeJavaStringLiteral(parameter.getName()));
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
			writer.writeExpression(JRApiConstants.ELEMENT_hyperlinkWhenExpression, JASPERREPORTS_NAMESPACE,
					hyperlink.getHyperlinkWhenExpression(), false);
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

			if(hyperlink.getLinkType() != null)
			{
				write( hyperlinkName + ".setLinkType(\"{0}\");\n", JRStringUtil.escapeJavaStringLiteral(hyperlink.getLinkType()), HyperlinkTypeEnum.NONE.getName());
			}
			if(hyperlink.getLinkTarget() != null)
			{
				write( hyperlinkName + ".setLinkTarget(\"{0}\");\n", JRStringUtil.escapeJavaStringLiteral(hyperlink.getLinkTarget()), HyperlinkTargetEnum.SELF.getName());
			}

			writeExpression( hyperlink.getHyperlinkReferenceExpression(), hyperlinkName, "HyperlinkReferenceExpression");
			writeExpression( hyperlink.getHyperlinkWhenExpression(), hyperlinkName, "HyperlinkWhenExpression");
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
			write( "JRDesignConditionalStyle " + styleName + " = new JRDesignConditionalStyle();\n");
			writeExpression( style.getConditionExpression(), styleName, "ConditionExpression");

			if (style.getStyle() != null)//FIXME double check which one to use; style or styleNameReference?
			{
				write( styleName + ".setParentStyle({0});\n", JRStringUtil.escapeJavaStringLiteral(style.getStyle().getName()));
			}
			else if (style.getStyleNameReference() != null)
			{
				write( styleName + ".setParentStyleNameReference(\"{0}\");\n", JRStringUtil.escapeJavaStringLiteral(style.getStyleNameReference()));
			}

			writeCommonStyle(style, styleName);
			
			flush();
		}
	}

//	/**
//	 * Returns the XML write helper used by this report writer.
//	 * 
//	 * The helper can be used to output XML elements and attributes.
//	 * 
//	 * @return the XML write helper used by this report writer
//	 */
//	public JRXmlWriteHelper getXmlWriteHelper()
//	{
//		return writer;
//	}
//
//	/**
//	 * Returns the underlying stream to which this writer outputs to.
//	 * 
//	 * @return the underlying stream used by this writer
//	 */
//	public Writer getUnderlyingWriter()
//	{
//		return writer.getUnderlyingWriter();
//	}

	/**
	 * 
	 */
	public void writeComponentElement( JRComponentElement componentElement, String componentName)
	{
		write( "JRDesignComponentElement " + componentName + " = new JRDesignComponentElement(jasperDesign);\n");
		writeReportElement( componentElement, componentName);
		
//		ComponentKey componentKey = componentElement.getComponentKey();
//		Component component = componentElement.getComponent();
		//TODO: component specific API writer
//		ComponentXmlWriter componentXmlWriter = ComponentsEnvironment.
//			getComponentManager(componentKey).getComponentXmlWriter();
//		componentXmlWriter.writeToXml(componentKey, component, this);
		
		flush();
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
		
		write( elementName + ".setEvaluationTime({0});\n", element.getEvaluationTimeValue(), EvaluationTimeEnum.NOW);
		write( elementName + ".setEvaluationGroup(\"{0}\");\n", JRStringUtil.escapeJavaStringLiteral(element.getEvaluationGroupName()));

		writeReportElement( element, elementName);
		
		JRGenericElementType printKey = element.getGenericType();
//		JRGenericElementType t = new JRGenericElementType(printKey.getNamespace(), printKey.getName());

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

			String paramName =  elementName + "Parameter" + i;
			write( "JRDesignGenericElementParameter " + paramName + " = new JRDesignGenericElementParameter();\n");
			write( paramName + ".setName(\"{0}\");\n", JRStringUtil.escapeJavaStringLiteral(param.getName()));
			write( paramName + ".setSkipWhenEmpty({0});\n", param.isSkipWhenEmpty(), false);
			writeExpression(param.getValueExpression(), paramName, "ValueExpression");

			write( elementName + ".addParameter({0});\n", paramName);
			
			flush();//genericElementParameter
		}
		
		flush();//genericElement
	}

	protected void writeStyleReferenceAttr( JRStyleContainer styleContainer, String styleName)
	{
		if (styleContainer.getStyle() != null)
		{
			write( styleName + ".setStyle(" + JRStringUtil.getJavaIdentifier(styleContainer.getStyle().getName()) + ");\n");
		}
		else if (styleContainer.getStyleNameReference() != null)
		{
			write( styleName + ".setStyleNameReference(\"{0}\");\n", styleContainer.getStyleNameReference());
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
			write( penHolder + ".setLineWidth({0});\n", pen.getOwnLineWidth());
			write( penHolder + ".setLineStyle({0});\n", pen.getOwnLineStyleValue());
			write( penHolder + ".setLineColor({0});\n", pen.getOwnLineColor());
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
			write( boxHolder + ".setPadding(Integer.valueOf({0, number, #}));\n", box.getOwnPadding());
			write( boxHolder + ".setTopPadding(Integer.valueOf({0, number, #}));\n", box.getOwnTopPadding());
			write( boxHolder + ".setLeftPadding(Integer.valueOf({0, number, #}));\n", box.getOwnLeftPadding());
			write( boxHolder + ".setBottomPadding(Integer.valueOf({0, number, #}));\n", box.getOwnBottomPadding());
			write( boxHolder + ".setRightPadding(Integer.valueOf({0, number, #}));\n", box.getOwnRightPadding());

			writePen( box.getPen(), boxHolder + ".getPen()");
			writePen( box.getTopPen(), boxHolder + ".getTopPen()");
			writePen( box.getLeftPen(), boxHolder + ".getLeftPen()");
			writePen( box.getBottomPen(), boxHolder + ".getBottomPen()");
			writePen( box.getRightPen(), boxHolder + ".getRightPen()");

			flush();
		}
	}

	/**
	 *
	 */
	protected void writeParagraph(JRParagraph paragraph, String paragraphHolder)
	{
		if (paragraph != null)
		{
			String paragraphName = paragraphHolder + "Paragraph";
			write( "JRParagraph " + paragraphName + " = " + paragraphHolder + ".getParagraph();\n");
			write( paragraphName + ".setLineSpacing({0});\n", paragraph.getOwnLineSpacing());
			write( paragraphName + ".setLineSpacingSize({0});\n", paragraph.getOwnLineSpacingSize());
			write( paragraphName + ".setFirstLineIndent({0});\n", paragraph.getOwnFirstLineIndent());
			write( paragraphName + ".setLeftIndent({0});\n", paragraph.getOwnLeftIndent());
			write( paragraphName + ".setRightIndent({0});\n", paragraph.getOwnRightIndent());
			write( paragraphName + ".setSpacingBefore({0});\n", paragraph.getOwnSpacingBefore());
			write( paragraphName + ".setSpacingAfter({0});\n", paragraph.getOwnSpacingAfter());
			write( paragraphName + ".setTabStopWidth(Integer.valueOf({0, number, #}));\n", paragraph.getOwnTabStopWidth());//FIXMENOW is this pattern needed?

			TabStop[] tabStops = paragraph.getTabStops();
			if (tabStops != null && tabStops.length > 0)
			{
				for(int i = 0; i < tabStops.length; i++)
				{
					writeTabStop( tabStops[i], paragraphName + "TabStop" + i);
					write( paragraphName +".addTabStop(" + paragraphName + "TabStop" + i + ");\n");
				}
			}

			flush();
		}
	}

	
	/**
	 *
	 */
	private void writeTabStop(TabStop tabStop, String tabStopName)
	{
		if (tabStop != null)
		{
			write( "TabStop " + tabStopName + " = new TabStop();\n");
			write( tabStopName + ".setAlignment({0});\n", tabStop.getAlignment(), TabStopAlignEnum.LEFT);
			write( tabStopName + ".setPosition({0});\n", tabStop.getPosition());
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
			write( expressionName + ".setId({0, number, #});\n", expression.getId());
			write( expressionName + ".setText(\"{0}\");\n", JRStringUtil.escapeJavaStringLiteral(expression.getText()));

			write( parentName + ".set" + expressionSuffix + "(" + expressionName + ");\n");
			
			flush();
		}
	}

	private String getGroupName(JRGroup group)
	{
		if(group != null)
		{
			if(groupsMap.get(group.getName()) == null)
			{
				writeGroup( group);
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
		write(pattern, value, null);
	}

	
	/**
	 *
	 */
	protected void write(String pattern, Object value, Object defaultValue)
	{
		if (value != null && value != defaultValue)
		{
			write(MessageFormat.format(pattern, new Object[]{value}));
		}
	}

	
	/**
	 *
	 */
	protected void write(String pattern, Enum<?> value)
	{
		write(pattern, value, null);
	}

	
	/**
	 *
	 */
	protected void write(String pattern, Enum<?> value, Enum<?> defaultValue)
	{
		if (value != null && value != defaultValue)
		{
			write(MessageFormat.format(pattern, new Object[]{value.getClass().getName() + "." + value.name()}));
		}
	}

	
	/**
	 *
	 */
	protected void write(String pattern, int value)
	{
		write(MessageFormat.format(pattern, new Object[]{Integer.valueOf(value)}));
	}

	
	/**
	 *
	 */
	protected void write(String pattern, int value, int defaultValue)
	{
		if (value != defaultValue)
		{
			write(MessageFormat.format(pattern, new Object[]{Integer.valueOf(value)}));
		}
	}

	
	/**
	 *
	 */
	protected void write(String pattern, Float value)
	{
		write(pattern, value, null);
	}

	
	/**
	 *
	 */
	protected void write(String pattern, Float value, Float defaultValue)
	{
		if (value != null && value != defaultValue)
		{
			String strFloat = 
				MessageFormat.format(
					"new Float({0})", 
					new Object[]{NumberFormat.getInstance(Locale.ENGLISH).format(value).replaceAll(",", "")}
					);
			write(MessageFormat.format(pattern, new Object[]{strFloat}));
		}
	}

	
	/**
	 *
	 */
	protected void write(String pattern, Double value)
	{
		write(pattern, value, null);
	}

	
	/**
	 *
	 */
	protected void write(String pattern, Double value, Double defaultValue)
	{
		if (value != null && value != defaultValue)
		{
			String strDouble = 
				MessageFormat.format(
					"new Double({0})", 
					new Object[]{NumberFormat.getInstance(Locale.ENGLISH).format(value).replaceAll(",", "")}
					);
			write(MessageFormat.format(pattern, new Object[]{strDouble}));
		}
	}

	
	/**
	 *
	 */
	protected void write(String pattern, boolean value, boolean defaultValue)
	{
		if (value != defaultValue)
		{
			write(MessageFormat.format(pattern, new Object[]{value ? Boolean.TRUE : Boolean.FALSE}));
		}
	}

	
	/**
	 *
	 */
	protected void write(String pattern, byte value, byte defaultValue)
	{
		if (value != defaultValue)
		{
			write(MessageFormat.format(pattern, new Object[]{new Byte(value)}));
		}
	}

	
	/**
	 *
	 */
	protected void write(String pattern, Color value)
	{
		write(pattern, value, null);
	}

	
	/**
	 *
	 */
	protected void write(String pattern, Color value, Color defaultValue)
	{
		if (value != null && value != defaultValue)
		{
			String strColor = 
				MessageFormat.format(
					"new Color({0}, {1}, {2})", 
					new Object[]{value.getRed(), value.getGreen(), value.getBlue()}
					);
			write(MessageFormat.format(pattern, new Object[]{strColor}));
		}
	}
	
	protected void writePattern(String pattern, Object ... arguments)
	{
		for (int i = 0; i < arguments.length; i++)
		{
			if (arguments[i] != null && arguments[i].getClass().isEnum())
			{
				arguments[i] = arguments[i].getClass().getCanonicalName() + "." + ((Enum<?>) arguments[i]).name();
			}
		}
		
		write(MessageFormat.format(pattern, arguments));
	}

	
	private String getBooleanText(Boolean key)
	{
		return key == null 
			? null 
			: (key.booleanValue() ? "Boolean.TRUE" : "Boolean.FALSE");
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
			Class<?> reportCreatorClass = Class.forName(reportCreatorClassName);
			ReportCreator reportCreator = (ReportCreator)reportCreatorClass.newInstance();
			JasperDesign jasperDesign = reportCreator.create();
			new JRXmlWriter(DefaultJasperReportsContext.getInstance()).write(jasperDesign, destFileName, "UTF-8");
		}
		catch (Exception e)
		{
			if (log.isErrorEnabled())
			{
				log.error("Error running report creator class : " + reportCreatorClassName, e);
			}
		}
	}

	
}


class JRApiWriterVisitor implements JRVisitor
{
	
	private JRApiWriter apiWriter;
	private String name;
	
	/**
	 *
	 */
	public JRApiWriterVisitor(JRApiWriter apiWriter)
	{
		this.apiWriter = apiWriter;
	}

	/**
	 *
	 */
	public void visitBreak(JRBreak breakElement)
	{
		apiWriter.writeBreak(breakElement, name);
	}

	/**
	 *
	 */
	public void visitChart(JRChart chart)
	{
		apiWriter.writeChartTag(chart, name);
	}

	/**
	 *
	 */
	public void visitCrosstab(JRCrosstab crosstab)
	{
		apiWriter.writeCrosstab(crosstab, name);
	}

	/**
	 *
	 */
	public void visitElementGroup(JRElementGroup elementGroup)
	{
		apiWriter.writeElementGroup(elementGroup, name);
	}

	/**
	 *
	 */
	public void visitEllipse(JREllipse ellipse)
	{
		apiWriter.writeEllipse(ellipse, name);
	}

	/**
	 *
	 */
	public void visitFrame(JRFrame frame)
	{
		apiWriter.writeFrame(frame, name);
	}

	/**
	 *
	 */
	public void visitImage(JRImage image)
	{
		apiWriter.writeImage(image, name);
	}

	/**
	 *
	 */
	public void visitLine(JRLine line)
	{
		apiWriter.writeLine(line, name);
	}

	/**
	 *
	 */
	public void visitRectangle(JRRectangle rectangle)
	{
		apiWriter.writeRectangle(rectangle, name);
	}

	/**
	 *
	 */
	public void visitStaticText(JRStaticText staticText)
	{
		apiWriter.writeStaticText(staticText, name);
	}

	/**
	 *
	 */
	public void visitSubreport(JRSubreport subreport)
	{
		apiWriter.writeSubreport(subreport, name);
	}

	/**
	 *
	 */
	public void visitTextField(JRTextField textField)
	{
		apiWriter.writeTextField(textField, name);
	}

	/**
	 *
	 */
	public void visitComponentElement(JRComponentElement componentElement)
	{
		apiWriter.writeComponentElement(componentElement, name);
	}

	/**
	 *
	 */
	public void visitGenericElement(JRGenericElement element)
	{
		apiWriter.writeGenericElement(element, name);
	}

	/**
	 * @return the name
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name)
	{
		this.name = name;
	}

}
