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

import net.sf.jasperreports.charts.JRBarPlot;
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
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JRBaseBarPlot extends JRBaseChartPlot implements JRBarPlot
{


	/**
	 *
	 */
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

	public static final String PROPERTY_SHOW_LABELS = "showLabels";

	public static final String PROPERTY_SHOW_TICK_LABELS = "showTickLabels";

	public static final String PROPERTY_SHOW_TICK_MARKS = "showTickMarks";

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

	protected Boolean showTickMarks = null;
	protected Boolean showTickLabels = null;
	protected Boolean showLabels = null;


	/**
	 *
	 */
	public JRBaseBarPlot(JRChartPlot barPlot, JRChart chart)
	{
		super(barPlot, chart);
	}

	/**
	 *
	 */
	public JRBaseBarPlot(JRBarPlot barPlot, JRBaseObjectFactory factory)
	{
		super(barPlot, factory);

		showTickMarks = barPlot.getShowTickMarks();
		showTickLabels = barPlot.getShowTickLabels();
		showLabels = barPlot.getShowLabels();

		categoryAxisLabelExpression = factory.getExpression( barPlot.getCategoryAxisLabelExpression() );
		categoryAxisLabelFont = new JRBaseFont(barPlot.getChart(), barPlot.getCategoryAxisLabelFont());
		categoryAxisLabelColor = barPlot.getOwnCategoryAxisLabelColor();
		categoryAxisTickLabelFont = new JRBaseFont(barPlot.getChart(), barPlot.getCategoryAxisTickLabelFont());
		categoryAxisTickLabelColor = barPlot.getOwnCategoryAxisTickLabelColor();
		categoryAxisTickLabelMask = barPlot.getCategoryAxisTickLabelMask();
		categoryAxisLineColor = barPlot.getOwnCategoryAxisLineColor();

		valueAxisLabelExpression = factory.getExpression( barPlot.getValueAxisLabelExpression() );
		valueAxisLabelFont = new JRBaseFont(barPlot.getChart(), barPlot.getValueAxisLabelFont());
		valueAxisLabelColor = barPlot.getOwnValueAxisLabelColor();
		valueAxisTickLabelFont = new JRBaseFont(barPlot.getChart(), barPlot.getValueAxisTickLabelFont());
		valueAxisTickLabelColor = barPlot.getOwnValueAxisTickLabelColor();
		valueAxisTickLabelMask = barPlot.getValueAxisTickLabelMask();
		valueAxisLineColor = barPlot.getOwnValueAxisLineColor();
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
	 * @deprecated Replaced by {@link #getShowLabels()}
	 */
	public boolean isShowLabels(){
		return showLabels == null ? false : showLabels.booleanValue();
	}

	/**
	 *
	 */
	public Boolean getShowLabels(){
		return showLabels;
	}

	/**
	 * @deprecated Replaced by {@link #setShowLabels(Boolean)}
	 */
	public void setShowLabels( boolean isShowLabels ){
		setShowLabels(Boolean.valueOf(isShowLabels));
	}

	/**
	 *
	 */
	public void setShowLabels( Boolean showLabels ){
		Boolean old = this.showLabels;
		this.showLabels = showLabels;
		getEventSupport().firePropertyChange(PROPERTY_SHOW_LABELS, old, this.showLabels);
	}


	/**
	 * @deprecated Replaced by {@link #getShowTickMarks()}
	 */
	public boolean isShowTickMarks()
	{
		return showTickMarks == null ? true : showTickMarks.booleanValue();
	}

	/**
	 *
	 */
	public Boolean getShowTickMarks()
	{
		return showTickMarks;
	}

	/**
	 * @deprecated Replaced by {@link #setShowTickMarks(Boolean)}
	 */
	public void setShowTickMarks(boolean isShowTickMarks)
	{
		setShowTickMarks(Boolean.valueOf(isShowTickMarks));
	}

	/**
	 *
	 */
	public void setShowTickMarks(Boolean isShowTickMarks)
	{
		Boolean old = this.showTickMarks;
		this.showTickMarks = isShowTickMarks;
		getEventSupport().firePropertyChange(PROPERTY_SHOW_TICK_MARKS, old, this.showTickMarks);
	}

	/**
	 * @deprecated Replaced by {@link #getShowTickLabels()}
	 */
	public boolean isShowTickLabels()
	{
		return showTickLabels == null ? true : showTickLabels.booleanValue();
	}

	/**
	 *
	 */
	public Boolean getShowTickLabels()
	{
		return showTickLabels;
	}

	/**
	 * @deprecated Replaced by {@link #setShowTickLabels(Boolean)}
	 */
	public void setShowTickLabels(boolean isShowTickLabels)
	{
		setShowTickLabels(Boolean.valueOf(isShowTickLabels));
	}

	/**
	 *
	 */
	public void setShowTickLabels(Boolean showTickLabels)
	{
		Boolean old = this.showTickLabels;
		this.showTickLabels = showTickLabels;
		getEventSupport().firePropertyChange(PROPERTY_SHOW_TICK_LABELS, old, this.showTickLabels);
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
		JRBaseBarPlot clone = (JRBaseBarPlot)super.clone(parentChart);
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

	/**
	 * This field is only for serialization backward compatibility.
	 */
	private int PSEUDO_SERIAL_VERSION_UID = JRConstants.PSEUDO_SERIAL_VERSION_UID_3_1_0;
	private boolean isShowTickMarks = false;
	private boolean isShowTickLabels = true;
	private boolean isShowLabels = true;


	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException
	{
		in.defaultReadObject();

		if (PSEUDO_SERIAL_VERSION_UID < JRConstants.PSEUDO_SERIAL_VERSION_UID_3_1_0)
		{
			showTickMarks = Boolean.valueOf(isShowTickMarks);
			showTickLabels = Boolean.valueOf(isShowTickLabels);
			showLabels = Boolean.valueOf(isShowLabels);
		}
	}

}
