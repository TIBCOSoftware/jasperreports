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
package net.sf.jasperreports.engine.component;

import java.io.IOException;

import net.sf.jasperreports.engine.JRPropertiesUtil;
import net.sf.jasperreports.engine.xml.JRXmlBaseWriter;
import net.sf.jasperreports.engine.xml.JRXmlWriter;

/**
 * A component writer is responsible for producing a XML representation of
 * component instances.
 * 
 * <p>
 * Its function is inverse to the one of {@link XmlDigesterConfigurer}, which
 * transforms XML fragments into object instances.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public interface ComponentXmlWriter
{

	/**
	 * Property prefix used by properties associated with a given component. These properties have no meaning in the absence of the related 
	 * {@link #PROPERTY_COMPONENT_TYPE_PREFIX PROPERTY_COMPONENT_TYPE_PREFIX} property. To specify a valid component property, the prefix of this property 
	 * should be the same as the suffix of the corresponding {@link #PROPERTY_COMPONENT_TYPE_PREFIX PROPERTY_COMPONENT_TYPE_PREFIX} property.
	 * 
	 * @see JRXmlBaseWriter
	 */
	public static final String PROPERTY_COMPONENT_PREFIX = JRPropertiesUtil.PROPERTY_PREFIX + "component.";

	/**
	 * Properties having this prefix are associated with a component type in a given report. Its value contains the component namespace 
	 * followed by a | separator, followed by the component name.
	 * <br/>
	 * For instance, for the table component shipped with JR source distribution the associated property may be written as:
	 * <br/>
	 * <code>&lt;property name="net.sf.jasperreports.component.type.1" value="http://jasperreports.sourceforge.net/jasperreports/components|table" /&gt;</code>
	 */
	public static final String PROPERTY_COMPONENT_TYPE_PREFIX = PROPERTY_COMPONENT_PREFIX + "type.";

	/**
	 * Property suffix associated with a component version in a given report. The prefix for this property should follow the composition rule described for the 
	 * {@link #PROPERTY_COMPONENT_PREFIX PROPERTY_COMPONENT_PREFIX} property prefix.
	 * <br/>
	 * If not provided, the component version is considered to be the currently deployed version of the component. 
	 * 
	 * @see JRXmlBaseWriter
	 */
	public static final String PROPERTY_COMPONENT_VERSION_SUFFIX = ".version";

	/**
	 * Outputs the XML representation of a component.
	 * 
	 * @param componentKey the component type key
	 * @param component the component instance
	 * @param reportWriter the report writer to which output is to be written
	 * @throws IOException exceptions produced while writing to the
	 * output stream
	 * @see ComponentKey#getNamespacePrefix()
	 * @see JRXmlWriter#getXmlWriteHelper()
	 */
	void writeToXml(ComponentKey componentKey, Component component, 
			JRXmlWriter reportWriter) throws IOException;

}
