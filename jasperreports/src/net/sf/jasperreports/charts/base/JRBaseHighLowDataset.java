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

import net.sf.jasperreports.charts.JRHighLowDataset;
import net.sf.jasperreports.engine.JRChartDataset;
import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRExpressionCollector;
import net.sf.jasperreports.engine.JRHyperlink;
import net.sf.jasperreports.engine.base.JRBaseChartDataset;
import net.sf.jasperreports.engine.base.JRBaseObjectFactory;
import net.sf.jasperreports.engine.design.JRVerifier;
import net.sf.jasperreports.engine.util.JRCloneUtils;


/**
 * @author Ionut Nedelcu (ionutned@users.sourceforge.net)
 */
public class JRBaseHighLowDataset extends JRBaseChartDataset implements JRHighLowDataset
{

	/**
	 *
	 */
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

	protected JRExpression seriesExpression;
	protected JRExpression dateExpression;
	protected JRExpression highExpression;
	protected JRExpression lowExpression;
	protected JRExpression openExpression;
	protected JRExpression closeExpression;
	protected JRExpression volumeExpression;
	private JRHyperlink itemHyperlink;


	/**
	 *
	 */
	public JRBaseHighLowDataset(JRChartDataset dataset)
	{
		super(dataset);
	}


	/**
	 *
	 */
	public JRBaseHighLowDataset(JRHighLowDataset dataset, JRBaseObjectFactory factory)
	{
		super(dataset, factory);

		seriesExpression = factory.getExpression(dataset.getSeriesExpression());
		dateExpression = factory.getExpression(dataset.getDateExpression());
		highExpression = factory.getExpression(dataset.getHighExpression());
		lowExpression = factory.getExpression(dataset.getLowExpression());
		openExpression = factory.getExpression(dataset.getOpenExpression());
		closeExpression = factory.getExpression(dataset.getCloseExpression());
		volumeExpression = factory.getExpression(dataset.getVolumeExpression());
		itemHyperlink = factory.getHyperlink(dataset.getItemHyperlink());
	}



	public JRExpression getSeriesExpression()
	{
		return seriesExpression;
	}


	public JRExpression getDateExpression()
	{
		return dateExpression;
	}


	public JRExpression getHighExpression()
	{
		return highExpression;
	}


	public JRExpression getLowExpression()
	{
		return lowExpression;
	}


	public JRExpression getOpenExpression()
	{
		return openExpression;
	}


	public JRExpression getCloseExpression()
	{
		return closeExpression;
	}

	public JRExpression getVolumeExpression()
	{
		return volumeExpression;
	}


	/* (non-Javadoc)
	 * @see net.sf.jasperreports.engine.JRChartDataset#getDatasetType()
	 */
	public byte getDatasetType() {
		return JRChartDataset.HIGHLOW_DATASET;
	}


	/**
	 *
	 */
	public void collectExpressions(JRExpressionCollector collector)
	{
		collector.collect(this);
	}

	
	public JRHyperlink getItemHyperlink()
	{
		return itemHyperlink;
	}


	public void validate(JRVerifier verifier)
	{
		verifier.verify(this);
	}

	
	/**
	 * 
	 */
	public Object clone() 
	{
		JRBaseHighLowDataset clone = (JRBaseHighLowDataset)super.clone();
		clone.seriesExpression = JRCloneUtils.nullSafeClone(seriesExpression);
		clone.dateExpression = JRCloneUtils.nullSafeClone(dateExpression);
		clone.highExpression = JRCloneUtils.nullSafeClone(highExpression);
		clone.lowExpression = JRCloneUtils.nullSafeClone(lowExpression);
		clone.openExpression = JRCloneUtils.nullSafeClone(openExpression);
		clone.closeExpression = JRCloneUtils.nullSafeClone(closeExpression);
		clone.volumeExpression = JRCloneUtils.nullSafeClone(volumeExpression);
		clone.itemHyperlink = JRCloneUtils.nullSafeClone(itemHyperlink);
		return clone;
	}
}
