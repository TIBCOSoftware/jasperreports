/*
 * ============================================================================
 *                   The JasperReports License, Version 1.0
 * ============================================================================
 * 
 * Copyright (C) 2001-2004 Teodor Danciu (teodord@users.sourceforge.net). All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 
 * 3. The end-user documentation included with the redistribution, if any, must
 *    include the following acknowledgment: "This product includes software
 *    developed by Teodor Danciu (http://jasperreports.sourceforge.net)."
 *    Alternately, this acknowledgment may appear in the software itself, if
 *    and wherever such third-party acknowledgments normally appear.
 * 
 * 4. The name "JasperReports" must not be used to endorse or promote products 
 *    derived from this software without prior written permission. For written 
 *    permission, please contact teodord@users.sourceforge.net.
 * 
 * 5. Products derived from this software may not be called "JasperReports", nor 
 *    may "JasperReports" appear in their name, without prior written permission
 *    of Teodor Danciu.
 * 
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS  FOR A PARTICULAR  PURPOSE ARE  DISCLAIMED.  IN NO  EVENT SHALL  THE
 * APACHE SOFTWARE  FOUNDATION  OR ITS CONTRIBUTORS  BE LIABLE FOR  ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL,  EXEMPLARY, OR CONSEQUENTIAL  DAMAGES (INCLU-
 * DING, BUT NOT LIMITED TO, PROCUREMENT  OF SUBSTITUTE GOODS OR SERVICES; LOSS
 * OF USE, DATA, OR  PROFITS; OR BUSINESS  INTERRUPTION)  HOWEVER CAUSED AND ON
 * ANY  THEORY OF LIABILITY,  WHETHER  IN CONTRACT,  STRICT LIABILITY,  OR TORT
 * (INCLUDING  NEGLIGENCE OR  OTHERWISE) ARISING IN  ANY WAY OUT OF THE  USE OF
 * THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

/*
 * ============================================================================
 *                   GNU Lesser General Public License
 * ============================================================================
 *
 * JasperReports - Free Java report-generating library.
 * Copyright (C) 2001-2004 Teodor Danciu teodord@users.sourceforge.net
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
package dori.jasper.engine.fill;

import java.math.BigDecimal;
import java.util.Map;

import dori.jasper.engine.JRException;
import dori.jasper.engine.JRExpression;
import dori.jasper.engine.JRVariable;


/**
 *
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
	protected JRFillVariable getPageNumber()
	{
		return this.pageNumber;
	}
	

	/**
	 *
	 */
	protected JRFillVariable getColumnNumber()
	{
		return this.columnNumber;
	}
	

	/**
	 *
	 */
	protected void calculateVariables() throws JRException
	{
		if (variables != null && variables.length > 0)
		{
			JRFillVariable variable = null;
			Object expressionValue = null;
			Object newValue = null;
			
			for(int i = 0; i < variables.length; i++)
			{
				variable = variables[i];
				expressionValue = this.evaluate(variable.getExpression());
				newValue = calculateVariable(variable, expressionValue);
				variable.setValue(newValue);
				variable.setInitialized(false);
			}
		}
	}


	/**
	 *
	 */
	protected void estimateVariables() throws JRException
	{
		if (variables != null && variables.length > 0)
		{
			JRFillVariable variable = null;
			Object expressionValue = null;
			Object newValue = null;
			
			for(int i = 0; i < variables.length; i++)
			{
				variable = variables[i];
				expressionValue = this.evaluateEstimated(variable.getExpression());
				newValue = calculateVariable(variable, expressionValue);
				variable.setEstimatedValue(newValue);
				variable.setInitialized(false);
			}
		}
	}


	/**
	 *
	 */
	protected void estimateGroupRuptures() throws JRException
	{
		this.estimateVariables();

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
					oldValue = this.evaluateOld(group.getExpression());
					estimatedValue = this.evaluateEstimated(group.getExpression());

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
	private Object calculateVariable(JRFillVariable variable, Object expressionValue) throws JRException
	{
		Object retValue = null;

		switch (variable.getCalculation())
		{
			case JRVariable.CALCULATION_COUNT :
			{
				if (variable.getValueClass().equals(java.math.BigDecimal.class))
				{
					BigDecimal value = (BigDecimal)variable.getValue();
					if (value == null || variable.isInitialized())
					{
						value = new BigDecimal("0");
					}
					BigDecimal newValue = null;
					if (expressionValue == null)
					{
						newValue = value;
					}
					else
					{
						newValue = value.add(new BigDecimal("1"));
					}
					
					retValue = newValue;
				}
				else
				{
					Number value = (Number)variable.getValue();
					if (value == null || variable.isInitialized())
					{
						value = new Double(0);
					}

					Number newValue = null;
					if (expressionValue == null)
					{
						newValue = value;
					}
					else
					{
						newValue = new Double(value.doubleValue() + 1);
					}
					
					retValue = convertToVariableClass(variable, newValue);
				}
				
				break;
			}
			case JRVariable.CALCULATION_SUM :
			{
				if (variable.getValueClass().equals(java.math.BigDecimal.class))
				{
					BigDecimal value = (BigDecimal)variable.getValue();
					if (value == null || variable.isInitialized())
					{
						value = new BigDecimal("0");
					}
					BigDecimal newValue = (BigDecimal)expressionValue;
					if (newValue == null)
					{
						newValue = new BigDecimal("0");
					}
					newValue = value.add(newValue);
					
					retValue = newValue;
				}
				else
				{
					Number value = (Number)variable.getValue();
					if (value == null || variable.isInitialized())
					{
						value = new Double(0);
					}
					Number newValue = (Number)expressionValue;
					if (newValue == null)
					{
						newValue = new Double(0);
					}
					newValue = new Double(value.doubleValue() + newValue.doubleValue());
					
					retValue = convertToVariableClass(variable, newValue);
				}
				
				break;
			}
			case JRVariable.CALCULATION_AVERAGE :
			{
				if (variable.getValueClass().equals(java.math.BigDecimal.class))
				{
					BigDecimal newValue = null;

					long count = ((java.lang.Number)((JRFillVariable)variable.getCountVariable()).getValue()).longValue();
					if (count > 0)
					{
						BigDecimal countValue = BigDecimal.valueOf(count);
						BigDecimal sumValue = (BigDecimal)((JRFillVariable)variable.getSumVariable()).getValue();
						newValue = sumValue.divide(countValue, BigDecimal.ROUND_HALF_UP);
					}
					
					retValue = newValue;
				}
				else
				{
					Number newValue = null;

					Number countValue = (Number)((JRFillVariable)variable.getCountVariable()).getValue();
					if (countValue.longValue() > 0)
					{
						Number sumValue = (Number)((JRFillVariable)variable.getSumVariable()).getValue();
						newValue = convertToVariableClass(variable, new Double(sumValue.doubleValue() / countValue.doubleValue()));
					}
					
					retValue = newValue;
				}
				
				break;
			}
			case JRVariable.CALCULATION_LOWEST :
			{
				Comparable value = (Comparable)variable.getValue();
				Comparable newValue = (Comparable)expressionValue;

				if (
					value != null && !variable.isInitialized() &&
					(newValue == null || value.compareTo(newValue) < 0)
					)
				{
					newValue = value;
				}
				
				retValue = newValue;
				
				break;
			}
			case JRVariable.CALCULATION_HIGHEST :
			{
				Comparable value = (Comparable)variable.getValue();
				Comparable newValue = (Comparable)expressionValue;

				if (
					value != null && !variable.isInitialized() &&
					(newValue == null || value.compareTo(newValue) > 0)
					)
				{
					newValue = value;
				}
				
				retValue = newValue;
				
				break;
			}
			case JRVariable.CALCULATION_STANDARD_DEVIATION :
			{
				if (variable.getValueClass().equals(java.math.BigDecimal.class))
				{
					BigDecimal varianceValue = (BigDecimal)((JRFillVariable)variable.getVarianceVariable()).getValue();
					BigDecimal newValue = new BigDecimal( Math.sqrt(varianceValue.doubleValue()) );
					
					retValue = newValue;
				}
				else
				{
					Number varianceValue = (Number)((JRFillVariable)variable.getVarianceVariable()).getValue();
					Number newValue = new Double( Math.sqrt(varianceValue.doubleValue()) );
					
					retValue = convertToVariableClass(variable, newValue);
				}
				
				break;
			}
			case JRVariable.CALCULATION_VARIANCE :
			{
				if (variable.getValueClass().equals(java.math.BigDecimal.class))
				{
					BigDecimal value = (BigDecimal)variable.getValue();
					if (value == null || variable.isInitialized())
					{
						value = new BigDecimal("0");
					}
					BigDecimal countValue = BigDecimal.valueOf( ((java.lang.Number)((JRFillVariable)variable.getCountVariable()).getValue()).longValue() );
					BigDecimal sumValue = (BigDecimal)((JRFillVariable)variable.getSumVariable()).getValue();
					BigDecimal newValue = (BigDecimal)expressionValue;
	
					if (countValue.intValue() == 1)
					{
						newValue = new BigDecimal("0");
					}
					else
					{
						newValue = 
							countValue.subtract(new BigDecimal("1")).multiply(value).divide(countValue, BigDecimal.ROUND_HALF_UP).add(
								sumValue.divide(countValue, BigDecimal.ROUND_HALF_UP).subtract(newValue).multiply(
									sumValue.divide(countValue, BigDecimal.ROUND_HALF_UP).subtract(newValue)
									).divide(countValue.subtract(new BigDecimal("1")), BigDecimal.ROUND_HALF_UP)
								);
					}
					
					retValue = newValue;
				}
				else
				{
					Number value = (Number)variable.getValue();
					if (value == null || variable.isInitialized())
					{
						value = new Double(0);
					}
					Number countValue = (Number)((JRFillVariable)variable.getCountVariable()).getValue();
					Number sumValue = (Number)((JRFillVariable)variable.getSumVariable()).getValue();
					Number newValue = (Number)expressionValue;
	
					if (countValue.intValue() == 1)
					{
						newValue = new Double(0);
					}
					else
					{
						newValue = new Double(
							(countValue.doubleValue() - 1) * value.doubleValue() / countValue.doubleValue() +
							( sumValue.doubleValue() / countValue.doubleValue() - newValue.doubleValue() ) *
							( sumValue.doubleValue() / countValue.doubleValue() - newValue.doubleValue() ) /
							(countValue.doubleValue() - 1)
							);
					}
					
					retValue = convertToVariableClass(variable, newValue);
				}
				
				break;
			}
			case JRVariable.CALCULATION_SYSTEM :
			{
				retValue = variable.getValue();
				break;
			}
			case JRVariable.CALCULATION_NOTHING :
			default :
			{
				retValue = expressionValue;
				break;
			}
		}

		return retValue;
	}


	/**
	 *
	 */
	protected Number convertToVariableClass(JRFillVariable variable, Number newValue) throws JRException
	{
		Class valueClass = variable.getValueClass();

		if (valueClass.equals(java.lang.Byte.class))
		{
			newValue = new Byte(newValue.byteValue());
		}
		else if (valueClass.equals(java.lang.Short.class))
		{
			newValue = new Short(newValue.shortValue());
		}
		else if (valueClass.equals(java.lang.Integer.class))
		{
			newValue = new Integer(newValue.intValue());
		}
		else if (valueClass.equals(java.lang.Long.class))
		{
			newValue = new Long(newValue.longValue());
		}
		else if (valueClass.equals(java.lang.Float.class))
		{
			newValue = new Float(newValue.floatValue());
		}
		else if (valueClass.equals(java.lang.Double.class))
		{
			newValue = new Double(newValue.doubleValue());
		}
		
		return newValue;
	}


	/**
	 *
	 */
	protected void initializeVariables(byte resetType) throws JRException
	{
		if (variables != null && variables.length > 0)
		{
			for(int i = 0; i < variables.length; i++)
			{
				initializeVariable(variables[i], resetType);
			}
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
					this.evaluate(variable.getInitialValueExpression())
					);
				variable.setInitialized(true);
			}
		}
		else
		{
			variable.setValue(
				this.evaluate(variable.getExpression())
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
				value = this.evaluateOld(expression);
				break;
			}
			case JRExpression.EVALUATION_ESTIMATED :
			{
				value = this.evaluateEstimated(expression);
				break;
			}
			case JRExpression.EVALUATION_DEFAULT :
			default :
			{
				value = this.evaluate(expression);
				break;
			}
		}

		return value;
	}
	

	/**
	 *
	 */
	protected Object evaluateOld(JRExpression expression) throws JRException
	{
		Object value = null;
		
		try
		{
			value = this.evaluateOld(expression.getId());
		}
		catch (NullPointerException e)
		{
		}
		catch (Throwable e)
		{
			throw new JRException("Error evaluating expression old value : " + expression.getName(), e);
		}
		
		return value;
	}


	/**
	 *
	 */
	protected Object evaluateEstimated(JRExpression expression) throws JRException
	{
		Object value = null;
		
		try
		{
			value = this.evaluateEstimated(expression.getId());
		}
		catch (NullPointerException e)
		{
		}
		catch (Throwable e)
		{
			throw new JRException("Error evaluating expression estimated value : " + expression.getName(), e);
		}
		
		return value;
	}


	/**
	 *
	 */
	protected Object evaluate(JRExpression expression) throws JRException
	{
		Object value = null;
		
		try
		{
			value = this.evaluate(expression.getId());
		}
		catch (NullPointerException e)
		{
		}
		catch (Throwable e)
		{
			throw new JRException("Error evaluating expression value : " + expression.getName(), e);
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

	
}
