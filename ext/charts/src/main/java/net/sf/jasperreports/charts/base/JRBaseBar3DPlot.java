/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2023 Cloud Software Group, Inc. All rights reserved.
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
package net.sf.jasperreports.charts.base;

import net.sf.jasperreports.charts.ChartCopyObjectFactory;
import net.sf.jasperreports.charts.JRBar3DPlot;
import net.sf.jasperreports.charts.JRChart;
import net.sf.jasperreports.charts.JRChartPlot;
import net.sf.jasperreports.engine.JRConstants;

/**
 * @author Flavius Sana (flavius_sana@users.sourceforge.net)
 * @deprecated To be removed
 */
public class JRBaseBar3DPlot extends JRBaseBarPlot
{
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;
	
	public JRBaseBar3DPlot(JRChartPlot plot, JRChart chart)
	{
		this(plot, chart, ChartCopyBaseObjectFactory.instance());
	}
	
	protected JRBaseBar3DPlot(JRChartPlot plot, JRChart chart, ChartCopyObjectFactory copyObjectFactory)
	{
		super(plot, chart);
	}


	/**
	 * 
	 */
	public JRBaseBar3DPlot(JRBar3DPlot barPlot, ChartsBaseObjectFactory factory )
	{
		super( barPlot, factory );
	}
	
}
