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
package net.sf.jasperreports.crosstabs.design;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.io.Serializable;
import java.net.URLStreamHandlerFactory;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.commons.collections.SequencedHashMap;

import net.sf.jasperreports.crosstabs.JRCellContents;
import net.sf.jasperreports.crosstabs.JRCrosstab;
import net.sf.jasperreports.crosstabs.JRCrosstabBucket;
import net.sf.jasperreports.crosstabs.JRCrosstabCell;
import net.sf.jasperreports.crosstabs.JRCrosstabColumnGroup;
import net.sf.jasperreports.crosstabs.JRCrosstabDataset;
import net.sf.jasperreports.crosstabs.JRCrosstabGroup;
import net.sf.jasperreports.crosstabs.JRCrosstabMeasure;
import net.sf.jasperreports.crosstabs.JRCrosstabParameter;
import net.sf.jasperreports.crosstabs.JRCrosstabRowGroup;
import net.sf.jasperreports.crosstabs.base.JRBaseCrosstab;
import net.sf.jasperreports.engine.JRAbstractObjectFactory;
import net.sf.jasperreports.engine.JRChild;
import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRDefaultStyleProvider;
import net.sf.jasperreports.engine.JRElement;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRExpressionCollector;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JRVariable;
import net.sf.jasperreports.engine.design.JRDesignElement;
import net.sf.jasperreports.engine.design.JRDesignVariable;
import net.sf.jasperreports.engine.util.JRStyleResolver;
import net.sf.jasperreports.engine.xml.JRXmlWriter;

