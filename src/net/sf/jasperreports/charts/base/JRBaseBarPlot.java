/*
 * ============================================================================
 * GNU Lesser General Public License
 * ============================================================================
 *
 * JasperReports - Free Java report-generating library.
 * Copyright (C) 2001-2005 JasperSoft Corporation http://www.jaspersoft.com
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
 * 185, Berry Street, Suite 6200
 * San Francisco CA 94107
 * http://www.jaspersoft.com
 */
package net.sf.jasperreports.charts.base;

import net.sf.jasperreports.charts.JRBarPlot;
import net.sf.jasperreports.engine.JRChartPlot;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRExpressionCollector;
import net.sf.jasperreports.engine.base.JRBaseChartPlot;
import net.sf.jasperreports.engine.base.JRBaseObjectFactory;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JRBaseBarPlot extends JRBaseChartPlot implements JRBarPlot
{


	/**
	 *
	 */
	private static final long serialVersionUID = 10002;

	protected JRExpression categoryAxisLabelExpression = null;
	protected JRExpression valueAxisLabelExpression = null;
	
	protected boolean isShowTickMarks = false;
	protected boolean isShowTickLabels = false;
	protected boolean isShowLabels = false;

	
	/**
	 *
	 */
	public JRBaseBarPlot(JRChartPlot barPlot)
	{
		super(barPlot);
	}
	
	/**
	 *
	 */
	public JRBaseBarPlot(JRBarPlot barPlot, JRBaseObjectFactory factory)
	{
		super(barPlot, factory);

		isShowTickMarks = barPlot.isShowTickMarks();
		isShowTickLabels = barPlot.isShowTickLabels();
		isShowLabels = barPlot.isShowLabels();
		
		categoryAxisLabelExpression = factory.getExpression(barPlot.getCategoryAxisLabelExpression());
		valueAxisLabelExpression = factory.getExpression(barPlot.getValueAxisLabelExpression());
	}

	/**
	 *
	 */
	public JRExpression getCategoryAxisLabelExpression()
	{
		return categoryAxisLabelExpression;
	}
		
	/**
	 *
	 */
	public JRExpression getValueAxisLabelExpression()
	{
		return valueAxisLabelExpression;
	}
		
	/**
	 *
	 */
	public boolean isShowTickMarks()
	{
		return isShowTickMarks;
	}
		
	/**
	 *
	 */
	public void setShowTickMarks(boolean isShowTickMarks)
	{
		this.isShowTickMarks = isShowTickMarks;
	}
		
	/**
	 *
	 */
	public boolean isShowTickLabels()
	{
		return isShowTickLabels;
	}
		
	/**
	 *
	 */
	public void setShowTickLabels(boolean isShowTickLabels)
	{
		this.isShowTickLabels = isShowTickLabels;
	}
	
	/**
	 * 
	 */
	public boolean isShowLabels(){
		return isShowLabels;
	}
	
	/**
	 * 
	 */
	public void setShowLabels( boolean isShowLabels ){
		this.isShowLabels = isShowLabels;
	}
		
	/**
	 *
	 */
	public void collectExpressions(JRExpressionCollector collector)
	{
		collector.collect(this);
	}

}
