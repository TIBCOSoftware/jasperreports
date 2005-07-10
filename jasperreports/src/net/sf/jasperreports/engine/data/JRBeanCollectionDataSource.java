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

import java.util.Collection;
import java.util.Iterator;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

import org.apache.commons.beanutils.PropertyUtils;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JRBeanCollectionDataSource extends JRAbstractBeanDataSource
{
	

	/**
	 *
	 */
	private Collection data = null;
	private Iterator iterator = null;
	private Object currentBean = null;
	

	/**
	 *
	 */
	public JRBeanCollectionDataSource(Collection beanCollection)
	{
		this(beanCollection, true);
	}
	

	/**
	 *
	 */
	public JRBeanCollectionDataSource(Collection beanCollection, boolean isUseFieldDescription)
	{
		super(isUseFieldDescription);
		
		this.data = beanCollection;

		if (this.data != null)
		{
			this.iterator = this.data.iterator();
		}
	}
	

	/**
	 *
	 */
	public boolean next()
	{
		boolean hasNext = false;
		
		if (this.iterator != null)
		{
			hasNext = this.iterator.hasNext();
			
			if (hasNext)
			{
				this.currentBean = this.iterator.next();
			}
		}
		
		return hasNext;
	}
	
	
	/**
	 *
	 */
	public Object getFieldValue(JRField field) throws JRException
	{
		Object value = null;
		
		if (currentBean != null)
		{
			String propertyName = propertyNameProvider.getPropertyName(field);
			
			try
			{
				value = PropertyUtils.getProperty(currentBean, propertyName);
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
	public void moveFirst()
	{
		if (this.data != null)
		{
			this.iterator = this.data.iterator();
		}
	}


}
