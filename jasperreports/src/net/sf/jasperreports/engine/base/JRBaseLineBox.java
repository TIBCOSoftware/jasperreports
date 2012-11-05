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
package net.sf.jasperreports.engine.base;

import java.awt.Color;
import java.io.Serializable;

import net.sf.jasperreports.engine.Deduplicable;
import net.sf.jasperreports.engine.JRBoxContainer;
import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRDefaultStyleProvider;
import net.sf.jasperreports.engine.JRLineBox;
import net.sf.jasperreports.engine.JRPen;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.JRStyle;
import net.sf.jasperreports.engine.design.events.JRChangeEventsSupport;
import net.sf.jasperreports.engine.design.events.JRPropertyChangeSupport;
import net.sf.jasperreports.engine.util.JRStyleResolver;
import net.sf.jasperreports.engine.util.ObjectUtils;


/**
 * This is useful for drawing borders around text elements and images. Boxes can have borders and paddings, which can
 * have different width and colour on each side of the element.
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JRBaseLineBox implements JRLineBox, Serializable, Cloneable, JRChangeEventsSupport, Deduplicable
{


	/**
	 *
	 */
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;
	
	public static final String PROPERTY_PADDING = "padding";
	
	public static final String PROPERTY_TOP_PADDING = "topPadding";
	
	public static final String PROPERTY_LEFT_PADDING = "leftPadding";
	
	public static final String PROPERTY_BOTTOM_PADDING = "bottomPadding";
	
	public static final String PROPERTY_RIGHT_PADDING = "rightPadding";
	

	protected JRBoxContainer boxContainer;

	/**
	 *
	 */
	protected JRBoxPen pen; 
	protected JRBoxPen topPen;
	protected JRBoxPen leftPen;
	protected JRBoxPen bottomPen;
	protected JRBoxPen rightPen;

	protected Integer padding;
	protected Integer topPadding;
	protected Integer leftPadding;
	protected Integer bottomPadding;
	protected Integer rightPadding;

	
	/**
	 *
	 */
	public JRBaseLineBox(JRBoxContainer boxContainer)
	{
		this.boxContainer = boxContainer;

		pen = new JRBaseBoxPen(this);
		topPen = new JRBaseBoxTopPen(this);
		leftPen = new JRBaseBoxLeftPen(this);
		bottomPen = new JRBaseBoxBottomPen(this);
		rightPen = new JRBaseBoxRightPen(this);
	}
	
	
	/**
	 *
	 */
	public JRDefaultStyleProvider getDefaultStyleProvider() 
	{
		if (boxContainer != null)
		{
			return boxContainer.getDefaultStyleProvider();
		}
		return null;
	}

	/**
	 *
	 */
	public JRStyle getStyle() 
	{
		if (boxContainer != null)
		{
			return boxContainer.getStyle();
		}
		return null;
	}

	/**
	 *
	 */
	public String getStyleNameReference()
	{
		if (boxContainer != null)
		{
			return boxContainer.getStyleNameReference();
		}
		return null;
	}

	/**
	 *
	 */
	public JRBoxContainer getBoxContainer()
	{
		return boxContainer;
	}

	/**
	 *
	 */
	public Float getDefaultLineWidth()
	{
		return JRPen.LINE_WIDTH_0;
	}

	/**
	 *
	 */
	public Color getDefaultLineColor()
	{
		if (boxContainer != null)
		{
			return boxContainer.getDefaultLineColor();
		}
		return Color.black;
	}

	/**
	 *
	 */
	public JRBoxPen getPen()
	{
		return pen;
	}

	/**
	 *
	 */
	public void copyPen(JRBoxPen pen)
	{
		this.pen = pen.clone(this);
	}

	/**
	 *
	 */
	public JRBoxPen getTopPen()
	{
		return topPen;
	}

	/**
	 *
	 */
	public void copyTopPen(JRBoxPen topPen)
	{
		this.topPen = topPen.clone(this);
	}

	/**
	 *
	 */
	public JRBoxPen getLeftPen()
	{
		return leftPen;
	}

	/**
	 *
	 */
	public void copyLeftPen(JRBoxPen leftPen)
	{
		this.leftPen = leftPen.clone(this);
	}

	/**
	 *
	 */
	public JRBoxPen getBottomPen()
	{
		return bottomPen;
	}

	/**
	 *
	 */
	public void copyBottomPen(JRBoxPen bottomPen)
	{
		this.bottomPen = bottomPen.clone(this);
	}

	/**
	 *
	 */
	public JRBoxPen getRightPen()
	{
		return rightPen;
	}

	/**
	 *
	 */
	public void copyRightPen(JRBoxPen rightPen)
	{
		this.rightPen = rightPen.clone(this);
	}

	/**
	 *
	 */
	public Integer getPadding()
	{
		return JRStyleResolver.getPadding(this);
	}

	public Integer getOwnPadding()
	{
		return padding;
	}
	
	/**
	 *
	 */
	public void setPadding(int padding)
	{
		setPadding(Integer.valueOf(padding));
	}

	/**
	 *
	 */
	public void setPadding(Integer padding)
	{
		Object old = this.padding;
		this.padding = padding;
		getEventSupport().firePropertyChange(PROPERTY_PADDING, old, this.padding);
	}

	/**
	 *
	 */
	public Integer getTopPadding()
	{
		return JRStyleResolver.getTopPadding(this);
	}

	/**
	 *
	 */
	public Integer getOwnTopPadding()
	{
		return topPadding;
	}

	/**
	 *
	 */
	public void setTopPadding(int topPadding)
	{
		setTopPadding(Integer.valueOf(topPadding));
	}

	/**
	 *
	 */
	public void setTopPadding(Integer topPadding)
	{
		Object old = this.topPadding;
		this.topPadding = topPadding;
		getEventSupport().firePropertyChange(PROPERTY_TOP_PADDING, old, this.topPadding);
	}

	/**
	 *
	 */
	public Integer getLeftPadding()
	{
		return JRStyleResolver.getLeftPadding(this);
	}

	/**
	 *
	 */
	public Integer getOwnLeftPadding()
	{
		return leftPadding;
	}

	/**
	 *
	 */
	public void setLeftPadding(int leftPadding)
	{
		setLeftPadding(Integer.valueOf(leftPadding));
	}

	/**
	 *
	 */
	public void setLeftPadding(Integer leftPadding)
	{
		Object old = this.leftPadding;
		this.leftPadding = leftPadding;
		getEventSupport().firePropertyChange(PROPERTY_LEFT_PADDING, old, this.leftPadding);
	}

	/**
	 *
	 */
	public Integer getBottomPadding()
	{
		return JRStyleResolver.getBottomPadding(this);
	}

	/**
	 *
	 */
	public Integer getOwnBottomPadding()
	{
		return bottomPadding;
	}

	/**
	 *
	 */
	public void setBottomPadding(int bottomPadding)
	{
		setBottomPadding(Integer.valueOf(bottomPadding));
	}

	/**
	 *
	 */
	public void setBottomPadding(Integer bottomPadding)
	{
		Object old = this.bottomPadding;
		this.bottomPadding = bottomPadding;
		getEventSupport().firePropertyChange(PROPERTY_BOTTOM_PADDING, old, this.bottomPadding);
	}

	/**
	 *
	 */
	public Integer getRightPadding()
	{
		return JRStyleResolver.getRightPadding(this);
	}

	/**
	 *
	 */
	public Integer getOwnRightPadding()
	{
		return rightPadding;
	}

	/**
	 *
	 */
	public void setRightPadding(int rightPadding)
	{
		setRightPadding(Integer.valueOf(rightPadding));
	}

	/**
	 *
	 */
	public void setRightPadding(Integer rightPadding)
	{
		Object old = this.rightPadding;
		this.rightPadding = rightPadding;
		getEventSupport().firePropertyChange(PROPERTY_RIGHT_PADDING, old, this.rightPadding);
	}


	/**
	 * 
	 */
	public JRLineBox clone(JRBoxContainer boxContainer)
	{
		JRBaseLineBox clone = null;
		
		try
		{
			clone = (JRBaseLineBox)super.clone();
		}
		catch(CloneNotSupportedException e)
		{
			throw new JRRuntimeException(e);
		}
		
		clone.boxContainer = boxContainer;
		
		clone.pen = pen.clone(clone);
		clone.topPen = topPen.clone(clone);
		clone.leftPen = leftPen.clone(clone);
		clone.bottomPen = bottomPen.clone(clone);
		clone.rightPen = rightPen.clone(clone);
		
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
		hash.addIdentical(pen);
		hash.addIdentical(topPen);
		hash.addIdentical(leftPen);
		hash.addIdentical(bottomPen);
		hash.addIdentical(rightPen);
		hash.add(padding);
		hash.add(topPadding);
		hash.add(leftPadding);
		hash.add(bottomPadding);
		hash.add(rightPadding);
		return hash.getHashCode();
	}

	public boolean isIdentical(Object object)
	{
		if (this == object)
		{
			return true;
		}
		
		if (!(object instanceof JRBaseLineBox))
		{
			return false;
		}
		
		JRBaseLineBox box = (JRBaseLineBox) object;

		return 
				ObjectUtils.identical(pen, box.pen)
				&& ObjectUtils.identical(topPen, box.topPen)
				&& ObjectUtils.identical(leftPen, box.leftPen)
				&& ObjectUtils.identical(bottomPen, box.bottomPen)
				&& ObjectUtils.identical(rightPen, box.rightPen)
				&& ObjectUtils.equals(padding, box.padding)
				&& ObjectUtils.equals(topPadding, box.topPadding)
				&& ObjectUtils.equals(leftPadding, box.leftPadding)
				&& ObjectUtils.equals(bottomPadding, box.bottomPadding)
				&& ObjectUtils.equals(rightPadding, box.rightPadding);
	}

}
