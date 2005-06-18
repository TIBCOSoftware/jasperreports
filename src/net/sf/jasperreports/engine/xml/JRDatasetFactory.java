/*
 * ============================================================================
 *                   GNU Lesser General Public License
 * ============================================================================
 *
 * JasperReports - Free Java report-generating library.
 * Copyright (C) 2001-2005 JasperSoft Corporation http://www.jaspersoft.com
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
 * 185, Berry Street, Suite 6200
 * San Francisco CA 94107
 * http://www.jaspersoft.com
 */
package net.sf.jasperreports.engine.xml;

import java.util.Set;

import net.sf.jasperreports.engine.JRVariable;
import net.sf.jasperreports.engine.design.JRDesignDataset;
import net.sf.jasperreports.engine.design.JRDesignGroup;

import org.xml.sax.Attributes;


/**
 * @author Ionut Nedelcu (ionutned@users.sourceforge.net)
 * @version $Id$
 */
public class JRDatasetFactory extends JRBaseFactory
{

	private static final String ATTRIBUTE_resetType = "resetType";
	private static final String ATTRIBUTE_resetGroup = "resetGroup";
	private static final String ATTRIBUTE_incrementType = "incrementType";
	private static final String ATTRIBUTE_incrementGroup = "incrementGroup";


	/**
	 *
	 */
	public Object createObject(Attributes atts)
	{
		JRXmlLoader xmlLoader = (JRXmlLoader)digester.peek(digester.getCount() - 1);
		Set groupBoundDatasets = xmlLoader.getGroupBoundDatasets();

		JRDesignDataset dataset = (JRDesignDataset) digester.peek();

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

		return dataset;
	}

}
