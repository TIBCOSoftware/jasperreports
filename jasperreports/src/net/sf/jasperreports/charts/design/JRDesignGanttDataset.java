/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2014 TIBCO Software Inc. All rights reserved.
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
package net.sf.jasperreports.charts.design;

import java.util.ArrayList;
import java.util.List;

import net.sf.jasperreports.charts.JRGanttDataset;
import net.sf.jasperreports.charts.JRGanttSeries;
import net.sf.jasperreports.engine.JRChartDataset;
import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRExpressionCollector;
import net.sf.jasperreports.engine.design.JRDesignChartDataset;
import net.sf.jasperreports.engine.design.JRVerifier;

/**
 * @author Peter Risko (peter@risko.hu)
 */
public class JRDesignGanttDataset  extends JRDesignChartDataset implements JRGanttDataset {

	/**
	 *
	 */
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;
	
	public static final String PROPERTY_GANTT_SERIES = "ganttSeries";

	private List<JRGanttSeries> ganttSeriesList = new ArrayList<JRGanttSeries>();


	/**
	 *
	 */
	public JRDesignGanttDataset(JRChartDataset dataset)
	{
		super(dataset);
	}


	/**
	 *
	 */
	public JRGanttSeries[] getSeries()
	{
		JRGanttSeries[] ganttSeriesArray = new JRGanttSeries[ganttSeriesList.size()];

		ganttSeriesList.toArray(ganttSeriesArray);

		return ganttSeriesArray;
	}


	/**
	 *
	 */
	public List<JRGanttSeries> getSeriesList()
	{
		return ganttSeriesList;
	}


	/**
	 *
	 */
	public void addGanttSeries(JRGanttSeries ganttSeries)
	{
		ganttSeriesList.add(ganttSeries);
		getEventSupport().fireCollectionElementAddedEvent(PROPERTY_GANTT_SERIES, ganttSeries,	ganttSeriesList.size() - 1);
	}
	
	
	/**
	 *
	 */
	public void addGanttSeries(int index, JRGanttSeries ganttSeries)
	{
		ganttSeriesList.add(index, ganttSeries);
		getEventSupport().fireCollectionElementAddedEvent(PROPERTY_GANTT_SERIES, ganttSeries,	index);
	}


	/**
	 *
	 */
	public JRGanttSeries removeGanttSeries(JRGanttSeries ganttSeries)
	{
		if (ganttSeries != null)
		{
			int idx = ganttSeriesList.indexOf(ganttSeries);
			if (idx >= 0)
			{
				ganttSeriesList.remove(ganttSeries);
				getEventSupport().fireCollectionElementRemovedEvent(PROPERTY_GANTT_SERIES, ganttSeries, idx);
			}
		}
		return ganttSeries;
	}


	/**
	 *
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
