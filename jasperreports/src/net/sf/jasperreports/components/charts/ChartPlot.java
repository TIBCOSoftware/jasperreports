/**
 * 
 */
package net.sf.jasperreports.components.charts;

import java.awt.Color;
import java.io.Serializable;
import java.util.Collection;
import java.util.SortedSet;

import net.sf.jasperreports.engine.JRChartPlot;
import net.sf.jasperreports.engine.JRChartPlot.JRSeriesColor;

import org.jfree.chart.plot.PlotOrientation;

/**
 *
 * @author sanda zaharia (shertage@users.sourceforge.net)
 * @version $Id$
 */
public interface ChartPlot extends Serializable
{
	/**
	 * Gets the chart background color.
	 */
	public Color getBackcolor();
	
	/**
	 * Gets the plot orientation (horizontal or vertical).
	 */
	public PlotOrientation getOrientation();
	
	/**
	 * Gets the transparency factor for this plot background. The range is from 0 to 1, where 0 means transparent and 1
	 * opaque. The default is 1.
	 * @return a float value between 0 and 1.
	 */
	public Float getBackgroundAlpha();
	
	/**
	 * Gets the transparency factor for this plot foreground. The range is from 0 to 1, where 0 means transparent and 1
	 * opaque. The default is 1.
	 * @return a float value between 0 and 1.
	 */
	public Float getForegroundAlpha();
	
//	/**
//	 * Returns a list of all the defined series colors.  Every entry in the list is of type JRChartPlot.JRSeriesColor.
//	 * If there are no defined series colors this method will return an empty list, not null. 
//	 */
//	public SortedSet getSeriesColors();
//	
//	/**
//	 * Removes all defined series colors.
//	 */
//	public void clearSeriesColors();
//	
//	/**
//	 * Adds the specified series color to the plot.
//	 */
//	public void addSeriesColor(JRChartPlot.JRSeriesColor seriesColor);
//	
//	/**
//	 * Set the list of series colors.
//	 * 
//	 * @param colors the list of series colors ({@link JRSeriesColor} instances}
//	 */
//	public void setSeriesColors(Collection colors);

}
