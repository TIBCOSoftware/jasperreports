/*
 * ============================================================================
 * GNU Lesser General Public License
 * ============================================================================
 *
 * JasperReports - Free Java report-generating library.
 * Copyright (C) 2001-2005 JasperSoft Corporation http://www.jaspersoft.com
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
package net.sf.jasperreports.crosstabs.fill;

import java.util.Map;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.fill.JREvaluator;
import net.sf.jasperreports.engine.fill.JRFillExpressionEvaluator;
import net.sf.jasperreports.engine.fill.JRFillParameter;

/**
 * Expression evaluator used for crosstabs at fill time.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public class JRCrosstabExpressionEvaluator implements JRFillExpressionEvaluator
{
	private final JREvaluator evaluator;
	
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

	
	public void init(Map parametersMap, Map variablesMap, JRFillParameter resourceBundleParam, byte whenResourceMissingType) throws JRException
	{
		evaluator.init(parametersMap, null, variablesMap, resourceBundleParam, whenResourceMissingType);
	}
}
