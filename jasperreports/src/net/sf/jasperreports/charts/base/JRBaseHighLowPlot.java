/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2014 TIBCO Software Inc. All rights reserved.
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

import net.sf.jasperreports.charts.JRHighLowPlot;
import net.sf.jasperreports.engine.JRChart;
import net.sf.jasperreports.engine.JRChartPlot;
import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRExpressionCollector;
import net.sf.jasperreports.engine.JRFont;
import net.sf.jasperreports.engine.base.JRBaseChartPlot;
import net.sf.jasperreports.engine.base.JRBaseObjectFactory;
import net.sf.jasperreports.engine.util.JRCloneUtils;
import net.sf.jasperreports.engine.util.JRStyleResolver;


/**
 * @author Ionut Nedelcu (ionutned@users.sourceforge.net)
 */
public class JRBaseHighLowPlot extends JRBaseChartPlot implements JRHighLowPlot
{

	/**
	 *
	 */
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;
	
	public static final String PROPERTY_SHOW_CLOSE_TICKS = "isShowCloseTicks";
	
	public static final String PROPERTY_SHOW_OPEN_TICKS = "isShowOpenTicks";

	protected JRExpression timeAxisLabelExpression;
	protected JRFont timeAxisLabelFont;
	protected Color timeAxisLabelColor;
	protected JRFont timeAxisTickLabelFont;
	protected Color timeAxisTickLabelColor;
	protected String timeAxisTickLabelMask;
	protected Boolean timeAxisVerticalTickLabels;
	protected Color timeAxisLineColor;

	protected JRExpression valueAxisLabelExpression;
	protected JRExpression rangeAxisMinValueExpression;
	protected JRExpression rangeAxisMaxValueExpression;
	protected JRExpression domainAxisMinValueExpression;
	protected JRExpression domainAxisMaxValueExpression;
	protected JRFont valueAxisLabelFont;
	protected Color valueAxisLabelColor;
	protected JRFont valueAxisTickLabelFont;
	protected Color valueAxisTickLabelColor;
	protected String valueAxisTickLabelMask;
	protected Boolean valueAxisVerticalTickLabels;
	protected Color valueAxisLineColor;

	protected Boolean showOpenTicks;
	protected Boolean showCloseTicks;


	/**
	 *
	 */
	public JRBaseHighLowPlot(JRChartPlot plot, JRChart chart)
	{
		super(plot, chart);
		
		JRHighLowPlot highLowPlot = plot instanceof JRHighLowPlot ? (JRHighLowPlot)plot : null;
		if (highLowPlot != null)
		{
			timeAxisLabelFont = highLowPlot.getTimeAxisLabelFont();
			timeAxisTickLabelFont = highLowPlot.getTimeAxisTickLabelFont();
			valueAxisLabelFont = highLowPlot.getValueAxisLabelFont();
			valueAxisTickLabelFont = highLowPlot.getValueAxisTickLabelFont();
		}
	}


	/**
	 *
	 */
	public JRBaseHighLowPlot(JRHighLowPlot highLowPlot, JRBaseObjectFactory factory)
	{
		super(highLowPlot, factory);

		showOpenTicks = highLowPlot.getShowOpenTicks();
		showCloseTicks = highLowPlot.getShowCloseTicks();

		timeAxisLabelExpression = factory.getExpression( highLowPlot.getTimeAxisLabelExpression() );
		timeAxisLabelFont = factory.getFont(chart, highLowPlot.getTimeAxisLabelFont());
		timeAxisLabelColor = highLowPlot.getOwnTimeAxisLabelColor();
		timeAxisTickLabelFont = factory.getFont(chart, highLowPlot.getTimeAxisTickLabelFont());
		timeAxisTickLabelColor = highLowPlot.getOwnTimeAxisTickLabelColor();
		timeAxisTickLabelMask = highLowPlot.getTimeAxisTickLabelMask();
		timeAxisVerticalTickLabels = highLowPlot.getTimeAxisVerticalTickLabels();
		timeAxisLineColor = highLowPlot.getOwnTimeAxisLineColor();
		
		valueAxisLabelExpression = factory.getExpression( highLowPlot.getValueAxisLabelExpression() );
		domainAxisMinValueExpression = factory.getExpression( highLowPlot.getDomainAxisMinValueExpression() );
		domainAxisMaxValueExpression = factory.getExpression( highLowPlot.getDomainAxisMaxValueExpression() );
		rangeAxisMinValueExpression = factory.getExpression( highLowPlot.getRangeAxisMinValueExpression() );
		rangeAxisMaxValueExpression = factory.getExpression( highLowPlot.getRangeAxisMaxValueExpression() );
		valueAxisLabelFont = factory.getFont(chart, highLowPlot.getValueAxisLabelFont());
		valueAxisLabelColor = highLowPlot.getOwnValueAxisLabelColor();
		valueAxisTickLabelFont = factory.getFont(chart, highLowPlot.getValueAxisTickLabelFont());
		valueAxisTickLabelColor = highLowPlot.getOwnValueAxisTickLabelColor();
		valueAxisTickLabelMask = highLowPlot.getValueAxisTickLabelMask();
		valueAxisVerticalTickLabels = highLowPlot.getValueAxisVerticalTickLabels();
		valueAxisLineColor = highLowPlot.getOwnValueAxisTickLabelColor();
	}


