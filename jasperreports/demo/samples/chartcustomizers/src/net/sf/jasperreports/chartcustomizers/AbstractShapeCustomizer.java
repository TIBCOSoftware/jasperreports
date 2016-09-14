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
package net.sf.jasperreports.chartcustomizers;

import java.awt.Polygon;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.chartcustomizers.utils.Point;
import net.sf.jasperreports.chartcustomizers.utils.ShapeDefinition;
import net.sf.jasperreports.chartcustomizers.utils.ShapeDefinition.FigureShape;
import net.sf.jasperreports.engine.JRChartCustomizer;

/**
 * Abstract customizer that provide the utility methods to work with shapes
 * 
 * @author Marco Orlandin (dejawho2@users.sourceforge.net)
 */
public abstract class AbstractShapeCustomizer implements JRChartCustomizer, ConfigurableChartCustomizer {

	protected Map<String, String> configuration = null;
	
	/**
	 * Return the Width of the shape 
	 * 
	 * @return the width of the shape or null if it is not specified
	 */
	protected abstract Integer getWidth();
	
	/**
	 * Return the Height of the shape 
	 * 
	 * @return the height of the shape or null if it is not specified
	 */
	protected abstract Integer getHeight();	
	
	@Override
	public void setConfiguration(Map<String, String> properties) {
		this.configuration = properties;
	}
	
    /**
     * Build the shape from the points read from the property
     * 
     * @param points the string with the points encoded inside
     * @return the Shape or null if the string is not valid
     */
    protected Shape buildShape(String shapeType){
    	ShapeDefinition shapeDefinition = ShapeDefinition.decode(shapeType);	
    	if (shapeDefinition == null) return null;
    	
    	if (FigureShape.ELLIPSE.equals(shapeDefinition.getShape())){
    		return buildEllipse(shapeDefinition);
    	} else if (FigureShape.RECTANGLE.equals(shapeDefinition.getShape())){
    		return buildRectangle(shapeDefinition);
    	} else if (FigureShape.POLYLINE.equals(shapeDefinition.getShape())){
    		return buildPolyline(shapeDefinition);
    	} else if (FigureShape.POLYGON.equals(shapeDefinition.getShape())){
    		return buildPolygon(shapeDefinition);
    	}
    	return null;
    }
    
    /**
     * Return the width and height properties defined, if only one is defined it value will be used 
     * also for the other, if they are both undefined it will return null
     * 
     * @return a size or null if it is undefined
     */
    protected Point getSize(){
    	Integer width = getWidth();
    	Integer height = getHeight();
    	if (width == null && height == null){
    		return null;
    	} else if (width == null){
    		width = height;
    	} else if (height == null){
    		height = null;
    	}
    	return new Point(width, height);
    }
	
    /**
     * Build a polygon shape
     * 
     * @param definition the definition that contains the points of the polygon
     * @return the polygon or null if it can't be build from the current definition
     */
    protected Shape buildPolygon(ShapeDefinition definition){
    	try{
    		List<Point> points = definition.getPoints();
	    	if (points != null && !points.isEmpty()){
		    	int[] pointsX = new int[points.size()];
		    	int[] pointsY = new int[points.size()];
		    	int index = 0;
		    	
				Integer top = null;
				Integer bottom = null;
				Integer left = null;
				Integer right = null;
		    	for(Point point : points){
		    		pointsX[index] = point.getX();
		    		pointsY[index] = point.getY();
		    		
		    		if (top == null || pointsY[index] < top){
						top =  pointsY[index];
					}
					
					if (bottom == null || pointsY[index] > bottom){
						bottom =  pointsY[index];
					}
					
					if (left == null ||  pointsX[index] < left){
						left = pointsX[index];
					}
					
					if (right == null || pointsX[index] > right){
						right = pointsX[index];
					}
					
					index++;
		    	}
		    	
				int linesWidth = right - left;
				int linesHeight = bottom - top;
		    	float scaleFactorX = 1.0f;
		    	float scaleFactorY = 1.0f;
	    		Integer width = getWidth();
	    		Integer height = getHeight();
	    		if (width != null){
	    			scaleFactorX = new Float(width) / new Float(linesWidth);
	    		}
	    		if (height != null){
	    			scaleFactorY = new Float(height) / new Float(linesHeight);
	    		}
		    	
	    		for(int i = 0; i < points.size(); i++){
	    			pointsX[i] = Math.round(pointsX[i] * scaleFactorX);
	    			pointsY[i] = Math.round(pointsY[i] * scaleFactorY);
	    		}

		    	return new Polygon(pointsX, pointsY, points.size());
	    	}
    	} catch(Exception ex){
    		ex.printStackTrace();
    	}
		return null;
    }
    
