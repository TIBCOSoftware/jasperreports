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

import net.sf.jasperreports.engine.JRExporterParameter;


/**
 * Contains parameters useful for export to an AWT <tt>Graphics2D</tt> object.
 *
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
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
	 * The <tt>java.awt.Graphics2D</tt> instance used for export.
	 */
	public static final JRGraphics2DExporterParameter GRAPHICS_2D = new JRGraphics2DExporterParameter("Graphics2D");

	/**
	 * The zoom ratio used for the export. The default value is 1.
	 */
	public static final JRGraphics2DExporterParameter ZOOM_RATIO = new JRGraphics2DExporterParameter("Zoom Ratio");

	/**
	 * Flag to control the use of an AWT rendering fix which causes the printer job size to be reduced when
	 * the exporter draws onto a printer graphic context.
	 *
	 * The fix was introduced to solve an old Java printing problem related to the size of printer spool jobs.
	 * However, it causes problems when bidirectional text is rendered, by losing text direction information.
	 *
	 * This flag is true, by default and should be set to false when bidirectional writing is present in
	 * the document that is sent to the printer.
	 *
	 * This flag can be set system-wide using the
	 * {@link net.sf.jasperreports.engine.export.JRGraphics2DExporter#MINIMIZE_PRINTER_JOB_SIZE MINIMIZE_PRINTER_JOB_SIZE} property.
	 * This export parameter overrides the property value.
	 *
	 * @see net.sf.jasperreports.engine.export.JRGraphics2DExporter#MINIMIZE_PRINTER_JOB_SIZE
	 */
	public static final JRGraphics2DExporterParameter MINIMIZE_PRINTER_JOB_SIZE = new JRGraphics2DExporterParameter("Minimize Printer Job Size");


}
