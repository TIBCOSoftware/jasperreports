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
package net.sf.jasperreports.crosstabs;

import net.sf.jasperreports.engine.JRBoxContainer;
import net.sf.jasperreports.engine.JRElement;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JRVariable;
import net.sf.jasperreports.engine.type.RunDirectionEnum;
import net.sf.jasperreports.engine.util.JRProperties;

/**
 * Crosstab element interface.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public interface JRCrosstab extends JRElement, JRBoxContainer
{
	/**
	 * The default offset used for column breaks.
	 * @see #getColumnBreakOffset()
	 */
	public static final int DEFAULT_COLUMN_BREAK_OFFSET = 10;
	
	/**
	 * Name of the built-in row count variable.
	 */
	public static final String VARIABLE_ROW_COUNT = "ROW_COUNT";
	
	/**
	 * Name of the built-in column count variable.
	 */
	public static final String VARIABLE_COLUMN_COUNT = "COLUMN_COUNT";

	/**
	 * A property that provides a default value for the ignore width crosstab flag.
	 * 
	 * <p>
	 * The property can be set globally and at report level.  A flag/attribute
	 * set for a crosstab will override the property.
	 * 
	 * <p>
	 * The property value set at report level will be used when the crosstab
	 * flag is not set.  If neither the crosstab flag and report level property
	 * exist and the {@link JRParameter#IS_IGNORE_PAGINATION} parameter is set to
	 * true, the crosstab width is ignored.  Otherwise, the global property value
	 * will be used.
	 * 
	 * <p>
	 * The default global value of this property is <code>false</code>, i.e. crosstabs
	 * will break by default at the width set for the crosstab report element.
	 * 
	 * @see #setIgnoreWidth(Boolean)
	 */
	public static final String PROPERTY_IGNORE_WIDTH = 
		JRProperties.PROPERTY_PREFIX + "crosstab.ignore.width";
	 
	
	/**
	 * Returns the ID of the crosstab.
	 * <p>
	 * The ID is generated when the report is compiled and is used internally.
	 * 
	 * @return the ID of the crosstab
	 */
	public int getId();
	
	/**
	 * Returns the input dataset of the crosstab.
	 * 
	 * @return the input dataset of the crosstab
	 */
	public JRCrosstabDataset getDataset();
	
	/**
	 * Returns the row groups of the crosstab.
	 * 
	 * @return the row groups
	 */
	public JRCrosstabRowGroup[] getRowGroups();
		
	/**
	 * Returns the column groups of the crosstab.
	 * 
	 * @return the column groups
	 */
	public JRCrosstabColumnGroup[] getColumnGroups();
	
	/**
	 * Returns the crosstab measures.
	 * 
	 * @return the crosstab measures
	 */
	public JRCrosstabMeasure[] getMeasures();

	/**
	 * Returns the column break offset.
	 * <p>
	 * When the crosstab columns do not fit the width, the crosstab
	 * breaks the columns and prints the ones that don't fit after printing
	 * the first set of columns for all rows.  This method returns the offset
	 * to be used when continuing the printing after a column break.
	 * 
	 * @return the column break offset
	 */
	public int getColumnBreakOffset();
	
	/**
	 * Returns whether to repeat the column headers after a row break.
	 * 
	 * @return whether to repeat the column headers after a row break
	 */
	public boolean isRepeatColumnHeaders();
	
	/**
	 * Returns whether to repeat the row headers after a column break.
	 * 
	 * @return whether to repeat the row headers after a column break
	 */
	public boolean isRepeatRowHeaders();
	
	/**
	 * Returns the data cell matrix.
	 * <p>
	 * A crosstab can have multiple data cells for row/groups totals.
	 * These cells are organized in a (rowGroupCount + 1) x (columnGroupCount + 1)
	 * matrix as following:
	 * <ul>
	 * 	<li>the row index of a cell is
	 * 		<ul>
	 * 			<li>the row group index, if the cell corresponds to a total row</li>
	 * 			<li>rowGroupCount, otherwise</li>
	 * 		</ul>
	 * 	</li>
	 * 	<li>the column index of a cell is
	 * 		<ul>
	 * 			<li>the column group index, if the cell corresponds to a total column</li>
	 * 			<li>columnGroupCount, otherwise</li>
	 * 		</ul>
	 * 	</li>
	 * </ul>
	 * <p>
	 * E.g. if the crosstab has Country and City (subgroup of Country) row groups and Year and Month column
	 * groups, the cells will be organized in the following matrix
	 * <table border="1">
	 * 	<tr>
	 * 		<td></td>
	 * 		<td>0/Years total</td>
	 * 		<td>1/Months total</td>
	 * 		<td>2/Base</td>
	 * 	</tr>
	 * 	<tr>
	 * 		<td>0/Countries total</td>
	 * 		<td>Total for all countries and all years</td>
	 * 		<td>Total for all countries and a year</td>
	 * 		<td>Total for all countries and a month</td>
	 * 	</tr>
	 * 	<tr>
	 * 		<td>1/Cities total</td>
	 * 		<td>Total for a country and all years</td>
	 * 		<td>Total for a country and a year</td>
	 * 		<td>Total for a country and a month</td>
	 * 	</tr>
	 * 	<tr>
	 * 		<td>2/Base</td>
	 * 		<td>Total for a city and all years</td>
	 * 		<td>Total for a city and a year</td>
	 * 		<td>Total for a city and a month</td>
	 * 	</tr>
	 * </table>
	 * <p>
	 * If the data cell for a total row/column is not specified, the cell will be inherited
	 * from lower levels, if possible.  For example, if the "Total for all countries and a year"
	 * cell is not specified, the "Total for a country and a year" or "Total for a city and a year"
	 * cell will be used (in this order).
	 * <p>
	 * The data cell sizes are calculated from the base data cell sizes, i.e.
	 * the height of all data cells for a row will be the height of the base cell
	 * of that row.  The base cell sizes are also inherited, e.g. if the height of a total
	 * row is not specified the height of the base row will be used.
	 * 
	 * @return the data cell matrix
	 */
	public JRCrosstabCell[][] getCells();
	
	/**
	 * Returns the crosstab parameters.
	 * <p>
	 * Crosstabs have separate expression evaluators and cannot access
	 * the parameters/fields/variables of the report.
	 * In order to use a value from the report inside a crosstab, it should be
	 * passed as parameter to the crosstab.
	 * <p>
	 * A crosstab parameters has a name and a value expression.
	 * 
	 * @return the crosstab parameters
	 */
	public JRCrosstabParameter[] getParameters();
	
	/**
	 * Returns the parameters map expression.
	 * <p>
	 * The set of values for crosstab parameters can be set using a map.
	 * If a parameter has also a value expression, it will overwrite the value
	 * from the map.
	 * 
	 * @return the parameters map expression
	 */
	public JRExpression getParametersMapExpression();
	
	/**
	 * Returns a cell which will be rendered when no data was fed to the crosstab.
	 * <p>
	 * If this cell is not specified for a crosstab, nothing will be rendered for an
	 * empty crosstab.
	 * <p>
	 * The cell sizes are the same as the crosstab element sizes.  
	 * 
	 * @return a cell which will be rendered when no data was fed to the crosstab
	 */
	public JRCellContents getWhenNoDataCell();


	/**
	 * Searches for an element inside all crosstab cells.
	 * 
	 * @param key the element key
	 * @return the first element having the specified key
	 */
	public JRElement getElementByKey(String key);

	
	/**
	 * Returns a cell which will be rendered at the upper-left corner of the crosstab.
	 * <p/>
	 * The cell size will be calculated based on the row/column header cell widths/heights.  
	 * 
	 * @return a cell which will be rendered at the upper-left corner of the crosstab
	 */
	public JRCellContents getHeaderCell();
	
	
	/**
	 * Returns the variables defined for the crosstab.
	 * 
	 * @return variables defined for the crosstab
	 * @see JRCrosstabGroup#getVariable()
	 * @see JRCrosstabMeasure#getVariable()
	 * @see #VARIABLE_ROW_COUNT
	 * @see #VARIABLE_COLUMN_COUNT
	 */
	public JRVariable[] getVariables();
	
	
	/**
	 * Retrieves the run direction of this crosstab
	 * @return a value representing one of the run direction constants in {@link RunDirectionEnum}
	 */
	public RunDirectionEnum getRunDirectionValue();
	
	/**
	 * Sets the run direction of the crosstab.
	 * <p>
	 * Crosstabs can either be filled from left to right (the default)
	 * or from right to left (mainly for reports in RTL languages).
	 * </p>
	 * @param runDirectionEnum a value representing one of the run direction constants in {@link RunDirectionEnum}
	 */
	public void setRunDirection(RunDirectionEnum runDirectionEnum);

	/**
	 * Returns the ignore width flag for the crosstab.
	 * 
	 * @return the ignore width flag, or <code>null</code> is the crosstab
	 * does not specify a flag value
	 * @see #setIgnoreWidth(Boolean)
	 */
	public Boolean getIgnoreWidth();
	
	/**
	 * Set the ignore width crosstab flag.
	 * 
	 * <p>
	 * This flag determines whether the crosstab will break at the width set for
	 * the crosstab element, or whether the crosstab is to expand over this width
	 * (and over the page width as well).
	 * 
	 * <p>
	 * If this flag is set to <code>true</code>, the crosstab will expand towards
	 * the right (or towards the left if the crosstab direction is RTL) as long
	 * as it needs to.  This would result in crosstab cells being rendered over
	 * the page boundary; such elements will be exported by certain grid-based
	 * exporters such as the HTML or XLS ones, but will not be visible in export
	 * formats that observe the page width, such as the PDF exporter.
	 * 
	 * <p>
	 * The default value of this flag is given by the 
	 * {@link #PROPERTY_IGNORE_WIDTH} property and the 
	 * {@link JRParameter#IS_IGNORE_PAGINATION} parameter.
	 * 
	 * @param ignoreWidth whether the element width is to be ignored by the crosstab,
	 * or <code>null</code> if the default setting is to be used
	 * @see #PROPERTY_IGNORE_WIDTH
	 */
	public void setIgnoreWidth(Boolean ignoreWidth);
	
	/**
	 * Set the ignore width crosstab flag.
	 * 
	 * @param ignoreWidth
	 * @see #setIgnoreWidth(Boolean)
	 */
	public void setIgnoreWidth(boolean ignoreWidth);
}
