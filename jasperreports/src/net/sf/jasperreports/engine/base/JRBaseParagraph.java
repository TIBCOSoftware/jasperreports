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
package net.sf.jasperreports.engine.base;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import net.sf.jasperreports.engine.Deduplicable;
import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRDefaultStyleProvider;
import net.sf.jasperreports.engine.JRParagraph;
import net.sf.jasperreports.engine.JRParagraphContainer;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.JRStyle;
import net.sf.jasperreports.engine.TabStop;
import net.sf.jasperreports.engine.design.events.JRChangeEventsSupport;
import net.sf.jasperreports.engine.design.events.JRPropertyChangeSupport;
import net.sf.jasperreports.engine.type.LineSpacingEnum;
import net.sf.jasperreports.engine.util.JRCloneUtils;
import net.sf.jasperreports.engine.util.JRStyleResolver;
import net.sf.jasperreports.engine.util.ObjectUtils;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public class JRBaseParagraph implements JRParagraph, Serializable, Cloneable, JRChangeEventsSupport, Deduplicable
{


	/**
	 *
	 */
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;
	
	public static final String PROPERTY_LINE_SPACING = "lineSpacing";
	public static final String PROPERTY_LINE_SPACING_SIZE = "lineSpacingSize";
	public static final String PROPERTY_FIRST_LINE_INDENT = "firstLineIndent";
	public static final String PROPERTY_LEFT_INDENT = "leftIndent";
	public static final String PROPERTY_RIGHT_INDENT = "rightIndent";
	public static final String PROPERTY_SPACING_BEFORE = "spacingBefore";
	public static final String PROPERTY_SPACING_AFTER = "spacingAfter";
	public static final String PROPERTY_TAB_STOP_WIDTH = "tabStopWidth";
	public static final String PROPERTY_TAB_STOPS = "tabStops";


	protected JRParagraphContainer paragraphContainer;

	/**
	 *
	 */
	protected LineSpacingEnum lineSpacing;
	protected Float lineSpacingSize;
	protected Integer firstLineIndent;
	protected Integer leftIndent;
	protected Integer rightIndent;
	protected Integer spacingBefore;
	protected Integer spacingAfter;
	protected Integer tabStopWidth;
	protected List<TabStop> tabStops;

	
	/**
	 *
	 */
	public JRBaseParagraph(JRParagraphContainer paragraphContainer)
	{
		this.paragraphContainer = paragraphContainer;
	}
	
	
	/**
	 *
	 */
	public JRDefaultStyleProvider getDefaultStyleProvider() 
	{
		if (paragraphContainer != null)
		{
			return paragraphContainer.getDefaultStyleProvider();
		}
		return null;
	}

	/**
	 *
	 */
	public JRStyle getStyle() 
	{
		if (paragraphContainer != null)
		{
			return paragraphContainer.getStyle();
		}
		return null;
	}

	/**
	 *
	 */
	public String getStyleNameReference()
	{
		if (paragraphContainer != null)
		{
			return paragraphContainer.getStyleNameReference();
		}
		return null;
	}

	/**
	 *
	 */
	public JRParagraphContainer getParagraphContainer()
	{
		return paragraphContainer;
	}

	/**
	 *
	 */
	public LineSpacingEnum getLineSpacing()
	{
		return JRStyleResolver.getLineSpacing(this);
	}

	/**
	 *
	 */
	public LineSpacingEnum getOwnLineSpacing()
	{
		return lineSpacing;
	}
	
	/**
	 *
	 */
	public void setLineSpacing(LineSpacingEnum lineSpacing)
	{
		Object old = this.lineSpacing;
		this.lineSpacing = lineSpacing;
		getEventSupport().firePropertyChange(PROPERTY_LINE_SPACING, old, this.lineSpacing);
	}

	/**
	 *
	 */
	public Float getLineSpacingSize()
	{
		return JRStyleResolver.getLineSpacingSize(this);
	}

	/**
	 *
	 */
	public Float getOwnLineSpacingSize()
	{
		return lineSpacingSize;
	}
	
	/**
	 *
	 */
	public void setLineSpacingSize(Float lineSpacingSize)
	{
		Object old = this.lineSpacingSize;
		this.lineSpacingSize = lineSpacingSize;
		getEventSupport().firePropertyChange(PROPERTY_LINE_SPACING_SIZE, old, this.lineSpacingSize);
	}

	/**
	 *
	 */
	public Integer getFirstLineIndent()
	{
		return JRStyleResolver.getFirstLineIndent(this);
	}

	/**
	 *
	 */
	public Integer getOwnFirstLineIndent()
	{
		return firstLineIndent;
	}
	
	/**
	 *
	 */
	public void setFirstLineIndent(Integer firstLineIndent)
	{
		Object old = this.firstLineIndent;
		this.firstLineIndent = firstLineIndent;
		getEventSupport().firePropertyChange(PROPERTY_FIRST_LINE_INDENT, old, this.firstLineIndent);
	}

	/**
	 *
	 */
	public Integer getLeftIndent()
	{
		return JRStyleResolver.getLeftIndent(this);
	}

	/**
	 *
	 */
	public Integer getOwnLeftIndent()
	{
		return leftIndent;
	}
	
	/**
	 *
	 */
	public void setLeftIndent(Integer leftIndent)
	{
		Object old = this.leftIndent;
		this.leftIndent = leftIndent;
		getEventSupport().firePropertyChange(PROPERTY_LEFT_INDENT, old, this.leftIndent);
	}

	/**
	 *
	 */
	public Integer getRightIndent()
	{
		return JRStyleResolver.getRightIndent(this);
	}

	/**
	 *
	 */
	public Integer getOwnRightIndent()
	{
		return rightIndent;
	}
	
	/**
	 *
	 */
	public void setRightIndent(Integer rightIndent)
	{
		Object old = this.rightIndent;
		this.rightIndent = rightIndent;
		getEventSupport().firePropertyChange(PROPERTY_RIGHT_INDENT, old, this.rightIndent);
	}

	/**
	 *
	 */
	public Integer getSpacingBefore()
	{
		return JRStyleResolver.getSpacingBefore(this);
	}

	/**
	 *
	 */
	public Integer getOwnSpacingBefore()
	{
		return spacingBefore;
	}
	
	/**
	 *
	 */
	public void setSpacingBefore(Integer spacingBefore)
	{
		Object old = this.spacingBefore;
		this.spacingBefore = spacingBefore;
		getEventSupport().firePropertyChange(PROPERTY_SPACING_BEFORE, old, this.spacingBefore);
	}

	/**
	 *
	 */
	public Integer getSpacingAfter()
	{
		return JRStyleResolver.getSpacingAfter(this);
	}

	/**
	 *
	 */
	public Integer getOwnSpacingAfter()
	{
		return spacingAfter;
	}
	
	/**
	 *
	 */
	public void setSpacingAfter(Integer spacingAfter)
	{
		Object old = this.spacingAfter;
		this.spacingAfter = spacingAfter;
		getEventSupport().firePropertyChange(PROPERTY_SPACING_AFTER, old, this.spacingAfter);
	}

	/**
	 *
	 */
	public Integer getTabStopWidth()
	{
		return JRStyleResolver.getTabStopWidth(this);
	}

	/**
	 *
	 */
	public Integer getOwnTabStopWidth()
	{
		return tabStopWidth;
	}
	
	/**
	 *
	 */
	public void setTabStopWidth(Integer tabStopWidth)
	{
		Object old = this.tabStopWidth;
		this.tabStopWidth = tabStopWidth;
		getEventSupport().firePropertyChange(PROPERTY_TAB_STOP_WIDTH, old, this.tabStopWidth);
	}
	
	/**
	 *
	 */
	public TabStop[] getTabStops()
	{
		return JRStyleResolver.getTabStops(this);
	}
	
	
	/**
	 *
	 */
	public TabStop[] getOwnTabStops()
	{
		if (tabStops == null || tabStops.size() == 0)
		{
			return null;
		}
		
		return tabStops.toArray(new TabStop[tabStops.size()]);
	}
	
	/**
	 *
	 */
	public void addTabStop(TabStop tabStop)
	{
		if (tabStops == null)
		{
			tabStops = new ArrayList<TabStop>();
		}
		
		tabStops.add(tabStop);
		
		getEventSupport().fireCollectionElementAddedEvent(PROPERTY_TAB_STOPS, tabStop, tabStops.size() - 1);
	}
	
	/**
	 *
	 */
	public void addTabStop(int index, TabStop tabStop)
	{
		if (tabStops == null)
		{
			tabStops = new ArrayList<TabStop>();
		}
		
		tabStops.add(index, tabStop);
		
		getEventSupport().fireCollectionElementAddedEvent(PROPERTY_TAB_STOPS, tabStop, index);
	}
	
	/**
	 *
	 */
	public void removeTabStop(TabStop tabStop)
	{
		if (tabStops != null)
		{
			int index = tabStops.indexOf(tabStop);
			if (index >= 0)
			{
				tabStops.remove(index);
				getEventSupport().fireCollectionElementRemovedEvent(PROPERTY_TAB_STOPS, tabStop, index);
			}
		}
	}
	
	/**
	 *
	 */
	public void removeTabStop(int index)
	{
		if (tabStops != null)
		{
			if (index >= 0 && index < tabStops.size())
			{
				TabStop tabStop = tabStops.remove(index);
				getEventSupport().fireCollectionElementRemovedEvent(PROPERTY_TAB_STOPS, tabStop, index);
			}
		}
	}


	/**
	 * 
	 */
	public JRParagraph clone(JRParagraphContainer paragraphContainer)
	{
		JRBaseParagraph clone = null;
		
		try
		{
			clone = (JRBaseParagraph)super.clone();
		}
		catch(CloneNotSupportedException e)
		{
			throw new JRRuntimeException(e);
		}
		
		clone.paragraphContainer = paragraphContainer;
		clone.tabStops = JRCloneUtils.cloneList(tabStops);
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


	public int getHashCode()
	{
		ObjectUtils.HashCode hash = ObjectUtils.hash();
		hash.add(lineSpacing);
		hash.add(lineSpacingSize);
		hash.add(firstLineIndent);
		hash.add(leftIndent);
		hash.add(rightIndent);
		hash.add(spacingBefore);
		hash.add(spacingAfter);
		hash.add(tabStopWidth);
		hash.addIdentical(tabStops);
		return hash.getHashCode();
	}


	public boolean isIdentical(Object object)
	{
		if (this == object)
		{
			return true;
		}
		
		if (!(object instanceof JRBaseParagraph))
		{
			return false;
		}
		
		JRBaseParagraph para = (JRBaseParagraph) object;

		return 
				ObjectUtils.equals(lineSpacing, para.lineSpacing)
				&& ObjectUtils.equals(lineSpacingSize, para.lineSpacingSize)
				&& ObjectUtils.equals(firstLineIndent, para.firstLineIndent)
				&& ObjectUtils.equals(leftIndent, para.leftIndent)
				&& ObjectUtils.equals(rightIndent, para.rightIndent)
				&& ObjectUtils.equals(spacingBefore, para.spacingBefore)
				&& ObjectUtils.equals(spacingAfter, para.spacingAfter)
				&& ObjectUtils.equals(tabStopWidth, para.tabStopWidth)
				&& ObjectUtils.identical(tabStops, para.tabStops);
	}

}
