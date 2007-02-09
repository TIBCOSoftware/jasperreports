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
import net.sf.jasperreports.crosstabs.xml.JRCrosstabParameterFactory;
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
		writer.writePublicDoctype(JasperDesignFactory.ELEMENT_jasperReport, JasperDesignFactory.DOCUMENT_docType, JasperDesignFactory.DOCUMENT_uri);

		writer.startElement(JasperDesignFactory.ELEMENT_jasperReport);
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
					writer.startElement(JasperDesignFactory.ELEMENT_import);
					writer.addEncodedAttribute(JasperDesignFactory.ATTRIBUTE_value, value);
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
			writer.startElement(JasperDesignFactory.ELEMENT_background);
			writeBand(report.getBackground());
			writer.closeElement();
		}

		if (report.getTitle() != null)
		{
			writer.startElement(JasperDesignFactory.ELEMENT_title);
			writeBand(report.getTitle());
			writer.closeElement();
		}

		if (report.getPageHeader() != null)
		{
			writer.startElement(JasperDesignFactory.ELEMENT_pageHeader);
			writeBand(report.getPageHeader());
			writer.closeElement();
		}

		if (report.getColumnHeader() != null)
		{
			writer.startElement(JasperDesignFactory.ELEMENT_columnHeader);
			writeBand(report.getColumnHeader());
			writer.closeElement();
		}

		if (report.getDetail() != null)
		{
			writer.startElement(JasperDesignFactory.ELEMENT_detail);
			writeBand(report.getDetail());
			writer.closeElement();
		}

		if (report.getColumnFooter() != null)
		{
			writer.startElement(JasperDesignFactory.ELEMENT_columnFooter);
			writeBand(report.getColumnFooter());
			writer.closeElement();
		}

		if (report.getPageFooter() != null)
		{
			writer.startElement(JasperDesignFactory.ELEMENT_pageFooter);
			writeBand(report.getPageFooter());
			writer.closeElement();
		}

		if (report.getLastPageFooter() != null)
		{
			writer.startElement(JasperDesignFactory.ELEMENT_lastPageFooter);
			writeBand(report.getLastPageFooter());
			writer.closeElement();
		}

		if (report.getSummary() != null)
		{
			writer.startElement(JasperDesignFactory.ELEMENT_summary);
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
					writer.startElement(JasperDesignFactory.ELEMENT_property);
					writer.addEncodedAttribute(JasperDesignFactory.ATTRIBUTE_name, propertyNames[i]);
					writer.addEncodedAttribute(JasperDesignFactory.ATTRIBUTE_value, value);
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
		writer.startElement(JRReportFontFactory.ELEMENT_reportFont);
		writer.addEncodedAttribute(JRReportFontFactory.ATTRIBUTE_name, font.getName());
		writer.addAttribute(JRReportFontFactory.ATTRIBUTE_isDefault, font.isDefault());
		writer.addEncodedAttribute(JRReportFontFactory.ATTRIBUTE_fontName, font.getFontName());
		writer.addAttribute(JRReportFontFactory.ATTRIBUTE_size, font.getFontSize());
		writer.addAttribute(JRReportFontFactory.ATTRIBUTE_isBold, font.isBold());
		writer.addAttribute(JRReportFontFactory.ATTRIBUTE_isItalic, font.isItalic());
		writer.addAttribute(JRReportFontFactory.ATTRIBUTE_isUnderline, font.isUnderline());
		writer.addAttribute(JRReportFontFactory.ATTRIBUTE_isStrikeThrough, font.isStrikeThrough());
		writer.addEncodedAttribute(JRReportFontFactory.ATTRIBUTE_pdfFontName, font.getPdfFontName());
		writer.addEncodedAttribute(JRReportFontFactory.ATTRIBUTE_pdfEncoding, font.getPdfEncoding());
		writer.addAttribute(JRReportFontFactory.ATTRIBUTE_isPdfEmbedded, font.isPdfEmbedded());
		writer.closeElement();
	}


	/**
	 *
	 */
	private void writeStyle(JRStyle style) throws IOException
	{
		writer.startElement(JRStyleFactory.ELEMENT_style);
		writer.addEncodedAttribute(JRStyleFactory.ATTRIBUTE_name, style.getName());
		writer.addAttribute(JRStyleFactory.ATTRIBUTE_isDefault, style.isDefault());

		if (style.getStyle() != null)
		{
			JRStyle baseStyle = 
				(JRStyle)stylesMap.get(
						style.getStyle().getName()
					);
			if(baseStyle != null)
			{
				writer.addEncodedAttribute(JRStyleFactory.ATTRIBUTE_style, style.getStyle().getName());
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
	
		writer.addAttribute(JRStyleFactory.ATTRIBUTE_mode, style.getOwnMode(), JRXmlConstants.getModeMap());
		writer.addAttribute(JRStyleFactory.ATTRIBUTE_forecolor, style.getOwnForecolor());
		writer.addAttribute(JRStyleFactory.ATTRIBUTE_backcolor, style.getOwnBackcolor());
		writer.addAttribute(JRStyleFactory.ATTRIBUTE_pen, style.getOwnPen(), JRXmlConstants.getPenMap());
		writer.addAttribute(JRStyleFactory.ATTRIBUTE_fill, style.getOwnFill(), JRXmlConstants.getFillMap());
		writer.addAttribute(JRStyleFactory.ATTRIBUTE_radius, style.getOwnRadius());
		writer.addAttribute(JRStyleFactory.ATTRIBUTE_scaleImage, style.getOwnScaleImage(), JRXmlConstants.getScaleImageMap());
		writer.addAttribute(JRStyleFactory.ATTRIBUTE_hAlign, style.getOwnHorizontalAlignment(), JRXmlConstants.getHorizontalAlignMap());
		writer.addAttribute(JRStyleFactory.ATTRIBUTE_vAlign, style.getOwnVerticalAlignment(), JRXmlConstants.getVerticalAlignMap());
		writer.addAttribute(JRStyleFactory.ATTRIBUTE_rotation, style.getOwnRotation(), JRXmlConstants.getRotationMap());
		writer.addAttribute(JRStyleFactory.ATTRIBUTE_lineSpacing, style.getOwnLineSpacing(), JRXmlConstants.getLineSpacingMap());
		writer.addAttribute(JRStyleFactory.ATTRIBUTE_isStyledText, style.isOwnStyledText());
		writer.addEncodedAttribute(JRStyleFactory.ATTRIBUTE_pattern, style.getOwnPattern());
		writer.addAttribute(JRStyleFactory.ATTRIBUTE_isBlankWhenNull, style.isOwnBlankWhenNull());
		
		writer.addAttribute(JRStyleFactory.ATTRIBUTE_border, style.getOwnBorder(), JRXmlConstants.getPenMap());
		writer.addAttribute(JRStyleFactory.ATTRIBUTE_borderColor, style.getOwnBorderColor());
		writer.addAttribute(JRStyleFactory.ATTRIBUTE_padding, style.getOwnPadding());
		
		writer.addAttribute(JRStyleFactory.ATTRIBUTE_topBorder, style.getOwnTopBorder(), JRXmlConstants.getPenMap());
		writer.addAttribute(JRStyleFactory.ATTRIBUTE_topBorderColor, style.getOwnTopBorderColor());
		writer.addAttribute(JRStyleFactory.ATTRIBUTE_topPadding, style.getOwnTopPadding());
		
		writer.addAttribute(JRStyleFactory.ATTRIBUTE_leftBorder, style.getOwnLeftBorder(), JRXmlConstants.getPenMap());
		writer.addAttribute(JRStyleFactory.ATTRIBUTE_leftBorderColor, style.getOwnLeftBorderColor());
		writer.addAttribute(JRStyleFactory.ATTRIBUTE_leftPadding, style.getOwnLeftPadding());
		
		writer.addAttribute(JRStyleFactory.ATTRIBUTE_bottomBorder, style.getOwnBottomBorder(), JRXmlConstants.getPenMap());
		writer.addAttribute(JRStyleFactory.ATTRIBUTE_bottomBorderColor, style.getOwnBottomBorderColor());
		writer.addAttribute(JRStyleFactory.ATTRIBUTE_bottomPadding, style.getOwnBottomPadding());
		
		writer.addAttribute(JRStyleFactory.ATTRIBUTE_rightBorder, style.getOwnRightBorder(), JRXmlConstants.getPenMap());
		writer.addAttribute(JRStyleFactory.ATTRIBUTE_rightBorderColor, style.getOwnRightBorderColor());
		writer.addAttribute(JRStyleFactory.ATTRIBUTE_rightPadding, style.getOwnRightPadding());

		writer.addEncodedAttribute(JRStyleFactory.ATTRIBUTE_fontName, style.getOwnFontName());
		writer.addAttribute(JRStyleFactory.ATTRIBUTE_fontSize, style.getOwnFontSize());
		writer.addAttribute(JRStyleFactory.ATTRIBUTE_isBold, style.isOwnBold());
		writer.addAttribute(JRStyleFactory.ATTRIBUTE_isItalic, style.isOwnItalic());
		writer.addAttribute(JRStyleFactory.ATTRIBUTE_isUnderline, style.isOwnUnderline());
		writer.addAttribute(JRStyleFactory.ATTRIBUTE_isStrikeThrough, style.isOwnStrikeThrough());
		writer.addEncodedAttribute(JRStyleFactory.ATTRIBUTE_pdfFontName, style.getOwnPdfFontName());
		writer.addEncodedAttribute(JRStyleFactory.ATTRIBUTE_pdfEncoding, style.getOwnPdfEncoding());
		writer.addAttribute(JRStyleFactory.ATTRIBUTE_isPdfEmbedded, style.isOwnPdfEmbedded());

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
		writer.startElement(JRConditionalStyleFactory.ELEMENT_conditionalStyle);
		writer.writeExpression(JRConditionalStyleFactory.ELEMENT_conditionExpression, style.getConditionExpression(), false);
		writeStyle(style);
		writer.closeElement();
	}


	/**
	 *
	 */
	private void writeParameter(JRParameter parameter) throws IOException
	{
		writer.startElement(JRParameterFactory.ELEMENT_parameter);
		writer.addEncodedAttribute(JRParameterFactory.ATTRIBUTE_name, parameter.getName());
		writer.addAttribute(JRParameterFactory.ATTRIBUTE_class, parameter.getValueClassName());
		writer.addAttribute(JRParameterFactory.ATTRIBUTE_isForPrompting, parameter.isForPrompting(), true);

		writer.writeCDATAElement(JRParameterFactory.ELEMENT_parameterDescription, parameter.getDescription());
		writer.writeExpression(JRParameterFactory.ELEMENT_defaultValueExpression, parameter.getDefaultValueExpression(), false);

		writer.closeElement();
	}


	/**
	 *
	 */
	private void writeQuery(JRQuery query) throws IOException
	{
		writer.startElement(JRQueryFactory.ELEMENT_queryString);
		writer.addEncodedAttribute(JRQueryFactory.ATTRIBUTE_language, query.getLanguage(), JRJdbcQueryExecuterFactory.QUERY_LANGUAGE_SQL);
		writer.writeCDATA(query.getText());
		writer.closeElement();
	}


	/**
	 *
	 */
	private void writeField(JRField field) throws IOException
	{
		writer.startElement(JRFieldFactory.ELEMENT_field);
		writer.addEncodedAttribute(JRFieldFactory.ATTRIBUTE_name, field.getName());
		writer.addAttribute(JRFieldFactory.ATTRIBUTE_class, field.getValueClassName());

		writer.writeCDATAElement(JRFieldFactory.ELEMENT_fieldDescription, field.getDescription());
		
		writer.closeElement();
	}


	/**
	 *
	 */
	private void writeSortField(JRSortField sortField) throws IOException
	{
		writer.startElement(JRSortFieldFactory.ELEMENT_sortField);
		writer.addEncodedAttribute(JRSortFieldFactory.ATTRIBUTE_name, sortField.getName());
		writer.addAttribute(JRSortFieldFactory.ATTRIBUTE_order, sortField.getOrder(), JRXmlConstants.getSortOrderMap(), JRSortField.SORT_ORDER_ASCENDING);
		writer.closeElement();
	}


	/**
	 *
	 */
	private void writeVariable(JRVariable variable) throws IOException
	{
		writer.startElement(JRVariableFactory.ELEMENT_variable);
		writer.addEncodedAttribute(JRVariableFactory.ATTRIBUTE_name, variable.getName());
		writer.addAttribute(JRVariableFactory.ATTRIBUTE_class, variable.getValueClassName());
		writer.addAttribute(JRVariableFactory.ATTRIBUTE_resetType, variable.getResetType(), JRXmlConstants.getResetTypeMap(), JRVariable.RESET_TYPE_REPORT);
		if (variable.getResetGroup() != null)
		{
			writer.addEncodedAttribute(JRVariableFactory.ATTRIBUTE_resetGroup, variable.getResetGroup().getName());
		}
		writer.addAttribute(JRVariableFactory.ATTRIBUTE_incrementType, variable.getIncrementType(), JRXmlConstants.getResetTypeMap(), JRVariable.RESET_TYPE_NONE);
		if (variable.getIncrementGroup() != null)
		{
			writer.addEncodedAttribute(JRVariableFactory.ATTRIBUTE_incrementGroup, variable.getIncrementGroup().getName());
		}
		writer.addAttribute(JRVariableFactory.ATTRIBUTE_calculation, variable.getCalculation(), JRXmlConstants.getCalculationMap(), JRVariable.CALCULATION_NOTHING);
		writer.addAttribute(JRVariableFactory.ATTRIBUTE_incrementerFactoryClass, variable.getIncrementerFactoryClassName());

		writer.writeExpression(JRVariableFactory.ELEMENT_variableExpression, variable.getExpression(), false);
		writer.writeExpression(JRVariableFactory.ELEMENT_initialValueExpression, variable.getInitialValueExpression(), false);
		
		writer.closeElement();
	}


	/**
	 *
	 */
	private void writeGroup(JRGroup group) throws IOException
	{
		writer.startElement(JRGroupFactory.ELEMENT_group);
		writer.addEncodedAttribute(JRGroupFactory.ATTRIBUTE_name, group.getName());
		writer.addAttribute(JRGroupFactory.ATTRIBUTE_isStartNewColumn, group.isStartNewColumn(), false);
		writer.addAttribute(JRGroupFactory.ATTRIBUTE_isStartNewPage, group.isStartNewPage(), false);
		writer.addAttribute(JRGroupFactory.ATTRIBUTE_isResetPageNumber, group.isResetPageNumber(), false);
		writer.addAttribute(JRGroupFactory.ATTRIBUTE_isReprintHeaderOnEachPage, group.isReprintHeaderOnEachPage(), false);
		writer.addAttributePositive(JRGroupFactory.ATTRIBUTE_minHeightToStartNewPage, group.getMinHeightToStartNewPage());

		writer.writeExpression(JRGroupFactory.ELEMENT_groupExpression, group.getExpression(), false);

		if (group.getGroupHeader() != null)
		{
			writer.startElement(JRGroupFactory.ELEMENT_groupHeader);
			writeBand(group.getGroupHeader());
			writer.closeElement();
		}

		if (group.getGroupFooter() != null)
		{
			writer.startElement(JRGroupFactory.ELEMENT_groupFooter);
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
		writer.startElement(JRBandFactory.ELEMENT_band);
		writer.addAttributePositive(JRBandFactory.ATTRIBUTE_height, band.getHeight());
		writer.addAttribute(JRBandFactory.ATTRIBUTE_isSplitAllowed, band.isSplitAllowed(), true);

		writer.writeExpression(JRBandFactory.ELEMENT_printWhenExpression, band.getPrintWhenExpression(), false);

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
		writer.startElement(JRElementGroupFactory.ELEMENT_elementGroup);

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
		writer.startElement(JRBreakFactory.ELEMENT_break);
		writer.addAttribute(JRBreakFactory.ATTRIBUTE_type, breakElement.getType(), JRXmlConstants.getBreakTypeMap(), JRBreak.TYPE_PAGE);

		writeReportElement(breakElement);

		writer.closeElement();
	}


	/**
	 *
	 */
	public void writeLine(JRLine line) throws IOException
	{
		writer.startElement(JRLineFactory.ELEMENT_line);
		writer.addAttribute(JRLineFactory.ATTRIBUTE_direction, line.getDirection(), JRXmlConstants.getDirectionMap(), JRLine.DIRECTION_TOP_DOWN);

		writeReportElement(line);
		writeGraphicElement(line);

		writer.closeElement();
	}


	/**
	 *
	 */
	private void writeReportElement(JRElement element) throws IOException
	{
		writer.startElement(JRElementFactory.ELEMENT_reportElement);
		writer.addEncodedAttribute(JRElementFactory.ATTRIBUTE_key, element.getKey());
		JRStyle style = element.getStyle();
		if (style != null)
		{
			writer.addEncodedAttribute(JRElementFactory.ATTRIBUTE_style, style.getName());
		}
		writer.addAttribute(JRElementFactory.ATTRIBUTE_positionType, element.getPositionType(), JRXmlConstants.getPositionTypeMap(), JRElement.POSITION_TYPE_FIX_RELATIVE_TO_TOP);
		writer.addAttribute(JRElementFactory.ATTRIBUTE_stretchType, element.getStretchType(), JRXmlConstants.getStretchTypeMap(), JRElement.STRETCH_TYPE_NO_STRETCH);
		writer.addAttribute(JRElementFactory.ATTRIBUTE_isPrintRepeatedValues, element.isPrintRepeatedValues(), true);
		writer.addAttribute(JRElementFactory.ATTRIBUTE_mode, element.getOwnMode(), JRXmlConstants.getModeMap());

		writer.addAttribute(JRElementFactory.ATTRIBUTE_x, element.getX());
		writer.addAttribute(JRElementFactory.ATTRIBUTE_y, element.getY());
		writer.addAttribute(JRElementFactory.ATTRIBUTE_width, element.getWidth());
		writer.addAttribute(JRElementFactory.ATTRIBUTE_height, element.getHeight());
		writer.addAttribute(JRElementFactory.ATTRIBUTE_isRemoveLineWhenBlank, element.isRemoveLineWhenBlank(), false);
		writer.addAttribute(JRElementFactory.ATTRIBUTE_isPrintInFirstWholeBand, element.isPrintInFirstWholeBand(), false);
		writer.addAttribute(JRElementFactory.ATTRIBUTE_isPrintWhenDetailOverflows, element.isPrintWhenDetailOverflows(), false);

		if (element.getPrintWhenGroupChanges() != null)
		{
			writer.addEncodedAttribute(JRElementFactory.ATTRIBUTE_printWhenGroupChanges, element.getPrintWhenGroupChanges().getName());
		}
		
		writer.addAttribute(JRElementFactory.ATTRIBUTE_forecolor, element.getOwnForecolor());
		writer.addAttribute(JRElementFactory.ATTRIBUTE_backcolor, element.getOwnBackcolor());
		
		writer.writeExpression(JRElementFactory.ELEMENT_printWhenExpression, element.getPrintWhenExpression(), false);
		writer.closeElement();
	}


	/**
	 *
	 */
	private void writeGraphicElement(JRGraphicElement element) throws IOException
	{
		writer.startElement(JRGraphicElementFactory.ELEMENT_graphicElement);
		writer.addAttribute(JRGraphicElementFactory.ATTRIBUTE_pen, element.getOwnPen(), JRXmlConstants.getPenMap());
		writer.addAttribute(JRGraphicElementFactory.ATTRIBUTE_fill, element.getOwnFill(), JRXmlConstants.getFillMap());
		writer.closeElement();
	}


	/**
	 *
	 */
	public void writeRectangle(JRRectangle rectangle) throws IOException
	{
		writer.startElement(JRRectangleFactory.ELEMENT_rectangle);
		writer.addAttribute(JRRectangleFactory.ATTRIBUTE_radius, rectangle.getOwnRadius());

		writeReportElement(rectangle);
		writeGraphicElement(rectangle);

		writer.closeElement();
	}


	/**
	 *
	 */
	public void writeEllipse(JREllipse ellipse) throws IOException
	{
		writer.startElement(JREllipseFactory.ELEMENT_ellipse);

		writeReportElement(ellipse);
		writeGraphicElement(ellipse);

		writer.closeElement();
	}


	/**
	 *
	 */
	public void writeImage(JRImage image) throws IOException
	{
		writer.startElement(JRImageFactory.ELEMENT_image);
		writer.addAttribute(JRImageFactory.ATTRIBUTE_scaleImage, image.getOwnScaleImage(), JRXmlConstants.getScaleImageMap());
		writer.addAttribute(JRImageFactory.ATTRIBUTE_hAlign, image.getOwnHorizontalAlignment(), JRXmlConstants.getHorizontalAlignMap());
		writer.addAttribute(JRImageFactory.ATTRIBUTE_vAlign, image.getOwnVerticalAlignment(), JRXmlConstants.getVerticalAlignMap());
		writer.addAttribute(JRImageFactory.ATTRIBUTE_isUsingCache, image.isOwnUsingCache());
		writer.addAttribute(JRImageFactory.ATTRIBUTE_isLazy, image.isLazy(), false);
		writer.addAttribute(JRImageFactory.ATTRIBUTE_onErrorType, image.getOnErrorType(), JRXmlConstants.getOnErrorTypeMap(), JRImage.ON_ERROR_TYPE_ERROR);
		writer.addAttribute(JRImageFactory.ATTRIBUTE_evaluationTime, image.getEvaluationTime(), JRXmlConstants.getEvaluationTimeMap(), JRExpression.EVALUATION_TIME_NOW);

		if (image.getEvaluationGroup() != null)
		{
			writer.addEncodedAttribute(JRImageFactory.ATTRIBUTE_evaluationGroup, image.getEvaluationGroup().getName());
		}

		writer.addEncodedAttribute(JRImageFactory.ATTRIBUTE_hyperlinkType, image.getLinkType());
		writer.addAttribute(JRImageFactory.ATTRIBUTE_hyperlinkTarget, image.getHyperlinkTarget(), JRXmlConstants.getHyperlinkTargetMap(), JRHyperlink.HYPERLINK_TARGET_SELF);
		writer.addAttribute(JRImageFactory.ATTRIBUTE_bookmarkLevel, image.getBookmarkLevel(), JRAnchor.NO_BOOKMARK);

		writeReportElement(image);
		writeBox(image);
		writeGraphicElement(image);

		//FIXME class is mandatory in verifier
		
		writer.writeExpression(JRImageFactory.ELEMENT_imageExpression, image.getExpression(), true);
		writer.writeExpression(JRHyperlinkFactory.ELEMENT_anchorNameExpression, image.getAnchorNameExpression(), false);
		writer.writeExpression(JRHyperlinkFactory.ELEMENT_hyperlinkReferenceExpression, image.getHyperlinkReferenceExpression(), false);
		writer.writeExpression(JRHyperlinkFactory.ELEMENT_hyperlinkAnchorExpression, image.getHyperlinkAnchorExpression(), false);
		writer.writeExpression(JRHyperlinkFactory.ELEMENT_hyperlinkPageExpression, image.getHyperlinkPageExpression(), false);
		writer.writeExpression(JRHyperlinkFactory.ELEMENT_hyperlinkTooltipExpression, image.getHyperlinkTooltipExpression(), false);
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
			writer.startElement(JRBoxFactory.ELEMENT_box);
			writer.addAttribute(JRBoxFactory.ATTRIBUTE_border, box.getOwnBorder(), JRXmlConstants.getPenMap());
			writer.addAttribute(JRBoxFactory.ATTRIBUTE_borderColor, box.getOwnBorderColor());
			writer.addAttribute(JRBoxFactory.ATTRIBUTE_padding, box.getOwnPadding());

			writer.addAttribute(JRBoxFactory.ATTRIBUTE_topBorder, box.getOwnTopBorder(), JRXmlConstants.getPenMap());
			writer.addAttribute(JRBoxFactory.ATTRIBUTE_topBorderColor, box.getOwnTopBorderColor());
			writer.addAttribute(JRBoxFactory.ATTRIBUTE_topPadding, box.getOwnTopPadding());

			writer.addAttribute(JRBoxFactory.ATTRIBUTE_leftBorder, box.getOwnLeftBorder(), JRXmlConstants.getPenMap());
			writer.addAttribute(JRBoxFactory.ATTRIBUTE_leftBorderColor, box.getOwnLeftBorderColor());
			writer.addAttribute(JRBoxFactory.ATTRIBUTE_leftPadding, box.getOwnLeftPadding());

			writer.addAttribute(JRBoxFactory.ATTRIBUTE_bottomBorder, box.getOwnBottomBorder(), JRXmlConstants.getPenMap());
			writer.addAttribute(JRBoxFactory.ATTRIBUTE_bottomBorderColor, box.getOwnBottomBorderColor());
			writer.addAttribute(JRBoxFactory.ATTRIBUTE_bottomPadding, box.getOwnBottomPadding());

			writer.addAttribute(JRBoxFactory.ATTRIBUTE_rightBorder, box.getOwnRightBorder(), JRXmlConstants.getPenMap());
			writer.addAttribute(JRBoxFactory.ATTRIBUTE_rightBorderColor, box.getOwnRightBorderColor());
			writer.addAttribute(JRBoxFactory.ATTRIBUTE_rightPadding, box.getOwnRightPadding());
			
			writer.closeElement(true);
		}
	}


	/**
	 *
	 */
	public void writeStaticText(JRStaticText staticText) throws IOException
	{
		writer.startElement(JRStaticTextFactory.ELEMENT_staticText);

		writeReportElement(staticText);
		writeBox(staticText);
		writeTextElement(staticText);

		writer.writeCDATAElement(JRStaticTextFactory.ELEMENT_text, staticText.getText());

		writer.closeElement();
	}


	/**
	 *
	 */
	private void writeTextElement(JRTextElement textElement) throws IOException
	{
		writer.startElement(JRTextElementFactory.ELEMENT_textElement);
		writer.addAttribute(JRTextElementFactory.ATTRIBUTE_textAlignment, textElement.getOwnHorizontalAlignment(), JRXmlConstants.getHorizontalAlignMap());
		writer.addAttribute(JRTextElementFactory.ATTRIBUTE_verticalAlignment, textElement.getOwnVerticalAlignment(), JRXmlConstants.getVerticalAlignMap());
		writer.addAttribute(JRTextElementFactory.ATTRIBUTE_rotation, textElement.getOwnRotation(), JRXmlConstants.getRotationMap());
		writer.addAttribute(JRTextElementFactory.ATTRIBUTE_lineSpacing, textElement.getOwnLineSpacing(), JRXmlConstants.getLineSpacingMap());
		writer.addAttribute(JRTextElementFactory.ATTRIBUTE_isStyledText, textElement.isOwnStyledText());

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
			writer.startElement(JRFontFactory.ELEMENT_font);
			if (font.getReportFont() != null)
			{
				JRFont baseFont = 
					(JRFont)fontsMap.get(
						font.getReportFont().getName()
						);
				if(baseFont != null)
				{
					writer.addEncodedAttribute(JRFontFactory.ATTRIBUTE_reportFont, font.getReportFont().getName());
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
		
			writer.addEncodedAttribute(JRFontFactory.ATTRIBUTE_fontName, font.getOwnFontName());
			writer.addAttribute(JRFontFactory.ATTRIBUTE_size, font.getOwnFontSize());
			writer.addAttribute(JRFontFactory.ATTRIBUTE_isBold, font.isOwnBold());
			writer.addAttribute(JRFontFactory.ATTRIBUTE_isItalic, font.isOwnItalic());
			writer.addAttribute(JRFontFactory.ATTRIBUTE_isUnderline, font.isOwnUnderline());
			writer.addAttribute(JRFontFactory.ATTRIBUTE_isStrikeThrough, font.isOwnStrikeThrough());
			writer.addEncodedAttribute(JRFontFactory.ATTRIBUTE_pdfFontName, font.getOwnPdfFontName());
			writer.addEncodedAttribute(JRFontFactory.ATTRIBUTE_pdfEncoding, font.getOwnPdfEncoding());
			writer.addAttribute(JRFontFactory.ATTRIBUTE_isPdfEmbedded, font.isOwnPdfEmbedded());
			writer.closeElement(true);
		}
	}


	/**
	 *
	 */
	public void writeTextField(JRTextField textField) throws IOException
	{
		writer.startElement(JRTextFieldFactory.ELEMENT_textField);
		writer.addAttribute(JRTextFieldFactory.ATTRIBUTE_isStretchWithOverflow, textField.isStretchWithOverflow(), false);
		writer.addAttribute(JRTextFieldFactory.ATTRIBUTE_evaluationTime, textField.getEvaluationTime(), JRXmlConstants.getEvaluationTimeMap(), JRExpression.EVALUATION_TIME_NOW);

		if (textField.getEvaluationGroup() != null)
		{
			writer.addEncodedAttribute(JRTextFieldFactory.ATTRIBUTE_evaluationGroup, textField.getEvaluationGroup().getName());
		}

		writer.addEncodedAttribute(JRTextFieldFactory.ATTRIBUTE_pattern, textField.getOwnPattern());
		writer.addAttribute(JRTextFieldFactory.ATTRIBUTE_isBlankWhenNull, textField.isOwnBlankWhenNull());
		
		writer.addEncodedAttribute(JRTextFieldFactory.ATTRIBUTE_hyperlinkType, textField.getLinkType());
		writer.addAttribute(JRTextFieldFactory.ATTRIBUTE_hyperlinkTarget, textField.getHyperlinkTarget(), JRXmlConstants.getHyperlinkTargetMap(), JRHyperlink.HYPERLINK_TARGET_SELF);
		writer.addAttribute(JRTextFieldFactory.ATTRIBUTE_bookmarkLevel, textField.getBookmarkLevel(), JRAnchor.NO_BOOKMARK);

		writeReportElement(textField);
		writeBox(textField);
		writeTextElement(textField);

		writer.writeExpression(JRTextFieldExpressionFactory.ELEMENT_textFieldExpression, textField.getExpression(), true);
		
		writer.writeExpression(JRHyperlinkFactory.ELEMENT_anchorNameExpression, textField.getAnchorNameExpression(), false);
		writer.writeExpression(JRHyperlinkFactory.ELEMENT_hyperlinkReferenceExpression, textField.getHyperlinkReferenceExpression(), false);
		writer.writeExpression(JRHyperlinkFactory.ELEMENT_hyperlinkAnchorExpression, textField.getHyperlinkAnchorExpression(), false);
		writer.writeExpression(JRHyperlinkFactory.ELEMENT_hyperlinkPageExpression, textField.getHyperlinkPageExpression(), false);
		writer.writeExpression(JRHyperlinkFactory.ELEMENT_hyperlinkTooltipExpression, textField.getHyperlinkTooltipExpression(), false);
		writeHyperlinkParameters(textField.getHyperlinkParameters());
		
		writer.closeElement();
	}


	/**
	 *
	 */
	public void writeSubreport(JRSubreport subreport) throws IOException
	{
		writer.startElement(JRSubreportFactory.ELEMENT_subreport);
		writer.addAttribute(JRSubreportFactory.ATTRIBUTE_isUsingCache, subreport.isOwnUsingCache());

		writeReportElement(subreport);

		writer.writeExpression(JRSubreportFactory.ELEMENT_parametersMapExpression, subreport.getParametersMapExpression(), false);

		/*   */
		JRSubreportParameter[] parameters = subreport.getParameters();
		if (parameters != null && parameters.length > 0)
		{
			for(int i = 0; i < parameters.length; i++)
			{
				writeSubreportParameter(parameters[i]);
			}
		}

		writer.writeExpression(JRSubreportFactory.ELEMENT_connectionExpression, subreport.getConnectionExpression(), false);
		writer.writeExpression(JRSubreportFactory.ELEMENT_dataSourceExpression, subreport.getDataSourceExpression(), false);

		JRSubreportReturnValue[] returnValues = subreport.getReturnValues();
		if (returnValues != null && returnValues.length > 0)
		{
			for(int i = 0; i < returnValues.length; i++)
			{
				writeSubreportReturnValue(returnValues[i]);
			}
		}

		writer.writeExpression(JRSubreportExpressionFactory.ELEMENT_subreportExpression, subreport.getExpression(), true);
		
		writer.closeElement();
	}


	/**
	 *
	 */
	private void writeSubreportParameter(JRSubreportParameter subreportParameter) throws IOException
	{
		writer.startElement(JRSubreportParameterFactory.ELEMENT_subreportParameter);
		writer.addEncodedAttribute(JRSubreportParameterFactory.ATTRIBUTE_name, subreportParameter.getName());

		writer.writeExpression(JRSubreportParameterFactory.ELEMENT_subreportParameterExpression, subreportParameter.getExpression(), false);
		
		writer.closeElement();
	}


	private void writeDatasetParameter(JRDatasetParameter datasetParameter) throws IOException
	{
		writer.startElement(JRDatasetRunParameterFactory.ELEMENT_datasetParameter);
		writer.addEncodedAttribute(JRDatasetRunParameterFactory.ATTRIBUTE_name, datasetParameter.getName());

		writer.writeExpression(JRDatasetRunParameterExpressionFactory.ELEMENT_datasetParameterExpression, datasetParameter.getExpression(), false);
		
		writer.closeElement();
	}


	/**
	 *
	 */
	private void writeChart(JRChart chart) throws IOException
	{
		writer.startElement(JRChartFactory.ELEMENT_chart);
		writer.addAttribute(JRChartFactory.ATTRIBUTE_isShowLegend, chart.isShowLegend(), true);
		writer.addAttribute(JRChartFactory.ATTRIBUTE_evaluationTime, chart.getEvaluationTime(), JRXmlConstants.getEvaluationTimeMap(), JRExpression.EVALUATION_TIME_NOW);

		if (chart.getEvaluationTime() == JRExpression.EVALUATION_TIME_GROUP)
		{
			writer.addEncodedAttribute(JRChartFactory.ATTRIBUTE_evaluationGroup, chart.getEvaluationGroup().getName());
		}
		
		writer.addEncodedAttribute(JRChartFactory.ATTRIBUTE_hyperlinkType, chart.getLinkType());
		writer.addAttribute(JRChartFactory.ATTRIBUTE_hyperlinkTarget, chart.getHyperlinkTarget(), JRXmlConstants.getHyperlinkTargetMap(), JRHyperlink.HYPERLINK_TARGET_SELF);
		writer.addAttribute(JRChartFactory.ATTRIBUTE_bookmarkLevel, chart.getBookmarkLevel(), JRAnchor.NO_BOOKMARK);
		writer.addAttribute(JRChartFactory.ATTRIBUTE_customizerClass, chart.getCustomizerClass());

		writeReportElement(chart);
		writeBox(chart);

		// write title
		writer.startElement(JRChartFactory.ELEMENT_chartTitle);
		writer.addAttribute(JRChartFactory.JRChartTitleFactory.ATTRIBUTE_position, chart.getTitlePosition(), JRXmlConstants.getChartTitlePositionMap(), JRChart.TITLE_POSITION_TOP);
		writer.addAttribute(JRChartFactory.JRChartTitleFactory.ATTRIBUTE_color, chart.getOwnTitleColor());
		writeFont(chart.getTitleFont());
		if (chart.getTitleExpression() != null)
		{
			writer.writeExpression(JRChartFactory.ELEMENT_titleExpression, chart.getTitleExpression(), false);
		}
		writer.closeElement();

		// write subtitle
		writer.startElement(JRChartFactory.ELEMENT_chartSubtitle);
		writer.addAttribute(JRChartFactory.JRChartSubtitleFactory.ATTRIBUTE_color, chart.getOwnSubtitleColor());
		writeFont(chart.getSubtitleFont());
		if (chart.getSubtitleExpression() != null)
		{
			writer.writeExpression(JRChartFactory.ELEMENT_subtitleExpression, chart.getSubtitleExpression(), false);
		}
		writer.closeElement();
		
		// write chartLegend
		writer.startElement(JRChartFactory.ELEMENT_chartLegend);
		if (chart.getOwnLegendColor() != null)
			writer.addAttribute(JRChartFactory.JRChartLegendFactory.ATTRIBUTE_textColor, chart.getOwnLegendColor());
		if (chart.getOwnLegendBackgroundColor() != null)
			writer.addAttribute(JRChartFactory.JRChartLegendFactory.ATTRIBUTE_backgroundColor, chart.getOwnLegendBackgroundColor());
		writeFont(chart.getLegendFont());
		writer.closeElement();
		
		writer.writeExpression(JRHyperlinkFactory.ELEMENT_anchorNameExpression, chart.getAnchorNameExpression(), false);
		writer.writeExpression(JRHyperlinkFactory.ELEMENT_hyperlinkReferenceExpression, chart.getHyperlinkReferenceExpression(), false);
		writer.writeExpression(JRHyperlinkFactory.ELEMENT_hyperlinkAnchorExpression, chart.getHyperlinkAnchorExpression(), false);
		writer.writeExpression(JRHyperlinkFactory.ELEMENT_hyperlinkPageExpression, chart.getHyperlinkPageExpression(), false);
		writer.writeExpression(JRHyperlinkFactory.ELEMENT_hyperlinkTooltipExpression, chart.getHyperlinkTooltipExpression(), false);
		writeHyperlinkParameters(chart.getHyperlinkParameters());

		writer.closeElement();
	}


	/**
	 *
	 */
	private void writeElementDataset(JRElementDataset dataset) throws IOException
	{
		writer.startElement(JRElementDatasetFactory.ELEMENT_dataset);
		writer.addAttribute(JRElementDatasetFactory.ATTRIBUTE_resetType, dataset.getResetType(), JRXmlConstants.getResetTypeMap(), JRVariable.RESET_TYPE_REPORT);

		if (dataset.getResetType() == JRVariable.RESET_TYPE_GROUP)
		{
			writer.addEncodedAttribute(JRElementDatasetFactory.ATTRIBUTE_resetGroup, dataset.getResetGroup().getName());
		}
		writer.addAttribute(JRElementDatasetFactory.ATTRIBUTE_incrementType, dataset.getIncrementType(), JRXmlConstants.getResetTypeMap(), JRVariable.RESET_TYPE_NONE);

		if (dataset.getIncrementType() == JRVariable.RESET_TYPE_GROUP)
		{
			writer.addEncodedAttribute(JRElementDatasetFactory.ATTRIBUTE_incrementGroup, dataset.getIncrementGroup().getName());
		}

		writer.writeExpression(JRElementDatasetFactory.ELEMENT_incrementWhenExpression, dataset.getIncrementWhenExpression(), false);
		
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
		writer.startElement(JRElementDatasetFactory.ELEMENT_categoryDataset);

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
		writer.startElement(JRElementDatasetFactory.ELEMENT_timeSeriesDataset);
		if (dataset.getTimePeriod() != null && !Day.class.getName().equals(dataset.getTimePeriod().getName()))
		{
			writer.addAttribute(JRElementDatasetFactory.ATTRIBUTE_timePeriod, JRXmlConstants.getTimePeriodName(dataset.getTimePeriod()));
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
		writer.startElement(JRElementDatasetFactory.ELEMENT_timePeriodDataset);
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
		writer.startElement(JRElementDatasetFactory.ELEMENT_categorySeries);

		writer.writeExpression(JRElementDatasetFactory.ELEMENT_seriesExpression, categorySeries.getSeriesExpression(), false);
		writer.writeExpression(JRElementDatasetFactory.ELEMENT_categoryExpression, categorySeries.getCategoryExpression(), false);
		writer.writeExpression(JRElementDatasetFactory.ELEMENT_valueExpression, categorySeries.getValueExpression(), false);
		writer.writeExpression(JRElementDatasetFactory.ELEMENT_labelExpression, categorySeries.getLabelExpression(), false);
		writeHyperlink(JRHyperlinkFactory.ELEMENT_itemHyperlink, categorySeries.getItemHyperlink());

		writer.closeElement();
	}

	/**
	 * 
	 */
	private void writeXyzDataset(JRXyzDataset dataset) throws IOException
	{
		writer.startElement(JRElementDatasetFactory.ELEMENT_xyzDataset);
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
		writer.startElement(JRElementDatasetFactory.ELEMENT_xyzSeries);
		
		writer.writeExpression(JRElementDatasetFactory.ELEMENT_seriesExpression, series.getSeriesExpression(), false);
		writer.writeExpression(JRElementDatasetFactory.ELEMENT_xValueExpression, series.getXValueExpression(), false);
		writer.writeExpression(JRElementDatasetFactory.ELEMENT_yValueExpression, series.getYValueExpression(), false);
		writer.writeExpression(JRElementDatasetFactory.ELEMENT_zValueExpression, series.getZValueExpression(), false);
		writeHyperlink(JRHyperlinkFactory.ELEMENT_itemHyperlink, series.getItemHyperlink());

		writer.closeElement();
	}

	/**
	 *
	 */
	private void writeXySeries(JRXySeries xySeries) throws IOException
	{
		writer.startElement(JRElementDatasetFactory.ELEMENT_xySeries);

		writer.writeExpression(JRElementDatasetFactory.ELEMENT_seriesExpression, xySeries.getSeriesExpression(), false);
		writer.writeExpression(JRElementDatasetFactory.ELEMENT_xValueExpression, xySeries.getXValueExpression(), false);
		writer.writeExpression(JRElementDatasetFactory.ELEMENT_yValueExpression, xySeries.getYValueExpression(), false);
		writer.writeExpression(JRElementDatasetFactory.ELEMENT_labelExpression, xySeries.getLabelExpression(), false);
		writeHyperlink(JRHyperlinkFactory.ELEMENT_itemHyperlink, xySeries.getItemHyperlink());

		writer.closeElement();
	}


	/**
	 *
	 */
	private void writeXyDataset(JRXyDataset dataset) throws IOException
	{
		writer.startElement(JRElementDatasetFactory.ELEMENT_xyDataset);

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
		writer.startElement(JRElementDatasetFactory.ELEMENT_timeSeries);

		writer.writeExpression(JRElementDatasetFactory.ELEMENT_seriesExpression, timeSeries.getSeriesExpression(), false);
		writer.writeExpression(JRElementDatasetFactory.ELEMENT_timePeriodExpression, timeSeries.getTimePeriodExpression(), false);
		writer.writeExpression(JRElementDatasetFactory.ELEMENT_valueExpression, timeSeries.getValueExpression(), false);
		writer.writeExpression(JRElementDatasetFactory.ELEMENT_labelExpression, timeSeries.getLabelExpression(), false);
		writeHyperlink(JRHyperlinkFactory.ELEMENT_itemHyperlink, timeSeries.getItemHyperlink());
		
		writer.closeElement();
	}
	
	
	private void writeTimePeriodSeries(JRTimePeriodSeries timePeriodSeries) throws IOException
	{
		writer.startElement(JRElementDatasetFactory.ELEMENT_timePeriodSeries);
		
		writer.writeExpression(JRElementDatasetFactory.ELEMENT_seriesExpression, timePeriodSeries.getSeriesExpression(), false);
		writer.writeExpression(JRElementDatasetFactory.ELEMENT_startDateExpression, timePeriodSeries.getStartDateExpression(), false);
		writer.writeExpression(JRElementDatasetFactory.ELEMENT_endDateExpression, timePeriodSeries.getEndDateExpression(), false);
		writer.writeExpression(JRElementDatasetFactory.ELEMENT_valueExpression, timePeriodSeries.getValueExpression(), false);
		writer.writeExpression(JRElementDatasetFactory.ELEMENT_labelExpression, timePeriodSeries.getLabelExpression(), false);
		writeHyperlink(JRHyperlinkFactory.ELEMENT_itemHyperlink, timePeriodSeries.getItemHyperlink());
		
		writer.closeElement();
	}

	
	/**
	 *
	 */
	public void writePieDataset(JRPieDataset dataset) throws IOException
	{
		writer.startElement(JRElementDatasetFactory.ELEMENT_pieDataset);

		writeElementDataset(dataset);

		writer.writeExpression(JRElementDatasetFactory.ELEMENT_keyExpression, dataset.getKeyExpression(), false);
		writer.writeExpression(JRElementDatasetFactory.ELEMENT_valueExpression, dataset.getValueExpression(), false);
		writer.writeExpression(JRElementDatasetFactory.ELEMENT_labelExpression, dataset.getLabelExpression(), false);
		writeHyperlink(JRHyperlinkFactory.ELEMENT_sectionHyperlink, dataset.getSectionHyperlink());

		writer.closeElement();
	}

	/**
	 * Writes the description of a value dataset to the output stream.
	 * @param dataset the value dataset to persist
	 */
	public void writeValueDataset(JRValueDataset dataset) throws IOException
	{
		writer.startElement(JRElementDatasetFactory.ELEMENT_valueDataset);

		writeElementDataset(dataset);

		writer.writeExpression(JRElementDatasetFactory.ELEMENT_valueExpression, dataset.getValueExpression(), false);
		
		writer.closeElement();
	}


	/**
	 * Writes the description of how to display a value in a valueDataset.
	 * 
	 * @param valueDisplay the description to save
	 */
	public void writeValueDisplay(JRValueDisplay valueDisplay) throws IOException
	{
		writer.startElement(JRChartPlotFactory.ELEMENT_valueDisplay);

        writer.addAttribute(JRChartPlotFactory.ATTRIBUTE_color, valueDisplay.getColor());
        writer.addAttribute(JRChartPlotFactory.ATTRIBUTE_mask, valueDisplay.getMask());

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
		writer.startElement(JRChartPlotFactory.ELEMENT_dataRange);
		
		writer.writeExpression(JRChartPlotFactory.ELEMENT_lowExpression, dataRange.getLowExpression(), false);
		writer.writeExpression(JRChartPlotFactory.ELEMENT_highExpression, dataRange.getHighExpression(), false);
		
		writer.closeElement();
    }


	/**
	 * Writes a meter interval description to the output stream.
	 * 
	 * @param interval the interval to write
	 */
	private void writeMeterInterval(JRMeterInterval interval) throws IOException
	{
		writer.startElement(JRChartPlotFactory.ELEMENT_meterInterval);
		
		writer.addAttribute(JRChartPlotFactory.ATTRIBUTE_label, interval.getLabel());
		writer.addAttribute(JRChartPlotFactory.ATTRIBUTE_color, interval.getBackgroundColor());
		writer.addAttribute(JRChartPlotFactory.ATTRIBUTE_alpha, interval.getAlpha());
		
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
			writer.startElement(JRChartPlotFactory.JRSeriesColorFactory.ELEMENT_seriesColor);
			writer.addAttribute(JRChartPlotFactory.JRSeriesColorFactory.ATTRIBUTE_seriesOrder, colors[i].getSeriesOrder());
			writer.addAttribute(JRChartPlotFactory.JRSeriesColorFactory.ATTRIBUTE_color, colors[i].getColor());
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
		writer.startElement(JRChartPlotFactory.ELEMENT_plot);
		writer.addAttribute(JRChartPlotFactory.ATTRIBUTE_backcolor, plot.getBackcolor());
		writer.addAttribute(JRChartPlotFactory.ATTRIBUTE_orientation, plot.getOrientation(), JRXmlConstants.getPlotOrientationMap(), PlotOrientation.VERTICAL);
		writer.addAttribute(JRChartPlotFactory.ATTRIBUTE_backgroundAlpha, plot.getBackgroundAlpha(), 1.0f);
		writer.addAttribute(JRChartPlotFactory.ATTRIBUTE_foregroundAlpha, plot.getForegroundAlpha(), 1.0f);
		writer.addAttribute(JRChartPlotFactory.ATTRIBUTE_labelRotation, plot.getLabelRotation(), 0.0);
		writeSeriesColors(plot.getSeriesColors());
		
		writer.closeElement();
	}


	/**
	 *
	 */
	public void writePieChart(JRChart chart) throws IOException
	{
		writer.startElement(JRChartFactory.ELEMENT_pieChart);
		writeChart(chart);
		writePieDataset((JRPieDataset) chart.getDataset());

		// write plot
		writer.startElement(JRChartPlotFactory.ELEMENT_piePlot);
		writePlot(chart.getPlot());
		writer.closeElement();

		writer.closeElement();
	}


	/**
	 *
	 */
	public void writePie3DChart(JRChart chart) throws IOException
	{
		writer.startElement(JRChartFactory.ELEMENT_pie3DChart);
		writeChart(chart);
		writePieDataset((JRPieDataset) chart.getDataset());

		// write plot
		JRPie3DPlot plot = (JRPie3DPlot) chart.getPlot();
		writer.startElement(JRChartPlotFactory.ELEMENT_pie3DPlot);
		writer.addAttribute(JRChartPlotFactory.ATTRIBUTE_depthFactor, plot.getDepthFactor(), JRPie3DPlot.DEPTH_FACTOR_DEFAULT);
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
		writer.startElement(JRChartFactory.JRChartAxisFormatFactory.ELEMENT_axisFormat);
		writer.addAttribute(JRChartFactory.JRChartAxisFormatFactory.ATTRIBUTE_labelColor, axisLabelColor);
		writer.addAttribute(JRChartFactory.JRChartAxisFormatFactory.ATTRIBUTE_tickLabelColor, axisTickLabelColor);
		writer.addAttribute(JRChartFactory.JRChartAxisFormatFactory.ATTRIBUTE_tickLabelMask, axisTickLabelMask);
		writer.addAttribute(JRChartFactory.JRChartAxisFormatFactory.ATTRIBUTE_axisLineColor, axisLineColor);

		if (axisLabelFont != null)
		{
			writer.startElement(JRChartFactory.JRChartAxisFormatFactory.ELEMENT_labelFont);
			writeFont(axisLabelFont);
			writer.closeElement();
		}
		
		if (axisTickLabelFont != null)
		{
			writer.startElement(JRChartFactory.JRChartAxisFormatFactory.ELEMENT_tickLabelFont);
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
		writer.startElement(JRChartPlotFactory.ELEMENT_barPlot);
		writer.addAttribute(JRChartPlotFactory.ATTRIBUTE_isShowLabels, plot.isShowLabels(), false);
		writer.addAttribute(JRChartPlotFactory.ATTRIBUTE_isShowTickLabels, plot.isShowTickLabels(), true);
		writer.addAttribute(JRChartPlotFactory.ATTRIBUTE_isShowTickMarks, plot.isShowTickMarks(), true);
		writePlot(plot);

		writer.writeExpression(JRChartPlotFactory.ELEMENT_categoryAxisLabelExpression, plot.getCategoryAxisLabelExpression(), false);
		writeAxisFormat(JRChartPlotFactory.ELEMENT_categoryAxisFormat, plot.getCategoryAxisLabelFont(), plot.getCategoryAxisLabelColor(),
						plot.getCategoryAxisTickLabelFont(), plot.getCategoryAxisTickLabelColor(),
						plot.getCategoryAxisTickLabelMask(), plot.getCategoryAxisLineColor());
		writer.writeExpression(JRChartPlotFactory.ELEMENT_valueAxisLabelExpression, plot.getValueAxisLabelExpression(), false);
		writeAxisFormat(JRChartPlotFactory.ELEMENT_valueAxisFormat, plot.getValueAxisLabelFont(), plot.getValueAxisLabelColor(),
				plot.getValueAxisTickLabelFont(), plot.getValueAxisTickLabelColor(),
				plot.getValueAxisTickLabelMask(), plot.getValueAxisLineColor());


		writer.closeElement();
	}
	
	
	/**
	 * 
	 */
	private void writeBubblePlot(JRBubblePlot plot) throws IOException
	{
		writer.startElement(JRChartPlotFactory.ELEMENT_bubblePlot);
		writer.addAttribute(JRChartPlotFactory.ATTRIBUTE_scaleType, plot.getScaleType(), JRXmlConstants.getScaleTypeMap());
		writePlot(plot);

		writer.writeExpression(JRChartPlotFactory.ELEMENT_xAxisLabelExpression, plot.getXAxisLabelExpression(), false);
		writeAxisFormat(JRChartPlotFactory.ELEMENT_xAxisFormat, plot.getXAxisLabelFont(), plot.getXAxisLabelColor(),
				plot.getXAxisTickLabelFont(), plot.getXAxisTickLabelColor(),
				plot.getXAxisTickLabelMask(), plot.getXAxisLineColor());
		writer.writeExpression(JRChartPlotFactory.ELEMENT_yAxisLabelExpression, plot.getYAxisLabelExpression(), false);
		writeAxisFormat(JRChartPlotFactory.ELEMENT_yAxisFormat, plot.getYAxisLabelFont(), plot.getYAxisLabelColor(),
				plot.getYAxisTickLabelFont(), plot.getYAxisTickLabelColor(),
				plot.getYAxisTickLabelMask(), plot.getYAxisLineColor());

		writer.closeElement();
	}


	/**
	 *
	 */
	private void writeLinePlot(JRLinePlot plot) throws IOException
	{
		writer.startElement(JRChartPlotFactory.ELEMENT_linePlot);
		writer.addAttribute(JRChartPlotFactory.ATTRIBUTE_isShowLines, plot.isShowLines(), true);
		writer.addAttribute(JRChartPlotFactory.ATTRIBUTE_isShowShapes, plot.isShowShapes(), true);

		writePlot(plot);

		writer.writeExpression(JRChartPlotFactory.ELEMENT_categoryAxisLabelExpression, plot.getCategoryAxisLabelExpression(), false);
		writeAxisFormat(JRChartPlotFactory.ELEMENT_categoryAxisFormat, plot.getCategoryAxisLabelFont(), plot.getCategoryAxisLabelColor(),
				plot.getCategoryAxisTickLabelFont(), plot.getCategoryAxisTickLabelColor(),
				plot.getCategoryAxisTickLabelMask(), plot.getCategoryAxisLineColor());
		writer.writeExpression(JRChartPlotFactory.ELEMENT_valueAxisLabelExpression, plot.getValueAxisLabelExpression(), false);
		writeAxisFormat(JRChartPlotFactory.ELEMENT_valueAxisFormat, plot.getValueAxisLabelFont(), plot.getValueAxisLabelColor(),
				plot.getValueAxisTickLabelFont(), plot.getValueAxisTickLabelColor(),
				plot.getValueAxisTickLabelMask(), plot.getValueAxisLineColor());

		writer.closeElement();
	}
	
	
	private void writeTimeSeriesPlot(JRTimeSeriesPlot plot) throws IOException
	{
		writer.startElement(JRChartPlotFactory.ELEMENT_timeSeriesPlot);
		writer.addAttribute(JRChartPlotFactory.ATTRIBUTE_isShowLines, plot.isShowLines(), true);
		writer.addAttribute(JRChartPlotFactory.ATTRIBUTE_isShowShapes, plot.isShowShapes(), true);
		
		writePlot( plot );
		
		writer.writeExpression(JRChartPlotFactory.ELEMENT_timeAxisLabelExpression, plot.getTimeAxisLabelExpression(), false);
		writeAxisFormat(JRChartPlotFactory.ELEMENT_timeAxisFormat, plot.getTimeAxisLabelFont(), plot.getTimeAxisLabelColor(),
				plot.getTimeAxisTickLabelFont(), plot.getTimeAxisTickLabelColor(),
				plot.getTimeAxisTickLabelMask(), plot.getTimeAxisLineColor());
		writer.writeExpression(JRChartPlotFactory.ELEMENT_valueAxisLabelExpression, plot.getValueAxisLabelExpression(), false);
		writeAxisFormat(JRChartPlotFactory.ELEMENT_valueAxisFormat, plot.getValueAxisLabelFont(), plot.getValueAxisLabelColor(),
				plot.getValueAxisTickLabelFont(), plot.getValueAxisTickLabelColor(),
				plot.getValueAxisTickLabelMask(), plot.getValueAxisLineColor());


		writer.closeElement();
	}


	/**
	 *
	 */
	public void writeBar3DPlot(JRBar3DPlot plot) throws IOException
	{
		writer.startElement(JRChartPlotFactory.ELEMENT_bar3DPlot);
		writer.addAttribute(JRChartPlotFactory.ATTRIBUTE_isShowLabels, plot.isShowLabels(), false);
		writer.addAttribute(JRChartPlotFactory.ATTRIBUTE_xOffset, plot.getXOffset(), BarRenderer3D.DEFAULT_X_OFFSET);
		writer.addAttribute(JRChartPlotFactory.ATTRIBUTE_yOffset, plot.getYOffset(), BarRenderer3D.DEFAULT_Y_OFFSET);

		writePlot(plot);

		writer.writeExpression(JRChartPlotFactory.ELEMENT_categoryAxisLabelExpression, plot.getCategoryAxisLabelExpression(), false);
		writeAxisFormat(JRChartPlotFactory.ELEMENT_categoryAxisFormat, plot.getCategoryAxisLabelFont(), plot.getCategoryAxisLabelColor(),
				plot.getCategoryAxisTickLabelFont(), plot.getCategoryAxisTickLabelColor(),
				plot.getCategoryAxisTickLabelMask(), plot.getCategoryAxisLineColor());
		writer.writeExpression(JRChartPlotFactory.ELEMENT_valueAxisLabelExpression, plot.getValueAxisLabelExpression(), false);
		writeAxisFormat(JRChartPlotFactory.ELEMENT_valueAxisFormat, plot.getValueAxisLabelFont(), plot.getValueAxisLabelColor(),
				plot.getValueAxisTickLabelFont(), plot.getValueAxisTickLabelColor(),
				plot.getValueAxisTickLabelMask(), plot.getValueAxisLineColor());


		writer.closeElement();
	}


	/**
	 *
	 */
	public void writeBarChart(JRChart chart) throws IOException
	{
		writer.startElement(JRChartFactory.ELEMENT_barChart);

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
		writer.startElement(JRChartFactory.ELEMENT_bar3DChart);

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
		writer.startElement(JRChartFactory.ELEMENT_bubbleChart);
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
		writer.startElement(JRChartFactory.ELEMENT_stackedBarChart);

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
		writer.startElement(JRChartFactory.ELEMENT_stackedBar3DChart);

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
		writer.startElement(JRChartFactory.ELEMENT_lineChart);

		writeChart(chart);
		writeCategoryDataSet((JRCategoryDataset) chart.getDataset());
		writeLinePlot((JRLinePlot) chart.getPlot());
		writer.closeElement();
	}
	
	
	public void writeTimeSeriesChart(JRChart chart) throws IOException
	{
		writer.startElement(JRChartFactory.ELEMENT_timeSeriesChart);
		writeChart(chart);
		writeTimeSeriesDataset((JRTimeSeriesDataset)chart.getDataset());
		writeTimeSeriesPlot((JRTimeSeriesPlot)chart.getPlot());
		writer.closeElement();
	}

	public void writeHighLowDataset(JRHighLowDataset dataset) throws IOException
	{
		writer.startElement(JRElementDatasetFactory.ELEMENT_highLowDataset);

		writeElementDataset(dataset);

		writer.writeExpression(JRElementDatasetFactory.ELEMENT_seriesExpression, dataset.getSeriesExpression(), false);
		writer.writeExpression(JRElementDatasetFactory.ELEMENT_dateExpression, dataset.getDateExpression(), false);
		writer.writeExpression(JRElementDatasetFactory.ELEMENT_highExpression, dataset.getHighExpression(), false);
		writer.writeExpression(JRElementDatasetFactory.ELEMENT_lowExpression, dataset.getLowExpression(), false);
		writer.writeExpression(JRElementDatasetFactory.ELEMENT_openExpression, dataset.getOpenExpression(), false);
		writer.writeExpression(JRElementDatasetFactory.ELEMENT_closeExpression, dataset.getCloseExpression(), false);
		writer.writeExpression(JRElementDatasetFactory.ELEMENT_volumeExpression, dataset.getVolumeExpression(), false);
		writeHyperlink(JRHyperlinkFactory.ELEMENT_itemHyperlink, dataset.getItemHyperlink());

		writer.closeElement();
	}


	public void writeHighLowChart(JRChart chart) throws IOException
	{
		writer.startElement(JRChartFactory.ELEMENT_highLowChart);

		writeChart(chart);
		writeHighLowDataset((JRHighLowDataset) chart.getDataset());

		JRHighLowPlot plot = (JRHighLowPlot) chart.getPlot();
		writer.startElement(JRChartPlotFactory.ELEMENT_highLowPlot);
		writer.addAttribute(JRChartPlotFactory.ATTRIBUTE_isShowOpenTicks, plot.isShowOpenTicks(), true);
		writer.addAttribute(JRChartPlotFactory.ATTRIBUTE_isShowCloseTicks, plot.isShowCloseTicks(), true);

		writePlot(plot);

		writer.writeExpression(JRChartPlotFactory.ELEMENT_timeAxisLabelExpression, plot.getTimeAxisLabelExpression(), false);
		writeAxisFormat(JRChartPlotFactory.ELEMENT_timeAxisFormat, plot.getTimeAxisLabelFont(), plot.getTimeAxisLabelColor(),
				plot.getTimeAxisTickLabelFont(), plot.getTimeAxisTickLabelColor(),
				plot.getTimeAxisTickLabelMask(), plot.getTimeAxisLineColor());
		writer.writeExpression(JRChartPlotFactory.ELEMENT_valueAxisLabelExpression, plot.getValueAxisLabelExpression(), false);
		writeAxisFormat(JRChartPlotFactory.ELEMENT_valueAxisFormat, plot.getValueAxisLabelFont(), plot.getValueAxisLabelColor(),
				plot.getValueAxisTickLabelFont(), plot.getValueAxisTickLabelColor(),
				plot.getValueAxisTickLabelMask(), plot.getValueAxisLineColor());


		writer.closeElement();
		writer.closeElement();
	}


	public void writeCandlestickChart(JRChart chart) throws IOException
	{
		writer.startElement(JRChartFactory.ELEMENT_candlestickChart);

		writeChart(chart);
		writeHighLowDataset((JRHighLowDataset) chart.getDataset());

		JRCandlestickPlot plot = (JRCandlestickPlot) chart.getPlot();
		writer.startElement(JRChartPlotFactory.ELEMENT_candlestickPlot);
		writer.addAttribute(JRChartPlotFactory.ATTRIBUTE_isShowVolume, plot.isShowVolume(), true);

		writePlot(plot);

		writer.writeExpression(JRChartPlotFactory.ELEMENT_timeAxisLabelExpression, plot.getTimeAxisLabelExpression(), false);
		writeAxisFormat(JRChartPlotFactory.ELEMENT_timeAxisFormat, plot.getTimeAxisLabelFont(), plot.getTimeAxisLabelColor(),
				plot.getTimeAxisTickLabelFont(), plot.getTimeAxisTickLabelColor(),
				plot.getTimeAxisTickLabelMask(), plot.getTimeAxisLineColor());
		writer.writeExpression(JRChartPlotFactory.ELEMENT_valueAxisLabelExpression, plot.getValueAxisLabelExpression(), false);
		writeAxisFormat(JRChartPlotFactory.ELEMENT_valueAxisFormat, plot.getValueAxisLabelFont(), plot.getValueAxisLabelColor(),
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
		writer.startElement(JRChartPlotFactory.ELEMENT_areaPlot);
		writePlot(plot);

		writer.writeExpression(JRChartPlotFactory.ELEMENT_categoryAxisLabelExpression, plot.getCategoryAxisLabelExpression(), false);
		writeAxisFormat(JRChartPlotFactory.ELEMENT_categoryAxisFormat, plot.getCategoryAxisLabelFont(), plot.getCategoryAxisLabelColor(),
				plot.getCategoryAxisTickLabelFont(), plot.getCategoryAxisTickLabelColor(),
				plot.getCategoryAxisTickLabelMask(), plot.getCategoryAxisLineColor());
		writer.writeExpression(JRChartPlotFactory.ELEMENT_valueAxisLabelExpression, plot.getValueAxisLabelExpression(), false);
		writeAxisFormat(JRChartPlotFactory.ELEMENT_valueAxisFormat, plot.getValueAxisLabelFont(), plot.getValueAxisLabelColor(),
				plot.getValueAxisTickLabelFont(), plot.getValueAxisTickLabelColor(),
				plot.getValueAxisTickLabelMask(), plot.getValueAxisLineColor());


		writer.closeElement();
	}


	/**
	 *
	 */
	public void writeAreaChart(JRChart chart) throws IOException
	{
		writer.startElement(JRChartFactory.ELEMENT_areaChart);

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
		writer.startElement(JRChartPlotFactory.ELEMENT_scatterPlot);
		writer.addAttribute(JRChartPlotFactory.ATTRIBUTE_isShowLines, plot.isShowLines(), true);
		writer.addAttribute(JRChartPlotFactory.ATTRIBUTE_isShowShapes, plot.isShowShapes(), true);

		writePlot(plot);

		writer.writeExpression(JRChartPlotFactory.ELEMENT_xAxisLabelExpression, plot.getXAxisLabelExpression(), false);
		writeAxisFormat(JRChartPlotFactory.ELEMENT_xAxisFormat, plot.getXAxisLabelFont(), plot.getXAxisLabelColor(),
				plot.getXAxisTickLabelFont(), plot.getXAxisTickLabelColor(),
				plot.getXAxisTickLabelMask(), plot.getXAxisLineColor());
		writer.writeExpression(JRChartPlotFactory.ELEMENT_yAxisLabelExpression, plot.getYAxisLabelExpression(), false);
		writeAxisFormat(JRChartPlotFactory.ELEMENT_yAxisFormat, plot.getYAxisLabelFont(), plot.getYAxisLabelColor(),
				plot.getYAxisTickLabelFont(), plot.getYAxisTickLabelColor(),
				plot.getYAxisTickLabelMask(), plot.getYAxisLineColor());

		writer.closeElement();
	}


	/**
	 *
	 */
	public void writeScatterChart(JRChart chart) throws IOException
	{
		writer.startElement(JRChartFactory.ELEMENT_scatterChart);

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
		writer.startElement(JRChartFactory.ELEMENT_xyAreaChart);

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
		writer.startElement(JRChartFactory.ELEMENT_xyBarChart);

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
		writer.startElement(JRChartFactory.ELEMENT_xyLineChart);

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
		writer.startElement(JRChartFactory.ELEMENT_meterChart);
		
		writeChart(chart);
		writeValueDataset((JRValueDataset) chart.getDataset());

		// write plot
		JRMeterPlot plot = (JRMeterPlot) chart.getPlot();
		writer.startElement(JRMeterPlotFactory.ELEMENT_meterPlot);
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
		writer.startElement(JRChartFactory.ELEMENT_thermometerChart);
		
		writeChart(chart);
		writeValueDataset((JRValueDataset) chart.getDataset());

		// write plot
		JRThermometerPlot plot = (JRThermometerPlot) chart.getPlot();
		
		writer.startElement(JRThermometerPlotFactory.ELEMENT_meterPlot);
		
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
		writer.startElement(JRChartFactory.ELEMENT_multiAxisChart);
		
		writeChart(chart);

		// write plot
		JRMultiAxisPlot plot = (JRMultiAxisPlot) chart.getPlot();
		writer.startElement(JRChartPlotFactory.ELEMENT_multiAxisPlot);
		
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
		writer.startElement(JRChartFactory.ELEMENT_stackedAreaChart);

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
			default:
				throw new JRRuntimeException("Chart type not supported.");
		}
	}


	private void writeSubreportReturnValue(JRSubreportReturnValue returnValue) throws IOException
	{
		writer.startElement(JRSubreportReturnValueFactory.ELEMENT_returnValue);
		writer.addEncodedAttribute(JRSubreportReturnValueFactory.ATTRIBUTE_subreportVariable, returnValue.getSubreportVariable());
		writer.addEncodedAttribute(JRSubreportReturnValueFactory.ATTRIBUTE_toVariable, returnValue.getToVariable());
		writer.addAttribute(JRSubreportReturnValueFactory.ATTRIBUTE_calculation, returnValue.getCalculation(), JRXmlConstants.getCalculationMap(), JRVariable.CALCULATION_NOTHING);
		writer.addAttribute(JRSubreportReturnValueFactory.ATTRIBUTE_incrementerFactoryClass, returnValue.getIncrementerFactoryClassName());
		writer.closeElement();
	}


	public void writeCrosstab(JRCrosstab crosstab) throws IOException
	{
		writer.startElement(JRCrosstabFactory.ELEMENT_crosstab);
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
			writer.addAttribute(JRCellContentsFactory.ATTRIBUTE_mode, contents.getMode(), JRXmlConstants.getModeMap());
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
		writer.startElement(JRCrosstabParameterFactory.ELEMENT_crosstabParameter);
		writer.addEncodedAttribute(JRCrosstabParameterFactory.ATTRIBUTE_name, parameter.getName());
		writer.addAttribute(JRCrosstabParameterFactory.ATTRIBUTE_class, parameter.getValueClassName(), "java.lang.String");
		writer.writeExpression(JRCrosstabParameterFactory.ELEMENT_parameterValueExpression, parameter.getExpression(), false);
		writer.closeElement();
	}


	public void writeDataset(JRDataset dataset) throws IOException
	{
		writer.startElement(JRDatasetFactory.ELEMENT_subDataset);
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
		
		writer.writeExpression(JRDatasetFactory.ELEMENT_filterExpression, dataset.getFilterExpression(), false);

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
		writer.startElement(JRDatasetRunFactory.ELEMENT_datasetRun);
		writer.addEncodedAttribute(JRDatasetRunFactory.ATTRIBUTE_subDataset, datasetRun.getDatasetName());
		
		writer.writeExpression(JRDatasetRunFactory.ELEMENT_parametersMapExpression, datasetRun.getParametersMapExpression(), false);

		/*   */
		JRDatasetParameter[] parameters = datasetRun.getParameters();
		if (parameters != null && parameters.length > 0)
		{
			for(int i = 0; i < parameters.length; i++)
			{
				writeDatasetParameter(parameters[i]);
			}
		}

		writer.writeExpression(JRDatasetRunFactory.ELEMENT_connectionExpression, datasetRun.getConnectionExpression(), false);
		writer.writeExpression(JRDatasetRunFactory.ELEMENT_dataSourceExpression, datasetRun.getDataSourceExpression(), false);

		writer.closeElement();
	}
	
	
	public void writeFrame(JRFrame frame) throws IOException
	{
		writer.startElement(JRFrameFactory.ELEMENT_frame);
		
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
			writer.startElement(JRHyperlinkParameterFactory.ELEMENT_hyperlinkParameter);
			writer.addEncodedAttribute(JRHyperlinkParameterFactory.ATTRIBUTE_name, parameter.getName());
			
			writer.writeExpression(JRHyperlinkParameterExpressionFactory.ELEMENT_hyperlinkParameterExpression,
					parameter.getValueExpression(), true, String.class.getName());
			
			writer.closeElement();
		}
	}
	
	
	protected void writeHyperlink(String tagName, JRHyperlink hyperlink) throws IOException
	{
		if (hyperlink != null)
		{
			writer.startElement(tagName);
			
			writer.addEncodedAttribute(JRHyperlinkFactory.ATTRIBUTE_hyperlinkType, hyperlink.getLinkType());
			writer.addAttribute(JRHyperlinkFactory.ATTRIBUTE_hyperlinkTarget, hyperlink.getHyperlinkTarget(), JRXmlConstants.getHyperlinkTargetMap(), JRHyperlink.HYPERLINK_TARGET_SELF);
			
			writer.writeExpression(JRHyperlinkFactory.ELEMENT_hyperlinkReferenceExpression, hyperlink.getHyperlinkReferenceExpression(), false);
			writer.writeExpression(JRHyperlinkFactory.ELEMENT_hyperlinkAnchorExpression, hyperlink.getHyperlinkAnchorExpression(), false);
			writer.writeExpression(JRHyperlinkFactory.ELEMENT_hyperlinkPageExpression, hyperlink.getHyperlinkPageExpression(), false);
			writer.writeExpression(JRHyperlinkFactory.ELEMENT_hyperlinkTooltipExpression, hyperlink.getHyperlinkTooltipExpression(), false);
			writeHyperlinkParameters(hyperlink.getHyperlinkParameters());
			
			writer.closeElement();
		}
	}
}
