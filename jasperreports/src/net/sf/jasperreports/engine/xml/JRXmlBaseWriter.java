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
package net.sf.jasperreports.engine.xml;

import java.io.IOException;

import net.sf.jasperreports.engine.JRConditionalStyle;
import net.sf.jasperreports.engine.JRLineBox;
import net.sf.jasperreports.engine.JRPen;
import net.sf.jasperreports.engine.JRStyle;
import net.sf.jasperreports.engine.JRStyleContainer;
import net.sf.jasperreports.engine.util.JRXmlWriteHelper;


/**
 * Base XML writer.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public abstract class JRXmlBaseWriter
{
	
	protected JRXmlWriteHelper writer;
	
	/**
	 * Sets the XML write helper.
	 * 
	 * @param aWriter the XML write helper
	 */
	protected void useWriter(JRXmlWriteHelper aWriter)
	{
		this.writer = aWriter;
	}
	
	/**
	 * Writes a style.
	 * 
	 * @param style the style to write.
	 * @throws IOException
	 */
	protected void writeStyle(JRStyle style) throws IOException
	{
		writer.startElement(XmlConstants.ELEMENT_style);
		writer.addEncodedAttribute(XmlConstants.ATTRIBUTE_name, style.getName());
		writer.addAttribute(XmlConstants.ATTRIBUTE_isDefault, style.isDefault());
		writeStyleReferenceAttr(style);
		writer.addAttribute(XmlConstants.ATTRIBUTE_mode, style.getOwnModeValue());
		writer.addAttribute(XmlConstants.ATTRIBUTE_forecolor, style.getOwnForecolor());
		writer.addAttribute(XmlConstants.ATTRIBUTE_backcolor, style.getOwnBackcolor());
		writer.addAttribute(XmlConstants.ATTRIBUTE_fill, style.getOwnFillValue());
		writer.addAttribute(XmlConstants.ATTRIBUTE_radius, style.getOwnRadius());
		writer.addAttribute(XmlConstants.ATTRIBUTE_scaleImage, style.getOwnScaleImageValue());
		writer.addAttribute(XmlConstants.ATTRIBUTE_hAlign, style.getOwnHorizontalAlignmentValue());
		writer.addAttribute(XmlConstants.ATTRIBUTE_vAlign, style.getOwnVerticalAlignmentValue());
		writer.addAttribute(XmlConstants.ATTRIBUTE_rotation, style.getOwnRotationValue());
		writer.addAttribute(XmlConstants.ATTRIBUTE_lineSpacing, style.getOwnLineSpacingValue());
		writer.addAttribute(XmlConstants.ATTRIBUTE_markup, style.getOwnMarkup());
		writer.addEncodedAttribute(XmlConstants.ATTRIBUTE_pattern, style.getOwnPattern());
		writer.addAttribute(XmlConstants.ATTRIBUTE_isBlankWhenNull, style.isOwnBlankWhenNull());
		
		writer.addEncodedAttribute(XmlConstants.ATTRIBUTE_fontName, style.getOwnFontName());
		writer.addAttribute(XmlConstants.ATTRIBUTE_fontSize, style.getOwnFontSize());
		writer.addAttribute(XmlConstants.ATTRIBUTE_isBold, style.isOwnBold());
		writer.addAttribute(XmlConstants.ATTRIBUTE_isItalic, style.isOwnItalic());
		writer.addAttribute(XmlConstants.ATTRIBUTE_isUnderline, style.isOwnUnderline());
		writer.addAttribute(XmlConstants.ATTRIBUTE_isStrikeThrough, style.isOwnStrikeThrough());
		writer.addEncodedAttribute(XmlConstants.ATTRIBUTE_pdfFontName, style.getOwnPdfFontName());
		writer.addEncodedAttribute(XmlConstants.ATTRIBUTE_pdfEncoding, style.getOwnPdfEncoding());
		writer.addAttribute(XmlConstants.ATTRIBUTE_isPdfEmbedded, style.isOwnPdfEmbedded());

		writePen(style.getLinePen());
		writeBox(style.getLineBox());
		
		if (toWriteConditionalStyles())
		{
			JRConditionalStyle[] conditionalStyles = style.getConditionalStyles();
			if (!(style instanceof JRConditionalStyle) && conditionalStyles != null)
			{
				for (int i = 0; i < conditionalStyles.length; i++)
				{
					writeConditionalStyle(conditionalStyles[i]);
				}
			}
		}

		writer.closeElement();
	}

	protected void writeStyleReferenceAttr(JRStyleContainer styleContainer)
	{
		if (styleContainer.getStyle() != null)
		{
			writer.addEncodedAttribute(XmlConstants.ATTRIBUTE_style, styleContainer.getStyle().getName());
		}
		else if (styleContainer.getStyleNameReference() != null)
		{
			writer.addEncodedAttribute(XmlConstants.ATTRIBUTE_style, styleContainer.getStyleNameReference());
		}
	}

	/**
	 * Decides whether conditional styles are to be written.
	 * 
	 * @return whether conditional styles are to be written
	 */
	protected abstract boolean toWriteConditionalStyles();

	/**
	 * Writes a conditional style.
	 * 
	 * @param style the conditional style
	 * @throws IOException
	 */
	protected void writeConditionalStyle(JRConditionalStyle style) throws IOException
	{
		writer.startElement(XmlConstants.ELEMENT_conditionalStyle);
		writer.writeExpression(XmlConstants.ELEMENT_conditionExpression, style.getConditionExpression(), false);
		writeStyle(style);
		writer.closeElement();
	}

	/**
	 *
	 */
	protected void writePen(JRPen pen) throws IOException
	{
		writePen(XmlConstants.ELEMENT_pen, pen);
	}

	/**
	 *
	 */
	private void writePen(String element, JRPen pen) throws IOException
	{
		writer.startElement(element);
		writer.addAttribute(XmlConstants.ATTRIBUTE_lineWidth, pen.getOwnLineWidth());
		writer.addAttribute(XmlConstants.ATTRIBUTE_lineStyle, pen.getOwnLineStyleValue());
		writer.addAttribute(XmlConstants.ATTRIBUTE_lineColor, pen.getOwnLineColor());
		writer.closeElement(true);
	}

	/**
	 *
	 */
	protected void writeBox(JRLineBox box) throws IOException
	{
		if (box != null)
		{
			writer.startElement(XmlConstants.ELEMENT_box);
			
			writer.addAttribute(XmlConstants.ATTRIBUTE_padding, box.getOwnPadding());
			writer.addAttribute(XmlConstants.ATTRIBUTE_topPadding, box.getOwnTopPadding());
			writer.addAttribute(XmlConstants.ATTRIBUTE_leftPadding, box.getOwnLeftPadding());
			writer.addAttribute(XmlConstants.ATTRIBUTE_bottomPadding, box.getOwnBottomPadding());
			writer.addAttribute(XmlConstants.ATTRIBUTE_rightPadding, box.getOwnRightPadding());

			writePen(XmlConstants.ELEMENT_pen, box.getPen());
			writePen(XmlConstants.ELEMENT_topPen, box.getTopPen());
			writePen(XmlConstants.ELEMENT_leftPen, box.getLeftPen());
			writePen(XmlConstants.ELEMENT_bottomPen, box.getBottomPen());
			writePen(XmlConstants.ELEMENT_rightPen, box.getRightPen());

			writer.closeElement(true);
		}
	}

}
