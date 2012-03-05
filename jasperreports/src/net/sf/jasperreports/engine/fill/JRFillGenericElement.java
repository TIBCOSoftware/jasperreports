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
package net.sf.jasperreports.engine.fill;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRExpressionCollector;
import net.sf.jasperreports.engine.JRGenericElement;
import net.sf.jasperreports.engine.JRGenericElementParameter;
import net.sf.jasperreports.engine.JRGenericElementType;
import net.sf.jasperreports.engine.JRGenericPrintElement;
import net.sf.jasperreports.engine.JRPrintElement;
import net.sf.jasperreports.engine.JRVisitor;
import net.sf.jasperreports.engine.type.EvaluationTimeEnum;

/**
 * A {@link JRGenericElement} used during report fill.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public class JRFillGenericElement extends JRFillElement implements
		JRGenericElement
{
	
	private JRGenericElementParameter[] parameters;
	private Map<String,Object> parameterValues = new HashMap<String,Object>();
	
	public JRFillGenericElement(JRBaseFiller filler, JRGenericElement element,
			JRFillObjectFactory factory)
	{
		super(filler, element, factory);
		
		this.parameters = element.getParameters();
	}
	
	public JRFillGenericElement(JRFillGenericElement element,
			JRFillCloneFactory factory)
	{
		super(element, factory);
		
		this.parameters = element.parameters;
	}

	protected void evaluate(byte evaluation) throws JRException
	{
		initDelayedEvaluations();
		reset();
		evaluatePrintWhenExpression(evaluation);

		if (isPrintWhenExpressionNull() || isPrintWhenTrue())
		{
			if (isEvaluateNow())
			{
				evaluateElement(evaluation);
			}
		}
	}
	
	protected void collectDelayedEvaluations()
	{
		super.collectDelayedEvaluations();
		
		for (int i = 0; i < parameters.length; i++)
		{
			JRGenericElementParameter parameter = parameters[i];
			collectDelayedEvaluations(parameter.getValueExpression());
		}
	}

	protected void evaluateElement(byte evaluation) throws JRException
	{
		evaluateProperties(evaluation);
		
		parameterValues.clear();
		for (int i = 0; i < parameters.length; i++)
		{
			JRGenericElementParameter parameter = parameters[i];
			
			Object value = null;
			JRExpression valueExpression = parameter.getValueExpression();
			if (valueExpression != null)
			{
				value = evaluateExpression(valueExpression, evaluation);
			}
			
			if (value != null || !parameter.isSkipWhenEmpty())
			{
				parameterValues.put(parameter.getName(), value);
			}
		}
	}

	protected boolean prepare(int availableHeight, boolean isOverflow)
			throws JRException
	{
		boolean willOverflow = false;

		super.prepare(availableHeight, isOverflow);
		
		if (!isToPrint())
		{
			return willOverflow;
		}
		
		boolean isToPrint = true;
		boolean isReprinted = false;

		if (isOverflow && isAlreadyPrinted() && !isPrintWhenDetailOverflows())
		{
			isToPrint = false;
		}

		if (isToPrint && availableHeight < getRelativeY() + getHeight())
		{
			isToPrint = false;
			willOverflow = true;
		}

		if (isToPrint && isOverflow && isPrintWhenDetailOverflows()
				&& (isAlreadyPrinted() || !isPrintRepeatedValues()))
		{
			isReprinted = true;
		}
		
		setToPrint(isToPrint);
		setReprinted(isReprinted);
		
		return willOverflow;
	}

	protected JRPrintElement fill() throws JRException
	{
		JRTemplateGenericPrintElement printElement;
		if (isEvaluateAuto())
		{
			JRRecordedValuesGenericPrintElement recordedValuesElement = 
				new JRRecordedValuesGenericPrintElement(getTemplate(), elementId);
			copyBasicAttributes(recordedValuesElement);
			initDelayedEvaluationPrint(recordedValuesElement);
			printElement = recordedValuesElement;
		}
		else
		{
			printElement = new JRTemplateGenericPrintElement(getTemplate(), elementId);
			copyBasicAttributes(printElement);
			if (isEvaluateNow())
			{
				copy(printElement);
			}
			else
			{
				filler.addBoundElement(this, printElement, 
						getEvaluationTimeValue(), getEvaluationGroupName(), band);
			}
		}
		return printElement;
	}

	protected void copyBasicAttributes(JRGenericPrintElement printElement)
	{
		printElement.setX(this.getX());
		printElement.setY(this.getRelativeY());
		printElement.setWidth(getWidth());
		printElement.setHeight(this.getStretchHeight());
	}
	
	protected JRTemplateGenericElement getTemplate()
	{
		return (JRTemplateGenericElement) getElementTemplate();
	}

	protected JRTemplateElement createElementTemplate()
	{
		return new JRTemplateGenericElement(
				getElementOrigin(), 
				filler.getJasperPrint().getDefaultStyleProvider(), 
				this);
	}

	protected void copy(JRGenericPrintElement printElement)
	{
		for (Iterator<Map.Entry<String,Object>> it = parameterValues.entrySet().iterator(); it.hasNext();)
		{
			Map.Entry<String,Object> entry = it.next();
			String name = entry.getKey();
			Object value = entry.getValue();
			printElement.setParameterValue(name, value);
		}
		
		transferProperties(printElement);
	}

	protected void resolveElement(JRPrintElement element, byte evaluation)
			throws JRException
	{
		JRGenericPrintElement genericElement = (JRGenericPrintElement) element;
		evaluateElement(evaluation);
		copy(genericElement);
	}

	protected void rewind() throws JRException
	{
		// NOOP
	}

	public JRGenericElementType getGenericType()
	{
		return ((JRGenericElement) parent).getGenericType();
	}

	public JRGenericElementParameter[] getParameters()
	{
		return parameters;
	}

	public void collectExpressions(JRExpressionCollector collector)
	{
		collector.collect(this);
	}

	public void visit(JRVisitor visitor)
	{
		visitor.visitGenericElement(this);
	}

	public JRFillCloneable createClone(JRFillCloneFactory factory)
	{
		return new JRFillGenericElement(this, factory);
	}

	public EvaluationTimeEnum getEvaluationTimeValue()
	{
		return ((JRGenericElement) parent).getEvaluationTimeValue();
	}
	
	public String getEvaluationGroupName()
	{
		return ((JRGenericElement) parent).getEvaluationGroupName();
	}

}
