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
package net.sf.jasperreports.engine.xml;

import java.io.IOException;
import java.util.List;

import net.sf.jasperreports.engine.export.JRXmlExporter;
import net.sf.jasperreports.extensions.ExtensionsEnvironment;
import net.sf.jasperreports.extensions.ExtensionsRegistry;

import org.apache.commons.collections.ReferenceMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Class the provides access to {@link XmlValueHandler XML value handlers}.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public class XmlValueHandlerUtils
{

	private static final Log log = LogFactory.getLog(XmlValueHandlerUtils.class);
	
	private static final XmlValueHandlerUtils INSTANCE = new XmlValueHandlerUtils();
	
	/**
	 * Returns the singleton instance.
	 * 
	 * @return
	 */
	public static XmlValueHandlerUtils instance()
	{
		return INSTANCE;
	}
	
	private final ReferenceMap cache;
	
	private XmlValueHandlerUtils()
	{
		cache = new ReferenceMap(ReferenceMap.WEAK, ReferenceMap.HARD);
	}
	
	/**
	 * Returns a list of XML value handlers.
	 * 
	 * @return
	 */
	public List<XmlValueHandler> getHandlers()
	{
		Object cacheKey = ExtensionsEnvironment.getExtensionsCacheKey();
		synchronized (cache)
		{
			@SuppressWarnings("unchecked")
			List<XmlValueHandler> handlers = (List<XmlValueHandler>) cache.get(cacheKey);
			if (handlers == null)
			{
				ExtensionsRegistry extensionsRegistry = ExtensionsEnvironment.getExtensionsRegistry();
				handlers = extensionsRegistry.getExtensions(XmlValueHandler.class);
				cache.put(cacheKey, handlers);
			}
			return handlers;
		}
	}

	/**
	 * Outputs the XML representation of a value if the value is supported by any handler.
	 * 
	 * @param value the value
	 * @param exporter the exporter
	 * @return <code>true</code> iff a handler that supports the value was found
	 * @throws IOException
	 * @see {@link XmlValueHandler#writeToXml(Object, JRXmlExporter)}
	 */
	public boolean writeToXml(Object value, JRXmlExporter exporter) throws IOException
	{
		for (XmlValueHandler handler : getHandlers())
		{
			if (handler.writeToXml(value, exporter))
			{
				if (log.isDebugEnabled())
				{
					log.debug("Handler " + handler + " wrote value " + value);
				}
				
				return true;
			}
		}
		
		if (log.isDebugEnabled())
		{
			log.debug("No handler wrote value " + value);
		}
		
		return false;
	}
	
}
