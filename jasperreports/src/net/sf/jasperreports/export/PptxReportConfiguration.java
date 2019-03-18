/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2019 TIBCO Software Inc. All rights reserved.
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

import net.sf.jasperreports.annotations.properties.Property;
import net.sf.jasperreports.annotations.properties.PropertyScope;
import net.sf.jasperreports.engine.JRPrintHyperlink;
import net.sf.jasperreports.engine.JRPropertiesUtil;
import net.sf.jasperreports.engine.export.ooxml.JRPptxExporter;
import net.sf.jasperreports.export.annotations.ExporterProperty;
import net.sf.jasperreports.properties.PropertyConstants;


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
	@Property(
			category = PropertyConstants.CATEGORY_EXPORT,
			scopes = {PropertyScope.CONTEXT, PropertyScope.REPORT, PropertyScope.HYPERLINK},
			sinceVersion = PropertyConstants.VERSION_5_1_2,
			valueType = Boolean.class
			)
	public static final String PROPERTY_IGNORE_HYPERLINK = JRPptxExporter.PPTX_EXPORTER_PROPERTIES_PREFIX + JRPrintHyperlink.PROPERTY_IGNORE_HYPERLINK_SUFFIX;
	
	/**
	 * This property provides a default value for the {@link #getHideSlideMasterPages()} export configuration setting.
	 * </p>
	 * @see JRPropertiesUtil
	 */
	@Property(
			category = PropertyConstants.CATEGORY_EXPORT,
			scopes = {PropertyScope.CONTEXT, PropertyScope.REPORT},
			sinceVersion = PropertyConstants.VERSION_6_8_0
			)
	public static final String PROPERTY_HIDE_SLIDE_MASTER_PAGES = JRPptxExporter.PPTX_EXPORTER_PROPERTIES_PREFIX + "hide.slide.master.pages";

	/**
	 * @see #PROPERTY_IGNORE_HYPERLINK
	 */
	@ExporterProperty(
		value=PROPERTY_IGNORE_HYPERLINK, 
		booleanDefault=false
		)
	public Boolean isIgnoreHyperlink();
	
	/**
	 * This properties specifies the report pages on which the background contents coming from the slide master should be hidden.
	 * 
	 * The value of the property should be a comma separated list of page numbers or page ranges. Page ranges are made of page numbers separated by a hyphen-minus character.
	 * For example: 1, 3-5, 7
	 * 
	 * @see #PROPERTY_HIDE_SLIDE_MASTER_PAGES
	 */
	@ExporterProperty(PROPERTY_HIDE_SLIDE_MASTER_PAGES)
	public String getHideSlideMasterPages();
}
