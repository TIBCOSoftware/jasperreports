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
package net.sf.jasperreports.engine.export;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.util.JRClassLoader;

/**
 * A {@link ExporterFilterFactory} utility class.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public final class ExporterFilterFactoryUtil
{
	//FIXME this would add commons collections to applet jar
	//private static final JRSingletonCache cache = new JRSingletonCache(ExporterFilterFactory.class);

	/**
	 * Returns an exporter filter factory based on a factory class name.
	 * 
	 * @param factoryClassName the factory class name
	 * @return an exporter filter factory instance
	 * @throws JRException
	 */
	public static ExporterFilterFactory getFilterFactory(String factoryClassName) throws JRException
	{
		//return (ExporterFilterFactory) cache.getCachedInstance(factoryClassName);
		try
		{
			Class<?> clazz = JRClassLoader.loadClassForName(factoryClassName);
			return (ExporterFilterFactory)clazz.newInstance();
		}
		catch (ClassNotFoundException e)
		{
			throw new JRException("Class " + factoryClassName + " not found.", e);
		}
		catch (InstantiationException e)
		{
			throw new JRException("Error instantiating class " + factoryClassName + ".", e);
		}
		catch (IllegalAccessException e)
		{
			throw new JRException("Error instantiating class " + factoryClassName + ".", e);
		}
	}


	private ExporterFilterFactoryUtil()
	{
	}
}
