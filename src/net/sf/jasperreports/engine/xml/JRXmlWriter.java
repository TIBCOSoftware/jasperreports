/*
 * ============================================================================
 * GNU Lesser General Public License
 * ============================================================================
 *
 * JasperReports - Free Java report-generating library.
 * Copyright (C) 2001-2005 JasperSoft Corporation http://www.jaspersoft.com
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
 * 185, Berry Street, Suite 6200
 * San Francisco CA 94107
 * http://www.jaspersoft.com
 */
package net.sf.jasperreports.engine.xml;

import java.awt.Color;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.charts.JRAreaPlot;
import net.sf.jasperreports.charts.JRBar3DPlot;
import net.sf.jasperreports.charts.JRBarPlot;
import net.sf.jasperreports.charts.JRBubblePlot;
import net.sf.jasperreports.charts.JRCandlestickPlot;
import net.sf.jasperreports.charts.JRCategoryDataset;
import net.sf.jasperreports.charts.JRCategorySeries;
import net.sf.jasperreports.charts.JRHighLowDataset;
import net.sf.jasperreports.charts.JRHighLowPlot;
import net.sf.jasperreports.charts.JRLinePlot;
import net.sf.jasperreports.charts.JRPie3DPlot;
import net.sf.jasperreports.charts.JRPieDataset;
import net.sf.jasperreports.charts.JRScatterPlot;
import net.sf.jasperreports.charts.JRTimePeriodDataset;
import net.sf.jasperreports.charts.JRTimePeriodSeries;
import net.sf.jasperreports.charts.JRTimeSeries;
import net.sf.jasperreports.charts.JRTimeSeriesDataset;
import net.sf.jasperreports.charts.JRTimeSeriesPlot;
import net.sf.jasperreports.charts.JRXyDataset;
import net.sf.jasperreports.charts.JRXySeries;
import net.sf.jasperreports.charts.JRXyzDataset;
import net.sf.jasperreports.charts.JRXyzSeries;
import net.sf.jasperreports.engine.JRAlignment;
import net.sf.jasperreports.engine.JRAnchor;
import net.sf.jasperreports.engine.JRBand;
import net.sf.jasperreports.engine.JRBox;
import net.sf.jasperreports.engine.JRChart;
import net.sf.jasperreports.engine.JRChartDataset;
import net.sf.jasperreports.engine.JRChartPlot;
import net.sf.jasperreports.engine.JRChild;
import net.sf.jasperreports.engine.JRElement;
import net.sf.jasperreports.engine.JRElementGroup;
import net.sf.jasperreports.engine.JREllipse;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.engine.JRFont;
import net.sf.jasperreports.engine.JRGraphicElement;
import net.sf.jasperreports.engine.JRGroup;
import net.sf.jasperreports.engine.JRHyperlink;
import net.sf.jasperreports.engine.JRImage;
import net.sf.jasperreports.engine.JRLine;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JRQuery;
import net.sf.jasperreports.engine.JRRectangle;
import net.sf.jasperreports.engine.JRReport;
import net.sf.jasperreports.engine.JRReportFont;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.JRStaticText;
import net.sf.jasperreports.engine.JRSubreport;
import net.sf.jasperreports.engine.JRSubreportReturnValue;
import net.sf.jasperreports.engine.JRSubreportParameter;
import net.sf.jasperreports.engine.JRTextElement;
import net.sf.jasperreports.engine.JRTextField;
import net.sf.jasperreports.engine.JRVariable;
import net.sf.jasperreports.engine.util.JRStringUtil;

