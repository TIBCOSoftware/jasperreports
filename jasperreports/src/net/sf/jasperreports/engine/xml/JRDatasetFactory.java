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

import net.sf.jasperreports.engine.design.JRDesignDataset;

import org.xml.sax.Attributes;

/**
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public class JRDatasetFactory extends JRBaseFactory
{
	
	public Object createObject(Attributes attributes)
	{
		JRDesignDataset dataset = new JRDesignDataset(false);
		
		dataset.setName(attributes.getValue(XmlConstants.ATTRIBUTE_name));
		dataset.setScriptletClass(attributes.getValue(XmlConstants.ATTRIBUTE_scriptletClass));
		
		dataset.setResourceBundle(attributes.getValue(XmlConstants.ATTRIBUTE_resourceBundle));

		String resMissingAttr = attributes.getValue(XmlConstants.ATTRIBUTE_whenResourceMissingType);
		if (resMissingAttr != null && resMissingAttr.length() > 0)
		{
			Byte whenResourceMissingType = (Byte) JRXmlConstants.getWhenResourceMissingTypeMap().get(resMissingAttr);
			dataset.setWhenResourceMissingType(whenResourceMissingType.byteValue());
		}

		return dataset;
	}
}
