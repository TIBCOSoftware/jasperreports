/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2014 TIBCO Software Inc. All rights reserved.
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
package net.sf.jasperreports.crosstabs.base;

import java.awt.Color;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Iterator;
import java.util.List;

import net.sf.jasperreports.crosstabs.CrosstabColumnCell;
import net.sf.jasperreports.crosstabs.CrosstabDeepVisitor;
import net.sf.jasperreports.crosstabs.JRCellContents;
import net.sf.jasperreports.crosstabs.JRCrosstab;
import net.sf.jasperreports.crosstabs.JRCrosstabCell;
import net.sf.jasperreports.crosstabs.JRCrosstabColumnGroup;
import net.sf.jasperreports.crosstabs.JRCrosstabDataset;
import net.sf.jasperreports.crosstabs.JRCrosstabGroup;
import net.sf.jasperreports.crosstabs.JRCrosstabMeasure;
import net.sf.jasperreports.crosstabs.JRCrosstabParameter;
import net.sf.jasperreports.crosstabs.JRCrosstabRowGroup;
import net.sf.jasperreports.crosstabs.design.JRDesignCrosstab;
import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRElement;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRExpressionCollector;
import net.sf.jasperreports.engine.JRLineBox;
import net.sf.jasperreports.engine.JRVariable;
import net.sf.jasperreports.engine.JRVisitor;
import net.sf.jasperreports.engine.base.JRBaseElement;
import net.sf.jasperreports.engine.base.JRBaseLineBox;
import net.sf.jasperreports.engine.base.JRBaseObjectFactory;
import net.sf.jasperreports.engine.type.HorizontalPosition;
import net.sf.jasperreports.engine.type.ModeEnum;
import net.sf.jasperreports.engine.type.RunDirectionEnum;
import net.sf.jasperreports.engine.util.ElementsVisitorUtils;
import net.sf.jasperreports.engine.util.JRCloneUtils;
import net.sf.jasperreports.engine.util.JRStyleResolver;

/**
 * Base read-only {@link net.sf.jasperreports.crosstabs.JRCrosstab crosstab} implementation.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 */
public class JRBaseCrosstab extends JRBaseElement implements JRCrosstab
{
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;
	
	public static final String PROPERTY_RUN_DIRECTION = "runDirection";
	
	public static final String PROPERTY_HORIZONTAL_POSITION = "horizontalPosition";
	
	public static final String PROPERTY_IGNORE_WIDTH = "ignoreWidth";

	protected int id;
	protected JRCrosstabParameter[] parameters;
	protected JRVariable[] variables;
	protected JRExpression parametersMapExpression;
	protected JRCrosstabDataset dataset;
	protected JRCrosstabRowGroup[] rowGroups;
	protected JRCrosstabColumnGroup[] columnGroups;
	protected JRCrosstabMeasure[] measures;
	protected int columnBreakOffset;
	protected boolean repeatColumnHeaders = true;
	protected boolean repeatRowHeaders = true;
	protected RunDirectionEnum runDirectionValue;
	protected HorizontalPosition horizontalPosition;
	protected JRCrosstabCell[][] cells;
	protected JRCellContents whenNoDataCell;
	protected CrosstabColumnCell titleCell;
	protected JRCellContents headerCell;
	protected Boolean ignoreWidth;
	protected JRLineBox lineBox;
	
	public JRBaseCrosstab(JRCrosstab crosstab, JRBaseObjectFactory factory, int id)
	{
		super(crosstab, factory);
		
		this.id = id;
		
		this.columnBreakOffset = crosstab.getColumnBreakOffset();
		this.repeatColumnHeaders = crosstab.isRepeatColumnHeaders();
		this.repeatRowHeaders = crosstab.isRepeatRowHeaders();
		this.runDirectionValue = crosstab.getRunDirectionValue();
		this.horizontalPosition = crosstab.getHorizontalPosition();
		this.ignoreWidth = crosstab.getIgnoreWidth();
		
		this.dataset = factory.getCrosstabDataset(crosstab.getDataset());
		
		copyParameters(crosstab, factory);		
		copyVariables(crosstab, factory);		
		titleCell = factory.getCrosstabColumnCell(crosstab.getTitleCell());
		headerCell = factory.getCell(crosstab.getHeaderCell());
		copyRowGroups(crosstab, factory);		
		copyColumnGroups(crosstab, factory);
		copyMeasures(crosstab, factory);
		copyCells(crosstab, factory);
		
		whenNoDataCell = factory.getCell(crosstab.getWhenNoDataCell());
		lineBox = crosstab.getLineBox().clone(this);
	}

	/**
	 *
	 */
	public ModeEnum getModeValue()
	{
		return JRStyleResolver.getMode(this, ModeEnum.TRANSPARENT);
	}
	
