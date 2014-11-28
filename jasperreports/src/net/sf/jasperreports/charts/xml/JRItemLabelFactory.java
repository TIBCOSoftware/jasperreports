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
package net.sf.jasperreports.charts.xml;

import net.sf.jasperreports.charts.design.JRDesignItemLabel;
import net.sf.jasperreports.engine.JRChartPlot;
import net.sf.jasperreports.engine.util.JRColorUtil;
import net.sf.jasperreports.engine.xml.JRBaseFactory;

import org.xml.sax.Attributes;


/**
 * @author sanda zaharia (shertage@users.sourceforge.net)
 */
public class JRItemLabelFactory extends JRBaseFactory
{
	private static final String ATTRIBUTE_color = "color";
	private static final String ATTRIBUTE_backgroundColor = "backgroundColor";
//	private static final String ATTRIBUTE_mask = "mask";

	/**
	 *
	 */
	public Object createObject(Attributes atts)
	{
		JRDesignItemLabel itemLabel = new JRDesignItemLabel(null, ((JRChartPlot)digester.peek()).getChart());

		String color = atts.getValue(ATTRIBUTE_color);
		if (color != null && color.length() > 0)
		{
			itemLabel.setColor(JRColorUtil.getColor(color, null));
		}

		String backgroundColor = atts.getValue(ATTRIBUTE_backgroundColor);
		if (backgroundColor != null && backgroundColor.length() > 0)
		{
			itemLabel.setBackgroundColor(JRColorUtil.getColor(backgroundColor, null));
		}

//		String mask = atts.getValue(ATTRIBUTE_mask);
//		if (mask != null && mask.length() > 0)
//		{
//			itemLabel.setMask(mask);
//		}
		return itemLabel;
	}
}
