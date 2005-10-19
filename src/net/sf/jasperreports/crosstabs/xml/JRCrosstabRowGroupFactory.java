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
 * 185, Berry Street, Suite 6200
 * San Francisco CA 94107
 * http://www.jaspersoft.com
 */
package net.sf.jasperreports.crosstabs.xml;

import net.sf.jasperreports.crosstabs.design.JRDesignCrosstabRowGroup;
import net.sf.jasperreports.engine.xml.JRXmlConstants;

import org.xml.sax.Attributes;

/**
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public class JRCrosstabRowGroupFactory extends JRCrosstabGroupFactory
{
	public static final String ATTRIBUTE_width = "width";
	public static final String ATTRIBUTE_headerPosition = "headerPosition";

	public Object createObject(Attributes attributes)
	{
		JRDesignCrosstabRowGroup group = new JRDesignCrosstabRowGroup();
		
		setGroupAtts(attributes, group);
		
		String widthAttr = attributes.getValue(ATTRIBUTE_width);
		if (widthAttr != null)
		{
			group.setWidth(Integer.parseInt(widthAttr));
		}
		
		String posAttr = attributes.getValue(ATTRIBUTE_headerPosition);
		if (posAttr != null)
		{
			Byte pos = (Byte) JRXmlConstants.getCrosstabRowPositionMap().get(posAttr);
			group.setPosition(pos.byteValue());
		}
		
		return group;
	}

}
