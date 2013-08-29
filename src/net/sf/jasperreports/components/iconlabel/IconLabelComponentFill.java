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
import net.sf.jasperreports.engine.JRGenericPrintElement;
import net.sf.jasperreports.engine.JRPrintElement;
import net.sf.jasperreports.engine.JRPrintText;
import net.sf.jasperreports.engine.JRPropertiesUtil;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.component.BaseFillComponent;
import net.sf.jasperreports.engine.component.FillPrepareResult;
import net.sf.jasperreports.engine.component.StretchableFillComponent;
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
public class IconLabelComponentFill extends BaseFillComponent implements StretchableFillComponent
{
	private final IconLabelComponent iconLabelComponent;

	private final JRFillTextField labelTextField;
	private final JRFillTextField iconTextField;
	
	private	JRTemplateGenericElement template;
	private JRTemplateGenericPrintElement printElement;
	private JRPrintText labelPrintText;
	private JRPrintText iconPrintText;
	
	private int stretchHeight;

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
		printElement.setHeight(stretchHeight);
		
		if (
			iconLabelComponent.getLabelFill() != ContainerFillEnum.HORIZONTAL
			&& iconLabelComponent.getLabelFill() != ContainerFillEnum.BOTH
			)
		{
			int calculatedLabelWidth =
				(int)labelTextField.getTextWidth() 
				+ labelTextField.getLineBox().getLeftPadding() 
				+ labelTextField.getLineBox().getRightPadding() 
				+ 3;//we do +3 to avoid text wrap in html (+1 was enough for pdf)
			labelPrintText.setWidth(Math.min(labelTextField.getWidth(), calculatedLabelWidth));//for some reason, calculated text width seems to be larger then available text width
		}
		
		if (
			iconLabelComponent.getLabelFill() == ContainerFillEnum.VERTICAL
			|| iconLabelComponent.getLabelFill() == ContainerFillEnum.BOTH
			)
		{
			labelPrintText.setHeight(
				Math.max(
					labelTextField.getHeight(), 
					stretchHeight 
						- iconLabelComponent.getLineBox().getTopPadding()
						- iconLabelComponent.getLineBox().getBottomPadding()
					)
				);
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
		iconPrintText.setWidth(
			(int)iconTextField.getTextWidth()
			+ iconTextField.getLineBox().getLeftPadding() 
			+ iconTextField.getLineBox().getRightPadding() 
			);
		
		switch (iconLabelComponent.getHorizontalAlign())
		{
			case LEFT :
			case JUSTIFIED :
			{
				if (iconLabelComponent.getIconPosition() == IconPositionEnum.START)
				{
					labelPrintText.setX(iconPrintText.getWidth());
					iconPrintText.setX(0);
				}
				else
				{
					labelPrintText.setX(0);
					iconPrintText.setX(labelPrintText.getWidth());
					//iconPrintText.setX(labelPrintText.getX() + labelPrintText.getWidth());
				}
				break;
			}
			case CENTER :
			{
				if (iconLabelComponent.getIconPosition() == IconPositionEnum.START)
				{
					iconPrintText.setX(
						(
						iconLabelComponent.getContext().getComponentElement().getWidth() 
						- iconLabelComponent.getLineBox().getLeftPadding()
						- iconLabelComponent.getLineBox().getRightPadding()
						- labelPrintText.getWidth() 
						- iconPrintText.getWidth()
						) / 2
						);
					labelPrintText.setX(iconPrintText.getX() + iconPrintText.getWidth());
				}
				else
				{
					labelPrintText.setX(
						(
						iconLabelComponent.getContext().getComponentElement().getWidth() 
						- iconLabelComponent.getLineBox().getLeftPadding()
						- iconLabelComponent.getLineBox().getRightPadding()
						- labelPrintText.getWidth() 
						- iconPrintText.getWidth()
						) / 2
						);
					iconPrintText.setX(labelPrintText.getX() + labelPrintText.getWidth());
				}
				break;
			}
			case RIGHT :
			{
				if (iconLabelComponent.getIconPosition() == IconPositionEnum.START)
				{
					labelPrintText.setX(
						iconLabelComponent.getContext().getComponentElement().getWidth()
						- iconLabelComponent.getLineBox().getLeftPadding()
						- iconLabelComponent.getLineBox().getRightPadding()
						- labelPrintText.getWidth()
						);
					iconPrintText.setX(labelPrintText.getX() - iconPrintText.getWidth());
				}
				else
				{
					iconPrintText.setX(
						iconLabelComponent.getContext().getComponentElement().getWidth() 
						- iconLabelComponent.getLineBox().getLeftPadding()
						- iconLabelComponent.getLineBox().getRightPadding()
						- iconPrintText.getWidth()
						);
					labelPrintText.setX(iconPrintText.getX() - labelPrintText.getWidth());
				}
				break;
			}
		}

		switch (iconLabelComponent.getVerticalAlign())
		{
			case TOP :
			case JUSTIFIED :
			{
				labelPrintText.setY(0);
				iconPrintText.setY(0);
				break;
			}
			case MIDDLE :
			{
				labelPrintText.setY(
					(
					stretchHeight
					- iconLabelComponent.getLineBox().getTopPadding()
					- iconLabelComponent.getLineBox().getBottomPadding()
					- labelPrintText.getHeight()
					) / 2
					);
				iconPrintText.setY(
					(
					stretchHeight
					- iconLabelComponent.getLineBox().getTopPadding()
					- iconLabelComponent.getLineBox().getBottomPadding()
					- iconPrintText.getHeight()
					) / 2
					);
				break;
			}
			case BOTTOM :
			{
				labelPrintText.setY(
					stretchHeight
					- iconLabelComponent.getLineBox().getTopPadding()
					- iconLabelComponent.getLineBox().getBottomPadding()
					- labelPrintText.getHeight()
					);
				iconPrintText.setY(
						stretchHeight
						- iconLabelComponent.getLineBox().getTopPadding()
						- iconLabelComponent.getLineBox().getBottomPadding()
						- iconPrintText.getHeight()
						);
				break;
			}
		}
		
		copy(printElement);
		
		return printElement;
	}

