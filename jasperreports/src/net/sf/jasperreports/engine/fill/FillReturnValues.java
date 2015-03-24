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
package net.sf.jasperreports.engine.fill;

import java.util.ArrayList;
import java.util.List;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.JRSubreportReturnValue;
import net.sf.jasperreports.engine.JRVariable;
import net.sf.jasperreports.engine.ReturnValue;
import net.sf.jasperreports.engine.design.JRDesignSubreportReturnValue;
import net.sf.jasperreports.engine.type.CalculationEnum;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 */
public class FillReturnValues
{

	public static interface SourceContext
	{
		JRVariable getVariable(String name);
		
		Object getVariableValue(String name);
	}

	private static final Log log = LogFactory.getLog(FillReturnValues.class);
	public static final String EXCEPTION_MESSAGE_KEY_NUMERIC_TYPE_REQUIRED = "fill.return.values.numeric.type.required";
	public static final String EXCEPTION_MESSAGE_KEY_SOURCE_NOT_FOUND = "fill.return.values.source.not.found";
	public static final String EXCEPTION_MESSAGE_KEY_VARIABLE_NOT_ASSIGNABLE = "fill.return.values.variable.not.assignable";
	
	private final BaseReportFiller filler;
	private JRFillSubreportReturnValue[] returnValues;
	private JRFillBand band;
	
	public FillReturnValues(JRSubreportReturnValue[] values, JRFillObjectFactory factory, BaseReportFiller filler)
	{
		this.filler = filler;
		
		if (values != null && values.length > 0)
		{
			List<JRFillSubreportReturnValue> returnValuesList = 
					new ArrayList<JRFillSubreportReturnValue>(values.length * 2);
			for (JRSubreportReturnValue returnValue : values)
			{
				JRFillSubreportReturnValue fillReturnValue = factory.getSubreportReturnValue(returnValue);
				addReturnValue(fillReturnValue, returnValuesList, factory, filler);
			}
			
			returnValues = new JRFillSubreportReturnValue[returnValuesList.size()];
			returnValuesList.toArray(returnValues);
		}
	}
	
	public FillReturnValues(List<ReturnValue> values, JRFillObjectFactory factory, BaseReportFiller filler)
	{
		this.filler = filler;
		
		if (values != null && !values.isEmpty())
		{
			List<JRFillSubreportReturnValue> returnValuesList = 
					new ArrayList<JRFillSubreportReturnValue>(values.size() * 2);
			for (ReturnValue returnValue : values)
			{
				JRFillSubreportReturnValue fillReturnValue = factory.getReturnValue(returnValue);
				addReturnValue(fillReturnValue, returnValuesList, factory, filler);
			}
			
			returnValues = new JRFillSubreportReturnValue[returnValuesList.size()];
			returnValuesList.toArray(returnValues);
		}
	}

	protected FillReturnValues(FillReturnValues values, JRFillCloneFactory factory)
	{
		this.filler = values.filler;
		this.band = values.band;
		
		if (values.returnValues != null)
		{
			this.returnValues = new JRFillSubreportReturnValue[values.returnValues.length];
			for (int i = 0; i < values.returnValues.length; i++)
			{
				this.returnValues[i] = new JRFillSubreportReturnValue(values.returnValues[i], factory);
			}
		}
	}

	protected JRFillSubreportReturnValue addReturnValue(
			JRFillSubreportReturnValue returnValue,
			List<JRFillSubreportReturnValue> returnValueList,
			JRFillObjectFactory factory, BaseReportFiller filler)
	{
		CalculationEnum calculation = returnValue.getCalculationValue();
		switch (calculation)
		{
			case AVERAGE:
			case VARIANCE:
			{
				JRSubreportReturnValue countVal = createHelperReturnValue(returnValue, "_COUNT", CalculationEnum.COUNT);
				addDerivedReturnValue(countVal, returnValueList, factory, filler);

				JRSubreportReturnValue sumVal = createHelperReturnValue(returnValue, "_SUM", CalculationEnum.SUM);
				addDerivedReturnValue(sumVal, returnValueList, factory, filler);

				filler.addVariableCalculationReq(returnValue.getToVariable(), calculation);

				break;
			}
			case STANDARD_DEVIATION:
			{
				JRSubreportReturnValue varianceVal = createHelperReturnValue(returnValue, "_VARIANCE", CalculationEnum.VARIANCE);
				addDerivedReturnValue(varianceVal, returnValueList, factory, filler);
				
				filler.addVariableCalculationReq(returnValue.getToVariable(), calculation);
				break;
			}
			case DISTINCT_COUNT:
			{
				JRSubreportReturnValue countVal = createDistinctCountHelperReturnValue(returnValue);
				addDerivedReturnValue(countVal, returnValueList, factory, filler);
				
				filler.addVariableCalculationReq(returnValue.getToVariable(), calculation);
				break;
			}
		}

		returnValueList.add(returnValue);
		return returnValue;
	}

	protected JRFillSubreportReturnValue addDerivedReturnValue (
			JRSubreportReturnValue parentReturnValue, 
			List<JRFillSubreportReturnValue> returnValueList, 
			JRFillObjectFactory factory, BaseReportFiller filler
			)
	{
		JRFillSubreportReturnValue returnValue = factory.getSubreportReturnValue(parentReturnValue);
		returnValue.setDerived(true);
		return addReturnValue(returnValue, returnValueList, factory, filler);
	}
	
