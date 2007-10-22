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
package net.sf.jasperreports.engine.design;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.crosstabs.JRCrosstab;
import net.sf.jasperreports.crosstabs.design.JRDesignCrosstab;
import net.sf.jasperreports.engine.JRBand;
import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRDataset;
import net.sf.jasperreports.engine.JRElement;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRExpressionCollector;
import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.engine.JRFrame;
import net.sf.jasperreports.engine.JRGroup;
import net.sf.jasperreports.engine.JROrigin;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JRReportFont;
import net.sf.jasperreports.engine.JRReportTemplate;
import net.sf.jasperreports.engine.JRSortField;
import net.sf.jasperreports.engine.JRStyle;
import net.sf.jasperreports.engine.JRVariable;
import net.sf.jasperreports.engine.base.JRBaseReport;
import net.sf.jasperreports.engine.design.events.JRPropertyChangeSupport;
import net.sf.jasperreports.engine.design.events.PropagationChangeListener;


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
	/**
	 *
	 */
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

	public static final String PROPERTY_BACKGROUND = "background";

	public static final String PROPERTY_BOTTOM_MARGIN = "bottomMargin";

	public static final String PROPERTY_COLUMN_COUNT = "columnCount";

	public static final String PROPERTY_COLUMN_FOOTER = "columnFooter";

	public static final String PROPERTY_COLUMN_HEADER = "columnHeader";

	public static final String PROPERTY_COLUMN_SPACING = "columnSpacing";

	public static final String PROPERTY_COLUMN_WIDTH = "columnWidth";

	public static final String PROPERTY_DATASETS = "datasets";

	public static final String PROPERTY_DEFAULT_FONT = "defaultFont";

	public static final String PROPERTY_DEFAULT_STLYE = "defaultStyle";

	public static final String PROPERTY_DETAIL = "detail";

	public static final String PROPERTY_FLOAT_COLUMN_FOOTER = "floatColumnFooter";

	public static final String PROPERTY_FONTS = "fonts";

	public static final String PROPERTY_FORMAT_FACTORY_CLASS = "formatFactoryClass";

	public static final String PROPERTY_IGNORE_PAGINATION = "ignorePagination";

	public static final String PROPERTY_IMPORTS = "imports";

	public static final String PROPERTY_LANGUAGE = "language";

	public static final String PROPERTY_LAST_PAGE_FOOTER = "lastPageFooter";

	public static final String PROPERTY_LEFT_MARGIN = "leftMargin";

	public static final String PROPERTY_MAIN_DATASET = "mainDataset";
	
	public static final String PROPERTY_NAME = "name";

	public static final String PROPERTY_NO_DATA = "noData";

	public static final String PROPERTY_ORIENTATION = "orientation";

	public static final String PROPERTY_PAGE_FOOTER = "pageFooter";

	public static final String PROPERTY_PAGE_HEADER = "pageHeader";

	public static final String PROPERTY_PAGE_HEIGHT = "pageHeight";

	public static final String PROPERTY_PAGE_WIDTH = "pageWidth";

	public static final String PROPERTY_PRINT_ORDER = "printOrder";

	public static final String PROPERTY_RIGHT_MARGIN = "rightMargin";

	public static final String PROPERTY_STYLES = "styles";

	public static final String PROPERTY_SUMMARY = "summary";

	public static final String PROPERTY_SUMMARY_NEW_PAGE = "summaryNewPage";

	public static final String PROPERTY_TEMPLATES = "templates";

	public static final String PROPERTY_TITLE = "title";

	public static final String PROPERTY_TITLE_NEW_PAGE = "titleNewPage";

	public static final String PROPERTY_TOP_MARGIN = "topMargin";

	public static final String PROPERTY_WHEN_NO_DATA_TYPE = "whenNoDataType";
	
	private transient JRPropertyChangeSupport eventSupport;
	
	/**
	 * Report templates.
	 */
	private List templateList = new ArrayList();

	private Map fontsMap = new HashMap();
	private List fontsList = new ArrayList();
	private Map stylesMap = new HashMap();
	private List stylesList = new ArrayList();

	/**
	 * Main report dataset.
	 */
	private JRDesignDataset mainDesignDataset;

	/**
	 * Report sub datasets indexed by name.
	 */
	private Map datasetMap = new HashMap();
	private List datasetList = new ArrayList();

	private transient List crosstabs;

	/**
	 * Constructs a JasperDesign object and fills it with the default variables and parameters.
	 */
	public JasperDesign()
	{
		setMainDataset(new JRDesignDataset(true));
	}


	/**
	 * Sets the report name. It is strongly recommended that the report name matches the .jrxml file name, since report compilers
	 * usually use this name for the compiled .jasper file.
	 */
	public void setName(String name)
	{
		Object old = this.name;
		this.name = name;
		this.mainDesignDataset.setName(name);
		getEventSupport().firePropertyChange(PROPERTY_NAME, old, this.name);
	}


	/**
	 * Specifies the language used for report expressions (Java or Groovy). The default is Java.
	 */
	public void setLanguage(String language)
	{
		Object old = this.language;
		this.language = language;
		getEventSupport().firePropertyChange(PROPERTY_LANGUAGE, old, this.language);
	}


	/**
	 * Specifies the number of report columns.
	 */
	public void setColumnCount(int columnCount)
	{
		int old = this.columnCount;
		this.columnCount = columnCount;
		getEventSupport().firePropertyChange(PROPERTY_COLUMN_COUNT, old, this.columnCount);
	}


	/**
	 * Sets the print order. In case of multiple column reports, the engine can perform vertical or horizontal fill.
	 * @see net.sf.jasperreports.engine.JRReport PRINT_ORDER_VERTICAL,
	 * @see net.sf.jasperreports.engine.JRReport PRINT_ORDER_HORIZONTAL
	 */
	public void setPrintOrder(byte printOrder)
	{
		int old = this.printOrder;
		this.printOrder = printOrder;
		getEventSupport().firePropertyChange(PROPERTY_PRINT_ORDER, old, this.printOrder);
	}


	/**
	 * Sets page width (including margins etc.). Default is 595.
	 */
	public void setPageWidth(int pageWidth)
	{
		int old = this.pageWidth;
		this.pageWidth = pageWidth;
		getEventSupport().firePropertyChange(PROPERTY_PAGE_WIDTH, old, this.pageWidth);
	}


	/**
	 * Sets page height (including margins etc.). Default is 842.
	 */
	public void setPageHeight(int pageHeight)
	{
		int old = this.pageHeight;
		this.pageHeight = pageHeight;
		getEventSupport().firePropertyChange(PROPERTY_PAGE_HEIGHT, old, this.pageHeight);
	}


	/**
	 * Sets the report orientation.
	 * @see net.sf.jasperreports.engine.JRReport ORIENTATION_PORTRAIT,
	 * @see net.sf.jasperreports.engine.JRReport ORIENTATION_LANDSCAPE
	 */
	public void setOrientation(byte orientation)
	{
		int old = this.orientation;
		this.orientation = orientation;
		getEventSupport().firePropertyChange(PROPERTY_ORIENTATION, old, this.orientation);
	}


	/**
	 * Sets the column width.
	 */
	public void setColumnWidth(int columnWidth)
	{
		int old = columnWidth;
		this.columnWidth = columnWidth;
		getEventSupport().firePropertyChange(PROPERTY_COLUMN_WIDTH, old, this.columnWidth);
	}


	/**
	 * Sets the spacing between columns.
	 */
	public void setColumnSpacing(int columnSpacing)
	{
		int old = this.columnSpacing;
		this.columnSpacing = columnSpacing;
		getEventSupport().firePropertyChange(PROPERTY_COLUMN_SPACING, old, this.columnSpacing);
	}


	/**
	 * Sets the left margin. The working space is calculated by subtracting the margins from the page width.
	 */
	public void setLeftMargin(int leftMargin)
	{
		int old = this.leftMargin;
		this.leftMargin = leftMargin;
		getEventSupport().firePropertyChange(PROPERTY_LEFT_MARGIN, old, this.leftMargin);
	}


	/**
	 * Sets the right margin. The working space is calculated by subtracting the margins from the page width.
	 */
	public void setRightMargin(int rightMargin)
	{
		int old = this.rightMargin;
		this.rightMargin = rightMargin;
		getEventSupport().firePropertyChange(PROPERTY_RIGHT_MARGIN, old, this.rightMargin);
	}


	/**
	 * Sets the top margin. The working space is calculated by subtracting the margins from the page height.
	 */
	public void setTopMargin(int topMargin)
	{
		int old = this.topMargin;
		this.topMargin = topMargin;
		getEventSupport().firePropertyChange(PROPERTY_TOP_MARGIN, old, this.topMargin);
	}


	/**
	 * Sets the top margin. The working space is calculated by subtracting the margins from the page height.
	 */
	public void setBottomMargin(int bottomMargin)
	{
		int old = this.bottomMargin;
		this.bottomMargin = bottomMargin;
		getEventSupport().firePropertyChange(PROPERTY_BOTTOM_MARGIN, old, this.bottomMargin);
	}


	/**
	 * Sets the background band.
	 */
	public void setBackground(JRBand background)
	{
		Object old = this.background;
		this.background = background;
		setBandOrigin(this.background, JROrigin.BACKGROUND);
		getEventSupport().firePropertyChange(PROPERTY_BACKGROUND, old, this.background);
	}


	/**
	 * Sets the title band.
	 */
	public void setTitle(JRBand title)
	{
		Object old = this.title;
		this.title = title;
		setBandOrigin(this.title, JROrigin.TITLE);
		getEventSupport().firePropertyChange(PROPERTY_TITLE, old, this.title);
	}


	/**
	 * Flag used to specify if the title section should be printed on a separate initial page.
	 *
	 * @param isTitleNewPage true if the title section should be displayed on a separate initial page, false if
	 * it will be displayed on the first page along with other sections.
	 */
	public void setTitleNewPage(boolean isTitleNewPage)
	{
		boolean old = this.isTitleNewPage;
		this.isTitleNewPage = isTitleNewPage;
		getEventSupport().firePropertyChange(PROPERTY_TITLE_NEW_PAGE, old, this.isTitleNewPage);
	}


	/**
	 * Sets the summary band.
	 */
	public void setSummary(JRBand summary)
	{
		Object old = this.summary;
		this.summary = summary;
		setBandOrigin(this.summary, JROrigin.SUMMARY);
		getEventSupport().firePropertyChange(PROPERTY_SUMMARY, old, this.summary);
	}

	/**
	 * Sets the noData band.
	 */
	public void setNoData(JRBand noData)
	{
		Object old = this.noData;
		this.noData = noData;
		setBandOrigin(this.noData, JROrigin.NO_DATA);
		getEventSupport().firePropertyChange(PROPERTY_NO_DATA, old, this.noData);
	}


	/**
	 * Flag used to specify if the summary section should be printed on a separate last page.
	 *
	 * @param isSummaryNewPage true if the summary section should be displayed on a separate last page, false if
	 * it will be displayed on the last page along with other sections, if there is enough space.
	 */
	public void setSummaryNewPage(boolean isSummaryNewPage)
	{
		boolean old = this.isSummaryNewPage;
		this.isSummaryNewPage = isSummaryNewPage;
		getEventSupport().firePropertyChange(PROPERTY_SUMMARY_NEW_PAGE, old, this.isSummaryNewPage);
	}


	/**
	 * Flag used to specify if the column footer section should be printed at the bottom of the column or if it
	 * should immediately follow the last detail or group footer printed on the current column.
	 */
	public void setFloatColumnFooter(boolean isFloatColumnFooter)
	{
		boolean old = this.isFloatColumnFooter;
		this.isFloatColumnFooter = isFloatColumnFooter;
		getEventSupport().firePropertyChange(PROPERTY_FLOAT_COLUMN_FOOTER, old, this.isFloatColumnFooter);
	}


	/**
	 * Sets the page header band.
	 */
	public void setPageHeader(JRBand pageHeader)
	{
		Object old = this.pageHeader;
		this.pageHeader = pageHeader;
		setBandOrigin(this.pageHeader, JROrigin.PAGE_HEADER);
		getEventSupport().firePropertyChange(PROPERTY_PAGE_HEADER, old, this.pageHeader);
	}


	/**
	 * Sets the page footer band.
	 */
	public void setPageFooter(JRBand pageFooter)
	{
		Object old = this.pageFooter;
		this.pageFooter = pageFooter;
		setBandOrigin(this.pageFooter, JROrigin.PAGE_FOOTER);
		getEventSupport().firePropertyChange(PROPERTY_PAGE_FOOTER, old, this.pageFooter);
	}


	/**
	 * Sets the last page footer band.
	 */
	public void setLastPageFooter(JRBand lastPageFooter)
	{
		Object old = this.lastPageFooter;
		this.lastPageFooter = lastPageFooter;
		setBandOrigin(this.lastPageFooter, JROrigin.LAST_PAGE_FOOTER);
		getEventSupport().firePropertyChange(PROPERTY_LAST_PAGE_FOOTER, old, this.lastPageFooter);
	}


	/**
	 * Sets the column header band.
	 */
	public void setColumnHeader(JRBand columnHeader)
	{
		Object old = this.columnHeader;
		this.columnHeader = columnHeader;
		setBandOrigin(this.columnHeader, JROrigin.COLUMN_HEADER);
		getEventSupport().firePropertyChange(PROPERTY_COLUMN_HEADER, old, this.columnHeader);
	}


	/**
	 * Sets the column footer band.
	 */
	public void setColumnFooter(JRBand columnFooter)
	{
		Object old = this.columnFooter;
		this.columnFooter = columnFooter;
		setBandOrigin(this.columnFooter, JROrigin.COLUMN_FOOTER);
		getEventSupport().firePropertyChange(PROPERTY_COLUMN_FOOTER, old, this.columnFooter);
	}


	/**
	 * Sets the detail band.
	 */
	public void setDetail(JRBand detail)
	{
		Object old = this.detail;
		this.detail = detail;
		setBandOrigin(this.detail, JROrigin.DETAIL);
		getEventSupport().firePropertyChange(PROPERTY_DETAIL, old, this.detail);
	}


	/**
	 *
	 */
	public void setScriptletClass(String scriptletClass)
	{
		mainDesignDataset.setScriptletClass(scriptletClass);
	}


	/**
	 *
	 */
	public void setFormatFactoryClass(String formatFactoryClass)
	{
		Object old = this.formatFactoryClass;
		this.formatFactoryClass = formatFactoryClass;
		getEventSupport().firePropertyChange(PROPERTY_FORMAT_FACTORY_CLASS, old, this.formatFactoryClass);
	}


	/**
	 * Sets the base name of the report associated resource bundle.
	 */
	public void setResourceBundle(String resourceBundle)
	{
		mainDesignDataset.setResourceBundle(resourceBundle);
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
		
		if (importsSet.add(value))
		{
			getEventSupport().fireCollectionElementAddedEvent(PROPERTY_IMPORTS, value, importsSet.size() - 1);
		}
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
	 * @deprecated
	 */
	public void setDefaultFont(JRReportFont font)
	{
		Object old = this.defaultFont;
		this.defaultFont = font;
		getEventSupport().firePropertyChange(PROPERTY_DEFAULT_FONT, old, this.defaultFont);
	}


	/**
	 * Gets an array of report level fonts. These fonts can be referenced by text elements.
	 * @deprecated
	 */
	public JRReportFont[] getFonts()
	{
		JRReportFont[] fontsArray = new JRReportFont[fontsList.size()];

		fontsList.toArray(fontsArray);

		return fontsArray;
	}


	/**
	 * Gets a list of report level fonts. These fonts can be referenced by text elements.
	 * @deprecated
	 */
	public List getFontsList()
	{
		return fontsList;
	}


	/**
	 * @deprecated
	 */
	public Map getFontsMap()
	{
		return fontsMap;
	}


	/**
	 * Adds a report font, that can be referenced by text elements.
	 * @deprecated
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
		
		getEventSupport().fireCollectionElementAddedEvent(PROPERTY_FONTS, reportFont, fontsList.size() - 1);
	}


	/**
	 * Removes a report font from the list, based on the font name.
	 * @deprecated
	 */
	public JRReportFont removeFont(String propName)
	{
		return removeFont(
			(JRReportFont)fontsMap.get(propName)
			);
	}


	/**
	 * Removes a report font from the list.
	 * @deprecated
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
	public void setDefaultStyle(JRStyle style)
	{
		Object old = this.defaultStyle;
		this.defaultStyle = style;
		getEventSupport().firePropertyChange(PROPERTY_DEFAULT_STLYE, old, this.defaultStyle);
	}


	/**
	 * Gets an array of report level styles. These styles can be referenced by report elements.
	 */
	public JRStyle[] getStyles()
	{
		JRStyle[] stylesArray = new JRStyle[stylesList.size()];

		stylesList.toArray(stylesArray);

		return stylesArray;
	}


	/**
	 * Gets a list of report level styles. These styles can be referenced by report elements.
	 */
	public List getStylesList()
	{
		return stylesList;
	}


	/**
	 *
	 */
	public Map getStylesMap()
	{
		return stylesMap;
	}


	/**
	 * Adds a report style, that can be referenced by report elements.
	 */
	public void addStyle(JRStyle style) throws JRException
	{
		if (stylesMap.containsKey(style.getName()))
		{
			throw new JRException("Duplicate declaration of report style : " + style.getName());
		}

		stylesList.add(style);
		stylesMap.put(style.getName(), style);

		if (style.isDefault())
		{
			setDefaultStyle(style);
		}
		
		getEventSupport().fireCollectionElementAddedEvent(PROPERTY_STYLES, style, stylesList.size() - 1);
	}


	/**
	 * Removes a report style from the list, based on the style name.
	 */
	public JRStyle removeStyle(String styleName)
	{
		return removeStyle(
			(JRStyle)stylesMap.get(styleName)
			);
	}


	/**
	 * Removes a report style from the list.
	 */
	public JRStyle removeStyle(JRStyle style)
	{
		if (style != null)
		{
			if (style.isDefault())
			{
				setDefaultStyle(null);
			}

			stylesList.remove(style);
			stylesMap.remove(style.getName());
		}

		return style;
	}


	/**
	 * Gets a list of report parameters (including built-in ones).
	 */
	public List getParametersList()
	{
		return mainDesignDataset.getParametersList();
	}


	/**
	 * Gets a map of report parameters (including built-in ones).
	 */
	public Map getParametersMap()
	{
		return mainDesignDataset.getParametersMap();
	}


	/**
	 * Adds a report parameter.
	 */
	public void addParameter(JRParameter parameter) throws JRException
	{
		mainDesignDataset.addParameter(parameter);
	}


	/**
	 * Removes a report parameter, based on its name.
	 */
	public JRParameter removeParameter(String parameterName)
	{
		return mainDesignDataset.removeParameter(parameterName);
	}


	/**
	 * Removes a report parameter.
	 */
	public JRParameter removeParameter(JRParameter parameter)
	{
		return mainDesignDataset.removeParameter(parameter);
	}


	/**
	 * Specifies the report query. This is used only when datasource type is JDBC (a <tt>java.sql.Connection</tt>).
	 */
	public void setQuery(JRDesignQuery query)
	{
		mainDesignDataset.setQuery(query);
	}


	/**
	 * Gets a list of report fields.
	 */
	public List getFieldsList()
	{
		return mainDesignDataset.getFieldsList();
	}


	/**
	 * Gets a map of report fields.
	 */
	public Map getFieldsMap()
	{
		return mainDesignDataset.getFieldsMap();
	}


	/**
	 *
	 */
	public void addField(JRField field) throws JRException
	{
		mainDesignDataset.addField(field);
	}


	/**
	 *
	 */
	public JRField removeField(String fieldName)
	{
		return mainDesignDataset.removeField(fieldName);
	}


	/**
	 *
	 */
	public JRField removeField(JRField field)
	{
		return mainDesignDataset.removeField(field);
	}


	/**
	 * Gets a list of sort report fields.
	 */
	public List getSortFieldsList()
	{
		return mainDesignDataset.getSortFieldsList();
	}


	/**
	 *
	 */
	public void addSortField(JRSortField sortField) throws JRException
	{
		mainDesignDataset.addSortField(sortField);
	}


	/**
	 *
	 */
	public JRSortField removeSortField(String fieldName)
	{
		return mainDesignDataset.removeSortField(fieldName);
	}


	/**
	 *
	 */
	public JRSortField removeSortField(JRSortField sortField)
	{
		return mainDesignDataset.removeSortField(sortField);
	}


	/**
	 * Gets a list of report variables.
	 */
	public List getVariablesList()
	{
		return mainDesignDataset.getVariablesList();
	}


	/**
	 * Gets a map of report variables.
	 */
	public Map getVariablesMap()
	{
		return mainDesignDataset.getVariablesMap();
	}


	/**
	 *
	 */
	public void addVariable(JRDesignVariable variable) throws JRException
	{
		mainDesignDataset.addVariable(variable);
	}


	/**
	 *
	 */
	public JRVariable removeVariable(String variableName)
	{
		return mainDesignDataset.removeVariable(variableName);
	}


	/**
	 *
	 */
	public JRVariable removeVariable(JRVariable variable)
	{
		return mainDesignDataset.removeVariable(variable);
	}


	/**
	 * Gets an array of report groups.
	 */
	public List getGroupsList()
	{
		return mainDesignDataset.getGroupsList();
	}


	/**
	 * Gets a list of report groups.
	 */
	public Map getGroupsMap()
	{
		return mainDesignDataset.getGroupsMap();
	}


	/**
	 * Gets a map of report groups.
	 */
	public void addGroup(JRDesignGroup group) throws JRException
	{
		mainDesignDataset.addGroup(group);
	}


	/**
	 * Adds a new group to the report design. Groups are nested.
	 */
	public JRGroup removeGroup(String groupName)
	{
		return mainDesignDataset.removeGroup(groupName);
	}


	/**
	 *
	 */
	public JRGroup removeGroup(JRGroup group)
	{
		return mainDesignDataset.removeGroup(group);
	}


	/**
	 * Returns a collection of all report expressions.
	 */
	public Collection getExpressions()
	{
		return JRExpressionCollector.collectExpressions(this);
	}


	public JRDataset[] getDatasets()
	{
		JRDataset[] datasetArray = new JRDataset[datasetList.size()];
		datasetList.toArray(datasetArray);
		return datasetArray;
	}



	/**
	 * Returns the list of report sub datasets.
	 *
	 * @return list of {@link JRDesignDataset JRDesignDataset} objects
	 */
	public List getDatasetsList()
	{
		return datasetList;
	}


	/**
	 * Returns the sub datasets of the report indexed by name.
	 *
	 * @return the sub datasets of the report indexed by name
	 */
	public Map getDatasetMap()
	{
		return datasetMap;
	}


	/**
	 * Adds a sub dataset to the report.
	 *
	 * @param dataset the dataset
	 * @throws JRException
	 */
	public void addDataset(JRDesignDataset dataset) throws JRException
	{
		if (datasetMap.containsKey(dataset.getName()))
		{
			throw new JRException("Duplicate declaration of dataset : " + dataset.getName());
		}

		datasetList.add(dataset);
		datasetMap.put(dataset.getName(), dataset);
		
		getEventSupport().fireCollectionElementAddedEvent(PROPERTY_DATASETS, dataset, datasetList.size() - 1);
	}


	/**
	 * Removes a sub dataset from the report.
	 *
	 * @param datasetName the dataset name
	 * @return the removed dataset
	 */
	public JRDataset removeDataset(String datasetName)
	{
		return removeDataset(
			(JRDataset)datasetMap.get(datasetName)
			);
	}


	/**
	 * Removes a sub dataset from the report.
	 *
	 * @param dataset the dataset to be removed
	 * @return the dataset
	 */
	public JRDataset removeDataset(JRDataset dataset)
	{
		if (dataset != null)
		{
			datasetList.remove(dataset);
			datasetMap.remove(dataset.getName());
		}

		return dataset;
	}


	/**
	 * Returns the main report dataset.
	 *
	 * @return the main report dataset
	 */
	public JRDesignDataset getMainDesignDataset()
	{
		return mainDesignDataset;
	}


	/**
	 * Sets the main report dataset.
	 * <p>
	 * This method can be used as an alternative to setting the parameters, fields, etc directly on the report.
	 *
	 * @param dataset the dataset
	 */
	public void setMainDataset(JRDesignDataset dataset)
	{
		Object old = this.background;
		this.mainDataset = this.mainDesignDataset = dataset;
		this.mainDesignDataset.setName(getName());
		this.mainDesignDataset.getEventSupport().addPropertyChangeListener(new PropagationChangeListener(getEventSupport()));
		getEventSupport().firePropertyChange(PROPERTY_MAIN_DATASET, old, this.mainDataset);
	}


	/**
	 * Performs preliminary processing and calculations prior to compilation.
	 */
	public void preprocess()
	{
		collectCrosstabs();

		for (Iterator it = crosstabs.iterator(); it.hasNext();)
		{
			JRDesignCrosstab crosstab = (JRDesignCrosstab) it.next();
			crosstab.preprocess();
		}
	}

	protected List getCrosstabs()
	{
		if (crosstabs == null)
		{
			collectCrosstabs();
		}

		return crosstabs;
	}

	protected List collectCrosstabs()
	{
		crosstabs = new ArrayList();
		
		//TODO use JRElementsVisitor?
		collectCrosstabs(background);
		collectCrosstabs(title);
		collectCrosstabs(pageHeader);
		collectCrosstabs(columnHeader);
		collectCrosstabs(detail);
		collectCrosstabs(columnFooter);
		collectCrosstabs(pageFooter);
		collectCrosstabs(lastPageFooter);
		collectCrosstabs(summary);
		collectCrosstabs(noData);

		JRGroup[] groups = getGroups();
		if (groups != null)
		{
			for (int i = 0; i < groups.length; i++)
			{
				collectCrosstabs(groups[i].getGroupHeader());
				collectCrosstabs(groups[i].getGroupFooter());
			}
		}

		return crosstabs;
	}

	protected void collectCrosstabs(JRBand band)
	{
		if (band != null)
		{
			collectCrosstabs(band.getElements());
		}
	}


	protected void collectCrosstabs(JRElement[] elements)
	{
		if (elements != null)
		{
			for (int i = 0; i < elements.length; i++)
			{
				JRElement element = elements[i];
				if (element instanceof JRCrosstab)
				{
					crosstabs.add(element);
				}
				else if (element instanceof JRFrame)
				{
					JRFrame frame = (JRFrame) element;
					collectCrosstabs(frame.getElements());
				}
			}
		}
	}


	/**
	 * Sets the value of the ignore pagination flag.
	 *
	 * @param ignorePagination whether to ignore pagination when generating the report
	 * @see net.sf.jasperreports.engine.JRReport#isIgnorePagination()
	 */
	public void setIgnorePagination(boolean ignorePagination)
	{
		boolean old = this.ignorePagination;
		this.ignorePagination = ignorePagination;
		getEventSupport().firePropertyChange(PROPERTY_IGNORE_PAGINATION, old, this.ignorePagination);
	}


	/**
	 * Returns the main dataset filter expression.
	 *
	 * @return the main dataset filter expression
	 * @see JRDataset#getFilterExpression()
	 */
	public JRExpression getFilterExpression()
	{
		return mainDesignDataset.getFilterExpression();
	}


	/**
	 * Sets the main dataset filter expression.
	 *
	 * @param expression the boolean expression to use as main dataset filter expression
	 * @see JRDesignDataset#setFilterExpression(JRExpression)
	 * @see JRDataset#getFilterExpression()
	 */
	public void setFilterExpression(JRExpression expression)
	{
		mainDesignDataset.setFilterExpression(expression);
	}

	/**
	 * Adds a report template.
	 *
	 * @param template the template to add.
	 * @see #getTemplates()
	 */
	public void addTemplate(JRReportTemplate template)
	{
		templateList.add(template);
		
		getEventSupport().fireCollectionElementAddedEvent(PROPERTY_TEMPLATES, template, templateList.size() - 1);
	}

	/**
	 * Removes a report template.
	 *
	 * @param template the template to remove
	 * @return <code>true</code> iff the template has been found and removed
	 */
	public boolean removeTemplate(JRReportTemplate template)
	{
		return templateList.remove(template);
	}

	public JRReportTemplate[] getTemplates()
	{
		return (JRReportTemplate[]) templateList.toArray(new JRReportTemplate[templateList.size()]);
	}

	protected void setBandOrigin(JRBand band, byte type)
	{
		if (band instanceof JRDesignBand)
		{
			JROrigin origin = new JROrigin(type);
			((JRDesignBand) band).setOrigin(origin);
		}
	}
	
	public JRPropertyChangeSupport getEventSupport()
	{
		synchronized (this)
		{
			if (eventSupport == null)
			{
				eventSupport = new JRPropertyChangeSupport(this);
			}
		}
		
		return eventSupport;
	}

	public void setWhenNoDataType(byte whenNoDataType)
	{
		byte old = getWhenNoDataType();
		super.setWhenNoDataType(whenNoDataType);
		getEventSupport().firePropertyChange(PROPERTY_WHEN_NO_DATA_TYPE, old, getWhenNoDataType());
	}
	
}
