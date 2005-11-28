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
 * 303 Second Street, Suite 450 North
 * San Francisco, CA 94107
 * http://www.jaspersoft.com
 */
package net.sf.jasperreports.engine;


/**
 * An abstract representation of a report text element. It provides basic functionality for static texts and text fields.
 *
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public interface JRTextElement extends JRElement, JRAlignment, JRBox, JRFont
{


	/**
	 * @deprecated Replaced by {@link JRAlignment#HORIZONTAL_ALIGN_LEFT}.
	 */
	public static final byte TEXT_ALIGN_LEFT = HORIZONTAL_ALIGN_LEFT;
	/**
	 * @deprecated Replaced by {@link JRAlignment#HORIZONTAL_ALIGN_CENTER}.
	 */
	public static final byte TEXT_ALIGN_CENTER = HORIZONTAL_ALIGN_CENTER;
	/**
	 * @deprecated Replaced by {@link JRAlignment#HORIZONTAL_ALIGN_RIGHT}.
	 */
	public static final byte TEXT_ALIGN_RIGHT = HORIZONTAL_ALIGN_RIGHT;
	/**
	 * @deprecated Replaced by {@link JRAlignment#HORIZONTAL_ALIGN_JUSTIFIED}.
	 */
	public static final byte TEXT_ALIGN_JUSTIFIED = HORIZONTAL_ALIGN_JUSTIFIED;

	/**
	 * Constant useful for displaying the text without rotating it
	 */
	public static final byte ROTATION_NONE = 0;

	/**
	 * Constant useful for rotating the text 90 degrees counter clockwise.
	 */
	public static final byte ROTATION_LEFT = 1;

	/**
	 * Constant useful for rotating the text 90 degrees clockwise.
	 */
	public static final byte ROTATION_RIGHT = 2;


	/**
	 * Constant for setting normal spacing between lines.
	 */
	public static final byte LINE_SPACING_SINGLE = 0;

	/**
	 * Constant for setting spacing between lines to 50% more than normal.
	 */
	public static final byte LINE_SPACING_1_1_2 = 1;

	/**
	 * Constant for setting spacing between lines to double size.
	 */
	public static final byte LINE_SPACING_DOUBLE = 2;


	/**
	 * Gets the text horizontal alignment.
	 * @return a value representing one of the horizontal alignment constants in {@link JRAlignment}
	 * @deprecated Replaced by {@link #getHorizontalAlignment()}.
	 */
	public byte getTextAlignment();

	/**
	 * Sets the text horizontal alignment.
	 * @param horizontalAlignment a value representing one of the horizontal alignment constants in {@link JRAlignment}
	 * @deprecated Replaced by {@link #setHorizontalAlignment(byte)}.
	 */
	public void setTextAlignment(byte horizontalAlignment);

	/**
	 * Gets the text rotation.
	 * @return a value representing one of the rotation constants in this class
	 */
	public byte getRotation();
		
	public Byte getOwnRotation();

	/**
	 * Sets the text rotation.
	 * @param rotation a value representing one of the rotation constants in this class
	 */
	public void setRotation(byte rotation);
		
	public void setRotation(Byte rotation);
	
	/**
	 * Gets the line spacing.
	 * @return a value representing one of the line spacing constants in this class
	 */
	public byte getLineSpacing();

	public Byte getOwnLineSpacing();
	/**
	 * Sets the line spacing.
	 * @param lineSpacing a value representing one of the line spacing constants in this class
	 */
	public void setLineSpacing(byte lineSpacing);
		
	public void setLineSpacing(Byte lineSpacing);
	
	/**
	 * Returns true if the text can contain style tags.
	 */
	public boolean isStyledText();

	public Boolean isOwnStyledText();
	/**
	 * Specifies whether the text can contain style tags.
	 */
	public void setStyledText(boolean isStyledText);
		
	public void setStyledText(Boolean isStyledText);
	
	/**
	 * Returns an object containing all border and padding properties for this text element
	 * @deprecated
	 */
	public JRBox getBox();

	/**
	 * Returns an object containing all font properties for this text element
	 * @deprecated
	 */
	public JRFont getFont();

	
}
