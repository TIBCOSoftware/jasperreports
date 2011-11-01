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
package net.sf.jasperreports.crosstabs.design;

import java.awt.Color;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.io.ObjectInputStream;
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
import java.util.TimeZone;

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
import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRDefaultStyleProvider;
import net.sf.jasperreports.engine.JRElement;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRExpressionCollector;
import net.sf.jasperreports.engine.JRLineBox;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JRVariable;
import net.sf.jasperreports.engine.JRVisitor;
import net.sf.jasperreports.engine.ReportContext;
import net.sf.jasperreports.engine.base.JRBaseLineBox;
import net.sf.jasperreports.engine.design.JRDesignElement;
import net.sf.jasperreports.engine.design.JRDesignVariable;
import net.sf.jasperreports.engine.type.CalculationEnum;
import net.sf.jasperreports.engine.type.ModeEnum;
import net.sf.jasperreports.engine.type.RunDirectionEnum;
import net.sf.jasperreports.engine.util.FileResolver;
import net.sf.jasperreports.engine.util.FormatFactory;
import net.sf.jasperreports.engine.util.JRCloneUtils;
import net.sf.jasperreports.engine.util.JRStyleResolver;
import net.sf.jasperreports.engine.util.Pair;

import org.apache.commons.collections.SequencedHashMap;

