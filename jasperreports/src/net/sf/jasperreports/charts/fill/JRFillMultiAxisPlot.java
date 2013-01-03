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
package net.sf.jasperreports.charts.fill;

import net.sf.jasperreports.charts.JRChartAxis;
import net.sf.jasperreports.charts.JRMultiAxisPlot;
import net.sf.jasperreports.engine.fill.JRFillChartDataset;
import net.sf.jasperreports.engine.fill.JRFillChartPlot;
import net.sf.jasperreports.engine.fill.JRFillObjectFactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author Barry Klawans (bklawans@users.sourceforge.net)
 * @version $Id$
 */
public class JRFillMultiAxisPlot extends JRFillChartPlot implements JRMultiAxisPlot
{

	private List<JRChartAxis> axes;

	public JRFillMultiAxisPlot(JRMultiAxisPlot multiAxisPlot, JRFillObjectFactory factory)
	{
		super(multiAxisPlot, factory);

		List<JRChartAxis> parentAxes = multiAxisPlot.getAxes();
		this.axes = new ArrayList<JRChartAxis>(parentAxes.size());
		Iterator<JRChartAxis> iter = parentAxes.iterator();
		while (iter.hasNext())
		{
			JRChartAxis axis = iter.next();
			this.axes.add(factory.getChartAxis(axis));
		}
	}

	public List<JRChartAxis> getAxes()
	{
		return axes;
	}

	public JRFillChartDataset getMainDataset()
	{
		return (JRFillChartDataset) ((JRFillChartAxis) axes.get(0)).getFillChart().getDataset();
	}
}
