/*
 * ============================================================================
 *                   GNU Lesser General Public License
 * ============================================================================
 *
 * JasperReports - Free Java report-generating library.
 * Copyright (C) 2001-2004 Teodor Danciu teodord@users.sourceforge.net
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
 * Teodor Danciu
 * 173, Calea Calarasilor, Bl. 42, Sc. 1, Ap. 18
 * Postal code 030615, Sector 3
 * Bucharest, ROMANIA
 * Email: teodord@users.sourceforge.net
 */
package net.sf.jasperreports.engine.xml;

import java.util.Map;

import org.xml.sax.Attributes;

import net.sf.jasperreports.engine.JRReportFont;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.design.JRDesignFont;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JRPrintFontFactory extends JRBaseFactory
{


	/**
	 *
	 */
	private static final String ATTRIBUTE_reportFont = "reportFont";
	private static final String ATTRIBUTE_fontName = "fontName";
	private static final String ATTRIBUTE_isBold = "isBold";
	private static final String ATTRIBUTE_isItalic = "isItalic";
	private static final String ATTRIBUTE_isUnderline = "isUnderline";
	private static final String ATTRIBUTE_isStrikeThrough = "isStrikeThrough";
	private static final String ATTRIBUTE_size = "size";
	private static final String ATTRIBUTE_pdfFontName = "pdfFontName";
	private static final String ATTRIBUTE_pdfEncoding = "pdfEncoding";
	private static final String ATTRIBUTE_isPdfEmbedded = "isPdfEmbedded";


	/**
	 *
	 */
	public Object createObject(Attributes atts)
	{
		JRPrintXmlLoader printXmlLoader = (JRPrintXmlLoader)digester.peek(digester.getCount() - 1);
		JasperPrint jasperPrint = (JasperPrint)digester.peek(digester.getCount() - 2);

		JRDesignFont font = null;

		if (atts.getValue(ATTRIBUTE_reportFont) != null)
		{
			Map fontsMap = jasperPrint.getFontsMap();

			if ( !fontsMap.containsKey(atts.getValue(ATTRIBUTE_reportFont)) )
			{
				printXmlLoader.addError(new Exception("Unknown report font : " + atts.getValue(ATTRIBUTE_reportFont)));
			}

			font = new JRDesignFont();
			font.setReportFont((JRReportFont)fontsMap.get(atts.getValue(ATTRIBUTE_reportFont)));
		}
		else
		{
			font = new JRDesignFont(jasperPrint);
		}

		if (atts.getValue(ATTRIBUTE_fontName) != null)
			font.setFontName(atts.getValue(ATTRIBUTE_fontName));

		if (atts.getValue(ATTRIBUTE_isBold) != null)
			font.setBold(Boolean.valueOf(atts.getValue(ATTRIBUTE_isBold)).booleanValue());

		if (atts.getValue(ATTRIBUTE_isItalic) != null)
			font.setItalic(Boolean.valueOf(atts.getValue(ATTRIBUTE_isItalic)).booleanValue());

		if (atts.getValue(ATTRIBUTE_isUnderline) != null)
			font.setUnderline(Boolean.valueOf(atts.getValue(ATTRIBUTE_isUnderline)).booleanValue());

		if (atts.getValue(ATTRIBUTE_isStrikeThrough) != null)
			font.setStrikeThrough(Boolean.valueOf(atts.getValue(ATTRIBUTE_isStrikeThrough)).booleanValue());

		if (atts.getValue(ATTRIBUTE_size) != null)
			font.setSize(Integer.parseInt(atts.getValue(ATTRIBUTE_size)));

		if (atts.getValue(ATTRIBUTE_pdfFontName) != null)
			font.setPdfFontName(atts.getValue(ATTRIBUTE_pdfFontName));

		if (atts.getValue(ATTRIBUTE_pdfEncoding) != null)
			font.setPdfEncoding(atts.getValue(ATTRIBUTE_pdfEncoding));

		if (atts.getValue(ATTRIBUTE_isPdfEmbedded) != null)
			font.setPdfEmbedded(Boolean.valueOf(atts.getValue(ATTRIBUTE_isPdfEmbedded)).booleanValue());

		return font;
	}
	

}
