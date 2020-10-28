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

public class CVConstants
{
	public static final long SERIAL_VERSION_UID = 1L;
	public static final String NAMESPACE = "http://www.jaspersoft.com/cvcomponent";
	public static final String XSD_LOCATION = "http://www.jaspersoft.com/cvcomponent/component.xsd";
	public static final String XSD_RESOURCE = "net/sf/jasperreports/customvisualization/component.xsd";
	protected static final String COMPONENT_NAME = "customvisualization";

		
	public static final String CV_PREFIX = "com.jaspersoft.jasperreports.components.customvisualization.";
	
	// com.jaspersoft.jasperreports.components.customvisualization.script.path.
	/**
	 * @deprecated To be removed.
	 */
	public static final String CV_SCRIPT_PATH_PROPERTY = CV_PREFIX + "script.path.";

	/**
	 * com.jaspersoft.jasperreports.components.customvisualization.require.js
	 *
	 * Property used to specify the location of the require.js script
	 */
	/**
	 * @deprecated To be removed.
	 */
	public static final String CV_REQUIREJS_PROPERTY = CV_PREFIX + "require.js";

	/**
	 * If this property is set to true, scripts will be searched in classpath only.
	 *
	 * com.jaspersoft.jasperreports.components.customvisualization.allow.scripts.in.classpath.only
	 */
	/**
	 * @deprecated To be removed.
	 */
	public static final String CV_SCRIPT_FROM_CLASSPATH_ONLY_PROPERTY = CV_PREFIX + "allow.scripts.in.classpath.only";

	/**
	 * If set to true (default), JR will take care of generating a PNG trough Batik.
	 * Otherwise, PhantomJS will be used.
	 *
	 * com.jaspersoft.jasperreports.components.customvisualization.png.use.jr
	 * Possible values: true|false
	 */
	/**
	 * @deprecated To be removed.
	 */
	public static final String CV_PNG_USE_JR_TO_RENDER = CV_PREFIX + "png.use.jr";

	/**
	 * If JR is used to render a PNG, this property allows to set the minimum DPI of the rasterized image.
	 * 
	 * com.jaspersoft.jasperreports.components.customvisualization.png.min.dpi
	 * Possible values: integer, usually between 72 and 300.
	 * Default value: 300 (see CV_PNG_MIN_DPI_DEFAULT_VALUE)
	 */
	public static final String CV_PNG_MIN_DPI = CV_PREFIX + "png.min.dpi";

	/**
	 * If JR is used to render a PNG, this property allows to set the antialias on or off for the rasterized image.
	 *
	 * com.jaspersoft.jasperreports.components.customvisualization.png.antialias
	 * Possible values: true|false
	 * Default value: true
	 */
	public static final String CV_PNG_ANTIALIAS = CV_PREFIX + "png.antialias";

	public static final int CV_PNG_MIN_DPI_DEFAULT_VALUE = 300;
	public static final boolean CV_PNG_ANTIALIAS_DEFAULT_VALUE = true;

	/**
	 * @deprecated To be removed.
	 */
	public static final boolean CV_PNG_USE_JR_TO_RENDER_DEFAULT_VALUE = true;

	public static final String PROPERTY_ON_ERROR_TYPE = "onErrorType";

	/**
	 * Boolean property to produce images instead of generic elements
	 */
	public static final String PROPERTY_GENERATE_IMAGE = CV_PREFIX + "generate.image";

	/**
	 * Boolean property to produce images instead of HTML fragments during HTML export
	 */
	public static final String PROPERTY_HTML_GENERATE_IMAGE = CV_PREFIX + "html.generate.image";
}
