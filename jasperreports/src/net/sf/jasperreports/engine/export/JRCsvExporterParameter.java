/*
 * ============================================================================
 * GNU Lesser General Public License
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

/*
 * Contributors:
 * Mirko Wawrowsky - mawawrosky@users.sourceforge.net
 */
package net.sf.jasperreports.engine.export;

import net.sf.jasperreports.engine.JRExporterParameter;


/**
 * Contains parameters useful for export in CSV format.
 * <p>
 * The CSV exporter can send data to a string buffer, output stream, character stream or file on disk. The engine looks
 * among the export parameters in order to find the selected output type in this order: OUTPUT_STRING_BUFFER, OUTPUT_WRITER,
 * OUTPUT_STREAM, OUTPUT_FILE, OUTPUT_FILE_NAME.
 *
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JRCsvExporterParameter extends JRExporterParameter
{


	/**
	 *
	 */
	protected JRCsvExporterParameter(String name)
	{
		super(name);
	}


	/**
	 * A string representing the character or sequence of characters used to delimit two fields on the same line. The
	 * default value is a comma.
	 */
	public static final JRCsvExporterParameter FIELD_DELIMITER = new JRCsvExporterParameter("Field Delimiter");


	/**
	 * A string representing the character or sequence of characters used to delimit two lines. The default value is a
	 * character return (\n).
	 */
	public static final JRCsvExporterParameter RECORD_DELIMITER = new JRCsvExporterParameter("Record Delimiter");
	
	
}
