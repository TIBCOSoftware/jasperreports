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

import net.sf.jasperreports.charts.JRBubblePlot;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRFont;
import net.sf.jasperreports.engine.fill.JRFillChartPlot;
import net.sf.jasperreports.engine.fill.JRFillObjectFactory;

/**
 * @author Flavius Sana (flavius_sana@users.sourceforge.net)
 * @version $Id$
 */
public class JRFillBubblePlot extends JRFillChartPlot implements JRBubblePlot {

	/**
	 *
	 */
	public JRFillBubblePlot( JRBubblePlot bubblePlot, JRFillObjectFactory factory ){
		super( bubblePlot, factory );
	}
	
	/**
	 *
	 */
	public JRExpression getXAxisLabelExpression()
	{
		return ((JRBubblePlot)parent).getXAxisLabelExpression();
	}

	/**
	 *
	 */
	public JRFont getXAxisLabelFont()
	{
		return ((JRBubblePlot)parent).getXAxisLabelFont();
	}

	/**
	 *
	 */
	public void setXAxisLabelFont(JRFont font)
	{
	}
	
	/**
	 *
	 */
	public Color getXAxisLabelColor()
	{
		return ((JRBubblePlot)parent).getXAxisLabelColor();
	}

	/**
	 *
	 */
	public void setXAxisLabelColor(Color color)
	{
	}

	/**
	 *
	 */
	public JRFont getXAxisTickLabelFont()
	{
		return ((JRBubblePlot)parent).getXAxisTickLabelFont();
	}

	/**
	 *
	 */
	public void setXAxisTickLabelFont(JRFont font)
	{
	}

	/**
	 *
	 */
	public String getXAxisTickLabelMask()
	{
		return ((JRBubblePlot)parent).getXAxisTickLabelMask();
	}

	/**
	 *
	 */
	public void setXAxisTickLabelMask(String mask)
	{
	}

	/**
	 *
	 */
	public Color getXAxisTickLabelColor()
	{
		return ((JRBubblePlot)parent).getXAxisTickLabelColor();
	}

	/**
	 *
	 */
	public void setXAxisTickLabelColor(Color color)
	{
	}

	/**
	 *
	 */
	public Color getXAxisLineColor()
	{
		return ((JRBubblePlot)parent).getXAxisLineColor();
	}

	/**
	 *
	 */
	public void setXAxisLineColor(Color color)
	{
	}

	/**
	 *
	 */
	public JRExpression getYAxisLabelExpression()
	{
		return ((JRBubblePlot)parent).getYAxisLabelExpression();
	}

	/**
	 *
	 */
	public JRFont getYAxisLabelFont()
	{
		return ((JRBubblePlot)parent).getYAxisLabelFont();
	}

	/**
	 *
	 */
	public void setYAxisLabelFont(JRFont font)
	{
	}
	
	/**
	 *
	 */
	public Color getYAxisLabelColor()
	{
		return ((JRBubblePlot)parent).getYAxisLabelColor();
	}

	/**
	 *
	 */
	public void setYAxisLabelColor(Color color)
	{
	}

	/**
	 *
	 */
	public JRFont getYAxisTickLabelFont()
	{
		return ((JRBubblePlot)parent).getYAxisTickLabelFont();
	}

	/**
	 *
	 */
	public void setYAxisTickLabelFont(JRFont font)
	{
	}
	
	/**
	 *
	 */
	public Color getYAxisTickLabelColor()
	{
		return ((JRBubblePlot)parent).getYAxisTickLabelColor();
	}

	/**
	 *
	 */
	public void setYAxisTickLabelColor(Color color)
	{
	}

	/**
	 *
	 */
	public String getYAxisTickLabelMask()
	{
		return ((JRBubblePlot)parent).getYAxisTickLabelMask();
	}

	/**
	 *
	 */
	public void setYAxisTickLabelMask(String mask)
	{
	}

	/**
	 *
	 */
	public Color getYAxisLineColor()
	{
		return ((JRBubblePlot)parent).getYAxisLineColor();
	}

	/**
	 *
	 */
	public void setYAxisLineColor(Color color)
	{
	}

	/**
	 *
	 */
	public int getScaleType(){
		return ((JRBubblePlot)parent).getScaleType();
	}
	
	/**
	 *
	 */
	public void setScaleType( int scaleType ){
	}
	
}
