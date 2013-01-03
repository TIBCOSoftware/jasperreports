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
package net.sf.jasperreports.components.barbecue;

import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRExpressionCollector;
import net.sf.jasperreports.engine.base.JRBaseObjectFactory;
import net.sf.jasperreports.engine.component.Component;
import net.sf.jasperreports.engine.component.ComponentCompiler;
import net.sf.jasperreports.engine.design.JRVerifier;
import net.sf.jasperreports.engine.type.EvaluationTimeEnum;

/**
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public class BarbecueCompiler implements ComponentCompiler
{
	
	public void collectExpressions(Component component, JRExpressionCollector collector)
	{
		BarbecueComponent barcode = (BarbecueComponent) component;
		collector.addExpression(barcode.getCodeExpression());
		collector.addExpression(barcode.getApplicationIdentifierExpression());
	}

	public Component toCompiledComponent(Component component,
			JRBaseObjectFactory baseFactory)
	{
		BarbecueComponent barcode = (BarbecueComponent) component;
		return new StandardBarbecueComponent(barcode, baseFactory);
	}

	public void verify(Component component, JRVerifier verifier)
	{
		BarbecueComponent barcode = (BarbecueComponent) component;
		
		String type = barcode.getType();
		if (type == null)
		{
			verifier.addBrokenRule("No barcode type set", barcode);
		}
		
		JRExpression codeExpression = barcode.getCodeExpression();
		if (codeExpression == null)
		{
			verifier.addBrokenRule("Barcode expression is null", barcode);
		}
		
		EvaluationTimeEnum evaluationTime = barcode.getEvaluationTimeValue();
		if (evaluationTime == EvaluationTimeEnum.AUTO)
		{
			verifier.addBrokenRule("Auto evaluation time is not supported for barcodes", barcode);
		}
		else if (evaluationTime == EvaluationTimeEnum.GROUP)
		{
			String evaluationGroup = barcode.getEvaluationGroup();
			if (evaluationGroup == null || evaluationGroup.length() == 0)
			{
				verifier.addBrokenRule("No evaluation group set for barcode", barcode);
			}
			else if (!verifier.getReportDesign().getGroupsMap().containsKey(evaluationGroup))
			{
				verifier.addBrokenRule("Barcode evalution group \"" 
						+ evaluationGroup + " not found", barcode);
			}
		}
	}

}
