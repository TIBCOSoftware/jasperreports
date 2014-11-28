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
package net.sf.jasperreports.web.util;

import java.util.ArrayList;
import java.util.List;

import net.sf.jasperreports.engine.JRPropertiesMap;
import net.sf.jasperreports.extensions.ExtensionsRegistry;
import net.sf.jasperreports.extensions.ExtensionsRegistryFactory;


/**
 * @author Narcis Marcu (narcism@users.sourceforge.net)
 */
public class WebResourceHandlersExtensionRegistryFactory implements ExtensionsRegistryFactory {

	protected static List<?> getResourceHandlers() {
		List<WebResourceHandler> extensions = new ArrayList<WebResourceHandler>();

		JiveWebResourceHandler jiveHandler = new JiveWebResourceHandler("net.sf.jasperreports.components.headertoolbar.messages");
		jiveHandler.addMapping("jive.templates.tmpl", "net/sf/jasperreports/components/headertoolbar/resources/require/jive.templates.tmpl");
		jiveHandler.addMapping("jive.vm.css", "net/sf/jasperreports/components/headertoolbar/resources/jive.vm.css");
		jiveHandler.addMapping("jive.sort.vm.css", "net/sf/jasperreports/components/sort/resources/jive.sort.vm.css");
		jiveHandler.addMapping("jive.filterDialog.tmpl", "net/sf/jasperreports/components/sort/resources/jive.filterDialog.tmpl");
		jiveHandler.addMapping("jive.i18n.tmpl", "net/sf/jasperreports/components/headertoolbar/resources/require/jive.i18n.tmpl");

		// crosstab resources
		jiveHandler.addMapping("jive.crosstab.templates.styles.css", "net/sf/jasperreports/crosstabs/interactive/jive.crosstab.templates.styles.css");

		extensions.add(jiveHandler);
		
		ImageWebResourceHandler imageHandler = new ImageWebResourceHandler();
		extensions.add(imageHandler);
		
		FontWebResourceHandler fontHandler = new FontWebResourceHandler();
		extensions.add(fontHandler);
		
		DefaultWebResourceHandler defaultHandler =  DefaultWebResourceHandler.getInstance();
		extensions.add(defaultHandler);
		
		return extensions;
	}

	private static final ExtensionsRegistry REGISTRY = new ExtensionsRegistry() {
		private volatile List<?> resourceHandlers;
		
		@SuppressWarnings("unchecked")
		@Override
		public <T> List<T> getExtensions(Class<T> extensionType) {
			if (!extensionType.equals(WebResourceHandler.class)) {
				return null;
			}
			
			List<?> extensions = resourceHandlers;
			if (extensions == null) {
				synchronized (this) {
					// double checking
					extensions = resourceHandlers;
					if (extensions == null) {
						extensions = getResourceHandlers();
						resourceHandlers = extensions;
					}
				}
			}
			return (List<T>) extensions;
		}
	};
	
	@Override
	public ExtensionsRegistry createRegistry(String registryId,
			JRPropertiesMap properties) {
		return REGISTRY;
	}
	
}
