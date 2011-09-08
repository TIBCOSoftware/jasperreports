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

/*
 * Contributors:
 * Mirko Wawrowsky - mawawrosky@users.sourceforge.net
 */
package net.sf.jasperreports.engine.export;

import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.util.JRProperties;


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
	 * Property whose value is used as default for the {@link #FIELD_DELIMITER FIELD_DELIMITER} export parameter.
	 * 
	 * @see JRProperties
	 */
	public static final String PROPERTY_FIELD_DELIMITER = JRProperties.PROPERTY_PREFIX + "export.csv.field.delimiter";


	/**
	 * A string representing the character or sequence of characters used to delimit two lines. The default value is a
	 * character return (\n).
	 */
	public static final JRCsvExporterParameter RECORD_DELIMITER = new JRCsvExporterParameter("Record Delimiter");
	
	
	/**
	 * Property whose value is used as default for the {@link #RECORD_DELIMITER RECORD_DELIMITER} export parameter.
	 * 
	 * @see JRProperties
	 */
	public static final String PROPERTY_RECORD_DELIMITER = JRProperties.PROPERTY_PREFIX + "export.csv.record.delimiter";


}
