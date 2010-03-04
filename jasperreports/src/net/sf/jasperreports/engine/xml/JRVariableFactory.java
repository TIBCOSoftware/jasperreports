/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2009 Jaspersoft Corporation. All rights reserved.
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

import net.sf.jasperreports.engine.design.JRDesignGroup;
import net.sf.jasperreports.engine.design.JRDesignVariable;
import net.sf.jasperreports.engine.type.IncrementTypeEnum;
import net.sf.jasperreports.engine.type.ResetTypeEnum;

import org.xml.sax.Attributes;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JRVariableFactory extends JRBaseFactory
{

	/**
	 *
	 */
	public Object createObject(Attributes atts)
	{
		JRDesignVariable variable = new JRDesignVariable();
		
		variable.setName(atts.getValue(XmlConstants.ATTRIBUTE_name));

		if (atts.getValue(XmlConstants.ATTRIBUTE_class) != null)
		{
			variable.setValueClassName(atts.getValue(XmlConstants.ATTRIBUTE_class));
		}

		ResetTypeEnum resetType = ResetTypeEnum.getByName(atts.getValue(XmlConstants.ATTRIBUTE_resetType));
		if (resetType != null)
		{
			variable.setResetType(resetType);
		}
		
		String groupName = atts.getValue(XmlConstants.ATTRIBUTE_resetGroup);
		if (groupName != null)
		{
			JRDesignGroup group = new JRDesignGroup();
			group.setName(groupName);
			variable.setResetGroup(group);
		}

		IncrementTypeEnum incrementType = IncrementTypeEnum.getByName(atts.getValue(XmlConstants.ATTRIBUTE_incrementType));
		if (incrementType != null)
		{
			variable.setIncrementType(incrementType);
		}
		
		groupName = atts.getValue(XmlConstants.ATTRIBUTE_incrementGroup);
		if (groupName != null)
		{
			JRDesignGroup group = new JRDesignGroup();
			group.setName(groupName);
			variable.setIncrementGroup(group);
		}

		Byte calculation = (Byte)JRXmlConstants.getCalculationMap().get(atts.getValue(XmlConstants.ATTRIBUTE_calculation));
		if (calculation != null)
		{
			variable.setCalculation(calculation.byteValue());
		}

		if (atts.getValue(XmlConstants.ATTRIBUTE_incrementerFactoryClass) != null)
		{
			variable.setIncrementerFactoryClassName(atts.getValue(XmlConstants.ATTRIBUTE_incrementerFactoryClass));
		}

		return variable;
	}
	

}
