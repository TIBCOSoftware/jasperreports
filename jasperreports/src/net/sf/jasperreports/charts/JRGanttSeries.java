package net.sf.jasperreports.charts;

import net.sf.jasperreports.engine.JRChartDataset;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRHyperlink;

/**
 * @author Peter Risko (peter@risko.hu)
 */
public interface JRGanttSeries {

    /**
     * 
     */
    public JRExpression getSeriesExpression();

    /**
     * 
     */
    public JRExpression getTaskExpression();
    
    /**
     * 
     */
    public JRExpression getSubtaskExpression();
    
    /**
     * 
     */
    public JRExpression getStartDateExpression();

    /**
     * 
     */
    public JRExpression getEndDateExpression();

    /**
     * 
     */
    public JRExpression getPercentExpression();

    /**
     * 
     */
    public JRExpression getLabelExpression();

    
    /**
     * Returns the hyperlink specification for chart items.
     * <p>
     * The hyperlink will be evaluated for every chart item and a image map will be created for the chart.
     * </p>
     * 
     * @return hyperlink specification for chart items
     */
    public JRHyperlink getItemHyperlink();

}
