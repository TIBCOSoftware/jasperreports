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
package net.sf.jasperreports.engine.base;

import java.awt.Color;
import java.io.Serializable;

import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRDefaultStyleProvider;
import net.sf.jasperreports.engine.JRElement;
import net.sf.jasperreports.engine.JRElementGroup;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRGroup;
import net.sf.jasperreports.engine.JRPropertiesHolder;
import net.sf.jasperreports.engine.JRPropertiesMap;
import net.sf.jasperreports.engine.JRPropertyExpression;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.JRStyle;
import net.sf.jasperreports.engine.design.events.JRChangeEventsSupport;
import net.sf.jasperreports.engine.design.events.JRPropertyChangeSupport;
import net.sf.jasperreports.engine.util.JRStyleResolver;


/**
 * This class provides a skeleton implementation for a report element. It mostly provides internal variables, representing
 * the most common element properties, and their getter/setter methods. It also has a constructor for initializing
 * these properties.
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public abstract class JRBaseElement implements JRElement, Serializable, JRChangeEventsSupport
{


	/**
	 *
	 */
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

	public static final String PROPERTY_POSITION_TYPE = "positionType";

	public static final String PROPERTY_PRINT_IN_FIRST_WHOLE_BAND = "isPrintInFirstWholeBand";

	public static final String PROPERTY_PRINT_REPEATED_VALUES = "isPrintRepeatedValues";

	public static final String PROPERTY_PRINT_WHEN_DETAIL_OVERFLOWS = "isPrintWhenDetailOverflows";

	public static final String PROPERTY_REMOVE_LINE_WHEN_BLANK = "isRemoveLineWhenBlank";

	public static final String PROPERTY_STRETCH_TYPE = "stretchType";

	public static final String PROPERTY_WIDTH = "width";

	public static final String PROPERTY_X = "x";

	/**
	 *
	 */
	protected String key = null;
	protected byte positionType;
	protected byte stretchType;
	protected boolean isPrintRepeatedValues = true;
	protected Byte mode;
	protected int x = 0;
	protected int y = 0;
	protected int width = 0;
	protected int height = 0;
	protected boolean isRemoveLineWhenBlank = false;
	protected boolean isPrintInFirstWholeBand = false;
	protected boolean isPrintWhenDetailOverflows = false;
	protected Color forecolor = null;
	protected Color backcolor = null;

	/**
	 *
	 */
	protected JRExpression printWhenExpression = null;
	protected JRGroup printWhenGroupChanges = null;
	protected JRElementGroup elementGroup = null;

	protected JRDefaultStyleProvider defaultStyleProvider;
	protected JRStyle parentStyle;
	protected String parentStyleNameReference;

	private JRPropertiesMap propertiesMap;

	private JRPropertyExpression[] propertyExpressions;
	
	/**
	 *
	 */
	protected JRBaseElement(JRDefaultStyleProvider defaultStyleProvider)
	{
		this.defaultStyleProvider = defaultStyleProvider;
	}


	/**
	 * Initializes basic properties of the element.
	 * @param element an element whose properties are copied to this element. Usually it is a
	 * {@link net.sf.jasperreports.engine.design.JRDesignElement} that must be transformed into an
	 * <tt>JRBaseElement</tt> at compile time.
	 * @param factory a factory used in the compile process
	 */
	protected JRBaseElement(JRElement element, JRBaseObjectFactory factory)
	{
		factory.put(element, this);

		defaultStyleProvider = factory.getDefaultStyleProvider();

		parentStyle = factory.getStyle(element.getStyle());
		parentStyleNameReference = element.getStyleNameReference();

		key = element.getKey();
		positionType = element.getPositionType();
		stretchType = element.getStretchType();
		isPrintRepeatedValues = element.isPrintRepeatedValues();
		mode = element.getOwnMode();
		x = element.getX();
		y = element.getY();
		width = element.getWidth();
		height = element.getHeight();
		isRemoveLineWhenBlank = element.isRemoveLineWhenBlank();
		isPrintInFirstWholeBand = element.isPrintInFirstWholeBand();
		isPrintWhenDetailOverflows = element.isPrintWhenDetailOverflows();
		forecolor = element.getOwnForecolor();
		backcolor = element.getOwnBackcolor();

		printWhenExpression = factory.getExpression(element.getPrintWhenExpression());
		printWhenGroupChanges = factory.getGroup(element.getPrintWhenGroupChanges());
		elementGroup = (JRElementGroup)factory.getVisitResult(element.getElementGroup());
		
		propertiesMap = JRPropertiesMap.getPropertiesClone(element);
		copyPropertyExpressions(element, factory);
	}


	private void copyPropertyExpressions(JRElement element,
			JRBaseObjectFactory factory)
	{
		JRPropertyExpression[] props = element.getPropertyExpressions();
		if (props != null && props.length > 0)
		{
			propertyExpressions = new JRPropertyExpression[props.length];
			for (int i = 0; i < props.length; i++)
			{
				propertyExpressions[i] = factory.getPropertyExpression(props[i]);
			}
		}
	}


	/**
	 *
	 */
	public JRDefaultStyleProvider getDefaultStyleProvider()
	{
		return defaultStyleProvider;
	}

	/**
	 *
	 */
	protected JRStyle getBaseStyle()
	{
		if (parentStyle != null)
			return parentStyle;
		if (defaultStyleProvider != null)
			return defaultStyleProvider.getDefaultStyle();
		return null;
	}

	/**
	 *
	 */
	public String getKey()
	{
		return this.key;
	}

	/**
	 *
	 */
	public byte getPositionType()
	{
		return positionType;
	}

	/**
	 *
	 */
	public void setPositionType(byte positionType)
	{
		byte old = this.positionType;
		this.positionType = positionType;
		getEventSupport().firePropertyChange(PROPERTY_POSITION_TYPE, old, this.positionType);
	}

	/**
	 *
	 */
	public byte getStretchType()
	{
		return stretchType;
	}

	/**
	 *
	 */
	public void setStretchType(byte stretchType)
	{
		byte old = this.stretchType;
		this.stretchType = stretchType;
		getEventSupport().firePropertyChange(PROPERTY_STRETCH_TYPE, old, this.stretchType);
	}

	/**
	 *
	 */
	public boolean isPrintRepeatedValues()
	{
		return this.isPrintRepeatedValues;
	}

	/**
	 *
	 */
	public void setPrintRepeatedValues(boolean isPrintRepeatedValues)
	{
		boolean old = this.isPrintRepeatedValues;
		this.isPrintRepeatedValues = isPrintRepeatedValues;
		getEventSupport().firePropertyChange(PROPERTY_PRINT_REPEATED_VALUES, old, this.isPrintRepeatedValues);
	}

	/**
	 *
	 */
	public byte getMode()
	{
		return JRStyleResolver.getMode(this, MODE_OPAQUE);
	}

	/**
	 *
	 */
	public Byte getOwnMode()
	{
		return mode;
	}

	/**
	 *
	 */
	public void setMode(byte mode)
	{
		setMode(new Byte(mode));
	}

	/**
	 *
	 */
	public void setMode(Byte mode)
	{
		Object old = this.mode;
		this.mode = mode;
		getEventSupport().firePropertyChange(JRBaseStyle.PROPERTY_MODE, old, this.mode);
	}

	/**
	 *
	 */
	public int getX()
	{
		return this.x;
	}

	/**
	 *
	 */
	public void setX(int x)
	{
		int old = this.x;
		this.x = x;
		getEventSupport().firePropertyChange(PROPERTY_X, old, this.x);
	}

	/**
	 *
	 */
	public int getY()
	{
		return this.y;
	}

	/**
	 *
	 */
	public int getWidth()
	{
		return this.width;
	}

	/**
	 *
	 */
	public void setWidth(int width)
	{
		int old = this.width;
		this.width = width;
		getEventSupport().firePropertyChange(PROPERTY_WIDTH, old, this.width);
	}

	/**
	 *
	 */
	public int getHeight()
	{
		return this.height;
	}

	/**
	 *
	 */
	public boolean isRemoveLineWhenBlank()
	{
		return this.isRemoveLineWhenBlank;
	}

	/**
	 *
	 */
	public void setRemoveLineWhenBlank(boolean isRemoveLine)
	{
		boolean old = this.isRemoveLineWhenBlank;
		this.isRemoveLineWhenBlank = isRemoveLine;
		getEventSupport().firePropertyChange(PROPERTY_REMOVE_LINE_WHEN_BLANK, old, this.isRemoveLineWhenBlank);
	}

	/**
	 *
	 */
	public boolean isPrintInFirstWholeBand()
	{
		return this.isPrintInFirstWholeBand;
	}

	/**
	 *
	 */
	public void setPrintInFirstWholeBand(boolean isPrint)
	{
		boolean old = this.isPrintInFirstWholeBand;
		this.isPrintInFirstWholeBand = isPrint;
		getEventSupport().firePropertyChange(PROPERTY_PRINT_IN_FIRST_WHOLE_BAND, old, this.isPrintInFirstWholeBand);
	}

	/**
	 *
	 */
	public boolean isPrintWhenDetailOverflows()
	{
		return this.isPrintWhenDetailOverflows;
	}

	/**
	 *
	 */
	public void setPrintWhenDetailOverflows(boolean isPrint)
	{
		boolean old = this.isPrintWhenDetailOverflows;
		this.isPrintWhenDetailOverflows = isPrint;
		getEventSupport().firePropertyChange(PROPERTY_PRINT_WHEN_DETAIL_OVERFLOWS, old, this.isPrintWhenDetailOverflows);
	}

	/**
	 *
	 */
	public Color getForecolor()
	{
		return JRStyleResolver.getForecolor(this);
	}

	/**
	 *
	 */
	public Color getOwnForecolor()
	{
		return forecolor;
	}

	/**
	 *
	 */
	public void setForecolor(Color forecolor)
	{
		Object old = this.forecolor;
		this.forecolor = forecolor;
		getEventSupport().firePropertyChange(JRBaseStyle.PROPERTY_FORECOLOR, old, this.forecolor);
	}

	/**
	 *
	 */
	public Color getBackcolor()
	{
		return JRStyleResolver.getBackcolor(this);
	}

	/**
	 *
	 */
	public Color getOwnBackcolor()
	{
		return backcolor;
	}

	/**
	 *
	 */
	public void setBackcolor(Color backcolor)
	{
		Object old = this.backcolor;
		this.backcolor = backcolor;
		getEventSupport().firePropertyChange(JRBaseStyle.PROPERTY_BACKCOLOR, old, this.backcolor);
	}

	/**
	 *
	 */
	public JRExpression getPrintWhenExpression()
	{
		return this.printWhenExpression;
	}

	/**
	 *
	 */
	public JRGroup getPrintWhenGroupChanges()
	{
		return this.printWhenGroupChanges;
	}

	/**
	 *
	 */
	public JRElementGroup getElementGroup()
	{
		return this.elementGroup;
	}

	public JRStyle getStyle()
	{
		return parentStyle;
	}

	public String getStyleNameReference()
	{
		return parentStyleNameReference;
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
	 * 
	 */
	public Object clone() 
	{
		JRBaseElement clone = null;
		
		try
		{
			clone = (JRBaseElement)super.clone();
		}
		catch (CloneNotSupportedException e)
		{
			throw new JRRuntimeException(e);
		}

		if (printWhenExpression != null)
		{
			clone.printWhenExpression = (JRExpression)printWhenExpression.clone();
		}
		
		return clone;
	}

	/**
	 * 
	 */
	public Object clone(JRElementGroup parentGroup) 
	{
		JRBaseElement clone = (JRBaseElement)this.clone();
		
		clone.elementGroup = parentGroup;
		
		return clone;
	}

	public boolean hasProperties()
	{
		return propertiesMap != null && propertiesMap.hasProperties();
	}

	public JRPropertiesMap getPropertiesMap()
	{
		if (propertiesMap == null)
		{
			propertiesMap = new JRPropertiesMap();
		}
		return propertiesMap;
	}

	public JRPropertiesHolder getParentProperties()
	{
		return null;
	}

	public JRPropertyExpression[] getPropertyExpressions()
	{
		return propertyExpressions;
	}
	
}
