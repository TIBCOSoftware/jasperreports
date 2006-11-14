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

import net.sf.jasperreports.charts.JRTimeSeriesPlot;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRFont;
import net.sf.jasperreports.engine.fill.JRFillChartPlot;
import net.sf.jasperreports.engine.fill.JRFillObjectFactory;

/**
 * @author Flavius Sana (flavius_sana@users.sourceforge.net)
 * @version $Id$
 */

public class JRFillTimeSeriesPlot extends JRFillChartPlot implements JRTimeSeriesPlot {

	public JRFillTimeSeriesPlot( JRTimeSeriesPlot plot, JRFillObjectFactory factory ){
		super( plot, factory );
	}
	
	/**
	 *
	 */
	public JRExpression getTimeAxisLabelExpression()
	{
		return ((JRTimeSeriesPlot)parent).getTimeAxisLabelExpression();
	}

	/**
	 *
	 */
	public JRFont getTimeAxisLabelFont()
	{
		return ((JRTimeSeriesPlot)parent).getTimeAxisLabelFont();
	}

	/**
	 *
	 */
	public void setTimeAxisLabelFont(JRFont font)
	{
	}
	
	/**
	 *
	 */
	public Color getTimeAxisLabelColor()
	{
		return ((JRTimeSeriesPlot)parent).getTimeAxisLabelColor();
	}

	/**
	 *
	 */
	public void setTimeAxisLabelColor(Color color)
	{
	}

	/**
	 *
	 */
	public JRFont getTimeAxisTickLabelFont()
	{
		return ((JRTimeSeriesPlot)parent).getTimeAxisTickLabelFont();
	}

	/**
	 *
	 */
	public void setTimeAxisTickLabelFont(JRFont font)
	{
	}
	
	/**
	 *
	 */
	public Color getTimeAxisTickLabelColor()
	{
		return ((JRTimeSeriesPlot)parent).getTimeAxisTickLabelColor();
	}

	/**
	 *
	 */
	public void setTimeAxisTickLabelColor(Color color)
	{
	}

	/**
	 *
	 */
	public String getTimeAxisTickLabelMask()
	{
		return ((JRTimeSeriesPlot)parent).getTimeAxisTickLabelMask();
	}

	/**
	 *
	 */
	public void setTimeAxisTickLabelMask(String mask)
	{
	}

	/**
	 *
	 */
	public Color getTimeAxisLineColor()
	{
		return ((JRTimeSeriesPlot)parent).getTimeAxisLineColor();
	}

	/**
	 *
	 */
	public void setTimeAxisLineColor(Color color)
	{
	}

	/**
	 *
	 */
	public JRExpression getValueAxisLabelExpression()
	{
		return ((JRTimeSeriesPlot)parent).getValueAxisLabelExpression();
	}

	/**
	 *
	 */
	public JRFont getValueAxisLabelFont()
	{
		return ((JRTimeSeriesPlot)parent).getValueAxisLabelFont();
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
		return ((JRTimeSeriesPlot)parent).getValueAxisLabelColor();
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
		return ((JRTimeSeriesPlot)parent).getValueAxisTickLabelFont();
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
		return ((JRTimeSeriesPlot)parent).getValueAxisTickLabelColor();
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
		return ((JRTimeSeriesPlot)parent).getValueAxisTickLabelMask();
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
		return ((JRTimeSeriesPlot)parent).getValueAxisLineColor();
	}

	/**
	 *
	 */
	public void setValueAxisLineColor(Color color)
	{
	}

	public boolean isShowLines(){
		return ((JRTimeSeriesPlot)parent).isShowLines();
	}
	
	public void setShowLines(){
	}
	
	public boolean isShowShapes(){
		return ((JRTimeSeriesPlot)parent).isShowShapes();
	}
	
	public void setShowShapes(){
	}
}
