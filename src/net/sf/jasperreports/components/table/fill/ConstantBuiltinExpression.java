/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2009 Jaspersoft Corporation. All rights reserved.
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
package net.sf.jasperreports.components.table.fill;

import java.util.Map;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.fill.JRExpressionEvalException;
import net.sf.jasperreports.engine.type.WhenResourceMissingTypeEnum;

/**
 * Builtin expression that evaluates to a constant value.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id: BuiltinExpressionEvaluator.java 4347 2011-05-26 11:13:03Z shertage $
 */
public class ConstantBuiltinExpression implements BuiltinExpressionEvaluator
{

	private final Object value;

	/**
	 * Creates an expression evaluator for a specified value.
	 * 
	 * @param value the value
	 */
	public ConstantBuiltinExpression(Object value)
	{
		this.value = value;
	}
	
	public <T, U, V> void init(Map<String, T> parametersMap,
			Map<String, U> fieldsMap, Map<String, V> variablesMap,
			WhenResourceMissingTypeEnum resourceMissingType) throws JRException
	{
		// NOP
	}

	public Object evaluate() throws JRExpressionEvalException
	{
		return value;
	}

	public Object evaluateOld() throws JRExpressionEvalException
	{
		return value;
	}

	public Object evaluateEstimated() throws JRExpressionEvalException
	{
		return value;
	}

}
