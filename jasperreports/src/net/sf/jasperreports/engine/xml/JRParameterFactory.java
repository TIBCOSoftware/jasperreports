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

import net.sf.jasperreports.engine.design.JRDesignParameter;

import org.xml.sax.Attributes;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public class JRParameterFactory extends JRBaseFactory
{
	/**
	 *
	 */
	public Object createObject(Attributes atts)
	{
		JRDesignParameter parameter = new JRDesignParameter();
		
		setParameterAttributes(parameter, atts);

		return parameter;
	}


	protected void setParameterAttributes(JRDesignParameter parameter, Attributes atts)
	{
		parameter.setName(atts.getValue(JRXmlConstants.ATTRIBUTE_name));
		
		if (atts.getValue(JRXmlConstants.ATTRIBUTE_class) != null)
		{
			parameter.setValueClassName(atts.getValue(JRXmlConstants.ATTRIBUTE_class));
		}
		
		String nestedType = atts.getValue(JRXmlConstants.ATTRIBUTE_nestedType);
		if (nestedType != null)
		{
			parameter.setNestedTypeName(nestedType);
		}

		String isForPrompting = atts.getValue(JRXmlConstants.ATTRIBUTE_isForPrompting);
		if (isForPrompting != null && isForPrompting.length() > 0)
		{
			parameter.setForPrompting(Boolean.valueOf(isForPrompting).booleanValue());
		}
	}
	

}
