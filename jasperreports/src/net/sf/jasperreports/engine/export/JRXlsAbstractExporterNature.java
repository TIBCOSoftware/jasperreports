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
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import net.sf.jasperreports.engine.DefaultJasperReportsContext;
import net.sf.jasperreports.engine.JRPrintElement;
import net.sf.jasperreports.engine.JRPrintFrame;
import net.sf.jasperreports.engine.JRPrintText;
import net.sf.jasperreports.engine.JRPropertiesUtil;
import net.sf.jasperreports.engine.JRPropertiesUtil.PropertySuffix;
import net.sf.jasperreports.engine.JasperReportsContext;

/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JRXlsAbstractExporterNature extends AbstractExporterNature
{
	
	public static final String PROPERTY_BREAK_BEFORE_ROW = JRPropertiesUtil.PROPERTY_PREFIX + "export.xls.break.before.row";
	public static final String PROPERTY_BREAK_AFTER_ROW = JRPropertiesUtil.PROPERTY_PREFIX + "export.xls.break.after.row";

	protected boolean isIgnoreGraphics;
	protected boolean isIgnorePageMargins;

	/**
	 * 
	 */
	protected JRXlsAbstractExporterNature(
		JasperReportsContext jasperReportsContext,
		ExporterFilter filter, 
		boolean isIgnoreGraphics,
		boolean isIgnorePageMargins
		)
	{
		super(jasperReportsContext, filter);
		this.isIgnoreGraphics = isIgnoreGraphics;
		this.isIgnorePageMargins = isIgnorePageMargins;
	}
	
	/**
	 * @deprecated Replaced by {@link #JRXlsAbstractExporterNature(JasperReportsContext, ExporterFilter, boolean, boolean)}. 
	 */
	protected JRXlsAbstractExporterNature(ExporterFilter filter, boolean isIgnoreGraphics)
	{
		this(DefaultJasperReportsContext.getInstance(), filter, isIgnoreGraphics, false);
	}
	
	/**
	 * @deprecated Replaced by {@link #JRXlsAbstractExporterNature(JasperReportsContext, ExporterFilter, boolean, boolean)}. 
	 */
	protected JRXlsAbstractExporterNature(ExporterFilter filter, boolean isIgnoreGraphics, boolean isIgnorePageMargins)
	{
		this(DefaultJasperReportsContext.getInstance(), filter, isIgnoreGraphics, isIgnorePageMargins);
	}
	
	/**
	 *
	 */
	public JRPropertiesUtil getPropertiesUtil()
	{
		return propertiesUtil;
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
			return getPropertiesUtil().getBooleanProperty(element, JRXlsAbstractExporter.PROPERTY_AUTO_FIT_ROW, false);
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
			return getPropertiesUtil().getBooleanProperty(element, JRXlsAbstractExporter.PROPERTY_AUTO_FIT_COLUMN, false);
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
			return getPropertiesUtil().getIntegerProperty(element, JRXlsAbstractExporter.PROPERTY_COLUMN_WIDTH, 0);
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
			return getPropertiesUtil().getFloatProperty(element, JRXlsAbstractExporter.PROPERTY_COLUMN_WIDTH_RATIO, 0f);
		}
		return null;
	}

	public List<PropertySuffix> getRowLevelSuffixes(JRPrintElement element)
	{
		if (element.hasProperties())
		{
			return JRPropertiesUtil.getProperties(element,JRXlsAbstractExporter.PROPERTY_ROW_OUTLINE_LEVEL_PREFIX);
		}
		return null;
		
	}
	
	public String getSheetName(JRPrintElement element)
	{
		if (element.hasProperties()
				&& element.getPropertiesMap().containsProperty(JRXlsAbstractExporterParameter.PROPERTY_SHEET_NAME)
				)
			{
				// we make this test to avoid reaching the global default value of the property directly
				// and thus skipping the report level one, if present
				return getPropertiesUtil().getProperty(element, JRXlsAbstractExporterParameter.PROPERTY_SHEET_NAME);
			}
			return null;
	}
	
	public Integer getPageScale(JRPrintElement element)
	{
		if (element.hasProperties()
				&& element.getPropertiesMap().containsProperty(JRXlsAbstractExporter.PROPERTY_PAGE_SCALE)
				)
			{
				// we make this test to avoid reaching the global default value of the property directly
				// and thus skipping the report level one, if present
				return getPropertiesUtil().getIntegerProperty(element, JRXlsAbstractExporter.PROPERTY_PAGE_SCALE, 0);
			}
			return null;
	}

	public Integer getFirstPageNumber(JRPrintElement element)
	{
		if (element.hasProperties()
				&& element.getPropertiesMap().containsProperty(JRXlsAbstractExporter.PROPERTY_FIRST_PAGE_NUMBER)
				)
			{
				// we make this test to avoid reaching the global default value of the property directly
				// and thus skipping the report level one, if present
				return getPropertiesUtil().getIntegerProperty(element, JRXlsAbstractExporter.PROPERTY_FIRST_PAGE_NUMBER, 0);
			}
			return null;
	}

	public void setXProperties(CutsInfo xCuts, JRPrintElement element, int col)
	{
		Map<String,Object> xCutsProperties = xCuts.getPropertiesMap();
		Cut cut = xCuts.getCut(col);
		Map<String,Object> cutProperties = cut.getPropertiesMap();
		
		Boolean columnAutoFit = getColumnAutoFit(element);
		if (columnAutoFit != null)
		{
			if(!cutProperties.containsKey(JRXlsAbstractExporter.PROPERTY_AUTO_FIT_COLUMN))
			{
				cutProperties.put(JRXlsAbstractExporter.PROPERTY_AUTO_FIT_COLUMN, columnAutoFit);
			}
			else
			{
				cutProperties.put(JRXlsAbstractExporter.PROPERTY_AUTO_FIT_COLUMN, (Boolean)cutProperties.get(JRXlsAbstractExporter.PROPERTY_AUTO_FIT_COLUMN) && columnAutoFit);
			}
		}

		Integer columnCustomWidth = getCustomColumnWidth(element);
		Integer cutColumnCustomWidth = (Integer)cutProperties.get(JRXlsAbstractExporter.PROPERTY_COLUMN_WIDTH);
		if (columnCustomWidth != null && (cutColumnCustomWidth == null || cutColumnCustomWidth < columnCustomWidth))
		{
			cutProperties.put(JRXlsAbstractExporter.PROPERTY_COLUMN_WIDTH, columnCustomWidth);
		}

		setXProperties(xCutsProperties, element);
		
	}
	
	public void setXProperties(Map<String,Object> xCutsProperties, JRPrintElement element)
	{
		
		Float widthRatio = getColumnWidthRatio(element);
		Float xCutsWidthRatio = (Float)xCutsProperties.get(JRXlsAbstractExporter.PROPERTY_COLUMN_WIDTH_RATIO);
		if(widthRatio != null && (xCutsWidthRatio == null || xCutsWidthRatio < widthRatio))
		{
			xCutsProperties.put(JRXlsAbstractExporter.PROPERTY_COLUMN_WIDTH_RATIO, widthRatio);
		}

		String sheetName = getSheetName(element);
		if(sheetName != null)
		{
			xCutsProperties.put(JRXlsAbstractExporterParameter.PROPERTY_SHEET_NAME, sheetName);
		}

		Integer pageScale = getPageScale(element);
		if(pageScale != null && pageScale > 9 && pageScale < 401)
		{
			xCutsProperties.put(JRXlsAbstractExporter.PROPERTY_PAGE_SCALE, pageScale);
		}
		
		Integer firstPageNumber = getFirstPageNumber(element);
		if(firstPageNumber != null)
		{
			xCutsProperties.put(JRXlsAbstractExporter.PROPERTY_FIRST_PAGE_NUMBER, firstPageNumber);
		}
	}
	
	public void setYProperties(CutsInfo yCuts, JRPrintElement element, int row)
	{
		Map<String,Object> yCutsProperties = yCuts.getPropertiesMap();
		Cut cut = yCuts.getCut(row);
		Map<String,Object> cutProperties = cut.getPropertiesMap();
		
		Boolean rowAutoFit = getRowAutoFit(element);
		if (rowAutoFit != null)
		{
			if(!cutProperties.containsKey(JRXlsAbstractExporter.PROPERTY_AUTO_FIT_ROW))
			{
				cutProperties.put(JRXlsAbstractExporter.PROPERTY_AUTO_FIT_ROW, rowAutoFit);
			}
			else
			{
				cutProperties.put(JRXlsAbstractExporter.PROPERTY_AUTO_FIT_ROW, (Boolean)cutProperties.get(JRXlsAbstractExporter.PROPERTY_AUTO_FIT_ROW) && rowAutoFit);
			}
		}

		List<PropertySuffix> rowLevelSuffixes = getRowLevelSuffixes(element);
		if(rowLevelSuffixes != null && !rowLevelSuffixes.isEmpty())
		{
			SortedMap<String, Boolean> levelMap = new TreeMap<String, Boolean>();
			for(PropertySuffix suffix : rowLevelSuffixes)
			{
				String level = suffix.getSuffix();
				String marker = suffix.getValue();
				
				levelMap.put(level, "end".equalsIgnoreCase(marker));
			}
			
			cutProperties.put(JRXlsAbstractExporter.PROPERTY_ROW_OUTLINE_LEVEL_PREFIX, levelMap);
		}
		
		setYProperties(yCutsProperties, element);
	}
	
	public void setYProperties(Map<String,Object> yCutsProperties, JRPrintElement element)
	{
		// nothing to do here
	}

}
