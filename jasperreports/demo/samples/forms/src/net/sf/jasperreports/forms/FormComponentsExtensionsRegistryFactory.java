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
package net.sf.jasperreports.forms;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import net.sf.jasperreports.engine.JRPropertiesMap;
import net.sf.jasperreports.engine.component.ComponentManager;
import net.sf.jasperreports.engine.component.ComponentsBundle;
import net.sf.jasperreports.engine.component.DefaultComponentXmlParser;
import net.sf.jasperreports.engine.component.DefaultComponentsBundle;
import net.sf.jasperreports.extensions.ExtensionsRegistry;
import net.sf.jasperreports.extensions.ExtensionsRegistryFactory;
import net.sf.jasperreports.forms.textinput.TextInputComponentCompiler;
import net.sf.jasperreports.forms.textinput.TextInputComponentDesignConverter;
import net.sf.jasperreports.forms.textinput.TextInputComponentDigester;
import net.sf.jasperreports.forms.textinput.TextInputComponentFillFactory;
import net.sf.jasperreports.forms.textinput.TextInputComponentManager;

/**
 * Extension registry factory that includes built-in component element
 * implementations.
 * 
 * <p>
 * This registry factory is registered by default in JasperReports.
 * 
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public class FormComponentsExtensionsRegistryFactory implements
		ExtensionsRegistryFactory
{

	public static final String NAMESPACE = 
		"http://jasperreports.sourceforge.net/forms";
	public static final String XSD_LOCATION = 
		"http://jasperreports.sourceforge.net/xsd/forms.xsd";
	public static final String XSD_RESOURCE = 
		"net/sf/jasperreports/forms/forms.xsd";
	
	protected static final String TEXT_INPUT_COMPONENT_NAME = "textInput";
	private static final ExtensionsRegistry REGISTRY;
	
	static
	{
		final DefaultComponentsBundle bundle = new DefaultComponentsBundle();

		TextInputComponentDigester textInputDigester = new TextInputComponentDigester();
		
		DefaultComponentXmlParser parser = new DefaultComponentXmlParser();
		parser.setNamespace(NAMESPACE);
		parser.setPublicSchemaLocation(XSD_LOCATION);
		parser.setInternalSchemaResource(XSD_RESOURCE);
		parser.setDigesterConfigurer(textInputDigester);
		bundle.setXmlParser(parser);
		
		HashMap<String, ComponentManager> componentManagers = new HashMap<String, ComponentManager>();
		
		TextInputComponentManager textInputManager = new TextInputComponentManager();
		textInputManager.setDesignConverter(TextInputComponentDesignConverter.getInstance());
		textInputManager.setComponentCompiler(new TextInputComponentCompiler());
		textInputManager.setComponentFillFactory(new TextInputComponentFillFactory());
		componentManagers.put(TEXT_INPUT_COMPONENT_NAME, textInputManager);
		
		bundle.setComponentManagers(componentManagers);
		
		REGISTRY = new ExtensionsRegistry()
		{
			@SuppressWarnings("unchecked")
			public <T> List<T> getExtensions(Class<T> extensionType) 
			{
				if (ComponentsBundle.class.equals(extensionType))
				{
					return Collections.singletonList((T)bundle);
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
