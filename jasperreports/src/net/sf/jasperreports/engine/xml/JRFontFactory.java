/*
 * ============================================================================
 * GNU Lesser General Public License
 * ============================================================================
 *
 * JasperReports - Free Java report-generating library.
 * Copyright (C) 2001-2005 JasperSoft Corporation http://www.jaspersoft.com
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

import net.sf.jasperreports.engine.JRReportFont;
import net.sf.jasperreports.engine.design.JRDesignTextElement;
import net.sf.jasperreports.engine.design.JasperDesign;

import org.xml.sax.Attributes;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JRFontFactory extends JRBaseFactory
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
		JRDesignTextElement element = (JRDesignTextElement) digester.peek();
		JRXmlLoader xmlLoader = (JRXmlLoader)digester.peek(digester.getCount() - 1);
		JasperDesign jasperDesign = (JasperDesign)digester.peek(digester.getCount() - 2);

		if (atts.getValue(ATTRIBUTE_reportFont) != null)
		{
			Map fontsMap = jasperDesign.getFontsMap();

			if ( !fontsMap.containsKey(atts.getValue(ATTRIBUTE_reportFont)) )
			{
				xmlLoader.addError(new Exception("Unknown report font : " + atts.getValue(ATTRIBUTE_reportFont)));
			}

			element.setReportFont((JRReportFont)fontsMap.get(atts.getValue(ATTRIBUTE_reportFont)));
		}

		if (atts.getValue(ATTRIBUTE_fontName) != null)
			element.setFontName(atts.getValue(ATTRIBUTE_fontName));

		if (atts.getValue(ATTRIBUTE_isBold) != null)
			element.setBold(Boolean.valueOf(atts.getValue(ATTRIBUTE_isBold)));

		if (atts.getValue(ATTRIBUTE_isItalic) != null)
			element.setItalic(Boolean.valueOf(atts.getValue(ATTRIBUTE_isItalic)));

		if (atts.getValue(ATTRIBUTE_isUnderline) != null)
			element.setUnderline(Boolean.valueOf(atts.getValue(ATTRIBUTE_isUnderline)));

		if (atts.getValue(ATTRIBUTE_isStrikeThrough) != null)
			element.setStrikeThrough(Boolean.valueOf(atts.getValue(ATTRIBUTE_isStrikeThrough)));

		if (atts.getValue(ATTRIBUTE_size) != null)
			element.setFontSize(Integer.parseInt(atts.getValue(ATTRIBUTE_size)));

		if (atts.getValue(ATTRIBUTE_pdfFontName) != null)
			element.setPdfFontName(atts.getValue(ATTRIBUTE_pdfFontName));

		if (atts.getValue(ATTRIBUTE_pdfEncoding) != null)
			element.setPdfEncoding(atts.getValue(ATTRIBUTE_pdfEncoding));

		if (atts.getValue(ATTRIBUTE_isPdfEmbedded) != null)
			element.setPdfEmbedded(Boolean.valueOf(atts.getValue(ATTRIBUTE_isPdfEmbedded)));

		return element;
	}
	

}
