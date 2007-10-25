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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sf.jasperreports.engine.JRChild;
import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRElement;
import net.sf.jasperreports.engine.JRExpressionCollector;
import net.sf.jasperreports.engine.JRFrame;
import net.sf.jasperreports.engine.JRVisitor;
import net.sf.jasperreports.engine.util.JRStyleResolver;

/**
 * Base read-only implementation of {@link net.sf.jasperreports.engine.JRFrame JRFrame}.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public class JRBaseFrame extends JRBaseElement implements JRFrame
{
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;
	
	public static final String PROPERTY_BORDER = JRBaseStyle.PROPERTY_BORDER;
	
	public static final String PROPERTY_BORDER_COLOR = JRBaseStyle.PROPERTY_BORDER_COLOR;
	
	public static final String PROPERTY_BOTTOM_BORDER = JRBaseStyle.PROPERTY_BOTTOM_BORDER;
	
	public static final String PROPERTY_BOTTOM_BORDER_COLOR = JRBaseStyle.PROPERTY_BOTTOM_BORDER_COLOR;
	
	public static final String PROPERTY_BOTTOM_PADDING = JRBaseStyle.PROPERTY_BOTTOM_PADDING;
	
	public static final String PROPERTY_LEFT_BORDER = JRBaseStyle.PROPERTY_LEFT_BORDER;
	
	public static final String PROPERTY_LEFT_BORDER_COLOR = JRBaseStyle.PROPERTY_LEFT_BORDER_COLOR;
	
	public static final String PROPERTY_LEFT_PADDING = JRBaseStyle.PROPERTY_LEFT_PADDING;
	
	public static final String PROPERTY_PADDING = JRBaseStyle.PROPERTY_PADDING;
	
	public static final String PROPERTY_RIGHT_BORDER = JRBaseStyle.PROPERTY_RIGHT_BORDER;
	
	public static final String PROPERTY_RIGHT_BORDER_COLOR = JRBaseStyle.PROPERTY_RIGHT_BORDER_COLOR;
	
	public static final String PROPERTY_RIGHT_PADDING = JRBaseStyle.PROPERTY_RIGHT_PADDING;
	
	public static final String PROPERTY_TOP_BORDER = JRBaseStyle.PROPERTY_TOP_BORDER;
	
	public static final String PROPERTY_TOP_BORDER_COLOR = JRBaseStyle.PROPERTY_TOP_BORDER_COLOR;
	
	public static final String PROPERTY_TOP_PADDING = JRBaseStyle.PROPERTY_TOP_PADDING;
	
	protected List children;

	public JRBaseFrame(JRFrame frame, JRBaseObjectFactory factory)
	{
		super(frame, factory);

		List frameChildren = frame.getChildren();
		if (frameChildren != null)
		{
			children = new ArrayList(frameChildren.size());
			for (Iterator it = frameChildren.iterator(); it.hasNext();)
			{
				JRChild child = (JRChild) it.next();
				children.add(factory.getVisitResult(child));
			}
		}
		
		copyBox(frame);
	}

	private void copyBox(JRFrame frame)
	{
		border = frame.getOwnBorder();
		topBorder = frame.getOwnTopBorder();
		leftBorder = frame.getOwnLeftBorder();
		bottomBorder = frame.getOwnBottomBorder();
		rightBorder = frame.getOwnRightBorder();
		borderColor = frame.getOwnBorderColor();
		topBorderColor = frame.getOwnTopBorderColor();
		leftBorderColor = frame.getOwnLeftBorderColor();
		bottomBorderColor = frame.getOwnBottomBorderColor();
		rightBorderColor = frame.getOwnRightBorderColor();
		padding = frame.getOwnPadding();
		topPadding = frame.getOwnTopPadding();
		leftPadding = frame.getOwnLeftPadding();
		bottomPadding = frame.getOwnBottomPadding();
		rightPadding = frame.getOwnRightPadding();
	}

	public JRElement[] getElements()
	{
		return JRBaseElementGroup.getElements(children);
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
		visitor.visitFrame(this);
	}
	
	public List getChildren()
	{
		return children;
	}

	public JRElement getElementByKey(String elementKey)
	{
		return JRBaseElementGroup.getElementByKey(getElements(), elementKey);
	}


	// box
	protected Byte border;
	protected Byte topBorder = null;
	protected Byte leftBorder = null;
	protected Byte bottomBorder = null;
	protected Byte rightBorder = null;
	protected Color borderColor = null;
	protected Color topBorderColor = null;
	protected Color leftBorderColor = null;
	protected Color bottomBorderColor = null;
	protected Color rightBorderColor = null;
	protected Integer padding;
	protected Integer topPadding = null;
	protected Integer leftPadding = null;
	protected Integer bottomPadding = null;
	protected Integer rightPadding = null;


	public byte getBorder()
	{
		return JRStyleResolver.getBorder(this);
	}

	public Byte getOwnBorder()
	{
		return border;
	}

	public void setBorder(byte border)
	{
		setBorder(new Byte(border));
	}

	public void setBorder(Byte border)
	{
		Object old = this.border;
		this.border = border;
		getEventSupport().firePropertyChange(PROPERTY_BORDER, old, this.border);
	}

	public Color getBorderColor()
	{
		return JRStyleResolver.getBorderColor(this, getForecolor());
	}

	public Color getOwnBorderColor()
	{
		return borderColor;
	}

	public void setBorderColor(Color borderColor)
	{
		Object old = this.borderColor;
		this.borderColor = borderColor;
		getEventSupport().firePropertyChange(PROPERTY_BORDER_COLOR, old, this.borderColor);
	}

	public int getPadding()
	{
		return JRStyleResolver.getPadding(this);
	}

	public Integer getOwnPadding()
	{
		return padding;
	}

	public void setPadding(int padding)
	{
		setPadding(new Integer(padding));
	}

	public void setPadding(Integer padding)
	{
		Object old = this.padding;
		this.padding = padding;
		getEventSupport().firePropertyChange(PROPERTY_PADDING, old, this.padding);
	}

	public byte getTopBorder()
	{
		return JRStyleResolver.getTopBorder(this);
	}

	public Byte getOwnTopBorder()
	{
		return topBorder;
	}

	public void setTopBorder(byte topBorder)
	{
		setTopBorder(new Byte(topBorder));
	}

	public void setTopBorder(Byte topBorder)
	{
		Object old = this.topBorder;
		this.topBorder = topBorder;
		getEventSupport().firePropertyChange(PROPERTY_TOP_BORDER, old, this.topBorder);
	}

	public Color getTopBorderColor()
	{
		return JRStyleResolver.getTopBorderColor(this, getForecolor());
	}

	public Color getOwnTopBorderColor()
	{
		return topBorderColor;
	}

	public void setTopBorderColor(Color topBorderColor)
	{
		Object old = this.topBorderColor;
		this.topBorderColor = topBorderColor;
		getEventSupport().firePropertyChange(PROPERTY_TOP_BORDER_COLOR, old, this.topBorderColor);
	}

	public int getTopPadding()
	{
		return JRStyleResolver.getTopPadding(this);
	}

	public Integer getOwnTopPadding()
	{
		return topPadding;
	}

	public void setTopPadding(int topPadding)
	{
		setTopPadding(new Integer(topPadding));
	}

	public void setTopPadding(Integer topPadding)
	{
		Object old = this.topPadding;
		this.topPadding = topPadding;
		getEventSupport().firePropertyChange(PROPERTY_TOP_PADDING, old, this.topPadding);
	}

	public byte getLeftBorder()
	{
		return JRStyleResolver.getLeftBorder(this);
	}

	public Byte getOwnLeftBorder()
	{
		return leftBorder;
	}

	public void setLeftBorder(byte leftBorder)
	{
		setLeftBorder(new Byte(leftBorder));
	}

	public void setLeftBorder(Byte leftBorder)
	{
		Object old = this.leftBorder;
		this.leftBorder = leftBorder;
		getEventSupport().firePropertyChange(PROPERTY_LEFT_BORDER, old, this.leftBorder);
	}

	public Color getLeftBorderColor()
	{
		return JRStyleResolver.getLeftBorderColor(this, getForecolor());
	}

	public Color getOwnLeftBorderColor()
	{
		return leftBorderColor;
	}

	public void setLeftBorderColor(Color leftBorderColor)
	{
		Object old = this.leftBorderColor;
		this.leftBorderColor = leftBorderColor;
		getEventSupport().firePropertyChange(PROPERTY_LEFT_BORDER_COLOR, old, this.leftBorderColor);
	}

	public int getLeftPadding()
	{
		return JRStyleResolver.getLeftPadding(this);
	}

	public Integer getOwnLeftPadding()
	{
		return leftPadding;
	}

	public void setLeftPadding(int leftPadding)
	{
		setLeftPadding(new Integer(leftPadding));
	}

	public void setLeftPadding(Integer leftPadding)
	{
		Object old = this.leftPadding;
		this.leftPadding = leftPadding;
		getEventSupport().firePropertyChange(PROPERTY_LEFT_PADDING, old, this.leftPadding);
	}

	public byte getBottomBorder()
	{
		return JRStyleResolver.getBottomBorder(this);
	}

	public Byte getOwnBottomBorder()
	{
		return bottomBorder;
	}

	public void setBottomBorder(byte bottomBorder)
	{
		setBottomBorder(new Byte(bottomBorder));
	}

	public void setBottomBorder(Byte bottomBorder)
	{
		Object old = this.bottomBorder;
		this.bottomBorder = bottomBorder;
		getEventSupport().firePropertyChange(PROPERTY_BOTTOM_BORDER, old, this.bottomBorder);
	}

	public Color getBottomBorderColor()
	{
		return JRStyleResolver.getBottomBorderColor(this, getForecolor());
	}

	public Color getOwnBottomBorderColor()
	{
		return bottomBorderColor;
	}

	public void setBottomBorderColor(Color bottomBorderColor)
	{
		Object old = this.bottomBorderColor;
		this.bottomBorderColor = bottomBorderColor;
		getEventSupport().firePropertyChange(PROPERTY_BOTTOM_BORDER_COLOR, old, this.bottomBorderColor);
	}

	public int getBottomPadding()
	{
		return JRStyleResolver.getBottomPadding(this);
	}

	public Integer getOwnBottomPadding()
	{
		return bottomPadding;
	}

	public void setBottomPadding(int bottomPadding)
	{
		setBottomPadding(new Integer(bottomPadding));
	}

	public void setBottomPadding(Integer bottomPadding)
	{
		Object old = this.bottomPadding;
		this.bottomPadding = bottomPadding;
		getEventSupport().firePropertyChange(PROPERTY_BOTTOM_PADDING, old, this.bottomPadding);
	}

	public byte getRightBorder()
	{
		return JRStyleResolver.getRightBorder(this);
	}

	public Byte getOwnRightBorder()
	{
		return rightBorder;
	}

	public void setRightBorder(byte rightBorder)
	{
		setRightBorder(new Byte(rightBorder));
	}

	public void setRightBorder(Byte rightBorder)
	{
		Object old = this.rightBorder;
		this.rightBorder = rightBorder;
		getEventSupport().firePropertyChange(PROPERTY_RIGHT_BORDER, old, this.rightBorder);
	}

	public Color getRightBorderColor()
	{
		return JRStyleResolver.getRightBorderColor(this, getForecolor());
	}

	public Color getOwnRightBorderColor()
	{
		return rightBorderColor;
	}

	public void setRightBorderColor(Color rightBorderColor)
	{
		Object old = this.rightBorderColor;
		this.rightBorderColor = rightBorderColor;
		getEventSupport().firePropertyChange(PROPERTY_RIGHT_BORDER_COLOR, old, this.rightBorderColor);
	}

	public int getRightPadding()
	{
		return JRStyleResolver.getRightPadding(this);
	}

	public Integer getOwnRightPadding()
	{
		return rightPadding;
	}

	public void setRightPadding(int rightPadding)
	{
		setRightPadding(new Integer(rightPadding));
	}

	public void setRightPadding(Integer rightPadding)
	{
		Object old = this.rightPadding;
		this.rightPadding = rightPadding;
		getEventSupport().firePropertyChange(PROPERTY_RIGHT_PADDING, old, this.rightPadding);
	}
	
	public byte getMode()
	{
		return JRStyleResolver.getMode(this, MODE_TRANSPARENT);
	}
}
