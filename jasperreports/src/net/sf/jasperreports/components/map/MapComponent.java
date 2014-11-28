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
 * The Map component interface. 
 * 
 * @author Teodor Danciu (teodord@users.sourceforge.net)
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
	
	/**
	 * Returns a {@link net.sf.jasperreports.engine.JRExpression JRExpression} 
	 * representing the latitude coordinate of the map center
	 *  
	 * @return the latitude expression
	 */
	JRExpression getLatitudeExpression();

	/**
	 * Returns a {@link net.sf.jasperreports.engine.JRExpression JRExpression} 
	 * representing the longitude coordinate of the map center
	 *  
	 * @return the longitude expression
	 */
	JRExpression getLongitudeExpression();
	
	/**
	 * Returns a {@link net.sf.jasperreports.engine.JRExpression JRExpression} 
	 * representing the address of the map center. If no latitude or longitude 
	 * coordinates are provided, the address expression will be used to calculate 
	 * these coordinates
	 * 
	 * @return the address expression
	 */
	JRExpression getAddressExpression();

	/**
	 * Returns a numeric expression  
	 * representing the zoom factor of the map.
	 * 
	 * @return the zoom expression
	 */
	JRExpression getZoomExpression();
	
	/**
	 * Returns a {@link net.sf.jasperreports.engine.JRExpression JRExpression} 
	 * representing the language for the labels on the map.
	 * 
	 * @return the language expression 
	 */
	JRExpression getLanguageExpression();

	/**
	 * Returns the evaluation time of the map component element
	 * 
	 * @return the evaluation time
	 */
	EvaluationTimeEnum getEvaluationTime();
	
	/**
	 * Returns the evaluation group name for the map component element
	 * 
	 * @return the evaluation group
	 */
	String getEvaluationGroup();
	
	/**
	 * Returns the type of the Google map. Possible values are:
	 * <ul>
	 * <li>{@link net.sf.jasperreports.components.map.type.MapTypeEnum#ROADMAP ROADMAP} (default value)</li>
	 * <li>{@link net.sf.jasperreports.components.map.type.MapTypeEnum#SATELLITE SATELLITE}</li>
	 * <li>{@link net.sf.jasperreports.components.map.type.MapTypeEnum#TERRAIN TERRAIN}</li>
	 * <li>{@link net.sf.jasperreports.components.map.type.MapTypeEnum#HYBRID HYBRID}</li>
	 * </ul>
	 * @return the type of the Google map
	 * @see net.sf.jasperreports.components.map.type.MapTypeEnum
	 */
	MapTypeEnum getMapType();

	/**
	 * Returns the scale factor of the Google map used to return higher-resolution map images when 
	 * working with high resolution screens available on mobile devices. Possible values are:
	 * <ul>
	 * <li><code>1</code> (default value)</li>
	 * <li><code>2</code></li>
	 * <li><code>4</code> (for Business customers only)</li>
	 * </ul>
	 * @return the scale factor
	 */
	MapScaleEnum getMapScale();
	
	/**
	 * Returns the image format of the map. Possible values are:
	 * <ul>
	 * <li>{@link net.sf.jasperreports.components.map.type.MapImageTypeEnum#PNG PNG} (default value)</li>
	 * <li>{@link net.sf.jasperreports.components.map.type.MapImageTypeEnum#PNG_8 PNG_8}</li>
	 * <li>{@link net.sf.jasperreports.components.map.type.MapImageTypeEnum#PNG_32 PNG_32}</li>
	 * <li>{@link net.sf.jasperreports.components.map.type.MapImageTypeEnum#GIF GIF}</li>
	 * <li>{@link net.sf.jasperreports.components.map.type.MapImageTypeEnum#JPG JPG}</li>
	 * <li>{@link net.sf.jasperreports.components.map.type.MapImageTypeEnum#JPG_BASELINE JPG_BASELINE}</li>
	 * </ul>
	 * @return the image format of the map
	 * @see net.sf.jasperreports.components.map.type.MapImageTypeEnum
	 */
	MapImageTypeEnum getImageType();
	
	/**
	 * Returns an attribute that customizes the way the engine handles a missing map image 
	 * during report generation. Possible values are:
	 * <ul>
	 * <li>{@link net.sf.jasperreports.engine.type.OnErrorTypeEnum#ERROR ERROR} (default value)</li>
	 * <li>{@link net.sf.jasperreports.engine.type.OnErrorTypeEnum#BLANK BLANK}</li>
	 * <li>{@link net.sf.jasperreports.engine.type.OnErrorTypeEnum#ICON ICON}</li>
	 * </ul>
	 * @return the onErrorType attribute
	 * @see net.sf.jasperreports.engine.type.OnErrorTypeEnum
	 */
	OnErrorTypeEnum getOnErrorType();
	
	/**
	 * Returns a list of {@link net.sf.jasperreports.components.map.ItemData ItemData} objects 
	 * representing collections of markers on the map
	 * 
	 * @return a list of marker data
	 * @see net.sf.jasperreports.components.map.ItemData
	 */
	List<ItemData> getMarkerDataList();
	
	/**
	 * Returns a list of {@link net.sf.jasperreports.components.map.ItemData ItemData} objects 
	 * representing collections of path styles for the map
	 * 
	 * @return a list of path styles
	 * @see net.sf.jasperreports.components.map.ItemData
	 */
	List<ItemData> getPathStyleList();
	
	/**
	 * Returns a list of {@link net.sf.jasperreports.components.map.ItemData ItemData} objects 
	 * representing collections of paths on the map
	 * 
	 * @return a list of path data
	 * @see net.sf.jasperreports.components.map.ItemData
	 */
	List<ItemData> getPathDataList();
	
	/**
	 * @deprecated Replaced by {@link #getMarkerDataList()}.
	 */
	ItemData getMarkerData();
	
	/**
	 * @deprecated Replaced by {@link #getMarkerDataList()}.
	 */
	MarkerDataset getMarkerDataset();
}
