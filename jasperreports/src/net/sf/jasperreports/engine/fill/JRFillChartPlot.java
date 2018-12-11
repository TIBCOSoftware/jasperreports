/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2018 TIBCO Software Inc. All rights reserved.
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

import org.jfree.chart.plot.PlotOrientation;

import net.sf.jasperreports.charts.JRCategoryAxisFormat;
import net.sf.jasperreports.charts.type.PlotOrientationEnum;
import net.sf.jasperreports.engine.JRChart;
import net.sf.jasperreports.engine.JRChartPlot;
import net.sf.jasperreports.engine.JRExpressionCollector;
import net.sf.jasperreports.engine.util.StyleResolver;


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


	@Override
	public JRChart getChart()
	{
		return chart;
	}

	/**
	 *
	 */
	protected StyleResolver getStyleResolver() 
	{
		return getChart().getDefaultStyleProvider().getStyleResolver();
	}

	@Override
	public Color getBackcolor()
	{
		return getStyleResolver().getBackcolor(this);
	}
	
	@Override
	public Color getOwnBackcolor()
	{
		return parent.getOwnBackcolor();
	}
	
	@Override
	public void setBackcolor(Color backcolor)
	{
	}

	/**
	 * @deprecated Replaed by {@link #getOrientationValue()}.
	 */
	@Override
	public PlotOrientation getOrientation()
	{
		return getOrientationValue().getOrientation();
	}
	
	@Override
	public PlotOrientationEnum getOrientationValue()
	{
		return parent.getOrientationValue();
	}
	
	/**
	 * @deprecated Replaced by {@link #setOrientation(PlotOrientationEnum)}.
	 */
	@Override
	public void setOrientation(PlotOrientation orientation)
	{
		setOrientation(PlotOrientationEnum.getByValue(orientation));
	}
		
	@Override
	public void setOrientation(PlotOrientationEnum orientation)
	{
		throw new UnsupportedOperationException();
	}
		
	@Override
	public Float getBackgroundAlphaFloat()
	{
		return parent.getBackgroundAlphaFloat();
	}
	
	@Override
	public void setBackgroundAlpha(Float BackgroundAlpha)
	{
	}
	
	@Override
	public Float getForegroundAlphaFloat()
	{
		return parent.getForegroundAlphaFloat();
	}
	
	@Override
	public void setForegroundAlpha(Float foregroundAlpha)
	{
	}
	
	/**
	 * @deprecated Replaced by {@link JRCategoryAxisFormat#getCategoryAxisTickLabelRotation()}.
	 */
	@Override
	public Double getLabelRotationDouble()
	{
		return parent.getLabelRotationDouble();
	}
	
	/**
	 * @deprecated Replaced by {@link JRCategoryAxisFormat#setCategoryAxisTickLabelRotation(Double)}.
	 */
	@Override
	public void setLabelRotation(Double labelRotation)
	{
		throw new UnsupportedOperationException();
	}
	
	/**
	 * Returns a list of all the defined series colors.  Every entry in the list is of type JRChartPlot.JRSeriesColor.
	 * If there are no defined series colors this method will return an empty list, not null. 
	 */
	@Override
	public SortedSet<JRSeriesColor> getSeriesColors()
	{
		return parent.getSeriesColors();
	}
	
	/**
	 * Removes all defined series colors.
	 */
	@Override
	public void clearSeriesColors()
	{
	}
	
	/**
	 * Adds the specified series color to the plot.
	 */
	@Override
	public void addSeriesColor(JRSeriesColor seriesColor)
	{
	}

	@Override
	public void setSeriesColors(Collection<JRSeriesColor> colors)
	{
		// NOOP
	}
	
	@Override
	public void collectExpressions(JRExpressionCollector collector)
	{
	}

	@Override
	public Object clone() 
	{
		throw new UnsupportedOperationException();
	}
	
	@Override
	public Object clone(JRChart parentChart) 
	{
		throw new UnsupportedOperationException();
	}
}
