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

import net.sf.jasperreports.engine.export.JsonExporter;
import net.sf.jasperreports.export.annotations.ExporterParameter;
import net.sf.jasperreports.export.annotations.ExporterProperty;


/**
 * Interface containing settings used by the JSON exporter.
 *
 * @see JsonExporter
 * 
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public interface JsonExporterConfiguration extends ExporterConfiguration
{
	/**
	 * A flag that determines whether the JSON exporter should flush the
	 * output stream after writing the JSON content to it.
	 * @see HtmlExporterConfiguration#PROPERTY_FLUSH_OUTPUT
	 */
	@SuppressWarnings("deprecation")
	@ExporterParameter(
		type=net.sf.jasperreports.engine.export.JRHtmlExporterParameter.class, 
		name="FLUSH_OUTPUT"
		)
	@ExporterProperty(
		value=HtmlExporterConfiguration.PROPERTY_FLUSH_OUTPUT,
		booleanDefault=true
		)
	public Boolean isFlushOutput();

	/**
	 * A flag that determines whether the JSON exporter should export only the
	 * report components, thus ignoring the page components
	 */
	@SuppressWarnings("deprecation")
	@ExporterParameter(
		type=net.sf.jasperreports.engine.export.JsonExporterParameter.class,
		name="REPORT_COMPONENTS_EXPORT_ONLY"
		)
	public Boolean isReportComponentsExportOnly();
}