	public void setStretchHeight(int stretchHeight)
	{
		this.stretchHeight = stretchHeight;
	}
	
	public FillPrepareResult prepare(int availableHeight)
	{
		int textAvailableHeight = 
			availableHeight
			- iconLabelComponent.getLineBox().getTopPadding()
			- iconLabelComponent.getLineBox().getBottomPadding();
		
		iconTextField.setWidth(
			iconLabelComponent.getContext().getComponentElement().getWidth()
			- iconLabelComponent.getLineBox().getLeftPadding()
			- iconLabelComponent.getLineBox().getRightPadding()
			);
		
		try
		{
			iconTextField.prepare(textAvailableHeight, fillContext.getFillContainerContext().isCurrentOverflow());
		}
		catch (JRException e)
		{
			throw new JRRuntimeException(e);
		}
		
		labelTextField.setWidth(
			iconLabelComponent.getContext().getComponentElement().getWidth()
			- iconLabelComponent.getLineBox().getLeftPadding()
			- iconLabelComponent.getLineBox().getRightPadding()
			- (int)iconTextField.getTextWidth()
			- iconTextField.getLineBox().getLeftPadding()
			- iconTextField.getLineBox().getRightPadding()
			);

		try
		{
			labelTextField.prepare(textAvailableHeight, fillContext.getFillContainerContext().isCurrentOverflow());
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
		
			template.setStyle(iconLabelComponent.getContext().getComponentElement().getStyle());
			template.setMode(iconLabelComponent.getContext().getComponentElement().getModeValue());
			template.setBackcolor(iconLabelComponent.getContext().getComponentElement().getBackcolor());
			template.setForecolor(iconLabelComponent.getContext().getComponentElement().getForecolor());
			
			template = deduplicate(template);
		}
		
		printElement = new JRTemplateGenericPrintElement(template, elementId);
		printElement.setUUID(element.getUUID());
		printElement.setX(element.getX());

		printElement.setWidth(element.getWidth());
		stretchHeight = 
			Math.max(labelTextField.getStretchHeight(), iconTextField.getStretchHeight())
			+ iconLabelComponent.getLineBox().getTopPadding()
			+ iconLabelComponent.getLineBox().getBottomPadding();
		//printElement.setHeight(element.getHeight());
		printElement.setHeight(stretchHeight);
		JRPropertiesUtil.getInstance(fillContext.getFiller().getJasperReportsContext()).transferProperties(iconLabelComponent.getContext().getComponentElement(), printElement, JasperPrint.PROPERTIES_PRINT_TRANSFER_PREFIX);
		
		//printElement.setParameterValue(IconLabelElement.PARAMETER_MULTI_LINE, iconLabelComponent.isMultiLine());

//		copy(printElement);
		
		result = FillPrepareResult.printStretch(stretchHeight, false);
		//result = FillPrepareResult.printStretch(labelTextField.getStretchHeight(), false);
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
		printElement.setParameterValue(IconLabelElement.PARAMETER_LINE_BOX, iconLabelComponent.getLineBox().clone(null));
		printElement.setParameterValue(IconLabelElement.PARAMETER_LABEL_TEXT_ELEMENT, labelPrintText);
		printElement.setParameterValue(IconLabelElement.PARAMETER_ICON_TEXT_ELEMENT, iconPrintText);
	}
}
