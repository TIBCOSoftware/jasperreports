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
import net.sf.jasperreports.export.Graphics2DExporterConfiguration;
import net.sf.jasperreports.export.Graphics2DExporterOutput;
import net.sf.jasperreports.export.Graphics2DReportConfiguration;


/**
 * Contains parameters useful for export to an AWT <tt>Graphics2D</tt> object.
 *
 * @deprecated Replaced by {@link Graphics2DExporterConfiguration} and {@link Graphics2DExporterOutput}.
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public class JRGraphics2DExporterParameter extends JRExporterParameter
{


	/**
	 *
	 */
	protected JRGraphics2DExporterParameter(String name)
	{
		super(name);
	}


	/**
	 * @deprecated Replaced by {@link Graphics2DExporterOutput#getGraphics2D()}.
	 */
	public static final JRGraphics2DExporterParameter GRAPHICS_2D = new JRGraphics2DExporterParameter("Graphics2D");

	/**
	 * @deprecated Replaced by {@link Graphics2DReportConfiguration#getZoomRatio()}.
	 */
	public static final JRGraphics2DExporterParameter ZOOM_RATIO = new JRGraphics2DExporterParameter("Zoom Ratio");

	/**
	 * @deprecated Replaced by {@link Graphics2DReportConfiguration#isMinimizePrinterJobSize()}.
	 */
	public static final JRGraphics2DExporterParameter MINIMIZE_PRINTER_JOB_SIZE = new JRGraphics2DExporterParameter("Minimize Printer Job Size");


}
