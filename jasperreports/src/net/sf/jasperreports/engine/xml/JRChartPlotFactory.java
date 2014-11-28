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

import java.awt.Color;

import net.sf.jasperreports.charts.type.PlotOrientationEnum;
import net.sf.jasperreports.engine.JRChartPlot;
import net.sf.jasperreports.engine.base.JRBaseChartPlot;
import net.sf.jasperreports.engine.util.JRColorUtil;

import org.xml.sax.Attributes;


/**
 * @author Ionut Nedelcu (ionutned@users.sourceforge.net)
 */
public class JRChartPlotFactory extends JRBaseFactory
{

	/**
	 *
	 */
	@SuppressWarnings("deprecation")
	public Object createObject(Attributes atts)
	{
		JRChartPlot plot = (JRChartPlot) digester.peek();

		Color color = JRColorUtil.getColor(atts.getValue(JRXmlConstants.ATTRIBUTE_backcolor), Color.black);
		if (color != null)
		{
			plot.setBackcolor(color);
		}

		PlotOrientationEnum orientation = PlotOrientationEnum.getByName(atts.getValue(JRXmlConstants.ATTRIBUTE_orientation));
		if (orientation != null)
		{
			plot.setOrientation(orientation);
		}
		String foregroundAlpha = atts.getValue(JRXmlConstants.ATTRIBUTE_foregroundAlpha);
		if (foregroundAlpha != null && foregroundAlpha.length() > 0)
		{
			plot.setForegroundAlpha(Float.valueOf(foregroundAlpha));
		}
		String backgroundAlpha = atts.getValue(JRXmlConstants.ATTRIBUTE_backgroundAlpha);
		if (backgroundAlpha != null && backgroundAlpha.length() > 0)
		{
			plot.setBackgroundAlpha(Float.valueOf(backgroundAlpha));
		}
		String labelRotation = atts.getValue(JRXmlConstants.ATTRIBUTE_labelRotation);
		if (labelRotation != null && labelRotation.length() > 0)
		{
			plot.setLabelRotation(Double.valueOf(labelRotation));
		}
		return plot;
	}
	
	public static class JRSeriesColorFactory extends JRBaseFactory
	{
		public Object createObject(Attributes atts)
		{
			int seriesIndex = -1;
			Color color = null;
			
			String seriesNumber = atts.getValue(JRXmlConstants.ATTRIBUTE_seriesOrder);
			if (seriesNumber != null && seriesNumber.length() > 0)
			{
				seriesIndex = Integer.valueOf(seriesNumber).intValue();
			}
			String colorName = atts.getValue(JRXmlConstants.ATTRIBUTE_color);
			if (colorName != null && colorName.length() > 0)
			{
				color = JRColorUtil.getColor(colorName, null);
			}
			
			return new JRBaseChartPlot.JRBaseSeriesColor(seriesIndex, color);
		}
	}
}
