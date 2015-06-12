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

import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.JRSubreportReturnValue;
import net.sf.jasperreports.engine.JRVariable;
import net.sf.jasperreports.engine.ReturnValue;
import net.sf.jasperreports.engine.type.CalculationEnum;
import net.sf.jasperreports.engine.util.JRClassLoader;


/**
 * Implementation of {@link net.sf.jasperreports.engine.JRSubreportReturnValue JRSubreportReturnValue}
 * used by the filler.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 */
public class JRFillSubreportReturnValue implements JRSubreportReturnValue
{
	public static final String EXCEPTION_MESSAGE_KEY_INCREMENTER_CLASS_NOT_FOUND = "fill.subreport.return.value.incrementer.class.not.found";

	protected final String fromVariable;
	protected final String toVariable;
	protected final String incrementerFactoryClassName;
	protected final CalculationEnum calculation;
	private boolean derived;

	protected JRIncrementer incrementer;
	
	protected final BaseReportFiller filler;


	protected JRFillSubreportReturnValue(
		JRSubreportReturnValue returnValue, 
		JRFillObjectFactory factory, BaseReportFiller filler
		)
	{
		factory.put(returnValue, this);

		fromVariable = returnValue.getSubreportVariable();
		toVariable = returnValue.getToVariable();
		incrementerFactoryClassName = returnValue.getIncrementerFactoryClassName();
		calculation = returnValue.getCalculationValue();
		
		this.filler = filler;
	}

	public JRFillSubreportReturnValue(ReturnValue returnValue,
			JRFillObjectFactory factory, JRBaseFiller filler)
	{
		factory.put(returnValue, this);

		fromVariable = returnValue.getFromVariable();
		toVariable = returnValue.getToVariable();
		incrementerFactoryClassName = returnValue.getIncrementerFactoryClassName();
		calculation = returnValue.getCalculation();
		
		this.filler = filler;
	}

	/**
	 * Cloning constructor.
	 * 
	 * @param fillReturnValue the object to clone
	 * @param factory the clone factory
	 */
	protected JRFillSubreportReturnValue(JRFillSubreportReturnValue fillReturnValue, JRFillCloneFactory factory)
	{
		this.fromVariable = fillReturnValue.fromVariable;
		this.toVariable = fillReturnValue.toVariable;
		this.incrementerFactoryClassName = fillReturnValue.incrementerFactoryClassName;
		this.calculation = fillReturnValue.calculation;
		this.derived = fillReturnValue.derived;
		this.incrementer = fillReturnValue.incrementer;
		this.filler = fillReturnValue.filler;
	}

	public String getSubreportVariable()
	{
		return fromVariable;
	}

	public String getToVariable()
	{
		return toVariable;
	}
		
	public String getIncrementerFactoryClassName()
	{
		return incrementerFactoryClassName;
	}
		
	public CalculationEnum getCalculationValue()
	{
		return calculation;
	}

		
	/**
	 * Gets the incrementer to be used for this copied value.
	 */
	public JRIncrementer getIncrementer()
	{
		if (incrementer == null)
		{
			String incrementerFactoryClassName = getIncrementerFactoryClassName();
			
			JRIncrementerFactory incrementerFactory;
			if (incrementerFactoryClassName == null)
			{
				JRVariable toVariable = filler.getVariable(getToVariable());
				incrementerFactory = JRDefaultIncrementerFactory.getFactory(toVariable.getValueClass());
			}
			else
			{
				try
				{
					Class<?> incrementerFactoryClass = JRClassLoader.loadClassForName(incrementerFactoryClassName);
					incrementerFactory = JRIncrementerFactoryCache.getInstance(incrementerFactoryClass); 
				}
				catch (ClassNotFoundException e)
				{
					throw 
						new JRRuntimeException(
							EXCEPTION_MESSAGE_KEY_INCREMENTER_CLASS_NOT_FOUND,
							new Object[]{incrementerFactoryClassName},
							e);
				}
			}
			
			incrementer = incrementerFactory.getIncrementer(getCalculationValue().getValue());
		}
		
		return incrementer;
	}

	public boolean isDerived()
	{
		return derived;
	}

	public void setDerived(boolean derived)
	{
		this.derived = derived;
	}
	
	/**
	 *
	 */
	public Object clone() 
	{
		throw new UnsupportedOperationException();
	}
}
