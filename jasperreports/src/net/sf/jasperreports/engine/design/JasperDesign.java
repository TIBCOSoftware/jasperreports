/*
 * ============================================================================
 * GNU Lesser General Public License
 * ============================================================================
 *
 * JasperReports - Free Java report-generating library.
 * Copyright (C) 2001-2005 JasperSoft Corporation http://www.jaspersoft.com
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
 * 185, Berry Street, Suite 6200
 * San Francisco CA 94107
 * http://www.jaspersoft.com
 */
package net.sf.jasperreports.engine.design;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import net.sf.jasperreports.engine.JRAbstractScriptlet;
import net.sf.jasperreports.engine.JRBand;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExpressionCollector;
import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.engine.JRGroup;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JRQuery;
import net.sf.jasperreports.engine.JRReportFont;
import net.sf.jasperreports.engine.JRVariable;
import net.sf.jasperreports.engine.JRVirtualizer;
import net.sf.jasperreports.engine.base.JRBaseReport;


/**
 * JasperDesign is used for in-memory representation of a report design. Instances of this class can be easily
 * created from an XML template and viceversa. It contains all report properties and report elements in their design time
 * state.
 * <p>
 * The main reason for using this class is for modifying report templates at run time. Although using compiled reports
 * is usually recommended, sometimes people need to dinamically change a report design.
 *
 * @see net.sf.jasperreports.engine.xml.JRXmlLoader
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JasperDesign extends JRBaseReport
{
	/** Property change support mechanism. */
	private transient PropertyChangeSupport propSupport;

	/** Bean property name for the report's name. */
	public static final String NAME_PROPERTY = "name";

	/** Bean property name for language. */
	public static final String LANGUAGE_PROPERTY = "language";

	/** Bean property name for query. */
	public static final String QUERY_PROPERTY = "query";

	/** Bean property name for resource bundle. */
	public static final String RESOURCE_BUNDLE_PROPERTY = "resourceBundle";

	/** Bean property name for scriptlet class. */
	public static final String SCRIPTLET_CLASS_PROPERTY = "scriptletClass";

	/** Bean property name for orientation. */
	public static final String ORIENTATION_PROPERTY = "orientation";

	/** Bean property name for background. */
	public static final String BACKGROUND_PROPERTY = "background";

	/** Bean property name for column count. */
	public static final String COLUMN_COUNT_PROPERTY = "columnCount";

	/** Bean property name for column header. */
	public static final String COLUMN_HEADER_PROPERTY = "columnHeader";

	/** Bean property name for column footer. */
	public static final String COLUMN_FOOTER_PROPERTY = "columnFooter";

	/** Bean property name for left margin. */
	public static final String LEFT_MARGIN_PROPERTY = "leftMargin";

	/** Bean property name for right margin. */
	public static final String RIGHT_MARGIN_PROPERTY = "rightMargin";

	/** Bean property name for top margin. */
	public static final String TOP_MARGIN_PROPERTY = "topMargin";

	/** Bean property name for bottom margin. */
	public static final String BOTTOM_MARGIN_PROPERTY = "bottomMargin";

	/** Bean property name for column width. */
	public static final String COLUMN_WIDTH_PROPERTY = "columnWidth";

	/** Bean property name for column spacing. */
	public static final String COLUMN_SPACING_PROPERTY = "columnSpacing";

	/** Bean property name for last page footer. */
	public static final String PRINT_ORDER_PROPERTY = "printOrder";

	/** Bean property name for default font. */
	public static final String DEFAULT_FONT_PROPERTY = "defaultFont";

	/** Bean property name for title. */
	public static final String TITLE_PROPERTY = "title";

	/** Bean property name for title new page. */
	public static final String TITLE_NEW_PAGE_PROPERTY = "titleNewPage";

	/** Bean property name for page width. */
	public static final String PAGE_WIDTH_PROPERTY = "pageWidth";

	/** Bean property name for page height. */
	public static final String PAGE_HEIGHT_PROPERTY = "pageHeight";

	/** Bean property name for page header. */
	public static final String PAGE_HEADER_PROPERTY = "pageHeader";

	/** Bean property name for page footer. */
	public static final String PAGE_FOOTER_PROPERTY = "pageFooter";

	/** Bean property name for last page footer. */
	public static final String LAST_PAGE_FOOTER_PROPERTY = "lastPageFooter";

	/** Bean property name for summary. */
	public static final String SUMMARY_PROPERTY = "summary";

	/** Bean property name for summary new page. */
	public static final String SUMMARY_NEW_PAGE_PROPERTY = "summaryNewPage";

	/** Bean property name for float column footer. */
	public static final String FLOAT_COLUMN_FOOTER_PROPERTY = "floatColumnFooter";

	/** Bean property name for detail band. */
	public static final String DETAIL_PROPERTY = "detail";

	/**
	 *
	 */
	private static final long serialVersionUID = 10001;

	/**
	 * An array containing the built-in parameters that can be found and used in any report.
	 */
	private static final Object[] BUILT_IN_PARAMETERS = new Object[] 
	{
		JRParameter.REPORT_PARAMETERS_MAP, java.util.Map.class,
		JRParameter.REPORT_CONNECTION, Connection.class,
		JRParameter.REPORT_MAX_COUNT, Integer.class,
		JRParameter.REPORT_DATA_SOURCE, JRDataSource.class,
		JRParameter.REPORT_SCRIPTLET, JRAbstractScriptlet.class,
		JRParameter.REPORT_LOCALE, Locale.class,
		JRParameter.REPORT_RESOURCE_BUNDLE, ResourceBundle.class,
		JRParameter.REPORT_VIRTUALIZER, JRVirtualizer.class,
		JRParameter.IS_IGNORE_PAGINATION, Boolean.class
	};

	/**
	 *
	 */
	private Map fontsMap = new HashMap();
	private List fontsList = new ArrayList();
	private Map parametersMap = new HashMap();
	private List parametersList = new ArrayList();
	private Map fieldsMap = new HashMap();
	private List fieldsList = new ArrayList();
	private Map variablesMap = new HashMap();
	private List variablesList = new ArrayList();
	private Map groupsMap = new HashMap();
	private List groupsList = new ArrayList();


	/**
	 * Constructs a JasperDesign object and fills it with the default variables and parameters.
	 */
	public JasperDesign()
	{
		/*   */
		for (int i = 0; i < BUILT_IN_PARAMETERS.length; i++)
		{
			JRDesignParameter parameter = new JRDesignParameter();
			parameter.setName((String)BUILT_IN_PARAMETERS[i++]);
			parameter.setValueClass((Class)BUILT_IN_PARAMETERS[i]);
			parameter.setSystemDefined(true);
			try 
			{
				addParameter(parameter);
			}
			catch (JRException e)
			{
				//never reached
			}
		}


		/*   */
		JRDesignVariable variable = new JRDesignVariable();
		variable.setName(JRVariable.PAGE_NUMBER);
		variable.setValueClass(Integer.class);
		//variable.setResetType(JRVariable.RESET_TYPE_PAGE);
		variable.setResetType(JRVariable.RESET_TYPE_REPORT);
		variable.setCalculation(JRVariable.CALCULATION_SYSTEM);
		variable.setSystemDefined(true);
		JRDesignExpression expression = new JRDesignExpression();
		expression.setValueClass(Integer.class);
		//expression.setText("($V{PAGE_NUMBER} != null)?(new Integer($V{PAGE_NUMBER}.intValue() + 1)):(new Integer(1))");
		expression.setText("new Integer(1)");
		variable.setInitialValueExpression(expression);
		try 
		{
			addVariable(variable);
		}
		catch (JRException e)
		{
			//never reached
		}


		/*   */
		variable = new JRDesignVariable();
		variable.setName(JRVariable.COLUMN_NUMBER);
		variable.setValueClass(Integer.class);
		//variable.setResetType(JRVariable.RESET_TYPE_COLUMN);
		variable.setResetType(JRVariable.RESET_TYPE_REPORT);
		variable.setCalculation(JRVariable.CALCULATION_SYSTEM);
		variable.setSystemDefined(true);
		expression = new JRDesignExpression();
		expression.setValueClass(Integer.class);
		//expression.setText("($V{COLUMN_NUMBER} != null)?(new Integer($V{COLUMN_NUMBER}.intValue() + 1)):(new Integer(1))");
		expression.setText("new Integer(1)");
		variable.setInitialValueExpression(expression);
		try 
		{
			addVariable(variable);
		}
		catch (JRException e)
		{
			//never reached
		}


		/*   */
		variable = new JRDesignVariable();
		variable.setName(JRVariable.REPORT_COUNT);
		variable.setValueClass(Integer.class);
		variable.setResetType(JRVariable.RESET_TYPE_REPORT);
		variable.setCalculation(JRVariable.CALCULATION_COUNT);
		variable.setSystemDefined(true);
		expression = new JRDesignExpression();
		expression.setValueClass(Integer.class);
		expression.setText("new Integer(1)");
		variable.setExpression(expression);
		expression = new JRDesignExpression();
		expression.setValueClass(Integer.class);
		expression.setText("new Integer(0)");
		variable.setInitialValueExpression(expression);
		try 
		{
			addVariable(variable);
		}
		catch (JRException e)
		{
			//never reached
		}


		/*   */
		variable = new JRDesignVariable();
		variable.setName(JRVariable.PAGE_COUNT);
		variable.setValueClass(Integer.class);
		variable.setResetType(JRVariable.RESET_TYPE_PAGE);
		variable.setCalculation(JRVariable.CALCULATION_COUNT);
		variable.setSystemDefined(true);
		expression = new JRDesignExpression();
		expression.setValueClass(Integer.class);
		expression.setText("new Integer(1)");
		variable.setExpression(expression);
		expression = new JRDesignExpression();
		expression.setValueClass(Integer.class);
		expression.setText("new Integer(0)");
		variable.setInitialValueExpression(expression);
		try 
		{
			addVariable(variable);
		}
		catch (JRException e)
		{
			//never reached
		}


		/*   */
		variable = new JRDesignVariable();
		variable.setName(JRVariable.COLUMN_COUNT);
		variable.setValueClass(Integer.class);
		variable.setResetType(JRVariable.RESET_TYPE_COLUMN);
		variable.setCalculation(JRVariable.CALCULATION_COUNT);
		variable.setSystemDefined(true);
		expression = new JRDesignExpression();
		expression.setValueClass(Integer.class);
		expression.setText("new Integer(1)");
		variable.setExpression(expression);
		expression = new JRDesignExpression();
		expression.setValueClass(Integer.class);
		expression.setText("new Integer(0)");
		variable.setInitialValueExpression(expression);
		try 
		{
			addVariable(variable);
		}
		catch (JRException e)
		{
			//never reached
		}
	}


	/**
	 * Sets the report name. It is strongly recommended that the report name matches the .jrxml file name, since report compilers
	 * usually use this name for the compiled .jasper file.
	 */
	public void setName(String name)
	{
		Object oldValue = this.name;
		this.name = name;
		getPropertyChangeSupport().firePropertyChange(NAME_PROPERTY, oldValue, this.name);
	}


	/**
	 * Specifies the language used for report expressions (Java or Groovy). The default is Java.
	 */
	public void setLanguage(String language)
	{
		Object oldValue = this.language;
		this.language = language;
		getPropertyChangeSupport().firePropertyChange(LANGUAGE_PROPERTY, oldValue, this.language);
	}
		

	/**
	 * Specifies the number of report columns.
	 */
	public void setColumnCount(int columnCount)
	{
		int oldValue = this.columnCount;
		this.columnCount = columnCount;
		getPropertyChangeSupport().firePropertyChange(COLUMN_COUNT_PROPERTY, oldValue, this.columnCount);
	}
		

	/**
	 * Sets the print order. In case of multiple column reports, the engine can perform vertical or horizontal fill.
	 * @see net.sf.jasperreports.engine.JRReport PRINT_ORDER_VERTICAL,
	 * @see net.sf.jasperreports.engine.JRReport PRINT_ORDER_HORIZONTAL
	 */
	public void setPrintOrder(byte printOrder)
	{
		Object oldValue = new Byte(this.printOrder);
		this.printOrder = printOrder;
		getPropertyChangeSupport().firePropertyChange(PRINT_ORDER_PROPERTY, oldValue, new Byte(this.printOrder));
	}
		

	/**
	 * Sets page width (including margins etc.). Default is 595.
	 */
	public void setPageWidth(int pageWidth)
	{
		int oldValue = this.pageWidth;
		this.pageWidth = pageWidth;
		getPropertyChangeSupport().firePropertyChange(PAGE_WIDTH_PROPERTY, oldValue, this.pageWidth);
	}
		

	/**
	 * Sets page height (including margins etc.). Default is 842.
	 */
	public void setPageHeight(int pageHeight)
	{
		int oldValue = this.pageHeight;
		this.pageHeight = pageHeight;
		getPropertyChangeSupport().firePropertyChange(PAGE_HEIGHT_PROPERTY, oldValue,
				this.pageHeight);
	}
		

	/**
	 * Sets the report orientation.
	 * @see net.sf.jasperreports.engine.JRReport ORIENTATION_PORTRAIT,
	 * @see net.sf.jasperreports.engine.JRReport ORIENTATION_LANDSCAPE
	 */
	public void setOrientation(byte orientation)
	{
		Object oldValue = new Byte(this.orientation);
		this.orientation = orientation;
		getPropertyChangeSupport().firePropertyChange(ORIENTATION_PROPERTY, oldValue,
				new Byte(this.orientation));
	}
		

	/**
	 * Sets the column width.
	 */
	public void setColumnWidth(int columnWidth)
	{
		int oldValue = this.columnWidth;
		this.columnWidth = columnWidth;
		getPropertyChangeSupport().firePropertyChange(COLUMN_WIDTH_PROPERTY, oldValue,
				this.columnWidth);
	}
		

	/**
	 * Sets the spacing between columns.
	 */
	public void setColumnSpacing(int columnSpacing)
	{
		int oldValue = this.columnSpacing;
		this.columnSpacing = columnSpacing;
		getPropertyChangeSupport().firePropertyChange(COLUMN_SPACING_PROPERTY, oldValue,
				this.columnSpacing);
	}
		

	/**
	 * Sets the left margin. The working space is calculated by subtracting the margins from the page width.
	 */
	public void setLeftMargin(int leftMargin)
	{
		int oldValue = this.leftMargin;
		this.leftMargin = leftMargin;
		getPropertyChangeSupport().firePropertyChange(LEFT_MARGIN_PROPERTY, oldValue,
				this.leftMargin);
	}
		

	/**
	 * Sets the right margin. The working space is calculated by subtracting the margins from the page width.
	 */
	public void setRightMargin(int rightMargin)
	{
		int oldValue = this.rightMargin;
		this.rightMargin = rightMargin;
		getPropertyChangeSupport().firePropertyChange(RIGHT_MARGIN_PROPERTY, oldValue,
				this.rightMargin);
	}
		

	/**
	 * Sets the top margin. The working space is calculated by subtracting the margins from the page height.
	 */
	public void setTopMargin(int topMargin)
	{
		int oldValue = this.topMargin;
		this.topMargin = topMargin;
		getPropertyChangeSupport().firePropertyChange(TOP_MARGIN_PROPERTY, oldValue,
				this.topMargin);
	}
		

	/**
	 * Sets the top margin. The working space is calculated by subtracting the margins from the page height.
	 */
	public void setBottomMargin(int bottomMargin)
	{
		int oldValue = this.bottomMargin;
		this.bottomMargin = bottomMargin;
		getPropertyChangeSupport().firePropertyChange(BOTTOM_MARGIN_PROPERTY, oldValue,
				this.bottomMargin);
	}
		

	/**
	 * Sets the background band.
	 */
	public void setBackground(JRBand background)
	{
		Object oldValue = this.background;
		this.background = background;
		getPropertyChangeSupport().firePropertyChange(BACKGROUND_PROPERTY, oldValue, this.background);
	}
		

	/**
	 * Sets the title band.
	 */
	public void setTitle(JRBand title)
	{
		Object oldValue = this.title;
		this.title = title;
		getPropertyChangeSupport().firePropertyChange(TITLE_PROPERTY, oldValue, this.title);
	}
		

	/**
	 * Flag used to specify if the title section should be printed on a separate initial page.
	 *
	 * @param isTitleNewPage true if the title section should be displayed on a separate initial page, false if
	 * it will be displayed on the first page along with other sections.
	 */
	public void setTitleNewPage(boolean isTitleNewPage)
	{
		boolean oldValue = this.isTitleNewPage;
		this.isTitleNewPage = isTitleNewPage;
		getPropertyChangeSupport().firePropertyChange(TITLE_NEW_PAGE_PROPERTY, oldValue, this.isTitleNewPage);
	}
		

	/**
	 * Sets the summary band.
	 */
	public void setSummary(JRBand summary)
	{
		Object oldValue = this.summary;
		this.summary = summary;
		getPropertyChangeSupport().firePropertyChange(SUMMARY_PROPERTY, oldValue, this.summary);
	}
		

	/**
	 * Flag used to specify if the summary section should be printed on a separate last page.
	 *
	 * @param isSummaryNewPage true if the summary section should be displayed on a separate last page, false if
	 * it will be displayed on the last page along with other sections, if there is enough space.
	 */
	public void setSummaryNewPage(boolean isSummaryNewPage)
	{
		boolean oldValue = this.isSummaryNewPage;
		this.isSummaryNewPage = isSummaryNewPage;
		getPropertyChangeSupport().firePropertyChange(SUMMARY_NEW_PAGE_PROPERTY, oldValue,
				this.isSummaryNewPage);
	}
		

	/**
	 * Flag used to specify if the column footer section should be printed at the bottom of the column or if it
	 * should immediately follow the last detail or group footer printed on the current column.
	 */
	public void setFloatColumnFooter(boolean isFloatColumnFooter)
	{
		boolean oldValue = this.isFloatColumnFooter;
		this.isFloatColumnFooter = isFloatColumnFooter;
		getPropertyChangeSupport().firePropertyChange(FLOAT_COLUMN_FOOTER_PROPERTY, oldValue,
				this.isFloatColumnFooter);
	}
		

	/**
	 * Sets the page header band.
	 */
	public void setPageHeader(JRBand pageHeader)
	{
		Object oldValue = this.pageHeader;
		this.pageHeader = pageHeader;
		getPropertyChangeSupport().firePropertyChange(PAGE_HEADER_PROPERTY, oldValue, this.pageHeader);
	}
		

	/**
	 * Sets the page footer band.
	 */
	public void setPageFooter(JRBand pageFooter)
	{
		Object oldValue = this.pageFooter;
		this.pageFooter = pageFooter;
		getPropertyChangeSupport().firePropertyChange(PAGE_FOOTER_PROPERTY, oldValue, this.pageFooter);
	}
		

	/**
	 * Sets the last page footer band.
	 */
	public void setLastPageFooter(JRBand lastPageFooter)
	{
		Object oldValue = this.lastPageFooter;
		this.lastPageFooter = lastPageFooter;
		getPropertyChangeSupport().firePropertyChange(LAST_PAGE_FOOTER_PROPERTY, oldValue,
				this.lastPageFooter);
	}
		

	/**
	 * Sets the column header band.
	 */
	public void setColumnHeader(JRBand columnHeader)
	{
		Object oldValue = this.columnHeader;
		this.columnHeader = columnHeader;
		getPropertyChangeSupport().firePropertyChange(COLUMN_HEADER_PROPERTY, oldValue,
				this.columnHeader);
	}
		

	/**
	 * Sets the column footer band.
	 */
	public void setColumnFooter(JRBand columnFooter)
	{
		Object oldValue = this.columnFooter;
		this.columnFooter = columnFooter;
		getPropertyChangeSupport().firePropertyChange(COLUMN_FOOTER_PROPERTY, oldValue,
				this.columnFooter);
	}
		

	/**
	 * Sets the detail band.
	 */
	public void setDetail(JRBand detail)
	{
		Object oldValue = this.detail;
		this.detail = detail;
		getPropertyChangeSupport().firePropertyChange(DETAIL_PROPERTY, oldValue, this.detail);
	}
		

	/**
	 *
	 */
	public void setScriptletClass(String scriptletClass)
	{
		Object oldValue = this.scriptletClass;
		this.scriptletClass = scriptletClass;
		if (scriptletClass == null) {
			((JRDesignParameter)parametersMap.get(JRParameter.REPORT_SCRIPTLET)).setValueClass(JRAbstractScriptlet.class);
		}
		else {
			((JRDesignParameter)parametersMap.get(JRParameter.REPORT_SCRIPTLET)).setValueClassName(scriptletClass);
		}
		getPropertyChangeSupport().firePropertyChange(SCRIPTLET_CLASS_PROPERTY, oldValue, this.scriptletClass);
	}
		

	/**
	 * Sets the base name of the report associated resource bundle.
	 */
	public void setResourceBundle(String resourceBundle)
	{
		Object oldValue = this.resourceBundle;
		this.resourceBundle = resourceBundle;
		getPropertyChangeSupport().firePropertyChange(RESOURCE_BUNDLE_PROPERTY, oldValue, this.resourceBundle);
	}
		

	/**
	 * Adds an import (needed if report expression require additional classes in order to compile).
	 */
	public void addImport(String value)
	{
		if (importsSet == null)
		{
			importsSet = new HashSet();
		}
		importsSet.add(value);
	}


	/**
	 * Removes an import.
	 */
	public void removeImport(String value)
	{
		if (importsSet != null)
		{
			importsSet.remove(value);
		}
	}


	/**
	 *
	 */
	public void setDefaultFont(JRReportFont font)
	{
		Object oldValue = this.defaultFont;
		this.defaultFont = font;
		getPropertyChangeSupport().firePropertyChange(DEFAULT_FONT_PROPERTY, oldValue, this.defaultFont);
	}
		

	/**
	 * Gets an array of report level fonts. These fonts can be referenced by text elements.
	 */
	public JRReportFont[] getFonts()
	{
		JRReportFont[] fontsArray = new JRReportFont[fontsList.size()];
		
		fontsList.toArray(fontsArray);

		return fontsArray;
	}
	

	/**
	 * Gets a list of report level fonts. These fonts can be referenced by text elements.
	 */
	public List getFontsList()
	{
		return fontsList;
	}
	

	/**
	 *
	 */
	public Map getFontsMap()
	{
		return fontsMap;
	}
	

	/**
	 * Adds a report font, that can be referenced by text elements.
	 */
	public void addFont(JRReportFont reportFont) throws JRException
	{
		if (fontsMap.containsKey(reportFont.getName()))
		{
			throw new JRException("Duplicate declaration of report font : " + reportFont.getName());
		}

		fontsList.add(reportFont);
		fontsMap.put(reportFont.getName(), reportFont);
		
		if (reportFont.isDefault())
		{
			setDefaultFont(reportFont);
		}
	}
	

	/**
	 * Removes a report font from the list, based on the font name.
	 */
	public JRReportFont removeFont(String propName)
	{
		return removeFont(
			(JRReportFont)fontsMap.get(propName)
			);
	}


	/**
	 * Removes a report font from the list.
	 */
	public JRReportFont removeFont(JRReportFont reportFont)
	{
		if (reportFont != null)
		{
			if (reportFont.isDefault())
			{
				setDefaultFont(null);
			}

			fontsList.remove(reportFont);
			fontsMap.remove(reportFont.getName());
		}

		return reportFont;
	}


	/**
	 * Gets an array of report parameters (including built-in ones).
	 */
	public JRParameter[] getParameters()
	{
		JRParameter[] parametersArray = new JRParameter[parametersList.size()];

		parametersList.toArray(parametersArray);

		return parametersArray;
	}
	

	/**
	 * Gets a list of report parameters (including built-in ones).
	 */
	public List getParametersList()
	{
		return parametersList;
	}
	

	/**
	 * Gets a map of report parameters (including built-in ones).
	 */
	public Map getParametersMap()
	{
		return parametersMap;
	}
	

	/**
	 * Adds a report parameter.
	 */
	public void addParameter(JRParameter parameter) throws JRException
	{
		if (parametersMap.containsKey(parameter.getName()))
		{
			throw new JRException("Duplicate declaration of parameter : " + parameter.getName());
		}

		parametersList.add(parameter);
		parametersMap.put(parameter.getName(), parameter);
	}
	

	/**
	 * Removes a report parameter, based on its name.
	 */
	public JRParameter removeParameter(String parameterName)
	{
		return removeParameter(
			(JRParameter)parametersMap.get(parameterName)
			);
	}


	/**
	 * Removes a report parameter.
	 */
	public JRParameter removeParameter(JRParameter parameter)
	{
		if (parameter != null)
		{
			parametersList.remove(parameter);
			parametersMap.remove(parameter.getName());
		}
		
		return parameter;
	}


	/**
	 * Specifies the report query. This is used only when datasource type is JDBC (a <tt>java.sql.Connection</tt>).
	 */
	public void setQuery(JRQuery query)
	{
		Object oldValue = this.query;
		this.query = query;
		getPropertyChangeSupport().firePropertyChange(QUERY_PROPERTY, oldValue, this.query);
	}

	/**
	 * Add a property listener to listen to all properties of this class.
	 * @param l The property listener to add.
	 */
	public void addPropertyChangeListener(PropertyChangeListener l) {
		getPropertyChangeSupport().addPropertyChangeListener(l);
	}

	/**
	 * Add a property listener to receive property change events for only one specific
	 * property.
	 * @param propName The property to listen to.
	 * @param l The property listener to add.
	 */
	public void addPropertyChangeListener(String propName, PropertyChangeListener l) {
		getPropertyChangeSupport().addPropertyChangeListener(propName, l);
	}

	/**
	 * Remove a property change listener.  This will remove any listener that was added
	 * through either of the addPropertyListener methods.
	 * @param l The listener to remove.
	 */
	public void removePropertyChangeListener(PropertyChangeListener l) {
		getPropertyChangeSupport().removePropertyChangeListener(l);
	}

	/**
	 * Get the property change support object for this class.  Because the
	 * property change support object has to be transient, it may need to be
	 * created.
	 * @return The property change support object.
	 */
	protected PropertyChangeSupport getPropertyChangeSupport() {
		if (propSupport == null) {
			propSupport = new PropertyChangeSupport(this);
		}
		return propSupport;
	}

	/**
	 *  Gets an array of report fields.
	 */
	public JRField[] getFields()
	{
		JRField[] fieldsArray = new JRField[fieldsList.size()];
		
		fieldsList.toArray(fieldsArray);

		return fieldsArray;
	}
	

	/**
	 * Gets an array of report fields.
	 */
	public List getFieldsList()
	{
		return fieldsList;
	}
	

	/**
	 * Gets a map of report fields.
	 */
	public Map getFieldsMap()
	{
		return fieldsMap;
	}
	

	/**
	 *
	 */
	public void addField(JRField field) throws JRException
	{
		if (fieldsMap.containsKey(field.getName()))
		{
			throw new JRException("Duplicate declaration of field : " + field.getName());
		}

		fieldsList.add(field);
		fieldsMap.put(field.getName(), field);
	}
	

	/**
	 *
	 */
	public JRField removeField(String fieldName)
	{
		return removeField(
			(JRField)fieldsMap.get(fieldName)
			);
	}


	/**
	 *
	 */
	public JRField removeField(JRField field)
	{
		if (field != null)
		{
			fieldsList.remove(field);
			fieldsMap.remove(field.getName());
		}
		
		return field;
	}


	/**
	 * Gets an array of report variables.
	 */
	public JRVariable[] getVariables()
	{
		JRVariable[] variablesArray = new JRVariable[variablesList.size()];
		
		variablesList.toArray(variablesArray);

		return variablesArray;
	}
	

	/**
	 * Gets a list of report variables.
	 */
	public List getVariablesList()
	{
		return variablesList;
	}
	

	/**
	 * Gets a map of report variables.
	 */
	public Map getVariablesMap()
	{
		return variablesMap;
	}
	

	/**
	 *
	 */
	public void addVariable(JRDesignVariable variable) throws JRException
	{
		if (variablesMap.containsKey(variable.getName()))
		{
			throw new JRException("Duplicate declaration of variable : " + variable.getName());
		}

		variablesList.add(variable);
		variablesMap.put(variable.getName(), variable);
	}
	

	/**
	 *
	 */
	public JRVariable removeVariable(String variableName)
	{
		return removeVariable(
			(JRVariable)variablesMap.get(variableName)
			);
	}


	/**
	 *
	 */
	public JRVariable removeVariable(JRVariable variable)
	{
		if (variable != null)
		{
			variablesList.remove(variable);
			variablesMap.remove(variable.getName());
		}
		
		return variable;
	}


	/**
	 * Gets an array of report groups.
	 */
	public JRGroup[] getGroups()
	{
		JRGroup[] groupsArray = new JRGroup[groupsList.size()];
		
		groupsList.toArray(groupsArray);

		return groupsArray;
	}
	

	/**
	 * Gets a list of report groups.
	 */
	public List getGroupsList()
	{
		return groupsList;
	}
	

	/**
	 * Gets a map of report groups.
	 */
	public Map getGroupsMap()
	{
		return groupsMap;
	}
	

	/**
	 * Adds a new group to the report design. Groups are nested.
	 */
	public void addGroup(JRDesignGroup group) throws JRException
	{
		if (groupsMap.containsKey(group.getName()))
		{
			throw new JRException("Duplicate declaration of group : " + group.getName());
		}
		
		JRDesignVariable countVariable = new JRDesignVariable();
		countVariable.setName(group.getName() + "_COUNT");
		countVariable.setValueClass(Integer.class);
		countVariable.setResetType(JRVariable.RESET_TYPE_GROUP);
		countVariable.setResetGroup(group);
		countVariable.setCalculation(JRVariable.CALCULATION_COUNT);
		countVariable.setSystemDefined(true);
		JRDesignExpression expression = new JRDesignExpression();
		expression.setValueClass(Integer.class);
		expression.setText("new Integer(1)");
		countVariable.setExpression(expression);
		expression = new JRDesignExpression();
		expression.setValueClass(Integer.class);
		expression.setText("new Integer(0)");
		countVariable.setInitialValueExpression(expression);

		addVariable(countVariable);

		group.setCountVariable(countVariable);

		groupsList.add(group);
		groupsMap.put(group.getName(), group);
	}
	

	/**
	 *
	 */
	public JRGroup removeGroup(String groupName)
	{
		return removeGroup(
			(JRGroup)groupsMap.get(groupName)
			);
	}


	/**
	 *
	 */
	public JRGroup removeGroup(JRGroup group)
	{
		if (group != null)
		{
			removeVariable(group.getCountVariable());
			groupsList.remove(group);
			groupsMap.remove(group.getName());
		}
		
		return group;
	}


	/**
	 * Returns a collection of all report expressions.
	 */
	public Collection getExpressions()
	{
		return new JRExpressionCollector().collect(this);
	}

}
