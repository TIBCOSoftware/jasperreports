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
package dori.jasper.engine.xml;

import java.awt.Color;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dori.jasper.engine.JRAlignment;
import dori.jasper.engine.JRBand;
import dori.jasper.engine.JRElement;
import dori.jasper.engine.JRElementGroup;
import dori.jasper.engine.JREllipse;
import dori.jasper.engine.JRException;
import dori.jasper.engine.JRExpression;
import dori.jasper.engine.JRField;
import dori.jasper.engine.JRFont;
import dori.jasper.engine.JRGraphicElement;
import dori.jasper.engine.JRGroup;
import dori.jasper.engine.JRHyperlink;
import dori.jasper.engine.JRImage;
import dori.jasper.engine.JRLine;
import dori.jasper.engine.JRParameter;
import dori.jasper.engine.JRQuery;
import dori.jasper.engine.JRRectangle;
import dori.jasper.engine.JRReport;
import dori.jasper.engine.JRReportFont;
import dori.jasper.engine.JRRuntimeException;
import dori.jasper.engine.JRStaticText;
import dori.jasper.engine.JRSubreport;
import dori.jasper.engine.JRSubreportParameter;
import dori.jasper.engine.JRTextElement;
import dori.jasper.engine.JRTextField;
import dori.jasper.engine.JRVariable;


