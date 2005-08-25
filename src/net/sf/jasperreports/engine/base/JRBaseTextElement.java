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

import net.sf.jasperreports.engine.JRBox;
import net.sf.jasperreports.engine.JRFont;
import net.sf.jasperreports.engine.JRTextElement;


/**
 * This class provides functionality common to text elements. It provides implementation for the methods described
 * in <tt>JRTextElement</tt>.
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public abstract class JRBaseTextElement extends JRBaseElement implements JRTextElement
{


	/**
	 *
	 */
	private static final long serialVersionUID = 10001;

	/**
	 *
	 */
	protected byte horizontalAlignment = HORIZONTAL_ALIGN_LEFT;
	protected byte verticalAlignment = VERTICAL_ALIGN_TOP;
	protected byte rotation = ROTATION_NONE;
	protected byte lineSpacing = LINE_SPACING_SINGLE;
	protected boolean isStyledText = false;

	/**
	 *
	 */
	protected JRBox box = null;
	protected JRFont font = null;

	
	/**
	 * Constructs an empty text element. By default a text element is transparent.
	 */
	protected JRBaseTextElement()
	{
		super();
		
		mode = MODE_TRANSPARENT;
	}
		

	/**
	 * Initializes properties that are specific to text elements. Common properties are initialized by its
	 * parent constructor.
	 * @param textElement an element whose properties are copied to this element. Usually it is a
	 * {@link net.sf.jasperreports.engine.design.JRDesignTextElement} that must be transformed into an
	 * <tt>JRBaseTextElement</tt> at compile time.
	 * @param factory a factory used in the compile process
	 */
	protected JRBaseTextElement(JRTextElement textElement, JRBaseObjectFactory factory)
	{
		super(textElement, factory);
		
		box = textElement.getBox();
		horizontalAlignment = textElement.getHorizontalAlignment();
		verticalAlignment = textElement.getVerticalAlignment();
		rotation = textElement.getRotation();
		lineSpacing = textElement.getLineSpacing();
		isStyledText = textElement.isStyledText();

		font = factory.getFont(textElement.getFont());
	}
		

	/**
	 * @deprecated Replaced by {@link #getHorizontalAlignment()}.
	 */
	public byte getTextAlignment()
	{
		return horizontalAlignment;
	}
		
	/**
	 * @deprecated Replaced by {@link #setHorizontalAlignment(byte)}.
	 */
	public void setTextAlignment(byte horizontalAlignment)
	{
		this.horizontalAlignment = horizontalAlignment;
	}
		
	/**
	 *
	 */
	public byte getHorizontalAlignment()
	{
		return horizontalAlignment;
	}
		
	/**
	 *
	 */
	public void setHorizontalAlignment(byte horizontalAlignment)
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
	public JRFont getFont()
	{
		return font;
	}


}
