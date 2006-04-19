/*
 * ============================================================================
 * GNU Lesser General Public License
 * ============================================================================
 *
 * JasperReports - Free Java report-generating library.
 * Copyright (C) 2001-2006 JasperSoft Corporation http://www.jaspersoft.com
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
 * 303 Second Street, Suite 450 North
 * San Francisco, CA 94107
 * http://www.jaspersoft.com
 */
package net.sf.jasperreports.crosstabs.design;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import net.sf.jasperreports.crosstabs.base.JRBaseCrosstabMeasure;
import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRVariable;
import net.sf.jasperreports.engine.design.JRDesignVariable;

/**
 * Crosstab measure implementation to be used for report designing.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public class JRDesignCrosstabMeasure extends JRBaseCrosstabMeasure
{
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;
	
	public static final String PROPERTY_VALUE_CLASS = "valueClassName";

	/** Property change support mechanism. */
	private transient PropertyChangeSupport propSupport;
	
	private JRDesignVariable designVariable;
	
	
	/**
	 * Creates a crosstab measure.
	 */
	public JRDesignCrosstabMeasure()
	{
		super();
		
		variable = designVariable = new JRDesignVariable();
		designVariable.setCalculation(JRVariable.CALCULATION_SYSTEM);
		designVariable.setSystemDefined(true);
	}

	
	/**
	 * Sets the calculation type.
	 * 
	 * @param calculation the calculation type
	 * @see net.sf.jasperreports.crosstabs.JRCrosstabMeasure#getCalculation()
	 */
	public void setCalculation(byte calculation)
	{
		this.calculation = calculation;
	}

	
	/**
	 * Sets the measure value expression.
	 * 
	 * @param expression the measure value expression.
	 * @see net.sf.jasperreports.crosstabs.JRCrosstabMeasure#getValueExpression()
	 */
	public void setValueExpression(JRExpression expression)
	{
		this.expression = expression;
	}

	
	/**
	 * Sets the incrementer factory class name.
	 * 
	 * @param incrementerFactoryClassName the incrementer factory class name
	 * @see net.sf.jasperreports.crosstabs.JRCrosstabMeasure#getIncrementerFactoryClassName()
	 */
	public void setIncrementerFactoryClassName(String incrementerFactoryClassName)
	{
		this.incrementerFactoryClassName = incrementerFactoryClassName;
		this.incrementerFactoryClass = null;
	}

	
	/**
	 * Sets the measure name.
	 * 
	 * @param name the measure name
	 * @see net.sf.jasperreports.crosstabs.JRCrosstabMeasure#getName()
	 */
	public void setName(String name)
	{
		this.name = name;
		designVariable.setName(name);
	}

	
	/**
	 * Sets the percentage calculation type.
	 * 
	 * @param percentageOfType the percentage calculation type
	 * @see net.sf.jasperreports.crosstabs.JRCrosstabMeasure#getPercentageOfType()
	 */
	public void setPercentageOfType(byte percentageOfType)
	{
		this.percentageOfType = percentageOfType;
	}

	
	/**
	 * Sets the percentage calculator class name.
	 * 
	 * @param percentageCalculatorClassName the percentage calculator class name
	 * @see net.sf.jasperreports.crosstabs.JRCrosstabMeasure#getPercentageCalculatorClassName()
	 */
	public void setPercentageCalculatorClassName(String percentageCalculatorClassName)
	{
		this.percentageCalculatorClassName = percentageCalculatorClassName;
		this.percentageCalculatorClass = null;
	}

	
	/**
	 * Sets the measure value class name.
	 * 
	 * @param valueClassName the measure value class name
	 * @see net.sf.jasperreports.crosstabs.JRCrosstabMeasure#getValueClassName()
	 */
	public void setValueClassName(String valueClassName)
	{
		String oldValue = this.valueClassName;
		
		this.valueClassName = valueClassName;
		this.valueClass = null;
		designVariable.setValueClassName(valueClassName);
		
		getPropertyChangeSupport().firePropertyChange(PROPERTY_VALUE_CLASS, oldValue,
				this.valueClassName);
	}
	

	/**
	 * Add a property listener to listen to all properties of this class.
	 * 
	 * @param l
	 *            The property listener to add.
	 */
	public void addPropertyChangeListener(PropertyChangeListener l)
	{
		getPropertyChangeSupport().addPropertyChangeListener(l);
	}

	/**
	 * Add a property listener to receive property change events for only one
	 * specific property.
	 * 
	 * @param propName
	 *            The property to listen to.
	 * @param l
	 *            The property listener to add.
	 */
	public void addPropertyChangeListener(String propName, PropertyChangeListener l)
	{
		getPropertyChangeSupport().addPropertyChangeListener(propName, l);
	}

	/**
	 * Remove a property change listener. This will remove any listener that was
	 * added through either of the addPropertyListener methods.
	 * 
	 * @param l
	 *            The listener to remove.
	 */
	public void removePropertyChangeListener(PropertyChangeListener l)
	{
		getPropertyChangeSupport().removePropertyChangeListener(l);
	}

	/**
	 * Get the property change support object for this class. Because the
	 * property change support object has to be transient, it may need to be
	 * created.
	 * 
	 * @return The property change support object.
	 */
	protected PropertyChangeSupport getPropertyChangeSupport()
	{
		if (propSupport == null)
		{
			propSupport = new PropertyChangeSupport(this);
		}
		return propSupport;
	}
}
