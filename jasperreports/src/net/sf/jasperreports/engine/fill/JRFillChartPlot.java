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
package net.sf.jasperreports.engine.fill;

import java.awt.Color;
import java.util.Collection;
import java.util.SortedSet;

import net.sf.jasperreports.charts.JRCategoryAxisFormat;
import net.sf.jasperreports.charts.type.PlotOrientationEnum;
import net.sf.jasperreports.engine.JRChart;
import net.sf.jasperreports.engine.JRChartPlot;
import net.sf.jasperreports.engine.JRExpressionCollector;
import net.sf.jasperreports.engine.util.JRStyleResolver;

import org.jfree.chart.plot.PlotOrientation;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public class JRFillChartPlot implements JRChartPlot
{


	/**
	 *
	 */
	protected JRChartPlot parent;

	/**
	 *
	 */
	protected JRChart chart;


	/**
	 *
	 */
	protected JRFillChartPlot(
		JRChartPlot plot, 
		JRFillObjectFactory factory
		)
	{
		factory.put(plot, this);

		parent = plot;
		
		chart = (JRChart)factory.getVisitResult(plot.getChart());
	}

	protected void setBand(JRFillBand band)
	{
		//NOP
	}


	/**
	 *
	 */
	public JRChart getChart()
	{
		return chart;
	}
	
	/**
	 *
	 */
	public Color getBackcolor()
	{
		return JRStyleResolver.getBackcolor(this);
	}
	
	/**
	 *
	 */
	public Color getOwnBackcolor()
	{
		return parent.getOwnBackcolor();
	}
	
	/**
	 *
	 */
	public void setBackcolor(Color backcolor)
	{
	}

	/**
	 * @deprecated Replaed by {@link #getOrientationValue()}.
	 */
	public PlotOrientation getOrientation()
	{
		return getOrientationValue().getOrientation();
	}
	
	/**
	 *
	 */
	public PlotOrientationEnum getOrientationValue()
	{
		return parent.getOrientationValue();
	}
	
	/**
	 * @deprecated Replaced by {@link #setOrientation(PlotOrientationEnum)}.
	 */
	public void setOrientation(PlotOrientation orientation)
	{
		setOrientation(PlotOrientationEnum.getByValue(orientation));
	}
		
	/**
	 *
	 */
	public void setOrientation(PlotOrientationEnum orientation)
	{
		throw new UnsupportedOperationException();
	}
		
	/**
	 *
	 */
	public Float getBackgroundAlphaFloat()
	{
		return parent.getBackgroundAlphaFloat();
	}
	
	/**
	 *
	 */
	public void setBackgroundAlpha(Float BackgroundAlpha)
	{
	}
	
	/**
	 *
	 */
	public Float getForegroundAlphaFloat()
	{
		return parent.getForegroundAlphaFloat();
	}
	
	/**
	 *
	 */
	public void setForegroundAlpha(Float foregroundAlpha)
	{
	}
	
	/**
	 * @deprecated Replaced by {@link JRCategoryAxisFormat#getCategoryAxisTickLabelRotation()}.
	 */
	public Double getLabelRotationDouble()
	{
		return parent.getLabelRotationDouble();
	}
	
	/**
	 * @deprecated Replaced by {@link JRCategoryAxisFormat#setCategoryAxisTickLabelRotation(Double)}.
	 */
	public void setLabelRotation(Double labelRotation)
	{
		throw new UnsupportedOperationException();
	}
	
	/**
	 * Returns a list of all the defined series colors.  Every entry in the list is of type JRChartPlot.JRSeriesColor.
	 * If there are no defined series colors this method will return an empty list, not null. 
	 */
	public SortedSet<JRSeriesColor> getSeriesColors()
	{
		return parent.getSeriesColors();
	}
	
	/**
	 * Removes all defined series colors.
	 */
	public void clearSeriesColors()
	{
	}
	
	/**
	 * Adds the specified series color to the plot.
	 */
	public void addSeriesColor(JRSeriesColor seriesColor)
	{
	}

	public void setSeriesColors(Collection<JRSeriesColor> colors)
	{
		// NOOP
	}
	
	/**
	 *
	 */
	public void collectExpressions(JRExpressionCollector collector)
	{
	}

	/**
	 *
	 */
	public Object clone() 
	{
		throw new UnsupportedOperationException();
	}
	
	/**
	 *
	 */
	public Object clone(JRChart parentChart) 
	{
		throw new UnsupportedOperationException();
	}
}
