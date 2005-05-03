/*
 * ============================================================================
 *                   GNU Lesser General Public License
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

import net.sf.jasperreports.engine.JRAlignment;
import net.sf.jasperreports.engine.JRBand;
import net.sf.jasperreports.engine.JRBox;
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
import net.sf.jasperreports.engine.JRSubreportParameter;
import net.sf.jasperreports.engine.JRTextElement;
import net.sf.jasperreports.engine.JRTextField;
import net.sf.jasperreports.engine.JRVariable;
import net.sf.jasperreports.engine.util.JRStringUtil;


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
	 *
	 */
	private static final int colorMask = Integer.parseInt("FFFFFF", 16);


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
	public static String writeReport(JRReport report, String encoding) throws JRException
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
	protected String writeReport() throws JRException
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
			Object child = null;
			for(int i = 0; i < children.size(); i++)
			{
				child = children.get(i);
				if (child instanceof JRElementGroup)
				{
					writeElementGroup((JRElementGroup)child);
				}
				else
				{
					writeElement((JRElement)child);
				}
			}
		}

		sb.append("\t\t</band>\n");
	}


	/**
	 *
	 */
	private void writeElementGroup(JRElementGroup elementGroup)
	{
		sb.append("\t\t\t<elementGroup>\n");

		/*   */
		List children = elementGroup.getChildren();
		if (children != null && children.size() > 0)
		{
			Object child = null;
			for(int i = 0; i < children.size(); i++)
			{
				child = children.get(i);
				if (child instanceof JRElementGroup)
				{
					writeElementGroup((JRElementGroup)child);
				}
				else
				{
					writeElement((JRElement)child);
				}
			}
		}

		sb.append("\t\t\t</elementGroup>\n");
	}


	/**
	 *
	 */
	private void writeElement(JRElement element)
	{
		if(element instanceof JRLine)
		{
			writeLine((JRLine)element);
		}
		else if (element instanceof JRRectangle)
		{
			writeRectangle((JRRectangle)element);
		}
		else if (element instanceof JREllipse)
		{
			writeEllipse((JREllipse)element);
		}
		else if (element instanceof JRImage)
		{
			writeImage((JRImage)element);
		}
		else if (element instanceof JRStaticText)
		{
			writeStaticText((JRStaticText)element);
		}
		else if (element instanceof JRTextField)
		{
			writeTextField((JRTextField)element);
		}
		else if (element instanceof JRSubreport)
		{
			writeSubreport((JRSubreport)element);
		}
	}


	/**
	 *
	 */
	private void writeLine(JRLine line)
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
	private void writeRectangle(JRRectangle rectangle)
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
	private void writeEllipse(JREllipse ellipse)
	{
		sb.append("\t\t\t<ellipse>\n");

		writeReportElement(ellipse);
		writeGraphicElement(ellipse);

		sb.append("\t\t\t</ellipse>\n");
	}


	/**
	 *
	 */
	private void writeImage(JRImage image)
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

		if (!image.isUsingCache())
		{
			sb.append(" isUsingCache=\"");
			sb.append(image.isUsingCache());
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
	private void writeStaticText(JRStaticText staticText)
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

		if (textElement.getTextAlignment() != JRAlignment.HORIZONTAL_ALIGN_LEFT)
		{
			sb.append(" textAlignment=\"");
			sb.append((String)JRXmlConstants.getHorizontalAlignMap().get(new Byte(textElement.getTextAlignment())));
			sb.append("\"");
		}

		if (textElement.getVerticalAlignment() != JRTextElement.VERTICAL_ALIGN_TOP)
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
	private void writeTextField(JRTextField textField)
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
	private void writeSubreport(JRSubreport subreport)
	{
		sb.append("\t\t\t<subreport");

		if (!subreport.isUsingCache())
		{
			sb.append(" isUsingCache=\"");
			sb.append(subreport.isUsingCache());
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
	

}
