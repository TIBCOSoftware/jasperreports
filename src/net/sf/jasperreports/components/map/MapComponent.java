/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2013 Jaspersoft Corporation. All rights reserved.
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

import net.sf.jasperreports.components.map.type.MapImageTypeEnum;
import net.sf.jasperreports.components.map.type.MapScaleEnum;
import net.sf.jasperreports.components.map.type.MapTypeEnum;
import net.sf.jasperreports.engine.JRCloneable;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRPropertiesUtil;
import net.sf.jasperreports.engine.component.Component;
import net.sf.jasperreports.engine.type.EvaluationTimeEnum;
import net.sf.jasperreports.engine.type.OnErrorTypeEnum;

/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public interface MapComponent extends Component, JRCloneable
{
	public static final String PROPERTY_PREFIX = JRPropertiesUtil.PROPERTY_PREFIX + "components.map.";
	public static final String PROPERTY_KEY = PROPERTY_PREFIX + "key";
	public static final String PROPERTY_CLIENT_ID = PROPERTY_PREFIX + "client.id";
	public static final String PROPERTY_SIGNATURE = PROPERTY_PREFIX + "signature";
	public static final String PROPERTY_VERSION = PROPERTY_PREFIX + "version";

	public static final String PROPERTY_latitude = "latitude";
	public static final String PROPERTY_longitude = "longitude";
	public static final String PROPERTY_address = "address";
	public static final String PROPERTY_title = "title";
	public static final String PROPERTY_name = "name";
	public static final String PROPERTY_style = "style";
	public static final String PROPERTY_isPolygon = "isPolygon";
	public static final String PROPERTY_locations = "locations";
	
	public static final String ELEMENT_MARKER_DATA = "markerData";
	public static final String ELEMENT_PATH_STYLE = "pathStyle";
	public static final String ELEMENT_PATH_DATA = "pathData";
	

	public static final String DEFAULT_PATH_NAME = "DEFAULT_PATH_NAME";
	public static final Integer DEFAULT_ZOOM = 8;
	
	JRExpression getLatitudeExpression();

	JRExpression getLongitudeExpression();
	
	JRExpression getAddressExpression();

	JRExpression getZoomExpression();
	
	JRExpression getLanguageExpression();

	EvaluationTimeEnum getEvaluationTime();
	
	String getEvaluationGroup();
	
	MapTypeEnum getMapType();

	MapScaleEnum getMapScale();
	
	MapImageTypeEnum getImageType();
	
	OnErrorTypeEnum getOnErrorType();
	
	List<ItemData> getMarkerDataList();
	
	List<ItemData> getPathStyleList();
	
	List<ItemData> getPathDataList();
	
	/**
	 * @deprecated Replaced by {@link #getMarkerDataList()}.
	 */
	ItemData getMarkerData();
	
	/**
	 * @deprecated Replaced by {@link #getMarkerData()}.
	 */
	MarkerDataset getMarkerDataset();
}
