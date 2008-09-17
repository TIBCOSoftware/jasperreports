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
package net.sf.jasperreports.engine.component;

import java.util.Map;
import java.util.Set;

import net.sf.jasperreports.engine.JRRuntimeException;

/**
 * The default {@link ComponentsBundle components bundle} implementation.
 *
 * <p>
 * A components bundle consists of a {@link ComponentsXmlParser XML parser}
 * instance and a map of {@link ComponentManager component managers}.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id: JRCrosstab.java 1741 2007-06-08 10:53:33Z lucianc $
 */
public class DefaultComponentsBundle implements ComponentsBundle
{

	private ComponentsXmlParser xmlParser;
	private Map componentManagers;

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

	public Set getComponentNames()
	{
		return componentManagers.keySet();
	}
	
	public ComponentManager getComponentManager(String componentName)
	{
		ComponentManager manager = (ComponentManager) componentManagers.get(componentName);
		if (manager == null)
		{
			throw new JRRuntimeException("No component manager found for name " + componentName 
					+ ", namespace " + xmlParser.getNamespace());
		}
		return manager;
	}
	
	/**
	 * Returns the internal map of component managers, indexed by component name.
	 * 
	 * @return the map of component managers
	 * @see #setComponentManagers(Map)
	 */
	public Map getComponentManagers()
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
	public void setComponentManagers(Map componentManagers)
	{
		this.componentManagers = componentManagers;
	}
	
}
