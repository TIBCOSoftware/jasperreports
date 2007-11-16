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
package net.sf.jasperreports.engine;

/**
 * Common interface of design and print text elements.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id: JRTemplateReference.java 1759 2007-06-20 16:47:34Z lucianc $
 */
public interface JRText extends JRBox
{

	/**
	 * Constant useful for displaying the text without rotating it
	 */
	byte ROTATION_NONE = 0;

	/**
	 * Constant useful for rotating the text 90 degrees counter clockwise.
	 */
	byte ROTATION_LEFT = 1;

	/**
	 * Constant useful for rotating the text 90 degrees clockwise.
	 */
	byte ROTATION_RIGHT = 2;

	/**
	 * Constant useful for rotating the text 180 degrees.
	 */
	byte ROTATION_UPSIDE_DOWN = 3;

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

	int getWidth();
	
	int getHeight();
	
	/**
	 * Gets the text rotation.
	 * @return a value representing one of the rotation constants in this class
	 */
	byte getRotation();
	
	/**
	 * Gets the line spacing.
	 * @return a value representing one of the line spacing constants in this class
	 */
	byte getLineSpacing();
	
	/**
	 * Returns true if the text can contain style tags.
	 */
	boolean isStyledText();
	
	int getFontSize();
	
}
