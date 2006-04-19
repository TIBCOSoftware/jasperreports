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
package net.sf.jasperreports.charts.fill;

import net.sf.jasperreports.charts.JRBar3DPlot;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.fill.JRFillChartPlot;
import net.sf.jasperreports.engine.fill.JRFillObjectFactory;

/**
 * @author User
 * @version $Id$ 
 */
public class JRFillBar3DPlot extends JRFillChartPlot implements JRBar3DPlot {
	
	public JRFillBar3DPlot( JRBar3DPlot barPlot, JRFillObjectFactory factory ){
		super( barPlot, factory );
	}
	
	public JRExpression getCategoryAxisLabelExpression(){
		return ((JRBar3DPlot)parent).getCategoryAxisLabelExpression();
	}
	
	public JRExpression getValueAxisLabelExpression(){
		return ((JRBar3DPlot)parent).getValueAxisLabelExpression();
	}
	
	public double getXOffset(){
		return ((JRBar3DPlot)parent).getXOffset();
	}
	
	public void setXOffset( double xOffset ){
	}
	
	public double getYOffset(){
		return ((JRBar3DPlot)parent).getYOffset();
	}
	
	public void setYOffset( double yOffset ){
	}
	
	public boolean isShowLabels(){
		return ((JRBar3DPlot)parent).isShowLabels();
	}
	
	public void setShowLabels( boolean isShowLabels ){
	}
	
	
}
