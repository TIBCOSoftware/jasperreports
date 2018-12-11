/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2018 TIBCO Software Inc. All rights reserved.
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
package net.sf.jasperreports.customvisualization;

import net.sf.jasperreports.engine.JRGenericPrintElement;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.repo.RepositoryUtil;
import net.sf.jasperreports.web.util.VelocityUtil;
import org.apache.velocity.VelocityContext;

import java.io.*;
import java.util.Map;

/**
 *
 * @author gtoffoli
 */
public class CVUtils
{
	/**
	 * Check if the element configuration specifies to render this component as png instead of looking for an SVG
	 *
	 * @param element
	 * @return
	 */
	public static boolean isRenderAsPng(JRGenericPrintElement element) {

		if (element.hasParameter(CVPrintElement.CONFIGURATION))
		{
			Map<String, Object> componentConfiguration = (Map<String, Object>) element.getParameterValue(CVPrintElement.CONFIGURATION);
			if (componentConfiguration.containsKey("renderAsPng"))
			{
				Object renderAsPngProperty = componentConfiguration.get("renderAsPng");
				if (renderAsPngProperty != null)
				{
					return Boolean.valueOf(renderAsPngProperty.toString()).booleanValue();
				}
			}
		}
		return false;
	}


	/**
	 * Check if the element configuration specifies the zoom to be used when the export is performed as raster image.
	 * Default is 1.0f.
	 * Bigger is the zoom, higher is the quality, bigger is the file exported.
	 *
	 * @param element
	 * @return
	 */
	public static float getZoomFactor(JRGenericPrintElement element) {

		if (element.hasParameter(CVPrintElement.CONFIGURATION))
		{
			Map<String, Object> componentConfiguration = (Map<String, Object>) element.getParameterValue(CVPrintElement.CONFIGURATION);
			if (componentConfiguration.containsKey("render-zoom-factor"))
			{
				Object property = componentConfiguration.get("render-zoom-factor");
				if (property != null)
				{
					try {
						return Float.valueOf(property.toString()).floatValue();
					} catch (Exception ex)
					{

					}
				}
			}
		}
		return 1.0f;
	}
        
        
	public static long getTimeout(JRGenericPrintElement element) {

		if (element.hasParameter(CVPrintElement.CONFIGURATION))
		{
			Map<String, Object> componentConfiguration = (Map<String, Object>) element.getParameterValue(CVPrintElement.CONFIGURATION);
			if (componentConfiguration.containsKey("render-timeout"))
			{
				Object property = componentConfiguration.get("render-timeout");
				if (property != null)
				{
					try {
						return Long.valueOf(property.toString()).longValue();
					} catch (Exception ex)
					{

					}
				}
			}
		}
		return 3000;
	}

	public static void byteStreamCopy(InputStream is, OutputStream os) throws IOException {
		byte[] buf = new byte[8192];
		int read;
		while ((read = is.read(buf)) != -1) {
			os.write(buf, 0, read);
		}
	}

	public static String getResourceName(String resourceLocation) {
		// location can be both classpath resource and file path
		int slashIndex = resourceLocation.lastIndexOf('/');
		int separatorIndex = resourceLocation.lastIndexOf(File.separator);
		int nameIndex = Math.max(slashIndex, separatorIndex);
		return nameIndex >= 0 ? resourceLocation.substring(nameIndex + 1) : resourceLocation;
	}
}
