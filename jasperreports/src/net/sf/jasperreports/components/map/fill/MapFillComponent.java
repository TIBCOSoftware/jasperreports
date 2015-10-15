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
package net.sf.jasperreports.components.map.fill;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.xml.parsers.DocumentBuilderFactory;

import net.sf.jasperreports.components.items.ItemData;
import net.sf.jasperreports.components.map.MapComponent;
import net.sf.jasperreports.components.map.type.MapImageTypeEnum;
import net.sf.jasperreports.components.map.type.MapScaleEnum;
import net.sf.jasperreports.components.map.type.MapTypeEnum;
import net.sf.jasperreports.engine.JRComponentElement;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRGenericPrintElement;
import net.sf.jasperreports.engine.JRPrintElement;
import net.sf.jasperreports.engine.JRPropertiesHolder;
import net.sf.jasperreports.engine.JRPropertiesUtil;
import net.sf.jasperreports.engine.component.BaseFillComponent;
import net.sf.jasperreports.engine.component.FillContext;
import net.sf.jasperreports.engine.component.FillContextProvider;
import net.sf.jasperreports.engine.component.FillPrepareResult;
import net.sf.jasperreports.engine.fill.JRFillObjectFactory;
import net.sf.jasperreports.engine.fill.JRTemplateGenericElement;
import net.sf.jasperreports.engine.fill.JRTemplateGenericPrintElement;
import net.sf.jasperreports.engine.type.EvaluationTimeEnum;
import net.sf.jasperreports.engine.type.OnErrorTypeEnum;

import org.jaxen.dom.DOMXPath;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

