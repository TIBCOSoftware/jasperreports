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
package net.sf.jasperreports.charts.design;

import java.awt.Color;

import net.sf.jasperreports.charts.base.JRBaseBar3DPlot;
import net.sf.jasperreports.charts.util.JRAxisFormat;
import net.sf.jasperreports.engine.JRChart;
import net.sf.jasperreports.engine.JRChartPlot;
import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRFont;

/**
 * @author Flavius Sana (flavius_sana@users.sourceforge.net)
 * @version $Id$ 
 */
public class JRDesignBar3DPlot extends JRBaseBar3DPlot {
	
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;
	
	public static final String PROPERTY_CATEGORY_AXIS_LABEL_COLOR = "categoryAxisLabelColor";
	
	public static final String PROPERTY_CATEGORY_AXIS_LABEL_EXPRESSION = "categoryAxisLabelExpression";
	
	public static final String PROPERTY_CATEGORY_AXIS_LABEL_FONT = "categoryAxisLabelFont";
	
	public static final String PROPERTY_CATEGORY_AXIS_LINE_COLOR = "categoryAxisLineColor";
	
	public static final String PROPERTY_CATEGORY_AXIS_TICK_LABEL_COLOR = "categoryAxisTickLabelColor";
	
	public static final String PROPERTY_CATEGORY_AXIS_TICK_LABEL_FONT = "categoryAxisTickLabelFont";
	
	public static final String PROPERTY_CATEGORY_AXIS_TICK_LABEL_MASK = "categoryAxisTickLabelMask";
	
	public static final String PROPERTY_VALUE_AXIS_LABEL_COLOR = "valueAxisLabelColor";
	
	public static final String PROPERTY_VALUE_AXIS_LABEL_EXPRESSION = "valueAxisLabelExpression";
	
	public static final String PROPERTY_VALUE_AXIS_LABEL_FONT = "valueAxisLabelFont";
	
	public static final String PROPERTY_VALUE_AXIS_LINE_COLOR = "valueAxisLineColor";
	
	public static final String PROPERTY_VALUE_AXIS_TICK_LABEL_COLOR = "valueAxisTickLabelColor";
	
	public static final String PROPERTY_VALUE_AXIS_TICK_LABEL_FONT = "valueAxisTickLabelFont";
	
	public static final String PROPERTY_VALUE_AXIS_TICK_LABEL_MASK = "valueAxisTickLabelMask";


	public JRDesignBar3DPlot(JRChartPlot barPlot, JRChart chart)
	{
		super(barPlot, chart);
	}

	/**
	 *
	 */
	public void setCategoryAxisLabelExpression(JRExpression categoryAxisLabelExpression)
	{
		Object old = this.categoryAxisLabelExpression;
		this.categoryAxisLabelExpression = categoryAxisLabelExpression;
		getEventSupport().firePropertyChange(PROPERTY_CATEGORY_AXIS_LABEL_EXPRESSION, old, this.categoryAxisLabelExpression);
	}

	/**
	 *
	 */
	public void setCategoryAxisLabelFont(JRFont categoryAxisLabelFont)
	{
		Object old = this.categoryAxisLabelFont;
		this.categoryAxisLabelFont = categoryAxisLabelFont;
		getEventSupport().firePropertyChange(PROPERTY_CATEGORY_AXIS_LABEL_FONT, old, this.categoryAxisLabelFont);
	}

	/**
	 *
	 */
	public void setCategoryAxisLabelColor(Color categoryAxisLabelColor)
	{
		Object old = this.categoryAxisLabelColor;
		this.categoryAxisLabelColor = categoryAxisLabelColor;
		getEventSupport().firePropertyChange(PROPERTY_CATEGORY_AXIS_LABEL_COLOR, old, this.categoryAxisLabelColor);
	}

	/**
	 *
	 */
	public void setCategoryAxisTickLabelFont(JRFont categoryAxisTickLabelFont)
	{
		Object old = this.categoryAxisTickLabelFont;
		this.categoryAxisTickLabelFont = categoryAxisTickLabelFont;
		getEventSupport().firePropertyChange(PROPERTY_CATEGORY_AXIS_TICK_LABEL_FONT, old, this.categoryAxisTickLabelFont);
	}

	/**
	 *
	 */
	public void setCategoryAxisTickLabelColor(Color categoryAxisTickLabelColor)
	{
		Object old = this.categoryAxisTickLabelColor;
		this.categoryAxisTickLabelColor = categoryAxisTickLabelColor;
		getEventSupport().firePropertyChange(PROPERTY_CATEGORY_AXIS_TICK_LABEL_COLOR, old, this.categoryAxisTickLabelColor);
	}

