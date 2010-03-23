/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2009 Jaspersoft Corporation. All rights reserved.
 * http://www.jaspersoft.com
 *
 * Unless you have purchased a commercial license agreement from Jaspersoft,
 * the following license terms apply:
 *
 * This program is part of JasperReports.
 *
 * JasperReports is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * JasperReports is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with JasperReports. If not, see <http://www.gnu.org/licenses/>.
 */
package net.sf.jasperreports.charts.base;

import java.awt.Color;
import java.io.IOException;
import java.io.ObjectInputStream;

import net.sf.jasperreports.charts.JRBubblePlot;
import net.sf.jasperreports.charts.type.ScaleTypeEnum;
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

import org.jfree.chart.renderer.xy.XYBubbleRenderer;

/**
 * @author Flavius Sana (flavius_sana@users.sourceforge.net)
 * @version $Id$
 */
public class JRBaseBubblePlot extends JRBaseChartPlot implements JRBubblePlot {

	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;
	
	public static final String PROPERTY_SCALE_TYPE = "scaleType";
	
	protected JRExpression xAxisLabelExpression = null;
	protected JRFont xAxisLabelFont = null;
	protected Color xAxisLabelColor = null;
	protected JRFont xAxisTickLabelFont = null;
	protected Color xAxisTickLabelColor = null;
	protected String xAxisTickLabelMask = null;
	protected Boolean xAxisVerticalTickLabels = null;
	protected Color xAxisLineColor = null;

	protected JRExpression yAxisLabelExpression = null;
	protected JRFont yAxisLabelFont = null;
	protected Color yAxisLabelColor = null;
	protected JRFont yAxisTickLabelFont = null;
	protected Color yAxisTickLabelColor = null;
	protected String yAxisTickLabelMask = null;
	protected Boolean yAxisVerticalTickLabels = null;
	protected Color yAxisLineColor = null;
	
	protected JRExpression domainAxisMinValueExpression = null;
	protected JRExpression domainAxisMaxValueExpression = null;
	protected JRExpression rangeAxisMinValueExpression = null;
	protected JRExpression rangeAxisMaxValueExpression = null;
	protected ScaleTypeEnum scaleTypeValue = null;
	
	
	/**
	 * 
	 */
	public JRBaseBubblePlot(JRChartPlot plot, JRChart chart)
	{
		super(plot, chart);
		
		JRBubblePlot bubblePlot = plot instanceof JRBubblePlot ? (JRBubblePlot)plot : null;
		if (bubblePlot == null)
		{
			xAxisLabelFont = new JRBaseFont(chart, null);
			xAxisTickLabelFont = new JRBaseFont(chart, null);
			yAxisLabelFont = new JRBaseFont(chart, null);
			yAxisTickLabelFont = new JRBaseFont(chart, null);
		}
		else
		{
			xAxisLabelFont = new JRBaseFont(chart, bubblePlot.getXAxisLabelFont());
			xAxisTickLabelFont = new JRBaseFont(chart, bubblePlot.getXAxisTickLabelFont());
			yAxisLabelFont = new JRBaseFont(chart, bubblePlot.getYAxisLabelFont());
			yAxisTickLabelFont = new JRBaseFont(chart, bubblePlot.getYAxisTickLabelFont());
		}
	}


