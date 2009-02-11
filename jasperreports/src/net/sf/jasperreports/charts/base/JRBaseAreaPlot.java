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
package net.sf.jasperreports.charts.base;

import java.awt.Color;

import net.sf.jasperreports.charts.JRAreaPlot;
import net.sf.jasperreports.engine.JRChart;
import net.sf.jasperreports.engine.JRChartPlot;
import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRExpressionCollector;
import net.sf.jasperreports.engine.JRFont;
import net.sf.jasperreports.engine.base.JRBaseChartPlot;
import net.sf.jasperreports.engine.base.JRBaseFont;
import net.sf.jasperreports.engine.base.JRBaseObjectFactory;
import net.sf.jasperreports.engine.util.JRStyleResolver;

/**
 * @author Flavius Sana (flavius_sana@users.sourceforge.net)
 * @version $Id$
 */
public class JRBaseAreaPlot extends JRBaseChartPlot implements JRAreaPlot {

	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;
	
	protected JRExpression categoryAxisLabelExpression = null;
	protected JRFont categoryAxisLabelFont = null;
	protected Color categoryAxisLabelColor = null;
	protected JRFont categoryAxisTickLabelFont = null;
	protected Color categoryAxisTickLabelColor = null;
	protected String categoryAxisTickLabelMask = null;
	protected Color categoryAxisLineColor = null;

	protected JRExpression valueAxisLabelExpression = null;
	protected JRFont valueAxisLabelFont = null;
	protected Color valueAxisLabelColor = null;
	protected JRFont valueAxisTickLabelFont = null;
	protected Color valueAxisTickLabelColor = null;
	protected String valueAxisTickLabelMask = null;
	protected Color valueAxisLineColor = null;
	

	/**
	 * 
	 */
	public JRBaseAreaPlot(JRChartPlot plot, JRChart chart)
	{
		super(plot, chart);
		
		JRAreaPlot areaPlot = plot instanceof JRAreaPlot ? (JRAreaPlot)plot : null;
		if (areaPlot == null)//FIXMECHART make a common interface and try copy props that are common to different plots
		{
			categoryAxisLabelFont = new JRBaseFont(chart, null); 
			categoryAxisTickLabelFont = new JRBaseFont(chart, null);
			valueAxisLabelFont = new JRBaseFont(chart, null);
			valueAxisTickLabelFont = new JRBaseFont(chart, null);
		}
		else
		{
			categoryAxisLabelFont = new JRBaseFont(chart, areaPlot.getCategoryAxisLabelFont()); 
			categoryAxisTickLabelFont = new JRBaseFont(chart, areaPlot.getCategoryAxisTickLabelFont());
			valueAxisLabelFont = new JRBaseFont(chart, areaPlot.getValueAxisLabelFont());
			valueAxisTickLabelFont = new JRBaseFont(chart, areaPlot.getValueAxisTickLabelFont());
		}
	}


	/**
	 * 
	 */
	public JRBaseAreaPlot( JRAreaPlot areaPlot, JRBaseObjectFactory factory )
	{
		super( areaPlot, factory );
		
		categoryAxisLabelExpression = factory.getExpression( areaPlot.getCategoryAxisLabelExpression() );
		categoryAxisLabelFont = new JRBaseFont(areaPlot.getChart(), areaPlot.getCategoryAxisLabelFont()); 
		categoryAxisLabelColor = areaPlot.getOwnCategoryAxisLabelColor();
		categoryAxisTickLabelFont = new JRBaseFont(areaPlot.getChart(), areaPlot.getCategoryAxisTickLabelFont());
		categoryAxisTickLabelColor = areaPlot.getOwnCategoryAxisTickLabelColor();
		categoryAxisTickLabelMask = areaPlot.getCategoryAxisTickLabelMask();
		categoryAxisLineColor = areaPlot.getOwnCategoryAxisLineColor();
		
		valueAxisLabelExpression = factory.getExpression( areaPlot.getValueAxisLabelExpression() );
		valueAxisLabelFont = new JRBaseFont(areaPlot.getChart(), areaPlot.getValueAxisLabelFont());
		valueAxisLabelColor = areaPlot.getOwnValueAxisLabelColor();
		valueAxisTickLabelFont = new JRBaseFont(areaPlot.getChart(), areaPlot.getValueAxisTickLabelFont());
		valueAxisTickLabelColor = areaPlot.getOwnValueAxisTickLabelColor();
		valueAxisTickLabelMask = areaPlot.getValueAxisTickLabelMask();
		valueAxisLineColor = areaPlot.getOwnValueAxisLineColor();
	}
	
	/**
	 * 
	 */
	public JRExpression getCategoryAxisLabelExpression(){
		return categoryAxisLabelExpression;
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
	public String getCategoryAxisTickLabelMask()
	{
		return categoryAxisTickLabelMask;
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
	public JRExpression getValueAxisLabelExpression(){
		return valueAxisLabelExpression;
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
	public String getValueAxisTickLabelMask()
	{
		return valueAxisTickLabelMask;
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
	public void collectExpressions(JRExpressionCollector collector)
	{
		collector.collect(this);
	}

	/**
	 *
	 */
	public Object clone(JRChart parentChart) 
	{
		JRBaseAreaPlot clone = (JRBaseAreaPlot)super.clone(parentChart);
		if (categoryAxisLabelExpression != null)
		{
			clone.categoryAxisLabelExpression = (JRExpression)categoryAxisLabelExpression.clone();
		}
		if (valueAxisLabelExpression != null)
		{
			clone.valueAxisLabelExpression = (JRExpression)valueAxisLabelExpression.clone();
		}
		return clone;
	}
}
