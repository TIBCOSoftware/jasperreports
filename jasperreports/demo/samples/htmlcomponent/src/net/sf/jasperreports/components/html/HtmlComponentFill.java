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
package net.sf.jasperreports.components.html;

import net.sf.jasperreports.engine.JRComponentElement;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRGenericElementType;
import net.sf.jasperreports.engine.JRGenericPrintElement;
import net.sf.jasperreports.engine.JRPrintElement;
import net.sf.jasperreports.engine.component.BaseFillComponent;
import net.sf.jasperreports.engine.component.FillPrepareResult;
import net.sf.jasperreports.engine.fill.JRFillCloneFactory;
import net.sf.jasperreports.engine.fill.JRFillCloneable;
import net.sf.jasperreports.engine.fill.JRTemplateGenericElement;
import net.sf.jasperreports.engine.fill.JRTemplateGenericPrintElement;
import net.sf.jasperreports.engine.type.EvaluationTimeEnum;
import net.sf.jasperreports.engine.util.HtmlPrintElement;
import net.sf.jasperreports.extensions.HtmlElementHandlerBundle;

/**
 * 
 * @author Narcis Marcu (narcism@users.sourceforge.net)
 * @version $Id$
 */
public class HtmlComponentFill extends BaseFillComponent {

	public static final JRGenericElementType HTML_COMPONENT_PRINT_TYPE = 
		new JRGenericElementType(HtmlElementHandlerBundle.NAMESPACE, HtmlElementHandlerBundle.NAME);
	
	private final HtmlComponent htmlComponent;
	
	private String htmlContent;
	
	JRTemplateGenericElement template;

	public HtmlComponentFill(HtmlComponent htmlComponent)
	{
		this.htmlComponent = htmlComponent;
	}
	
	protected HtmlComponent getHtmlComponent()
	{
		return htmlComponent;
	}
	
	protected boolean isEvaluateNow()
	{
		return htmlComponent.getEvaluationTime() == EvaluationTimeEnum.NOW;
	}
	
	public void evaluate(byte evaluation) throws JRException
	{
		if (isEvaluateNow())
		{
			evaluateHtmlComponent(evaluation);
		}
	}
	
	protected void evaluateHtmlComponent(byte evaluation) throws JRException 
	{
		htmlContent = (String) fillContext.evaluate( htmlComponent.getHtmlContentExpression(), evaluation);
	}
	
	public JRPrintElement fill()
	{
		JRComponentElement element = fillContext.getComponentElement();
		
		if (template == null) {
			template = new JRTemplateGenericElement(
					fillContext.getElementOrigin(), 
					fillContext.getDefaultStyleProvider(),
					HTML_COMPONENT_PRINT_TYPE);
		
			template.setMode(htmlComponent.getContext().getComponentElement().getModeValue());
			template.setBackcolor(htmlComponent.getContext().getComponentElement().getBackcolor());
			template.setForecolor(htmlComponent.getContext().getComponentElement().getForecolor());
		}
		
		JRTemplateGenericPrintElement printElement = new JRTemplateGenericPrintElement(template);
		printElement.setX(element.getX());
		printElement.setY(fillContext.getElementPrintY());
		printElement.setWidth(element.getWidth());
		printElement.setHeight(element.getHeight());
		
		if (isEvaluateNow())
		{
			copy(printElement);
		}
		else
		{
			fillContext.registerDelayedEvaluation(printElement, 
					htmlComponent.getEvaluationTime(), null);
		}
		
		return printElement;
		
	}

	public FillPrepareResult prepare(int availableHeight)
	{
		return FillPrepareResult.PRINT_NO_STRETCH;
	}

	public JRFillCloneable createClone(JRFillCloneFactory factory)
	{
		throw new UnsupportedOperationException();
	}

	public void evaluateDelayedElement(JRPrintElement element, byte evaluation) throws JRException
	{
		evaluateHtmlComponent(evaluation);
		copy((JRGenericPrintElement) element);
	}

	protected void copy(JRGenericPrintElement printElement)
	{
		printElement.setParameterValue(HtmlPrintElement.PARAMETER_HTML_CONTENT, htmlContent);
		printElement.setParameterValue(HtmlPrintElement.PARAMETER_SCALE_TYPE, htmlComponent.getHtmlScaleType().getName());
		printElement.setParameterValue(HtmlPrintElement.PARAMETER_HORIZONTAL_ALIGN, htmlComponent.getHorizontalAlign().getName());
		printElement.setParameterValue(HtmlPrintElement.PARAMETER_VERTICAL_ALIGN, htmlComponent.getVerticalAlign().getName());
		printElement.setParameterValue(HtmlPrintElement.PARAMETER_WIDTH, htmlComponent.getHtmlWidth());
		printElement.setParameterValue(HtmlPrintElement.PARAMETER_HEIGHT, htmlComponent.getHtmlHeight());
	}

}
