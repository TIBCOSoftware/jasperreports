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

import net.sf.jasperreports.engine.JRAlignment;
import net.sf.jasperreports.engine.JRBox;
import net.sf.jasperreports.engine.JRElement;
import net.sf.jasperreports.engine.JRFont;
import net.sf.jasperreports.engine.JRHyperlink;
import net.sf.jasperreports.engine.JRPrintText;
import net.sf.jasperreports.engine.JRTextElement;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JRBasePrintText extends JRBasePrintElement implements JRPrintText
{


	/**
	 *
	 */
	private static final long serialVersionUID = 608;

	/**
	 *
	 */
	protected String text = "";
	protected float lineSpacingFactor = 0;
	protected float leadingOffset = 0;
	protected byte horizontalAlignment = JRAlignment.HORIZONTAL_ALIGN_LEFT;
	protected byte verticalAlignment = JRAlignment.VERTICAL_ALIGN_TOP;
	protected byte rotation = JRTextElement.ROTATION_NONE;
	protected byte runDirection = RUN_DIRECTION_LTR;
	protected float textHeight = 0;
	protected byte lineSpacing = JRTextElement.LINE_SPACING_SINGLE;
	protected boolean isStyledText = false;
	protected JRBox box = null;
	protected JRFont font = null;
	protected String anchorName = null;
	protected byte hyperlinkType = JRHyperlink.HYPERLINK_TYPE_NONE;
	protected byte hyperlinkTarget = JRHyperlink.HYPERLINK_TARGET_SELF;
	protected String hyperlinkReference = null;
	protected String hyperlinkAnchor = null;
	protected Integer hyperlinkPage = null;

	
	/**
	 *
	 */
	public JRBasePrintText()
	{
		super();
		
		mode = JRElement.MODE_TRANSPARENT;
	}


	/**
	 *
	 */
	public String getText()
	{
		return text;
	}
		
	/**
	 *
	 */
	public void setText(String text)
	{
		this.text = text;
	}

	/**
	 *
	 */
	public float getLineSpacingFactor()
	{
		return lineSpacingFactor;
	}
		
	/**
	 *
	 */
	public void setLineSpacingFactor(float lineSpacingFactor)
	{
		this.lineSpacingFactor = lineSpacingFactor;
	}

	/**
	 *
	 */
	public float getLeadingOffset()
	{
		return leadingOffset;
	}
		
	/**
	 *
	 */
	public void setLeadingOffset(float leadingOffset)
	{
		this.leadingOffset = leadingOffset;
	}

	/**
	 *
	 */
	public byte getTextAlignment()
	{
		return horizontalAlignment;
	}
		
	/**
	 *
	 */
	public void setTextAlignment(byte horizontalAlignment)
	{
		this.horizontalAlignment = horizontalAlignment;
	}

	/**
	 *
	 */
	public byte getVerticalAlignment()
	{
		return verticalAlignment;
	}
		
	/**
	 *
	 */
	public void setVerticalAlignment(byte verticalAlignment)
	{
		this.verticalAlignment = verticalAlignment;
	}

	/**
	 *
	 */
	public byte getRotation()
	{
		return rotation;
	}
		
	/**
	 *
	 */
	public void setRotation(byte rotation)
	{
		this.rotation = rotation;
	}

	/**
	 *
	 */
	public byte getRunDirection()
	{
		return runDirection;
	}
		
	/**
	 *
	 */
	public void setRunDirection(byte runDirection)
	{
		this.runDirection = runDirection;
	}

	/**
	 *
	 */
	public float getTextHeight()
	{
		return textHeight;
	}
		
	/**
	 *
	 */
	public void setTextHeight(float textHeight)
	{
		this.textHeight = textHeight;
	}

	/**
	 *
	 */
	public byte getLineSpacing()
	{
		return lineSpacing;
	}
		
	/**
	 *
	 */
	public void setLineSpacing(byte lineSpacing)
	{
		this.lineSpacing = lineSpacing;
	}

	/**
	 *
	 */
	public boolean isStyledText()
	{
		return isStyledText;
	}
		
	/**
	 *
	 */
	public void setStyledText(boolean isStyledText)
	{
		this.isStyledText = isStyledText;
	}

	/**
	 *
	 */
	public JRBox getBox()
	{
		return box;
	}

	/**
	 *
	 */
	public void setBox(JRBox box)
	{
		this.box = box;
	}

	/**
	 *
	 */
	public JRFont getFont()
	{
		return font;
	}

	/**
	 *
	 */
	public void setFont(JRFont font)
	{
		this.font = font;
	}

	/**
	 *
	 */
	public String getAnchorName()
	{
		return anchorName;
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
		return hyperlinkType;
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
	public byte getHyperlinkTarget()
	{
		return hyperlinkTarget;
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
	public String getHyperlinkReference()
	{
		return hyperlinkReference;
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
		return hyperlinkAnchor;
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
		return hyperlinkPage;
	}
		
	/**
	 *
	 */
	public void setHyperlinkPage(Integer hyperlinkPage)
	{
		this.hyperlinkPage = hyperlinkPage;
	}
		
	/**
	 *
	 */
	public void setHyperlinkPage(String hyperlinkPage)
	{
		this.hyperlinkPage = new Integer(hyperlinkPage);
	}

	
}
