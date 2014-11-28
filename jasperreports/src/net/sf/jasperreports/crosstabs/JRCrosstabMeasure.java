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
package net.sf.jasperreports.crosstabs;

import net.sf.jasperreports.crosstabs.type.CrosstabPercentageEnum;
import net.sf.jasperreports.engine.JRCloneable;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRVariable;
import net.sf.jasperreports.engine.type.CalculationEnum;

/**
 * Crosstab measure interface.
 * <p>
 * A measure is a value that is accumulated by the crosstab and is displayed
 * in the crosstab cells.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 */
public interface JRCrosstabMeasure extends JRCloneable
{
	
	/**
	 * Returns the name of the measure.
	 * 
	 * @return the name of the measure
	 * @see #getVariable()
	 */
	public String getName();
	
	
	/**
	 * Returns the name of the value class for this measure.
	 * 
	 * @return the name of the value class for this measure
	 */
	public String getValueClassName();
	
	
	/**
	 * Returns the value class of this measure.
	 * 
	 * @return the value class of this measure
	 */
	public Class<?> getValueClass();
	
	
	/**
	 * Returns the measure expression.
	 * 
	 * @return the measure expression
	 */
	public JRExpression getValueExpression();
	
	
	/**
	 * Returns the calculation type which will be performed on the measure values.
	 * <p>
	 * The incrementer factory associated with this measure will create
	 * an incrementer which will sum the measure values.
	 * <p>
	 * The possible calculation type are the same as the ones used for variables
	 * (see {@link JRVariable#getCalculationValue() JRVariable.getCalculationValue()} with
	 * the exception of {@link CalculationEnum#SYSTEM CalculationEnum.SYSTEM}.
	 * 
	 * @return the calculation type which will be performed on the measure values
	 * @see #getIncrementerFactoryClassName()
	 * @see net.sf.jasperreports.engine.fill.JRExtendedIncrementerFactory
	 * @see net.sf.jasperreports.engine.fill.JRExtendedIncrementer
	 */
	public CalculationEnum getCalculationValue();
	
	
	/**
	 * Returns the incrementer factory class name.
	 * <p>
	 * Crosstab measures require extended incrementers, therefore 
	 * the incrementer class should implement
	 * {@link net.sf.jasperreports.engine.fill.JRExtendedIncrementerFactory JRExtendedIncrementerFactory}.
	 * 
	 * @return the incrementer factory class name
	 */
	public String getIncrementerFactoryClassName();
	
	
	/**
	 * Returns the incrementer factory class.
	 * 
	 * @return the incrementer factory class
	 * @see #getIncrementerFactoryClassName()
	 */
	public Class<?> getIncrementerFactoryClass();

	
	/**
	 * Returns the percentage calculation type performed on this measure.
	 * <p>
	 * Currently, only percentage out of grand total is supported.
	 * <p>
	 * The possible values are:
	 * <ul>
	 * 	<li>{@link CrosstabPercentageEnum#NONE CrosstabPercentageEnum.NONE}</li>
	 * 	<li>{@link CrosstabPercentageEnum#GRAND_TOTAL CrosstabPercentageEnum.GRAND_TOTAL}</li>
	 * </ul>
	 * <p>
	 * If percentage calculation is required, the value class should be one of the built-in supported
	 * percentage types or the percentage calculator class should be specified.
	 * 
	 * @return the percentage calculation type
	 * @see net.sf.jasperreports.crosstabs.fill.JRPercentageCalculatorFactory#hasBuiltInCalculator(Class)
	 * @see #getPercentageCalculatorClassName()
	 */
	public CrosstabPercentageEnum getPercentageType();
	
	
	/**
	 * Returns the percentage calculator class name.
	 * 
	 * @return the percentage calculator class name
	 */
	public String getPercentageCalculatorClassName();
	
	
	/**
	 * Returns the percentage calculator class.
	 * 
	 * @return the percentage calculator class
	 */
	public Class<?> getPercentageCalculatorClass();
	
	
	/**
	 * Returns the variable associated with this measure.
	 * <p>
	 * The variable can be used inside the crosstab data cells as the
	 * measure value.  The variable has the same name and value class as
	 * the measure.
	 * 
	 * @return the variable associated with this measure
	 */
	public JRVariable getVariable();
}
