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

import java.io.IOException;
import java.io.ObjectInputStream;

import net.sf.jasperreports.charts.JRItemLabel;
import net.sf.jasperreports.charts.JRPie3DPlot;
import net.sf.jasperreports.engine.JRChart;
import net.sf.jasperreports.engine.JRChartPlot;
import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRExpressionCollector;
import net.sf.jasperreports.engine.base.JRBaseChartPlot;
import net.sf.jasperreports.engine.base.JRBaseObjectFactory;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public class JRBasePie3DPlot extends JRBaseChartPlot implements JRPie3DPlot
{


	/**
	 *
	 */
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;
	
	public static final String PROPERTY_CIRCULAR = "isCircular";
	
	public static final String PROPERTY_DEPTH_FACTOR = "depthFactor";
	
	public static final String PROPERTY_LABEL_FORMAT = "labelFormat";
	public static final String PROPERTY_LEGEND_LABEL_FORMAT = "legendLabelFormat";
	public static final String PROPERTY_ITEM_LABEL = "itemLabel";
	public static final String PROPERTY_SHOW_LABELS = "isShowLabels";
	
	protected Double depthFactorDouble;
	protected Boolean circular;
	protected String labelFormat;
	protected String legendLabelFormat;
	
	protected JRItemLabel itemLabel;
	protected Boolean showLabels;

	/**
	 *
	 */
	public JRBasePie3DPlot(JRChartPlot chartPlot, JRChart chart)
	{
		super(chartPlot, chart);
		
		JRPie3DPlot pie3DPlot = chartPlot instanceof JRPie3DPlot ? (JRPie3DPlot)chartPlot : null;
		
		if (pie3DPlot == null)
		{
			itemLabel = new JRBaseItemLabel(null, chart);
		}
		else
		{
			itemLabel = new JRBaseItemLabel(pie3DPlot.getItemLabel(), chart);
		}
	}

	
	/**
	 *
	 */
	public JRBasePie3DPlot(JRPie3DPlot pie3DPlot, JRBaseObjectFactory factory)
	{
		super(pie3DPlot, factory);
		
		depthFactorDouble = pie3DPlot.getDepthFactorDouble();
		circular = pie3DPlot.getCircular();
		labelFormat = pie3DPlot.getLabelFormat();
		legendLabelFormat = pie3DPlot.getLegendLabelFormat();
		itemLabel = new JRBaseItemLabel(pie3DPlot.getItemLabel(), factory);
		showLabels = pie3DPlot.getShowLabels();
	}

	
	/**
	 * 
	 */
	public Double getDepthFactorDouble()
	{
		return depthFactorDouble;
	}
	
	/**
	 *
	 */
	public JRItemLabel getItemLabel()
	{
		return itemLabel;
	}
	
	/**
	 *
	 */
	public void setDepthFactor(Double depthFactor)
	{
		Double old = this.depthFactorDouble;
		this.depthFactorDouble = depthFactor;
		getEventSupport().firePropertyChange(PROPERTY_DEPTH_FACTOR, old, this.depthFactorDouble);
	}
	
	/**
	 *
	 */
	public void collectExpressions(JRExpressionCollector collector)
	{
	}


	/**
	 * 
	 */
	public Boolean getCircular() {
		return circular;
	}

	/**
	 * @param isCircular the isCircular to set
	 */
	public void setCircular(Boolean isCircular) {
		Boolean old = this.circular;
		this.circular = isCircular;
		getEventSupport().firePropertyChange(PROPERTY_CIRCULAR, old, this.circular);
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

	/**
	 * @param itemLabel the itemLabel to set
	 */
	public void setItemLabel(JRItemLabel itemLabel) {
		JRItemLabel old = this.itemLabel;
		this.itemLabel = itemLabel;
		getEventSupport().firePropertyChange(PROPERTY_ITEM_LABEL, old, this.itemLabel);
	}

	/**
	 *
	 */
	public Boolean getShowLabels(){
		return showLabels;
	}

	/**
	 *
	 */
	public void setShowLabels( Boolean showLabels ){
		Boolean old = this.showLabels;
		this.showLabels = showLabels;
		getEventSupport().firePropertyChange(PROPERTY_SHOW_LABELS, old, this.showLabels);
	}
	
	public Object clone(JRChart parentChart) 
	{
		JRBasePie3DPlot clone = (JRBasePie3DPlot) super.clone(parentChart);
		clone.itemLabel = itemLabel == null ? null : itemLabel.clone(parentChart);
		return clone;
	}

	/*
	 * These fields are only for serialization backward compatibility.
	 */
	private int PSEUDO_SERIAL_VERSION_UID = JRConstants.PSEUDO_SERIAL_VERSION_UID; //NOPMD
	/**
	 * @deprecated
	 */
	private double depthFactor = DEPTH_FACTOR_DEFAULT;
	/**
	 * @deprecated
	 */
	private boolean isCircular;
	
	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException
	{
		in.defaultReadObject();
		
		if (PSEUDO_SERIAL_VERSION_UID < JRConstants.PSEUDO_SERIAL_VERSION_UID_3_1_3)
		{
			depthFactorDouble = new Double(depthFactor);
			circular = Boolean.valueOf(isCircular);
		}
	}
	
}
