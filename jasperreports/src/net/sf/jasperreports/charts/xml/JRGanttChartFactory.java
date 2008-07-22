package net.sf.jasperreports.charts.xml;

import net.sf.jasperreports.engine.JRChart;
import net.sf.jasperreports.engine.design.JRDesignChart;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRBaseFactory;

import org.xml.sax.Attributes;

/**
 * @author Peter Risko (peter@risko.hu)
 */
public class JRGanttChartFactory extends JRBaseFactory {

    public Object createObject( Attributes attrs ){
        JasperDesign jasperDesign = (JasperDesign)digester.peek(digester.getCount() - 2);

        JRDesignChart chart = new JRDesignChart(jasperDesign, JRChart.CHART_TYPE_GANTT);

        return chart;
    }

    
}