    /**
     * Build a ellipse shape
     * 
     * @param definition the definition 
     * @return the ellipse or null if it can't be build from the current definition
     */
    protected Shape buildEllipse(ShapeDefinition definition){
    	Point size = getSize();
    	if (size != null){
	    	try{
	    		int width = size.getX();
	    		int height = size.getY();
	    		Ellipse2D ellipse = new Ellipse2D.Float(0, 0, width, height);
	    		return ellipse;
	    	} catch(Exception ex){
	    		ex.printStackTrace();
	    	}
    	}
		return null;
    }
    
    /**
     * Build a rectangle shape
     * 
     * @param definition the definition that contains the points of the rectangle
     * @return the rectangle or null if it can't be build from the current definition
     */
    protected Shape buildRectangle(ShapeDefinition definition){
    	Point size = getSize();
    	if (size != null){
	    	try{
	    		int width = size.getX();
	    		int height = size.getY();
	    		
	    		Rectangle2D rectangle = new Rectangle2D.Float(0, 0, width, height);
	    		return rectangle;
	    	} catch(Exception ex){
	    		ex.printStackTrace();
	    	}
    	}
		return null;
    }
    
    /**
     * Use the points stored in the definition to build a polyline
     * 
     * @param definition the definition item
     * @return a polyline shape or null if it can be built
     */
    protected Shape buildPolyline(ShapeDefinition definition){
    	try{
    		List<Point> points = definition.getPoints();
	    	if (points != null && !points.isEmpty()){
		    	double[] pointsX = new double[points.size()];
		    	double[] pointsY = new double[points.size()];
		    	int index = 0;
		    	Path2D path = new Path2D.Double();
		    	
				Double top = null;
				Double bottom = null;
				Double left = null;
				Double right = null;
		    	for(Point point : points){
		    		pointsX[index] = point.getX();
		    		pointsY[index] = point.getY();
		    		
		    		if (top == null || pointsY[index] < top){
						top =  pointsY[index];
					}
					
					if (bottom == null || pointsY[index] > bottom){
						bottom =  pointsY[index];
					}
					
					if (left == null ||  pointsX[index] < left){
						left = pointsX[index];
					}
					
					if (right == null || pointsX[index] > right){
						right = pointsX[index];
					}
					index++;
		    	}
		    	
				double linesWidth = right - left;
				double linesHeight = bottom - top;
		    	float scaleFactorX = 1.0f;
		    	float scaleFactorY = 1.0f;
	    		Integer width = getWidth();
	    		Integer height = getHeight();
	    		if (width != null){
	    			scaleFactorX = new Float(width) / new Float(linesWidth);
	    		}
	    		if (height != null){
	    			scaleFactorY = new Float(height) / new Float(linesHeight);
	    		}
		    	
		    	
		    	if (points.size() > 1){
			    	path.moveTo(pointsX[0] * scaleFactorX, pointsY[0] * scaleFactorY);
			    	for(int i = 1; i < points.size(); i++){
			    		path.lineTo(pointsX[i] * scaleFactorX, pointsY[i] * scaleFactorY);
			    	}
		    	}
		    	return path;
	    	}
    	} catch(Exception ex){
    		ex.printStackTrace();
    	}
		return null;
    }
}
