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
package net.sf.jasperreports.components.iconlabel;

import net.sf.jasperreports.engine.JRComponentElement;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRGenericElementType;
import net.sf.jasperreports.engine.JRGenericPrintElement;
import net.sf.jasperreports.engine.JRPrintElement;
import net.sf.jasperreports.engine.JRPrintText;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.base.JRBaseGenericPrintElement;
import net.sf.jasperreports.engine.component.BaseFillComponent;
import net.sf.jasperreports.engine.component.FillPrepareResult;
import net.sf.jasperreports.engine.fill.JRFillCloneFactory;
import net.sf.jasperreports.engine.fill.JRFillCloneable;
import net.sf.jasperreports.engine.fill.JRFillObjectFactory;
import net.sf.jasperreports.engine.fill.JRFillTextField;
import net.sf.jasperreports.engine.fill.JRTemplateGenericElement;
import net.sf.jasperreports.engine.fill.JRTemplateGenericPrintElement;

/**
 * 
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id: TextInputComponentFill.java 5922 2013-02-19 11:03:27Z teodord $
 */
public class IconLabelComponentFill extends BaseFillComponent {

	private final IconLabelComponent iconLabelComponent;

	private final JRFillTextField labelTextField;
	private final JRFillTextField iconTextField;
	
	private	JRTemplateGenericElement template;
	private JRTemplateGenericPrintElement printElement;
	private JRPrintText labelPrintText;
	private JRPrintText iconPrintText;

	public IconLabelComponentFill(IconLabelComponent iconLabelComponent, JRFillObjectFactory factory)
	{
		this.iconLabelComponent = iconLabelComponent;
		this.labelTextField = (JRFillTextField)factory.getVisitResult(iconLabelComponent.getLabelTextField());
		this.iconTextField = (JRFillTextField)factory.getVisitResult(iconLabelComponent.getIconTextField());
	}
	
	public IconLabelComponentFill(IconLabelComponent iconLabelComponent, JRFillCloneFactory factory)
	{
		this.iconLabelComponent = iconLabelComponent;
		this.labelTextField = null;//FIXMEINPUT (JRFillTextField)factory.getVisitResult(iconLabelComponent.getTextField());
		this.iconTextField = null;//FIXMEINPUT (JRFillTextField)factory.getVisitResult(iconLabelComponent.getTextField());
	}
	
	protected IconLabelComponent getIconLabelComponent()
	{
		return iconLabelComponent;
	}
	
	public void evaluate(byte evaluation) throws JRException
	{
		labelTextField.evaluate(evaluation);
		iconTextField.evaluate(evaluation);
	}
	
	public JRPrintElement fill()
	{
		try
		{
			labelPrintText = (JRPrintText)labelTextField.fill();
		}
		catch (JRException e)
		{
			throw new JRRuntimeException(e);
		}
		
		printElement.setY(fillContext.getElementPrintY());
		printElement.setHeight(labelPrintText.getHeight());
		
		if (
			iconLabelComponent.getLabelFill() != ContainerFillEnum.HORIZONTAL
			&& iconLabelComponent.getLabelFill() != ContainerFillEnum.BOTH
			)
		{
			labelPrintText.setWidth((int)labelTextField.getTextWidth() + 1);
		}
		
		try
		{
			iconPrintText = (JRPrintText)iconTextField.fill();
		}
		catch (JRException e)
		{
			throw new JRRuntimeException(e);
		}
		
		iconPrintText.setY(labelPrintText.getY());
		iconPrintText.setX(labelPrintText.getX() + labelPrintText.getWidth());
		iconPrintText.setWidth((int)iconTextField.getTextWidth());//FIXMESORT take care of padding

		copy(printElement);
		
		return printElement;
	}

	public FillPrepareResult prepare(int availableHeight)
	{
		iconTextField.setWidth(iconLabelComponent.getContext().getComponentElement().getWidth());
		
		try
		{
			iconTextField.prepare(availableHeight, fillContext.getFillContainerContext().isCurrentOverflow());
		}
		catch (JRException e)
		{
			throw new JRRuntimeException(e);
		}
		
		labelTextField.setWidth(iconLabelComponent.getContext().getComponentElement().getWidth() - (int)iconTextField.getTextWidth());

		try
		{
			labelTextField.prepare(availableHeight, fillContext.getFillContainerContext().isCurrentOverflow());
		}
		catch (JRException e)
		{
			throw new JRRuntimeException(e);
		}
		
		FillPrepareResult result = null;
		
		JRComponentElement element = fillContext.getComponentElement();
		if (template == null) 
		{
			template = new JRTemplateGenericElement(
					fillContext.getElementOrigin(), 
					fillContext.getDefaultStyleProvider(),
					IconLabelElement.ICONLABEL_ELEMENT_TYPE);
		
			template.setMode(iconLabelComponent.getContext().getComponentElement().getModeValue());
			template.setBackcolor(iconLabelComponent.getContext().getComponentElement().getBackcolor());
			template.setForecolor(iconLabelComponent.getContext().getComponentElement().getForecolor());
			
			template = deduplicate(template);
		}
		
		printElement = new JRTemplateGenericPrintElement(template, elementId);
		printElement.setUUID(element.getUUID());
		printElement.setX(element.getX());

		printElement.setWidth(element.getWidth());
		printElement.setHeight(element.getHeight());
		
		//printElement.setParameterValue(IconLabelElement.PARAMETER_MULTI_LINE, iconLabelComponent.isMultiLine());

//		copy(printElement);
		
		result = FillPrepareResult.printStretch(labelTextField.getStretchHeight(), false);
		return result;
	}
	
	public JRFillCloneable createClone(JRFillCloneFactory factory)
	{
		throw new UnsupportedOperationException();
	}

	public void evaluateDelayedElement(JRPrintElement element, byte evaluation) throws JRException
	{
		evaluate(evaluation);
		copy((JRGenericPrintElement) element);
	}

	protected void copy(JRGenericPrintElement printElement)
	{
		printElement.setParameterValue(IconLabelElement.PARAMETER_LABEL_TEXT_ELEMENT, labelPrintText);

		JRBaseGenericPrintElement iconGenericElement = new JRBaseGenericPrintElement(labelPrintText.getDefaultStyleProvider());
		iconGenericElement.setGenericType(new JRGenericElementType("http://jasperreports.sourceforge.net/jasperreports/pictonic", "pictonic"));//FIXMEICONLABEL use constant
		iconGenericElement.setX(iconPrintText.getX());
		iconGenericElement.setY(iconPrintText.getY());
		iconGenericElement.setWidth(iconPrintText.getWidth());
		iconGenericElement.setHeight(iconPrintText.getHeight());
		iconGenericElement.setParameterValue("iconTextElement", iconPrintText);//FIXMEICONLABEL use constant
		
		printElement.setParameterValue(IconLabelElement.PARAMETER_ICON_GENERIC_ELEMENT, iconGenericElement);
	}
}
