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

import java.util.Map;

import net.sf.jasperreports.components.ComponentsExtensionsRegistryFactory;

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
public class XmlConstantPropertyRule extends Rule
{

	private static final Log log = LogFactory.getLog(XmlConstantPropertyRule.class);
	
	private final String attributeName;
	private final Map constantsMap;

	public XmlConstantPropertyRule(String attributeName, Map constantsMap)
	{
		this.attributeName = attributeName;
		this.constantsMap = constantsMap;
	}

	public void begin(String namespace, String name, Attributes attributes)
			throws Exception
	{
		String attrValue = attributes.getValue(attributeName);
		if (attrValue != null)
		{
			Object value = constantsMap.get(attrValue);
			if (value == null)
			{
				log.warn("Unrecognized attribute value \"" 
						+ attrValue + "\" for " + attrValue);
			}
			else
			{
				Object top = digester.peek();
				
				if (log.isDebugEnabled())
				{
					log.debug("Setting property " + attributeName + " on " + top
							+ " to " + value + " from attribute \"" + attrValue + "\"");
				}
				
				BeanUtils.setProperty(top, attributeName, value);
			}
		}
	}
	
}
