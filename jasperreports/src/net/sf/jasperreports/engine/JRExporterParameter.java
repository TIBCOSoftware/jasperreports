/*
 * ============================================================================
 *                   GNU Lesser General Public License
 * ============================================================================
 *
 * JasperReports - Free Java report-generating library.
 * Copyright (C) 2001-2005 Teodor Danciu teodord@users.sourceforge.net
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
 * Teodor Danciu
 * 173, Calea Calarasilor, Bl. 42, Sc. 1, Ap. 18
 * Postal code 030615, Sector 3
 * Bucharest, ROMANIA
 * Email: teodord@users.sourceforge.net
 */
package net.sf.jasperreports.engine;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JRExporterParameter
{


	/**
	 *
	 */
	private String name = null;


	/**
	 *
	 */
	protected JRExporterParameter(String name)
	{
		this.name = name;
	}


	/**
	 *
	 */
	public String toString()
	{
		return this.name;
	}


	/**
	 *
	 */
	public static final JRExporterParameter JASPER_PRINT = new JRExporterParameter("JasperPrint Object");
	public static final JRExporterParameter JASPER_PRINT_LIST = new JRExporterParameter("JasperPrint List");
	public static final JRExporterParameter INPUT_STREAM = new JRExporterParameter("InputStream Object");
	public static final JRExporterParameter INPUT_URL = new JRExporterParameter("URL Object");
	public static final JRExporterParameter INPUT_FILE = new JRExporterParameter("Input File");
	public static final JRExporterParameter INPUT_FILE_NAME = new JRExporterParameter("Input File Name");
	public static final JRExporterParameter OUTPUT_STRING_BUFFER = new JRExporterParameter("Output StringBuffer Object");
	public static final JRExporterParameter OUTPUT_WRITER = new JRExporterParameter("Output Writer Object");
	public static final JRExporterParameter OUTPUT_STREAM = new JRExporterParameter("OutputStream Object");
	public static final JRExporterParameter OUTPUT_FILE = new JRExporterParameter("Output File");
	public static final JRExporterParameter OUTPUT_FILE_NAME = new JRExporterParameter("Output File Name");
	public static final JRExporterParameter PAGE_INDEX = new JRExporterParameter("Page Index");
	public static final JRExporterParameter START_PAGE_INDEX = new JRExporterParameter("Start Page Index");
	public static final JRExporterParameter END_PAGE_INDEX = new JRExporterParameter("End Page Index");
	public static final JRExporterParameter CHARACTER_ENCODING = new JRExporterParameter("Character Encoding");
	public static final JRExporterParameter PROGRESS_MONITOR = new JRExporterParameter("Progress Monitor");
	public static final JRExporterParameter OFFSET_X = new JRExporterParameter("Offset X");
	public static final JRExporterParameter OFFSET_Y = new JRExporterParameter("Offset Y");


}
