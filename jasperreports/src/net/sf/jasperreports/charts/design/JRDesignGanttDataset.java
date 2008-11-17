package net.sf.jasperreports.charts.design;

import java.util.ArrayList;
import java.util.List;

import net.sf.jasperreports.charts.JRGanttDataset;
import net.sf.jasperreports.charts.JRGanttSeries;
import net.sf.jasperreports.charts.JRXyDataset;
import net.sf.jasperreports.charts.JRXySeries;
import net.sf.jasperreports.engine.JRChartDataset;
import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRExpressionCollector;
import net.sf.jasperreports.engine.design.JRDesignChartDataset;
import net.sf.jasperreports.engine.design.JRVerifier;

/**
 * @author Peter Risko (peter@risko.hu)
 * @version $Id: JRDesignTimePeriodSeries.java 1923 2007-10-25 09:44:32Z lucianc $
 */
public class JRDesignGanttDataset  extends JRDesignChartDataset implements JRGanttDataset {

	/**
	 *
	 */
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

	private List ganttSeriesList = new ArrayList();


	/**
	 *
	 */
	public JRDesignGanttDataset(JRChartDataset dataset)
	{
		super(dataset);
	}


	/**
	 *
	 */
	public JRGanttSeries[] getSeries()
	{
		JRGanttSeries[] ganttSeriesArray = new JRGanttSeries[ganttSeriesList.size()];

		ganttSeriesList.toArray(ganttSeriesArray);

		return ganttSeriesArray;
	}


	/**
	 *
	 */
	public List getSeriesList()
	{
		return ganttSeriesList;
	}


	/**
	 *
	 */
	public void addGanttSeries(JRGanttSeries ganttSeries)
	{
		ganttSeriesList.add(ganttSeries);
	}


	/**
	 *
	 */
	public JRGanttSeries removeGanttSeries(JRGanttSeries ganttSeries)
	{
		if (ganttSeries != null)
		{
			ganttSeriesList.remove(ganttSeries);
		}

		return ganttSeries;
	}


	/**
	 *
	 */
	public byte getDatasetType() {
		return JRChartDataset.GANTT_DATASET;
	}


	/**
	 *
	 */
	public void collectExpressions(JRExpressionCollector collector)
	{
		collector.collect(this);
	}


	public void validate(JRVerifier verifier)
	{
		verifier.verify(this);
	}


}
