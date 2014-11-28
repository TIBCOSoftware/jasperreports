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

import net.sf.jasperreports.engine.JRExporterParameter;


/**
 * Contains parameters useful for export in JSON format.
 *
 * @deprecated Replaced by {@link net.sf.jasperreports.export.JsonExporterConfiguration}.
 * @author Narcis Marcu (narcism@users.sourceforge.net)
 */
public class JsonExporterParameter extends JRExporterParameter
{


	/**
	 *
	 */
	public JsonExporterParameter(String name)
	{
		super(name);
	}


	/**
	 * @deprecated Replaced by {@link net.sf.jasperreports.export.JsonExporterConfiguration#isReportComponentsExportOnly()}.
	 */
	public static final JsonExporterParameter REPORT_COMPONENTS_EXPORT_ONLY = new JsonExporterParameter("Report Components Export Only");

}