/**
 * 
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public class MapFillComponent extends BaseFillComponent implements FillContextProvider
{
	public static final String PLACE_URL_PREFIX = "https://maps.googleapis.com/maps/api/geocode/xml?address=";
	public static final String PLACE_URL_SUFFIX = "&sensor=false&output=xml&oe=utf8";
	public static final String DEFAULT_ENCODING = "UTF-8";
	public static final String STATUS_NODE = "/GeocodeResponse/status";
	public static final String LATITUDE_NODE = "/GeocodeResponse/result/geometry/location/lat";
	public static final String LONGITUDE_NODE = "/GeocodeResponse/result/geometry/location/lng";
	public static final String STATUS_OK = "OK";
	
	public static final String EXCEPTION_MESSAGE_KEY_NULL_OR_EMPTY_VALUE_NOT_ALLOWED = "components.map.null.or.empty.value.not.allowed";
	public static final String EXCEPTION_MESSAGE_KEY_NULL_OR_EMPTY_VALUES_NOT_ALLOWED = "components.map.null.or.empty.values.not.allowed";
	public static final String EXCEPTION_MESSAGE_KEY_INVALID_ADDRESS_COORDINATES = "components.map.invalid.address.coordinates";
	public static final String EXCEPTION_MESSAGE_KEY_ADDRESS_REQUEST_FAILED = "components.map.address.request.failed";
	
	private final MapComponent mapComponent;
	
	private Float latitude;
	private Float longitude;
	private String center;
	private Integer zoom;
	private String language;
	private MapTypeEnum mapType;
	private MapScaleEnum mapScale;
	private MapImageTypeEnum imageType;
	private OnErrorTypeEnum onErrorType;
	private String clientId;
	private String signature;
	private String key;
	private String version;

	private List<FillItemData> markerDataList;
	private List<FillItemData> pathStyleList;
	private List<FillItemData> pathDataList;
	private List<Map<String,Object>> markers;
	private Map<String, Map<String,Object>> styles;
	private List<Map<String,Object>> paths;
	
	JRFillObjectFactory factory;
	
	public MapFillComponent(MapComponent map)
	{
		this.mapComponent = map;
	}
	
	public MapFillComponent(MapComponent map, JRFillObjectFactory factory)
	{
		this.mapComponent = map;
		this.factory = factory;
		
		if(mapComponent.getMarkerDataList() != null){
			markerDataList = new ArrayList<FillItemData>();
			for(ItemData markerData : mapComponent.getMarkerDataList()) {
				markerDataList.add(new FillPlaceItemData(this, markerData, factory));
			}
		}
		if(mapComponent.getPathStyleList() != null){
			pathStyleList = new ArrayList<FillItemData>();
			for(ItemData pathStyle : mapComponent.getPathStyleList()) {
				pathStyleList.add(new FillStyleItemData(this, pathStyle, factory));
			}
		}
		if(mapComponent.getPathDataList() != null){
			pathDataList = new ArrayList<FillItemData>();
			for(ItemData pathData : mapComponent.getPathDataList()) {
				pathDataList.add(new FillPlaceItemData(this, pathData, factory));
			}
		}
	}
	
	protected MapComponent getMap()
	{
		return mapComponent;
	}
	
	public FillContext getFillContext()
	{
		return fillContext;
	}
	
	public void evaluate(byte evaluation) throws JRException
	{
		if (isEvaluateNow())
		{
			evaluateMap(evaluation);
		}
	}
	
	protected void evaluateMap(byte evaluation) throws JRException
	{
		JRPropertiesHolder propertiesHolder = fillContext.getComponentElement().getParentProperties();
		JRPropertiesUtil util = JRPropertiesUtil.getInstance(fillContext.getFiller().getJasperReportsContext());
		clientId = util.getProperty(propertiesHolder, MapComponent.PROPERTY_CLIENT_ID);
		signature = util.getProperty(propertiesHolder, MapComponent.PROPERTY_SIGNATURE);
		key = util.getProperty(propertiesHolder, MapComponent.PROPERTY_KEY);
		version = util.getProperty(propertiesHolder, MapComponent.PROPERTY_VERSION);
		
		Number lat = (Number)fillContext.evaluate(mapComponent.getLatitudeExpression(), evaluation);
		latitude = lat == null ? null : lat.floatValue();
		
		Number lg = (Number)fillContext.evaluate(mapComponent.getLongitudeExpression(), evaluation);
		longitude = lg == null ? null : lg.floatValue();

		if(latitude == null || longitude == null) {
			center = (String)fillContext.evaluate(mapComponent.getAddressExpression(), evaluation);
			Float[] coords = getCoords(center);
			if(coords != null && coords[0] != null && coords[1] != null){
				latitude = coords[0];
				longitude = coords[1];
			} else {
				throw 
					new JRException(
						EXCEPTION_MESSAGE_KEY_INVALID_ADDRESS_COORDINATES,  
						new Object[]{MapComponent.ITEM_PROPERTY_latitude, MapComponent.ITEM_PROPERTY_longitude} 
						);
			}
		}
		zoom = (Integer)fillContext.evaluate(mapComponent.getZoomExpression(), evaluation);
		zoom = zoom == null ? MapComponent.DEFAULT_ZOOM : zoom;
		if(mapComponent.getLanguageExpression() != null)
		{
			language = (String)fillContext.evaluate(mapComponent.getLanguageExpression(), evaluation);
		}
		else
		{
			Locale locale = fillContext.getReportLocale();
			if(locale != null)
			{
				language = locale.getLanguage();
			}
		}
		mapType = mapComponent.getMapType() == null? MapTypeEnum.ROADMAP : mapComponent.getMapType();
		mapScale = mapComponent.getMapScale();
		imageType = mapComponent.getImageType();

		if(markerDataList != null) {
			markers = new ArrayList<Map<String,Object>>();
			
			for(FillItemData markerData : markerDataList) {
				List<Map<String,Object>> currentItemList = markerData.getEvaluateItems(evaluation);
				if(currentItemList != null && !currentItemList.isEmpty()){
					for(Map<String,Object> currentItem : currentItemList){
						if(currentItem != null){
							markers.add(currentItem);
						}
					}
				}
			}
		}
		
		if(pathDataList != null) {
			addPathStyles(evaluation);
			paths = new ArrayList<Map<String,Object>>();
			Map<String, Map<String,Object>> pathIds = new HashMap<String,Map<String,Object>>();

			for(FillItemData pathData : pathDataList) {
				List<Map<String,Object>> currentItemList = pathData.getEvaluateItems(evaluation);
				if(currentItemList != null && !currentItemList.isEmpty()){
					for(Map<String,Object> currentItem : currentItemList){
						if(currentItem != null){
							String pathName = currentItem.get(MapComponent.ITEM_PROPERTY_name) != null ? (String)currentItem.get(MapComponent.ITEM_PROPERTY_name) : MapComponent.DEFAULT_PATH_NAME;
							Map<String,Object> pathMap = null;
							if(pathIds.containsKey(pathName)){
								pathMap = pathIds.get(pathName);
							} else {
								pathMap = new HashMap<String,Object>();
								pathMap.put(MapComponent.PARAMETER_PATH_LOCATIONS, new ArrayList<Map<String,Object>>());
								pathIds.put(pathName, pathMap);
								paths.add(pathMap);
							}
							setStyle((String)currentItem.get(MapComponent.ITEM_PROPERTY_style), pathMap);
							boolean coordSet = false;
							for(String key : currentItem.keySet()){
								if(!(MapComponent.ITEM_PROPERTY_name.equals(key) || MapComponent.ITEM_PROPERTY_style.equals(key))){
									if(MapComponent.ITEM_PROPERTY_latitude.equals(key) || MapComponent.ITEM_PROPERTY_longitude.equals(key)){
										if(!coordSet){
											if(currentItem.get(MapComponent.ITEM_PROPERTY_latitude) == null || currentItem.get(MapComponent.ITEM_PROPERTY_longitude) == null){
												throw new JRException(
														EXCEPTION_MESSAGE_KEY_NULL_OR_EMPTY_VALUES_NOT_ALLOWED,  
														new Object[]{MapComponent.ITEM_PROPERTY_latitude, MapComponent.ITEM_PROPERTY_longitude}
														);
											}
											Map<String,Object> location = new HashMap<String,Object>();
											location.put(MapComponent.ITEM_PROPERTY_latitude, currentItem.get(MapComponent.ITEM_PROPERTY_latitude));
											location.put(MapComponent.ITEM_PROPERTY_longitude, currentItem.get(MapComponent.ITEM_PROPERTY_longitude));
											((List<Map<String,Object>>)pathMap.get(MapComponent.PARAMETER_PATH_LOCATIONS)).add(location);
											coordSet = true;
										}
									} else {
										pathMap.put(key, currentItem.get(key));
									}
								}
							}
						}
					}
				}
			}
		}
		onErrorType = mapComponent.getOnErrorType();
	}

	protected void addPathStyles(byte evaluation) throws JRException{
		styles = new HashMap<String, Map<String,Object>>();
		if (pathStyleList != null)
		{
			for(FillItemData styleData : pathStyleList){
				List<Map<String,Object>> currentStyleList = styleData.getEvaluateItems(evaluation);
				if(currentStyleList != null && !currentStyleList.isEmpty()){
					for(Map<String,Object> currentStyle : currentStyleList){
						String styleName = (String)currentStyle.get(MapComponent.ITEM_PROPERTY_name);
						if(styleName == null){
							throw 
								new JRException(
									EXCEPTION_MESSAGE_KEY_NULL_OR_EMPTY_VALUE_NOT_ALLOWED,  
									new Object[]{MapComponent.ITEM_PROPERTY_name}
									);
						}
						Map<String,Object> styleMap = null;
						if(styles.containsKey(styleName)){
							styleMap = styles.get(styleName);
						} else {
							styleMap = new HashMap<String,Object>();
							styles.put(styleName, styleMap);
						}
						setStyle(currentStyle, styleMap);
					}
				}
			}
		}
	}
	
	protected void setStyle(String styleName, Map<String,Object> styleMap){
		if(styleName != null){
			Map<String,Object> parentStyleMap = styles.get(styleName);
			if(parentStyleMap != null && ! parentStyleMap.isEmpty()){
				String parentStyleName = (String)parentStyleMap.get(MapComponent.ITEM_PROPERTY_style);
				if(parentStyleName != null){
					setStyle(parentStyleName, styleMap);
				}
				setStyle(parentStyleMap, styleMap);
			}
		}
	}
	
	protected void setStyle(Map<String,Object> parentStyleMap, Map<String,Object> styleMap){
		if(parentStyleMap != null){
			for(String styleProperty : parentStyleMap.keySet()) {
				if(!(MapComponent.ITEM_PROPERTY_name.equals(styleProperty) 
						|| MapComponent.ITEM_PROPERTY_latitude.equals(styleProperty) 
						|| MapComponent.ITEM_PROPERTY_longitude.equals(styleProperty))
						&& parentStyleMap.get(styleProperty) != null && parentStyleMap.get(styleProperty).toString().length() > 0){
					styleMap.put(styleProperty, parentStyleMap.get(styleProperty));
				}
			}
		}
	}
	
	protected boolean isEvaluateNow()
	{
		return mapComponent.getEvaluationTime() == EvaluationTimeEnum.NOW;
	}

	public FillPrepareResult prepare(int availableHeight)
	{
		return FillPrepareResult.PRINT_NO_STRETCH;
//		return isEvaluateNow() && (latitude == null || longitude == null)  
//				? FillPrepareResult.NO_PRINT_NO_OVERFLOW
//				: FillPrepareResult.PRINT_NO_STRETCH;
	}

	public JRPrintElement fill()
	{
		JRComponentElement element = fillContext.getComponentElement();
		JRTemplateGenericElement template = new JRTemplateGenericElement(
				fillContext.getElementOrigin(), 
				fillContext.getDefaultStyleProvider(),
				MapComponent.MAP_ELEMENT_TYPE);
		template = deduplicate(template);
		JRTemplateGenericPrintElement printElement = new JRTemplateGenericPrintElement(template, printElementOriginator);
		printElement.setUUID(element.getUUID());
		printElement.setX(element.getX());
		printElement.setY(fillContext.getElementPrintY());
		printElement.setWidth(element.getWidth());
		printElement.setHeight(element.getHeight());

		if (isEvaluateNow())
		{
			copy(printElement);
		}
		else
		{
			fillContext.registerDelayedEvaluation(printElement, 
					mapComponent.getEvaluationTime(), mapComponent.getEvaluationGroup());
		}
		
		return printElement;
	}

	public void evaluateDelayedElement(JRPrintElement element, byte evaluation)
			throws JRException
	{
		evaluateMap(evaluation);
		copy((JRGenericPrintElement) element);
	}

	protected void copy(JRGenericPrintElement printElement)
	{
		printElement.setParameterValue(MapComponent.ITEM_PROPERTY_latitude, latitude);
		printElement.setParameterValue(MapComponent.ITEM_PROPERTY_longitude, longitude);
		printElement.setParameterValue(MapComponent.PARAMETER_ZOOM, zoom);
		String reqParams = "";
		if(language != null)
		{
			reqParams += "&language=" + language;
		}
		if(clientId != null) {
			reqParams += "&client=" + clientId;
			if(signature != null) {
				reqParams += "&signature=" + signature;
			}
		} else if(key != null) {
			reqParams += "&key=" + key;
		}
		if(version != null) {
			reqParams += "&v=" + version;
		}
		if(reqParams.length() > 0) {
			printElement.setParameterValue(MapComponent.PARAMETER_REQ_PARAMS, reqParams);
		}
		if(mapType != null)
		{
			printElement.setParameterValue(MapComponent.ATTRIBUTE_MAP_TYPE, mapType.getName());
		}
		if(mapScale != null)
		{
			printElement.setParameterValue(MapComponent.ATTRIBUTE_MAP_SCALE, mapScale.getName());
		}
		if(imageType != null)
		{
			printElement.setParameterValue(MapComponent.ATTRIBUTE_IMAGE_TYPE, imageType.getName());
		}
		if(onErrorType != null)
		{
			printElement.setParameterValue(MapComponent.PARAMETER_ON_ERROR_TYPE, onErrorType.getName());
		}
		if(markers != null && !markers.isEmpty())
		{
			printElement.setParameterValue(MapComponent.PARAMETER_MARKERS, markers);
		}
		if(paths != null && !paths.isEmpty())
		{
			printElement.setParameterValue(MapComponent.PARAMETER_PATHS, paths);
		}
	}
	
	private Float[] getCoords(String address) throws JRException {
		Float[] coords = null;
		if(address != null) {
			try {
				String url = PLACE_URL_PREFIX + URLEncoder.encode(address, DEFAULT_ENCODING) + PLACE_URL_SUFFIX;
				byte[] response = read(url);
				Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new ByteArrayInputStream(response));
				Node statusNode = (Node) new DOMXPath(STATUS_NODE).selectSingleNode(document);
				String status = statusNode.getTextContent();
				if(STATUS_OK.equals(status)) {
					coords = new Float[2];
					Node latNode = (Node) new DOMXPath(LATITUDE_NODE).selectSingleNode(document);
					coords[0] = Float.valueOf(latNode.getTextContent());
					Node lngNode = (Node) new DOMXPath(LONGITUDE_NODE).selectSingleNode(document);
					coords[1] = Float.valueOf(lngNode.getTextContent());
				} else {
					throw 
						new JRException(
							EXCEPTION_MESSAGE_KEY_ADDRESS_REQUEST_FAILED,  
							new Object[]{status} 
							);
				}
			} catch (Exception e) {
				throw new JRException(e);
			}
		}
		return coords;
	}
	
	private byte[] read(String url) throws IOException {
		InputStream stream = null;
		try {
			URL u = new URL(url);
			stream = u.openStream();
			ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
			byte[] buf = new byte[4096];
			int read;
			while ((read = stream.read(buf)) > 0) {
				byteOut.write(buf, 0, read);
			}
			return byteOut.toByteArray();
		} finally {
			if(stream != null) {
				stream.close();
			}
		}
	}
	
}
