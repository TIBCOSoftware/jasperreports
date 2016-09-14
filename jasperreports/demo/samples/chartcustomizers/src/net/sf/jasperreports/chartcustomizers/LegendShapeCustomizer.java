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

import java.awt.Shape;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.LegendItemCollection;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.AbstractRenderer;

import net.sf.jasperreports.chartcustomizers.utils.ChartCustomizerUtils;
import net.sf.jasperreports.engine.JRChart;

/**
 * Customizer to define the shape of a specific legend item, works for both 
 * XY and Category plot. The shape and other informations are encoded into a
 * json
 * 
 * @author Marco Orlandin (dejawho2@users.sourceforge.net)
 */
public class LegendShapeCustomizer extends AbstractShapeCustomizer {
	
	public static final String LEGEND_ITEM_INDEX_KEY = "legendItemIndexKey";
	
	public static final String LEGEND_ITEM_SERIES_KEY = "legendItemSeriesKey";
	
	public static final String ALL_LEGEND_ITEMS_KEY = "allLegendItems";
	
	public static final String LEGEND_SHAPE_DEFINITION = "legendShapeDefinition";
	
	public static final String LEGEND_SHAPE_WIDTH_KEY = "legendShapeWidth";
	
	public static final String LEGEND_SHAPE_HEIGHT_KEY = "legendShapeHeight";
	
    @Override
    public void customize(JFreeChart jfc, JRChart jrc) {
		Integer legendItemIndex = resolveIndex(jfc.getPlot());
		if (legendItemIndex == null) return;
        if (jfc.getPlot() instanceof XYPlot){
            XYPlot plot = jfc.getXYPlot();
            if (plot.getRenderer() instanceof AbstractRenderer){
	            if (legendItemIndex == -1) updateLegendItems(plot.getLegendItems(), (AbstractRenderer)plot.getRenderer());
	            else updateLegendItem(plot.getLegendItems(), (AbstractRenderer)plot.getRenderer(), legendItemIndex);	
            }
        }  else if (jfc.getPlot() instanceof CategoryPlot){
        	CategoryPlot plot = jfc.getCategoryPlot();
        	if (plot.getRenderer() instanceof AbstractRenderer){
	            if (legendItemIndex == -1) updateLegendItems(plot.getLegendItems(), (AbstractRenderer) plot.getRenderer());
	            else updateLegendItem(plot.getLegendItems(), (AbstractRenderer) plot.getRenderer(), legendItemIndex);
        	}
        }
    }
    
    /**
     * Resolve the index of the legend item looking at the properties. If there is the flag for all 
     * the items it return -1, if it is already the index it return it or if is a series name it try
     * to find the legend item for it
     * 
     * @param chartPlot the current plot
     * @return -1 to apply the shape to all the legend items, the index to apply the shape only to
     * the legend item in the index position or null if the index can't be resolved
     */
    protected Integer resolveIndex(Plot chartPlot){
    	//check the all items flag first
    	Boolean allItems = ChartCustomizerUtils.asBoolean(configuration.get(ALL_LEGEND_ITEMS_KEY));
    	if (allItems != null && allItems){
    		return -1;
    	}
    	
    	//check the index number for second
    	Integer legendIndex = ChartCustomizerUtils.asInteger(configuration.get(LEGEND_ITEM_INDEX_KEY));
    	if (legendIndex != null) {
    		return legendIndex;
    	}

    	String seriesName = configuration.get(LEGEND_ITEM_SERIES_KEY);  
    	if (seriesName != null){
    		return seriesNameToIndex(chartPlot, seriesName);
    	}
    	return null;
    }
    
    /**
     * Convert a series  name to a legend item number
     * 
     * @param chartPlot the plot of the chart
     * @param seriesName the series name, must be not null
     * @return the index of the legend item or null if it can't be found
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
     * Apply the shaped defined in the configuration to the LegendItem in the specified
     * index of the passed parameter
     * 
     * @param items the plot {@link LegendItemCollection}, must be not null
     * @param renderer the renderer of the current chart
     * @param legendIndex the index of the item to update
     */
    protected void updateLegendItem(LegendItemCollection items, AbstractRenderer renderer, int legendIndex){
        if (items.getItemCount() > legendIndex){
            String points = configuration.get(LEGEND_SHAPE_DEFINITION);
        	Shape shape = buildShape(points); 
        	if (shape != null){
        		renderer.setLegendShape(legendIndex, shape);
        	}
        }	
    }
    
    /**
     * Update all the legend items in the collection
     * 
     * @param items the items to update
     * @param renderer the renderer of the current chart
     */
    protected void updateLegendItems(LegendItemCollection items, AbstractRenderer renderer){
        String shapeType = configuration.get(LEGEND_SHAPE_DEFINITION);
    	Shape shape = buildShape(shapeType); 
        for(int i= 0; i < items.getItemCount(); i++){
        	if (shape != null){
        		renderer.setLegendShape(i, shape);
        	}
        }	
    }

	@Override
	protected Integer getWidth() {
		return ChartCustomizerUtils.asInteger(configuration.get(LEGEND_SHAPE_WIDTH_KEY));
	}

	@Override
	protected Integer getHeight() {
		return ChartCustomizerUtils.asInteger(configuration.get(LEGEND_SHAPE_HEIGHT_KEY));
	}
}

