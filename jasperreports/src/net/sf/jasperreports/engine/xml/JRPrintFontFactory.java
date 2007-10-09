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

		if (atts.getValue(JRXmlConstants.ATTRIBUTE_reportFont) != null)
		{
			Map fontsMap = jasperPrint.getFontsMap();

			if ( !fontsMap.containsKey(atts.getValue(JRXmlConstants.ATTRIBUTE_reportFont)) )
			{
				printXmlLoader.addError(new Exception("Unknown report font : " + atts.getValue(JRXmlConstants.ATTRIBUTE_reportFont)));
			}

			element.setReportFont((JRReportFont)fontsMap.get(atts.getValue(JRXmlConstants.ATTRIBUTE_reportFont)));
		}

		if (atts.getValue(JRXmlConstants.ATTRIBUTE_fontName) != null)
			element.setFontName(atts.getValue(JRXmlConstants.ATTRIBUTE_fontName));

		if (atts.getValue(JRXmlConstants.ATTRIBUTE_isBold) != null)
			element.setBold(Boolean.valueOf(atts.getValue(JRXmlConstants.ATTRIBUTE_isBold)));

		if (atts.getValue(JRXmlConstants.ATTRIBUTE_isItalic) != null)
			element.setItalic(Boolean.valueOf(atts.getValue(JRXmlConstants.ATTRIBUTE_isItalic)));

		if (atts.getValue(JRXmlConstants.ATTRIBUTE_isUnderline) != null)
			element.setUnderline(Boolean.valueOf(atts.getValue(JRXmlConstants.ATTRIBUTE_isUnderline)));

		if (atts.getValue(JRXmlConstants.ATTRIBUTE_isStrikeThrough) != null)
			element.setStrikeThrough(Boolean.valueOf(atts.getValue(JRXmlConstants.ATTRIBUTE_isStrikeThrough)));

		if (atts.getValue(JRXmlConstants.ATTRIBUTE_size) != null)
			element.setFontSize(Integer.parseInt(atts.getValue(JRXmlConstants.ATTRIBUTE_size)));

		if (atts.getValue(JRXmlConstants.ATTRIBUTE_pdfFontName) != null)
			element.setPdfFontName(atts.getValue(JRXmlConstants.ATTRIBUTE_pdfFontName));

		if (atts.getValue(JRXmlConstants.ATTRIBUTE_pdfEncoding) != null)
			element.setPdfEncoding(atts.getValue(JRXmlConstants.ATTRIBUTE_pdfEncoding));

		if (atts.getValue(JRXmlConstants.ATTRIBUTE_isPdfEmbedded) != null)
			element.setPdfEmbedded(Boolean.valueOf(atts.getValue(JRXmlConstants.ATTRIBUTE_isPdfEmbedded)));

		return element;
	}
	

}
