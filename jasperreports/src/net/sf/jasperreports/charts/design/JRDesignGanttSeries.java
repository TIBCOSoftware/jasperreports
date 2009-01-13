package net.sf.jasperreports.charts.design;

import net.sf.jasperreports.charts.base.JRBaseGanttSeries;
import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRHyperlink;
import net.sf.jasperreports.engine.design.events.JRChangeEventsSupport;
import net.sf.jasperreports.engine.design.events.JRPropertyChangeSupport;

/**
 * @author Peter Risko (peter@risko.hu)
 * @version $Id: JRDesignTimePeriodSeries.java 1923 2007-10-25 09:44:32Z lucianc $
 */
public class JRDesignGanttSeries extends JRBaseGanttSeries implements JRChangeEventsSupport
{

	/**
	 *
	 */
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

	public static final String PROPERTY_SERIES_EXPRESSION = "seriesExpression";

	public static final String PROPERTY_TASK_EXPRESSION = "taskExpression";

	public static final String PROPERTY_SUBTASK_EXPRESSION = "subtaskExpression";

	public static final String PROPERTY_START_DATE_EXPRESSION = "startDateExpression";

	public static final String PROPERTY_END_DATE_EXPRESSION = "endDateExpression";

	public static final String PROPERTY_PERCENT_EXPRESSION = "percentExpression";

	public static final String PROPERTY_LABEL_EXPRESSION = "labelExpression";//FIXMEGANTT check what label is used for

	public static final String PROPERTY_ITEM_HYPERLINK = "itemHyperlink";


	/**
	 *
	 */
	public void setSeriesExpression(JRExpression seriesExpression)
	{
		Object old = this.seriesExpression;
		this.seriesExpression = seriesExpression;
		getEventSupport().firePropertyChange(PROPERTY_SERIES_EXPRESSION, old, this.seriesExpression);
	}

	/**
	 *
	 */
	public void setTaskExpression(JRExpression taskExpression)
	{
		Object old = this.taskExpression;
		this.taskExpression = taskExpression;
		getEventSupport().firePropertyChange(PROPERTY_TASK_EXPRESSION, old, this.taskExpression);
	}

	/**
	 *
	 */
	public void setSubtaskExpression(JRExpression subtaskExpression)
	{
		Object old = this.subtaskExpression;
		this.subtaskExpression = subtaskExpression;
		getEventSupport().firePropertyChange(PROPERTY_SUBTASK_EXPRESSION, old, this.subtaskExpression);
	}

	/**
	 *
	 */
	public void setStartDateExpression(JRExpression startDateExpression)
	{
		Object old = this.startDateExpression;
		this.startDateExpression = startDateExpression;
		getEventSupport().firePropertyChange(PROPERTY_START_DATE_EXPRESSION, old, this.startDateExpression);
	}

	/**
	 *
	 */
	public void setEndDateExpression(JRExpression endDateExpression)
	{
		Object old = this.endDateExpression;
		this.endDateExpression = endDateExpression;
		getEventSupport().firePropertyChange(PROPERTY_END_DATE_EXPRESSION, old, this.endDateExpression);
	}

	/**
	 *
	 */
	public void setPercentExpression(JRExpression percentExpression)
	{
		Object old = this.percentExpression;
		this.percentExpression = percentExpression;
		getEventSupport().firePropertyChange(PROPERTY_PERCENT_EXPRESSION, old, this.percentExpression);
	}

	/**
	 *
	 */
	public void setLabelExpression(JRExpression labelExpression)
	{
		Object old = this.labelExpression;
		this.labelExpression = labelExpression;
		getEventSupport().firePropertyChange(PROPERTY_LABEL_EXPRESSION, old, this.labelExpression);
	}


	/**
	 * Sets the hyperlink specification for chart items.
	 *
	 * @param itemHyperlink the hyperlink specification
	 * @see #getItemHyperlink()
	 */
	public void setItemHyperlink(JRHyperlink itemHyperlink)
	{
		Object old = this.itemHyperlink;
		this.itemHyperlink = itemHyperlink;
		getEventSupport().firePropertyChange(PROPERTY_ITEM_HYPERLINK, old, this.itemHyperlink);
	}


	private transient JRPropertyChangeSupport eventSupport;

	public JRPropertyChangeSupport getEventSupport()
	{
		synchronized (this)
		{
			if (eventSupport == null)
			{
				eventSupport = new JRPropertyChangeSupport(this);
			}
		}

		return eventSupport;
	}
}
