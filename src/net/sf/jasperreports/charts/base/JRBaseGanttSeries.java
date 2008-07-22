package net.sf.jasperreports.charts.base;

import java.io.Serializable;

import net.sf.jasperreports.charts.JRGanttSeries;
import net.sf.jasperreports.charts.JRXySeries;
import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRHyperlink;
import net.sf.jasperreports.engine.base.JRBaseObjectFactory;

/**
 * @author Peter Risko (peter@risko.hu)
 */
public class JRBaseGanttSeries implements JRGanttSeries, Serializable {
    
    /**
     *
     */
    private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

    protected JRExpression seriesExpression = null;
    protected JRExpression taskExpression = null;
    protected JRExpression subtaskExpression = null;
    protected JRExpression startDateExpression = null;
    protected JRExpression endDateExpression = null;
    protected JRExpression percentExpression = null;
    protected JRExpression labelExpression = null;
    protected JRHyperlink itemHyperlink;

    /**
     *
     */
    protected JRBaseGanttSeries()
    {
    }
    
    
    /**
     *
     */
    public JRBaseGanttSeries(JRGanttSeries ganttSeries, JRBaseObjectFactory factory)
    {
        factory.put(ganttSeries, this);

        seriesExpression = factory.getExpression(ganttSeries.getSeriesExpression());
        taskExpression = factory.getExpression(ganttSeries.getTaskExpression());
        subtaskExpression = factory.getExpression(ganttSeries.getSubtaskExpression());
        startDateExpression = factory.getExpression(ganttSeries.getStartDateExpression());
        endDateExpression = factory.getExpression(ganttSeries.getEndDateExpression());
        percentExpression = factory.getExpression(ganttSeries.getPercentExpression());
        labelExpression = factory.getExpression(ganttSeries.getLabelExpression());
        itemHyperlink = factory.getHyperlink(ganttSeries.getItemHyperlink());
    }

    
    /**
     *
     */
    public JRExpression getSeriesExpression()
    {
        return seriesExpression;
    }
        
    /**
     *
     */
    public JRExpression getTaskExpression()
    {
        return taskExpression;
    }
        
    /**
     *
     */
    public JRExpression getSubtaskExpression()
    {
        return subtaskExpression;
    }
       
    /**
     *
     */
    public JRExpression getStartDateExpression()
    {
        return startDateExpression;
    }
      
    /**
     *
     */
    public JRExpression getEndDateExpression()
    {
        return endDateExpression;
    }
        
    /**
     *
     */
    public JRExpression getPercentExpression()
    {
        return percentExpression;
    }
       
    /**
     *
     */
    public JRExpression getLabelExpression()
    {
        return labelExpression;
    }

    
    public JRHyperlink getItemHyperlink()
    {
        return itemHyperlink;
    }
        


}
