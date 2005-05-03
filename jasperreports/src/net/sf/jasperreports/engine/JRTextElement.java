/*
 * ============================================================================
 *                   GNU Lesser General Public License
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
public interface JRTextElement extends JRElement, JRAlignment
{


	/**
	 *
	 */
	public static final byte TEXT_ALIGN_LEFT = HORIZONTAL_ALIGN_LEFT;
	public static final byte TEXT_ALIGN_CENTER = HORIZONTAL_ALIGN_CENTER;
	public static final byte TEXT_ALIGN_RIGHT = HORIZONTAL_ALIGN_RIGHT;
	public static final byte TEXT_ALIGN_JUSTIFIED = HORIZONTAL_ALIGN_JUSTIFIED;

	/**
	 *
	 */
	public static final byte ROTATION_NONE = 0;
	public static final byte ROTATION_LEFT = 1;
	public static final byte ROTATION_RIGHT = 2;

	/**
	 *
	 */
	public static final byte LINE_SPACING_SINGLE = 0;
	public static final byte LINE_SPACING_1_1_2 = 1;
	public static final byte LINE_SPACING_DOUBLE = 2;


	/**
	 *
	 */
	public byte getTextAlignment();
		
	/**
	 *
	 */
	public void setTextAlignment(byte horizontalAlignment);
		
	/**
	 *
	 */
	public byte getVerticalAlignment();
		
	/**
	 *
	 */
	public void setVerticalAlignment(byte verticalAlignment);
		
	/**
	 *
	 */
	public byte getRotation();
		
	/**
	 *
	 */
	public void setRotation(byte rotation);
		
	/**
	 *
	 */
	public byte getLineSpacing();

	/**
	 *
	 */
	public void setLineSpacing(byte lineSpacing);
		
	/**
	 *
	 */
	public boolean isStyledText();
		
	/**
	 *
	 */
	public void setStyledText(boolean isStyledText);
		
	/**
	 *
	 */
	public JRBox getBox();

	/**
	 *
	 */
	public JRFont getFont();

	
}
