/*
 * ============================================================================
 * GNU Lesser General Public License
 * ============================================================================
 *
 * JasperReports - Free Java report-generating library.
 * Copyright (C) 2001-2005 JasperSoft Corporation http://www.jaspersoft.com
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
 * 303 Second Street, Suite 450 North
 * San Francisco, CA 94107
 * http://www.jaspersoft.com
 */
package net.sf.jasperreports.engine;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.SequencedHashMap;

/**
 * Properties map of an JR element.
 * <p/>
 * The order of the properties (obtained by {@link #getPropertyNames() getPropertyNames()}
 * is the same as the order in which the properties were added.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public class JRPropertiesMap implements Serializable
{
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;
	
	private final Map propertiesMap;
	
	
	/**
	 * Creates a properties map.
	 */
	public JRPropertiesMap()
	{
		propertiesMap = new SequencedHashMap();
	}

	
	/**
	 * Clones a properties map.
	 * 
	 * @param propertiesMap the original properties map
	 */
	public JRPropertiesMap(JRPropertiesMap propertiesMap)
	{
		this.propertiesMap = new SequencedHashMap();
		
		String[] propertyNames = propertiesMap.getPropertyNames();
		if (propertyNames != null && propertyNames.length > 0)
		{
			for(int i = 0; i < propertyNames.length; i++)
			{
				setProperty(propertyNames[i], propertiesMap.getProperty(propertyNames[i]));
			}
		}
	}

	
	/**
	 * Returns the names of the properties.
	 *  
	 * @return the names of the properties
	 */
	public String[] getPropertyNames()
	{
		Set names = propertiesMap.keySet(); 
		String[] namesArray = new String[names.size()];
		return (String[]) names.toArray(namesArray);
	}

	
	/**
	 * Returns the value of a property.
	 * 
	 * @param name the name of the property
	 * @return the value
	 */
	public String getProperty(String propName)
	{
		return (String)propertiesMap.get(propName);
	}

	
	/**
	 * Adds/sets a property value.
	 * 
	 * @param name the name of the property
	 * @param value the value of the property
	 */
	public void setProperty(String propName, String value)
	{
		propertiesMap.put(propName, value);
	}
	
	
	/**
	 * Removes a property.
	 * 
	 * @param name the property name
	 */	
	public void removeProperty(String propName)
	{
		propertiesMap.remove(propName);
	}
}
