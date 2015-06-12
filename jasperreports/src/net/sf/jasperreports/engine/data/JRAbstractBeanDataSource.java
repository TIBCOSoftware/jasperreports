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
package net.sf.jasperreports.engine.data;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.engine.JRRewindableDataSource;

import org.apache.commons.beanutils.NestedNullException;
import org.apache.commons.beanutils.PropertyUtils;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public abstract class JRAbstractBeanDataSource implements JRRewindableDataSource
{
	
	public static final String EXCEPTION_MESSAGE_KEY_BEAN_FIELD_VALUE_NOT_RETRIEVED = "data.bean.field.value.not.retrieved";
	/**
	 * Field mapping that produces the current bean.
	 * <p/>
	 * If the field name/description matches this constant (the case is important),
	 * the data source will return the current bean as the field value.
	 */
	public static final String CURRENT_BEAN_MAPPING = "_THIS";

	/**
	 *
	 */
	protected PropertyNameProvider propertyNameProvider;

	protected static final PropertyNameProvider FIELD_NAME_PROPERTY_NAME_PROVIDER =
		new PropertyNameProvider()
		{
			public String getPropertyName(JRField field) 
			{
				return field.getName();
			}
		};

	protected static final PropertyNameProvider FIELD_DESCRIPTION_PROPERTY_NAME_PROVIDER =
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

	/**
	 *
	 */
	public JRAbstractBeanDataSource(boolean isUseFieldDescription)
	{
		propertyNameProvider = isUseFieldDescription ? 
				FIELD_DESCRIPTION_PROPERTY_NAME_PROVIDER : 
				FIELD_NAME_PROPERTY_NAME_PROVIDER;
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
		return getBeanProperty(bean, getPropertyName(field));
	}
	
	protected static Object getBeanProperty(Object bean, String propertyName) throws JRException
	{
		Object value = null;
		
		if (isCurrentBeanMapping(propertyName))
		{
			value = bean;
		}
		else if (bean != null)
		{
			try
			{
				value = PropertyUtils.getProperty(bean, propertyName);
			}
			catch (java.lang.IllegalAccessException e)
			{
				throw 
					new JRException(
						EXCEPTION_MESSAGE_KEY_BEAN_FIELD_VALUE_NOT_RETRIEVED,
						new Object[]{propertyName}, 
						e);
			}
			catch (java.lang.reflect.InvocationTargetException e)
			{
				throw 
					new JRException(
						EXCEPTION_MESSAGE_KEY_BEAN_FIELD_VALUE_NOT_RETRIEVED,
						new Object[]{propertyName}, 
						e);
			}
			catch (java.lang.NoSuchMethodException e)
			{
				throw 
					new JRException(
						EXCEPTION_MESSAGE_KEY_BEAN_FIELD_VALUE_NOT_RETRIEVED,
						new Object[]{propertyName}, 
						e);
			}
			catch (NestedNullException e)
			{
				// deliberately to be ignored
			}
		}

		return value;
	}

	protected static boolean isCurrentBeanMapping(String propertyName)
	{
		return CURRENT_BEAN_MAPPING.equals(propertyName);
	}

	protected String getPropertyName(JRField field)
	{
		return propertyNameProvider.getPropertyName(field);
	}
}
