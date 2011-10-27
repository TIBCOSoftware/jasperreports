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

/*
 * Contributors:
 * Greg Hilton 
 */

package net.sf.jasperreports.engine.export;

import java.util.List;

import net.sf.jasperreports.engine.JRPrintElement;
import net.sf.jasperreports.engine.JRPrintFrame;
import net.sf.jasperreports.engine.JRPrintText;
import net.sf.jasperreports.engine.util.JRProperties;

/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JRXlsAbstractExporterNature implements ExporterNature
{
	
	public static final String PROPERTY_BREAK_BEFORE_ROW = JRProperties.PROPERTY_PREFIX + "export.xls.break.before.row";
	public static final String PROPERTY_BREAK_AFTER_ROW = JRProperties.PROPERTY_PREFIX + "export.xls.break.after.row";

	protected ExporterFilter filter;
	protected boolean isIgnoreGraphics;
	protected boolean isIgnorePageMargins;

	/**
	 * 
	 */
	protected JRXlsAbstractExporterNature(ExporterFilter filter, boolean isIgnoreGraphics)
	{
		this(filter, isIgnoreGraphics, false);
	}
	
	/**
	 * 
	 */
	protected JRXlsAbstractExporterNature(ExporterFilter filter, boolean isIgnoreGraphics, boolean isIgnorePageMargins)
	{
		this.filter = filter;
		this.isIgnoreGraphics = isIgnoreGraphics;
		this.isIgnorePageMargins = isIgnorePageMargins;
	}
	
	/**
	 *
	 */
	public boolean isToExport(JRPrintElement element)
	{
		return 
			(!isIgnoreGraphics || (element instanceof JRPrintText) || (element instanceof JRPrintFrame))
			&& (filter == null || filter.isToExport(element));
	}
	
	/**
	 * 
	 */
	public boolean isDeep(JRPrintFrame frame)
	{
		return true;
	}
	
	/**
	 * 
	 */
	public boolean isSplitSharedRowSpan()
	{
		return false;
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
	
	/**
	 * 
	 */
	public boolean isHorizontallyMergeEmptyCells()
	{
		return false;
	}

	/**
	 * Specifies whether empty page margins should be ignored
	 */
	public boolean isIgnorePageMargins()
	{
		return isIgnorePageMargins;
	}
	
	/**
	 *
	 */
	public boolean isBreakBeforeRow(JRPrintElement element)
	{
		return Boolean.valueOf(element.getPropertiesMap().getProperty(PROPERTY_BREAK_BEFORE_ROW)).booleanValue();
	}
	
	/**
	 *
	 */
	public boolean isBreakAfterRow(JRPrintElement element)
	{
		return Boolean.valueOf(element.getPropertiesMap().getProperty(PROPERTY_BREAK_AFTER_ROW)).booleanValue();
	}
	
	/**
	 *
	 */
	public Boolean getRowAutoFit(JRPrintElement element)
	{
		if (
			element.hasProperties()
			&& element.getPropertiesMap().containsProperty(JRXlsAbstractExporter.PROPERTY_AUTO_FIT_ROW)
			)
		{
			// we make this test to avoid reaching the global default value of the property directly
			// and thus skipping the report level one, if present
			return JRProperties.getBooleanProperty(element, JRXlsAbstractExporter.PROPERTY_AUTO_FIT_ROW, false);
		}
		return null;
	}
	
	/**
	 *
	 */
	public Boolean getColumnAutoFit(JRPrintElement element)
	{
		if (
			element.hasProperties()
			&& element.getPropertiesMap().containsProperty(JRXlsAbstractExporter.PROPERTY_AUTO_FIT_COLUMN)
			)
		{
			// we make this test to avoid reaching the global default value of the property directly
			// and thus skipping the report level one, if present
			return JRProperties.getBooleanProperty(element, JRXlsAbstractExporter.PROPERTY_AUTO_FIT_COLUMN, false);
		}
		return null;
	}

	public Integer getCustomColumnWidth(JRPrintElement element) {
		if (element.hasProperties()
			&& element.getPropertiesMap().containsProperty(JRXlsAbstractExporter.PROPERTY_COLUMN_WIDTH)
			)
		{
			// we make this test to avoid reaching the global default value of the property directly
			// and thus skipping the report level one, if present
			return JRProperties.getIntegerProperty(element, JRXlsAbstractExporter.PROPERTY_COLUMN_WIDTH, 0);
		}
		return null;
	}
	
	public Float getColumnWidthRatio(JRPrintElement element) {
		if (element.hasProperties()
			&& element.getPropertiesMap().containsProperty(JRXlsAbstractExporter.PROPERTY_COLUMN_WIDTH_RATIO)
			)
		{
			// we make this test to avoid reaching the global default value of the property directly
			// and thus skipping the report level one, if present
			return JRProperties.getFloatProperty(element, JRXlsAbstractExporter.PROPERTY_COLUMN_WIDTH_RATIO, 0f);
		}
		return null;
	}

	public List<JRProperties.PropertySuffix> getRowLevelSuffixes(JRPrintElement element)
	{
		if (element.hasProperties())
		{
			return JRProperties.getProperties(element,JRXlsAbstractExporter.PROPERTY_ROW_OUTLINE_LEVEL_PREFIX);
		}
		return null;
		
	}
}
