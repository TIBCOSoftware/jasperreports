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
package net.sf.jasperreports.export;

import net.sf.jasperreports.engine.JRPropertiesUtil;
import net.sf.jasperreports.engine.export.JRCsvExporter;
import net.sf.jasperreports.engine.export.JRCsvMetadataExporter;
import net.sf.jasperreports.export.annotations.ExporterParameter;
import net.sf.jasperreports.export.annotations.ExporterProperty;


/**
 * Interface containing settings used by the CSV exporters.
 *
 * @see JRCsvExporter
 * @see JRCsvMetadataExporter
 * 
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public interface CsvExporterConfiguration extends ExporterConfiguration
{
	/**
	 * Property whose value is used as default for the {@link #getFieldDelimiter()} export configuration setting.
	 * The default is a comma character.
	 * 
	 * @see JRPropertiesUtil
	 */
	public static final String PROPERTY_FIELD_DELIMITER = JRPropertiesUtil.PROPERTY_PREFIX + "export.csv.field.delimiter";

	/**
	 * Property whose value is used as default for the {@link #getRecordDelimiter()} export configuration setting.
	 * The default is a character return (\n).
	 * 
	 * @see JRPropertiesUtil
	 */
	public static final String PROPERTY_RECORD_DELIMITER = JRPropertiesUtil.PROPERTY_PREFIX + "export.csv.record.delimiter";

	/**
	 * Property whose value is used as default for the {@link #isWriteBOM()} export configuration setting.
	 * The default is <code>false</code>.
	 * 
	 * @see JRPropertiesUtil
	 */
	public static final String PROPERTY_WRITE_BOM = JRPropertiesUtil.PROPERTY_PREFIX + "export.csv.write.bom";

	/**
	 * Returns the string representing the character or sequence of characters to be used to delimit two fields on the same record.
	 * @see #PROPERTY_FIELD_DELIMITER
	 */
	@SuppressWarnings("deprecation")
	@ExporterParameter(
		type=net.sf.jasperreports.engine.export.JRCsvExporterParameter.class,
		name="FIELD_DELIMITER"
		)
	@ExporterProperty(PROPERTY_FIELD_DELIMITER)
	public String getFieldDelimiter();

	/**
	 * Returns the string representing the character or sequence of characters to be used to delimit two records.
	 * @see #PROPERTY_RECORD_DELIMITER
	 */
	@SuppressWarnings("deprecation")
	@ExporterParameter(
		type=net.sf.jasperreports.engine.export.JRCsvExporterParameter.class,
		name="RECORD_DELIMITER"
		)
	@ExporterProperty(PROPERTY_RECORD_DELIMITER)
	public String getRecordDelimiter();

	/**
	 * Specifies whether the exporter should put a BOM character at the beginning of the output.
	 * @see #PROPERTY_WRITE_BOM
	 */
	@ExporterProperty(PROPERTY_WRITE_BOM)
	public Boolean isWriteBOM();
}
