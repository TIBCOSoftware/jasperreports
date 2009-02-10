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
package net.sf.jasperreports.extensions;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.ReferenceMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.ListableBeanFactory;

/**
 * An {@link ExtensionsRegistry extension registry} which works by looking 
 * for beans of a specific extension type in a Spring beans factory.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
//TODO generic element fallback handlers
public class SpringExtensionsRegistry implements ExtensionsRegistry
{

	private static final Log log = LogFactory.getLog(SpringExtensionsRegistry.class);
	
	private final ListableBeanFactory beanFactory;
	
	private final ReferenceMap extensionBeanNamesCache = new ReferenceMap(
			ReferenceMap.WEAK, ReferenceMap.HARD);

	/**
	 * Creates a Spring-based extension registry.
	 * 
	 * @param beanFactory the Spring bean factory
	 */
	public SpringExtensionsRegistry(ListableBeanFactory beanFactory)
	{
		this.beanFactory = beanFactory;
	}
	
	/**
	 * Returns all beans that match the extension class.
	 */
	public List getExtensions(Class extensionType)
	{
		String[] beanNames = getExtensionBeanNames(extensionType);
		List beans = new ArrayList(beanNames.length);
		for (int i = 0; i < beanNames.length; i++)
		{
			String name = beanNames[i];
			if (log.isDebugEnabled())
			{
				log.debug("Getting bean " + name + " as extension of type "
						+ extensionType.getName());
			}
			Object bean = beanFactory.getBean(name, extensionType);
			beans.add(bean);
		}
		return beans;
	}

	protected String[] getExtensionBeanNames(Class extensionType)
	{
		synchronized (extensionBeanNamesCache)
		{
			String[] beanNames = (String[]) extensionBeanNamesCache.get(extensionType);
			if (beanNames == null)
			{
				beanNames = findExtensionBeanNames(extensionType);
				extensionBeanNamesCache.put(extensionType, beanNames);
			}
			return beanNames;
		}
	}

	protected String[] findExtensionBeanNames(Class extensionType)
	{
		String[] beanNames = beanFactory.getBeanNamesForType(extensionType);
		
		if (log.isDebugEnabled())
		{
			log.debug("Found " + beanNames.length + " beans for extension type " + extensionType);
		}
		
		return beanNames;
	}

}
