/*
 * Jaspersoft Open Studio - Eclipse-based JasperReports Designer. Copyright (C) 2005 - 2010 Jaspersoft Corporation. All
 * rights reserved. http://www.jaspersoft.com
 * 
 * Unless you have purchased a commercial license agreement from Jaspersoft, the following license terms apply:
 * 
 * This program is part of Jaspersoft Open Studio.
 * 
 * Jaspersoft Open Studio is free software: you can redistribute it and/or modify it under the terms of the GNU Affero
 * General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 * 
 * Jaspersoft Open Studio is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the
 * implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License along with Jaspersoft Open Studio. If not,
 * see <http://www.gnu.org/licenses/>.
 */
package net.sf.jasperreports.data.ds;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import net.sf.jasperreports.data.AbstractDataAdapterService;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRParameter;

/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id: JRBaseBand.java 4319 2011-05-17 09:22:14Z teodord $
 */
public class DataSourceDataAdapterService extends AbstractDataAdapterService
{
	
	public DataSourceDataAdapterService(DataSourceDataAdapter dsDataAdapter)
	{
		super(dsDataAdapter);
	}
	
	public DataSourceDataAdapter getDataSourceDataAdapter() 
	{
		return (DataSourceDataAdapter)getDataAdapter();
	}
	
	@Override
	public void contributeParameters(Map<String, Object> parameters) throws JRException 
	{
		DataSourceDataAdapter dsDataAdapter = getDataSourceDataAdapter();
		if (dsDataAdapter != null)
		{
			JRDataSource ds = null;
			try 
			{
	            Class clazz = Class.forName( dsDataAdapter.getFactoryClass(), true, Thread.currentThread().getContextClassLoader() );
	            ds = (JRDataSource) clazz.getMethod( dsDataAdapter.getMethodToCall(), new Class[0]).invoke(null,new Object[0]);
	        }
			catch (ClassNotFoundException e)
			{
				throw new JRException(e);			
			} 
			catch (NoSuchMethodException e)
			{
				throw new JRException(e);			
			} 
			catch (InvocationTargetException e)
			{
				throw new JRException(e);			
			} 
			catch (IllegalAccessException e)
			{
				throw new JRException(e);			
			} 

			parameters.put(JRParameter.REPORT_DATA_SOURCE, ds);
		}
	}
	
}
