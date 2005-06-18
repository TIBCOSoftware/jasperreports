/*
 * ============================================================================
 *                   GNU Lesser General Public License
 * ============================================================================
 *
 * JasperReports - Free Java report-generating library.
 * Copyright (C) 2001-2005 JasperSoft Corporation http://www.jaspersoft.com
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307, USA.
 * 
 * JasperSoft Corporation
 * 185, Berry Street, Suite 6200
 * San Francisco CA 94107
 * http://www.jaspersoft.com
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


}
