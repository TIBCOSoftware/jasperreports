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

import net.sf.jasperreports.engine.JRBox;
import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRDefaultStyleProvider;
import net.sf.jasperreports.engine.JRGraphicElement;
import net.sf.jasperreports.engine.JRStyle;
import net.sf.jasperreports.engine.util.JRStyleResolver;


/**
 * This is useful for drawing borders around text elements and images. Boxes can have borders and paddings, which can
 * have different width and colour on each side of the element.
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JRBaseBox implements JRBox, Serializable
{


	/**
	 *
	 */
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

	/**
	 *
	 */
	protected Byte border = null;
	protected Byte topBorder = null;
	protected Byte leftBorder = null;
	protected Byte bottomBorder = null;
	protected Byte rightBorder = null;
	protected Color borderColor = null;
	protected Color topBorderColor = null;
	protected Color leftBorderColor = null;
	protected Color bottomBorderColor = null;
	protected Color rightBorderColor = null;
	protected Integer padding = null;
	protected Integer topPadding = null;
	protected Integer leftPadding = null;
	protected Integer bottomPadding = null;
	protected Integer rightPadding = null;

	
	/**
	 *
	 */
	public JRBaseBox()
	{
		super();
	}
	
	
	public JRBaseBox(byte pen, Color color)
	{
		setBorder(pen);
		setBorderColor(color);
	}

	/**
	 * Creates a clone of a box object.
	 * 
	 * @param box the object to be cloned
	 */
	public JRBaseBox(JRBox box)
	{
		border = box.getOwnBorder();
		borderColor = box.getOwnBorderColor();
		padding = box.getOwnPadding();
		
		topBorder = box.getOwnTopBorder();
		topBorderColor = box.getOwnTopBorderColor();
		topPadding = box.getOwnTopPadding();
		
		leftBorder = box.getOwnLeftBorder();
		leftBorderColor = box.getOwnLeftBorderColor();
		leftPadding = box.getOwnLeftPadding();
		
		bottomBorder = box.getOwnBottomBorder();
		bottomBorderColor = box.getOwnBottomBorderColor();
		bottomPadding = box.getOwnBottomPadding();
		
		rightBorder = box.getOwnRightBorder();
		rightBorderColor = box.getOwnRightBorderColor();
		rightPadding = box.getOwnRightPadding();
	}
	

	/**
	 * Creates a copy of a box object by keeping only some of the border sides.
	 * 
	 * @param box the object to be cloned
	 * @param left whether to keep the left border
	 * @param right whether to keep the right border
	 * @param top whether to keep the top border
	 * @param bottom whether to keep the bottom border
	 * @param complementaryBox complementary box
	 */
	public JRBaseBox(JRBox box, boolean left, boolean right, boolean top, boolean bottom, JRBox complementaryBox)
	{
		if (left && box.getLeftBorder() != JRGraphicElement.PEN_NONE)
		{
			leftBorder = new Byte(box.getLeftBorder());
			leftBorderColor = box.getLeftBorderColor();
			leftPadding = new Integer(box.getLeftPadding());
		}
		else if (complementaryBox != null)
		{
			leftBorder = new Byte(complementaryBox.getLeftBorder());
			leftBorderColor = complementaryBox.getLeftBorderColor();
			leftPadding = new Integer(complementaryBox.getLeftPadding());
		}
		
		if (right && box.getRightBorder() != JRGraphicElement.PEN_NONE)
		{
			rightBorder = new Byte(box.getRightBorder());
			rightBorderColor = box.getRightBorderColor();
			rightPadding = new Integer(box.getRightPadding());
		}
		else if (complementaryBox != null)
		{
			rightBorder = new Byte(complementaryBox.getRightBorder());
			rightBorderColor = complementaryBox.getRightBorderColor();
			rightPadding = new Integer(complementaryBox.getRightPadding());
		}
		
		if (top && box.getTopBorder() != JRGraphicElement.PEN_NONE)
		{
			topBorder = new Byte(box.getTopBorder());
			topBorderColor = box.getTopBorderColor();
			topPadding = new Integer(box.getTopPadding());
		}
		else if (complementaryBox != null)
		{
			topBorder = new Byte(complementaryBox.getTopBorder());
			topBorderColor = complementaryBox.getTopBorderColor();
			topPadding = new Integer(complementaryBox.getTopPadding());
		}
		
		if (bottom && box.getBottomBorder() != JRGraphicElement.PEN_NONE)
		{
			bottomBorder = new Byte(box.getBottomBorder());
			bottomBorderColor = box.getBottomBorderColor();
			bottomPadding = new Integer(box.getBottomPadding());
		}
		else if (complementaryBox != null)
		{
			bottomBorder = new Byte(complementaryBox.getBottomBorder());
			bottomBorderColor = complementaryBox.getBottomBorderColor();
			bottomPadding = new Integer(complementaryBox.getBottomPadding());
		}
	}

	
	public JRBaseBox(JRBox box, boolean resetLeft, boolean resetRight, boolean resetTop, boolean resetBottom)
	{
		border = box.getOwnBorder();
		borderColor = box.getOwnBorderColor();
		padding = box.getOwnPadding();
		
		if (resetLeft)
		{
			leftBorder = new Byte(JRGraphicElement.PEN_NONE);
			leftBorderColor = null;
			leftPadding = new Integer(0);
		}
		else
		{
			leftBorder = box.getOwnLeftBorder();
			leftBorderColor = box.getOwnLeftBorderColor();
			leftPadding = box.getOwnLeftPadding();
		}
		
		if (resetTop)
		{
			topBorder = new Byte(JRGraphicElement.PEN_NONE);
			topBorderColor = null;
			topPadding = new Integer(0);
		}
		else
		{
			topBorder = box.getOwnTopBorder();
			topBorderColor = box.getOwnTopBorderColor();
			topPadding = box.getOwnTopPadding();
		}
		
		if (resetRight)
		{
			rightBorder = new Byte(JRGraphicElement.PEN_NONE);
			rightBorderColor = null;
			rightPadding = new Integer(0);
		}
		else
		{
			rightBorder = box.getOwnRightBorder();
			rightBorderColor = box.getOwnRightBorderColor();
			rightPadding = box.getOwnRightPadding();
		}
		
		if (resetBottom)
		{
			bottomBorder = new Byte(JRGraphicElement.PEN_NONE);
			bottomBorderColor = null;
			bottomPadding = new Integer(0);
		}
		else
		{
			bottomBorder = box.getOwnBottomBorder();
			bottomBorderColor = box.getOwnBottomBorderColor();
			bottomPadding = box.getOwnBottomPadding();
		}
	}
	
	/**
	 *
	 */
	public JRDefaultStyleProvider getDefaultStyleProvider()
	{
		return null;
	}

	/**
	 *
	 */
	public JRStyle getStyle()
	{
		return null;
	}

	/**
	 *
	 */
	public byte getBorder()
	{
		return JRStyleResolver.getBorder(this);
	}

	/**
	 *
	 */
	public Byte getOwnBorder()
	{
		return border;
	}

	/**
	 *
	 */
	public void setBorder(byte border)
	{
		this.border = new Byte(border);
	}

	/**
	 *
	 */
	public void setBorder(Byte border)
	{
		this.border = border;
	}

	/**
	 *
	 */
	public Color getBorderColor()
	{
		return JRStyleResolver.getBorderColor(this, Color.black);
	}

	public Color getOwnBorderColor()
	{
		return borderColor;
	}
	
	/**
	 *
	 */
	public void setBorderColor(Color borderColor)
	{
		this.borderColor = borderColor;
	}

	/**
	 *
	 */
	public int getPadding()
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
		this.padding = new Integer(padding);
	}

	/**
	 *
	 */
	public void setPadding(Integer padding)
	{
		this.padding = padding;
	}

	/**
	 *
	 */
	public byte getTopBorder()
	{
		return JRStyleResolver.getTopBorder(this);
	}

	/**
	 *
	 */
	public Byte getOwnTopBorder()
	{
		return topBorder;
	}

	/**
	 *
	 */
	public void setTopBorder(byte topBorder)
	{
		this.topBorder = new Byte(topBorder);
	}

	/**
	 *
	 */
	public void setTopBorder(Byte topBorder)
	{
		this.topBorder = topBorder;
	}

	/**
	 *
	 */
	public Color getTopBorderColor()
	{
		return JRStyleResolver.getTopBorderColor(this, Color.black);
	}

	/**
	 *
	 */
	public Color getOwnTopBorderColor()
	{
		return topBorderColor;
	}

	/**
	 *
	 */
	public void setTopBorderColor(Color topBorderColor)
	{
		this.topBorderColor = topBorderColor;
	}

	/**
	 *
	 */
	public int getTopPadding()
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
		this.topPadding = new Integer(topPadding);
	}

	/**
	 *
	 */
	public void setTopPadding(Integer topPadding)
	{
		this.topPadding = topPadding;
	}

	/**
	 *
	 */
	public byte getLeftBorder()
	{
		return JRStyleResolver.getLeftBorder(this);
	}

	/**
	 *
	 */
	public Byte getOwnLeftBorder()
	{
		return leftBorder;
	}

	/**
	 *
	 */
	public void setLeftBorder(byte leftBorder)
	{
		this.leftBorder = new Byte(leftBorder);
	}

	/**
	 *
	 */
	public void setLeftBorder(Byte leftBorder)
	{
		this.leftBorder = leftBorder;
	}

	/**
	 *
	 */
	public Color getLeftBorderColor()
	{
		return JRStyleResolver.getLeftBorderColor(this, Color.black);
	}

	/**
	 *
	 */
	public Color getOwnLeftBorderColor()
	{
		return leftBorderColor;
	}

	/**
	 *
	 */
	public void setLeftBorderColor(Color leftBorderColor)
	{
		this.leftBorderColor = leftBorderColor;
	}

	/**
	 *
	 */
	public int getLeftPadding()
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
		this.leftPadding = new Integer(leftPadding);
	}

	/**
	 *
	 */
	public void setLeftPadding(Integer leftPadding)
	{
		this.leftPadding = leftPadding;
	}

	/**
	 *
	 */
	public byte getBottomBorder()
	{
		return JRStyleResolver.getBottomBorder(this);
	}

	/**
	 *
	 */
	public Byte getOwnBottomBorder()
	{
		return bottomBorder;
	}

	/**
	 *
	 */
	public void setBottomBorder(byte bottomBorder)
	{
		this.bottomBorder = new Byte(bottomBorder);
	}

	/**
	 *
	 */
	public void setBottomBorder(Byte bottomBorder)
	{
		this.bottomBorder = bottomBorder;
	}

	/**
	 *
	 */
	public Color getBottomBorderColor()
	{
		return JRStyleResolver.getBottomBorderColor(this, Color.black);
	}

	/**
	 *
	 */
	public Color getOwnBottomBorderColor()
	{
		return bottomBorderColor;
	}

	/**
	 *
	 */
	public void setBottomBorderColor(Color bottomBorderColor)
	{
		this.bottomBorderColor = bottomBorderColor;
	}

	/**
	 *
	 */
	public int getBottomPadding()
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
		this.bottomPadding = new Integer(bottomPadding);
	}

	/**
	 *
	 */
	public void setBottomPadding(Integer bottomPadding)
	{
		this.bottomPadding = bottomPadding;
	}

	/**
	 *
	 */
	public byte getRightBorder()
	{
		return JRStyleResolver.getRightBorder(this);
	}

	/**
	 *
	 */
	public Byte getOwnRightBorder()
	{
		return rightBorder;
	}

	/**
	 *
	 */
	public void setRightBorder(byte rightBorder)
	{
		this.rightBorder = new Byte(rightBorder);
	}

	/**
	 *
	 */
	public void setRightBorder(Byte rightBorder)
	{
		this.rightBorder = rightBorder;
	}

	/**
	 *
	 */
	public Color getRightBorderColor()
	{
		return JRStyleResolver.getRightBorderColor(this, Color.black);
	}

	/**
	 *
	 */
	public Color getOwnRightBorderColor()
	{
		return rightBorderColor;
	}

	/**
	 *
	 */
	public void setRightBorderColor(Color rightBorderColor)
	{
		this.rightBorderColor = rightBorderColor;
	}

	/**
	 *
	 */
	public int getRightPadding()
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
		this.rightPadding = new Integer(rightPadding);
	}

	/**
	 *
	 */
	public void setRightPadding(Integer rightPadding)
	{
		this.rightPadding = rightPadding;
	}


	public String getStyleNameReference()
	{
		return null;
	}


}
