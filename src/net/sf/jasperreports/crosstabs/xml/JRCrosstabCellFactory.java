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
package net.sf.jasperreports.crosstabs.xml;

import net.sf.jasperreports.crosstabs.design.JRDesignCrosstabCell;
import net.sf.jasperreports.engine.xml.JRBaseFactory;

import org.xml.sax.Attributes;

/**
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public class JRCrosstabCellFactory extends JRBaseFactory
{
	public static final String ATTRIBUTE_rowTotalGroup = "rowTotalGroup";
	public static final String ATTRIBUTE_columnTotalGroup = "columnTotalGroup";
	public static final String ATTRIBUTE_width = "width";
	public static final String ATTRIBUTE_height = "height";
	
	public Object createObject(Attributes attributes)
	{
		JRDesignCrosstabCell cell = new JRDesignCrosstabCell();
		
		cell.setRowTotalGroup(attributes.getValue(ATTRIBUTE_rowTotalGroup));
		cell.setColumnTotalGroup(attributes.getValue(ATTRIBUTE_columnTotalGroup));
		
		String widthAttr = attributes.getValue(ATTRIBUTE_width);
		if (widthAttr != null)
		{
			cell.setWidth(new Integer(widthAttr));
		}
		
		String heightAttr = attributes.getValue(ATTRIBUTE_height);
		if (heightAttr != null)
		{
			cell.setHeight(new Integer(heightAttr));
		}

		return cell;
	}
}
