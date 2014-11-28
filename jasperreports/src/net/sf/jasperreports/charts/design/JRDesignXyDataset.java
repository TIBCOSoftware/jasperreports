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

import java.util.ArrayList;
import java.util.List;

import net.sf.jasperreports.charts.JRXyDataset;
import net.sf.jasperreports.charts.JRXySeries;
import net.sf.jasperreports.engine.JRChartDataset;
import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRExpressionCollector;
import net.sf.jasperreports.engine.design.JRDesignChartDataset;
import net.sf.jasperreports.engine.design.JRVerifier;
import net.sf.jasperreports.engine.util.JRCloneUtils;



/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public class JRDesignXyDataset extends JRDesignChartDataset implements JRXyDataset
{


	/**
	 *
	 */
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;
	
	public static final String PROPERTY_XY_SERIES = "xySeries";

	private List<JRXySeries> xySeriesList = new ArrayList<JRXySeries>();


	/**
	 *
	 */
	public JRDesignXyDataset(JRChartDataset dataset)
	{
		super(dataset);
	}


	/**
	 *
	 */
	public JRXySeries[] getSeries()
	{
		JRXySeries[] xySeriesArray = new JRXySeries[xySeriesList.size()];
		
		xySeriesList.toArray(xySeriesArray);

		return xySeriesArray;
	}
	

	/**
	 * 
	 */
	public List<JRXySeries> getSeriesList()
	{
		return xySeriesList;
	}

	
	/**
	 *
	 */
	public void addXySeries(JRXySeries xySeries)
	{
		xySeriesList.add(xySeries);
		getEventSupport().fireCollectionElementAddedEvent(PROPERTY_XY_SERIES, 
				xySeries, xySeriesList.size() - 1);
	}
	
	/**
	 *
	 */
	public void addXySeries(int index, JRXySeries xySeries)
	{
		xySeriesList.add(index, xySeries);
		getEventSupport().fireCollectionElementAddedEvent(PROPERTY_XY_SERIES, 
				xySeries, index);
	}
	

	/**
	 *
	 */
	public JRXySeries removeXySeries(JRXySeries xySeries)
	{
		if (xySeries != null)
		{
			int idx = xySeriesList.indexOf(xySeries);
			if (idx >= 0)
			{
				xySeriesList.remove(idx);
				getEventSupport().fireCollectionElementRemovedEvent(PROPERTY_XY_SERIES, xySeries, idx);
			}
		}
		
		return xySeries;
	}


	/** 
	 * 
	 */
	public byte getDatasetType() {
		return JRChartDataset.XY_DATASET;
	}
	
	
	/**
	 *
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
		JRDesignXyDataset clone = (JRDesignXyDataset)super.clone();
		clone.xySeriesList = JRCloneUtils.cloneList(xySeriesList);
		return clone;
	}
}
