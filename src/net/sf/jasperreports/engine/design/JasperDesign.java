/*
 * ============================================================================
 *                   GNU Lesser General Public License
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

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyChangeListener;

import net.sf.jasperreports.engine.JRAbstractScriptlet;
import net.sf.jasperreports.engine.JRAnchor;
import net.sf.jasperreports.engine.JRBand;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRElement;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.engine.JRGroup;
import net.sf.jasperreports.engine.JRHyperlink;
import net.sf.jasperreports.engine.JRImage;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JRQuery;
import net.sf.jasperreports.engine.JRReportFont;
import net.sf.jasperreports.engine.JRSubreport;
import net.sf.jasperreports.engine.JRSubreportParameter;
import net.sf.jasperreports.engine.JRTextField;
import net.sf.jasperreports.engine.JRVariable;
import net.sf.jasperreports.engine.base.JRBaseReport;


/**
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
	private static final long serialVersionUID = 608;

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
	 *
	 */
	public JasperDesign()
	{
		/*   */
		JRDesignParameter parameter = new JRDesignParameter();
		parameter.setName(JRParameter.REPORT_PARAMETERS_MAP);
		parameter.setValueClass(java.util.Map.class);
		parameter.setSystemDefined(true);
		try 
		{
			addParameter(parameter);
		}
		catch (JRException e)
		{
			e.printStackTrace();
		}


		/*   */
		parameter = new JRDesignParameter();
		parameter.setName(JRParameter.REPORT_CONNECTION);
		parameter.setValueClass(Connection.class);
		parameter.setSystemDefined(true);
		try 
		{
			addParameter(parameter);
		}
		catch (JRException e)
		{
			e.printStackTrace();
		}


		/*   */
		parameter = new JRDesignParameter();
		parameter.setName(JRParameter.REPORT_MAX_COUNT);
		parameter.setValueClass(Integer.class);
		parameter.setSystemDefined(true);
		try 
		{
			addParameter(parameter);
		}
		catch (JRException e)
		{
			e.printStackTrace();
		}


		/*   */
		parameter = new JRDesignParameter();
		parameter.setName(JRParameter.REPORT_DATA_SOURCE);
		parameter.setValueClass(JRDataSource.class);
		parameter.setSystemDefined(true);
		try 
		{
			addParameter(parameter);
		}
		catch (JRException e)
		{
			e.printStackTrace();
		}


		/*   */
		parameter = new JRDesignParameter();
		parameter.setName(JRParameter.REPORT_SCRIPTLET);
		parameter.setValueClass(JRAbstractScriptlet.class);
		parameter.setSystemDefined(true);
		try 
		{
			addParameter(parameter);
		}
		catch (JRException e)
		{
			e.printStackTrace();
		}


		/*   */
		parameter = new JRDesignParameter();
		parameter.setName(JRParameter.REPORT_LOCALE);
		parameter.setValueClass(Locale.class);
		parameter.setSystemDefined(true);
		try 
		{
			addParameter(parameter);
		}
		catch (JRException e)
		{
			e.printStackTrace();
		}


		/*   */
		parameter = new JRDesignParameter();
		parameter.setName(JRParameter.REPORT_RESOURCE_BUNDLE);
		parameter.setValueClass(ResourceBundle.class);
		parameter.setSystemDefined(true);
		try 
		{
			addParameter(parameter);
		}
		catch (JRException e)
		{
			e.printStackTrace();
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
		expression.setName(JRExpression.PREFIX_variableInitialValue + JRVariable.PAGE_NUMBER);
		//expression.setText("($V{PAGE_NUMBER} != null)?(new Integer($V{PAGE_NUMBER}.intValue() + 1)):(new Integer(1))");
		expression.setText("new Integer(1)");
		variable.setInitialValueExpression((JRExpression)expression);
		try 
		{
			addVariable(variable);
		}
		catch (JRException e)
		{
			e.printStackTrace();
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
		expression.setName(JRExpression.PREFIX_variableInitialValue + JRVariable.COLUMN_NUMBER);
		//expression.setText("($V{COLUMN_NUMBER} != null)?(new Integer($V{COLUMN_NUMBER}.intValue() + 1)):(new Integer(1))");
		expression.setText("new Integer(1)");
		variable.setInitialValueExpression((JRExpression)expression);
		try 
		{
			addVariable(variable);
		}
		catch (JRException e)
		{
			e.printStackTrace();
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
		expression.setName(JRExpression.PREFIX_variable + JRVariable.REPORT_COUNT);
		expression.setText("new Integer(1)");
		variable.setExpression(expression);
		expression = new JRDesignExpression();
		expression.setValueClass(Integer.class);
		expression.setName(JRExpression.PREFIX_variableInitialValue + JRVariable.REPORT_COUNT);
		expression.setText("new Integer(0)");
		variable.setInitialValueExpression((JRExpression)expression);
		try 
		{
			addVariable(variable);
		}
		catch (JRException e)
		{
			e.printStackTrace();
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
		expression.setName(JRExpression.PREFIX_variable + JRVariable.PAGE_COUNT);
		expression.setText("new Integer(1)");
		variable.setExpression((JRExpression)expression);
		expression = new JRDesignExpression();
		expression.setValueClass(Integer.class);
		expression.setName(JRExpression.PREFIX_variableInitialValue + JRVariable.PAGE_COUNT);
		expression.setText("new Integer(0)");
		variable.setInitialValueExpression((JRExpression)expression);
		try 
		{
			addVariable(variable);
		}
		catch (JRException e)
		{
			e.printStackTrace();
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
		expression.setName(JRExpression.PREFIX_variable + JRVariable.COLUMN_COUNT);
		expression.setText("new Integer(1)");
		variable.setExpression((JRExpression)expression);
		expression = new JRDesignExpression();
		expression.setValueClass(Integer.class);
		expression.setName(JRExpression.PREFIX_variableInitialValue + JRVariable.COLUMN_COUNT);
		expression.setText("new Integer(0)");
		variable.setInitialValueExpression((JRExpression)expression);
		try 
		{
			addVariable(variable);
		}
		catch (JRException e)
		{
			e.printStackTrace();
		}
	}


	/**
	 *
	 */
	public void setName(String name)
	{
        Object oldValue = this.name;
		this.name = name;
        getPropertyChangeSupport().firePropertyChange(NAME_PROPERTY, oldValue, this.name);
	}


	/**
	 *
	 */
	public void setLanguage(String language)
	{
        Object oldValue = this.language;
		this.language = language;
        getPropertyChangeSupport().firePropertyChange(LANGUAGE_PROPERTY, oldValue, this.language);
	}
		

	/**
	 *
	 */
	public void setColumnCount(int columnCount)
	{
        int oldValue = this.columnCount;
		this.columnCount = columnCount;
        getPropertyChangeSupport().firePropertyChange(COLUMN_COUNT_PROPERTY, oldValue,
                this.columnCount);
	}
		

	/**
	 *
	 */
	public void setPrintOrder(byte printOrder)
	{
        Object oldValue = new Byte(this.printOrder);
		this.printOrder = printOrder;
        getPropertyChangeSupport().firePropertyChange(PRINT_ORDER_PROPERTY, oldValue,
                new Byte(this.printOrder));
	}
		

	/**
	 *
	 */
	public void setPageWidth(int pageWidth)
	{
        int oldValue = this.pageWidth;
		this.pageWidth = pageWidth;
        getPropertyChangeSupport().firePropertyChange(PAGE_WIDTH_PROPERTY, oldValue,
                this.pageWidth);
	}
		

	/**
	 *
	 */
	public void setPageHeight(int pageHeight)
	{
        int oldValue = this.pageHeight;
		this.pageHeight = pageHeight;
        getPropertyChangeSupport().firePropertyChange(PAGE_HEIGHT_PROPERTY, oldValue,
                this.pageHeight);
	}
		

	/**
	 *
	 */
	public void setOrientation(byte orientation)
	{
        Object oldValue = new Byte(this.orientation);
		this.orientation = orientation;
        getPropertyChangeSupport().firePropertyChange(ORIENTATION_PROPERTY, oldValue,
                new Byte(this.orientation));
	}
		

	/**
	 *
	 */
	public void setColumnWidth(int columnWidth)
	{
        int oldValue = this.columnWidth;
		this.columnWidth = columnWidth;
        getPropertyChangeSupport().firePropertyChange(COLUMN_WIDTH_PROPERTY, oldValue,
                this.columnWidth);
	}
		

	/**
	 *
	 */
	public void setColumnSpacing(int columnSpacing)
	{
        int oldValue = this.columnSpacing;
		this.columnSpacing = columnSpacing;
        getPropertyChangeSupport().firePropertyChange(COLUMN_SPACING_PROPERTY, oldValue,
                this.columnSpacing);
	}
		

	/**
	 *
	 */
	public void setLeftMargin(int leftMargin)
	{
        int oldValue = this.leftMargin;
		this.leftMargin = leftMargin;
        getPropertyChangeSupport().firePropertyChange(LEFT_MARGIN_PROPERTY, oldValue,
                this.leftMargin);
	}
		

	/**
	 *
	 */
	public void setRightMargin(int rightMargin)
	{
        int oldValue = this.rightMargin;
		this.rightMargin = rightMargin;
        getPropertyChangeSupport().firePropertyChange(RIGHT_MARGIN_PROPERTY, oldValue,
                this.rightMargin);
	}
		

	/**
	 *
	 */
	public void setTopMargin(int topMargin)
	{
        int oldValue = this.topMargin;
		this.topMargin = topMargin;
        getPropertyChangeSupport().firePropertyChange(TOP_MARGIN_PROPERTY, oldValue,
                this.topMargin);
	}
		

	/**
	 *
	 */
	public void setBottomMargin(int bottomMargin)
	{
        int oldValue = this.bottomMargin;
		this.bottomMargin = bottomMargin;
        getPropertyChangeSupport().firePropertyChange(BOTTOM_MARGIN_PROPERTY, oldValue,
                this.bottomMargin);
	}
		

	/**
	 *
	 */
	public void setBackground(JRBand background)
	{
        Object oldValue = this.background;
		this.background = background;
        getPropertyChangeSupport().firePropertyChange(BACKGROUND_PROPERTY, oldValue, this.background);
	}
		

	/**
	 *
	 */
	public void setTitle(JRBand title)
	{
        Object oldValue = this.title;
		this.title = title;
        getPropertyChangeSupport().firePropertyChange(TITLE_PROPERTY, oldValue, this.title);
	}
		

	/**
	 *
	 */
	public void setTitleNewPage(boolean isTitleNewPage)
	{
        boolean oldValue = this.isTitleNewPage;
		this.isTitleNewPage = isTitleNewPage;
        getPropertyChangeSupport().firePropertyChange(TITLE_NEW_PAGE_PROPERTY, oldValue, this.isTitleNewPage);
	}
		

	/**
	 *
	 */
	public void setSummary(JRBand summary)
	{
        Object oldValue = this.summary;
		this.summary = summary;
        getPropertyChangeSupport().firePropertyChange(SUMMARY_PROPERTY, oldValue, this.summary);
	}
		

	/**
	 *
	 */
	public void setSummaryNewPage(boolean isSummaryNewPage)
	{
        boolean oldValue = this.isSummaryNewPage;
		this.isSummaryNewPage = isSummaryNewPage;
        getPropertyChangeSupport().firePropertyChange(SUMMARY_NEW_PAGE_PROPERTY, oldValue,
                this.isSummaryNewPage);
	}
		

	/**
	 *
	 */
	public void setFloatColumnFooter(boolean isFloatColumnFooter)
	{
        boolean oldValue = this.isFloatColumnFooter;
		this.isFloatColumnFooter = isFloatColumnFooter;
        getPropertyChangeSupport().firePropertyChange(FLOAT_COLUMN_FOOTER_PROPERTY, oldValue,
                this.isFloatColumnFooter);
	}
		

	/**
	 *
	 */
	public void setPageHeader(JRBand pageHeader)
	{
        Object oldValue = this.pageHeader;
		this.pageHeader = pageHeader;
        getPropertyChangeSupport().firePropertyChange(PAGE_HEADER_PROPERTY, oldValue, this.pageHeader);
	}
		

	/**
	 *
	 */
	public void setPageFooter(JRBand pageFooter)
	{
        Object oldValue = this.pageFooter;
		this.pageFooter = pageFooter;
        getPropertyChangeSupport().firePropertyChange(PAGE_FOOTER_PROPERTY, oldValue, this.pageFooter);
	}
		

	/**
	 *
	 */
	public void setLastPageFooter(JRBand lastPageFooter)
	{
        Object oldValue = this.lastPageFooter;
		this.lastPageFooter = lastPageFooter;
        getPropertyChangeSupport().firePropertyChange(LAST_PAGE_FOOTER_PROPERTY, oldValue,
                this.lastPageFooter);
	}
		

	/**
	 *
	 */
	public void setColumnHeader(JRBand columnHeader)
	{
        Object oldValue = this.columnHeader;
		this.columnHeader = columnHeader;
        getPropertyChangeSupport().firePropertyChange(COLUMN_HEADER_PROPERTY, oldValue,
                this.columnHeader);
	}
		

	/**
	 *
	 */
	public void setColumnFooter(JRBand columnFooter)
	{
        Object oldValue = this.columnFooter;
		this.columnFooter = columnFooter;
        getPropertyChangeSupport().firePropertyChange(COLUMN_FOOTER_PROPERTY, oldValue,
                this.columnFooter);
	}
		

	/**
	 *
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
        ((JRDesignParameter)parametersMap.get(JRParameter.REPORT_SCRIPTLET)).setValueClassName(scriptletClass);
        getPropertyChangeSupport().firePropertyChange(SCRIPTLET_CLASS_PROPERTY, oldValue, this.scriptletClass);
	}
		

	/**
	 *
	 */
	public void setResourceBundle(String resourceBundle)
	{
        Object oldValue = this.resourceBundle;
		this.resourceBundle = resourceBundle;
        getPropertyChangeSupport().firePropertyChange(RESOURCE_BUNDLE_PROPERTY, oldValue, this.resourceBundle);
	}
		

	/**
	 *
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
	 *
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
	 *
	 */
	public JRReportFont[] getFonts()
	{
		JRReportFont[] fontsArray = new JRReportFont[fontsList.size()];
		
		fontsList.toArray(fontsArray);

		return fontsArray;
	}
	

	/**
	 *
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
	 *
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
	 *
	 */
	public JRReportFont removeFont(String name)
	{
		return removeFont(
			(JRReportFont)fontsMap.get(name)
			);
	}


	/**
	 *
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
	 *
	 */
	public JRParameter[] getParameters()
	{
		JRParameter[] parametersArray = new JRParameter[parametersList.size()];

		parametersList.toArray(parametersArray);

		return parametersArray;
	}
	

	/**
	 *
	 */
	public List getParametersList()
	{
		return parametersList;
	}
	

	/**
	 *
	 */
	public Map getParametersMap()
	{
		return parametersMap;
	}
	

	/**
	 *
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
	 *
	 */
	public JRParameter removeParameter(String parameterName)
	{
		return removeParameter(
			(JRParameter)parametersMap.get(parameterName)
			);
	}


	/**
	 *
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
	 *
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
	 *
	 */
	public JRField[] getFields()
	{
		JRField[] fieldsArray = new JRField[fieldsList.size()];
		
		fieldsList.toArray(fieldsArray);

		return fieldsArray;
	}
	

	/**
	 *
	 */
	public List getFieldsList()
	{
		return fieldsList;
	}
	

	/**
	 *
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
	 *
	 */
	public JRVariable[] getVariables()
	{
		JRVariable[] variablesArray = new JRVariable[variablesList.size()];
		
		variablesList.toArray(variablesArray);

		return variablesArray;
	}
	

	/**
	 *
	 */
	public List getVariablesList()
	{
		return variablesList;
	}
	

	/**
	 *
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
		
		byte calculation = variable.getCalculation();

		if (
			calculation == JRVariable.CALCULATION_AVERAGE ||
			calculation == JRVariable.CALCULATION_VARIANCE
			)
		{
			JRDesignVariable countVariable = new JRDesignVariable();
			countVariable.setName(variable.getName() + "_COUNT");
			countVariable.setValueClassName(variable.getValueClassName());
			countVariable.setIncrementerFactoryClassName(variable.getIncrementerFactoryClassName());
			countVariable.setResetType(variable.getResetType());
			countVariable.setResetGroup(variable.getResetGroup());
			countVariable.setIncrementType(variable.getIncrementType());
			countVariable.setIncrementGroup(variable.getIncrementGroup());
			countVariable.setCalculation(JRVariable.CALCULATION_COUNT);
			countVariable.setSystemDefined(true);
			countVariable.setExpression(variable.getExpression());
			addVariable(countVariable);
			variable.setCountVariable(countVariable);

			JRDesignVariable sumVariable = new JRDesignVariable();
			sumVariable.setName(variable.getName() + "_SUM");
			sumVariable.setValueClassName(variable.getValueClassName());
			sumVariable.setIncrementerFactoryClassName(variable.getIncrementerFactoryClassName());
			sumVariable.setResetType(variable.getResetType());
			sumVariable.setResetGroup(variable.getResetGroup());
			sumVariable.setIncrementType(variable.getIncrementType());
			sumVariable.setIncrementGroup(variable.getIncrementGroup());
			sumVariable.setCalculation(JRVariable.CALCULATION_SUM);
			sumVariable.setSystemDefined(true);
			sumVariable.setExpression(variable.getExpression());
			addVariable(sumVariable);
			variable.setSumVariable(sumVariable);
		}

		if (calculation == JRVariable.CALCULATION_STANDARD_DEVIATION)
		{
			JRDesignVariable varianceVariable = new JRDesignVariable();
			varianceVariable.setName(variable.getName() + "_VARIANCE");
			varianceVariable.setValueClassName(variable.getValueClassName());
			varianceVariable.setIncrementerFactoryClassName(variable.getIncrementerFactoryClassName());
			varianceVariable.setResetType(variable.getResetType());
			varianceVariable.setResetGroup(variable.getResetGroup());
			varianceVariable.setIncrementType(variable.getIncrementType());
			varianceVariable.setIncrementGroup(variable.getIncrementGroup());
			varianceVariable.setCalculation(JRVariable.CALCULATION_VARIANCE);
			varianceVariable.setSystemDefined(true);
			varianceVariable.setExpression(variable.getExpression());
			addVariable(varianceVariable);
			variable.setVarianceVariable(varianceVariable);
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
			removeVariable(variable.getSumVariable());
			removeVariable(variable.getVarianceVariable());

			byte calculation = variable.getCalculation();

			if (
				calculation == JRVariable.CALCULATION_AVERAGE ||
				calculation == JRVariable.CALCULATION_VARIANCE
				)
			{
				removeVariable(variable.getCountVariable());
				removeVariable(variable.getSumVariable());
			}

			if (calculation == JRVariable.CALCULATION_STANDARD_DEVIATION)
			{
				removeVariable(variable.getVarianceVariable());
			}

			variablesList.remove(variable);
			variablesMap.remove(variable.getName());
		}
		
		return variable;
	}


	/**
	 *
	 */
	public JRGroup[] getGroups()
	{
		JRGroup[] groupsArray = new JRGroup[groupsList.size()];
		
		groupsList.toArray(groupsArray);

		return groupsArray;
	}
	

	/**
	 *
	 */
	public List getGroupsList()
	{
		return groupsList;
	}
	

	/**
	 *
	 */
	public Map getGroupsMap()
	{
		return groupsMap;
	}
	

	/**
	 *
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
		expression.setName(JRExpression.PREFIX_variable + countVariable.getName());
		expression.setText("new Integer(1)");
		countVariable.setExpression((JRExpression)expression);
		expression = new JRDesignExpression();
		expression.setValueClass(Integer.class);
		expression.setName(JRExpression.PREFIX_variableInitialValue + countVariable.getName());
		expression.setText("new Integer(0)");
		countVariable.setInitialValueExpression((JRExpression)expression);

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
	 *
	 */
	public Collection getExpressions()
	{
		Collection expressions = new HashSet();
		
		expressions.addAll(getParameterExpressions());
		expressions.addAll(getVariableExpressions());
		expressions.addAll(getGroupExpressions());

		expressions.addAll(getBandExpressions(background));
		expressions.addAll(getBandExpressions(title));
		expressions.addAll(getBandExpressions(pageHeader));
		expressions.addAll(getBandExpressions(columnHeader));
		expressions.addAll(getBandExpressions(detail));
		expressions.addAll(getBandExpressions(columnFooter));
		expressions.addAll(getBandExpressions(pageFooter));
		expressions.addAll(getBandExpressions(lastPageFooter));
		expressions.addAll(getBandExpressions(summary));
		
		return expressions;
	}
		

	/**
	 *
	 */
	private Collection getParameterExpressions()
	{
		Collection expressions = new HashSet();
		
		if (parametersList != null && parametersList.size() > 0)
		{
			JRParameter parameter = null;
			JRExpression expression = null;
			for(int i = 0; i < parametersList.size(); i++)
			{
				parameter = (JRParameter)parametersList.get(i);
				expression = parameter.getDefaultValueExpression();
				if (expression != null)
				{
					expressions.add(expression);
				}
			}
		}
		
		return expressions;
	}


	/**
	 *
	 */
	private Collection getVariableExpressions()
	{
		Collection expressions = new HashSet();
		
		if (variablesList != null && variablesList.size() > 0)
		{
			JRVariable variable = null;
			JRExpression expression = null;
			for(int i = 0; i < variablesList.size(); i++)
			{
				variable = (JRVariable)variablesList.get(i);
				expression = variable.getExpression();
				if (expression != null)
				{
					expressions.add(expression);
				}

				expression = variable.getInitialValueExpression();
				if (expression != null)
				{
					expressions.add(expression);
				}
			}
		}
		
		return expressions;
	}


	/**
	 *
	 */
	private Collection getGroupExpressions()
	{
		Collection expressions = new HashSet();
		
		if (groupsList != null && groupsList.size() > 0)
		{
			JRGroup group = null;
			JRExpression expression = null;
			for(int i = 0; i < groupsList.size(); i++)
			{
				group = (JRGroup)groupsList.get(i);
				expression = group.getExpression();
				if (expression != null)
				{
					expressions.add(expression);
				}

				expressions.addAll(getBandExpressions(group.getGroupHeader()));
				expressions.addAll(getBandExpressions(group.getGroupFooter()));
			}
		}
		
		return expressions;
	}


	/**
	 *
	 */
	private Collection getBandExpressions(JRBand band)
	{
		Collection expressions = new HashSet();
		
		if (band != null)
		{
			JRExpression expression = null;
			expression = band.getPrintWhenExpression();
			if (expression != null)
			{
				expressions.add(expression);
			}
	
			JRElement[] elements = band.getElements();
			if (elements != null && elements.length > 0)
			{
				JRElement element = null;
				for(int i = 0; i < elements.length; i++)
				{
					element = elements[i];
					expression = element.getPrintWhenExpression();
					if (expression != null)
					{
						expressions.add(expression);
					}
	
					if (element instanceof JRImage)
					{
						expression = ((JRImage)element).getExpression();
						if (expression != null)
						{
							expressions.add(expression);
						}
					}
					else if (element instanceof JRTextField)
					{
						expression = ((JRTextField)element).getExpression();
						if (expression != null)
						{
							expressions.add(expression);
						}
					}
					else if (element instanceof JRSubreport)
					{
						expression = ((JRSubreport)element).getParametersMapExpression();
						if (expression != null)
						{
							expressions.add(expression);
						}
	
						JRSubreportParameter[] parameters = ((JRSubreport)element).getParameters();
						if (parameters != null && parameters.length > 0)
						{
							for(int j = 0; j < parameters.length; j++)
							{
								expression = parameters[j].getExpression();
								if (expression != null)
								{
									expressions.add(expression);
								}
							}
						}
	
						expression = ((JRSubreport)element).getConnectionExpression();
						if (expression != null)
						{
							expressions.add(expression);
						}
						expression = ((JRSubreport)element).getDataSourceExpression();
						if (expression != null)
						{
							expressions.add(expression);
						}
						expression = ((JRSubreport)element).getExpression();
						if (expression != null)
						{
							expressions.add(expression);
						}
					}
	
					if (element instanceof JRAnchor)
					{
						expression = ((JRAnchor)element).getAnchorNameExpression();
						if (expression != null)
						{
							expressions.add(expression);
						}
					}
	
					if (element instanceof JRHyperlink)
					{
						expression = ((JRHyperlink)element).getHyperlinkReferenceExpression();
						if (expression != null)
						{
							expressions.add(expression);
						}
						expression = ((JRHyperlink)element).getHyperlinkAnchorExpression();
						if (expression != null)
						{
							expressions.add(expression);
						}
						expression = ((JRHyperlink)element).getHyperlinkPageExpression();
						if (expression != null)
						{
							expressions.add(expression);
						}
					}
				}
			}
		}
		
		return expressions;
	}
	

}
