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
import java.util.Map;

import javax.xml.parsers.DocumentBuilderFactory;

import net.sf.jasperreports.components.items.Item;
import net.sf.jasperreports.components.items.ItemProperty;
import net.sf.jasperreports.components.items.fill.FillItem;
import net.sf.jasperreports.components.map.MapComponent;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.fill.JRFillExpressionEvaluator;
import net.sf.jasperreports.engine.fill.JRFillObjectFactory;
import net.sf.jasperreports.engine.type.ColorEnum;
import net.sf.jasperreports.engine.util.JRColorUtil;

import org.jaxen.dom.DOMXPath;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public class FillPlaceItem extends FillItem
{
	public static final String PROPERTY_COLOR = "color";
	public static final String EXCEPTION_MESSAGE_KEY_MISSING_COORDINATES = "components.map.missing.coordinates";

	/**
	 *
	 */
	public FillPlaceItem(
		Item item, 
		JRFillObjectFactory factory
		)
	{
		super(item, factory);
	}

	@Override
	public Object getEvaluatedValue(ItemProperty property, JRFillExpressionEvaluator evaluator, byte evaluation) throws JRException
	{
		Object result = super.getEvaluatedValue(property, evaluator, evaluation);
		return MapComponent.ITEM_PROPERTY_address.equals(property.getName())
			? getCoords((String)result)
			: (PROPERTY_COLOR.equals(property.getName()) 
				? JRColorUtil.getColorHexa(JRColorUtil.getColor((String)result, ColorEnum.RED.getColor()))
				: result);
	}
	
	@Override
	public void verifyValue(ItemProperty property, Object value) throws JRException {
	}
	
	@Override
	public void verifyValues(Map<String, Object> result) throws JRException {
		if(result != null) {
			Object latitude = result.get(MapComponent.ITEM_PROPERTY_latitude);
			Object longitude = result.get(MapComponent.ITEM_PROPERTY_longitude);
			Object address = result.get(MapComponent.ITEM_PROPERTY_address);

			Float fLatitude = null;
			if (latitude instanceof Number)
			{
				fLatitude = ((Number)latitude).floatValue();
			}
			else
			{
				String strLatitude = latitude == null ? null : String.valueOf(latitude);
				fLatitude = strLatitude == null || strLatitude.trim().length() == 0 ? null : Float.parseFloat(strLatitude);
			}
			
			Float fLongitude = null;
			if (longitude instanceof Number)
			{
				fLongitude = ((Number)longitude).floatValue();
			}
			else
			{
				String strLongitude = longitude == null ? null : String.valueOf(longitude);
				fLongitude = strLongitude == null || strLongitude.trim().length() == 0 ? null : Float.parseFloat(strLongitude);
			}
			
			if (fLatitude != null && fLongitude != null)
			{
				result.remove(MapComponent.ITEM_PROPERTY_address);
				result.put(MapComponent.ITEM_PROPERTY_latitude, fLatitude);
				result.put(MapComponent.ITEM_PROPERTY_longitude, fLongitude);
			}
			else if (address != null)
			{
				Float[] coords = (Float[])address;
				if(coords[0] != null && coords[1] != null){
					result.put(MapComponent.ITEM_PROPERTY_latitude, coords[0]);
					result.put(MapComponent.ITEM_PROPERTY_longitude, coords[1]);
					result.remove(MapComponent.ITEM_PROPERTY_address);
				} else {
					throw 
						new JRException(
							MapFillComponent.EXCEPTION_MESSAGE_KEY_INVALID_ADDRESS_COORDINATES,  
							new Object[]{coords[0], coords[1]} 
							);
				}
			}
			else 
			{
				throw 
					new JRException(
						EXCEPTION_MESSAGE_KEY_MISSING_COORDINATES,  
						new Object[]{fLatitude == null ? MapComponent.ITEM_PROPERTY_latitude : MapComponent.ITEM_PROPERTY_longitude}
						);
			}
		}
	}
	
	private Float[] getCoords(String address) throws JRException {
		Float[] coords = null;
		if(address != null) {
			try {
				String url = MapFillComponent.PLACE_URL_PREFIX + URLEncoder.encode(address, MapFillComponent.DEFAULT_ENCODING) + MapFillComponent.PLACE_URL_SUFFIX;
				byte[] response = read(url);
				Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new ByteArrayInputStream(response));
				Node statusNode = (Node) new DOMXPath(MapFillComponent.STATUS_NODE).selectSingleNode(document);
				String status = statusNode.getTextContent();
				if(MapFillComponent.STATUS_OK.equals(status)) {
					coords = new Float[2];
					Node latNode = (Node) new DOMXPath(MapFillComponent.LATITUDE_NODE).selectSingleNode(document);
					coords[0] = Float.valueOf(latNode.getTextContent());
					Node lngNode = (Node) new DOMXPath(MapFillComponent.LONGITUDE_NODE).selectSingleNode(document);
					coords[1] = Float.valueOf(lngNode.getTextContent());
				} else {
					throw 
						new JRException(
							MapFillComponent.EXCEPTION_MESSAGE_KEY_ADDRESS_REQUEST_FAILED,  
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
