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
import net.sf.jasperreports.engine.JRPrintFrame;
import net.sf.jasperreports.engine.JasperReportsContext;

/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @deprecated To be removed.
 */
public class JRHtmlExporterNature extends AbstractExporterNature
{
	
	private final boolean deep;
	private final boolean ignorePageMargins;
	
	/**
	 * 
	 */
	public JRHtmlExporterNature(JasperReportsContext jasperReportsContext, ExporterFilter filter, boolean deep, boolean ignorePageMargins)
	{
		super(jasperReportsContext, filter);
		this.deep = deep;
		this.ignorePageMargins = ignorePageMargins;
	}
	
	/**
	 * @deprecated Replaced by {@link #JRHtmlExporterNature(JasperReportsContext, ExporterFilter, boolean, boolean)}.
	 */
	public JRHtmlExporterNature(ExporterFilter filter, boolean deep)
	{
		this(DefaultJasperReportsContext.getInstance(), filter, deep, false);
	}
	
	/**
	 * @deprecated Replaced by {@link #JRHtmlExporterNature(JasperReportsContext, ExporterFilter, boolean, boolean)}.
	 */
	public JRHtmlExporterNature(ExporterFilter filter, boolean deep, boolean ignorePageMargins)
	{
		this(DefaultJasperReportsContext.getInstance(), filter, deep, ignorePageMargins);
	}
	
	/**
	 * 
	 */
	public boolean isToExport(JRPrintElement element)
	{
		if (element instanceof JRGenericPrintElement)
		{
			JRGenericPrintElement genericElement = (JRGenericPrintElement) element;
			GenericElementHtmlHandler handler = (GenericElementHtmlHandler)handlerEnvironment.getElementHandler(
					genericElement.getGenericType(), JRHtmlExporter.HTML_EXPORTER_KEY);
			if (handler == null || !handler.toExport(genericElement))
			{
				return false;
			}
		}

		return filter == null || filter.isToExport(element);
	}
	
	/**
	 * 
	 */
	public boolean isDeep(JRPrintFrame frame)
	{
		if (
			frame.hasProperties()
			&& frame.getPropertiesMap().containsProperty(JRHtmlExporterParameter.PROPERTY_FRAMES_AS_NESTED_TABLES)
			)
		{
			// we make this test to avoid reaching the global default value of the property directly
			// and thus skipping the report level one, if present
			return !getPropertiesUtil().getBooleanProperty(frame, JRHtmlExporterParameter.PROPERTY_FRAMES_AS_NESTED_TABLES, !deep);
		}
		return deep;
	}

	/**
	 * 
	 */
	public boolean isSpanCells()
	{
		return true;
	}
	
	/**
	 * 
	 */
	public boolean isIgnoreLastRow()
	{
		return false;
	}

	public boolean isHorizontallyMergeEmptyCells()
	{
		return true;
	}

	/**
	 * Specifies whether empty page margins should be ignored
	 */
	public boolean isIgnorePageMargins()
	{
		return ignorePageMargins;
	}
	
	/**
	 *
	 */
	public boolean isBreakBeforeRow(JRPrintElement element)
	{
		return false;
	}
	
	/**
	 *
	 */
	public boolean isBreakAfterRow(JRPrintElement element)
	{
		return false;
	}
	
}