	/**
	 *
	 */
	public void setCategoryAxisTickLabelMask(String categoryAxisTickLabelMask)
	{
		Object old = this.categoryAxisTickLabelMask;
		this.categoryAxisTickLabelMask = categoryAxisTickLabelMask;
		getEventSupport().firePropertyChange(PROPERTY_CATEGORY_AXIS_TICK_LABEL_MASK, old, this.categoryAxisTickLabelMask);
	}

	/**
	 *
	 */
	public void setCategoryAxisLineColor(Color categoryAxisLineColor)
	{
		Object old = this.categoryAxisLineColor;
		this.categoryAxisLineColor = categoryAxisLineColor;
		getEventSupport().firePropertyChange(PROPERTY_CATEGORY_AXIS_LINE_COLOR, old, this.categoryAxisLineColor);
	}
	
	/**
	 *
	 */
	public void setValueAxisLabelExpression(JRExpression valueAxisLabelExpression)
	{
		Object old = this.valueAxisLabelExpression;
		this.valueAxisLabelExpression = valueAxisLabelExpression;
		getEventSupport().firePropertyChange(PROPERTY_VALUE_AXIS_LABEL_EXPRESSION, old, this.valueAxisLabelExpression);
	}

	/**
	 *
	 */
	public void setValueAxisLabelFont(JRFont valueAxisLabelFont)
	{
		Object old = this.valueAxisLabelFont;
		this.valueAxisLabelFont = valueAxisLabelFont;
		getEventSupport().firePropertyChange(PROPERTY_VALUE_AXIS_LABEL_FONT, old, this.valueAxisLabelFont);
	}

	/**
	 *
	 */
	public void setValueAxisLabelColor(Color valueAxisLabelColor)
	{
		Object old = this.valueAxisLabelColor;
		this.valueAxisLabelColor = valueAxisLabelColor;
		getEventSupport().firePropertyChange(PROPERTY_VALUE_AXIS_LABEL_COLOR, old, this.valueAxisLabelColor);
	}
	
	/**
	 *
	 */
	public void setValueAxisTickLabelFont(JRFont valueAxisTickLabelFont)
	{
		Object old = this.valueAxisTickLabelFont;
		this.valueAxisTickLabelFont = valueAxisTickLabelFont;
		getEventSupport().firePropertyChange(PROPERTY_VALUE_AXIS_TICK_LABEL_FONT, old, this.valueAxisTickLabelFont);
	}

	/**
	 *
	 */
	public void setValueAxisTickLabelColor(Color valueAxisTickLabelColor)
	{
		Object old = this.valueAxisTickLabelColor;
		this.valueAxisTickLabelColor = valueAxisTickLabelColor;
		getEventSupport().firePropertyChange(PROPERTY_VALUE_AXIS_TICK_LABEL_COLOR, old, this.valueAxisTickLabelColor);
	}

	/**
	 *
	 */
	public void setValueAxisTickLabelMask(String valueAxisTickLabelMask)
	{
		Object old = this.valueAxisTickLabelMask;
		this.valueAxisTickLabelMask = valueAxisTickLabelMask;
		getEventSupport().firePropertyChange(PROPERTY_VALUE_AXIS_TICK_LABEL_MASK, old, this.valueAxisTickLabelMask);
	}

	/**
	 *
	 */
	public void setValueAxisLineColor(Color valueAxisLineColor)
	{
		Object old = this.valueAxisLineColor;
		this.valueAxisLineColor = valueAxisLineColor;
		getEventSupport().firePropertyChange(PROPERTY_VALUE_AXIS_LINE_COLOR, old, this.valueAxisLineColor);
	}
	
	/**
	 * 
	 */
	public void setCategoryAxisFormat(JRAxisFormat axisFormat)
	{
		setCategoryAxisLabelFont(axisFormat.getLabelFont());
		setCategoryAxisLabelColor(axisFormat.getLabelColor());
		setCategoryAxisTickLabelFont(axisFormat.getTickLabelFont());
		setCategoryAxisTickLabelColor(axisFormat.getTickLabelColor());
		setCategoryAxisTickLabelMask(axisFormat.getTickLabelMask());
		setCategoryAxisLineColor(axisFormat.getLineColor());
	}

	/**
	 * 
	 */
	public void setValueAxisFormat(JRAxisFormat axisFormat)
	{
		setValueAxisLabelFont(axisFormat.getLabelFont());
		setValueAxisLabelColor(axisFormat.getLabelColor());
		setValueAxisTickLabelFont(axisFormat.getTickLabelFont());
		setValueAxisTickLabelColor(axisFormat.getTickLabelColor());
		setValueAxisTickLabelMask(axisFormat.getTickLabelMask());
		setValueAxisLineColor(axisFormat.getLineColor());
	}
}
