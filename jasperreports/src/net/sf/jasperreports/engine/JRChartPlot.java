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
package net.sf.jasperreports.engine;

import java.awt.Color;
import java.util.Collection;
import java.util.SortedSet;

import net.sf.jasperreports.charts.JRCategoryAxisFormat;
import net.sf.jasperreports.charts.type.PlotOrientationEnum;

import org.jfree.chart.plot.PlotOrientation;


/**
 * Chart plots define chart appearance and display details such as colors, legend or labels. Each plot may have different
 * characteristics, depending on the chart type it belongs to. This is the superinterface for all plots and contains common
 * properties.
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public interface JRChartPlot extends JRCloneable
{


	/**
	 * Gets the chart associated with this chart plot, if available. 
	 * Implementations can return null on this method, especially if the chart plot 
	 * is reused by multiple charts, which is not recommended since it prevents style properties 
	 * inheritence from parent chart.
	 */
	public JRChart getChart();
	
	/**
	 * Gets the chart background color.
	 */
	public Color getBackcolor();
	
	/**
	 *
	 */
	public Color getOwnBackcolor();
	
	/**
	 * Sets the chart background color.
	 */
	public void setBackcolor(Color backcolor);


	/**
	 * @deprecated Replaced by {@link #getOrientationValue()}.
	 */
	public PlotOrientation getOrientation();
	
	/**
	 * Gets the plot orientation (horizontal or vertical).
	 */
	public PlotOrientationEnum getOrientationValue();
	
	/**
	 * @deprecated Replaced by {@link #setOrientation(PlotOrientationEnum)}.
	 */
	public void setOrientation(PlotOrientation orientation);

	/**
	 * Sets the plot orientation (horizontal or vertical).
	 */
	public void setOrientation(PlotOrientationEnum orientation);

	/**
	 * Gets the transparency factor for this plot background. The range is from 0 to 1, where 0 means transparent and 1
	 * opaque. The default is 1.
	 * @return a float value between 0 and 1.
	 */
	public Float getBackgroundAlphaFloat();
	
	/**
	 * Sets the transparency factor for this plot background. The range is from 0 to 1, where 0 means transparent and 1
	 * opaque. The default is 1.
	 */
	public void setBackgroundAlpha(Float backgroundAlpha);

	/**
	 * Gets the transparency factor for this plot foreground. The range is from 0 to 1, where 0 means transparent and 1
	 * opaque. The default is 1.
	 * @return a float value between 0 and 1.
	 */
	public Float getForegroundAlphaFloat();
	
	/**
	 * Sets the transparency factor for this plot foreground. The range is from 0 to 1, where 0 means transparent and 1
	 * opaque. The default is 1.
	 */
	public void setForegroundAlpha(Float foregroundAlpha);

	/**
	 * Gets the angle in degrees to rotate the data axis labels.  The range is -360 to 360.  A positive value angles
	 * the label so it reads downwards wile a negative value angles the label so it reads upwards.  Only charts that
	 * use a category based axis (such as line or bar charts) support label rotation.
	 * @deprecated Replaced by {@link JRCategoryAxisFormat#getCategoryAxisTickLabelRotation()}.
	 */
	public Double getLabelRotationDouble();
	
	/**
	 * Sets the angle in degrees to rotate the data axis labels.  The range is -360 to 360.  A positive value angles
	 * the label so it reads downwards wile a negative value angles the label so it reads upwards.  Only charts that
	 * use a category based axis (such as line or bar charts) support label rotation.
	 * @deprecated Replaced by {@link JRCategoryAxisFormat#setCategoryAxisTickLabelRotation(Double)}.
	 */
	public void setLabelRotation(Double labelRotation);
	
	/**
	 * Returns a list of all the defined series colors.  Every entry in the list is of type JRChartPlot.JRSeriesColor.
	 * If there are no defined series colors this method will return an empty list, not null. 
	 */
	public SortedSet<JRSeriesColor> getSeriesColors();
	
	/**
	 * Removes all defined series colors.
	 */
	public void clearSeriesColors();
	
	/**
	 * Adds the specified series color to the plot.
	 */
	public void addSeriesColor(JRSeriesColor seriesColor);
	
	/**
	 * Set the list of series colors.
	 * 
	 * @param colors the list of series colors ({@link JRSeriesColor} instances}
	 */
	public void setSeriesColors(Collection<JRSeriesColor> colors);
	
	/**
	 *
	 */
	public void collectExpressions(JRExpressionCollector collector);

	public interface JRSeriesColor extends JRCloneable
	{
		/**
		 * Returns the series order that this color applies to.  The series order is relative to
		 * the series order of all other {@link JRSeriesColor}s defined for this plot.  The
		 * relative ordering defines the order of the colors in the series.
		 */
		public int getSeriesOrder();
		
		/**
		 * Returns the color to use for this series.
		 */
		public Color getColor();
	}
	
	/**
	 * 
	 */
	public Object clone(JRChart parentChart);
	
}
