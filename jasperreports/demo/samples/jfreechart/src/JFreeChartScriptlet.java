/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2011 Jaspersoft Corporation. All rights reserved.
 * http://www.jaspersoft.com
 *
 * Unless you have purchased a commercial license agreement from Jaspersoft,
 * the following license terms apply:
 *
 * This program is part of JasperReports.
 *
 * JasperReports is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * JasperReports is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with JasperReports. If not, see <http://www.gnu.org/licenses/>.
 */

/*
 * Contributors:
 * David Gilbert - david.gilbert@object-refinery.com
 */

import net.sf.jasperreports.engine.JRDefaultScriptlet;
import net.sf.jasperreports.engine.JRScriptletException;
import net.sf.jasperreports.renderers.JCommonDrawableRenderer;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.util.Rotation;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JFreeChartScriptlet extends JRDefaultScriptlet
{


	/**
	 *
	 */
	public void afterReportInit() throws JRScriptletException
	{
		DefaultPieDataset dataset = new DefaultPieDataset();
		dataset.setValue("Java", new Double(43.2));
		dataset.setValue("Visual Basic", new Double(10.0));
		dataset.setValue("C/C++", new Double(17.5));
		dataset.setValue("PHP", new Double(32.5));
		dataset.setValue("Perl", new Double(1.0));

		JFreeChart chart = 
			ChartFactory.createPieChart3D(
				"Pie Chart 3D Demo 1",
				dataset,
				true,
				true,
				false
				);

		PiePlot3D plot = (PiePlot3D) chart.getPlot();
		plot.setStartAngle(290);
		plot.setDirection(Rotation.CLOCKWISE);
		plot.setForegroundAlpha(0.5f);
		plot.setNoDataMessage("No data to display");

		/*   */
		this.setVariableValue("Chart", new JCommonDrawableRenderer(chart));
	}


}
