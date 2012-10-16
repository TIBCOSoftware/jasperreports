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

import net.sf.jasperreports.components.map.MarkerProperty;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.component.FillContext;
import net.sf.jasperreports.engine.fill.JRCalculator;
import net.sf.jasperreports.engine.fill.JRExpressionEvalException;
import net.sf.jasperreports.engine.fill.JRFillObjectFactory;


/**
 * @author sanda zaharia (shertage@users.sourceforge.net)
 * @version $Id$
 */
public class FillMarkerProperty implements MarkerProperty
{

	/**
	 *
	 */
	protected MarkerProperty parent;
	private String value;
	
	/**
	 *
	 */
	public FillMarkerProperty(
		MarkerProperty markerProperty, 
		JRFillObjectFactory factory
		)
	{
		factory.put(markerProperty, this);

		parent = markerProperty;
		value = markerProperty.getValue();
	}


	/**
	 * 
	 */
	public String getName()
	{
		return parent.getName();
	}
	
	
	/**
	 * 
	 */
	public String getValue()
	{
		return value;
	}
		

	/**
	 *
	 */
	public JRExpression getValueExpression()
	{
		return parent.getValueExpression();
	}

	
	/**
	 *
	 */
	public Object evaluateValueExpression(FillContext fillContext, byte evaluation) throws JRException
	{
		return getValueExpression() == null ? null : fillContext.evaluate(getValueExpression(), evaluation);
	}


	/**
	 *
	 */
	public Object clone() 
	{
		throw new UnsupportedOperationException();
	}
}
