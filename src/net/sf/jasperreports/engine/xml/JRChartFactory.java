package net.sf.jasperreports.engine.xml;

import java.awt.Color;
import java.util.Collection;

import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.design.JRDesignChart;
import net.sf.jasperreports.engine.design.JRDesignExpression;
import net.sf.jasperreports.engine.design.JRDesignGroup;

import org.xml.sax.Attributes;

/**
 * @author Ionut Nedelcu
 */
public class JRChartFactory extends JRBaseFactory
{

    private static final String ATTRIBUTE_isShowLegend = "isShowLegend";
    private static final String ATTRIBUTE_evaluationTime = "evaluationTime";
    private static final String ATTRIBUTE_evaluationGroup = "evaluationGroup";


    /**
     *
     */
    public Object createObject(Attributes atts)
    {
        JRXmlLoader xmlLoader = (JRXmlLoader)digester.peek(digester.getCount() - 1);
        Collection groupEvaluatedCharts = xmlLoader.getGroupEvaluatedCharts();

        JRDesignChart chart = (JRDesignChart) digester.peek();

        String isShowLegend = atts.getValue(ATTRIBUTE_isShowLegend);
        if (isShowLegend != null && isShowLegend.length() > 0)
            chart.setShowLegend(Boolean.valueOf(isShowLegend).booleanValue());

        Byte evaluationTime = (Byte)JRXmlConstants.getEvaluationTimeMap().get(atts.getValue(ATTRIBUTE_evaluationTime));
        if (evaluationTime != null)
        {
            chart.setEvaluationTime(evaluationTime.byteValue());
        }
        if (chart.getEvaluationTime() == JRExpression.EVALUATION_TIME_GROUP)
        {
            groupEvaluatedCharts.add(chart);

            String groupName = atts.getValue(ATTRIBUTE_evaluationGroup);
            if (groupName != null)
            {
                JRDesignGroup group = new JRDesignGroup();
                group.setName(groupName);
                chart.setEvaluationGroup(group);
            }
        }

        return chart;
    }


    /**
     *
     */
    public static class JRChartTitleFactory extends JRBaseFactory
    {
        private static final String ATTRIBUTE_position = "position";
        private static final String ATTRIBUTE_color = "color";


        public Object createObject(Attributes atts)
        {
            JRDesignChart chart = (JRDesignChart) digester.peek();

            String position = atts.getValue(ATTRIBUTE_position);
            if (position != null && position.length() > 0)
                chart.setTitlePosition(((Byte)JRXmlConstants.getChartTitlePositionMap().get(position)).byteValue());


            String color = atts.getValue(ATTRIBUTE_color);
            if (color != null && color.length() > 0)
            {
                char firstChar = color.charAt(0);
                if (firstChar == '#')
                {
                    chart.setTitleColor(new Color(Integer.parseInt(color.substring(1), 16)));
                }
                else if ('0' <= firstChar && firstChar <= '9')
                {
                    chart.setTitleColor(new Color(Integer.parseInt(color)));
                }
                else
                {
                    if (JRXmlConstants.getColorMap().containsKey(color))
                    {
                        chart.setTitleColor((Color)JRXmlConstants.getColorMap().get(color));
                    }
                    else
                    {
                        chart.setTitleColor(Color.black);
                    }
                }
            }

            return chart;
        }
    }



    /**
     *
     */
    public static class JRChartSubtitleFactory extends JRBaseFactory
    {
        private static final String ATTRIBUTE_color = "color";


        public Object createObject(Attributes atts)
        {
            JRDesignChart chart = (JRDesignChart) digester.peek();

            // TODO: implement subtitle color
            /*
            String color = atts.getValue(ATTRIBUTE_color);
            if (color != null && color.length() > 0)
            {
                char firstChar = color.charAt(0);
                if (firstChar == '#')
                {
                    chart.setsu(new Color(Integer.parseInt(color.substring(1), 16)));
                }
                else if ('0' <= firstChar && firstChar <= '9')
                {
                    chart.setTitleColor(new Color(Integer.parseInt(color)));
                }
                else
                {
                    if (JRXmlConstants.getColorMap().containsKey(color))
                    {
                        chart.setTitleColor((Color)JRXmlConstants.getColorMap().get(color));
                    }
                    else
                    {
                        chart.setTitleColor(Color.black);
                    }
                }
            }
            */
            return chart;
        }
    }


    /**
     *
     */
    public static class JRTitleExpressionFactory extends JRBaseFactory {
        public Object createObject(Attributes atts)
        {
            JRDesignExpression expression = new JRDesignExpression();
            expression.setValueClass(String.class);
            expression.setValueClassName(String.class.getName());
            return expression;
        }
    }


}
