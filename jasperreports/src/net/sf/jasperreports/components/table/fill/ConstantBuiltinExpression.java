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
package net.sf.jasperreports.components.table.fill;

import java.util.Map;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.fill.DatasetExpressionEvaluator;
import net.sf.jasperreports.engine.fill.JRExpressionEvalException;
import net.sf.jasperreports.engine.fill.JRFillField;
import net.sf.jasperreports.engine.fill.JRFillParameter;
import net.sf.jasperreports.engine.fill.JRFillVariable;
import net.sf.jasperreports.engine.type.WhenResourceMissingTypeEnum;

/**
 * Builtin expression that evaluates to a constant value.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
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
	
	public void init(Map<String, JRFillParameter> parametersMap,
			Map<String, JRFillField> fieldsMap, 
			Map<String, JRFillVariable> variablesMap,
			WhenResourceMissingTypeEnum resourceMissingType) throws JRException
	{
		// NOP
	}

	public Object evaluate(DatasetExpressionEvaluator evaluator) throws JRExpressionEvalException
	{
		return value;
	}

	public Object evaluateOld(DatasetExpressionEvaluator evaluator) throws JRExpressionEvalException
	{
		return value;
	}

	public Object evaluateEstimated(DatasetExpressionEvaluator evaluator) throws JRExpressionEvalException
	{
		return value;
	}

}
