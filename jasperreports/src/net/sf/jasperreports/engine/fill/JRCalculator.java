/*
 * ============================================================================
 *                   GNU Lesser General Public License
 * ============================================================================
 *
 * JasperReports - Free Java report-generating library.
 * Copyright (C) 2001-2005 Teodor Danciu teodord@users.sourceforge.net
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
 * Teodor Danciu
 * 173, Calea Calarasilor, Bl. 42, Sc. 1, Ap. 18
 * Postal code 030615, Sector 3
 * Bucharest, ROMANIA
 * Email: teodord@users.sourceforge.net
 */

/*
 * Contributors:
 * Peter Severin - peter_p_s@users.sourceforge.net 
 */
package net.sf.jasperreports.engine.fill;

import java.text.MessageFormat;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JRVariable;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public abstract class JRCalculator
{


	/**
	 *
	 */
	protected Map parsm = null;
	protected Map fldsm = null;
	protected Map varsm = null;
	protected JRFillVariable[] variables = null;
	protected JRFillGroup[] groups = null;

	private JRFillParameter resourceBundle = null;
	private JRFillVariable pageNumber = null;
	private JRFillVariable columnNumber = null;


	/**
	 *
	 */
	protected JRCalculator()
	{
	}


	/**
	 *
	 */
	protected void init(
		Map parametersMap,
		Map fieldsMap,
		Map variablesMap,
		JRFillVariable[] vars,
		JRFillGroup[] grps
		) throws JRException
	{
		parsm = parametersMap;
		fldsm = fieldsMap;
		varsm = variablesMap;
		variables = vars;
		groups = grps;

		resourceBundle = (JRFillParameter)parametersMap.get(JRParameter.REPORT_RESOURCE_BUNDLE);
		pageNumber = (JRFillVariable)variablesMap.get(JRVariable.PAGE_NUMBER);
		columnNumber = (JRFillVariable)variablesMap.get(JRVariable.COLUMN_NUMBER);
		
		customizedInit(
			parametersMap,
			fieldsMap,
			variablesMap
			);
	}


	/**
	 *
	 */
	protected abstract void customizedInit(
		Map parametersMap,
		Map fieldsMap,
		Map variablesMap
		) throws JRException;


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
			JRFillVariable variable = null;
			Object expressionValue = null;
			Object newValue = null;
			
			for(int i = 0; i < variables.length; i++)
			{
				variable = variables[i];
				expressionValue = evaluate(variable.getExpression());
				newValue = variable.getIncrementer().increment(variable, expressionValue, AbstractValueProvider.getCurrentValueProvider());
				variable.setValue(newValue);
				variable.setInitialized(false);

				if (variable.getIncrementType() == JRVariable.RESET_TYPE_NONE)
				{
					variable.setIncrementedValue(variable.getValue());
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
	}


	/**
	 *
	 */
	private void incrementVariable(JRFillVariable variable, byte incrementType) throws JRException
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
			}
		}
		else
		{
			variable.setValue(
				evaluate(variable.getExpression())
				);
		}
	}


	/**
	 *
	 */
	protected Object evaluate(
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
		Object value = null;
		
		try
		{
			value = evaluateOld(expression.getId());
		}
		catch (NullPointerException e)
		{
		}
		catch (OutOfMemoryError e)
		{
			throw e;
		}
		catch (Throwable e)
		{
			throw new JRExpressionEvalException(expression, e); 
		}
		
		return value;
	}


	/**
	 *
	 */
	public Object evaluateEstimated(JRExpression expression) throws JRExpressionEvalException
	{
		Object value = null;
		
		try
		{
			value = evaluateEstimated(expression.getId());
		}
		catch (NullPointerException e)
		{
		}
		catch (OutOfMemoryError e)
		{
			throw e;
		}
		catch (Throwable e)
		{
			throw new JRExpressionEvalException(expression, e); 
		}
		
		return value;
	}


	/**
	 *
	 */
	public Object evaluate(JRExpression expression) throws JRExpressionEvalException
	{
		Object value = null;
		
		try
		{
			value = evaluate(expression.getId());
		}
		catch (NullPointerException e)
		{
		}
		catch (OutOfMemoryError e)
		{
			throw e;
		}
		catch (Throwable e)
		{
			throw new JRExpressionEvalException(expression, e); 
		}
		
		return value;
	}


	/**
	 *
	 */
	protected abstract Object evaluateOld(int id) throws Throwable;


	/**
	 *
	 */
	protected abstract Object evaluateEstimated(int id) throws Throwable;


	/**
	 *
	 */
	protected abstract Object evaluate(int id) throws Throwable;


	/**
	 *
	 */
	public String str(String key)
	{
		String str = null;
		
		try
		{
			str = ((ResourceBundle)resourceBundle.getValue()).getString(key);
		}
		catch (NullPointerException e)
		{
		}
		catch (MissingResourceException e)
		{
		}

		return str;
	}


	/**
	 *
	 */
	public String msg(String pattern, Object arg0)
	{
		return MessageFormat.format(pattern, new Object[]{arg0});
	}

	/**
	 *
	 */
	public String msg(String pattern, Object arg0, Object arg1)
	{
		return MessageFormat.format(pattern, new Object[]{arg0, arg1});
	}

	/**
	 *
	 */
	public String msg(String pattern, Object arg0, Object arg1, Object arg2)
	{
		return MessageFormat.format(pattern, new Object[]{arg0, arg1, arg2});
	}


}
