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

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.digester.Rule;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.Attributes;

/**
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public abstract class TransformedPropertyRule extends Rule
{

	private static final Log log = LogFactory.getLog(TransformedPropertyRule.class);
	
	protected final String attributeName;
	protected final String propertyName;

	protected TransformedPropertyRule(String attributeName)
	{
		this(attributeName, attributeName);
	}

	protected TransformedPropertyRule(String attributeName, String propertyName)
	{
		this.attributeName = attributeName;
		this.propertyName = propertyName;
	}

	public void begin(String namespace, String name, Attributes attributes)
			throws Exception
	{
		String attrValue = attributes.getValue(attributeName);
		if (attrValue != null)
		{
			Object value = toPropertyValue(attrValue);
			if (value != null)
			{
				Object top = digester.peek();
				
				if (log.isDebugEnabled())
				{
					log.debug("Setting property " + propertyName + " on " + top
							+ " to " + value + " from attribute \"" + attrValue + "\"");
				}
				
				BeanUtils.setProperty(top, propertyName, value);
			}
			else
			{
				if (log.isDebugEnabled())
				{
					log.debug("Attribute value " + attrValue 
							+ " resulted in null property value, not setting");
				}
			}
		}
	}
	
	protected abstract Object toPropertyValue(String attributeValue);
	
}
