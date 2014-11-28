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
package net.sf.jasperreports.engine.xml;

import java.util.Set;

import net.sf.jasperreports.engine.JRElementDataset;
import net.sf.jasperreports.engine.design.JRDesignElementDataset;
import net.sf.jasperreports.engine.design.JRDesignGroup;
import net.sf.jasperreports.engine.type.IncrementTypeEnum;
import net.sf.jasperreports.engine.type.ResetTypeEnum;

import org.xml.sax.Attributes;


/**
 * @author Ionut Nedelcu (ionutned@users.sourceforge.net)
 */
public class JRElementDatasetFactory extends JRBaseFactory
{

	
	public Object createObject(Attributes atts)
	{
		JRDesignElementDataset dataset = getDataset();

		setDatasetAtts(atts, dataset);

		return dataset;
	}

	protected JRDesignElementDataset getDataset()
	{
		return (JRDesignElementDataset) digester.peek();
	}

	protected void setDatasetAtts(Attributes atts, JRDesignElementDataset dataset)
	{
		JRXmlLoader xmlLoader = (JRXmlLoader)digester.peek(digester.getCount() - 1);
		Set<JRElementDataset> groupBoundDatasets = xmlLoader.getGroupBoundDatasets();
		
		ResetTypeEnum resetType = ResetTypeEnum.getByName(atts.getValue(JRXmlConstants.ATTRIBUTE_resetType));
		if (resetType != null)
		{
			dataset.setResetType(resetType);
		}
		if (dataset.getResetTypeValue() == ResetTypeEnum.GROUP)
		{
			groupBoundDatasets.add(dataset);

			String groupName = atts.getValue(JRXmlConstants.ATTRIBUTE_resetGroup);
			if (groupName != null)
			{
				JRDesignGroup group = new JRDesignGroup();
				group.setName(groupName);
				dataset.setResetGroup(group);
			}
		}

		IncrementTypeEnum incrementType = IncrementTypeEnum.getByName(atts.getValue(JRXmlConstants.ATTRIBUTE_incrementType));
		if (incrementType != null)
		{
			dataset.setIncrementType(incrementType);
		}
		if (dataset.getIncrementTypeValue() == IncrementTypeEnum.GROUP)
		{
			groupBoundDatasets.add(dataset);

			String groupName = atts.getValue(JRXmlConstants.ATTRIBUTE_incrementGroup);
			if (groupName != null)
			{
				JRDesignGroup group = new JRDesignGroup();
				group.setName(groupName);
				dataset.setIncrementGroup(group);
			}
		}
	}

}
