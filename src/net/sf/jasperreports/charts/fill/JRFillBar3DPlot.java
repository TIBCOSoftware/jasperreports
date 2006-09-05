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

import java.awt.Color;

import net.sf.jasperreports.charts.JRBar3DPlot;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRFont;
import net.sf.jasperreports.engine.fill.JRFillChartPlot;
import net.sf.jasperreports.engine.fill.JRFillObjectFactory;

/**
 * @author User
 * @version $Id$ 
 */
public class JRFillBar3DPlot extends JRFillChartPlot implements JRBar3DPlot {
	
	/**
	 *
	 */
	public JRFillBar3DPlot( JRBar3DPlot barPlot, JRFillObjectFactory factory ){
		super( barPlot, factory );
	}
	
	/**
	 *
	 */
	public JRExpression getCategoryAxisLabelExpression()
	{
		return ((JRBar3DPlot)parent).getCategoryAxisLabelExpression();
	}

	/**
	 *
	 */
	public JRFont getCategoryAxisLabelFont()
	{
		return ((JRBar3DPlot)parent).getCategoryAxisLabelFont();
	}

	/**
	 *
	 */
	public void setCategoryAxisLabelFont(JRFont font)
	{
	}
	
	/**
	 *
	 */
	public Color getCategoryAxisLabelColor()
	{
		return ((JRBar3DPlot)parent).getCategoryAxisLabelColor();
	}

	/**
	 *
	 */
	public void setCategoryAxisLabelColor(Color color)
	{
	}

	/**
	 *
	 */
	public JRFont getCategoryAxisTickLabelFont()
	{
		return ((JRBar3DPlot)parent).getCategoryAxisTickLabelFont();
	}

	/**
	 *
	 */
	public void setCategoryAxisTickLabelFont(JRFont font)
	{
	}
	
	/**
	 *
	 */
	public Color getCategoryAxisTickLabelColor()
	{
		return ((JRBar3DPlot)parent).getCategoryAxisTickLabelColor();
	}

	/**
	 *
	 */
	public void setCategoryAxisTickLabelColor(Color color)
	{
	}

	/**
	 *
	 */
	public String getCategoryAxisTickLabelMask()
	{
		return ((JRBar3DPlot)parent).getCategoryAxisTickLabelMask();
	}

	/**
	 *
	 */
	public void setCategoryAxisTickLabelMask(String mask)
	{
	}

	/**
	 *
	 */
	public Color getCategoryAxisLineColor()
	{
		return ((JRBar3DPlot)parent).getCategoryAxisLineColor();
	}

	/**
	 *
	 */
	public void setCategoryAxisLineColor(Color color)
	{
	}

	/**
	 *
	 */
	public JRExpression getValueAxisLabelExpression()
	{
		return ((JRBar3DPlot)parent).getValueAxisLabelExpression();
	}

	/**
	 *
	 */
	public JRFont getValueAxisLabelFont()
	{
		return ((JRBar3DPlot)parent).getValueAxisLabelFont();
	}

	/**
	 *
	 */
	public void setValueAxisLabelFont(JRFont font)
	{
	}
	
	/**
	 *
	 */
	public Color getValueAxisLabelColor()
	{
		return ((JRBar3DPlot)parent).getValueAxisLabelColor();
	}

	/**
	 *
	 */
	public void setValueAxisLabelColor(Color color)
	{
	}

	/**
	 *
	 */
	public JRFont getValueAxisTickLabelFont()
	{
		return ((JRBar3DPlot)parent).getValueAxisTickLabelFont();
	}

	/**
	 *
	 */
	public void setValueAxisTickLabelFont(JRFont font)
	{
	}
	
	/**
	 *
	 */
	public Color getValueAxisTickLabelColor()
	{
		return ((JRBar3DPlot)parent).getValueAxisTickLabelColor();
	}

	/**
	 *
	 */
	public void setValueAxisTickLabelColor(Color color)
	{
	}

	/**
	 *
	 */
	public String getValueAxisTickLabelMask()
	{
		return ((JRBar3DPlot)parent).getValueAxisTickLabelMask();
	}

	/**
	 *
	 */
	public void setValueAxisTickLabelMask(String mask)
	{
	}

	/**
	 *
	 */
	public Color getValueAxisLineColor()
	{
		return ((JRBar3DPlot)parent).getValueAxisLineColor();
	}

	/**
	 *
	 */
	public void setValueAxisLineColor(Color color)
	{
	}
	
	/**
	 *
	 */
	public double getXOffset(){
		return ((JRBar3DPlot)parent).getXOffset();
	}
	
	/**
	 *
	 */
	public void setXOffset( double xOffset ){
	}
	
	/**
	 *
	 */
	public double getYOffset(){
		return ((JRBar3DPlot)parent).getYOffset();
	}
	
	/**
	 *
	 */
	public void setYOffset( double yOffset ){
	}
	
	/**
	 *
	 */
	public boolean isShowLabels(){
		return ((JRBar3DPlot)parent).isShowLabels();
	}
	
	/**
	 *
	 */
	public void setShowLabels( boolean isShowLabels ){
	}
}
