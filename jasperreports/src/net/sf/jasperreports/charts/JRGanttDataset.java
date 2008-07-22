package net.sf.jasperreports.charts;

import net.sf.jasperreports.engine.JRChartDataset;

/**
 * @author Peter Risko (peter@risko.hu)
 */
public interface JRGanttDataset extends JRChartDataset {
 
    /**
     * 
     */
    public JRGanttSeries[] getSeries();


}
