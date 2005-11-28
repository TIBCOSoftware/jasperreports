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

import net.sf.jasperreports.engine.design.JRDesignReportFont;

import org.xml.sax.Attributes;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JRReportFontFactory extends JRBaseFactory
{


	/**
	 *
	 */
	private static final String ATTRIBUTE_name = "name";
	private static final String ATTRIBUTE_isDefault = "isDefault";
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
		JRDesignReportFont reportFont = new JRDesignReportFont();

		reportFont.setName(atts.getValue(ATTRIBUTE_name));
		
		String isDefault = atts.getValue(ATTRIBUTE_isDefault);
		if (isDefault != null && isDefault.length() > 0)
		{
			reportFont.setDefault(Boolean.valueOf(isDefault).booleanValue());
		}

		reportFont.setFontName(atts.getValue(ATTRIBUTE_fontName));

		String isBold = atts.getValue(ATTRIBUTE_isBold);
		if (isBold != null && isBold.length() > 0)
		{
			reportFont.setBold(Boolean.valueOf(isBold));
		}

		String isItalic = atts.getValue(ATTRIBUTE_isItalic);
		if (isItalic != null && isItalic.length() > 0)
		{
			reportFont.setItalic(Boolean.valueOf(isItalic));
		}

		String isUnderline = atts.getValue(ATTRIBUTE_isUnderline);
		if (isUnderline != null && isUnderline.length() > 0)
		{
			reportFont.setUnderline(Boolean.valueOf(isUnderline));
		}

		String isStrikeThrough = atts.getValue(ATTRIBUTE_isStrikeThrough);
		if (isStrikeThrough != null && isStrikeThrough.length() > 0)
		{
			reportFont.setStrikeThrough(Boolean.valueOf(isStrikeThrough));
		}

		String size = atts.getValue(ATTRIBUTE_size);
		if (size != null && size.length() > 0)
		{
			reportFont.setFontSize(Integer.parseInt(size));
		}

		reportFont.setPdfFontName(atts.getValue(ATTRIBUTE_pdfFontName));
		reportFont.setPdfEncoding(atts.getValue(ATTRIBUTE_pdfEncoding));

		String isPdfEmbedded = atts.getValue(ATTRIBUTE_isPdfEmbedded);
		if (isPdfEmbedded != null && isPdfEmbedded.length() > 0)
		{
			reportFont.setPdfEmbedded(Boolean.valueOf(isPdfEmbedded));
		}

		return reportFont;
	}
	

}
