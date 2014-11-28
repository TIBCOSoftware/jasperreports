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
package net.sf.jasperreports.engine;

import net.sf.jasperreports.engine.fill.JRBaseFiller;
import net.sf.jasperreports.engine.fill.JRFillChart;
import net.sf.jasperreports.engine.fill.JRFillChartDataset;

/**
 * Abstract implementation of {@link net.sf.jasperreports.engine.JRChartCustomizer JRChartCustomizer} that provides
 * access to parameter, variable and field values.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 */
public abstract class JRAbstractChartCustomizer implements JRChartCustomizer
{
	private JRBaseFiller filler;
	private JRFillChartDataset chartDataset;
	
	
	/**
	 * Default constructor.
	 */
	protected JRAbstractChartCustomizer()
	{
	}

	
	/**
	 * Initializes the chart customizer.
	 * 
	 * @param chartFiller the filler instance
	 * @param chart the fill chart object
	 */
	public void init(JRBaseFiller chartFiller, JRFillChart chart)
	{
		this.filler = chartFiller;
		this.chartDataset = (JRFillChartDataset) chart.getDataset();
	}
	
	
	/**
	 * Returns the value of a report parameter.
	 * 
	 * @param parameterName the parameter name
	 * @return the value of a report parameter
	 */
	protected final Object getParameterValue(String parameterName)
	{
		return getParameterValue(parameterName, false);
	}
	
	
	/**
	 * Returns the value of a report or input dataset parameter.
	 * <p>
	 * The input dataset differs from the report dataset when the chart
	 * uses a sub dataset as input.
	 * 
	 * @param parameterName the parameter name
	 * @param fromInputDataset whether the parameter belongs to the input dataset rather than the report.
	 * <p>
	 * This is usefull only when the chart uses a sub dataset as input.
	 *  
	 * @return the value of the parameter
	 */
	protected final Object getParameterValue(String parameterName, boolean fromInputDataset)
	{
		return (fromInputDataset ? chartDataset.getInputDataset() : filler.getMainDataset()).getParameterValue(parameterName);
	}
	
		
	/**
	 * Returns the value of a report variable.
	 * 
	 * @param variableName the variable name
	 * @return the value of a report variable
	 */
	protected final Object getVariableValue(String variableName)
	{
		return getVariableValue(variableName, false);
	}
	
	
	/**
	 * Returns the value of a report or input dataset variable.
	 * <p>
	 * The input dataset differs from the report dataset when the chart
	 * uses a sub dataset as input.
	 * 
	 * @param variableName the variable name
	 * @param fromInputDataset whether the variable belongs to the input dataset rather than the report.
	 * <p>
	 * This is usefull only when the chart uses a sub dataset as input.
	 *  
	 * @return the value of the variable
	 */
	protected final Object getVariableValue(String variableName, boolean fromInputDataset)
	{
		return (fromInputDataset ? chartDataset.getInputDataset() : filler.getMainDataset()).getVariableValue(variableName);
	}
	
		
	/**
	 * Returns the value of a report field.
	 * 
	 * @param fieldName the field name
	 * @return the value of a report field
	 */
	protected final Object getFieldValue(String fieldName)
	{
		return getFieldValue(fieldName, false);
	}
	
	
	/**
	 * Returns the value of a report or input dataset field.
	 * <p>
	 * The input dataset differs from the report dataset when the chart
	 * uses a sub dataset as input.
	 * 
	 * @param fieldName the field name
	 * @param fromInputDataset whether the field belongs to the input dataset rather than the report.
	 * <p>
	 * This is usefull only when the chart uses a sub dataset as input.
	 *  
	 * @return the value of the field
	 */
	protected final Object getFieldValue(String fieldName, boolean fromInputDataset)
	{
		return (fromInputDataset ? chartDataset.getInputDataset() : filler.getMainDataset()).getFieldValue(fieldName);
	}
}
