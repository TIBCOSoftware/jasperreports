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
import java.io.IOException;
import java.io.ObjectInputStream;

import net.sf.jasperreports.engine.JRAnchor;
import net.sf.jasperreports.engine.JRBox;
import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRExpressionCollector;
import net.sf.jasperreports.engine.JRGroup;
import net.sf.jasperreports.engine.JRHyperlink;
import net.sf.jasperreports.engine.JRHyperlinkHelper;
import net.sf.jasperreports.engine.JRHyperlinkParameter;
import net.sf.jasperreports.engine.JRImage;
import net.sf.jasperreports.engine.JRVisitor;
import net.sf.jasperreports.engine.util.JRStyleResolver;


/**
 * The actual implementation of a graphic element representing an image.
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JRBaseImage extends JRBaseGraphicElement implements JRImage
{


	/**
	 *
	 */
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;
	
	/*
	 * Box properties
	 */
	
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

	/*
	 * Image properties
	 */
	
	public static final String PROPERTY_HORIZONTAL_ALIGNMENT = JRBaseStyle.PROPERTY_HORIZONTAL_ALIGNMENT;
	
	public static final String PROPERTY_LAZY = "lazy";
	
	public static final String PROPERTY_ON_ERROR_TYPE = "onErrorType";
	
	public static final String PROPERTY_SCALE_IMAGE = JRBaseStyle.PROPERTY_SCALE_IMAGE;
	
	public static final String PROPERTY_USING_CACHE = "usingCache";
	
	public static final String PROPERTY_VERTICAL_ALIGNMENT = JRBaseStyle.PROPERTY_VERTICAL_ALIGNMENT;
	
	
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
	protected byte hyperlinkType = JRHyperlink.HYPERLINK_TYPE_NULL;
	protected String linkType;
	protected byte hyperlinkTarget = JRHyperlink.HYPERLINK_TARGET_SELF;
	private JRHyperlinkParameter[] hyperlinkParameters;

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
	private JRExpression hyperlinkTooltipExpression;

	/**
	 * The bookmark level for the anchor associated with this image.
	 * @see JRAnchor#getBookmarkLevel()
	 */
	protected int bookmarkLevel = JRAnchor.NO_BOOKMARK;

	/**
	 *
	 *
	protected JRBaseImage()
	{
		super();
	}
		

	/**
	 * Initializes properties that are specific to images. Common properties are initialized by its
	 * parent constructors.
	 * @param image an element whose properties are copied to this element. Usually it is a
	 * {@link net.sf.jasperreports.engine.design.JRDesignImage} that must be transformed into an
	 * <tt>JRBaseImage</tt> at compile time.
	 * @param factory a factory used in the compile process
	 */
	protected JRBaseImage(JRImage image, JRBaseObjectFactory factory)
	{
		super(image, factory);
		
		scaleImage = image.getOwnScaleImage();
		horizontalAlignment = image.getOwnHorizontalAlignment();
		verticalAlignment = image.getOwnVerticalAlignment();
		isUsingCache = image.isOwnUsingCache();
		isLazy = image.isLazy();
		onErrorType = image.getOnErrorType();
		evaluationTime = image.getEvaluationTime();
		linkType = image.getLinkType();
		hyperlinkTarget = image.getHyperlinkTarget();
		hyperlinkParameters = JRBaseHyperlink.copyHyperlinkParameters(image, factory);

//		box = image.getBox();

		border = image.getOwnBorder();
		topBorder = image.getOwnTopBorder();
		leftBorder = image.getOwnLeftBorder();
		bottomBorder = image.getOwnBottomBorder();
		rightBorder = image.getOwnRightBorder();
		borderColor = image.getOwnBorderColor();
		topBorderColor = image.getOwnTopBorderColor();
		leftBorderColor = image.getOwnLeftBorderColor();
		bottomBorderColor = image.getOwnBottomBorderColor();
		rightBorderColor = image.getOwnRightBorderColor();
		padding = image.getOwnPadding();
		topPadding = image.getOwnTopPadding();
		leftPadding = image.getOwnLeftPadding();
		bottomPadding = image.getOwnBottomPadding();
		rightPadding = image.getOwnRightPadding();

		evaluationGroup = factory.getGroup(image.getEvaluationGroup());
		expression = factory.getExpression(image.getExpression());
		anchorNameExpression = factory.getExpression(image.getAnchorNameExpression());
		hyperlinkReferenceExpression = factory.getExpression(image.getHyperlinkReferenceExpression());
		hyperlinkAnchorExpression = factory.getExpression(image.getHyperlinkAnchorExpression());
		hyperlinkPageExpression = factory.getExpression(image.getHyperlinkPageExpression());
		hyperlinkTooltipExpression = factory.getExpression(image.getHyperlinkTooltipExpression());
		bookmarkLevel = image.getBookmarkLevel();
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
	public byte getPen()
	{
		return JRStyleResolver.getPen(this, PEN_NONE);
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
		setScaleImage(new Byte(scaleImage));
	}

	/**
	 *
	 */
	public void setScaleImage(Byte scaleImage)
	{
		Object old = this.scaleImage;
		this.scaleImage = scaleImage;
		getEventSupport().firePropertyChange(PROPERTY_SCALE_IMAGE, old, this.scaleImage);
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
		setHorizontalAlignment(new Byte(horizontalAlignment));
	}
		
	/**
	 *
	 */
	public void setHorizontalAlignment(Byte horizontalAlignment)
	{
		Object old = this.horizontalAlignment;
		this.horizontalAlignment = horizontalAlignment;
		getEventSupport().firePropertyChange(PROPERTY_HORIZONTAL_ALIGNMENT, old, this.horizontalAlignment);
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
		setVerticalAlignment(new Byte(verticalAlignment));
	}
		
	/**
	 *
	 */
	public void setVerticalAlignment(Byte verticalAlignment)
	{
		Object old = this.verticalAlignment;
		this.verticalAlignment = verticalAlignment;
		getEventSupport().firePropertyChange(PROPERTY_VERTICAL_ALIGNMENT, old, this.verticalAlignment);
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
	public void setUsingCache(boolean isUsingCache)
	{
		setUsingCache(isUsingCache ? Boolean.TRUE : Boolean.FALSE);
	}

	/**
	 *
	 */
	public void setUsingCache(Boolean isUsingCache)
	{
		Object old = this.isUsingCache;
		this.isUsingCache = isUsingCache;
		getEventSupport().firePropertyChange(PROPERTY_USING_CACHE, old, this.isUsingCache);
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
		boolean old = this.isLazy;
		this.isLazy = isLazy;
		getEventSupport().firePropertyChange(PROPERTY_LAZY, old, this.isLazy);
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
		byte old = this.onErrorType;
		this.onErrorType = onErrorType;
		getEventSupport().firePropertyChange(PROPERTY_ON_ERROR_TYPE, old, this.onErrorType);
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
		return JRHyperlinkHelper.getHyperlinkType(this);
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
	public void collectExpressions(JRExpressionCollector collector)
	{
		collector.collect(this);
	}

	/**
	 *
	 */
	public void visit(JRVisitor visitor)
	{
		visitor.visitImage(this);
	}

	
	public int getBookmarkLevel()
	{
		return bookmarkLevel;
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
		setBorder(new Byte(border));
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
		Object old = this.borderColor;
		this.borderColor = borderColor;
		getEventSupport().firePropertyChange(PROPERTY_BORDER_COLOR, old, this.borderColor);
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
		setPadding(new Integer(padding));
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
		setTopBorder(new Byte(topBorder));
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
		Object old = this.topBorderColor;
		this.topBorderColor = topBorderColor;
		getEventSupport().firePropertyChange(PROPERTY_TOP_BORDER_COLOR, old, this.topBorderColor);
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
		setTopPadding(new Integer(topPadding));
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
		setLeftBorder(new Byte(leftBorder));
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
		Object old = this.leftBorderColor;
		this.leftBorderColor = leftBorderColor;
		getEventSupport().firePropertyChange(PROPERTY_LEFT_BORDER_COLOR, old, this.leftBorderColor);
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
		setLeftPadding(new Integer(leftPadding));
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
		setBottomBorder(new Byte(bottomBorder));
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
		Object old = this.bottomBorderColor;
		this.bottomBorderColor = bottomBorderColor;
		getEventSupport().firePropertyChange(PROPERTY_BOTTOM_BORDER_COLOR, old, this.bottomBorderColor);
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
		setBottomPadding(new Integer(bottomPadding));
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
		setRightBorder(new Byte(rightBorder));
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
		Object old = this.rightBorderColor;
		this.rightBorderColor = rightBorderColor;
		getEventSupport().firePropertyChange(PROPERTY_RIGHT_BORDER_COLOR, old, this.rightBorderColor);
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
		setRightPadding(new Integer(rightPadding));
	}


	/**
	 *
	 */
	public void setBorder(Byte border)
	{
		Object old = this.border;
		this.border = border;
		getEventSupport().firePropertyChange(PROPERTY_BORDER, old, this.border);
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
	public void setTopBorder(Byte topBorder)
	{
		Object old = this.topBorder;
		this.topBorder = topBorder;
		getEventSupport().firePropertyChange(PROPERTY_TOP_BORDER, old, this.topBorder);
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
	public void setLeftBorder(Byte leftBorder)
	{
		Object old = this.leftBorder;
		this.leftBorder = leftBorder;
		getEventSupport().firePropertyChange(PROPERTY_LEFT_BORDER, old, this.leftBorder);
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
	public void setBottomBorder(Byte bottomBorder)
	{
		Object old = this.bottomBorder;
		this.bottomBorder = bottomBorder;
		getEventSupport().firePropertyChange(PROPERTY_BOTTOM_BORDER, old, this.bottomBorder);
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
	public void setRightBorder(Byte rightBorder)
	{
		Object old = this.rightBorder;
		this.rightBorder = rightBorder;
		getEventSupport().firePropertyChange(PROPERTY_RIGHT_BORDER, old, this.rightBorder);
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


	public String getLinkType()
	{
		return linkType;
	}


	public JRHyperlinkParameter[] getHyperlinkParameters()
	{
		return hyperlinkParameters;
	}
	
	
	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException
	{
		in.defaultReadObject();
		normalizeLinkType();
	}


	protected void normalizeLinkType()
	{
		if (linkType == null)
		{
			 linkType = JRHyperlinkHelper.getLinkType(hyperlinkType);
		}
		hyperlinkType = JRHyperlink.HYPERLINK_TYPE_NULL;
	}

	
	public JRExpression getHyperlinkTooltipExpression()
	{
		return hyperlinkTooltipExpression;
	}

	
	/**
	 * 
	 */
	public Object clone() throws CloneNotSupportedException 
	{
		JRBaseImage clone = (JRBaseImage)super.clone();
		
		if (hyperlinkParameters != null)
		{
			clone.hyperlinkParameters = new JRHyperlinkParameter[hyperlinkParameters.length];
			for(int i = 0; i < hyperlinkParameters.length; i++)
			{
				clone.hyperlinkParameters[i] = (JRHyperlinkParameter)hyperlinkParameters[i].clone();
			}
		}

		if (expression != null)
		{
			clone.expression = (JRExpression)expression.clone();
		}
		if (anchorNameExpression != null)
		{
			clone.anchorNameExpression = (JRExpression)anchorNameExpression.clone();
		}
		if (hyperlinkReferenceExpression != null)
		{
			clone.hyperlinkReferenceExpression = (JRExpression)hyperlinkReferenceExpression.clone();
		}
		if (hyperlinkAnchorExpression != null)
		{
			clone.hyperlinkAnchorExpression = (JRExpression)hyperlinkAnchorExpression.clone();
		}
		if (hyperlinkPageExpression != null)
		{
			clone.hyperlinkPageExpression = (JRExpression)hyperlinkPageExpression.clone();
		}
		if (hyperlinkTooltipExpression != null)
		{
			clone.hyperlinkTooltipExpression = (JRExpression)hyperlinkTooltipExpression.clone();
		}

		return clone;
	}


}