	/**
	 * 
	 */
	public JRExpression getTimeAxisLabelExpression(){
		return timeAxisLabelExpression;
	}
	
	/**
	 * 
	 */
	public JRFont getTimeAxisLabelFont()
	{
		return timeAxisLabelFont;
	}
	
	/**
	 * 
	 */
	public Color getTimeAxisLabelColor()
	{
		return JRStyleResolver.getTimeAxisLabelColor(this, this);
	}
	
	/**
	 * 
	 */
	public Color getOwnTimeAxisLabelColor()
	{
		return timeAxisLabelColor;
	}
	
	/**
	 * 
	 */
	public JRFont getTimeAxisTickLabelFont()
	{
		return timeAxisTickLabelFont;
	}
	
	/**
	 * 
	 */
	public Color getTimeAxisTickLabelColor()
	{
		return JRStyleResolver.getTimeAxisTickLabelColor(this, this);
	}

	/**
	 * 
	 */
	public Color getOwnTimeAxisTickLabelColor()
	{
		return timeAxisTickLabelColor;
	}

	/**
	 * 
	 */
	public String getTimeAxisTickLabelMask()
	{
		return timeAxisTickLabelMask;
	}

	/**
	 * 
	 */
	public Boolean getTimeAxisVerticalTickLabels()
	{
		return timeAxisVerticalTickLabels;
	}

	/**
	 * 
	 */
	public Color getTimeAxisLineColor()
	{
		return JRStyleResolver.getTimeAxisLineColor(this, this);
	}

	/**
	 * 
	 */
	public Color getOwnTimeAxisLineColor()
	{
		return timeAxisLineColor;
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
	public Boolean getValueAxisVerticalTickLabels()
	{
		return valueAxisVerticalTickLabels;
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
	public Boolean getShowOpenTicks()
	{
		return showOpenTicks;
	}


	/**
	 * 
	 */
	public void setShowOpenTicks(Boolean showOpenTicks)
	{
		Boolean old = this.showOpenTicks;
		this.showOpenTicks = showOpenTicks;
		getEventSupport().firePropertyChange(PROPERTY_SHOW_OPEN_TICKS, old, this.showOpenTicks);
	}

	/**
	 * 
	 */
	public Boolean getShowCloseTicks()
	{
		return showCloseTicks;
	}

	/**
	 * 
	 */
	public void setShowCloseTicks(Boolean showCloseTicks)
	{
		Boolean old = this.showCloseTicks;
		this.showCloseTicks = showCloseTicks;
		getEventSupport().firePropertyChange(PROPERTY_SHOW_CLOSE_TICKS, old, this.showCloseTicks);
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
		JRBaseHighLowPlot clone = (JRBaseHighLowPlot)super.clone(parentChart);
		clone.timeAxisLabelExpression = JRCloneUtils.nullSafeClone(timeAxisLabelExpression);
		clone.valueAxisLabelExpression = JRCloneUtils.nullSafeClone(valueAxisLabelExpression);
		clone.domainAxisMinValueExpression = JRCloneUtils.nullSafeClone(domainAxisMinValueExpression);
		clone.domainAxisMaxValueExpression = JRCloneUtils.nullSafeClone(domainAxisMaxValueExpression);
		clone.rangeAxisMinValueExpression = JRCloneUtils.nullSafeClone(rangeAxisMinValueExpression);
		clone.rangeAxisMaxValueExpression = JRCloneUtils.nullSafeClone(rangeAxisMaxValueExpression);
		return clone;
	}
	
	/*
	 * These fields are only for serialization backward compatibility.
	 */
	private int PSEUDO_SERIAL_VERSION_UID = JRConstants.PSEUDO_SERIAL_VERSION_UID; //NOPMD
	/**
	 * @deprecated
	 */
	private boolean isShowOpenTicks;
	/**
	 * @deprecated
	 */
	private boolean isShowCloseTicks;
	
	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException
	{
		in.defaultReadObject();
		
		if (PSEUDO_SERIAL_VERSION_UID < JRConstants.PSEUDO_SERIAL_VERSION_UID_3_1_3)
		{
			showOpenTicks = Boolean.valueOf(isShowOpenTicks);
			showCloseTicks = Boolean.valueOf(isShowCloseTicks);
		}
	}
	
}
