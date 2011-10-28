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
package net.sf.jasperreports.engine.export;

import net.sf.jasperreports.engine.JRExporterParameter;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JRPrintServiceExporterParameter extends JRExporterParameter
{


	/**
	 *
	 */
	protected JRPrintServiceExporterParameter(String name)
	{
		super(name);
	}


	/**
	 * An instance of <tt>javax.print.attribute.PrintRequestAttributeSet</tt>.
	 */
	public static final JRPrintServiceExporterParameter PRINT_REQUEST_ATTRIBUTE_SET = new JRPrintServiceExporterParameter("PrintRequestAttributeSet Object");


	/**
	 * An instance of <tt>javax.print.attribute.PrintServiceAttributeSet</tt>.
	 */
	public static final JRPrintServiceExporterParameter PRINT_SERVICE_ATTRIBUTE_SET = new JRPrintServiceExporterParameter("PrintServiceAttributeSet Object");


	/**
	 * A boolean value specifying whether a page setup dialog should be opened before printing.
	 */
	public static final JRPrintServiceExporterParameter DISPLAY_PAGE_DIALOG = new JRPrintServiceExporterParameter("Display Page Dialog");

	/**
	 * A boolean value specifying whether a page setup dialog should be opened only once before printing in a batch export job.
	 */
	public static final JRPrintServiceExporterParameter DISPLAY_PAGE_DIALOG_ONLY_ONCE = new JRPrintServiceExporterParameter("Display Page Dialog Only Once");

	/**
	 * A boolean value specifying whether a print setup dialog should be opened before printing.
	 */
	public static final JRPrintServiceExporterParameter DISPLAY_PRINT_DIALOG = new JRPrintServiceExporterParameter("Display Print Dialog");

	/**
	 * A boolean value specifying whether a print setup dialog should be opened only once in a batch export job.
	 */
	public static final JRPrintServiceExporterParameter DISPLAY_PRINT_DIALOG_ONLY_ONCE = new JRPrintServiceExporterParameter("Display Print Dialog Only Once");

	/**
	 * An instance of <tt>javax.print.PrintService</tt>, useful if users do not want JPS to lookup for an available print service.
	 */
	public static final JRPrintServiceExporterParameter PRINT_SERVICE = new JRPrintServiceExporterParameter("Print Service");
}
