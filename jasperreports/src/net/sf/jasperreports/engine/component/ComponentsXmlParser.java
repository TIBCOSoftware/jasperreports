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

/**
 * A components XML parsers contains information required to parse components
 * contained by a single component bundle.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public interface ComponentsXmlParser
{

	/**
	 * Returns the namespace used by the component bundle.
	 * 
	 * <p>
	 * The XML representation of the components will use this namespace.
	 * 
	 * @return the component bundle namespace
	 */
	String getNamespace();
	
	/**
	 * Returns the public location of the component bundle XML schema. 
	 * 
	 * <p>
	 * This would be listed in <code>schemaLocation</code> XML attributes.
	 * 
	 * @return the public location of the component bundle XML schema
	 */
	String getPublicSchemaLocation();

	/**
	 * Returns a resource name that resolves to an internal XML schema
	 * resource.
	 * 
	 * <p>
	 * If not null, the resource (which needs to be present on the classpath)
	 * will be used when parsing component XML fragments instead of the public
	 * XML schema.
	 * 
	 * @return the name of the internal XML schema resource
	 */
	String getInternalSchemaResource();

	/**
	 * Returns a digester configurer for the component bundle.
	 *
	 * <p>
	 * The digester configurer is responsible for providing digester rules
	 * that transform an XML fragment into a {@link Component} instance.
	 * 
	 * @return a digester configurer
	 */
	XmlDigesterConfigurer getDigesterConfigurer();

}