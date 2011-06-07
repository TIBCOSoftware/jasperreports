/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2009 Jaspersoft Corporation. All rights reserved.
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
package net.sf.jasperreports.extensions;

import java.util.Collections;
import java.util.List;

import net.sf.jasperreports.charts.ChartThemeBundle;
import net.sf.jasperreports.engine.JRPropertiesMap;
import net.sf.jasperreports.engine.export.FlashPrintElement;
import net.sf.jasperreports.engine.export.GenericElementHandlerBundle;
import net.sf.jasperreports.engine.fill.DefaultChartTheme;
import net.sf.jasperreports.engine.query.DefaultQueryExecuterFactoryBundle;
import net.sf.jasperreports.engine.query.QueryExecuterFactoryBundle;
import net.sf.jasperreports.engine.scriptlets.DefaultScriptletFactory;
import net.sf.jasperreports.engine.scriptlets.ScriptletFactory;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class DefaultExtensionsRegistryFactory implements ExtensionsRegistryFactory
{
	private static final ExtensionsRegistry defaultExtensionsRegistry = 
		new ExtensionsRegistry()
		{
			public List<?> getExtensions(Class<?> extensionType) 
			{
				if (QueryExecuterFactoryBundle.class.equals(extensionType))
				{
					return Collections.singletonList(DefaultQueryExecuterFactoryBundle.getInstance());
				}
				else if (ScriptletFactory.class.equals(extensionType))
				{
					return Collections.singletonList(DefaultScriptletFactory.getInstance());
				}
				else if (ChartThemeBundle.class.equals(extensionType))
				{
					return Collections.singletonList(DefaultChartTheme.BUNDLE);
				}
				else if (GenericElementHandlerBundle.class.equals(extensionType))
				{
					return Collections.singletonList(FlashPrintElement.getHandlerBundle());
				}
				return null;
			}
		};
	
	public ExtensionsRegistry createRegistry(String registryId, JRPropertiesMap properties) 
	{
		return defaultExtensionsRegistry;
	}
}
