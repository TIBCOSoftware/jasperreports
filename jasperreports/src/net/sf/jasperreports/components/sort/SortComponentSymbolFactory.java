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
package net.sf.jasperreports.components.sort;

import java.awt.Color;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.type.HorizontalImageAlignEnum;
import net.sf.jasperreports.engine.type.SortFieldTypeEnum;
import net.sf.jasperreports.engine.type.VerticalImageAlignEnum;
import net.sf.jasperreports.engine.util.JRColorUtil;
import net.sf.jasperreports.engine.xml.JRBaseFactory;

import org.xml.sax.Attributes;


/**
 * @author Narcis Marcu (narcism@users.sourceforge.net)
 */
public class SortComponentSymbolFactory extends JRBaseFactory
{
	/**
	 *
	 */
	public Object createObject(Attributes atts) throws JRException
	{
		SortComponent sortComponent = (SortComponent)digester.peek();
		
		// Set the text color
		String attrValue = atts.getValue(SortComponent.PROPERTY_HANDLER_COLOR);
		if (attrValue != null && attrValue.length() > 0)
		{
			Color color = JRColorUtil.getColor(attrValue, null);
			sortComponent.setHandlerColor(color);
		}
		
		sortComponent.setSortFieldName(atts.getValue(SortComponent.PROPERTY_COLUMN_NAME));

		SortFieldTypeEnum fieldType = SortFieldTypeEnum.getByName(atts.getValue(SortComponent.PROPERTY_COLUMN_TYPE));
		if (fieldType != null)
		{
			sortComponent.setSortFieldType(fieldType);
		}

		HorizontalImageAlignEnum hAlign = HorizontalImageAlignEnum.getByName(atts.getValue(SortComponent.PROPERTY_HANDLER_HORIZONTAL_ALIGN));
		if (hAlign != null)
		{
			sortComponent.setHandlerHorizontalImageAlign(hAlign);
		}
		
		VerticalImageAlignEnum vAlign = VerticalImageAlignEnum.getByName(atts.getValue(SortComponent.PROPERTY_HANDLER_VERTICAL_ALIGN));
		if (vAlign != null)
		{
			sortComponent.setHandlerVerticalImageAlign(vAlign);
		}
		
		return sortComponent;
	}
}
