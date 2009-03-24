/*
 * ============================================================================
 * GNU Lesser General Public License
 * ============================================================================
 *
 * JasperReports - Free Java report-generating library.
 * Copyright (C) 2001-2009 JasperSoft Corporation http://www.jaspersoft.com
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307, USA.
 * 
 * JasperSoft Corporation
 * 539 Bryant Street, Suite 100
 * San Francisco, CA 94107
 * http://www.jaspersoft.com
 */
package net.sf.jasperreports.charts.base;

import net.sf.jasperreports.charts.JRGanttDataset;
import net.sf.jasperreports.charts.JRGanttSeries;
import net.sf.jasperreports.engine.JRChartDataset;
import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRExpressionCollector;
import net.sf.jasperreports.engine.base.JRBaseChartDataset;
import net.sf.jasperreports.engine.base.JRBaseObjectFactory;
import net.sf.jasperreports.engine.design.JRVerifier;

/**
 * @author Peter Risko (peter@risko.hu)
 * @version $Id$
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
