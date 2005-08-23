import net.sf.jasperreports.engine.JRChartCustomizer;
import net.sf.jasperreports.engine.JRChartDataset;
import net.sf.jasperreports.engine.JRChartPlot;
import net.sf.jasperreports.engine.JRChart;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.renderer.category.BarRenderer;

import java.awt.*;

public class BarChartCustomizer implements JRChartCustomizer
{

	public void customize(JFreeChart chart, JRChart jasperChart)
	{
		BarRenderer renderer = (BarRenderer) chart.getCategoryPlot().getRenderer();
		renderer.setSeriesPaint(0, Color.green);
		renderer.setSeriesPaint(1, Color.orange);
	}
}