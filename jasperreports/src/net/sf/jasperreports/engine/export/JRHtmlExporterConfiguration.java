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
package net.sf.jasperreports.engine.export;

import net.sf.jasperreports.engine.JRPropertiesUtil;
import net.sf.jasperreports.export.HtmlExporterConfiguration;
import net.sf.jasperreports.export.annotations.ExporterParameter;
import net.sf.jasperreports.export.annotations.ExporterProperty;


/**
 * @deprecated To be removed.
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public interface JRHtmlExporterConfiguration extends HtmlExporterConfiguration
{
	/**
	 * Property whose value is used as default state of the {@link #isUsingImagesToAlign()} export configuration flag.
	 * <p/>
	 * This property is set by default (<code>true</code>).
	 * 
	 * @see JRPropertiesUtil
	 */
	public static final String PROPERTY_USING_IMAGES_TO_ALIGN = JRPropertiesUtil.PROPERTY_PREFIX + "export.html.using.images.to.align";

	/**
	 * Returns a boolean value specifying whether the export engine should use small images for aligning. This is useful when you don't have
	 * images in your report anyway and you don't want to have to handle images at all.
	 * @see #PROPERTY_USING_IMAGES_TO_ALIGN
	 */
	@ExporterParameter(
		type=JRHtmlExporterParameter.class, 
		name="IS_USING_IMAGES_TO_ALIGN"
		)
	@ExporterProperty(
		value=PROPERTY_USING_IMAGES_TO_ALIGN, 
		booleanDefault=true
		)
	public Boolean isUsingImagesToAlign();
}
