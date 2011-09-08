/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2011 Jaspersoft Corporation. All rights reserved.
 * http://www.jaspersoft.com
 *
 * Unless you have purchased a commercial license agreement from Jaspersoft,
 * the following license terms apply:
 *
 * This program is part of JasperReports.
 *
 * JasperReports is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * JasperReports is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with JasperReports. If not, see <http://www.gnu.org/licenses/>.
 */
package net.sf.jasperreports.engine;

import net.sf.jasperreports.engine.type.PositionTypeEnum;
import net.sf.jasperreports.engine.type.StretchTypeEnum;


/**
 * An abstract representation of a report element. All report elements implement this interface. The interface contains
 * constants and methods that apply to all report elements.
 *
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public interface JRElement extends JRChild, JRCommonElement, JRPropertiesHolder
{

	/**
	 * Returns the string value that uniquely identifies the element.
	 */
	public String getKey();

	/**
	 * Returns the position type for the element
	 * @return the position type
	 */
	public PositionTypeEnum getPositionTypeValue();

	/**
	 * Sets the position type for the element.
	 * @param positionType the position type
	 */
	public void setPositionType(PositionTypeEnum positionType);

	/**
	 * Indicates the stretch type for the element
	 * @return a value representing one of the stretch type constants in {@link StretchTypeEnum}
	 */
	public StretchTypeEnum getStretchTypeValue();
	
	/**
	 * Specifies how the engine should treat a missing image.
	 * @param stretchTypeEnum a value representing one of the stretch type constants in {@link StretchTypeEnum}
	 */
	public void setStretchType(StretchTypeEnum stretchTypeEnum);
	
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
	public void setWidth(int width);
	
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
	 * @see StretchTypeEnum#RELATIVE_TO_TALLEST_OBJECT
	 */
	public JRElementGroup getElementGroup();

	/**
	 *
	 */
	public void collectExpressions(JRExpressionCollector collector);


	/**
	 * Returns the list of dynamic/expression-based properties for this report element.
	 * 
	 * @return an array containing the expression-based properties of this report element
	 */
	public JRPropertyExpression[] getPropertyExpressions();
}
