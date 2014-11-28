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
package net.sf.jasperreports.engine.fill;

import java.util.Locale;
import java.util.TimeZone;

import net.sf.jasperreports.engine.JRElement;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.style.StyleProviderContext;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public class FillStyleProviderContext implements StyleProviderContext
{
	private JRFillElement element;

	
	/**
	 *
	 */
	protected FillStyleProviderContext(JRFillElement element)
	{
		this.element = element;
	}

	
	/**
	 *
	 */
	public JasperReportsContext getJasperReportsContext()
	{
		return element.filler.getJasperReportsContext();
	}


	/**
	 *
	 */
	public JRElement getElement()
	{
		return element;
	}


	/**
	 *
	 */
	public Object evaluateExpression(JRExpression expression, byte evaluation)
	{
		try
		{
			return element.evaluateExpression(expression, evaluation);
		}
		catch (JRException e)
		{
			throw new JRRuntimeException(e);
		}
	}


	/**
	 *
	 */
	public Object getFieldValue(String fieldName, byte evaluation)
	{
		return element.getField(fieldName).getValue(evaluation);
	}


	/**
	 *
	 */
	public Object getVariableValue(String variableName, byte evaluation)
	{
		return element.getVariable(variableName).getValue(evaluation);
	}


	/**
	 *
	 */
	public Locale getLocale()
	{
		return element.filler.getLocale();
	}


	/**
	 *
	 */
	public TimeZone getTimeZone()
	{
		return element.filler.getTimeZone();
	}
}
