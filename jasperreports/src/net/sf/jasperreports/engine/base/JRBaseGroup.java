/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2009 Jaspersoft Corporation. All rights reserved.
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
package net.sf.jasperreports.engine.base;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;

import net.sf.jasperreports.engine.JRBand;
import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRGroup;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.JRSection;
import net.sf.jasperreports.engine.JRVariable;
import net.sf.jasperreports.engine.design.events.JRChangeEventsSupport;
import net.sf.jasperreports.engine.design.events.JRPropertyChangeSupport;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JRBaseGroup implements JRGroup, Serializable, JRChangeEventsSupport
{


	/**
	 *
	 */
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;
	
	public static final String PROPERTY_MIN_HEIGHT_TO_START_NEW_PAGE = "minHeightToStartNewPage";
	
	public static final String PROPERTY_RESET_PAGE_NUMBER = "resetPageNumber";
	
	public static final String PROPERTY_REPRINT_HEADER_ON_EACH_PAGE = "reprintHeaderOnEachPage";
	
	public static final String PROPERTY_START_NEW_COLUMN = "startNewColumn";
	
	public static final String PROPERTY_START_NEW_PAGE = "startNewPage";

	/**
	 *
	 */
	protected String name = null;
	protected boolean isStartNewColumn = false;
	protected boolean isStartNewPage = false;
	protected boolean isResetPageNumber = false;
	protected boolean isReprintHeaderOnEachPage = false;
	protected int minHeightToStartNewPage = 0;

	/**
	 *
	 */
	protected JRExpression expression = null;
	protected JRSection groupHeaderSection = null;
	protected JRSection groupFooterSection = null;
	protected JRVariable countVariable = null;
	

	/**
	 *
	 */
	protected JRBaseGroup()
	{
	}
		

	/**
	 *
	 */
	protected JRBaseGroup(JRGroup group, JRBaseObjectFactory factory)
	{
		factory.put(group, this);
		
		name = group.getName();
		isStartNewColumn = group.isStartNewColumn();
		isStartNewPage = group.isStartNewPage();
		isResetPageNumber = group.isResetPageNumber();
		isReprintHeaderOnEachPage = group.isReprintHeaderOnEachPage();
		minHeightToStartNewPage = group.getMinHeightToStartNewPage();
		
		expression = factory.getExpression(group.getExpression());

		groupHeaderSection = factory.getSection(group.getGroupHeaderSection());
		groupFooterSection = factory.getSection(group.getGroupFooterSection());
		countVariable = factory.getVariable(group.getCountVariable());
	}
		

	/**
	 *
	 */
	public String getName()
	{
		return this.name;
	}
	
	/**
	 *
	 */
	public boolean isStartNewColumn()
	{
		return this.isStartNewColumn;
	}
		
	/**
	 *
	 */
	public void setStartNewColumn(boolean isStart)
	{
		boolean old = this.isStartNewColumn;
		this.isStartNewColumn = isStart;
		getEventSupport().firePropertyChange(PROPERTY_START_NEW_COLUMN, old, this.isStartNewColumn);
	}
		
	/**
	 *
	 */
	public boolean isStartNewPage()
	{
		return this.isStartNewPage;
	}
		
	/**
	 *
	 */
	public void setStartNewPage(boolean isStart)
	{
		boolean old = this.isStartNewPage;
		this.isStartNewPage = isStart;
		getEventSupport().firePropertyChange(PROPERTY_START_NEW_PAGE, old, this.isStartNewPage);
	}
		
	/**
	 *
	 */
	public boolean isResetPageNumber()
	{
		return this.isResetPageNumber;
	}
		
	/**
	 *
	 */
	public void setResetPageNumber(boolean isReset)
	{
		boolean old = this.isResetPageNumber;
		this.isResetPageNumber = isReset;
		getEventSupport().firePropertyChange(PROPERTY_RESET_PAGE_NUMBER, old, this.isResetPageNumber);
	}
		
	/**
	 *
	 */
	public boolean isReprintHeaderOnEachPage()
	{
		return this.isReprintHeaderOnEachPage;
	}
		
	/**
	 *
	 */
	public void setReprintHeaderOnEachPage(boolean isReprint)
	{
		boolean old = this.isReprintHeaderOnEachPage;
		this.isReprintHeaderOnEachPage = isReprint;
		getEventSupport().firePropertyChange(PROPERTY_REPRINT_HEADER_ON_EACH_PAGE, old, this.isReprintHeaderOnEachPage);
	}
		
	/**
	 *
	 */
	public int getMinHeightToStartNewPage()
	{
		return this.minHeightToStartNewPage;
	}

	/**
	 *
	 */
	public void setMinHeightToStartNewPage(int minHeight)
	{
		int old = this.minHeightToStartNewPage;
		this.minHeightToStartNewPage = minHeight;
		getEventSupport().firePropertyChange(PROPERTY_MIN_HEIGHT_TO_START_NEW_PAGE, old, this.minHeightToStartNewPage);
	}
		
	/**
	 *
	 */
	public JRExpression getExpression()
	{
		return this.expression;
	}
	
	/**
	 * @deprecated Replaced by {@link #getGroupHeaderSection()}.
	 */
	public JRBand getGroupHeader()
	{
		return 
			groupHeaderSection == null 
			|| groupHeaderSection.getBands() == null 
			|| groupHeaderSection.getBands().length == 0 
				? null 
				: (JRBand)groupHeaderSection.getBands()[0];
	}
		
	/**
	 *
	 */
	public JRSection getGroupHeaderSection()
	{
		return this.groupHeaderSection;
	}
		
	/**
	 * @deprecated Replaced by {@link #getGroupFooterSection()}.
	 */
	public JRBand getGroupFooter()
	{
		return 
			groupFooterSection == null 
			|| groupFooterSection.getBands() == null 
			|| groupFooterSection.getBands().length == 0 
				? null 
				: (JRBand)groupFooterSection.getBands()[0];
	}
		
	/**
	 *
	 */
	public JRSection getGroupFooterSection()
	{
		return this.groupFooterSection;
	}
		
	/**
	 *
	 */
	public JRVariable getCountVariable()
	{
		return this.countVariable;
	}

	
	/**
	 *
	 */
	public Object clone() 
	{
		JRBaseGroup clone = null;

		try
		{
			clone = (JRBaseGroup)super.clone();
		}
		catch (CloneNotSupportedException e)
		{
			throw new JRRuntimeException(e);
		}
		
		if (expression != null)
		{
			clone.expression = (JRExpression)expression.clone();
		}
		if (groupHeader != null)
		{
			clone.groupHeader = (JRBand)groupHeader.clone();
		}
		if (groupFooter != null)
		{
			clone.groupFooter = (JRBand)groupFooter.clone();
		}
		if (countVariable != null)
		{
			clone.countVariable = (JRVariable)countVariable.clone();
		}
		
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

	
	/**
	 * This field is only for serialization backward compatibility.
	 */
	private JRBand groupHeader = null;
	private JRBand groupFooter = null;
	
	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException
	{
		in.defaultReadObject();
		
		if (groupHeader != null)
		{
			groupHeaderSection = new JRBaseSection(groupHeader);
			groupHeader = null;
		}
		
		if (groupFooter != null)
		{
			groupFooterSection = new JRBaseSection(groupFooter);
			groupFooter = null;
		}
	}

}
