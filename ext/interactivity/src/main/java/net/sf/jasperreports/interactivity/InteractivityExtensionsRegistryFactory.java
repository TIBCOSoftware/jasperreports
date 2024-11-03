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
package net.sf.jasperreports.interactivity;

import net.sf.jasperreports.components.headertoolbar.HeaderToolbarElement;
import net.sf.jasperreports.engine.JRPropertiesMap;
import net.sf.jasperreports.engine.export.GenericElementHandler;
import net.sf.jasperreports.engine.export.GenericElementHandlerBundle;
import net.sf.jasperreports.engine.export.HtmlExporter;
import net.sf.jasperreports.engine.fill.JRFillCrosstab;
import net.sf.jasperreports.engine.xml.JRXmlConstants;
import net.sf.jasperreports.extensions.ExtensionsRegistry;
import net.sf.jasperreports.extensions.ExtensionsRegistryFactory;
import net.sf.jasperreports.extensions.SingletonExtensionRegistry;
import net.sf.jasperreports.interactivity.crosstabs.CrosstabInteractiveJsonHandler;
import net.sf.jasperreports.interactivity.headertoolbar.json.HeaderToolbarElementJsonHandler;
import net.sf.jasperreports.interactivity.sort.SortElement;
import net.sf.jasperreports.interactivity.sort.SortElementHtmlHandler;
import net.sf.jasperreports.interactivity.sort.SortElementJsonHandler;
import net.sf.jasperreports.json.export.JsonExporter;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public class InteractivityExtensionsRegistryFactory implements ExtensionsRegistryFactory
{
	private static final GenericElementHandlerBundle HANDLER_BUNDLE = 
		new GenericElementHandlerBundle()
		{
			@Override
			public String getNamespace()
			{
				return JRXmlConstants.JASPERREPORTS_NAMESPACE;
			}
			
			@Override
			public GenericElementHandler getHandler(String elementName,
					String exporterKey)
			{
				switch (elementName)
				{
					case SortElement.SORT_ELEMENT_NAME:
					{
						if (HtmlExporter.HTML_EXPORTER_KEY.equals(exporterKey))
						{
							return SortElementHtmlHandler.getInstance();
						}
						else if (JsonExporter.JSON_EXPORTER_KEY.equals(exporterKey))
						{
							return new SortElementJsonHandler();
						}
					}
					case HeaderToolbarElement.ELEMENT_NAME:
					{
						if (JsonExporter.JSON_EXPORTER_KEY.equals(exporterKey))
						{
							return HeaderToolbarElementJsonHandler.getInstance();
						}
					}
					case JRFillCrosstab.CROSSTAB_INTERACTIVE_ELEMENT_NAME:
					{
						if (JsonExporter.JSON_EXPORTER_KEY.equals(exporterKey))
						{
							return CrosstabInteractiveJsonHandler.getInstance();
						}
					}
				}
				
				return null;
			}
		};

	private static final ExtensionsRegistry REGISTRY = new SingletonExtensionRegistry<>(GenericElementHandlerBundle.class, HANDLER_BUNDLE);

	@Override
	public ExtensionsRegistry createRegistry(String registryId, JRPropertiesMap properties) 
	{
		return REGISTRY;
	}
}
