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
package net.sf.jasperreports.engine;

import java.util.Map;

import net.sf.jasperreports.export.Exporter;
import net.sf.jasperreports.export.ExporterConfiguration;
import net.sf.jasperreports.export.ExporterInput;
import net.sf.jasperreports.export.ReportExportConfiguration;
import net.sf.jasperreports.export.ExporterOutput;


/**
 * All document exporting in JasperReports is done through this interface. There is an implementation of
 * this interface for every document format that JasperReports supports at the moment.
 * <p>
 * Export customization is realized by using export parameters. Each exporter is able to recognize and use
 * its own parameters, but there is a subset of predefined parameters that are common to all exporters.
 * Those are identified by constants in the {@link JRExporterParameter} base class. All parameters are documented
 * inside the classes where they are defined.
 * <p>
 * An important aspect is the output type of each exporter. There are three types of exporters depending on
 * the type of output they produce:
 * <ul>
 * <li>exporters that export to text or character based file formats (HTML, RTF, CSV, TXT, XML exporters);
 * <li>exporters that export to binary file formats (PDF and XLS exporters)
 * <li>exporters that export directly to graphic devices (Graphics2D and Java Print Service exporters)
 * </ul>
 *<p>
 * All existing exporters fall into two categories depending on the way the content of the documents they
 * produce could be structured:
 * <ul>
 * <li> there are the exporters which target document formats which support free-form page content. These
 * are the Grapchis2D, PDF, RTF and XML exporters.
 * <li> the second category of exporters groups those exporters that target document formats which only
 * support relative positioning of elements on a page or a grid-based layout. In this category we have the
 * HTML, XLS and CSV exporters.
 * </ul>
 *
 * @deprecated Replaced by {@link Exporter}.
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public interface JRExporter<I extends ExporterInput, IC extends ReportExportConfiguration, C extends ExporterConfiguration, O extends ExporterOutput> extends Exporter<I, IC, C, O>
{


	/**
	 * Sets an export parameter for advanced customization of the export process. Parameters can be either
	 * common parameters or specialized ones, depending on the exporter type.
	 * @param parameter the parameter, selected from the static parameters defined by JasperReports
	 * @param value the parameter value
	 * @see JRExporterParameter
	 * @deprecated Replaced by  {@link #setExporterInput(net.sf.jasperreports.export.ExporterInput)}, 
	 * {@link #setConfiguration(ExporterConfiguration)} and {@link #setExporterOutput(net.sf.jasperreports.export.ExporterOutput)}
	 */
	public void setParameter(JRExporterParameter parameter, Object value);


	/**
	 * Gets an export parameter.
	 * @deprecated Replaced by {@link ExporterInput}, {@link ExporterConfiguration} and {@link ExporterOutput}.
	 */
	public Object getParameter(JRExporterParameter parameter);


	/**
	 * Sets export parameters from a specified map.
	 * @see JRExporter#setParameter(JRExporterParameter, Object)
	 * @deprecated Replaced by  {@link #setExporterInput(net.sf.jasperreports.export.ExporterInput)}, 
	 * {@link #setConfiguration(ExporterConfiguration)} and {@link #setExporterOutput(net.sf.jasperreports.export.ExporterOutput)}
	 */
	public void setParameters(Map<JRExporterParameter,Object> parameters);
	

	/**
	 * Gets a map containing all export parameters.
	 * @deprecated Replaced by {@link ExporterInput}, {@link ExporterConfiguration} and {@link ExporterOutput}.
	 */
	public Map<JRExporterParameter, Object> getParameters();

	
}
