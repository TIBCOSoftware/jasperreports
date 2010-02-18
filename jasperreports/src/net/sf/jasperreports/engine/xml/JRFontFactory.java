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

import java.util.Map;

import net.sf.jasperreports.engine.JRFont;
import net.sf.jasperreports.engine.JRReportFont;
import net.sf.jasperreports.engine.design.JRDesignFont;
import net.sf.jasperreports.engine.design.JRValidationException;
import net.sf.jasperreports.engine.design.JasperDesign;

import org.xml.sax.Attributes;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
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
	public Object createObject(Attributes atts)
	{
		JRFont font = getFont();
		JRXmlLoader xmlLoader = (JRXmlLoader)digester.peek(digester.getCount() - 1);
		JasperDesign jasperDesign = (JasperDesign)digester.peek(digester.getCount() - 2);

		if (atts.getValue(XmlConstants.ATTRIBUTE_reportFont) != null)
		{
			Map fontsMap = jasperDesign.getFontsMap();

			if ( !fontsMap.containsKey(atts.getValue(XmlConstants.ATTRIBUTE_reportFont)) )
			{
				xmlLoader.addError(new JRValidationException("Unknown report font : " + atts.getValue(XmlConstants.ATTRIBUTE_reportFont), font));
			}

			font.setReportFont((JRReportFont)fontsMap.get(atts.getValue(XmlConstants.ATTRIBUTE_reportFont)));
		}

		if (atts.getValue(XmlConstants.ATTRIBUTE_fontName) != null)
			font.setFontName(atts.getValue(XmlConstants.ATTRIBUTE_fontName));

		if (atts.getValue(XmlConstants.ATTRIBUTE_isBold) != null)
			font.setBold(Boolean.valueOf(atts.getValue(XmlConstants.ATTRIBUTE_isBold)));

		if (atts.getValue(XmlConstants.ATTRIBUTE_isItalic) != null)
			font.setItalic(Boolean.valueOf(atts.getValue(XmlConstants.ATTRIBUTE_isItalic)));

		if (atts.getValue(XmlConstants.ATTRIBUTE_isUnderline) != null)
			font.setUnderline(Boolean.valueOf(atts.getValue(XmlConstants.ATTRIBUTE_isUnderline)));

		if (atts.getValue(XmlConstants.ATTRIBUTE_isStrikeThrough) != null)
			font.setStrikeThrough(Boolean.valueOf(atts.getValue(XmlConstants.ATTRIBUTE_isStrikeThrough)));

		if (atts.getValue(XmlConstants.ATTRIBUTE_size) != null)
			font.setFontSize(Integer.parseInt(atts.getValue(XmlConstants.ATTRIBUTE_size)));

		if (atts.getValue(XmlConstants.ATTRIBUTE_pdfFontName) != null)
			font.setPdfFontName(atts.getValue(XmlConstants.ATTRIBUTE_pdfFontName));

		if (atts.getValue(XmlConstants.ATTRIBUTE_pdfEncoding) != null)
			font.setPdfEncoding(atts.getValue(XmlConstants.ATTRIBUTE_pdfEncoding));

		if (atts.getValue(XmlConstants.ATTRIBUTE_isPdfEmbedded) != null)
			font.setPdfEmbedded(Boolean.valueOf(atts.getValue(XmlConstants.ATTRIBUTE_isPdfEmbedded)));

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
	}
	

	/**
	 *
	 */
	public static class ChartFontFactory extends JRFontFactory
	{
		public JRFont getFont()
		{
			return new JRDesignFont();
		}
	}
	
	
}
