/*
 * ============================================================================
 *                   GNU Lesser General Public License
 * ============================================================================
 *
 * JasperReports - Free Java report-generating library.
 * Copyright (C) 2001-2005 Teodor Danciu teodord@users.sourceforge.net
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

import org.xml.sax.Attributes;

import net.sf.jasperreports.engine.JasperPrint;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JasperPrintFactory extends JRBaseFactory
{

	
	/**
	 *
	 */
	private static final String ATTRIBUTE_name = "name";
	private static final String ATTRIBUTE_pageWidth = "pageWidth";
	private static final String ATTRIBUTE_pageHeight = "pageHeight";
	private static final String ATTRIBUTE_orientation = "orientation";


	/**
	 *
	 */
	public Object createObject(Attributes atts)
	{
		JasperPrint jasperPrint = new JasperPrint();
		
		jasperPrint.setName(atts.getValue(ATTRIBUTE_name));

		String pageWidth = atts.getValue(ATTRIBUTE_pageWidth);
		if (pageWidth != null && pageWidth.length() > 0)
		{
			jasperPrint.setPageWidth(Integer.parseInt(pageWidth));
		}

		String pageHeight = atts.getValue(ATTRIBUTE_pageHeight);
		if (pageHeight != null && pageHeight.length() > 0)
		{
			jasperPrint.setPageHeight(Integer.parseInt(pageHeight));
		}

		Byte orientation = (Byte)JRXmlConstants.getOrientationMap().get(atts.getValue(ATTRIBUTE_orientation));
		if (orientation != null)
		{
			jasperPrint.setOrientation(orientation.byteValue());
		}

		return jasperPrint;
	}
	

}
