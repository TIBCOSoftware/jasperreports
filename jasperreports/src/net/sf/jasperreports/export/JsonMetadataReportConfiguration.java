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

import net.sf.jasperreports.engine.JRPropertiesUtil;
import net.sf.jasperreports.export.annotations.ExporterProperty;


/**
 * Interface containing settings used by the JSON Metadata exporter.
 *
 * @see net.sf.jasperreports.engine.export.JsonMetadataExporter
 *
 * @author Narcis Marcu (narcism@users.sourceforge.net)
 */
public interface JsonMetadataReportConfiguration extends JsonReportConfiguration {

	/**
	 * Property whose value is used as default for the {@link #isEscapeMembers()} export configuration setting.
	 * <p>
	 * The property itself defaults to <code>true</code>.
	 * </p>
	 *
	 * @see net.sf.jasperreports.engine.JRPropertiesUtil
	 */
	public static final String JSON_EXPORTER_ESCAPE_MEMBERS = JRPropertiesUtil.PROPERTY_PREFIX + "export.json.escape.members";


	/**
	 * Property whose value is used as default for the {@link #getJsonSchemaResource()} export configuration setting.
	 *
	 * @see net.sf.jasperreports.engine.JRPropertiesUtil
	 */
	public static final String JSON_EXPORTER_JSON_SCHEMA = JRPropertiesUtil.PROPERTY_PREFIX + "export.json.schema";



	/**
	 *
	 */
	@ExporterProperty(
		value=JSON_EXPORTER_ESCAPE_MEMBERS,
		booleanDefault=true
		)
	public Boolean isEscapeMembers();

	/**
	 *
	 */
	@ExporterProperty(value=JSON_EXPORTER_JSON_SCHEMA)
	public String getJsonSchemaResource();
}
