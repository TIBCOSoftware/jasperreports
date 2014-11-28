/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2014 TIBCO Software Inc. All rights reserved.
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
import java.util.ArrayList;
import java.util.List;

import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.design.events.JRChangeEventsSupport;
import net.sf.jasperreports.engine.design.events.JRPropertyChangeSupport;
import net.sf.jasperreports.engine.util.JRCloneUtils;

/**
 * @deprecated Replaced by {@link StandardItem}.
 * @author sanda zaharia (shertage@users.sourceforge.net)
 */
public class StandardMarker implements Marker, JRChangeEventsSupport, Serializable
{
	
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;
	public static final String PROPERTY_MARKER_PROPERTIES = "markerProperties";
	
	private transient JRPropertyChangeSupport eventSupport;

	private List<MarkerProperty> properties = new ArrayList<MarkerProperty>();

	public StandardMarker()
	{
	}
	
	public StandardMarker(List<MarkerProperty> properties)
	{
		this.properties = properties;
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
			clone.properties = JRCloneUtils.cloneList(properties);
			return clone;
		}
		catch (CloneNotSupportedException e)
		{
			// never
			throw new RuntimeException(e);
		}
	}

	@Override
	public List<MarkerProperty> getProperties() 
	{
		return properties;
	}
	
	public void addMarkerProperty(MarkerProperty property)
	{
		properties.add(property);
		getEventSupport().fireCollectionElementAddedEvent(PROPERTY_MARKER_PROPERTIES, property, properties.size() - 1);
	}
	
	public void removeMarkerProperty(MarkerProperty property)
	{
		int idx = properties.indexOf(property);
		if (idx >= 0)
		{
			properties.remove(idx);
			
			getEventSupport().fireCollectionElementRemovedEvent(PROPERTY_MARKER_PROPERTIES, 
					property, idx);
		}
	}
}
