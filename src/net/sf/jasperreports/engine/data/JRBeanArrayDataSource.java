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
package net.sf.jasperreports.engine.data;

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
	public boolean next()
	{
		this.index++;

		if (this.data != null)
		{
			return (this.index < this.data.length);
		}

		return false;
	}
	
	
	/**
	 *
	 */
	public Object getFieldValue(JRField field) throws JRException
	{
		return getFieldValue(data[this.index], field);
	}

	
	/**
	 *
	 */
	public void moveFirst()
	{
		this.index = -1;
	}

	/**
	 * Returns the underlying bean array used by this data source.
	 * 
	 * @return the underlying bean array
	 */
	public Object[] getData()
	{
		return data;
	}

	/**
	 * Returns the total number of records/beans that this data source
	 * contains.
	 * 
	 * @return the total number of records of this data source
	 */
	public int getRecordCount()
	{
		return data == null ? 0 : data.length;
	}
	
	/**
	 * Clones this data source by creating a new instance that reuses the same
	 * underlying bean array. 
	 * 
	 * @return a clone of this data source
	 */
	public JRBeanArrayDataSource cloneDataSource()
	{
		return new JRBeanArrayDataSource(data);
	}

}
