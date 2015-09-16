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

import java.util.List;

import net.sf.jasperreports.engine.JRCloneable;

/**
 * @deprecated Replaced by {@link Item}.
 * @author sanda zaharia (shertage@users.sourceforge.net)
 */
public interface Marker extends JRCloneable {
	/**
	 * @deprecated Replaced by {@link MapComponent#ITEM_PROPERTY_latitude}.
	 */
	public static final String PROPERTY_latitude = MapComponent.ITEM_PROPERTY_latitude;
	/**
	 * @deprecated Replaced by {@link MapComponent#ITEM_PROPERTY_longitude}.
	 */
	public static final String PROPERTY_longitude = MapComponent.ITEM_PROPERTY_longitude;
	/**
	 * @deprecated Replaced by {@link MapComponent#ITEM_PROPERTY_MARKER_title}.
	 */
	public static final String PROPERTY_title = MapComponent.ITEM_PROPERTY_MARKER_title;
	
	public List<MarkerProperty> getProperties();
}
