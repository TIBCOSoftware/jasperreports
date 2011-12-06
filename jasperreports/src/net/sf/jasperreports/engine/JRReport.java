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
package net.sf.jasperreports.engine;

import net.sf.jasperreports.engine.type.OrientationEnum;
import net.sf.jasperreports.engine.type.PrintOrderEnum;
import net.sf.jasperreports.engine.type.RunDirectionEnum;
import net.sf.jasperreports.engine.type.WhenNoDataTypeEnum;
import net.sf.jasperreports.engine.type.WhenResourceMissingTypeEnum;


/**
 * An abstract representation of a Jasper report. This interface is inherited by all report implementations
 * (designs, compiled reports, filled reports). It only contains constants and getters and setters for the most common
 * report properties and elements.
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public interface JRReport extends JRDefaultStyleProvider, JRPropertiesHolder
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
	 * @return a value representing one of the print order constants in {@link PrintOrderEnum}
	 */
	public PrintOrderEnum getPrintOrderValue();
	
	/**
	 * Specifies whether columns will be filled from left to right or from right to left.
	 * @return a value representing one of the column direction constants in {@link RunDirectionEnum}
	 */
	public RunDirectionEnum getColumnDirection();
	
	/**
	 *
	 */
	public int getPageWidth();

	/**
	 *
	 */
	public int getPageHeight();

	/**
	 * Specifies whether document pages will be rendered in a portrait or landscape layout.
	 * @return a value representing one of the orientation constants in {@link OrientationEnum}
	 */
	public OrientationEnum getOrientationValue();

	/**
	 * Specifies the report behavior in case of empty datasources.
	 */
	public WhenNoDataTypeEnum getWhenNoDataTypeValue();

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
	 * Returns the resource missing handling type.
	 */
	public WhenResourceMissingTypeEnum getWhenResourceMissingTypeValue();

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