/**
 *
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
		this.sbuffer = new StringBuffer();
		
		this.sbuffer.append("<?xml version=\"1.0\" encoding=\"" + this.encoding + "\"?>\n");
		this.sbuffer.append("<!DOCTYPE jasperReport PUBLIC \"-//JasperReports//DTD Report Design//EN\" \"http://jasperreports.sourceforge.net/dtds/jasperreport.dtd\">\n");
		this.sbuffer.append("\n");

		this.sbuffer.append("<jasperReport name=\"");
		this.sbuffer.append(report.getName());
		this.sbuffer.append("\"");

		if(report.getColumnCount() != 1)
		{
			this.sbuffer.append(" columnCount=\"");
			this.sbuffer.append(report.getColumnCount());
			this.sbuffer.append("\"");
		}

		this.sbuffer.append(" pageWidth=\"");
		this.sbuffer.append(report.getPageWidth());
		this.sbuffer.append("\"");

		this.sbuffer.append(" pageHeight=\"");
		this.sbuffer.append(report.getPageHeight());
		this.sbuffer.append("\"");
		
		if(report.getOrientation() != JRReport.ORIENTATION_PORTRAIT)
		{
			this.sbuffer.append(" orientation=\"");
			this.sbuffer.append((String)JRXmlConstants.getOrientationMap().get(new Byte(report.getOrientation())));
			this.sbuffer.append("\"");
		}

		if(report.getWhenNoDataType() != JRReport.WHEN_NO_DATA_TYPE_NO_PAGES)
		{
			this.sbuffer.append(" whenNoDataType=\"");
			this.sbuffer.append((String)JRXmlConstants.getWhenNoDataTypeMap().get(new Byte(report.getWhenNoDataType())));
			this.sbuffer.append("\"");
		}

		this.sbuffer.append(" columnWidth=\"");
		this.sbuffer.append(report.getColumnWidth());
		this.sbuffer.append("\"");
		
		if(report.getColumnSpacing() != 0)
		{
			this.sbuffer.append(" columnSpacing=\"");
			this.sbuffer.append(report.getColumnSpacing());
			this.sbuffer.append("\"");
		}

		this.sbuffer.append(" leftMargin=\"");
		this.sbuffer.append(report.getLeftMargin());
		this.sbuffer.append("\"");
		
		this.sbuffer.append(" rightMargin=\"");
		this.sbuffer.append(report.getRightMargin());
		this.sbuffer.append("\"");
		
		this.sbuffer.append(" topMargin=\"");
		this.sbuffer.append(report.getTopMargin());
		this.sbuffer.append("\"");
		
		this.sbuffer.append(" bottomMargin=\"");
		this.sbuffer.append(report.getBottomMargin());
		this.sbuffer.append("\"");
		
		if(report.isTitleNewPage())
		{
			this.sbuffer.append(" isTitleNewPage=\"");
			this.sbuffer.append(report.isTitleNewPage());
			this.sbuffer.append("\"");
		}

		if(report.isSummaryNewPage())
		{
			this.sbuffer.append(" isSummaryNewPage=\"");
			this.sbuffer.append(report.isSummaryNewPage());
			this.sbuffer.append("\"");
		}

		if(report.getScriptletClass() != null)
		{
			this.sbuffer.append(" scriptletClass=\"");
			this.sbuffer.append(report.getScriptletClass());
			this.sbuffer.append("\"");
		}

		this.sbuffer.append(">\n");
		
		/*   */
		JRReportFont[] fonts = report.getFonts();
		if (fonts != null && fonts.length > 0)
		{
			for(int i = 0; i < fonts.length; i++)
			{
				this.fontsMap.put(fonts[i].getName(), fonts[i]);
				this.writeReportFont(fonts[i]);
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
					this.writeParameter(parameters[i]);
				}
			}
		}

		/*   */
		if(report.getQuery() != null)
		{
			this.writeQuery(report.getQuery());
		}

		/*   */
		JRField[] fields = report.getFields();
		if (fields != null && fields.length > 0)
		{
			for(int i = 0; i < fields.length; i++)
			{
				this.writeField(fields[i]);
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
					this.writeVariable(variables[i]);
				}
			}
		}

		/*   */
		JRGroup[] groups = report.getGroups();
		if (groups != null && groups.length > 0)
		{
			for(int i = 0; i < groups.length; i++)
			{
				this.writeGroup(groups[i]);
			}
		}

		
		if (report.getBackground() != null)
		{
			this.sbuffer.append("\t<background>\n");
			this.writeBand(report.getBackground());
			this.sbuffer.append("\t</background>\n");
		}

		if (report.getTitle() != null)
		{
			this.sbuffer.append("\t<title>\n");
			this.writeBand(report.getTitle());
			this.sbuffer.append("\t</title>\n");
		}

		if (report.getPageHeader() != null)
		{
			this.sbuffer.append("\t<pageHeader>\n");
			this.writeBand(report.getPageHeader());
			this.sbuffer.append("\t</pageHeader>\n");
		}

		if (report.getColumnHeader() != null)
		{
			this.sbuffer.append("\t<columnHeader>\n");
			this.writeBand(report.getColumnHeader());
			this.sbuffer.append("\t</columnHeader>\n");
		}

		if (report.getDetail() != null)
		{
			this.sbuffer.append("\t<detail>\n");
			this.writeBand(report.getDetail());
			this.sbuffer.append("\t</detail>\n");
		}

		if (report.getColumnFooter() != null)
		{
			this.sbuffer.append("\t<columnFooter>\n");
			this.writeBand(report.getColumnFooter());
			this.sbuffer.append("\t</columnFooter>\n");
		}

		if (report.getPageFooter() != null)
		{
			this.sbuffer.append("\t<pageFooter>\n");
			this.writeBand(report.getPageFooter());
			this.sbuffer.append("\t</pageFooter>\n");
		}

		if (report.getSummary() != null)
		{
			this.sbuffer.append("\t<summary>\n");
			this.writeBand(report.getSummary());
			this.sbuffer.append("\t</summary>\n");
		}

		this.sbuffer.append("</jasperReport>\n");

		return sbuffer.toString();
	}


	/**
	 *
	 */
	private void writeReportFont(JRReportFont font)
	{
		this.sbuffer.append("\t<reportFont");

		this.sbuffer.append(" name=\"");
		this.sbuffer.append(font.getName());
		this.sbuffer.append("\"");

		this.sbuffer.append(" isDefault=\"");
		this.sbuffer.append(font.isDefault());
		this.sbuffer.append("\"");

		this.sbuffer.append(" fontName=\"");
		this.sbuffer.append(font.getFontName());
		this.sbuffer.append("\"");

		this.sbuffer.append(" size=\"");
		this.sbuffer.append(font.getSize());
		this.sbuffer.append("\"");

		this.sbuffer.append(" isBold=\"");
		this.sbuffer.append(font.isBold());
		this.sbuffer.append("\"");

		this.sbuffer.append(" isItalic=\"");
		this.sbuffer.append(font.isItalic());
		this.sbuffer.append("\"");

		this.sbuffer.append(" isUnderline=\"");
		this.sbuffer.append(font.isUnderline());
		this.sbuffer.append("\"");

		this.sbuffer.append(" isStrikeThrough=\"");
		this.sbuffer.append(font.isStrikeThrough());
		this.sbuffer.append("\"");

		this.sbuffer.append(" pdfFontName=\"");
		this.sbuffer.append(font.getPdfFontName());
		this.sbuffer.append("\"");

		this.sbuffer.append(" pdfEncoding=\"");
		this.sbuffer.append(font.getPdfEncoding());
		this.sbuffer.append("\"");

		this.sbuffer.append(" isPdfEmbedded=\"");
		this.sbuffer.append(font.isPdfEmbedded());
		this.sbuffer.append("\"");

		this.sbuffer.append("/>\n");
	}


	/**
	 *
	 */
	private void writeParameter(JRParameter parameter)
	{
		this.sbuffer.append("\t<parameter");

		this.sbuffer.append(" name=\"");
		this.sbuffer.append(parameter.getName());
		this.sbuffer.append("\"");

		this.sbuffer.append(" class=\"");
		this.sbuffer.append(parameter.getValueClassName());
		this.sbuffer.append("\"");

		if (!parameter.isForPrompting())
		{
			this.sbuffer.append(" isForPrompting=\"");
			this.sbuffer.append(parameter.isForPrompting());
			this.sbuffer.append("\"");
		}

		this.sbuffer.append(">\n");

		if (parameter.getDescription() != null)
		{
			this.sbuffer.append("\t\t<parameterDescription><![CDATA[");
			this.sbuffer.append(parameter.getDescription());
			this.sbuffer.append("]]></parameterDescription>\n");
		}

		if (parameter.getDefaultValueExpression() != null)
		{
			this.sbuffer.append("\t\t<defaultValueExpression><![CDATA[");
			this.sbuffer.append(parameter.getDefaultValueExpression().getText());
			this.sbuffer.append("]]></defaultValueExpression>\n");
		}

		this.sbuffer.append("\t</parameter>\n");
	}


	/**
	 *
	 */
	private void writeQuery(JRQuery query)
	{
		this.sbuffer.append("\t<queryString><![CDATA[");

		this.sbuffer.append(query.getText());

		this.sbuffer.append("]]></queryString>\n");
	}


	/**
	 *
	 */
	private void writeField(JRField field)
	{
		this.sbuffer.append("\t<field");

		this.sbuffer.append(" name=\"");
		this.sbuffer.append(field.getName());
		this.sbuffer.append("\"");

		this.sbuffer.append(" class=\"");
		this.sbuffer.append(field.getValueClassName());
		this.sbuffer.append("\"");

		this.sbuffer.append(">\n");

		if (field.getDescription() != null)
		{
			this.sbuffer.append("\t\t<fieldDescription><![CDATA[");
			this.sbuffer.append(field.getDescription());
			this.sbuffer.append("]]></fieldDescription>\n");
		}

		this.sbuffer.append("\t</field>\n");
	}


	/**
	 *
	 */
	private void writeVariable(JRVariable variable)
	{
		this.sbuffer.append("\t<variable");

		this.sbuffer.append(" name=\"");
		this.sbuffer.append(variable.getName());
		this.sbuffer.append("\"");

		this.sbuffer.append(" class=\"");
		this.sbuffer.append(variable.getValueClassName());
		this.sbuffer.append("\"");

		if (variable.getResetType() != JRVariable.RESET_TYPE_REPORT)
		{
			this.sbuffer.append(" resetType=\"");
			this.sbuffer.append((String)JRXmlConstants.getResetTypeMap().get(new Byte(variable.getResetType())));
			this.sbuffer.append("\"");
		}

		if (variable.getResetGroup() != null)
		{
			this.sbuffer.append(" resetGroup=\"");
			this.sbuffer.append(variable.getResetGroup().getName());
			this.sbuffer.append("\"");
		}

		if (variable.getCalculation() != JRVariable.CALCULATION_NOTHING)
		{
			this.sbuffer.append(" calculation=\"");
			this.sbuffer.append((String)JRXmlConstants.getCalculationMap().get(new Byte(variable.getCalculation())));
			this.sbuffer.append("\"");
		}

		this.sbuffer.append(">\n");

		if (variable.getExpression() != null)
		{
			this.sbuffer.append("\t\t<variableExpression><![CDATA[");
			this.sbuffer.append(variable.getExpression().getText());
			this.sbuffer.append("]]></variableExpression>\n");
		}

		if (variable.getInitialValueExpression() != null)
		{
			this.sbuffer.append("\t\t<initialValueExpression><![CDATA[");
			this.sbuffer.append(variable.getInitialValueExpression().getText());
			this.sbuffer.append("]]></initialValueExpression>\n");
		}

		this.sbuffer.append("\t</variable>\n");
	}


	/**
	 *
	 */
	private void writeGroup(JRGroup group)
	{
		this.sbuffer.append("\t<group");

		this.sbuffer.append(" name=\"");
		this.sbuffer.append(group.getName());
		this.sbuffer.append("\"");

		if (group.isStartNewColumn())
		{
			this.sbuffer.append(" isStartNewColumn=\"");
			this.sbuffer.append(group.isStartNewColumn());
			this.sbuffer.append("\"");
		}

		if (group.isStartNewPage())
		{
			this.sbuffer.append(" isStartNewPage=\"");
			this.sbuffer.append(group.isStartNewPage());
			this.sbuffer.append("\"");
		}

		if (group.isResetPageNumber())
		{
			this.sbuffer.append(" isResetPageNumber=\"");
			this.sbuffer.append(group.isResetPageNumber());
			this.sbuffer.append("\"");
		}

		if (group.isReprintHeaderOnEachPage())
		{
			this.sbuffer.append(" isReprintHeaderOnEachPage=\"");
			this.sbuffer.append(group.isReprintHeaderOnEachPage());
			this.sbuffer.append("\"");
		}

		if (group.getMinHeightToStartNewPage() > 0)
		{
			this.sbuffer.append(" minHeightToStartNewPage=\"");
			this.sbuffer.append(group.getMinHeightToStartNewPage());
			this.sbuffer.append("\"");
		}

		this.sbuffer.append(">\n");

		if (group.getExpression() != null)
		{
			this.sbuffer.append("\t\t<groupExpression><![CDATA[");
			this.sbuffer.append(group.getExpression().getText());
			this.sbuffer.append("]]></groupExpression>\n");
		}

		if (group.getGroupHeader() != null)
		{
			this.sbuffer.append("\t\t<groupHeader>\n");
			this.writeBand(group.getGroupHeader());
			this.sbuffer.append("\t\t</groupHeader>\n");
		}

		if (group.getGroupFooter() != null)
		{
			this.sbuffer.append("\t\t<groupFooter>\n");
			this.writeBand(group.getGroupFooter());
			this.sbuffer.append("\t\t</groupFooter>\n");
		}

		this.sbuffer.append("\t</group>\n");
	}


	/**
	 *
	 */
	private void writeBand(JRBand band)
	{
		this.sbuffer.append("\t\t<band");

		if (band.getHeight() > 0)
		{
			this.sbuffer.append(" height=\"");
			this.sbuffer.append(band.getHeight());
			this.sbuffer.append("\"");
		}

		if (!band.isSplitAllowed())
		{
			this.sbuffer.append(" isSplitAllowed=\"");
			this.sbuffer.append(band.isSplitAllowed());
			this.sbuffer.append("\"");
		}

		this.sbuffer.append(">\n");

		/*   */
		if (band.getPrintWhenExpression() != null)
		{
			this.sbuffer.append("\t\t\t<printWhenExpression><![CDATA[");
			this.sbuffer.append(band.getPrintWhenExpression().getText());
			this.sbuffer.append("]]></printWhenExpression>\n");
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
					this.writeElementGroup((JRElementGroup)child);
				}
				else
				{
					this.writeElement((JRElement)child);
				}
			}
		}

		this.sbuffer.append("\t\t</band>\n");
	}


	/**
	 *
	 */
	private void writeElementGroup(JRElementGroup elementGroup)
	{
		this.sbuffer.append("\t\t\t<elementGroup>\n");

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
					this.writeElementGroup((JRElementGroup)child);
				}
				else
				{
					this.writeElement((JRElement)child);
				}
			}
		}

		this.sbuffer.append("\t\t\t</elementGroup>\n");
	}


	/**
	 *
	 */
	private void writeElement(JRElement element)
	{
		if(element instanceof JRLine)
		{
			this.writeLine((JRLine)element);
		}
		else if (element instanceof JRRectangle)
		{
			this.writeRectangle((JRRectangle)element);
		}
		else if (element instanceof JREllipse)
		{
			this.writeEllipse((JREllipse)element);
		}
		else if (element instanceof JRImage)
		{
			this.writeImage((JRImage)element);
		}
		else if (element instanceof JRStaticText)
		{
			this.writeStaticText((JRStaticText)element);
		}
		else if (element instanceof JRTextField)
		{
			this.writeTextField((JRTextField)element);
		}
		else if (element instanceof JRSubreport)
		{
			this.writeSubreport((JRSubreport)element);
		}
	}


	/**
	 *
	 */
	private void writeLine(JRLine line)
	{
		this.sbuffer.append("\t\t\t<line");

		if (line.getDirection() != JRLine.DIRECTION_TOP_DOWN)
		{
			this.sbuffer.append(" direction=\"");
			this.sbuffer.append((String)JRXmlConstants.getDirectionMap().get(new Byte(line.getDirection())));
			this.sbuffer.append("\"");
		}

		this.sbuffer.append(">\n");

		this.writeReportElement(line);
		this.writeGraphicElement(line);

		this.sbuffer.append("\t\t\t</line>\n");
	}


	/**
	 *
	 */
	private void writeReportElement(JRElement element)
	{
		this.sbuffer.append("\t\t\t\t<reportElement");

		if (element.getKey() != null)
		{
			this.sbuffer.append(" key=\"");
			this.sbuffer.append(element.getKey());
			this.sbuffer.append("\"");
		}

		if (element.getPositionType() != JRElement.POSITION_TYPE_FIX_RELATIVE_TO_TOP)
		{
			this.sbuffer.append(" positionType=\"");
			this.sbuffer.append((String)JRXmlConstants.getPositionTypeMap().get(new Byte(element.getPositionType())));
			this.sbuffer.append("\"");
		}

		if (!element.isPrintRepeatedValues())
		{
			this.sbuffer.append(" isPrintRepeatedValues=\"");
			this.sbuffer.append(element.isPrintRepeatedValues());
			this.sbuffer.append("\"");
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
			this.sbuffer.append(" mode=\"");
			this.sbuffer.append((String)JRXmlConstants.getModeMap().get(new Byte(element.getMode())));
			this.sbuffer.append("\"");
		}

		this.sbuffer.append(" x=\"");
		this.sbuffer.append(element.getX());
		this.sbuffer.append("\"");

		this.sbuffer.append(" y=\"");
		this.sbuffer.append(element.getY());
		this.sbuffer.append("\"");

		this.sbuffer.append(" width=\"");
		this.sbuffer.append(element.getWidth());
		this.sbuffer.append("\"");

		this.sbuffer.append(" height=\"");
		this.sbuffer.append(element.getHeight());
		this.sbuffer.append("\"");

		if (element.isRemoveLineWhenBlank())
		{
			this.sbuffer.append(" isRemoveLineWhenBlank=\"");
			this.sbuffer.append(element.isRemoveLineWhenBlank());
			this.sbuffer.append("\"");
		}

		if (element.isPrintInFirstWholeBand())
		{
			this.sbuffer.append(" isPrintInFirstWholeBand=\"");
			this.sbuffer.append(element.isPrintInFirstWholeBand());
			this.sbuffer.append("\"");
		}

		if (element.isPrintWhenDetailOverflows())
		{
			this.sbuffer.append(" isPrintWhenDetailOverflows=\"");
			this.sbuffer.append(element.isPrintWhenDetailOverflows());
			this.sbuffer.append("\"");
		}

		if (element.getPrintWhenGroupChanges() != null)
		{
			this.sbuffer.append(" printWhenGroupChanges=\"");
			this.sbuffer.append(element.getPrintWhenGroupChanges().getName());
			this.sbuffer.append("\"");
		}

		if (element.getForecolor().getRGB() != Color.black.getRGB())
		{
			this.sbuffer.append(" forecolor=\"#");
			this.sbuffer.append(Integer.toHexString(element.getForecolor().getRGB() & colorMask));
			this.sbuffer.append("\"");
		}

		if (element.getBackcolor().getRGB() != Color.white.getRGB())
		{
			this.sbuffer.append(" backcolor=\"#");
			this.sbuffer.append(Integer.toHexString(element.getBackcolor().getRGB() & colorMask));
			this.sbuffer.append("\"");
		}

		if (element.getPrintWhenExpression() != null)
		{
			this.sbuffer.append(">\n");
			
			this.sbuffer.append("\t\t\t\t\t<printWhenExpression><![CDATA[");
			this.sbuffer.append(element.getPrintWhenExpression().getText());
			this.sbuffer.append("]]></printWhenExpression>\n");

			this.sbuffer.append("\t\t\t\t</reportElement>\n");
		}
		else
		{
			this.sbuffer.append("/>\n");
		}
	}


	/**
	 *
	 */
	private void writeGraphicElement(JRGraphicElement element)
	{
		this.sbuffer.append("\t\t\t\t<graphicElement");

		if (element.getStretchType() != JRGraphicElement.STRETCH_TYPE_NO_STRETCH)
		{
			this.sbuffer.append(" stretchType=\"");
			this.sbuffer.append((String)JRXmlConstants.getStretchTypeMap().get(new Byte(element.getStretchType())));
			this.sbuffer.append("\"");
		}

		if (
			(element instanceof JRLine && element.getPen() != JRGraphicElement.PEN_1_POINT) ||
			(element instanceof JRRectangle && element.getPen() != JRGraphicElement.PEN_1_POINT) ||
			(element instanceof JREllipse && element.getPen() != JRGraphicElement.PEN_1_POINT) ||
			(element instanceof JRImage && element.getPen() != JRGraphicElement.PEN_NONE)
			)
		{
			this.sbuffer.append(" pen=\"");
			this.sbuffer.append((String)JRXmlConstants.getPenMap().get(new Byte(element.getPen())));
			this.sbuffer.append("\"");
		}

		if (element.getFill() != JRGraphicElement.FILL_SOLID)
		{
			this.sbuffer.append(" fill=\"");
			this.sbuffer.append((String)JRXmlConstants.getFillMap().get(new Byte(element.getFill())));
			this.sbuffer.append("\"");
		}

		this.sbuffer.append("/>\n");
	}


	/**
	 *
	 */
	private void writeRectangle(JRRectangle rectangle)
	{
		this.sbuffer.append("\t\t\t<rectangle");

		if (rectangle.getRadius() != 0)
		{
			this.sbuffer.append(" radius=\"");
			this.sbuffer.append(rectangle.getRadius());
			this.sbuffer.append("\"");
		}

		this.sbuffer.append(">\n");

		this.writeReportElement(rectangle);
		this.writeGraphicElement(rectangle);

		this.sbuffer.append("\t\t\t</rectangle>\n");
	}


	/**
	 *
	 */
	private void writeEllipse(JREllipse ellipse)
	{
		this.sbuffer.append("\t\t\t<ellipse>\n");

		this.writeReportElement(ellipse);
		this.writeGraphicElement(ellipse);

		this.sbuffer.append("\t\t\t</ellipse>\n");
	}


	/**
	 *
	 */
	private void writeImage(JRImage image)
	{
		this.sbuffer.append("\t\t\t<image");

		if (image.getScaleImage() != JRImage.SCALE_IMAGE_RETAIN_SHAPE)
		{
			this.sbuffer.append(" scaleImage=\"");
			this.sbuffer.append((String)JRXmlConstants.getScaleImageMap().get(new Byte(image.getScaleImage())));
			this.sbuffer.append("\"");
		}

		if (image.getHorizontalAlignment() != JRAlignment.HORIZONTAL_ALIGN_LEFT)
		{
			this.sbuffer.append(" hAlign=\"");
			this.sbuffer.append((String)JRXmlConstants.getHorizontalAlignMap().get(new Byte(image.getHorizontalAlignment())));
			this.sbuffer.append("\"");
		}

		if (image.getVerticalAlignment() != JRAlignment.VERTICAL_ALIGN_TOP)
		{
			this.sbuffer.append(" vAlign=\"");
			this.sbuffer.append((String)JRXmlConstants.getVerticalAlignMap().get(new Byte(image.getVerticalAlignment())));
			this.sbuffer.append("\"");
		}

		if (!image.isUsingCache())
		{
			this.sbuffer.append(" isUsingCache=\"");
			this.sbuffer.append(image.isUsingCache());
			this.sbuffer.append("\"");
		}

		if (image.getEvaluationTime() != JRExpression.EVALUATION_TIME_NOW)
		{
			this.sbuffer.append(" evaluationTime=\"");
			this.sbuffer.append((String)JRXmlConstants.getEvaluationTimeMap().get(new Byte(image.getEvaluationTime())));
			this.sbuffer.append("\"");
		}

		if (image.getEvaluationGroup() != null)
		{
			this.sbuffer.append(" evaluationGroup=\"");
			this.sbuffer.append(image.getEvaluationGroup().getName());
			this.sbuffer.append("\"");
		}

		if (image.getHyperlinkType() != JRHyperlink.HYPERLINK_TYPE_NONE)
		{
			this.sbuffer.append(" hyperlinkType=\"");
			this.sbuffer.append((String)JRXmlConstants.getHyperlinkTypeMap().get(new Byte(image.getHyperlinkType())));
			this.sbuffer.append("\"");
		}

		this.sbuffer.append(">\n");

		this.writeReportElement(image);
		this.writeGraphicElement(image);

		if (image.getExpression() != null)
		{
			this.sbuffer.append("\t\t\t\t<imageExpression");
	
			this.sbuffer.append(" class=\"");
			this.sbuffer.append(image.getExpression().getValueClassName());//FIXME class is mandatory in verifier
			this.sbuffer.append("\"");
	
			this.sbuffer.append("><![CDATA[");
	
			this.sbuffer.append(image.getExpression().getText());
			this.sbuffer.append("]]></imageExpression>\n");
		}

		if (image.getAnchorNameExpression() != null)
		{
			this.sbuffer.append("\t\t\t\t<anchorNameExpression><![CDATA[");
			this.sbuffer.append(image.getAnchorNameExpression().getText());
			this.sbuffer.append("]]></anchorNameExpression>\n");
		}

		if (image.getHyperlinkReferenceExpression() != null)
		{
			this.sbuffer.append("\t\t\t\t<hyperlinkReferenceExpression><![CDATA[");
			this.sbuffer.append(image.getHyperlinkReferenceExpression().getText());
			this.sbuffer.append("]]></hyperlinkReferenceExpression>\n");
		}

		if (image.getHyperlinkAnchorExpression() != null)
		{
			this.sbuffer.append("\t\t\t\t<hyperlinkAnchorExpression><![CDATA[");
			this.sbuffer.append(image.getHyperlinkAnchorExpression().getText());
			this.sbuffer.append("]]></hyperlinkAnchorExpression>\n");
		}

		if (image.getHyperlinkPageExpression() != null)
		{
			this.sbuffer.append("\t\t\t\t<hyperlinkPageExpression><![CDATA[");
			this.sbuffer.append(image.getHyperlinkPageExpression().getText());
			this.sbuffer.append("]]></hyperlinkPageExpression>\n");
		}

		this.sbuffer.append("\t\t\t</image>\n");
	}


	/**
	 *
	 */
	private void writeStaticText(JRStaticText staticText)
	{
		this.sbuffer.append("\t\t\t<staticText>\n");

		this.writeReportElement(staticText);
		this.writeTextElement(staticText);

		if (staticText.getText() != null)
		{
			this.sbuffer.append("\t\t\t\t<text><![CDATA[");
			this.sbuffer.append(staticText.getText());
			this.sbuffer.append("]]></text>\n");
		}

		this.sbuffer.append("\t\t\t</staticText>\n");
	}


	/**
	 *
	 */
	private void writeTextElement(JRTextElement textElement)
	{
		this.sbuffer.append("\t\t\t\t<textElement");

		if (textElement.getTextAlignment() != JRAlignment.HORIZONTAL_ALIGN_LEFT)
		{
			this.sbuffer.append(" textAlignment=\"");
			this.sbuffer.append((String)JRXmlConstants.getHorizontalAlignMap().get(new Byte(textElement.getTextAlignment())));
			this.sbuffer.append("\"");
		}

		if (textElement.getVerticalAlignment() != JRTextElement.VERTICAL_ALIGN_TOP)
		{
			this.sbuffer.append(" verticalAlignment=\"");
			this.sbuffer.append((String)JRXmlConstants.getVerticalAlignMap().get(new Byte(textElement.getVerticalAlignment())));
			this.sbuffer.append("\"");
		}

		if (textElement.getLineSpacing() != JRTextElement.LINE_SPACING_SINGLE)
		{
			this.sbuffer.append(" lineSpacing=\"");
			this.sbuffer.append((String)JRXmlConstants.getLineSpacingMap().get(new Byte(textElement.getLineSpacing())));
			this.sbuffer.append("\"");
		}

		String font = this.writeFont(textElement.getFont());
		if (font != null)
		{
			this.sbuffer.append(">\n");

			this.sbuffer.append("\t\t\t\t\t" + font + "\n");
			
			this.sbuffer.append("\t\t\t\t</textElement>\n");
		}
		else
		{
			this.sbuffer.append("/>\n");
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
					(JRFont)this.fontsMap.get(
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
		this.sbuffer.append("\t\t\t<textField");

		if (textField.isStretchWithOverflow())
		{
			this.sbuffer.append(" isStretchWithOverflow=\"");
			this.sbuffer.append(textField.isStretchWithOverflow());
			this.sbuffer.append("\"");
		}

		if (textField.getEvaluationTime() != JRExpression.EVALUATION_TIME_NOW)
		{
			this.sbuffer.append(" evaluationTime=\"");
			this.sbuffer.append((String)JRXmlConstants.getEvaluationTimeMap().get(new Byte(textField.getEvaluationTime())));
			this.sbuffer.append("\"");
		}

		if (textField.getEvaluationGroup() != null)
		{
			this.sbuffer.append(" evaluationGroup=\"");
			this.sbuffer.append(textField.getEvaluationGroup().getName());
			this.sbuffer.append("\"");
		}

		if (textField.getPattern() != null)
		{
			this.sbuffer.append(" pattern=\"");
			this.sbuffer.append(textField.getPattern());
			this.sbuffer.append("\"");
		}

		if (textField.isBlankWhenNull())
		{
			this.sbuffer.append(" isBlankWhenNull=\"");
			this.sbuffer.append(textField.isBlankWhenNull());
			this.sbuffer.append("\"");
		}

		if (textField.getHyperlinkType() != JRHyperlink.HYPERLINK_TYPE_NONE)
		{
			this.sbuffer.append(" hyperlinkType=\"");
			this.sbuffer.append((String)JRXmlConstants.getHyperlinkTypeMap().get(new Byte(textField.getHyperlinkType())));
			this.sbuffer.append("\"");
		}

		this.sbuffer.append(">\n");

		this.writeReportElement(textField);
		this.writeTextElement(textField);

		if (textField.getExpression() != null)
		{
			this.sbuffer.append("\t\t\t\t<textFieldExpression");
	
			this.sbuffer.append(" class=\"");
			this.sbuffer.append(textField.getExpression().getValueClassName());
			this.sbuffer.append("\"");
	
			this.sbuffer.append("><![CDATA[");
	
			this.sbuffer.append(textField.getExpression().getText());
			this.sbuffer.append("]]></textFieldExpression>\n");
		}

		if (textField.getAnchorNameExpression() != null)
		{
			this.sbuffer.append("\t\t\t\t<anchorNameExpression><![CDATA[");
			this.sbuffer.append(textField.getAnchorNameExpression().getText());
			this.sbuffer.append("]]></anchorNameExpression>\n");
		}

		if (textField.getHyperlinkReferenceExpression() != null)
		{
			this.sbuffer.append("\t\t\t\t<hyperlinkReferenceExpression><![CDATA[");
			this.sbuffer.append(textField.getHyperlinkReferenceExpression().getText());
			this.sbuffer.append("]]></hyperlinkReferenceExpression>\n");
		}

		if (textField.getHyperlinkAnchorExpression() != null)
		{
			this.sbuffer.append("\t\t\t\t<hyperlinkAnchorExpression><![CDATA[");
			this.sbuffer.append(textField.getHyperlinkAnchorExpression().getText());
			this.sbuffer.append("]]></hyperlinkAnchorExpression>\n");
		}

		if (textField.getHyperlinkPageExpression() != null)
		{
			this.sbuffer.append("\t\t\t\t<hyperlinkPageExpression><![CDATA[");
			this.sbuffer.append(textField.getHyperlinkPageExpression().getText());
			this.sbuffer.append("]]></hyperlinkPageExpression>\n");
		}
		
		this.sbuffer.append("\t\t\t</textField>\n");
	}


	/**
	 *
	 */
	private void writeSubreport(JRSubreport subreport)
	{
		this.sbuffer.append("\t\t\t<subreport");

		if (!subreport.isUsingCache())
		{
			this.sbuffer.append(" isUsingCache=\"");
			this.sbuffer.append(subreport.isUsingCache());
			this.sbuffer.append("\"");
		}

		this.sbuffer.append(">\n");

		this.writeReportElement(subreport);

		if (subreport.getParametersMapExpression() != null)
		{
			this.sbuffer.append("\t\t\t\t<parametersMapExpression><![CDATA[");
			this.sbuffer.append(subreport.getParametersMapExpression().getText());
			this.sbuffer.append("]]></parametersMapExpression>\n");
		}

		/*   */
		JRSubreportParameter[] parameters = subreport.getParameters();
		if (parameters != null && parameters.length > 0)
		{
			for(int i = 0; i < parameters.length; i++)
			{
				this.writeSubreportParameter(parameters[i]);
			}
		}

		if (subreport.getConnectionExpression() != null)
		{
			this.sbuffer.append("\t\t\t\t<connectionExpression><![CDATA[");
			this.sbuffer.append(subreport.getConnectionExpression().getText());
			this.sbuffer.append("]]></connectionExpression>\n");
		}

		if (subreport.getDataSourceExpression() != null)
		{
			this.sbuffer.append("\t\t\t\t<dataSourceExpression><![CDATA[");
			this.sbuffer.append(subreport.getDataSourceExpression().getText());
			this.sbuffer.append("]]></dataSourceExpression>\n");
		}

		if (subreport.getExpression() != null)
		{
			this.sbuffer.append("\t\t\t\t<subreportExpression");
	
			this.sbuffer.append(" class=\"");
			this.sbuffer.append(subreport.getExpression().getValueClassName());
			this.sbuffer.append("\"");
	
			this.sbuffer.append("><![CDATA[");
	
			this.sbuffer.append(subreport.getExpression().getText());
			this.sbuffer.append("]]></subreportExpression>\n");
		}

		this.sbuffer.append("\t\t\t</subreport>\n");
	}


	/**
	 *
	 */
	private void writeSubreportParameter(JRSubreportParameter subreportParameter)
	{
		this.sbuffer.append("\t\t\t\t<subreportParameter");

		this.sbuffer.append(" name=\"");
		this.sbuffer.append(subreportParameter.getName());
		this.sbuffer.append("\"");

		this.sbuffer.append(">\n");

		if (subreportParameter.getExpression() != null)
		{
			this.sbuffer.append("\t\t\t\t\t<subreportParameterExpression><![CDATA[");
			this.sbuffer.append(subreportParameter.getExpression().getText());
			this.sbuffer.append("]]></subreportParameterExpression>\n");
		}

		this.sbuffer.append("\t\t\t\t</subreportParameter>\n");
	}
	

}
