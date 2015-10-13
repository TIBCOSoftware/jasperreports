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

import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import net.sf.jasperreports.charts.type.EdgeEnum;
import net.sf.jasperreports.engine.DefaultJasperReportsContext;
import net.sf.jasperreports.engine.JRGenericPrintElement;
import net.sf.jasperreports.engine.JRPrintElement;
import net.sf.jasperreports.engine.JRPrintFrame;
import net.sf.jasperreports.engine.JRPrintText;
import net.sf.jasperreports.engine.JRPropertiesUtil;
import net.sf.jasperreports.engine.JRPropertiesUtil.PropertySuffix;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.export.XlsReportConfiguration;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public class JRXlsAbstractExporterNature extends AbstractExporterNature
{
	/**
	 * @deprecated Replaced by {@link JRXlsAbstractExporter#PROPERTY_BREAK_BEFORE_ROW}.
	 */
	public static final String PROPERTY_BREAK_BEFORE_ROW = JRXlsAbstractExporter.PROPERTY_BREAK_BEFORE_ROW;
	/**
	 * @deprecated Replaced by {@link JRXlsAbstractExporter#PROPERTY_BREAK_AFTER_ROW}.
	 */
	public static final String PROPERTY_BREAK_AFTER_ROW = JRXlsAbstractExporter.PROPERTY_BREAK_AFTER_ROW;

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
			(!isIgnoreGraphics || (element instanceof JRPrintText) || (element instanceof JRPrintFrame) || (element instanceof JRGenericPrintElement))
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
		return element.hasProperties() 
				&& JRPropertiesUtil.asBoolean(element.getPropertiesMap().getProperty(JRXlsAbstractExporter.PROPERTY_BREAK_BEFORE_ROW));
	}
	
	/**
	 *
	 */
	public boolean isBreakAfterRow(JRPrintElement element)
	{
		return element.hasProperties()
				&& JRPropertiesUtil.asBoolean(element.getPropertiesMap().getProperty(JRXlsAbstractExporter.PROPERTY_BREAK_AFTER_ROW));
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
	
	/**
	 *
	 */
	public Boolean getShowGridlines(JRPrintElement element)
	{
		if (
				element.hasProperties()
				&& element.getPropertiesMap().containsProperty(XlsReportConfiguration.PROPERTY_SHOW_GRIDLINES)
				)
		{
			// we make this test to avoid reaching the global default value of the property directly
			// and thus skipping the report level one, if present
			return getPropertiesUtil().getBooleanProperty(element, XlsReportConfiguration.PROPERTY_SHOW_GRIDLINES, true);
		}
		return null;
	}
	
	public Boolean getIgnoreCellBackground(JRPrintElement element)
	{
		if (
				element.hasProperties()
				&& element.getPropertiesMap().containsProperty(XlsReportConfiguration.PROPERTY_IGNORE_CELL_BACKGROUND)
				)
		{
			// we make this test to avoid reaching the global default value of the property directly
			// and thus skipping the report level one, if present
			return getPropertiesUtil().getBooleanProperty(element, XlsReportConfiguration.PROPERTY_IGNORE_CELL_BACKGROUND, false);
		}
		return null;
	}
	
	public Boolean getIgnoreCellBorder(JRPrintElement element)
	{
		if (
			element.hasProperties()
			&& element.getPropertiesMap().containsProperty(XlsReportConfiguration.PROPERTY_IGNORE_CELL_BORDER)
			)
		{
			// we make this test to avoid reaching the global default value of the property directly
			// and thus skipping the report level one, if present
			return getPropertiesUtil().getBooleanProperty(element, XlsReportConfiguration.PROPERTY_IGNORE_CELL_BORDER, false);
		}
		return null;
	}

	public Boolean getWhitePageBackground(JRPrintElement element)
	{
		if (
				element.hasProperties()
				&& element.getPropertiesMap().containsProperty(XlsReportConfiguration.PROPERTY_WHITE_PAGE_BACKGROUND)
				)
		{
			// we make this test to avoid reaching the global default value of the property directly
			// and thus skipping the report level one, if present
			return getPropertiesUtil().getBooleanProperty(element, XlsReportConfiguration.PROPERTY_WHITE_PAGE_BACKGROUND, false);
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
			&& element.getPropertiesMap().containsProperty(XlsReportConfiguration.PROPERTY_COLUMN_WIDTH_RATIO)
			)
		{
			// we make this test to avoid reaching the global default value of the property directly
			// and thus skipping the report level one, if present
			return getPropertiesUtil().getFloatProperty(element, XlsReportConfiguration.PROPERTY_COLUMN_WIDTH_RATIO, 0f);
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
				&& element.getPropertiesMap().containsProperty(JRXlsAbstractExporter.PROPERTY_SHEET_NAME)
				)
		{
			// we make this test to avoid reaching the global default value of the property directly
			// and thus skipping the report level one, if present
			return getPropertiesUtil().getProperty(element, JRXlsAbstractExporter.PROPERTY_SHEET_NAME);
		}
		return null;
	}
	
	public EdgeEnum getFreezeRowEdge(JRPrintElement element)
	{
		if (element.hasProperties()
				&& element.getPropertiesMap().containsProperty(JRXlsAbstractExporter.PROPERTY_FREEZE_ROW_EDGE)
				)
		{
			// we make this test to avoid reaching the global default value of the property directly
			// and thus skipping the report level one, if present
			return EdgeEnum.getByName(getPropertiesUtil().getProperty(element, JRXlsAbstractExporter.PROPERTY_FREEZE_ROW_EDGE));
		}
		return null;
	}
	
	public EdgeEnum getFreezeColumnEdge(JRPrintElement element)
	{
		if (element.hasProperties()
				&& element.getPropertiesMap().containsProperty(JRXlsAbstractExporter.PROPERTY_FREEZE_COLUMN_EDGE)
				)
			{
				// we make this test to avoid reaching the global default value of the property directly
				// and thus skipping the report level one, if present
				return EdgeEnum.getByName(getPropertiesUtil().getProperty(element, JRXlsAbstractExporter.PROPERTY_FREEZE_COLUMN_EDGE));
			}
			return null;
	}
	
	public String getSheetTabColor(JRPrintElement element)
	{
		if (element.hasProperties()
				&& element.getPropertiesMap().containsProperty(XlsReportConfiguration.PROPERTY_SHEET_TAB_COLOR)
				)
			{
				// we make this test to avoid reaching the global default value of the property directly
				// and thus skipping the report level one, if present
				return getPropertiesUtil().getProperty(element, XlsReportConfiguration.PROPERTY_SHEET_TAB_COLOR);
			}
			return null;
	}
	
	public Integer getPageScale(JRPrintElement element)
	{
		if (
			element.hasProperties()
			&& element.getPropertiesMap().containsProperty(XlsReportConfiguration.PROPERTY_PAGE_SCALE)
			)
		{
			// we make this test to avoid reaching the global default value of the property directly
			// and thus skipping the report level one, if present
			return getPropertiesUtil().getIntegerProperty(element, XlsReportConfiguration.PROPERTY_PAGE_SCALE, 0);
		}
		return null;
	}

	public Integer getFirstPageNumber(JRPrintElement element)
	{
		if (element.hasProperties()
			&& element.getPropertiesMap().containsProperty(XlsReportConfiguration.PROPERTY_FIRST_PAGE_NUMBER)
			)
		{
			// we make this test to avoid reaching the global default value of the property directly
			// and thus skipping the report level one, if present
			return getPropertiesUtil().getIntegerProperty(element, XlsReportConfiguration.PROPERTY_FIRST_PAGE_NUMBER, 0);
		}
		return null;
	}

	public void setXProperties(CutsInfo xCuts, JRPrintElement element, int row1, int col1, int row2, int col2)
	{
		Map<String,Object> xCutsProperties = xCuts.getPropertiesMap();
		Cut cut = xCuts.getCut(col1);
		
		Boolean columnAutoFit = getColumnAutoFit(element);
		if (columnAutoFit != null)
		{
			if(!cut.hasProperty(JRXlsAbstractExporter.PROPERTY_AUTO_FIT_COLUMN))
			{
				cut.setProperty(JRXlsAbstractExporter.PROPERTY_AUTO_FIT_COLUMN, columnAutoFit);
			}
			else
			{
				cut.setProperty(JRXlsAbstractExporter.PROPERTY_AUTO_FIT_COLUMN, (Boolean)cut.getProperty(JRXlsAbstractExporter.PROPERTY_AUTO_FIT_COLUMN) && columnAutoFit);
			}
		}

		Integer columnCustomWidth = getCustomColumnWidth(element);
		Integer cutColumnCustomWidth = (Integer)cut.getProperty(JRXlsAbstractExporter.PROPERTY_COLUMN_WIDTH);
		if (columnCustomWidth != null && (cutColumnCustomWidth == null || cutColumnCustomWidth < columnCustomWidth))
		{
			cut.setProperty(JRXlsAbstractExporter.PROPERTY_COLUMN_WIDTH, columnCustomWidth);
		}

		setXProperties(xCutsProperties, element);
		
	}
	
	public void setXProperties(Map<String,Object> xCutsProperties, JRPrintElement element)
	{
		Float widthRatio = getColumnWidthRatio(element);
		Float xCutsWidthRatio = (Float)xCutsProperties.get(XlsReportConfiguration.PROPERTY_COLUMN_WIDTH_RATIO);
		if(widthRatio != null && (xCutsWidthRatio == null || xCutsWidthRatio < widthRatio))
		{
			xCutsProperties.put(XlsReportConfiguration.PROPERTY_COLUMN_WIDTH_RATIO, widthRatio);
		}
	}
	
	public void setYProperties(CutsInfo yCuts, JRPrintElement element, int row1, int col1, int row2, int col2)
	{
		Map<String,Object> yCutsProperties = yCuts.getPropertiesMap();
		Cut cut = yCuts.getCut(row1);
		
		Boolean rowAutoFit = getRowAutoFit(element);
		if (rowAutoFit != null)
		{
			if(!cut.hasProperty(JRXlsAbstractExporter.PROPERTY_AUTO_FIT_ROW))
			{
				cut.setProperty(JRXlsAbstractExporter.PROPERTY_AUTO_FIT_ROW, rowAutoFit);
			}
			else
			{
				cut.setProperty(JRXlsAbstractExporter.PROPERTY_AUTO_FIT_ROW, (Boolean)cut.getProperty(JRXlsAbstractExporter.PROPERTY_AUTO_FIT_ROW) && rowAutoFit);
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
			
			cut.setProperty(JRXlsAbstractExporter.PROPERTY_ROW_OUTLINE_LEVEL_PREFIX, levelMap);
		}
		
		String sheetName = getSheetName(element);
		if(sheetName != null)
		{
			cut.setProperty(JRXlsAbstractExporter.PROPERTY_SHEET_NAME, sheetName);
		}

		String tabColor = getSheetTabColor(element);
		if(tabColor != null)
		{
			cut.setProperty(XlsReportConfiguration.PROPERTY_SHEET_TAB_COLOR, tabColor);
		}

		Integer pageScale = getPageScale(element);
		if(pageScale != null && pageScale > 9 && pageScale < 401)
		{
			cut.setProperty(XlsReportConfiguration.PROPERTY_PAGE_SCALE, pageScale);
		}
		
		Integer firstPageNumber = getFirstPageNumber(element);
		if(firstPageNumber != null)
		{
			cut.setProperty(XlsReportConfiguration.PROPERTY_FIRST_PAGE_NUMBER, firstPageNumber);
		}
		
		Boolean showGridlines = getShowGridlines(element);
		if(showGridlines != null)
		{
			cut.setProperty(XlsReportConfiguration.PROPERTY_SHOW_GRIDLINES, showGridlines);
		}
		
		Boolean ignoreCellBackground = getIgnoreCellBackground(element);
		if(ignoreCellBackground != null)
		{
			cut.setProperty(XlsReportConfiguration.PROPERTY_IGNORE_CELL_BACKGROUND, ignoreCellBackground);
		}
		
		Boolean ignoreCellBorder = getIgnoreCellBorder(element);
		if(ignoreCellBorder != null)
		{
			cut.setProperty(XlsReportConfiguration.PROPERTY_IGNORE_CELL_BORDER, ignoreCellBorder);
		}

		Boolean whitePageBackground = getWhitePageBackground(element);
		if(whitePageBackground != null)
		{
			cut.setProperty(XlsReportConfiguration.PROPERTY_WHITE_PAGE_BACKGROUND, whitePageBackground);
		}

		EdgeEnum freezeColumnEdge = getFreezeColumnEdge(element);
		int columnFreezeIndex = freezeColumnEdge == null 
				? 0
				: (EdgeEnum.RIGHT.equals(freezeColumnEdge) 
					? col2
					: col1
					);
		if(columnFreezeIndex > 0)
		{
			cut.setProperty(JRXlsAbstractExporter.PROPERTY_FREEZE_COLUMN_EDGE, columnFreezeIndex);
		}
		
		EdgeEnum freezeRowEdge = getFreezeRowEdge(element);
		int rowFreezeIndex = freezeRowEdge == null 
			? 0
			: (EdgeEnum.BOTTOM.equals(freezeRowEdge) 
					? row2
					: row1
					);
		if(rowFreezeIndex > 0)
		{
			cut.setProperty(JRXlsAbstractExporter.PROPERTY_FREEZE_ROW_EDGE, rowFreezeIndex);
		}
		
		setYProperties(yCutsProperties, element);
	}
	
	public void setYProperties(Map<String,Object> yCutsProperties, JRPrintElement element)
	{
		// nothing to do here
	}

}
