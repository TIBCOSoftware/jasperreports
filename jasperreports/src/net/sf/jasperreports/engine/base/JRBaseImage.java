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

import java.io.IOException;
import java.io.ObjectInputStream;

import net.sf.jasperreports.engine.JRAnchor;
import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRExpressionCollector;
import net.sf.jasperreports.engine.JRGroup;
import net.sf.jasperreports.engine.JRHyperlinkHelper;
import net.sf.jasperreports.engine.JRHyperlinkParameter;
import net.sf.jasperreports.engine.JRImage;
import net.sf.jasperreports.engine.JRLineBox;
import net.sf.jasperreports.engine.JRPen;
import net.sf.jasperreports.engine.JRVisitor;
import net.sf.jasperreports.engine.type.EvaluationTimeEnum;
import net.sf.jasperreports.engine.type.HorizontalImageAlignEnum;
import net.sf.jasperreports.engine.type.HyperlinkTargetEnum;
import net.sf.jasperreports.engine.type.HyperlinkTypeEnum;
import net.sf.jasperreports.engine.type.ModeEnum;
import net.sf.jasperreports.engine.type.OnErrorTypeEnum;
import net.sf.jasperreports.engine.type.ScaleImageEnum;
import net.sf.jasperreports.engine.type.VerticalImageAlignEnum;
import net.sf.jasperreports.engine.util.JRCloneUtils;


