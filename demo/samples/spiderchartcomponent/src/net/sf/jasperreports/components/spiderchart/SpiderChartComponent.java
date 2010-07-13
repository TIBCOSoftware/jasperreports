/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2009 Jaspersoft Corporation. All rights reserved.
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
package net.sf.jasperreports.components.spiderchart;

import java.io.Serializable;

import net.sf.jasperreports.engine.base.JRBaseObjectFactory;
import net.sf.jasperreports.engine.component.Component;
import net.sf.jasperreports.engine.type.EvaluationTimeEnum;

/**
 * 
 * @author sanda zaharia (shertage@users.sourceforge.net)
 * @version $Id$
 */
public class SpiderChartComponent implements Component, Serializable
{

	private static final long serialVersionUID = 1L;
	
	private EvaluationTimeEnum evaluationTime;
	private String evaluationGroup;
	
	private ChartSettings chart;
	private SpiderDataset dataset;
	private SpiderPlot plot;
	
	public SpiderChartComponent()
	{
	}

	protected SpiderChartComponent(SpiderChartComponent chartComponent, JRBaseObjectFactory baseFactory)
	{
		this.evaluationTime = chartComponent.getEvaluationTime();
		this.evaluationGroup = chartComponent.getEvaluationGroup();
		
		this.chart = new StandardChartSettings(chartComponent.getChart(), baseFactory);
		this.dataset = new StandardSpiderDataset(chartComponent.getDataset(), baseFactory);
		this.plot = new StandardSpiderPlot(chartComponent.getPlot(), baseFactory);
		
	}

	/**
	 * @return the chart
	 */
	public ChartSettings getChart() {
		return chart;
	}

	/**
	 * @param chart the chart to set
	 */
	public void setChart(ChartSettings chart) {
		this.chart = chart;
	}

	/**
	 * @return the dataset
	 */
	public SpiderDataset getDataset() {
		return this.dataset;
	}


	/**
	 * @return the plot
	 */
	public SpiderPlot getPlot() {
		return this.plot;
	}

	public void setDataset(SpiderDataset dataset) {
		this.dataset = dataset;
	}

	public void setPlot(SpiderPlot plot) {
		this.plot = plot;
	}

	/**
	 * @return the evaluationTime
	 */
	public EvaluationTimeEnum getEvaluationTime() {
		return evaluationTime;
	}

	/**
	 * @param evaluationTime the evaluationTime to set
	 */
	public void setEvaluationTime(EvaluationTimeEnum evaluationTime) {
		this.evaluationTime = evaluationTime;
	}

	/**
	 * @return the evaluationGroup
	 */
	public String getEvaluationGroup() {
		return evaluationGroup;
	}

	/**
	 * @param evaluationGroup the evaluationGroup to set
	 */
	public void setEvaluationGroup(String evaluationGroup) {
		this.evaluationGroup = evaluationGroup;
	}

	
}
