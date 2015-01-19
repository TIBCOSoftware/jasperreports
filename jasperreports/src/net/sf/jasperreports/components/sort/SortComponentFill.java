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
package net.sf.jasperreports.components.sort;

import net.sf.jasperreports.engine.JRComponentElement;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRGenericPrintElement;
import net.sf.jasperreports.engine.JRPrintElement;
import net.sf.jasperreports.engine.component.BaseFillComponent;
import net.sf.jasperreports.engine.component.FillPrepareResult;
import net.sf.jasperreports.engine.design.JRAbstractCompiler;
import net.sf.jasperreports.engine.fill.JRFillCloneFactory;
import net.sf.jasperreports.engine.fill.JRFillCloneable;
import net.sf.jasperreports.engine.fill.JRFillDataset;
import net.sf.jasperreports.engine.fill.JRFillField;
import net.sf.jasperreports.engine.fill.JRFillVariable;
import net.sf.jasperreports.engine.fill.JRTemplateGenericElement;
import net.sf.jasperreports.engine.fill.JRTemplateGenericPrintElement;
import net.sf.jasperreports.engine.type.EvaluationTimeEnum;
import net.sf.jasperreports.engine.type.SortFieldTypeEnum;

/**
 * 
 * @author Narcis Marcu (narcism@users.sourceforge.net)
 */
public class SortComponentFill extends BaseFillComponent {

	private final SortComponent sortComponent;
	
	private	JRTemplateGenericElement template;
	
	private JRTemplateGenericPrintElement printElement;

	
	public SortComponentFill(SortComponent sortComponent)
	{
		this.sortComponent = sortComponent;
	}
	
	protected SortComponent getSortComponent()
	{
		return sortComponent;
	}
	
	protected boolean isEvaluateNow()
	{
		return sortComponent.getEvaluationTime() == EvaluationTimeEnum.NOW;
	}
	
	public void evaluate(byte evaluation) throws JRException
	{
		if (isEvaluateNow())
		{
			evaluateSortComponent(evaluation);
		}
	}
	
	protected void evaluateSortComponent(byte evaluation) throws JRException 
	{
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
					SortElement.SORT_ELEMENT_TYPE);
		
			template.setMode(sortComponent.getContext().getComponentElement().getModeValue());
			template.setBackcolor(sortComponent.getContext().getComponentElement().getBackcolor());
			template.setForecolor(sortComponent.getContext().getComponentElement().getForecolor());
			
			template = deduplicate(template);
		}
		
		printElement = new JRTemplateGenericPrintElement(template, printElementOriginator);
		printElement.setUUID(element.getUUID());
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
					sortComponent.getEvaluationTime(), null);
		}
		
		result = FillPrepareResult.PRINT_NO_STRETCH;
		return result;
	}
	
	public JRFillCloneable createClone(JRFillCloneFactory factory)
	{
		throw new UnsupportedOperationException();
	}

	public void evaluateDelayedElement(JRPrintElement element, byte evaluation) throws JRException
	{
		evaluateSortComponent(evaluation);
		copy((JRGenericPrintElement) element);
	}

	protected void copy(JRGenericPrintElement printElement)
	{
		printElement.setParameterValue(SortElement.PARAMETER_SORT_COLUMN_NAME, sortComponent.getSortFieldName());
		printElement.setParameterValue(SortElement.PARAMETER_SORT_COLUMN_TYPE, sortComponent.getSortFieldType().getName());
		printElement.setParameterValue(SortElement.PARAMETER_SORT_HANDLER_COLOR, sortComponent.getHandlerColor());
		printElement.setParameterValue(SortElement.PARAMETER_SORT_HANDLER_FONT, sortComponent.getSymbolFont());
		
		if (sortComponent.getSymbolFont() != null ) {
			printElement.setParameterValue(SortElement.PARAMETER_SORT_HANDLER_FONT_SIZE, String.valueOf(sortComponent.getSymbolFont().getFontsize()));
		} 
		if (sortComponent.getHandlerHorizontalImageAlign() != null) 
		{
			printElement.setParameterValue(SortElement.PARAMETER_SORT_HANDLER_HORIZONTAL_ALIGN, sortComponent.getHandlerHorizontalImageAlign().getName());
		}
		if (sortComponent.getHandlerVerticalImageAlign() != null) 
		{
			printElement.setParameterValue(SortElement.PARAMETER_SORT_HANDLER_VERTICAL_ALIGN, sortComponent.getHandlerVerticalImageAlign().getName());
		}
		
		FilterTypesEnum filterType = getFilterType();
		if (filterType != null)
		{
			printElement.getPropertiesMap().setProperty(SortElement.PROPERTY_FILTER_TYPE, filterType.getName());
		}
		
		String datasetName = JRAbstractCompiler.getUnitName(
				fillContext.getFiller().getJasperReport(), fillContext.getFillDataset());
		printElement.getPropertiesMap().setProperty(SortElement.PROPERTY_DATASET_RUN, datasetName);
	}

	protected FilterTypesEnum getFilterType()
	{
		SortFieldTypeEnum type = sortComponent.getSortFieldType();
		String name = sortComponent.getSortFieldName();
		JRFillDataset dataset = fillContext.getFillDataset();
		
		FilterTypesEnum filterType = null;
		if (SortFieldTypeEnum.FIELD.equals(type))
		{
			JRFillField field = dataset.getFillField(name);
			filterType = SortElementUtils.getFilterType(field.getValueClass());
		}
		else if (SortFieldTypeEnum.VARIABLE.equals(type))
		{
			JRFillVariable variable = dataset.getFillVariable(name);
			filterType = SortElementUtils.getFilterType(variable.getValueClass());
		}
		return filterType;
	}
}
