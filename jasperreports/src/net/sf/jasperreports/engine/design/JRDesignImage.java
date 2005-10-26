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
package net.sf.jasperreports.engine.design;

import java.awt.Color;
import java.io.IOException;

import net.sf.jasperreports.engine.JRAbstractObjectFactory;
import net.sf.jasperreports.engine.JRAnchor;
import net.sf.jasperreports.engine.JRBox;
import net.sf.jasperreports.engine.JRChild;
import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRDefaultStyleProvider;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRExpressionCollector;
import net.sf.jasperreports.engine.JRGroup;
import net.sf.jasperreports.engine.JRHyperlink;
import net.sf.jasperreports.engine.JRImage;
import net.sf.jasperreports.engine.util.JRStyleResolver;
import net.sf.jasperreports.engine.xml.JRXmlWriter;


/**
 * The actual implementation of a graphic element representing an image, used at design time.
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JRDesignImage extends JRDesignGraphicElement implements JRImage
{


	/**
	 *
	 */
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

	/**
	 *
	 */
	protected Byte scaleImage;
	protected Byte horizontalAlignment;
	protected Byte verticalAlignment;
	protected Boolean isUsingCache = null;
	protected boolean isLazy = false;
	protected byte onErrorType = ON_ERROR_TYPE_ERROR;
	protected byte evaluationTime = JRExpression.EVALUATION_TIME_NOW;
	protected byte hyperlinkType = JRHyperlink.HYPERLINK_TYPE_NONE;
	protected byte hyperlinkTarget = JRHyperlink.HYPERLINK_TARGET_SELF;

	/**
	 *
	 */
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

	
	/**
	 *
	 */
