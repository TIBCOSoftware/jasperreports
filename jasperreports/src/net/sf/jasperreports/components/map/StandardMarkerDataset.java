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

import java.util.ArrayList;
import java.util.List;

import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRExpressionCollector;
import net.sf.jasperreports.engine.base.JRBaseObjectFactory;
import net.sf.jasperreports.engine.design.JRDesignElementDataset;

/**
 * @author sanda zaharia (shertage@users.sourceforge.net)
 * @version $Id$
 */
public class StandardMarkerDataset extends JRDesignElementDataset implements MarkerDataset {

	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;
	public static final String PROPERTY_MARKER = "marker";

	private List<Marker> markerList = new ArrayList<Marker>();
	
	public StandardMarkerDataset()
	{
		super();
	}

	public StandardMarkerDataset(MarkerDataset dataset, JRBaseObjectFactory factory)
	{
		super(dataset, factory);
		List<Marker> markers = dataset.getMarkers();
		if(markers != null && !markers.isEmpty())
		{
			markerList.addAll(markers);
		}
	}

	@Override
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

	

}
