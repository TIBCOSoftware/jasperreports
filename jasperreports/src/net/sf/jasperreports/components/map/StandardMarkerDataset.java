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
import java.util.Iterator;
import java.util.List;

import net.sf.jasperreports.components.items.ItemData;
import net.sf.jasperreports.components.items.StandardItemData;
import net.sf.jasperreports.components.items.StandardItemProperty;
import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRDatasetRun;
import net.sf.jasperreports.engine.JRExpressionCollector;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.base.JRBaseObjectFactory;
import net.sf.jasperreports.engine.design.JRDesignElementDataset;
import net.sf.jasperreports.engine.design.events.JRChangeEventsSupport;
import net.sf.jasperreports.engine.design.events.JRPropertyChangeSupport;
import net.sf.jasperreports.engine.util.JRCloneUtils;

/**
 * @deprecated Replaced by {@link StandardItemData}.
 * @author sanda zaharia (shertage@users.sourceforge.net)
 */
public class StandardMarkerDataset implements Serializable, MarkerDataset, JRChangeEventsSupport
{//FIXMEMAP implement clone?

	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;
	public static final String PROPERTY_MARKER = "marker";
	public static final String PROPERTY_DATASET_RUN = "datasetRun";

	private List<Marker> markerList = new ArrayList<Marker>();
	private JRDatasetRun datasetRun;
	
	private transient JRPropertyChangeSupport eventSupport;
	
	public StandardMarkerDataset()
	{
	}

	public StandardMarkerDataset(MarkerDataset dataset, JRBaseObjectFactory factory)
	{
		markerList = getCompiledMarkers(dataset.getMarkers(), factory);
		datasetRun = factory.getDatasetRun(dataset.getDatasetRun());
	}

	private static List<Marker> getCompiledMarkers(List<Marker> markers, JRBaseObjectFactory factory)
	{
		if (markers == null)
		{
			return null;
		}
		
		List<Marker> compiledMarkers = new ArrayList<Marker>(markers.size());
		for (Iterator<Marker> it = markers.iterator(); it.hasNext();)
		{
			Marker marker = it.next();
			Marker compiledMarker = new StandardMarker(getCompiledProperties(marker.getProperties(), factory));
			compiledMarkers.add(compiledMarker);
		}
		return compiledMarkers;
	}

	private static List<MarkerProperty> getCompiledProperties(List<MarkerProperty> properties, JRBaseObjectFactory factory)
	{
		if (properties == null)
		{
			return null;
		}
		
		List<MarkerProperty> compiledProperties = new ArrayList<MarkerProperty>(properties.size());
		for (Iterator<MarkerProperty> it = properties.iterator(); it.hasNext();)
		{
			MarkerProperty property = it.next();
			MarkerProperty compiledProperty = new StandardMarkerProperty(property.getName(), property.getValue(), factory.getExpression(property.getValueExpression()));
			compiledProperties.add(compiledProperty);
		}
		return compiledProperties;
	}

	public void collectExpressions(JRExpressionCollector collector) {
		MapCompiler.collectExpressions(this, collector);
	}

	@Override
	public List<Marker> getMarkers() {
		return markerList;
	}
	
	/**
	 *
	 */
	public void addMarker(Marker marker)
	{
		markerList.add(marker);
		getEventSupport().fireCollectionElementAddedEvent(PROPERTY_MARKER, marker, markerList.size() - 1);
	}
	
	/**
	 *
	 */
	public void addMarker(int index, Marker marker)
	{
		if(index >=0 && index < markerList.size())
			markerList.add(index, marker);
		else{
			markerList.add(marker);
			index = markerList.size() - 1;
		}
		getEventSupport().fireCollectionElementAddedEvent(PROPERTY_MARKER, markerList, index);
	}

	/**
	 *
	 */
	public Marker removeMarker(Marker marker)
	{
		if (marker != null)
		{
			int idx = markerList.indexOf(marker);
			if (idx >= 0)
			{
				markerList.remove(idx);
				getEventSupport().fireCollectionElementRemovedEvent(PROPERTY_MARKER, marker, idx);
			}
		}
		return marker;
	}

	@Override
	public JRDatasetRun getDatasetRun()
	{
		return datasetRun;
	}
	
	/**
	 * Sets the subdataset run information that will be used to create the marker list.
	 * 
	 * @param datasetRun the subdataset run information
	 * @see #getDatasetRun()
	 */
	public void setDatasetRun(JRDatasetRun datasetRun)
	{
		Object old = this.datasetRun;
		this.datasetRun = datasetRun;
		getEventSupport().firePropertyChange(PROPERTY_DATASET_RUN, old, this.datasetRun);
	}
	
	/**
	 * 
	 */
	public static ItemData getItemData(MarkerDataset markerDataset)
	{
		if (markerDataset != null)
		{
			StandardItemData itemData = new StandardItemData();
			for (Marker marker : markerDataset.getMarkers())
			{
				StandardItem item = new StandardItem();
				for (MarkerProperty markerProperty : marker.getProperties())
				{
					StandardItemProperty itemProperty = new StandardItemProperty();
					itemProperty.setName(markerProperty.getName());
					itemProperty.setValue(markerProperty.getValue());
					itemProperty.setValueExpression(markerProperty.getValueExpression());
					item.addItemProperty(itemProperty);
				}
				itemData.addItem(item);
			}
			
			if (markerDataset.getDatasetRun() != null)
			{
				JRDesignElementDataset dataset = new JRDesignElementDataset();
				dataset.setDatasetRun(markerDataset.getDatasetRun());
				itemData.setDataset(dataset);
			}
			return itemData;
		}
		return null;
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
		StandardMarkerDataset clone = null;
		try
		{
			clone = (StandardMarkerDataset) super.clone();
		} 
		catch (CloneNotSupportedException e)
		{
			// never
			throw new JRRuntimeException(e);
		}
		clone.datasetRun = JRCloneUtils.nullSafeClone(datasetRun);
		clone.markerList = JRCloneUtils.cloneList(markerList);
		clone.eventSupport = null;
		return clone;
	}

}
