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

import java.io.IOException;
import java.io.ObjectInputStream;

import net.sf.jasperreports.engine.JRAbstractObjectFactory;
import net.sf.jasperreports.engine.JRAnchor;
import net.sf.jasperreports.engine.JRChild;
import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRExpressionCollector;
import net.sf.jasperreports.engine.JRGroup;
import net.sf.jasperreports.engine.JRHyperlink;
import net.sf.jasperreports.engine.JRHyperlinkHelper;
import net.sf.jasperreports.engine.JRHyperlinkParameter;
import net.sf.jasperreports.engine.JRTextField;
import net.sf.jasperreports.engine.util.JRStyleResolver;
import net.sf.jasperreports.engine.xml.JRXmlWriter;


/**
 * This class is used for representing a text field.
 *
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JRBaseTextField extends JRBaseTextElement implements JRTextField
{


	/**
	 *
	 */
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

	/**
	 *
	 */
	protected boolean isStretchWithOverflow = false;
	protected byte evaluationTime = JRExpression.EVALUATION_TIME_NOW;
	protected String pattern;
	protected Boolean isBlankWhenNull = null;
	protected byte hyperlinkType = JRHyperlink.HYPERLINK_TYPE_NULL;
	protected String linkType;
	protected byte hyperlinkTarget = JRHyperlink.HYPERLINK_TARGET_SELF;
	private JRHyperlinkParameter[] hyperlinkParameters;

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
		evaluationTime = textField.getEvaluationTime();
		pattern = textField.getOwnPattern();
		isBlankWhenNull = textField.isOwnBlankWhenNull();
		linkType = textField.getLinkType();
		hyperlinkTarget = textField.getHyperlinkTarget();
		hyperlinkParameters = JRBaseHyperlink.copyHyperlinkParameters(textField, factory);

		evaluationGroup = factory.getGroup(textField.getEvaluationGroup());
		expression = factory.getExpression(textField.getExpression());
		anchorNameExpression = factory.getExpression(textField.getAnchorNameExpression());
		hyperlinkReferenceExpression = factory.getExpression(textField.getHyperlinkReferenceExpression());
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
		this.isStretchWithOverflow = isStretchWithOverflow;
	}
		
	/**
	 *
	 */
	public byte getEvaluationTime()
	{
		return this.evaluationTime;
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
		this.pattern = pattern;
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
		this.isBlankWhenNull = isBlank;
	}

	/**
	 *
	 */
	public void setBlankWhenNull(boolean isBlank)
	{
		this.isBlankWhenNull = isBlank ? Boolean.TRUE : Boolean.FALSE;
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
		return this.hyperlinkTarget;
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
	public JRChild getCopy(JRAbstractObjectFactory factory)
	{
		return factory.getTextField(this);
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
		xmlWriter.writeTextField(this);
	}


	public int getBookmarkLevel()
	{
		return bookmarkLevel;
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
	
}
