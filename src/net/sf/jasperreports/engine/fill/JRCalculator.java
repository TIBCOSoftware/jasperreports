/*
 * ============================================================================
 * GNU Lesser General Public License
 * ============================================================================
 *
 * JasperReports - Free Java report-generating library.
 * Copyright (C) 2001-2005 JasperSoft Corporation http://www.jaspersoft.com
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
 * 185, Berry Street, Suite 6200
 * San Francisco CA 94107
 * http://www.jaspersoft.com
 */

/*
 * Contributors:
 * Peter Severin - peter_p_s@users.sourceforge.net 
 */
package net.sf.jasperreports.engine.fill;

import java.util.Map;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JRVariable;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JRCalculator implements JRFillExpressionEvaluator
{


	/**
	 *
	 */
	protected Map parsm = null;
	protected Map fldsm = null;
	protected Map varsm = null;
	protected JRFillVariable[] variables = null;
	protected JRFillGroup[] groups = null;
	protected JRFillChartDataset[] datasets = null;

	private JRFillVariable pageNumber = null;
	private JRFillVariable columnNumber = null;
	
	/**
	 * The expression evaluator
	 */
	private final JREvaluator evaluator;


	/**
	 * Creates a calculator using an expression evaluator.
	 * 
	 * @param evaluator the expression evaluator
	 */
	protected JRCalculator(JREvaluator evaluator)
	{
		this.evaluator = evaluator;
	}


	/**
	 * Initializes the calculator.
	 * 
	 * @param dataset the dataset this calculator is used for
	 * @throws JRException
	 */
	protected void init(JRFillDataset dataset) throws JRException
	{
		parsm = dataset.parametersMap;
		fldsm = dataset.fieldsMap;
		varsm = dataset.variablesMap;
		variables = dataset.variables;
		groups = dataset.groups;
		datasets = dataset.chartDatasets;

		pageNumber = (JRFillVariable)varsm.get(JRVariable.PAGE_NUMBER);
		columnNumber = (JRFillVariable)varsm.get(JRVariable.COLUMN_NUMBER);
		
		JRFillParameter resourceBundle = (JRFillParameter) parsm.get(JRParameter.REPORT_RESOURCE_BUNDLE);
		byte whenResourceMissingType = dataset.getWhenResourceMissingType();
		evaluator.init(parsm, fldsm,varsm, resourceBundle, whenResourceMissingType);
	}


	/**
	 *
	 */
	public JRFillVariable getPageNumber()
	{
		return pageNumber;
	}
	

	/**
	 *
	 */
	public JRFillVariable getColumnNumber()
	{
		return columnNumber;
	}
	

	/**
	 *
	 */
	public void calculateVariables() throws JRException
	{
		if (variables != null && variables.length > 0)
		{
			for(int i = 0; i < variables.length; i++)
			{
				JRFillVariable variable = variables[i];
				Object expressionValue = evaluate(variable.getExpression());
				Object newValue = variable.getIncrementer().increment(variable, expressionValue, AbstractValueProvider.getCurrentValueProvider());
				variable.setValue(newValue);
				variable.setInitialized(false);

				if (variable.getIncrementType() == JRVariable.RESET_TYPE_NONE)
				{
					variable.setIncrementedValue(variable.getValue());
				}
			}
		}

		if (datasets != null && datasets.length > 0)
		{
			for(int i = 0; i < datasets.length; i++)
			{
				JRFillChartDataset chartDataset = datasets[i];
				chartDataset.evaluate(this);

				if (chartDataset.getIncrementType() == JRVariable.RESET_TYPE_NONE)
				{
					chartDataset.increment();
				}
			}
		}
	}


	/**
	 *
	 */
	public void estimateVariables() throws JRException
	{
		if (variables != null && variables.length > 0)
		{
			JRFillVariable variable = null;
			Object expressionValue = null;
			Object newValue = null;
			
			for(int i = 0; i < variables.length; i++)
			{
				variable = variables[i];
				expressionValue = evaluateEstimated(variable.getExpression());
				newValue = variable.getIncrementer().increment(variable, expressionValue,  AbstractValueProvider.getEstimatedValueProvider());
				variable.setEstimatedValue(newValue);
				//variable.setInitialized(false);
			}
		}
	}


	/**
	 *
	 */
	public void estimateGroupRuptures() throws JRException
	{
		estimateVariables();

		JRFillGroup group = null;
		Object oldValue = null;
		Object estimatedValue = null;
		boolean groupHasChanged = false;
		boolean isTopLevelChange = false;
		if (groups != null && groups.length > 0)
		{
			for(int i = 0; i < groups.length; i++)
			{
				group = groups[i];
				
				isTopLevelChange = false;

				if (!groupHasChanged)
				{
					oldValue = evaluateOld(group.getExpression());
					estimatedValue = evaluateEstimated(group.getExpression());

					if ( 
						(oldValue == null && estimatedValue != null) ||
						(oldValue != null && !oldValue.equals(estimatedValue))
						)
					{
						groupHasChanged = true;
						isTopLevelChange = true;
					}
				}

				group.setHasChanged(groupHasChanged);
				group.setTopLevelChange(isTopLevelChange);
			}
		}
	}


	/**
	 *
	 */
	public void initializeVariables(byte resetType) throws JRException
	{
		if (variables != null && variables.length > 0)
		{
			for(int i = 0; i < variables.length; i++)
			{
				incrementVariable(variables[i], resetType);
				initializeVariable(variables[i], resetType);
			}
		}

		if (datasets != null && datasets.length > 0)
		{
			for(int i = 0; i < datasets.length; i++)
			{
				incrementDataset(datasets[i], resetType);
				initializeDataset(datasets[i], resetType);
			}
		}
	}


	/**
	 *
	 */
	private void incrementVariable(JRFillVariable variable, byte incrementType)
	{
		if (variable.getIncrementType() != JRVariable.RESET_TYPE_NONE)
		{
			boolean toIncrement = false;
			switch (incrementType)
			{
				case JRVariable.RESET_TYPE_REPORT :
				{
					toIncrement = true;
					break;
				}
				case JRVariable.RESET_TYPE_PAGE :
				{
					toIncrement = 
						(
						variable.getIncrementType() == JRVariable.RESET_TYPE_PAGE || 
						variable.getIncrementType() == JRVariable.RESET_TYPE_COLUMN
						);
					break;
				}
				case JRVariable.RESET_TYPE_COLUMN :
				{
					toIncrement = (variable.getIncrementType() == JRVariable.RESET_TYPE_COLUMN);
					break;
				}
				case JRVariable.RESET_TYPE_GROUP :
				{
					if (variable.getIncrementType() == JRVariable.RESET_TYPE_GROUP)
					{
						JRFillGroup group = (JRFillGroup)variable.getIncrementGroup();
						toIncrement = group.hasChanged();
					}
					break;
				}
				case JRVariable.RESET_TYPE_NONE :
				default :
				{
				}
			}

			if (toIncrement)
			{
				variable.setIncrementedValue(variable.getValue());
//				variable.setValue(
//					evaluate(variable.getInitialValueExpression())
//					);
//				variable.setInitialized(true);
			}
		}
		else
		{
			variable.setIncrementedValue(variable.getValue());
//			variable.setValue(
//				evaluate(variable.getExpression())
//				);
		}
	}


	/**
	 *
	 */
	private void incrementDataset(JRFillChartDataset chartDataset, byte incrementType)
	{
		if (chartDataset.getIncrementType() != JRVariable.RESET_TYPE_NONE)
		{
			boolean toIncrement = false;
			switch (incrementType)
			{
				case JRVariable.RESET_TYPE_REPORT :
				{
					toIncrement = true;
					break;
				}
				case JRVariable.RESET_TYPE_PAGE :
				{
					toIncrement = 
						(
						chartDataset.getIncrementType() == JRVariable.RESET_TYPE_PAGE || 
						chartDataset.getIncrementType() == JRVariable.RESET_TYPE_COLUMN
						);
					break;
				}
				case JRVariable.RESET_TYPE_COLUMN :
				{
					toIncrement = (chartDataset.getIncrementType() == JRVariable.RESET_TYPE_COLUMN);
					break;
				}
				case JRVariable.RESET_TYPE_GROUP :
				{
					if (chartDataset.getIncrementType() == JRVariable.RESET_TYPE_GROUP)
					{
						JRFillGroup group = (JRFillGroup)chartDataset.getIncrementGroup();
						toIncrement = group.hasChanged();
					}
					break;
				}
				case JRVariable.RESET_TYPE_NONE :
				default :
				{
				}
			}

			if (toIncrement)
			{
				chartDataset.increment();
			}
		}
		else
		{
			//FIXME NOW dataset.increment();
		}
	}


	/**
	 *
	 */
	private void initializeVariable(JRFillVariable variable, byte resetType) throws JRException
	{
		//if (jrVariable.getCalculation() != JRVariable.CALCULATION_NOTHING)
		if (variable.getResetType() != JRVariable.RESET_TYPE_NONE)
		{
			boolean toInitialize = false;
			switch (resetType)
			{
				case JRVariable.RESET_TYPE_REPORT :
				{
					toInitialize = true;
					break;
				}
				case JRVariable.RESET_TYPE_PAGE :
				{
					toInitialize = 
						(
						variable.getResetType() == JRVariable.RESET_TYPE_PAGE || 
						variable.getResetType() == JRVariable.RESET_TYPE_COLUMN
						);
					break;
				}
				case JRVariable.RESET_TYPE_COLUMN :
				{
					toInitialize = (variable.getResetType() == JRVariable.RESET_TYPE_COLUMN);
					break;
				}
				case JRVariable.RESET_TYPE_GROUP :
				{
					if (variable.getResetType() == JRVariable.RESET_TYPE_GROUP)
					{
						JRFillGroup group = (JRFillGroup)variable.getResetGroup();
						toInitialize = group.hasChanged();
					}
					break;
				}
				case JRVariable.RESET_TYPE_NONE :
				default :
				{
				}
			}

			if (toInitialize)
			{
				variable.setValue(
					evaluate(variable.getInitialValueExpression())
					);
				variable.setInitialized(true);
				variable.setIncrementedValue(null);
			}
		}
		else
		{
			variable.setValue(
				evaluate(variable.getExpression())
				);
			variable.setIncrementedValue(variable.getValue());
		}
	}


	/**
	 *
	 */
	private void initializeDataset(JRFillChartDataset chartDataset, byte resetType)
	{
		//if (jrVariable.getCalculation() != JRVariable.CALCULATION_NOTHING)
//		if (dataset.getResetType() != JRVariable.RESET_TYPE_NONE)
//		{
			boolean toInitialize = false;
			switch (resetType)
			{
				case JRVariable.RESET_TYPE_REPORT :
				{
					toInitialize = true;
					break;
				}
				case JRVariable.RESET_TYPE_PAGE :
				{
					toInitialize = 
						(
						chartDataset.getResetType() == JRVariable.RESET_TYPE_PAGE || 
						chartDataset.getResetType() == JRVariable.RESET_TYPE_COLUMN
						);
					break;
				}
				case JRVariable.RESET_TYPE_COLUMN :
				{
					toInitialize = (chartDataset.getResetType() == JRVariable.RESET_TYPE_COLUMN);
					break;
				}
				case JRVariable.RESET_TYPE_GROUP :
				{
					if (chartDataset.getResetType() == JRVariable.RESET_TYPE_GROUP)
					{
						JRFillGroup group = (JRFillGroup)chartDataset.getResetGroup();
						toInitialize = group.hasChanged();
					}
					break;
				}
				case JRVariable.RESET_TYPE_NONE :
				default :
				{
				}
			}

			if (toInitialize)
			{
//				dataset.setValue(
//					evaluate(dataset.getInitialValueExpression())
//					);
//				dataset.setInitialized(true);
//				dataset.setIncrementedValue(null);
				chartDataset.initialize();
			}
//		}FIXME NOW verify that reset type none does not make any sense
//		else
//		{
//			dataset.setValue(
//				evaluate(dataset.getExpression())
//				);
//			dataset.setIncrementedValue(dataset.getValue());
//		}
	}


	/**
	 *
	 */
	public Object evaluate(
		JRExpression expression,
		byte evaluationType
		) throws JRException
	{
		Object value = null;
		
		switch (evaluationType)
		{
			case JRExpression.EVALUATION_OLD :
			{
				value = evaluateOld(expression);
				break;
			}
			case JRExpression.EVALUATION_ESTIMATED :
			{
				value = evaluateEstimated(expression);
				break;
			}
			case JRExpression.EVALUATION_DEFAULT :
			default :
			{
				value = evaluate(expression);
				break;
			}
		}

		return value;
	}
	

	/**
	 *
	 */
	public Object evaluateOld(JRExpression expression) throws JRExpressionEvalException
	{
		return evaluator.evaluateOld(expression);
	}


	/**
	 *
	 */
	public Object evaluateEstimated(JRExpression expression) throws JRExpressionEvalException
	{
		return evaluator.evaluateEstimated(expression);
	}


	/**
	 *
	 */
	public Object evaluate(JRExpression expression) throws JRExpressionEvalException
	{
		return evaluator.evaluate(expression);
	}
}
