/*
 * ============================================================================
 * GNU Lesser General Public License
 * ============================================================================
 *
 * JasperReports - Free Java report-generating library.
 * Copyright (C) 2001-2006 JasperSoft Corporation http://www.jaspersoft.com
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
 * 303 Second Street, Suite 450 North
 * San Francisco, CA 94107
 * http://www.jaspersoft.com
 */
package net.sf.jasperreports.engine.export;

import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JRPrintFrame;


/**
 * Contains parameters useful for export in SWF format.
 * <p>
 * The SWF exporter can send data to an output stream or a file on disk. The engine looks among the export parameters in
 * order to find the selected output type in this order: OUTPUT_STREAM, OUTPUT_FILE, OUTPUT_FILE_NAME.
 * <p>
 *
 * @author Sanda Zaharia (szaharia@users.sourceforge.net)
 * @version $Id$
 */
public class JRSwfExporterParameter extends JRExporterParameter
{


	/**
	 *
	 */
	protected JRSwfExporterParameter(String name)
	{
		super(name);
	}

	/**
	 * An absolute path to a folder on a local disk, where all the swf fonts are stored.
	 */
	public static final JRSwfExporterParameter SWF_FONT_DIR = new JRSwfExporterParameter("Fonts Directory");


}
