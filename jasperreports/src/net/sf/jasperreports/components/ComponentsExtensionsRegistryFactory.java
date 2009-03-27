/*
 * ============================================================================
 * GNU Lesser General Public License
 * ============================================================================
 *
 * JasperReports - Free Java report-generating library.
 * Copyright (C) 2001-2009 JasperSoft Corporation http://www.jaspersoft.com
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
 * 539 Bryant Street, Suite 100
 * San Francisco, CA 94107
 * http://www.jaspersoft.com
 */
package net.sf.jasperreports.components;

import java.util.Collections;
import java.util.List;

import net.sf.jasperreports.components.list.FillListFactory;
import net.sf.jasperreports.components.list.ListComponent;
import net.sf.jasperreports.components.list.ListComponentCompiler;
import net.sf.jasperreports.engine.JRPropertiesMap;
import net.sf.jasperreports.engine.component.ComponentsBundle;
import net.sf.jasperreports.engine.component.DefaultComponentManager;
import net.sf.jasperreports.engine.component.DefaultComponentXmlParser;
import net.sf.jasperreports.engine.component.DefaultComponentsBundle;
import net.sf.jasperreports.extensions.ExtensionsRegistry;
import net.sf.jasperreports.extensions.ExtensionsRegistryFactory;

/**
 * Extension registry factory that includes built-in component element
 * implementations.
 * 
 * <p>
 * This registry factory is registered by default in JasperReports.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 * @see ListComponent
 */
public class ComponentsExtensionsRegistryFactory implements
		ExtensionsRegistryFactory
{

	protected static final String NAMESPACE = 
		"http://jasperreports.sourceforge.net/jasperreports/components";
	protected static final String XSD_LOCATION = 
		"http://jasperreports.sourceforge.net/xsd/components.xsd";
	protected static final String XSD_RESOURCE = 
		"net/sf/jasperreports/components/components.xsd";
	protected static final String LIST_COMPONENT_NAME = "list";
	
	private static final ExtensionsRegistry REGISTRY;
	
	static
	{
		final DefaultComponentsBundle bundle = new DefaultComponentsBundle();

		ComponentsXmlHandler xmlHandler = new ComponentsXmlHandler();
		
		DefaultComponentXmlParser parser = new DefaultComponentXmlParser();
		parser.setNamespace(NAMESPACE);
		parser.setPublicSchemaLocation(XSD_LOCATION);
		parser.setInternalSchemaResource(XSD_RESOURCE);
		parser.setDigesterConfigurer(xmlHandler);
		bundle.setXmlParser(parser);
		
		DefaultComponentManager listManager = new DefaultComponentManager();
		listManager.setComponentCompiler(new ListComponentCompiler());
		listManager.setComponentXmlWriter(xmlHandler);
		listManager.setComponentFillFactory(new FillListFactory());
		bundle.setComponentManagers(
				Collections.singletonMap(LIST_COMPONENT_NAME, listManager));
		
		REGISTRY = new ExtensionsRegistry()
		{
			public List getExtensions(Class extensionType)
			{
				if (ComponentsBundle.class.equals(extensionType))
				{
					return Collections.singletonList(bundle);
				}
				
				return null;
			}
		};
	}
	
	public ExtensionsRegistry createRegistry(String registryId,
			JRPropertiesMap properties)
	{
		return REGISTRY;
	}

}
