/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2016 TIBCO Software Inc. All rights reserved.
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
package net.sf.jasperreports.chartcustomizers.utils;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Define a shape of a specific figure
 * 
 * @author Orlandin Marco
 *
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ShapeDefinition {

	/**
	 * The shape types
	 */
	public enum FigureShape{RECTANGLE, ELLIPSE, POLYLINE, POLYGON};
	
	private FigureShape shape;
	
	/**
	 * The points that compose the shape, used only for polyline and polygons
	 */
	private List<Point> points;

	public FigureShape getShape() {
		return shape;
	}

	public void setShape(FigureShape shape) {
		this.shape = shape;
	}

	public List<Point> getPoints() {
		return points;
	}

	public void setPoints(List<Point> points) {
		this.points = points;
	}
	
	/**
	 * Build  a {@link ShapeDefinition} from its json string
	 * 
	 * @param encodedShape the json string of the class
	 * @return a {@link ShapeDefinition} or null if the json is invalid
	 */
	public static ShapeDefinition decode(String encodedShape){
    	if(encodedShape == null) return null;
    	ShapeDefinition result = null;
    	try {
	    	ObjectMapper mapper = new ObjectMapper();
	    	mapper.configure(JsonGenerator.Feature.ESCAPE_NON_ASCII, true);
			result = (ShapeDefinition)mapper.readValue(encodedShape, ShapeDefinition.class);  
    	} catch (Exception ex){
    		ex.printStackTrace();
    	} 
    	return result;
	}
	
	/**
	 * Create a json string from a {@link ShapeDefinition}
	 * 
	 * @param shape a {@link ShapeDefinition}
	 * @return its json string or null if the definition is invalid
	 */
	public static String encode(ShapeDefinition shape){
    	if(shape == null) return null;
    	ObjectMapper mapper = new ObjectMapper();
    	try {
			return mapper.writeValueAsString(shape);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
    	return null;
	}
}
