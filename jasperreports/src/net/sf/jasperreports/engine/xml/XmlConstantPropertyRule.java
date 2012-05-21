/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2011 Jaspersoft Corporation. All rights reserved.
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

import net.sf.jasperreports.engine.type.EnumUtil;
import net.sf.jasperreports.engine.type.JREnum;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public class XmlConstantPropertyRule extends TransformedPropertyRule
{

	private static final Log log = LogFactory.getLog(XmlConstantPropertyRule.class);
	
	private final JREnum[] values;

	public XmlConstantPropertyRule(String attributeName, JREnum[] values)
	{
		super(attributeName);
		this.values = values;
	}

	public XmlConstantPropertyRule(String attributeName, String propertyName, 
			JREnum[] values)
	{
		super(attributeName, propertyName);
		this.values = values;
	}

	protected Object toPropertyValue(String attributeValue)
	{
		Object value = EnumUtil.getByName(values, attributeValue);
		if (value == null)
		{
			log.warn("Unrecognized attribute value \"" 
					+ attributeValue + "\" for " + attributeName);
		}
		return value;
	}
	
}
