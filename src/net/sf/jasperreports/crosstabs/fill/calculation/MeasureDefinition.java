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
package net.sf.jasperreports.crosstabs.fill.calculation;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.fill.AbstractValueProvider;
import net.sf.jasperreports.engine.fill.JRCalculable;
import net.sf.jasperreports.engine.fill.JRExtendedIncrementer;
import net.sf.jasperreports.engine.fill.JRExtendedIncrementerFactory;

/**
 * Crosstab measure definition.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public class MeasureDefinition
{
	protected final byte calculation;
	protected final JRExtendedIncrementerFactory incrementerFactory;
	protected final Class valueClass;
	protected final boolean isSystemDefined;
	
	protected final JRExtendedIncrementer incrementer;
	
	
	/**
	 * Create a measure definition.
	 * 
	 * @param valueClass the value class
	 * @param calculation the calculation type
	 * @param incrementerFactory the incrementer factory
	 */
	public MeasureDefinition(
			Class valueClass, 
			byte calculation, 
			JRExtendedIncrementerFactory incrementerFactory) 
	{
		this(valueClass, calculation, incrementerFactory, false);
	}
	
	protected MeasureDefinition(
			Class valueClass, 
			byte calculation, 
			JRExtendedIncrementerFactory incrementerFactory, 
			boolean isSystemDefined)
	{
		this.valueClass = valueClass;
		this.calculation = calculation;
		this.incrementerFactory = incrementerFactory;
		this.isSystemDefined = isSystemDefined;
		this.incrementer = getIncrementer();
	}
	
	
	/**
	 * Creates a helper measure for a specific calculation.
	 * 
	 * @param measure the measure
	 * @param helperCalculation the calculation
	 * @return the helper measure having the specified calculation
	 */
	public static MeasureDefinition createHelperMeasure(MeasureDefinition measure, byte helperCalculation)
	{
		return new MeasureDefinition(measure.valueClass, helperCalculation, measure.incrementerFactory, true);
	}

	
	/**
	 * Returns the calculation type.
	 * 
	 * @return the calculation type
	 */
	public byte getCalculation()
	{
		return calculation;
	}

	
	/**
	 * Returns the incrementer factory.
	 * 
	 * @return the incrementer factory
	 */
	public JRExtendedIncrementerFactory getIncrementerFactory()
	{
		return incrementerFactory;
	}
	
	
	/**
	 * Returns the incrementer used for this measure.
	 * 
	 * @return the incrementer used for this measure
	 */
	public JRExtendedIncrementer getIncrementer()
	{
		return incrementerFactory.getExtendedIncrementer(calculation);
	}
	
	protected boolean isSystemDefined()
	{
		return isSystemDefined;
	}
	
	
	/**
	 * Returns the measure value class.
	 * 
	 * @return the measure value class
	 */
	public Class getValueClass()
	{
		return valueClass;
	}
	
	
	/**
	 * Measure value provider.
	 */
	protected static final AbstractValueProvider VALUE_PROVIDER = new AbstractValueProvider()
	{
		public Object getValue(JRCalculable calculable)
		{
			return calculable.getValue();
		}
	};

	
	/**
	 * An accumulated value of a crosstab measure.
	 *  
	 * @author Lucian Chirita (lucianc@users.sourceforge.net)
	 */
	public class MeasureValue implements JRCalculable
	{
		private Object value;
		private MeasureValue[] helpers;
		private boolean initialized;
		
		
		/**
		 * Initializes the value.
		 */
		public MeasureValue()
		{
			this.value = null;
			this.helpers = new MeasureValue[HELPER_SIZE];
			
			init();
		}
		
		protected void init()
		{
			this.value = incrementer.initialValue();
			setInitialized(true);
		}
		
		
		/**
		 * Accumulates a value.
		 * 
		 * @param addValue the value
		 * @throws JRException
		 */
		public void addValue(Object addValue) throws JRException
		{
			this.value = incrementer.increment(this, addValue, VALUE_PROVIDER);
			setInitialized(false);
		}
		
		
		/**
		 * Accumulates another measure value.
		 * <p>
		 * This is used for total calculations, when two accumulated values are combined into a total.
		 * 
		 * @param measureValue the measure value
		 * @throws JRException
		 */
		public void addValue(MeasureValue measureValue) throws JRException
		{
			if (!measureValue.isInitialized())
			{
				this.value = incrementer.combine(this, measureValue, VALUE_PROVIDER);
				setInitialized(false);
			}
		}
		
		public Object getValue()
		{
			return value;
		}
		
		public String toString()
		{
			return String.valueOf(getValue());
		}

		
		/**
		 * Sets a helper variable.
		 * 
		 * @param helperVariable the helper variable
		 * @param type the helper type
		 * @return the previous helper variable for the type
		 */
		public MeasureValue setHelper(MeasureValue helperVariable, byte type)
		{
			MeasureValue old = helpers[type]; 
			helpers[type] = helperVariable;
			return old;
		}

		public boolean isInitialized()
		{
			return initialized;
		}

		public Object getIncrementedValue()
		{
			return value;
		}

		public JRCalculable getHelperVariable(byte helperType)
		{
			return helpers[helperType];
		}

		public void setInitialized(boolean isInitialized)
		{
			this.initialized = isInitialized;
		}
	}
}
