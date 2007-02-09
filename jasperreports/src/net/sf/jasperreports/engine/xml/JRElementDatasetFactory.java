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
package net.sf.jasperreports.engine.xml;

import java.util.Set;

import net.sf.jasperreports.engine.JRVariable;
import net.sf.jasperreports.engine.design.JRDesignElementDataset;
import net.sf.jasperreports.engine.design.JRDesignGroup;

import org.xml.sax.Attributes;


/**
 * @author Ionut Nedelcu (ionutned@users.sourceforge.net)
 * @version $Id$
 */
public class JRElementDatasetFactory extends JRBaseFactory
{

	public static final String ELEMENT_dataset = "dataset";
	public static final String ELEMENT_categoryDataset = "categoryDataset";
	public static final String ELEMENT_timeSeriesDataset = "timeSeriesDataset";
	public static final String ELEMENT_timePeriodDataset = "timePeriodDataset";
	public static final String ELEMENT_xyzDataset = "xyzDataset";
	public static final String ELEMENT_xyDataset = "xyDataset";
	public static final String ELEMENT_pieDataset = "pieDataset";
	public static final String ELEMENT_valueDataset = "pieDataset";
	public static final String ELEMENT_highLowDataset = "highLowDataset";
	
	public static final String ELEMENT_categorySeries = "categorySeries";
	public static final String ELEMENT_xyzSeries = "xyzSeries";
	public static final String ELEMENT_xySeries = "xySeries";
	public static final String ELEMENT_timeSeries = "timeSeries";
	public static final String ELEMENT_timePeriodSeries = "timePeriodSeries";
	
	public static final String ELEMENT_incrementWhenExpression = "incrementWhenExpression";
	public static final String ELEMENT_keyExpression = "keyExpression";
	public static final String ELEMENT_valueExpression = "valueExpression";
	public static final String ELEMENT_labelExpression = "labelExpression";
	public static final String ELEMENT_seriesExpression = "seriesExpression";
	public static final String ELEMENT_categoryExpression = "categoryExpression";
	public static final String ELEMENT_xValueExpression = "xValueExpression";
	public static final String ELEMENT_yValueExpression = "yValueExpression";
	public static final String ELEMENT_zValueExpression = "zValueExpression";
	public static final String ELEMENT_timePeriodExpression = "timePeriodExpression";
	public static final String ELEMENT_startDateExpression = "startDateExpression";
	public static final String ELEMENT_endDateExpression = "endDateExpression";
	public static final String ELEMENT_dateExpression = "dateExpression";
	public static final String ELEMENT_highExpression = "highExpression";
	public static final String ELEMENT_lowExpression = "lowExpression";
	public static final String ELEMENT_openExpression = "openExpression";
	public static final String ELEMENT_closeExpression = "closeExpression";
	public static final String ELEMENT_volumeExpression = "volumeExpression";

	public static final String ATTRIBUTE_resetType = "resetType";
	public static final String ATTRIBUTE_resetGroup = "resetGroup";
	public static final String ATTRIBUTE_incrementType = "incrementType";
	public static final String ATTRIBUTE_incrementGroup = "incrementGroup";

	public static final String ATTRIBUTE_timePeriod = "timePeriod";
	
	public Object createObject(Attributes atts)
	{
		JRDesignElementDataset dataset = (JRDesignElementDataset) digester.peek();

		setDatasetAtts(atts, dataset);

		return dataset;
	}

	protected void setDatasetAtts(Attributes atts, JRDesignElementDataset dataset)
	{
		JRXmlLoader xmlLoader = (JRXmlLoader)digester.peek(digester.getCount() - 1);
		Set groupBoundDatasets = xmlLoader.getGroupBoundDatasets();
		
		Byte resetType = (Byte)JRXmlConstants.getResetTypeMap().get(atts.getValue(ATTRIBUTE_resetType));
		if (resetType != null)
		{
			dataset.setResetType(resetType.byteValue());
		}
		if (dataset.getResetType() == JRVariable.RESET_TYPE_GROUP)
		{
			groupBoundDatasets.add(dataset);

			String groupName = atts.getValue(ATTRIBUTE_resetGroup);
			if (groupName != null)
			{
				JRDesignGroup group = new JRDesignGroup();
				group.setName(groupName);
				dataset.setResetGroup(group);
			}
		}

		Byte incrementType = (Byte)JRXmlConstants.getResetTypeMap().get(atts.getValue(ATTRIBUTE_incrementType));
		if (incrementType != null)
		{
			dataset.setIncrementType(incrementType.byteValue());
		}
		if (dataset.getIncrementType() == JRVariable.RESET_TYPE_GROUP)
		{
			groupBoundDatasets.add(dataset);

			String groupName = atts.getValue(ATTRIBUTE_incrementGroup);
			if (groupName != null)
			{
				JRDesignGroup group = new JRDesignGroup();
				group.setName(groupName);
				dataset.setIncrementGroup(group);
			}
		}
	}

}
