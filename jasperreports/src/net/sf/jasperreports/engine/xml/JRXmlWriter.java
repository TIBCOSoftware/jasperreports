/*
 * ============================================================================
 * GNU Lesser General Public License
 * ============================================================================
 *
 * JasperReports - Free Java report-generating library.
 * Copyright (C) 2001-2006 JasperSoft Corporation http://www.jaspersoft.com
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307, USA.
 * 
 * JasperSoft Corporation
 * 303 Second Street, Suite 450 North
 * San Francisco, CA 94107
 * http://www.jaspersoft.com
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
import net.sf.jasperreports.charts.JRHighLowDataset;
import net.sf.jasperreports.charts.JRHighLowPlot;
import net.sf.jasperreports.charts.JRLinePlot;
import net.sf.jasperreports.charts.JRMeterPlot;
import net.sf.jasperreports.charts.JRMultiAxisPlot;
import net.sf.jasperreports.charts.JRPie3DPlot;
import net.sf.jasperreports.charts.JRPieDataset;
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
import net.sf.jasperreports.crosstabs.xml.JRCrosstabRowGroupFactory;
import net.sf.jasperreports.engine.JRAnchor;
import net.sf.jasperreports.engine.JRBand;
import net.sf.jasperreports.engine.JRBox;
import net.sf.jasperreports.engine.JRBreak;
import net.sf.jasperreports.engine.JRChart;
import net.sf.jasperreports.engine.JRChartDataset;
import net.sf.jasperreports.engine.JRChartPlot;
import net.sf.jasperreports.engine.JRChild;
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
import net.sf.jasperreports.engine.JRGraphicElement;
import net.sf.jasperreports.engine.JRGroup;
import net.sf.jasperreports.engine.JRHyperlink;
import net.sf.jasperreports.engine.JRHyperlinkParameter;
import net.sf.jasperreports.engine.JRImage;
import net.sf.jasperreports.engine.JRLine;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JRPropertiesMap;
import net.sf.jasperreports.engine.JRQuery;
import net.sf.jasperreports.engine.JRRectangle;
import net.sf.jasperreports.engine.JRReport;
import net.sf.jasperreports.engine.JRReportFont;
import net.sf.jasperreports.engine.JRRuntimeException;
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
import net.sf.jasperreports.engine.query.JRJdbcQueryExecuterFactory;
import net.sf.jasperreports.engine.util.JRXmlWriteHelper;
import net.sf.jasperreports.engine.xml.JRChartPlotFactory.JRSeriesColorFactory;

import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer3D;
import org.jfree.data.time.Day;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @author Minor enhancements by Barry Klawans (bklawans@users.sourceforge.net)
 * @version $Id$
 */
public class JRXmlWriter
{


	/**
	 *
	 */
	private JRReport report = null;
	private String encoding = null;

	/**
	 *
	 */
	private JRXmlWriteHelper writer;
	private Map fontsMap = new HashMap();
	private Map stylesMap = new HashMap();


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
		writer = new JRXmlWriteHelper(out);
		
		writer.writeProlog(encoding);
		writer.writePublicDoctype("jasperReport", "-//JasperReports//DTD Report Design//EN", "http://jasperreports.sourceforge.net/dtds/jasperreport.dtd");

		writer.startElement(JasperDesignFactory.TAG_jasperReport);
		writer.addEncodedAttribute(JasperDesignFactory.ATTRIBUTE_name, report.getName());
		writer.addEncodedAttribute(JasperDesignFactory.ATTRIBUTE_language, report.getLanguage(), JRReport.LANGUAGE_JAVA);
		writer.addAttribute(JasperDesignFactory.ATTRIBUTE_columnCount, report.getColumnCount(), 1);
		writer.addAttribute(JasperDesignFactory.ATTRIBUTE_printOrder, report.getPrintOrder(), JRXmlConstants.getPrintOrderMap(), JRReport.PRINT_ORDER_VERTICAL);
		writer.addAttribute(JasperDesignFactory.ATTRIBUTE_pageWidth, report.getPageWidth());
		writer.addAttribute(JasperDesignFactory.ATTRIBUTE_pageHeight, report.getPageHeight());
		writer.addAttribute(JasperDesignFactory.ATTRIBUTE_orientation, report.getOrientation(), JRXmlConstants.getOrientationMap(), JRReport.ORIENTATION_PORTRAIT);
		writer.addAttribute(JasperDesignFactory.ATTRIBUTE_whenNoDataType, report.getWhenNoDataType(), JRXmlConstants.getWhenNoDataTypeMap(), JRReport.WHEN_NO_DATA_TYPE_NO_PAGES);
		writer.addAttribute(JasperDesignFactory.ATTRIBUTE_columnWidth, report.getColumnWidth());
		writer.addAttribute(JasperDesignFactory.ATTRIBUTE_columnSpacing, report.getColumnSpacing(), 0);
		writer.addAttribute(JasperDesignFactory.ATTRIBUTE_leftMargin, report.getLeftMargin());
		writer.addAttribute(JasperDesignFactory.ATTRIBUTE_rightMargin, report.getRightMargin());
		writer.addAttribute(JasperDesignFactory.ATTRIBUTE_topMargin, report.getTopMargin());
		writer.addAttribute(JasperDesignFactory.ATTRIBUTE_bottomMargin, report.getBottomMargin());
		writer.addAttribute(JasperDesignFactory.ATTRIBUTE_isTitleNewPage, report.isTitleNewPage(), false);
		writer.addAttribute(JasperDesignFactory.ATTRIBUTE_isSummaryNewPage, report.isSummaryNewPage(), false);
		writer.addAttribute(JasperDesignFactory.ATTRIBUTE_isFloatColumnFooter, report.isFloatColumnFooter(), false);
		writer.addAttribute(JasperDesignFactory.ATTRIBUTE_scriptletClass, report.getScriptletClass());
		writer.addAttribute(JasperDesignFactory.ATTRIBUTE_formatFactoryClass, report.getFormatFactoryClass());
		writer.addEncodedAttribute(JasperDesignFactory.ATTRIBUTE_resourceBundle, report.getResourceBundle());
		writer.addAttribute(JasperDesignFactory.ATTRIBUTE_whenResourceMissingType, report.getWhenResourceMissingType(), JRXmlConstants.getWhenResourceMissingTypeMap(), JRReport.WHEN_RESOURCE_MISSING_TYPE_NULL);
		writer.addAttribute(JasperDesignFactory.ATTRIBUTE_isIgnorePagination, report.isIgnorePagination(), false);
		
		writeProperties(report.getPropertiesMap());

