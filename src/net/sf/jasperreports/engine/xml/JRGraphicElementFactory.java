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

import net.sf.jasperreports.engine.design.JRDesignGraphicElement;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JRGraphicElementFactory extends JRBaseFactory
{


	/**
	 *
	 */
	private static final String ATTRIBUTE_stretchType = "stretchType";
	private static final String ATTRIBUTE_pen = "pen";
	private static final String ATTRIBUTE_fill = "fill";


	/**
	 *
	 */
	public Object createObject(Attributes atts)
	{
		JRDesignGraphicElement graphicElement = (JRDesignGraphicElement)digester.peek();
		
		Byte stretchType = (Byte)JRXmlConstants.getStretchTypeMap().get(atts.getValue(ATTRIBUTE_stretchType));
		if (stretchType != null)
		{
			graphicElement.setStretchType(stretchType.byteValue());
		}

		Byte pen = (Byte)JRXmlConstants.getPenMap().get(atts.getValue(ATTRIBUTE_pen));
		if (pen != null)
		{
			graphicElement.setPen(pen.byteValue());
		}

		Byte fill = (Byte)JRXmlConstants.getFillMap().get(atts.getValue(ATTRIBUTE_fill));
		if (fill != null)
		{
			graphicElement.setFill(fill.byteValue());
		}

		return graphicElement;
	}
	

}
