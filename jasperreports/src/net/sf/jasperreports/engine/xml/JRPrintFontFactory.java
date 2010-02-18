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

import net.sf.jasperreports.engine.JRPrintText;
import net.sf.jasperreports.engine.JRReportFont;
import net.sf.jasperreports.engine.JasperPrint;

import org.xml.sax.Attributes;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JRPrintFontFactory extends JRBaseFactory
{

	/**
	 *
	 */
	public Object createObject(Attributes atts)
	{
		JRPrintText element = (JRPrintText) digester.peek();
		JRPrintXmlLoader printXmlLoader = (JRPrintXmlLoader)digester.peek(digester.getCount() - 1);
		JasperPrint jasperPrint = (JasperPrint)digester.peek(digester.getCount() - 2);

		if (atts.getValue(XmlConstants.ATTRIBUTE_reportFont) != null)
		{
			Map fontsMap = jasperPrint.getFontsMap();

			if ( !fontsMap.containsKey(atts.getValue(XmlConstants.ATTRIBUTE_reportFont)) )
			{
				printXmlLoader.addError(new Exception("Unknown report font : " + atts.getValue(XmlConstants.ATTRIBUTE_reportFont)));
			}

			element.setReportFont((JRReportFont)fontsMap.get(atts.getValue(XmlConstants.ATTRIBUTE_reportFont)));
		}

		if (atts.getValue(XmlConstants.ATTRIBUTE_fontName) != null)
			element.setFontName(atts.getValue(XmlConstants.ATTRIBUTE_fontName));

		if (atts.getValue(XmlConstants.ATTRIBUTE_isBold) != null)
			element.setBold(Boolean.valueOf(atts.getValue(XmlConstants.ATTRIBUTE_isBold)));

		if (atts.getValue(XmlConstants.ATTRIBUTE_isItalic) != null)
			element.setItalic(Boolean.valueOf(atts.getValue(XmlConstants.ATTRIBUTE_isItalic)));

		if (atts.getValue(XmlConstants.ATTRIBUTE_isUnderline) != null)
			element.setUnderline(Boolean.valueOf(atts.getValue(XmlConstants.ATTRIBUTE_isUnderline)));

		if (atts.getValue(XmlConstants.ATTRIBUTE_isStrikeThrough) != null)
			element.setStrikeThrough(Boolean.valueOf(atts.getValue(XmlConstants.ATTRIBUTE_isStrikeThrough)));

		if (atts.getValue(XmlConstants.ATTRIBUTE_size) != null)
			element.setFontSize(Integer.parseInt(atts.getValue(XmlConstants.ATTRIBUTE_size)));

		if (atts.getValue(XmlConstants.ATTRIBUTE_pdfFontName) != null)
			element.setPdfFontName(atts.getValue(XmlConstants.ATTRIBUTE_pdfFontName));

		if (atts.getValue(XmlConstants.ATTRIBUTE_pdfEncoding) != null)
			element.setPdfEncoding(atts.getValue(XmlConstants.ATTRIBUTE_pdfEncoding));

		if (atts.getValue(XmlConstants.ATTRIBUTE_isPdfEmbedded) != null)
			element.setPdfEmbedded(Boolean.valueOf(atts.getValue(XmlConstants.ATTRIBUTE_isPdfEmbedded)));

		return element;
	}
	

}