		/*   */
		String[] imports = report.getImports();
		if (imports != null && imports.length > 0)
		{
			for(int i = 0; i < imports.length; i++)
			{
				String value = imports[i];
				if (value != null)
				{
					writer.startElement("import");
					writer.addEncodedAttribute("value", value);
					writer.closeElement();
				}
			}
		}

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
				stylesMap.put(styles[i].getName(), styles[i]);
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
			writer.startElement("background");
			writeBand(report.getBackground());
			writer.closeElement();
		}

		if (report.getTitle() != null)
		{
			writer.startElement("title");
			writeBand(report.getTitle());
			writer.closeElement();
		}

		if (report.getPageHeader() != null)
		{
			writer.startElement("pageHeader");
			writeBand(report.getPageHeader());
			writer.closeElement();
		}

		if (report.getColumnHeader() != null)
		{
			writer.startElement("columnHeader");
			writeBand(report.getColumnHeader());
			writer.closeElement();
		}

		if (report.getDetail() != null)
		{
			writer.startElement("detail");
			writeBand(report.getDetail());
			writer.closeElement();
		}

		if (report.getColumnFooter() != null)
		{
			writer.startElement("columnFooter");
			writeBand(report.getColumnFooter());
			writer.closeElement();
		}

		if (report.getPageFooter() != null)
		{
			writer.startElement("pageFooter");
			writeBand(report.getPageFooter());
			writer.closeElement();
		}

		if (report.getLastPageFooter() != null)
		{
			writer.startElement("lastPageFooter");
			writeBand(report.getLastPageFooter());
			writer.closeElement();
		}

		if (report.getSummary() != null)
		{
			writer.startElement("summary");
			writeBand(report.getSummary());
			writer.closeElement();
		}

		writer.closeElement();
		
		out.flush();
	}


	private void writeProperties(JRPropertiesMap propertiesMap) throws IOException
	{
		String[] propertyNames = propertiesMap.getPropertyNames();
		if (propertyNames != null && propertyNames.length > 0)
		{
			for(int i = 0; i < propertyNames.length; i++)
			{
				String value = propertiesMap.getProperty(propertyNames[i]);
				if (value != null)
				{
					writer.startElement("property");
					writer.addEncodedAttribute("name", propertyNames[i]);
					writer.addEncodedAttribute("value", value);
					writer.closeElement();
				}
			}
		}
	}


	/**
	 *
	 */
	private void writeReportFont(JRReportFont font) throws IOException
	{
		writer.startElement("reportFont");
		writer.addEncodedAttribute("name", font.getName());
		writer.addAttribute("isDefault", font.isDefault());
		writer.addEncodedAttribute("fontName", font.getFontName());
		writer.addAttribute("size", font.getFontSize());
		writer.addAttribute("isBold", font.isBold());
		writer.addAttribute("isItalic", font.isItalic());
		writer.addAttribute("isUnderline", font.isUnderline());
		writer.addAttribute("isStrikeThrough", font.isStrikeThrough());
		writer.addEncodedAttribute("pdfFontName", font.getPdfFontName());
		writer.addEncodedAttribute("pdfEncoding", font.getPdfEncoding());
		writer.addAttribute("isPdfEmbedded", font.isPdfEmbedded());
		writer.closeElement();
	}


	/**
	 *
	 */
	private void writeStyle(JRStyle style) throws IOException
	{
		writer.startElement("style");
		writer.addEncodedAttribute("name", style.getName());
		writer.addAttribute("isDefault", style.isDefault());

		if (style.getStyle() != null)
		{
			JRStyle baseStyle = 
				(JRStyle)stylesMap.get(
						style.getStyle().getName()
					);
			if(baseStyle != null)
			{
				writer.addEncodedAttribute("style", style.getStyle().getName());
			}
			else
			{
				throw 
					new JRRuntimeException(
						"Referenced report style not found : " 
						+ style.getStyle().getName()
						);
			}
		}
	
		writer.addAttribute("mode", style.getOwnMode(), JRXmlConstants.getModeMap());
		writer.addAttribute("forecolor", style.getOwnForecolor());
		writer.addAttribute("backcolor", style.getOwnBackcolor());
		writer.addAttribute("pen", style.getOwnPen(), JRXmlConstants.getPenMap());
		writer.addAttribute("fill", style.getOwnFill(), JRXmlConstants.getFillMap());
		writer.addAttribute("radius", style.getOwnRadius());
		writer.addAttribute("scaleImage", style.getOwnScaleImage(), JRXmlConstants.getScaleImageMap());
		writer.addAttribute("hAlign", style.getOwnHorizontalAlignment(), JRXmlConstants.getHorizontalAlignMap());
		writer.addAttribute("vAlign", style.getOwnVerticalAlignment(), JRXmlConstants.getVerticalAlignMap());
		writer.addAttribute("rotation", style.getOwnRotation(), JRXmlConstants.getRotationMap());
		writer.addAttribute("lineSpacing", style.getOwnLineSpacing(), JRXmlConstants.getLineSpacingMap());
		writer.addAttribute("isStyledText", style.isOwnStyledText());
		writer.addEncodedAttribute("pattern", style.getOwnPattern());
		writer.addAttribute("isBlankWhenNull", style.isOwnBlankWhenNull());
		
		writer.addAttribute("border", style.getOwnBorder(), JRXmlConstants.getPenMap());
		writer.addAttribute("borderColor", style.getOwnBorderColor());
		writer.addAttribute("padding", style.getOwnPadding());
		
		writer.addAttribute("topBorder", style.getOwnTopBorder(), JRXmlConstants.getPenMap());
		writer.addAttribute("topBorderColor", style.getOwnTopBorderColor());
		writer.addAttribute("topPadding", style.getOwnTopPadding());
		
		writer.addAttribute("leftBorder", style.getOwnLeftBorder(), JRXmlConstants.getPenMap());
		writer.addAttribute("leftBorderColor", style.getOwnLeftBorderColor());
		writer.addAttribute("leftPadding", style.getOwnLeftPadding());
		
		writer.addAttribute("bottomBorder", style.getOwnBottomBorder(), JRXmlConstants.getPenMap());
		writer.addAttribute("bottomBorderColor", style.getOwnBottomBorderColor());
		writer.addAttribute("bottomPadding", style.getOwnBottomPadding());
		
		writer.addAttribute("rightBorder", style.getOwnRightBorder(), JRXmlConstants.getPenMap());
		writer.addAttribute("rightBorderColor", style.getOwnRightBorderColor());
		writer.addAttribute("rightPadding", style.getOwnRightPadding());

		writer.addEncodedAttribute("fontName", style.getOwnFontName());
		writer.addAttribute("fontSize", style.getOwnFontSize());
		writer.addAttribute("isBold", style.isOwnBold());
		writer.addAttribute("isItalic", style.isOwnItalic());
		writer.addAttribute("isUnderline", style.isOwnUnderline());
		writer.addAttribute("isStrikeThrough", style.isOwnStrikeThrough());
		writer.addEncodedAttribute("pdfFontName", style.getOwnPdfFontName());
		writer.addEncodedAttribute("pdfEncoding", style.getOwnPdfEncoding());
		writer.addAttribute("isPdfEmbedded", style.isOwnPdfEmbedded());

		JRConditionalStyle[] conditionalStyles = style.getConditionalStyles();
		if (!(style instanceof JRConditionalStyle) && conditionalStyles != null) {
			for (int i = 0; i < conditionalStyles.length; i++)
				writeConditionalStyle(conditionalStyles[i]);
		}
		
		writer.closeElement();
	}


	/**
	 *
	 */
	private void writeConditionalStyle(JRConditionalStyle style) throws IOException
	{
		writer.startElement("conditionalStyle");
		writer.writeExpression("conditionExpression", style.getConditionExpression(), false);
		writeStyle(style);
		writer.closeElement();
	}


	/**
	 *
	 */
	private void writeParameter(JRParameter parameter) throws IOException
	{
		writer.startElement("parameter");
		writer.addEncodedAttribute("name", parameter.getName());
		writer.addAttribute("class", parameter.getValueClassName());
		writer.addAttribute("isForPrompting", parameter.isForPrompting(), true);

		writer.writeCDATAElement("parameterDescription", parameter.getDescription());
		writer.writeExpression("defaultValueExpression", parameter.getDefaultValueExpression(), false);

		writer.closeElement();
	}


	/**
	 *
	 */
	private void writeQuery(JRQuery query) throws IOException
	{
		writer.startElement("queryString");
		writer.addEncodedAttribute(JRQueryFactory.ATTRIBUTE_language, query.getLanguage(), JRJdbcQueryExecuterFactory.QUERY_LANGUAGE_SQL);
		writer.writeCDATA(query.getText());
		writer.closeElement();
	}


	/**
	 *
	 */
	private void writeField(JRField field) throws IOException
	{
		writer.startElement("field");
		writer.addEncodedAttribute("name", field.getName());
		writer.addAttribute("class", field.getValueClassName());

		writer.writeCDATAElement("fieldDescription", field.getDescription());
		
		writer.closeElement();
	}


	/**
	 *
	 */
	private void writeSortField(JRSortField sortField) throws IOException
	{
		writer.startElement("sortField");
		writer.addEncodedAttribute("name", sortField.getName());
		writer.addAttribute("order", sortField.getOrder(), JRXmlConstants.getSortOrderMap(), JRSortField.SORT_ORDER_ASCENDING);
		writer.closeElement();
	}


	/**
	 *
	 */
	private void writeVariable(JRVariable variable) throws IOException
	{
		writer.startElement("variable");
		writer.addEncodedAttribute("name", variable.getName());
		writer.addAttribute("class", variable.getValueClassName());
		writer.addAttribute("resetType", variable.getResetType(), JRXmlConstants.getResetTypeMap(), JRVariable.RESET_TYPE_REPORT);
		if (variable.getResetGroup() != null)
		{
			writer.addEncodedAttribute("resetGroup", variable.getResetGroup().getName());
		}
		writer.addAttribute("incrementType", variable.getIncrementType(), JRXmlConstants.getResetTypeMap(), JRVariable.RESET_TYPE_NONE);
		if (variable.getIncrementGroup() != null)
		{
			writer.addEncodedAttribute("incrementGroup", variable.getIncrementGroup().getName());
		}
		writer.addAttribute("calculation", variable.getCalculation(), JRXmlConstants.getCalculationMap(), JRVariable.CALCULATION_NOTHING);
		writer.addAttribute("incrementerFactoryClass", variable.getIncrementerFactoryClassName());

		writer.writeExpression("variableExpression", variable.getExpression(), false);
		writer.writeExpression("initialValueExpression", variable.getInitialValueExpression(), false);
		
		writer.closeElement();
	}


	/**
	 *
	 */
	private void writeGroup(JRGroup group) throws IOException
	{
		writer.startElement("group");
		writer.addEncodedAttribute("name", group.getName());
		writer.addAttribute("isStartNewColumn", group.isStartNewColumn(), false);
		writer.addAttribute("isStartNewPage", group.isStartNewPage(), false);
		writer.addAttribute("isResetPageNumber", group.isResetPageNumber(), false);
		writer.addAttribute("isReprintHeaderOnEachPage", group.isReprintHeaderOnEachPage(), false);
		writer.addAttributePositive("minHeightToStartNewPage", group.getMinHeightToStartNewPage());

		writer.writeExpression("groupExpression", group.getExpression(), false);

		if (group.getGroupHeader() != null)
		{
			writer.startElement("groupHeader");
			writeBand(group.getGroupHeader());
			writer.closeElement();
		}

		if (group.getGroupFooter() != null)
		{
			writer.startElement("groupFooter");
			writeBand(group.getGroupFooter());
			writer.closeElement();
		}

		writer.closeElement();
	}


	/**
	 *
	 */
	private void writeBand(JRBand band) throws IOException
	{
		writer.startElement("band");
		writer.addAttributePositive("height", band.getHeight());
		writer.addAttribute("isSplitAllowed", band.isSplitAllowed(), true);

		writer.writeExpression("printWhenExpression", band.getPrintWhenExpression(), false);

		/*   */
		List children = band.getChildren();
		if (children != null && children.size() > 0)
		{
			for(int i = 0; i < children.size(); i++)
			{
				((JRChild)children.get(i)).writeXml(this);
			}
		}

		writer.closeElement();
	}


	/**
	 *
	 */
	public void writeElementGroup(JRElementGroup elementGroup) throws IOException
	{
		writer.startElement("elementGroup");

		/*   */
		List children = elementGroup.getChildren();
		if (children != null && children.size() > 0)
		{
			for(int i = 0; i < children.size(); i++)
			{
				JRChild child = (JRChild)children.get(i);
				child.writeXml(this);
			}
		}

		writer.closeElement();
	}


	/**
	 *
	 */
	public void writeBreak(JRBreak breakElement) throws IOException
	{
		writer.startElement("break");
		writer.addAttribute("type", breakElement.getType(), JRXmlConstants.getBreakTypeMap(), JRBreak.TYPE_PAGE);

		writeReportElement(breakElement);

		writer.closeElement();
	}


	/**
	 *
	 */
	public void writeLine(JRLine line) throws IOException
	{
		writer.startElement("line");
		writer.addAttribute("direction", line.getDirection(), JRXmlConstants.getDirectionMap(), JRLine.DIRECTION_TOP_DOWN);

		writeReportElement(line);
		writeGraphicElement(line);

		writer.closeElement();
	}


	/**
	 *
	 */
	private void writeReportElement(JRElement element) throws IOException
	{
		writer.startElement("reportElement");
		writer.addEncodedAttribute("key", element.getKey());
		JRStyle style = element.getStyle();
		if (style != null)
		{
			writer.addEncodedAttribute("style", style.getName());
		}
		writer.addAttribute("positionType", element.getPositionType(), JRXmlConstants.getPositionTypeMap(), JRElement.POSITION_TYPE_FIX_RELATIVE_TO_TOP);
		writer.addAttribute("stretchType", element.getStretchType(), JRXmlConstants.getStretchTypeMap(), JRElement.STRETCH_TYPE_NO_STRETCH);
		writer.addAttribute("isPrintRepeatedValues", element.isPrintRepeatedValues(), true);
		writer.addAttribute("mode", element.getOwnMode(), JRXmlConstants.getModeMap());

		writer.addAttribute("x", element.getX());
		writer.addAttribute("y", element.getY());
		writer.addAttribute("width", element.getWidth());
		writer.addAttribute("height", element.getHeight());
		writer.addAttribute("isRemoveLineWhenBlank", element.isRemoveLineWhenBlank(), false);
		writer.addAttribute("isPrintInFirstWholeBand", element.isPrintInFirstWholeBand(), false);
		writer.addAttribute("isPrintWhenDetailOverflows", element.isPrintWhenDetailOverflows(), false);

		if (element.getPrintWhenGroupChanges() != null)
		{
			writer.addEncodedAttribute("printWhenGroupChanges", element.getPrintWhenGroupChanges().getName());
		}
		
		writer.addAttribute("forecolor", element.getOwnForecolor());
		writer.addAttribute("backcolor", element.getOwnBackcolor());
		
		writer.writeExpression("printWhenExpression", element.getPrintWhenExpression(), false);
		writer.closeElement();
	}


	/**
	 *
	 */
	private void writeGraphicElement(JRGraphicElement element) throws IOException
	{
		writer.startElement("graphicElement");
		writer.addAttribute("pen", element.getOwnPen(), JRXmlConstants.getPenMap());
		writer.addAttribute("fill", element.getOwnFill(), JRXmlConstants.getFillMap());
		writer.closeElement();
	}


	/**
	 *
	 */
	public void writeRectangle(JRRectangle rectangle) throws IOException
	{
		writer.startElement("rectangle");
		writer.addAttribute("radius", rectangle.getOwnRadius());

		writeReportElement(rectangle);
		writeGraphicElement(rectangle);

		writer.closeElement();
	}


	/**
	 *
	 */
	public void writeEllipse(JREllipse ellipse) throws IOException
	{
		writer.startElement("ellipse");

		writeReportElement(ellipse);
		writeGraphicElement(ellipse);

		writer.closeElement();
	}


	/**
	 *
	 */
	public void writeImage(JRImage image) throws IOException
	{
		writer.startElement("image");
		writer.addAttribute("scaleImage", image.getOwnScaleImage(), JRXmlConstants.getScaleImageMap());
		writer.addAttribute("hAlign", image.getOwnHorizontalAlignment(), JRXmlConstants.getHorizontalAlignMap());
		writer.addAttribute("vAlign", image.getOwnVerticalAlignment(), JRXmlConstants.getVerticalAlignMap());
		writer.addAttribute("isUsingCache", image.isOwnUsingCache());
		writer.addAttribute("isLazy", image.isLazy(), false);
		writer.addAttribute("onErrorType", image.getOnErrorType(), JRXmlConstants.getOnErrorTypeMap(), JRImage.ON_ERROR_TYPE_ERROR);
		writer.addAttribute("evaluationTime", image.getEvaluationTime(), JRXmlConstants.getEvaluationTimeMap(), JRExpression.EVALUATION_TIME_NOW);

		if (image.getEvaluationGroup() != null)
		{
			writer.addEncodedAttribute("evaluationGroup", image.getEvaluationGroup().getName());
		}

		writer.addEncodedAttribute("hyperlinkType", image.getLinkType());
		writer.addAttribute("hyperlinkTarget", image.getHyperlinkTarget(), JRXmlConstants.getHyperlinkTargetMap(), JRHyperlink.HYPERLINK_TARGET_SELF);
		writer.addAttribute("bookmarkLevel", image.getBookmarkLevel(), JRAnchor.NO_BOOKMARK);

		writeReportElement(image);
		writeBox(image);
		writeGraphicElement(image);

		//FIXME class is mandatory in verifier
		
		writer.writeExpression("imageExpression", image.getExpression(), true);
		writer.writeExpression("anchorNameExpression", image.getAnchorNameExpression(), false);
		writer.writeExpression("hyperlinkReferenceExpression", image.getHyperlinkReferenceExpression(), false);
		writer.writeExpression("hyperlinkAnchorExpression", image.getHyperlinkAnchorExpression(), false);
		writer.writeExpression("hyperlinkPageExpression", image.getHyperlinkPageExpression(), false);
		writer.writeExpression(JRHyperlinkFactory.ELEMENT_HYPERLINK_TOOLTIP_EXPRESSION, image.getHyperlinkTooltipExpression(), false);
		writeHyperlinkParameters(image.getHyperlinkParameters());
		
		writer.closeElement();
	}


	/**
	 *
	 */
	private void writeBox(JRBox box) throws IOException
	{
		if (box != null)
		{
			writer.startElement("box");
			writer.addAttribute("border", box.getOwnBorder(), JRXmlConstants.getPenMap());
			writer.addAttribute("borderColor", box.getOwnBorderColor());
			writer.addAttribute("padding", box.getOwnPadding());

			writer.addAttribute("topBorder", box.getOwnTopBorder(), JRXmlConstants.getPenMap());
			writer.addAttribute("topBorderColor", box.getOwnTopBorderColor());
			writer.addAttribute("topPadding", box.getOwnTopPadding());

			writer.addAttribute("leftBorder", box.getOwnLeftBorder(), JRXmlConstants.getPenMap());
			writer.addAttribute("leftBorderColor", box.getOwnLeftBorderColor());
			writer.addAttribute("leftPadding", box.getOwnLeftPadding());

			writer.addAttribute("bottomBorder", box.getOwnBottomBorder(), JRXmlConstants.getPenMap());
			writer.addAttribute("bottomBorderColor", box.getOwnBottomBorderColor());
			writer.addAttribute("bottomPadding", box.getOwnBottomPadding());

			writer.addAttribute("rightBorder", box.getOwnRightBorder(), JRXmlConstants.getPenMap());
			writer.addAttribute("rightBorderColor", box.getOwnRightBorderColor());
			writer.addAttribute("rightPadding", box.getOwnRightPadding());
			
			writer.closeElement(true);
		}
	}


	/**
	 *
	 */
	public void writeStaticText(JRStaticText staticText) throws IOException
	{
		writer.startElement("staticText");

		writeReportElement(staticText);
		writeBox(staticText);
		writeTextElement(staticText);

		writer.writeCDATAElement("text", staticText.getText());

		writer.closeElement();
	}


	/**
	 *
	 */
	private void writeTextElement(JRTextElement textElement) throws IOException
	{
		writer.startElement("textElement");
		writer.addAttribute("textAlignment", textElement.getOwnHorizontalAlignment(), JRXmlConstants.getHorizontalAlignMap());
		writer.addAttribute("verticalAlignment", textElement.getOwnVerticalAlignment(), JRXmlConstants.getVerticalAlignMap());
		writer.addAttribute("rotation", textElement.getOwnRotation(), JRXmlConstants.getRotationMap());
		writer.addAttribute("lineSpacing", textElement.getOwnLineSpacing(), JRXmlConstants.getLineSpacingMap());
		writer.addAttribute("isStyledText", textElement.isOwnStyledText());

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
			writer.startElement("font");
			if (font.getReportFont() != null)
			{
				JRFont baseFont = 
					(JRFont)fontsMap.get(
						font.getReportFont().getName()
						);
				if(baseFont != null)
				{
					writer.addEncodedAttribute("reportFont", font.getReportFont().getName());
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
		
			writer.addEncodedAttribute("fontName", font.getOwnFontName());
			writer.addAttribute("size", font.getOwnFontSize());
			writer.addAttribute("isBold", font.isOwnBold());
			writer.addAttribute("isItalic", font.isOwnItalic());
			writer.addAttribute("isUnderline", font.isOwnUnderline());
			writer.addAttribute("isStrikeThrough", font.isOwnStrikeThrough());
			writer.addEncodedAttribute("pdfFontName", font.getOwnPdfFontName());
			writer.addEncodedAttribute("pdfEncoding", font.getOwnPdfEncoding());
			writer.addAttribute("isPdfEmbedded", font.isOwnPdfEmbedded());
			writer.closeElement(true);
		}
	}


	/**
	 *
	 */
	public void writeTextField(JRTextField textField) throws IOException
	{
		writer.startElement("textField");
		writer.addAttribute("isStretchWithOverflow", textField.isStretchWithOverflow(), false);
		writer.addAttribute("evaluationTime", textField.getEvaluationTime(), JRXmlConstants.getEvaluationTimeMap(), JRExpression.EVALUATION_TIME_NOW);

		if (textField.getEvaluationGroup() != null)
		{
			writer.addEncodedAttribute("evaluationGroup", textField.getEvaluationGroup().getName());
		}

		writer.addEncodedAttribute("pattern", textField.getOwnPattern());
		writer.addAttribute("isBlankWhenNull", textField.isOwnBlankWhenNull());
		
		writer.addEncodedAttribute("hyperlinkType", textField.getLinkType());
		writer.addAttribute("hyperlinkTarget", textField.getHyperlinkTarget(), JRXmlConstants.getHyperlinkTargetMap(), JRHyperlink.HYPERLINK_TARGET_SELF);
		writer.addAttribute("bookmarkLevel", textField.getBookmarkLevel(), JRAnchor.NO_BOOKMARK);

		writeReportElement(textField);
		writeBox(textField);
		writeTextElement(textField);

		writer.writeExpression("textFieldExpression", textField.getExpression(), true);
		
		writer.writeExpression("anchorNameExpression", textField.getAnchorNameExpression(), false);
		writer.writeExpression("hyperlinkReferenceExpression", textField.getHyperlinkReferenceExpression(), false);
		writer.writeExpression("hyperlinkAnchorExpression", textField.getHyperlinkAnchorExpression(), false);
		writer.writeExpression("hyperlinkPageExpression", textField.getHyperlinkPageExpression(), false);
		writer.writeExpression(JRHyperlinkFactory.ELEMENT_HYPERLINK_TOOLTIP_EXPRESSION, textField.getHyperlinkTooltipExpression(), false);
		writeHyperlinkParameters(textField.getHyperlinkParameters());
		
		writer.closeElement();
	}


	/**
	 *
	 */
	public void writeSubreport(JRSubreport subreport) throws IOException
	{
		writer.startElement("subreport");
		writer.addAttribute("isUsingCache", subreport.isOwnUsingCache());

		writeReportElement(subreport);

		writer.writeExpression("parametersMapExpression", subreport.getParametersMapExpression(), false);

		/*   */
		JRSubreportParameter[] parameters = subreport.getParameters();
		if (parameters != null && parameters.length > 0)
		{
			for(int i = 0; i < parameters.length; i++)
			{
				writeSubreportParameter(parameters[i]);
			}
		}

		writer.writeExpression("connectionExpression", subreport.getConnectionExpression(), false);
		writer.writeExpression("dataSourceExpression", subreport.getDataSourceExpression(), false);

		JRSubreportReturnValue[] returnValues = subreport.getReturnValues();
		if (returnValues != null && returnValues.length > 0)
		{
			for(int i = 0; i < returnValues.length; i++)
			{
				writeSubreportReturnValue(returnValues[i]);
			}
		}

		writer.writeExpression("subreportExpression", subreport.getExpression(), true);
		
		writer.closeElement();
	}


	/**
	 *
	 */
	private void writeSubreportParameter(JRSubreportParameter subreportParameter) throws IOException
	{
		writer.startElement("subreportParameter");
		writer.addEncodedAttribute("name", subreportParameter.getName());

		writer.writeExpression("subreportParameterExpression", subreportParameter.getExpression(), false);
		
		writer.closeElement();
	}


	private void writeDatasetParameter(JRDatasetParameter datasetParameter) throws IOException
	{
		writer.startElement(JRDatasetRunParameterFactory.TAG_DATASET_PARAMETER);
		writer.addEncodedAttribute(JRDatasetRunParameterFactory.ATTRIBUTE_name, datasetParameter.getName());

		writer.writeExpression(JRDatasetRunParameterExpressionFactory.TAG_DATASET_PARAMETER_EXPRESSION, datasetParameter.getExpression(), false);
		
		writer.closeElement();
	}


	/**
	 *
	 */
	private void writeChart(JRChart chart) throws IOException
	{
		writer.startElement("chart");
		writer.addAttribute("isShowLegend", chart.isShowLegend(), true);
		writer.addAttribute("evaluationTime", chart.getEvaluationTime(), JRXmlConstants.getEvaluationTimeMap(), JRExpression.EVALUATION_TIME_NOW);

		if (chart.getEvaluationTime() == JRExpression.EVALUATION_TIME_GROUP)
		{
			writer.addEncodedAttribute("evaluationGroup", chart.getEvaluationGroup().getName());
		}
		
		writer.addEncodedAttribute("hyperlinkType", chart.getLinkType());
		writer.addAttribute("hyperlinkTarget", chart.getHyperlinkTarget(), JRXmlConstants.getHyperlinkTargetMap(), JRHyperlink.HYPERLINK_TARGET_SELF);
		writer.addAttribute("bookmarkLevel", chart.getBookmarkLevel(), JRAnchor.NO_BOOKMARK);
		writer.addAttribute("customizerClass", chart.getCustomizerClass());

		writeReportElement(chart);
		writeBox(chart);

		// write title
		writer.startElement("chartTitle");
		writer.addAttribute("position", chart.getTitlePosition(), JRXmlConstants.getChartTitlePositionMap(), JRChart.TITLE_POSITION_TOP);
		writer.addAttribute("color", chart.getTitleColor(), Color.black);
		writeFont(chart.getTitleFont());
		if (chart.getTitleExpression() != null)
		{
			writer.writeExpression("titleExpression", chart.getTitleExpression(), false);
		}
		writer.closeElement();

		// write subtitle
		writer.startElement("chartSubtitle");
		writer.addAttribute("color", chart.getSubtitleColor());
		writeFont(chart.getSubtitleFont());
		if (chart.getSubtitleExpression() != null)
		{
			writer.writeExpression("subtitleExpression", chart.getSubtitleExpression(), false);
		}
		writer.closeElement();
		
		// write chartLegend
		writer.startElement("chartLegend");
		if (chart.getLegendColor() != null)
			writer.addAttribute(JRChartFactory.JRChartLegendFactory.ATTRIBUTE_textColor, chart.getLegendColor());
		if (chart.getLegendBackgroundColor() != null)
			writer.addAttribute(JRChartFactory.JRChartLegendFactory.ATTRIBUTE_backgroundColor, chart.getLegendBackgroundColor());
		writeFont(chart.getLegendFont());
		writer.closeElement();
		
		writer.writeExpression("anchorNameExpression", chart.getAnchorNameExpression(), false);
		writer.writeExpression("hyperlinkReferenceExpression", chart.getHyperlinkReferenceExpression(), false);
		writer.writeExpression("hyperlinkAnchorExpression", chart.getHyperlinkAnchorExpression(), false);
		writer.writeExpression("hyperlinkPageExpression", chart.getHyperlinkPageExpression(), false);
		writer.writeExpression(JRHyperlinkFactory.ELEMENT_HYPERLINK_TOOLTIP_EXPRESSION, chart.getHyperlinkTooltipExpression(), false);
		writeHyperlinkParameters(chart.getHyperlinkParameters());

		writer.closeElement();
	}


	/**
	 *
	 */
	private void writeElementDataset(JRElementDataset dataset) throws IOException
	{
		writer.startElement("dataset");
		writer.addAttribute("resetType", dataset.getResetType(), JRXmlConstants.getResetTypeMap(), JRVariable.RESET_TYPE_REPORT);

		if (dataset.getResetType() == JRVariable.RESET_TYPE_GROUP)
		{
			writer.addEncodedAttribute("resetGroup", dataset.getResetGroup().getName());
		}
		writer.addAttribute("incrementType", dataset.getIncrementType(), JRXmlConstants.getResetTypeMap(), JRVariable.RESET_TYPE_NONE);

		if (dataset.getIncrementType() == JRVariable.RESET_TYPE_GROUP)
		{
			writer.addEncodedAttribute("incrementGroup", dataset.getIncrementGroup().getName());
		}

		writer.writeExpression(JRElementDatasetFactory.ELEMENT_INCREMENT_WHEN_EXPRESSION, dataset.getIncrementWhenExpression(), false);
		
		JRDatasetRun datasetRun = dataset.getDatasetRun();
		if (datasetRun != null)
		{
			writeDatasetRun(datasetRun);
		}

		writer.closeElement(true);
	}


	/**
	 *
	 */
	private void writeCategoryDataSet(JRCategoryDataset dataset) throws IOException
	{
		writer.startElement("categoryDataset");

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
		writer.startElement("timeSeriesDataset");
		if (dataset.getTimePeriod() != null && !Day.class.getName().equals(dataset.getTimePeriod().getName()))
		{
			writer.addAttribute("timePeriod", JRXmlConstants.getTimePeriodName(dataset.getTimePeriod()));
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
	
	
	private void writeTimePeriodDataset(JRTimePeriodDataset dataset) throws IOException
	{
		writer.startElement("timePeriodDataset");
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
	private void writeCategorySeries(JRCategorySeries categorySeries) throws IOException
	{
		writer.startElement("categorySeries");

		writer.writeExpression("seriesExpression", categorySeries.getSeriesExpression(), false);
		writer.writeExpression("categoryExpression", categorySeries.getCategoryExpression(), false);
		writer.writeExpression("valueExpression", categorySeries.getValueExpression(), false);
		writer.writeExpression("labelExpression", categorySeries.getLabelExpression(), false);
		writeHyperlink(JRHyperlinkFactory.ELEMENT_ITEM_HYPERLINK, categorySeries.getItemHyperlink());

		writer.closeElement();
	}

	/**
	 * 
	 */
	private void writeXyzDataset(JRXyzDataset dataset) throws IOException
	{
		writer.startElement("xyzDataset");
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
		writer.startElement("xyzSeries");
		
		writer.writeExpression("seriesExpression", series.getSeriesExpression(), false);
		writer.writeExpression("xValueExpression", series.getXValueExpression(), false);
		writer.writeExpression("yValueExpression", series.getYValueExpression(), false);
		writer.writeExpression("zValueExpression", series.getZValueExpression(), false);
		writeHyperlink(JRHyperlinkFactory.ELEMENT_ITEM_HYPERLINK, series.getItemHyperlink());

		writer.closeElement();
	}

	/**
	 *
	 */
	private void writeXySeries(JRXySeries xySeries) throws IOException
	{
		writer.startElement("xySeries");

		writer.writeExpression("seriesExpression", xySeries.getSeriesExpression(), false);
		writer.writeExpression("xValueExpression", xySeries.getXValueExpression(), false);
		writer.writeExpression("yValueExpression", xySeries.getYValueExpression(), false);
		writer.writeExpression("labelExpression", xySeries.getLabelExpression(), false);
		writeHyperlink(JRHyperlinkFactory.ELEMENT_ITEM_HYPERLINK, xySeries.getItemHyperlink());

		writer.closeElement();
	}


	/**
	 *
	 */
	private void writeXyDataset(JRXyDataset dataset) throws IOException
	{
		writer.startElement("xyDataset");

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
		writer.startElement("timeSeries");

		writer.writeExpression("seriesExpression", timeSeries.getSeriesExpression(), false);
		writer.writeExpression("timePeriodExpression", timeSeries.getTimePeriodExpression(), false);
		writer.writeExpression("valueExpression", timeSeries.getValueExpression(), false);
		writer.writeExpression("labelExpression", timeSeries.getLabelExpression(), false);
		writeHyperlink(JRHyperlinkFactory.ELEMENT_ITEM_HYPERLINK, timeSeries.getItemHyperlink());
		
		writer.closeElement();
	}
	
	
	private void writeTimePeriodSeries(JRTimePeriodSeries timePeriodSeries) throws IOException
	{
		writer.startElement("timePeriodSeries");
		
		writer.writeExpression("seriesExpression", timePeriodSeries.getSeriesExpression(), false);
		writer.writeExpression("startDateExpression", timePeriodSeries.getStartDateExpression(), false);
		writer.writeExpression("endDateExpression", timePeriodSeries.getEndDateExpression(), false);
		writer.writeExpression("valueExpression", timePeriodSeries.getValueExpression(), false);
		writer.writeExpression("labelExpression", timePeriodSeries.getLabelExpression(), false);
		writeHyperlink(JRHyperlinkFactory.ELEMENT_ITEM_HYPERLINK, timePeriodSeries.getItemHyperlink());
		
		writer.closeElement();
	}

	
	/**
	 *
	 */
	public void writePieDataset(JRPieDataset dataset) throws IOException
	{
		writer.startElement("pieDataset");

		writeElementDataset(dataset);

		writer.writeExpression("keyExpression", dataset.getKeyExpression(), false);
		writer.writeExpression("valueExpression", dataset.getValueExpression(), false);
		writer.writeExpression("labelExpression", dataset.getLabelExpression(), false);
		writeHyperlink(JRHyperlinkFactory.ELEMENT_SECTION_HYPERLINK, dataset.getSectionHyperlink());

		writer.closeElement();
	}

	/**
	 * Writes the description of a value dataset to the output stream.
	 * @param dataset the value dataset to persist
	 */
	public void writeValueDataset(JRValueDataset dataset) throws IOException
	{
		writer.startElement("valueDataset");

		writeElementDataset(dataset);

		writer.writeExpression("valueExpression", dataset.getValueExpression(), false);
		
		writer.closeElement();
	}


	/**
	 * Writes the description of how to display a value in a valueDataset.
	 * 
	 * @param valueDisplay the description to save
	 */
	public void writeValueDisplay(JRValueDisplay valueDisplay) throws IOException
	{
		writer.startElement("valueDisplay");

        writer.addAttribute("color", valueDisplay.getColor());
        writer.addAttribute("mask", valueDisplay.getMask());

		writeFont(valueDisplay.getFont());
		
		writer.closeElement();
	}

	/**
	 * Writes a data range block to the output stream.
	 * 
	 * @param dataRange the range to write
	 */
	public void writeDataRange(JRDataRange dataRange) throws IOException
	{
		writer.startElement("dataRange");
		
		writer.writeExpression("lowExpression", dataRange.getLowExpression(), false);
		writer.writeExpression("highExpression", dataRange.getHighExpression(), false);
		
		writer.closeElement();
    }


	/**
	 * Writes a meter interval description to the output stream.
	 * 
	 * @param interval the interval to write
	 */
	private void writeMeterInterval(JRMeterInterval interval) throws IOException
	{
		writer.startElement("meterInterval");
		
		writer.addAttribute("label", interval.getLabel());
		writer.addAttribute("color", interval.getBackgroundColor());
		writer.addAttribute("alpha", interval.getAlpha());
		
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
			writer.startElement("seriesColor");
			writer.addAttribute(JRSeriesColorFactory.ATTRIBUTE_seriesOrder, colors[i].getSeriesOrder());
			writer.addAttribute(JRSeriesColorFactory.ATTRIBUTE_color, colors[i].getColor());
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
	   writer.startElement("axis");
	   writer.addAttribute(JRChartAxisFactory.ATTRIBUTE_position, chartAxis.getPosition(),
	                       JRXmlConstants.getAxisPositionMap(), JRChartAxis.POSITION_LEFT_OR_TOP);
	   
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
		writer.startElement("plot");
		writer.addAttribute("backcolor", plot.getBackcolor());
		writer.addAttribute("orientation", plot.getOrientation(), JRXmlConstants.getPlotOrientationMap(), PlotOrientation.VERTICAL);
		writer.addAttribute("backgroundAlpha", plot.getBackgroundAlpha(), 1.0f);
		writer.addAttribute("foregroundAlpha", plot.getForegroundAlpha(), 1.0f);
		writer.addAttribute("labelRotation", plot.getLabelRotation(), 0.0);
		writeSeriesColors(plot.getSeriesColors());
		
		writer.closeElement();
	}


	/**
	 *
	 */
	public void writePieChart(JRChart chart) throws IOException
	{
		writer.startElement("pieChart");
		writeChart(chart);
		writePieDataset((JRPieDataset) chart.getDataset());

		// write plot
		writer.startElement("piePlot");
		writePlot(chart.getPlot());
		writer.closeElement();

		writer.closeElement();
	}


	/**
	 *
	 */
	public void writePie3DChart(JRChart chart) throws IOException
	{
		writer.startElement("pie3DChart");
		writeChart(chart);
		writePieDataset((JRPieDataset) chart.getDataset());

		// write plot
		JRPie3DPlot plot = (JRPie3DPlot) chart.getPlot();
		writer.startElement("pie3DPlot");
		writer.addAttribute("depthFactor", plot.getDepthFactor(), JRPie3DPlot.DEPTH_FACTOR_DEFAULT);
		writePlot(chart.getPlot());
		writer.closeElement();

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
	 * @param axisLineColor the color to use for the axis line and any tick marks
	 * 
	 */
	public void writeAxisFormat(String axisFormatElementName,
								JRFont axisLabelFont, Color axisLabelColor,
								JRFont axisTickLabelFont, Color axisTickLabelColor,
								String axisTickLabelMask, Color axisLineColor)  throws IOException
	{
		if (axisLabelFont == null && axisLabelColor == null &&
			axisTickLabelFont == null && axisTickLabelColor == null && axisLineColor == null)
			return;
		
		writer.startElement(axisFormatElementName);
		writer.startElement("axisFormat");
		writer.addAttribute(JRChartFactory.JRChartAxisFormatFactory.ATTRIBUTE_labelColor, axisLabelColor);
		writer.addAttribute(JRChartFactory.JRChartAxisFormatFactory.ATTRIBUTE_tickLabelColor, axisTickLabelColor);
		writer.addAttribute(JRChartFactory.JRChartAxisFormatFactory.ATTRIBUTE_tickLabelMask, axisTickLabelMask);
		writer.addAttribute(JRChartFactory.JRChartAxisFormatFactory.ATTRIBUTE_axisLineColor, axisLineColor);

		if (axisLabelFont != null)
		{
			writer.startElement("labelFont");
			writeFont(axisLabelFont);
			writer.closeElement();
		}
		
		if (axisTickLabelFont != null)
		{
			writer.startElement("tickLabelFont");
			writeFont(axisTickLabelFont);
			writer.closeElement();
		}

		writer.closeElement();
		writer.closeElement();
	}
	/**
	 *
	 */
	private void writeBarPlot(JRBarPlot plot) throws IOException
	{
		writer.startElement("barPlot");
		writer.addAttribute("isShowLabels", plot.isShowLabels(), false);
		writer.addAttribute("isShowTickLabels", plot.isShowTickLabels(), true);
		writer.addAttribute("isShowTickMarks", plot.isShowTickMarks(), true);
		writePlot(plot);

		writer.writeExpression("categoryAxisLabelExpression", plot.getCategoryAxisLabelExpression(), false);
		writeAxisFormat("categoryAxisFormat", plot.getCategoryAxisLabelFont(), plot.getCategoryAxisLabelColor(),
						plot.getCategoryAxisTickLabelFont(), plot.getCategoryAxisTickLabelColor(),
						plot.getCategoryAxisTickLabelMask(), plot.getCategoryAxisLineColor());
		writer.writeExpression("valueAxisLabelExpression", plot.getValueAxisLabelExpression(), false);
		writeAxisFormat("valueAxisFormat", plot.getValueAxisLabelFont(), plot.getValueAxisLabelColor(),
				plot.getValueAxisTickLabelFont(), plot.getValueAxisTickLabelColor(),
				plot.getValueAxisTickLabelMask(), plot.getValueAxisLineColor());


		writer.closeElement();
	}
	
	
	/**
	 * 
	 */
	private void writeBubblePlot(JRBubblePlot plot) throws IOException
	{
		writer.startElement("bubblePlot");
		writer.addAttribute("scaleType", plot.getScaleType(), JRXmlConstants.getScaleTypeMap());
		writePlot(plot);

		writer.writeExpression("xAxisLabelExpression", plot.getXAxisLabelExpression(), false);
		writeAxisFormat("xAxisFormat", plot.getXAxisLabelFont(), plot.getXAxisLabelColor(),
				plot.getXAxisTickLabelFont(), plot.getXAxisTickLabelColor(),
				plot.getXAxisTickLabelMask(), plot.getXAxisLineColor());
		writer.writeExpression("yAxisLabelExpression", plot.getYAxisLabelExpression(), false);
		writeAxisFormat("yAxisFormat", plot.getYAxisLabelFont(), plot.getYAxisLabelColor(),
				plot.getYAxisTickLabelFont(), plot.getYAxisTickLabelColor(),
				plot.getYAxisTickLabelMask(), plot.getYAxisLineColor());

		writer.closeElement();
	}


	/**
	 *
	 */
	private void writeLinePlot(JRLinePlot plot) throws IOException
	{
		writer.startElement("linePlot");
		writer.addAttribute("isShowLines", plot.isShowLines(), true);
		writer.addAttribute("isShowShapes", plot.isShowShapes(), true);

		writePlot(plot);

		writer.writeExpression("categoryAxisLabelExpression", plot.getCategoryAxisLabelExpression(), false);
		writeAxisFormat("categoryAxisFormat", plot.getCategoryAxisLabelFont(), plot.getCategoryAxisLabelColor(),
				plot.getCategoryAxisTickLabelFont(), plot.getCategoryAxisTickLabelColor(),
				plot.getCategoryAxisTickLabelMask(), plot.getCategoryAxisLineColor());
		writer.writeExpression("valueAxisLabelExpression", plot.getValueAxisLabelExpression(), false);
		writeAxisFormat("valueAxisFormat", plot.getValueAxisLabelFont(), plot.getValueAxisLabelColor(),
				plot.getValueAxisTickLabelFont(), plot.getValueAxisTickLabelColor(),
				plot.getValueAxisTickLabelMask(), plot.getValueAxisLineColor());

		writer.closeElement();
	}
	
	
	private void writeTimeSeriesPlot(JRTimeSeriesPlot plot) throws IOException
	{
		writer.startElement("timeSeriesPlot");
		writer.addAttribute("isShowLines", plot.isShowLines(), true);
		writer.addAttribute("isShowShapes", plot.isShowShapes(), true);
		
		writePlot( plot );
		
		writer.writeExpression("timeAxisLabelExpression", plot.getTimeAxisLabelExpression(), false);
		writeAxisFormat("timeAxisFormat", plot.getTimeAxisLabelFont(), plot.getTimeAxisLabelColor(),
				plot.getTimeAxisTickLabelFont(), plot.getTimeAxisTickLabelColor(),
				plot.getTimeAxisTickLabelMask(), plot.getTimeAxisLineColor());
		writer.writeExpression("valueAxisLabelExpression", plot.getValueAxisLabelExpression(), false);
		writeAxisFormat("valueAxisFormat", plot.getValueAxisLabelFont(), plot.getValueAxisLabelColor(),
				plot.getValueAxisTickLabelFont(), plot.getValueAxisTickLabelColor(),
				plot.getValueAxisTickLabelMask(), plot.getValueAxisLineColor());


		writer.closeElement();
	}


	/**
	 *
	 */
	public void writeBar3DPlot(JRBar3DPlot plot) throws IOException
	{
		writer.startElement("bar3DPlot");
		writer.addAttribute("isShowLabels", plot.isShowLabels(), false);
		writer.addAttribute("xOffset", plot.getXOffset(), BarRenderer3D.DEFAULT_X_OFFSET);
		writer.addAttribute("yOffset", plot.getYOffset(), BarRenderer3D.DEFAULT_Y_OFFSET);

		writePlot(plot);

		writer.writeExpression("categoryAxisLabelExpression", plot.getCategoryAxisLabelExpression(), false);
		writeAxisFormat("categoryAxisFormat", plot.getCategoryAxisLabelFont(), plot.getCategoryAxisLabelColor(),
				plot.getCategoryAxisTickLabelFont(), plot.getCategoryAxisTickLabelColor(),
				plot.getCategoryAxisTickLabelMask(), plot.getCategoryAxisLineColor());
		writer.writeExpression("valueAxisLabelExpression", plot.getValueAxisLabelExpression(), false);
		writeAxisFormat("valueAxisFormat", plot.getValueAxisLabelFont(), plot.getValueAxisLabelColor(),
				plot.getValueAxisTickLabelFont(), plot.getValueAxisTickLabelColor(),
				plot.getValueAxisTickLabelMask(), plot.getValueAxisLineColor());


		writer.closeElement();
	}


	/**
	 *
	 */
	public void writeBarChart(JRChart chart) throws IOException
	{
		writer.startElement("barChart");

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
		writer.startElement("bar3DChart");

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
		writer.startElement("bubbleChart");
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
		writer.startElement("stackedBarChart");

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
		writer.startElement("stackedBar3DChart");

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
		writer.startElement("lineChart");

		writeChart(chart);
		writeCategoryDataSet((JRCategoryDataset) chart.getDataset());
		writeLinePlot((JRLinePlot) chart.getPlot());
		writer.closeElement();
	}
	
	
	public void writeTimeSeriesChart(JRChart chart) throws IOException
	{
		writer.startElement("timeSeriesChart");
		writeChart(chart);
		writeTimeSeriesDataset((JRTimeSeriesDataset)chart.getDataset());
		writeTimeSeriesPlot((JRTimeSeriesPlot)chart.getPlot());
		writer.closeElement();
	}

	public void writeHighLowDataset(JRHighLowDataset dataset) throws IOException
	{
		writer.startElement("highLowDataset");

		writeElementDataset(dataset);

		writer.writeExpression("seriesExpression", dataset.getSeriesExpression(), false);
		writer.writeExpression("dateExpression", dataset.getDateExpression(), false);
		writer.writeExpression("highExpression", dataset.getHighExpression(), false);
		writer.writeExpression("lowExpression", dataset.getLowExpression(), false);
		writer.writeExpression("openExpression", dataset.getOpenExpression(), false);
		writer.writeExpression("closeExpression", dataset.getCloseExpression(), false);
		writer.writeExpression("volumeExpression", dataset.getVolumeExpression(), false);
		writeHyperlink(JRHyperlinkFactory.ELEMENT_ITEM_HYPERLINK, dataset.getItemHyperlink());

		writer.closeElement();
	}


	public void writeHighLowChart(JRChart chart) throws IOException
	{
		writer.startElement("highLowChart");

		writeChart(chart);
		writeHighLowDataset((JRHighLowDataset) chart.getDataset());

		JRHighLowPlot plot = (JRHighLowPlot) chart.getPlot();
		writer.startElement("highLowPlot");
		writer.addAttribute("isShowOpenTicks", plot.isShowOpenTicks(), true);
		writer.addAttribute("isShowCloseTicks", plot.isShowCloseTicks(), true);

		writePlot(plot);

		writer.writeExpression("timeAxisLabelExpression", plot.getTimeAxisLabelExpression(), false);
		writeAxisFormat("timeAxisFormat", plot.getTimeAxisLabelFont(), plot.getTimeAxisLabelColor(),
				plot.getTimeAxisTickLabelFont(), plot.getTimeAxisTickLabelColor(),
				plot.getTimeAxisTickLabelMask(), plot.getTimeAxisLineColor());
		writer.writeExpression("valueAxisLabelExpression", plot.getValueAxisLabelExpression(), false);
		writeAxisFormat("valueAxisFormat", plot.getValueAxisLabelFont(), plot.getValueAxisLabelColor(),
				plot.getValueAxisTickLabelFont(), plot.getValueAxisTickLabelColor(),
				plot.getValueAxisTickLabelMask(), plot.getValueAxisLineColor());


		writer.closeElement();
		writer.closeElement();
	}


	public void writeCandlestickChart(JRChart chart) throws IOException
	{
		writer.startElement("candlestickChart");

		writeChart(chart);
		writeHighLowDataset((JRHighLowDataset) chart.getDataset());

		JRCandlestickPlot plot = (JRCandlestickPlot) chart.getPlot();
		writer.startElement("candlestickPlot");
		writer.addAttribute("isShowVolume", plot.isShowVolume(), true);

		writePlot(plot);

		writer.writeExpression("timeAxisLabelExpression", plot.getTimeAxisLabelExpression(), false);
		writeAxisFormat("timeAxisFormat", plot.getTimeAxisLabelFont(), plot.getTimeAxisLabelColor(),
				plot.getTimeAxisTickLabelFont(), plot.getTimeAxisTickLabelColor(),
				plot.getTimeAxisTickLabelMask(), plot.getTimeAxisLineColor());
		writer.writeExpression("valueAxisLabelExpression", plot.getValueAxisLabelExpression(), false);
		writeAxisFormat("valueAxisFormat", plot.getValueAxisLabelFont(), plot.getValueAxisLabelColor(),
				plot.getValueAxisTickLabelFont(), plot.getValueAxisTickLabelColor(),
				plot.getValueAxisTickLabelMask(), plot.getValueAxisLineColor());


		writer.closeElement();
		writer.closeElement();
	}

	/**
	 *
	 */
	private void writeAreaPlot(JRAreaPlot plot) throws IOException
	{
		writer.startElement("areaPlot");
		writePlot(plot);

		writer.writeExpression("categoryAxisLabelExpression", plot.getCategoryAxisLabelExpression(), false);
		writeAxisFormat("categoryAxisFormat", plot.getCategoryAxisLabelFont(), plot.getCategoryAxisLabelColor(),
				plot.getCategoryAxisTickLabelFont(), plot.getCategoryAxisTickLabelColor(),
				plot.getCategoryAxisTickLabelMask(), plot.getCategoryAxisLineColor());
		writer.writeExpression("valueAxisLabelExpression", plot.getValueAxisLabelExpression(), false);
		writeAxisFormat("valueAxisFormat", plot.getValueAxisLabelFont(), plot.getValueAxisLabelColor(),
				plot.getValueAxisTickLabelFont(), plot.getValueAxisTickLabelColor(),
				plot.getValueAxisTickLabelMask(), plot.getValueAxisLineColor());


		writer.closeElement();
	}


	/**
	 *
	 */
	public void writeAreaChart(JRChart chart) throws IOException
	{
		writer.startElement("areaChart");

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
		writer.startElement("scatterPlot");
		writer.addAttribute("isShowLines", plot.isShowLines(), true);
		writer.addAttribute("isShowShapes", plot.isShowShapes(), true);

		writePlot(plot);

		writer.writeExpression("xAxisLabelExpression", plot.getXAxisLabelExpression(), false);
		writeAxisFormat("xAxisFormat", plot.getXAxisLabelFont(), plot.getXAxisLabelColor(),
				plot.getXAxisTickLabelFont(), plot.getXAxisTickLabelColor(),
				plot.getXAxisTickLabelMask(), plot.getXAxisLineColor());
		writer.writeExpression("yAxisLabelExpression", plot.getYAxisLabelExpression(), false);
		writeAxisFormat("yAxisFormat", plot.getYAxisLabelFont(), plot.getYAxisLabelColor(),
				plot.getYAxisTickLabelFont(), plot.getYAxisTickLabelColor(),
				plot.getYAxisTickLabelMask(), plot.getYAxisLineColor());

		writer.closeElement();
	}


	/**
	 *
	 */
	public void writeScatterChart(JRChart chart) throws IOException
	{
		writer.startElement("scatterChart");

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
		writer.startElement("xyAreaChart");

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
		writer.startElement("xyBarChart");

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
		writer.startElement("xyLineChart");

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
		writer.startElement("meterChart");
		
		writeChart(chart);
		writeValueDataset((JRValueDataset) chart.getDataset());

		// write plot
		JRMeterPlot plot = (JRMeterPlot) chart.getPlot();
		writer.startElement("meterPlot");
		writer.addAttribute(JRMeterPlotFactory.ATTRIBUTE_shape, plot.getShape(),
		                    JRXmlConstants.getMeterShapeMap(), JRMeterPlot.SHAPE_PIE);
		writer.addAttribute(JRMeterPlotFactory.ATTRIBUTE_angle, plot.getMeterAngle());
		writer.addAttribute(JRMeterPlotFactory.ATTRIBUTE_units, plot.getUnits());
		writer.addAttribute(JRMeterPlotFactory.ATTRIBUTE_tickInterval, plot.getTickInterval());
		writer.addAttribute(JRMeterPlotFactory.ATTRIBUTE_meterColor, plot.getMeterBackgroundColor());
		writer.addAttribute(JRMeterPlotFactory.ATTRIBUTE_needleColor, plot.getNeedleColor());
		writer.addAttribute(JRMeterPlotFactory.ATTRIBUTE_tickColor, plot.getTickColor());
		
		writePlot(chart.getPlot());
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
		writer.startElement("thermometerChart");
		
		writeChart(chart);
		writeValueDataset((JRValueDataset) chart.getDataset());

		// write plot
		JRThermometerPlot plot = (JRThermometerPlot) chart.getPlot();
		
		writer.startElement("thermometerPlot");
		
		writer.addAttribute(JRThermometerPlotFactory.ATTRIBUTE_valueLocation,
		                    plot.getValueLocation(),
		                    JRXmlConstants.getThermometerValueLocationMap(),
		                    JRThermometerPlot.LOCATION_BULB);
		writer.addAttribute(JRThermometerPlotFactory.ATTRIBUTE_showValueLines, plot.isShowValueLines());
		writer.addAttribute(JRThermometerPlotFactory.ATTRIBUTE_mercuryColor, plot.getMercuryColor());
		
		writePlot(chart.getPlot());
		
		writeValueDisplay(plot.getValueDisplay());
		writeDataRange(plot.getDataRange());
		
		if (plot.getLowRange() != null)
		{
            writer.startElement("lowRange");
            writeDataRange(plot.getLowRange());
            writer.closeElement();
		}
		
		if (plot.getMediumRange() != null)
		{
            writer.startElement("mediumRange");
            writeDataRange(plot.getMediumRange());
            writer.closeElement();
		}
		
		if (plot.getHighRange() != null)
		{
            writer.startElement("highRange");
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
		writer.startElement("multiAxisChart");
		
		writeChart(chart);

		// write plot
		JRMultiAxisPlot plot = (JRMultiAxisPlot) chart.getPlot();
		writer.startElement("multiAxisPlot");
		
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
			default:
				throw new JRRuntimeException("Chart type not supported.");
		}
	}


	private void writeSubreportReturnValue(JRSubreportReturnValue returnValue) throws IOException
	{
		writer.startElement("returnValue");
		writer.addEncodedAttribute("subreportVariable", returnValue.getSubreportVariable());
		writer.addEncodedAttribute("toVariable", returnValue.getToVariable());
		writer.addAttribute("calculation", returnValue.getCalculation(), JRXmlConstants.getCalculationMap(), JRVariable.CALCULATION_NOTHING);
		writer.addAttribute("incrementerFactoryClass", returnValue.getIncrementerFactoryClassName());
		writer.closeElement();
	}


	public void writeCrosstab(JRCrosstab crosstab) throws IOException
	{
		writer.startElement("crosstab");
		writer.addAttribute(JRCrosstabFactory.ATTRIBUTE_isRepeatColumnHeaders, crosstab.isRepeatColumnHeaders(), true);
		writer.addAttribute(JRCrosstabFactory.ATTRIBUTE_isRepeatRowHeaders, crosstab.isRepeatRowHeaders(), true);
		writer.addAttribute(JRCrosstabFactory.ATTRIBUTE_columnBreakOffset, crosstab.getColumnBreakOffset(), JRCrosstab.DEFAULT_COLUMN_BREAK_OFFSET);
		
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
		
		writer.writeExpression("parametersMapExpression", crosstab.getParametersMapExpression(), false);
		
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
		writer.startElement("crosstabDataset");
		writer.addAttribute(JRCrosstabDatasetFactory.ATTRIBUTE_isDataPreSorted, dataset.isDataPreSorted(), false);		
		writeElementDataset(dataset);
		writer.closeElement(true);
	}


	private void writeCrosstabWhenNoDataCell(JRCrosstab crosstab) throws IOException
	{
		JRCellContents whenNoDataCell = crosstab.getWhenNoDataCell();
		if (whenNoDataCell != null)
		{
			writer.startElement("whenNoDataCell");
			writeCellContents(whenNoDataCell);
			writer.closeElement();
		}
	}


	private void writeCrosstabHeaderCell(JRCrosstab crosstab) throws IOException
	{
		JRCellContents headerCell = crosstab.getHeaderCell();
		if (headerCell != null)
		{
			writer.startElement("crosstabHeaderCell");
			writeCellContents(headerCell);
			writer.closeElement();
		}
	}
	
	
	protected void writeCrosstabRowGroup(JRCrosstabRowGroup group) throws IOException
	{
		writer.startElement("rowGroup");
		writer.addEncodedAttribute(JRCrosstabGroupFactory.ATTRIBUTE_name, group.getName());
		writer.addAttribute(JRCrosstabRowGroupFactory.ATTRIBUTE_width, group.getWidth());
		writer.addAttribute(JRCrosstabGroupFactory.ATTRIBUTE_totalPosition, group.getTotalPosition(), JRXmlConstants.getCrosstabTotalPositionMap(), BucketDefinition.TOTAL_POSITION_NONE);
		writer.addAttribute(JRCrosstabRowGroupFactory.ATTRIBUTE_headerPosition, group.getPosition(), JRXmlConstants.getCrosstabRowPositionMap(), JRCellContents.POSITION_Y_TOP);

		writeBucket(group.getBucket());
		
		JRCellContents header = group.getHeader();
		writer.startElement("crosstabRowHeader");
		writeCellContents(header);
		writer.closeElement();
		
		JRCellContents totalHeader = group.getTotalHeader();
		writer.startElement("crosstabTotalRowHeader");
		writeCellContents(totalHeader);
		writer.closeElement();
		
		writer.closeElement();		
	}
	
	
	protected void writeCrosstabColumnGroup(JRCrosstabColumnGroup group) throws IOException
	{
		writer.startElement("columnGroup");
		writer.addEncodedAttribute(JRCrosstabGroupFactory.ATTRIBUTE_name, group.getName());
		writer.addAttribute(JRCrosstabColumnGroupFactory.ATTRIBUTE_height, group.getHeight());
		writer.addAttribute(JRCrosstabGroupFactory.ATTRIBUTE_totalPosition, group.getTotalPosition(), JRXmlConstants.getCrosstabTotalPositionMap(), BucketDefinition.TOTAL_POSITION_NONE);
		writer.addAttribute(JRCrosstabColumnGroupFactory.ATTRIBUTE_headerPosition, group.getPosition(), JRXmlConstants.getCrosstabColumnPositionMap(), JRCellContents.POSITION_X_LEFT);

		writeBucket(group.getBucket());
		
		JRCellContents header = group.getHeader();
		writer.startElement("crosstabColumnHeader");
		writeCellContents(header);
		writer.closeElement();
		
		JRCellContents totalHeader = group.getTotalHeader();
		writer.startElement("crosstabTotalColumnHeader");
		writeCellContents(totalHeader);
		writer.closeElement();
		
		writer.closeElement();		
	}


	protected void writeBucket(JRCrosstabBucket bucket) throws IOException
	{
		writer.startElement("bucket");
		writer.addAttribute(JRCrosstabBucketFactory.ATTRIBUTE_order, bucket.getOrder(), JRXmlConstants.getCrosstabBucketOrderMap(), BucketDefinition.ORDER_ASCENDING);
		writer.writeExpression("bucketExpression", bucket.getExpression(), true);
		writer.writeExpression("comparatorExpression", bucket.getComparatorExpression(), false);		
		writer.closeElement();
	}


	protected void writeCrosstabMeasure(JRCrosstabMeasure measure) throws IOException
	{
		writer.startElement("measure");
		writer.addEncodedAttribute(JRCrosstabMeasureFactory.ATTRIBUTE_name, measure.getName());
		writer.addAttribute(JRCrosstabMeasureFactory.ATTRIBUTE_class, measure.getValueClassName());
		writer.addAttribute(JRCrosstabMeasureFactory.ATTRIBUTE_calculation, measure.getCalculation(), JRXmlConstants.getCalculationMap(), JRVariable.CALCULATION_NOTHING);
		writer.addAttribute(JRCrosstabMeasureFactory.ATTRIBUTE_percentageOf, measure.getPercentageOfType(), JRXmlConstants.getCrosstabPercentageMap(), JRCrosstabMeasure.PERCENTAGE_TYPE_NONE);
		writer.addAttribute(JRCrosstabMeasureFactory.ATTRIBUTE_percentageCalculatorClass, measure.getPercentageCalculatorClassName());
		writer.writeExpression("measureExpression", measure.getValueExpression(), false);
		writer.closeElement();
	}


	protected void writeCrosstabCell(JRCrosstabCell cell) throws IOException
	{
		writer.startElement("crosstabCell");
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
			writer.startElement("cellContents");
			writer.addAttribute(JRCellContentsFactory.ATTRIBUTE_backcolor, contents.getBackcolor());
			writer.addAttribute("mode", contents.getMode(), JRXmlConstants.getModeMap());
			JRStyle style = contents.getStyle();
			if (style != null)
			{
				writer.addEncodedAttribute(JRCellContentsFactory.ATTRIBUTE_style, style.getName());
			}
			
			writeBox(contents.getBox());
			
			List children = contents.getChildren();
			if (children != null)
			{
				for (Iterator it = children.iterator(); it.hasNext();)
				{
					JRChild element = (JRChild) it.next();
					element.writeXml(this);
				}
			}
			
			writer.closeElement();
		}
	}


	protected void writeCrosstabParameter(JRCrosstabParameter parameter) throws IOException
	{
		writer.startElement("crosstabParameter");
		writer.addEncodedAttribute("name", parameter.getName());
		writer.addAttribute("class", parameter.getValueClassName(), "java.lang.String");
		writer.writeExpression("parameterValueExpression", parameter.getExpression(), false);
		writer.closeElement();
	}


	public void writeDataset(JRDataset dataset) throws IOException
	{
		writer.startElement(JRDatasetFactory.TAG_SUB_DATASET);
		writer.addEncodedAttribute(JRDatasetFactory.ATTRIBUTE_name, dataset.getName());
		writer.addAttribute(JRDatasetFactory.ATTRIBUTE_scriptletClass, dataset.getScriptletClass());
		writer.addEncodedAttribute(JRDatasetFactory.ATTRIBUTE_resourceBundle, dataset.getResourceBundle());
		writer.addAttribute(JRDatasetFactory.ATTRIBUTE_whenResourceMissingType, dataset.getWhenResourceMissingType(), JRXmlConstants.getWhenResourceMissingTypeMap(), JRReport.WHEN_RESOURCE_MISSING_TYPE_NULL);
		
		writeProperties(dataset.getPropertiesMap());
		
		writeDatasetContents(dataset);
		
		writer.closeElement();
	}
	
	protected void writeDatasetContents(JRDataset dataset) throws IOException
	{
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
		
		writer.writeExpression(JRDatasetFactory.TAG_FILTER_EXPRESSION, dataset.getFilterExpression(), false);

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
	
	
	protected void writeDatasetRun(JRDatasetRun datasetRun) throws IOException
	{
		writer.startElement(JRDatasetRunFactory.TAG_DATASET_RUN);
		writer.addEncodedAttribute(JRDatasetRunFactory.ATTRIBUTE_subDataset, datasetRun.getDatasetName());
		
		writer.writeExpression("parametersMapExpression", datasetRun.getParametersMapExpression(), false);

		/*   */
		JRDatasetParameter[] parameters = datasetRun.getParameters();
		if (parameters != null && parameters.length > 0)
		{
			for(int i = 0; i < parameters.length; i++)
			{
				writeDatasetParameter(parameters[i]);
			}
		}

		writer.writeExpression("connectionExpression", datasetRun.getConnectionExpression(), false);
		writer.writeExpression("dataSourceExpression", datasetRun.getDataSourceExpression(), false);

		writer.closeElement();
	}
	
	
	public void writeFrame(JRFrame frame) throws IOException
	{
		writer.startElement(JRFrameFactory.TAG_FRAME);
		
		writeReportElement(frame);
		writeBox(frame);
		
		List children = frame.getChildren();
		if (children != null)
		{
			for (Iterator it = children.iterator(); it.hasNext();)
			{
				JRChild element = (JRChild) it.next();
				element.writeXml(this);
			}
		}
		
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
			writer.startElement(JRHyperlinkParameterFactory.TAG_hyperlinkParameter);
			writer.addEncodedAttribute(JRHyperlinkParameterFactory.ATTRIBUTE_name, parameter.getName());
			
			writer.writeExpression(JRHyperlinkParameterExpressionFactory.TAG_VALUE_EXPRESSION,
					parameter.getValueExpression(), true, String.class.getName());
			
			writer.closeElement();
		}
	}
	
	
	protected void writeHyperlink(String tagName, JRHyperlink hyperlink) throws IOException
	{
		if (hyperlink != null)
		{
			writer.startElement(tagName);
			
			writer.addEncodedAttribute("hyperlinkType", hyperlink.getLinkType());
			writer.addAttribute("hyperlinkTarget", hyperlink.getHyperlinkTarget(), JRXmlConstants.getHyperlinkTargetMap(), JRHyperlink.HYPERLINK_TARGET_SELF);
			
			writer.writeExpression("hyperlinkReferenceExpression", hyperlink.getHyperlinkReferenceExpression(), false);
			writer.writeExpression("hyperlinkAnchorExpression", hyperlink.getHyperlinkAnchorExpression(), false);
			writer.writeExpression("hyperlinkPageExpression", hyperlink.getHyperlinkPageExpression(), false);
			writer.writeExpression(JRHyperlinkFactory.ELEMENT_HYPERLINK_TOOLTIP_EXPRESSION, hyperlink.getHyperlinkTooltipExpression(), false);
			writeHyperlinkParameters(hyperlink.getHyperlinkParameters());
			
			writer.closeElement();
		}
	}
}
