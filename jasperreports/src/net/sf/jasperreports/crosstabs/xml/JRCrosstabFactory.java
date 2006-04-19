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


import net.sf.jasperreports.crosstabs.design.JRDesignCrosstab;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRBaseFactory;

import org.xml.sax.Attributes;


/**
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public class JRCrosstabFactory extends JRBaseFactory
{
	public static final String ATTRIBUTE_columnBreakOffset = "columnBreakOffset";
	public static final String ATTRIBUTE_isRepeatColumnHeaders = "isRepeatColumnHeaders";
	public static final String ATTRIBUTE_isRepeatRowHeaders = "isRepeatRowHeaders";
	
	public Object createObject(Attributes atts)
	{
		JasperDesign jasperDesign = (JasperDesign)digester.peek(digester.getCount() - 2);
		
		JRDesignCrosstab crosstab = new JRDesignCrosstab(jasperDesign);
		
		String columnBreakOffsetAttr = atts.getValue(ATTRIBUTE_columnBreakOffset);
		if (columnBreakOffsetAttr != null && columnBreakOffsetAttr.length() > 0)
		{
			crosstab.setColumnBreakOffset(Integer.parseInt(columnBreakOffsetAttr));
		}
		
		String repeatColHeadersAttr = atts.getValue(ATTRIBUTE_isRepeatColumnHeaders);
		if (repeatColHeadersAttr != null && repeatColHeadersAttr.length() > 0)
		{
			crosstab.setRepeatColumnHeaders(Boolean.valueOf(repeatColHeadersAttr).booleanValue());
		}
		
		String repeatRowHeadersAttr = atts.getValue(ATTRIBUTE_isRepeatRowHeaders);
		if (repeatRowHeadersAttr != null && repeatColHeadersAttr.length() > 0)
		{
			crosstab.setRepeatRowHeaders(Boolean.valueOf(repeatRowHeadersAttr).booleanValue());
		}
		
		return crosstab;
	}
}
