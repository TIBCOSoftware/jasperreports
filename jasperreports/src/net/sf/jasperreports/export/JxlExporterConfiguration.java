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
import net.sf.jasperreports.export.annotations.ExporterProperty;


/**
 * Interface containing settings used by the JExcelApi exporters.
 *
 * @see net.sf.jasperreports.engine.export.JExcelApiExporter
 * @see net.sf.jasperreports.engine.export.JExcelApiMetadataExporter
 * 
 * @deprecated To be removed.
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public interface JxlExporterConfiguration extends XlsExporterConfiguration
{
	/**
	 * Boolean property providing a default for the {@link #isUseTempFile()} export configuration flag.
	 * <p/>
	 * This property is by default not set (<code>false</code>).
	 * 
	 * @see JRPropertiesUtil
	 */
	public static final String PROPERTY_USE_TEMP_FILE = JRPropertiesUtil.PROPERTY_PREFIX + "export.jxl.use.temp.file";

	/**
	 * Flag enabling the JExcelApiExporter to use temporary files when creating large documents.
	 * @see #PROPERTY_USE_TEMP_FILE
	 */
	@ExporterProperty(
		value=PROPERTY_USE_TEMP_FILE,
		booleanDefault=false
		)
	public Boolean isUseTempFile();
}
