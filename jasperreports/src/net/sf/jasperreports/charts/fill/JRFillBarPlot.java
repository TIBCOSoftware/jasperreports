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
import net.sf.jasperreports.engine.base.JRBaseFont;
import net.sf.jasperreports.engine.fill.JRFillChartPlot;
import net.sf.jasperreports.engine.fill.JRFillObjectFactory;
import net.sf.jasperreports.engine.util.JRStyleResolver;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JRFillBarPlot extends JRFillChartPlot implements JRBarPlot
{


	/**
	 *
	 */
	protected JRFont categoryAxisLabelFont = null;
	protected Color categoryAxisLabelColor = null;
	protected JRFont categoryAxisTickLabelFont = null;
	protected Color categoryAxisTickLabelColor = null;
	protected Color categoryAxisLineColor = null;

	protected JRFont valueAxisLabelFont = null;
	protected Color valueAxisLabelColor = null;
	protected JRFont valueAxisTickLabelFont = null;
	protected Color valueAxisTickLabelColor = null;
	protected Color valueAxisLineColor = null;

	
	/**
	 *
	 */
	public JRFillBarPlot(
		JRBarPlot barPlot, 
		JRFillObjectFactory factory
		)
	{
		super(barPlot, factory);

		categoryAxisLabelFont = new JRBaseFont(barPlot.getChart(), barPlot.getCategoryAxisLabelFont()); 
		categoryAxisLabelColor = barPlot.getOwnCategoryAxisLabelColor();
		categoryAxisTickLabelFont = new JRBaseFont(barPlot.getChart(), barPlot.getCategoryAxisTickLabelFont());
		categoryAxisTickLabelColor = barPlot.getOwnCategoryAxisTickLabelColor();
		categoryAxisLineColor = barPlot.getOwnCategoryAxisLineColor();
		
		valueAxisLabelFont = new JRBaseFont(barPlot.getChart(), barPlot.getValueAxisLabelFont());
		valueAxisLabelColor = barPlot.getOwnValueAxisLabelColor();
		valueAxisTickLabelFont = new JRBaseFont(barPlot.getChart(), barPlot.getValueAxisTickLabelFont());
		valueAxisTickLabelColor = barPlot.getOwnValueAxisTickLabelColor();
		valueAxisLineColor = barPlot.getOwnValueAxisLineColor();
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
		return categoryAxisLabelFont;
	}

	/**
	 *
	 */
	public Color getCategoryAxisLabelColor()
	{
		return JRStyleResolver.getCategoryAxisLabelColor(this, this);
	}

	/**
	 *
	 */
	public Color getOwnCategoryAxisLabelColor()
	{
		return categoryAxisLabelColor;
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
		return categoryAxisTickLabelFont;
	}

	/**
	 *
	 */
	public Color getCategoryAxisTickLabelColor()
	{
		return JRStyleResolver.getCategoryAxisTickLabelColor(this, this);
	}

	/**
	 *
	 */
	public Color getOwnCategoryAxisTickLabelColor()
	{
		return categoryAxisTickLabelColor;
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
		return JRStyleResolver.getCategoryAxisLineColor(this, this);
	}

	/**
	 *
	 */
	public Color getOwnCategoryAxisLineColor()
	{
		return categoryAxisLineColor;
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
		return valueAxisLabelFont;
	}

	/**
	 *
	 */
	public Color getValueAxisLabelColor()
	{
		return JRStyleResolver.getValueAxisLabelColor(this, this);
	}

	/**
	 *
	 */
	public Color getOwnValueAxisLabelColor()
	{
		return valueAxisLabelColor;
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
		return valueAxisTickLabelFont;
	}

	/**
	 *
	 */
	public Color getValueAxisTickLabelColor()
	{
		return JRStyleResolver.getValueAxisTickLabelColor(this, this);
	}

	/**
	 *
	 */
	public Color getOwnValueAxisTickLabelColor()
	{
		return valueAxisTickLabelColor;
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
		return JRStyleResolver.getValueAxisLineColor(this, this);
	}

	/**
	 *
	 */
	public Color getOwnValueAxisLineColor()
	{
		return valueAxisLineColor;
	}

	/**
	 *
	 */
	public void setValueAxisLineColor(Color color)
	{
	}

	/**
	 * @deprecated Replaced by {@link #getShowTickMarks()} 
	 */
	public boolean isShowTickMarks()
	{
		return ((JRBarPlot)parent).isShowTickMarks();
	}
		
	/**
	 *
	 */
	public Boolean getShowTickMarks()
	{
		return ((JRBarPlot)parent).getShowTickMarks();
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
	public void setShowTickMarks(Boolean isShowTickMarks)
	{
	}
		
	/**
	 * @deprecated Replaced by {@link #getShowTickLabels()}
	 */
	public boolean isShowTickLabels()
	{
		return ((JRBarPlot)parent).isShowTickLabels();
	}
		
	/**
	 *
	 */
	public Boolean getShowTickLabels()
	{
		return ((JRBarPlot)parent).getShowTickLabels();
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
	public void setShowTickLabels(Boolean isShowTickLabels)
	{
	}

	/**
	 * @deprecated Replaced by {@link #getShowLabels()}
	 */
	public boolean isShowLabels(){
		return ((JRBarPlot)parent).isShowLabels();
	}
	
	/**
	 *
	 */
	public Boolean getShowLabels(){
		return ((JRBarPlot)parent).getShowLabels();
	}
	
	/**
	 *
	 */
	public void setShowLabels( boolean isShowLabels ){
	}
	/**
	 *
	 */
	public void setShowLabels( Boolean isShowLabels ){
	}
	
}
