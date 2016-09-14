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

import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;

import net.sf.jasperreports.chartcustomizers.utils.ChartCustomizerUtils;
import net.sf.jasperreports.chartcustomizers.utils.Point;
import net.sf.jasperreports.chartcustomizers.utils.ShapeDefinition;
import net.sf.jasperreports.engine.JRChart;

/**
 * Chart customizer to define the shape and the size of the symbol along the line.
 * It works for {@link LineAndShapeRenderer} and {@link XYLineAndShapeRenderer}
 * 
 * @author Marco Orlandin (dejawho2@users.sourceforge.net)
 */
public class LineDotShapeCustomizer extends AbstractShapeCustomizer {

	public static final String LINE_ITEM_INDEX_KEY = "lineDotItemIndexKey";
	
	public static final String LINE_ITEM_SERIES_KEY = "lineDotItemSeriesKey";
	
	public static final String ALL_LINE_ITEMS_KEY = "allLineDotItems";
	
	public static final String LINE_SHAPE_DEFINITION = "lineDotShapeDefinition";
	
	public static final String LINE_SHAPE_WIDTH_KEY = "lineDotShapeWidth";
	
	public static final String LINE_SHAPE_HEIGHT_KEY = "lineDotShapeHeight";
	
	@Override
    public void customize(JFreeChart jfc, JRChart jrc) {
        if (jfc.getPlot() instanceof XYPlot){
            XYPlot plot = jfc.getXYPlot();
    		Integer seriesItemIndex = resolveIndex(plot);
    		if (seriesItemIndex == null) return;
            if (plot.getRenderer() instanceof XYLineAndShapeRenderer){
	            if (seriesItemIndex == -1) updateSeriesItems(plot, plot.getRenderer());
	            else updateSeriesItem(plot, plot.getRenderer(), seriesItemIndex);	
            }
        } else if (jfc.getPlot() instanceof CategoryPlot){
            CategoryPlot plot = jfc.getCategoryPlot();
    		Integer seriesItemIndex = resolveIndex(plot);
    		if (seriesItemIndex == null) return;
            if (plot.getRenderer() instanceof LineAndShapeRenderer){
	            if (seriesItemIndex == -1) updateSeriesItems(plot, (LineAndShapeRenderer)plot.getRenderer());
	            else updateSeriesItem(plot, (LineAndShapeRenderer)plot.getRenderer(), seriesItemIndex);	
            }	
        }
    }
    
    /**
     * Resolve the index of the series item looking at the properties. If there is the flag for all 
     * the items it return -1, if it is already the index it return it or if is a series name it try
     * to find the index  for it
     * 
     * @param chartPlot the current plot
     * @return -1 to apply the shape to all the series items, the index to apply the shape only to
     * the series in the index position or null if the index can't be resolved
     */
    protected Integer resolveIndex(Plot chartPlot){
    	//check the all items flag first
    	Boolean allItems = ChartCustomizerUtils.asBoolean(configuration.get(ALL_LINE_ITEMS_KEY));
    	if (allItems != null && allItems){
    		return -1;
    	}
    	
    	//check the index number for second
    	Integer legendIndex = ChartCustomizerUtils.asInteger(configuration.get(LINE_ITEM_INDEX_KEY));
    	if (legendIndex != null) {
    		return legendIndex;
    	}

    	String seriesName = configuration.get(LINE_ITEM_SERIES_KEY);  
    	if (seriesName != null){
    		return seriesNameToIndex(chartPlot, seriesName);
    	}
    	return null;
    }
    
    /**
     * Convert a series name to its index number
     * 
     * @param chartPlot the plot of the chart
     * @param seriesName the series name, must be not null
     * @return the index of the series or null if it can't be found
     */
    protected Integer seriesNameToIndex(Plot chartPlot, String seriesName){
    	if (chartPlot instanceof XYPlot){
    		XYPlot xyPlot = (XYPlot)chartPlot;
    		for(int i=0; i<xyPlot.getSeriesCount();i++){
    			Comparable<?> key = xyPlot.getDataset().getSeriesKey(i);
    			if (seriesName.equals(key.toString())){
    				return i;
    			}
    		}
    	} else if (chartPlot instanceof CategoryPlot){
    		CategoryPlot catPlot = (CategoryPlot)chartPlot;
    		for(int i = 0; i < catPlot.getDataset().getRowCount(); i++){
    			Comparable<?> key = catPlot.getDataset().getRowKey(i);
    			if (seriesName.equals(key.toString())){
    				return i;
    			}
    		}
    	}
    	return null;
    }
    
    /**
     * Build a ellipse shape, with the center in the point 0,0
     * 
     * @param definition the definition 
     * @return the ellipse or null if it can't be build from the current definition
     */
    @Override
    protected Shape buildEllipse(ShapeDefinition definition) {
    	Point size = getSize();
    	if (size != null){
	    	try{
	    		int width = size.getX();
	    		int height = size.getY();
	    		Ellipse2D ellipse = new Ellipse2D.Float(-(width/2), -(height/2), width, height);
	    		return ellipse;
	    	} catch(Exception ex){
	    		ex.printStackTrace();
	    	}
    	}
		return null;
    }
    
