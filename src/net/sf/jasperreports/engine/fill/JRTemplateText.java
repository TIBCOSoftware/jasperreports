/*
 * ============================================================================
 *                   GNU Lesser General Public License
 * ============================================================================
 *
 * JasperReports - Free Java report-generating library.
 * Copyright (C) 2001-2004 Teodor Danciu teodord@users.sourceforge.net
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
package net.sf.jasperreports.engine.fill;

import net.sf.jasperreports.engine.JRAlignment;
import net.sf.jasperreports.engine.JRBox;
import net.sf.jasperreports.engine.JRFont;
import net.sf.jasperreports.engine.JRHyperlink;
import net.sf.jasperreports.engine.JRStaticText;
import net.sf.jasperreports.engine.JRTextElement;
import net.sf.jasperreports.engine.JRTextField;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JRTemplateText extends JRTemplateElement
{


	/**
	 *
	 */
	private static final long serialVersionUID = 604;

	/**
	 *
	 */
	private byte horizontalAlignment = JRAlignment.HORIZONTAL_ALIGN_LEFT;
	private byte verticalAlignment = JRAlignment.VERTICAL_ALIGN_TOP;
	private byte rotation = JRTextElement.ROTATION_NONE;
	private byte lineSpacing = JRTextElement.LINE_SPACING_SINGLE;
	private boolean isStyledText = false;
	private byte hyperlinkType = JRHyperlink.HYPERLINK_TYPE_NONE;
	private byte hyperlinkTarget = JRHyperlink.HYPERLINK_TARGET_SELF;
	private JRBox box = null;
	private JRFont font = null;


	/**
	 *
	 */
	protected JRTemplateText(JRStaticText staticText, JRFont font)
	{
		setStaticText(staticText);
		
		this.font = font;
	}

	/**
	 *
	 */
	protected JRTemplateText(JRTextField textField, JRFont font)
	{
		setTextField(textField);
		
		this.font = font;
	}


	/**
	 *
	 */
	protected void setStaticText(JRStaticText staticText)
	{
		setTextElement(staticText);
	}

	/**
	 *
	 */
	protected void setTextField(JRTextField textField)
	{
		setTextElement(textField);

		hyperlinkType = textField.getHyperlinkType();
		hyperlinkTarget = textField.getHyperlinkTarget();
	}

	/**
	 *
	 */
	protected void setTextElement(JRTextElement textElement)
	{
		super.setElement(textElement);

		box = textElement.getBox();
		horizontalAlignment = textElement.getTextAlignment();
		verticalAlignment = textElement.getVerticalAlignment();
		rotation = textElement.getRotation();
		lineSpacing = textElement.getLineSpacing();
		isStyledText = textElement.isStyledText();
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
	public byte getVerticalAlignment()
	{
		return verticalAlignment;
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
	public byte getLineSpacing()
	{
		return lineSpacing;
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

	
}
