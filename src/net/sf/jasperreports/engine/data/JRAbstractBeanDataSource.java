/*
 * ============================================================================
 * GNU Lesser General Public License
 * ============================================================================
 *
 * JasperReports - Free Java report-generating library.
 * Copyright (C) 2001-2005 JasperSoft Corporation http://www.jaspersoft.com
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
 * 185, Berry Street, Suite 6200
 * San Francisco CA 94107
 * http://www.jaspersoft.com
 */
package net.sf.jasperreports.engine.data;

import org.apache.commons.beanutils.PropertyUtils;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.engine.JRRewindableDataSource;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public abstract class JRAbstractBeanDataSource implements JRRewindableDataSource
{
	

	/**
	 *
	 */
	protected PropertyNameProvider propertyNameProvider = null;
	

	/**
	 *
	 */
	public JRAbstractBeanDataSource(boolean isUseFieldDescription)
	{
		if (isUseFieldDescription)
		{
			propertyNameProvider = 
				new PropertyNameProvider()
				{
					public String getPropertyName(JRField field) 
					{
						if (field.getDescription() == null)
						{
							return field.getName();
						}
						return field.getDescription();
					}
				};
		}
		else
		{
			propertyNameProvider = 
				new PropertyNameProvider()
				{
					public String getPropertyName(JRField field) 
					{
						return field.getName();
					}
				};
		}
	}
	

	/**
	 *
	 */
	interface PropertyNameProvider
	{
		public String getPropertyName(JRField field);
	}

	
	protected Object getFieldValue(Object bean, JRField field) throws JRException
	{
		Object value = null;
		
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

}
