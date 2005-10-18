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
 * 185, Berry Street, Suite 6200
 * San Francisco CA 94107
 * http://www.jaspersoft.com
 */
package net.sf.jasperreports.engine.fill;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import net.sf.jasperreports.engine.JRBox;
import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRPrintElement;

/**
 * Implementation of {@link net.sf.jasperreports.engine.fill.JRPrintFrame JRPrintFrame} that uses
 * {@link net.sf.jasperreports.engine.fill.JRTemplateFrame template frames} for keeping common data. 
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public class JRTemplatePrintFrame extends JRTemplatePrintElement implements JRPrintFrame, JRPrintElementContainer
{
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

	protected JRTemplateFrame templateFrame;
	private List elements;

	protected JRTemplatePrintFrame(JRTemplateFrame templateFrame)
	{
		super(templateFrame);
		
		this.templateFrame = templateFrame;
		elements = new ArrayList();
	}

	public JRBox getBox()
	{
		return templateFrame.getBox();
	}

	public List getElements()
	{
		return elements;
	}

	public void addElement(JRPrintElement element)
	{
		elements.add(element);
	}

	/**
	 *
	 */
	public byte getBorder()
	{
		return templateFrame.getBorder();
	}

	public Byte getOwnBorder()
	{
		return templateFrame.getOwnBorder();
	}

	/**
	 *
	 */
	public void setBorder(byte border)
	{
	}

	/**
	 *
	 */
	public Color getBorderColor()
	{
		return templateFrame.getBorderColor();
	}

	public Color getOwnBorderColor()
	{
		return templateFrame.getOwnBorderColor();
	}

	/**
	 *
	 */
	public void setBorderColor(Color borderColor)
	{
	}

	/**
	 *
	 */
	public int getPadding()
	{
		return templateFrame.getPadding();
	}

	public Integer getOwnPadding()
	{
		return templateFrame.getOwnPadding();
	}

	/**
	 *
	 */
	public void setPadding(int padding)
	{
	}

	/**
	 *
	 */
	public byte getTopBorder()
	{
		return templateFrame.getTopBorder();
	}

	/**
	 *
	 */
	public Byte getOwnTopBorder()
	{
		return templateFrame.getOwnTopBorder();
	}

	/**
	 *
	 */
	public void setTopBorder(byte topBorder)
	{
	}

	/**
	 *
	 */
	public Color getTopBorderColor()
	{
		return templateFrame.getTopBorderColor();
	}

	/**
	 *
	 */
	public Color getOwnTopBorderColor()
	{
		return templateFrame.getOwnTopBorderColor();
	}

	/**
	 *
	 */
	public void setTopBorderColor(Color topBorderColor)
	{
	}

	/**
	 *
	 */
	public int getTopPadding()
	{
		return templateFrame.getTopPadding();
	}

	/**
	 *
	 */
	public Integer getOwnTopPadding()
	{
		return templateFrame.getOwnTopPadding();
	}

	/**
	 *
	 */
	public void setTopPadding(int topPadding)
	{
	}

	/**
	 *
	 */
	public byte getLeftBorder()
	{
		return templateFrame.getLeftBorder();
	}

	/**
	 *
	 */
	public Byte getOwnLeftBorder()
	{
		return templateFrame.getOwnLeftBorder();
	}

	/**
	 *
	 */
	public void setLeftBorder(byte leftBorder)
	{
	}

	/**
	 *
	 */
	public Color getLeftBorderColor()
	{
		return templateFrame.getLeftBorderColor();
	}

	/**
	 *
	 */
	public Color getOwnLeftBorderColor()
	{
		return templateFrame.getOwnLeftBorderColor();
	}

	/**
	 *
	 */
	public void setLeftBorderColor(Color leftBorderColor)
	{
	}

	/**
	 *
	 */
	public int getLeftPadding()
	{
		return templateFrame.getLeftPadding();
	}

	/**
	 *
	 */
	public Integer getOwnLeftPadding()
	{
		return templateFrame.getOwnLeftPadding();
	}

	/**
	 *
	 */
	public void setLeftPadding(int leftPadding)
	{
	}

	/**
	 *
	 */
	public byte getBottomBorder()
	{
		return templateFrame.getBottomBorder();
	}

	/**
	 *
	 */
	public Byte getOwnBottomBorder()
	{
		return templateFrame.getOwnBottomBorder();
	}

	/**
	 *
	 */
	public void setBottomBorder(byte bottomBorder)
	{
	}

	/**
	 *
	 */
	public Color getBottomBorderColor()
	{
		return templateFrame.getBottomBorderColor();
	}

	/**
	 *
	 */
	public Color getOwnBottomBorderColor()
	{
		return templateFrame.getOwnBottomBorderColor();
	}

	/**
	 *
	 */
	public void setBottomBorderColor(Color bottomBorderColor)
	{
	}

	/**
	 *
	 */
	public int getBottomPadding()
	{
		return templateFrame.getBottomPadding();
	}

	/**
	 *
	 */
	public Integer getOwnBottomPadding()
	{
		return templateFrame.getOwnBottomPadding();
	}

	/**
	 *
	 */
	public void setBottomPadding(int bottomPadding)
	{
	}

	/**
	 *
	 */
	public byte getRightBorder()
	{
		return templateFrame.getRightBorder();
	}

	/**
	 *
	 */
	public Byte getOwnRightBorder()
	{
		return templateFrame.getOwnRightBorder();
	}

	/**
	 *
	 */
	public void setRightBorder(byte rightBorder)
	{
	}

	/**
	 *
	 */
	public Color getRightBorderColor()
	{
		return templateFrame.getRightBorderColor();
	}

	/**
	 *
	 */
	public Color getOwnRightBorderColor()
	{
		return templateFrame.getOwnRightBorderColor();
	}

	/**
	 *
	 */
	public void setRightBorderColor(Color rightBorderColor)
	{
	}

	/**
	 *
	 */
	public int getRightPadding()
	{
		return templateFrame.getRightPadding();
	}

	/**
	 *
	 */
	public Integer getOwnRightPadding()
	{
		return templateFrame.getOwnRightPadding();
	}

	/**
	 *
	 */
	public void setRightPadding(int rightPadding)
	{
	}

	/**
	 *
	 */
	public void setBorder(Byte border)
	{
	}

	/**
	 *
	 */
	public void setPadding(Integer padding)
	{
	}

	/**
	 *
	 */
	public void setTopBorder(Byte topBorder)
	{
	}

	/**
	 *
	 */
	public void setTopPadding(Integer topPadding)
	{
	}

	/**
	 *
	 */
	public void setLeftBorder(Byte leftBorder)
	{
	}

	/**
	 *
	 */
	public void setLeftPadding(Integer leftPadding)
	{
	}

	/**
	 *
	 */
	public void setBottomBorder(Byte bottomBorder)
	{
	}

	/**
	 *
	 */
	public void setBottomPadding(Integer bottomPadding)
	{
	}

	/**
	 *
	 */
	public void setRightBorder(Byte rightBorder)
	{
	}

	/**
	 *
	 */
	public void setRightPadding(Integer rightPadding)
	{
	}
}
