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
package net.sf.jasperreports.engine.data;

import org.apache.commons.beanutils.PropertyUtils;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JRBeanArrayDataSource extends JRAbstractBeanDataSource
{
	

	/**
	 *
	 */
	private Object[] data = null;
	private int index = -1;
	

	/**
	 *
	 */
	public JRBeanArrayDataSource(Object[] beanArray)
	{
		this(beanArray, true);
	}
	

	/**
	 *
	 */
	public JRBeanArrayDataSource(Object[] beanArray, boolean isUseFieldDescription)
	{
		super(isUseFieldDescription);
		
		this.data = beanArray;
	}
	

	/**
	 *
	 */
	public boolean next() throws JRException
	{
		this.index++;

		if (this.data != null)
		{
			return (this.index < this.data.length);
		}
		else
		{
			return false;
		}
	}
	
	
	/**
	 *
	 */
	public Object getFieldValue(JRField field) throws JRException
	{
		Object value = null;
		
		Object bean = data[this.index];

		if (bean != null)
		{
			String propertyName = propertyNameProvider.getPropertyName(field);
			
			try
			{
				value = PropertyUtils.getProperty(bean, propertyName);
			}
			catch (java.lang.IllegalAccessException e)
			{
				throw new JRException("Error retrieving field value from bean : " + propertyName, e);
			}
			catch (java.lang.reflect.InvocationTargetException e)
			{
				throw new JRException("Error retrieving field value from bean : " + propertyName, e);
			}
			catch (java.lang.NoSuchMethodException e)
			{
				throw new JRException("Error retrieving field value from bean : " + propertyName, e);
			}
		}

		return value;
	}

	
	/**
	 *
	 */
	public void moveFirst() throws JRException
	{
		this.index = -1;
	}


}
