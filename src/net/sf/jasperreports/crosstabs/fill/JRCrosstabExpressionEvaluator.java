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
package net.sf.jasperreports.crosstabs.fill;

import java.util.Map;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.fill.JREvaluator;
import net.sf.jasperreports.engine.fill.JRFillDataset;
import net.sf.jasperreports.engine.fill.JRFillExpressionEvaluator;
import net.sf.jasperreports.engine.fill.JRFillParameter;
import net.sf.jasperreports.engine.fill.JRFillVariable;
import net.sf.jasperreports.engine.type.WhenResourceMissingTypeEnum;

/**
 * Expression evaluator used for crosstabs at fill time.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public class JRCrosstabExpressionEvaluator implements JRFillExpressionEvaluator
{
	private final JREvaluator evaluator;
	private JRFillDataset dataset;


	public JRCrosstabExpressionEvaluator(JREvaluator evaluator)
	{
		this.evaluator = evaluator;
	}
	
	
	public Object evaluate(JRExpression expression, byte evaluationType) throws JRException
	{
		if (evaluationType != JRExpression.EVALUATION_DEFAULT)
		{
			throw new JRException("The crosstab evaluator doesn't support old or estimated expression evaluation.");
		}
		
		return evaluator.evaluate(expression);
	}

	
	public void init(Map<String, JRFillParameter> parametersMap, 
			Map<String, JRFillVariable> variablesMap, WhenResourceMissingTypeEnum whenResourceMissingType) throws JRException
	{
		evaluator.init(parametersMap, null, variablesMap, whenResourceMissingType);
	}
	
	public void setFillDataset(JRFillDataset dataset)
	{
		this.dataset = dataset;
	}

	public JRFillDataset getFillDataset()
	{
		return dataset;
	}
}
