/*
 * ============================================================================
 * GNU Lesser General Public License
 * ============================================================================
 *
 * JasperReports - Free Java report-generating library.
 * Copyright (C) 2001-2006 JasperSoft Corporation http://www.jaspersoft.com
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
package net.sf.jasperreports.engine.fill;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRHyperlink;
import net.sf.jasperreports.engine.JRHyperlinkParameter;
import net.sf.jasperreports.engine.JRPrintHyperlinkParameter;
import net.sf.jasperreports.engine.JRPrintHyperlinkParameters;


/**
 * Utility class used to evaluate custom hyperlink parameters.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public class JRFillHyperlinkHelper
{

	/**
	 * Evaluates a list of hyperlink parameters and produces a hyperlink paramters set
	 * that can be associated with a print element.
	 *  
	 * @param hyperlink the hyperlink instance
	 * @param expressionEvaluator the expression evaluator to use for evaluation parameter value
	 * @param evaluationType the evaluation type
	 * @return a print hyperlink paramters set
	 * @throws JRException
	 */
	public static JRPrintHyperlinkParameters evaluateHyperlinkParameters(
			JRHyperlink hyperlink, 
			JRFillExpressionEvaluator expressionEvaluator,
			byte evaluationType) throws JRException
	{
		JRHyperlinkParameter[] hyperlinkParameters = hyperlink.getHyperlinkParameters();
		JRPrintHyperlinkParameters printParameters;
		if (hyperlinkParameters == null)
		{
			printParameters = null;
		}
		else
		{
			printParameters = new JRPrintHyperlinkParameters();
			for (int i = 0; i < hyperlinkParameters.length; i++)
			{
				JRHyperlinkParameter hyperlinkParameter = hyperlinkParameters[i];
				JRExpression valueExpression = hyperlinkParameter.getValueExpression();
				Class valueClass;
				Object value;
				if (valueExpression == null)
				{
					value = null;
					valueClass = Object.class;
				}
				else
				{
					value = expressionEvaluator.evaluate(valueExpression, evaluationType);
					valueClass = valueExpression.getValueClass();
				}
				
				JRPrintHyperlinkParameter printParam = new JRPrintHyperlinkParameter(hyperlinkParameter.getName(), valueClass.getName(), value);
				printParameters.addParameter(printParam);
			}
		}
		return printParameters;
	}
}
