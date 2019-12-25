/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2019 TIBCO Software Inc. All rights reserved.
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

import net.sf.jasperreports.engine.JRGenericElementType;
import net.sf.jasperreports.engine.type.OnErrorTypeEnum;

/**
 * Utility class that creates generic print elements of cv type.
 * 
 * @author Giulio Toffoli (gtoffoli@tibco.com)
 */
public final class CVPrintElement
{
	public static final String PARAMETER_ON_ERROR_TYPE = CVConstants.PROPERTY_ON_ERROR_TYPE;

	/**
	 * The qualified type of the Custom Visualization generic elements.
	 */
	public static final JRGenericElementType CV_ELEMENT_TYPE =
		new JRGenericElementType(CVConstants.NAMESPACE, CVConstants.COMPONENT_NAME);

	public static final String PARAMETER_ELEMENT_ID = "elementId";
	
	public static final String CONFIGURATION = "configuration";
	public static final String SCRIPT = "script";
	public static final String SCRIPT_URI = "script_uri";
	public static final String NAMESPACE = "namespace";
	public static final String CSS = "css";
	public static final String CSS_URI = "css_uri";
	public static final String MODULE = "module";
	public static final OnErrorTypeEnum DEFAULT_ON_ERROR_TYPE = OnErrorTypeEnum.ERROR;

	/**
	 * The cached image renderer.
	 */
	public static final String PARAMETER_SVG_CACHE_RENDERER = "cacheRenderer";
	public static final String PARAMETER_PNG_CACHE_RENDERER = "pngCacheRenderer";

	private CVPrintElement()
	{
	}
}
