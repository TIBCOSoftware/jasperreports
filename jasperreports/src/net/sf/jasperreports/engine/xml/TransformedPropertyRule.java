/*
 * ============================================================================
 * GNU Lesser General Public License
 * ============================================================================
 *
 * JasperReports - Free Java report-generating library.
 * Copyright (C) 2001-2009 JasperSoft Corporation http://www.jaspersoft.com
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
 * 539 Bryant Street, Suite 100
 * San Francisco, CA 94107
 * http://www.jaspersoft.com
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
