/*
 * ============================================================================
 *                   GNU Lesser General Public License
 * ============================================================================
 *
 * JasperReports - Free Java report-generating library.
 * Copyright (C) 2001-2005 Teodor Danciu teodord@users.sourceforge.net
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
 * Teodor Danciu
 * 173, Calea Calarasilor, Bl. 42, Sc. 1, Ap. 18
 * Postal code 030615, Sector 3
 * Bucharest, ROMANIA
 * Email: teodord@users.sourceforge.net
 */
package net.sf.jasperreports.engine.design;

import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRGroup;
import net.sf.jasperreports.engine.JRHyperlink;
import net.sf.jasperreports.engine.JRTextField;

//import java.text.Format;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JRDesignTextField extends JRDesignTextElement implements JRTextField
{


	/**
	 *
	 */
	private static final long serialVersionUID = 605;

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
	 *
	 */
	public boolean isStretchWithOverflow()
	{
		return this.isStretchWithOverflow;
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
	public boolean isBlankWhenNull()
	{
		return this.isBlankWhenNull;
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
	public void setStretchWithOverflow(boolean isStretch)
	{
		this.isStretchWithOverflow = isStretch;
	}
		
	/**
	 *
	 */
	public void setEvaluationTime(byte evaluationTime)
	{
		this.evaluationTime = evaluationTime;
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
	public void setBlankWhenNull(boolean isBlank)
	{
		this.isBlankWhenNull = isBlank;
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


}
