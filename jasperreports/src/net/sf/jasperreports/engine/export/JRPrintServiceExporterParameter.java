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
package net.sf.jasperreports.engine.export;

import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.export.PrintServiceExporterConfiguration;


/**
 * @deprecated Replaced by {@link PrintServiceExporterConfiguration}.
 * @author Teodor Danciu (teodord@users.sourceforge.net)
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
	 * @deprecated Replaced by {@link PrintServiceExporterConfiguration#getPrintRequestAttributeSet()}.
	 */
	public static final JRPrintServiceExporterParameter PRINT_REQUEST_ATTRIBUTE_SET = new JRPrintServiceExporterParameter("PrintRequestAttributeSet Object");


	/**
	 * @deprecated Replaced by {@link PrintServiceExporterConfiguration#getPrintServiceAttributeSet()}.
	 */
	public static final JRPrintServiceExporterParameter PRINT_SERVICE_ATTRIBUTE_SET = new JRPrintServiceExporterParameter("PrintServiceAttributeSet Object");


	/**
	 * @deprecated Replaced by {@link PrintServiceExporterConfiguration#isDisplayPageDialog()}.
	 */
	public static final JRPrintServiceExporterParameter DISPLAY_PAGE_DIALOG = new JRPrintServiceExporterParameter("Display Page Dialog");

	/**
	 * @deprecated Replaced by {@link PrintServiceExporterConfiguration#isDisplayPageDialogOnlyOnce()}.
	 */
	public static final JRPrintServiceExporterParameter DISPLAY_PAGE_DIALOG_ONLY_ONCE = new JRPrintServiceExporterParameter("Display Page Dialog Only Once");

	/**
	 * @deprecated Replaced by {@link PrintServiceExporterConfiguration#isDisplayPrintDialog()}.
	 */
	public static final JRPrintServiceExporterParameter DISPLAY_PRINT_DIALOG = new JRPrintServiceExporterParameter("Display Print Dialog");

	/**
	 * @deprecated Replaced by {@link PrintServiceExporterConfiguration#isDisplayPrintDialogOnlyOnce()}.
	 */
	public static final JRPrintServiceExporterParameter DISPLAY_PRINT_DIALOG_ONLY_ONCE = new JRPrintServiceExporterParameter("Display Print Dialog Only Once");

	/**
	 * @deprecated Replaced by {@link PrintServiceExporterConfiguration#getPrintService()}.
	 */
	public static final JRPrintServiceExporterParameter PRINT_SERVICE = new JRPrintServiceExporterParameter("Print Service");
}
