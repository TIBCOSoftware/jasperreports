/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2013 Jaspersoft Corporation. All rights reserved.
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
package net.sf.jasperreports.components.map.fill;

import net.sf.jasperreports.components.map.Item;
import net.sf.jasperreports.components.map.ItemProperty;
import net.sf.jasperreports.components.map.MapComponent;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.fill.JRFillObjectFactory;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class FillMarkerItem extends FillItem
{
	
	/**
	 *
	 */
	public FillMarkerItem(
		Item item, 
		JRFillObjectFactory factory
		)
	{
		super(item, factory);
	}

	public void verifyValue(ItemProperty property, Object value) throws JRException
	{
		if(MapComponent.PROPERTY_latitude.equals(property.getName()) || MapComponent.PROPERTY_longitude.equals(property.getName()))
		{
			if(value == null || "".equals(value))
			{
				throw new JRException("Empty item "+ property.getName()+ " found.");
			}
		}
	}

}
