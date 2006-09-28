/*
 * ============================================================================
 * GNU Lesser General Public License
 * ============================================================================
 *
 * JasperReports - Free Java report-generating library.
 * Copyright (C) 2001-2006 JasperSoft Corporation http://www.jaspersoft.com
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307, USA.
 * 
 * JasperSoft Corporation
 * 303 Second Street, Suite 450 North
 * San Francisco, CA 94107
 * http://www.jaspersoft.com
 */
package net.sf.jasperreports.engine;


/**
 * An abstract representation of a Jasper report. This interface is inherited by all report implementations
 * (designs, compiled reports, filled reports). It only contains constants and getters and setters for the most common
 * report properties and elements.
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public interface JRReport extends JRDefaultFontProvider, JRDefaultStyleProvider
{

	
	/**
	 * A constant used to specify that the language used by expressions is Java.
	 */
	public static final String LANGUAGE_JAVA = "java";

	/**
	 * A constant used to specify that the language used by expressions is Groovy.
	 */
	public static final String LANGUAGE_GROOVY = "groovy";

	/**
	 * Specifies that columns in a report should be filled vertically (fill an entire column and then go to the
	 * next one).
	 */
	public static final byte PRINT_ORDER_VERTICAL = 1;


	/**
	 * Specifies that columns in a report should be filled horizontalyy (columns are filled proportionally).
	 */
	public static final byte PRINT_ORDER_HORIZONTAL = 2;


	/**
	 * Specifies a portrait orientation. This is used mostly to inform printers of page layouts.
	 */
	public static final byte ORIENTATION_PORTRAIT = 1;


	/**
	 * Specifies a landscape orientation. This is used mostly to inform printers of page layouts.
	 */
	public static final byte ORIENTATION_LANDSCAPE = 2;

	/**
	 * Specifies that in case of empty datasources, there will be an empty report.
	 */
	public static final byte WHEN_NO_DATA_TYPE_NO_PAGES = 1;


	/**
	 * Specifies that in case of empty datasources, there will be a report with just one blank page.
	 */
	public static final byte WHEN_NO_DATA_TYPE_BLANK_PAGE = 2;


	/**
	 * Specifies that in case of empty datasources, all sections except detail will displayed.
	 */
	public static final byte WHEN_NO_DATA_TYPE_ALL_SECTIONS_NO_DETAIL = 3;


	/**
	 * Return NULL when a resource is missing.
	 */
	public static final byte WHEN_RESOURCE_MISSING_TYPE_NULL = JRDataset.WHEN_RESOURCE_MISSING_TYPE_NULL;


	/**
	 * Return empty string when a resource is missing.
	 */
	public static final byte WHEN_RESOURCE_MISSING_TYPE_EMPTY = JRDataset.WHEN_RESOURCE_MISSING_TYPE_EMPTY;


	/**
	 * Return the key when a resource is missing.
	 */
	public static final byte WHEN_RESOURCE_MISSING_TYPE_KEY = JRDataset.WHEN_RESOURCE_MISSING_TYPE_KEY;


	/**
	 * Throw an exception when a resource is missing.
	 */
	public static final byte WHEN_RESOURCE_MISSING_TYPE_ERROR = JRDataset.WHEN_RESOURCE_MISSING_TYPE_ERROR;

	
	/**
	 * Gets the report name.
	 */
	public String getName();

	/**
	 * Gets the report language. Should be Java or Groovy.
	 */
	public String getLanguage();
		
	/**
	 * Gets the number of columns on each page
	 */
	public int getColumnCount();
		
	/**
	 * Specifies whether columns will be filled horizontally or vertically.
	 * @see JRReport PRINT_ORDER_VERTICAL,
	 * @see JRReport PRINT_ORDER_HORIZONTAL
	 */
	public byte getPrintOrder();
		
	/**
	 *
	 */
	public int getPageWidth();
		
	/**
	 *
	 */
	public int getPageHeight();
		
	/**
	 *
	 */
	public byte getOrientation();
		
	/**
	 * Specifies the report behaviour in case of empty datasources.
	 */
	public byte getWhenNoDataType();
		
	/**
	 * Sets the report behaviour in case of empty datasources.
	 */
	public void setWhenNoDataType(byte whenNoDataType);
		
	/**
	 *
	 */
	public int getColumnWidth();
		
	/**
	 * Specifies the space between columns on the same page.
	 */
	public int getColumnSpacing();
		
	/**
	 *
	 */
	public int getLeftMargin();
		
	/**
	 *
	 */
	public int getRightMargin();
		
	/**
	 *
	 */
	public int getTopMargin();
		
	/**
	 *
	 */
	public int getBottomMargin();
		
	/**
	 * Specifies if the title section will be printed on a separate initial page.
	 */
	public boolean isTitleNewPage();
		
	/**
	 * Specifies if the summary section will be printed on a separate last page.
	 */
	public boolean isSummaryNewPage();
		
	/**
	 * Specifie if the column footer section will be printed at the bottom of the column or if it
	 * will immediately follow the last detail or group footer printed on the current column.

	 */
	public boolean isFloatColumnFooter();
		
	/**
	 *
	 */
	public String getScriptletClass();

	/**
	 * Gets the base name of the report associated resource bundle.
	 */
	public String getResourceBundle();

	/**
	 * Gets an array of report properties names.
	 */
	public String[] getPropertyNames();

	/**
	 * Gets a property value
	 * @param name the property name
	 */
	public String getProperty(String name);

	/**
	 *
	 */
	public void setProperty(String name, String value);

	/**
	 *
	 */
	public void removeProperty(String name);

	/**
	 * Gets an array of imports (needed if report expression require additional classes in order to compile).
	 */
	public String[] getImports();

	/**
	 * Gets an array of report fonts.
	 */
	public JRReportFont[] getFonts();

	/**
	 * Gets an array of report styles.
	 */
	public JRStyle[] getStyles();

	/**
	 *
	 */
	public JRParameter[] getParameters();

	/**
	 *
	 */
	public JRQuery getQuery();

	/**
	 *
	 */
	public JRField[] getFields();

	/**
	 *
	 */
	public JRSortField[] getSortFields();

	/**
	 *
	 */
	public JRVariable[] getVariables();

	/**
	 *
	 */
	public JRGroup[] getGroups();

	/**
	 *
	 */
	public JRBand getBackground();

	/**
	 *
	 */
	public JRBand getTitle();

	/**
	 *
	 */
	public JRBand getPageHeader();

	/**
	 *
	 */
	public JRBand getColumnHeader();

	/**
	 *
	 */
	public JRBand getDetail();

	/**
	 *
	 */
	public JRBand getColumnFooter();

	/**
	 *
	 */
	public JRBand getPageFooter();

	/**
	 *
	 */
	public JRBand getLastPageFooter();

	/**
	 *
	 */
	public JRBand getSummary();

	/**
	 * Returns the resource missing handling type.
	 */
	public byte getWhenResourceMissingType();
		
	/**
	 * Sets the resource missing handling type.
	 * @param whenResourceMissingType the resource missing handling type
	 */
	public void setWhenResourceMissingType(byte whenResourceMissingType);

	
	/**
	 * Returns the main report dataset.
	 * <p>
	 * The main report dataset consists of all parameters, fields, variables and groups of the report.
	 * 
	 * @return the main report dataset
	 */
	public JRDataset getMainDataset();
	
	
	/**
	 * Returns the datasets of this report.
	 * 
	 * @return the datasets of this report
	 */
	public JRDataset[] getDatasets();
	
	
	/**
	 * Decides whether to use pagination when filling the report.
	 * <p>
	 * If set to <code>true</code> the report will be generated on one long page.
	 * <p>
	 * The design attribute can be overridden at fill time by the {@link JRParameter#IS_IGNORE_PAGINATION IS_IGNORE_PAGINATION}
	 * parameter.
	 * 
	 * @return whether to use pagination when filling the report
	 */
	public boolean isIgnorePagination();
	
	
	/**
	 * Returns this report's properties map.
	 *  
	 * @return this report's properties map
	 */
	public JRPropertiesMap getPropertiesMap();	
}
