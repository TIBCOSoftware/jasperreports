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
package net.sf.jasperreports.engine.part;

import java.util.Map;
import java.util.Set;

import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.component.ComponentsXmlParser;

/**
 * The default {@link PartComponentsBundle components bundle} implementation.
 *
 * <p>
 * A components bundle consists of a {@link ComponentsXmlParser XML parser}
 * instance and a map of {@link PartComponentManager component managers}.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id: DefaultComponentsBundle.java 5878 2013-01-07 20:23:13Z teodord $
 */
public class DefaultPartComponentsBundle implements PartComponentsBundle
{
	public static final String EXCEPTION_MESSAGE_KEY_COMPONENT_MANAGER_NOT_FOUND = "engine.part.component.manager.not.found";
	
	private ComponentsXmlParser xmlParser;
	private Map<String,PartComponentManager> componentManagers;

	public ComponentsXmlParser getXmlParser()
	{
		return xmlParser;
	}

	/**
	 * Sets the components XML parser implementation.
	 * 
	 * @param xmlParser the components XML parser
	 * @see #getXmlParser()
	 */
	public void setXmlParser(ComponentsXmlParser xmlParser)
	{
		this.xmlParser = xmlParser;
	}

	public Set<String> getComponentNames()
	{
		return componentManagers.keySet();
	}
	
	public PartComponentManager getComponentManager(String componentName)
	{
		PartComponentManager manager = componentManagers.get(componentName);
		if (manager == null)
		{
			throw 
				new JRRuntimeException(
					EXCEPTION_MESSAGE_KEY_COMPONENT_MANAGER_NOT_FOUND,
					new Object[]{componentName, xmlParser.getNamespace()});
		}
		return manager;
	}
	
	/**
	 * Returns the internal map of component managers, indexed by component name.
	 * 
	 * @return the map of component managers
	 * @see #setComponentManagers(Map)
	 */
	public Map<String,PartComponentManager> getComponentManagers()
	{
		return componentManagers;
	}

	/**
	 * Sets the map of component managers.
	 * 
	 * <p>
	 * The map needs to use component names as keys, and {@link PartComponentManager}
	 * instances as values.
	 * 
	 * @param componentManagers the map of component managers
	 */
	public void setComponentManagers(Map<String,PartComponentManager> componentManagers)
	{
		this.componentManagers = componentManagers;
	}
	
}
