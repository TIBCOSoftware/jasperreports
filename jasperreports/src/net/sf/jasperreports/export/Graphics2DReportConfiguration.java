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
import net.sf.jasperreports.engine.export.JRGraphics2DExporter;
import net.sf.jasperreports.engine.util.JRStyledText;
import net.sf.jasperreports.export.annotations.ExporterParameter;
import net.sf.jasperreports.export.annotations.ExporterProperty;


/**
 * Interface containing settings used by the Graphics2D exporter.
 *
 * @see JRGraphics2DExporter
 * 
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public interface Graphics2DReportConfiguration extends ReportExportConfiguration
{
	/**
	 * Property that provides a default value for the {@link #isMinimizePrinterJobSize()} exporter configuration setting.
	 */
	public static final String MINIMIZE_PRINTER_JOB_SIZE = JRPropertiesUtil.PROPERTY_PREFIX + "export.graphics2d.min.job.size";

	/**
	 * The zoom ratio used for the export. The default value is 1.
	 */
	@SuppressWarnings("deprecation")
	@ExporterParameter(
		type=net.sf.jasperreports.engine.export.JRGraphics2DExporterParameter.class,
		name="ZOOM_RATIO"
		)
	public Float getZoomRatio();
	
	/**
	 * Flag to control the use of an AWT rendering fix which causes the printer job size to be reduced when
	 * the exporter draws onto a printer graphic context.
	 *
	 * The fix was introduced to solve an old Java printing problem related to the size of printer spool jobs.
	 * However, it causes problems when bidirectional text is rendered, by losing text direction information.
	 *
	 * This flag is true, by default and should be set to false when bidirectional writing is present in
	 * the document that is sent to the printer.
	 *
	 * @see #MINIMIZE_PRINTER_JOB_SIZE
	 */
	@SuppressWarnings("deprecation")
	@ExporterParameter(
		type=net.sf.jasperreports.engine.export.JRGraphics2DExporterParameter.class, 
		name="MINIMIZE_PRINTER_JOB_SIZE"
		)
	@ExporterProperty(
		value=MINIMIZE_PRINTER_JOB_SIZE, 
		booleanDefault=true
		)
	public Boolean isMinimizePrinterJobSize();
	
	/**
	 * 
	 */
	@ExporterProperty(
		value=JRStyledText.PROPERTY_AWT_IGNORE_MISSING_FONT, 
		booleanDefault=false
		)
	public Boolean isIgnoreMissingFont();
}
