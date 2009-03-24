package net.sf.jasperreports.charts;

import net.sf.jasperreports.engine.JRChartDataset;

/**
 * @author Peter Risko (peter@risko.hu)
 * @version $Id$
 */
public interface JRGanttDataset extends JRChartDataset {

	/**
	 *
	 */
	public JRGanttSeries[] getSeries();


}
