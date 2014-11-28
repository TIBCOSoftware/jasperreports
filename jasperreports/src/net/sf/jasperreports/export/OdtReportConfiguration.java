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
package net.sf.jasperreports.export;

import net.sf.jasperreports.engine.JRPrintHyperlink;
import net.sf.jasperreports.engine.JRPropertiesUtil;
import net.sf.jasperreports.engine.export.oasis.JROdtExporter;
import net.sf.jasperreports.export.annotations.ExporterParameter;
import net.sf.jasperreports.export.annotations.ExporterProperty;


/**
 * Interface containing settings used by the ODT exporter.
 *
 * @see JROdtExporter
 * 
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public interface OdtReportConfiguration extends ReportExportConfiguration
{
	/**
	 * This property serves as default value for the {@link #isFlexibleRowHeight()} export configuration flag.
	 * <p>
	 * The property itself defaults to <code>false</code>.
	 * </p>
	 * @see JRPropertiesUtil
	 */
	public static final String PROPERTY_ODT_FLEXIBLE_ROW_HEIGHT = JRPropertiesUtil.PROPERTY_PREFIX + "export.odt.flexible.row.height";

	/**
	 * Property that provides a default for the {@link #isIgnoreHyperlink()} export configuration flag.
	 */
	public static final String PROPERTY_IGNORE_HYPERLINK = JRPropertiesUtil.PROPERTY_PREFIX + "export.odt." + JRPrintHyperlink.PROPERTY_IGNORE_HYPERLINK_SUFFIX;

	/**
	 * Indicates whether table rows in ODT documents can grow if more text is added into cells.
	 * <p>
	 * Is set to <code>false</code>, the table rows do not increase in height automatically and the user has to enlarge them manually.
	 * </p>
	 * @see #PROPERTY_ODT_FLEXIBLE_ROW_HEIGHT
	 */
	@SuppressWarnings("deprecation")
	@ExporterParameter(
		type=net.sf.jasperreports.engine.export.oasis.JROpenDocumentExporterParameter.class, 
		name="ODT_FLEXIBLE_ROW_HEIGHT"
		)
	@ExporterProperty(
		value=PROPERTY_ODT_FLEXIBLE_ROW_HEIGHT, 
		booleanDefault=false
		)
	public Boolean isFlexibleRowHeight();

	/**
	 * @see #PROPERTY_IGNORE_HYPERLINK
	 */
	@ExporterProperty(
		value=PROPERTY_IGNORE_HYPERLINK, 
		booleanDefault=false
		)
	public Boolean isIgnoreHyperlink();
}
