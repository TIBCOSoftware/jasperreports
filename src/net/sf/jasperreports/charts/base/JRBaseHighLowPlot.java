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
import net.sf.jasperreports.engine.base.JRBaseFont;
import net.sf.jasperreports.engine.base.JRBaseObjectFactory;
import net.sf.jasperreports.engine.util.JRStyleResolver;


/**
 * @author Ionut Nedelcu (ionutned@users.sourceforge.net)
 * @version $Id$
 */
public class JRBaseHighLowPlot extends JRBaseChartPlot implements JRHighLowPlot
{

	/**
	 *
	 */
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;
	
	public static final String PROPERTY_SHOW_CLOSE_TICKS = "showCloseTicks";
	
	public static final String PROPERTY_SHOW_OPEN_TICKS = "showOpenTicks";

	protected JRExpression timeAxisLabelExpression = null;
	protected JRFont timeAxisLabelFont = null;
	protected Color timeAxisLabelColor = null;
	protected JRFont timeAxisTickLabelFont = null;
	protected Color timeAxisTickLabelColor = null;
	protected String timeAxisTickLabelMask = null;
	protected Color timeAxisLineColor = null;

	protected JRExpression valueAxisLabelExpression = null;
	protected JRFont valueAxisLabelFont = null;
	protected Color valueAxisLabelColor = null;
	protected JRFont valueAxisTickLabelFont = null;
	protected Color valueAxisTickLabelColor = null;
	protected String valueAxisTickLabelMask = null;
	protected Color valueAxisLineColor = null;

	protected Boolean showOpenTicks = null;
	protected Boolean showCloseTicks = null;


	/**
	 *
	 */
	public JRBaseHighLowPlot(JRChartPlot highLowPlot, JRChart chart)
	{
		super(highLowPlot, chart);
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
		timeAxisLabelFont = new JRBaseFont(null, null, highLowPlot.getChart(), highLowPlot.getTimeAxisLabelFont());
		timeAxisLabelColor = highLowPlot.getOwnTimeAxisLabelColor();
		timeAxisTickLabelFont = new JRBaseFont(null, null, highLowPlot.getChart(), highLowPlot.getTimeAxisTickLabelFont());
		timeAxisTickLabelColor = highLowPlot.getOwnTimeAxisTickLabelColor();
		timeAxisTickLabelMask = highLowPlot.getTimeAxisTickLabelMask();
		timeAxisLineColor = highLowPlot.getTimeAxisLineColor();
		
		valueAxisLabelExpression = factory.getExpression( highLowPlot.getValueAxisLabelExpression() );
		valueAxisLabelFont = new JRBaseFont(null, null, highLowPlot.getChart(), highLowPlot.getValueAxisLabelFont());
		valueAxisLabelColor = highLowPlot.getOwnValueAxisLabelColor();
		valueAxisTickLabelFont = new JRBaseFont(null, null, highLowPlot.getChart(), highLowPlot.getValueAxisTickLabelFont());
		valueAxisTickLabelColor = highLowPlot.getOwnValueAxisTickLabelColor();
		valueAxisTickLabelMask = highLowPlot.getValueAxisTickLabelMask();
		valueAxisLineColor = highLowPlot.getValueAxisTickLabelColor();
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
	 * @deprecated Replaced by {@link #getShowOpenTicks()}
	 */
	public boolean isShowOpenTicks()
	{
		return showOpenTicks == null ? false : showOpenTicks.booleanValue();
	}

	/**
	 * 
	 */
	public Boolean getShowOpenTicks()
	{
		return showOpenTicks;
	}


	/**
	 * @deprecated Replaced by {@link #setShowOpenTicks(Boolean)}
	 */
	public void setShowOpenTicks(boolean showOpenTicks)
	{
		setShowOpenTicks(Boolean.valueOf(showOpenTicks));
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
	 * @deprecated Replaced by {@link #getShowCloseTicks()}
	 */
	public boolean isShowCloseTicks()
	{
		return showCloseTicks == null ? false : showCloseTicks.booleanValue();
	}

	/**
	 * 
	 */
	public Boolean getShowCloseTicks()
	{
		return showCloseTicks;
	}

	/**
	 * @deprecated Replaced by {@link #setShowCloseTicks(Boolean)}
	 */
	public void setShowCloseTicks(boolean showCloseTicks)
	{
		setShowCloseTicks(Boolean.valueOf(showCloseTicks));
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
		if (timeAxisLabelExpression != null)
		{
			clone.timeAxisLabelExpression = (JRExpression)timeAxisLabelExpression.clone();
		}
		if (valueAxisLabelExpression != null)
		{
			clone.valueAxisLabelExpression = (JRExpression)valueAxisLabelExpression.clone();
		}
		return clone;
	}
	
	/**
	 * This field is only for serialization backward compatibility.
	 */
	private int PSEUDO_SERIAL_VERSION_UID = JRConstants.PSEUDO_SERIAL_VERSION_UID_3_1_0;
	private boolean isShowOpenTicks = false;
	private boolean isShowCloseTicks = false;
	
	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException
	{
		in.defaultReadObject();
		
		if (PSEUDO_SERIAL_VERSION_UID < JRConstants.PSEUDO_SERIAL_VERSION_UID_3_1_0)
		{
			showOpenTicks = Boolean.valueOf(isShowOpenTicks);
			showCloseTicks = Boolean.valueOf(isShowCloseTicks);
		}
	}
	
}
