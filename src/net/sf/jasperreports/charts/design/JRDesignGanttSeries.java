package net.sf.jasperreports.charts.design;

import net.sf.jasperreports.charts.base.JRBaseGanttSeries;
import net.sf.jasperreports.charts.base.JRBaseXySeries;
import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRHyperlink;

/**
 * @author Peter Risko (peter@risko.hu)
 */
public class JRDesignGanttSeries extends JRBaseGanttSeries {

    /**
     *
     */
    private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

    
    /**
     *
     */
    public void setSeriesExpression(JRExpression seriesExpression)
    {
        this.seriesExpression = seriesExpression;
    }

    /**
     *
     */
    public void setTaskExpression(JRExpression taskExpression)
    {
        this.taskExpression = taskExpression;
    }

    /**
     *
     */
    public void setSubtaskExpression(JRExpression subtaskExpression)
    {
        this.subtaskExpression = subtaskExpression;
    }

    /**
     *
     */
    public void setStartDateExpression(JRExpression startDateExpression)
    {
       this.startDateExpression = startDateExpression;
    }

    /**
     *
     */
    public void setEndDateExpression(JRExpression endDateExpression)
    {
        this.endDateExpression = endDateExpression;
    }

    /**
     *
     */
    public void setPercentExpression(JRExpression percentExpression)
    {
        this.percentExpression = percentExpression;
    }

    /**
     *
     */
    public void setLabelExpression(JRExpression labelExpression)
    {
        this.labelExpression = labelExpression;
    }


    /**
     * Sets the hyperlink specification for chart items.
     * 
     * @param itemHyperlink the hyperlink specification
     * @see #getItemHyperlink()
     */
    public void setItemHyperlink(JRHyperlink itemHyperlink)
    {
        this.itemHyperlink = itemHyperlink;
    }


    
}
