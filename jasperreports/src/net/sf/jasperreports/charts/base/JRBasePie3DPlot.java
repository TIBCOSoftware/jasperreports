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

import net.sf.jasperreports.charts.JRPie3DPlot;
import net.sf.jasperreports.engine.JRChart;
import net.sf.jasperreports.engine.JRChartPlot;
import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRExpressionCollector;
import net.sf.jasperreports.engine.base.JRBaseChartPlot;
import net.sf.jasperreports.engine.base.JRBaseObjectFactory;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JRBasePie3DPlot extends JRBaseChartPlot implements JRPie3DPlot
{


	/**
	 *
	 */
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;
	
	public static final String PROPERTY_CIRCULAR = "circular";
	
	public static final String PROPERTY_DEPTH_FACTOR = "depthFactor";
	
	public static final String PROPERTY_LABEL_FORMAT = "labelFormat";
	public static final String PROPERTY_LEGEND_LABEL_FORMAT = "legendLabelFormat";
	
	protected double depthFactor = DEPTH_FACTOR_DEFAULT;
	protected boolean isCircular = false;
	protected String labelFormat = null;
	protected String legendLabelFormat = null;
	
	/**
	 *
	 */
	public JRBasePie3DPlot(JRChartPlot pie3DPlot, JRChart chart)
	{
		super(pie3DPlot, chart);
	}

	
	/**
	 *
	 */
	public JRBasePie3DPlot(JRPie3DPlot pie3DPlot, JRBaseObjectFactory factory)
	{
		super(pie3DPlot, factory);
		
		depthFactor = pie3DPlot.getDepthFactor();
		isCircular = pie3DPlot.isCircular();
		labelFormat = pie3DPlot.getLabelFormat();
		legendLabelFormat = pie3DPlot.getLegendLabelFormat();
	}

	
	/**
	 *
	 */
	public double getDepthFactor()
	{
		return depthFactor;
	}
	
	/**
	 *
	 */
	public void setDepthFactor(double depthFactor)
	{
		double old = this.depthFactor;
		this.depthFactor = depthFactor;
		getEventSupport().firePropertyChange(PROPERTY_DEPTH_FACTOR, old, this.depthFactor);
	}
	
	/**
	 *
	 */
	public void collectExpressions(JRExpressionCollector collector)
	{
	}


	/**
	 * @return the isCircular
	 */
	public boolean isCircular() {
		return isCircular;
	}


	/**
	 * @param isCircular the isCircular to set
	 */
	public void setCircular(boolean isCircular) {
		boolean old = this.isCircular;
		this.isCircular = isCircular;
		getEventSupport().firePropertyChange(PROPERTY_CIRCULAR, old, this.isCircular);
	}

	
	/**
	 * @return the labelFormat
	 */
	public String getLabelFormat() {
		return labelFormat;
	}


	/**
	 * @param labelFormat the labelFormat to set
	 */
	public void setLabelFormat(String labelFormat) {
		String old = this.labelFormat;
		this.labelFormat = labelFormat;
		getEventSupport().firePropertyChange(PROPERTY_LABEL_FORMAT, old, this.labelFormat);
	}

	
	/**
	 * @return the legendLabelFormat
	 */
	public String getLegendLabelFormat() {
		return legendLabelFormat;
	}


	/**
	 * @param legendLabelFormat the legendLabelFormat to set
	 */
	public void setLegendLabelFormat(String legendLabelFormat) {
		String old = this.legendLabelFormat;
		this.legendLabelFormat = legendLabelFormat;
		getEventSupport().firePropertyChange(PROPERTY_LEGEND_LABEL_FORMAT, old, this.legendLabelFormat);
	}

	
}