/**
 * Design-time {@link net.sf.jasperreports.crosstabs.JRCrosstab crosstab} implementation.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public class JRDesignCrosstab extends JRDesignElement implements JRCrosstab
{
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;
	
	public static final String PROPERTY_COLUMN_BREAK_OFFSET = "columnBreakOffset";
	
	public static final String PROPERTY_DATASET = "dataset";
	
	public static final String PROPERTY_HEADER_CELL = "headerCell";
	
	public static final String PROPERTY_PARAMETERS_MAP_EXPRESSION = "parametersMapExpression";
	
	public static final String PROPERTY_REPEAT_COLUMN_HEADERS = "repeatColumnHeaders";
	
	public static final String PROPERTY_REPEAT_ROW_HEADERS = "repeatRowHeaders";
	
	public static final String PROPERTY_WHEN_NO_DATA_CELL = "whenNoDataCell";
	
	public static final String PROPERTY_CELLS = "cells";
	
	public static final String PROPERTY_ROW_GROUPS = "rowGroups";
	
	public static final String PROPERTY_COLUMN_GROUPS = "columnGroups";
	
	public static final String PROPERTY_MEASURES = "measures";
	
	public static final String PROPERTY_PARAMETERS = "parameters";
	
	public static final String PROPERTY_IGNORE_WIDTH = "ignoreWidth";

	protected List<JRCrosstabParameter> parametersList;
	protected Map<String, JRCrosstabParameter> parametersMap;
	protected SequencedHashMap variablesList;
	protected JRExpression parametersMapExpression;
	protected JRDesignCrosstabDataset dataset;
	protected List<JRCrosstabRowGroup> rowGroups;
	protected List<JRCrosstabColumnGroup> columnGroups;
	protected List<JRCrosstabMeasure> measures;
	protected Map<String, Integer> rowGroupsMap;
	protected Map<String, Integer> columnGroupsMap;
	protected Map<String, Integer> measuresMap;
	protected int columnBreakOffset = DEFAULT_COLUMN_BREAK_OFFSET;
	protected boolean repeatColumnHeaders = true;
	protected boolean repeatRowHeaders = true;
	protected RunDirectionEnum runDirectionValue;
	protected List<JRCrosstabCell> cellsList;
	protected Map<Pair,JRCrosstabCell> cellsMap;
	protected JRDesignCrosstabCell[][] crossCells;
	protected JRDesignCellContents whenNoDataCell;
	protected JRDesignCellContents headerCell;
	protected Boolean ignoreWidth;
	protected JRLineBox lineBox;
	
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
		JRParameter.REPORT_CONTEXT, ReportContext.class, 
		JRParameter.REPORT_PARAMETERS_MAP, java.util.Map.class, 
		JRParameter.REPORT_LOCALE, Locale.class, 
		JRParameter.REPORT_RESOURCE_BUNDLE, ResourceBundle.class,
		JRParameter.REPORT_TIME_ZONE, TimeZone.class, 
		JRParameter.REPORT_FORMAT_FACTORY, FormatFactory.class, 
		JRParameter.REPORT_CLASS_LOADER, ClassLoader.class,
		JRParameter.REPORT_URL_HANDLER_FACTORY, URLStreamHandlerFactory.class,
		JRParameter.REPORT_FILE_RESOLVER, FileResolver.class};
	
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
		
		parametersList = new ArrayList<JRCrosstabParameter>();
		parametersMap = new HashMap<String, JRCrosstabParameter>();
		rowGroupsMap = new HashMap<String, Integer>();
		rowGroups = new ArrayList<JRCrosstabRowGroup>();
		columnGroupsMap = new HashMap<String, Integer>();
		columnGroups = new ArrayList<JRCrosstabColumnGroup>();
		measuresMap = new HashMap<String, Integer>();
		measures = new ArrayList<JRCrosstabMeasure>();
		
		cellsMap = new HashMap<Pair,JRCrosstabCell>();
		cellsList = new ArrayList<JRCrosstabCell>();
		
		addBuiltinParameters();
		
		variablesList = new SequencedHashMap();
		addBuiltinVariables();
		
		dataset = new JRDesignCrosstabDataset();
		lineBox = new JRBaseLineBox(this);
	}

	private void addBuiltinParameters()
	{
		for (int i = 0; i < BUILT_IN_PARAMETERS.length; i++)
		{
			JRDesignCrosstabParameter parameter = new JRDesignCrosstabParameter();
			parameter.setName((String) BUILT_IN_PARAMETERS[i++]);
			parameter.setValueClass((Class<?>) BUILT_IN_PARAMETERS[i]);
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
			variable.setValueClass((Class<?>) BUILT_IN_VARIABLES[++i]);
			variable.setCalculation(CalculationEnum.SYSTEM);
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

	/**
	 *
	 */
	public void visit(JRVisitor visitor)
	{
		visitor.visitCrosstab(this);
	}

	
	/**
	 * Sets the crosstab input dataset.
	 * 
	 * @param dataset the dataset
	 * @see JRCrosstab#getDataset()
	 */
	public void setDataset(JRDesignCrosstabDataset dataset)
	{
		Object old = this.dataset;
		this.dataset = dataset;
		getEventSupport().firePropertyChange(PROPERTY_DATASET, old, this.dataset);
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
		
		rowGroupsMap.put(groupName, Integer.valueOf(rowGroups.size()));
		rowGroups.add(group);
		
		addRowGroupVars(group);
		
		setParent(group);
		
		getEventSupport().fireCollectionElementAddedEvent(PROPERTY_ROW_GROUPS, group, rowGroups.size() - 1);
	}
	
	/**
	 * Adds a row group.
	 * <p>
	 * This group will be a sub group of the last row group, if any.
	 * 
	 * @param group the group
	 * @param index position
	 * @throws JRException
	 * @see JRCrosstab#getRowGroups()
	 */
	public void addRowGroup(int index, JRDesignCrosstabRowGroup group) throws JRException
	{
		String groupName = group.getName();
		if (rowGroupsMap.containsKey(groupName) ||
				columnGroupsMap.containsKey(groupName) ||
				measuresMap.containsKey(groupName))
		{
			throw new JRException("A group or measure having the same name already exists in the crosstab.");
		}
		
		rowGroupsMap.put(groupName, Integer.valueOf(rowGroups.size()));
		rowGroups.add(index, group);
		
		addRowGroupVars(group);
		
		setParent(group);
		
		getEventSupport().fireCollectionElementAddedEvent(PROPERTY_ROW_GROUPS, group, index);
	}

	
	protected void addRowGroupVars(JRDesignCrosstabRowGroup rowGroup)
	{
		addVariable(rowGroup.getVariable());
		
		for (Iterator<JRCrosstabMeasure> measureIt = measures.iterator(); measureIt.hasNext();)
		{
			JRCrosstabMeasure measure = measureIt.next();
			addTotalVar(measure, rowGroup, null);
			
			for (Iterator<JRCrosstabColumnGroup> colIt = columnGroups.iterator(); colIt.hasNext();)
			{
				JRCrosstabColumnGroup colGroup = colIt.next();
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
		
		columnGroupsMap.put(groupName, Integer.valueOf(columnGroups.size()));
		columnGroups.add(group);
		
		addColGroupVars(group);
		
		setParent(group);
		
		getEventSupport().fireCollectionElementAddedEvent(PROPERTY_COLUMN_GROUPS, group, columnGroups.size() - 1);
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
	public void addColumnGroup(int index, JRDesignCrosstabColumnGroup group) throws JRException
	{
		String groupName = group.getName();
		if (rowGroupsMap.containsKey(groupName) ||
				columnGroupsMap.containsKey(groupName) ||
				measuresMap.containsKey(groupName))
		{
			throw new JRException("A group or measure having the same name already exists in the crosstab.");
		}
		
		columnGroupsMap.put(groupName, Integer.valueOf(columnGroups.size()));
		columnGroups.add(index, group);
		
		addColGroupVars(group);
		
		setParent(group);
		
		getEventSupport().fireCollectionElementAddedEvent(PROPERTY_COLUMN_GROUPS, group, index);
	}
	
	
	protected void addColGroupVars(JRDesignCrosstabColumnGroup colGroup)
	{
		addVariable(colGroup.getVariable());
		
		for (Iterator<JRCrosstabMeasure> measureIt = measures.iterator(); measureIt.hasNext();)
		{
			JRCrosstabMeasure measure = measureIt.next();
			addTotalVar(measure, null, colGroup);

			for (Iterator<JRCrosstabRowGroup> rowIt = rowGroups.iterator(); rowIt.hasNext();)
			{
				JRCrosstabRowGroup rowGroup = rowIt.next();
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
		
		measuresMap.put(measureName, Integer.valueOf(measures.size()));
		measures.add(measure);
		
		addMeasureVars(measure);
		
		getEventSupport().fireCollectionElementAddedEvent(PROPERTY_MEASURES, measure, measures.size() - 1);
	}
	
	/**
	 * Adds a measure to the crosstab.
	 * 
	 * @param measure the measure
	 * @throws JRException
	 * @see JRCrosstab#getMeasures()
	 */
	public void addMeasure(int index, JRDesignCrosstabMeasure measure) throws JRException
	{
		String measureName = measure.getName();
		if (rowGroupsMap.containsKey(measureName) ||
				columnGroupsMap.containsKey(measureName) ||
				measuresMap.containsKey(measureName))
		{
			throw new JRException("A group or measure having the same name already exists in the crosstab.");
		}
		
		measure.addPropertyChangeListener(JRDesignCrosstabMeasure.PROPERTY_VALUE_CLASS, measureClassChangeListener);
		
		measuresMap.put(measureName, Integer.valueOf(measures.size()));
		measures.add(index, measure);
		
		addMeasureVars(measure);
		
		getEventSupport().fireCollectionElementAddedEvent(PROPERTY_MEASURES, measure, index);
	}

	protected void addMeasureVars(JRDesignCrosstabMeasure measure)
	{
		addVariable(measure.getVariable());
		
		for (Iterator<JRCrosstabColumnGroup> colIt = columnGroups.iterator(); colIt.hasNext();)
		{
			JRCrosstabColumnGroup colGroup = colIt.next();
			addTotalVar(measure, null, colGroup);
		}
		
		for (Iterator<JRCrosstabRowGroup> rowIt = rowGroups.iterator(); rowIt.hasNext();)
		{
			JRCrosstabRowGroup rowGroup = rowIt.next();
			addTotalVar(measure, rowGroup, null);
			
			for (Iterator<JRCrosstabColumnGroup> colIt = columnGroups.iterator(); colIt.hasNext();)
			{
				JRCrosstabColumnGroup colGroup = colIt.next();
				addTotalVar(measure, rowGroup, colGroup);
			}
		}
	}


	protected void addTotalVar(JRCrosstabMeasure measure, JRCrosstabRowGroup rowGroup, JRCrosstabColumnGroup colGroup)
	{
		JRDesignVariable var = new JRDesignVariable();
		var.setCalculation(CalculationEnum.SYSTEM);
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
		
		Integer idx = rowGroupsMap.remove(groupName);
		if (idx != null)
		{
			removed = rowGroups.remove(idx.intValue());
			
			for (ListIterator<JRCrosstabRowGroup> it = rowGroups.listIterator(idx.intValue()); it.hasNext();)
			{
				JRCrosstabRowGroup group = it.next();
				rowGroupsMap.put(group.getName(), Integer.valueOf(it.previousIndex()));
			}
			
			for (Iterator<JRCrosstabCell> it = cellsList.iterator(); it.hasNext();)
			{
				JRCrosstabCell cell = it.next();
				String rowTotalGroup = cell.getRowTotalGroup();
				if (rowTotalGroup != null && rowTotalGroup.equals(groupName))
				{
					it.remove();
					cellsMap.remove(new Pair(rowTotalGroup, cell.getColumnTotalGroup()));
					getEventSupport().fireCollectionElementRemovedEvent(PROPERTY_CELLS, cell, -1);
				}
			}
			
			removeRowGroupVars(removed);
			
			getEventSupport().fireCollectionElementRemovedEvent(PROPERTY_ROW_GROUPS, removed, idx.intValue());
		}
		
		return removed;
	}

	protected void removeRowGroupVars(JRCrosstabRowGroup rowGroup)
	{
		removeVariable(rowGroup.getVariable());
		
		for (Iterator<JRCrosstabMeasure> measureIt = measures.iterator(); measureIt.hasNext();)
		{
			JRCrosstabMeasure measure = measureIt.next();
			removeTotalVar(measure, rowGroup, null);
			
			for (Iterator<JRCrosstabColumnGroup> colIt = columnGroups.iterator(); colIt.hasNext();)
			{
				JRCrosstabColumnGroup colGroup = colIt.next();
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
		
		Integer idx = columnGroupsMap.remove(groupName);
		if (idx != null)
		{
			removed = columnGroups.remove(idx.intValue());
			
			for (ListIterator<JRCrosstabColumnGroup> it = columnGroups.listIterator(idx.intValue()); it.hasNext();)
			{
				JRCrosstabColumnGroup group = it.next();
				columnGroupsMap.put(group.getName(), Integer.valueOf(it.previousIndex()));
			}
			
			for (Iterator<JRCrosstabCell> it = cellsList.iterator(); it.hasNext();)
			{
				JRCrosstabCell cell = it.next();
				String columnTotalGroup = cell.getColumnTotalGroup();
				if (columnTotalGroup != null && columnTotalGroup.equals(groupName))
				{
					it.remove();
					cellsMap.remove(new Pair(cell.getRowTotalGroup(), columnTotalGroup));
					getEventSupport().fireCollectionElementRemovedEvent(PROPERTY_CELLS, cell, -1);
				}
			}
			
			removeColGroupVars(removed);
			
			getEventSupport().fireCollectionElementRemovedEvent(PROPERTY_COLUMN_GROUPS, removed, idx.intValue());
		}
		
		return removed;
	}

	
	protected void removeColGroupVars(JRCrosstabColumnGroup colGroup)
	{
		removeVariable(colGroup.getVariable());
		
		for (Iterator<JRCrosstabMeasure> measureIt = measures.iterator(); measureIt.hasNext();)
		{
			JRCrosstabMeasure measure = measureIt.next();
			removeTotalVar(measure, null, colGroup);

			for (Iterator<JRCrosstabRowGroup> rowIt = rowGroups.iterator(); rowIt.hasNext();)
			{
				JRCrosstabRowGroup rowGroup = rowIt.next();
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
		
		Integer idx = measuresMap.remove(measureName);
		if (idx != null)
		{
			removed = (JRDesignCrosstabMeasure) measures.remove(idx.intValue());
			
			for (ListIterator<JRCrosstabMeasure> it = measures.listIterator(idx.intValue()); it.hasNext();)
			{
				JRCrosstabMeasure group = it.next();
				measuresMap.put(group.getName(), Integer.valueOf(it.previousIndex()));
			}
			
			removeMeasureVars(removed);
			
			removed.removePropertyChangeListener(JRDesignCrosstabMeasure.PROPERTY_VALUE_CLASS, measureClassChangeListener);
			
			getEventSupport().fireCollectionElementRemovedEvent(PROPERTY_MEASURES, removed, idx.intValue());
		}
		
		return removed;
	}

	protected void removeMeasureVars(JRDesignCrosstabMeasure measure)
	{
		removeVariable(measure.getVariable());
		
		for (Iterator<JRCrosstabColumnGroup> colIt = columnGroups.iterator(); colIt.hasNext();)
		{
			JRCrosstabColumnGroup colGroup = colIt.next();
			removeTotalVar(measure, null, colGroup);
		}
		
		for (Iterator<JRCrosstabRowGroup> rowIt = rowGroups.iterator(); rowIt.hasNext();)
		{
			JRCrosstabRowGroup rowGroup = rowIt.next();
			removeTotalVar(measure, rowGroup, null);
			
			for (Iterator<JRCrosstabColumnGroup> colIt = columnGroups.iterator(); colIt.hasNext();)
			{
				JRCrosstabColumnGroup colGroup = colIt.next();
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
		boolean old = this.repeatColumnHeaders;
		this.repeatColumnHeaders = repeatColumnHeaders;
		getEventSupport().firePropertyChange(PROPERTY_REPEAT_COLUMN_HEADERS, old, this.repeatColumnHeaders);
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
		boolean old = this.repeatRowHeaders;
		this.repeatRowHeaders = repeatRowHeaders;
		getEventSupport().firePropertyChange(PROPERTY_REPEAT_ROW_HEADERS, old, this.repeatRowHeaders);
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
	public List<JRCrosstabCell> getCellsList()
	{
		return cellsList;
	}
	
	
	/**
	 * Returns the crosstab cells indexed by corresponding row total group/
	 * column total group {@link Pair pairs}.
	 * 
	 * @return the crosstab cells indexed by row/column total groups
	 * @see JRCrosstabCell#getRowTotalGroup()
	 * @see JRCrosstabCell#getColumnTotalGroup()
	 */
	public Map<Pair,JRCrosstabCell> getCellsMap()
	{
		return cellsMap;
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
		
		Pair cellKey = new Pair(rowTotalGroup, columnTotalGroup);
		if (cellsMap.containsKey(cellKey))
		{
			throw new JRException("Duplicate cell in crosstab.");
		}
		
		cellsMap.put(cellKey, cell);
		cellsList.add(cell);
		
		setCellOrigin(cell.getContents(),
				new JRCrosstabOrigin(this, JRCrosstabOrigin.TYPE_DATA_CELL,
						rowTotalGroup, columnTotalGroup));
		
		getEventSupport().fireCollectionElementAddedEvent(PROPERTY_CELLS, cell, cellsList.size() - 1);
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
		
		JRCrosstabCell cell = cellsMap.remove(cellKey);
		if (cell != null)
		{
			cellsList.remove(cell);
			getEventSupport().fireCollectionElementRemovedEvent(PROPERTY_CELLS, cell, -1);
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
		return removeCell(cell.getRowTotalGroup(), cell.getColumnTotalGroup());
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
	public List<JRCrosstabParameter> getParametersList()
	{
		return parametersList;
	}
	
	
	/**
	 * Returns the parameters indexed by names.
	 * 
	 * @return the parameters indexed by names
	 */
	public Map<String, JRCrosstabParameter> getParametersMap()
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
		
		getEventSupport().fireCollectionElementAddedEvent(PROPERTY_PARAMETERS, parameter, parametersList.size() - 1);
	}
	
	/**
	 * Adds a parameter to the crosstab.
	 * 
	 * @param parameter the parameter
	 * @throws JRException
	 * @see JRCrosstab#getMeasures()
	 */
	public void addParameter(int index, JRCrosstabParameter parameter) throws JRException
	{
		if (parametersMap.containsKey(parameter.getName()))
		{
			if (parametersMap.containsKey(parameter.getName()))
			{
				throw new JRException("Duplicate declaration of parameter : " + parameter.getName());
			}
		}
		
		parametersMap.put(parameter.getName(), parameter);
		parametersList.add(index, parameter);
		
		getEventSupport().fireCollectionElementAddedEvent(PROPERTY_PARAMETERS, parameter, index);
	}
	
	
	/**
	 * Removes a parameter.
	 * 
	 * @param parameterName the name of the parameter to be removed
	 * @return the removed parameter
	 */
	public JRCrosstabParameter removeParameter(String parameterName)
	{
		JRCrosstabParameter param = parametersMap.remove(parameterName);
		
		if (param != null)
		{
			int idx = parametersList.indexOf(param);
			if (idx >= 0)
			{
				parametersList.remove(idx);
			}
			getEventSupport().fireCollectionElementRemovedEvent(PROPERTY_PARAMETERS, param, idx);
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
		Object old = this.parametersMapExpression;
		this.parametersMapExpression = expression;
		getEventSupport().firePropertyChange(PROPERTY_PARAMETERS_MAP_EXPRESSION, old, this.parametersMapExpression);
	}
	
	
	/**
	 * Returns the variables of this crosstab indexed by name.
	 * 
	 * @return the variables of this crosstab indexed by name
	 */
	public Map<String, JRVariable> getVariablesMap()
	{
		JRVariable[] variables = getVariables();
		Map<String, JRVariable> variablesMap = new HashMap<String, JRVariable>();
		
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
		int old = this.columnBreakOffset;
		this.columnBreakOffset = columnBreakOffset;
		getEventSupport().firePropertyChange(PROPERTY_COLUMN_BREAK_OFFSET, old, this.columnBreakOffset);
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

	
	protected <T> void setGroupVariablesClass(List<T> groups)
	{
		for (Iterator<T> it = groups.iterator(); it.hasNext();)
		{
			JRDesignCrosstabGroup group = (JRDesignCrosstabGroup) it.next();
			JRCrosstabBucket bucket = group.getBucket();
			if (bucket != null)
			{
				JRExpression expression = bucket.getExpression();
				if (expression != null)
				{
					group.designVariable.setValueClassName(bucket.getValueClassName());
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
		for (Iterator<JRCrosstabCell> it = cellsList.iterator(); it.hasNext();)
		{
			JRDesignCrosstabCell crosstabCell = (JRDesignCrosstabCell) it.next();
			JRDesignCellContents contents = (JRDesignCellContents) crosstabCell.getContents();
			
			String rowTotalGroup = crosstabCell.getRowTotalGroup();
			int rowGroupIndex = rowTotalGroup == null ? rowGroups.size() : (rowGroupsMap.get(rowTotalGroup)).intValue();
			
			Integer cellWidth = crosstabCell.getWidth();
			if (cellWidth != null)
			{
				contents.setWidth(cellWidth.intValue());
			}

			String columnTotalGroup = crosstabCell.getColumnTotalGroup();
			int columnGroupIndex = columnTotalGroup == null ? columnGroups.size() : (columnGroupsMap.get(columnTotalGroup)).intValue();
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
			emptyCell.setRowTotalGroup((rowGroups.get(i)).getName());
		}
		if (j < columnGroups.size())
		{
			emptyCell.setColumnTotalGroup((columnGroups.get(j)).getName());
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
		Object old = this.whenNoDataCell;
		this.whenNoDataCell = whenNoDataCell;
		setCellOrigin(this.whenNoDataCell, new JRCrosstabOrigin(this, JRCrosstabOrigin.TYPE_WHEN_NO_DATA_CELL));
		getEventSupport().firePropertyChange(PROPERTY_WHEN_NO_DATA_CELL, old, this.whenNoDataCell);
	}

	
	public JRElement getElementByKey(String elementKey)
	{
		return JRBaseCrosstab.getElementByKey(this, elementKey);
	}
	
	
	public ModeEnum getModeValue()
	{
		return JRStyleResolver.getMode(this, ModeEnum.TRANSPARENT);
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
		Object old = this.headerCell;
		this.headerCell = headerCell;
		setCellOrigin(this.headerCell, new JRCrosstabOrigin(this, JRCrosstabOrigin.TYPE_HEADER_CELL));
		getEventSupport().firePropertyChange(PROPERTY_HEADER_CELL, old, this.headerCell);
	}

	
	protected void measureClassChanged(JRDesignCrosstabMeasure measure, String valueClassName)
	{
		for (Iterator<JRCrosstabColumnGroup> colIt = columnGroups.iterator(); colIt.hasNext();)
		{
			JRCrosstabColumnGroup colGroup = colIt.next();
			setTotalVarClass(measure, null, colGroup, valueClassName);
		}
		
		for (Iterator<JRCrosstabRowGroup> rowIt = rowGroups.iterator(); rowIt.hasNext();)
		{
			JRCrosstabRowGroup rowGroup = rowIt.next();
			setTotalVarClass(measure, rowGroup, null, valueClassName);
			
			for (Iterator<JRCrosstabColumnGroup> colIt = columnGroups.iterator(); colIt.hasNext();)
			{
				JRCrosstabColumnGroup colGroup = colIt.next();
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
	
	
	/**
	 *
	 */
	public RunDirectionEnum getRunDirectionValue()
	{
		return this.runDirectionValue;
	}

	/**
	 *
	 */
	public void setRunDirection(RunDirectionEnum runDirectionValue)
	{
		RunDirectionEnum old = this.runDirectionValue;
		this.runDirectionValue = runDirectionValue;
		getEventSupport().firePropertyChange(JRBaseCrosstab.PROPERTY_RUN_DIRECTION, old, this.runDirectionValue);
	}

	protected void setCellOrigin(JRCellContents cell, JRCrosstabOrigin origin)
	{
		if (cell instanceof JRDesignCellContents)
		{
			setCellOrigin((JRDesignCellContents) cell, origin);
		}
	}
	
	protected void setCellOrigin(JRDesignCellContents cell, JRCrosstabOrigin origin)
	{
		if (cell != null)
		{
			cell.setOrigin(origin);
		}
	}
	
	protected void setParent(JRDesignCrosstabGroup group)
	{
		if (group != null)
		{
			group.setParent(this);
		}
	}
	
	/**
	 * 
	 */
	public Object clone() 
	{
		JRDesignCrosstab clone = (JRDesignCrosstab)super.clone();
		
		if (parametersList != null)
		{
			clone.parametersList = new ArrayList<JRCrosstabParameter>(parametersList.size());
			clone.parametersMap = new HashMap<String, JRCrosstabParameter>(parametersList.size());
			for(int i = 0; i < parametersList.size(); i++)
			{
				JRCrosstabParameter parameter = JRCloneUtils.nullSafeClone(parametersList.get(i));
				clone.parametersList.add(parameter);
				clone.parametersMap.put(parameter.getName(), parameter);
			}
		}
		
		clone.parametersMapExpression = JRCloneUtils.nullSafeClone(parametersMapExpression);
		clone.dataset = JRCloneUtils.nullSafeClone(dataset);
		clone.lineBox = lineBox.clone(clone);
		
		// keep group and measure cloned variables to reuse the clone instances
		// in the variables list
		Map<JRVariable,JRVariable> clonedVariables = new HashMap<JRVariable,JRVariable>();
		
		if (rowGroups != null)
		{
			clone.rowGroups = new ArrayList<JRCrosstabRowGroup>(rowGroups.size());
			clone.rowGroupsMap = new HashMap<String, Integer>(rowGroups.size());
			for(int i = 0; i < rowGroups.size(); i++)
			{
				JRDesignCrosstabRowGroup group = 
					(JRDesignCrosstabRowGroup) rowGroups.get(i);
				JRDesignCrosstabRowGroup groupClone = 
					(JRDesignCrosstabRowGroup) group.clone(clone);
				clone.rowGroups.add(groupClone);
				clone.rowGroupsMap.put(groupClone.getName(), Integer.valueOf(i));

				adjustCrosstabReference(clone, (JRDesignCellContents) groupClone.getTotalHeader());
				adjustCrosstabReference(clone, (JRDesignCellContents) groupClone.getHeader());
				
				if (group.designVariable != null)
				{
					clonedVariables.put(group.designVariable, groupClone.designVariable);
				}
			}
		}
		
		if (columnGroups != null)
		{
			clone.columnGroups = new ArrayList<JRCrosstabColumnGroup>(columnGroups.size());
			clone.columnGroupsMap = new HashMap<String, Integer>(columnGroups.size());
			for(int i = 0; i < columnGroups.size(); i++)
			{
				JRDesignCrosstabColumnGroup group = 
					(JRDesignCrosstabColumnGroup) columnGroups.get(i);
				JRDesignCrosstabColumnGroup groupClone = 
					(JRDesignCrosstabColumnGroup) group.clone(clone);
				clone.columnGroups.add(groupClone);
				clone.columnGroupsMap.put(groupClone.getName(), Integer.valueOf(i));
				
				adjustCrosstabReference(clone,(JRDesignCellContents) groupClone.getTotalHeader());
				adjustCrosstabReference(clone,(JRDesignCellContents) groupClone.getHeader());

				if (group.designVariable != null)
				{
					clonedVariables.put(group.designVariable, groupClone.designVariable);
				}
			}
		}
		
		if (measures != null)
		{
			clone.measures = new ArrayList<JRCrosstabMeasure>(measures.size());
			clone.measuresMap = new HashMap<String,Integer>(measures.size());
			for(int i = 0; i < measures.size(); i++)
			{
				JRDesignCrosstabMeasure measure = 
					(JRDesignCrosstabMeasure) measures.get(i);
				JRDesignCrosstabMeasure clonedMeasure = JRCloneUtils.nullSafeClone(measure);
				clone.measures.add(clonedMeasure);
				clone.measuresMap.put(clonedMeasure.getName(), Integer.valueOf(i));
				
				if (clonedMeasure.designVariable != null)
				{
					clonedVariables.put(measure.designVariable, 
							clonedMeasure.designVariable);
				}
			}
		}
		
		if (variablesList != null)
		{
			clone.variablesList = new SequencedHashMap(variablesList.size());
			for(Iterator<?> it = variablesList.values().iterator(); it.hasNext();)
			{
				JRVariable variable = (JRVariable) it.next();
				// check whether the variable was already cloned as part of a group or measure
				JRVariable variableClone = clonedVariables.get(variable);
				variableClone = JRCloneUtils.nullSafeClone(variable);
				clone.variablesList.put(variableClone.getName(), variableClone);
			}
		}
		
		if (cellsList != null)
		{
			clone.cellsList = new ArrayList<JRCrosstabCell>(cellsList.size());
			clone.cellsMap = new HashMap<Pair,JRCrosstabCell>(cellsList.size());
			for(int i = 0; i < cellsList.size(); i++)
			{
				JRCrosstabCell cell = JRCloneUtils.nullSafeClone(cellsList.get(i));
				adjustCrosstabReference(clone, (JRDesignCellContents) cell.getContents());
				clone.cellsList.add(cell);
				clone.cellsMap.put(new Pair(cell.getRowTotalGroup(), cell.getColumnTotalGroup()), cell);
			}
		}
		
		// clone not preprocessed
		clone.crossCells = null;
		
		clone.whenNoDataCell = JRCloneUtils.nullSafeClone(whenNoDataCell);
		adjustCrosstabReference(clone, clone.whenNoDataCell);
		clone.headerCell = JRCloneUtils.nullSafeClone(headerCell);
		adjustCrosstabReference(clone, clone.headerCell);

		return clone;
	}

	/**
	 * Adjust the crosstab reference inside the origin to point to this
	 * crosstab. Used in the clone method.
	 * @param contents
	 */
	private void adjustCrosstabReference(JRDesignCrosstab clone, JRDesignCellContents contents)
	{
		if (contents == null)
		{
			return;
		}
		
		contents.setOrigin(
			new JRCrosstabOrigin(
				clone,
				contents.getOrigin().getType(),
				contents.getOrigin().getRowGroupName(),
				contents.getOrigin().getColumnGroupName()
				)
			);
	}
	
	public List<JRCrosstabRowGroup> getRowGroupsList()
	{
		return rowGroups;
	}
	
	public Map<String, Integer> getRowGroupIndicesMap()
	{
		return rowGroupsMap;
	}
	
	public List<JRCrosstabColumnGroup> getColumnGroupsList()
	{
		return columnGroups;
	}
	
	public Map<String, Integer> getColumnGroupIndicesMap()
	{
		return columnGroupsMap;
	}
	
	public List<JRCrosstabMeasure> getMesuresList()
	{
		return measures;
	}
	
	public Map<String, Integer> getMeasureIndicesMap()
	{
		return measuresMap;
	}

	public Boolean getIgnoreWidth()
	{
		return ignoreWidth;
	}

	public void setIgnoreWidth(Boolean ignoreWidth)
	{
		Object old = this.ignoreWidth;
		this.ignoreWidth = ignoreWidth;
		getEventSupport().firePropertyChange(PROPERTY_IGNORE_WIDTH, 
				old, this.ignoreWidth);
	}

	public void setIgnoreWidth(boolean ignoreWidth)
	{
		setIgnoreWidth(Boolean.valueOf(ignoreWidth));
	}

	public Color getDefaultLineColor()
	{
		return getForecolor();
	}

	public JRLineBox getLineBox()
	{
		return lineBox;
	}

	/*
	 * These fields are only for serialization backward compatibility.
	 */
	private int PSEUDO_SERIAL_VERSION_UID = JRConstants.PSEUDO_SERIAL_VERSION_UID; //NOPMD
	/**
	 * @deprecated
	 */
	private byte runDirection;
	
	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException
	{
		in.defaultReadObject();

		if (PSEUDO_SERIAL_VERSION_UID < JRConstants.PSEUDO_SERIAL_VERSION_UID_3_7_2)
		{
			runDirectionValue = RunDirectionEnum.getByValue(runDirection);
		}
		
		if (lineBox == null)
		{
			lineBox = new JRBaseLineBox(this);
		}
	}

	
}