//	protected JRBox box = null;

	/**
	 *
	 */
	protected JRGroup evaluationGroup = null;
	protected JRExpression expression = null;
	protected JRExpression anchorNameExpression = null;
	protected JRExpression hyperlinkReferenceExpression = null;
	protected JRExpression hyperlinkAnchorExpression = null;
	protected JRExpression hyperlinkPageExpression = null;

	/**
	 * The bookmark level for the anchor associated with this image.
	 * @see JRAnchor#getBookmarkLevel()
	 */
	protected int bookmarkLevel = JRAnchor.NO_BOOKMARK;


	/**
	 *
	 */
	public JRDesignImage(JRDefaultStyleProvider defaultStyleProvider)
	{
		super(defaultStyleProvider);
	}
		

	/**
	 *
	 */
	public byte getPen()
	{
		return JRStyleResolver.getPen(this, PEN_NONE);
	}

	/**
	 *
	 */
	public byte getMode()
	{
		return JRStyleResolver.getMode(this, MODE_TRANSPARENT);
	}


	/**
	 *
	 */
	public byte getScaleImage()
	{
		return JRStyleResolver.getScaleImage(this);
	}

	public Byte getOwnScaleImage()
	{
		return scaleImage;
	}

	/**
	 *
	 */
	public void setScaleImage(byte scaleImage)
	{
		this.scaleImage = new Byte(scaleImage);
	}

	/**
	 *
	 */
	public void setScaleImage(Byte scaleImage)
	{
		this.scaleImage = scaleImage;
	}

	/**
	 *
	 */
	public byte getHorizontalAlignment()
	{
		return JRStyleResolver.getHorizontalAlignment(this);
	}

	public Byte getOwnHorizontalAlignment()
	{
		return horizontalAlignment;
	}

	/**
	 *
	 */
	public void setHorizontalAlignment(byte horizontalAlignment)
	{
		this.horizontalAlignment = new Byte(horizontalAlignment);
	}

	/**
	 *
	 */
	public void setHorizontalAlignment(Byte horizontalAlignment)
	{
		this.horizontalAlignment = horizontalAlignment;
	}

	/**
	 *
	 */
	public byte getVerticalAlignment()
	{
		return JRStyleResolver.getVerticalAlignment(this);
	}

	public Byte getOwnVerticalAlignment()
	{
		return verticalAlignment;
	}

	/**
	 *
	 */
	public void setVerticalAlignment(byte verticalAlignment)
	{
		this.verticalAlignment = new Byte(verticalAlignment);
	}

	/**
	 *
	 */
	public void setVerticalAlignment(Byte verticalAlignment)
	{
		this.verticalAlignment = verticalAlignment;
	}

	/**
	 *
	 */
	public boolean isUsingCache()
	{
		if (isUsingCache == null)
		{
			if (getExpression() != null)
			{
				return String.class.getName().equals(getExpression().getValueClassName());
			}
			return true;
		}
		return isUsingCache.booleanValue();
	}

	/**
	 *
	 */
	public Boolean isOwnUsingCache()
	{
		return isUsingCache;
	}

	/**
	 *
	 */
	public byte getEvaluationTime()
	{
		return evaluationTime;
	}
		
	/**
	 * @deprecated
	 */
	public JRBox getBox()
	{
		return this;
	}

	/**
	 *
	 */
	public byte getHyperlinkType()
	{
		return hyperlinkType;
	}
		
	/**
	 *
	 */
	public byte getHyperlinkTarget()
	{
		return hyperlinkTarget;
	}
		
	/**
	 *
	 */
	public JRGroup getEvaluationGroup()
	{
		return evaluationGroup;
	}
		
	/**
	 *
	 */
	public JRExpression getExpression()
	{
		return expression;
	}

	/**
	 *
	 */
	public JRExpression getAnchorNameExpression()
	{
		return anchorNameExpression;
	}

	/**
	 *
	 */
	public JRExpression getHyperlinkReferenceExpression()
	{
		return hyperlinkReferenceExpression;
	}

	/**
	 *
	 */
	public JRExpression getHyperlinkAnchorExpression()
	{
		return hyperlinkAnchorExpression;
	}

	/**
	 *
	 */
	public JRExpression getHyperlinkPageExpression()
	{
		return hyperlinkPageExpression;
	}


	/**
	 *
	 */
	public void setUsingCache(boolean isUsingCache)
	{
		setUsingCache(isUsingCache ? Boolean.TRUE : Boolean.FALSE);
	}

	/**
	 *
	 */
	public void setUsingCache(Boolean isUsingCache)
	{
		this.isUsingCache = isUsingCache;
	}

	/**
	 *
	 */
	public boolean isLazy()
	{
		return isLazy;
	}

	/**
	 *
	 */
	public void setLazy(boolean isLazy)
	{
		this.isLazy = isLazy;
	}

	/**
	 *
	 */
	public byte getOnErrorType()
	{
		return onErrorType;
	}

	/**
	 *
	 */
	public void setOnErrorType(byte onErrorType)
	{
		this.onErrorType = onErrorType;
	}

	/**
	 * Sets the evaluation time for this image.
	 * 
	 */
	public void setEvaluationTime(byte evaluationTime)
	{
		this.evaluationTime = evaluationTime;
	}
		
	/**
	 * @deprecated
	 */
	public void setBox(JRBox box)
	{
		border = box.getOwnBorder();
		topBorder = box.getOwnTopBorder();
		leftBorder = box.getOwnLeftBorder();
		bottomBorder = box.getOwnBottomBorder();
		rightBorder = box.getOwnRightBorder();
		borderColor = box.getOwnBorderColor();
		topBorderColor = box.getOwnTopBorderColor();
		leftBorderColor = box.getOwnLeftBorderColor();
		bottomBorderColor = box.getOwnBottomBorderColor();
		rightBorderColor = box.getOwnRightBorderColor();
		padding = box.getOwnPadding();
		topPadding = box.getOwnTopPadding();
		leftPadding = box.getOwnLeftPadding();
		bottomPadding = box.getOwnBottomPadding();
		rightPadding = box.getOwnRightPadding();
	}
		
	/**
	 *
	 */
	public void setHyperlinkType(byte hyperlinkType)
	{
		this.hyperlinkType = hyperlinkType;
	}
		
	/**
	 *
	 */
	public void setHyperlinkTarget(byte hyperlinkTarget)
	{
		this.hyperlinkTarget = hyperlinkTarget;
	}
		
	/**
	 *
	 */
	public void setEvaluationGroup(JRGroup evaluationGroup)
	{
		this.evaluationGroup = evaluationGroup;
	}
		
	/**
	 *
	 */
	public void setExpression(JRExpression expression)
	{
		this.expression = expression;
	}

	/**
	 *
	 */
	public void setAnchorNameExpression(JRExpression anchorNameExpression)
	{
		this.anchorNameExpression = anchorNameExpression;
	}

	/**
	 *
	 */
	public void setHyperlinkReferenceExpression(JRExpression hyperlinkReferenceExpression)
	{
		this.hyperlinkReferenceExpression = hyperlinkReferenceExpression;
	}

	/**
	 *
	 */
	public void setHyperlinkAnchorExpression(JRExpression hyperlinkAnchorExpression)
	{
		this.hyperlinkAnchorExpression = hyperlinkAnchorExpression;
	}

	/**
	 *
	 */
	public void setHyperlinkPageExpression(JRExpression hyperlinkPageExpression)
	{
		this.hyperlinkPageExpression = hyperlinkPageExpression;
	}
	
	/**
	 *
	 */
	public JRChild getCopy(JRAbstractObjectFactory factory)
	{
		return factory.getImage(this);
	}

	/**
	 *
	 */
	public void collectExpressions(JRExpressionCollector collector)
	{
		collector.collect(this);
	}

	/**
	 *
	 */
	public void writeXml(JRXmlWriter xmlWriter) throws IOException
	{
		xmlWriter.writeImage(this);
	}


	public int getBookmarkLevel()
	{
		return bookmarkLevel;
	}


	/**
	 * Sets the boomark level for the anchor associated with this image.
	 * 
	 * @param bookmarkLevel the bookmark level (starting from 1)
	 * or {@link JRAnchor#NO_BOOKMARK NO_BOOKMARK} if no bookmark should be created 
	 */
	public void setBookmarkLevel(int bookmarkLevel)
	{
		this.bookmarkLevel = bookmarkLevel;
	}

	/**
	 *
	 */
	public byte getBorder()
	{
		return JRStyleResolver.getBorder(this);
	}

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
	public Color getBorderColor()
	{
		return JRStyleResolver.getBorderColor(this, getForecolor());
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
	public Color getTopBorderColor()
	{
		return JRStyleResolver.getTopBorderColor(this, getForecolor());
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
	public Color getLeftBorderColor()
	{
		return JRStyleResolver.getLeftBorderColor(this, getForecolor());
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
	public Color getBottomBorderColor()
	{
		return JRStyleResolver.getBottomBorderColor(this, getForecolor());
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
	public Color getRightBorderColor()
	{
		return JRStyleResolver.getRightBorderColor(this, getForecolor());
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
	public void setBorder(Byte border)
	{
		this.border = border;
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
	public void setTopBorder(Byte topBorder)
	{
		this.topBorder = topBorder;
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
	public void setLeftBorder(Byte leftBorder)
	{
		this.leftBorder = leftBorder;
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
	public void setBottomBorder(Byte bottomBorder)
	{
		this.bottomBorder = bottomBorder;
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
	public void setRightBorder(Byte rightBorder)
	{
		this.rightBorder = rightBorder;
	}

	/**
	 *
	 */
	public void setRightPadding(Integer rightPadding)
	{
		this.rightPadding = rightPadding;
	}

}
