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

import net.sf.jasperreports.charts.design.JRDesignMeterPlot;
import net.sf.jasperreports.charts.type.MeterShapeEnum;
import net.sf.jasperreports.engine.JRChart;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.util.JRColorUtil;
import net.sf.jasperreports.engine.xml.JRBaseFactory;

import org.xml.sax.Attributes;


/**
 * @author Barry Klawans (bklawans@users.sourceforge.net)
 * @version $Id$
 */
public class JRMeterPlotFactory extends JRBaseFactory
{
	public static final String ELEMENT_meterPlot = "meterPlot";

	public static final String ATTRIBUTE_shape = "shape";
	public static final String ATTRIBUTE_angle = "angle";
	public static final String ATTRIBUTE_units = "units";
	public static final String ATTRIBUTE_tickInterval = "tickInterval";
	public static final String ATTRIBUTE_meterColor = "meterColor";
	public static final String ATTRIBUTE_needleColor = "needleColor";
	public static final String ATTRIBUTE_tickColor = "tickColor";

	/**
	 *
	 */
	public Object createObject(Attributes atts) throws JRException
	{
		JRChart chart = (JRChart)digester.peek();
		JRDesignMeterPlot meterPlot = (JRDesignMeterPlot)chart.getPlot();

		MeterShapeEnum shape = MeterShapeEnum.getByName(atts.getValue(ATTRIBUTE_shape));
		if (shape != null)
		{
			meterPlot.setShape(shape);
		}

		String angle = atts.getValue(ATTRIBUTE_angle);
		if (angle != null && angle.length() > 0)
		{
			meterPlot.setMeterAngle(Integer.valueOf(angle));
		}

		String units = atts.getValue(ATTRIBUTE_units);
		if (units != null && units.length() > 0)
		{
			meterPlot.setUnits(units);
		}

		String tickInterval = atts.getValue(ATTRIBUTE_tickInterval);
		if (tickInterval != null && tickInterval.length() > 0)
		{
			meterPlot.setTickInterval(Double.valueOf(tickInterval));
		}

		String meterColor = atts.getValue(ATTRIBUTE_meterColor);
		if (meterColor != null && meterColor.length() > 0)
		{
			meterPlot.setMeterBackgroundColor(JRColorUtil.getColor(meterColor, null));
		}

		String needleColor = atts.getValue(ATTRIBUTE_needleColor);
		if (needleColor != null && needleColor.length() > 0)
		{
			meterPlot.setNeedleColor(JRColorUtil.getColor(needleColor, null));
		}

		String tickColor = atts.getValue(ATTRIBUTE_tickColor);
		if (tickColor != null && tickColor.length() > 0)
		{
			meterPlot.setTickColor(JRColorUtil.getColor(tickColor, null));
		}

		return meterPlot;
	}
}
