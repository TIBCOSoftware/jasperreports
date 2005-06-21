/*
 * ============================================================================
 *                   GNU Lesser General Public License
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

import org.jfree.chart.renderer.category.BarRenderer3D;

import net.sf.jasperreports.charts.JRBar3DPlot;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.base.JRBaseChartPlot;
import net.sf.jasperreports.engine.base.JRBaseObjectFactory;

/**
 * @author Flavius Sana
 * @version $Id$ 
 */
public class JRBaseBar3DPlot extends JRBaseChartPlot implements JRBar3DPlot {

	private static final long serialVersionUID = 608;
	
	protected JRExpression categoryAxisLabelExpression = null;
	protected JRExpression valueAxisLabelExpression = null;
	protected double xOffset = BarRenderer3D.DEFAULT_X_OFFSET;
	protected double yOffset = BarRenderer3D.DEFAULT_X_OFFSET;
	
	
	protected JRBaseBar3DPlot(){
		
	}
	
	public JRBaseBar3DPlot( JRBar3DPlot barPlot, JRBaseObjectFactory factory ){
		super( barPlot, factory );
		
		xOffset = barPlot.getXOffset();
		yOffset = barPlot.getYOffset();
		
		categoryAxisLabelExpression = factory.getExpression( barPlot.getCategoryAxisLabelExpression() );
		valueAxisLabelExpression = factory.getExpression( barPlot.getValueAxisLabelExpression() );
		
	}
	
	public JRExpression getCategoryAxisLabelExpression(){
		return categoryAxisLabelExpression;
	}
	
	public JRExpression getValueAxisLabelExpression(){
		return valueAxisLabelExpression;
	}
	
	public double getXOffset(){
		return xOffset;
	}
	
	public void setXOffset( double xOffset ){
		this.xOffset = xOffset;
	}
	
	public double getYOffset(){
		return yOffset;
	}
	
	public void setYOffset( double yOffset ){
		this.yOffset = yOffset;
	}
}
