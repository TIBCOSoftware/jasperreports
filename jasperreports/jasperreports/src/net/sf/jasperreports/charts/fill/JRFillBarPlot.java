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

import net.sf.jasperreports.charts.JRBarPlot;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRFont;
import net.sf.jasperreports.engine.fill.JRFillChartPlot;
import net.sf.jasperreports.engine.fill.JRFillObjectFactory;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JRFillBarPlot extends JRFillChartPlot implements JRBarPlot
{


	/**
	 *
	 */
	public JRFillBarPlot(
		JRBarPlot barPlot, 
		JRFillObjectFactory factory
		)
	{
		super(barPlot, factory);
	}
		

	/**
	 *
	 */
	public JRExpression getCategoryAxisLabelExpression()
	{
		return ((JRBarPlot)parent).getCategoryAxisLabelExpression();
	}

	/**
	 *
	 */
	public JRFont getCategoryAxisLabelFont()
	{
		return ((JRBarPlot)parent).getCategoryAxisLabelFont();
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
		return ((JRBarPlot)parent).getCategoryAxisLabelColor();
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
		return ((JRBarPlot)parent).getCategoryAxisTickLabelFont();
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
		return ((JRBarPlot)parent).getCategoryAxisTickLabelColor();
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
		return ((JRBarPlot)parent).getCategoryAxisTickLabelMask();
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
		return ((JRBarPlot)parent).getCategoryAxisLineColor();
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
		return ((JRBarPlot)parent).getValueAxisLabelExpression();
	}

	/**
	 *
	 */
	public JRFont getValueAxisLabelFont()
	{
		return ((JRBarPlot)parent).getValueAxisLabelFont();
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
		return ((JRBarPlot)parent).getValueAxisLabelColor();
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
		return ((JRBarPlot)parent).getValueAxisTickLabelFont();
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
		return ((JRBarPlot)parent).getValueAxisTickLabelColor();
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
		return ((JRBarPlot)parent).getValueAxisTickLabelMask();
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
		return ((JRBarPlot)parent).getValueAxisLineColor();
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
	public boolean isShowTickMarks()
	{
		return ((JRBarPlot)parent).isShowTickMarks();
	}
		
	/**
	 *
	 */
	public void setShowTickMarks(boolean isShowTickMarks)
	{
	}
		
	/**
	 *
	 */
	public boolean isShowTickLabels()
	{
		return ((JRBarPlot)parent).isShowTickLabels();
	}
		
	/**
	 *
	 */
	public void setShowTickLabels(boolean isShowTickLabels)
	{
	}

	/**
	 *
	 */
	public boolean isShowLabels(){
		return ((JRBarPlot)parent).isShowLabels();
	}
	
	/**
	 *
	 */
	public void setShowLabels( boolean isShowLabels ){
	}
	
}
