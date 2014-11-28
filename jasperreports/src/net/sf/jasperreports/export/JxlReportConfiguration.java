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
public interface JxlReportConfiguration extends XlsReportConfiguration
{
	/**
	 * Boolean property providing a default for the {@link #isComplexFormat()} export configuration flag.
	 * 
	 * @see JRPropertiesUtil
	 */
	public static final String PROPERTY_COMPLEX_FORMAT = JRPropertiesUtil.PROPERTY_PREFIX + "export.jxl.cell.complex.format";

	/**
	 * Flag specifying whether the cell format pattern is user-defined.
	 * When set to true, the exporter will assume that the specified pattern is well defined. 
	 * If the pattern is invalid, it won't be taken into account by the Excel file viewer.
	 * @see #PROPERTY_COMPLEX_FORMAT
	 */
	@ExporterProperty(
		value=PROPERTY_COMPLEX_FORMAT,
		booleanDefault=false
		)
	public Boolean isComplexFormat();
}
