package net.sf.jasperreports.charts.base;

import net.sf.jasperreports.charts.JRGanttDataset;
import net.sf.jasperreports.charts.JRGanttSeries;
import net.sf.jasperreports.charts.JRXyDataset;
import net.sf.jasperreports.charts.JRXySeries;
import net.sf.jasperreports.engine.JRChartDataset;
import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRExpressionCollector;
import net.sf.jasperreports.engine.base.JRBaseChartDataset;
import net.sf.jasperreports.engine.base.JRBaseObjectFactory;
import net.sf.jasperreports.engine.design.JRVerifier;

/**
 * @author Peter Risko (peter@risko.hu)
 * @version $Id: JRDesignTimePeriodSeries.java 1923 2007-10-25 09:44:32Z lucianc $
 */
public class JRBaseGanttDataset extends JRBaseChartDataset implements JRGanttDataset {

    /**
     *
     */
    private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

    protected JRGanttSeries[] ganttSeries = null;

    /**
     *
     */
    public JRBaseGanttDataset(JRChartDataset dataset)
    {
        super(dataset);
    }
    
    /**
     *
     */
    public JRBaseGanttDataset(JRGanttDataset dataset, JRBaseObjectFactory factory)
    {
        super(dataset, factory);

        /*   */
        JRGanttSeries[] srcGanttSeries = dataset.getSeries();
        if (srcGanttSeries != null && srcGanttSeries.length > 0)
        {
            ganttSeries = new JRGanttSeries[srcGanttSeries.length];
            for(int i = 0; i < ganttSeries.length; i++)
            {
                ganttSeries[i] = factory.getGanttSeries(srcGanttSeries[i]);
            }
        }

    }

    
    /**
     *
     */
    public JRGanttSeries[] getSeries()
    {
        return ganttSeries;
    }


    /* (non-Javadoc)
     * @see net.sf.jasperreports.engine.JRChartDataset#getDatasetType()
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
