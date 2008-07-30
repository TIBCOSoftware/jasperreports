package net.sf.jasperreports.charts.xml;

import net.sf.jasperreports.charts.design.JRDesignGanttDataset;
import net.sf.jasperreports.engine.design.JRDesignChart;
import net.sf.jasperreports.engine.xml.JRBaseFactory;

import org.xml.sax.Attributes;

/**
 * @author Peter Risko (peter@risko.hu)
 * @version $Id: JRDesignTimePeriodSeries.java 1923 2007-10-25 09:44:32Z lucianc $
 */
public class JRGanttDatasetFactory extends JRBaseFactory {

    /**
     *
     */
    public Object createObject(Attributes atts)
    {
        JRDesignChart chart = (JRDesignChart) digester.peek();
        
        JRDesignGanttDataset dataset = null; 
        
        if( chart.getDataset() == null ){
            dataset = new JRDesignGanttDataset( chart.getDataset() );
        }
        else {
            dataset = (JRDesignGanttDataset)chart.getDataset();
        }
        
        chart.setDataset( dataset );
        return dataset;
    }

    
}
