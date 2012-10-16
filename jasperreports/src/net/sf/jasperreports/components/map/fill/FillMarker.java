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
package net.sf.jasperreports.components.map.fill;

import java.util.HashMap;
import java.util.Map;

import net.sf.jasperreports.components.map.Marker;
import net.sf.jasperreports.components.map.MarkerProperty;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.component.FillContext;
import net.sf.jasperreports.engine.fill.JRFillObjectFactory;


/**
 * @author sanda zaharia (shertage@users.sourceforge.net)
 * @version $Id$
 */
public class FillMarker implements Marker
{

	/**
	 *
	 */
	protected Marker parent;
	private JRFillObjectFactory factory;
	
	/**
	 *
	 */
	public FillMarker(
		Marker marker, 
		JRFillObjectFactory factory
		)
	{
		factory.put(marker, this);

		parent = marker;
		this.factory = factory;
	}
	
	
	/**
	 *
	 */
	public Map<String, Object> evaluateProperties(FillContext fillContext, byte evaluation) throws JRException
	{
		Map<String, MarkerProperty> markerProperties = getMarkerProperties();
		Map<String, Object> result = null;
		if(markerProperties != null && !markerProperties.isEmpty())
		{
			result = new HashMap<String, Object>();
			for(String name : markerProperties.keySet())
			{
				FillMarkerProperty markerProperty = new FillMarkerProperty(markerProperties.get(name), factory);
				result.put(
					"'"+name+"'", 
					markerProperty.getValue() != null
					? markerProperty.getValue()
					: markerProperty.evaluateValueExpression(fillContext, evaluation));
			}
		}
		return result;
	}


	/**
	 *
	 */
	public Object clone() 
	{
		throw new UnsupportedOperationException();
	}


	@Override
	public MarkerProperty getMarkerProperty(String name) 
	{
		return parent.getMarkerProperty(name);
	}


	@Override
	public Map<String, MarkerProperty> getMarkerProperties() 
	{
		return parent.getMarkerProperties();
	}
}
