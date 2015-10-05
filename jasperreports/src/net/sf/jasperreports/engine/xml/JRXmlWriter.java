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
package net.sf.jasperreports.engine.xml;

import java.awt.Color;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.regex.Pattern;

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
import net.sf.jasperreports.charts.type.PlotOrientationEnum;
import net.sf.jasperreports.charts.type.TimePeriodEnum;
import net.sf.jasperreports.charts.util.JRMeterInterval;
import net.sf.jasperreports.charts.xml.JRChartAxisFactory;
import net.sf.jasperreports.charts.xml.JRMeterPlotFactory;
import net.sf.jasperreports.charts.xml.JRThermometerPlotFactory;
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
import net.sf.jasperreports.engine.JRConstants;
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
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JRPart;
import net.sf.jasperreports.engine.JRPropertiesHolder;
import net.sf.jasperreports.engine.JRPropertiesMap;
import net.sf.jasperreports.engine.JRPropertiesUtil;
import net.sf.jasperreports.engine.JRPropertiesUtil.PropertySuffix;
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
import net.sf.jasperreports.engine.JRSubreport;
import net.sf.jasperreports.engine.JRSubreportParameter;
import net.sf.jasperreports.engine.JRSubreportReturnValue;
import net.sf.jasperreports.engine.JRTextElement;
import net.sf.jasperreports.engine.JRTextField;
import net.sf.jasperreports.engine.JRVariable;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.ReturnValue;
import net.sf.jasperreports.engine.VariableReturnValue;
import net.sf.jasperreports.engine.analytics.dataset.BucketOrder;
import net.sf.jasperreports.engine.analytics.dataset.DataAxis;
import net.sf.jasperreports.engine.analytics.dataset.DataAxisLevel;
import net.sf.jasperreports.engine.analytics.dataset.DataLevelBucket;
import net.sf.jasperreports.engine.analytics.dataset.DataLevelBucketProperty;
import net.sf.jasperreports.engine.analytics.dataset.DataMeasure;
import net.sf.jasperreports.engine.analytics.dataset.MultiAxisData;
import net.sf.jasperreports.engine.component.ComponentKey;
import net.sf.jasperreports.engine.component.ComponentXmlWriter;
import net.sf.jasperreports.engine.component.ComponentsEnvironment;
import net.sf.jasperreports.engine.part.PartComponentXmlWriter;
import net.sf.jasperreports.engine.part.PartComponentsEnvironment;
import net.sf.jasperreports.engine.part.PartEvaluationTime;
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
import net.sf.jasperreports.engine.type.PositionTypeEnum;
import net.sf.jasperreports.engine.type.PrintOrderEnum;
import net.sf.jasperreports.engine.type.ResetTypeEnum;
import net.sf.jasperreports.engine.type.RunDirectionEnum;
import net.sf.jasperreports.engine.type.SectionTypeEnum;
import net.sf.jasperreports.engine.type.SortFieldTypeEnum;
import net.sf.jasperreports.engine.type.SortOrderEnum;
import net.sf.jasperreports.engine.type.SplitTypeEnum;
import net.sf.jasperreports.engine.type.StretchTypeEnum;
import net.sf.jasperreports.engine.type.WhenNoDataTypeEnum;
import net.sf.jasperreports.engine.type.WhenResourceMissingTypeEnum;
import net.sf.jasperreports.engine.util.JRXmlWriteHelper;
import net.sf.jasperreports.engine.util.XmlNamespace;


/**
 * A writer that produces the JRXML representation of an in-memory report.
 * <p>
 * Sometimes report designs are generated automatically using the JasperReports 
 * API. Report design objects obtained this way can be serialized for disk storage or 
 * transferred over the network, but they also can be stored in JRXML format.
 * </p><p>
 * The JRXML representation of a given report design object can be obtained by using one 
 * of the <code>public static writeReport()</code> methods exposed by this class.
 * </p>
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @author Minor enhancements by Barry Klawans (bklawans@users.sourceforge.net)
 */
public class JRXmlWriter extends JRXmlBaseWriter
{

	public static final XmlNamespace JASPERREPORTS_NAMESPACE = 
		new XmlNamespace(JRXmlConstants.JASPERREPORTS_NAMESPACE, null, JRXmlConstants.JASPERREPORT_XSD_SYSTEM_ID);
	public static final String EXCEPTION_MESSAGE_KEY_FILE_WRITE_ERROR = "xml.writer.file.write.error";
	public static final String EXCEPTION_MESSAGE_KEY_OUTPUT_STREAM_WRITE_ERROR = "xml.writer.output.stream.write.error";
	public static final String EXCEPTION_MESSAGE_KEY_REPORT_DESIGN_WRITE_ERROR = "xml.writer.report.design.write.error";
	public static final String EXCEPTION_MESSAGE_KEY_UNSUPPORTED_CHART_TYPE = "xml.writer.unsupported.chart.type";
	
	//FIXME doc
	public static final String PREFIX_EXCLUDE_PROPERTIES = 
			JRPropertiesUtil.PROPERTY_PREFIX + "jrxml.writer.exclude.properties.";

	/**
	 *
	 */
	private JasperReportsContext jasperReportsContext;
	
	private List<Pattern> excludePropertiesPattern;

	/**
	 *
	 */
	private JRReport report;
	/**
	 * @deprecated To be removed.
	 */
	private String encoding;

	private XmlWriterVisitor xmlWriterVisitor = new XmlWriterVisitor(this);


	/**
	 *
	 */
	public JRXmlWriter(JasperReportsContext jasperReportsContext)
	{
		this.jasperReportsContext = jasperReportsContext;
		
		initExcludeProperties();
	}


	/**
	 * @deprecated To be removed.
	 */
	protected JRXmlWriter(JRReport report, String encoding)
	{
		this.report = report;
		this.encoding = encoding;
		
		initExcludeProperties();
	}


	private void initExcludeProperties()
	{
		JasperReportsContext context = jasperReportsContext == null 
				? DefaultJasperReportsContext.getInstance() : jasperReportsContext;
		List<PropertySuffix> excludeProperties = JRPropertiesUtil.getInstance(context).getProperties(
				PREFIX_EXCLUDE_PROPERTIES);
		
		excludePropertiesPattern = new ArrayList<Pattern>(excludeProperties.size());
		for (PropertySuffix propertySuffix : excludeProperties)
		{
			String regex = propertySuffix.getValue();
			Pattern pattern = Pattern.compile(regex);
			excludePropertiesPattern.add(pattern);
		}
	}


	/**
	 *
	 */
	public JRReport getReport()
	{
		return report;
	}


	/**
	 *
	 */
	public String write(JRReport report, String encoding)
	{
		StringWriter buffer = new StringWriter();
		try
		{
			writeReport(report, encoding, buffer);
		}
		catch (IOException e)
		{
			// doesn't actually happen
			throw 
				new JRRuntimeException(
					EXCEPTION_MESSAGE_KEY_REPORT_DESIGN_WRITE_ERROR,
					(Object[])null,
					e);
		}
		return buffer.toString();
	}


