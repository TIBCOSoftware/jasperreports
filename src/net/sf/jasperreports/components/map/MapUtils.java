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
	
	public static final String STATUS_OK = "OK";
	public static final String LOCATION_PREFIX = "http://maps.googleapis.com/maps/api/geocode/xml?address=";
	public static final String LOCATION_SUFFIX = "&sensor=false&output=xml&oe=utf8";
	
	public static Float[] getCoords(String address) throws JRException {
		if(address != null) {
			try {
				String location = LOCATION_PREFIX + URLEncoder.encode(address, "UTF-8") + LOCATION_SUFFIX;
				byte[] response = readData(location);
				Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new ByteArrayInputStream(response));
				Node statusNode = (Node) new DOMXPath("/GeocodeResponse/status").selectSingleNode(document);
				String status = statusNode.getTextContent();
				if(STATUS_OK.equals(status)) {
					Float[] coords = new Float[2];
					Node latNode = (Node) new DOMXPath("/GeocodeResponse/result/geometry/location/lat").selectSingleNode(document);
					coords[0] = Float.valueOf(latNode.getTextContent());
					Node lngNode = (Node) new DOMXPath("/GeocodeResponse/result/geometry/location/lng").selectSingleNode(document);
					coords[1] = Float.valueOf(lngNode.getTextContent());
					return coords;
				} else {
					throw new JRRuntimeException("Address request failed (see status: " + status + ")");
				}
			} catch (Exception e) {
				throw new JRException(e);
			}
		}
		return null;
	}
	
	protected static byte[] readData(String location) throws IOException {
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
