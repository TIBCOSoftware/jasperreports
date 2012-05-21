/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2011 Jaspersoft Corporation. All rights reserved.
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
import net.sf.jasperreports.engine.type.CalculationEnum;
import net.sf.jasperreports.engine.util.JRClassLoader;


/**
 * Implementation of {@link net.sf.jasperreports.engine.JRSubreportReturnValue JRSubreportReturnValue}
 * used by the filler.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public class JRFillSubreportReturnValue implements JRSubreportReturnValue
{
	protected final JRSubreportReturnValue parent;

	protected JRIncrementer incrementer;
	
	protected final JRBaseFiller filler;


	protected JRFillSubreportReturnValue(
		JRSubreportReturnValue returnValue, 
		JRFillObjectFactory factory, JRBaseFiller filler
		)
	{
		factory.put(returnValue, this);

		parent = returnValue;
		
		this.filler = filler;
	}

	public String getSubreportVariable()
	{
		return parent.getSubreportVariable();
	}

	public String getToVariable()
	{
		return parent.getToVariable();
	}
		
	public String getIncrementerFactoryClassName()
	{
		return parent.getIncrementerFactoryClassName();
	}
		
	public CalculationEnum getCalculationValue()
	{
		return parent.getCalculationValue();
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
					throw new JRRuntimeException("Increment class " + incrementerFactoryClassName + " not found.", e);
				}
			}
			
			incrementer = incrementerFactory.getIncrementer(getCalculationValue().getValue());
		}
		
		return incrementer;
	}
	
	/**
	 *
	 */
	public Object clone() 
	{
		throw new UnsupportedOperationException();
	}
}
