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
package net.sf.jasperreports.engine.fill;

import java.awt.Color;

import net.sf.jasperreports.engine.JRAnchor;
import net.sf.jasperreports.engine.JRBox;
import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRPrintImage;
import net.sf.jasperreports.engine.JRRenderable;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JRTemplatePrintImage extends JRTemplatePrintGraphicElement implements JRPrintImage
{


	/**
	 *
	 */
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

	/**
	 *
	 */
	private JRRenderable renderer = null;
	private String anchorName = null;
	private String hyperlinkReference = null;
	private String hyperlinkAnchor = null;
	private Integer hyperlinkPage = null;

	/**
	 * The bookmark level for the anchor associated with this field.
	 * @see JRAnchor#getBookmarkLevel()
	 */
	protected int bookmarkLevel = JRAnchor.NO_BOOKMARK;


	/**
	 *
	 */
	public JRTemplatePrintImage(JRTemplateImage image)
	{
		super(image);
	}

	/**
	 *
	 */
	public JRRenderable getRenderer()
	{
		return this.renderer;
	}
		
	/**
	 *
	 */
	public void setRenderer(JRRenderable renderer)
	{
		this.renderer = renderer;
	}
		
	/**
	 *
	 */
	public byte getScaleImage()
	{
		return ((JRTemplateImage)this.template).getScaleImage();
	}

	/**
	 *
	 */
	public Byte getOwnScaleImage()
	{
		return ((JRTemplateImage)this.template).getOwnScaleImage();
	}

	/**
	 *
	 */
	public void setScaleImage(byte scaleImage)
	{
	}

	/**
	 *
	 */
	public void setScaleImage(Byte scaleImage)
	{
	}

	/**
	 *
	 */
	public byte getHorizontalAlignment()
	{
		return ((JRTemplateImage)this.template).getHorizontalAlignment();
	}
		
	/**
	 *
	 */
	public Byte getOwnHorizontalAlignment()
	{
		return ((JRTemplateImage)this.template).getOwnHorizontalAlignment();
	}
		
	/**
	 *
	 */
	public void setHorizontalAlignment(byte horizontalAlignment)
	{
	}
		
	/**
	 *
	 */
	public void setHorizontalAlignment(Byte horizontalAlignment)
	{
	}
		
	/**
	 *
	 */
	public byte getVerticalAlignment()
	{
		return ((JRTemplateImage)this.template).getVerticalAlignment();
	}
		
	/**
	 *
	 */
	public Byte getOwnVerticalAlignment()
	{
		return ((JRTemplateImage)this.template).getOwnVerticalAlignment();
	}
		
	/**
	 *
	 */
	public void setVerticalAlignment(byte verticalAlignment)
	{
	}
		
	/**
	 *
	 */
	public void setVerticalAlignment(Byte verticalAlignment)
	{
	}
		
	/**
	 *
	 */
	public boolean isLazy()
	{
		return ((JRTemplateImage)this.template).isLazy();
	}

	/**
	 *
	 */
	public void setLazy(boolean isLazy)
	{
	}

	/**
	 *
	 */
	public byte getOnErrorType()
	{
		return ((JRTemplateImage)this.template).getOnErrorType();
	}

	/**
	 *
	 */
	public void setOnErrorType(byte onErrorType)
	{
	}

	/**
	 * @deprecated
	 */
	public JRBox getBox()
	{
		return ((JRTemplateImage)template).getBox();
	}
		
	/**
	 * @deprecated
	 */
	public void setBox(JRBox box)
	{
	}

	/**
	 *
	 */
	public String getAnchorName()
	{
		return this.anchorName;
	}
		
	/**
	 *
	 */
	public void setAnchorName(String anchorName)
	{
		this.anchorName = anchorName;
	}
		
	/**
	 *
	 */
	public byte getHyperlinkType()
	{
		return ((JRTemplateImage)this.template).getHyperlinkType();
	}
		
	/**
	 *
	 */
	public void setHyperlinkType(byte hyperlinkType)
	{
	}

	/**
	 *
	 */
	public byte getHyperlinkTarget()
	{
		return ((JRTemplateImage)this.template).getHyperlinkTarget();
	}
		
	/**
	 *
	 */
	public void setHyperlinkTarget(byte hyperlinkTarget)
	{
	}

	/**
	 *
	 */
	public String getHyperlinkReference()
	{
		return this.hyperlinkReference;
	}
		
	/**
	 *
	 */
	public void setHyperlinkReference(String hyperlinkReference)
	{
		this.hyperlinkReference = hyperlinkReference;
	}
		
	/**
	 *
	 */
	public String getHyperlinkAnchor()
	{
		return this.hyperlinkAnchor;
	}
		
	/**
	 *
	 */
	public void setHyperlinkAnchor(String hyperlinkAnchor)
	{
		this.hyperlinkAnchor = hyperlinkAnchor;
	}
		
	/**
	 *
	 */
	public Integer getHyperlinkPage()
	{
		return this.hyperlinkPage;
	}
		
	/**
	 *
	 */
	public void setHyperlinkPage(Integer hyperlinkPage)
	{
		this.hyperlinkPage = hyperlinkPage;
	}


	public int getBookmarkLevel()
	{
		return bookmarkLevel;
	}


	public void setBookmarkLevel(int bookmarkLevel)
	{
		this.bookmarkLevel = bookmarkLevel;
	}
	

	/**
	 *
	 */
	public byte getBorder()
	{
		return ((JRTemplateImage)template).getBorder();
	}

	public Byte getOwnBorder()
	{
		return ((JRTemplateImage)template).getOwnBorder();
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
		return ((JRTemplateImage)template).getBorderColor();
	}

	public Color getOwnBorderColor()
	{
		return ((JRTemplateImage)template).getOwnBorderColor();
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
		return ((JRTemplateImage)template).getPadding();
	}

	public Integer getOwnPadding()
	{
		return ((JRTemplateImage)template).getOwnPadding();
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
		return ((JRTemplateImage)template).getTopBorder();
	}

	/**
	 *
	 */
	public Byte getOwnTopBorder()
	{
		return ((JRTemplateImage)template).getOwnTopBorder();
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
		return ((JRTemplateImage)template).getTopBorderColor();
	}

	/**
	 *
	 */
	public Color getOwnTopBorderColor()
	{
		return ((JRTemplateImage)template).getOwnTopBorderColor();
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
		return ((JRTemplateImage)template).getTopPadding();
	}

	/**
	 *
	 */
	public Integer getOwnTopPadding()
	{
		return ((JRTemplateImage)template).getOwnTopPadding();
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
		return ((JRTemplateImage)template).getLeftBorder();
	}

	/**
	 *
	 */
	public Byte getOwnLeftBorder()
	{
		return ((JRTemplateImage)template).getOwnLeftBorder();
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
		return ((JRTemplateImage)template).getLeftBorderColor();
	}

	/**
	 *
	 */
	public Color getOwnLeftBorderColor()
	{
		return ((JRTemplateImage)template).getOwnLeftBorderColor();
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
		return ((JRTemplateImage)template).getLeftPadding();
	}

	/**
	 *
	 */
	public Integer getOwnLeftPadding()
	{
		return ((JRTemplateImage)template).getOwnLeftPadding();
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
		return ((JRTemplateImage)template).getBottomBorder();
	}

	/**
	 *
	 */
	public Byte getOwnBottomBorder()
	{
		return ((JRTemplateImage)template).getOwnBottomBorder();
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
		return ((JRTemplateImage)template).getBottomBorderColor();
	}

	/**
	 *
	 */
	public Color getOwnBottomBorderColor()
	{
		return ((JRTemplateImage)template).getOwnBottomBorderColor();
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
		return ((JRTemplateImage)template).getBottomPadding();
	}

	/**
	 *
	 */
	public Integer getOwnBottomPadding()
	{
		return ((JRTemplateImage)template).getOwnBottomPadding();
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
		return ((JRTemplateImage)template).getRightBorder();
	}

	/**
	 *
	 */
	public Byte getOwnRightBorder()
	{
		return ((JRTemplateImage)template).getOwnRightBorder();
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
		return ((JRTemplateImage)template).getRightBorderColor();
	}

	/**
	 *
	 */
	public Color getOwnRightBorderColor()
	{
		return ((JRTemplateImage)template).getOwnRightBorderColor();
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
		return ((JRTemplateImage)template).getRightPadding();
	}

	/**
	 *
	 */
	public Integer getOwnRightPadding()
	{
		return ((JRTemplateImage)template).getOwnRightPadding();
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