	private void copyParameters(JRCrosstab crosstab, JRBaseObjectFactory factory)
	{
		JRCrosstabParameter[] crossParameters = crosstab.getParameters();
		if (crossParameters != null)
		{
			parameters = new JRCrosstabParameter[crossParameters.length];
			for (int i = 0; i < parameters.length; i++)
			{
				parameters[i] = factory.getCrosstabParameter(crossParameters[i]);
			}
		}
		
		parametersMapExpression = factory.getExpression(crosstab.getParametersMapExpression());
	}

	private void copyVariables(JRCrosstab crosstab, JRBaseObjectFactory factory)
	{
		JRVariable[] vars = crosstab.getVariables();
		if (vars != null)
		{
			variables = new JRVariable[vars.length];
			for (int i = 0; i < vars.length; i++)
			{
				variables[i] = factory.getVariable(vars[i]);
			}
		}
	}

	private void copyRowGroups(JRCrosstab crosstab, JRBaseObjectFactory factory)
	{
		JRCrosstabRowGroup[] crossRowGroups = crosstab.getRowGroups();
		if (crossRowGroups != null)
		{
			this.rowGroups = new JRCrosstabRowGroup[crossRowGroups.length];
			for (int i = 0; i < crossRowGroups.length; ++i)
			{
				this.rowGroups[i] = factory.getCrosstabRowGroup(crossRowGroups[i]);
			}
		}
	}

	private void copyColumnGroups(JRCrosstab crosstab, JRBaseObjectFactory factory)
	{
		JRCrosstabColumnGroup[] crossColumnGroups = crosstab.getColumnGroups();
		if (crossColumnGroups != null)
		{
			this.columnGroups = new JRCrosstabColumnGroup[crossColumnGroups.length];
			for (int i = 0; i < crossColumnGroups.length; ++i)
			{
				this.columnGroups[i] = factory.getCrosstabColumnGroup(crossColumnGroups[i]);
			}
		}
	}

	private void copyMeasures(JRCrosstab crosstab, JRBaseObjectFactory factory)
	{
		JRCrosstabMeasure[] crossMeasures = crosstab.getMeasures();
		if (crossMeasures != null)
		{
			this.measures = new JRCrosstabMeasure[crossMeasures.length];
			for (int i = 0; i < crossMeasures.length; ++i)
			{
				this.measures[i] = factory.getCrosstabMeasure(crossMeasures[i]);
			}
		}
	}

	private void copyCells(JRCrosstab crosstab, JRBaseObjectFactory factory)
	{
		JRCrosstabCell[][] crossCells = crosstab.getCells();
		if (crossCells != null)
		{
			this.cells = new JRCrosstabCell[rowGroups.length + 1][columnGroups.length + 1];
			for (int i = 0; i <= rowGroups.length; i++)
			{
				for (int j = 0; j <= columnGroups.length; ++j)
				{
					this.cells[i][j] = factory.getCrosstabCell(crossCells[i][j]);
				}
			}
		}
	}
	
	public int getId()
	{
		return id;
	}

	public JRCrosstabDataset getDataset()
	{
		return dataset;
	}

	public JRCrosstabRowGroup[] getRowGroups()
	{
		return rowGroups;
	}

	public JRCrosstabColumnGroup[] getColumnGroups()
	{
		return columnGroups;
	}

	public JRCrosstabMeasure[] getMeasures()
	{
		return measures;
	}

	public void collectExpressions(JRExpressionCollector collector)
	{
		collector.collect(this);
	}

	public void visit(JRVisitor visitor)
	{
		visitor.visitCrosstab(this);
		
		if (ElementsVisitorUtils.visitDeepElements(visitor))
		{
			new CrosstabDeepVisitor(visitor).deepVisitCrosstab(this);
		}
	}

	public int getColumnBreakOffset()
	{
		return columnBreakOffset;
	}

	public boolean isRepeatColumnHeaders()
	{
		return repeatColumnHeaders;
	}

	public boolean isRepeatRowHeaders()
	{
		return repeatRowHeaders;
	}

	public JRCrosstabCell[][] getCells()
	{
		return cells;
	}

	public JRCrosstabParameter[] getParameters()
	{
		return parameters;
	}

	public JRExpression getParametersMapExpression()
	{
		return parametersMapExpression;
	}

	public JRCellContents getWhenNoDataCell()
	{
		return whenNoDataCell;
	}
	
