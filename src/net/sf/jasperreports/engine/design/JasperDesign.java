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
		this.name = name;
		this.mainDesignDataset.setName(name);
	}


	/**
	 * Specifies the language used for report expressions (Java or Groovy). The default is Java.
	 */
	public void setLanguage(String language)
	{
		this.language = language;
	}


	/**
	 * Specifies the number of report columns.
	 */
	public void setColumnCount(int columnCount)
	{
		this.columnCount = columnCount;
	}


	/**
	 * Sets the print order. In case of multiple column reports, the engine can perform vertical or horizontal fill.
	 * @see net.sf.jasperreports.engine.JRReport PRINT_ORDER_VERTICAL,
	 * @see net.sf.jasperreports.engine.JRReport PRINT_ORDER_HORIZONTAL
	 */
	public void setPrintOrder(byte printOrder)
	{
		this.printOrder = printOrder;
	}


	/**
	 * Sets page width (including margins etc.). Default is 595.
	 */
	public void setPageWidth(int pageWidth)
	{
		this.pageWidth = pageWidth;
	}


	/**
	 * Sets page height (including margins etc.). Default is 842.
	 */
	public void setPageHeight(int pageHeight)
	{
		this.pageHeight = pageHeight;
	}


	/**
	 * Sets the report orientation.
	 * @see net.sf.jasperreports.engine.JRReport ORIENTATION_PORTRAIT,
	 * @see net.sf.jasperreports.engine.JRReport ORIENTATION_LANDSCAPE
	 */
	public void setOrientation(byte orientation)
	{
		this.orientation = orientation;
	}


	/**
	 * Sets the column width.
	 */
	public void setColumnWidth(int columnWidth)
	{
		this.columnWidth = columnWidth;
	}


	/**
	 * Sets the spacing between columns.
	 */
	public void setColumnSpacing(int columnSpacing)
	{
		this.columnSpacing = columnSpacing;
	}


	/**
	 * Sets the left margin. The working space is calculated by subtracting the margins from the page width.
	 */
	public void setLeftMargin(int leftMargin)
	{
		this.leftMargin = leftMargin;
	}


	/**
	 * Sets the right margin. The working space is calculated by subtracting the margins from the page width.
	 */
	public void setRightMargin(int rightMargin)
	{
		this.rightMargin = rightMargin;
	}


	/**
	 * Sets the top margin. The working space is calculated by subtracting the margins from the page height.
	 */
	public void setTopMargin(int topMargin)
	{
		this.topMargin = topMargin;
	}


	/**
	 * Sets the top margin. The working space is calculated by subtracting the margins from the page height.
	 */
	public void setBottomMargin(int bottomMargin)
	{
		this.bottomMargin = bottomMargin;
	}


	/**
	 * Sets the background band.
	 */
	public void setBackground(JRBand background)
	{
		this.background = background;
		setBandOrigin(this.background, JROrigin.BACKGROUND);
	}


	/**
	 * Sets the title band.
	 */
	public void setTitle(JRBand title)
	{
		this.title = title;
		setBandOrigin(this.title, JROrigin.TITLE);
	}


	/**
	 * Flag used to specify if the title section should be printed on a separate initial page.
	 *
	 * @param isTitleNewPage true if the title section should be displayed on a separate initial page, false if
	 * it will be displayed on the first page along with other sections.
	 */
	public void setTitleNewPage(boolean isTitleNewPage)
	{
		this.isTitleNewPage = isTitleNewPage;
	}


	/**
	 * Sets the summary band.
	 */
	public void setSummary(JRBand summary)
	{
		this.summary = summary;
		setBandOrigin(this.summary, JROrigin.SUMMARY);
	}

	/**
	 * Sets the noData band.
	 */
	public void setNoData(JRBand noData)
	{
		this.noData = noData;
		setBandOrigin(this.noData, JROrigin.NO_DATA);
	}


	/**
	 * Flag used to specify if the summary section should be printed on a separate last page.
	 *
	 * @param isSummaryNewPage true if the summary section should be displayed on a separate last page, false if
	 * it will be displayed on the last page along with other sections, if there is enough space.
	 */
	public void setSummaryNewPage(boolean isSummaryNewPage)
	{
		this.isSummaryNewPage = isSummaryNewPage;
	}


	/**
	 * Flag used to specify if the column footer section should be printed at the bottom of the column or if it
	 * should immediately follow the last detail or group footer printed on the current column.
	 */
	public void setFloatColumnFooter(boolean isFloatColumnFooter)
	{
		this.isFloatColumnFooter = isFloatColumnFooter;
	}


	/**
	 * Sets the page header band.
	 */
	public void setPageHeader(JRBand pageHeader)
	{
		this.pageHeader = pageHeader;
		setBandOrigin(this.pageHeader, JROrigin.PAGE_HEADER);
	}


	/**
	 * Sets the page footer band.
	 */
	public void setPageFooter(JRBand pageFooter)
	{
		this.pageFooter = pageFooter;
		setBandOrigin(this.pageFooter, JROrigin.PAGE_FOOTER);
	}


	/**
	 * Sets the last page footer band.
	 */
	public void setLastPageFooter(JRBand lastPageFooter)
	{
		this.lastPageFooter = lastPageFooter;
		setBandOrigin(this.lastPageFooter, JROrigin.LAST_PAGE_FOOTER);
	}


	/**
	 * Sets the column header band.
	 */
	public void setColumnHeader(JRBand columnHeader)
	{
		this.columnHeader = columnHeader;
		setBandOrigin(this.columnHeader, JROrigin.COLUMN_HEADER);
	}


	/**
	 * Sets the column footer band.
	 */
	public void setColumnFooter(JRBand columnFooter)
	{
		this.columnFooter = columnFooter;
		setBandOrigin(this.columnFooter, JROrigin.COLUMN_FOOTER);
	}


	/**
	 * Sets the detail band.
	 */
	public void setDetail(JRBand detail)
	{
		this.detail = detail;
		setBandOrigin(this.detail, JROrigin.DETAIL);
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
		this.formatFactoryClass = formatFactoryClass;
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
	 * @deprecated
	 */
	public void setDefaultFont(JRReportFont font)
	{
		this.defaultFont = font;
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
		this.defaultStyle = style;
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
		this.mainDataset = this.mainDesignDataset = dataset;
		this.mainDesignDataset.setName(getName());
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
		this.ignorePagination = ignorePagination;
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
}
