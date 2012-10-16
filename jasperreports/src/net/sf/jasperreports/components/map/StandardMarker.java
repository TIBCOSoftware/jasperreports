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
package net.sf.jasperreports.components.map;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.design.events.JRChangeEventsSupport;
import net.sf.jasperreports.engine.design.events.JRPropertyChangeSupport;

/**
 * @author sanda zaharia (shertage@users.sourceforge.net)
 * @version $Id$
 */
public class StandardMarker implements Marker, JRChangeEventsSupport, Serializable
{
	
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;
	public static final String PROPERTY_MARKER_PROPERTIES = "markerProperties";
	
	private transient JRPropertyChangeSupport eventSupport;

	private Map<String, MarkerProperty> markerProperties;

	public StandardMarker()
	{
		markerProperties = new HashMap<String, MarkerProperty>();
	}
	
	
	public MarkerProperty getMarkerProperty(String name)
	{
		return markerProperties == null 
			? null 
			: markerProperties.get(name);
	}
	
	public Collection<MarkerProperty> getAllMarkerProperties()
	{
		return markerProperties == null 
			? null 
			: markerProperties.values();
	}
	
	public String getPropertyValue(String name)
	{
		return markerProperties == null 
			? null 
			: (markerProperties.get(name) == null
				? null
				: markerProperties.get(name).getValue());
	}
	
	public JRExpression getPropertyValueExpression(String name)
	{
		return markerProperties == null 
			? null 
			: (markerProperties.get(name) == null
				? null
				: markerProperties.get(name).getValueExpression());
	}
	
	public void addMarkerProperty(MarkerProperty markerProperty)
	{
		if(markerProperty != null)
		{
			markerProperties.put(markerProperty.getName(), markerProperty);
		}
	}
	
	public void addAllMarkerProperties(List<MarkerProperty> markerPropertiesList)
	{
		if(markerPropertiesList != null)
		{
			for(MarkerProperty markerProperty : markerPropertiesList)
			{
				if(markerProperty != null)
				{
					markerProperties.put(markerProperty.getName(), markerProperty);
				}
			}
		}
	}
	
	public void addAllMarkerProperties(Map<String, MarkerProperty> markerProperties)
	{
		if(markerProperties != null)
		{
			markerProperties.putAll(markerProperties);
		}
	}
	
	public MarkerProperty removeMarkerProperty(MarkerProperty markerProperty)
	{
		if(markerProperty != null)
		{
			return markerProperties.remove(markerProperty.getName());
		}
		return markerProperty;
	}
	
	public MarkerProperty removeMarkerProperty(String name)
	{
		if(name != null)
		{
			return markerProperties.remove(name);
		}
		return null;
	}

	public void removeAllMarkerProperties()
	{
		markerProperties.clear();
	}
	
	public JRPropertyChangeSupport getEventSupport()
	{
		synchronized (this)
		{
			if (eventSupport == null)
			{
				eventSupport = new JRPropertyChangeSupport(this);
			}
		}
		
		return eventSupport;
	}

	public Object clone()
	{
		try
		{
			StandardMarker clone = (StandardMarker) super.clone();
			return clone;
		}
		catch (CloneNotSupportedException e)
		{
			// never
			throw new RuntimeException(e);
		}
	}


	public Map<String, MarkerProperty> getMarkerProperties() {
		return this.markerProperties;
	}


	public void setMarkerProperties(Map<String, MarkerProperty> markerProperties) {
		Object old = this.markerProperties;
		this.markerProperties = markerProperties;
		getEventSupport().firePropertyChange(PROPERTY_MARKER_PROPERTIES, old, this.markerProperties);
		
	}
}
