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
 * Greg Hilton 
 */

package net.sf.jasperreports.engine.export;

import net.sf.jasperreports.engine.DefaultJasperReportsContext;
import net.sf.jasperreports.engine.JRGenericPrintElement;
import net.sf.jasperreports.engine.JRPrintElement;
import net.sf.jasperreports.engine.JasperReportsContext;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public class JRXlsExporterNature extends JRXlsAbstractExporterNature
{

	/**
	 * @deprecated Replaced by {@link #JRXlsExporterNature(JasperReportsContext, ExporterFilter, boolean, boolean)}.
	 */
	public JRXlsExporterNature(ExporterFilter filter, boolean isIgnoreGraphics)
	{
		super(DefaultJasperReportsContext.getInstance(), filter, isIgnoreGraphics, false);
	}
	
	/**
	 * @deprecated Replaced by {@link #JRXlsExporterNature(JasperReportsContext, ExporterFilter, boolean, boolean)}.
	 */
	public JRXlsExporterNature(ExporterFilter filter, boolean isIgnoreGraphics, boolean isIgnorePageMargins)
	{
		super(DefaultJasperReportsContext.getInstance(), filter, isIgnoreGraphics, isIgnorePageMargins);
	}

	/**
	 * 
	 */
	public JRXlsExporterNature(
		JasperReportsContext jasperReportsContext,
		ExporterFilter filter, 
		boolean isIgnoreGraphics, 
		boolean isIgnorePageMargins
		)
	{
		super(jasperReportsContext, filter, isIgnoreGraphics, isIgnorePageMargins);
	}

	public boolean isToExport(JRPrintElement element)
	{
		boolean isToExport = true;
		if (element instanceof JRGenericPrintElement)
		{
			JRGenericPrintElement genericElement = (JRGenericPrintElement) element;
			GenericElementHandler handler = handlerEnvironment.getElementHandler(
					genericElement.getGenericType(), JRXlsExporter.XLS_EXPORTER_KEY);
			if (handler == null || !handler.toExport(genericElement))
			{
				isToExport = false;
			}
		}

		return isToExport && super.isToExport(element);
	}
	
}
