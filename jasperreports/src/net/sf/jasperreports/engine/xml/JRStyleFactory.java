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
import java.util.Map;

import net.sf.jasperreports.engine.JRStyle;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.design.JRDesignStyle;
import net.sf.jasperreports.engine.design.JasperDesign;

import org.xml.sax.Attributes;

/**
 * @author Ionut Nedelcu (ionutned@users.sourceforge.net)
 * @version $Id$
 */
public class JRStyleFactory extends JRBaseFactory
{
	public static final String ELEMENT_style = "style";
	
	public static final String ATTRIBUTE_name = "name";
	public static final String ATTRIBUTE_isDefault = "isDefault";
	public static final String ATTRIBUTE_mode = "mode";
	public static final String ATTRIBUTE_forecolor = "forecolor";
	public static final String ATTRIBUTE_backcolor = "backcolor";
	public static final String ATTRIBUTE_style = "style";

	public static final String ATTRIBUTE_pen = "pen";
	public static final String ATTRIBUTE_fill = "fill";

	public static final String ATTRIBUTE_radius = "radius";

	public static final String ATTRIBUTE_scaleImage = "scaleImage";
	// these are inherited by both images and texts.
	public static final String ATTRIBUTE_hAlign = "hAlign";
	public static final String ATTRIBUTE_vAlign = "vAlign";

	public static final String ATTRIBUTE_border = "border";
	public static final String ATTRIBUTE_borderColor = "borderColor";
	public static final String ATTRIBUTE_padding = "padding";
	public static final String ATTRIBUTE_topBorder = "topBorder";
	public static final String ATTRIBUTE_topBorderColor = "topBorderColor";
	public static final String ATTRIBUTE_topPadding = "topPadding";
	public static final String ATTRIBUTE_leftBorder = "leftBorder";
	public static final String ATTRIBUTE_leftBorderColor = "leftBorderColor";
	public static final String ATTRIBUTE_leftPadding = "leftPadding";
	public static final String ATTRIBUTE_bottomBorder = "bottomBorder";
	public static final String ATTRIBUTE_bottomBorderColor = "bottomBorderColor";
	public static final String ATTRIBUTE_bottomPadding = "bottomPadding";
	public static final String ATTRIBUTE_rightBorder = "rightBorder";
	public static final String ATTRIBUTE_rightBorderColor = "rightBorderColor";
	public static final String ATTRIBUTE_rightPadding = "rightPadding";

	public static final String ATTRIBUTE_rotation = "rotation";
	public static final String ATTRIBUTE_lineSpacing = "lineSpacing";
	public static final String ATTRIBUTE_isStyledText = "isStyledText";
	public static final String ATTRIBUTE_pattern = "pattern";
	public static final String ATTRIBUTE_isBlankWhenNull = "isBlankWhenNull";

	public static final String ATTRIBUTE_fontName = "fontName";
	public static final String ATTRIBUTE_isBold = "isBold";
	public static final String ATTRIBUTE_isItalic = "isItalic";
	public static final String ATTRIBUTE_isUnderline = "isUnderline";
	public static final String ATTRIBUTE_isStrikeThrough = "isStrikeThrough";
	public static final String ATTRIBUTE_fontSize = "fontSize";
	public static final String ATTRIBUTE_pdfFontName = "pdfFontName";
	public static final String ATTRIBUTE_pdfEncoding = "pdfEncoding";
	public static final String ATTRIBUTE_isPdfEmbedded = "isPdfEmbedded";


