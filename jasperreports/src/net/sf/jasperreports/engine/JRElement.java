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

import java.awt.Color;


/**
 * An abstract representation of a report element. All report elements implement this interface. The interface contains
 * constants and methods that apply to all report elements.
 *
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public interface JRElement extends JRChild, JRStyleContainer
{


	/**
	 * The element will float in its parent section if it is pushed downwards by other elements fount above it.
	 * It will try to conserve the distance between it and the neighboring elements placed immediately above.
	 */
	public static final byte POSITION_TYPE_FLOAT = 1;


	/**
	 * The element will simply ignore what happens to the other section elements and tries to
	 * conserve the y offset measured from the top of its parent report section.
	 */
	public static final byte POSITION_TYPE_FIX_RELATIVE_TO_TOP = 2;


	/**
	 * If the height of the parent report section is affected by elements that stretch, the current element will try to
	 * conserve the original distance between its bottom margin and the bottom of the band.
	 */
	public static final byte POSITION_TYPE_FIX_RELATIVE_TO_BOTTOM = 3;

	/**
	 * Specifies that the element is opaque.
	 */
	public static final byte MODE_OPAQUE = 1;


	/**
	 * Specifies that the element is transparent.
	 */
	public static final byte MODE_TRANSPARENT = 2;

	/**
	 * The element preserves its original specified height.
	 */
	public static final byte STRETCH_TYPE_NO_STRETCH = 0;

	/**
	 * Users have the possibility to group the elements of a report section in multiple imbricate groups. The only
	 * reason one might have for grouping your report elements is to be able to stretch them to fit the tallest object.
	 */
	public static final byte STRETCH_TYPE_RELATIVE_TO_TALLEST_OBJECT = 1;

	/**
	 * The graphic element will adapt its height to match the new height of the report section it placed on, which
	 * has been affected by stretch.
	 */
	public static final byte STRETCH_TYPE_RELATIVE_TO_BAND_HEIGHT = 2;


	/**
	 * Returns the string value that uniquely identifies the element.
	 */
	public String getKey();

	/**
	 * Returns the position type for the element
	 * @return a byte value representing one of the position type constants in this class
	 */
	public byte getPositionType();

	/**
	 * Sets the position type for the element.
	 * @param positionType a byte value that must be one of the position type constants in this class
	 */
	public void setPositionType(byte positionType);

	/**
	 * Returns the stretch type for the element
	 * @return a byte value representing one of the strech type constants in this class
	 */
	public byte getStretchType();

	/**
	 * Sets the stretch type for the element.
	 * @param stretchType a byte value that must be one of the stretch type constants in this class
	 */
	public void setStretchType(byte stretchType);
		
	/**
	 * Specifies if the element value will be printed for every iteration, even if its value has not changed.
	 * @see JRElement#isRemoveLineWhenBlank()
	 * @see JRElement#isPrintInFirstWholeBand()
	 */
	public boolean isPrintRepeatedValues();
	
	/**
	 *
	 */
	public void setPrintRepeatedValues(boolean isPrintRepeatedValues);


	/**
	 * Returns the element transparency mode.
	 * The default value depends on the type of the report element. Graphic elements like rectangles and lines are
	 * opaque by default, but the images are transparent. Both static texts and text fields are transparent
	 * by default, and so are the subreport elements.
	 * @return MODE_OPAQUE or MODE_TRANSPARENT
	 */
	public byte getMode();
	
	public Byte getOwnMode();

	/**
	 * Returns the element transparency mode.
	 * The default value depends on the type of the report element. Graphic elements like rectangles and lines are
	 * opaque by default, but the images are transparent. Both static texts and text fields are transparent
	 * by default, and so are the subreport elements.
	 */
	public void setMode(byte mode);
	
	public void setMode(Byte mode);
	
	/**
	 * Gets the the section relative horizontal offset of the element top left corner.
	 */
	public int getX();
	
	/**
	 * Sets the the section relative horizontal offset of the element top left corner.
	 */
	public void setX(int x);
	
	/**
	 * Gets the the section relative vertical offset of the element top left corner.
	 */
	public int getY();
	
	/**
	 *
	 */
	public int getWidth();
	
	/**
	 *
	 */
	public void setWidth(int width);
	
	/**
	 *
	 */
	public int getHeight();
	
	/**
	 * Returns true if the remaining blank space appearing when the value is not printed will be removed. Under certain
	 * circumstances (the element has an empty string as its value or contains a repeated value that is supressed) the
	 * space reserved for the current element remains empty. If this method returns true, it means the engine will try
	 * to suppress the blank line, but will only succeed if no other elements occupy the same vertical space.
	 */
	public boolean isRemoveLineWhenBlank();
	
	/**
	 * Specifies whether the remaining blank space appearing when the value is not printed will be removed. Under certain
	 * circumstances (the element has an empty string as its value or contains a repeated value that is supressed) the
	 * space reserved for the current element remains empty. If the parameter is set to true, it means the engine will try
	 * to suppress the blank line, but will only succeed if no other elements occupy the same vertical space.
	 */
	public void setRemoveLineWhenBlank(boolean isRemoveLineWhenBlank);
	
	/**
	 * Returns true if an element with a <i>printRepeatedValues</i> attribute set to true will be redisplayed for every
	 * new page or column that is not an overflow from a previous page or column.
	 * @see JRElement#isPrintRepeatedValues()
	 */
	public boolean isPrintInFirstWholeBand();
	
	/**
	 * Specifies whether an element with a <i>printRepeatedValues</i> attribute set to true should be redisplayed for every
	 * new page or column that is not an overflow from a previous page or column.
	 * @see JRElement#isPrintRepeatedValues()
	 */
	public void setPrintInFirstWholeBand(boolean isPrintInFirstWholeBand);
	
	/**
	 * If this is set to true, the element will be reprinted on the next page if the band does not fit in the current page.
	 * Actually if there is at least one element with this attribute, the band is redisplayed from the beginning, except
	 * those elements that fitted in the current page and have <i>isPrintWhenDetailOverflow</i> set to false.
	 */
	public boolean isPrintWhenDetailOverflows();
	
	/**
	 * If this is set to true, the element will be reprinted on the next page if the band does not fit in the current page.
	 * Actually if there is at least one element with this attribute, the band is redisplayed from the beginning, except
	 * those elements that fitted in the current page and have <i>isPrintWhenDetailOverflow</i> set to false.
	 */
	public void setPrintWhenDetailOverflows(boolean isPrintWhenDetailOverflows);
	
	/**
	 *
	 */
	public Color getForecolor();
	
	/**
	 *
	 */
	public Color getOwnForecolor();

	/**
	 *
	 */
	public void setForecolor(Color forecolor);
	
	/**
	 *
	 */
	public Color getBackcolor();
	
	/**
	 *
	 */
	public Color getOwnBackcolor();

	/**
	 *
	 */
	public void setBackcolor(Color backcolor);
	
	/**
	 * Gets the the expression that is evaluated in order to decide if the element should be displayed. The print
	 * expression always returns a boolean value.
	 */
	public JRExpression getPrintWhenExpression();
	
	/**
	 * Returns the group for which an element with a <i>printRepeatedValues</i> attribute set to true will be redisplayed
	 * even if the value has not changed.
	 * @see JRElement#isPrintRepeatedValues()
	 */
	public JRGroup getPrintWhenGroupChanges();
	
	/**
	 * Indicates the logical group that the element belongs to. More elements can be grouped in order to get the height
	 * of the tallest one.
	 * @see JRElement#STRETCH_TYPE_RELATIVE_TO_TALLEST_OBJECT
	 */
	public JRElementGroup getElementGroup();

	/**
	 *
	 */
	public void collectExpressions(JRExpressionCollector collector);


}