	/**
	 *
	 */
	public void write(
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
			writeReport(report, encoding, out);
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
		OutputStream outputStream,
		String encoding
		) throws JRException
	{
		try
		{
			Writer out = new OutputStreamWriter(outputStream, encoding);
			writeReport(report, encoding, out);
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
	 * @see #write(JRReport, String)
	 */
	public static String writeReport(JRReport report, String encoding)
	{
		return new JRXmlWriter(DefaultJasperReportsContext.getInstance()).write(report, encoding);
	}


	/**
	 * @see #write(JRReport, String, String)
	 */
	public static void writeReport(
		JRReport report,
		String destFileName,
		String encoding
		) throws JRException
	{
		new JRXmlWriter(DefaultJasperReportsContext.getInstance()).write(report, destFileName, encoding);
	}


	/**
	 * @see #write(JRReport, OutputStream, String)
	 */
	public static void writeReport(
		JRReport report,
		OutputStream outputStream,
		String encoding
		) throws JRException
	{
			new JRXmlWriter(DefaultJasperReportsContext.getInstance()).write(report, outputStream, encoding);
	}


	/**
	 *
	 */
	protected void writeReport(JRReport report, String encoding, Writer out) throws IOException
	{
		this.report = report;
		
		String version = JRPropertiesUtil.getInstance(jasperReportsContext).getProperty(report, JRXmlBaseWriter.PROPERTY_REPORT_VERSION);
		useWriter(new JRXmlWriteHelper(out), version);

		writer.writeProlog(encoding);

		writer.startElement(JRXmlConstants.ELEMENT_jasperReport, getNamespace());
		writer.addEncodedAttribute(JRXmlConstants.ATTRIBUTE_name, report.getName());
		writer.addEncodedAttribute(JRXmlConstants.ATTRIBUTE_language, report.getLanguage(), JRReport.LANGUAGE_JAVA);
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_columnCount, report.getColumnCount(), 1);
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_printOrder, report.getPrintOrderValue(), PrintOrderEnum.VERTICAL);
		if(isNewerVersionOrEqual(JRConstants.VERSION_3_7_5))
		{
			writer.addAttribute(JRXmlConstants.ATTRIBUTE_columnDirection, report.getColumnDirection(), RunDirectionEnum.LTR);
		}
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_pageWidth, report.getPageWidth());
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_pageHeight, report.getPageHeight());
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_orientation, report.getOrientationValue(), OrientationEnum.PORTRAIT);
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_whenNoDataType, report.getWhenNoDataTypeValue(), WhenNoDataTypeEnum.NO_PAGES);
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_sectionType, report.getSectionType(), SectionTypeEnum.BAND);
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_columnWidth, report.getColumnWidth());
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_columnSpacing, report.getColumnSpacing(), 0);
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_leftMargin, report.getLeftMargin());
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_rightMargin, report.getRightMargin());
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_topMargin, report.getTopMargin());
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_bottomMargin, report.getBottomMargin());
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_isTitleNewPage, report.isTitleNewPage(), false);
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_isSummaryNewPage, report.isSummaryNewPage(), false);
		if(isNewerVersionOrEqual(JRConstants.VERSION_3_6_1))
		{
			writer.addAttribute(JRXmlConstants.ATTRIBUTE_isSummaryWithPageHeaderAndFooter, report.isSummaryWithPageHeaderAndFooter(), false);
		}
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_isFloatColumnFooter, report.isFloatColumnFooter(), false);
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_scriptletClass, report.getScriptletClass());
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_formatFactoryClass, report.getFormatFactoryClass());
		writer.addEncodedAttribute(JRXmlConstants.ATTRIBUTE_resourceBundle, report.getResourceBundle());
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_whenResourceMissingType, report.getWhenResourceMissingTypeValue(), WhenResourceMissingTypeEnum.NULL);
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_isIgnorePagination, report.isIgnorePagination(), false);
		if(isNewerVersionOrEqual(JRConstants.VERSION_4_6_0))
		{
			writer.addAttribute(JRXmlConstants.ATTRIBUTE_uuid, report.getUUID().toString());
		}

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
					writer.startElement(JRXmlConstants.ELEMENT_import);
					writer.addEncodedAttribute(JRXmlConstants.ATTRIBUTE_value, value);
					writer.closeElement();
				}
			}
		}

		writeTemplates(report);

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
			writer.startElement(JRXmlConstants.ELEMENT_background);
			writeBand(report.getBackground());
			writer.closeElement();
		}

		if (report.getTitle() != null)
		{
			writer.startElement(JRXmlConstants.ELEMENT_title);
			writeBand(report.getTitle());
			writer.closeElement();
		}

		if (report.getPageHeader() != null)
		{
			writer.startElement(JRXmlConstants.ELEMENT_pageHeader);
			writeBand(report.getPageHeader());
			writer.closeElement();
		}

		if (report.getColumnHeader() != null)
		{
			writer.startElement(JRXmlConstants.ELEMENT_columnHeader);
			writeBand(report.getColumnHeader());
			writer.closeElement();
		}

		JRSection detail = report.getDetailSection();
		if (detail != null)
		{
			writer.startElement(JRXmlConstants.ELEMENT_detail);
			writeSection(detail);
			writer.closeElement(true);
		}

		if (report.getColumnFooter() != null)
		{
			writer.startElement(JRXmlConstants.ELEMENT_columnFooter);
			writeBand(report.getColumnFooter());
			writer.closeElement();
		}

		if (report.getPageFooter() != null)
		{
			writer.startElement(JRXmlConstants.ELEMENT_pageFooter);
			writeBand(report.getPageFooter());
			writer.closeElement();
		}

		if (report.getLastPageFooter() != null)
		{
			writer.startElement(JRXmlConstants.ELEMENT_lastPageFooter);
			writeBand(report.getLastPageFooter());
			writer.closeElement();
		}

		if (report.getSummary() != null)
		{
			writer.startElement(JRXmlConstants.ELEMENT_summary);
			writeBand(report.getSummary());
			writer.closeElement();
		}

		if (report.getNoData() != null)
		{
			writer.startElement(JRXmlConstants.ELEMENT_noData);
			writeBand(report.getNoData());
			writer.closeElement();
		}

		writer.closeElement();

		out.flush();
	}


	/**
	 * @deprecated Replaced by {@link #writeReport(JRReport, String, Writer)}.
	 */
	protected void writeReport(Writer out) throws IOException
	{
		writeReport(report, encoding, out);
	}


	public void writeProperties(JRPropertiesHolder propertiesHolder) throws IOException
	{
		if (propertiesHolder.hasProperties())
		{
			JRPropertiesMap propertiesMap = propertiesHolder.getPropertiesMap();
			String[] propertyNames = propertiesMap.getPropertyNames();
			if (propertyNames != null && propertyNames.length > 0)
			{
				for(int i = 0; i < propertyNames.length; i++)
				{
					String propertyName = propertyNames[i];
					if (isPropertyToWrite(propertiesHolder, propertyName))
					{
						writer.startElement(JRXmlConstants.ELEMENT_property, getNamespace());
						writer.addEncodedAttribute(JRXmlConstants.ATTRIBUTE_name, propertyName);
						String value = propertiesMap.getProperty(propertyName);
						if (value != null)
						{
							writer.addEncodedAttribute(JRXmlConstants.ATTRIBUTE_value, value);
						}
						writer.closeElement();
					}
				}
			}
		}
	}
	
	protected boolean isPropertyToWrite(JRPropertiesHolder propertiesHolder, String propertyName)
	{
		// currently the properties holder does not matter, we just look at the property name
		boolean toWrite = true;
		for (Pattern pattern : excludePropertiesPattern)
		{
			if (pattern.matcher(propertyName).matches())
			{
				// excluding
				toWrite = false;
				break;
			}
		}
		return toWrite;
	}


	protected void writeTemplates(JRReport report) throws IOException
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


	/**
	 * @deprecated Replaced by {@link #writeTemplates(JRReport)}.
	 */
	protected void writeTemplates() throws IOException
	{
		writeTemplates(report);
	}


	/**
	 * 
	 * @param template
	 * @throws IOException
	 */
	protected void writeTemplate(JRReportTemplate template) throws IOException
	{
		writeExpression(
				JRXmlConstants.ELEMENT_template, 
				template.getSourceExpression(),
				true, 
				String.class.getName());
	}


	/**
	 *
	 */
	private void writeScriptlet(JRScriptlet scriptlet) throws IOException
	{
		if(isNewerVersionOrEqual(JRConstants.VERSION_3_1_4))
		{
			writer.startElement(JRXmlConstants.ELEMENT_scriptlet);
			writer.addEncodedAttribute(JRXmlConstants.ATTRIBUTE_name, scriptlet.getName());
			writer.addAttribute(JRXmlConstants.ATTRIBUTE_class, scriptlet.getValueClassName());
	
			writeProperties(scriptlet);
	
			writer.writeCDATAElement(JRXmlConstants.ELEMENT_scriptletDescription, scriptlet.getDescription());
	
			writer.closeElement();
		}
	}


	/**
	 *
	 */
	private void writeParameter(JRParameter parameter) throws IOException
	{
		writer.startElement(JRXmlConstants.ELEMENT_parameter);
		writer.addEncodedAttribute(JRXmlConstants.ATTRIBUTE_name, parameter.getName());
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_class, parameter.getValueClassName());
		if(isNewerVersionOrEqual(JRConstants.VERSION_3_1_4))
		{
			writer.addAttribute(JRXmlConstants.ATTRIBUTE_nestedType, parameter.getNestedTypeName());
		}
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_isForPrompting, parameter.isForPrompting(), true);

		writeProperties(parameter);

		writer.writeCDATAElement(JRXmlConstants.ELEMENT_parameterDescription, parameter.getDescription());
		writeExpression(JRXmlConstants.ELEMENT_defaultValueExpression, parameter.getDefaultValueExpression(), false);

		writer.closeElement();
	}


	/**
	 *
	 */
	private void writeQuery(JRQuery query) throws IOException
	{
		writer.startElement(JRXmlConstants.ELEMENT_queryString);
		writer.addEncodedAttribute(JRXmlConstants.ATTRIBUTE_language, query.getLanguage(), JRJdbcQueryExecuterFactory.QUERY_LANGUAGE_SQL);
		writer.writeCDATA(query.getText());
		writer.closeElement();
	}


	/**
	 *
	 */
	private void writeField(JRField field) throws IOException
	{
		writer.startElement(JRXmlConstants.ELEMENT_field);
		writer.addEncodedAttribute(JRXmlConstants.ATTRIBUTE_name, field.getName());
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_class, field.getValueClassName());

		writeProperties(field);

		writer.writeCDATAElement(JRXmlConstants.ELEMENT_fieldDescription, field.getDescription());

		writer.closeElement();
	}


	/**
	 *
	 */
	private void writeSortField(JRSortField sortField) throws IOException
	{
		writer.startElement(JRXmlConstants.ELEMENT_sortField);
		writer.addEncodedAttribute(JRXmlConstants.ATTRIBUTE_name, sortField.getName());
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_order, sortField.getOrderValue(), SortOrderEnum.ASCENDING);
		if(isNewerVersionOrEqual(JRConstants.VERSION_3_7_5))
		{
			writer.addAttribute(JRXmlConstants.ATTRIBUTE_type, sortField.getType(), SortFieldTypeEnum.FIELD);
		}
		writer.closeElement();
	}


	/**
	 *
	 */
	private void writeVariable(JRVariable variable) throws IOException
	{
		writer.startElement(JRXmlConstants.ELEMENT_variable);
		writer.addEncodedAttribute(JRXmlConstants.ATTRIBUTE_name, variable.getName());
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_class, variable.getValueClassName());
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_resetType, variable.getResetTypeValue(), ResetTypeEnum.REPORT);
		if (variable.getResetGroup() != null)
		{
			writer.addEncodedAttribute(JRXmlConstants.ATTRIBUTE_resetGroup, variable.getResetGroup().getName());
		}
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_incrementType, variable.getIncrementTypeValue(), IncrementTypeEnum.NONE);
		if (variable.getIncrementGroup() != null)
		{
			writer.addEncodedAttribute(JRXmlConstants.ATTRIBUTE_incrementGroup, variable.getIncrementGroup().getName());
		}
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_calculation, variable.getCalculationValue(), CalculationEnum.NOTHING);
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_incrementerFactoryClass, variable.getIncrementerFactoryClassName());

		writeExpression(JRXmlConstants.ELEMENT_variableExpression, variable.getExpression(), false);
		writeExpression(JRXmlConstants.ELEMENT_initialValueExpression, variable.getInitialValueExpression(), false);

		writer.closeElement();
	}


	/**
	 *
	 */
	private void writeGroup(JRGroup group) throws IOException
	{
		writer.startElement(JRXmlConstants.ELEMENT_group);
		writer.addEncodedAttribute(JRXmlConstants.ATTRIBUTE_name, group.getName());
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_isStartNewColumn, group.isStartNewColumn(), false);
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_isStartNewPage, group.isStartNewPage(), false);
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_isResetPageNumber, group.isResetPageNumber(), false);
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_isReprintHeaderOnEachPage, group.isReprintHeaderOnEachPage(), false);
		writer.addAttributePositive(JRXmlConstants.ATTRIBUTE_minHeightToStartNewPage, group.getMinHeightToStartNewPage());
		if(isNewerVersionOrEqual(JRConstants.VERSION_3_6_2))
		{
			writer.addAttribute(JRXmlConstants.ATTRIBUTE_footerPosition, group.getFooterPositionValue(), FooterPositionEnum.NORMAL);
			writer.addAttribute(JRXmlConstants.ATTRIBUTE_keepTogether, group.isKeepTogether(), false);
		}

		writeExpression(JRXmlConstants.ELEMENT_groupExpression, group.getExpression(), false);

		JRSection groupHeader = group.getGroupHeaderSection();
		if (groupHeader != null)
		{
			writer.startElement(JRXmlConstants.ELEMENT_groupHeader);
			writeSection(groupHeader);
			writer.closeElement(true);
		}

		JRSection groupFooter = group.getGroupFooterSection();
		if (groupFooter != null)
		{
			writer.startElement(JRXmlConstants.ELEMENT_groupFooter);
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
				if(isNewerVersionOrEqual(JRConstants.VERSION_3_5_2))
				{
					for(int i = 0; i < bands.length; i++)
					{
						writeBand(bands[i]);
					}
				}
				else
				{
					writeBand(bands[0]);
				}
			}

			if (isNewerVersionOrEqual(JRConstants.VERSION_6_0_0))
			{
				JRPart[] parts = section.getParts();
				if (parts != null && parts.length > 0)
				{
					for(int i = 0; i < parts.length; i++)
					{
						writePart(parts[i]);
					}
				}
			}
		}
	}


	/**
	 *
	 */
	private void writeBand(JRBand band) throws IOException
	{
		writer.startElement(JRXmlConstants.ELEMENT_band);
		writer.addAttributePositive(JRXmlConstants.ATTRIBUTE_height, band.getHeight());
		if(isNewerVersionOrEqual(JRConstants.VERSION_3_5_2))
		{
			writer.addAttribute(JRXmlConstants.ATTRIBUTE_splitType, band.getSplitTypeValue());
		}
		else
		{
			writer.addAttribute(JRXmlConstants.ATTRIBUTE_isSplitAllowed, band.getSplitTypeValue() != SplitTypeEnum.PREVENT, true);
		}

		if(isNewerVersionOrEqual(JRConstants.VERSION_4_8_0))
		{
			writeProperties(band);
		}
		writeExpression(JRXmlConstants.ELEMENT_printWhenExpression, band.getPrintWhenExpression(), false);
		
		writeChildElements(band);

		List<ExpressionReturnValue> returnValues = band.getReturnValues();
		if (returnValues != null && !returnValues.isEmpty())
		{
			for (ExpressionReturnValue returnValue : returnValues)
			{
				writeReturnValue(returnValue);
			}
		}
		
		writer.closeElement();
	}


	/**
	 *
	 */
	private void writePart(JRPart part) throws IOException
	{
		ComponentKey componentKey = part.getComponentKey();
		PartComponentXmlWriter componentXmlWriter = 
			PartComponentsEnvironment.getInstance(jasperReportsContext).getManager(componentKey).getComponentXmlWriter(jasperReportsContext);
		
		if (componentXmlWriter.isToWrite(part, this))
		{
			writer.startElement(JRXmlConstants.ELEMENT_part, getNamespace());
			PartEvaluationTime evaluationTime = part.getEvaluationTime();
			if (evaluationTime != null)
			{
				writer.addAttribute(JRXmlConstants.ATTRIBUTE_evaluationTime, evaluationTime.getEvaluationTimeType());
				writer.addAttribute(JRXmlConstants.ATTRIBUTE_evaluationGroup, evaluationTime.getEvaluationGroup());
			}
			writer.addAttribute(JRXmlConstants.ATTRIBUTE_uuid, part.getUUID().toString());

			writeProperties(part);
			writeExpression(JRXmlConstants.ELEMENT_printWhenExpression, part.getPrintWhenExpression(), false);
			writeExpression(JRXmlConstants.ELEMENT_partNameExpression, part.getPartNameExpression(), false);
			
			componentXmlWriter.writeToXml(part, this);

			writer.closeElement();
		}
	}

	
	/**
	 * Writes the contents (child elements) of an element container.
	 * 
	 * @param elementContainer the element container
	 */
	public void writeChildElements(JRElementGroup elementContainer)
	{
		List<JRChild> children = elementContainer.getChildren();
		if (children != null && children.size() > 0)
		{
			for(int i = 0; i < children.size(); i++)
			{
				children.get(i).visit(xmlWriterVisitor);
			}
		}
	}

	/**
	 *
	 */
	public void writeElementGroup(JRElementGroup elementGroup) throws IOException
	{
		writer.startElement(JRXmlConstants.ELEMENT_elementGroup, getNamespace());

		/*   */
		writeChildElements(elementGroup);

		writer.closeElement();
	}


	/**
	 *
	 */
	public void writeBreak(JRBreak breakElement) throws IOException
	{
		writer.startElement(JRXmlConstants.ELEMENT_break, getNamespace());
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_type, breakElement.getTypeValue(), BreakTypeEnum.PAGE);

		writeReportElement(breakElement);

		writer.closeElement();
	}


	/**
	 *
	 */
	public void writeLine(JRLine line) throws IOException
	{
		writer.startElement(JRXmlConstants.ELEMENT_line, getNamespace());
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_direction, line.getDirectionValue(), LineDirectionEnum.TOP_DOWN);

		writeReportElement(line);
		writeGraphicElement(line);

		writer.closeElement();
	}


	/**
	 *
	 */
	private void writeReportElement(JRElement element) throws IOException
	{
		writer.startElement(JRXmlConstants.ELEMENT_reportElement);
		writer.addEncodedAttribute(JRXmlConstants.ATTRIBUTE_key, element.getKey());
		writeStyleReferenceAttr(element);
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_positionType, element.getPositionTypeValue(), PositionTypeEnum.FIX_RELATIVE_TO_TOP);
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_stretchType, element.getStretchTypeValue(), StretchTypeEnum.NO_STRETCH);
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_isPrintRepeatedValues, element.isPrintRepeatedValues(), true);
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_mode, element.getOwnModeValue());

		writer.addAttribute(JRXmlConstants.ATTRIBUTE_x, element.getX());
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_y, element.getY());
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_width, element.getWidth());
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_height, element.getHeight());
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_isRemoveLineWhenBlank, element.isRemoveLineWhenBlank(), false);
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_isPrintInFirstWholeBand, element.isPrintInFirstWholeBand(), false);
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_isPrintWhenDetailOverflows, element.isPrintWhenDetailOverflows(), false);

		if (element.getPrintWhenGroupChanges() != null)
		{
			writer.addEncodedAttribute(JRXmlConstants.ATTRIBUTE_printWhenGroupChanges, element.getPrintWhenGroupChanges().getName());
		}

		writer.addAttribute(JRXmlConstants.ATTRIBUTE_forecolor, element.getOwnForecolor());
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_backcolor, element.getOwnBackcolor());
		
		if(isNewerVersionOrEqual(JRConstants.VERSION_4_6_0))
		{
			writer.addEncodedAttribute(JRXmlConstants.ATTRIBUTE_uuid, element.getUUID().toString());
		}

		writeProperties(element);
		writePropertyExpressions(element.getPropertyExpressions());
		writeExpression(JRXmlConstants.ELEMENT_printWhenExpression, element.getPrintWhenExpression(), false);
		writer.closeElement();
	}


	public void writePropertyExpressions(
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
		JRExpression valueExpression = propertyExpression.getValueExpression();
		String expressionText = valueExpression == null ? "" : valueExpression.getText();
		writer.writeCDATAElement(JRXmlConstants.ELEMENT_propertyExpression, getNamespace(), expressionText, 
				JRXmlConstants.ATTRIBUTE_name, propertyExpression.getName());
	}


	/**
	 *
	 */
	private void writeGraphicElement(JRGraphicElement element) throws IOException
	{
		writer.startElement(JRXmlConstants.ELEMENT_graphicElement);
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_fill, element.getOwnFillValue());
		writePen(element.getLinePen());
		writer.closeElement(true);
	}


	/**
	 *
	 */
	public void writeRectangle(JRRectangle rectangle) throws IOException
	{
		writer.startElement(JRXmlConstants.ELEMENT_rectangle, getNamespace());
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_radius, rectangle.getOwnRadius());

		writeReportElement(rectangle);
		writeGraphicElement(rectangle);

		writer.closeElement();
	}


	/**
	 *
	 */
	public void writeEllipse(JREllipse ellipse) throws IOException
	{
		writer.startElement(JRXmlConstants.ELEMENT_ellipse, getNamespace());

		writeReportElement(ellipse);
		writeGraphicElement(ellipse);

		writer.closeElement();
	}


	/**
	 *
	 */
	public void writeImage(JRImage image) throws IOException
	{
		writer.startElement(JRXmlConstants.ELEMENT_image, getNamespace());
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_scaleImage, image.getOwnScaleImageValue());
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_hAlign, image.getOwnHorizontalImageAlign());
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_vAlign, image.getOwnVerticalImageAlign());
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_isUsingCache, image.getUsingCache());
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_isLazy, image.isLazy(), false);
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_onErrorType, image.getOnErrorTypeValue(), OnErrorTypeEnum.ERROR);
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_evaluationTime, image.getEvaluationTimeValue(), EvaluationTimeEnum.NOW);

		if (image.getEvaluationGroup() != null)
		{
			writer.addEncodedAttribute(JRXmlConstants.ATTRIBUTE_evaluationGroup, image.getEvaluationGroup().getName());
		}

		writer.addEncodedAttribute(JRXmlConstants.ATTRIBUTE_hyperlinkType, image.getLinkType(), HyperlinkTypeEnum.NONE.getName());
		writer.addEncodedAttribute(JRXmlConstants.ATTRIBUTE_hyperlinkTarget, image.getLinkTarget(), HyperlinkTargetEnum.SELF.getName());
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_bookmarkLevel, image.getBookmarkLevel(), JRAnchor.NO_BOOKMARK);

		writeReportElement(image);
		writeBox(image.getLineBox());
		writeGraphicElement(image);

		//FIXME class is mandatory in verifier

		writeExpression(JRXmlConstants.ELEMENT_imageExpression, image.getExpression(), true);
		writeExpression(JRXmlConstants.ELEMENT_anchorNameExpression, image.getAnchorNameExpression(), false);
		writeExpression(JRXmlConstants.ELEMENT_hyperlinkReferenceExpression, image.getHyperlinkReferenceExpression(), false);
		writeExpression(JRXmlConstants.ELEMENT_hyperlinkWhenExpression, image.getHyperlinkWhenExpression(), false);//FIXMENOW can we reuse method for writing hyperlink?
		writeExpression(JRXmlConstants.ELEMENT_hyperlinkAnchorExpression, image.getHyperlinkAnchorExpression(), false);
		writeExpression(JRXmlConstants.ELEMENT_hyperlinkPageExpression, image.getHyperlinkPageExpression(), false);
		writeExpression(JRXmlConstants.ELEMENT_hyperlinkTooltipExpression, image.getHyperlinkTooltipExpression(), false);
		writeHyperlinkParameters(image.getHyperlinkParameters());

		writer.closeElement();
	}


	/**
	 *
	 */
	public void writeStaticText(JRStaticText staticText) throws IOException
	{
		writer.startElement(JRXmlConstants.ELEMENT_staticText, getNamespace());

		writeReportElement(staticText);
		writeBox(staticText.getLineBox());
		writeTextElement(staticText);

		writer.writeCDATAElement(JRXmlConstants.ELEMENT_text, staticText.getText());

		writer.closeElement();
	}


	/**
	 *
	 */
	private void writeTextElement(JRTextElement textElement) throws IOException
	{
		writer.startElement(JRXmlConstants.ELEMENT_textElement);
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_textAlignment, textElement.getOwnHorizontalTextAlign());
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_verticalAlignment, textElement.getOwnVerticalTextAlign());
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_rotation, textElement.getOwnRotationValue());
		if (isOlderVersionThan(JRConstants.VERSION_4_0_2))
		{
			writer.addAttribute(JRXmlConstants.ATTRIBUTE_lineSpacing, textElement.getParagraph().getLineSpacing());
		}
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_markup, textElement.getOwnMarkup());

		writeFont(textElement);
		writeParagraph(textElement.getParagraph());

		writer.closeElement(true);
	}


	/**
	 *
	 */
	public void writeFont(JRFont font) throws IOException
	{
		if (font != null)
		{
			writer.startElement(JRXmlConstants.ELEMENT_font, getNamespace());
			writer.addEncodedAttribute(JRXmlConstants.ATTRIBUTE_fontName, font.getOwnFontName());
			writer.addAttribute(JRXmlConstants.ATTRIBUTE_size, font.getOwnFontsize(), true);
			writer.addAttribute(JRXmlConstants.ATTRIBUTE_isBold, font.isOwnBold());
			writer.addAttribute(JRXmlConstants.ATTRIBUTE_isItalic, font.isOwnItalic());
			writer.addAttribute(JRXmlConstants.ATTRIBUTE_isUnderline, font.isOwnUnderline());
			writer.addAttribute(JRXmlConstants.ATTRIBUTE_isStrikeThrough, font.isOwnStrikeThrough());
			writer.addEncodedAttribute(JRXmlConstants.ATTRIBUTE_pdfFontName, font.getOwnPdfFontName());
			writer.addEncodedAttribute(JRXmlConstants.ATTRIBUTE_pdfEncoding, font.getOwnPdfEncoding());
			writer.addAttribute(JRXmlConstants.ATTRIBUTE_isPdfEmbedded, font.isOwnPdfEmbedded());
			writer.closeElement(true);
		}
	}


	/**
	 *
	 */
	
	public void writeTextField(JRTextField textField) throws IOException
	{
		writer.startElement(JRXmlConstants.ELEMENT_textField, getNamespace());
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_isStretchWithOverflow, textField.isStretchWithOverflow(), false);
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_evaluationTime, textField.getEvaluationTimeValue(), EvaluationTimeEnum.NOW);

		if (textField.getEvaluationGroup() != null)
		{
			writer.addEncodedAttribute(JRXmlConstants.ATTRIBUTE_evaluationGroup, textField.getEvaluationGroup().getName());
		}

		writer.addEncodedAttribute(JRXmlConstants.ATTRIBUTE_pattern, textField.getOwnPattern());
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_isBlankWhenNull, textField.isOwnBlankWhenNull());

		writer.addEncodedAttribute(JRXmlConstants.ATTRIBUTE_hyperlinkType, textField.getLinkType(), HyperlinkTypeEnum.NONE.getName());
		writer.addEncodedAttribute(JRXmlConstants.ATTRIBUTE_hyperlinkTarget, textField.getLinkTarget(), HyperlinkTargetEnum.SELF.getName());
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_bookmarkLevel, textField.getBookmarkLevel(), JRAnchor.NO_BOOKMARK);

		writeReportElement(textField);
		writeBox(textField.getLineBox());
		writeTextElement(textField);

		writeExpression(JRXmlConstants.ELEMENT_textFieldExpression, textField.getExpression(), true);
		
		if(isNewerVersionOrEqual(JRConstants.VERSION_4_1_1))
		{
			writer.writeExpression(JRXmlConstants.ELEMENT_patternExpression, textField.getPatternExpression());
		}
		writeExpression(JRXmlConstants.ELEMENT_anchorNameExpression, textField.getAnchorNameExpression(), false);
		writeExpression(JRXmlConstants.ELEMENT_hyperlinkReferenceExpression, textField.getHyperlinkReferenceExpression(), false);
		writeExpression(JRXmlConstants.ELEMENT_hyperlinkWhenExpression, textField.getHyperlinkWhenExpression(), false);
		writeExpression(JRXmlConstants.ELEMENT_hyperlinkAnchorExpression, textField.getHyperlinkAnchorExpression(), false);
		writeExpression(JRXmlConstants.ELEMENT_hyperlinkPageExpression, textField.getHyperlinkPageExpression(), false);
		writeExpression(JRXmlConstants.ELEMENT_hyperlinkTooltipExpression, textField.getHyperlinkTooltipExpression(), false);
		writeHyperlinkParameters(textField.getHyperlinkParameters());

		writer.closeElement();
	}


	/**
	 *
	 */
	
	public void writeSubreport(JRSubreport subreport) throws IOException
	{
		writer.startElement(JRXmlConstants.ELEMENT_subreport, getNamespace());
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_isUsingCache, subreport.getUsingCache());
		if(isNewerVersionOrEqual(JRConstants.VERSION_3_5_0))
		{
			writer.addAttribute(JRXmlConstants.ATTRIBUTE_runToBottom, subreport.isRunToBottom());
		}
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_overflowType, subreport.getOverflowType());

		writeReportElement(subreport);

		writeExpression(JRXmlConstants.ELEMENT_parametersMapExpression, subreport.getParametersMapExpression(), false);

		/*   */
		JRSubreportParameter[] parameters = subreport.getParameters();
		if (parameters != null && parameters.length > 0)
		{
			for(int i = 0; i < parameters.length; i++)
			{
				writeSubreportParameter(parameters[i], getNamespace());
			}
		}

		writeExpression(JRXmlConstants.ELEMENT_connectionExpression, subreport.getConnectionExpression(), false);
		writeExpression(JRXmlConstants.ELEMENT_dataSourceExpression, subreport.getDataSourceExpression(), false);

		JRSubreportReturnValue[] returnValues = subreport.getReturnValues();
		if (returnValues != null && returnValues.length > 0)
		{
			for(int i = 0; i < returnValues.length; i++)
			{
				writeSubreportReturnValue(returnValues[i], getNamespace());
			}
		}

		writeExpression(JRXmlConstants.ELEMENT_subreportExpression, subreport.getExpression(), true);

		writer.closeElement();
	}


	/**
	 *
	 */
	
	public void writeSubreportParameter(JRSubreportParameter subreportParameter, XmlNamespace namespace) throws IOException
	{
		writer.startElement(JRXmlConstants.ELEMENT_subreportParameter, namespace);
		writer.addEncodedAttribute(JRXmlConstants.ATTRIBUTE_name, subreportParameter.getName());

		writeExpression(JRXmlConstants.ELEMENT_subreportParameterExpression, subreportParameter.getExpression(), false);

		writer.closeElement();
	}


	
	private void writeDatasetParameter(JRDatasetParameter datasetParameter) throws IOException
	{
		writer.startElement(JRXmlConstants.ELEMENT_datasetParameter);
		writer.addEncodedAttribute(JRXmlConstants.ATTRIBUTE_name, datasetParameter.getName());

		writeExpression(JRXmlConstants.ELEMENT_datasetParameterExpression, datasetParameter.getExpression(), false);

		writer.closeElement();
	}


	/**
	 *
	 */
	
	private void writeChart(JRChart chart) throws IOException
	{
		writer.startElement(JRXmlConstants.ELEMENT_chart);
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_isShowLegend, chart.getShowLegend());
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_evaluationTime, chart.getEvaluationTimeValue(), EvaluationTimeEnum.NOW);

		if (chart.getEvaluationTimeValue() == EvaluationTimeEnum.GROUP)
		{
			writer.addEncodedAttribute(JRXmlConstants.ATTRIBUTE_evaluationGroup, chart.getEvaluationGroup().getName());
		}

		writer.addEncodedAttribute(JRXmlConstants.ATTRIBUTE_hyperlinkType, chart.getLinkType(), HyperlinkTypeEnum.NONE.getName());
		writer.addEncodedAttribute(JRXmlConstants.ATTRIBUTE_hyperlinkTarget, chart.getLinkTarget(), HyperlinkTargetEnum.SELF.getName());
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_bookmarkLevel, chart.getBookmarkLevel(), JRAnchor.NO_BOOKMARK);
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_customizerClass, chart.getCustomizerClass());
		writer.addEncodedAttribute(JRXmlConstants.ATTRIBUTE_renderType, chart.getRenderType());
		if(isNewerVersionOrEqual(JRConstants.VERSION_3_1_0))
		{
			writer.addEncodedAttribute(JRXmlConstants.ATTRIBUTE_theme, chart.getTheme());
		}

		writeReportElement(chart);
		writeBox(chart.getLineBox());

		// write title
		writer.startElement(JRXmlConstants.ELEMENT_chartTitle);
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_position, chart.getTitlePositionValue());
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_color, chart.getOwnTitleColor());
		writeFont(chart.getTitleFont());
		if (chart.getTitleExpression() != null)
		{
			writeExpression(JRXmlConstants.ELEMENT_titleExpression, chart.getTitleExpression(), false);
		}
		writer.closeElement();

		// write subtitle
		writer.startElement(JRXmlConstants.ELEMENT_chartSubtitle);
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_color, chart.getOwnSubtitleColor());
		writeFont(chart.getSubtitleFont());
		if (chart.getSubtitleExpression() != null)
		{
			writeExpression(JRXmlConstants.ELEMENT_subtitleExpression, chart.getSubtitleExpression(), false);
		}
		writer.closeElement();

		// write chartLegend
		writer.startElement(JRXmlConstants.ELEMENT_chartLegend);
		if (chart.getOwnLegendColor() != null)
		{
			writer.addAttribute(JRXmlConstants.ATTRIBUTE_textColor, chart.getOwnLegendColor());
		}
		if (chart.getOwnLegendBackgroundColor() != null)
		{
			writer.addAttribute(JRXmlConstants.ATTRIBUTE_backgroundColor, chart.getOwnLegendBackgroundColor());
		}
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_position, chart.getLegendPositionValue());
		writeFont(chart.getLegendFont());
		writer.closeElement();

		writeExpression(JRXmlConstants.ELEMENT_anchorNameExpression, chart.getAnchorNameExpression(), false);
		writeExpression(JRXmlConstants.ELEMENT_hyperlinkReferenceExpression, chart.getHyperlinkReferenceExpression(), false);
		writeExpression(JRXmlConstants.ELEMENT_hyperlinkWhenExpression, chart.getHyperlinkWhenExpression(), false);
		writeExpression(JRXmlConstants.ELEMENT_hyperlinkAnchorExpression, chart.getHyperlinkAnchorExpression(), false);
		writeExpression(JRXmlConstants.ELEMENT_hyperlinkPageExpression, chart.getHyperlinkPageExpression(), false);
		writeExpression(JRXmlConstants.ELEMENT_hyperlinkTooltipExpression, chart.getHyperlinkTooltipExpression(), false);
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
		writeElementDataset(dataset, ResetTypeEnum.REPORT, true);
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
		writeElementDataset(dataset, ResetTypeEnum.REPORT, skipIfEmpty);
	}
	
	
	/**
	 * Writes the JRXML representation of an {@link JRElementDataset element dataset}.
	 * 
	 * <p>
	 * The method produces a <code>&lt;dataset&gt;</code> XML element.
	 * 
	 * @param dataset the element dataset
	 * @param defaultResetType the default dataset reset type
	 * @param skipIfEmpty if set, no output will be produced if the element dataset
	 * only has default attribute values
	 * @throws IOException any I/O exception that occurred while writing the
	 * XML output
	 */
	
	public void writeElementDataset(JRElementDataset dataset, ResetTypeEnum defaultResetType, 
			boolean skipIfEmpty) throws IOException
	{
		writer.startElement(JRXmlConstants.ELEMENT_dataset, getNamespace());
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_resetType, dataset.getResetTypeValue(), defaultResetType);

		if (dataset.getResetTypeValue() == ResetTypeEnum.GROUP)
		{
			writer.addEncodedAttribute(JRXmlConstants.ATTRIBUTE_resetGroup, dataset.getResetGroup().getName());
		}
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_incrementType, dataset.getIncrementTypeValue(), IncrementTypeEnum.NONE);

		if (dataset.getIncrementTypeValue() == IncrementTypeEnum.GROUP)
		{
			writer.addEncodedAttribute(JRXmlConstants.ATTRIBUTE_incrementGroup, dataset.getIncrementGroup().getName());
		}

		writeExpression(JRXmlConstants.ELEMENT_incrementWhenExpression, dataset.getIncrementWhenExpression(), false);

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
		writer.startElement(JRXmlConstants.ELEMENT_categoryDataset);

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
		writer.startElement(JRXmlConstants.ELEMENT_timeSeriesDataset);
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_timePeriod, TimePeriodEnum.getByValue(dataset.getTimePeriod()), TimePeriodEnum.DAY);

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
		writer.startElement(JRXmlConstants.ELEMENT_ganttDataset);
		
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
		writer.startElement(JRXmlConstants.ELEMENT_timePeriodDataset);
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
		writer.startElement(JRXmlConstants.ELEMENT_pieSeries);

		writeExpression(JRXmlConstants.ELEMENT_keyExpression, pieSeries.getKeyExpression(), false);
		writeExpression(JRXmlConstants.ELEMENT_valueExpression, pieSeries.getValueExpression(), false);
		writeExpression(JRXmlConstants.ELEMENT_labelExpression, pieSeries.getLabelExpression(), false);
		writeHyperlink(JRXmlConstants.ELEMENT_sectionHyperlink, pieSeries.getSectionHyperlink());

		writer.closeElement();
	}

	/**
	 *
	 */
	
	private void writeCategorySeries(JRCategorySeries categorySeries) throws IOException
	{
		writer.startElement(JRXmlConstants.ELEMENT_categorySeries);

		writeExpression(JRXmlConstants.ELEMENT_seriesExpression, categorySeries.getSeriesExpression(), false);
		writeExpression(JRXmlConstants.ELEMENT_categoryExpression, categorySeries.getCategoryExpression(), false);
		writeExpression(JRXmlConstants.ELEMENT_valueExpression, categorySeries.getValueExpression(), false);
		writeExpression(JRXmlConstants.ELEMENT_labelExpression, categorySeries.getLabelExpression(), false);
		writeHyperlink(JRXmlConstants.ELEMENT_itemHyperlink, categorySeries.getItemHyperlink());

		writer.closeElement();
	}

	/**
	 *
	 */
	private void writeXyzDataset(JRXyzDataset dataset) throws IOException
	{
		writer.startElement(JRXmlConstants.ELEMENT_xyzDataset);
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
		writer.startElement(JRXmlConstants.ELEMENT_xyzSeries);

		writeExpression(JRXmlConstants.ELEMENT_seriesExpression, series.getSeriesExpression(), false);
		writeExpression(JRXmlConstants.ELEMENT_xValueExpression, series.getXValueExpression(), false);
		writeExpression(JRXmlConstants.ELEMENT_yValueExpression, series.getYValueExpression(), false);
		writeExpression(JRXmlConstants.ELEMENT_zValueExpression, series.getZValueExpression(), false);
		writeHyperlink(JRXmlConstants.ELEMENT_itemHyperlink, series.getItemHyperlink());

		writer.closeElement();
	}

	/**
	 *
	 */
	
	private void writeXySeries(JRXySeries xySeries) throws IOException
	{
		writer.startElement(JRXmlConstants.ELEMENT_xySeries);
		if(isNewerVersionOrEqual(JRConstants.VERSION_5_0_1))
		{
			writer.addAttribute(JRXmlConstants.ATTRIBUTE_autoSort, xySeries.getAutoSort());
		}
		writeExpression(JRXmlConstants.ELEMENT_seriesExpression, xySeries.getSeriesExpression(), false);
		writeExpression(JRXmlConstants.ELEMENT_xValueExpression, xySeries.getXValueExpression(), false);
		writeExpression(JRXmlConstants.ELEMENT_yValueExpression, xySeries.getYValueExpression(), false);
		writeExpression(JRXmlConstants.ELEMENT_labelExpression, xySeries.getLabelExpression(), false);
		writeHyperlink(JRXmlConstants.ELEMENT_itemHyperlink, xySeries.getItemHyperlink());

		writer.closeElement();
	}


	/**
	 *
	 */
	private void writeXyDataset(JRXyDataset dataset) throws IOException
	{
		writer.startElement(JRXmlConstants.ELEMENT_xyDataset);

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
		writer.startElement(JRXmlConstants.ELEMENT_timeSeries);

		writeExpression(JRXmlConstants.ELEMENT_seriesExpression, timeSeries.getSeriesExpression(), false);
		writeExpression(JRXmlConstants.ELEMENT_timePeriodExpression, timeSeries.getTimePeriodExpression(), false);
		writeExpression(JRXmlConstants.ELEMENT_valueExpression, timeSeries.getValueExpression(), false);
		writeExpression(JRXmlConstants.ELEMENT_labelExpression, timeSeries.getLabelExpression(), false);
		writeHyperlink(JRXmlConstants.ELEMENT_itemHyperlink, timeSeries.getItemHyperlink());

		writer.closeElement();
	}


	/**
	 * 
	 */
	
	private void writeGanttSeries(JRGanttSeries ganttSeries) throws IOException
	{
		writer.startElement(JRXmlConstants.ELEMENT_ganttSeries);
		
		writeExpression(JRXmlConstants.ELEMENT_seriesExpression, ganttSeries.getSeriesExpression(), false);
		writeExpression(JRXmlConstants.ELEMENT_taskExpression, ganttSeries.getTaskExpression(), false);
		writeExpression(JRXmlConstants.ELEMENT_subtaskExpression, ganttSeries.getSubtaskExpression(), false);
		writeExpression(JRXmlConstants.ELEMENT_startDateExpression, ganttSeries.getStartDateExpression(), false);
		writeExpression(JRXmlConstants.ELEMENT_endDateExpression, ganttSeries.getEndDateExpression(), false);
		writeExpression(JRXmlConstants.ELEMENT_percentExpression, ganttSeries.getPercentExpression(), false);
		writeExpression(JRXmlConstants.ELEMENT_labelExpression, ganttSeries.getLabelExpression(), false);
		writeHyperlink(JRXmlConstants.ELEMENT_itemHyperlink, ganttSeries.getItemHyperlink());
		
		writer.closeElement();
	}


	
	private void writeTimePeriodSeries(JRTimePeriodSeries timePeriodSeries) throws IOException
	{
		writer.startElement(JRXmlConstants.ELEMENT_timePeriodSeries);

		writeExpression(JRXmlConstants.ELEMENT_seriesExpression, timePeriodSeries.getSeriesExpression(), false);
		writeExpression(JRXmlConstants.ELEMENT_startDateExpression, timePeriodSeries.getStartDateExpression(), false);
		writeExpression(JRXmlConstants.ELEMENT_endDateExpression, timePeriodSeries.getEndDateExpression(), false);
		writeExpression(JRXmlConstants.ELEMENT_valueExpression, timePeriodSeries.getValueExpression(), false);
		writeExpression(JRXmlConstants.ELEMENT_labelExpression, timePeriodSeries.getLabelExpression(), false);
		writeHyperlink(JRXmlConstants.ELEMENT_itemHyperlink, timePeriodSeries.getItemHyperlink());

		writer.closeElement();
	}


	/**
	 *
	 */
	
	public void writePieDataset(JRPieDataset dataset) throws IOException
	{
		writer.startElement(JRXmlConstants.ELEMENT_pieDataset, getNamespace());

		if(isNewerVersionOrEqual(JRConstants.VERSION_3_5_0))
		{
			writer.addAttribute(JRXmlConstants.ATTRIBUTE_maxCount, dataset.getMaxCount());
			writer.addAttribute(JRXmlConstants.ATTRIBUTE_minPercentage, dataset.getMinPercentage());
		}

		writeElementDataset(dataset);

		/*   */
		JRPieSeries[] pieSeries = dataset.getSeries();
		if (pieSeries != null)
		{
			if (isNewerVersionOrEqual(JRConstants.VERSION_3_5_0) && pieSeries.length > 1)
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
				writeExpression(JRXmlConstants.ELEMENT_keyExpression, ps.getKeyExpression(), false);
				writeExpression(JRXmlConstants.ELEMENT_valueExpression, ps.getValueExpression(), false);
				writeExpression(JRXmlConstants.ELEMENT_labelExpression, ps.getLabelExpression(), false);
				writeHyperlink(JRXmlConstants.ELEMENT_sectionHyperlink, ps.getSectionHyperlink());
			}
		}

		if(isNewerVersionOrEqual(JRConstants.VERSION_3_5_0))
		{
			writeExpression(JRXmlConstants.ELEMENT_otherKeyExpression, dataset.getOtherKeyExpression(), false);
			writeExpression(JRXmlConstants.ELEMENT_otherLabelExpression, dataset.getOtherLabelExpression(), false);
			writeHyperlink(JRXmlConstants.ELEMENT_otherSectionHyperlink, dataset.getOtherSectionHyperlink());
		}

		writer.closeElement();
	}

	/**
	 * Writes the description of a value dataset to the output stream.
	 * @param dataset the value dataset to persist
	 */
	
	public void writeValueDataset(JRValueDataset dataset) throws IOException
	{
		writer.startElement(JRXmlConstants.ELEMENT_valueDataset, getNamespace());

		// default reset type of value datasets is None
		writeElementDataset(dataset, ResetTypeEnum.NONE, true);

		writeExpression(JRXmlConstants.ELEMENT_valueExpression, dataset.getValueExpression(), false);
		writer.closeElement();
	}


	/**
	 * Writes the description of how to display a value in a valueDataset.
	 *
	 * @param valueDisplay the description to save
	 */
	public void writeValueDisplay(JRValueDisplay valueDisplay) throws IOException
	{
		writer.startElement(JRXmlConstants.ELEMENT_valueDisplay, getNamespace());

		writer.addAttribute(JRXmlConstants.ATTRIBUTE_color, valueDisplay.getColor());
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_mask, valueDisplay.getMask());

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
		if(isNewerVersionOrEqual(JRConstants.VERSION_3_5_3) && itemLabel != null)
		{
			writer.startElement(JRXmlConstants.ELEMENT_itemLabel, getNamespace());
	
			writer.addAttribute(JRXmlConstants.ATTRIBUTE_color, itemLabel.getColor());
			writer.addAttribute(JRXmlConstants.ATTRIBUTE_backgroundColor, itemLabel.getBackgroundColor());
	//		writer.addAttribute(JRXmlConstants.ATTRIBUTE_mask, itemLabel.getMask());
	
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
		writer.startElement(JRXmlConstants.ELEMENT_dataRange, getNamespace());

		writeExpression(JRXmlConstants.ELEMENT_lowExpression, dataRange.getLowExpression(), false);
		writeExpression(JRXmlConstants.ELEMENT_highExpression, dataRange.getHighExpression(), false);
		writer.closeElement();
	}


	/**
	 * Writes a meter interval description to the output stream.
	 *
	 * @param interval the interval to write
	 */
	private void writeMeterInterval(JRMeterInterval interval) throws IOException
	{
		writer.startElement(JRXmlConstants.ELEMENT_meterInterval);

		writer.addAttribute(JRXmlConstants.ATTRIBUTE_label, interval.getLabel());
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_color, interval.getBackgroundColor());
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_alpha, interval.getAlphaDouble());

		writeDataRange(interval.getDataRange());

		writer.closeElement();
	}

	/**
	 * Writes out the contents of a series colors block for a chart.  Assumes the caller
	 * has already written the <code>&lt;seriesColors&gt;</code> tag.
	 *
	 * @param seriesColors the colors to write
	 */
	private void writeSeriesColors(SortedSet<JRSeriesColor> seriesColors) throws IOException
	{
		if (seriesColors == null || seriesColors.size() == 0)
		{
			return;
		}
		//FIXME why do we need an array?
		JRSeriesColor[] colors = seriesColors.toArray(new JRSeriesColor[seriesColors.size()]);
		for (int i = 0; i < colors.length; i++)
		{
			writer.startElement(JRXmlConstants.ELEMENT_seriesColor);
			writer.addAttribute(JRXmlConstants.ATTRIBUTE_seriesOrder, colors[i].getSeriesOrder());
			writer.addAttribute(JRXmlConstants.ATTRIBUTE_color, colors[i].getColor());
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
		writer.addAttribute(JRChartAxisFactory.ATTRIBUTE_position, chartAxis.getPositionValue());

		// Let the nested chart describe itself
		writeChartTag(chartAxis.getChart());
		writer.closeElement();
	}

	/**
	 *
	 *
	 */
	@SuppressWarnings("deprecation")
	private void writePlot(JRChartPlot plot) throws IOException
	{
		writer.startElement(JRXmlConstants.ELEMENT_plot);
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_backcolor, plot.getOwnBackcolor());
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_orientation, plot.getOrientationValue(), PlotOrientationEnum.VERTICAL);
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_backgroundAlpha, plot.getBackgroundAlphaFloat());
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_foregroundAlpha, plot.getForegroundAlphaFloat());
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_labelRotation, plot.getLabelRotationDouble());
		writeSeriesColors(plot.getSeriesColors());

		writer.closeElement();
	}


	/**
	 *
	 */
	public void writePieChart(JRChart chart) throws IOException
	{
		writer.startElement(JRXmlConstants.ELEMENT_pieChart, getNamespace());
		writeChart(chart);
		writePieDataset((JRPieDataset) chart.getDataset());

		// write plot
		JRPiePlot plot = (JRPiePlot) chart.getPlot();
		writer.startElement(JRXmlConstants.ELEMENT_piePlot);
		if(isNewerVersionOrEqual(JRConstants.VERSION_3_7_5))
		{
			writer.addAttribute(JRXmlConstants.ATTRIBUTE_isShowLabels, plot.getShowLabels());
		}
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_isCircular, plot.getCircular());
		if(isNewerVersionOrEqual(JRConstants.VERSION_3_1_0))
		{
			writer.addAttribute(JRXmlConstants.ATTRIBUTE_labelFormat, plot.getLabelFormat());
			writer.addAttribute(JRXmlConstants.ATTRIBUTE_legendLabelFormat, plot.getLegendLabelFormat());
		}
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
		writer.startElement(JRXmlConstants.ELEMENT_pie3DChart, getNamespace());
		writeChart(chart);
		writePieDataset((JRPieDataset) chart.getDataset());

		// write plot
		JRPie3DPlot plot = (JRPie3DPlot) chart.getPlot();
		writer.startElement(JRXmlConstants.ELEMENT_pie3DPlot);
		if(isNewerVersionOrEqual(JRConstants.VERSION_3_7_5))
		{
			writer.addAttribute(JRXmlConstants.ATTRIBUTE_isShowLabels, plot.getShowLabels());
		}
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_depthFactor, plot.getDepthFactorDouble());
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_isCircular, plot.getCircular());
		if(isNewerVersionOrEqual(JRConstants.VERSION_3_1_0))
		{
			writer.addAttribute(JRXmlConstants.ATTRIBUTE_labelFormat, plot.getLabelFormat());
			writer.addAttribute(JRXmlConstants.ATTRIBUTE_legendLabelFormat, plot.getLegendLabelFormat());
		}
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
		writer.startElement(JRXmlConstants.ELEMENT_categoryAxisFormat, getNamespace());
		
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_labelRotation, labelRotation);

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
		writer.startElement(JRXmlConstants.ELEMENT_axisFormat);
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_labelColor, axisLabelColor);
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_tickLabelColor, axisTickLabelColor);
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_tickLabelMask, axisTickLabelMask);
		if(isNewerVersionOrEqual(JRConstants.VERSION_3_6_2))
		{
			writer.addAttribute(JRXmlConstants.ATTRIBUTE_verticalTickLabels, axisVerticalTickLabels);
		}
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_axisLineColor, axisLineColor);

		if (axisLabelFont != null)
		{
			writer.startElement(JRXmlConstants.ELEMENT_labelFont);
			writeFont(axisLabelFont);
			writer.closeElement();
		}

		if (axisTickLabelFont != null)
		{
			writer.startElement(JRXmlConstants.ELEMENT_tickLabelFont);
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
		writer.startElement(JRXmlConstants.ELEMENT_barPlot);
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_isShowLabels, plot.getShowLabels());
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_isShowTickLabels, plot.getShowTickLabels());
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_isShowTickMarks, plot.getShowTickMarks());

		writePlot(plot);
		writeItemLabel(plot.getItemLabel());
		
		writeExpression(JRXmlConstants.ELEMENT_categoryAxisLabelExpression, plot.getCategoryAxisLabelExpression(), false);
		writeCategoryAxisFormat(plot.getCategoryAxisLabelFont(), plot.getOwnCategoryAxisLabelColor(),
						plot.getCategoryAxisTickLabelFont(), plot.getOwnCategoryAxisTickLabelColor(),
						plot.getCategoryAxisTickLabelMask(), plot.getCategoryAxisVerticalTickLabels(), 
						plot.getCategoryAxisTickLabelRotation(), plot.getOwnCategoryAxisLineColor());
		writeExpression(JRXmlConstants.ELEMENT_valueAxisLabelExpression, plot.getValueAxisLabelExpression(), false);
		writeAxisFormat(JRXmlConstants.ELEMENT_valueAxisFormat, plot.getValueAxisLabelFont(), plot.getOwnValueAxisLabelColor(),
				plot.getValueAxisTickLabelFont(), plot.getOwnValueAxisTickLabelColor(),
				plot.getValueAxisTickLabelMask(), plot.getValueAxisVerticalTickLabels(), plot.getOwnValueAxisLineColor());
		if(isNewerVersionOrEqual(JRConstants.VERSION_3_5_0))
		{
			writeExpression(JRXmlConstants.ELEMENT_domainAxisMinValueExpression, plot.getDomainAxisMinValueExpression(), false);
			writeExpression(JRXmlConstants.ELEMENT_domainAxisMaxValueExpression, plot.getDomainAxisMaxValueExpression(), false);
			writeExpression(JRXmlConstants.ELEMENT_rangeAxisMinValueExpression, plot.getRangeAxisMinValueExpression(), false);
			writeExpression(JRXmlConstants.ELEMENT_rangeAxisMaxValueExpression, plot.getRangeAxisMaxValueExpression(), false);
		}

		writer.closeElement();
	}


	/**
	 *
	 */
	
	private void writeBubblePlot(JRBubblePlot plot) throws IOException
	{
		writer.startElement(JRXmlConstants.ELEMENT_bubblePlot);
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_scaleType, plot.getScaleTypeValue());
		writePlot(plot);

		writeExpression(JRXmlConstants.ELEMENT_xAxisLabelExpression, plot.getXAxisLabelExpression(), false);
		writeAxisFormat(JRXmlConstants.ELEMENT_xAxisFormat, plot.getXAxisLabelFont(), plot.getOwnXAxisLabelColor(),
				plot.getXAxisTickLabelFont(), plot.getOwnXAxisTickLabelColor(),
				plot.getXAxisTickLabelMask(), plot.getXAxisVerticalTickLabels(), plot.getOwnXAxisLineColor());
		writeExpression(JRXmlConstants.ELEMENT_yAxisLabelExpression, plot.getYAxisLabelExpression(), false);
		writeAxisFormat(JRXmlConstants.ELEMENT_yAxisFormat, plot.getYAxisLabelFont(), plot.getOwnYAxisLabelColor(),
				plot.getYAxisTickLabelFont(), plot.getOwnYAxisTickLabelColor(),
				plot.getYAxisTickLabelMask(), plot.getYAxisVerticalTickLabels(), plot.getOwnYAxisLineColor());
		
		if(isNewerVersionOrEqual(JRConstants.VERSION_3_5_0))
		{
			writeExpression(JRXmlConstants.ELEMENT_domainAxisMinValueExpression, plot.getDomainAxisMinValueExpression(), false);
			writeExpression(JRXmlConstants.ELEMENT_domainAxisMaxValueExpression, plot.getDomainAxisMaxValueExpression(), false);
			writeExpression(JRXmlConstants.ELEMENT_rangeAxisMinValueExpression, plot.getRangeAxisMinValueExpression(), false);
			writeExpression(JRXmlConstants.ELEMENT_rangeAxisMaxValueExpression, plot.getRangeAxisMaxValueExpression(), false);
		}

		writer.closeElement();
	}


	/**
	 *
	 */
	
	private void writeLinePlot(JRLinePlot plot) throws IOException
	{
		writer.startElement(JRXmlConstants.ELEMENT_linePlot);
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_isShowLines, plot.getShowLines());
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_isShowShapes, plot.getShowShapes());

		writePlot(plot);

		writeExpression(JRXmlConstants.ELEMENT_categoryAxisLabelExpression, plot.getCategoryAxisLabelExpression(), false);
		writeCategoryAxisFormat(plot.getCategoryAxisLabelFont(), plot.getOwnCategoryAxisLabelColor(),
				plot.getCategoryAxisTickLabelFont(), plot.getOwnCategoryAxisTickLabelColor(),
				plot.getCategoryAxisTickLabelMask(), plot.getCategoryAxisVerticalTickLabels(), 
				plot.getCategoryAxisTickLabelRotation(), plot.getOwnCategoryAxisLineColor());
		writeExpression(JRXmlConstants.ELEMENT_valueAxisLabelExpression, plot.getValueAxisLabelExpression(), false);
		writeAxisFormat(JRXmlConstants.ELEMENT_valueAxisFormat, plot.getValueAxisLabelFont(), plot.getOwnValueAxisLabelColor(),
				plot.getValueAxisTickLabelFont(), plot.getOwnValueAxisTickLabelColor(),
				plot.getValueAxisTickLabelMask(), plot.getValueAxisVerticalTickLabels(), plot.getOwnValueAxisLineColor());
		if(isNewerVersionOrEqual(JRConstants.VERSION_3_5_0))
		{
			writeExpression(JRXmlConstants.ELEMENT_domainAxisMinValueExpression, plot.getDomainAxisMinValueExpression(), false);
			writeExpression(JRXmlConstants.ELEMENT_domainAxisMaxValueExpression, plot.getDomainAxisMaxValueExpression(), false);
			writeExpression(JRXmlConstants.ELEMENT_rangeAxisMinValueExpression, plot.getRangeAxisMinValueExpression(), false);
			writeExpression(JRXmlConstants.ELEMENT_rangeAxisMaxValueExpression, plot.getRangeAxisMaxValueExpression(), false);
		}
		
		writer.closeElement();
	}


	
	private void writeTimeSeriesPlot(JRTimeSeriesPlot plot) throws IOException
	{
		writer.startElement(JRXmlConstants.ELEMENT_timeSeriesPlot);
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_isShowLines, plot.getShowLines());
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_isShowShapes, plot.getShowShapes());

		writePlot( plot );

		writeExpression(JRXmlConstants.ELEMENT_timeAxisLabelExpression, plot.getTimeAxisLabelExpression(), false);
		writeAxisFormat(JRXmlConstants.ELEMENT_timeAxisFormat, plot.getTimeAxisLabelFont(), plot.getOwnTimeAxisLabelColor(),
				plot.getTimeAxisTickLabelFont(), plot.getOwnTimeAxisTickLabelColor(),
				plot.getTimeAxisTickLabelMask(), plot.getTimeAxisVerticalTickLabels(), plot.getOwnTimeAxisLineColor());
		writeExpression(JRXmlConstants.ELEMENT_valueAxisLabelExpression, plot.getValueAxisLabelExpression(), false);
		writeAxisFormat(JRXmlConstants.ELEMENT_valueAxisFormat, plot.getValueAxisLabelFont(), plot.getOwnValueAxisLabelColor(),
				plot.getValueAxisTickLabelFont(), plot.getOwnValueAxisTickLabelColor(),
				plot.getValueAxisTickLabelMask(), plot.getValueAxisVerticalTickLabels(), plot.getOwnValueAxisLineColor());
		if(isNewerVersionOrEqual(JRConstants.VERSION_3_5_0))
		{
			writeExpression(JRXmlConstants.ELEMENT_domainAxisMinValueExpression, plot.getDomainAxisMinValueExpression(), false);
			writeExpression(JRXmlConstants.ELEMENT_domainAxisMaxValueExpression, plot.getDomainAxisMaxValueExpression(), false);
			writeExpression(JRXmlConstants.ELEMENT_rangeAxisMinValueExpression, plot.getRangeAxisMinValueExpression(), false);
			writeExpression(JRXmlConstants.ELEMENT_rangeAxisMaxValueExpression, plot.getRangeAxisMaxValueExpression(), false);
		}

		writer.closeElement();
	}


	/**
	 *
	 */
	
	public void writeBar3DPlot(JRBar3DPlot plot) throws IOException
	{
		writer.startElement(JRXmlConstants.ELEMENT_bar3DPlot, getNamespace());
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_isShowLabels, plot.getShowLabels());
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_xOffset, plot.getXOffsetDouble());
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_yOffset, plot.getYOffsetDouble());
		writePlot(plot);
		writeItemLabel(plot.getItemLabel());
		
		writeExpression(JRXmlConstants.ELEMENT_categoryAxisLabelExpression, plot.getCategoryAxisLabelExpression(), false);
		writeCategoryAxisFormat(plot.getCategoryAxisLabelFont(), plot.getOwnCategoryAxisLabelColor(),
				plot.getCategoryAxisTickLabelFont(), plot.getOwnCategoryAxisTickLabelColor(),
				plot.getCategoryAxisTickLabelMask(), plot.getCategoryAxisVerticalTickLabels(), 
				plot.getCategoryAxisTickLabelRotation(), plot.getOwnCategoryAxisLineColor());
		writeExpression(JRXmlConstants.ELEMENT_valueAxisLabelExpression, plot.getValueAxisLabelExpression(), false);
		writeAxisFormat(JRXmlConstants.ELEMENT_valueAxisFormat, plot.getValueAxisLabelFont(), plot.getOwnValueAxisLabelColor(),
				plot.getValueAxisTickLabelFont(), plot.getOwnValueAxisTickLabelColor(),
				plot.getValueAxisTickLabelMask(), plot.getValueAxisVerticalTickLabels(), plot.getOwnValueAxisLineColor());
		if(isNewerVersionOrEqual(JRConstants.VERSION_3_5_0))
		{
			writeExpression(JRXmlConstants.ELEMENT_domainAxisMinValueExpression, plot.getDomainAxisMinValueExpression(), false);
			writeExpression(JRXmlConstants.ELEMENT_domainAxisMaxValueExpression, plot.getDomainAxisMaxValueExpression(), false);
			writeExpression(JRXmlConstants.ELEMENT_rangeAxisMinValueExpression, plot.getRangeAxisMinValueExpression(), false);
			writeExpression(JRXmlConstants.ELEMENT_rangeAxisMaxValueExpression, plot.getRangeAxisMaxValueExpression(), false);
		}
		
		writer.closeElement();
	}


	/**
	 *
	 */
	public void writeBarChart(JRChart chart) throws IOException
	{
		writer.startElement(JRXmlConstants.ELEMENT_barChart, getNamespace());

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
		writer.startElement(JRXmlConstants.ELEMENT_bar3DChart, getNamespace());

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
		writer.startElement(JRXmlConstants.ELEMENT_bubbleChart, getNamespace());
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
		writer.startElement(JRXmlConstants.ELEMENT_stackedBarChart, getNamespace());

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
		writer.startElement(JRXmlConstants.ELEMENT_stackedBar3DChart, getNamespace());

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
		writer.startElement(JRXmlConstants.ELEMENT_lineChart, getNamespace());

		writeChart(chart);
		writeCategoryDataSet((JRCategoryDataset) chart.getDataset());
		writeLinePlot((JRLinePlot) chart.getPlot());
		writer.closeElement();
	}


	public void writeTimeSeriesChart(JRChart chart) throws IOException
	{
		writer.startElement(JRXmlConstants.ELEMENT_timeSeriesChart, getNamespace());
		writeChart(chart);
		writeTimeSeriesDataset((JRTimeSeriesDataset)chart.getDataset());
		writeTimeSeriesPlot((JRTimeSeriesPlot)chart.getPlot());
		writer.closeElement();
	}

	
	public void writeHighLowDataset(JRHighLowDataset dataset) throws IOException
	{
		writer.startElement(JRXmlConstants.ELEMENT_highLowDataset, getNamespace());

		writeElementDataset(dataset);

		writeExpression(JRXmlConstants.ELEMENT_seriesExpression, dataset.getSeriesExpression(), false);
		writeExpression(JRXmlConstants.ELEMENT_dateExpression, dataset.getDateExpression(), false);
		writeExpression(JRXmlConstants.ELEMENT_highExpression, dataset.getHighExpression(), false);
		writeExpression(JRXmlConstants.ELEMENT_lowExpression, dataset.getLowExpression(), false);
		writeExpression(JRXmlConstants.ELEMENT_openExpression, dataset.getOpenExpression(), false);
		writeExpression(JRXmlConstants.ELEMENT_closeExpression, dataset.getCloseExpression(), false);
		writeExpression(JRXmlConstants.ELEMENT_volumeExpression, dataset.getVolumeExpression(), false);
		writeHyperlink(JRXmlConstants.ELEMENT_itemHyperlink, dataset.getItemHyperlink());

		writer.closeElement();
	}


	
	public void writeHighLowChart(JRChart chart) throws IOException
	{
		writer.startElement(JRXmlConstants.ELEMENT_highLowChart, getNamespace());

		writeChart(chart);
		writeHighLowDataset((JRHighLowDataset) chart.getDataset());

		JRHighLowPlot plot = (JRHighLowPlot) chart.getPlot();
		writer.startElement(JRXmlConstants.ELEMENT_highLowPlot);
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_isShowOpenTicks, plot.getShowOpenTicks());
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_isShowCloseTicks, plot.getShowCloseTicks());

		writePlot(plot);

		writeExpression(JRXmlConstants.ELEMENT_timeAxisLabelExpression, plot.getTimeAxisLabelExpression(), false);
		writeAxisFormat(JRXmlConstants.ELEMENT_timeAxisFormat, plot.getTimeAxisLabelFont(), plot.getOwnTimeAxisLabelColor(),
				plot.getTimeAxisTickLabelFont(), plot.getOwnTimeAxisTickLabelColor(),
				plot.getTimeAxisTickLabelMask(), plot.getTimeAxisVerticalTickLabels(), plot.getOwnTimeAxisLineColor());
		writeExpression(JRXmlConstants.ELEMENT_valueAxisLabelExpression, plot.getValueAxisLabelExpression(), false);
		writeAxisFormat(JRXmlConstants.ELEMENT_valueAxisFormat, plot.getValueAxisLabelFont(), plot.getOwnValueAxisLabelColor(),
				plot.getValueAxisTickLabelFont(), plot.getOwnValueAxisTickLabelColor(),
				plot.getValueAxisTickLabelMask(), plot.getValueAxisVerticalTickLabels(), plot.getOwnValueAxisLineColor());
		if(isNewerVersionOrEqual(JRConstants.VERSION_3_5_0))
		{
			writeExpression(JRXmlConstants.ELEMENT_domainAxisMinValueExpression, plot.getDomainAxisMinValueExpression(), false);
			writeExpression(JRXmlConstants.ELEMENT_domainAxisMaxValueExpression, plot.getDomainAxisMaxValueExpression(), false);
			writeExpression(JRXmlConstants.ELEMENT_rangeAxisMinValueExpression, plot.getRangeAxisMinValueExpression(), false);
			writeExpression(JRXmlConstants.ELEMENT_rangeAxisMaxValueExpression, plot.getRangeAxisMaxValueExpression(), false);
		}

		writer.closeElement();
		writer.closeElement();
	}


	/**
	 * 
	 */
	public void writeGanttChart(JRChart chart) throws IOException
	{
		writer.startElement(JRXmlConstants.ELEMENT_ganttChart, getNamespace());
		
		writeChart(chart);
		writeGanttDataset((JRGanttDataset) chart.getDataset());
		writeBarPlot((JRBarPlot) chart.getPlot());
		
		writer.closeElement();
	}


	
	public void writeCandlestickChart(JRChart chart) throws IOException
	{
		writer.startElement(JRXmlConstants.ELEMENT_candlestickChart, getNamespace());

		writeChart(chart);
		writeHighLowDataset((JRHighLowDataset) chart.getDataset());

		JRCandlestickPlot plot = (JRCandlestickPlot) chart.getPlot();
		writer.startElement(JRXmlConstants.ELEMENT_candlestickPlot);
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_isShowVolume, plot.getShowVolume());

		writePlot(plot);

		writeExpression(JRXmlConstants.ELEMENT_timeAxisLabelExpression, plot.getTimeAxisLabelExpression(), false);
		writeAxisFormat(JRXmlConstants.ELEMENT_timeAxisFormat, plot.getTimeAxisLabelFont(), plot.getOwnTimeAxisLabelColor(),
				plot.getTimeAxisTickLabelFont(), plot.getOwnTimeAxisTickLabelColor(),
				plot.getTimeAxisTickLabelMask(), plot.getTimeAxisVerticalTickLabels(), plot.getOwnTimeAxisLineColor());
		writeExpression(JRXmlConstants.ELEMENT_valueAxisLabelExpression, plot.getValueAxisLabelExpression(), false);
		writeAxisFormat(JRXmlConstants.ELEMENT_valueAxisFormat, plot.getValueAxisLabelFont(), plot.getOwnValueAxisLabelColor(),
				plot.getValueAxisTickLabelFont(), plot.getOwnValueAxisTickLabelColor(),
				plot.getValueAxisTickLabelMask(), plot.getValueAxisVerticalTickLabels(), plot.getOwnValueAxisLineColor());
		if(isNewerVersionOrEqual(JRConstants.VERSION_3_5_0))
		{
			writeExpression(JRXmlConstants.ELEMENT_domainAxisMinValueExpression, plot.getDomainAxisMinValueExpression(), false);
			writeExpression(JRXmlConstants.ELEMENT_domainAxisMaxValueExpression, plot.getDomainAxisMaxValueExpression(), false);
			writeExpression(JRXmlConstants.ELEMENT_rangeAxisMinValueExpression, plot.getRangeAxisMinValueExpression(), false);
			writeExpression(JRXmlConstants.ELEMENT_rangeAxisMaxValueExpression, plot.getRangeAxisMaxValueExpression(), false);
		}

		writer.closeElement();
		writer.closeElement();
	}

	/**
	 *
	 */
	
	private void writeAreaPlot(JRAreaPlot plot) throws IOException
	{
		writer.startElement(JRXmlConstants.ELEMENT_areaPlot);
		writePlot(plot);

		writeExpression(JRXmlConstants.ELEMENT_categoryAxisLabelExpression, plot.getCategoryAxisLabelExpression(), false);
		writeCategoryAxisFormat(plot.getCategoryAxisLabelFont(), plot.getOwnCategoryAxisLabelColor(),
				plot.getCategoryAxisTickLabelFont(), plot.getOwnCategoryAxisTickLabelColor(),
				plot.getCategoryAxisTickLabelMask(), plot.getCategoryAxisVerticalTickLabels(), 
				plot.getCategoryAxisTickLabelRotation(), plot.getOwnCategoryAxisLineColor());
		writeExpression(JRXmlConstants.ELEMENT_valueAxisLabelExpression, plot.getValueAxisLabelExpression(), false);
		writeAxisFormat(JRXmlConstants.ELEMENT_valueAxisFormat, plot.getValueAxisLabelFont(), plot.getOwnValueAxisLabelColor(),
				plot.getValueAxisTickLabelFont(), plot.getOwnValueAxisTickLabelColor(),
				plot.getValueAxisTickLabelMask(), plot.getValueAxisVerticalTickLabels(), plot.getOwnValueAxisLineColor());
		if(isNewerVersionOrEqual(JRConstants.VERSION_3_5_0))
		{
			writeExpression(JRXmlConstants.ELEMENT_domainAxisMinValueExpression, plot.getDomainAxisMinValueExpression(), false);
			writeExpression(JRXmlConstants.ELEMENT_domainAxisMaxValueExpression, plot.getDomainAxisMaxValueExpression(), false);
			writeExpression(JRXmlConstants.ELEMENT_rangeAxisMinValueExpression, plot.getRangeAxisMinValueExpression(), false);
			writeExpression(JRXmlConstants.ELEMENT_rangeAxisMaxValueExpression, plot.getRangeAxisMaxValueExpression(), false);
		}

		writer.closeElement();
	}


	/**
	 *
	 */
	public void writeAreaChart(JRChart chart) throws IOException
	{
		writer.startElement(JRXmlConstants.ELEMENT_areaChart, getNamespace());

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
		writer.startElement(JRXmlConstants.ELEMENT_scatterPlot);
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_isShowLines, plot.getShowLines());
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_isShowShapes, plot.getShowShapes());

		writePlot(plot);

		writeExpression(JRXmlConstants.ELEMENT_xAxisLabelExpression, plot.getXAxisLabelExpression(), false);
		writeAxisFormat(JRXmlConstants.ELEMENT_xAxisFormat, plot.getXAxisLabelFont(), plot.getOwnXAxisLabelColor(),
				plot.getXAxisTickLabelFont(), plot.getOwnXAxisTickLabelColor(),
				plot.getXAxisTickLabelMask(), plot.getXAxisVerticalTickLabels(), plot.getOwnXAxisLineColor());
		writeExpression(JRXmlConstants.ELEMENT_yAxisLabelExpression, plot.getYAxisLabelExpression(), false);
		writeAxisFormat(JRXmlConstants.ELEMENT_yAxisFormat, plot.getYAxisLabelFont(), plot.getOwnYAxisLabelColor(),
				plot.getYAxisTickLabelFont(), plot.getOwnYAxisTickLabelColor(),
				plot.getYAxisTickLabelMask(), plot.getYAxisVerticalTickLabels(), plot.getOwnYAxisLineColor());
		if(isNewerVersionOrEqual(JRConstants.VERSION_3_5_0))
		{
			writeExpression(JRXmlConstants.ELEMENT_domainAxisMinValueExpression, plot.getDomainAxisMinValueExpression(), false);
			writeExpression(JRXmlConstants.ELEMENT_domainAxisMaxValueExpression, plot.getDomainAxisMaxValueExpression(), false);
			writeExpression(JRXmlConstants.ELEMENT_rangeAxisMinValueExpression, plot.getRangeAxisMinValueExpression(), false);
			writeExpression(JRXmlConstants.ELEMENT_rangeAxisMaxValueExpression, plot.getRangeAxisMaxValueExpression(), false);
		}
		
		writer.closeElement();
	}


	/**
	 *
	 */
	public void writeScatterChart(JRChart chart) throws IOException
	{
		writer.startElement(JRXmlConstants.ELEMENT_scatterChart, getNamespace());

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
		writer.startElement(JRXmlConstants.ELEMENT_xyAreaChart, getNamespace());

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
		writer.startElement(JRXmlConstants.ELEMENT_xyBarChart, getNamespace());

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
		writer.startElement(JRXmlConstants.ELEMENT_xyLineChart, getNamespace());

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
		writer.startElement(JRXmlConstants.ELEMENT_meterChart, getNamespace());

		writeChart(chart);
		writeValueDataset((JRValueDataset) chart.getDataset());

		// write plot
		JRMeterPlot plot = (JRMeterPlot) chart.getPlot();
		writer.startElement(JRMeterPlotFactory.ELEMENT_meterPlot);
		writer.addAttribute(JRMeterPlotFactory.ATTRIBUTE_shape, plot.getShapeValue());
		writer.addAttribute(JRMeterPlotFactory.ATTRIBUTE_angle, plot.getMeterAngleInteger());
		writer.addAttribute(JRMeterPlotFactory.ATTRIBUTE_units, plot.getUnits());
		writer.addAttribute(JRMeterPlotFactory.ATTRIBUTE_tickInterval, plot.getTickIntervalDouble());
		writer.addAttribute(JRMeterPlotFactory.ATTRIBUTE_meterColor, plot.getMeterBackgroundColor());
		writer.addAttribute(JRMeterPlotFactory.ATTRIBUTE_needleColor, plot.getNeedleColor());
		writer.addAttribute(JRMeterPlotFactory.ATTRIBUTE_tickColor, plot.getTickColor());
		if(isNewerVersionOrEqual(JRConstants.VERSION_4_6_0))
		{
			writer.addAttribute(JRMeterPlotFactory.ATTRIBUTE_tickCount, plot.getTickCount());
		}
		writePlot(chart.getPlot());
		if (isNewerVersionOrEqual(JRConstants.VERSION_3_5_0) && plot.getTickLabelFont() != null)
		{
			writer.startElement(JRXmlConstants.ELEMENT_tickLabelFont);
			writeFont(plot.getTickLabelFont());
			writer.closeElement();
		}
		writeValueDisplay(plot.getValueDisplay());
		writeDataRange(plot.getDataRange());

		List<JRMeterInterval> intervals = plot.getIntervals();
		if (intervals != null)
		{
			Iterator<JRMeterInterval> iter = intervals.iterator();
			while (iter.hasNext())
			{
				JRMeterInterval meterInterval = iter.next();
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
		writer.startElement(JRXmlConstants.ELEMENT_thermometerChart, getNamespace());

		writeChart(chart);
		writeValueDataset((JRValueDataset) chart.getDataset());

		// write plot
		JRThermometerPlot plot = (JRThermometerPlot) chart.getPlot();

		writer.startElement(JRThermometerPlotFactory.ELEMENT_thermometerPlot, getNamespace());

		writer.addAttribute(JRThermometerPlotFactory.ATTRIBUTE_valueLocation, plot.getValueLocationValue());
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
		writer.startElement(JRXmlConstants.ELEMENT_multiAxisChart, getNamespace());

		writeChart(chart);

		// write plot
		JRMultiAxisPlot plot = (JRMultiAxisPlot) chart.getPlot();
		writer.startElement(JRXmlConstants.ELEMENT_multiAxisPlot);

		writePlot(chart.getPlot());

		List<JRChartAxis> axes = plot.getAxes();
		if (axes != null)
		{
			Iterator<JRChartAxis> iter = axes.iterator();
			while (iter.hasNext())
			{
				JRChartAxis chartAxis = iter.next();
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
		writer.startElement(JRXmlConstants.ELEMENT_stackedAreaChart, getNamespace());

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
				throw 
				new JRRuntimeException(
						EXCEPTION_MESSAGE_KEY_UNSUPPORTED_CHART_TYPE,
						(Object[])null);
		}
	}


	public void writeSubreportReturnValue(JRSubreportReturnValue returnValue, XmlNamespace namespace) throws IOException
	{
		writer.startElement(JRXmlConstants.ELEMENT_returnValue, namespace);
		writer.addEncodedAttribute(JRXmlConstants.ATTRIBUTE_subreportVariable, returnValue.getFromVariable());
		writer.addEncodedAttribute(JRXmlConstants.ATTRIBUTE_toVariable, returnValue.getToVariable());
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_calculation, returnValue.getCalculation(), CalculationEnum.NOTHING);
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_incrementerFactoryClass, returnValue.getIncrementerFactoryClassName());
		writer.closeElement();
	}

	
	protected void writeReturnValue(VariableReturnValue returnValue) throws IOException
	{
		writer.startElement(JRXmlConstants.ELEMENT_returnValue);
		writer.addEncodedAttribute(JRXmlConstants.ATTRIBUTE_fromVariable, returnValue.getFromVariable());
		writer.addEncodedAttribute(JRXmlConstants.ATTRIBUTE_toVariable, returnValue.getToVariable());
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_calculation, returnValue.getCalculation(), CalculationEnum.NOTHING);
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_incrementerFactoryClass, returnValue.getIncrementerFactoryClassName());
		writer.closeElement();
	}


	protected void writeReturnValue(ExpressionReturnValue returnValue) throws IOException
	{
		writer.startElement(JRXmlConstants.ELEMENT_returnValue);
		writer.addEncodedAttribute(JRXmlConstants.ATTRIBUTE_toVariable, returnValue.getToVariable());
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_calculation, returnValue.getCalculation(), CalculationEnum.NOTHING);
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_incrementerFactoryClass, returnValue.getIncrementerFactoryClassName());
		writeExpression(JRXmlConstants.ELEMENT_expression, returnValue.getExpression(), false);
		writer.closeElement();
	}

	
	public void writeCrosstab(JRCrosstab crosstab) throws IOException
	{
		writer.startElement(JRCrosstabFactory.ELEMENT_crosstab, getNamespace());
		writer.addAttribute(JRCrosstabFactory.ATTRIBUTE_isRepeatColumnHeaders, crosstab.isRepeatColumnHeaders(), true);
		writer.addAttribute(JRCrosstabFactory.ATTRIBUTE_isRepeatRowHeaders, crosstab.isRepeatRowHeaders(), true);
		writer.addAttribute(JRCrosstabFactory.ATTRIBUTE_columnBreakOffset, crosstab.getColumnBreakOffset(), JRCrosstab.DEFAULT_COLUMN_BREAK_OFFSET);
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_runDirection, crosstab.getRunDirectionValue(), RunDirectionEnum.LTR);
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_horizontalPosition, crosstab.getHorizontalPosition());
		if(isNewerVersionOrEqual(JRConstants.VERSION_3_5_3))
		{
			writer.addAttribute(JRCrosstabFactory.ATTRIBUTE_ignoreWidth, crosstab.getIgnoreWidth());
		}
		writeReportElement(crosstab);
		if(isNewerVersionOrEqual(JRConstants.VERSION_4_5_0))
		{
			writeBox(crosstab.getLineBox());
		}

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

		writeExpression(JRCrosstabFactory.ELEMENT_parametersMapExpression, crosstab.getParametersMapExpression(), false);

		writeCrosstabDataset(crosstab);

		writeCrosstabTitle(crosstab);
		
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
			List<JRCrosstabCell> cellsList = ((JRDesignCrosstab) crosstab).getCellsList();
			for (Iterator<JRCrosstabCell> it = cellsList.iterator(); it.hasNext();)
			{
				JRCrosstabCell cell = it.next();
				writeCrosstabCell(cell);
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


	private void writeCrosstabTitle(JRCrosstab crosstab) throws IOException
	{
		CrosstabColumnCell titleCell = crosstab.getTitleCell();
		if (titleCell != null)
		{
			writer.startElement(JRCrosstabFactory.ELEMENT_titleCell);
			writer.addAttribute(JRXmlConstants.ATTRIBUTE_height, titleCell.getHeight());
			writer.addAttribute(JRXmlConstants.ATTRIBUTE_contentsPosition, titleCell.getContentsPosition(), CrosstabColumnPositionEnum.LEFT);
			
			writeCellContents(titleCell.getCellContents());
			
			writer.closeElement();
		}
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
		writer.addAttribute(JRCrosstabGroupFactory.ATTRIBUTE_totalPosition, group.getTotalPositionValue(), CrosstabTotalPositionEnum.NONE);
		writer.addAttribute(JRCrosstabRowGroupFactory.ATTRIBUTE_headerPosition, group.getPositionValue(), CrosstabRowPositionEnum.TOP);
		writer.addAttribute(JRCrosstabGroupFactory.ATTRIBUTE_mergeHeaderCells, group.getMergeHeaderCells());

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
		writer.addAttribute(JRCrosstabGroupFactory.ATTRIBUTE_totalPosition, group.getTotalPositionValue(), CrosstabTotalPositionEnum.NONE);
		writer.addAttribute(JRCrosstabColumnGroupFactory.ATTRIBUTE_headerPosition, group.getPositionValue(), CrosstabColumnPositionEnum.LEFT);
		writer.addAttribute(JRCrosstabGroupFactory.ATTRIBUTE_mergeHeaderCells, group.getMergeHeaderCells());

		writeBucket(group.getBucket());

		JRCellContents crosstabHeader = group.getCrosstabHeader();
		if (crosstabHeader != null)
		{
			writer.startElement(JRCrosstabColumnGroupFactory.ELEMENT_crosstabHeader);
			writeCellContents(crosstabHeader);
			writer.closeElement();
		}
		
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
		writer.addAttribute(JRCrosstabBucketFactory.ATTRIBUTE_order, bucket.getOrder(), BucketOrder.ASCENDING);
		if(isNewerVersionOrEqual(JRConstants.VERSION_4_1_1))
		{
			writer.addAttribute(JRCrosstabMeasureFactory.ATTRIBUTE_class, bucket.getValueClassName());
		}
		writeExpression(JRCrosstabBucketFactory.ELEMENT_bucketExpression, bucket.getExpression(), true);
		if(isNewerVersionOrEqual(JRConstants.VERSION_3_6_2))
		{
			writeExpression(JRCrosstabBucketFactory.ELEMENT_orderByExpression, 
					bucket.getOrderByExpression(), true, Object.class.getName());
		}
		writeExpression(JRCrosstabBucketFactory.ELEMENT_comparatorExpression, bucket.getComparatorExpression(), false);

		writer.closeElement();
	}


	
	protected void writeCrosstabMeasure(JRCrosstabMeasure measure) throws IOException
	{
		writer.startElement(JRCrosstabMeasureFactory.ELEMENT_measure);
		writer.addEncodedAttribute(JRCrosstabMeasureFactory.ATTRIBUTE_name, measure.getName());
		writer.addAttribute(JRCrosstabMeasureFactory.ATTRIBUTE_class, measure.getValueClassName());
		writer.addAttribute(JRCrosstabMeasureFactory.ATTRIBUTE_calculation, measure.getCalculationValue(), CalculationEnum.NOTHING);
		writer.addAttribute(JRCrosstabMeasureFactory.ATTRIBUTE_incrementerFactoryClass, measure.getIncrementerFactoryClassName());
		writer.addAttribute(JRCrosstabMeasureFactory.ATTRIBUTE_percentageOf, measure.getPercentageType(), CrosstabPercentageEnum.NONE);
		writer.addAttribute(JRCrosstabMeasureFactory.ATTRIBUTE_percentageCalculatorClass, measure.getPercentageCalculatorClassName());
		writeExpression(JRCrosstabMeasureFactory.ELEMENT_measureExpression, measure.getValueExpression(), false);

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

			if(isNewerVersionOrEqual(JRConstants.VERSION_4_8_0))
			{
				writeProperties(contents);
			}
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
		writeExpression(JRCrosstabParameterFactory.ELEMENT_parameterValueExpression, parameter.getExpression(), false);

		writer.closeElement();
	}


	public void writeDataset(JRDataset dataset) throws IOException
	{
		writer.startElement(JRXmlConstants.ELEMENT_subDataset, getNamespace());
		writer.addEncodedAttribute(JRXmlConstants.ATTRIBUTE_name, dataset.getName());
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_scriptletClass, dataset.getScriptletClass());
		writer.addEncodedAttribute(JRXmlConstants.ATTRIBUTE_resourceBundle, dataset.getResourceBundle());
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_whenResourceMissingType, dataset.getWhenResourceMissingTypeValue(), WhenResourceMissingTypeEnum.NULL);
		if (isNewerVersionOrEqual(JRConstants.VERSION_4_6_0))
		{
			writer.addAttribute(JRXmlConstants.ATTRIBUTE_uuid, dataset.getUUID().toString());
		}

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

		writeExpression(JRXmlConstants.ELEMENT_filterExpression, dataset.getFilterExpression(), false);

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
		writer.startElement(JRXmlConstants.ELEMENT_datasetRun, getNamespace());
		writer.addEncodedAttribute(JRXmlConstants.ATTRIBUTE_subDataset, datasetRun.getDatasetName());
		if (isNewerVersionOrEqual(JRConstants.VERSION_4_6_0))
		{
			writer.addEncodedAttribute(JRXmlConstants.ATTRIBUTE_uuid, datasetRun.getUUID().toString());
			writeProperties(datasetRun);
		}

		writeExpression(JRXmlConstants.ELEMENT_parametersMapExpression, datasetRun.getParametersMapExpression(), false);

		/*   */
		JRDatasetParameter[] parameters = datasetRun.getParameters();
		if (parameters != null && parameters.length > 0)
		{
			for(int i = 0; i < parameters.length; i++)
			{
				writeDatasetParameter(parameters[i]);
			}
		}

		writeExpression(JRXmlConstants.ELEMENT_connectionExpression, datasetRun.getConnectionExpression(), false);
		writeExpression(JRXmlConstants.ELEMENT_dataSourceExpression, datasetRun.getDataSourceExpression(), false);

		List<ReturnValue> returnValues = datasetRun.getReturnValues();
		if (returnValues != null && !returnValues.isEmpty())
		{
			for (ReturnValue returnValue : returnValues)
			{
				writeReturnValue(returnValue);
			}
		}
		
		writer.closeElement();
	}


	public void writeFrame(JRFrame frame) throws IOException
	{
		writer.startElement(JRXmlConstants.ELEMENT_frame, getNamespace());
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_borderSplitType, frame.getBorderSplitType());

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
			writer.startElement(JRXmlConstants.ELEMENT_hyperlinkParameter, JASPERREPORTS_NAMESPACE);
			writer.addEncodedAttribute(JRXmlConstants.ATTRIBUTE_name, parameter.getName());

			writeExpression(JRXmlConstants.ELEMENT_hyperlinkParameterExpression,
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

			writer.addEncodedAttribute(JRXmlConstants.ATTRIBUTE_hyperlinkType, hyperlink.getLinkType(), HyperlinkTypeEnum.NONE.getName());
			writer.addEncodedAttribute(JRXmlConstants.ATTRIBUTE_hyperlinkTarget, hyperlink.getLinkTarget(), HyperlinkTargetEnum.SELF.getName());

			writeExpression(JRXmlConstants.ELEMENT_hyperlinkReferenceExpression, JASPERREPORTS_NAMESPACE,
					hyperlink.getHyperlinkReferenceExpression(), false);
			writeExpression(JRXmlConstants.ELEMENT_hyperlinkWhenExpression, JASPERREPORTS_NAMESPACE,
					hyperlink.getHyperlinkWhenExpression(), false);
			writeExpression(JRXmlConstants.ELEMENT_hyperlinkAnchorExpression, JASPERREPORTS_NAMESPACE,
					hyperlink.getHyperlinkAnchorExpression(), false);
			writeExpression(JRXmlConstants.ELEMENT_hyperlinkPageExpression, JASPERREPORTS_NAMESPACE,
					hyperlink.getHyperlinkPageExpression(), false);
			writeExpression(JRXmlConstants.ELEMENT_hyperlinkTooltipExpression, JASPERREPORTS_NAMESPACE,
					hyperlink.getHyperlinkTooltipExpression(), false);
			writeHyperlinkParameters(hyperlink.getHyperlinkParameters());
			
			if(isNewerVersionOrEqual(JRConstants.VERSION_3_5_1))
			{
				writer.closeElement(true);
			}
			else
			{
				writer.closeElement();
			}
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
		ComponentKey componentKey = componentElement.getComponentKey();
		ComponentXmlWriter componentXmlWriter = 
			ComponentsEnvironment.getInstance(jasperReportsContext).getManager(componentKey).getComponentXmlWriter(jasperReportsContext);
		
		if (componentXmlWriter.isToWrite(componentElement, this))
		{
			writer.startElement(JRXmlConstants.ELEMENT_componentElement, getNamespace());
			writeReportElement(componentElement);
			
			componentXmlWriter.writeToXml(componentElement, this);
			
			writer.closeElement();
		}
	}
	
	protected XmlNamespace getNamespace()
	{
		return JASPERREPORTS_NAMESPACE;
	}


	
	public void writeGenericElement(JRGenericElement element) throws IOException
	{
		if(isNewerVersionOrEqual(JRConstants.VERSION_3_1_0))
		{
			writer.startElement(JRXmlConstants.ELEMENT_genericElement, getNamespace());
			
			writer.addAttribute(JRXmlConstants.ATTRIBUTE_evaluationTime, 
					element.getEvaluationTimeValue(),
					EvaluationTimeEnum.NOW);
			if (element.getEvaluationGroupName() != null)
			{
				writer.addEncodedAttribute(JRXmlConstants.ATTRIBUTE_evaluationGroup, 
						element.getEvaluationGroupName());
			}
	
			writeReportElement(element);
			
			writer.startElement(JRXmlConstants.ELEMENT_genericElementType);
			JRGenericElementType printKey = element.getGenericType();
			if (printKey != null)
			{
				writer.addEncodedAttribute(JRXmlConstants.ATTRIBUTE_namespace, 
						printKey.getNamespace());
				writer.addEncodedAttribute(JRXmlConstants.ATTRIBUTE_name, 
						printKey.getName());
			}
			writer.closeElement();//genericElementType
	
			JRGenericElementParameter[] params = element.getParameters();
			for (int i = 0; i < params.length; i++)
			{
				JRGenericElementParameter param = params[i];
				writer.startElement(JRXmlConstants.ELEMENT_genericElementParameter);
				writer.addEncodedAttribute(JRXmlConstants.ATTRIBUTE_name, 
						param.getName());
				writer.addAttribute(JRXmlConstants.ATTRIBUTE_skipWhenNull, 
						param.isSkipWhenEmpty(), false);
				
				JRExpression valueExpression = param.getValueExpression();
				if (valueExpression != null)
				{
					writeExpression(JRXmlConstants.ELEMENT_genericElementParameter_valueExpression, 
							valueExpression, true, Object.class.getName());
				}
				
				writer.closeElement();//genericElementParameter
			}
			
			writer.closeElement();//genericElement
		}
	}
	
	public void writeMultiAxisData(MultiAxisData data) throws IOException
	{
		if(isNewerVersionOrEqual(JRConstants.VERSION_4_8_0))
		{
			writer.startElement(JRXmlConstants.ELEMENT_multiAxisData, getNamespace());
			
			writer.startElement(JRXmlConstants.ELEMENT_multiAxisDataset);
			writeElementDataset(data.getDataset());
			writer.closeElement();//JRXmlConstants.ELEMENT_multiAxisDataset
			
			for (DataAxis dataAxis : data.getDataAxisList())
			{
				writer.startElement(JRXmlConstants.ELEMENT_dataAxis);
				writer.addAttribute(JRXmlConstants.ATTRIBUTE_axis, dataAxis.getAxis());
				
				for (DataAxisLevel level : dataAxis.getLevels())
				{
					writer.startElement(JRXmlConstants.ELEMENT_axisLevel);
					writer.addEncodedAttribute(JRXmlConstants.ATTRIBUTE_name, level.getName());
					writer.writeExpression(JRXmlConstants.ELEMENT_labelExpression, level.getLabelExpression());
					writeDataLevelBucket(level.getBucket());
					writer.closeElement();//JRXmlConstants.ELEMENT_axisLevel
				}
				
				writer.closeElement();//JRXmlConstants.ELEMENT_dataAxis
			}
			
			for (DataMeasure measure : data.getMeasures())
			{
				writer.startElement(JRXmlConstants.ELEMENT_multiAxisMeasure);
				writer.addEncodedAttribute(JRXmlConstants.ATTRIBUTE_name, measure.getName());
				writer.addAttribute(JRXmlConstants.ATTRIBUTE_class, measure.getValueClassName());
				writer.addAttribute(JRXmlConstants.ATTRIBUTE_calculation, measure.getCalculation());
				writer.addAttribute(JRXmlConstants.ATTRIBUTE_incrementerFactoryClass, measure.getIncrementerFactoryClassName());
				writer.writeExpression(JRXmlConstants.ELEMENT_labelExpression, measure.getLabelExpression());
				writer.writeExpression(JRXmlConstants.ELEMENT_valueExpression, measure.getValueExpression());
				writer.closeElement();//JRXmlConstants.ELEMENT_multiAxisMeasure
			}
			
			writer.closeElement();//JRXmlConstants.ELEMENT_multiAxisData
		}
	}

	protected void writeDataLevelBucket(DataLevelBucket bucket) throws IOException
	{
		writer.startElement(JRXmlConstants.ELEMENT_axisLevelBucket);
		writer.addAttribute(JRCrosstabBucketFactory.ATTRIBUTE_order, bucket.getOrder(), BucketOrder.ASCENDING);
		writer.addAttribute(JRCrosstabMeasureFactory.ATTRIBUTE_class, bucket.getValueClassName());
		writer.writeExpression(JRCrosstabBucketFactory.ELEMENT_bucketExpression, bucket.getExpression());
		writer.writeExpression(JRCrosstabBucketFactory.ELEMENT_comparatorExpression, bucket.getComparatorExpression());
		
		List<DataLevelBucketProperty> bucketProperties = bucket.getBucketProperties();
		if (bucketProperties != null)
		{
			for (DataLevelBucketProperty bucketProperty : bucketProperties)
			{
				JRExpression valueExpression = bucketProperty.getExpression();
				String expressionText = valueExpression == null ? "" : valueExpression.getText();
				writer.writeCDATAElement(JRXmlConstants.ELEMENT_bucketProperty, getNamespace(), expressionText, 
						JRXmlConstants.ATTRIBUTE_name, bucketProperty.getName());
			}
		}
		
		writer.closeElement();//JRXmlConstants.ELEMENT_axisLevelBucket
	}
	
	@SuppressWarnings("deprecation")
	protected void writeExpression(String name, JRExpression expression, boolean writeClass, String defaultClassName) throws IOException
	{
		if(isNewerVersionOrEqual(JRConstants.VERSION_4_1_1))
		{
			writer.writeExpression(name, expression);
		}
		else
		{
			writer.writeExpression(name, expression, writeClass, defaultClassName);
			
		}
	}

	@SuppressWarnings("deprecation")
	protected void writeExpression(String name, XmlNamespace namespace, JRExpression expression, boolean writeClass)  throws IOException
	{
		if(isNewerVersionOrEqual(JRConstants.VERSION_4_1_1))
		{
			writer.writeExpression(name, namespace, expression);
		}
		else
		{
			writer.writeExpression(name, namespace, expression, writeClass);
		}
	}
	
	
}