	/**
	 *
	 */
	public Object createObject(Attributes atts)
	{
		JRDesignStyle style = new JRDesignStyle();

		// get style name
		style.setName(atts.getValue(ATTRIBUTE_name));

		String isDefault = atts.getValue(ATTRIBUTE_isDefault);
		if (isDefault != null && isDefault.length() > 0)
		{
			style.setDefault(Boolean.valueOf(isDefault).booleanValue());
		}

		// get parent style
		if (atts.getValue(ATTRIBUTE_style) != null)
		{
			JRXmlLoader xmlLoader = null;
			JRPrintXmlLoader printXmlLoader = null;
			Map stylesMap = null;

			Object loader = digester.peek(digester.getCount() - 1);
			if (loader instanceof JRXmlLoader)
			{
				xmlLoader = (JRXmlLoader)loader;
				JasperDesign jasperDesign = (JasperDesign)digester.peek(digester.getCount() - 2);
				stylesMap = jasperDesign.getStylesMap();
			}
			else
			{
				printXmlLoader = (JRPrintXmlLoader)loader;
				JasperPrint jasperPrint = (JasperPrint)digester.peek(digester.getCount() - 2);
				stylesMap = jasperPrint.getStylesMap();
			}

			if ( !stylesMap.containsKey(atts.getValue(ATTRIBUTE_style)) )
			{
				if (printXmlLoader == null)
				{
					xmlLoader.addError(new Exception("Unknown report style : " + atts.getValue(ATTRIBUTE_style)));
				}
				else
				{
					printXmlLoader.addError(new Exception("Unknown report style : " + atts.getValue(ATTRIBUTE_style)));
				}
			}

			style.setParentStyle((JRStyle) stylesMap.get(atts.getValue(ATTRIBUTE_style)));
		}


		// get JRElement attributes
		Byte mode = (Byte)JRXmlConstants.getModeMap().get(atts.getValue(ATTRIBUTE_mode));
		if (mode != null)
		{
			style.setMode(mode);
		}

		String forecolor = atts.getValue(ATTRIBUTE_forecolor);
		style.setForecolor(JRXmlConstants.getColor(forecolor, null));

		String backcolor = atts.getValue(ATTRIBUTE_backcolor);
		style.setBackcolor(JRXmlConstants.getColor(backcolor, null));



		// get graphic element attributes
		Byte pen = (Byte)JRXmlConstants.getPenMap().get(atts.getValue(ATTRIBUTE_pen));
		style.setPen(pen);

		Byte fill = (Byte)JRXmlConstants.getFillMap().get(atts.getValue(ATTRIBUTE_fill));
		style.setFill(fill);



		// get rectangle attributes
		String radius = atts.getValue(ATTRIBUTE_radius);
		if (radius != null && radius.length() > 0)
		{
			style.setRadius(Integer.parseInt(radius));
		}



		// get image attributes
		Byte scaleImage = (Byte)JRXmlConstants.getScaleImageMap().get(atts.getValue(ATTRIBUTE_scaleImage));
		if (scaleImage != null)
		{
			style.setScaleImage(scaleImage);
		}

		Byte horizontalAlignment = (Byte)JRXmlConstants.getHorizontalAlignMap().get(atts.getValue(ATTRIBUTE_hAlign));
		if (horizontalAlignment != null)
		{
			style.setHorizontalAlignment(horizontalAlignment);
		}

		Byte verticalAlignment = (Byte)JRXmlConstants.getVerticalAlignMap().get(atts.getValue(ATTRIBUTE_vAlign));
		if (verticalAlignment != null)
		{
			style.setVerticalAlignment(verticalAlignment);
		}


		// get box attributes
		Byte border = (Byte)JRXmlConstants.getPenMap().get(atts.getValue(ATTRIBUTE_border));
		if (border != null)
		{
			style.setBorder(border);
		}

		Color borderColor = JRXmlConstants.getColor(atts.getValue(ATTRIBUTE_borderColor), null);
		if (borderColor != null)
		{
			style.setBorderColor(borderColor);
		}

		String padding = atts.getValue(ATTRIBUTE_padding);
		if (padding != null && padding.length() > 0)
		{
			style.setPadding(Integer.parseInt(padding));
		}

		border = (Byte)JRXmlConstants.getPenMap().get(atts.getValue(ATTRIBUTE_topBorder));
		if (border != null)
		{
			style.setTopBorder(border);
		}

		borderColor = JRXmlConstants.getColor(atts.getValue(ATTRIBUTE_topBorderColor), Color.black);
		if (borderColor != null)
		{
			style.setTopBorderColor(borderColor);
		}

		padding = atts.getValue(ATTRIBUTE_topPadding);
		if (padding != null && padding.length() > 0)
		{
			style.setTopPadding(Integer.parseInt(padding));
		}

		border = (Byte)JRXmlConstants.getPenMap().get(atts.getValue(ATTRIBUTE_leftBorder));
		if (border != null)
		{
			style.setLeftBorder(border);
		}

		borderColor = JRXmlConstants.getColor(atts.getValue(ATTRIBUTE_leftBorderColor), Color.black);
		if (borderColor != null)
		{
			style.setLeftBorderColor(borderColor);
		}

		padding = atts.getValue(ATTRIBUTE_leftPadding);
		if (padding != null && padding.length() > 0)
		{
			style.setLeftPadding(Integer.parseInt(padding));
		}

		border = (Byte)JRXmlConstants.getPenMap().get(atts.getValue(ATTRIBUTE_bottomBorder));
		if (border != null)
		{
			style.setBottomBorder(border);
		}

		borderColor = JRXmlConstants.getColor(atts.getValue(ATTRIBUTE_bottomBorderColor), Color.black);
		if (borderColor != null)
		{
			style.setBottomBorderColor(borderColor);
		}

		padding = atts.getValue(ATTRIBUTE_bottomPadding);
		if (padding != null && padding.length() > 0)
		{
			style.setBottomPadding(Integer.parseInt(padding));
		}

		border = (Byte)JRXmlConstants.getPenMap().get(atts.getValue(ATTRIBUTE_rightBorder));
		if (border != null)
		{
			style.setRightBorder(border);
		}

		borderColor = JRXmlConstants.getColor(atts.getValue(ATTRIBUTE_rightBorderColor), Color.black);
		if (borderColor != null)
		{
			style.setRightBorderColor(borderColor);
		}

		padding = atts.getValue(ATTRIBUTE_rightPadding);
		if (padding != null && padding.length() > 0)
		{
			style.setRightPadding(Integer.parseInt(padding));
		}



		Byte rotation = (Byte)JRXmlConstants.getRotationMap().get(atts.getValue(ATTRIBUTE_rotation));
		if (rotation != null)
		{
			style.setRotation(rotation);
		}

		Byte lineSpacing = (Byte)JRXmlConstants.getLineSpacingMap().get(atts.getValue(ATTRIBUTE_lineSpacing));
		if (lineSpacing != null)
		{
			style.setLineSpacing(lineSpacing);
		}

		String isStyledText = atts.getValue(ATTRIBUTE_isStyledText);
		if (isStyledText != null && isStyledText.length() > 0)
		{
			style.setStyledText(Boolean.valueOf(isStyledText));
		}

		style.setPattern(atts.getValue(ATTRIBUTE_pattern));

		String isBlankWhenNull = atts.getValue(ATTRIBUTE_isBlankWhenNull);
		if (isBlankWhenNull != null && isBlankWhenNull.length() > 0)
		{
			style.setBlankWhenNull(Boolean.valueOf(isBlankWhenNull));
		}

		if (atts.getValue(ATTRIBUTE_fontName) != null)
			style.setFontName(atts.getValue(ATTRIBUTE_fontName));

		if (atts.getValue(ATTRIBUTE_isBold) != null)
			style.setBold(Boolean.valueOf(atts.getValue(ATTRIBUTE_isBold)));

		if (atts.getValue(ATTRIBUTE_isItalic) != null)
			style.setItalic(Boolean.valueOf(atts.getValue(ATTRIBUTE_isItalic)));

		if (atts.getValue(ATTRIBUTE_isUnderline) != null)
			style.setUnderline(Boolean.valueOf(atts.getValue(ATTRIBUTE_isUnderline)));

		if (atts.getValue(ATTRIBUTE_isStrikeThrough) != null)
			style.setStrikeThrough(Boolean.valueOf(atts.getValue(ATTRIBUTE_isStrikeThrough)));

		if (atts.getValue(ATTRIBUTE_fontSize) != null)
			style.setFontSize(Integer.valueOf(atts.getValue(ATTRIBUTE_fontSize)));

		if (atts.getValue(ATTRIBUTE_pdfFontName) != null)
			style.setPdfFontName(atts.getValue(ATTRIBUTE_pdfFontName));

		if (atts.getValue(ATTRIBUTE_pdfEncoding) != null)
			style.setPdfEncoding(atts.getValue(ATTRIBUTE_pdfEncoding));

		if (atts.getValue(ATTRIBUTE_isPdfEmbedded) != null)
			style.setPdfEmbedded(Boolean.valueOf(atts.getValue(ATTRIBUTE_isPdfEmbedded)));


		return style;
	}
}
