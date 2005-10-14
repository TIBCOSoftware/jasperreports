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
package net.sf.jasperreports.engine;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public interface JRPrintText extends JRPrintElement, JRAlignment, JRPrintAnchor, JRPrintHyperlink, JRBox, JRFont
{


	/**
	 *
	 */
	public static final byte RUN_DIRECTION_LTR = 0;
	public static final byte RUN_DIRECTION_RTL = 1;

	
	/**
	 *
	 */
	public String getText();
		
	/**
	 *
	 */
	public void setText(String text);

	/**
	 *
	 */
	public float getLineSpacingFactor();
		
	/**
	 *
	 */
	public void setLineSpacingFactor(float lineSpacingFactor);

	/**
	 *
	 */
	public float getLeadingOffset();
		
	/**
	 *
	 */
	public void setLeadingOffset(float leadingOffset);

	/**
	 * @deprecated Replaced by {@link #getHorizontalAlignment()}.
	 */
	public byte getTextAlignment();
		
	/**
	 * @deprecated Replaced by {@link #setHorizontalAlignment(byte)}.
	 */
	public void setTextAlignment(byte horizontalAlignment);
		
	/**
	 *
	 */
	public byte getRotation();
		
	/**
	 *
	 */
	public Byte getOwnRotation();
		
	/**
	 *
	 */
	public void setRotation(byte rotation);
		
	/**
	 *
	 */
	public void setRotation(Byte rotation);
		
	/**
	 *
	 */
	public byte getRunDirection();
		
	/**
	 *
	 */
	public void setRunDirection(byte rotation);
		
	/**
	 *
	 */
	public float getTextHeight();
		
	/**
	 *
	 */
	public void setTextHeight(float textHeight);
		
	/**
	 *
	 */
	public byte getLineSpacing();
		
	/**
	 *
	 */
	public Byte getOwnLineSpacing();
		
	/**
	 *
	 */
	public void setLineSpacing(byte lineSpacing);
		
	/**
	 *
	 */
	public void setLineSpacing(Byte lineSpacing);
		
	/**
	 *
	 */
	public boolean isStyledText();
		
	/**
	 *
	 */
	public Boolean isOwnStyledText();
		
	/**
	 *
	 */
	public void setStyledText(boolean isStyledText);
		
	/**
	 *
	 */
	public void setStyledText(Boolean isStyledText);
		
	/**
	 * @deprecated
	 */
	public JRBox getBox();

	/**
	 * @deprecated
	 */
	public void setBox(JRBox box);

	/**
	 * @deprecated
	 */
	public JRFont getFont();

	/**
	 * @deprecated
	 */
	public void setFont(JRFont font);
		
	
}