	/**
	 * 
	 */
	public JRBaseBubblePlot(JRBubblePlot bubblePlot, JRBaseObjectFactory factory )
	{
		super( bubblePlot, factory );
		
		scaleTypeValue = bubblePlot.getScaleTypeValue();
		
		xAxisLabelExpression = factory.getExpression( bubblePlot.getXAxisLabelExpression() );
		xAxisLabelFont = new JRBaseFont(bubblePlot.getChart(), bubblePlot.getXAxisLabelFont());
		xAxisLabelColor = bubblePlot.getOwnXAxisLabelColor();
		xAxisTickLabelFont = new JRBaseFont(bubblePlot.getChart(), bubblePlot.getXAxisTickLabelFont());
		xAxisTickLabelColor = bubblePlot.getOwnXAxisTickLabelColor();
		xAxisTickLabelMask = bubblePlot.getXAxisTickLabelMask();
		xAxisVerticalTickLabels = bubblePlot.getXAxisVerticalTickLabels();
		xAxisLineColor = bubblePlot.getOwnXAxisLineColor();
		
		yAxisLabelExpression = factory.getExpression( bubblePlot.getYAxisLabelExpression() );
		yAxisLabelFont = new JRBaseFont(bubblePlot.getChart(), bubblePlot.getYAxisLabelFont());
		yAxisLabelColor = bubblePlot.getOwnYAxisLabelColor();
		yAxisTickLabelFont = new JRBaseFont(bubblePlot.getChart(), bubblePlot.getYAxisTickLabelFont());
		yAxisTickLabelColor = bubblePlot.getOwnYAxisTickLabelColor();
		yAxisTickLabelMask = bubblePlot.getYAxisTickLabelMask();
		yAxisVerticalTickLabels = bubblePlot.getYAxisVerticalTickLabels();
		yAxisLineColor = bubblePlot.getOwnYAxisLineColor();
		
		domainAxisMinValueExpression = factory.getExpression( bubblePlot.getDomainAxisMinValueExpression() );
		domainAxisMaxValueExpression = factory.getExpression( bubblePlot.getDomainAxisMaxValueExpression() );
		rangeAxisMinValueExpression = factory.getExpression( bubblePlot.getRangeAxisMinValueExpression() );
		rangeAxisMaxValueExpression = factory.getExpression( bubblePlot.getRangeAxisMaxValueExpression() );
	}
	
	/**
	 * 
	 */
	public JRExpression getXAxisLabelExpression(){
		return xAxisLabelExpression;
	}
	
	/**
	 * 
	 */
	public JRFont getXAxisLabelFont()
	{
		return xAxisLabelFont;
	}
	
	/**
	 * 
	 */
	public Color getXAxisLabelColor()
	{
		return JRStyleResolver.getXAxisLabelColor(this, this);
	}
		
	/**
	 * 
	 */
	public Color getOwnXAxisLabelColor()
	{
		return xAxisLabelColor;
	}
		
	/**
	 * 
	 */
	public JRFont getXAxisTickLabelFont()
	{
		return xAxisTickLabelFont;
	}
	
	/**
	 * 
	 */
	public Color getXAxisTickLabelColor()
	{
		return JRStyleResolver.getXAxisTickLabelColor(this, this);
	}

	/**
	 * 
	 */
	public Color getOwnXAxisTickLabelColor()
	{
		return xAxisTickLabelColor;
	}

	/**
	 * 
	 */
	public String getXAxisTickLabelMask()
	{
		return xAxisTickLabelMask;
	}

	/**
	 * 
	 */
	public Boolean getXAxisVerticalTickLabels()
	{
		return xAxisVerticalTickLabels;
	}

	/**
	 * 
	 */
	public Color getXAxisLineColor()
	{
		return JRStyleResolver.getXAxisLineColor(this, this);
	}

	/**
	 * 
	 */
	public Color getOwnXAxisLineColor()
	{
		return xAxisLineColor;
	}

	/**
	 * 
	 */
	public JRExpression getYAxisLabelExpression(){
		return yAxisLabelExpression;
	}
	
	/**
	 * 
	 */
	public JRFont getYAxisLabelFont()
	{
		return yAxisLabelFont;
	}
	
	/**
	 * 
	 */
	public Color getYAxisLabelColor()
	{
		return JRStyleResolver.getYAxisLabelColor(this, this);
	}
	
	/**
	 * 
	 */
	public Color getOwnYAxisLabelColor()
	{
		return yAxisLabelColor;
	}
	
	/**
	 * 
	 */
	public JRFont getYAxisTickLabelFont()
	{
		return yAxisTickLabelFont;
	}
	
	/**
	 * 
	 */
	public Color getYAxisTickLabelColor()
	{
		return JRStyleResolver.getYAxisTickLabelColor(this, this);
	}
	
	/**
	 * 
	 */
	public Color getOwnYAxisTickLabelColor()
	{
		return yAxisTickLabelColor;
	}
	
	/**
	 * 
	 */
	public String getYAxisTickLabelMask()
	{
		return yAxisTickLabelMask;
	}

	/**
	 * 
	 */
	public Boolean getYAxisVerticalTickLabels()
	{
		return yAxisVerticalTickLabels;
	}

	/**
	 * 
	 */
	public Color getYAxisLineColor()
	{
		return JRStyleResolver.getYAxisLineColor(this, this);
	}
	
