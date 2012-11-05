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

import net.sf.jasperreports.crosstabs.JRCellContents;
import net.sf.jasperreports.crosstabs.base.JRBaseCrosstabGroup;
import net.sf.jasperreports.crosstabs.type.CrosstabTotalPositionEnum;
import net.sf.jasperreports.engine.design.JRDesignVariable;
import net.sf.jasperreports.engine.design.events.JRChangeEventsSupport;
import net.sf.jasperreports.engine.design.events.JRPropertyChangeSupport;
import net.sf.jasperreports.engine.type.CalculationEnum;

/**
 * Base crosstab row/column group implementation to be used at design time.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public abstract class JRDesignCrosstabGroup extends JRBaseCrosstabGroup implements JRChangeEventsSupport
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3142244933088846956L;//it's OK to have calculated UID here, because we missed it when first releasing this class

	public static final String PROPERTY_BUCKET = "bucket";

	public static final String PROPERTY_HEADER = "header";

	public static final String PROPERTY_NAME = "name";

	public static final String PROPERTY_TOTAL_HEADER = "totalHeader";

	public static final String PROPERTY_TOTAL_POSITION = "totalPosition";
	
	protected JRDesignVariable designVariable;
	
	protected JRDesignCrosstab parent;
	
	protected JRDesignCrosstabGroup()
	{
		super();
		
		variable = designVariable = new JRDesignVariable();
		designVariable.setCalculation(CalculationEnum.SYSTEM);
		designVariable.setSystemDefined(true);
		
		header = new JRDesignCellContents();
		totalHeader = new JRDesignCellContents();
	}
	
	
	/**
	 * Sets the group name.
	 * 
	 * @param name the name
	 * @see net.sf.jasperreports.crosstabs.JRCrosstabGroup#getName()
	 */
	public void setName(String name)
	{
		Object old = this.name;
		this.name = name;
		designVariable.setName(name);
		getEventSupport().firePropertyChange(PROPERTY_NAME, old, this.name);
	}
	
	
	/**
	 * Sets the position of the total row/column.
	 * 
	 * @param totalPositionValue the position of the total row/column
	 * @see net.sf.jasperreports.crosstabs.JRCrosstabGroup#getTotalPositionValue()
	 */
	public void setTotalPosition(CrosstabTotalPositionEnum totalPositionValue)
	{
		Object old = this.totalPositionValue;
		this.totalPositionValue = totalPositionValue;
		getEventSupport().firePropertyChange(PROPERTY_TOTAL_POSITION, old, this.totalPositionValue);
	}
	
	
	/**
	 * Sets the group bucketing information.
	 * 
	 * @param bucket the bucketing information
	 * @see net.sf.jasperreports.crosstabs.JRCrosstabGroup#getBucket()
	 */
	public void setBucket(JRDesignCrosstabBucket bucket)
	{
		Object old = this.bucket;
		this.bucket = bucket;
		getEventSupport().firePropertyChange(PROPERTY_BUCKET, old, this.bucket);
	}

	
	/**
	 * Sets the group header cell.
	 * 
	 * @param header the header cell
	 * @see net.sf.jasperreports.crosstabs.JRCrosstabGroup#getHeader()
	 */
	public void setHeader(JRDesignCellContents header)
	{
		Object old = this.header;
		if (header == null)
		{
			header = new JRDesignCellContents();
		}
		this.header = header;
		getEventSupport().firePropertyChange(PROPERTY_HEADER, old, this.header);
	}

	
	/**
	 * Sets the group total header cell.
	 * 
	 * @param totalHeader the total header
	 * @see net.sf.jasperreports.crosstabs.JRCrosstabGroup#getTotalHeader()
	 */
	public void setTotalHeader(JRDesignCellContents totalHeader)
	{
		Object old = this.totalHeader;
		if (totalHeader == null)
		{
			totalHeader = new JRDesignCellContents();
		}
		this.totalHeader = totalHeader;
		getEventSupport().firePropertyChange(PROPERTY_TOTAL_HEADER, old, this.totalHeader);
	}

	/**
	 * Returns the group parent crosstab.
	 * <p/>
	 * The parent is automatically set when the group is added to the crosstab.
	 * 
	 * @return the parent crosstab
	 */
	public JRDesignCrosstab getParent()
	{
		return parent;
	}

	void setParent(JRDesignCrosstab parent)
	{
		this.parent = parent;
	}
	
	protected void setCellOrigin(JRCellContents cell, JRCrosstabOrigin origin)
	{
		if (cell instanceof JRDesignCellContents)
		{
			((JRDesignCellContents) cell).setOrigin(origin);
		}
	}
	
	/**
	 * 
	 */
	public Object clone() 
	{
		JRDesignCrosstabGroup clone = (JRDesignCrosstabGroup) super.clone();
		// always the same instance
		clone.designVariable = (JRDesignVariable) clone.variable;
		return clone;
	}

	public JRDesignCrosstabGroup clone(JRDesignCrosstab parent)
	{
		JRDesignCrosstabGroup clone = (JRDesignCrosstabGroup) clone();
		clone.setParent(parent);
		clone.eventSupport = null;
		return clone;
	}
	
	private transient JRPropertyChangeSupport eventSupport;
	
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
}
