/*
 * ============================================================================
 * GNU Lesser General Public License
 * ============================================================================
 *
 * JasperReports - Free Java report-generating library.
 * Copyright (C) 2001-2006 JasperSoft Corporation http://www.jaspersoft.com
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
 * 303 Second Street, Suite 450 North
 * San Francisco, CA 94107
 * http://www.jaspersoft.com
 */
package net.sf.jasperreports.engine.xml;

import java.awt.Color;

import net.sf.jasperreports.engine.JRChartPlot;
import net.sf.jasperreports.engine.base.JRBaseChartPlot;

import org.jfree.chart.plot.PlotOrientation;
import org.xml.sax.Attributes;


/**
 * @author Ionut Nedelcu (ionutned@users.sourceforge.net)
 * @version $Id$
 */
public class JRChartPlotFactory extends JRBaseFactory
{

	/**
	 *
	 */
	public Object createObject(Attributes atts)
	{
		JRChartPlot plot = (JRChartPlot) digester.peek();

		Color color = JRXmlConstants.getColor(atts.getValue(JRXmlConstants.ATTRIBUTE_backcolor), Color.black);
		if (color != null)
		{
			plot.setBackcolor(color);
		}

		String orientation = atts.getValue(JRXmlConstants.ATTRIBUTE_orientation);
		if (orientation != null && orientation.length() > 0)
			plot.setOrientation((PlotOrientation)JRXmlConstants.getPlotOrientationMap().get(orientation));

		String foregroundAlpha = atts.getValue(JRXmlConstants.ATTRIBUTE_foregroundAlpha);
		if (foregroundAlpha != null && foregroundAlpha.length() > 0)
			plot.setForegroundAlpha(Float.valueOf(foregroundAlpha).floatValue());

		String backgroundAlpha = atts.getValue(JRXmlConstants.ATTRIBUTE_backgroundAlpha);
		if (backgroundAlpha != null && backgroundAlpha.length() > 0)
			plot.setBackgroundAlpha(Float.valueOf(backgroundAlpha).floatValue());

		String labelRotation = atts.getValue(JRXmlConstants.ATTRIBUTE_labelRotation);
		if (labelRotation != null && labelRotation.length() > 0)
			plot.setLabelRotation(Double.valueOf(labelRotation).doubleValue());

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
				seriesIndex = Integer.valueOf(seriesNumber).intValue();

			String colorName = atts.getValue(JRXmlConstants.ATTRIBUTE_color);
			if (colorName != null && colorName.length() > 0)
				color = JRXmlConstants.getColor(colorName, null);
			
			return new JRBaseChartPlot.JRBaseSeriesColor(seriesIndex, color);
		}
	}
}
