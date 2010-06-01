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
package net.sf.jasperreports.components.ofc;

import java.io.Serializable;

import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.base.JRBaseObjectFactory;
import net.sf.jasperreports.engine.component.Component;
import net.sf.jasperreports.engine.type.EvaluationTimeEnum;

/**
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id: PieChartComponent.java 3031 2009-08-27 11:14:57Z teodord $
 */
public class PieChartComponent implements Component, Serializable
{

	private static final long serialVersionUID = 1L;

	private EvaluationTimeEnum evaluationTime;
	private String evaluationGroup;
	
	private JRExpression titleExpression;
	private PieDataset dataset;

	public PieChartComponent()
	{
	}

	protected PieChartComponent(PieChartComponent chart, JRBaseObjectFactory baseFactory)
	{
		this.evaluationTime = chart.getEvaluationTime();
		this.evaluationGroup = chart.getEvaluationGroup();
		
		this.titleExpression = baseFactory.getExpression(chart.getTitleExpression());
		this.dataset = new CompiledPieDataset(chart.getDataset(), baseFactory);
	}
	
	public PieDataset getDataset()
	{
		return dataset;
	}

	public void setDataset(PieDataset dataset)
	{
		this.dataset = dataset;
	}
	
	public EvaluationTimeEnum getEvaluationTime()
	{
		return evaluationTime;
	}

	public void setEvaluationTime(EvaluationTimeEnum evaluationTime)
	{
		this.evaluationTime = evaluationTime;
	}

	public String getEvaluationGroup()
	{
		return evaluationGroup;
	}

	public void setEvaluationGroup(String evaluationGroup)
	{
		this.evaluationGroup = evaluationGroup;
	}
	
	public JRExpression getTitleExpression()
	{
		return titleExpression;
	}

	public void setTitleExpression(JRExpression titleExpression)
	{
		this.titleExpression = titleExpression;
	}

}
