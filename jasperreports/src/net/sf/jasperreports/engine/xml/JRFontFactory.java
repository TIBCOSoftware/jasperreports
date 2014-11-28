/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2014 TIBCO Software Inc. All rights reserved.
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

import java.util.Map;

import net.sf.jasperreports.engine.JRChart;
import net.sf.jasperreports.engine.JRFont;
import net.sf.jasperreports.engine.JRStyle;
import net.sf.jasperreports.engine.design.JRDesignElement;
import net.sf.jasperreports.engine.design.JRDesignFont;
import net.sf.jasperreports.engine.design.JasperDesign;

import org.xml.sax.Attributes;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public abstract class JRFontFactory extends JRBaseFactory
{

	/**
	 *
	 */
	public abstract JRFont getFont();
	
	
	/**
	 *
	 */
	public abstract void setStyle(JRFont font, Attributes atts);
	
	
	/**
	 *
	 */
	public Object createObject(Attributes atts)
	{
		JRFont font = getFont();

		if (atts.getValue(JRXmlConstants.ATTRIBUTE_fontName) != null)
		{
			font.setFontName(atts.getValue(JRXmlConstants.ATTRIBUTE_fontName));
		}

		if (atts.getValue(JRXmlConstants.ATTRIBUTE_isBold) != null)
		{
			font.setBold(Boolean.valueOf(atts.getValue(JRXmlConstants.ATTRIBUTE_isBold)));
		}
		if (atts.getValue(JRXmlConstants.ATTRIBUTE_isItalic) != null)
		{
			font.setItalic(Boolean.valueOf(atts.getValue(JRXmlConstants.ATTRIBUTE_isItalic)));
		}
		if (atts.getValue(JRXmlConstants.ATTRIBUTE_isUnderline) != null)
		{
			font.setUnderline(Boolean.valueOf(atts.getValue(JRXmlConstants.ATTRIBUTE_isUnderline)));
		}
		if (atts.getValue(JRXmlConstants.ATTRIBUTE_isStrikeThrough) != null)
		{
			font.setStrikeThrough(Boolean.valueOf(atts.getValue(JRXmlConstants.ATTRIBUTE_isStrikeThrough)));
		}
		if (atts.getValue(JRXmlConstants.ATTRIBUTE_size) != null)
		{
			font.setFontSize(Float.parseFloat(atts.getValue(JRXmlConstants.ATTRIBUTE_size)));
		}
		if (atts.getValue(JRXmlConstants.ATTRIBUTE_pdfFontName) != null)
		{
			font.setPdfFontName(atts.getValue(JRXmlConstants.ATTRIBUTE_pdfFontName));
		}
		if (atts.getValue(JRXmlConstants.ATTRIBUTE_pdfEncoding) != null)
		{
			font.setPdfEncoding(atts.getValue(JRXmlConstants.ATTRIBUTE_pdfEncoding));
		}
		if (atts.getValue(JRXmlConstants.ATTRIBUTE_isPdfEmbedded) != null)
		{
			font.setPdfEmbedded(Boolean.valueOf(atts.getValue(JRXmlConstants.ATTRIBUTE_isPdfEmbedded)));
		}
		
		setStyle(font, atts);
		
		return font;
	}
	

	/**
	 *
	 */
	public static class TextElementFontFactory extends JRFontFactory
	{
		public JRFont getFont()
		{
			return (JRFont)digester.peek();
		}

		public void setStyle(JRFont font, Attributes atts)
		{
			JRDesignElement element = (JRDesignElement)font;

			if (
				element.getStyle() == null
				&& element.getStyleNameReference() == null
				)
			{
				String styleName = atts.getValue(JRXmlConstants.ATTRIBUTE_reportFont);
				if (styleName != null)
				{
					JasperDesign jasperDesign = (JasperDesign)digester.peek(digester.getCount() - 2);
					Map<String,JRStyle> stylesMap = jasperDesign.getStylesMap();

					if (stylesMap.containsKey(styleName))
					{
						JRStyle style = stylesMap.get(styleName);
						element.setStyle(style);
					}
					else
					{
						element.setStyleNameReference(styleName);
					}
				}
			}
		}
	}
	

	/**
	 *
	 */
	public static class ChartFontFactory extends JRFontFactory
	{
		public JRFont getFont()
		{
			int i = 0;
			JRChart chart = null;
			while (chart == null && i < digester.getCount())
			{
				Object obj = digester.peek(i);
				chart = obj instanceof JRChart ? (JRChart)obj : null;
				i++;
			}
			
			return new JRDesignFont(chart);
		}
		
		public void setStyle(JRFont font, Attributes atts)
		{
			JRDesignFont designFont = (JRDesignFont)font;

//			if (
//				designFont.getStyle() == null
//				&& designFont.getStyleNameReference() == null
//				)
//			{
				String styleName = atts.getValue(JRXmlConstants.ATTRIBUTE_reportFont);
				if (styleName != null)
				{
					JasperDesign jasperDesign = (JasperDesign)digester.peek(digester.getCount() - 2);
					Map<String,JRStyle> stylesMap = jasperDesign.getStylesMap();

					if (stylesMap.containsKey(styleName))
					{
						JRStyle style = stylesMap.get(styleName);
						designFont.setStyle(style);
					}
					else
					{
						designFont.setStyleNameReference(styleName);
					}
				}
//			}
		}
	}
	
	
}
