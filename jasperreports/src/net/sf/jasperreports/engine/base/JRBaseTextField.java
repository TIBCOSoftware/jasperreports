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
import net.sf.jasperreports.engine.JRTextField;
import net.sf.jasperreports.engine.JRVisitor;
import net.sf.jasperreports.engine.type.EvaluationTimeEnum;
import net.sf.jasperreports.engine.type.HyperlinkTargetEnum;
import net.sf.jasperreports.engine.type.HyperlinkTypeEnum;
import net.sf.jasperreports.engine.util.JRCloneUtils;
import net.sf.jasperreports.engine.util.JRStyleResolver;


/**
 * This class is used for representing a text field.
 *
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public class JRBaseTextField extends JRBaseTextElement implements JRTextField
{


	/**
	 *
	 */
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;
	
	public static final String PROPERTY_STRETCH_WITH_OVERFLOW = "isStretchWithOverflow";

	/**
	 *
	 */
	protected boolean isStretchWithOverflow;
	protected EvaluationTimeEnum evaluationTimeValue = EvaluationTimeEnum.NOW;
	protected String pattern;
	protected Boolean isBlankWhenNull;
	protected String linkType;
	protected String linkTarget;
	private JRHyperlinkParameter[] hyperlinkParameters;

	/**
	 *
	 */
	protected JRGroup evaluationGroup;
	protected JRExpression expression;
	protected JRExpression patternExpression;
	protected JRExpression anchorNameExpression;
	protected JRExpression hyperlinkReferenceExpression;
	protected JRExpression hyperlinkWhenExpression;
	protected JRExpression hyperlinkAnchorExpression;
	protected JRExpression hyperlinkPageExpression;
	private JRExpression hyperlinkTooltipExpression;

	/**
	 * The bookmark level for the anchor associated with this field.
	 * @see JRAnchor#getBookmarkLevel()
	 */
	protected int bookmarkLevel = JRAnchor.NO_BOOKMARK;

	/**
	 * Initializes the text field properties.
	 */
	protected JRBaseTextField(JRTextField textField, JRBaseObjectFactory factory)
	{
		super(textField, factory);
		
		isStretchWithOverflow = textField.isStretchWithOverflow();
		evaluationTimeValue = textField.getEvaluationTimeValue();
		pattern = textField.getOwnPattern();
		isBlankWhenNull = textField.isOwnBlankWhenNull();
		linkType = textField.getLinkType();
		linkTarget = textField.getLinkTarget();
		hyperlinkParameters = JRBaseHyperlink.copyHyperlinkParameters(textField, factory);

		evaluationGroup = factory.getGroup(textField.getEvaluationGroup());
		expression = factory.getExpression(textField.getExpression());
		patternExpression = factory.getExpression(textField.getPatternExpression());
		anchorNameExpression = factory.getExpression(textField.getAnchorNameExpression());
		hyperlinkReferenceExpression = factory.getExpression(textField.getHyperlinkReferenceExpression());
		hyperlinkWhenExpression = factory.getExpression(textField.getHyperlinkWhenExpression());
		hyperlinkAnchorExpression = factory.getExpression(textField.getHyperlinkAnchorExpression());
		hyperlinkPageExpression = factory.getExpression(textField.getHyperlinkPageExpression());
		hyperlinkTooltipExpression = factory.getExpression(textField.getHyperlinkTooltipExpression());
		bookmarkLevel = textField.getBookmarkLevel();
	}
		

	/**
	 *
	 */
	public boolean isStretchWithOverflow()
	{
		return this.isStretchWithOverflow;
	}
		
	/**
	 *
	 */
	public void setStretchWithOverflow(boolean isStretchWithOverflow)
	{
		boolean old = this.isStretchWithOverflow;
		this.isStretchWithOverflow = isStretchWithOverflow;
		getEventSupport().firePropertyChange(PROPERTY_STRETCH_WITH_OVERFLOW, old, this.isStretchWithOverflow);
	}
		
	/**
	 *
	 */
	public EvaluationTimeEnum getEvaluationTimeValue()
	{
		return this.evaluationTimeValue;
	}
		
	/**
	 *
	 */
	public String getPattern()
	{
		return JRStyleResolver.getPattern(this);
	}
		
	public String getOwnPattern()
	{
		return this.pattern;
	}

	/**
	 *
	 */
	public void setPattern(String pattern)
	{
		Object old = this.pattern;
		this.pattern = pattern;
		getEventSupport().firePropertyChange(JRBaseStyle.PROPERTY_PATTERN, old, this.pattern);
	}
		
	/**
	 *
	 */
	public boolean isBlankWhenNull()
	{
		return JRStyleResolver.isBlankWhenNull(this);
	}

	/**
	 *
	 */
	public Boolean isOwnBlankWhenNull()
	{
		return isBlankWhenNull;
	}

	/**
	 *
	 */
	public void setBlankWhenNull(Boolean isBlank)
	{
		Object old = this.isBlankWhenNull;
		this.isBlankWhenNull = isBlank;
		getEventSupport().firePropertyChange(JRBaseStyle.PROPERTY_BLANK_WHEN_NULL, old, this.isBlankWhenNull);
	}

	/**
	 *
	 */
	public void setBlankWhenNull(boolean isBlank)
	{
		setBlankWhenNull(isBlank ? Boolean.TRUE : Boolean.FALSE);
	}

	/**
	 * @deprecated Replaced by {@link #getHyperlinkTypeValue()}.
	 */
	public byte getHyperlinkType()
	{
		return getHyperlinkTypeValue().getValue();
	}
		
	/**
	 *
	 */
	public HyperlinkTypeEnum getHyperlinkTypeValue()
	{
		return JRHyperlinkHelper.getHyperlinkTypeValue(this);
	}
		
	/**
	 *
	 */
	public byte getHyperlinkTarget()
	{
		return JRHyperlinkHelper.getHyperlinkTarget(this);
	}
		
	/**
	 *
	 */
	public JRGroup getEvaluationGroup()
	{
		return this.evaluationGroup;
	}
		
	/**
	 *
	 */
	public JRExpression getExpression()
	{
		return this.expression;
	}

	/**
	 *
	 */
	public JRExpression getPatternExpression()
	{
		return this.patternExpression;
	}
		
	/**
	 *
	 */
	public JRExpression getAnchorNameExpression()
	{
		return this.anchorNameExpression;
	}

	/**
	 *
	 */
	public JRExpression getHyperlinkReferenceExpression()
	{
		return this.hyperlinkReferenceExpression;
	}

	/**
	 *
	 */
	public JRExpression getHyperlinkWhenExpression()
	{
		return this.hyperlinkWhenExpression;
	}

	/**
	 *
	 */
	public JRExpression getHyperlinkAnchorExpression()
	{
		return this.hyperlinkAnchorExpression;
	}

	/**
	 *
	 */
	public JRExpression getHyperlinkPageExpression()
	{
		return this.hyperlinkPageExpression;
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
		visitor.visitTextField(this);
	}


	public int getBookmarkLevel()
	{
		return bookmarkLevel;
	}


	public String getLinkType()
	{
		return linkType;
	}

	public String getLinkTarget()
	{
		return linkTarget;
	}


	public JRHyperlinkParameter[] getHyperlinkParameters()
	{
		return hyperlinkParameters;
	}
	

	public JRExpression getHyperlinkTooltipExpression()
	{
		return hyperlinkTooltipExpression;
	}
	
	/**
	 * 
	 */
	public Object clone() 
	{
		JRBaseTextField clone = (JRBaseTextField)super.clone();
		clone.hyperlinkParameters = JRCloneUtils.cloneArray(hyperlinkParameters);
		clone.expression = JRCloneUtils.nullSafeClone(expression);
		clone.patternExpression = JRCloneUtils.nullSafeClone(patternExpression);
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
	private byte hyperlinkType;
	/**
	 * @deprecated
	 */
	private byte hyperlinkTarget;
	/**
	 * @deprecated
	 */
	private byte evaluationTime;
	
	@SuppressWarnings("deprecation")
	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException
	{
		in.defaultReadObject();

		if (linkType == null)
		{
			 linkType = JRHyperlinkHelper.getLinkType(HyperlinkTypeEnum.getByValue(hyperlinkType));
		}

		if (linkTarget == null)
		{
			 linkTarget = JRHyperlinkHelper.getLinkTarget(HyperlinkTargetEnum.getByValue(hyperlinkTarget));
		}

		if (PSEUDO_SERIAL_VERSION_UID < JRConstants.PSEUDO_SERIAL_VERSION_UID_3_7_2)
		{
			evaluationTimeValue = EvaluationTimeEnum.getByValue(evaluationTime);
		}
	}
}
