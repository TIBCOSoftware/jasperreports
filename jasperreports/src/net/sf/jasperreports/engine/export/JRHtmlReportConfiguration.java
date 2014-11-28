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

import net.sf.jasperreports.engine.JRPrintFrame;
import net.sf.jasperreports.engine.JRPropertiesUtil;
import net.sf.jasperreports.export.HtmlReportConfiguration;
import net.sf.jasperreports.export.annotations.ExporterParameter;
import net.sf.jasperreports.export.annotations.ExporterProperty;


/**
 * @deprecated To be removed.
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public interface JRHtmlReportConfiguration extends HtmlReportConfiguration
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
	 * Indicates whether {@link JRPrintFrame frames} are to be exported as nested HTML tables.
	 * <p>
	 * If set to <code>false</code>, the frame contents will be integrated into the master/page HTML table.
	 * This can be useful when exporting frames as nested tables causes output misalignments.
	 * </p>
	 * @see #PROPERTY_FRAMES_AS_NESTED_TABLES
	 */
	@ExporterParameter(
		type=JRHtmlExporterParameter.class, 
		name="FRAMES_AS_NESTED_TABLES"
		)
	@ExporterProperty(
		value=PROPERTY_FRAMES_AS_NESTED_TABLES, 
		booleanDefault=true
		)
	public Boolean isFramesAsNestedTables();
}
