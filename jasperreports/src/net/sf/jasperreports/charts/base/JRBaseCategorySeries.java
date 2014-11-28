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

import java.io.Serializable;

import net.sf.jasperreports.charts.JRCategorySeries;
import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRHyperlink;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.base.JRBaseObjectFactory;
import net.sf.jasperreports.engine.util.JRCloneUtils;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public class JRBaseCategorySeries implements JRCategorySeries, Serializable
{


	/**
	 *
	 */
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

	protected JRExpression seriesExpression;
	protected JRExpression categoryExpression;
	protected JRExpression valueExpression;
	protected JRExpression labelExpression;
	protected JRHyperlink itemHyperlink;

	
	/**
	 *
	 */
	protected JRBaseCategorySeries()
	{
	}
	
	
	/**
	 *
	 */
	public JRBaseCategorySeries(JRCategorySeries categorySeries, JRBaseObjectFactory factory)
	{
		factory.put(categorySeries, this);

		seriesExpression = factory.getExpression(categorySeries.getSeriesExpression());
		categoryExpression = factory.getExpression(categorySeries.getCategoryExpression());
		valueExpression = factory.getExpression(categorySeries.getValueExpression());
		labelExpression = factory.getExpression(categorySeries.getLabelExpression());
		itemHyperlink = factory.getHyperlink(categorySeries.getItemHyperlink());
	}

	
	/**
	 *
	 */
	public JRExpression getSeriesExpression()
	{
		return seriesExpression;
	}
		
	/**
	 *
	 */
	public JRExpression getCategoryExpression()
	{
		return categoryExpression;
	}
		
	/**
	 *
	 */
	public JRExpression getValueExpression()
	{
		return valueExpression;
	}
		
	/**
	 *
	 */
	public JRExpression getLabelExpression()
	{
		return labelExpression;
	}

	
	public JRHyperlink getItemHyperlink()
	{
		return itemHyperlink;
	}
		
	/**
	 * 
	 */
	public Object clone() 
	{
		JRBaseCategorySeries clone = null;
		
		try
		{
			clone = (JRBaseCategorySeries)super.clone();
		}
		catch (CloneNotSupportedException e)
		{
			throw new JRRuntimeException(e);
		}
		
		clone.seriesExpression = JRCloneUtils.nullSafeClone(seriesExpression);
		clone.categoryExpression = JRCloneUtils.nullSafeClone(categoryExpression);
		clone.valueExpression = JRCloneUtils.nullSafeClone(valueExpression);
		clone.labelExpression = JRCloneUtils.nullSafeClone(labelExpression);
		clone.itemHyperlink = JRCloneUtils.nullSafeClone(itemHyperlink);
		
		return clone;
	}
}
