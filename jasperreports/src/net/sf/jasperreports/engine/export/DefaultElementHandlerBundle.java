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
package net.sf.jasperreports.engine.export;

import java.util.Map;

import net.sf.jasperreports.engine.JRRuntimeException;

/**
 * The default {@link GenericElementHandlerBundle} implementation.
 * 
 * <p>
 * This implementation uses a {@link Map map} to keep element handlers.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public class DefaultElementHandlerBundle implements GenericElementHandlerBundle
{

	private String namespace;
	private Map elementHandlers;

	/**
	 * Uses the handler map to locate a handler for the element name
	 * and exporter key.
	 * 
	 * @throws JRRuntimeException if no handler is found
	 */
	public GenericElementHandler getHandler(String elementName,
			String exporterKey)
	{
		Map handlers = (Map) elementHandlers.get(elementName);
		if (handlers == null)
		{
			throw new JRRuntimeException("No handlers for generic elements of type "
					+ namespace + "#" + elementName);
		}
		
		GenericElementHandler handler = (GenericElementHandler) handlers.get(
				exporterKey);
		if (handler == null)
		{
			throw new JRRuntimeException("No " + exporterKey 
					+ "handler for generic elements of type "
					+ namespace + "#" + elementName);
		}
		
		return handler;
	}

	public String getNamespace()
	{
		return namespace;
	}
	
	/**
	 * Sets the namespace of this bundle.
	 * 
	 * @param namespace the namespace
	 * @see #getNamespace()
	 */
	public void setNamespace(String namespace)
	{
		this.namespace = namespace;
	}

	/**
	 * Returns the map of element handlers.
	 * 
	 * @return the map of element handlers
	 */
	public Map getElementHandlers()
	{
		return elementHandlers;
	}

	/**
	 * Sets the map of element handlers.
	 * 
	 * <p>
	 * The map needs to be a two level map, the first one indexed by element
	 * names and the second level indexed by exporter keys.
	 * 
	 * @param elementHandlers the map of element handlers
	 */
	public void setElementHandlers(Map elementHandlers)
	{
		this.elementHandlers = elementHandlers;
	}

}
