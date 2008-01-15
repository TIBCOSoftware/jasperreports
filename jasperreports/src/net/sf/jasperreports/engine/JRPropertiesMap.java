/*
 * ============================================================================
 * GNU Lesser General Public License
 * ============================================================================
 *
 * JasperReports - Free Java report-generating library.
 * Copyright (C) 2001-2006 JasperSoft Corporation http://www.jaspersoft.com
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

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Properties map of an JR element.
 * <p/>
 * The order of the properties (obtained by {@link #getPropertyNames() getPropertyNames()}
 * is the same as the order in which the properties were added.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public class JRPropertiesMap implements Serializable, Cloneable
{
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;
	
	private Map propertiesMap;
	private List propertiesList;
	
	
	/**
	 * Creates a properties map.
	 */
	public JRPropertiesMap()
	{
		propertiesMap = new HashMap();
		propertiesList = new ArrayList();
	}

	
	/**
	 * Clones a properties map.
	 * 
	 * @param propertiesMap the original properties map
	 */
	public JRPropertiesMap(JRPropertiesMap propertiesMap)
	{
		this();
		
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
		return (String[]) propertiesList.toArray(new String[propertiesList.size()]);
	}

	
	/**
	 * Returns the value of a property.
	 * 
	 * @param propName the name of the property
	 * @return the value
	 */
	public String getProperty(String propName)
	{
		return (String)propertiesMap.get(propName);
	}
	
	
	/**
	 * Decides whether the map contains a specified property.
	 * 
	 * The method returns true even if the property value is null.
	 * 
	 * @param propName the property name
	 * @return <code>true</code> iff the map contains the property
	 */
	public boolean containsProperty(String propName)
	{
		return propertiesMap.containsKey(propName);
	}

	
	/**
	 * Adds/sets a property value.
	 * 
	 * @param propName the name of the property
	 * @param value the value of the property
	 */
	public void setProperty(String propName, String value)
	{
		if (!propertiesMap.containsKey(propName))
		{
			propertiesList.add(propName);
		}
		propertiesMap.put(propName, value);
	}
	
	
	/**
	 * Removes a property.
	 * 
	 * @param propName the property name
	 */	
	public void removeProperty(String propName)
	{
		if (propertiesMap.containsKey(propName))
		{
			propertiesList.remove(propName);
			propertiesMap.remove(propName);
		}
	}
	
	
	/**
	 * Clones this property map.
	 * 
	 * @return a clone of this property map
	 */
	public JRPropertiesMap cloneProperties()
	{
		return new JRPropertiesMap(this);
	}
	
	
	/**
	 *
	 */
	public Object clone()
	{
		return this.cloneProperties();
	}
	
	
	public String toString()
	{
		return propertiesMap.toString();
	}
	
	
	private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException
	{
		in.defaultReadObject();
		
		if (propertiesList == null)// an instance from an old version has been deserialized
		{
			//recreate the properties list and map
			propertiesList = new ArrayList(propertiesMap.keySet());
			propertiesMap = new HashMap(propertiesMap);
		}
	}
	
	
	/**
	 * Checks whether there are any properties.
	 * 
	 * @return whether there are any properties
	 */
	public boolean hasProperties()
	{
		return !propertiesList.isEmpty();
	}
	
	
	/**
	 * Clones the properties map of a properties holder.
	 * If the holder does not have any properties, null is returned.
	 * 
	 * @param propertiesHolder the properties holder
	 * @return a clone of the holder's properties map, or <code>null</code>
	 * if the holder does not have any properties
	 */
	public static JRPropertiesMap getPropertiesClone(JRPropertiesHolder propertiesHolder)
	{
		JRPropertiesMap clone;
		if (propertiesHolder.hasProperties())
		{
			clone = propertiesHolder.getPropertiesMap().cloneProperties();
		}
		else
		{
			clone = null;
		}
		return clone;
	}
}
