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
package net.sf.jasperreports.charts.xml;

import net.sf.jasperreports.charts.design.JRDesignThermometerPlot;
import net.sf.jasperreports.charts.type.ValueLocationEnum;
import net.sf.jasperreports.engine.JRChart;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.util.JRColorUtil;
import net.sf.jasperreports.engine.xml.JRBaseFactory;

import org.xml.sax.Attributes;


/**
 * @author Barry Klawans (bklawans@users.sourceforge.net)
 * @version $Id$
 */
public class JRThermometerPlotFactory extends JRBaseFactory
{
	public static final String ELEMENT_thermometerPlot = "thermometerPlot";
	public static final String ELEMENT_lowRange = "lowRange";
	public static final String ELEMENT_mediumRange = "mediumRange";
	public static final String ELEMENT_highRange = "highRange";

	/**
	 * @deprecated No longer used.
	 */
	public static final String ATTRIBUTE_showValueLines = "isShowValueLines";
	public static final String ATTRIBUTE_valueLocation = "valueLocation";
	public static final String ATTRIBUTE_mercuryColor = "mercuryColor";

	/**
	 *
	 */
	public Object createObject(Attributes atts) throws JRException
	{
		JRChart chart = (JRChart)digester.peek();
		JRDesignThermometerPlot thermometerPlot = (JRDesignThermometerPlot)chart.getPlot();

		String location = atts.getValue(ATTRIBUTE_valueLocation);
		ValueLocationEnum loc = ValueLocationEnum.getByName(atts.getValue(ATTRIBUTE_valueLocation));
		if (loc == null)
		{
			throw new JRException("Invalid thermometer value location: " + location);
		}
		else
		{
			thermometerPlot.setValueLocation(loc);
		}

		String mercuryColor = atts.getValue(ATTRIBUTE_mercuryColor);
		if (mercuryColor != null && mercuryColor.length() > 0)
		{
			thermometerPlot.setMercuryColor(JRColorUtil.getColor(mercuryColor, null));
		}

		return thermometerPlot;
	}
}