/**
 * Design-time {@link net.sf.jasperreports.crosstabs.JRCrosstab crosstab} implementation.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public class JRDesignCrosstab extends JRDesignElement implements JRCrosstab
{
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

	protected List parametersList;
	protected Map parametersMap;
	protected Map variablesList;
	protected JRExpression parametersMapExpression;
	protected JRDesignCrosstabDataset dataset;
	protected List rowGroups;
	protected List columnGroups;
	protected List measures;
	protected Map rowGroupsMap;
	protected Map columnGroupsMap;
	protected Map measuresMap;
	protected int columnBreakOffset = DEFAULT_COLUMN_BREAK_OFFSET;
	protected boolean repeatColumnHeaders = true;
	protected boolean repeatRowHeaders = true;
	protected List cellsList;
	protected Map cellsMap;
	protected JRDesignCrosstabCell[][] crossCells;
	protected JRDesignCellContents whenNoDataCell;
	protected JRDesignCellContents headerCell;
	
	
	private class MeasureClassChangeListener implements PropertyChangeListener, Serializable
	{
		private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

		public void propertyChange(PropertyChangeEvent evt)
		{
			measureClassChanged((JRDesignCrosstabMeasure) evt.getSource(), (String) evt.getNewValue());
		}
	}

	private PropertyChangeListener measureClassChangeListener = new MeasureClassChangeListener();
	
	private static final Object[] BUILT_IN_PARAMETERS = new Object[] { 
		JRParameter.REPORT_PARAMETERS_MAP, java.util.Map.class, 
		JRParameter.REPORT_LOCALE, Locale.class, 
		JRParameter.REPORT_RESOURCE_BUNDLE, ResourceBundle.class,
		JRParameter.REPORT_CLASS_LOADER, ClassLoader.class,
		JRParameter.REPORT_URL_HANDLER_FACTORY, URLStreamHandlerFactory.class};
	
	private static final Object[] BUILT_IN_VARIABLES = new Object[] { 
		JRCrosstab.VARIABLE_ROW_COUNT, Integer.class, 
		JRCrosstab.VARIABLE_COLUMN_COUNT, Integer.class};

	
	/**
	 * Creates a new crosstab.
	 * 
	 * @param defaultStyleProvider default style provider
	 */
	public JRDesignCrosstab(JRDefaultStyleProvider defaultStyleProvider)
	{
		super(defaultStyleProvider);
		
		parametersList = new ArrayList();
		parametersMap = new HashMap();
		
		rowGroupsMap = new HashMap();
		rowGroups = new ArrayList();
		columnGroupsMap = new HashMap();
		columnGroups = new ArrayList();
		measuresMap = new HashMap();
		measures = new ArrayList();
		
		cellsMap = new HashMap();
		cellsList = new ArrayList();
		
		addBuiltinParameters();
		
		variablesList = new SequencedHashMap();
		addBuiltinVariables();
		
		dataset = new JRDesignCrosstabDataset();
	}

	private void addBuiltinParameters()
	{
		for (int i = 0; i < BUILT_IN_PARAMETERS.length; i++)
		{
			JRDesignCrosstabParameter parameter = new JRDesignCrosstabParameter();
			parameter.setName((String) BUILT_IN_PARAMETERS[i++]);
			parameter.setValueClass((Class) BUILT_IN_PARAMETERS[i]);
			parameter.setSystemDefined(true);
			try
			{
				addParameter(parameter);
			}
			catch (JRException e)
			{
				// never reached
			}
		}
	}

	private void addBuiltinVariables()
	{
		for (int i = 0; i < BUILT_IN_VARIABLES.length; ++i)
		{
			JRDesignVariable variable = new JRDesignVariable();
			variable.setName((String) BUILT_IN_VARIABLES[i]);
			variable.setValueClass((Class) BUILT_IN_VARIABLES[++i]);
			variable.setCalculation(JRVariable.CALCULATION_SYSTEM);
			variable.setSystemDefined(true);
			addVariable(variable);
		}
	}
	
	/**
	 * Creates a new crosstab.
	 */
	public JRDesignCrosstab()
	{
		this(null);
	}
	
	
	/**
	 * The ID of the crosstab is only generated at compile time.
	 */
	public int getId()
	{
		return 0;
	}

	public JRCrosstabDataset getDataset()
	{
		return dataset;
	}

	
	/**
	 * Returns the crosstab dataset object to be used for report designing.
	 * 
	 * @return the crosstab dataset design object
	 */
	public JRDesignCrosstabDataset getDesignDataset()
	{
		return dataset;
	}

	public JRCrosstabRowGroup[] getRowGroups()
	{
		JRCrosstabRowGroup[] groups = new JRCrosstabRowGroup[rowGroups.size()];
		rowGroups.toArray(groups);
		return groups;
	}

	public JRCrosstabColumnGroup[] getColumnGroups()
	{
		JRCrosstabColumnGroup[] groups = new JRCrosstabColumnGroup[columnGroups.size()];
		columnGroups.toArray(groups);
		return groups;
	}

	public JRCrosstabMeasure[] getMeasures()
	{
		JRCrosstabMeasure[] measureArray = new JRCrosstabMeasure[measures.size()];
		measures.toArray(measureArray);
		return measureArray;
	}

	public void collectExpressions(JRExpressionCollector collector)
	{
		collector.collect(this);
	}

	public JRChild getCopy(JRAbstractObjectFactory factory)
	{
		return factory.getCrosstab(this);
	}

	public void writeXml(JRXmlWriter writer) throws IOException
	{
		writer.writeCrosstab(this);
	}
	
	
	/**
	 * Sets the crosstab input dataset.
	 * 
	 * @param dataset the dataset
	 * @see JRCrosstab#getDataset()
	 */
	public void setDataset(JRDesignCrosstabDataset dataset)
	{
		this.dataset = dataset;
	}
	
	
	/**
	 * Adds a row group.
	 * <p>
	 * This group will be a sub group of the last row group, if any.
	 * 
	 * @param group the group
	 * @throws JRException
	 * @see JRCrosstab#getRowGroups()
	 */
	public void addRowGroup(JRDesignCrosstabRowGroup group) throws JRException
	{
		String groupName = group.getName();
		if (rowGroupsMap.containsKey(groupName) ||
				columnGroupsMap.containsKey(groupName) ||
				measuresMap.containsKey(groupName))
		{
			throw new JRException("A group or measure having the same name already exists in the crosstab.");
		}
		
		rowGroupsMap.put(groupName, new Integer(rowGroups.size()));
		rowGroups.add(group);
		
		addRowGroupVars(group);
	}

	
	protected void addRowGroupVars(JRDesignCrosstabRowGroup rowGroup)
	{
		addVariable(rowGroup.getVariable());
		
		for (Iterator measureIt = measures.iterator(); measureIt.hasNext();)
		{
			JRCrosstabMeasure measure = (JRCrosstabMeasure) measureIt.next();
			addTotalVar(measure, rowGroup, null);
			
			for (Iterator colIt = columnGroups.iterator(); colIt.hasNext();)
			{
				JRCrosstabColumnGroup colGroup = (JRCrosstabColumnGroup) colIt.next();
				addTotalVar(measure, rowGroup, colGroup);
			}
		}
	}
	
	
	/**
	 * Adds a column group.
	 * <p>
	 * This group will be a sub group of the last column group, if any.
	 * 
	 * @param group the group
	 * @throws JRException
	 * @see JRCrosstab#getColumnGroups()
	 */
	public void addColumnGroup(JRDesignCrosstabColumnGroup group) throws JRException
	{
		String groupName = group.getName();
		if (rowGroupsMap.containsKey(groupName) ||
				columnGroupsMap.containsKey(groupName) ||
				measuresMap.containsKey(groupName))
		{
			throw new JRException("A group or measure having the same name already exists in the crosstab.");
		}
		
		columnGroupsMap.put(groupName, new Integer(columnGroups.size()));
		columnGroups.add(group);
		
		addColGroupVars(group);
	}
	
	
	protected void addColGroupVars(JRDesignCrosstabColumnGroup colGroup)
	{
		addVariable(colGroup.getVariable());
		
		for (Iterator measureIt = measures.iterator(); measureIt.hasNext();)
		{
			JRCrosstabMeasure measure = (JRCrosstabMeasure) measureIt.next();
			addTotalVar(measure, null, colGroup);

			for (Iterator rowIt = rowGroups.iterator(); rowIt.hasNext();)
			{
				JRCrosstabRowGroup rowGroup = (JRCrosstabRowGroup) rowIt.next();
				addTotalVar(measure, rowGroup, colGroup);
			}
		}
	}

	/**
	 * Adds a measure to the crosstab.
	 * 
	 * @param measure the measure
	 * @throws JRException
	 * @see JRCrosstab#getMeasures()
	 */
	public void addMeasure(JRDesignCrosstabMeasure measure) throws JRException
	{
		String measureName = measure.getName();
		if (rowGroupsMap.containsKey(measureName) ||
				columnGroupsMap.containsKey(measureName) ||
				measuresMap.containsKey(measureName))
		{
			throw new JRException("A group or measure having the same name already exists in the crosstab.");
		}
		
		measure.addPropertyChangeListener(JRDesignCrosstabMeasure.PROPERTY_VALUE_CLASS, measureClassChangeListener);
		
		measuresMap.put(measureName, new Integer(measures.size()));
		measures.add(measure);
		
		addMeasureVars(measure);
	}

	protected void addMeasureVars(JRDesignCrosstabMeasure measure)
	{
		addVariable(measure.getVariable());
		
		for (Iterator colIt = columnGroups.iterator(); colIt.hasNext();)
		{
			JRCrosstabColumnGroup colGroup = (JRCrosstabColumnGroup) colIt.next();
			addTotalVar(measure, null, colGroup);
		}
		
		for (Iterator rowIt = rowGroups.iterator(); rowIt.hasNext();)
		{
			JRCrosstabRowGroup rowGroup = (JRCrosstabRowGroup) rowIt.next();
			addTotalVar(measure, rowGroup, null);
			
			for (Iterator colIt = columnGroups.iterator(); colIt.hasNext();)
			{
				JRCrosstabColumnGroup colGroup = (JRCrosstabColumnGroup) colIt.next();
				addTotalVar(measure, rowGroup, colGroup);
			}
		}
	}


	protected void addTotalVar(JRCrosstabMeasure measure, JRCrosstabRowGroup rowGroup, JRCrosstabColumnGroup colGroup)
	{
		JRDesignVariable var = new JRDesignVariable();
		var.setCalculation(JRVariable.CALCULATION_SYSTEM);
		var.setSystemDefined(true);
		var.setName(getTotalVariableName(measure, rowGroup, colGroup));
		var.setValueClassName(measure.getValueClassName());
		addVariable(var);
	}


	protected void removeTotalVar(JRCrosstabMeasure measure, JRCrosstabRowGroup rowGroup, JRCrosstabColumnGroup colGroup)
	{
		String varName = getTotalVariableName(measure, rowGroup, colGroup);
		removeVariable(varName);
	}

	
	public static String getTotalVariableName(JRCrosstabMeasure measure, JRCrosstabRowGroup rowGroup, JRCrosstabColumnGroup colGroup)
	{
		StringBuffer name = new StringBuffer();
		name.append(measure.getName());
		if (rowGroup != null)
		{
			name.append('_');
			name.append(rowGroup.getName());
		}
		if (colGroup != null)
		{
			name.append('_');
			name.append(colGroup.getName());
		}
		name.append("_ALL");
		return name.toString();
	}


	/**
	 * Removes a row group.
	 * 
	 * @param groupName the group name
	 * @return the removed group
	 */
	public JRCrosstabRowGroup removeRowGroup(String groupName)
	{
		JRCrosstabRowGroup removed = null;
		
		Integer idx = (Integer) rowGroupsMap.remove(groupName);
		if (idx != null)
		{
			removed = (JRCrosstabRowGroup) rowGroups.remove(idx.intValue());
			
			for (ListIterator it = rowGroups.listIterator(idx.intValue()); it.hasNext();)
			{
				JRCrosstabRowGroup group = (JRCrosstabRowGroup) it.next();
				rowGroupsMap.put(group.getName(), new Integer(it.previousIndex()));
			}
			
			for (Iterator it = cellsList.iterator(); it.hasNext();)
			{
				JRCrosstabCell cell = (JRCrosstabCell) it.next();
				String rowTotalGroup = cell.getRowTotalGroup();
				if (rowTotalGroup != null && rowTotalGroup.equals(groupName))
				{
					it.remove();
					cellsMap.remove(new Pair(rowTotalGroup, cell.getColumnTotalGroup()));
				}
			}
			
			removeRowGroupVars(removed);
		}
		
		return removed;
	}

	protected void removeRowGroupVars(JRCrosstabRowGroup rowGroup)
	{
		removeVariable(rowGroup.getVariable());
		
		for (Iterator measureIt = measures.iterator(); measureIt.hasNext();)
		{
			JRCrosstabMeasure measure = (JRCrosstabMeasure) measureIt.next();
			removeTotalVar(measure, rowGroup, null);
			
			for (Iterator colIt = columnGroups.iterator(); colIt.hasNext();)
			{
				JRCrosstabColumnGroup colGroup = (JRCrosstabColumnGroup) colIt.next();
				removeTotalVar(measure, rowGroup, colGroup);
			}
		}
	}


	/**
	 * Removes a row group.
	 * 
	 * @param group the group to be removed
	 * @return the removed group
	 */
	public JRCrosstabRowGroup removeRowGroup(JRCrosstabRowGroup group)
	{
		return removeRowGroup(group.getName());
	}
	
	
	/**
	 * Removes a column group.
	 * 
	 * @param groupName the group name
	 * @return the removed group
	 */
	public JRCrosstabColumnGroup removeColumnGroup(String groupName)
	{
		JRCrosstabColumnGroup removed = null;
		
		Integer idx = (Integer) columnGroupsMap.remove(groupName);
		if (idx != null)
		{
			removed = (JRCrosstabColumnGroup) columnGroups.remove(idx.intValue());
			
			for (ListIterator it = columnGroups.listIterator(idx.intValue()); it.hasNext();)
			{
				JRCrosstabColumnGroup group = (JRCrosstabColumnGroup) it.next();
				columnGroupsMap.put(group.getName(), new Integer(it.previousIndex()));
			}
			
			for (Iterator it = cellsList.iterator(); it.hasNext();)
			{
				JRCrosstabCell cell = (JRCrosstabCell) it.next();
				String columnTotalGroup = cell.getColumnTotalGroup();
				if (columnTotalGroup != null && columnTotalGroup.equals(groupName))
				{
					it.remove();
					cellsMap.remove(new Pair(cell.getRowTotalGroup(), columnTotalGroup));
				}
			}
			
			removeColGroupVars(removed);
		}
		
		return removed;
	}

	
	protected void removeColGroupVars(JRCrosstabColumnGroup colGroup)
	{
		removeVariable(colGroup.getVariable());
		
		for (Iterator measureIt = measures.iterator(); measureIt.hasNext();)
		{
			JRCrosstabMeasure measure = (JRCrosstabMeasure) measureIt.next();
			removeTotalVar(measure, null, colGroup);

			for (Iterator rowIt = rowGroups.iterator(); rowIt.hasNext();)
			{
				JRCrosstabRowGroup rowGroup = (JRCrosstabRowGroup) rowIt.next();
				removeTotalVar(measure, rowGroup, colGroup);
			}
		}
	}
	
	
	/**
	 * Removes a column group.
	 * 
	 * @param group the group
	 * @return the removed group
	 */
	public JRCrosstabColumnGroup removeColumnGroup(JRCrosstabColumnGroup group)
	{
		return removeColumnGroup(group.getName());
	}
	
	
	/**
	 * Removes a measure.
	 * 
	 * @param measureName the measure name
	 * @return the removed measure
	 */
	public JRCrosstabMeasure removeMeasure(String measureName)
	{
		JRDesignCrosstabMeasure removed = null;
		
		Integer idx = (Integer) measuresMap.remove(measureName);
		if (idx != null)
		{
			removed = (JRDesignCrosstabMeasure) measures.remove(idx.intValue());
			
			for (ListIterator it = measures.listIterator(idx.intValue()); it.hasNext();)
			{
				JRCrosstabMeasure group = (JRCrosstabMeasure) it.next();
				measuresMap.put(group.getName(), new Integer(it.previousIndex()));
			}
			
			removeMeasureVars(removed);
			
			removed.removePropertyChangeListener(measureClassChangeListener);
		}
		
		return removed;
	}

	protected void removeMeasureVars(JRDesignCrosstabMeasure measure)
	{
		removeVariable(measure.getVariable());
		
		for (Iterator colIt = columnGroups.iterator(); colIt.hasNext();)
		{
			JRCrosstabColumnGroup colGroup = (JRCrosstabColumnGroup) colIt.next();
			removeTotalVar(measure, null, colGroup);
		}
		
		for (Iterator rowIt = rowGroups.iterator(); rowIt.hasNext();)
		{
			JRCrosstabRowGroup rowGroup = (JRCrosstabRowGroup) rowIt.next();
			removeTotalVar(measure, rowGroup, null);
			
			for (Iterator colIt = columnGroups.iterator(); colIt.hasNext();)
			{
				JRCrosstabColumnGroup colGroup = (JRCrosstabColumnGroup) colIt.next();
				removeTotalVar(measure, rowGroup, colGroup);
			}
		}
	}
	
	
	/**
	 * Removes a measure.
	 * 
	 * @param measure the measure
	 * @return the removed measure
	 */
	public JRCrosstabMeasure removeMeasure(JRCrosstabMeasure measure)
	{
		return removeMeasure(measure.getName());
	}

	public boolean isRepeatColumnHeaders()
	{
		return repeatColumnHeaders;
	}

	
	/**
	 * Sets the repeat column headers flag.
	 * 
	 * @param repeatColumnHeaders whether to repeat the column headers on row breaks
	 * @see JRCrosstab#isRepeatColumnHeaders()
	 */
	public void setRepeatColumnHeaders(boolean repeatColumnHeaders)
	{
		this.repeatColumnHeaders = repeatColumnHeaders;
	}

	public boolean isRepeatRowHeaders()
	{
		return repeatRowHeaders;
	}

	
	/**
	 * Sets the repeat row headers flag.
	 * 
	 * @param repeatRowHeaders whether to repeat the row headers on column breaks
	 * @see JRCrosstab#isRepeatRowHeaders()
	 */
	public void setRepeatRowHeaders(boolean repeatRowHeaders)
	{
		this.repeatRowHeaders = repeatRowHeaders;
	}

	public JRCrosstabCell[][] getCells()
	{
		return crossCells;
	}

	
	/**
	 * Returns the data cells list.
	 * 
	 * @return the data cells list
	 * @see #addCell(JRDesignCrosstabCell)
	 */
	public List getCellsList()
	{
		return cellsList;
	}
	
	
	/**
	 * Adds a data cell to the crosstab.
	 * 
	 * @param cell the cell
	 * @throws JRException
	 * @see JRCrosstab#getCells()
	 */
	public void addCell(JRDesignCrosstabCell cell) throws JRException
	{
		String rowTotalGroup = cell.getRowTotalGroup();		
		if (rowTotalGroup != null && !rowGroupsMap.containsKey(rowTotalGroup))
		{
			throw new JRException("Row group " + rowTotalGroup + " does not exist.");
		}
		
		String columnTotalGroup = cell.getColumnTotalGroup();
		if (columnTotalGroup != null && !columnGroupsMap.containsKey(columnTotalGroup))
		{
			throw new JRException("Row group " + columnTotalGroup + " does not exist.");
		}
		
		Object cellKey = new Pair(rowTotalGroup, columnTotalGroup);
		if (cellsMap.containsKey(cellKey))
		{
			throw new JRException("Duplicate cell in crosstab.");
		}
		
		cellsMap.put(cellKey, cell);
		cellsList.add(cell);
	}
	
	
	/**
	 * Removes a data cell.
	 * 
	 * @param rowTotalGroup the cell's total row group 
	 * @param columnTotalGroup the cell's total column group
	 * @return the removed cell
	 */
	public JRCrosstabCell removeCell(String rowTotalGroup, String columnTotalGroup)
	{
		Object cellKey = new Pair(rowTotalGroup, columnTotalGroup);
		
		JRCrosstabCell cell = (JRCrosstabCell) cellsMap.remove(cellKey);
		if (cell != null)
		{
			cellsList.remove(cell);
		}
		
		return cell;
	}
	
	
	/**
	 * Removes a data cell.
	 * 
	 * @param cell the cell to be removed
	 * @return the removed cell
	 */
	public JRCrosstabCell removeCell(JRCrosstabCell cell)
	{
		Object cellKey = new Pair(cell.getRowTotalGroup(), cell.getColumnTotalGroup());
		JRCrosstabCell removedCell = (JRCrosstabCell) cellsMap.remove(cellKey);
		if (removedCell != null)
		{
			cellsList.remove(removedCell);
		}
		
		return removedCell;
	}
	
	private static class Pair implements Serializable
	{
		private static final long serialVersionUID = 1L;
		
		final Object o1;
		final Object o2;
		
		Pair(Object o1, Object o2)
		{
			this.o1 = o1;
			this.o2 = o2;
		}
		
		public boolean equals(Object o)
		{
			if (o == this)
			{
				return true;
			}
			
			if (o == null || !(o instanceof Pair))
			{
				return false;
			}
			
			Pair p = (Pair) o;
			
			return (p.o1 == null ? o1 == null : (o1 != null && p.o1.equals(o1))) &&
				(p.o2 == null ? o2 == null : (o2 != null && p.o2.equals(o2)));
		}
		
		public int hashCode()
		{
			int hash = o1 == null ? 0 : o1.hashCode();
			
			hash *= 31;
			
			hash += o2 == null ? 0 : o2.hashCode();
			
			return hash;
		}
	}

	public JRCrosstabParameter[] getParameters()
	{
		JRCrosstabParameter[] parameters = new JRCrosstabParameter[parametersList.size()];
		parametersList.toArray(parameters);
		return parameters;
	}
	
	
	/**
	 * Returns the paremeters list.
	 * 
	 * @return the paremeters list
	 */
	public List getParametersList()
	{
		return parametersList;
	}
	
	
	/**
	 * Returns the parameters indexed by names.
	 * 
	 * @return the parameters indexed by names
	 */
	public Map getParametersMap()
	{
		return parametersMap;
	}

	public JRExpression getParametersMapExpression()
	{
		return parametersMapExpression;
	}
	
	
	/**
	 * Adds a parameter to the crosstab.
	 * 
	 * @param parameter the parameter
	 * @throws JRException
	 * @see JRCrosstab#getMeasures()
	 */
	public void addParameter(JRCrosstabParameter parameter) throws JRException
	{
		if (parametersMap.containsKey(parameter.getName()))
		{
			if (parametersMap.containsKey(parameter.getName()))
			{
				throw new JRException("Duplicate declaration of parameter : " + parameter.getName());
			}
		}
		
		parametersMap.put(parameter.getName(), parameter);
		parametersList.add(parameter);
	}
	
	
	/**
	 * Removes a parameter.
	 * 
	 * @param parameterName the name of the parameter to be removed
	 * @return the removed parameter
	 */
	public JRCrosstabParameter removeParameter(String parameterName)
	{
		JRCrosstabParameter param = (JRCrosstabParameter) parametersMap.remove(parameterName);
		
		if (param != null)
		{
			parametersList.remove(param);
		}
		
		return param;
	}
	
	
	/**
	 * Removes a parameter.
	 * 
	 * @param parameter the parameter to be removed
	 * @return the removed parameter
	 */
	public JRCrosstabParameter removeParameter(JRCrosstabParameter parameter)
	{
		return removeParameter(parameter.getName());
	}
	
	
	/**
	 * Sets the parameters map expression.
	 * 
	 * @param expression the parameters map expression
	 * @see JRCrosstab#getParametersMapExpression()
	 */
	public void setParametersMapExpression(JRExpression expression)
	{
		this.parametersMapExpression = expression;
	}
	
	
	/**
	 * Returns the variables of this crosstab indexed by name.
	 * 
	 * @return the variables of this crosstab indexed by name
	 */
	public Map getVariablesMap()
	{
		JRVariable[] variables = getVariables();
		Map variablesMap = new HashMap();
		
		for (int i = 0; i < variables.length; i++)
		{
			variablesMap.put(variables[i].getName(), variables[i]);
		}
		
		return variablesMap;
	}
	
	
	/**
	 * Returns the list of variables created for this crosstab.
	 * 
	 * @return the list of variables created for this crosstab
	 * @see JRCrosstabGroup#getVariable()
	 * @see JRCrosstabMeasure#getVariable()
	 * @see JRCrosstab#VARIABLE_ROW_COUNT
	 * @see JRCrosstab#VARIABLE_COLUMN_COUNT
	 */
	public JRVariable[] getVariables()
	{
		JRVariable[] variables = new JRVariable[variablesList.size()];
		variablesList.values().toArray(variables);
		return variables;
	}
	

	public int getColumnBreakOffset()
	{
		return columnBreakOffset;
	}

	
	/**
	 * Sets the column break offset.
	 * 
	 * @param columnBreakOffset the offset
	 * @see JRCrosstab#getColumnBreakOffset()
	 */
	public void setColumnBreakOffset(int columnBreakOffset)
	{
		this.columnBreakOffset = columnBreakOffset;
	}

	
	/**
	 * Performs all the calculations required for report compilation.
	 */
	public void preprocess()
	{
		setGroupVariablesClass(rowGroups);
		setGroupVariablesClass(columnGroups);
		
		calculateSizes();
	}

	
	protected void setGroupVariablesClass(List groups)
	{
		for (Iterator it = groups.iterator(); it.hasNext();)
		{
			JRDesignCrosstabGroup group = (JRDesignCrosstabGroup) it.next();
			JRCrosstabBucket bucket = group.getBucket();
			if (bucket != null)
			{
				JRExpression expression = bucket.getExpression();
				if (expression != null)
				{
					group.designVariable.setValueClassName(expression.getValueClassName());
				}
			}
		}
	}

	protected void calculateSizes()
	{
		setWhenNoDataCellSize();
		
		createCellMatrix();
		
		int rowHeadersWidth = calculateRowHeadersSizes();
		int colHeadersHeight = calculateColumnHeadersSizes();
		
		if (headerCell != null)
		{
			headerCell.setWidth(rowHeadersWidth);
			headerCell.setHeight(colHeadersHeight);
		}
	}

	protected void setWhenNoDataCellSize()
	{
		if (whenNoDataCell != null)
		{
			whenNoDataCell.setWidth(getWidth());
			whenNoDataCell.setHeight(getHeight());
		}
	}

	protected void createCellMatrix()
	{
		crossCells = new JRDesignCrosstabCell[rowGroups.size() + 1][columnGroups.size() + 1];
		for (Iterator it = cellsList.iterator(); it.hasNext();)
		{
			JRDesignCrosstabCell crosstabCell = (JRDesignCrosstabCell) it.next();
			JRDesignCellContents contents = (JRDesignCellContents) crosstabCell.getContents();
			
			String rowTotalGroup = crosstabCell.getRowTotalGroup();
			int rowGroupIndex = rowTotalGroup == null ? rowGroupIndex = rowGroups.size() : ((Integer) rowGroupsMap.get(rowTotalGroup)).intValue();
			
			Integer cellWidth = crosstabCell.getWidth();
			if (cellWidth != null)
			{
				contents.setWidth(cellWidth.intValue());
			}

			String columnTotalGroup = crosstabCell.getColumnTotalGroup();
			int columnGroupIndex = columnTotalGroup == null ? columnGroupIndex = columnGroups.size() : ((Integer) columnGroupsMap.get(columnTotalGroup)).intValue();
			Integer cellHeight = crosstabCell.getHeight();
			if (cellHeight != null)
			{
				contents.setHeight(cellHeight.intValue());
			}

			crossCells[rowGroupIndex][columnGroupIndex] = crosstabCell;
		}
		
		inheritCells();
	}

	protected JRDesignCrosstabRowGroup getRowGroup(int rowGroupIndex)
	{
		return (JRDesignCrosstabRowGroup) rowGroups.get(rowGroupIndex);
	}

	protected JRDesignCrosstabColumnGroup getColumnGroup(int columnGroupIndex)
	{
		return (JRDesignCrosstabColumnGroup) columnGroups.get(columnGroupIndex);
	}

	protected void inheritCells()
	{
		for (int i = rowGroups.size(); i >= 0 ; --i)
		{
			for (int j = columnGroups.size(); j >= 0 ; --j)
			{
				boolean used = (i == rowGroups.size() || getRowGroup(i).hasTotal()) &&
					(j == columnGroups.size() || getColumnGroup(j).hasTotal());
				
				if (used)
				{
					if (crossCells[i][j] == null)
					{
						inheritCell(i, j);
						
						if (crossCells[i][j] == null)
						{
							crossCells[i][j] = emptyCell(i, j);
							inheritCellSize(i, j);
						}
					}
					else
					{
						inheritCellSize(i, j);
					}
				}
				else
				{
					crossCells[i][j] = null;
				}
			}
		}
	}

	private JRDesignCrosstabCell emptyCell(int i, int j)
	{
		JRDesignCrosstabCell emptyCell = new JRDesignCrosstabCell();
		if (i < rowGroups.size())
		{
			emptyCell.setRowTotalGroup(((JRCrosstabRowGroup) rowGroups.get(i)).getName());
		}
		if (j < columnGroups.size())
		{
			emptyCell.setColumnTotalGroup(((JRCrosstabColumnGroup) columnGroups.get(j)).getName());
		}
		return emptyCell;
	}

	protected void inheritCellSize(int i, int j)
	{
		JRDesignCrosstabCell cell = crossCells[i][j];
		
		JRDesignCellContents contents = (JRDesignCellContents) cell.getContents();
		
		if (contents.getWidth() == JRCellContents.NOT_CALCULATED)
		{
			if (i < rowGroups.size())
			{
				JRDesignCrosstabCell rowCell = crossCells[rowGroups.size()][j];
				if (rowCell != null)
				{
					contents.setWidth(rowCell.getContents().getWidth());
				}
			}
			else
			{
				for (int k = j + 1; k <= columnGroups.size(); ++k)
				{
					if (crossCells[i][k] != null)
					{
						contents.setWidth(crossCells[i][k].getContents().getWidth());
						break;
					}
				}
			}
		}
		
		if (contents.getHeight() == JRCellContents.NOT_CALCULATED)
		{
			if (j < columnGroups.size())
			{
				JRDesignCrosstabCell colCell = crossCells[i][columnGroups.size()];
				if (colCell != null)
				{
					contents.setHeight(colCell.getContents().getHeight());
				}
			}
			else
			{
				for (int k = i + 1; k <= rowGroups.size(); ++k)
				{
					if (crossCells[k][j] != null)
					{
						contents.setHeight(crossCells[k][j].getContents().getHeight());
					}
				}
			}
		}
	}

	
	protected void inheritCell(int i, int j)
	{
		JRDesignCrosstabCell inheritedCell = null;
		
		if (j < columnGroups.size())
		{
			JRDesignCrosstabCell colCell = crossCells[rowGroups.size()][j];
			JRDesignCellContents colContents = colCell == null ? null : (JRDesignCellContents) colCell.getContents();
			for (int k = j + 1; inheritedCell == null && k <= columnGroups.size(); ++k)
			{
				JRDesignCrosstabCell cell = crossCells[i][k];
				if (cell != null)
				{
					JRDesignCellContents contents = (JRDesignCellContents) cell.getContents();
					if (colContents == null || contents.getWidth() == colContents.getWidth())
					{
						inheritedCell = cell;
					}
				}
			}
		}
		
		if (inheritedCell == null && i < rowGroups.size())
		{
			JRDesignCrosstabCell rowCell = crossCells[i][columnGroups.size()];
			JRDesignCellContents rowContents = rowCell == null ? null : (JRDesignCellContents) rowCell.getContents();
			for (int k = i + 1; inheritedCell == null && k <= rowGroups.size(); ++k)
			{
				JRDesignCrosstabCell cell = crossCells[k][j];
				if (cell != null)
				{
					JRDesignCellContents contents = (JRDesignCellContents) cell.getContents();
					if (rowContents == null || contents.getHeight() == rowContents.getHeight())
					{
						inheritedCell = cell;
					}
				}
			}
		}
		
		crossCells[i][j] = inheritedCell;
	}

	protected int calculateRowHeadersSizes()
	{
		int widthSum = 0;
		for (int i = rowGroups.size() - 1, heightSum = 0; i >= 0; --i)
		{
			JRDesignCrosstabRowGroup group = (JRDesignCrosstabRowGroup) rowGroups.get(i);

			widthSum += group.getWidth();
			
			JRDesignCrosstabCell cell = crossCells[i + 1][columnGroups.size()];
			if (cell != null)
			{
				heightSum += cell.getContents().getHeight();
			}

			JRDesignCellContents header = (JRDesignCellContents) group.getHeader();
			header.setHeight(heightSum);
			header.setWidth(group.getWidth());

			if (group.hasTotal())
			{
				JRDesignCellContents totalHeader = (JRDesignCellContents) group.getTotalHeader();
				totalHeader.setWidth(widthSum);
				JRDesignCrosstabCell totalCell = crossCells[i][columnGroups.size()];
				if (totalCell != null)
				{
					totalHeader.setHeight(totalCell.getContents().getHeight());
				}
			}
		}
		return widthSum;
	}

	protected int calculateColumnHeadersSizes()
	{
		int heightSum = 0;
		for (int i = columnGroups.size() - 1, widthSum = 0; i >= 0; --i)
		{
			JRDesignCrosstabColumnGroup group = (JRDesignCrosstabColumnGroup) columnGroups.get(i);

			heightSum += group.getHeight();
			JRDesignCrosstabCell cell = crossCells[rowGroups.size()][i + 1];
			if (cell != null)
			{
				widthSum += cell.getContents().getWidth();
			}

			JRDesignCellContents header = (JRDesignCellContents) group.getHeader();
			header.setHeight(group.getHeight());
			header.setWidth(widthSum);

			if (group.hasTotal())
			{
				JRDesignCellContents totalHeader = (JRDesignCellContents) group.getTotalHeader();
				totalHeader.setHeight(heightSum);
				JRDesignCrosstabCell totalCell = crossCells[rowGroups.size()][i];
				if (totalCell != null)
				{
					totalHeader.setWidth(totalCell.getContents().getWidth());
				}
			}
		}
		return heightSum;
	}

	public JRCellContents getWhenNoDataCell()
	{
		return whenNoDataCell;
	}

	
	/**
	 * Sets the "No data" cell.
	 * 
	 * @param whenNoDataCell the cell
	 * @see JRCrosstab#getWhenNoDataCell()
	 */
	public void setWhenNoDataCell(JRDesignCellContents whenNoDataCell)
	{
		this.whenNoDataCell = whenNoDataCell;
	}

	
	public JRElement getElementByKey(String elementKey)
	{
		return JRBaseCrosstab.getElementByKey(this, elementKey);
	}
	
	
	public byte getMode()
	{
		return JRStyleResolver.getMode(this, MODE_TRANSPARENT);
	}

	public JRCellContents getHeaderCell()
	{
		return headerCell;
	}
	
	
	/**
	 * Sets the crosstab header cell (this cell will be rendered at the upper-left corder of the crosstab).
	 * 
	 * @param headerCell the cell
	 * @see JRCrosstab#getHeaderCell()
	 */
	public void setHeaderCell(JRDesignCellContents headerCell)
	{
		this.headerCell = headerCell;
	}

	
	protected void measureClassChanged(JRDesignCrosstabMeasure measure, String valueClassName)
	{
		for (Iterator colIt = columnGroups.iterator(); colIt.hasNext();)
		{
			JRCrosstabColumnGroup colGroup = (JRCrosstabColumnGroup) colIt.next();
			setTotalVarClass(measure, null, colGroup, valueClassName);
		}
		
		for (Iterator rowIt = rowGroups.iterator(); rowIt.hasNext();)
		{
			JRCrosstabRowGroup rowGroup = (JRCrosstabRowGroup) rowIt.next();
			setTotalVarClass(measure, rowGroup, null, valueClassName);
			
			for (Iterator colIt = columnGroups.iterator(); colIt.hasNext();)
			{
				JRCrosstabColumnGroup colGroup = (JRCrosstabColumnGroup) colIt.next();
				setTotalVarClass(measure, rowGroup, colGroup, valueClassName);
			}
		}
	}
	
	protected void setTotalVarClass(JRCrosstabMeasure measure, JRCrosstabRowGroup rowGroup, JRCrosstabColumnGroup colGroup, String valueClassName)
	{
		JRDesignVariable variable = getVariable(getTotalVariableName(measure, rowGroup, colGroup));
		variable.setValueClassName(valueClassName);
	}

	private void addVariable(JRVariable variable)
	{
		variablesList.put(variable.getName(), variable);
	}

	private void removeVariable(JRVariable variable)
	{
		removeVariable(variable.getName());
	}

	private void removeVariable(String varName)
	{
		variablesList.remove(varName);
	}
	
	private JRDesignVariable getVariable(String varName)
	{
		return (JRDesignVariable) variablesList.get(varName);
	}
}