    /**
     * Build a rectangle shape, with the center in the point 0,0
     * 
     * @param definition the definition that contains the points of the rectangle
     * @return the rectangle or null if it can't be build from the current definition
     */
    @Override
    protected Shape buildRectangle(ShapeDefinition definition){
    	Point size = getSize();
    	if (size != null){
	    	try{
	    		int width = size.getX();
	    		int height = size.getY();
	    		
	    		Rectangle2D rectangle = new Rectangle2D.Float(-(width/2), -(height/2), width, height);
	    		return rectangle;
	    	} catch(Exception ex){
	    		ex.printStackTrace();
	    	}
    	}
		return null;
    }
    
    /**
     * Build a polygon shape, with the center in the point 0,0
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
				
				//Calculate the offsets to center it in 0,0
				float xOffset = linesWidth / 2 + left;
				float yOffset = linesHeight / 2 + top;
				
				//calculate the scale factor to resize the shape
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
	    			pointsX[i] = Math.round((pointsX[i] - xOffset) * scaleFactorX);
	    			pointsY[i] = Math.round((pointsY[i] - yOffset) * scaleFactorY);
	    		}

		    	return new Polygon(pointsX, pointsY, points.size());
	    	}
    	} catch(Exception ex){
    		ex.printStackTrace();
    	}
		return null;
    }
    
    /**
     * Use the points stored in the definition to build a polyline, with the center in 0,0 
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
				
				//Calculate the offsets to center it in 0,0
				double xOffset = linesWidth / 2 + left;
				double yOffset = linesHeight / 2 + top;
				
				//calculate the scale factor to resize the shape
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
			    	path.moveTo((pointsX[0] - xOffset) * scaleFactorX, (pointsY[0] - yOffset) * scaleFactorY);
			    	for(int i = 1; i < points.size(); i++){
			    		path.lineTo((pointsX[i] - xOffset) * scaleFactorX, (pointsY[i] - yOffset) * scaleFactorY);
			    	}
		    	}
		    	return path;
	    	}
    	} catch(Exception ex){
    		ex.printStackTrace();
    	}
		return null;
    }
    
    /**
     * Apply the shape defined in the configuration to the Series in the specified
     * index of the passed parameter. Used for XY charts
     * 
     * @param plot the plot of the chart, must be not null
     * @param renderer the renderer of the current chart, must be not null
     * @param seriesIndex the index of the item to update
     */
    protected void updateSeriesItem(XYPlot plot, XYItemRenderer renderer, int seriesIndex){
        if (plot.getSeriesCount() > seriesIndex){
            String points = configuration.get(LINE_SHAPE_DEFINITION);
        	Shape shape = buildShape(points); 
        	if (shape != null){
        		renderer.setSeriesShape(seriesIndex, shape);
        	}
        }	
    }
    
    /**
     * Update the shape of all the series in the collection. Used for XY charts
     * 
     * @param plot the plot of the chart, must be not null
     * @param renderer the renderer of the current chart
     */
    protected void updateSeriesItems(XYPlot plot, XYItemRenderer renderer){
        String shapeType = configuration.get(LINE_SHAPE_DEFINITION);
    	Shape shape = buildShape(shapeType); 
        for(int i= 0; i < plot.getSeriesCount(); i++){
        	if (shape != null){
        		renderer.setSeriesShape(i, shape); //Shape legendShape = renderer.getLegendItems().get(i).getShape();
        	}
        }	
    }
    
    /**
     * Apply the shape defined in the configuration to the Series in the specified
     * index of the passed parameter. Used for Category charts
     * 
     * @param plot the plot of the chart, must be not null
     * @param renderer the renderer of the current chart, must be not null
     * @param seriesIndex the index of the item to update
     */
    protected void updateSeriesItem(CategoryPlot plot, LineAndShapeRenderer renderer, int seriesIndex){
        if (plot.getDataset().getRowCount() > seriesIndex){
            String points = configuration.get(LINE_SHAPE_DEFINITION);
        	Shape shape = buildShape(points); 
        	if (shape != null){
        		renderer.setSeriesShape(seriesIndex, shape);
        	}
        }	
    }
    
    /**
     * Update the shape of all the series in the collection. Used for Category charts
     * 
     * @param plot the plot of the chart, must be not null
     * @param renderer the renderer of the current chart
     */
    protected void updateSeriesItems(CategoryPlot plot, LineAndShapeRenderer renderer){
        String shapeType = configuration.get(LINE_SHAPE_DEFINITION);
    	Shape shape = buildShape(shapeType); 
        for(int i= 0; i < plot.getDataset().getRowCount(); i++){
        	if (shape != null){
        		renderer.setSeriesShape(i, shape);
        	}
        }	
    }

	@Override
	protected Integer getWidth() {
		return ChartCustomizerUtils.asInteger(configuration.get(LINE_SHAPE_WIDTH_KEY));
	}

	@Override
	protected Integer getHeight() {
		return ChartCustomizerUtils.asInteger(configuration.get(LINE_SHAPE_HEIGHT_KEY));
	}

}
