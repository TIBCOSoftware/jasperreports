/*
 * ============================================================================
 *                   The JasperReports License, Version 1.0
 * ============================================================================
 * 
 * Copyright (C) 2001-2004 Teodor Danciu (teodord@users.sourceforge.net). All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 
 * 3. The end-user documentation included with the redistribution, if any, must
 *    include the following acknowledgment: "This product includes software
 *    developed by Teodor Danciu (http://jasperreports.sourceforge.net)."
 *    Alternately, this acknowledgment may appear in the software itself, if
 *    and wherever such third-party acknowledgments normally appear.
 * 
 * 4. The name "JasperReports" must not be used to endorse or promote products 
 *    derived from this software without prior written permission. For written 
 *    permission, please contact teodord@users.sourceforge.net.
 * 
 * 5. Products derived from this software may not be called "JasperReports", nor 
 *    may "JasperReports" appear in their name, without prior written permission
 *    of Teodor Danciu.
 * 
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS  FOR A PARTICULAR  PURPOSE ARE  DISCLAIMED.  IN NO  EVENT SHALL  THE
 * APACHE SOFTWARE  FOUNDATION  OR ITS CONTRIBUTORS  BE LIABLE FOR  ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL,  EXEMPLARY, OR CONSEQUENTIAL  DAMAGES (INCLU-
 * DING, BUT NOT LIMITED TO, PROCUREMENT  OF SUBSTITUTE GOODS OR SERVICES; LOSS
 * OF USE, DATA, OR  PROFITS; OR BUSINESS  INTERRUPTION)  HOWEVER CAUSED AND ON
 * ANY  THEORY OF LIABILITY,  WHETHER  IN CONTRACT,  STRICT LIABILITY,  OR TORT
 * (INCLUDING  NEGLIGENCE OR  OTHERWISE) ARISING IN  ANY WAY OUT OF THE  USE OF
 * THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

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
package dori.jasper.engine.xml;

import org.xml.sax.Attributes;

import dori.jasper.engine.design.JRDesignReportFont;


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
			reportFont.setBold(Boolean.valueOf(isBold).booleanValue());
		}

		String isItalic = atts.getValue(ATTRIBUTE_isItalic);
		if (isItalic != null && isItalic.length() > 0)
		{
			reportFont.setItalic(Boolean.valueOf(isItalic).booleanValue());
		}

		String isUnderline = atts.getValue(ATTRIBUTE_isUnderline);
		if (isUnderline != null && isUnderline.length() > 0)
		{
			reportFont.setUnderline(Boolean.valueOf(isUnderline).booleanValue());
		}

		String isStrikeThrough = atts.getValue(ATTRIBUTE_isStrikeThrough);
		if (isStrikeThrough != null && isStrikeThrough.length() > 0)
		{
			reportFont.setStrikeThrough(Boolean.valueOf(isStrikeThrough).booleanValue());
		}

		String size = atts.getValue(ATTRIBUTE_size);
		if (size != null && size.length() > 0)
		{
			reportFont.setSize(Integer.parseInt(size));
		}

		reportFont.setPdfFontName(atts.getValue(ATTRIBUTE_pdfFontName));
		reportFont.setPdfEncoding(atts.getValue(ATTRIBUTE_pdfEncoding));

		String isPdfEmbedded = atts.getValue(ATTRIBUTE_isPdfEmbedded);
		if (isPdfEmbedded != null && isPdfEmbedded.length() > 0)
		{
			reportFont.setPdfEmbedded(Boolean.valueOf(isPdfEmbedded).booleanValue());
		}

		return reportFont;
	}
	

}
