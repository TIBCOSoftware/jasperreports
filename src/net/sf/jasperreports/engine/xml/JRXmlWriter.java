/*
 * ============================================================================
 *                   The JasperReports License, Version 1.0
 * ============================================================================
 * 
 * Copyright (C) 2001-2004 Teodor Danciu (teodord@users.sourceforge.net). All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 
 * 3. The end-user documentation included with the redistribution, if any, must
 *    include the following acknowledgment: "This product includes software
 *    developed by Teodor Danciu (http://jasperreports.sourceforge.net)."
 *    Alternately, this acknowledgment may appear in the software itself, if
 *    and wherever such third-party acknowledgments normally appear.
 * 
 * 4. The name "JasperReports" must not be used to endorse or promote products 
 *    derived from this software without prior written permission. For written 
 *    permission, please contact teodord@users.sourceforge.net.
 * 
 * 5. Products derived from this software may not be called "JasperReports", nor 
 *    may "JasperReports" appear in their name, without prior written permission
 *    of Teodor Danciu.
 * 
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS  FOR A PARTICULAR  PURPOSE ARE  DISCLAIMED.  IN NO  EVENT SHALL  THE
 * APACHE SOFTWARE  FOUNDATION  OR ITS CONTRIBUTORS  BE LIABLE FOR  ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL,  EXEMPLARY, OR CONSEQUENTIAL  DAMAGES (INCLU-
 * DING, BUT NOT LIMITED TO, PROCUREMENT  OF SUBSTITUTE GOODS OR SERVICES; LOSS
 * OF USE, DATA, OR  PROFITS; OR BUSINESS  INTERRUPTION)  HOWEVER CAUSED AND ON
 * ANY  THEORY OF LIABILITY,  WHETHER  IN CONTRACT,  STRICT LIABILITY,  OR TORT
 * (INCLUDING  NEGLIGENCE OR  OTHERWISE) ARISING IN  ANY WAY OUT OF THE  USE OF
 * THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

/*
 * ============================================================================
 *                   GNU Lesser General Public License
 * ============================================================================
 *
 * JasperReports - Free Java report-generating library.
 * Copyright (C) 2001-2004 Teodor Danciu teodord@users.sourceforge.net
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
 * Teodor Danciu
 * 173, Calea Calarasilor, Bl. 42, Sc. 1, Ap. 18
 * Postal code 030615, Sector 3
 * Bucharest, ROMANIA
 * Email: teodord@users.sourceforge.net
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
	private StringBuffer sbuffer = null;
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
		sbuffer = new StringBuffer();
		
		sbuffer.append("<?xml version=\"1.0\" encoding=\"" + encoding + "\"?>\n");
		sbuffer.append("<!DOCTYPE jasperReport PUBLIC \"-//JasperReports//DTD Report Design//EN\" \"http://jasperreports.sourceforge.net/dtds/jasperreport.dtd\">\n");
		sbuffer.append("\n");

		sbuffer.append("<jasperReport name=\"");
		sbuffer.append(report.getName());
		sbuffer.append("\"");

		if(report.getColumnCount() != 1)
		{
			sbuffer.append(" columnCount=\"");
			sbuffer.append(report.getColumnCount());
			sbuffer.append("\"");
		}

		if(report.getPrintOrder() != JRReport.PRINT_ORDER_VERTICAL)
		{
			sbuffer.append(" printOrder=\"");
			sbuffer.append((String)JRXmlConstants.getPrintOrderMap().get(new Byte(report.getPrintOrder())));
			sbuffer.append("\"");
		}

		sbuffer.append(" pageWidth=\"");
		sbuffer.append(report.getPageWidth());
		sbuffer.append("\"");

		sbuffer.append(" pageHeight=\"");
		sbuffer.append(report.getPageHeight());
		sbuffer.append("\"");
		
		if(report.getOrientation() != JRReport.ORIENTATION_PORTRAIT)
		{
			sbuffer.append(" orientation=\"");
			sbuffer.append((String)JRXmlConstants.getOrientationMap().get(new Byte(report.getOrientation())));
			sbuffer.append("\"");
		}

		if(report.getWhenNoDataType() != JRReport.WHEN_NO_DATA_TYPE_NO_PAGES)
		{
			sbuffer.append(" whenNoDataType=\"");
			sbuffer.append((String)JRXmlConstants.getWhenNoDataTypeMap().get(new Byte(report.getWhenNoDataType())));
			sbuffer.append("\"");
		}

		sbuffer.append(" columnWidth=\"");
		sbuffer.append(report.getColumnWidth());
		sbuffer.append("\"");
		
		if(report.getColumnSpacing() != 0)
		{
			sbuffer.append(" columnSpacing=\"");
			sbuffer.append(report.getColumnSpacing());
			sbuffer.append("\"");
		}

		sbuffer.append(" leftMargin=\"");
		sbuffer.append(report.getLeftMargin());
		sbuffer.append("\"");
		
		sbuffer.append(" rightMargin=\"");
		sbuffer.append(report.getRightMargin());
		sbuffer.append("\"");
		
		sbuffer.append(" topMargin=\"");
		sbuffer.append(report.getTopMargin());
		sbuffer.append("\"");
		
		sbuffer.append(" bottomMargin=\"");
		sbuffer.append(report.getBottomMargin());
		sbuffer.append("\"");
		
		if(report.isTitleNewPage())
		{
			sbuffer.append(" isTitleNewPage=\"");
			sbuffer.append(report.isTitleNewPage());
			sbuffer.append("\"");
		}

		if(report.isSummaryNewPage())
		{
			sbuffer.append(" isSummaryNewPage=\"");
			sbuffer.append(report.isSummaryNewPage());
			sbuffer.append("\"");
		}

		if(report.getScriptletClass() != null)
		{
			sbuffer.append(" scriptletClass=\"");
			sbuffer.append(report.getScriptletClass());
			sbuffer.append("\"");
		}

		sbuffer.append(">\n");
		
		/*   */
		String[] propertyNames = report.getPropertyNames();
		if (propertyNames != null && propertyNames.length > 0)
		{
			for(int i = 0; i < propertyNames.length; i++)
			{
				String value = report.getProperty(propertyNames[i]);
				if (value != null)
				{
					sbuffer.append("\t<property name=\"");
					sbuffer.append(propertyNames[i]);
					sbuffer.append("\" value=\"");
					sbuffer.append(JRStringUtil.xmlEncode(value));
					sbuffer.append("\"/>\n");
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
			sbuffer.append("\t<background>\n");
			writeBand(report.getBackground());
			sbuffer.append("\t</background>\n");
		}

		if (report.getTitle() != null)
		{
			sbuffer.append("\t<title>\n");
			writeBand(report.getTitle());
			sbuffer.append("\t</title>\n");
		}

		if (report.getPageHeader() != null)
		{
			sbuffer.append("\t<pageHeader>\n");
			writeBand(report.getPageHeader());
			sbuffer.append("\t</pageHeader>\n");
		}

		if (report.getColumnHeader() != null)
		{
			sbuffer.append("\t<columnHeader>\n");
			writeBand(report.getColumnHeader());
			sbuffer.append("\t</columnHeader>\n");
		}

		if (report.getDetail() != null)
		{
			sbuffer.append("\t<detail>\n");
			writeBand(report.getDetail());
			sbuffer.append("\t</detail>\n");
		}

		if (report.getColumnFooter() != null)
		{
			sbuffer.append("\t<columnFooter>\n");
			writeBand(report.getColumnFooter());
			sbuffer.append("\t</columnFooter>\n");
		}

		if (report.getPageFooter() != null)
		{
			sbuffer.append("\t<pageFooter>\n");
			writeBand(report.getPageFooter());
			sbuffer.append("\t</pageFooter>\n");
		}

		if (report.getSummary() != null)
		{
			sbuffer.append("\t<summary>\n");
			writeBand(report.getSummary());
			sbuffer.append("\t</summary>\n");
		}

		sbuffer.append("</jasperReport>\n");

		return sbuffer.toString();
	}


	/**
	 *
	 */
	private void writeReportFont(JRReportFont font)
	{
		sbuffer.append("\t<reportFont");

		sbuffer.append(" name=\"");
		sbuffer.append(font.getName());
		sbuffer.append("\"");

		sbuffer.append(" isDefault=\"");
		sbuffer.append(font.isDefault());
		sbuffer.append("\"");

		sbuffer.append(" fontName=\"");
		sbuffer.append(font.getFontName());
		sbuffer.append("\"");

		sbuffer.append(" size=\"");
		sbuffer.append(font.getSize());
		sbuffer.append("\"");

		sbuffer.append(" isBold=\"");
		sbuffer.append(font.isBold());
		sbuffer.append("\"");

		sbuffer.append(" isItalic=\"");
		sbuffer.append(font.isItalic());
		sbuffer.append("\"");

		sbuffer.append(" isUnderline=\"");
		sbuffer.append(font.isUnderline());
		sbuffer.append("\"");

		sbuffer.append(" isStrikeThrough=\"");
		sbuffer.append(font.isStrikeThrough());
		sbuffer.append("\"");

		sbuffer.append(" pdfFontName=\"");
		sbuffer.append(font.getPdfFontName());
		sbuffer.append("\"");

		sbuffer.append(" pdfEncoding=\"");
		sbuffer.append(font.getPdfEncoding());
		sbuffer.append("\"");

		sbuffer.append(" isPdfEmbedded=\"");
		sbuffer.append(font.isPdfEmbedded());
		sbuffer.append("\"");

		sbuffer.append("/>\n");
	}


	/**
	 *
	 */
	private void writeParameter(JRParameter parameter)
	{
		sbuffer.append("\t<parameter");

		sbuffer.append(" name=\"");
		sbuffer.append(parameter.getName());
		sbuffer.append("\"");

		sbuffer.append(" class=\"");
		sbuffer.append(parameter.getValueClassName());
		sbuffer.append("\"");

		if (!parameter.isForPrompting())
		{
			sbuffer.append(" isForPrompting=\"");
			sbuffer.append(parameter.isForPrompting());
			sbuffer.append("\"");
		}

		sbuffer.append(">\n");

		if (parameter.getDescription() != null)
		{
			sbuffer.append("\t\t<parameterDescription><![CDATA[");
			sbuffer.append(parameter.getDescription());
			sbuffer.append("]]></parameterDescription>\n");
		}

		if (parameter.getDefaultValueExpression() != null)
		{
			sbuffer.append("\t\t<defaultValueExpression><![CDATA[");
			sbuffer.append(parameter.getDefaultValueExpression().getText());
			sbuffer.append("]]></defaultValueExpression>\n");
		}

		sbuffer.append("\t</parameter>\n");
	}


	/**
	 *
	 */
	private void writeQuery(JRQuery query)
	{
		sbuffer.append("\t<queryString><![CDATA[");

		sbuffer.append(query.getText());

		sbuffer.append("]]></queryString>\n");
	}


	/**
	 *
	 */
	private void writeField(JRField field)
	{
		sbuffer.append("\t<field");

		sbuffer.append(" name=\"");
		sbuffer.append(field.getName());
		sbuffer.append("\"");

		sbuffer.append(" class=\"");
		sbuffer.append(field.getValueClassName());
		sbuffer.append("\"");

		sbuffer.append(">\n");

		if (field.getDescription() != null)
		{
			sbuffer.append("\t\t<fieldDescription><![CDATA[");
			sbuffer.append(field.getDescription());
			sbuffer.append("]]></fieldDescription>\n");
		}

		sbuffer.append("\t</field>\n");
	}


	/**
	 *
	 */
	private void writeVariable(JRVariable variable)
	{
		sbuffer.append("\t<variable");

		sbuffer.append(" name=\"");
		sbuffer.append(variable.getName());
		sbuffer.append("\"");

		sbuffer.append(" class=\"");
		sbuffer.append(variable.getValueClassName());
		sbuffer.append("\"");

		if (variable.getResetType() != JRVariable.RESET_TYPE_REPORT)
		{
			sbuffer.append(" resetType=\"");
			sbuffer.append((String)JRXmlConstants.getResetTypeMap().get(new Byte(variable.getResetType())));
			sbuffer.append("\"");
		}

		if (variable.getResetGroup() != null)
		{
			sbuffer.append(" resetGroup=\"");
			sbuffer.append(variable.getResetGroup().getName());
			sbuffer.append("\"");
		}

		if (variable.getCalculation() != JRVariable.CALCULATION_NOTHING)
		{
			sbuffer.append(" calculation=\"");
			sbuffer.append((String)JRXmlConstants.getCalculationMap().get(new Byte(variable.getCalculation())));
			sbuffer.append("\"");
		}

		if (variable.getIncrementerFactoryClassName() != null)
		{
			sbuffer.append(" incrementerFactoryClass=\"");
			sbuffer.append(variable.getIncrementerFactoryClassName());
			sbuffer.append("\"");
		}

		sbuffer.append(">\n");

		if (variable.getExpression() != null)
		{
			sbuffer.append("\t\t<variableExpression><![CDATA[");
			sbuffer.append(variable.getExpression().getText());
			sbuffer.append("]]></variableExpression>\n");
		}

		if (variable.getInitialValueExpression() != null)
		{
			sbuffer.append("\t\t<initialValueExpression><![CDATA[");
			sbuffer.append(variable.getInitialValueExpression().getText());
			sbuffer.append("]]></initialValueExpression>\n");
		}

		sbuffer.append("\t</variable>\n");
	}


	/**
	 *
	 */
	private void writeGroup(JRGroup group)
	{
		sbuffer.append("\t<group");

		sbuffer.append(" name=\"");
		sbuffer.append(group.getName());
		sbuffer.append("\"");

		if (group.isStartNewColumn())
		{
			sbuffer.append(" isStartNewColumn=\"");
			sbuffer.append(group.isStartNewColumn());
			sbuffer.append("\"");
		}

		if (group.isStartNewPage())
		{
			sbuffer.append(" isStartNewPage=\"");
			sbuffer.append(group.isStartNewPage());
			sbuffer.append("\"");
		}

		if (group.isResetPageNumber())
		{
			sbuffer.append(" isResetPageNumber=\"");
			sbuffer.append(group.isResetPageNumber());
			sbuffer.append("\"");
		}

		if (group.isReprintHeaderOnEachPage())
		{
			sbuffer.append(" isReprintHeaderOnEachPage=\"");
			sbuffer.append(group.isReprintHeaderOnEachPage());
			sbuffer.append("\"");
		}

		if (group.getMinHeightToStartNewPage() > 0)
		{
			sbuffer.append(" minHeightToStartNewPage=\"");
			sbuffer.append(group.getMinHeightToStartNewPage());
			sbuffer.append("\"");
		}

		sbuffer.append(">\n");

		if (group.getExpression() != null)
		{
			sbuffer.append("\t\t<groupExpression><![CDATA[");
			sbuffer.append(group.getExpression().getText());
			sbuffer.append("]]></groupExpression>\n");
		}

		if (group.getGroupHeader() != null)
		{
			sbuffer.append("\t\t<groupHeader>\n");
			writeBand(group.getGroupHeader());
			sbuffer.append("\t\t</groupHeader>\n");
		}

		if (group.getGroupFooter() != null)
		{
			sbuffer.append("\t\t<groupFooter>\n");
			writeBand(group.getGroupFooter());
			sbuffer.append("\t\t</groupFooter>\n");
		}

		sbuffer.append("\t</group>\n");
	}


	/**
	 *
	 */
	private void writeBand(JRBand band)
	{
		sbuffer.append("\t\t<band");

		if (band.getHeight() > 0)
		{
			sbuffer.append(" height=\"");
			sbuffer.append(band.getHeight());
			sbuffer.append("\"");
		}

		if (!band.isSplitAllowed())
		{
			sbuffer.append(" isSplitAllowed=\"");
			sbuffer.append(band.isSplitAllowed());
			sbuffer.append("\"");
		}

		sbuffer.append(">\n");

		/*   */
		if (band.getPrintWhenExpression() != null)
		{
			sbuffer.append("\t\t\t<printWhenExpression><![CDATA[");
			sbuffer.append(band.getPrintWhenExpression().getText());
			sbuffer.append("]]></printWhenExpression>\n");
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

		sbuffer.append("\t\t</band>\n");
	}


	/**
	 *
	 */
	private void writeElementGroup(JRElementGroup elementGroup)
	{
		sbuffer.append("\t\t\t<elementGroup>\n");

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

		sbuffer.append("\t\t\t</elementGroup>\n");
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
		sbuffer.append("\t\t\t<line");

		if (line.getDirection() != JRLine.DIRECTION_TOP_DOWN)
		{
			sbuffer.append(" direction=\"");
			sbuffer.append((String)JRXmlConstants.getDirectionMap().get(new Byte(line.getDirection())));
			sbuffer.append("\"");
		}

		sbuffer.append(">\n");

		writeReportElement(line);
		writeGraphicElement(line);

		sbuffer.append("\t\t\t</line>\n");
	}


	/**
	 *
	 */
	private void writeReportElement(JRElement element)
	{
		sbuffer.append("\t\t\t\t<reportElement");

		if (element.getKey() != null)
		{
			sbuffer.append(" key=\"");
			sbuffer.append(element.getKey());
			sbuffer.append("\"");
		}

		if (element.getPositionType() != JRElement.POSITION_TYPE_FIX_RELATIVE_TO_TOP)
		{
			sbuffer.append(" positionType=\"");
			sbuffer.append((String)JRXmlConstants.getPositionTypeMap().get(new Byte(element.getPositionType())));
			sbuffer.append("\"");
		}

		if (element.getStretchType() != JRElement.STRETCH_TYPE_NO_STRETCH)
		{
			sbuffer.append(" stretchType=\"");
			sbuffer.append((String)JRXmlConstants.getStretchTypeMap().get(new Byte(element.getStretchType())));
			sbuffer.append("\"");
		}

		if (!element.isPrintRepeatedValues())
		{
			sbuffer.append(" isPrintRepeatedValues=\"");
			sbuffer.append(element.isPrintRepeatedValues());
			sbuffer.append("\"");
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
			sbuffer.append(" mode=\"");
			sbuffer.append((String)JRXmlConstants.getModeMap().get(new Byte(element.getMode())));
			sbuffer.append("\"");
		}

		sbuffer.append(" x=\"");
		sbuffer.append(element.getX());
		sbuffer.append("\"");

		sbuffer.append(" y=\"");
		sbuffer.append(element.getY());
		sbuffer.append("\"");

		sbuffer.append(" width=\"");
		sbuffer.append(element.getWidth());
		sbuffer.append("\"");

		sbuffer.append(" height=\"");
		sbuffer.append(element.getHeight());
		sbuffer.append("\"");

		if (element.isRemoveLineWhenBlank())
		{
			sbuffer.append(" isRemoveLineWhenBlank=\"");
			sbuffer.append(element.isRemoveLineWhenBlank());
			sbuffer.append("\"");
		}

		if (element.isPrintInFirstWholeBand())
		{
			sbuffer.append(" isPrintInFirstWholeBand=\"");
			sbuffer.append(element.isPrintInFirstWholeBand());
			sbuffer.append("\"");
		}

		if (element.isPrintWhenDetailOverflows())
		{
			sbuffer.append(" isPrintWhenDetailOverflows=\"");
			sbuffer.append(element.isPrintWhenDetailOverflows());
			sbuffer.append("\"");
		}

		if (element.getPrintWhenGroupChanges() != null)
		{
			sbuffer.append(" printWhenGroupChanges=\"");
			sbuffer.append(element.getPrintWhenGroupChanges().getName());
			sbuffer.append("\"");
		}

		if (element.getForecolor().getRGB() != Color.black.getRGB())
		{
			sbuffer.append(" forecolor=\"#");
			sbuffer.append(Integer.toHexString(element.getForecolor().getRGB() & colorMask));
			sbuffer.append("\"");
		}

		if (element.getBackcolor().getRGB() != Color.white.getRGB())
		{
			sbuffer.append(" backcolor=\"#");
			sbuffer.append(Integer.toHexString(element.getBackcolor().getRGB() & colorMask));
			sbuffer.append("\"");
		}

		if (element.getPrintWhenExpression() != null)
		{
			sbuffer.append(">\n");
			
			sbuffer.append("\t\t\t\t\t<printWhenExpression><![CDATA[");
			sbuffer.append(element.getPrintWhenExpression().getText());
			sbuffer.append("]]></printWhenExpression>\n");

			sbuffer.append("\t\t\t\t</reportElement>\n");
		}
		else
		{
			sbuffer.append("/>\n");
		}
	}


	/**
	 *
	 */
	private void writeGraphicElement(JRGraphicElement element)
	{
		sbuffer.append("\t\t\t\t<graphicElement");

		if (
			(element instanceof JRLine && element.getPen() != JRGraphicElement.PEN_1_POINT) ||
			(element instanceof JRRectangle && element.getPen() != JRGraphicElement.PEN_1_POINT) ||
			(element instanceof JREllipse && element.getPen() != JRGraphicElement.PEN_1_POINT) ||
			(element instanceof JRImage && element.getPen() != JRGraphicElement.PEN_NONE)
			)
		{
			sbuffer.append(" pen=\"");
			sbuffer.append((String)JRXmlConstants.getPenMap().get(new Byte(element.getPen())));
			sbuffer.append("\"");
		}

		if (element.getFill() != JRGraphicElement.FILL_SOLID)
		{
			sbuffer.append(" fill=\"");
			sbuffer.append((String)JRXmlConstants.getFillMap().get(new Byte(element.getFill())));
			sbuffer.append("\"");
		}

		sbuffer.append("/>\n");
	}


	/**
	 *
	 */
	private void writeRectangle(JRRectangle rectangle)
	{
		sbuffer.append("\t\t\t<rectangle");

		if (rectangle.getRadius() != 0)
		{
			sbuffer.append(" radius=\"");
			sbuffer.append(rectangle.getRadius());
			sbuffer.append("\"");
		}

		sbuffer.append(">\n");

		writeReportElement(rectangle);
		writeGraphicElement(rectangle);

		sbuffer.append("\t\t\t</rectangle>\n");
	}


	/**
	 *
	 */
	private void writeEllipse(JREllipse ellipse)
	{
		sbuffer.append("\t\t\t<ellipse>\n");

		writeReportElement(ellipse);
		writeGraphicElement(ellipse);

		sbuffer.append("\t\t\t</ellipse>\n");
	}


	/**
	 *
	 */
	private void writeImage(JRImage image)
	{
		sbuffer.append("\t\t\t<image");

		if (image.getScaleImage() != JRImage.SCALE_IMAGE_RETAIN_SHAPE)
		{
			sbuffer.append(" scaleImage=\"");
			sbuffer.append((String)JRXmlConstants.getScaleImageMap().get(new Byte(image.getScaleImage())));
			sbuffer.append("\"");
		}

		if (image.getHorizontalAlignment() != JRAlignment.HORIZONTAL_ALIGN_LEFT)
		{
			sbuffer.append(" hAlign=\"");
			sbuffer.append((String)JRXmlConstants.getHorizontalAlignMap().get(new Byte(image.getHorizontalAlignment())));
			sbuffer.append("\"");
		}

		if (image.getVerticalAlignment() != JRAlignment.VERTICAL_ALIGN_TOP)
		{
			sbuffer.append(" vAlign=\"");
			sbuffer.append((String)JRXmlConstants.getVerticalAlignMap().get(new Byte(image.getVerticalAlignment())));
			sbuffer.append("\"");
		}

		if (!image.isUsingCache())
		{
			sbuffer.append(" isUsingCache=\"");
			sbuffer.append(image.isUsingCache());
			sbuffer.append("\"");
		}

		if (image.getEvaluationTime() != JRExpression.EVALUATION_TIME_NOW)
		{
			sbuffer.append(" evaluationTime=\"");
			sbuffer.append((String)JRXmlConstants.getEvaluationTimeMap().get(new Byte(image.getEvaluationTime())));
			sbuffer.append("\"");
		}

		if (image.getEvaluationGroup() != null)
		{
			sbuffer.append(" evaluationGroup=\"");
			sbuffer.append(image.getEvaluationGroup().getName());
			sbuffer.append("\"");
		}

		if (image.getHyperlinkType() != JRHyperlink.HYPERLINK_TYPE_NONE)
		{
			sbuffer.append(" hyperlinkType=\"");
			sbuffer.append((String)JRXmlConstants.getHyperlinkTypeMap().get(new Byte(image.getHyperlinkType())));
			sbuffer.append("\"");
		}

		sbuffer.append(">\n");

		writeReportElement(image);
		writeGraphicElement(image);

		if (image.getExpression() != null)
		{
			sbuffer.append("\t\t\t\t<imageExpression");
	
			sbuffer.append(" class=\"");
			sbuffer.append(image.getExpression().getValueClassName());//FIXME class is mandatory in verifier
			sbuffer.append("\"");
	
			sbuffer.append("><![CDATA[");
	
			sbuffer.append(image.getExpression().getText());
			sbuffer.append("]]></imageExpression>\n");
		}

		if (image.getAnchorNameExpression() != null)
		{
			sbuffer.append("\t\t\t\t<anchorNameExpression><![CDATA[");
			sbuffer.append(image.getAnchorNameExpression().getText());
			sbuffer.append("]]></anchorNameExpression>\n");
		}

		if (image.getHyperlinkReferenceExpression() != null)
		{
			sbuffer.append("\t\t\t\t<hyperlinkReferenceExpression><![CDATA[");
			sbuffer.append(image.getHyperlinkReferenceExpression().getText());
			sbuffer.append("]]></hyperlinkReferenceExpression>\n");
		}

		if (image.getHyperlinkAnchorExpression() != null)
		{
			sbuffer.append("\t\t\t\t<hyperlinkAnchorExpression><![CDATA[");
			sbuffer.append(image.getHyperlinkAnchorExpression().getText());
			sbuffer.append("]]></hyperlinkAnchorExpression>\n");
		}

		if (image.getHyperlinkPageExpression() != null)
		{
			sbuffer.append("\t\t\t\t<hyperlinkPageExpression><![CDATA[");
			sbuffer.append(image.getHyperlinkPageExpression().getText());
			sbuffer.append("]]></hyperlinkPageExpression>\n");
		}

		sbuffer.append("\t\t\t</image>\n");
	}


	/**
	 *
	 */
	private void writeStaticText(JRStaticText staticText)
	{
		sbuffer.append("\t\t\t<staticText>\n");

		writeReportElement(staticText);
		writeTextElement(staticText);

		if (staticText.getText() != null)
		{
			sbuffer.append("\t\t\t\t<text><![CDATA[");
			sbuffer.append(staticText.getText());
			sbuffer.append("]]></text>\n");
		}

		sbuffer.append("\t\t\t</staticText>\n");
	}


	/**
	 *
	 */
	private void writeTextElement(JRTextElement textElement)
	{
		sbuffer.append("\t\t\t\t<textElement");

		if (textElement.getTextAlignment() != JRAlignment.HORIZONTAL_ALIGN_LEFT)
		{
			sbuffer.append(" textAlignment=\"");
			sbuffer.append((String)JRXmlConstants.getHorizontalAlignMap().get(new Byte(textElement.getTextAlignment())));
			sbuffer.append("\"");
		}

		if (textElement.getVerticalAlignment() != JRTextElement.VERTICAL_ALIGN_TOP)
		{
			sbuffer.append(" verticalAlignment=\"");
			sbuffer.append((String)JRXmlConstants.getVerticalAlignMap().get(new Byte(textElement.getVerticalAlignment())));
			sbuffer.append("\"");
		}

		if (textElement.getRotation() != JRTextElement.ROTATION_NONE)
		{
			sbuffer.append(" rotation=\"");
			sbuffer.append((String)JRXmlConstants.getRotationMap().get(new Byte(textElement.getRotation())));
			sbuffer.append("\"");
		}

		if (textElement.getLineSpacing() != JRTextElement.LINE_SPACING_SINGLE)
		{
			sbuffer.append(" lineSpacing=\"");
			sbuffer.append((String)JRXmlConstants.getLineSpacingMap().get(new Byte(textElement.getLineSpacing())));
			sbuffer.append("\"");
		}

		if (textElement.isStyledText())
		{
			sbuffer.append(" isStyledText=\"");
			sbuffer.append(textElement.isStyledText());
			sbuffer.append("\"");
		}

		String font = writeFont(textElement.getFont());
		if (font != null)
		{
			sbuffer.append(">\n");

			sbuffer.append("\t\t\t\t\t" + font + "\n");
			
			sbuffer.append("\t\t\t\t</textElement>\n");
		}
		else
		{
			sbuffer.append("/>\n");
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
		sbuffer.append("\t\t\t<textField");

		if (textField.isStretchWithOverflow())
		{
			sbuffer.append(" isStretchWithOverflow=\"");
			sbuffer.append(textField.isStretchWithOverflow());
			sbuffer.append("\"");
		}

		if (textField.getEvaluationTime() != JRExpression.EVALUATION_TIME_NOW)
		{
			sbuffer.append(" evaluationTime=\"");
			sbuffer.append((String)JRXmlConstants.getEvaluationTimeMap().get(new Byte(textField.getEvaluationTime())));
			sbuffer.append("\"");
		}

		if (textField.getEvaluationGroup() != null)
		{
			sbuffer.append(" evaluationGroup=\"");
			sbuffer.append(textField.getEvaluationGroup().getName());
			sbuffer.append("\"");
		}

		if (textField.getPattern() != null)
		{
			sbuffer.append(" pattern=\"");
			sbuffer.append(textField.getPattern());
			sbuffer.append("\"");
		}

		if (textField.isBlankWhenNull())
		{
			sbuffer.append(" isBlankWhenNull=\"");
			sbuffer.append(textField.isBlankWhenNull());
			sbuffer.append("\"");
		}

		if (textField.getHyperlinkType() != JRHyperlink.HYPERLINK_TYPE_NONE)
		{
			sbuffer.append(" hyperlinkType=\"");
			sbuffer.append((String)JRXmlConstants.getHyperlinkTypeMap().get(new Byte(textField.getHyperlinkType())));
			sbuffer.append("\"");
		}

		sbuffer.append(">\n");

		writeReportElement(textField);
		writeTextElement(textField);

		if (textField.getExpression() != null)
		{
			sbuffer.append("\t\t\t\t<textFieldExpression");
	
			sbuffer.append(" class=\"");
			sbuffer.append(textField.getExpression().getValueClassName());
			sbuffer.append("\"");
	
			sbuffer.append("><![CDATA[");
	
			sbuffer.append(textField.getExpression().getText());
			sbuffer.append("]]></textFieldExpression>\n");
		}

		if (textField.getAnchorNameExpression() != null)
		{
			sbuffer.append("\t\t\t\t<anchorNameExpression><![CDATA[");
			sbuffer.append(textField.getAnchorNameExpression().getText());
			sbuffer.append("]]></anchorNameExpression>\n");
		}

		if (textField.getHyperlinkReferenceExpression() != null)
		{
			sbuffer.append("\t\t\t\t<hyperlinkReferenceExpression><![CDATA[");
			sbuffer.append(textField.getHyperlinkReferenceExpression().getText());
			sbuffer.append("]]></hyperlinkReferenceExpression>\n");
		}

		if (textField.getHyperlinkAnchorExpression() != null)
		{
			sbuffer.append("\t\t\t\t<hyperlinkAnchorExpression><![CDATA[");
			sbuffer.append(textField.getHyperlinkAnchorExpression().getText());
			sbuffer.append("]]></hyperlinkAnchorExpression>\n");
		}

		if (textField.getHyperlinkPageExpression() != null)
		{
			sbuffer.append("\t\t\t\t<hyperlinkPageExpression><![CDATA[");
			sbuffer.append(textField.getHyperlinkPageExpression().getText());
			sbuffer.append("]]></hyperlinkPageExpression>\n");
		}
		
		sbuffer.append("\t\t\t</textField>\n");
	}


	/**
	 *
	 */
	private void writeSubreport(JRSubreport subreport)
	{
		sbuffer.append("\t\t\t<subreport");

		if (!subreport.isUsingCache())
		{
			sbuffer.append(" isUsingCache=\"");
			sbuffer.append(subreport.isUsingCache());
			sbuffer.append("\"");
		}

		sbuffer.append(">\n");

		writeReportElement(subreport);

		if (subreport.getParametersMapExpression() != null)
		{
			sbuffer.append("\t\t\t\t<parametersMapExpression><![CDATA[");
			sbuffer.append(subreport.getParametersMapExpression().getText());
			sbuffer.append("]]></parametersMapExpression>\n");
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
			sbuffer.append("\t\t\t\t<connectionExpression><![CDATA[");
			sbuffer.append(subreport.getConnectionExpression().getText());
			sbuffer.append("]]></connectionExpression>\n");
		}

		if (subreport.getDataSourceExpression() != null)
		{
			sbuffer.append("\t\t\t\t<dataSourceExpression><![CDATA[");
			sbuffer.append(subreport.getDataSourceExpression().getText());
			sbuffer.append("]]></dataSourceExpression>\n");
		}

		if (subreport.getExpression() != null)
		{
			sbuffer.append("\t\t\t\t<subreportExpression");
	
			sbuffer.append(" class=\"");
			sbuffer.append(subreport.getExpression().getValueClassName());
			sbuffer.append("\"");
	
			sbuffer.append("><![CDATA[");
	
			sbuffer.append(subreport.getExpression().getText());
			sbuffer.append("]]></subreportExpression>\n");
		}

		sbuffer.append("\t\t\t</subreport>\n");
	}


	/**
	 *
	 */
	private void writeSubreportParameter(JRSubreportParameter subreportParameter)
	{
		sbuffer.append("\t\t\t\t<subreportParameter");

		sbuffer.append(" name=\"");
		sbuffer.append(subreportParameter.getName());
		sbuffer.append("\"");

		sbuffer.append(">\n");

		if (subreportParameter.getExpression() != null)
		{
			sbuffer.append("\t\t\t\t\t<subreportParameterExpression><![CDATA[");
			sbuffer.append(subreportParameter.getExpression().getText());
			sbuffer.append("]]></subreportParameterExpression>\n");
		}

		sbuffer.append("\t\t\t\t</subreportParameter>\n");
	}
	

}