import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer3D;
import org.jfree.data.time.Day;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
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
	private StringBuffer sb = null;
	private Map fontsMap = new HashMap();

	/**
	 * This color mask is used to delete the alpha byte from a 32-bit RGB component
	 */
	private static final int colorMask = Integer.parseInt("00FFFFFF", 16);


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
		return writer.writeReport();
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
		String xmlString = JRXmlWriter.writeReport(report, encoding);
		
		FileOutputStream fos = null;

		try
		{
			byte[] bytes = xmlString.getBytes(encoding);
			fos = new FileOutputStream(destFileName);
			fos.write(bytes, 0, bytes.length);
			fos.flush();
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
		String xmlString = JRXmlWriter.writeReport(report, encoding);

		try
		{
			byte[] bytes = xmlString.getBytes(encoding);
			outputStream.write(bytes, 0, bytes.length);
		}
		catch (Exception e)
		{
			throw new JRException("Error writing to OutputStream : " + report.getName(), e);
		}
	}


	/**
	 *
	 */
	protected String writeReport()
	{
		sb = new StringBuffer();
		
		sb.append("<?xml version=\"1.0\" encoding=\"" + encoding + "\"?>\n");
		sb.append("<!DOCTYPE jasperReport PUBLIC \"-//JasperReports//DTD Report Design//EN\" \"http://jasperreports.sourceforge.net/dtds/jasperreport.dtd\">\n");
		sb.append("\n");

		sb.append("<jasperReport name=\"");
		sb.append(report.getName());
		sb.append("\"");

		if(report.getLanguage() != JRReport.LANGUAGE_JAVA)
		{
			sb.append(" language=\"");
			sb.append(report.getLanguage());
			sb.append("\"");
		}

		if(report.getColumnCount() != 1)
		{
			sb.append(" columnCount=\"");
			sb.append(report.getColumnCount());
			sb.append("\"");
		}

		if(report.getPrintOrder() != JRReport.PRINT_ORDER_VERTICAL)
		{
			sb.append(" printOrder=\"");
			sb.append((String)JRXmlConstants.getPrintOrderMap().get(new Byte(report.getPrintOrder())));
			sb.append("\"");
		}

		sb.append(" pageWidth=\"");
		sb.append(report.getPageWidth());
		sb.append("\"");

		sb.append(" pageHeight=\"");
		sb.append(report.getPageHeight());
		sb.append("\"");
		
		if(report.getOrientation() != JRReport.ORIENTATION_PORTRAIT)
		{
			sb.append(" orientation=\"");
			sb.append((String)JRXmlConstants.getOrientationMap().get(new Byte(report.getOrientation())));
			sb.append("\"");
		}

		if(report.getWhenNoDataType() != JRReport.WHEN_NO_DATA_TYPE_NO_PAGES)
		{
			sb.append(" whenNoDataType=\"");
			sb.append((String)JRXmlConstants.getWhenNoDataTypeMap().get(new Byte(report.getWhenNoDataType())));
			sb.append("\"");
		}

		sb.append(" columnWidth=\"");
		sb.append(report.getColumnWidth());
		sb.append("\"");
		
		if(report.getColumnSpacing() != 0)
		{
			sb.append(" columnSpacing=\"");
			sb.append(report.getColumnSpacing());
			sb.append("\"");
		}

		sb.append(" leftMargin=\"");
		sb.append(report.getLeftMargin());
		sb.append("\"");
		
		sb.append(" rightMargin=\"");
		sb.append(report.getRightMargin());
		sb.append("\"");
		
		sb.append(" topMargin=\"");
		sb.append(report.getTopMargin());
		sb.append("\"");
		
		sb.append(" bottomMargin=\"");
		sb.append(report.getBottomMargin());
		sb.append("\"");
		
		if(report.isTitleNewPage())
		{
			sb.append(" isTitleNewPage=\"");
			sb.append(report.isTitleNewPage());
			sb.append("\"");
		}

		if(report.isSummaryNewPage())
		{
			sb.append(" isSummaryNewPage=\"");
			sb.append(report.isSummaryNewPage());
			sb.append("\"");
		}

		if(report.isFloatColumnFooter())
		{
			sb.append(" isFloatColumnFooter=\"");
			sb.append(report.isFloatColumnFooter());
			sb.append("\"");
		}

		if(report.getScriptletClass() != null)
		{
			sb.append(" scriptletClass=\"");
			sb.append(report.getScriptletClass());
			sb.append("\"");
		}

		if(report.getResourceBundle() != null)
		{
			sb.append(" resourceBundle=\"");
			sb.append(report.getResourceBundle());
			sb.append("\"");
		}

		if(report.getWhenResourceMissingType() != JRReport.WHEN_RESOURCE_MISSING_TYPE_NULL)
		{
			sb.append(" whenResourceMissingType=\"");
			sb.append((String)JRXmlConstants.getWhenResourceMissingTypeMap().get(new Byte(report.getWhenResourceMissingType())));
			sb.append("\"");
		}

		sb.append(">\n");
		
		/*   */
		String[] propertyNames = report.getPropertyNames();
		if (propertyNames != null && propertyNames.length > 0)
		{
			for(int i = 0; i < propertyNames.length; i++)
			{
				String value = report.getProperty(propertyNames[i]);
				if (value != null)
				{
					sb.append("\t<property name=\"");
					sb.append(propertyNames[i]);
					sb.append("\" value=\"");
					sb.append(JRStringUtil.xmlEncode(value));
					sb.append("\"/>\n");
				}
			}
		}

		/*   */
		String[] imports = report.getImports();
		if (imports != null && imports.length > 0)
		{
			for(int i = 0; i < imports.length; i++)
			{
				String value = imports[i];
				if (value != null)
				{
					sb.append("\t<import value=\"");
					sb.append(JRStringUtil.xmlEncode(value));
					sb.append("\"/>\n");
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
		JRParameter[] parameters = report.getParameters();
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
		if(report.getQuery() != null)
		{
			writeQuery(report.getQuery());
		}

		/*   */
		JRField[] fields = report.getFields();
		if (fields != null && fields.length > 0)
		{
			for(int i = 0; i < fields.length; i++)
			{
				writeField(fields[i]);
			}
		}

		/*   */
		JRVariable[] variables = report.getVariables();
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

		/*   */
		JRGroup[] groups = report.getGroups();
		if (groups != null && groups.length > 0)
		{
			for(int i = 0; i < groups.length; i++)
			{
				writeGroup(groups[i]);
			}
		}

		
		if (report.getBackground() != null)
		{
			sb.append("\t<background>\n");
			writeBand(report.getBackground());
			sb.append("\t</background>\n");
		}

		if (report.getTitle() != null)
		{
			sb.append("\t<title>\n");
			writeBand(report.getTitle());
			sb.append("\t</title>\n");
		}

		if (report.getPageHeader() != null)
		{
			sb.append("\t<pageHeader>\n");
			writeBand(report.getPageHeader());
			sb.append("\t</pageHeader>\n");
		}

		if (report.getColumnHeader() != null)
		{
			sb.append("\t<columnHeader>\n");
			writeBand(report.getColumnHeader());
			sb.append("\t</columnHeader>\n");
		}

		if (report.getDetail() != null)
		{
			sb.append("\t<detail>\n");
			writeBand(report.getDetail());
			sb.append("\t</detail>\n");
		}

		if (report.getColumnFooter() != null)
		{
			sb.append("\t<columnFooter>\n");
			writeBand(report.getColumnFooter());
			sb.append("\t</columnFooter>\n");
		}

		if (report.getPageFooter() != null)
		{
			sb.append("\t<pageFooter>\n");
			writeBand(report.getPageFooter());
			sb.append("\t</pageFooter>\n");
		}

		if (report.getLastPageFooter() != null)
		{
			sb.append("\t<lastPageFooter>\n");
			writeBand(report.getLastPageFooter());
			sb.append("\t</lastPageFooter>\n");
		}

		if (report.getSummary() != null)
		{
			sb.append("\t<summary>\n");
			writeBand(report.getSummary());
			sb.append("\t</summary>\n");
		}

		sb.append("</jasperReport>\n");

		return sb.toString();
	}


	/**
	 *
	 */
	private void writeReportFont(JRReportFont font)
	{
		sb.append("\t<reportFont");

		sb.append(" name=\"");
		sb.append(font.getName());
		sb.append("\"");

		sb.append(" isDefault=\"");
		sb.append(font.isDefault());
		sb.append("\"");

		sb.append(" fontName=\"");
		sb.append(font.getFontName());
		sb.append("\"");

		sb.append(" size=\"");
		sb.append(font.getSize());
		sb.append("\"");

		sb.append(" isBold=\"");
		sb.append(font.isBold());
		sb.append("\"");

		sb.append(" isItalic=\"");
		sb.append(font.isItalic());
		sb.append("\"");

		sb.append(" isUnderline=\"");
		sb.append(font.isUnderline());
		sb.append("\"");

		sb.append(" isStrikeThrough=\"");
		sb.append(font.isStrikeThrough());
		sb.append("\"");

		sb.append(" pdfFontName=\"");
		sb.append(font.getPdfFontName());
		sb.append("\"");

		sb.append(" pdfEncoding=\"");
		sb.append(font.getPdfEncoding());
		sb.append("\"");

		sb.append(" isPdfEmbedded=\"");
		sb.append(font.isPdfEmbedded());
		sb.append("\"");

		sb.append("/>\n");
	}


	/**
	 *
	 */
	private void writeParameter(JRParameter parameter)
	{
		sb.append("\t<parameter");

		sb.append(" name=\"");
		sb.append(parameter.getName());
		sb.append("\"");

		sb.append(" class=\"");
		sb.append(parameter.getValueClassName());
		sb.append("\"");

		if (!parameter.isForPrompting())
		{
			sb.append(" isForPrompting=\"");
			sb.append(parameter.isForPrompting());
			sb.append("\"");
		}

		sb.append(">\n");

		if (parameter.getDescription() != null)
		{
			sb.append("\t\t<parameterDescription><![CDATA[");
			sb.append(parameter.getDescription());
			sb.append("]]></parameterDescription>\n");
		}

		if (parameter.getDefaultValueExpression() != null)
		{
			sb.append("\t\t<defaultValueExpression><![CDATA[");
			sb.append(parameter.getDefaultValueExpression().getText());
			sb.append("]]></defaultValueExpression>\n");
		}

		sb.append("\t</parameter>\n");
	}


	/**
	 *
	 */
	private void writeQuery(JRQuery query)
	{
		sb.append("\t<queryString><![CDATA[");

		sb.append(query.getText());

		sb.append("]]></queryString>\n");
	}


	/**
	 *
	 */
	private void writeField(JRField field)
	{
		sb.append("\t<field");

		sb.append(" name=\"");
		sb.append(field.getName());
		sb.append("\"");

		sb.append(" class=\"");
		sb.append(field.getValueClassName());
		sb.append("\"");

		sb.append(">\n");

		if (field.getDescription() != null)
		{
			sb.append("\t\t<fieldDescription><![CDATA[");
			sb.append(field.getDescription());
			sb.append("]]></fieldDescription>\n");
		}

		sb.append("\t</field>\n");
	}


	/**
	 *
	 */
	private void writeVariable(JRVariable variable)
	{
		sb.append("\t<variable");

		sb.append(" name=\"");
		sb.append(variable.getName());
		sb.append("\"");

		sb.append(" class=\"");
		sb.append(variable.getValueClassName());
		sb.append("\"");

		if (variable.getResetType() != JRVariable.RESET_TYPE_REPORT)
		{
			sb.append(" resetType=\"");
			sb.append((String)JRXmlConstants.getResetTypeMap().get(new Byte(variable.getResetType())));
			sb.append("\"");
		}

		if (variable.getResetGroup() != null)
		{
			sb.append(" resetGroup=\"");
			sb.append(variable.getResetGroup().getName());
			sb.append("\"");
		}

		if (variable.getIncrementType() != JRVariable.RESET_TYPE_NONE)
		{
			sb.append(" incrementType=\"");
			sb.append((String)JRXmlConstants.getResetTypeMap().get(new Byte(variable.getIncrementType())));
			sb.append("\"");
		}

		if (variable.getIncrementGroup() != null)
		{
			sb.append(" incrementGroup=\"");
			sb.append(variable.getIncrementGroup().getName());
			sb.append("\"");
		}

		if (variable.getCalculation() != JRVariable.CALCULATION_NOTHING)
		{
			sb.append(" calculation=\"");
			sb.append((String)JRXmlConstants.getCalculationMap().get(new Byte(variable.getCalculation())));
			sb.append("\"");
		}

		if (variable.getIncrementerFactoryClassName() != null)
		{
			sb.append(" incrementerFactoryClass=\"");
			sb.append(variable.getIncrementerFactoryClassName());
			sb.append("\"");
		}

		sb.append(">\n");

		if (variable.getExpression() != null)
		{
			sb.append("\t\t<variableExpression><![CDATA[");
			sb.append(variable.getExpression().getText());
			sb.append("]]></variableExpression>\n");
		}

		if (variable.getInitialValueExpression() != null)
		{
			sb.append("\t\t<initialValueExpression><![CDATA[");
			sb.append(variable.getInitialValueExpression().getText());
			sb.append("]]></initialValueExpression>\n");
		}

		sb.append("\t</variable>\n");
	}


	/**
	 *
	 */
	private void writeGroup(JRGroup group)
	{
		sb.append("\t<group");

		sb.append(" name=\"");
		sb.append(group.getName());
		sb.append("\"");

		if (group.isStartNewColumn())
		{
			sb.append(" isStartNewColumn=\"");
			sb.append(group.isStartNewColumn());
			sb.append("\"");
		}

		if (group.isStartNewPage())
		{
			sb.append(" isStartNewPage=\"");
			sb.append(group.isStartNewPage());
			sb.append("\"");
		}

		if (group.isResetPageNumber())
		{
			sb.append(" isResetPageNumber=\"");
			sb.append(group.isResetPageNumber());
			sb.append("\"");
		}

		if (group.isReprintHeaderOnEachPage())
		{
			sb.append(" isReprintHeaderOnEachPage=\"");
			sb.append(group.isReprintHeaderOnEachPage());
			sb.append("\"");
		}

		if (group.getMinHeightToStartNewPage() > 0)
		{
			sb.append(" minHeightToStartNewPage=\"");
			sb.append(group.getMinHeightToStartNewPage());
			sb.append("\"");
		}

		sb.append(">\n");

		if (group.getExpression() != null)
		{
			sb.append("\t\t<groupExpression><![CDATA[");
			sb.append(group.getExpression().getText());
			sb.append("]]></groupExpression>\n");
		}

		if (group.getGroupHeader() != null)
		{
			sb.append("\t\t<groupHeader>\n");
			writeBand(group.getGroupHeader());
			sb.append("\t\t</groupHeader>\n");
		}

		if (group.getGroupFooter() != null)
		{
			sb.append("\t\t<groupFooter>\n");
			writeBand(group.getGroupFooter());
			sb.append("\t\t</groupFooter>\n");
		}

		sb.append("\t</group>\n");
	}


	/**
	 *
	 */
	private void writeBand(JRBand band)
	{
		sb.append("\t\t<band");

		if (band.getHeight() > 0)
		{
			sb.append(" height=\"");
			sb.append(band.getHeight());
			sb.append("\"");
		}

		if (!band.isSplitAllowed())
		{
			sb.append(" isSplitAllowed=\"");
			sb.append(band.isSplitAllowed());
			sb.append("\"");
		}

		sb.append(">\n");

		/*   */
		if (band.getPrintWhenExpression() != null)
		{
			sb.append("\t\t\t<printWhenExpression><![CDATA[");
			sb.append(band.getPrintWhenExpression().getText());
			sb.append("]]></printWhenExpression>\n");
		}

		/*   */
		List children = band.getChildren();
		if (children != null && children.size() > 0)
		{
			for(int i = 0; i < children.size(); i++)
			{
				((JRChild)children.get(i)).writeXml(this);
			}
		}

		sb.append("\t\t</band>\n");
	}


	/**
	 *
	 */
	public void writeElementGroup(JRElementGroup elementGroup)
	{
		sb.append("\t\t\t<elementGroup>\n");

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

		sb.append("\t\t\t</elementGroup>\n");
	}


	/**
	 *
	 */
	public void writeLine(JRLine line)
	{
		sb.append("\t\t\t<line");

		if (line.getDirection() != JRLine.DIRECTION_TOP_DOWN)
		{
			sb.append(" direction=\"");
			sb.append((String)JRXmlConstants.getDirectionMap().get(new Byte(line.getDirection())));
			sb.append("\"");
		}

		sb.append(">\n");

		writeReportElement(line);
		writeGraphicElement(line);

		sb.append("\t\t\t</line>\n");
	}


	/**
	 *
	 */
	private void writeReportElement(JRElement element)
	{
		sb.append("\t\t\t\t<reportElement");

		if (element.getKey() != null)
		{
			sb.append(" key=\"");
			sb.append(element.getKey());
			sb.append("\"");
		}

		if (element.getPositionType() != JRElement.POSITION_TYPE_FIX_RELATIVE_TO_TOP)
		{
			sb.append(" positionType=\"");
			sb.append((String)JRXmlConstants.getPositionTypeMap().get(new Byte(element.getPositionType())));
			sb.append("\"");
		}

		if (element.getStretchType() != JRElement.STRETCH_TYPE_NO_STRETCH)
		{
			sb.append(" stretchType=\"");
			sb.append((String)JRXmlConstants.getStretchTypeMap().get(new Byte(element.getStretchType())));
			sb.append("\"");
		}

		if (!element.isPrintRepeatedValues())
		{
			sb.append(" isPrintRepeatedValues=\"");
			sb.append(element.isPrintRepeatedValues());
			sb.append("\"");
		}

		if (
			(element instanceof JRLine && element.getMode() != JRElement.MODE_OPAQUE) ||
			(element instanceof JRRectangle && element.getMode() != JRElement.MODE_OPAQUE) ||
			(element instanceof JREllipse && element.getMode() != JRElement.MODE_OPAQUE) ||
			(element instanceof JRImage && element.getMode() != JRElement.MODE_TRANSPARENT) ||
			(element instanceof JRTextElement && element.getMode() != JRElement.MODE_TRANSPARENT) ||
			(element instanceof JRSubreport && element.getMode() != JRElement.MODE_TRANSPARENT)
			)
		{
			sb.append(" mode=\"");
			sb.append((String)JRXmlConstants.getModeMap().get(new Byte(element.getMode())));
			sb.append("\"");
		}

		sb.append(" x=\"");
		sb.append(element.getX());
		sb.append("\"");

		sb.append(" y=\"");
		sb.append(element.getY());
		sb.append("\"");

		sb.append(" width=\"");
		sb.append(element.getWidth());
		sb.append("\"");

		sb.append(" height=\"");
		sb.append(element.getHeight());
		sb.append("\"");

		if (element.isRemoveLineWhenBlank())
		{
			sb.append(" isRemoveLineWhenBlank=\"");
			sb.append(element.isRemoveLineWhenBlank());
			sb.append("\"");
		}

		if (element.isPrintInFirstWholeBand())
		{
			sb.append(" isPrintInFirstWholeBand=\"");
			sb.append(element.isPrintInFirstWholeBand());
			sb.append("\"");
		}

		if (element.isPrintWhenDetailOverflows())
		{
			sb.append(" isPrintWhenDetailOverflows=\"");
			sb.append(element.isPrintWhenDetailOverflows());
			sb.append("\"");
		}

		if (element.getPrintWhenGroupChanges() != null)
		{
			sb.append(" printWhenGroupChanges=\"");
			sb.append(element.getPrintWhenGroupChanges().getName());
			sb.append("\"");
		}

		if (element.getForecolor().getRGB() != Color.black.getRGB())
		{
			sb.append(" forecolor=\"#");
			sb.append(Integer.toHexString(element.getForecolor().getRGB() & colorMask));
			sb.append("\"");
		}

		if (element.getBackcolor().getRGB() != Color.white.getRGB())
		{
			sb.append(" backcolor=\"#");
			sb.append(Integer.toHexString(element.getBackcolor().getRGB() & colorMask));
			sb.append("\"");
		}

		if (element.getPrintWhenExpression() != null)
		{
			sb.append(">\n");
			
			sb.append("\t\t\t\t\t<printWhenExpression><![CDATA[");
			sb.append(element.getPrintWhenExpression().getText());
			sb.append("]]></printWhenExpression>\n");

			sb.append("\t\t\t\t</reportElement>\n");
		}
		else
		{
			sb.append("/>\n");
		}
	}


	/**
	 *
	 */
	private void writeGraphicElement(JRGraphicElement element)
	{
		sb.append("\t\t\t\t<graphicElement");

		if (
			(element instanceof JRLine && element.getPen() != JRGraphicElement.PEN_1_POINT) ||
			(element instanceof JRRectangle && element.getPen() != JRGraphicElement.PEN_1_POINT) ||
			(element instanceof JREllipse && element.getPen() != JRGraphicElement.PEN_1_POINT) ||
			(element instanceof JRImage && element.getPen() != JRGraphicElement.PEN_NONE)
			)
		{
			sb.append(" pen=\"");
			sb.append((String)JRXmlConstants.getPenMap().get(new Byte(element.getPen())));
			sb.append("\"");
		}

		if (element.getFill() != JRGraphicElement.FILL_SOLID)
		{
			sb.append(" fill=\"");
			sb.append((String)JRXmlConstants.getFillMap().get(new Byte(element.getFill())));
			sb.append("\"");
		}

		sb.append("/>\n");
	}


	/**
	 *
	 */
	public void writeRectangle(JRRectangle rectangle)
	{
		sb.append("\t\t\t<rectangle");

		if (rectangle.getRadius() != 0)
		{
			sb.append(" radius=\"");
			sb.append(rectangle.getRadius());
			sb.append("\"");
		}

		sb.append(">\n");

		writeReportElement(rectangle);
		writeGraphicElement(rectangle);

		sb.append("\t\t\t</rectangle>\n");
	}


	/**
	 *
	 */
	public void writeEllipse(JREllipse ellipse)
	{
		sb.append("\t\t\t<ellipse>\n");

		writeReportElement(ellipse);
		writeGraphicElement(ellipse);

		sb.append("\t\t\t</ellipse>\n");
	}


	/**
	 *
	 */
	public void writeImage(JRImage image)
	{
		sb.append("\t\t\t<image");

		if (image.getScaleImage() != JRImage.SCALE_IMAGE_RETAIN_SHAPE)
		{
			sb.append(" scaleImage=\"");
			sb.append((String)JRXmlConstants.getScaleImageMap().get(new Byte(image.getScaleImage())));
			sb.append("\"");
		}

		if (image.getHorizontalAlignment() != JRAlignment.HORIZONTAL_ALIGN_LEFT)
		{
			sb.append(" hAlign=\"");
			sb.append((String)JRXmlConstants.getHorizontalAlignMap().get(new Byte(image.getHorizontalAlignment())));
			sb.append("\"");
		}

		if (image.getVerticalAlignment() != JRAlignment.VERTICAL_ALIGN_TOP)
		{
			sb.append(" vAlign=\"");
			sb.append((String)JRXmlConstants.getVerticalAlignMap().get(new Byte(image.getVerticalAlignment())));
			sb.append("\"");
		}

		if (image.isOwnUsingCache() != null)
		{
			sb.append(" isUsingCache=\"");
			sb.append(image.isOwnUsingCache());
			sb.append("\"");
		}

		if (image.isLazy())
		{
			sb.append(" isLazy=\"");
			sb.append(image.isLazy());
			sb.append("\"");
		}

		if (image.getOnErrorType() != JRImage.ON_ERROR_TYPE_ERROR)
		{
			sb.append(" onErrorType=\"");
			sb.append((String)JRXmlConstants.getOnErrorTypeMap().get(new Byte(image.getOnErrorType())));
			sb.append("\"");
		}

		if (image.getEvaluationTime() != JRExpression.EVALUATION_TIME_NOW)
		{
			sb.append(" evaluationTime=\"");
			sb.append((String)JRXmlConstants.getEvaluationTimeMap().get(new Byte(image.getEvaluationTime())));
			sb.append("\"");
		}

		if (image.getEvaluationGroup() != null)
		{
			sb.append(" evaluationGroup=\"");
			sb.append(image.getEvaluationGroup().getName());
			sb.append("\"");
		}

		if (image.getHyperlinkType() != JRHyperlink.HYPERLINK_TYPE_NONE)
		{
			sb.append(" hyperlinkType=\"");
			sb.append((String)JRXmlConstants.getHyperlinkTypeMap().get(new Byte(image.getHyperlinkType())));
			sb.append("\"");
		}

		if (image.getHyperlinkTarget() != JRHyperlink.HYPERLINK_TARGET_SELF)
		{
			sb.append(" hyperlinkTarget=\"");
			sb.append((String)JRXmlConstants.getHyperlinkTargetMap().get(new Byte(image.getHyperlinkTarget())));
			sb.append("\"");
		}
		
		if (image.getBookmarkLevel() != JRAnchor.NO_BOOKMARK)
		{
			sb.append(" bookmarkLevel=\"");
			sb.append(image.getBookmarkLevel());
			sb.append("\"");
		}

		sb.append(">\n");

		writeReportElement(image);
		writeBox(image.getBox());
		writeGraphicElement(image);

		if (image.getExpression() != null)
		{
			sb.append("\t\t\t\t<imageExpression");
	
			sb.append(" class=\"");
			sb.append(image.getExpression().getValueClassName());//FIXME class is mandatory in verifier
			sb.append("\"");
	
			sb.append("><![CDATA[");
	
			sb.append(image.getExpression().getText());
			sb.append("]]></imageExpression>\n");
		}

		if (image.getAnchorNameExpression() != null)
		{
			sb.append("\t\t\t\t<anchorNameExpression><![CDATA[");
			sb.append(image.getAnchorNameExpression().getText());
			sb.append("]]></anchorNameExpression>\n");
		}

		if (image.getHyperlinkReferenceExpression() != null)
		{
			sb.append("\t\t\t\t<hyperlinkReferenceExpression><![CDATA[");
			sb.append(image.getHyperlinkReferenceExpression().getText());
			sb.append("]]></hyperlinkReferenceExpression>\n");
		}

		if (image.getHyperlinkAnchorExpression() != null)
		{
			sb.append("\t\t\t\t<hyperlinkAnchorExpression><![CDATA[");
			sb.append(image.getHyperlinkAnchorExpression().getText());
			sb.append("]]></hyperlinkAnchorExpression>\n");
		}

		if (image.getHyperlinkPageExpression() != null)
		{
			sb.append("\t\t\t\t<hyperlinkPageExpression><![CDATA[");
			sb.append(image.getHyperlinkPageExpression().getText());
			sb.append("]]></hyperlinkPageExpression>\n");
		}

		sb.append("\t\t\t</image>\n");
	}


	/**
	 *
	 */
	private void writeBox(JRBox box)
	{
		if (box != null)
		{
			StringBuffer tmpBuffer = new StringBuffer();

			if (box.getBorder() != JRGraphicElement.PEN_NONE)
			{
				tmpBuffer.append(" border=\"");
				tmpBuffer.append((String)JRXmlConstants.getPenMap().get(new Byte(box.getBorder())));
				tmpBuffer.append("\"");
			}
			if (box.getBorderColor() != null)
			{
				tmpBuffer.append(" borderColor=\"#");
				tmpBuffer.append(Integer.toHexString(box.getBorderColor().getRGB() & colorMask));
				tmpBuffer.append("\"");
			}
			if (box.getPadding() > 0)
			{
				tmpBuffer.append(" padding=\"");
				tmpBuffer.append(box.getPadding());
				tmpBuffer.append("\"");
			}
		

			if (box.getOwnTopBorder() != null)
			{
				tmpBuffer.append(" topBorder=\"");
				tmpBuffer.append((String)JRXmlConstants.getPenMap().get(new Byte(box.getOwnTopBorder().byteValue())));
				tmpBuffer.append("\"");
			}
			if (box.getOwnTopBorderColor() != null)
			{
				tmpBuffer.append(" topBorderColor=\"#");
				tmpBuffer.append(Integer.toHexString(box.getOwnTopBorderColor().getRGB() & colorMask));
				tmpBuffer.append("\"");
			}
			if (box.getOwnTopPadding() != null)
			{
				tmpBuffer.append(" topPadding=\"");
				tmpBuffer.append(box.getOwnTopPadding());
				tmpBuffer.append("\"");
			}

			
			if (box.getOwnLeftBorder() != null)
			{
				tmpBuffer.append(" leftBorder=\"");
				tmpBuffer.append((String)JRXmlConstants.getPenMap().get(new Byte(box.getOwnLeftBorder().byteValue())));
				tmpBuffer.append("\"");
			}
			if (box.getOwnLeftBorderColor() != null)
			{
				tmpBuffer.append(" leftBorderColor=\"#");
				tmpBuffer.append(Integer.toHexString(box.getOwnLeftBorderColor().getRGB() & colorMask));
				tmpBuffer.append("\"");
			}
			if (box.getOwnLeftPadding() != null)
			{
				tmpBuffer.append(" leftPadding=\"");
				tmpBuffer.append(box.getOwnLeftPadding());
				tmpBuffer.append("\"");
			}

			
			if (box.getOwnBottomBorder() != null)
			{
				tmpBuffer.append(" bottomBorder=\"");
				tmpBuffer.append((String)JRXmlConstants.getPenMap().get(new Byte(box.getOwnBottomBorder().byteValue())));
				tmpBuffer.append("\"");
			}
			if (box.getOwnBottomBorderColor() != null)
			{
				tmpBuffer.append(" bottomBorderColor=\"#");
				tmpBuffer.append(Integer.toHexString(box.getOwnBottomBorderColor().getRGB() & colorMask));
				tmpBuffer.append("\"");
			}
			if (box.getOwnBottomPadding() != null)
			{
				tmpBuffer.append(" bottomPadding=\"");
				tmpBuffer.append(box.getOwnBottomPadding());
				tmpBuffer.append("\"");
			}

			
			if (box.getOwnRightBorder() != null)
			{
				tmpBuffer.append(" rightBorder=\"");
				tmpBuffer.append((String)JRXmlConstants.getPenMap().get(new Byte(box.getOwnRightBorder().byteValue())));
				tmpBuffer.append("\"");
			}
			if (box.getOwnRightBorderColor() != null)
			{
				tmpBuffer.append(" rightBorderColor=\"#");
				tmpBuffer.append(Integer.toHexString(box.getOwnRightBorderColor().getRGB() & colorMask));
				tmpBuffer.append("\"");
			}
			if (box.getOwnRightPadding() != null)
			{
				tmpBuffer.append(" rightPadding=\"");
				tmpBuffer.append(box.getOwnRightPadding());
				tmpBuffer.append("\"");
			}

			
			if (tmpBuffer.length() > 0)
			{
				sb.append("\t\t\t\t<box");
				sb.append(tmpBuffer.toString());
				sb.append("/>\n");
			}
		}
	}


	/**
	 *
	 */
	public void writeStaticText(JRStaticText staticText)
	{
		sb.append("\t\t\t<staticText>\n");

		writeReportElement(staticText);
		writeBox(staticText.getBox());
		writeTextElement(staticText);

		if (staticText.getText() != null)
		{
			sb.append("\t\t\t\t<text><![CDATA[");
			sb.append(staticText.getText());
			sb.append("]]></text>\n");
		}

		sb.append("\t\t\t</staticText>\n");
	}


	/**
	 *
	 */
	private void writeTextElement(JRTextElement textElement)
	{
		sb.append("\t\t\t\t<textElement");

		if (textElement.getHorizontalAlignment() != JRAlignment.HORIZONTAL_ALIGN_LEFT)
		{
			sb.append(" textAlignment=\"");
			sb.append((String)JRXmlConstants.getHorizontalAlignMap().get(new Byte(textElement.getHorizontalAlignment())));
			sb.append("\"");
		}

		if (textElement.getVerticalAlignment() != JRAlignment.VERTICAL_ALIGN_TOP)
		{
			sb.append(" verticalAlignment=\"");
			sb.append((String)JRXmlConstants.getVerticalAlignMap().get(new Byte(textElement.getVerticalAlignment())));
			sb.append("\"");
		}

		if (textElement.getRotation() != JRTextElement.ROTATION_NONE)
		{
			sb.append(" rotation=\"");
			sb.append((String)JRXmlConstants.getRotationMap().get(new Byte(textElement.getRotation())));
			sb.append("\"");
		}

		if (textElement.getLineSpacing() != JRTextElement.LINE_SPACING_SINGLE)
		{
			sb.append(" lineSpacing=\"");
			sb.append((String)JRXmlConstants.getLineSpacingMap().get(new Byte(textElement.getLineSpacing())));
			sb.append("\"");
		}

		if (textElement.isStyledText())
		{
			sb.append(" isStyledText=\"");
			sb.append(textElement.isStyledText());
			sb.append("\"");
		}

		String font = writeFont(textElement.getFont());
		if (font != null)
		{
			sb.append(">\n");

			sb.append("\t\t\t\t\t" + font + "\n");
			
			sb.append("\t\t\t\t</textElement>\n");
		}
		else
		{
			sb.append("/>\n");
		}
	}


	/**
	 *
	 */
	private String writeFont(JRFont font)
	{
		String fontChunk = null;
		
		if (font != null)
		{
			StringBuffer tmpBuffer = new StringBuffer();

			if (font.getReportFont() != null)
			{
				JRFont baseFont = 
					(JRFont)fontsMap.get(
						font.getReportFont().getName()
						);
				if(baseFont != null)
				{
					tmpBuffer.append(" reportFont=\"");
					tmpBuffer.append(font.getReportFont().getName());
					tmpBuffer.append("\"");
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
		
			if (font.getOwnFontName() != null)
			{
				tmpBuffer.append(" fontName=\"");
				tmpBuffer.append(font.getOwnFontName());
				tmpBuffer.append("\"");
			}

			if (font.getOwnSize() != null)
			{
				tmpBuffer.append(" size=\"");
				tmpBuffer.append(font.getOwnSize());
				tmpBuffer.append("\"");
			}

			if (font.isOwnBold() != null)
			{
				tmpBuffer.append(" isBold=\"");
				tmpBuffer.append(font.isOwnBold());
				tmpBuffer.append("\"");
			}

			if (font.isOwnItalic() != null)
			{
				tmpBuffer.append(" isItalic=\"");
				tmpBuffer.append(font.isOwnItalic());
				tmpBuffer.append("\"");
			}
	
			if (font.isOwnUnderline() != null)
			{
				tmpBuffer.append(" isUnderline=\"");
				tmpBuffer.append(font.isOwnUnderline());
				tmpBuffer.append("\"");
			}
	
			if (font.isOwnStrikeThrough() != null)
			{
				tmpBuffer.append(" isStrikeThrough=\"");
				tmpBuffer.append(font.isOwnStrikeThrough());
				tmpBuffer.append("\"");
			}

			if (font.getOwnPdfFontName() != null)
			{
				tmpBuffer.append(" pdfFontName=\"");
				tmpBuffer.append(font.getOwnPdfFontName());
				tmpBuffer.append("\"");
			}

			if (font.getOwnPdfEncoding() != null)
			{
				tmpBuffer.append(" pdfEncoding=\"");
				tmpBuffer.append(font.getOwnPdfEncoding());
				tmpBuffer.append("\"");
			}

			if (font.isOwnPdfEmbedded() != null)
			{
				tmpBuffer.append(" isPdfEmbedded=\"");
				tmpBuffer.append(font.isOwnPdfEmbedded());
				tmpBuffer.append("\"");
			}

			if (tmpBuffer.length() > 0)
			{
				fontChunk = "<font" + tmpBuffer.toString() + "/>";
			}
		}
		
		return fontChunk;
	}


	/**
	 *
	 */
	public void writeTextField(JRTextField textField)
	{
		sb.append("\t\t\t<textField");

		if (textField.isStretchWithOverflow())
		{
			sb.append(" isStretchWithOverflow=\"");
			sb.append(textField.isStretchWithOverflow());
			sb.append("\"");
		}

		if (textField.getEvaluationTime() != JRExpression.EVALUATION_TIME_NOW)
		{
			sb.append(" evaluationTime=\"");
			sb.append((String)JRXmlConstants.getEvaluationTimeMap().get(new Byte(textField.getEvaluationTime())));
			sb.append("\"");
		}

		if (textField.getEvaluationGroup() != null)
		{
			sb.append(" evaluationGroup=\"");
			sb.append(textField.getEvaluationGroup().getName());
			sb.append("\"");
		}

		if (textField.getPattern() != null)
		{
			sb.append(" pattern=\"");
			sb.append(textField.getPattern());
			sb.append("\"");
		}

		if (textField.isBlankWhenNull())
		{
			sb.append(" isBlankWhenNull=\"");
			sb.append(textField.isBlankWhenNull());
			sb.append("\"");
		}

		if (textField.getHyperlinkType() != JRHyperlink.HYPERLINK_TYPE_NONE)
		{
			sb.append(" hyperlinkType=\"");
			sb.append((String)JRXmlConstants.getHyperlinkTypeMap().get(new Byte(textField.getHyperlinkType())));
			sb.append("\"");
		}

		if (textField.getHyperlinkTarget() != JRHyperlink.HYPERLINK_TARGET_SELF)
		{
			sb.append(" hyperlinkTarget=\"");
			sb.append((String)JRXmlConstants.getHyperlinkTargetMap().get(new Byte(textField.getHyperlinkTarget())));
			sb.append("\"");
		}
		
		if (textField.getBookmarkLevel() != JRAnchor.NO_BOOKMARK)
		{
			sb.append(" bookmarkLevel=\"");
			sb.append(textField.getBookmarkLevel());
			sb.append("\"");
		}

		sb.append(">\n");

		writeReportElement(textField);
		writeBox(textField.getBox());
		writeTextElement(textField);

		if (textField.getExpression() != null)
		{
			sb.append("\t\t\t\t<textFieldExpression");
	
			sb.append(" class=\"");
			sb.append(textField.getExpression().getValueClassName());
			sb.append("\"");
	
			sb.append("><![CDATA[");
	
			sb.append(textField.getExpression().getText());
			sb.append("]]></textFieldExpression>\n");
		}

		if (textField.getAnchorNameExpression() != null)
		{
			sb.append("\t\t\t\t<anchorNameExpression><![CDATA[");
			sb.append(textField.getAnchorNameExpression().getText());
			sb.append("]]></anchorNameExpression>\n");
		}

		if (textField.getHyperlinkReferenceExpression() != null)
		{
			sb.append("\t\t\t\t<hyperlinkReferenceExpression><![CDATA[");
			sb.append(textField.getHyperlinkReferenceExpression().getText());
			sb.append("]]></hyperlinkReferenceExpression>\n");
		}

		if (textField.getHyperlinkAnchorExpression() != null)
		{
			sb.append("\t\t\t\t<hyperlinkAnchorExpression><![CDATA[");
			sb.append(textField.getHyperlinkAnchorExpression().getText());
			sb.append("]]></hyperlinkAnchorExpression>\n");
		}

		if (textField.getHyperlinkPageExpression() != null)
		{
			sb.append("\t\t\t\t<hyperlinkPageExpression><![CDATA[");
			sb.append(textField.getHyperlinkPageExpression().getText());
			sb.append("]]></hyperlinkPageExpression>\n");
		}
		
		sb.append("\t\t\t</textField>\n");
	}


	/**
	 *
	 */
	public void writeSubreport(JRSubreport subreport)
	{
		sb.append("\t\t\t<subreport");

		if (subreport.isOwnUsingCache() != null)
		{
			sb.append(" isUsingCache=\"");
			sb.append(subreport.isOwnUsingCache());
			sb.append("\"");
		}

		sb.append(">\n");

		writeReportElement(subreport);

		if (subreport.getParametersMapExpression() != null)
		{
			sb.append("\t\t\t\t<parametersMapExpression><![CDATA[");
			sb.append(subreport.getParametersMapExpression().getText());
			sb.append("]]></parametersMapExpression>\n");
		}

		/*   */
		JRSubreportParameter[] parameters = subreport.getParameters();
		if (parameters != null && parameters.length > 0)
		{
			for(int i = 0; i < parameters.length; i++)
			{
				writeSubreportParameter(parameters[i]);
			}
		}

		if (subreport.getConnectionExpression() != null)
		{
			sb.append("\t\t\t\t<connectionExpression><![CDATA[");
			sb.append(subreport.getConnectionExpression().getText());
			sb.append("]]></connectionExpression>\n");
		}

		if (subreport.getDataSourceExpression() != null)
		{
			sb.append("\t\t\t\t<dataSourceExpression><![CDATA[");
			sb.append(subreport.getDataSourceExpression().getText());
			sb.append("]]></dataSourceExpression>\n");
		}

		JRSubreportReturnValue[] returnValues = subreport.getReturnValues();
		if (returnValues != null && returnValues.length > 0)
		{
			for(int i = 0; i < returnValues.length; i++)
			{
				writeSubreportReturnValue(returnValues[i]);
			}
		}

		if (subreport.getExpression() != null)
		{
			sb.append("\t\t\t\t<subreportExpression");
	
			sb.append(" class=\"");
			sb.append(subreport.getExpression().getValueClassName());
			sb.append("\"");
	
			sb.append("><![CDATA[");
	
			sb.append(subreport.getExpression().getText());
			sb.append("]]></subreportExpression>\n");
		}

		sb.append("\t\t\t</subreport>\n");
	}


	/**
	 *
	 */
	private void writeSubreportParameter(JRSubreportParameter subreportParameter)
	{
		sb.append("\t\t\t\t<subreportParameter");

		sb.append(" name=\"");
		sb.append(subreportParameter.getName());
		sb.append("\"");

		sb.append(">\n");

		if (subreportParameter.getExpression() != null)
		{
			sb.append("\t\t\t\t\t<subreportParameterExpression><![CDATA[");
			sb.append(subreportParameter.getExpression().getText());
			sb.append("]]></subreportParameterExpression>\n");
		}

		sb.append("\t\t\t\t</subreportParameter>\n");
	}


	/**
	 *
	 * @param chart
	 */
	private void writeChart(JRChart chart)
	{
		sb.append("\t\t\t\t<chart");

		if (!chart.isShowLegend())
			sb.append(" isShowLegend=\"false\"");

		if (chart.getEvaluationTime() != JRExpression.EVALUATION_TIME_NOW)
			sb.append(" evaluationTime=\"" + JRXmlConstants.getEvaluationTimeMap().get(new Byte(chart.getEvaluationTime())) + "\"");

		if (chart.getEvaluationTime() == JRExpression.EVALUATION_TIME_GROUP)
			sb.append(" evaluationGroup=\"" + chart.getEvaluationGroup().getName() + "\"");

		if (chart.getHyperlinkType() != JRHyperlink.HYPERLINK_TYPE_NONE)
		{
			sb.append(" hyperlinkType=\"");
			sb.append((String)JRXmlConstants.getHyperlinkTypeMap().get(new Byte(chart.getHyperlinkType())));
			sb.append("\"");
		}

		if (chart.getHyperlinkTarget() != JRHyperlink.HYPERLINK_TARGET_SELF)
		{
			sb.append(" hyperlinkTarget=\"");
			sb.append((String)JRXmlConstants.getHyperlinkTargetMap().get(new Byte(chart.getHyperlinkTarget())));
			sb.append("\"");
		}
		
		if (chart.getBookmarkLevel() != JRAnchor.NO_BOOKMARK)
		{
			sb.append(" bookmarkLevel=\"");
			sb.append(chart.getBookmarkLevel());
			sb.append("\"");
		}

		sb.append(">\n");

		writeReportElement(chart);
		writeBox(chart.getBox());

		// write title
		sb.append("\t\t\t\t\t<chartTitle");
		if (chart.getTitlePosition() != JRChart.TITLE_POSITION_TOP)
		{
			sb.append(" position=\"" + JRXmlConstants.getChartTitlePositionMap().get(new Byte(chart.getTitlePosition())) + "\"");
		}
		if (chart.getTitleColor().getRGB() != Color.black.getRGB())
		{
			sb.append(" color=\"#");
			sb.append(Integer.toHexString(chart.getTitleColor().getRGB() & colorMask));
			sb.append("\"");
		}
		sb.append(">\n");
		String titleFont = writeFont(chart.getTitleFont());
		if (titleFont != null)
		{
			sb.append("\t\t\t\t\t\t" + titleFont +"\n");
		}
		if (chart.getTitleExpression() != null)
		{
			sb.append("\t\t\t\t\t\t<titleExpression><![CDATA[");
			sb.append(chart.getTitleExpression().getText());
			sb.append("]]></titleExpression>\n");
		}
		sb.append("\t\t\t\t\t</chartTitle>\n");

		// write subtitle
		sb.append("\t\t\t\t\t<chartSubtitle");
		if (chart.getSubtitleColor().getRGB() != Color.black.getRGB())
		{
			sb.append(" color=\"#");
			sb.append(Integer.toHexString(chart.getSubtitleColor().getRGB() & colorMask));
			sb.append("\"");
		}
		sb.append(">\n");
		String subtitleFont = writeFont(chart.getSubtitleFont());
		if (subtitleFont != null)
		{
			sb.append("\t\t\t\t\t\t" + subtitleFont +"\n");
		}
		if (chart.getSubtitleExpression() != null)
		{
			sb.append("\t\t\t\t\t\t<subtitleExpression><![CDATA[");
			sb.append(chart.getSubtitleExpression().getText());
			sb.append("]]></subtitleExpression>\n");
		}
		sb.append("\t\t\t\t\t</chartSubtitle>\n");

		if (chart.getAnchorNameExpression() != null)
		{
			sb.append("\t\t\t\t\t<anchorNameExpression><![CDATA[");
			sb.append(chart.getAnchorNameExpression().getText());
			sb.append("]]></anchorNameExpression>\n");
		}

		if (chart.getHyperlinkReferenceExpression() != null)
		{
			sb.append("\t\t\t\t\t<hyperlinkReferenceExpression><![CDATA[");
			sb.append(chart.getHyperlinkReferenceExpression().getText());
			sb.append("]]></hyperlinkReferenceExpression>\n");
		}

		if (chart.getHyperlinkAnchorExpression() != null)
		{
			sb.append("\t\t\t\t\t<hyperlinkAnchorExpression><![CDATA[");
			sb.append(chart.getHyperlinkAnchorExpression().getText());
			sb.append("]]></hyperlinkAnchorExpression>\n");
		}

		if (chart.getHyperlinkPageExpression() != null)
		{
			sb.append("\t\t\t\t\t<hyperlinkPageExpression><![CDATA[");
			sb.append(chart.getHyperlinkPageExpression().getText());
			sb.append("]]></hyperlinkPageExpression>\n");
		}

		sb.append("\t\t\t\t</chart>\n");

	}


	/**
	 *
	 * @param dataset
	 */
	private void writeDataset(JRChartDataset dataset)
	{
		sb.append("\t\t\t\t\t<dataset");

		if (dataset.getResetType() != JRVariable.RESET_TYPE_REPORT)
			sb.append(" resetType=\"" + JRXmlConstants.getResetTypeMap().get(new Byte(dataset.getResetType())) + "\"");

		if (dataset.getResetType() == JRVariable.RESET_TYPE_GROUP)
			sb.append(" resetGroup=\"" + dataset.getResetGroup().getName() + "\"");

		if (dataset.getIncrementType() != JRVariable.RESET_TYPE_NONE)
			sb.append(" incrementType=\"" + JRXmlConstants.getResetTypeMap().get(new Byte(dataset.getIncrementType())) + "\"");

		if (dataset.getIncrementType() == JRVariable.RESET_TYPE_GROUP)
			sb.append(" incrementGroup=\"" + dataset.getIncrementGroup().getName() + "\"");

		sb.append("/>\n");
	}


	/**
	 *
	 * @param dataset
	 */
	private void writeCategoryDataSet(JRCategoryDataset dataset)
	{
		sb.append("\t\t\t\t<categoryDataset>\n");

		writeDataset(dataset);

		/*   */
		JRCategorySeries[] categorySeries = dataset.getSeries();
		if (categorySeries != null && categorySeries.length > 0)
		{
			for(int i = 0; i < categorySeries.length; i++)
			{
				writeCategorySeries(categorySeries[i]);
			}
		}

		sb.append("\t\t\t\t</categoryDataset>\n");
	}
	
	
	private void writeTimeSeriesDataset( JRTimeSeriesDataset dataset )
	{
		sb.append( "\t\t\t\t<timeSeriesDataset");
		if (dataset.getTimePeriod() != null && !Day.class.getName().equals(dataset.getTimePeriod().getName()))
		{
			sb.append( " timePeriod=\"" + JRXmlConstants.getTimePeriodName( dataset.getTimePeriod() ) + "\"" );
		}
		sb.append(">\n" );
		
		writeDataset( dataset );
		
		JRTimeSeries[] timeSeries = dataset.getSeries();
		if( timeSeries != null && timeSeries.length > 0 ){
			for( int i = 0; i < timeSeries.length; i++ ){
				writeTimeSeries( timeSeries[i] );
			}
		}
		sb.append( "\t\t\t\t</timeSeriesDataset>\n" );
		
	}
	
	
	private void writeTimePeriodDataset( JRTimePeriodDataset dataset ){
		sb.append( "\t\t\t\t<timePeriodDataset>\n");
		writeDataset( dataset );
		
		JRTimePeriodSeries[] timePeriodSeries = dataset.getSeries();
		if( timePeriodSeries != null && timePeriodSeries.length > 0 ){
			for( int i = 0; i < timePeriodSeries.length; i++ ){
				writeTimePeriodSeries( timePeriodSeries[i] );
			}
		}
		sb.append( "\t\t\t\t</timePeriodDataset>\n" );
	}


	/**
	 *
	 * @param categorySeries
	 */
	private void writeCategorySeries(JRCategorySeries categorySeries)
	{
		sb.append("\t\t\t\t\t<categorySeries>\n");

		if (categorySeries.getSeriesExpression() != null)
		{
			sb.append("\t\t\t\t\t\t<seriesExpression><![CDATA[");
			sb.append(categorySeries.getSeriesExpression().getText());
			sb.append("]]></seriesExpression>\n");
		}

		if (categorySeries.getCategoryExpression() != null)
		{
			sb.append("\t\t\t\t\t\t<categoryExpression><![CDATA[");
			sb.append(categorySeries.getCategoryExpression().getText());
			sb.append("]]></categoryExpression>\n");
		}

		if (categorySeries.getValueExpression() != null)
		{
			sb.append("\t\t\t\t\t\t<valueExpression><![CDATA[");
			sb.append(categorySeries.getValueExpression().getText());
			sb.append("]]></valueExpression>\n");
		}

		if (categorySeries.getLabelExpression() != null) {
			sb.append("\t\t\t\t\t\t<labelExpression><![CDATA[");
			sb.append(categorySeries.getLabelExpression().getText());
			sb.append("]]></labelExpression>\n");
		}

		sb.append("\t\t\t\t\t</categorySeries>\n");
	}

	/**
	 * 
	 * @param dataset
	 */
	private void writeXyzDataset( JRXyzDataset dataset ){
		sb.append( "\t\t\t\t<xyzDataset>\n" );
		writeDataset( dataset );
		
		JRXyzSeries[] series = dataset.getSeries();
		if( series != null && series.length > 0 ){
			for( int i = 0; i < series.length; i++ ){
				writeXyzSeries( series[i] ); 
			}
		}
		sb.append( "\t\t\t\t</xyzDataset>\n" );
	}
	
	
	/**
	 * 
	 * @param series
	 */
	private void writeXyzSeries( JRXyzSeries series ){
		sb.append( "\t\t\t\t\t<xyzSeries>\n" );
		
		if (series.getSeriesExpression() != null)
		{
			sb.append( "\t\t\t\t\t\t<seriesExpression><![CDATA[" );
			sb.append( series.getSeriesExpression().getText() );
			sb.append("]]></seriesExpression>\n");
		}
		
		if (series.getXValueExpression() != null)
		{
			sb.append( "\t\t\t\t\t\t<xValueExpression><![CDATA[" );
			sb.append( series.getXValueExpression().getText() );
			sb.append( "]]></xValueExpression>\n" );
		}
		
		if (series.getYValueExpression() != null)
		{
			sb.append( "\t\t\t\t\t\t<yValueExpression><![CDATA[" );
			sb.append( series.getYValueExpression().getText() );
			sb.append( "]]></yValueExpression>\n" );
		}
		
		if (series.getZValueExpression() != null)
		{
			sb.append( "\t\t\t\t\t\t<zValueExpression><![CDATA[" );
			sb.append( series.getZValueExpression().getText() );
			sb.append( "]]></zValueExpression>\n" );
		}
		
		sb.append( "\t\t\t\t\t</xyzSeries>\n" );
	}

	/**
	 *
	 * @param xySeries
	 */
	private void writeXySeries(JRXySeries xySeries)
	{
		sb.append("\t\t\t\t\t<xySeries>\n");

		if (xySeries.getSeriesExpression() != null)
		{
			sb.append("\t\t\t\t\t\t<seriesExpression><![CDATA[");
			sb.append(xySeries.getSeriesExpression().getText());
			sb.append("]]></seriesExpression>\n");
		}

		if (xySeries.getXValueExpression() != null)
		{
			sb.append("\t\t\t\t\t\t<xValueExpression><![CDATA[");
			sb.append(xySeries.getXValueExpression().getText());
			sb.append("]]></xValueExpression>\n");
		}

		if (xySeries.getYValueExpression() != null)
		{
			sb.append("\t\t\t\t\t\t<yValueExpression><![CDATA[");
			sb.append(xySeries.getYValueExpression().getText());
			sb.append("]]></yValueExpression>\n");
		}
		
		if( xySeries.getLabelExpression() != null ){
			sb.append("\t\t\t\t\t\t<labelExpression><![CDATA[");
			sb.append(xySeries.getLabelExpression().getText());
			sb.append("]]></labelExpression>\n");
		}
		
		sb.append("\t\t\t\t\t</xySeries>\n");
	}


	/**
	 *
	 * @param dataset
	 */
	private void writeXyDataset(JRXyDataset dataset)
	{
		sb.append("\t\t\t\t<xyDataset>\n");

		writeDataset(dataset);

		/*   */
		JRXySeries[] xySeries = dataset.getSeries();
		if (xySeries != null && xySeries.length > 0)
		{
			for(int i = 0; i < xySeries.length; i++)
			{
				writeXySeries(xySeries[i]);
			}
		}

		sb.append("\t\t\t\t</xyDataset>\n");
	}


	/**
	 *
	 * @param timeSeries
	 */
	private void writeTimeSeries(JRTimeSeries timeSeries)
	{
		sb.append("\t\t\t\t\t<timeSeries>\n");

		if (timeSeries.getSeriesExpression() != null)
		{
			sb.append("\t\t\t\t\t\t<seriesExpression><![CDATA[");
			sb.append(timeSeries.getSeriesExpression().getText());
			sb.append("]]></seriesExpression>\n");
		}

		if (timeSeries.getTimePeriodExpression() != null)
		{
			sb.append("\t\t\t\t\t\t<timePeriodExpression><![CDATA[");
			sb.append(timeSeries.getTimePeriodExpression().getText());
			sb.append("]]></timePeriodExpression>\n");
		}

		if (timeSeries.getValueExpression() != null)
		{
			sb.append("\t\t\t\t\t\t<valueExpression><![CDATA[");
			sb.append(timeSeries.getValueExpression().getText());
			sb.append("]]></valueExpression>\n");
		}
		
		if( timeSeries.getLabelExpression() != null )
		{
			sb.append("\t\t\t\t\t\t<labelExpression><![CDATA[");
			sb.append(timeSeries.getLabelExpression().getText());
			sb.append("]]></labelExpression>\n");
		}

		sb.append("\t\t\t\t\t</timeSeries>\n");
	}
	
	
	private void writeTimePeriodSeries( JRTimePeriodSeries timePeriodSeries ){
		sb.append( "\t\t\t\t\t<timePeriodSeries>\n" );
		
		if (timePeriodSeries.getSeriesExpression() != null)
		{
			sb.append( "\t\t\t\t\t\t<seriesExpression><![CDATA[" );
			sb.append( timePeriodSeries.getSeriesExpression().getText() );
			sb.append( "]]></seriesExpression>\n" );
		}
		
		if (timePeriodSeries.getStartDateExpression() != null)
		{
			sb.append( "\t\t\t\t\t\t<startDateExpression><![CDATA[" );
			sb.append( timePeriodSeries.getStartDateExpression().getText() );
			sb.append( "]]></startDateExpression>\n" );
		}
		
		if (timePeriodSeries.getEndDateExpression() != null)
		{
			sb.append( "\t\t\t\t\t\t<endDateExpression><![CDATA[" );
			sb.append( timePeriodSeries.getEndDateExpression().getText() );
			sb.append( "]]></endDateExpression>\n" );
		}
		
		if (timePeriodSeries.getValueExpression() != null)
		{
			sb.append( "\t\t\t\t\t\t<valueExpression><![CDATA[" );
			sb.append( timePeriodSeries.getValueExpression().getText() );
			sb.append( "]]></valueExpression>\n" );
		}
		
		if( timePeriodSeries.getLabelExpression() != null ){
			sb.append( "\t\t\t\t\t\t<labelExpression><![CDATA[" );
			sb.append( timePeriodSeries.getLabelExpression().getText() );
			sb.append( "]]></labelExpression>\n" );
		}
		
		sb.append( "\t\t\t\t\t</timePeriodSeries>\n" );
	}

	
	/**
	 *
	 */
	public void writePieDataset(JRPieDataset dataset)
	{
		sb.append("\t\t\t\t<pieDataset>\n");

		writeDataset(dataset);

		if (dataset.getKeyExpression() != null)
		{
			sb.append("\t\t\t\t\t<keyExpression><![CDATA[");
			sb.append(dataset.getKeyExpression().getText());
			sb.append("]]></keyExpression>\n");
		}

		if (dataset.getValueExpression() != null)
		{
			sb.append("\t\t\t\t\t<valueExpression><![CDATA[");
			sb.append(dataset.getValueExpression().getText());
			sb.append("]]></valueExpression>\n");
		}

		if (dataset.getLabelExpression() != null) {
			sb.append("\t\t\t\t\t<labelExpression><![CDATA[");
			sb.append(dataset.getLabelExpression().getText());
			sb.append("]]></labelExpression>\n");
		}

		sb.append("\t\t\t\t</pieDataset>\n");
	}


	/**
	 *
	 * @param plot
	 */
	private void writePlot(JRChartPlot plot)
	{
		sb.append("\t\t\t\t\t<plot");

		if (plot.getBackcolor() != null)
		{
			sb.append(" backcolor=\"#");
			sb.append(Integer.toHexString(plot.getBackcolor().getRGB() & colorMask));
			sb.append("\"");
		}

		if (!plot.getOrientation().equals(PlotOrientation.VERTICAL))
			sb.append(" orientation=\"" + JRXmlConstants.getPlotOrientationMap().get(PlotOrientation.HORIZONTAL)+ "\"");

		if (plot.getBackgroundAlpha() != 1.0f)
			sb.append(" backgroundAlpha=\"" + plot.getBackgroundAlpha() + "\"");

		if (plot.getForegroundAlpha() != 1.0f)
			sb.append(" foregroundAlpha=\"" + plot.getForegroundAlpha() + "\"");

		sb.append("/>\n");
	}


	/**
	 *
	 * @param chart
	 */
	public void writePieChart(JRChart chart)
	{
		sb.append("\t\t\t<pieChart>\n");

		writeChart(chart);
		writePieDataset((JRPieDataset) chart.getDataset());

		// write plot
		sb.append("\t\t\t\t<piePlot>\n");
		writePlot(chart.getPlot());
		sb.append("\t\t\t\t</piePlot>\n");

		sb.append("\t\t\t</pieChart>\n");

	}


	/**
	 *
	 * @param chart
	 */
	public void writePie3DChart(JRChart chart)
	{
		sb.append("\t\t\t<pie3DChart>\n");

		writeChart(chart);
		writePieDataset((JRPieDataset) chart.getDataset());

		// write plot
		JRPie3DPlot plot = (JRPie3DPlot) chart.getPlot();
		sb.append("\t\t\t\t<pie3DPlot");
		if (plot.getDepthFactor() != JRPie3DPlot.DEPTH_FACTOR_DEFAULT)
			sb.append(" depthFactor=\"" + String.valueOf(plot.getDepthFactor()) + "\"");
		sb.append(">\n");
		writePlot(chart.getPlot());
		sb.append("\t\t\t\t</pie3DPlot>\n");

		sb.append("\t\t\t</pie3DChart>\n");

	}


	/**
	 *
	 * @param plot
	 */
	private void writeBarPlot(JRBarPlot plot)
	{
		sb.append("\t\t\t\t<barPlot");
		if (plot.isShowLabels())
			sb.append(" isShowTickLabels=\"true\"");
		if (!plot.isShowTickLabels())
			sb.append(" isShowTickLabels=\"false\"");
		if (!plot.isShowTickMarks())
			sb.append(" isShowTickMarks=\"false\"");
		sb.append(">\n");
		writePlot(plot);

		if (plot.getCategoryAxisLabelExpression() != null) {
			sb.append("\t\t\t\t\t<categoryAxisLabelExpression><![CDATA[");
			sb.append(plot.getCategoryAxisLabelExpression().getText());
			sb.append("]]></categoryAxisLabelExpression>\n");
		}

		if (plot.getValueAxisLabelExpression() != null) {
			sb.append("\t\t\t\t\t<valueAxisLabelExpression><![CDATA[");
			sb.append(plot.getValueAxisLabelExpression().getText());
			sb.append("]]></valueAxisLabelExpression>\n");
		}

		sb.append("\t\t\t\t</barPlot>\n");
	}
	
	
	/**
	 * 
	 */
	private void writeBubblePlot( JRBubblePlot plot ){
		sb.append( "\t\t\t\t<bubblePlot scaleType=\"" );
		
		Map scaleTypeMap = JRXmlConstants.getScaleTypeMap();
		sb.append( scaleTypeMap.get( new Integer( plot.getScaleType() )));
		
		sb.append( "\">\n" );
		writePlot( plot );
		if( plot.getXAxisLabelExpression() != null ){
			sb.append( "\t\t\t\t\t<xAxisLabelExpression><![CDATA[" );
			sb.append( plot.getXAxisLabelExpression().getText() );
			sb.append( "]]></xAxisLabelExpression>\n" );
		}
		
		if( plot.getYAxisLabelExpression() != null ){
			sb.append( "\t\t\t\t\t<yAxisLabelExpression><![CDATA[" );
			sb.append( plot.getYAxisLabelExpression().getText() );
			sb.append( "]]></yAxisLabelExpression>\n" );
		}
		
		sb.append( "\t\t\t\t</bubblePlot>\n" );
	}


	/**
	 *
	 * @param plot
	 */
	private void writeLinePlot(JRLinePlot plot)
	{
		sb.append("\t\t\t\t<linePlot");
		if (!plot.isShowLines())
			sb.append(" isShowLines=\"false\"");
		if (!plot.isShowShapes())
			sb.append(" isShowShapes=\"false\"");
		sb.append(">\n");
		writePlot(plot);

		if (plot.getCategoryAxisLabelExpression() != null) {
			sb.append("\t\t\t\t\t<categoryAxisLabelExpression><![CDATA[");
			sb.append(plot.getCategoryAxisLabelExpression().getText());
			sb.append("]]></categoryAxisLabelExpression>\n");
		}

		if (plot.getValueAxisLabelExpression() != null) {
			sb.append("\t\t\t\t\t<valueAxisLabelExpression><![CDATA[");
			sb.append(plot.getValueAxisLabelExpression().getText());
			sb.append("]]></valueAxisLabelExpression>\n");
		}

		sb.append("\t\t\t\t</linePlot>\n");
	}
	
	
	private void writeTimeSeriesPlot( JRTimeSeriesPlot plot ){
		sb.append( "\t\t\t\t<timeSeriesPlot" );
		if( !plot.isShowLines() ){
			sb.append( " isShowLines=\"false\" " );
		}
		if( !plot.isShowShapes() ){
			sb.append( "isShowShapes=\"false\" ");
		}
		sb.append( ">\n" );
		
		writePlot( plot );
		
		if(plot.getTimeAxisLabelExpression() != null ){
			sb.append( "\t\t\t\t\t<timeAxisLabelExpression><![CDATA[" );
			sb.append( plot.getTimeAxisLabelExpression().getText() );
			sb.append( "]]></timeAxisLabelExpression>\n" );
		}
		
		if( plot.getValueAxisLabelExpression() != null ){
			sb.append( "\t\t\t\t\t<valueAxisLabelExpression><![CDATA[" );
			sb.append( plot.getValueAxisLabelExpression().getText() );
			sb.append( "]]></valueAxisLabelExpression>\n" );
		}
		
		sb.append( "\t\t\t\t</timeSeriesPlot>\n" );
	}


	/**
	 *
	 * @param plot
	 */
	public void writeBar3DPlot(JRBar3DPlot plot)
	{
		sb.append("\t\t\t\t<bar3DPlot");
		if (plot.isShowLabels())
			sb.append(" isShowLabels=\"true\"");
		if (plot.getXOffset() != BarRenderer3D.DEFAULT_X_OFFSET)
			sb.append(" xOffset=\"" + plot.getXOffset() + "\"");
		if (plot.getYOffset() != BarRenderer3D.DEFAULT_Y_OFFSET)
			sb.append(" yOffset=\"" + plot.getYOffset() + "\"");
		sb.append(">\n");
		writePlot(plot);

		if (plot.getCategoryAxisLabelExpression() != null) {
			sb.append("\t\t\t\t\t<categoryAxisLabelExpression><![CDATA[");
			sb.append(plot.getCategoryAxisLabelExpression().getText());
			sb.append("]]></categoryAxisLabelExpression>\n");
		}

		if (plot.getValueAxisLabelExpression() != null) {
			sb.append("\t\t\t\t\t<valueAxisLabelExpression><![CDATA[");
			sb.append(plot.getValueAxisLabelExpression().getText());
			sb.append("]]></valueAxisLabelExpression>\n");
		}

		sb.append("\t\t\t\t</bar3DPlot>\n");
	}


	/**
	 *
	 * @param chart
	 */
	public void writeBarChart(JRChart chart)
	{
		sb.append("\t\t\t<barChart>\n");

		writeChart(chart);
		writeCategoryDataSet((JRCategoryDataset) chart.getDataset());
		writeBarPlot((JRBarPlot) chart.getPlot());

		sb.append("\t\t\t</barChart>\n");
	}


	/**
	 *
	 * @param chart
	 */
	public void writeBar3DChart(JRChart chart)
	{
		sb.append("\t\t\t<bar3DChart>\n");

		writeChart(chart);
		writeCategoryDataSet((JRCategoryDataset) chart.getDataset());
		writeBar3DPlot((JRBar3DPlot) chart.getPlot());

		sb.append("\t\t\t</bar3DChart>\n");
	}
	
	
	/**
	 * 
	 * @param chart
	 */
	public void writeBubbleChart( JRChart chart ){
		sb.append( "\t\t\t<bubbleChart>\n" );
		writeChart( chart );
		writeXyzDataset( (JRXyzDataset)chart.getDataset() );
		writeBubblePlot( (JRBubblePlot)chart.getPlot());
		sb.append( "\t\t\t</bubbleChart>\n" );
	}


	/**
	 *
	 * @param chart
	 */
	public void writeStackedBarChart(JRChart chart)
	{
		sb.append("\t\t\t<stackedBarChart>\n");

		writeChart(chart);
		writeCategoryDataSet((JRCategoryDataset) chart.getDataset());
		writeBarPlot((JRBarPlot) chart.getPlot());

		sb.append("\t\t\t</stackedBarChart>\n");
	}


	/**
	 *
	 * @param chart
	 */
	public void writeStackedBar3DChart(JRChart chart)
	{
		sb.append("\t\t\t<stackedBar3DChart>\n");

		writeChart(chart);
		writeCategoryDataSet((JRCategoryDataset) chart.getDataset());
		writeBar3DPlot((JRBar3DPlot) chart.getPlot());

		sb.append("\t\t\t</stackedBar3DChart>\n");
	}


	/**
	 *
	 * @param chart
	 */
	public void writeLineChart(JRChart chart)
	{
		sb.append("\t\t\t<lineChart>\n");

		writeChart(chart);
		writeCategoryDataSet((JRCategoryDataset) chart.getDataset());
		writeLinePlot((JRLinePlot) chart.getPlot());

		sb.append("\t\t\t</lineChart>\n");
	}
	
	
	public void writeTimeSeriesChart( JRChart chart ){
		sb.append( "\t\t\t<timeSeriesChart>\n" );
		writeChart( chart );
		writeTimeSeriesDataset( (JRTimeSeriesDataset)chart.getDataset() );
		writeTimeSeriesPlot( (JRTimeSeriesPlot)chart.getPlot() );
		sb.append( "\t\t\t</timeSeriesChart>\n" );
	}

	public void writeHighLowDataset(JRHighLowDataset dataset)
	{
		sb.append("\t\t\t\t<highLowDataset>\n");

		writeDataset(dataset);

		if (dataset.getSeriesExpression() != null)
		{
			sb.append("\t\t\t\t\t<seriesExpression><![CDATA[");
			sb.append(dataset.getSeriesExpression().getText());
			sb.append("]]></seriesExpression>\n");
		}

		if (dataset.getDateExpression() != null)
		{
			sb.append("\t\t\t\t\t<dateExpression><![CDATA[");
			sb.append(dataset.getDateExpression().getText());
			sb.append("]]></dateExpression>\n");
		}

		if (dataset.getHighExpression() != null)
		{
			sb.append("\t\t\t\t\t<highExpression><![CDATA[");
			sb.append(dataset.getHighExpression().getText());
			sb.append("]]></highExpression>\n");
		}

		if (dataset.getLowExpression() != null)
		{
			sb.append("\t\t\t\t\t<lowExpression><![CDATA[");
			sb.append(dataset.getLowExpression().getText());
			sb.append("]]></lowExpression>\n");
		}

		if (dataset.getOpenExpression() != null)
		{
			sb.append("\t\t\t\t\t<openExpression><![CDATA[");
			sb.append(dataset.getOpenExpression().getText());
			sb.append("]]></openExpression>\n");
		}

		if (dataset.getCloseExpression() != null)
		{
			sb.append("\t\t\t\t\t<closeExpression><![CDATA[");
			sb.append(dataset.getCloseExpression().getText());
			sb.append("]]></closeExpression>\n");
		}

		if (dataset.getVolumeExpression() != null)
		{
			sb.append("\t\t\t\t\t<volumeExpression><![CDATA[");
			sb.append(dataset.getVolumeExpression().getText());
			sb.append("]]></volumeExpression>\n");
		}

		sb.append("\t\t\t\t</highLowDataset>\n");
	}


	public void writeHighLowChart(JRChart chart)
	{
		sb.append("\t\t\t<highLowChart>\n");

		writeChart(chart);
		writeHighLowDataset((JRHighLowDataset) chart.getDataset());

		JRHighLowPlot plot = (JRHighLowPlot) chart.getPlot();
		sb.append("\t\t\t\t<highLowPlot");
		if (!plot.isShowOpenTicks())
			sb.append(" isShowOpenTicks=\"false\"");
		if (!plot.isShowCloseTicks())
			sb.append(" isShowCloseTicks=\"false\"");
		sb.append(">\n");
		writePlot(plot);

		if (plot.getTimeAxisLabelExpression() != null) {
			sb.append("\t\t\t\t\t<timeAxisLabelExpression><![CDATA[");
			sb.append(plot.getTimeAxisLabelExpression().getText());
			sb.append("]]></timeAxisLabelExpression>\n");
		}

		if (plot.getValueAxisLabelExpression() != null) {
			sb.append("\t\t\t\t\t<valueAxisLabelExpression><![CDATA[");
			sb.append(plot.getValueAxisLabelExpression().getText());
			sb.append("]]></valueAxisLabelExpression>\n");
		}

		sb.append("\t\t\t\t</highLowPlot>\n");

		sb.append("\t\t\t</highLowChart>\n");
	}


	public void writeCandlestickChart(JRChart chart)
	{
		sb.append("\t\t\t<candlestickChart>\n");

		writeChart(chart);
		writeHighLowDataset((JRHighLowDataset) chart.getDataset());

		JRCandlestickPlot plot = (JRCandlestickPlot) chart.getPlot();
		sb.append("\t\t\t\t<candlestickPlot");
		if (!plot.isShowVolume())
			sb.append(" isShowVolume=\"false\"");
		sb.append(">\n");
		writePlot(plot);

		if (plot.getTimeAxisLabelExpression() != null) {
			sb.append("\t\t\t\t\t<timeAxisLabelExpression><![CDATA[");
			sb.append(plot.getTimeAxisLabelExpression().getText());
			sb.append("]]></timeAxisLabelExpression>\n");
		}

		if (plot.getValueAxisLabelExpression() != null) {
			sb.append("\t\t\t\t\t<valueAxisLabelExpression><![CDATA[");
			sb.append(plot.getValueAxisLabelExpression().getText());
			sb.append("]]></valueAxisLabelExpression>\n");
		}

		sb.append("\t\t\t\t</candlestickPlot>\n");

		sb.append("\t\t\t</candlestickChart>\n");
	}

	/**
	 *
	 * @param plot
	 */
	private void writeAreaPlot(JRAreaPlot plot)
	{
		sb.append("\t\t\t\t<areaPlot>\n");
		writePlot(plot);

		if (plot.getCategoryAxisLabelExpression() != null) {
			sb.append("\t\t\t\t\t<categoryAxisLabelExpression><![CDATA[");
			sb.append(plot.getCategoryAxisLabelExpression().getText());
			sb.append("]]></categoryAxisLabelExpression>\n");
		}

		if (plot.getValueAxisLabelExpression() != null) {
			sb.append("\t\t\t\t\t<valueAxisLabelExpression><![CDATA[");
			sb.append(plot.getValueAxisLabelExpression().getText());
			sb.append("]]></valueAxisLabelExpression>\n");
		}

		sb.append("\t\t\t\t</areaPlot>\n");
	}


	/**
	 *
	 * @param chart
	 */
	public void writeAreaChart(JRChart chart)
	{
		sb.append("\t\t\t<areaChart>\n");

		writeChart(chart);
		writeCategoryDataSet((JRCategoryDataset) chart.getDataset());
		writeAreaPlot((JRAreaPlot) chart.getPlot());

		sb.append("\t\t\t</areaChart>\n");
	}


	/**
	 *
	 * @param plot
	 */
	private void writeScatterPlot(JRScatterPlot plot)
	{
		sb.append("\t\t\t\t<scatterPlot");
		if (!plot.isShowLines())
			sb.append(" isShowLines=\"false\"");
		if (!plot.isShowShapes())
			sb.append(" isShowShapes=\"false\"");
		sb.append(">\n");
		writePlot(plot);

		if (plot.getXAxisLabelExpression() != null) {
			sb.append("\t\t\t\t\t<xAxisLabelExpression><![CDATA[");
			sb.append(plot.getXAxisLabelExpression().getText());
			sb.append("]]></xAxisLabelExpression>\n");
		}

		if (plot.getYAxisLabelExpression() != null) {
			sb.append("\t\t\t\t\t<yAxisLabelExpression><![CDATA[");
			sb.append(plot.getYAxisLabelExpression().getText());
			sb.append("]]></yAxisLabelExpression>\n");
		}

		sb.append("\t\t\t\t</scatterPlot>\n");
	}


	/**
	 *
	 * @param chart
	 */
	public void writeScatterChart(JRChart chart)
	{
		sb.append("\t\t\t<scatterChart>\n");

		writeChart(chart);
		writeXyDataset((JRXyDataset) chart.getDataset());
		writeScatterPlot((JRScatterPlot) chart.getPlot());

		sb.append("\t\t\t</scatterChart>\n");
	}


	/**
	 *
	 * @param chart
	 */
	public void writeXyAreaChart(JRChart chart)
	{
		sb.append("\t\t\t<xyAreaChart>\n");

		writeChart(chart);
		writeXyDataset((JRXyDataset) chart.getDataset());
		writeAreaPlot((JRAreaPlot) chart.getPlot());

		sb.append("\t\t\t</xyAreaChart>\n");
	}


	/**
	 *
	 * @param chart
	 */
	public void writeXyBarChart(JRChart chart)
	{
		sb.append("\t\t\t<xyBarChart>\n");

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

		sb.append("\t\t\t</xyBarChart>\n");
	}


	/**
	 *
	 */
	public void writeXyLineChart(JRChart chart)
	{
		sb.append("\t\t\t<xyLineChart>\n");

		writeChart(chart);
		writeXyDataset((JRXyDataset) chart.getDataset());
		writeLinePlot((JRLinePlot) chart.getPlot());

		sb.append("\t\t\t</xyLineChart>\n");
	}


	public void writeChartTag(JRChart chart)
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


	private void writeSubreportReturnValue(JRSubreportReturnValue returnValue)
	{
		sb.append("\t\t\t\t<returnValue");
		if (returnValue.getSubreportVariable() != null)
		{
			sb.append(" subreportVariable=\"");
			sb.append(returnValue.getSubreportVariable());
			sb.append("\"");
		}
		if (returnValue.getToVariable() != null)
		{
			sb.append(" toVariable=\"");
			sb.append(returnValue.getToVariable());
			sb.append("\"");
		}
		if (returnValue.getCalculation() != JRVariable.CALCULATION_NOTHING)
		{
			sb.append(" calculation=\"");
			sb.append((String)JRXmlConstants.getCalculationMap().get(new Byte(returnValue.getCalculation())));
			sb.append("\"");
		}
		if (returnValue.getIncrementerFactoryClassName() != null)
		{
			sb.append(" incrementerFactoryClass=\"");
			sb.append(returnValue.getIncrementerFactoryClassName());
			sb.append("\"");
}
		sb.append("/>\n");
	}
}
