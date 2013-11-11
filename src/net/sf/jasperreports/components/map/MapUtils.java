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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLEncoder;

import javax.xml.parsers.DocumentBuilderFactory;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRRuntimeException;

import org.jaxen.dom.DOMXPath;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

public class MapUtils {
	
	public static final String PLACE_URL_PREFIX = "http://maps.googleapis.com/maps/api/geocode/xml?address=";
	public static final String PLACE_URL_SUFFIX = "&sensor=false&output=xml&oe=utf8";
	public static final String DEFAULT_ENCODING = "UTF-8";
	public static final String STATUS_NODE = "/GeocodeResponse/status";
	public static final String LATITUDE_NODE = "/GeocodeResponse/result/geometry/location/lat";
	public static final String LONGITUDE_NODE = "/GeocodeResponse/result/geometry/location/lng";
	public static final String STATUS_OK = "OK";
	
	public static Float[] getCoords(String address) throws JRException {
		Float[] coords = null;
		if(address != null) {
			try {
				String location = PLACE_URL_PREFIX + URLEncoder.encode(address, DEFAULT_ENCODING) + PLACE_URL_SUFFIX;
				byte[] response = readData(location);
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
					throw new JRRuntimeException("Address request failed (see status: " + status + ")");
				}
			} catch (Exception e) {
				throw new JRException(e);
			}
		}
		return coords;
	}
	
	private static byte[] readData(String location) throws IOException {
		InputStream stream = null;
		try {
			URL url = new URL(location);
			stream = url.openStream();
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
