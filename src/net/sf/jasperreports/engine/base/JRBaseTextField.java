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
package net.sf.jasperreports.engine.base;

import net.sf.jasperreports.engine.JRAbstractObjectFactory;
import net.sf.jasperreports.engine.JRAnchor;
import net.sf.jasperreports.engine.JRChild;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRExpressionCollector;
import net.sf.jasperreports.engine.JRGroup;
import net.sf.jasperreports.engine.JRHyperlink;
import net.sf.jasperreports.engine.JRTextField;
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
	private static final long serialVersionUID = 10003;

	/**
	 *
	 */
	protected boolean isStretchWithOverflow = false;
	protected byte evaluationTime = JRExpression.EVALUATION_TIME_NOW;
	protected String pattern = null;
	protected boolean isBlankWhenNull = false;
	protected byte hyperlinkType = JRHyperlink.HYPERLINK_TYPE_NONE;
	protected byte hyperlinkTarget = JRHyperlink.HYPERLINK_TARGET_SELF;

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
		pattern = textField.getPattern();
		isBlankWhenNull = textField.isBlankWhenNull();
		hyperlinkType = textField.getHyperlinkType();
		hyperlinkTarget = textField.getHyperlinkTarget();

		evaluationGroup = factory.getGroup(textField.getEvaluationGroup());
		expression = factory.getExpression(textField.getExpression());
		anchorNameExpression = factory.getExpression(textField.getAnchorNameExpression());
		hyperlinkReferenceExpression = factory.getExpression(textField.getHyperlinkReferenceExpression());
		hyperlinkAnchorExpression = factory.getExpression(textField.getHyperlinkAnchorExpression());
		hyperlinkPageExpression = factory.getExpression(textField.getHyperlinkPageExpression());
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
		return this.isBlankWhenNull;
	}

	/**
	 *
	 */
	public void setBlankWhenNull(boolean isBlank)
	{
		this.isBlankWhenNull = isBlank;
	}

	/**
	 *
	 */
	public byte getHyperlinkType()
	{
		return this.hyperlinkType;
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
	public void writeXml(JRXmlWriter xmlWriter)
	{
		xmlWriter.writeTextField(this);
	}


	public int getBookmarkLevel()
	{
		return bookmarkLevel;
	}
}
