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
package net.sf.jasperreports.forms.textinput;

import net.sf.jasperreports.engine.JRComponentElement;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRGenericPrintElement;
import net.sf.jasperreports.engine.JRPrintElement;
import net.sf.jasperreports.engine.JRPrintText;
import net.sf.jasperreports.engine.JRRuntimeException;
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
 */
public class TextInputComponentFill extends BaseFillComponent {

	private final TextInputComponent textInputComponent;

	private final JRFillTextField textField;
	
	private	JRTemplateGenericElement template;
	private JRTemplateGenericPrintElement printElement;
	private JRPrintText printText;
	
	public TextInputComponentFill(TextInputComponent textInputComponent, JRFillObjectFactory factory)
	{
		this.textInputComponent = textInputComponent;
		this.textField = (JRFillTextField)factory.getVisitResult(textInputComponent.getTextField());
	}
	
	public TextInputComponentFill(TextInputComponent textInputComponent, JRFillCloneFactory factory)
	{
		this.textInputComponent = textInputComponent;
		this.textField = null;//FIXMEINPUT (JRFillTextField)factory.getVisitResult(textInputComponent.getTextField());
	}
	
	protected TextInputComponent getTextInputComponent()
	{
		return textInputComponent;
	}
	
	public void evaluate(byte evaluation) throws JRException
	{
		textField.evaluate(evaluation);
	}
	
	public JRPrintElement fill()
	{
		try
		{
			printText = (JRPrintText)textField.fill();
		}
		catch (JRException e)
		{
			throw new JRRuntimeException(e);
		}
		
		printElement.setY(fillContext.getElementPrintY());
		printElement.setHeight(printText.getHeight());

		copy(printElement);
		
		return printElement;
	}

	public FillPrepareResult prepare(int availableHeight)
	{
		try
		{
			textField.prepare(availableHeight, fillContext.getFillContainerContext().isCurrentOverflow());
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
					TextInputElement.TEXT_INPUT_ELEMENT_TYPE);
		
			template.setMode(textInputComponent.getContext().getComponentElement().getModeValue());
			template.setBackcolor(textInputComponent.getContext().getComponentElement().getBackcolor());
			template.setForecolor(textInputComponent.getContext().getComponentElement().getForecolor());
			
			template = deduplicate(template);
		}
		
		printElement = new JRTemplateGenericPrintElement(template, printElementOriginator);
		printElement.setUUID(element.getUUID());
		printElement.setX(element.getX());

		printElement.setWidth(element.getWidth());
		printElement.setHeight(element.getHeight());
		
		printElement.setParameterValue(TextInputElement.PARAMETER_MULTI_LINE, textInputComponent.isMultiLine());

//		copy(printElement);
		
		result = FillPrepareResult.printStretch(textField.getStretchHeight(), false);
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
		printElement.setParameterValue(TextInputElement.PARAMETER_PRINT_TEXT_ELEMENT, printText);//FIXMEINPUT
//		printElement.setParameterValue(SortElement.PARAMETER_SORT_COLUMN_NAME, sortComponent.getSortFieldName());
//		printElement.setParameterValue(SortElement.PARAMETER_SORT_COLUMN_TYPE, sortComponent.getSortFieldType().getName());
//		printElement.setParameterValue(SortElement.PARAMETER_SORT_HANDLER_COLOR, sortComponent.getHandlerColor());
//		printElement.setParameterValue(SortElement.PARAMETER_SORT_HANDLER_FONT, sortComponent.getSymbolFont());
//		
//		if (sortComponent.getSymbolFont() != null ) {
//			printElement.setParameterValue(SortElement.PARAMETER_SORT_HANDLER_FONT_SIZE, String.valueOf(sortComponent.getSymbolFont().getFontSize()));
//		} 
//		if (sortComponent.getHandlerHorizontalAlign() != null) 
//		{
//			printElement.setParameterValue(SortElement.PARAMETER_SORT_HANDLER_HORIZONTAL_ALIGN, sortComponent.getHandlerHorizontalAlign().getName());
//		}
//		if (sortComponent.getHandlerVerticalAlign() != null) 
//		{
//			printElement.setParameterValue(SortElement.PARAMETER_SORT_HANDLER_VERTICAL_ALIGN, sortComponent.getHandlerVerticalAlign().getName());
//		}
//		
//		FilterTypesEnum filterType = getFilterType();
//		if (filterType != null)
//		{
//			printElement.getPropertiesMap().setProperty(SortElement.PROPERTY_FILTER_TYPE, filterType.getName());
//		}
//		
//		String datasetName = JRAbstractCompiler.getUnitName(
//				fillContext.getFiller().getJasperReport(), fillContext.getFillDataset());
//		printElement.getPropertiesMap().setProperty(SortElement.PROPERTY_DATASET_RUN, datasetName);
	}
}
