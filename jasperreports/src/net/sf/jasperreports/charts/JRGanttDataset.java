package net.sf.jasperreports.charts;

import net.sf.jasperreports.engine.JRChartDataset;

/**
 * @author Peter Risko (peter@risko.hu)
 * @version $Id: JRDesignTimePeriodSeries.java 1923 2007-10-25 09:44:32Z lucianc $
 */
public interface JRGanttDataset extends JRChartDataset {
 
    /**
     * 
     */
    public JRGanttSeries[] getSeries();


}
