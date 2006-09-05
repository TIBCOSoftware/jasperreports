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

import net.sf.jasperreports.charts.JRLinePlot;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRFont;
import net.sf.jasperreports.engine.fill.JRFillChartPlot;
import net.sf.jasperreports.engine.fill.JRFillObjectFactory;

/**
 * @author Flavius Sana (flavius_sana@users.sourceforge.net)
 * @version $Id$
 */
public class JRFillLinePlot extends JRFillChartPlot implements JRLinePlot {
	
	/**
	 *
	 */
	public JRFillLinePlot( JRLinePlot plot, JRFillObjectFactory factory ){
		super( plot, factory );
	}
	
	/**
	 *
	 */
	public JRExpression getCategoryAxisLabelExpression()
	{
		return ((JRLinePlot)parent).getCategoryAxisLabelExpression();
	}

	/**
	 *
	 */
	public JRFont getCategoryAxisLabelFont()
	{
		return ((JRLinePlot)parent).getCategoryAxisLabelFont();
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
		return ((JRLinePlot)parent).getCategoryAxisLabelColor();
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
		return ((JRLinePlot)parent).getCategoryAxisTickLabelFont();
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
		return ((JRLinePlot)parent).getCategoryAxisTickLabelColor();
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
		return ((JRLinePlot)parent).getCategoryAxisTickLabelMask();
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
		return ((JRLinePlot)parent).getCategoryAxisLineColor();
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
		return ((JRLinePlot)parent).getValueAxisLabelExpression();
	}

	/**
	 *
	 */
	public JRFont getValueAxisLabelFont()
	{
		return ((JRLinePlot)parent).getValueAxisLabelFont();
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
		return ((JRLinePlot)parent).getValueAxisLabelColor();
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
		return ((JRLinePlot)parent).getValueAxisTickLabelFont();
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
		return ((JRLinePlot)parent).getValueAxisTickLabelColor();
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
		return ((JRLinePlot)parent).getValueAxisTickLabelMask();
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
		return ((JRLinePlot)parent).getValueAxisLineColor();
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
	public boolean isShowShapes(){
		return ((JRLinePlot)parent).isShowShapes();
	}
	
	/**
	 *
	 */
	public void setShowShapes( boolean value ){
	}
	
	/**
	 *
	 */
	public boolean isShowLines(){
		return ((JRLinePlot)parent).isShowLines();
	}
	
	/**
	 *
	 */
	public void setShowLines( boolean value ){
	}
	
}
