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

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import net.sf.jasperreports.components.items.ItemData;
import net.sf.jasperreports.components.items.StandardItemData;
import net.sf.jasperreports.components.map.type.MapImageTypeEnum;
import net.sf.jasperreports.components.map.type.MapScaleEnum;
import net.sf.jasperreports.components.map.type.MapTypeEnum;
import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.base.JRBaseObjectFactory;
import net.sf.jasperreports.engine.design.events.JRChangeEventsSupport;
import net.sf.jasperreports.engine.design.events.JRPropertyChangeSupport;
import net.sf.jasperreports.engine.type.EvaluationTimeEnum;
import net.sf.jasperreports.engine.type.OnErrorTypeEnum;
import net.sf.jasperreports.engine.util.JRCloneUtils;

/**
 * 
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public class StandardMapComponent implements MapComponent, Serializable, JRChangeEventsSupport
{

	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;
	
	public static final String PROPERTY_LATITUDE_EXPRESSION = "latitudeExpression";
	public static final String PROPERTY_LONGITUDE_EXPRESSION = "longitudeExpression";
	public static final String PROPERTY_ADDRESS_EXPRESSION = "addressExpression";
	public static final String PROPERTY_ZOOM_EXPRESSION = "zoomExpression";
	public static final String PROPERTY_LANGUAGE_EXPRESSION = "languageExpression";
	public static final String PROPERTY_EVALUATION_TIME = "evaluationTime";
	public static final String PROPERTY_EVALUATION_GROUP = "evaluationGroup";
	public static final String PROPERTY_MAP_TYPE = "mapType";
	public static final String PROPERTY_MAP_SCALE = "mapScale";
	public static final String PROPERTY_IMAGE_TYPE = "imageType";
	public static final String PROPERTY_ON_ERROR_TYPE = "onErrorType";
	public static final String PROPERTY_MARKER_DATA_LIST = "markerDataList";
	public static final String PROPERTY_PATH_STYLE_LIST = "pathStyleList";
	public static final String PROPERTY_PATH_DATA_LIST = "pathDataList";
	
	/**
	 * @deprecated Replaced by {@link #PROPERTY_MARKER_DATA}.
	 */
	public static final String PROPERTY_MARKER_DATASET = "markerDataset";

	/**
	 * @deprecated Replaced by {@link #PROPERTY_MARKER_DATA_LIST}.
	 */
	public static final String PROPERTY_MARKER_DATA = "markerData";
	
	private JRExpression latitudeExpression;
	private JRExpression longitudeExpression;
	private JRExpression addressExpression;
	private JRExpression zoomExpression;
	private JRExpression languageExpression;
	private EvaluationTimeEnum evaluationTime = EvaluationTimeEnum.NOW;
	private String evaluationGroup;
	private MapTypeEnum mapType;
	private MapScaleEnum mapScale;
	private MapImageTypeEnum imageType;

	private OnErrorTypeEnum onErrorType;
	private List<ItemData> markerDataList = new ArrayList<ItemData>();
	private List<ItemData> pathStyleList = new ArrayList<ItemData>();
	private List<ItemData> pathDataList = new ArrayList<ItemData>();
	
	private transient JRPropertyChangeSupport eventSupport;

	public StandardMapComponent()
	{
	}

	public StandardMapComponent(MapComponent map, JRBaseObjectFactory objectFactory)
	{
		this.latitudeExpression = objectFactory.getExpression(map.getLatitudeExpression());
		this.longitudeExpression = objectFactory.getExpression(map.getLongitudeExpression());
		this.addressExpression = objectFactory.getExpression(map.getAddressExpression());
		this.zoomExpression = objectFactory.getExpression(map.getZoomExpression());
		this.languageExpression = objectFactory.getExpression(map.getLanguageExpression());
		this.evaluationTime = map.getEvaluationTime();
		this.evaluationGroup = map.getEvaluationGroup();
		this.mapType = map.getMapType();
		this.mapScale = map.getMapScale();
		this.imageType = map.getImageType();
		List<ItemData> markerList = map.getMarkerDataList();
		if(markerList != null && markerList.size() > 0)
		{
			this.markerDataList = new ArrayList<ItemData>();
			for(ItemData markerData : markerList){
				this.markerDataList.add(new StandardItemData(markerData, objectFactory));
			}
		} 
		this.onErrorType = map.getOnErrorType();
		List<ItemData> styleList = map.getPathStyleList();
		if(styleList != null && styleList.size() > 0)
		{
			this.pathStyleList = new ArrayList<ItemData>();
			for(ItemData pathStyle : styleList){
				pathStyleList.add(new StandardItemData(pathStyle, objectFactory));
			}
		}
		List<ItemData> pathList = map.getPathDataList();
		if(pathList != null && pathList.size() > 0)
		{
			this.pathDataList = new ArrayList<ItemData>();
			for(ItemData pathData : pathList){
				pathDataList.add(new StandardItemData(pathData, objectFactory));
			}
		}
	}
	
	public JRExpression getLatitudeExpression()
	{
		return latitudeExpression;
	}

	public void setLatitudeExpression(JRExpression latitudeExpression)
	{
		Object old = this.latitudeExpression;
		this.latitudeExpression = latitudeExpression;
		getEventSupport().firePropertyChange(PROPERTY_LATITUDE_EXPRESSION, old, this.latitudeExpression);
	}

	public JRExpression getLongitudeExpression()
	{
		return longitudeExpression;
	}
	
	public void setLongitudeExpression(JRExpression longitudeExpression)
	{
		Object old = this.longitudeExpression;
		this.longitudeExpression = longitudeExpression;
		getEventSupport().firePropertyChange(PROPERTY_LONGITUDE_EXPRESSION, old, this.longitudeExpression);
	}
	
	public JRExpression getAddressExpression()
	{
		return addressExpression;
	}

	public void setAddressExpression(JRExpression addressExpression)
	{
		Object old = this.addressExpression;
		this.addressExpression = addressExpression;
		getEventSupport().firePropertyChange(PROPERTY_ADDRESS_EXPRESSION, old, this.addressExpression);
	}

	public JRExpression getZoomExpression()
	{
		return zoomExpression;
	}
	
	public void setZoomExpression(JRExpression zoomExpression)
	{
		Object old = this.zoomExpression;
		this.zoomExpression = zoomExpression;
		getEventSupport().firePropertyChange(PROPERTY_ZOOM_EXPRESSION, old, this.zoomExpression);
	}
	
	public JRExpression getLanguageExpression()
	{
		return languageExpression;
	}

	public void setLanguageExpression(JRExpression languageExpression)
	{
		Object old = this.languageExpression;
		this.languageExpression = languageExpression;
		getEventSupport().firePropertyChange(PROPERTY_LANGUAGE_EXPRESSION, old, this.languageExpression);
	}

	public EvaluationTimeEnum getEvaluationTime()
	{
		return evaluationTime;
	}

	public void setEvaluationTime(EvaluationTimeEnum evaluationTimeValue)
	{
		Object old = this.evaluationTime;
		this.evaluationTime = evaluationTimeValue;
		getEventSupport().firePropertyChange(PROPERTY_EVALUATION_TIME, old, this.evaluationTime);
	}

	public String getEvaluationGroup()
	{
		return evaluationGroup;
	}

	public void setEvaluationGroup(String evaluationGroup)
	{
		Object old = this.evaluationGroup;
		this.evaluationGroup = evaluationGroup;
		getEventSupport().firePropertyChange(PROPERTY_EVALUATION_GROUP, 
				old, this.evaluationGroup);
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
		StandardMapComponent clone = null;
		try
		{
			clone = (StandardMapComponent) super.clone();
		}
		catch (CloneNotSupportedException e)
		{
			// never
			throw new JRRuntimeException(e);
		}
		clone.latitudeExpression = JRCloneUtils.nullSafeClone(latitudeExpression);
		clone.longitudeExpression = JRCloneUtils.nullSafeClone(longitudeExpression);
		clone.addressExpression = JRCloneUtils.nullSafeClone(addressExpression);
		clone.zoomExpression = JRCloneUtils.nullSafeClone(zoomExpression);
		clone.languageExpression = JRCloneUtils.nullSafeClone(languageExpression);
		clone.markerDataList = JRCloneUtils.cloneList(markerDataList);
		clone.pathStyleList = JRCloneUtils.cloneList(pathStyleList);
		clone.pathDataList = JRCloneUtils.cloneList(pathDataList);
		clone.eventSupport = null;
		return clone;
	}

	public MapTypeEnum getMapType() {
		return mapType;
	}

	public void setMapType(MapTypeEnum mapType) {
		Object old = this.mapType;
		this.mapType = mapType;
		getEventSupport().firePropertyChange(PROPERTY_MAP_TYPE, old, this.mapType);
	}

	public MapScaleEnum getMapScale() {
		return mapScale;
	}
	
	public void setMapScale(MapScaleEnum mapScale) {
		Object old = this.mapScale;
		this.mapScale = mapScale;
		getEventSupport().firePropertyChange(PROPERTY_MAP_SCALE, old, this.mapScale);
	}
	
	public MapImageTypeEnum getImageType() {
		return imageType;
	}

	public void setImageType(MapImageTypeEnum imageType) {
		Object old = this.imageType;
		this.imageType = imageType;
		getEventSupport().firePropertyChange(PROPERTY_IMAGE_TYPE, old, this.imageType);
	}

	/**
	 * @deprecated Replaced by {@link #getMarkerDataList()}.
	 */
	public ItemData getMarkerData() {
		return !markerDataList.isEmpty() ? markerDataList.get(0) : null;
	}

	/**
	 * @deprecated Replaced by {@link #addMarkerData(ItemData)}.
	 */
	public void setMarkerData(ItemData markerData) {
		addMarkerData(markerData);
	}


	public OnErrorTypeEnum getOnErrorType() {
		return onErrorType;
	}

	public void setOnErrorType(OnErrorTypeEnum onErrorType) {
		Object old = this.onErrorType;
		this.onErrorType = onErrorType;
		getEventSupport().firePropertyChange(PROPERTY_ON_ERROR_TYPE, old, this.onErrorType);
	}


	/**
	 * @deprecated Replaced by {@link #getMarkerData()}.
	 */
	public MarkerDataset getMarkerDataset() {
		return markerDataset; //FIXMEMAP make dummy marker dataset
	}

	/**
	 * @deprecated Replaced by {@link #setMarkerData(ItemData)}.
	 */
	public void setMarkerDataset(MarkerDataset markerDataset) {
		setMarkerData(StandardMarkerDataset.getItemData(markerDataset));
	}

	/*
	 * These fields are only for serialization backward compatibility.
	 */
	private int PSEUDO_SERIAL_VERSION_UID = JRConstants.PSEUDO_SERIAL_VERSION_UID; //NOPMD
	/**
	 * @deprecated
	 */
	private MarkerDataset markerDataset;
	/**
	 * @deprecated Replaced by {@link #markerDataList}.
	 */
	private ItemData markerData;
	
	@SuppressWarnings("deprecation")
	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException
	{
		in.defaultReadObject();
		
		if (PSEUDO_SERIAL_VERSION_UID < JRConstants.PSEUDO_SERIAL_VERSION_UID_5_5_2)
		{
			if (markerDataset != null)
			{
				if (PSEUDO_SERIAL_VERSION_UID < JRConstants.PSEUDO_SERIAL_VERSION_UID_3_1_0){
					markerData = StandardMarkerDataset.getItemData(markerDataset);
				} else {
					this.markerDataList = new ArrayList<ItemData>();
					this.markerDataList.add(StandardMarkerDataset.getItemData(markerDataset));
				}
			}
			markerDataset = null;
			
			if (markerData != null)
			{
				this.markerDataList = new ArrayList<ItemData>();
				this.markerDataList.add(markerData);
			}
			markerData = null;
		}
	}

	@Override
	public List<ItemData> getPathStyleList() {
		return this.pathStyleList;
	}
	
	/**
	 *
	 */
	public void addPathStyle(ItemData pathStyle)
	{
		pathStyleList.add(pathStyle);
		getEventSupport().fireCollectionElementAddedEvent(PROPERTY_PATH_STYLE_LIST, pathStyle, pathStyleList.size() - 1);
	}
	
	/**
	 *
	 */
	public void addPathStyle(int index, ItemData pathStyle)
	{
		if(index >=0 && index < pathStyleList.size())
			pathStyleList.add(index, pathStyle);
		else{
			pathStyleList.add(pathStyle);
			index = pathStyleList.size() - 1;
		}
		getEventSupport().fireCollectionElementAddedEvent(PROPERTY_PATH_STYLE_LIST, pathStyleList, index);
	}
	
	/**
	 *
	 */
	public ItemData removePathStyle(ItemData pathStyle)
	{
		if (pathStyle != null)
		{
			int idx = pathStyleList.indexOf(pathStyle);
			if (idx >= 0)
			{
				pathStyleList.remove(idx);
				getEventSupport().fireCollectionElementRemovedEvent(PROPERTY_PATH_STYLE_LIST, pathStyle, idx);
			}
		}
		return pathStyle;
	}
	
	@Override
	public List<ItemData> getMarkerDataList() {
		return this.markerDataList;
	}
	
	/**
	 *
	 */
	public void addMarkerData(ItemData markerData)
	{
		markerDataList.add(markerData);
		getEventSupport().fireCollectionElementAddedEvent(PROPERTY_MARKER_DATA_LIST, markerData, markerDataList.size() - 1);
	}
	
	/**
	 *
	 */
	public void addMarkerData(int index, ItemData markerData)
	{
		if(index >=0 && index < markerDataList.size())
			markerDataList.add(index, markerData);
		else{
			markerDataList.add(markerData);
			index = markerDataList.size() - 1;
		}
		getEventSupport().fireCollectionElementAddedEvent(PROPERTY_MARKER_DATA_LIST, markerDataList, index);
	}
	
	/**
	 *
	 */
	public ItemData removeMarkerData(ItemData markerData)
	{
		if (markerData != null)
		{
			int idx = markerDataList.indexOf(markerData);
			if (idx >= 0)
			{
				markerDataList.remove(idx);
				getEventSupport().fireCollectionElementRemovedEvent(PROPERTY_MARKER_DATA_LIST, markerData, idx);
			}
		}
		return markerData;
	}
	
	@Override
	public List<ItemData> getPathDataList() {
		return this.pathDataList;
	}
	
	/**
	 *
	 */
	public void addPathData(ItemData pathData)
	{
		pathDataList.add(pathData);
		getEventSupport().fireCollectionElementAddedEvent(PROPERTY_PATH_DATA_LIST, pathData, pathDataList.size() - 1);
	}
	
	/**
	 *
	 */
	public void addPathData(int index, ItemData pathData)
	{
		if(index >=0 && index < pathDataList.size())
			pathDataList.add(index, pathData);
		else{
			pathDataList.add(pathData);
			index = pathDataList.size() - 1;
		}
		getEventSupport().fireCollectionElementAddedEvent(PROPERTY_PATH_DATA_LIST, pathDataList, index);
	}

	/**
	 *
	 */
	public ItemData removePathData(ItemData pathData)
	{
		if (pathData != null)
		{
			int idx = pathDataList.indexOf(pathData);
			if (idx >= 0)
			{
				pathDataList.remove(idx);
				getEventSupport().fireCollectionElementRemovedEvent(PROPERTY_PATH_DATA_LIST, pathData, idx);
			}
		}
		return pathData;
	}
	
}
