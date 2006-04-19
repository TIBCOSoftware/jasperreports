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
package net.sf.jasperreports.engine.fill;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRPrintImage;
import net.sf.jasperreports.engine.JRRenderable;

/**
 * Context used to store data shared by virtualized objects resulted from a report fill process.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public class JRVirtualizationContext implements Serializable
{
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;
	
	private Map cachedRenderers;
	private Map cachedTemplates;
	
	/**
	 * Constructs a context.
	 */
	public JRVirtualizationContext()
	{
		cachedRenderers = new HashMap();
		cachedTemplates = new HashMap();
	}

	
	/**
	 * Caches an image renderer.
	 * 
	 * @param image the image whose renderer should be cached
	 */
	public void cacheRenderer(JRPrintImage image)
	{
		JRRenderable renderer = image.getRenderer();
		if (renderer != null)
		{
			cachedRenderers.put(renderer.getId(), renderer);
		}
	}

	
	/**
	 * Retrieves a cached image renderer based on an ID.
	 * 
	 * @param id the ID
	 * @return the cached image renderer for the ID
	 */
	public JRRenderable getCachedRenderer(String id)
	{
		return (JRRenderable) cachedRenderers.get(id);
	}

	
	/**
	 * Determines whether a cached image renderer for a specified ID exists.
	 * 
	 * @param id the ID
	 * @return <code>true</code> iff the context contains a cached renderer with the specified ID
	 */
	public boolean hasCachedRenderer(String id)
	{
		return cachedRenderers.containsKey(id);
	}
	
	
	/**
	 * Caches an element template.
	 * 
	 * @param template the template to cache
	 */
	public void cacheTemplate(JRTemplateElement template)
	{
		cachedTemplates.put(template.getId(), template);
	}
	
	
	/**
	 * Retrieves a cached template.
	 * 
	 * @param templateId the template ID
	 * @return the cached template having the given ID
	 */
	public JRTemplateElement getCachedTemplate(String templateId)
	{
		return (JRTemplateElement) cachedTemplates.get(templateId);
	}
}
