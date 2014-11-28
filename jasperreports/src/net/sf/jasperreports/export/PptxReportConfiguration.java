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

import net.sf.jasperreports.engine.JRPrintHyperlink;
import net.sf.jasperreports.engine.JRPropertiesUtil;
import net.sf.jasperreports.engine.export.ooxml.JRPptxExporter;
import net.sf.jasperreports.export.annotations.ExporterProperty;


/**
 * Interface containing settings used by the PPTX exporter.
 *
 * @see JRPptxExporter
 * 
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public interface PptxReportConfiguration extends ReportExportConfiguration
{
	/**
	 * Property that provides a default value for the {@link #isIgnoreHyperlink()} export configuration flag.
	 */
	public static final String PROPERTY_IGNORE_HYPERLINK = JRPropertiesUtil.PROPERTY_PREFIX +  "export.pptx." + JRPrintHyperlink.PROPERTY_IGNORE_HYPERLINK_SUFFIX;
	
	/**
	 * @see #PROPERTY_IGNORE_HYPERLINK
	 */
	@ExporterProperty(
		value=PROPERTY_IGNORE_HYPERLINK, 
		booleanDefault=false
		)
	public Boolean isIgnoreHyperlink();
}
