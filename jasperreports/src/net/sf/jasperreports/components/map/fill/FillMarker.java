/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2014 TIBCO Software Inc. All rights reserved.
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
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.components.map.Marker;
import net.sf.jasperreports.components.map.MarkerProperty;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.fill.JRFillExpressionEvaluator;
import net.sf.jasperreports.engine.fill.JRFillObjectFactory;


/**
 * @deprecated Replaced by {@link FillItem}.
 * @author sanda zaharia (shertage@users.sourceforge.net)
 */
public class FillMarker implements Marker
{
	public static final String EXCEPTION_MESSAGE_KEY_INVALID_VALUE_LOCATION = "charts.thermometer.plot.invalid.value.location";

	/**
	 *
	 */
	protected Marker parent;
	
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
	}
	
	
	/**
	 *
	 */
	public Map<String, Object> evaluateProperties(JRFillExpressionEvaluator evaluator, byte evaluation) throws JRException
	{
		List<MarkerProperty> markerProperties = getProperties();
		Map<String, Object> result = null;
		if(markerProperties != null && !markerProperties.isEmpty())
		{
			result = new HashMap<String, Object>();
			for(MarkerProperty property : markerProperties)
			{
				result.put(property.getName(), getEvaluatedValue(property, evaluator, evaluation));
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
	public List<MarkerProperty> getProperties() 
	{
		return parent.getProperties();
	}
	
	public Object getEvaluatedValue(MarkerProperty property, JRFillExpressionEvaluator evaluator, byte evaluation) throws JRException
	{
		Object result = null;
		if (
			property.getValueExpression() == null 
			|| property.getValueExpression().getText() == null
			|| property.getValueExpression().getText().trim().length() == 0
			)
		{
			if(Marker.PROPERTY_latitude.equals(property.getName()) || Marker.PROPERTY_longitude.equals(property.getName()))
			{
				if(property.getValue() == null || property.getValue().length() == 0)
				{
					throw 
						new JRException(
							MapFillComponent.EXCEPTION_MESSAGE_KEY_NULL_OR_EMPTY_VALUE_NOT_ALLOWED,
							new Object[]{property.getName()});
				}
			}
			result = property.getValue();
		}
		else
		{
			result = evaluator.evaluate(property.getValueExpression(), evaluation);
			if(Marker.PROPERTY_latitude.equals(property.getName()) || Marker.PROPERTY_longitude.equals(property.getName()))
			{
				if(
					result == null 
					|| String.valueOf(result).trim().length() == 0
					)
				{
					throw 
						new JRException(
							MapFillComponent.EXCEPTION_MESSAGE_KEY_NULL_OR_EMPTY_VALUE_NOT_ALLOWED,
							new Object[]{property.getName()});
				}
			}
		}
		return result;
	}
}
