/*
 * ============================================================================
 * GNU Lesser General Public License
 * ============================================================================
 *
 * JasperReports - Free Java report-generating library.
 * Copyright (C) 2001-2005 JasperSoft Corporation http://www.jaspersoft.com
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
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sf.jasperreports.engine.JRAbstractObjectFactory;
import net.sf.jasperreports.engine.JRChild;
import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRElement;
import net.sf.jasperreports.engine.JRExpressionCollector;
import net.sf.jasperreports.engine.JRFrame;
import net.sf.jasperreports.engine.util.JRStyleResolver;
import net.sf.jasperreports.engine.xml.JRXmlWriter;

/**
 * Base read-only implementation of {@link net.sf.jasperreports.engine.JRFrame JRFrame}.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public class JRBaseFrame extends JRBaseElement implements JRFrame
{
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;
	
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
				children.add(child.getCopy(factory));
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

	public JRChild getCopy(JRAbstractObjectFactory factory)
	{
		return factory.getFrame(this);
	}

	public void writeXml(JRXmlWriter writer) throws IOException
	{
		writer.writeFrame(this);
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
		this.border = new Byte(border);
	}

	public void setBorder(Byte border)
	{
		this.border = border;
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
		this.borderColor = borderColor;
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
		this.padding = new Integer(padding);
	}

	public void setPadding(Integer padding)
	{
		this.padding = padding;
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
		this.topBorder = new Byte(topBorder);
	}

	public void setTopBorder(Byte topBorder)
	{
		this.topBorder = topBorder;
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
		this.topBorderColor = topBorderColor;
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
		this.topPadding = new Integer(topPadding);
	}

	public void setTopPadding(Integer topPadding)
	{
		this.topPadding = topPadding;
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
		this.leftBorder = new Byte(leftBorder);
	}

	public void setLeftBorder(Byte leftBorder)
	{
		this.leftBorder = leftBorder;
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
		this.leftBorderColor = leftBorderColor;
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
		this.leftPadding = new Integer(leftPadding);
	}

	public void setLeftPadding(Integer leftPadding)
	{
		this.leftPadding = leftPadding;
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
		this.bottomBorder = new Byte(bottomBorder);
	}

	public void setBottomBorder(Byte bottomBorder)
	{
		this.bottomBorder = bottomBorder;
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
		this.bottomBorderColor = bottomBorderColor;
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
		this.bottomPadding = new Integer(bottomPadding);
	}

	public void setBottomPadding(Integer bottomPadding)
	{
		this.bottomPadding = bottomPadding;
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
		this.rightBorder = new Byte(rightBorder);
	}

	public void setRightBorder(Byte rightBorder)
	{
		this.rightBorder = rightBorder;
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
		this.rightBorderColor = rightBorderColor;
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
		this.rightPadding = new Integer(rightPadding);
	}

	public void setRightPadding(Integer rightPadding)
	{
		this.rightPadding = rightPadding;
	}
	
	public byte getMode()
	{
		return JRStyleResolver.getMode(this, MODE_TRANSPARENT);
	}
}
