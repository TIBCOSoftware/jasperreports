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

import java.io.Serializable;

import net.sf.jasperreports.engine.JRComponentElement;
import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.xml.JRXmlWriter;

/**
 * A key identifying a component type/class.
 *
 * <p>
 * A component type is identified by a namespace and a component name.
 * An instance of this class includes in addition a namespace prefix, which
 * is required when producing a JRXML representation of the component
 * (via {@link JRXmlWriter}).
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 * @see JRComponentElement#getComponentKey()
 */
public class ComponentKey implements Serializable
{

	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;
	
	private final String namespace;
	private final String namespacePrefix;
	private final String name;
	
	
	/**
	 * Create a component type key.
	 * 
	 * @param namespace the component namespace
	 * @param namespacePrefix the prefix to use for the namespace
	 * @param name the component name
	 */
	public ComponentKey(String namespace, String namespacePrefix, String name)
	{
		this.namespace = namespace;
		this.namespacePrefix = namespacePrefix;
		this.name = name;
	}
	
	public int hashCode()
	{
		int hash = 17;
		hash = 37 * hash + namespace.hashCode();
		hash = 37 * hash + namespacePrefix.hashCode();
		hash = 37 * hash + name.hashCode();
		return hash;
	}
	
	public boolean equals(Object o)
	{
		if (o == this)
		{
			return true;
		}

		if (!(o instanceof ComponentKey))
		{
			return false;
		}
		
		ComponentKey key = (ComponentKey) o;
		return namespace.equals(key.namespace)
				&& namespacePrefix.equals(key.namespacePrefix)
				&& name.equals(key.name);
	}

	/**
	 * Returns the component namespace.
	 * 
	 * <p>
	 * This value must match the namespece of a components bundle, as returned by
	 * {@link ComponentsXmlParser#getNamespace()}.
	 * 
	 * @return the component namespace
	 */
	public String getNamespace()
	{
		return namespace;
	}
	
	/**
	 * Returns the component name.
	 * 
	 * <p>
	 * The value must match a component name in the components bundle, as
	 * used in {@link ComponentsBundle#getComponentManager(String)}.
	 * 
	 * @return the coponent name
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * Returns the namespace prefix which is to be used when producing
	 * an XML representation of a component of this type.
	 * 
	 * @return the namespace prefix
	 */
	public String getNamespacePrefix()
	{
		return namespacePrefix;
	}

}
