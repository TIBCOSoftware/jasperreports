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
package net.sf.jasperreports.engine.part;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRPart;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.component.ComponentKey;
import net.sf.jasperreports.engine.fill.JRFillExpressionEvaluator;
import net.sf.jasperreports.engine.fill.JRFillObjectFactory;
import net.sf.jasperreports.engine.fill.PartReportFiller;

/**
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 */
public class FillPart
{

	private JRPart reportPart;
	private JRFillExpressionEvaluator expressionEvaluator;
	private PartReportFiller reportFiller;
	
	private PartFillComponent fillComponent;
	private String partName;

	public FillPart(JRPart part, JRFillObjectFactory fillFactory)
	{
		this.reportPart = part;
		this.expressionEvaluator = fillFactory.getExpressionEvaluator();
		reportFiller = (PartReportFiller) fillFactory.getReportFiller();//FIXMEBOOK
		
		ComponentKey componentKey = part.getComponentKey();
		PartComponent component = part.getComponent();
		
		JasperReportsContext jasperReportsContext = fillFactory.getReportFiller().getJasperReportsContext();
		PartComponentsEnvironment partsEnv = PartComponentsEnvironment.getInstance(jasperReportsContext);
		PartComponentManager componentManager = partsEnv.getManager(componentKey);
		PartComponentFillFactory componentFactory = componentManager.getComponentFillFactory(jasperReportsContext);
		
		//fillFactory.trackDatasetRuns();FIXMEBOOK
		this.fillComponent = componentFactory.toFillComponent(component, fillFactory);
		fillComponent.initialize(new Context());
	}
	
	public void fill(byte evaluation, PartPrintOutput output) throws JRException
	{
		boolean toPrint = evaluatePrintWhenExpression(evaluation);
		if (!toPrint)
		{
			return;
		}
		
		evaluatePartNameExpression(evaluation);
		fillComponent.evaluate(evaluation);
		fillComponent.fill(output);
	}

	protected boolean evaluatePrintWhenExpression(byte evaluation) throws JRException
	{
		JRExpression expression = reportPart.getPrintWhenExpression();
		boolean result;
		if (expression == null)
		{
			result = true;
		}
		else
		{
			Boolean expressionResult = (Boolean) expressionEvaluator.evaluate(expression, evaluation);
			result = expressionResult != null && expressionResult;
		}
		return result;
	}

	protected void evaluatePartNameExpression(byte evaluation) throws JRException
	{
		JRExpression expression = reportPart.getPartNameExpression();
		partName = expression == null ? null : (String) expressionEvaluator.evaluate(expression, evaluation);
	}

	public PartEvaluationTime getEvaluationTime()
	{
		PartEvaluationTime evaluationTime = reportPart.getEvaluationTime();
		return evaluationTime == null ? StandardPartEvaluationTime.EVALUATION_NOW : evaluationTime;
	}

	public String getPartName()
	{
		return partName;
	}
	
	protected class Context implements PartFillContext
	{
		@Override
		public JRPart getPart()
		{
			return reportPart;
		}

		@Override
		public FillPart getFillPart()
		{
			return FillPart.this;
		}

		@Override
		public PartReportFiller getFiller()
		{
			return reportFiller;
		}

		@Override
		public Object evaluate(JRExpression expression, byte evaluation) throws JRException
		{
			return reportFiller.evaluateExpression(expression, evaluation);
		}
	}
}
