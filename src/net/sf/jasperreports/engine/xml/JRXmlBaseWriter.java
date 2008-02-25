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
		writer.startElement(JRXmlConstants.ELEMENT_style);
		writer.addEncodedAttribute(JRXmlConstants.ATTRIBUTE_name, style.getName());
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_isDefault, style.isDefault());
		writeStyleReferenceAttr(style);
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_mode, style.getOwnMode(), JRXmlConstants.getModeMap());
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_forecolor, style.getOwnForecolor());
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_backcolor, style.getOwnBackcolor());
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_fill, style.getOwnFill(), JRXmlConstants.getFillMap());
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_radius, style.getOwnRadius());
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_scaleImage, style.getOwnScaleImage(), JRXmlConstants.getScaleImageMap());
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_hAlign, style.getOwnHorizontalAlignment(), JRXmlConstants.getHorizontalAlignMap());
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_vAlign, style.getOwnVerticalAlignment(), JRXmlConstants.getVerticalAlignMap());
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_rotation, style.getOwnRotation(), JRXmlConstants.getRotationMap());
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_lineSpacing, style.getOwnLineSpacing(), JRXmlConstants.getLineSpacingMap());
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_markup, style.getOwnMarkup());
		writer.addEncodedAttribute(JRXmlConstants.ATTRIBUTE_pattern, style.getOwnPattern());
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_isBlankWhenNull, style.isOwnBlankWhenNull());
		
		writer.addEncodedAttribute(JRXmlConstants.ATTRIBUTE_fontName, style.getOwnFontName());
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_fontSize, style.getOwnFontSize());
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_isBold, style.isOwnBold());
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_isItalic, style.isOwnItalic());
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_isUnderline, style.isOwnUnderline());
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_isStrikeThrough, style.isOwnStrikeThrough());
		writer.addEncodedAttribute(JRXmlConstants.ATTRIBUTE_pdfFontName, style.getOwnPdfFontName());
		writer.addEncodedAttribute(JRXmlConstants.ATTRIBUTE_pdfEncoding, style.getOwnPdfEncoding());
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_isPdfEmbedded, style.isOwnPdfEmbedded());

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
			writer.addEncodedAttribute(JRXmlConstants.ATTRIBUTE_style, styleContainer.getStyle().getName());
		}
		else if (styleContainer.getStyleNameReference() != null)
		{
			writer.addEncodedAttribute(JRXmlConstants.ATTRIBUTE_style, styleContainer.getStyleNameReference());
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
		writer.startElement(JRXmlConstants.ELEMENT_conditionalStyle);
		writer.writeExpression(JRXmlConstants.ELEMENT_conditionExpression, style.getConditionExpression(), false);
		writeStyle(style);
		writer.closeElement();
	}

	/**
	 *
	 */
	protected void writePen(JRPen pen) throws IOException
	{
		writePen(JRXmlConstants.ELEMENT_pen, pen);
	}

	/**
	 *
	 */
	private void writePen(String element, JRPen pen) throws IOException
	{
		writer.startElement(element);
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_lineWidth, pen.getOwnLineWidth());
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_lineStyle, pen.getOwnLineStyle(), JRXmlConstants.getLineStyleMap());
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_lineColor, pen.getOwnLineColor());
		writer.closeElement(true);
	}

	/**
	 *
	 */
	protected void writeBox(JRLineBox box) throws IOException
	{
		if (box != null)
		{
			writer.startElement(JRXmlConstants.ELEMENT_box);
			
			writePen(JRXmlConstants.ELEMENT_pen, box.getPen());
			writePen(JRXmlConstants.ELEMENT_topPen, box.getTopPen());
			writePen(JRXmlConstants.ELEMENT_leftPen, box.getLeftPen());
			writePen(JRXmlConstants.ELEMENT_bottomPen, box.getBottomPen());
			writePen(JRXmlConstants.ELEMENT_rightPen, box.getRightPen());

			writer.closeElement(true);
		}
	}

}
