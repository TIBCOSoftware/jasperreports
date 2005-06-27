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

import org.jfree.chart.renderer.xy.XYBubbleRenderer;

import net.sf.jasperreports.charts.JRBubblePlot;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRChartPlot;
import net.sf.jasperreports.engine.base.JRBaseObjectFactory;
import net.sf.jasperreports.engine.base.JRBaseChartPlot;

/**
 * @author Flavius Sana (flavius_sana@users.sourceforge.net)
 * @version $Id$
 */
public class JRBaseBubblePlot extends JRBaseChartPlot implements JRBubblePlot {

	private static final long serialVersionUID = 608;
	
	protected JRExpression xAxisLabelExpression = null;
	protected JRExpression yAxisLabelExpression = null;
	
	protected int scaleType = XYBubbleRenderer.SCALE_ON_RANGE_AXIS;
	
	
	public JRBaseBubblePlot( JRChartPlot bubblePlot){
		super( bubblePlot);
	}


	public JRBaseBubblePlot( JRBubblePlot bubblePlot, JRBaseObjectFactory factory ){
		super( bubblePlot, factory );
		
		scaleType = bubblePlot.getScaleType();
		
		xAxisLabelExpression = factory.getExpression( bubblePlot.getXAxisLabelExpression() );
		yAxisLabelExpression = factory.getExpression( bubblePlot.getYAxisLabelExpression() );
	}
	
	public JRExpression getXAxisLabelExpression(){
		return xAxisLabelExpression;
	}
	
	public JRExpression getYAxisLabelExpression(){
		return yAxisLabelExpression;
	}
	
	public int getScaleType(){
		return scaleType;
	}
	
	public void setScaleType( int scaleType ){
		this.scaleType = scaleType;
	}
}
