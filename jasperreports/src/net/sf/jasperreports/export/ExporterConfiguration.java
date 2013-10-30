/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2013 Jaspersoft Corporation. All rights reserved.
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
import net.sf.jasperreports.engine.export.ExporterFilter;
import net.sf.jasperreports.engine.export.JRExportProgressMonitor;
import net.sf.jasperreports.engine.export.JRHyperlinkProducerFactory;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public interface ExporterConfiguration
{
	/**
	 * This property serves as default value for the {@link XlsExporterConfiguration#isIgnorePageMargins()} 
	 * and {@link HtmlExporterConfiguration#isIgnorePageMargins()} exporter configuration settings.
	 * <p>
	 * The property itself defaults to <code>false</code>.
	 * </p>
	 * @see JRPropertiesUtil
	 */
	public static final String PROPERTY_IGNORE_PAGE_MARGINS = JRPropertiesUtil.PROPERTY_PREFIX + "export.ignore.page.margins";

	/**
	 * 
	 */
	public Boolean isOverrideHints();
	

	/**
	 * 
	 */
	public Integer getStartPageIndex();
	

	/**
	 * 
	 */
	public Integer getEndPageIndex();
	

	/**
	 * 
	 */
	public Integer getPageIndex();
	

	/**
	 * 
	 */
	public JRExportProgressMonitor getProgressMonitor();
	

	/**
	 * 
	 */
	public ExporterFilter getExporterFilter();
	

	/**
	 * 
	 */
	public Integer getOffsetX();
	

	/**
	 * 
	 */
	public Integer getOffsetY();
	

	/**
	 * 
	 */
	public JRHyperlinkProducerFactory getHyperlinkProducerFactory();
}
