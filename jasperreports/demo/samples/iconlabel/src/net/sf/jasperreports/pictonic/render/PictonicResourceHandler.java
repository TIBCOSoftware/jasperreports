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
package net.sf.jasperreports.pictonic.render;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.web.util.VelocityUtil;
import net.sf.jasperreports.web.util.WebResourceHandler;
import net.sf.jasperreports.web.util.WebUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * @author Narcis Marcu (narcism@users.sourceforge.net)
 * @version $Id$
 */
public class PictonicResourceHandler implements ResourceHandler, WebResourceHandler {

	private static final Log log = LogFactory.getLog(PictonicResourceHandler.class);

	private ResourceResolver resourceResolver;
	private List<String> resources;


	public boolean hadlesResource(String resourceKey) {
		return resourceResolver.resolve(resourceKey) != null;
	}

	public void exportResource(String resourceKey, File dir) {
		if (log.isDebugEnabled()) {
			log.debug("exporting resource with key: " + resourceKey);
		}
		Resource res = resourceResolver.resolve(resourceKey);
		
		if (res.isDynamic()) {
			Map<String, Object> contextMap = new HashMap<String, Object>();
			
			if (res.getDependencies() != null && res.getDependencies().size() > 0) {
				for (Entry<String, String> entry: res.getDependencies().entrySet()) {
					contextMap.put(entry.getKey(), exportResourceToDir(resourceResolver.resolve(entry.getValue()), dir));
				}
			}
		
			byte[] bytes = VelocityUtil.processTemplate(res.getLocation(), contextMap).getBytes();
			writeBytesToFile(bytes, dir, getResourceName(res));
		} else {
			exportResourceToDir(res, dir);
		}
	}

	public String getResourceName(String resourceKey) {
		Resource res = resourceResolver.resolve(resourceKey);
		return getResourceName(res);
	}

	public byte[] getData(String resourceKey, HttpServletRequest request, JasperReportsContext jrContext) {
		Resource res = resourceResolver.resolve(resourceKey);
		if (res != null) {
			Map<String, Object> contextMap = new HashMap<String, Object>();
			WebUtil webUtil = WebUtil.getInstance(jrContext);
			
			if (res.isDynamic()) {
				if (res.getDependencies() != null && res.getDependencies().size() > 0) {
					for (Entry<String, String> entry: res.getDependencies().entrySet()) {
						contextMap.put(entry.getKey(), webUtil.getResourcePath(request.getContextPath() + webUtil.getResourcesBasePath(), entry.getValue()));
					}
				}
				return VelocityUtil.processTemplate(res.getLocation(), contextMap).getBytes();
			} else {
				try {
					return JRLoader.loadBytesFromResource(res.getLocation());
				} catch (JRException e) {
					throw new JRRuntimeException("failed to load resource " + res.getLocation(), e);
				}
			}
		}
		return null;
	}

	public String getResourceType(String resourceKey) {
		String name = getResourceName(resourceResolver.resolve(resourceKey));
		if (name != null && name.lastIndexOf(".") != -1) {
			return name.substring(name.lastIndexOf(".") + 1);
		}
		return null;
	}

	private String exportResourceToDir(Resource res, File dir) {
		String resourceName = getResourceName(res);
		try {
			byte[] bytes = JRLoader.loadBytesFromResource(res.getLocation());
			writeBytesToFile(bytes, dir, resourceName);
		} catch (JRException e) {
			throw new JRRuntimeException("failed to load resource " + res.getLocation(), e);
		}
		return resourceName;
	}

	private void writeBytesToFile(byte[] bytes, File dir, String fileName) {
		File resourceFile = new File(dir, fileName);
		if (log.isDebugEnabled()) {
			log.debug("creating file: " + resourceFile.getAbsolutePath());
		}
		FileOutputStream fos = null;
		try {
			if (resourceFile.createNewFile()) {
				fos = new FileOutputStream(resourceFile);
				fos.write(bytes);
				fos.flush();
			}
		} catch (IOException e) {
			throw new JRRuntimeException("unable to create file: " + resourceFile.getAbsolutePath(), e);
		} finally {
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					if (log.isWarnEnabled()) {
						log.warn("failed to close file stream for file: " + resourceFile.getAbsolutePath(), e);
					}
				}
			}
		}
	}

	private String getResourceName(Resource resource) {
		if (resource == null) {
			return null;
		}
		if (resource.getName() != null && resource.getName().length() > 0) {
			return resource.getName();
		}
		String resourceLocation = resource.getLocation();
		// location can be both classpath resource and file path
		int slashIndex = resourceLocation.lastIndexOf('/');
		int separatorIndex = resourceLocation.lastIndexOf(File.separator);
		int nameIndex = Math.max(slashIndex, separatorIndex);
		return nameIndex >= 0 ? resourceLocation.substring(nameIndex + 1) : resourceLocation;
	}

	public ResourceResolver getResourceResolver() {
		return resourceResolver;
	}

	public void setResourceResolver(ResourceResolver resourceResolver) {
		this.resourceResolver = resourceResolver;
	}

	public List<String> getResources() {
		return resources;
	}

	public void setResources(List<String> resources) {
		this.resources = resources;
	}

}
