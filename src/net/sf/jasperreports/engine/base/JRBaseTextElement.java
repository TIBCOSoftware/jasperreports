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
package net.sf.jasperreports.engine.base;

import net.sf.jasperreports.engine.JRBox;
import net.sf.jasperreports.engine.JRElement;
import net.sf.jasperreports.engine.JRFont;
import net.sf.jasperreports.engine.JRTextElement;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public abstract class JRBaseTextElement extends JRBaseElement implements JRTextElement
{


	/**
	 *
	 */
	private static final long serialVersionUID = 606;

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
	 *
	 */
	protected JRBaseTextElement()
	{
		super();
		
		mode = MODE_TRANSPARENT;
	}
		

	/**
	 *
	 */
	protected JRBaseTextElement(JRTextElement textElement, JRBaseObjectFactory factory)
	{
		super((JRElement)textElement, factory);
		
		box = textElement.getBox();
		horizontalAlignment = textElement.getTextAlignment();
		verticalAlignment = textElement.getVerticalAlignment();
		rotation = textElement.getRotation();
		lineSpacing = textElement.getLineSpacing();
		isStyledText = textElement.isStyledText();

		font = factory.getFont(textElement.getFont());
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