	protected JRSubreportReturnValue createHelperReturnValue(JRSubreportReturnValue returnValue, String nameSuffix, CalculationEnum calculation)
	{
		JRDesignSubreportReturnValue helper = new JRDesignSubreportReturnValue();
		helper.setToVariable(returnValue.getToVariable() + nameSuffix);
		helper.setSubreportVariable(returnValue.getSubreportVariable());
		helper.setCalculation(calculation);
		helper.setIncrementerFactoryClassName(helper.getIncrementerFactoryClassName());//FIXME shouldn't it be returnValue?
		
		return helper;
	}

	protected JRSubreportReturnValue createDistinctCountHelperReturnValue(JRSubreportReturnValue returnValue)
	{
		JRDesignSubreportReturnValue helper = new JRDesignSubreportReturnValue();
		helper.setToVariable(returnValue.getToVariable() + "_DISTINCT_COUNT");
		helper.setSubreportVariable(returnValue.getSubreportVariable());
		helper.setCalculation(CalculationEnum.NOTHING);
		helper.setIncrementerFactoryClassName(JRDistinctCountIncrementerFactory.class.getName());
		
		return helper;
	}

	public void setBand(JRFillBand band)
	{
		this.band = band;
		band.registerReturnValues(this);
	}

	public void saveReturnVariables()
	{
		if (log.isDebugEnabled())
		{
			log.debug("saving return variables on band " + band);
		}
		
		if (returnValues != null && band != null)
		{
			for (int i = 0; i < returnValues.length; i++)
			{
				String varName = returnValues[i].getToVariable();
				band.saveVariable(varName);
			}
		}
	}
	
	public boolean usesForReturnValue(String variableName)
	{
		boolean used = false;
		if (returnValues != null)
		{
			for (int j = 0; j < returnValues.length; j++)
			{
				JRSubreportReturnValue returnValue = returnValues[j];
				if (returnValue.getToVariable().equals(variableName))
				{
					if (log.isDebugEnabled())
					{
						log.debug("variable " + variableName 
								+ " used for return value of " + returnValue.getSubreportVariable());
					}
					
					used = true;
					break;
				}
			}
		}
		return used;
	}

	/**
	 * Copies the values from the source to the variables of the master report.
	 */
	public void copyValues(SourceContext sourceContext)
	{
		if (returnValues != null && returnValues.length > 0)
		{
			for (int i = 0; i < returnValues.length; i++)
			{
				copyValue(returnValues[i], sourceContext);
			}
		}
	}

	protected void copyValue(JRFillSubreportReturnValue returnValue, SourceContext sourceContext)
	{
		try
		{
			JRFillVariable variable = filler.getVariable(returnValue.getToVariable());
			Object value = sourceContext.getVariableValue(returnValue.getSubreportVariable());
			
			if (log.isTraceEnabled())
			{
				log.trace("copying value " + value + " of " + returnValue.getSubreportVariable()
						+ " to " + returnValue.getToVariable());
			}
			
			Object newValue = returnValue.getIncrementer().increment(variable, value, AbstractValueProvider.getCurrentValueProvider());
			variable.setOldValue(newValue);
			variable.setValue(newValue);
			variable.setIncrementedValue(newValue);
		}
		catch (JRException e)
		{
			throw new JRRuntimeException(e);
		}
	}
	
	/**
	 * Verifies the list of copied values against the subreport.
	 * 
	 * @throws JRException
	 */
	public void checkReturnValues(SourceContext sourceContext) throws JRException
	{
		if (returnValues != null && returnValues.length > 0)
		{
			for (int i = 0; i < returnValues.length; i++)
			{
				JRFillSubreportReturnValue returnValue = returnValues[i];
				if (returnValue.isDerived())
				{
					// internally created, not checking
					continue;
				}
				
				String subreportVariableName = returnValue.getSubreportVariable();
				JRVariable subrepVariable = sourceContext.getVariable(subreportVariableName);
				if (subrepVariable == null)
				{
					throw 
						new JRException(
							EXCEPTION_MESSAGE_KEY_SOURCE_NOT_FOUND,  
							new Object[]{subreportVariableName, returnValue.getToVariable()} 
							);
				}
				
				JRVariable variable = filler.getVariable(returnValue.getToVariable());
				if (
					returnValue.getCalculationValue() == CalculationEnum.COUNT
					|| returnValue.getCalculationValue() == CalculationEnum.DISTINCT_COUNT
					)
				{
					if (!Number.class.isAssignableFrom(variable.getValueClass()))
					{
						throw 
							new JRException(
								EXCEPTION_MESSAGE_KEY_NUMERIC_TYPE_REQUIRED,  
								new Object[]{returnValue.getToVariable()} 
								);
					}
				}
				else if (!variable.getValueClass().isAssignableFrom(subrepVariable.getValueClass()) &&
						!(Number.class.isAssignableFrom(variable.getValueClass()) && Number.class.isAssignableFrom(subrepVariable.getValueClass())))
				{
					throw 
						new JRException(
							EXCEPTION_MESSAGE_KEY_VARIABLE_NOT_ASSIGNABLE,  
							new Object[]{returnValue.getToVariable(), subreportVariableName}
							);
				}
			}
		}
	}
	
}
