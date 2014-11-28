/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2014 TIBCO Software Inc. All rights reserved.
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
package net.sf.jasperreports.engine.xml;

import java.util.Map;
import java.util.UUID;

import net.sf.jasperreports.engine.JRStyle;
import net.sf.jasperreports.engine.design.JRDesignElement;
import net.sf.jasperreports.engine.design.JRDesignGroup;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.type.ModeEnum;
import net.sf.jasperreports.engine.type.PositionTypeEnum;
import net.sf.jasperreports.engine.type.StretchTypeEnum;
import net.sf.jasperreports.engine.util.JRColorUtil;

import org.xml.sax.Attributes;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public class JRElementFactory extends JRBaseFactory
{

	/**
	 *
	 */
	public Object createObject(Attributes atts)
	{
		JRXmlLoader xmlLoader = (JRXmlLoader)digester.peek(digester.getCount() - 1);

		JRDesignElement element = (JRDesignElement)digester.peek();

		String uuid = atts.getValue(JRXmlConstants.ATTRIBUTE_uuid);
		if (uuid != null)
		{
			element.setUUID(UUID.fromString(uuid));
		}

		element.setKey(atts.getValue(JRXmlConstants.ATTRIBUTE_key));

		PositionTypeEnum positionType = PositionTypeEnum.getByName(atts.getValue(JRXmlConstants.ATTRIBUTE_positionType));
		if (positionType != null)
		{
			element.setPositionType(positionType);
		}

		StretchTypeEnum stretchType = StretchTypeEnum.getByName(atts.getValue(JRXmlConstants.ATTRIBUTE_stretchType));
		if (stretchType != null)
		{
			element.setStretchType(stretchType);
		}

		String isPrintRepeatedValues = atts.getValue(JRXmlConstants.ATTRIBUTE_isPrintRepeatedValues);
		if (isPrintRepeatedValues != null && isPrintRepeatedValues.length() > 0)
		{
			element.setPrintRepeatedValues(Boolean.valueOf(isPrintRepeatedValues).booleanValue());
		}
		
		ModeEnum mode = ModeEnum.getByName(atts.getValue(JRXmlConstants.ATTRIBUTE_mode));
		if (mode != null)
		{
			element.setMode(mode);
		}
		
		String x = atts.getValue(JRXmlConstants.ATTRIBUTE_x);
		if (x != null && x.length() > 0)
		{
			element.setX(Integer.parseInt(x));
		}

		String y = atts.getValue(JRXmlConstants.ATTRIBUTE_y);
		if (y != null && y.length() > 0)
		{
			element.setY(Integer.parseInt(y));
		}

		String width = atts.getValue(JRXmlConstants.ATTRIBUTE_width);
		if (width != null && width.length() > 0)
		{
			element.setWidth(Integer.parseInt(width));
		}

		String height = atts.getValue(JRXmlConstants.ATTRIBUTE_height);
		if (height != null && height.length() > 0)
		{
			element.setHeight(Integer.parseInt(height));
		}

		String isRemoveLineWhenBlank = atts.getValue(JRXmlConstants.ATTRIBUTE_isRemoveLineWhenBlank);
		if (isRemoveLineWhenBlank != null && isRemoveLineWhenBlank.length() > 0)
		{
			element.setRemoveLineWhenBlank(Boolean.valueOf(isRemoveLineWhenBlank).booleanValue());
		}

		String isPrintInFirstWholeBand = atts.getValue(JRXmlConstants.ATTRIBUTE_isPrintInFirstWholeBand);
		if (isPrintInFirstWholeBand != null && isPrintInFirstWholeBand.length() > 0)
		{
			element.setPrintInFirstWholeBand(Boolean.valueOf(isPrintInFirstWholeBand).booleanValue());
		}

		String isPrintWhenDetailOverflows = atts.getValue(JRXmlConstants.ATTRIBUTE_isPrintWhenDetailOverflows);
		if (isPrintWhenDetailOverflows != null && isPrintWhenDetailOverflows.length() > 0)
		{
			element.setPrintWhenDetailOverflows(Boolean.valueOf(isPrintWhenDetailOverflows).booleanValue());
		}

		String groupName = atts.getValue(JRXmlConstants.ATTRIBUTE_printWhenGroupChanges);
		if (groupName != null)
		{
			JRDesignGroup group = new JRDesignGroup();
			group.setName(groupName);
			element.setPrintWhenGroupChanges(group);
			xmlLoader.addGroupReprintedElement(element);
		}

		String forecolor = atts.getValue(JRXmlConstants.ATTRIBUTE_forecolor);
		if (forecolor != null && forecolor.length() > 0)
		{
			element.setForecolor(JRColorUtil.getColor(forecolor, null));
		}

		String backcolor = atts.getValue(JRXmlConstants.ATTRIBUTE_backcolor);
		if (backcolor != null && backcolor.length() > 0)
		{
			element.setBackcolor(JRColorUtil.getColor(backcolor, null));
		}
		
		String styleName = atts.getValue(JRXmlConstants.ATTRIBUTE_style);
		if (styleName != null)
		{
			JasperDesign jasperDesign = (JasperDesign)digester.peek(digester.getCount() - 2);
			Map<String,JRStyle> stylesMap = jasperDesign.getStylesMap();

			if (stylesMap.containsKey(styleName))
			{
				JRStyle style = stylesMap.get(styleName);
				element.setStyle(style);
			}
			else
			{
				element.setStyleNameReference(styleName);
			}
		}

		return element;
	}
}
