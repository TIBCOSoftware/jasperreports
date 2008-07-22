package net.sf.jasperreports.charts.xml;

import net.sf.jasperreports.charts.design.JRDesignGanttSeries;
import net.sf.jasperreports.engine.xml.JRBaseFactory;

import org.xml.sax.Attributes;

/**
 * @author Peter Risko (peter@risko.hu)
 */
public class JRGanttSeriesFactory extends JRBaseFactory {

    /**
     *
     */
    public Object createObject(Attributes atts)
    {
        return new JRDesignGanttSeries();
    }

    
}
