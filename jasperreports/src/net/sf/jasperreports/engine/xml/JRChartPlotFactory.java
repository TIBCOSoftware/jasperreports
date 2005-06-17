package net.sf.jasperreports.engine.xml;

import org.xml.sax.*;
import org.jfree.chart.plot.PlotOrientation;
import net.sf.jasperreports.engine.JRChartPlot;

import java.awt.*;

/**
 * @author Ionut Nedelcu
 */
public class JRChartPlotFactory extends JRBaseFactory
{

	private static final String ATTRIBUTE_backcolor = "backcolor";
	private static final String ATTRIBUTE_orientation = "orientation";
	private static final String ATTRIBUTE_backgroundAlpha = "backgroundAlpha";
	private static final String ATTRIBUTE_foregroundAlpha = "foregroundAlpha";


	/**
	 *
	 */
    public Object createObject(Attributes atts)
    {
        JRChartPlot plot = (JRChartPlot) digester.peek();

		String color = atts.getValue(ATTRIBUTE_backcolor);
		if (color != null && color.length() > 0)
		{
			char firstChar = color.charAt(0);
			if (firstChar == '#')
			{
				plot.setBackcolor(new Color(Integer.parseInt(color.substring(1), 16)));
			}
			else if ('0' <= firstChar && firstChar <= '9')
			{
				plot.setBackcolor(new Color(Integer.parseInt(color)));
			}
			else
			{
				if (JRXmlConstants.getColorMap().containsKey(color))
				{
					plot.setBackcolor((Color)JRXmlConstants.getColorMap().get(color));
				}
				else
				{
					plot.setBackcolor(Color.black);
				}
			}
		}

		String orientation = atts.getValue(ATTRIBUTE_orientation);
		if (orientation != null && orientation.length() > 0)
			plot.setOrientation((PlotOrientation)JRXmlConstants.getPlotOrientationMap().get(orientation));

		String foregroundAlpha = atts.getValue(ATTRIBUTE_foregroundAlpha);
		if (foregroundAlpha != null && foregroundAlpha.length() > 0)
			plot.setForegroundAlpha(Float.valueOf(foregroundAlpha).floatValue());

		String backgroundAlpha = atts.getValue(ATTRIBUTE_backgroundAlpha);
		if (backgroundAlpha != null && backgroundAlpha.length() > 0)
			plot.setBackgroundAlpha(Float.valueOf(backgroundAlpha).floatValue());

		return plot;
    }
}
