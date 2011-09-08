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
package net.sf.jasperreports.data;

import java.util.HashMap;
import java.util.Map;

import net.sf.jasperreports.engine.JRException;

/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public abstract class AbstractDataAdapterService implements DataAdapterService
{
	/**
	 *
	 */
	private String name;
	private DataAdapter dataAdapter;

	/**
	 * FIXME consider removing
	 */
	public AbstractDataAdapterService()
	{
	}
	  
	/**
	 *
	 */
	public AbstractDataAdapterService(DataAdapter dataAdapter)
	{
		this.dataAdapter = dataAdapter;
	}
	  
	/**
	 *
	 */
	public String getName()
	{
		return name;
	}
	  
	/**
	 *
	 */
	public void setName(String name)
	{
		this.name = name;
	}
	  
	/**
	 *
	 */
	public DataAdapter getDataAdapter()
	{
		return dataAdapter;
	}
	  
	/**
	 * FIXME consider removing
	 */
	public void setDataAdapter(DataAdapter dataAdapter)
	{
		this.dataAdapter = dataAdapter;
	}
	  
	/**
	 *
	 */
	public abstract void contributeParameters(Map<String, Object> parameters) throws JRException;
	
	/**
	 *
	 */
	public void dispose() 
	{
	}

	/**
	 *
	 */
	public void test() throws JRException
	{
		contributeParameters(new HashMap<String, Object>());
		dispose();
	}
 
}
