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

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

import net.sf.jasperreports.engine.util.FileResolver;
import net.sf.jasperreports.engine.util.JRResourcesUtil;
import net.sf.jasperreports.engine.util.SimpleFileResolver;

/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public abstract class AbstractClasspathAwareDataAdapterService extends AbstractDataAdapterService 
{

	/**
	 *
	 */
	public AbstractClasspathAwareDataAdapterService(ClasspathAwareDataAdapter dataAdapter) {
		super(dataAdapter);
	}

	/**
	 *
	 */
	protected ClassLoader getClassLoader()
	{
		FileResolver fileResolver = JRResourcesUtil.getFileResolver(null);
		if (fileResolver == null)
		{
			SimpleFileResolver sfr = new SimpleFileResolver(new File("."));//FIXMEREPO
			sfr.setResolveAbsolutePath(true);
			fileResolver = sfr;
		}

		ClasspathAwareDataAdapter dataAdapter = (ClasspathAwareDataAdapter)getDataAdapter();
		List<String> classpath = dataAdapter.getClasspath();
		List<URL> urls = new ArrayList<URL>();
		for (String path : classpath) 
		{
			File file = fileResolver.resolveFile(path);

			if (file != null && file.exists()) {
				try {
					urls.add(file.toURI().toURL());
				} catch (MalformedURLException e) {
					// e.printStackTrace();
					// We don't care if the entry cannot be found.
				}
			}
		}

		return new URLClassLoader(urls.toArray(new URL[urls.size()]));
	}

}