/**
 * The actual implementation of a graphic element representing an image.
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public class JRBaseImage extends JRBaseGraphicElement implements JRImage
{


	/**
	 *
	 */
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;
	
	/*
	 * Image properties
	 */

	public static final String PROPERTY_LAZY = "isLazy";
	
	public static final String PROPERTY_ON_ERROR_TYPE = "onErrorType";
	
	public static final String PROPERTY_USING_CACHE = "isUsingCache";
	
	
	/**
	 *
	 */
	protected ScaleImageEnum scaleImageValue;
	protected HorizontalImageAlignEnum horizontalImageAlign;
	protected VerticalImageAlignEnum verticalImageAlign;
	protected Boolean isUsingCache;
	protected boolean isLazy;
	protected OnErrorTypeEnum onErrorTypeValue = OnErrorTypeEnum.ERROR;
	protected EvaluationTimeEnum evaluationTimeValue = EvaluationTimeEnum.NOW;
	protected String linkType;
	protected String linkTarget;
	private JRHyperlinkParameter[] hyperlinkParameters;

	/**
	 *
	 */
	protected JRLineBox lineBox;

	/**
	 *
	 */
	protected JRGroup evaluationGroup;
	protected JRExpression expression;
	protected JRExpression anchorNameExpression;
	protected JRExpression hyperlinkReferenceExpression;
	protected JRExpression hyperlinkWhenExpression;
	protected JRExpression hyperlinkAnchorExpression;
	protected JRExpression hyperlinkPageExpression;
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
		
		scaleImageValue = image.getOwnScaleImageValue();
		horizontalImageAlign = image.getOwnHorizontalImageAlign();
		verticalImageAlign = image.getOwnVerticalImageAlign();
		isUsingCache = image.getUsingCache();
		isLazy = image.isLazy();
		onErrorTypeValue = image.getOnErrorTypeValue();
		evaluationTimeValue = image.getEvaluationTimeValue();
		linkType = image.getLinkType();
		linkTarget = image.getLinkTarget();
		hyperlinkParameters = JRBaseHyperlink.copyHyperlinkParameters(image, factory);

		lineBox = image.getLineBox().clone(this);

		evaluationGroup = factory.getGroup(image.getEvaluationGroup());
		expression = factory.getExpression(image.getExpression());
		anchorNameExpression = factory.getExpression(image.getAnchorNameExpression());
		hyperlinkReferenceExpression = factory.getExpression(image.getHyperlinkReferenceExpression());
		hyperlinkWhenExpression = factory.getExpression(image.getHyperlinkWhenExpression());
		hyperlinkAnchorExpression = factory.getExpression(image.getHyperlinkAnchorExpression());
		hyperlinkPageExpression = factory.getExpression(image.getHyperlinkPageExpression());
		hyperlinkTooltipExpression = factory.getExpression(image.getHyperlinkTooltipExpression());
		bookmarkLevel = image.getBookmarkLevel();
	}


	@Override
	public ModeEnum getModeValue()
	{
		return getStyleResolver().getMode(this, ModeEnum.TRANSPARENT);
	}

	@Override
	public ScaleImageEnum getScaleImageValue()
	{
		return getStyleResolver().getScaleImageValue(this);
	}

	@Override
	public ScaleImageEnum getOwnScaleImageValue()
	{
		return this.scaleImageValue;
	}

	@Override
	public void setScaleImage(ScaleImageEnum scaleImageValue)
	{
		Object old = this.scaleImageValue;
		this.scaleImageValue = scaleImageValue;
		getEventSupport().firePropertyChange(JRBaseStyle.PROPERTY_SCALE_IMAGE, old, this.scaleImageValue);
	}


	/**
	 * @deprecated Replaced by {@link #getHorizontalImageAlign()}.
	 */
	@Override
	public net.sf.jasperreports.engine.type.HorizontalAlignEnum getHorizontalAlignmentValue()
	{
		return net.sf.jasperreports.engine.type.HorizontalAlignEnum.getHorizontalAlignEnum(getHorizontalImageAlign());
	}
		
	/**
	 * @deprecated Replaced by {@link #getOwnHorizontalImageAlign()}.
	 */
	@Override
	public net.sf.jasperreports.engine.type.HorizontalAlignEnum getOwnHorizontalAlignmentValue()
	{
		return net.sf.jasperreports.engine.type.HorizontalAlignEnum.getHorizontalAlignEnum(getOwnHorizontalImageAlign());
	}
		
	/**
	 * @deprecated Replaced by {@link #setHorizontalImageAlign(HorizontalImageAlignEnum)}.
	 */
	@Override
	public void setHorizontalAlignment(net.sf.jasperreports.engine.type.HorizontalAlignEnum horizontalAlignmentValue)
	{
		setHorizontalImageAlign(net.sf.jasperreports.engine.type.HorizontalAlignEnum.getHorizontalImageAlignEnum(horizontalAlignmentValue));
	}

	/**
	 * @deprecated Replaced by {@link #getVerticalImageAlign()}.
	 */
	@Override
	public net.sf.jasperreports.engine.type.VerticalAlignEnum getVerticalAlignmentValue()
	{
		return net.sf.jasperreports.engine.type.VerticalAlignEnum.getVerticalAlignEnum(getVerticalImageAlign());
	}
		
	/**
	 * @deprecated Replaced by {@link #getOwnVerticalImageAlign()}.
	 */
	@Override
	public net.sf.jasperreports.engine.type.VerticalAlignEnum getOwnVerticalAlignmentValue()
	{
		return net.sf.jasperreports.engine.type.VerticalAlignEnum.getVerticalAlignEnum(getOwnVerticalImageAlign());
	}
		
	/**
	 * @deprecated Replaced by {@link #setVerticalImageAlign(VerticalImageAlignEnum)}.
	 */
	@Override
	public void setVerticalAlignment(net.sf.jasperreports.engine.type.VerticalAlignEnum verticalAlignmentValue)
	{
		setVerticalImageAlign(net.sf.jasperreports.engine.type.VerticalAlignEnum.getVerticalImageAlignEnum(verticalAlignmentValue));
	}
		
	@Override
	public HorizontalImageAlignEnum getHorizontalImageAlign()
	{
		return getStyleResolver().getHorizontalImageAlign(this);
	}
		
	@Override
	public HorizontalImageAlignEnum getOwnHorizontalImageAlign()
	{
		return horizontalImageAlign;
	}
		
	@Override
	public void setHorizontalImageAlign(HorizontalImageAlignEnum horizontalImageAlign)
	{
		Object old = this.horizontalImageAlign;
		this.horizontalImageAlign = horizontalImageAlign;
		getEventSupport().firePropertyChange(JRBaseStyle.PROPERTY_HORIZONTAL_IMAGE_ALIGNMENT, old, this.horizontalImageAlign);
	}

	@Override
	public VerticalImageAlignEnum getVerticalImageAlign()
	{
		return getStyleResolver().getVerticalImageAlign(this);
	}
		
	@Override
	public VerticalImageAlignEnum getOwnVerticalImageAlign()
	{
		return verticalImageAlign;
	}
		
	@Override
	public void setVerticalImageAlign(VerticalImageAlignEnum verticalImageAlign)
	{
		Object old = this.verticalImageAlign;
		this.verticalImageAlign = verticalImageAlign;
		getEventSupport().firePropertyChange(JRBaseStyle.PROPERTY_VERTICAL_IMAGE_ALIGNMENT, old, this.verticalImageAlign);
	}

	/**
	 * @deprecated Replaced by {@link #getUsingCache()}.
	 */
	@Override
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
	 * @deprecated Replaced by {@link #getUsingCache()}.
	 */
	@Override
	public Boolean isOwnUsingCache()
	{
		return isUsingCache;
	}

	@Override
	public Boolean getUsingCache()
	{
		return isUsingCache;
	}

	@Override
	public void setUsingCache(boolean isUsingCache)
	{
		setUsingCache(isUsingCache ? Boolean.TRUE : Boolean.FALSE);
	}

	@Override
	public void setUsingCache(Boolean isUsingCache)
	{
		Object old = this.isUsingCache;
		this.isUsingCache = isUsingCache;
		getEventSupport().firePropertyChange(PROPERTY_USING_CACHE, old, this.isUsingCache);
	}

	@Override
	public boolean isLazy()
	{
		return isLazy;
	}

	@Override
	public void setLazy(boolean isLazy)
	{
		boolean old = this.isLazy;
		this.isLazy = isLazy;
		getEventSupport().firePropertyChange(PROPERTY_LAZY, old, this.isLazy);
	}

	@Override
	public OnErrorTypeEnum getOnErrorTypeValue()
	{
		return this.onErrorTypeValue;
	}

	@Override
	public void setOnErrorType(OnErrorTypeEnum onErrorTypeValue)
	{
		OnErrorTypeEnum old = this.onErrorTypeValue;
		this.onErrorTypeValue = onErrorTypeValue;
		getEventSupport().firePropertyChange(PROPERTY_ON_ERROR_TYPE, old, this.onErrorTypeValue);
	}

	@Override
	public EvaluationTimeEnum getEvaluationTimeValue()
	{
		return evaluationTimeValue;
	}
		
	@Override
	public JRLineBox getLineBox()
	{
		return lineBox;
	}

	/**
	 * @deprecated Replaced by {@link #getHyperlinkTypeValue()}.
	 */
	public byte getHyperlinkType()
	{
		return getHyperlinkTypeValue().getValue();
	}
		
	@Override
	public HyperlinkTypeEnum getHyperlinkTypeValue()
	{
		return JRHyperlinkHelper.getHyperlinkTypeValue(this);
	}
		
	@Override
	public byte getHyperlinkTarget()
	{
		return JRHyperlinkHelper.getHyperlinkTarget(this);
	}
		
	@Override
	public JRGroup getEvaluationGroup()
	{
		return evaluationGroup;
	}
		
	@Override
	public JRExpression getExpression()
	{
		return expression;
	}

	@Override
	public JRExpression getAnchorNameExpression()
	{
		return anchorNameExpression;
	}

	@Override
	public JRExpression getHyperlinkReferenceExpression()
	{
		return hyperlinkReferenceExpression;
	}

	@Override
	public JRExpression getHyperlinkWhenExpression()
	{
		return hyperlinkWhenExpression;
	}

	@Override
	public JRExpression getHyperlinkAnchorExpression()
	{
		return hyperlinkAnchorExpression;
	}

	@Override
	public JRExpression getHyperlinkPageExpression()
	{
		return hyperlinkPageExpression;
	}
	
	@Override
	public void collectExpressions(JRExpressionCollector collector)
	{
		collector.collect(this);
	}

	@Override
	public void visit(JRVisitor visitor)
	{
		visitor.visitImage(this);
	}

	
	@Override
	public int getBookmarkLevel()
	{
		return bookmarkLevel;
	}

	
	@Override
	public Float getDefaultLineWidth() 
	{
		return JRPen.LINE_WIDTH_0;
	}

	
	@Override
	public String getLinkType()
	{
		return linkType;
	}

	@Override
	public String getLinkTarget()
	{
		return linkTarget;
	}


	@Override
	public JRHyperlinkParameter[] getHyperlinkParameters()
	{
		return hyperlinkParameters;
	}
	
	
	@Override
	public JRExpression getHyperlinkTooltipExpression()
	{
		return hyperlinkTooltipExpression;
	}

	
	@Override
	public Object clone() 
	{
		JRBaseImage clone = (JRBaseImage)super.clone();
		clone.lineBox = lineBox.clone(clone);
		clone.hyperlinkParameters = JRCloneUtils.cloneArray(hyperlinkParameters);
		clone.expression = JRCloneUtils.nullSafeClone(expression);
		clone.anchorNameExpression = JRCloneUtils.nullSafeClone(anchorNameExpression);
		clone.hyperlinkReferenceExpression = JRCloneUtils.nullSafeClone(hyperlinkReferenceExpression);
		clone.hyperlinkWhenExpression = JRCloneUtils.nullSafeClone(hyperlinkWhenExpression);
		clone.hyperlinkAnchorExpression = JRCloneUtils.nullSafeClone(hyperlinkAnchorExpression);
		clone.hyperlinkPageExpression = JRCloneUtils.nullSafeClone(hyperlinkPageExpression);
		clone.hyperlinkTooltipExpression = JRCloneUtils.nullSafeClone(hyperlinkTooltipExpression);
		return clone;
	}


	/*
	 * These fields are only for serialization backward compatibility.
	 */
	private int PSEUDO_SERIAL_VERSION_UID = JRConstants.PSEUDO_SERIAL_VERSION_UID; //NOPMD
	/**
	 * @deprecated
	 */
	private Byte horizontalAlignment;
	/**
	 * @deprecated
	 */
	private Byte verticalAlignment;
	/**
	 * @deprecated
	 */
	private net.sf.jasperreports.engine.type.HorizontalAlignEnum horizontalAlignmentValue;
	/**
	 * @deprecated
	 */
	private net.sf.jasperreports.engine.type.VerticalAlignEnum verticalAlignmentValue;
	/**
	 * @deprecated
	 */
	private byte hyperlinkType;
	/**
	 * @deprecated
	 */
	private byte hyperlinkTarget;
	/**
	 * @deprecated
	 */
	private Byte scaleImage;
	/**
	 * @deprecated
	 */
	private byte onErrorType;
	/**
	 * @deprecated
	 */
	private byte evaluationTime;
	
	@SuppressWarnings("deprecation")
	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException
	{
		in.defaultReadObject();

		if (PSEUDO_SERIAL_VERSION_UID < JRConstants.PSEUDO_SERIAL_VERSION_UID_3_7_2)
		{
			horizontalAlignmentValue = net.sf.jasperreports.engine.type.HorizontalAlignEnum.getByValue(horizontalAlignment);
			verticalAlignmentValue = net.sf.jasperreports.engine.type.VerticalAlignEnum.getByValue(verticalAlignment);
			scaleImageValue = ScaleImageEnum.getByValue(scaleImage);
			onErrorTypeValue = OnErrorTypeEnum.getByValue(onErrorType);
			evaluationTimeValue = EvaluationTimeEnum.getByValue(evaluationTime);

			horizontalAlignment = null;
			verticalAlignment = null;
			scaleImage = null;
		}

		if (linkType == null)
		{
			 linkType = JRHyperlinkHelper.getLinkType(HyperlinkTypeEnum.getByValue(hyperlinkType));
		}

		if (linkTarget == null)
		{
			 linkTarget = JRHyperlinkHelper.getLinkTarget(HyperlinkTargetEnum.getByValue(hyperlinkTarget));
		}

		if (PSEUDO_SERIAL_VERSION_UID < JRConstants.PSEUDO_SERIAL_VERSION_UID_6_0_2)
		{
			horizontalImageAlign = net.sf.jasperreports.engine.type.HorizontalAlignEnum.getHorizontalImageAlignEnum(horizontalAlignmentValue);
			verticalImageAlign = net.sf.jasperreports.engine.type.VerticalAlignEnum.getVerticalImageAlignEnum(verticalAlignmentValue);

			horizontalAlignmentValue = null;
			verticalAlignmentValue = null;
		}
	}
}
