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

/*
 * Contributors:
 * Eugene D - eugenedruy@users.sourceforge.net 
 * Adrian Jackson - iapetus@users.sourceforge.net
 * David Taylor - exodussystems@users.sourceforge.net
 * Lars Kristensen - llk@users.sourceforge.net
 */
package net.sf.jasperreports.renderers;

import java.util.HashMap;
import java.util.Map;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.type.OnErrorTypeEnum;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public class ResourceRendererCache
{
	private final RenderableUtil renderableUtil;
	private final Map<String, Renderable> loadedRenderers;
	
	
	/**
	 * 
	 */
	public ResourceRendererCache(JasperReportsContext jasperReportsContext)
	{
		this.renderableUtil = RenderableUtil.getInstance(jasperReportsContext);
		this.loadedRenderers = new HashMap<String, Renderable>();
	}


	/**
	 * 
	 */
	public Renderable getLoadedRenderer(ResourceRenderer resourceRenderer) throws JRException
	{
		Renderable loadedRenderer;
		String resourceRendererId = resourceRenderer.getId();
		if (loadedRenderers.containsKey(resourceRendererId))
		{
			loadedRenderer = loadedRenderers.get(resourceRendererId);
		}
		else
		{
			loadedRenderer = renderableUtil.getNonLazyRenderable(resourceRenderer.getResourceLocation(), OnErrorTypeEnum.ERROR);
			loadedRenderers.put(resourceRendererId, loadedRenderer);
		}
		
		return loadedRenderer;
	}
}