	/**
	 * 
	 */
	public Color getOwnYAxisLineColor()
	{
		return yAxisLineColor;
	}
	
	/**
	 * @deprecated Replaced by {@link #getScaleTypeValue()}
	 */
	public int getScaleType(){
		return getScaleTypeValue() == null ? ScaleTypeEnum.ON_RANGE_AXIS.getValue() : getScaleTypeValue().getValue();
	}
	
	/**
	 * @deprecated Replaced by {@link #getScaleTypeValue()}
	 */
	public Integer getScaleTypeInteger(){
		return new Integer(getScaleTypeValue().getValue());
	}
	
	/**
	 * 
	 */
	public ScaleTypeEnum getScaleTypeValue(){
		return scaleTypeValue;
	}
	
	/**
	 * 
	 */
	public JRExpression getDomainAxisMinValueExpression(){
		return domainAxisMinValueExpression;
	}

	/**
	 * 
	 */
	public JRExpression getDomainAxisMaxValueExpression(){
		return domainAxisMaxValueExpression;
	}

	/**
	 * 
	 */
	public JRExpression getRangeAxisMinValueExpression(){
		return rangeAxisMinValueExpression;
	}

	/**
	 * 
	 */
	public JRExpression getRangeAxisMaxValueExpression(){
		return rangeAxisMaxValueExpression;
	}

	/**
	 * @deprecated Replaced by {@link #setScaleType(ScaleTypeEnum)}
	 */
	public void setScaleType( int scaleType ){
		setScaleType(ScaleTypeEnum.getByValue((byte)scaleType));
	}

	/**
	 * @deprecated Replaced by {@link #setScaleType(ScaleTypeEnum)}
	 */
	public void setScaleType( Integer scaleType ){
		setScaleType(ScaleTypeEnum.getByValue(scaleType));
	}

	/**
	 * 
	 */
	public void setScaleType( ScaleTypeEnum scaleTypeValue ){
		ScaleTypeEnum old = this.scaleTypeValue;
		this.scaleTypeValue = scaleTypeValue;
		getEventSupport().firePropertyChange(PROPERTY_SCALE_TYPE, old, this.scaleTypeValue);
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
		JRBaseBubblePlot clone = (JRBaseBubblePlot)super.clone(parentChart);
		if (xAxisLabelExpression != null)
		{
			clone.xAxisLabelExpression = (JRExpression)xAxisLabelExpression.clone();
		}
		if (yAxisLabelExpression != null)
		{
			clone.yAxisLabelExpression = (JRExpression)yAxisLabelExpression.clone();
		}
		if (domainAxisMinValueExpression != null)
		{
			clone.domainAxisMinValueExpression = (JRExpression)domainAxisMinValueExpression.clone();
		}
		if (domainAxisMaxValueExpression != null)
		{
			clone.domainAxisMaxValueExpression = (JRExpression)domainAxisMaxValueExpression.clone();
		}
		if (rangeAxisMinValueExpression != null)
		{
			clone.rangeAxisMinValueExpression = (JRExpression)rangeAxisMinValueExpression.clone();
		}
		if (rangeAxisMaxValueExpression != null)
		{
			clone.rangeAxisMaxValueExpression = (JRExpression)rangeAxisMaxValueExpression.clone();
		}
		return clone;
	}
	
	/**
	 * This field is only for serialization backward compatibility.
	 */
	private int PSEUDO_SERIAL_VERSION_UID = JRConstants.PSEUDO_SERIAL_VERSION_UID;
	private int scaleType = XYBubbleRenderer.SCALE_ON_RANGE_AXIS;
	private Integer scaleTypeInteger = null;
	
	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException
	{
		in.defaultReadObject();
		
		if (PSEUDO_SERIAL_VERSION_UID < JRConstants.PSEUDO_SERIAL_VERSION_UID_3_1_0)
		{
			scaleTypeValue = ScaleTypeEnum.getByValue(scaleType);
		}
		else if (PSEUDO_SERIAL_VERSION_UID < JRConstants.PSEUDO_SERIAL_VERSION_UID_3_7_2)
		{
			scaleTypeValue = ScaleTypeEnum.getByValue(scaleTypeInteger);
			scaleTypeInteger = null;
		}
	}
	
}
