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
package net.sf.jasperreports.engine.query;

import java.util.Map;

import net.sf.jasperreports.engine.JRDataset;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRValueParameter;
import net.sf.jasperreports.engine.util.JRProperties;

/**
 * Jaxen XPath query executer factory.
 * <p/>
 * The factory creates {@link net.sf.jasperreports.engine.query.JaxenXPathQueryExecuter JaxenXPathQueryExecuter}
 * query executers.
 * 
 * @author Narcis Marcu (narcism@users.sourceforge.net)
 * @version $Id$
 */
public class JaxenXPathQueryExecuterFactory extends JRXPathQueryExecuterFactory
{
	/**
	 * Built-in parameter holdin the value of the <code>javax.xml.parsers.DocumentBuilderFactor</code> used to create 
	 * documents of type <code>org.w3c.dom.Document<code>
	 */
	public final static String PARAMETER_DOCUMENT_BUILDER_FACTORY = "DOCUMENT_BUILDER_FACTORY";
	
	/**
	 * Parameter that holds the <code>java.util.Map&lt;String,String&gt;</code> with XML namespace information in the 
	 * &lt;prefix, uri&gt; format
	 */
	public final static String PARAMETER_XML_NAMESPACE_MAP = "XML_NAMESPACE_MAP";

	/**
	 * Prefix for properties holding the namespace prefix and uri:
	 * e.g. net.sf.jasperreports.xml.namespace.{prefix} = uri
	 * <p>
	 * This property has a lower priority than {@link #PARAMETER_XML_NAMESPACE_MAP}, which if it is specified it will cause
	 * the prefixed properties not to be searched for.  
	 * </p>
	 */
	public final static String XML_NAMESPACE_PREFIX = JRProperties.PROPERTY_PREFIX + "xml.namespace.";
	
	/**
	 * Boolean parameter/property that specifies whether the XML document should be parsed for namespaces or not.
	 * <p>
	 * This parameter is meaningful only when:
	 * <ul>
	 * <li>
	 * the {@link #PARAMETER_XML_NAMESPACE_MAP} parameter is not provided or provided with a <code>null</code> value
	 * </li>
	 * <li>
	 * there are no properties prefixed with {@link #XML_NAMESPACE_PREFIX};
	 * </li>
	 * <li>
	 * the xpath query expression that is provided <b>contains</b> XML namespace prefixes
	 * </li>
	 * </ul>
	 * </p>
	 * It defaults to <code>false</code>
	 */
	public final static String XML_DETECT_NAMESPACES = JRProperties.PROPERTY_PREFIX + "xml.detect.namespaces";
	
	private final static Object[] JAXEN_XPATH_BUILTIN_PARAMETERS = {
		PARAMETER_XML_DATA_DOCUMENT,  "org.w3c.dom.Document",
		PARAMETER_DOCUMENT_BUILDER_FACTORY, "javax.xml.parsers.DocumentBuilderFactory",
		PARAMETER_XML_NAMESPACE_MAP, "java.util.Map",
		XML_DATE_PATTERN, "java.lang.String",
		XML_NUMBER_PATTERN, "java.lang.String",
		XML_LOCALE, "java.util.Locale",
		XML_TIME_ZONE, "java.util.TimeZone",
		};

	public Object[] getBuiltinParameters()
	{
		return JAXEN_XPATH_BUILTIN_PARAMETERS;
	}
	
	public JRQueryExecuter createQueryExecuter(JRDataset dataset, Map<String,? extends JRValueParameter> parameters)
			throws JRException
	{
		return new JaxenXPathQueryExecuter(dataset, parameters);
	}

}
