/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2011 Jaspersoft Corporation. All rights reserved.
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

import java.util.Map;

import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperPrint;

/**
 * A context that represents information about an export process.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public interface JRExporterContext
{

	/**
	 * Returns the current exporter.
	 * 
	 * @return current exporter
	 */
	JRExporter getExporter();

	/**
	 * Returns the report which is currently exported.
	 * 
	 * @return currently exported report
	 */
	JasperPrint getExportedReport();

	/**
	 * Returns the properties prefix for the current exporter.
	 * 
	 * @return the properties prefix for the current exporter
	 */
	String getExportPropertiesPrefix();
	
	/**
	 * Returns the map of export parameters.
	 * 
	 * <p>
	 * The map uses {@link JRExporterParameter} instances as keys.
	 * 
	 * @return the map of export parameters
	 */
	Map<JRExporterParameter,Object> getExportParameters();

	/**
	 * Returns the current X-axis offset at which elements should be exported.
	 * 
	 * @return the current X-axis offset
	 */
	int getOffsetX();

	/**
	 * Returns the current Y-axis offset at which elements should be exported.
	 * 
	 * @return the current Y-axis offset
	 */
	int getOffsetY();
}
