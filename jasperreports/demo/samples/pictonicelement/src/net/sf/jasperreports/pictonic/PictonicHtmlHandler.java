/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2013 Jaspersoft Corporation. All rights reserved.
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
package net.sf.jasperreports.pictonic;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import net.sf.jasperreports.engine.JRGenericPrintElement;
import net.sf.jasperreports.engine.JRPrintText;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.ReportContext;
import net.sf.jasperreports.engine.export.GenericElementWithResourcesHtmlHandler;
import net.sf.jasperreports.engine.export.JRHtmlExporterContext;
import net.sf.jasperreports.engine.util.JRColorUtil;
import net.sf.jasperreports.pictonic.render.ResourceHandler;
import net.sf.jasperreports.web.WebReportContext;
import net.sf.jasperreports.web.util.VelocityUtil;
import net.sf.jasperreports.web.util.WebUtil;

/**
 * @author Narcis Marcu (narcism@users.sourceforge.net)
 * @version $Id$
 */
public class PictonicHtmlHandler implements GenericElementWithResourcesHtmlHandler
{

	private static final String PICTONIC_ELEMENT_HTML_TEMPLATE = "net/sf/jasperreports/pictonic/resources/PictonicElementHtmlTemplate.vm";

	private ResourceHandler resourceHandler;
	private Map<String, String> resources;
	
	private Map<String, Object> contextMap;
	private boolean isExportToDir;
	private File exportDir;
	
	
	public void prepareForExport(JRHtmlExporterContext context, boolean isExportToDir, File exportDir, String exportDirUri)
	{
		this.isExportToDir = isExportToDir;
		this.exportDir = exportDir;
		
		contextMap = new HashMap<String, Object>();
		JasperReportsContext jrContext = context.getJasperReportsContext();
		WebUtil webUtil = WebUtil.getInstance(jrContext);
		
		ReportContext rc = context.getExporter().getReportContext();
		String contextPath = null;
		if (rc != null) {
			contextPath = (String)rc.getParameterValue(WebReportContext.APPLICATION_CONTEXT_PATH);
		}
		
		for (Entry<String, String> entry: resources.entrySet()) {
			if (isExportToDir) {
				contextMap.put(entry.getKey(), exportDirUri + resourceHandler.getResourceName(entry.getValue()));
			} else {
				contextMap.put(entry.getKey(), webUtil.getResourcePath((contextPath != null ? contextPath : "") + webUtil.getResourcesBasePath(), entry.getValue()));
			}
		}
	}

	public String getHtmlFragment(JRHtmlExporterContext context, JRGenericPrintElement element)
	{
		JRPrintText iconText = (JRPrintText)element.getParameterValue(PictonicElement.PARAM_ICON_TEXT_ELEMENT);
		if (iconText != null) {
			contextMap.put("pictonicFontSize", iconText.getFontSize());
			contextMap.put("pictonicText", iconText.getText());
			contextMap.put("pictonicFontColor", JRColorUtil.getColorHexa(iconText.getForecolor()));
			contextMap.put("pictonicBackgroundColor", JRColorUtil.getColorHexa(iconText.getBackcolor()));
			return VelocityUtil.processTemplate(PictonicHtmlHandler.PICTONIC_ELEMENT_HTML_TEMPLATE, contextMap);
		}
		return null;
	}

	@Override
	public boolean toExport(JRGenericPrintElement element) {
		return true;
	}

	public Map<String, String> getResources() {
		return resources;
	}

	public void setResources(Map<String, String> resources) {
		this.resources = resources;
	}
	
	public void exportResources() {
		if (!isExportToDir) {
			return;
		}
		
		if (!exportDir.exists()) {
			exportDir.mkdir();
		}
		
		for (Entry<String, String> entry: resources.entrySet()) {
			resourceHandler.exportResource(entry.getValue(), exportDir);
		}
	}

	public ResourceHandler getResourceHandler() {
		return resourceHandler;
	}

	public void setResourceHandler(ResourceHandler resourceHandler) {
		this.resourceHandler = resourceHandler;
	}
}
