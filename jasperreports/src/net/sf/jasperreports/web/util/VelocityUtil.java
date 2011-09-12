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
package net.sf.jasperreports.web.util;

import java.io.IOException;
import java.io.StringWriter;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;


/**
 * @author Narcis Marcu (narcism@users.sourceforge.net)
 * @version $Id$
 */
public class VelocityUtil
{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static final VelocityEngine velocityEngine;
	
	static {
		velocityEngine = new VelocityEngine();
		velocityEngine.setProperty("file.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
		velocityEngine.setProperty("file.resource.loader.cache", "true");
		velocityEngine.init();
	}
	
	public static VelocityEngine getVelocityEngine() {
		return velocityEngine;
	}
	
	public static String processTemplate(String templateName, VelocityContext vContext) {
		Template template = getVelocityEngine().getTemplate(templateName);

		StringWriter writer = new StringWriter(128);
		template.merge(vContext, writer);
		writer.flush();
		try {
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return writer.getBuffer().toString();
	}
}
