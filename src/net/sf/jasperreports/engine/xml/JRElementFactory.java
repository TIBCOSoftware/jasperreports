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
package net.sf.jasperreports.engine.xml;

import java.awt.Color;
import java.util.Collection;

import org.xml.sax.Attributes;

import net.sf.jasperreports.engine.design.JRDesignElement;
import net.sf.jasperreports.engine.design.JRDesignGroup;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JRElementFactory extends JRBaseFactory
{


	/**
	 *
	 */
	private static final String ATTRIBUTE_key = "key";
	private static final String ATTRIBUTE_positionType = "positionType";
	private static final String ATTRIBUTE_stretchType = "stretchType";
	private static final String ATTRIBUTE_isPrintRepeatedValues = "isPrintRepeatedValues";
	private static final String ATTRIBUTE_mode = "mode";
	private static final String ATTRIBUTE_x = "x";
	private static final String ATTRIBUTE_y = "y";
	private static final String ATTRIBUTE_width = "width";
	private static final String ATTRIBUTE_height = "height";
	private static final String ATTRIBUTE_isRemoveLineWhenBlank = "isRemoveLineWhenBlank";
	private static final String ATTRIBUTE_isPrintInFirstWholeBand = "isPrintInFirstWholeBand";
	private static final String ATTRIBUTE_isPrintWhenDetailOverflows = "isPrintWhenDetailOverflows";
	private static final String ATTRIBUTE_printWhenGroupChanges = "printWhenGroupChanges";
	private static final String ATTRIBUTE_forecolor = "forecolor";
	private static final String ATTRIBUTE_backcolor = "backcolor";


	/**
	 *
	 */
	public Object createObject(Attributes atts)
	{
		JRXmlLoader xmlLoader = (JRXmlLoader)digester.peek(digester.getCount() - 1);
		Collection groupReprintedElements = xmlLoader.getGroupReprintedElements();

		JRDesignElement element = (JRDesignElement)digester.peek();

		element.setKey(atts.getValue(ATTRIBUTE_key));

		Byte positionType = (Byte)JRXmlConstants.getPositionTypeMap().get(atts.getValue(ATTRIBUTE_positionType));
		if (positionType != null)
		{
			element.setPositionType(positionType.byteValue());
		}

		Byte stretchType = (Byte)JRXmlConstants.getStretchTypeMap().get(atts.getValue(ATTRIBUTE_stretchType));
		if (stretchType != null)
		{
			element.setStretchType(stretchType.byteValue());
		}

		String isPrintRepeatedValues = atts.getValue(ATTRIBUTE_isPrintRepeatedValues);
		if (isPrintRepeatedValues != null && isPrintRepeatedValues.length() > 0)
		{
			element.setPrintRepeatedValues(Boolean.valueOf(isPrintRepeatedValues).booleanValue());
		}
		
		Byte mode = (Byte)JRXmlConstants.getModeMap().get(atts.getValue(ATTRIBUTE_mode));
		if (mode != null)
		{
			element.setMode(mode.byteValue());
		}
		
		String x = atts.getValue(ATTRIBUTE_x);
		if (x != null && x.length() > 0)
		{
			element.setX(Integer.parseInt(x));
		}

		String y = atts.getValue(ATTRIBUTE_y);
		if (y != null && y.length() > 0)
		{
			element.setY(Integer.parseInt(y));
		}

		String width = atts.getValue(ATTRIBUTE_width);
		if (width != null && width.length() > 0)
		{
			element.setWidth(Integer.parseInt(width));
		}

		String height = atts.getValue(ATTRIBUTE_height);
		if (height != null && height.length() > 0)
		{
			element.setHeight(Integer.parseInt(height));
		}

		String isRemoveLineWhenBlank = atts.getValue(ATTRIBUTE_isRemoveLineWhenBlank);
		if (isRemoveLineWhenBlank != null && isRemoveLineWhenBlank.length() > 0)
		{
			element.setRemoveLineWhenBlank(Boolean.valueOf(isRemoveLineWhenBlank).booleanValue());
		}

		String isPrintInFirstWholeBand = atts.getValue(ATTRIBUTE_isPrintInFirstWholeBand);
		if (isPrintInFirstWholeBand != null && isPrintInFirstWholeBand.length() > 0)
		{
			element.setPrintInFirstWholeBand(Boolean.valueOf(isPrintInFirstWholeBand).booleanValue());
		}

		String isPrintWhenDetailOverflows = atts.getValue(ATTRIBUTE_isPrintWhenDetailOverflows);
		if (isPrintWhenDetailOverflows != null && isPrintWhenDetailOverflows.length() > 0)
		{
			element.setPrintWhenDetailOverflows(Boolean.valueOf(isPrintWhenDetailOverflows).booleanValue());
		}

		String groupName = atts.getValue(ATTRIBUTE_printWhenGroupChanges);
		if (groupName != null)
		{
			JRDesignGroup group = new JRDesignGroup();
			group.setName(groupName);
			element.setPrintWhenGroupChanges(group);
			groupReprintedElements.add(element);
		}

		String forecolor = atts.getValue(ATTRIBUTE_forecolor);
		if (forecolor != null && forecolor.length() > 0)
		{
			char firstChar = forecolor.charAt(0);
			if (firstChar == '#')
			{
				element.setForecolor(new Color(Integer.parseInt(forecolor.substring(1), 16)));
			}
			else if ('0' <= firstChar && firstChar <= '9')
			{
				element.setForecolor(new Color(Integer.parseInt(forecolor)));
			}
			else
			{
				if (JRXmlConstants.getColorMap().containsKey(forecolor))
				{
					element.setForecolor((Color)JRXmlConstants.getColorMap().get(forecolor));
				}
				else
				{
					element.setForecolor(Color.black);
				}
			}
		}

		String backcolor = atts.getValue(ATTRIBUTE_backcolor);
		if (backcolor != null && backcolor.length() > 0)
		{
			char firstChar = backcolor.charAt(0);
			if (firstChar == '#')
			{
				element.setBackcolor(new Color(Integer.parseInt(backcolor.substring(1), 16)));
			}
			else if ('0' <= firstChar && firstChar <= '9')
			{
				element.setBackcolor(new Color(Integer.parseInt(backcolor)));
			}
			else
			{
				if (JRXmlConstants.getColorMap().containsKey(backcolor))
				{
					element.setBackcolor((Color)JRXmlConstants.getColorMap().get(backcolor));
				}
				else
				{
					element.setBackcolor(Color.white);
				}
			}
		}

		return element;
	}
	

}
