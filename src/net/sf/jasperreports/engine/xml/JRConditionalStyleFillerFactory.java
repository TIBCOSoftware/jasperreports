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

import org.xml.sax.Attributes;
import net.sf.jasperreports.engine.design.JRDesignConditionalStyle;

/**
 * @author Ionut Nedelcu (ionutned@users.sourceforge.net)
 * @version $Id$
 */
public class JRConditionalStyleFillerFactory extends JRBaseFactory
{


	/**
	 *
	 */
	public Object createObject(Attributes atts)
	{
		JRDesignConditionalStyle style = (JRDesignConditionalStyle) digester.peek();


		// get JRElement attributes
		Byte mode = (Byte)JRXmlConstants.getModeMap().get(atts.getValue(JRXmlConstants.ATTRIBUTE_mode));
		if (mode != null)
		{
			style.setMode(mode);
		}

		String forecolor = atts.getValue(JRXmlConstants.ATTRIBUTE_forecolor);
		style.setForecolor(JRXmlConstants.getColor(forecolor, null));

		String backcolor = atts.getValue(JRXmlConstants.ATTRIBUTE_backcolor);
		style.setBackcolor(JRXmlConstants.getColor(backcolor, null));



		// get graphic element attributes
		Byte pen = (Byte)JRXmlConstants.getPenMap().get(atts.getValue(JRXmlConstants.ATTRIBUTE_pen));
		style.setPen(pen);

		Byte fill = (Byte)JRXmlConstants.getFillMap().get(atts.getValue(JRXmlConstants.ATTRIBUTE_fill));
		style.setFill(fill);



		// get rectangle attributes
		String radius = atts.getValue(JRXmlConstants.ATTRIBUTE_radius);
		if (radius != null && radius.length() > 0)
		{
			style.setRadius(Integer.parseInt(radius));
		}



		// get image attributes
		Byte scaleImage = (Byte)JRXmlConstants.getScaleImageMap().get(atts.getValue(JRXmlConstants.ATTRIBUTE_scaleImage));
		if (scaleImage != null)
		{
			style.setScaleImage(scaleImage);
		}

		Byte horizontalAlignment = (Byte)JRXmlConstants.getHorizontalAlignMap().get(atts.getValue(JRXmlConstants.ATTRIBUTE_hAlign));
		if (horizontalAlignment != null)
		{
			style.setHorizontalAlignment(horizontalAlignment);
		}

		Byte verticalAlignment = (Byte)JRXmlConstants.getVerticalAlignMap().get(atts.getValue(JRXmlConstants.ATTRIBUTE_vAlign));
		if (verticalAlignment != null)
		{
			style.setVerticalAlignment(verticalAlignment);
		}


		// get box attributes
		Byte border = (Byte)JRXmlConstants.getPenMap().get(atts.getValue(JRXmlConstants.ATTRIBUTE_border));
		if (border != null)
		{
			style.setBorder(border);
		}

		Color borderColor = JRXmlConstants.getColor(atts.getValue(JRXmlConstants.ATTRIBUTE_borderColor), null);
		if (borderColor != null)
		{
			style.setBorderColor(borderColor);
		}

		String padding = atts.getValue(JRXmlConstants.ATTRIBUTE_padding);
		if (padding != null && padding.length() > 0)
		{
			style.setPadding(Integer.parseInt(padding));
		}

		border = (Byte)JRXmlConstants.getPenMap().get(atts.getValue(JRXmlConstants.ATTRIBUTE_topBorder));
		if (border != null)
		{
			style.setTopBorder(border);
		}

		borderColor = JRXmlConstants.getColor(atts.getValue(JRXmlConstants.ATTRIBUTE_topBorderColor), Color.black);
		if (borderColor != null)
		{
			style.setTopBorderColor(borderColor);
		}

		padding = atts.getValue(JRXmlConstants.ATTRIBUTE_topPadding);
		if (padding != null && padding.length() > 0)
		{
			style.setTopPadding(Integer.parseInt(padding));
		}

		border = (Byte)JRXmlConstants.getPenMap().get(atts.getValue(JRXmlConstants.ATTRIBUTE_leftBorder));
		if (border != null)
		{
			style.setLeftBorder(border);
		}

		borderColor = JRXmlConstants.getColor(atts.getValue(JRXmlConstants.ATTRIBUTE_leftBorderColor), Color.black);
		if (borderColor != null)
		{
			style.setLeftBorderColor(borderColor);
		}

		padding = atts.getValue(JRXmlConstants.ATTRIBUTE_leftPadding);
		if (padding != null && padding.length() > 0)
		{
			style.setLeftPadding(Integer.parseInt(padding));
		}

		border = (Byte)JRXmlConstants.getPenMap().get(atts.getValue(JRXmlConstants.ATTRIBUTE_bottomBorder));
		if (border != null)
		{
			style.setBottomBorder(border);
		}

		borderColor = JRXmlConstants.getColor(atts.getValue(JRXmlConstants.ATTRIBUTE_bottomBorderColor), Color.black);
		if (borderColor != null)
		{
			style.setBottomBorderColor(borderColor);
		}

		padding = atts.getValue(JRXmlConstants.ATTRIBUTE_bottomPadding);
		if (padding != null && padding.length() > 0)
		{
			style.setBottomPadding(Integer.parseInt(padding));
		}

		border = (Byte)JRXmlConstants.getPenMap().get(atts.getValue(JRXmlConstants.ATTRIBUTE_rightBorder));
		if (border != null)
		{
			style.setRightBorder(border);
		}

		borderColor = JRXmlConstants.getColor(atts.getValue(JRXmlConstants.ATTRIBUTE_rightBorderColor), Color.black);
		if (borderColor != null)
		{
			style.setRightBorderColor(borderColor);
		}

		padding = atts.getValue(JRXmlConstants.ATTRIBUTE_rightPadding);
		if (padding != null && padding.length() > 0)
		{
			style.setRightPadding(Integer.parseInt(padding));
		}



		Byte rotation = (Byte)JRXmlConstants.getRotationMap().get(atts.getValue(JRXmlConstants.ATTRIBUTE_rotation));
		if (rotation != null)
		{
			style.setRotation(rotation);
		}

		Byte lineSpacing = (Byte)JRXmlConstants.getLineSpacingMap().get(atts.getValue(JRXmlConstants.ATTRIBUTE_lineSpacing));
		if (lineSpacing != null)
		{
			style.setLineSpacing(lineSpacing);
		}

		String isStyledText = atts.getValue(JRXmlConstants.ATTRIBUTE_isStyledText);
		if (isStyledText != null && isStyledText.length() > 0)
		{
			style.setStyledText(Boolean.valueOf(isStyledText));
		}

		style.setPattern(atts.getValue(JRXmlConstants.ATTRIBUTE_pattern));

		String isBlankWhenNull = atts.getValue(JRXmlConstants.ATTRIBUTE_isBlankWhenNull);
		if (isBlankWhenNull != null && isBlankWhenNull.length() > 0)
		{
			style.setBlankWhenNull(Boolean.valueOf(isBlankWhenNull));
		}

		if (atts.getValue(JRXmlConstants.ATTRIBUTE_fontName) != null)
			style.setFontName(atts.getValue(JRXmlConstants.ATTRIBUTE_fontName));

		if (atts.getValue(JRXmlConstants.ATTRIBUTE_isBold) != null)
			style.setBold(Boolean.valueOf(atts.getValue(JRXmlConstants.ATTRIBUTE_isBold)));

		if (atts.getValue(JRXmlConstants.ATTRIBUTE_isItalic) != null)
			style.setItalic(Boolean.valueOf(atts.getValue(JRXmlConstants.ATTRIBUTE_isItalic)));

		if (atts.getValue(JRXmlConstants.ATTRIBUTE_isUnderline) != null)
			style.setUnderline(Boolean.valueOf(atts.getValue(JRXmlConstants.ATTRIBUTE_isUnderline)));

		if (atts.getValue(JRXmlConstants.ATTRIBUTE_isStrikeThrough) != null)
			style.setStrikeThrough(Boolean.valueOf(atts.getValue(JRXmlConstants.ATTRIBUTE_isStrikeThrough)));

		if (atts.getValue(JRXmlConstants.ATTRIBUTE_fontSize) != null)
			style.setFontSize(Integer.valueOf(atts.getValue(JRXmlConstants.ATTRIBUTE_fontSize)));

		if (atts.getValue(JRXmlConstants.ATTRIBUTE_pdfFontName) != null)
			style.setPdfFontName(atts.getValue(JRXmlConstants.ATTRIBUTE_pdfFontName));

		if (atts.getValue(JRXmlConstants.ATTRIBUTE_pdfEncoding) != null)
			style.setPdfEncoding(atts.getValue(JRXmlConstants.ATTRIBUTE_pdfEncoding));

		if (atts.getValue(JRXmlConstants.ATTRIBUTE_isPdfEmbedded) != null)
			style.setPdfEmbedded(Boolean.valueOf(atts.getValue(JRXmlConstants.ATTRIBUTE_isPdfEmbedded)));

		return style;
	}
}
