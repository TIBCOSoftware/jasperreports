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
package net.sf.jasperreports.components.html;

import java.awt.Dimension;

import net.sf.jasperreports.engine.JRComponentElement;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRGenericElementType;
import net.sf.jasperreports.engine.JRGenericPrintElement;
import net.sf.jasperreports.engine.JRPrintElement;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.component.BaseFillComponent;
import net.sf.jasperreports.engine.component.FillPrepareResult;
import net.sf.jasperreports.engine.fill.JRFillCloneFactory;
import net.sf.jasperreports.engine.fill.JRFillCloneable;
import net.sf.jasperreports.engine.fill.JRTemplateGenericElement;
import net.sf.jasperreports.engine.fill.JRTemplateGenericPrintElement;
import net.sf.jasperreports.engine.type.EvaluationTimeEnum;
import net.sf.jasperreports.engine.type.ScaleImageEnum;
import net.sf.jasperreports.engine.util.HtmlPrintElement;
import net.sf.jasperreports.engine.util.HtmlPrintElementUtils;
import net.sf.jasperreports.extensions.HtmlElementHandlerBundle;

/**
 * 
 * @author Narcis Marcu (narcism@users.sourceforge.net)
 */
public class HtmlComponentFill extends BaseFillComponent {

	public static final JRGenericElementType HTML_COMPONENT_PRINT_TYPE = 
		new JRGenericElementType(HtmlElementHandlerBundle.NAMESPACE, HtmlElementHandlerBundle.NAME);
	
	private final HtmlComponent htmlComponent;
	
	private String htmlContent;
	
	private	JRTemplateGenericElement template;
	
	private JRTemplateGenericPrintElement printElement;
	
	private boolean hasOverflowed;

	
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
			hasOverflowed = false;
			evaluateHtmlComponent(evaluation);
		}
	}
	
	protected void evaluateHtmlComponent(byte evaluation) throws JRException 
	{
		htmlContent = (String) fillContext.evaluate( htmlComponent.getHtmlContentExpression(), evaluation);
	}
	
	
	public JRPrintElement fill()
	{
		printElement.setY(fillContext.getElementPrintY());
		return printElement;
	}

	public FillPrepareResult prepare(int availableHeight)
	{
		FillPrepareResult result = null;
		
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
		
		printElement = new JRTemplateGenericPrintElement(template, printElementOriginator);
		printElement.setX(element.getX());

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
		
		Dimension realSize = computeSizeOfPrintElement(printElement);
		int realHeight = realSize.height;
		int realWidth = realSize.width;
		int imageWidth = realWidth;
		int imageHeight = realHeight;
		
		if (htmlComponent.getScaleType() == ScaleImageEnum.REAL_SIZE || htmlComponent.getScaleType() == ScaleImageEnum.REAL_HEIGHT) {
			
			if (realWidth >  element.getWidth()) {
				double wRatio = ((double) element.getWidth()) / realWidth;
				imageHeight = (int) (wRatio * realHeight);
				imageWidth = element.getWidth();
			}
			int printElementHeight = Math.max(imageHeight, element.getHeight());
			
			if (imageHeight <= availableHeight) {
				result = FillPrepareResult.printStretch(imageHeight, false);
			} else {
				if (hasOverflowed) {
					result = FillPrepareResult.printStretch(availableHeight, false);
					
					if (htmlComponent.getScaleType() == ScaleImageEnum.REAL_SIZE) {
						printElement.setWidth(imageWidth);
					} else {
						printElement.setWidth(element.getWidth());
					}
					
					printElement.setHeight(availableHeight);
					printElement.setParameterValue(HtmlPrintElement.BUILTIN_PARAMETER_HAS_OVERFLOWED, Boolean.TRUE);
				} else {
					result = FillPrepareResult.noPrintOverflow(printElementHeight);
					hasOverflowed = true;
				}
			}
			
		} else {
			result = FillPrepareResult.PRINT_NO_STRETCH;
		}
		return result;
	}
	
	private Dimension computeSizeOfPrintElement(JRTemplateGenericPrintElement printElement) {
		HtmlPrintElement htmlPrintElement = null; 
		try{
			htmlPrintElement = HtmlPrintElementUtils.getHtmlPrintElement();
		} catch (JRException e) {
			throw new JRRuntimeException(e);
		}
		return htmlPrintElement.getComputedSize(printElement);
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
		printElement.setParameterValue(HtmlPrintElement.PARAMETER_SCALE_TYPE, htmlComponent.getScaleType().getName());
		printElement.setParameterValue(HtmlPrintElement.PARAMETER_HORIZONTAL_ALIGN, htmlComponent.getHorizontalImageAlign().getName());
		printElement.setParameterValue(HtmlPrintElement.PARAMETER_VERTICAL_ALIGN, htmlComponent.getVerticalImageAlign().getName());
		printElement.setParameterValue(HtmlPrintElement.PARAMETER_CLIP_ON_OVERFLOW, htmlComponent.getClipOnOverflow());
	}

}
