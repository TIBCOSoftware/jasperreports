/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2023 Cloud Software Group, Inc. All rights reserved.
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
package net.sf.jasperreports.engine.component;

import java.util.Map;
import java.util.Set;

/**
 * The default {@link ComponentsBundle components bundle} implementation.
 *
 * <p>
 * A components bundle consists of a {@link ComponentsXmlParser XML parser}
 * instance and a map of {@link ComponentManager component managers}.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 */
public class DefaultComponentsBundle implements ComponentsBundle
{

	private String namespace;
	private Map<String,ComponentManager> componentManagers;

	@Override
	public String getNamespace()
	{
		return namespace;
	}

	/**
	 * Sets the components namespace.
	 * 
	 * @param namespace the components namespace 
	 * @see #getNamespace()
	 */
	public void setNamespace(String namespace)
	{
		this.namespace = namespace;
	}

	@Override
	public Set<String> getComponentNames()
	{
		return componentManagers.keySet();
	}
	
	@Override
	public ComponentManager getComponentManager(String componentName)
	{
		ComponentManager manager = componentManagers.get(componentName);
		return manager;
	}
	
	/**
	 * Returns the internal map of component managers, indexed by component name.
	 * 
	 * @return the map of component managers
	 * @see #setComponentManagers(Map)
	 */
	public Map<String,ComponentManager> getComponentManagers()
	{
		return componentManagers;
	}

	/**
	 * Sets the map of component managers.
	 * 
	 * <p>
	 * The map needs to use component names as keys, and {@link ComponentManager}
	 * instances as values.
	 * 
	 * @param componentManagers the map of component managers
	 */
	public void setComponentManagers(Map<String,ComponentManager> componentManagers)
	{
		this.componentManagers = componentManagers;
	}
	
}
