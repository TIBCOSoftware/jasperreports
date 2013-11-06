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
package net.sf.jasperreports.engine.export;

import net.sf.jasperreports.engine.JRPrintFrame;
import net.sf.jasperreports.engine.JRPropertiesUtil;
import net.sf.jasperreports.export.HtmlExporterConfiguration;
import net.sf.jasperreports.export.annotations.ExporterParameter;
import net.sf.jasperreports.export.annotations.ExporterProperty;


/**
 * @deprecated To be removed.
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public interface JRHtmlExporterConfiguration extends HtmlExporterConfiguration
{
	/**
	 * This property serves as default value for the {@link #isFramesAsNestedTables()} export configuration flag.
	 * <p>
	 * The property itself defaults to <code>true</code>.
	 * </p>
	 * @see JRPropertiesUtil
	 */
	public static final String PROPERTY_FRAMES_AS_NESTED_TABLES = JRPropertiesUtil.PROPERTY_PREFIX + "export.html.frames.as.nested.tables";

	/**
	 * Property whose value is used as default state of the {@link #isUsingImagesToAlign()} export configuration flag.
	 * <p/>
	 * This property is set by default (<code>true</code>).
	 * 
	 * @see JRPropertiesUtil
	 */
	public static final String PROPERTY_USING_IMAGES_TO_ALIGN = JRPropertiesUtil.PROPERTY_PREFIX + "export.html.using.images.to.align";

	/**
	 * Indicates whether {@link JRPrintFrame frames} are to be exported as nested HTML tables.
	 * <p>
	 * If set to <code>false</code>, the frame contents will be integrated into the master/page HTML table.
	 * This can be useful when exporting frames as nested tables causes output misalignments.
	 * </p>
	 * @see #PROPERTY_FRAMES_AS_NESTED_TABLES
	 */
	@ExporterParameter(
		parameterClass=JRHtmlExporterParameter.class, 
		parameterName="FRAMES_AS_NESTED_TABLES"
		)
	@ExporterProperty(
		value=PROPERTY_FRAMES_AS_NESTED_TABLES, 
		booleanDefault=true
		)
	public Boolean isFramesAsNestedTables();
	
	/**
	 * Returns a boolean value specifying whether the export engine should use small images for aligning. This is useful when you don't have
	 * images in your report anyway and you don't want to have to handle images at all.
	 * @see #PROPERTY_USING_IMAGES_TO_ALIGN
	 */
	@ExporterParameter(
		parameterClass=JRHtmlExporterParameter.class, 
		parameterName="IS_USING_IMAGES_TO_ALIGN"
		)
	@ExporterProperty(
		value=PROPERTY_USING_IMAGES_TO_ALIGN, 
		booleanDefault=true
		)
	public Boolean isUsingImagesToAlign();
}
