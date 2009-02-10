/*
 * ============================================================================
 * GNU Lesser General Public License
 * ============================================================================
 *
 * JasperReports - Free Java report-generating library.
 * Copyright (C) 2001-2006 JasperSoft Corporation http://www.jaspersoft.com
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
 * 303 Second Street, Suite 450 North
 * San Francisco, CA 94107
 * http://www.jaspersoft.com
 */
package com.jaspersoft.sample.ofc;

import java.io.Serializable;

import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.base.JRBaseObjectFactory;
import net.sf.jasperreports.engine.component.Component;

/**
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public class PieChartComponent implements Component, Serializable
{

	private static final long serialVersionUID = 1L;

	private byte evaluationTime;
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
	
	public byte getEvaluationTime()
	{
		return evaluationTime;
	}

	public void setEvaluationTime(byte evaluationTime)
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
