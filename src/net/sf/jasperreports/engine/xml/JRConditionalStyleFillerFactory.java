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

import java.awt.Color;

import net.sf.jasperreports.engine.design.JRDesignConditionalStyle;
import net.sf.jasperreports.engine.type.HorizontalAlignEnum;
import net.sf.jasperreports.engine.type.LineSpacingEnum;
import net.sf.jasperreports.engine.type.ModeEnum;
import net.sf.jasperreports.engine.type.PenEnum;
import net.sf.jasperreports.engine.type.RotationEnum;
import net.sf.jasperreports.engine.type.VerticalAlignEnum;
import net.sf.jasperreports.engine.util.JRColorUtil;
import net.sf.jasperreports.engine.util.JRPenUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.Attributes;

/**
 * @author Ionut Nedelcu (ionutned@users.sourceforge.net)
 * @version $Id$
 */
public class JRConditionalStyleFillerFactory extends JRBaseFactory
{
	private static final Log log = LogFactory.getLog(JRConditionalStyleFillerFactory.class);


	/**
	 *
	 */
	public Object createObject(Attributes atts)
	{
		JRDesignConditionalStyle style = (JRDesignConditionalStyle) digester.peek();


		// get JRElement attributes
		ModeEnum mode = ModeEnum.getByName(atts.getValue(XmlConstants.ATTRIBUTE_mode));
		if (mode != null)
		{
			style.setMode(mode);
		}

		String forecolor = atts.getValue(XmlConstants.ATTRIBUTE_forecolor);
		style.setForecolor(JRColorUtil.getColor(forecolor, null));

		String backcolor = atts.getValue(XmlConstants.ATTRIBUTE_backcolor);
		style.setBackcolor(JRColorUtil.getColor(backcolor, null));



		// get graphic element attributes
		PenEnum pen = PenEnum.getByName(atts.getValue(XmlConstants.ATTRIBUTE_pen));
		if (pen != null)
		{
			if (log.isWarnEnabled())
				log.warn("The 'pen' attribute is deprecated. Use the <pen> tag instead.");
				
			JRPenUtil.setLinePenFromPen(pen, style.getLinePen());
		}

		Byte fill = (Byte)JRXmlConstants.getFillMap().get(atts.getValue(XmlConstants.ATTRIBUTE_fill));
		style.setFill(fill);



		// get rectangle attributes
		String radius = atts.getValue(XmlConstants.ATTRIBUTE_radius);
		if (radius != null && radius.length() > 0)
		{
			style.setRadius(Integer.parseInt(radius));
		}



		// get image attributes
		Byte scaleImage = (Byte)JRXmlConstants.getScaleImageMap().get(atts.getValue(XmlConstants.ATTRIBUTE_scaleImage));
		if (scaleImage != null)
		{
			style.setScaleImage(scaleImage);
		}

		HorizontalAlignEnum horizontalAlignment = HorizontalAlignEnum.getByName(atts.getValue(XmlConstants.ATTRIBUTE_hAlign));
		if (horizontalAlignment != null)
		{
			style.setHorizontalAlignment(horizontalAlignment);
		}

		VerticalAlignEnum verticalAlignment = VerticalAlignEnum.getByName(atts.getValue(XmlConstants.ATTRIBUTE_vAlign));
		if (verticalAlignment != null)
		{
			style.setVerticalAlignment(verticalAlignment);
		}


		// get box attributes
		PenEnum border = PenEnum.getByName(atts.getValue(XmlConstants.ATTRIBUTE_border));
		if (border != null)
		{
			if (log.isWarnEnabled())
				log.warn("The 'border' attribute is deprecated. Use the <pen> tag instead.");
				
			JRPenUtil.setLinePenFromPen(border, style.getLineBox().getPen());
		}

		Color borderColor = JRColorUtil.getColor(atts.getValue(XmlConstants.ATTRIBUTE_borderColor), null);
		if (borderColor != null)
		{
			if (log.isWarnEnabled())
				log.warn("The 'borderColor' attribute is deprecated. Use the <pen> tag instead.");
				
			style.getLineBox().getPen().setLineColor(borderColor);
		}

		String padding = atts.getValue(XmlConstants.ATTRIBUTE_padding);
		if (padding != null && padding.length() > 0)
		{
			if (log.isWarnEnabled())
				log.warn("The 'padding' attribute is deprecated. Use the <box> tag instead.");
				
			style.getLineBox().setPadding(Integer.parseInt(padding));
		}

		border = PenEnum.getByName(atts.getValue(XmlConstants.ATTRIBUTE_topBorder));
		if (border != null)
		{
			if (log.isWarnEnabled())
				log.warn("The 'topBorder' attribute is deprecated. Use the <pen> tag instead.");

			JRPenUtil.setLinePenFromPen(border, style.getLineBox().getTopPen());
		}
				
		borderColor = JRColorUtil.getColor(atts.getValue(XmlConstants.ATTRIBUTE_topBorderColor), Color.black);
		if (borderColor != null)
		{
			if (log.isWarnEnabled())
				log.warn("The 'topBorderColor' attribute is deprecated. Use the <pen> tag instead.");
				
			style.getLineBox().getTopPen().setLineColor(borderColor);
		}

		padding = atts.getValue(XmlConstants.ATTRIBUTE_topPadding);
		if (padding != null && padding.length() > 0)
		{
			if (log.isWarnEnabled())
				log.warn("The 'topPadding' attribute is deprecated. Use the <box> tag instead.");
				
			style.getLineBox().setTopPadding(Integer.parseInt(padding));
		}

		border = PenEnum.getByName(atts.getValue(XmlConstants.ATTRIBUTE_leftBorder));
		if (border != null)
		{
			if (log.isWarnEnabled())
				log.warn("The 'leftBorder' attribute is deprecated. Use the <pen> tag instead.");

			JRPenUtil.setLinePenFromPen(border, style.getLineBox().getLeftPen());
		}

		borderColor = JRColorUtil.getColor(atts.getValue(XmlConstants.ATTRIBUTE_leftBorderColor), Color.black);
		if (borderColor != null)
		{
			if (log.isWarnEnabled())
				log.warn("The 'leftBorderColor' attribute is deprecated. Use the <pen> tag instead.");
				
			style.getLineBox().getLeftPen().setLineColor(borderColor);
		}

		padding = atts.getValue(XmlConstants.ATTRIBUTE_leftPadding);
		if (padding != null && padding.length() > 0)
		{
			if (log.isWarnEnabled())
				log.warn("The 'leftPadding' attribute is deprecated. Use the <box> tag instead.");
				
			style.getLineBox().setLeftPadding(Integer.parseInt(padding));
		}

		border = PenEnum.getByName(atts.getValue(XmlConstants.ATTRIBUTE_bottomBorder));
		if (border != null)
		{
			if (log.isWarnEnabled())
				log.warn("The 'bottomBorder' attribute is deprecated. Use the <pen> tag instead.");

			JRPenUtil.setLinePenFromPen(border, style.getLineBox().getBottomPen());
		}

		borderColor = JRColorUtil.getColor(atts.getValue(XmlConstants.ATTRIBUTE_bottomBorderColor), Color.black);
		if (borderColor != null)
		{
			if (log.isWarnEnabled())
				log.warn("The 'bottomBorderColor' attribute is deprecated. Use the <pen> tag instead.");
				
			style.getLineBox().getBottomPen().setLineColor(borderColor);
		}

		padding = atts.getValue(XmlConstants.ATTRIBUTE_bottomPadding);
		if (padding != null && padding.length() > 0)
		{
			if (log.isWarnEnabled())
				log.warn("The 'bottomPadding' attribute is deprecated. Use the <box> tag instead.");
				
			style.getLineBox().setBottomPadding(Integer.parseInt(padding));
		}

		border = PenEnum.getByName(atts.getValue(XmlConstants.ATTRIBUTE_rightBorder));
		if (border != null)
		{
			if (log.isWarnEnabled())
				log.warn("The 'rightBorder' attribute is deprecated. Use the <pen> tag instead.");

			JRPenUtil.setLinePenFromPen(border, style.getLineBox().getRightPen());
		}

		borderColor = JRColorUtil.getColor(atts.getValue(XmlConstants.ATTRIBUTE_rightBorderColor), Color.black);
		if (borderColor != null)
		{
			if (log.isWarnEnabled())
				log.warn("The 'rightBorderColor' attribute is deprecated. Use the <pen> tag instead.");
				
			style.getLineBox().getRightPen().setLineColor(borderColor);
		}

		padding = atts.getValue(XmlConstants.ATTRIBUTE_rightPadding);
		if (padding != null && padding.length() > 0)
		{
			if (log.isWarnEnabled())
				log.warn("The 'rightPadding' attribute is deprecated. Use the <box> tag instead.");
				
			style.getLineBox().setRightPadding(Integer.parseInt(padding));
		}



		RotationEnum rotation = RotationEnum.getByName(atts.getValue(XmlConstants.ATTRIBUTE_rotation));
		if (rotation != null)
		{
			style.setRotation(rotation);
		}

		LineSpacingEnum lineSpacing = LineSpacingEnum.getByName(atts.getValue(XmlConstants.ATTRIBUTE_lineSpacing));
		if (lineSpacing != null)
		{
			style.setLineSpacing(lineSpacing);
		}

		style.setMarkup(atts.getValue(XmlConstants.ATTRIBUTE_markup));

		String isStyledText = atts.getValue(XmlConstants.ATTRIBUTE_isStyledText);
		if (isStyledText != null && isStyledText.length() > 0)
		{
			if (log.isWarnEnabled())
				log.warn("The 'isStyledText' attribute is deprecated. Use the 'markup' attribute instead.");
				
			//OK to use deprecated method here, until it is removed
			style.setStyledText(Boolean.valueOf(isStyledText));
		}

		style.setPattern(atts.getValue(XmlConstants.ATTRIBUTE_pattern));

		String isBlankWhenNull = atts.getValue(XmlConstants.ATTRIBUTE_isBlankWhenNull);
		if (isBlankWhenNull != null && isBlankWhenNull.length() > 0)
		{
			style.setBlankWhenNull(Boolean.valueOf(isBlankWhenNull));
		}

		if (atts.getValue(XmlConstants.ATTRIBUTE_fontName) != null)
			style.setFontName(atts.getValue(XmlConstants.ATTRIBUTE_fontName));

		if (atts.getValue(XmlConstants.ATTRIBUTE_isBold) != null)
			style.setBold(Boolean.valueOf(atts.getValue(XmlConstants.ATTRIBUTE_isBold)));

		if (atts.getValue(XmlConstants.ATTRIBUTE_isItalic) != null)
			style.setItalic(Boolean.valueOf(atts.getValue(XmlConstants.ATTRIBUTE_isItalic)));

		if (atts.getValue(XmlConstants.ATTRIBUTE_isUnderline) != null)
			style.setUnderline(Boolean.valueOf(atts.getValue(XmlConstants.ATTRIBUTE_isUnderline)));

		if (atts.getValue(XmlConstants.ATTRIBUTE_isStrikeThrough) != null)
			style.setStrikeThrough(Boolean.valueOf(atts.getValue(XmlConstants.ATTRIBUTE_isStrikeThrough)));

		if (atts.getValue(XmlConstants.ATTRIBUTE_fontSize) != null)
			style.setFontSize(Integer.valueOf(atts.getValue(XmlConstants.ATTRIBUTE_fontSize)));

		if (atts.getValue(XmlConstants.ATTRIBUTE_pdfFontName) != null)
			style.setPdfFontName(atts.getValue(XmlConstants.ATTRIBUTE_pdfFontName));

		if (atts.getValue(XmlConstants.ATTRIBUTE_pdfEncoding) != null)
			style.setPdfEncoding(atts.getValue(XmlConstants.ATTRIBUTE_pdfEncoding));

		if (atts.getValue(XmlConstants.ATTRIBUTE_isPdfEmbedded) != null)
			style.setPdfEmbedded(Boolean.valueOf(atts.getValue(XmlConstants.ATTRIBUTE_isPdfEmbedded)));

		return style;
	}
}
