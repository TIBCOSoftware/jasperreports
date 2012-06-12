/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2011 Jaspersoft Corporation. All rights reserved.
 * http://www.jaspersoft.com
 *
 * Unless you have purchased a commercial license agreement from Jaspersoft,
 * the following license terms apply:
 *
 * This program is part of JasperReports.
 *
 * JasperReports is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * JasperReports is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with JasperReports. If not, see <http://www.gnu.org/licenses/>.
 */
package net.sf.jasperreports.crosstabs.xml;

import net.sf.jasperreports.crosstabs.design.JRDesignCrosstabRowGroup;
import net.sf.jasperreports.crosstabs.type.CrosstabRowPositionEnum;

import org.xml.sax.Attributes;

/**
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public class JRCrosstabRowGroupFactory extends JRCrosstabGroupFactory
{
	public static final String ELEMENT_rowGroup = "rowGroup";
	public static final String ELEMENT_crosstabRowHeader = "crosstabRowHeader";
	public static final String ELEMENT_crosstabTotalRowHeader = "crosstabTotalRowHeader";

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
		
		CrosstabRowPositionEnum position = CrosstabRowPositionEnum.getByName(attributes.getValue(ATTRIBUTE_headerPosition));
		if (position != null)
		{
			group.setPosition(position);
		}
		
		return group;
	}

}
