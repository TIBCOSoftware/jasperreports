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
package net.sf.jasperreports.charts.design;

import net.sf.jasperreports.charts.JRValueDataset;
import net.sf.jasperreports.engine.JRChartDataset;
import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRExpressionCollector;
import net.sf.jasperreports.engine.design.JRDesignChartDataset;
import net.sf.jasperreports.engine.design.JRVerifier;
import net.sf.jasperreports.engine.type.ResetTypeEnum;
import net.sf.jasperreports.engine.util.JRCloneUtils;


/**
 * A data set that contains a single value.  A value
 * dataset is suitable for using with charts that show a single value against
 * a potential range, such as meter chart or a thermometer chart.
 *
 * @author Barry Klawans (bklawans@users.sourceforge.net)
 */
public class JRDesignValueDataset extends JRDesignChartDataset implements JRValueDataset
{


	/**
	 *
	 */
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;
	
	public static final String PROPERTY_VALUE_EXPRESSION = "valueExpression";

	/**
	 * The expression that returns the single value contained in this dataset.
	 */
	protected JRExpression valueExpression;


	/**
	 * Construct a new dataset that is a copy of an existing one.
	 *
	 * @param dataset the dataset to copy
	 */
	public JRDesignValueDataset(JRChartDataset dataset)
	{
		super(dataset);
		
		if (dataset == null)
		{
			// value datasets hold a single value hence a reset type other
			// than None doesn't make sense
			// setting None as default reset type so that it doesn't need to be
			// explicitly set in order for the dataset to work as expected
			this.resetTypeValue = ResetTypeEnum.NONE;
		}
	}

	/**
	 *
	 */
	public JRExpression getValueExpression()
	{
		return valueExpression;
	}


	/**
	 * Sets the expression that indicates the value held by this dataset.
	 *
	 * @param valueExpression the expression that returns the value held by
	 * 						  this dataset
	 */
	public void setValueExpression(JRExpression valueExpression)
	{
		Object old = this.valueExpression;
		this.valueExpression = valueExpression;
		getEventSupport().firePropertyChange(PROPERTY_VALUE_EXPRESSION, old, this.valueExpression);
	}



	/**
	 * Returns the type of this dataset.
	 *
	 * @return the type of this dataset - always
	 *    {@link net.sf.jasperreports.engine.JRChartDataset#VALUE_DATASET VALUE_DATASET}
	 */
	public byte getDatasetType() {
		return JRChartDataset.VALUE_DATASET;
	}

	/**
	 * Adds all the expression used by this plot with the specified collector.
	 * All collected expression that are also registered with a factory will
	 * be included with the report is compiled.
	 *
	 * @param collector the expression collector to use
	 */
	public void collectExpressions(JRExpressionCollector collector)
	{
		collector.collect(this);
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
		JRDesignValueDataset clone = (JRDesignValueDataset)super.clone();
		clone.valueExpression = JRCloneUtils.nullSafeClone(valueExpression);
		return clone;
	}
}
