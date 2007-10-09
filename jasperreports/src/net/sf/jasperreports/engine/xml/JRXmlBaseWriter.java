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
import net.sf.jasperreports.engine.JRStyle;
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

		if (style.getStyle() != null)
		{
			writer.addEncodedAttribute(JRXmlConstants.ATTRIBUTE_style, style.getStyle().getName());
		}
		else if (style.getStyleNameReference() != null)
		{
			writer.addEncodedAttribute(JRXmlConstants.ATTRIBUTE_style, style.getStyleNameReference());
		}
	
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_mode, style.getOwnMode(), JRXmlConstants.getModeMap());
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_forecolor, style.getOwnForecolor());
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_backcolor, style.getOwnBackcolor());
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_pen, style.getOwnPen(), JRXmlConstants.getPenMap());
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_fill, style.getOwnFill(), JRXmlConstants.getFillMap());
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_radius, style.getOwnRadius());
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_scaleImage, style.getOwnScaleImage(), JRXmlConstants.getScaleImageMap());
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_hAlign, style.getOwnHorizontalAlignment(), JRXmlConstants.getHorizontalAlignMap());
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_vAlign, style.getOwnVerticalAlignment(), JRXmlConstants.getVerticalAlignMap());
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_rotation, style.getOwnRotation(), JRXmlConstants.getRotationMap());
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_lineSpacing, style.getOwnLineSpacing(), JRXmlConstants.getLineSpacingMap());
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_isStyledText, style.isOwnStyledText());
		writer.addEncodedAttribute(JRXmlConstants.ATTRIBUTE_pattern, style.getOwnPattern());
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_isBlankWhenNull, style.isOwnBlankWhenNull());
		
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_border, style.getOwnBorder(), JRXmlConstants.getPenMap());
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_borderColor, style.getOwnBorderColor());
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_padding, style.getOwnPadding());
		
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_topBorder, style.getOwnTopBorder(), JRXmlConstants.getPenMap());
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_topBorderColor, style.getOwnTopBorderColor());
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_topPadding, style.getOwnTopPadding());
		
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_leftBorder, style.getOwnLeftBorder(), JRXmlConstants.getPenMap());
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_leftBorderColor, style.getOwnLeftBorderColor());
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_leftPadding, style.getOwnLeftPadding());
		
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_bottomBorder, style.getOwnBottomBorder(), JRXmlConstants.getPenMap());
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_bottomBorderColor, style.getOwnBottomBorderColor());
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_bottomPadding, style.getOwnBottomPadding());
		
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_rightBorder, style.getOwnRightBorder(), JRXmlConstants.getPenMap());
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_rightBorderColor, style.getOwnRightBorderColor());
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_rightPadding, style.getOwnRightPadding());

		writer.addEncodedAttribute(JRXmlConstants.ATTRIBUTE_fontName, style.getOwnFontName());
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_fontSize, style.getOwnFontSize());
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_isBold, style.isOwnBold());
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_isItalic, style.isOwnItalic());
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_isUnderline, style.isOwnUnderline());
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_isStrikeThrough, style.isOwnStrikeThrough());
		writer.addEncodedAttribute(JRXmlConstants.ATTRIBUTE_pdfFontName, style.getOwnPdfFontName());
		writer.addEncodedAttribute(JRXmlConstants.ATTRIBUTE_pdfEncoding, style.getOwnPdfEncoding());
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_isPdfEmbedded, style.isOwnPdfEmbedded());

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

}
