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

/*
 * Contributors:
 * Mirko Wawrowsky - mawawrosky@users.sourceforge.net
 */
package net.sf.jasperreports.engine.export;

import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.export.CsvExporterConfiguration;


/**
 * @deprecated Replaced by {@link CsvExporterConfiguration}.
 * @author Teodor Danciu (teodord@users.sourceforge.net)
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
	 * @deprecated Replaced by {@link CsvExporterConfiguration#getFieldDelimiter()}.
	 */
	public static final JRCsvExporterParameter FIELD_DELIMITER = new JRCsvExporterParameter("Field Delimiter");


	/**
	 * @deprecated Replaced by {@link CsvExporterConfiguration#PROPERTY_FIELD_DELIMITER}.
	 */
	public static final String PROPERTY_FIELD_DELIMITER = CsvExporterConfiguration.PROPERTY_FIELD_DELIMITER;


	/**
	 * @deprecated Replaced by {@link CsvExporterConfiguration#getRecordDelimiter()}.
	 */
	public static final JRCsvExporterParameter RECORD_DELIMITER = new JRCsvExporterParameter("Record Delimiter");
	
	
	/**
	 * @deprecated Replaced by {@link CsvExporterConfiguration#PROPERTY_RECORD_DELIMITER}.
	 */
	public static final String PROPERTY_RECORD_DELIMITER = CsvExporterConfiguration.PROPERTY_RECORD_DELIMITER;


}
