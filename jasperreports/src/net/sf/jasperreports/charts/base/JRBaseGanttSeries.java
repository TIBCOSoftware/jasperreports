package net.sf.jasperreports.charts.base;

import java.io.Serializable;

import net.sf.jasperreports.charts.JRGanttSeries;
import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRHyperlink;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.base.JRBaseObjectFactory;

/**
 * @author Peter Risko (peter@risko.hu)
 * @version $Id: JRDesignTimePeriodSeries.java 1923 2007-10-25 09:44:32Z lucianc $
 */
public class JRBaseGanttSeries implements JRGanttSeries, Serializable 
{
    
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
        

	/**
	 * 
	 */
	public Object clone() 
	{
		JRBaseGanttSeries clone = null;
		
		try
		{
			clone = (JRBaseGanttSeries)super.clone();
		}
		catch (CloneNotSupportedException e)
		{
			throw new JRRuntimeException(e);
		}
		
		if (seriesExpression != null)
		{
			clone.seriesExpression = (JRExpression)seriesExpression.clone();
		}
		if (taskExpression != null)
		{
			clone.taskExpression = (JRExpression)taskExpression.clone();
		}
		if (subtaskExpression != null)
		{
			clone.subtaskExpression = (JRExpression)subtaskExpression.clone();
		}
		if (startDateExpression != null)
		{
			clone.startDateExpression = (JRExpression)startDateExpression.clone();
		}
		if (endDateExpression != null)
		{
			clone.endDateExpression = (JRExpression)endDateExpression.clone();
		}
		if (percentExpression != null)
		{
			clone.percentExpression = (JRExpression)percentExpression.clone();
		}
		if (labelExpression != null)
		{
			clone.labelExpression = (JRExpression)labelExpression.clone();
		}
		if (itemHyperlink != null)
		{
			clone.itemHyperlink = (JRHyperlink)itemHyperlink.clone();
		}
		
		return clone;
	}

}
