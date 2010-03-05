/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2009 Jaspersoft Corporation. All rights reserved.
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
package net.sf.jasperreports.engine;

import net.sf.jasperreports.engine.type.OrientationEnum;
import net.sf.jasperreports.engine.type.PrintOrderEnum;
import net.sf.jasperreports.engine.type.WhenNoDataTypeEnum;
import net.sf.jasperreports.engine.type.WhenResourceMissingTypeEnum;


/**
 * An abstract representation of a Jasper report. This interface is inherited by all report implementations
 * (designs, compiled reports, filled reports). It only contains constants and getters and setters for the most common
 * report properties and elements.
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public interface JRReport extends JRDefaultFontProvider, JRDefaultStyleProvider, JRPropertiesHolder
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
	 * @deprecated Replaced by {@link PrintOrderEnum#VERTICAL}.
	 */
	public static final byte PRINT_ORDER_VERTICAL = 1;


	/**
	 * @deprecated Replaced by {@link PrintOrderEnum#HORIZONTAL}.
	 */
	public static final byte PRINT_ORDER_HORIZONTAL = 2;


	/**
	 * @deprecated Replaced by {@link OrientationEnum#PORTRAIT}.
	 */
	public static final byte ORIENTATION_PORTRAIT = 1;


	/**
	 * @deprecated Replaced by {@link OrientationEnum#LANDSCAPE}.
	 */
	public static final byte ORIENTATION_LANDSCAPE = 2;

	
	/**
	 * Specifies that in case of empty datasources, there will be an empty report.
	 * @deprecated Replaced by {@link WhenNoDataTypeEnum#NO_PAGES}.
	 */
	public static final byte WHEN_NO_DATA_TYPE_NO_PAGES = 1;


	/**
	 * Specifies that in case of empty datasources, there will be a report with just one blank page.
	 * @deprecated Replaced by {@link WhenNoDataTypeEnum#BLANK_PAGE}.
	 */
	public static final byte WHEN_NO_DATA_TYPE_BLANK_PAGE = 2;


	/**
	 * Specifies that in case of empty datasources, all sections except detail will displayed.
	 * @deprecated Replaced by {@link WhenNoDataTypeEnum#ALL_SECTIONS_NO_DETAIL}.
	 */
	public static final byte WHEN_NO_DATA_TYPE_ALL_SECTIONS_NO_DETAIL = 3;

	/**
	 * Specifies that in case of empty datasources, the NoData section will be displayed.
	 * @deprecated Replaced by {@link WhenNoDataTypeEnum#NO_DATA_SECTION}.
	 */
	public static final byte WHEN_NO_DATA_TYPE_NO_DATA_SECTION = 4;


	/**
	 * Return NULL when a resource is missing.
	 * @deprecated Replaced by {@link WhenResourceMissingTypeEnum#NULL}.
	 */
	public static final byte WHEN_RESOURCE_MISSING_TYPE_NULL = JRDataset.WHEN_RESOURCE_MISSING_TYPE_NULL;


	/**
	 * Return empty string when a resource is missing.
	 * @deprecated Replaced by {@link WhenResourceMissingTypeEnum#EMPTY}.
	 */
	public static final byte WHEN_RESOURCE_MISSING_TYPE_EMPTY = JRDataset.WHEN_RESOURCE_MISSING_TYPE_EMPTY;


	/**
	 * Return the key when a resource is missing.
	 * @deprecated Replaced by {@link WhenResourceMissingTypeEnum#KEY}.
	 */
	public static final byte WHEN_RESOURCE_MISSING_TYPE_KEY = JRDataset.WHEN_RESOURCE_MISSING_TYPE_KEY;


	/**
	 * Throw an exception when a resource is missing.
	 * @deprecated Replaced by {@link WhenResourceMissingTypeEnum#ERROR}.
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
	 * @deprecated Replaced by {@link getPrintOrderValue()}.
	 */
	public byte getPrintOrder();
	
	/**
	 * Specifies whether columns will be filled horizontally or vertically.
	 * @return a value representing one of the print order constants in {@link PrintOrderEnum}
	 */
	public PrintOrderEnum getPrintOrderValue();
	
	/**
	 *
	 */
	public int getPageWidth();

	/**
	 *
	 */
	public int getPageHeight();

	/**
	 * @deprecated Replaced by {@link getOrientationValue()}.
	 */
	public byte getOrientation();

	/**
	 * Specifies whether document pages will be rendered in a portrait or landscape layout.
	 * @return a value representing one of the orientation constants in {@link OrientationEnum}
	 */
	public OrientationEnum getOrientationValue();

	/**
	 * @deprecated Replaced by {@link #getWhenNoDataTypeValue()}.
	 */
	public byte getWhenNoDataType();

	/**
	 * Specifies the report behavior in case of empty datasources.
	 */
	public WhenNoDataTypeEnum getWhenNoDataTypeValue();

	/**
	 * @deprecated Replaced by {@link #setWhenNoDataType(WhenNoDataTypeEnum)}.
	 */
	public void setWhenNoDataType(byte whenNoDataType);

	/**
	 * Sets the report behavior in case of empty datasources.
	 */
	public void setWhenNoDataType(WhenNoDataTypeEnum whenNoDataType);

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
	 * Specifies if the summary section will be accompanied by the page header and footer.
	 */
	public boolean isSummaryWithPageHeaderAndFooter();

	/**
	 * Specifies if the column footer section will be printed at the bottom of the column or if it
	 * will immediately follow the last detail or group footer printed on the current column.

	 */
	public boolean isFloatColumnFooter();

	/**
	 *
	 */
	public String getScriptletClass();

	/**
	 * Gets the name of the class implementing the {@link net.sf.jasperreports.engine.util.FormatFactory FormatFactory}
	 * interface to use with this report.
	 */
	public String getFormatFactoryClass();

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
	public JRScriptlet[] getScriptlets();

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
	 * @deprecated Replaced by {@link #getDetailSection()}.
	 */
	public JRBand getDetail();

	/**
	 *
	 */
	public JRSection getDetailSection();

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
	 *
	 */
	public JRBand getNoData();

	/**
	 * @deprecated Replaced by {@link #getWhenResourceMissingTypeValue()}.
	 */
	public byte getWhenResourceMissingType();

	/**
	 * Returns the resource missing handling type.
	 */
	public WhenResourceMissingTypeEnum getWhenResourceMissingTypeValue();

	/**
	 * @deprecated Replaced by {@link #setWhenResourceMissingType(WhenResourceMissingTypeEnum)}
	 */
	public void setWhenResourceMissingType(byte whenResourceMissingType);

	/**
	 * Sets the resource missing handling type.
	 * @param whenResourceMissingType the resource missing handling type
	 */
	public void setWhenResourceMissingType(WhenResourceMissingTypeEnum whenResourceMissingType);


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
	 * Returns the list of report templates.
	 * <p/>
	 * A report template is an expression which resolves at runtime to a {@link JRTemplate template}.
	 * Templates include styles which can be used in the report.
	 * <p/>
	 * The order in which the templates are included in the report is important:
	 * <ul>
	 * 	<li>A style's parent must appear before the style itself.</li>
	 * 	<li>A style overrides styles with the same name that are placed before it.
	 * 		Also, report styles override templates styles with the same name.</li>
	 * </ul>
	 *
	 * @return the list of report templates, or <code>null</code> if none
	 * @see JRTemplate
	 * @see JRParameter#REPORT_TEMPLATES
	 */
	public JRReportTemplate[] getTemplates();

}