	public static JRElement getElementByKey(JRCrosstab crosstab, String key)
	{
		JRElement element = null;
		
		if (crosstab.getTitleCell() != null && crosstab.getTitleCell().getCellContents() != null)
		{
			element = crosstab.getTitleCell().getCellContents().getElementByKey(key);
		}
		
		if (element == null && crosstab.getHeaderCell() != null)
		{
			element = crosstab.getHeaderCell().getElementByKey(key);
		}
		
		if (element == null)
		{
			element = getHeadersElement(crosstab.getRowGroups(), key);
		}		

		if (element == null)
		{
			element = getHeadersElement(crosstab.getColumnGroups(), key);
		}
		
		if (element == null)
		{
			if (crosstab instanceof JRDesignCrosstab)
			{
				List<JRCrosstabCell> cellsList = ((JRDesignCrosstab) crosstab).getCellsList();
				for (Iterator<JRCrosstabCell> it = cellsList.iterator(); element == null && it.hasNext();)
				{
					JRCrosstabCell cell = it.next();
					element = cell.getContents().getElementByKey(key);
				}
			}
			else
			{
				JRCrosstabCell[][] cells = crosstab.getCells();
				for (int i = cells.length - 1; element == null && i >= 0; --i)
				{
					for (int j = cells[i].length - 1; element == null && j >= 0; --j)
					{
						JRCrosstabCell cell = cells[i][j];
						if (cell != null)
						{
							element = cell.getContents().getElementByKey(key);
						}
					}
				}
			}
		}
		
		if (element == null && crosstab.getWhenNoDataCell() != null)
		{
			element = crosstab.getWhenNoDataCell().getElementByKey(key);
		}
		
		return element;
	}

	private static JRElement getHeadersElement(JRCrosstabGroup[] groups, String key)
	{
		JRElement element = null;
		
		if (groups != null)
		{
			for (int i = 0; element == null && i < groups.length; i++)
			{
				JRCrosstabGroup group = groups[i];
				JRCellContents header = group.getHeader();
				element = header.getElementByKey(key);
				
				if (element == null)
				{
					JRCellContents totalHeader = group.getTotalHeader();
					element = totalHeader.getElementByKey(key);
				}
				
				// ugly
				if (element == null && group instanceof JRCrosstabColumnGroup)
				{
					JRCellContents crosstabHeader = ((JRCrosstabColumnGroup) group).getCrosstabHeader();
					if (crosstabHeader != null)
					{
						element = crosstabHeader.getElementByKey(key);
					}
				}
			}
		}
		
		return element;
	}

	
	public JRElement getElementByKey(String elementKey)
	{
		return getElementByKey(this, elementKey);
	}

	@Override
	public CrosstabColumnCell getTitleCell()
	{
		return titleCell;
	}

	public JRCellContents getHeaderCell()
	{
		return headerCell;
	}

	public JRVariable[] getVariables()
	{
		return variables;
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
		getEventSupport().firePropertyChange(PROPERTY_RUN_DIRECTION, old, this.runDirectionValue);
	}

	@Override
	public HorizontalPosition getHorizontalPosition()
	{
		return horizontalPosition;
	}

	@Override
	public void setHorizontalPosition(HorizontalPosition horizontalPosition)
	{
		HorizontalPosition old = this.horizontalPosition;
		this.horizontalPosition = horizontalPosition;
		getEventSupport().firePropertyChange(PROPERTY_RUN_DIRECTION, old, this.horizontalPosition);
	}

	/**
	 * 
	 */
	public Object clone() 
	{
		CrosstabBaseCloneFactory factory = new CrosstabBaseCloneFactory();
		
		JRBaseCrosstab clone = (JRBaseCrosstab) super.clone();
		clone.parameters = JRCloneUtils.cloneArray(parameters);
		
		if (variables != null)
		{
			clone.variables = new JRVariable[variables.length];
			for (int i = 0; i < variables.length; i++)
			{
				clone.variables[i] = factory.clone(variables[i]);
			}
		}

		clone.parametersMapExpression = JRCloneUtils.nullSafeClone(parametersMapExpression);
		clone.dataset = JRCloneUtils.nullSafeClone(dataset);
		clone.rowGroups = factory.cloneCrosstabObjects(rowGroups);
		clone.columnGroups = factory.cloneCrosstabObjects(columnGroups);
		clone.measures = factory.cloneCrosstabObjects(measures);
		
		clone.cells = new JRCrosstabCell[cells.length][];
		for (int i = 0; i < cells.length; i++)
		{
			clone.cells[i] = JRCloneUtils.cloneArray(cells[i]);
		}
		
		clone.whenNoDataCell = JRCloneUtils.nullSafeClone(whenNoDataCell);
		clone.titleCell = JRCloneUtils.nullSafeClone(titleCell);
		clone.headerCell = JRCloneUtils.nullSafeClone(headerCell);
		clone.lineBox = lineBox.clone(clone);
		return clone;
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
	
	@SuppressWarnings("deprecation")
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
