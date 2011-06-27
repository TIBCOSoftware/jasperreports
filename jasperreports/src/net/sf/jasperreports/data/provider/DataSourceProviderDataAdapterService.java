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
package net.sf.jasperreports.data.provider;

import java.util.Map;

import net.sf.jasperreports.data.AbstractClasspathAwareDataAdapterService;
import net.sf.jasperreports.engine.JRDataSourceProvider;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.util.CompositeClassloader;
import net.sf.jasperreports.engine.util.JRClassLoader;

/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id: JRBaseBand.java 4319 2011-05-17 09:22:14Z teodord $
 */
public class DataSourceProviderDataAdapterService extends AbstractClasspathAwareDataAdapterService 
{

	public DataSourceProviderDataAdapterService(
			DataSourceProviderDataAdapter dsDataAdapter) {
		super(dsDataAdapter);
	}

	public DataSourceProviderDataAdapter getDataSourceProviderDataAdapter() {
		return (DataSourceProviderDataAdapter) getDataAdapter();
	}

	@Override
	public void contributeParameters(Map<String, Object> parameters) throws JRException 
	{
		DataSourceProviderDataAdapter dsDataAdapter = getDataSourceProviderDataAdapter();
		if (dsDataAdapter != null) 
		{
			JRDataSourceProvider provider = null;
			
			ClassLoader oldThreadClassLoader = Thread.currentThread().getContextClassLoader();

			try 
			{
				Thread.currentThread().setContextClassLoader(
					new CompositeClassloader(getClassLoader(), oldThreadClassLoader)
					);

				Class<?> clazz = JRClassLoader.loadClassForRealName(dsDataAdapter.getProviderClass());
				provider = (JRDataSourceProvider) clazz.newInstance();
				// FIXME: I don't have a report, why I need a report??!
			} catch (ClassNotFoundException e) {
				throw new JRException(e);
			} catch (IllegalAccessException e) {
				throw new JRException(e);
			} catch (InstantiationException e) {
				throw new JRException(e);
			}
			finally
			{
				Thread.currentThread().setContextClassLoader(oldThreadClassLoader);
			}

			parameters.put(JRParameter.REPORT_DATA_SOURCE, provider.create(null));
		}
	}

}
