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

import net.sf.jasperreports.components.items.ItemData;
import net.sf.jasperreports.components.map.type.MapImageTypeEnum;
import net.sf.jasperreports.components.map.type.MapScaleEnum;
import net.sf.jasperreports.components.map.type.MapTypeEnum;
import net.sf.jasperreports.engine.JRCloneable;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRGenericElementType;
import net.sf.jasperreports.engine.JRPropertiesUtil;
import net.sf.jasperreports.engine.component.Component;
import net.sf.jasperreports.engine.type.EvaluationTimeEnum;
import net.sf.jasperreports.engine.type.OnErrorTypeEnum;
import net.sf.jasperreports.engine.xml.JRXmlConstants;

/**
 * The Map component interface. 
 * 
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public interface MapComponent extends Component, JRCloneable
{
	// properties at report element level:
	
	public static final String PROPERTY_PREFIX = JRPropertiesUtil.PROPERTY_PREFIX + "components.map.";
	public static final String PROPERTY_KEY = PROPERTY_PREFIX + "key";
	public static final String PROPERTY_CLIENT_ID = PROPERTY_PREFIX + "client.id";
	public static final String PROPERTY_SIGNATURE = PROPERTY_PREFIX + "signature";
	public static final String PROPERTY_VERSION = PROPERTY_PREFIX + "version";

	// map attributes:
	
	/**
	 * The attribute that provides the map type. Possible values are:
	 * <ul>
	 * <li>roadmap (default)</li>
	 * <li>satellite</li>
	 * <li>terrain</li>
	 * <li>hybrid</li>
	 * </ul>
	 */
	public static final String ATTRIBUTE_MAP_TYPE = "mapType";
	
	/**
	 * Applies to static maps only. This attribute specifies the behavior of the engine when the 
	 * image is not available. Possible values are: 
	 * <ul>
	 * <li>Error (default)</li>
	 * <li>Blank</li>
	 * <li>Icon</li>
	 * </ul>
	 */
	public static final String ATTRIBUTE_ON_ERROR_TYPE = "onErrorType";
	
	/**
	 * Numeric (positive integer) attribute that provides the scale value used to return higher-resolution map images 
	 * when working with high resolution screens available on mobile devices. Possible values are: 
	 * <ul>
	 * <li>1 (default)</li>
	 * <li>2</li>
	 * <li>4 (for Business customers only)</li>
	 * </ul>
	 */
	public static final String ATTRIBUTE_MAP_SCALE = "mapScale";
	
	/**
	 * This attribute represents the image format of the map. Possible values are: 
	 * <ul>
	 * <li>png (default)</li>
	 * <li>png8</li>
	 * <li>png32</li>
	 * <li>gif</li>
	 * <li>jpg</li>
	 * <li>jpg-baseline</li>
	 * </ul>
	 */
	public static final String ATTRIBUTE_IMAGE_TYPE = "imageType";
	
	// common item properties:
	
	/**
	 * Numeric floating-point value representing the latitude of the place on the map; 
	 * required if no equivalent {@link #ITEM_PROPERTY_address} is set.
	 * <p/>
	 * Applies to items in <code>markerData</code> and <code>pathData</code>.
	 */
	public static final String ITEM_PROPERTY_latitude = "latitude";
	
	/**
	 * Numeric floating-point value representing the longitude of the place on the map; 
	 * required if no equivalent {@link #ITEM_PROPERTY_address} is set.
	 * <p/>
	 * Applies to items in <code>markerData</code> and <code>pathData</code>.
	 */
	public static final String ITEM_PROPERTY_longitude = "longitude";
	
	/**
	 * Specifies the address of a given point on the map. 
	 * <p/>
	 * The address-based geo-location is considered only if no {@link #ITEM_PROPERTY_latitude} 
	 * and {@link #ITEM_PROPERTY_longitude} properties are set. 
	 * <p/>
	 * Applies to items in <code>markerData</code> and <code>pathData</code>.
	 */
	public static final String ITEM_PROPERTY_address = "address";

	/**
	 * String value representing the name, as unique identifier.
	 * <p/>
	 * Applies to items in <code>pathData</code> and <code>pathStyle</code>. It is required for items in <code>pathStyle</code> 
	 * and optional for items in <code>pathData</code>. Default name for items in <code>pathData</code> is {@link #DEFAULT_PATH_NAME}.
	 */
	public static final String ITEM_PROPERTY_name = "name";

	/**
	 * String value representing the name of a path style (in <code>pathData</code>) or the name of the
	 * parent style (in <code>pathStyle</code>); optional.
	 * <p/>
	 * Applies to items in <code>pathData</code> and <code>pathStyle</code>.
	 * <p/>
	 *  If not provided, the path will be generated using Google default style settings for paths.
	 */
	public static final String ITEM_PROPERTY_style = "style";
		
	/**
	 * boolean value that specifies if this item (marker or path) is clickable; optional. Default value is true.
	 * <p/>
	 * Applies to items in <code>markerData</code> and <code>pathStyle</code>.
	 */
	public static final String ITEM_PROPERTY_clickable = "clickable";
	
	/**
	 * boolean value that specifies if this item (marker or path) is draggable; optional. Default value is false.
	 * <p/>
	 * Applies to items in <code>markerData</code> and <code>pathStyle</code>.
	 */
	public static final String ITEM_PROPERTY_draggable = "draggable";
	
	/**
	 * boolean value that specifies if this item (marker or path) is visible on the map; optional. Default value is true.
	 * <p/>
	 * Applies to items in <code>markerData</code> and <code>pathStyle</code>.
	 */
	public static final String ITEM_PROPERTY_visible = "visible";
	
	/**
	 * Integer value representing the z index of this item (marker or path); optional. 
	 * <p/>
	 * Applies to items in <code>markerData</code> and <code>pathStyle</code>.
	 */
	public static final String ITEM_PROPERTY_MARKER_zIndex = "zIndex";
	
	// item properties for markerData:
	
	/**
	 * String value representing the tooltip for the marker icon; optional.
	 */
	public static final String ITEM_PROPERTY_MARKER_title = "title";
		
	/**
	 * String value representing a link to a page or document that will be open when the marker is clicked on; optional.
	 * @see #ITEM_PROPERTY_MARKER_INFOWINDOW_content
	 */
	public static final String ITEM_PROPERTY_MARKER_url = "url";
					
	/**
	 * String value representing the hyperlink target for the marker; optional.
	 * <p/>
	 * Allowed values are:
	 * <ul>
	 * <li>_blank (default)</li>
	 * <li>_parent</li>
	 * <li>_self</li>
	 * <li>_top</li>
	 * <li>a valid window/frame name</li>
	 * </ul>
	 */
	public static final String ITEM_PROPERTY_MARKER_target = "target";
	
	/**
	 * Optional String value representing the color for the default pin icons. This property is taken 
	 * into account when {@link #ITEM_PROPERTY_MARKER_ICON_url} property is not set. Its value may represent:
	 * <p/>
	 * <ul>
	 * <li>one of the predefined color names in the following list (see also ColorEnum):
	 * <ul>
	 * <li>black</li>
	 * <li>blue</li>
	 * <li>cyan</li>
	 * <li>darkGray</li>
	 * <li>gray</li>
	 * <li>green</li>
	 * <li>lightGray</li>
	 * <li>magenta</li>
	 * <li>orange</li>
	 * <li>pink</li>
	 * <li>red(default)</li>
	 * <li>yellow</li>
	 * <li>white</li>
	 * </ul>
	 * </li>
	 * <li>a valid color hex code (prefixed by #). For instance: #0000FF.</li>
	 * <li>the color RGB value.</li>
	 * </ul>
	 */
	public static final String ITEM_PROPERTY_MARKER_color = "color";	

	/**
	 * String value representing the label for the default pin icons; optional. This property is taken into account 
	 * when {@link #ITEM_PROPERTY_MARKER_ICON_url} property is not set.
	 */
	public static final String ITEM_PROPERTY_MARKER_label = "label";
	
	/**
	 * String value representing the cursor value; optional. 
	 */
	public static final String ITEM_PROPERTY_MARKER_cursor = "cursor";
	
	/**
	 * Predefined String value representing the default marker icon size; optional.
	 * <p/>
	 * This property is considered for static maps only. Allowed values are:
	 * <ul>
	 * <li>mid</li>
	 * <li>tiny</li>
	 * <li>small</li>
	 * </ul>
	 */
	public static final String ITEM_PROPERTY_MARKER_size = "size";
	
	/**
	 * boolean value that specifies if the marker icon is flat; optional.
	 */
	public static final String ITEM_PROPERTY_MARKER_flat = "flat";
	
	/**
	 * boolean value that specifies if the marker icon is optimized; optional.
	 */
	public static final String ITEM_PROPERTY_MARKER_optimized = "optimized";
	
	/**
	 * boolean value to be applied for the raiseOnDrag property; optional.
	 */
	public static final String ITEM_PROPERTY_MARKER_raiseOnDrag = "raiseOnDrag";
	
	// marker icon properties
	
	/**
	 * String value representing the URL where the marker icon is loaded from; optional.
	 * <p/>
	 * This property is overriden by the {@link #ITEM_PROPERTY_MARKER_ICON_url} property value.
	 */
	public static final String ITEM_PROPERTY_MARKER_icon = "icon";

	/**
	 * String value representing the URL where the marker icon is loaded from; optional.
	 * <p/>
	 * When is set, it overrides the value of {@link #ITEM_PROPERTY_MARKER_icon} property.
	 * <p/>
	 * If not set, a default google pin icon will be loaded.
	 */
	public static final String ITEM_PROPERTY_MARKER_ICON_url = "icon.url";

	/**
	 * Integer value representing the icon width, in pixels. To be considered, 
	 * {@link #ITEM_PROPERTY_MARKER_ICON_height} must be also set.
	 * <p/>
	 * This property is optional.
	 */
	public static final String ITEM_PROPERTY_MARKER_ICON_width = "icon.width";

	/**
	 * Integer value representing the icon height, in pixels. To be considered, 
	 * {@link #ITEM_PROPERTY_MARKER_ICON_width} must be also set.
	 * <p/>
	 * This property is optional.
	 */
	public static final String ITEM_PROPERTY_MARKER_ICON_height = "icon.height";    
	
	/**
	 * Integer value representing the horizontal position of the marker icon within a sprite, 
	 * if any, in pixels. Default value is 0.
	 * <p/>
	 * This property is optional.
	 */
	public static final String ITEM_PROPERTY_MARKER_ICON_ORIGIN_x = "icon.origin.x";    
	
	/**
	 * Integer value representing the vertical position of the marker icon within a sprite, if any, 
	 * in pixels. Default value is 0.
	 * <p/>
	 * This property is optional.
	 */
	public static final String ITEM_PROPERTY_MARKER_ICON_ORIGIN_y = "icon.origin.y";    
	
	/**
	 * Integer value representing the horizontal position to anchor the marker icon with respect to the 
	 * location of the marker on the map. Default value is 0.
	 * <p/>
	 * This property is optional.
	 */
	public static final String ITEM_PROPERTY_MARKER_ICON_ANCHOR_x = "icon.anchor.x";    
	
	/**
	 * Integer value representing the vertical position to anchor the marker icon with respect to the 
	 * location of the marker on the map. Default value is 0.
	 * <p/>
	 * This property is optional.
	 */
	public static final String ITEM_PROPERTY_MARKER_ICON_ANCHOR_y = "icon.anchor.y";    
	
	// marker shadow properties
	
	/**
	 * String value representing the URL where the custom marker shadow icon is loaded from; optional.
	 * <p/>
	 * This property is overriden by {@link #ITEM_PROPERTY_MARKER_SHADOW_url}.
	 */
	public static final String ITEM_PROPERTY_MARKER_shadow = "shadow";

	/**
	 * String value representing the URL where the custom marker shadow icon is loaded from; optional.
	 * <p/>
	 * When is set, it overrides the value of the {@link #ITEM_PROPERTY_MARKER_shadow} property.
	 */
	public static final String ITEM_PROPERTY_MARKER_SHADOW_url = "shadow.url";

	/**
	 * Integer value representing the shadow icon width, in pixels. To be considered, 
	 * {@link #ITEM_PROPERTY_MARKER_SHADOW_height} must be also set.
	 * <p/>
	 * This property is optional.
	 */
	public static final String ITEM_PROPERTY_MARKER_SHADOW_width = "shadow.width";

	/**
	 * Integer value representing the shadow icon height, in pixels. To be considered, 
	 * {@link #ITEM_PROPERTY_MARKER_SHADOW_width} must be also set.
	 * <p/>
	 * This property is optional.
	 */
	public static final String ITEM_PROPERTY_MARKER_SHADOW_height = "shadow.height";    
	
	/**
	 * Integer value representing the horizontal position of the shadow icon within a sprite, 
	 * if any, in pixels. Default value is 0.
	 * <p/>
	 * This property is optional.
	 */
	public static final String ITEM_PROPERTY_MARKER_SHADOW_ORIGIN_x = "shadow.origin.x";    
	
	/**
	 * Integer value representing the vertical position of the shadow icon within a sprite, if any, 
	 * in pixels. Default value is 0.
	 * <p/>
	 * This property is optional.
	 */
	public static final String ITEM_PROPERTY_MARKER_SHADOW_ORIGIN_y = "shadow.origin.y";    
	
	/**
	 * Integer value representing the horizontal position to anchor the shadow icon with respect to the 
	 * location of the marker on the map. Default value is 0.
	 * <p/>
	 * This property is optional.
	 */
	public static final String ITEM_PROPERTY_MARKER_SHADOW_ANCHOR_x = "shadow.anchor.x";    
	
	/**
	 * Integer value representing the vertical position to anchor the shadow icon with respect to the 
	 * location of the marker on the map. Default value is 0.
	 * <p/>
	 * This property is optional.
	 */
	public static final String ITEM_PROPERTY_MARKER_SHADOW_ANCHOR_y = "shadow.anchor.y";    
	
    // marker infowindow properties

	/**
	 * String value representing the content to be displayed in an info window; optional. Empty contents are not processed.
	 * When a marker icon is clicked on the map, additional information can be displayed either in a separate 
	 * window/frame or in an info window floating above the map. 
	 * <p/>
	 * If both {@link #ITEM_PROPERTY_MARKER_url} and info window settings are present in a marker definition, the info window takes precedence 
	 * over the marker URL. Clicking on the marker icon will open the info window.
	 * @see #ITEM_PROPERTY_MARKER_url
	 */
	public static final String ITEM_PROPERTY_MARKER_INFOWINDOW_content = "infowindow.content";
					
	/**
	 * Optional integer value representing the offset (in pixels) from the tip of the info window to the location 
	 * on which the info window is anchored. Default value is 0.
	 */
	public static final String ITEM_PROPERTY_MARKER_INFOWINDOW_pixelOffset = "infowindow.pixelOffset";
					
	/**
	 * Optional integer value representing the maximum width in pixels of the info window.
	 */
	public static final String ITEM_PROPERTY_MARKER_INFOWINDOW_maxWidth = "infowindow.maxWidth";
	
	/**
	 * Optional floating-point value representing the latitude used to determine the info window position on the map. 
	 * Neglected for info windows attached to markers.
	 */
	public static final String ITEM_PROPERTY_MARKER_INFOWINDOW_latitude = "infowindow.latitude";
	
	/**
	 * Optional floating-point value representing the longitude used to determine the info window position on the map. 
	 * Neglected for info windows attached to markers.
	 */
	public static final String ITEM_PROPERTY_MARKER_INFOWINDOW_longitude = "infowindow.longitude";
					
	// item properties for pathData:
	// see the common item properties that can be applied for items in pathData.
	
	// item properties for pathStyle:
	 
	/**
	 * Flag that specifies whether the path is a polygon; optional. 
	 * <p/>
	 * Default value: false.
	 */
	public static final String ITEM_PROPERTY_STYLE_isPolygon = "isPolygon";
	
	/**
	 * Numeric integer value representing the stroke weight; optional. 
	 */
	public static final String ITEM_PROPERTY_STYLE_strokeWeight = "strokeWeight";
	
	/**
	 * The color of the stroke; optional. Can be represented as #-prefixed HTML color code or as predefined HTML color name 
	 */
	public static final String ITEM_PROPERTY_STYLE_strokeColor = "strokeColor";
	
	/**
	 * Numeric floating-point value in [0..1] representing the opacity of the path stroke; optional.
	 */
	public static final String ITEM_PROPERTY_STYLE_strokeOpacity = "strokeOpacity";
	
	/**
	 * String value representing the color of the fill; optional. 
	 * <p/>
	 * Can be represented as #-prefixed HTML color code or as predefined HTML color name 
	 */
	public static final String ITEM_PROPERTY_STYLE_fillColor = "fillColor";
	
	/**
	 * Numeric floating-point value in [0..1] representing the opacity of the path fill; optional.
	 */
	public static final String ITEM_PROPERTY_STYLE_fillOpacity = "fillOpacity";
	
	/**
	 * Flag that specifies if the path is editable; optional. 
	 */
	public static final String ITEM_PROPERTY_STYLE_editable = "editable";
	
	/**
	 * Flag that specifies if the path is geodesic; optional. 
	 */
	public static final String ITEM_PROPERTY_STYLE_geodesic = "geodesic";
	
	// map elements
	
	public static final String ELEMENT_MARKER_DATA = "markerData";
	public static final String ELEMENT_PATH_STYLE = "pathStyle";
	public static final String ELEMENT_PATH_DATA = "pathData";
	
	// generic element parameters:
	
	/**
	 * The name of map generic elements.
	 */
	public static final String MAP_ELEMENT_NAME = "map";
	
	/**
	 * The qualified type of Flash generic elements. 
	 */
	public static final JRGenericElementType MAP_ELEMENT_TYPE = 
		new JRGenericElementType(JRXmlConstants.JASPERREPORTS_NAMESPACE, MAP_ELEMENT_NAME);

	/**
	 * The name of the parameter that provides the onErrorType attribute.
	 */
	public static final String PARAMETER_ON_ERROR_TYPE = "onErrorType";
	
	/**
	 * The name of the parameter that provides the list of marker objects for the map.
	 */
	public static final String PARAMETER_MARKERS = "markers";
	
	/**
	 * The name of the parameter that provides the map language.
	 */
	public static final String PARAMETER_LANGUAGE = "language";
	
	/**
	 * The name of the parameter that provides the zoom factor.
	 */
	public static final String PARAMETER_ZOOM = "zoom";
	
	/**
	 * The cached image renderer.
	 */
	public static final String PARAMETER_CACHE_RENDERER = "cacheRenderer";
	
	/**
	 * The name of the parameter that provides the Google API map request parameters.
	 */
	public static final String PARAMETER_REQ_PARAMS = "reqParams";
	
	/**
	 * The name of the flag parameter that specifies paths on the map
	 */
	public static final String PARAMETER_PATHS = "paths";
	
	/**
	 * The name of the parameter that provides the path locations.
	 */
	public static final String PARAMETER_PATH_LOCATIONS = "locations";

	// map defaults:
	
	public static final Float DEFAULT_LATITUDE = 0f;
	public static final Float DEFAULT_LONGITUDE = 0f;
	public static final Integer DEFAULT_ZOOM = 0;
	public static final MapTypeEnum DEFAULT_MAP_TYPE = MapTypeEnum.ROADMAP;
	public static final OnErrorTypeEnum DEFAULT_ON_ERROR_TYPE = OnErrorTypeEnum.ERROR;
	public static final String DEFAULT_PATH_NAME = "DEFAULT_PATH_NAME";
	
	
	
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
	 * Returns a list of {@link ItemData ItemData} objects 
	 * representing collections of markers on the map
	 * 
	 * @return a list of marker data
	 * @see ItemData
	 */
	List<ItemData> getMarkerDataList();
	
	/**
	 * Returns a list of {@link ItemData ItemData} objects 
	 * representing collections of path styles for the map
	 * 
	 * @return a list of path styles
	 * @see ItemData
	 */
	List<ItemData> getPathStyleList();
	
	/**
	 * Returns a list of {@link ItemData ItemData} objects 
	 * representing collections of paths on the map
	 * 
	 * @return a list of path data
	 * @see ItemData
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
