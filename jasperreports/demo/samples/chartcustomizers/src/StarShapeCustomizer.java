import java.awt.Shape;
import java.awt.geom.Path2D;
import java.util.Map;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.LegendItemCollection;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.AbstractRenderer;

import net.sf.jasperreports.chartcustomizers.ConfigurableChartCustomizer;
import net.sf.jasperreports.chartcustomizers.utils.ChartCustomizerUtils;
import net.sf.jasperreports.engine.JRChart;
import net.sf.jasperreports.engine.JRChartCustomizer;

public class StarShapeCustomizer implements JRChartCustomizer, ConfigurableChartCustomizer {

	private double[] pointsX = {0, 747, 971, 1201, 1943, 1343, 1572, 971, 371, 600, 0};
	
	private double[] pointsY = {707, 707, 0, 707, 707, 1143, 1849, 1414, 1849, 1143, 707};
	
	protected Map<String, String> configuration = null;
	
	public static final String SHAPE_WIDTH = "legendShapeWidth";
	
	public static final String SHAPE_HEIGHT = "legendShapeHeight";
	
	@Override
	public void customize(JFreeChart jfc, JRChart jasperChart) {
		if (jfc.getPlot() instanceof XYPlot){
            XYPlot plot = jfc.getXYPlot();
            if (plot.getRenderer() instanceof AbstractRenderer){
	            updateLegendItems(plot.getLegendItems(), (AbstractRenderer)plot.getRenderer());
            }
        }  else if (jfc.getPlot() instanceof CategoryPlot){
        	CategoryPlot plot = jfc.getCategoryPlot();
        	if (plot.getRenderer() instanceof AbstractRenderer){
	            updateLegendItems(plot.getLegendItems(), (AbstractRenderer) plot.getRenderer());
        	}
        }
	}

	protected Integer getWidth() {
		return ChartCustomizerUtils.asInteger(configuration.get(SHAPE_WIDTH));
	}

	protected Integer getHeight() {
		return ChartCustomizerUtils.asInteger(configuration.get(SHAPE_HEIGHT));
	}

	@Override
	public void setConfiguration(Map<String, String> properties) {
		this.configuration = properties;
	}
	
    /**
     * Update all the legend items in the collection
     * 
     * @param items the items to update
     * @param renderer the renderer of the current chart
     */
    protected void updateLegendItems(LegendItemCollection items, AbstractRenderer renderer){
    	Shape shape = buildShape(); 
        for(int i= 0; i < items.getItemCount(); i++){
        	if (shape != null){
        		renderer.setLegendShape(i, shape);
        	}
        }	
    }
	
    /**
     * Use the points stored in the definition to build a polyline
     * 
     * @param definition the definition item
     * @return a polyline shape or null if it can be built
     */
    protected Shape buildShape(){
    	try{
			Double top = null;
			Double bottom = null;
			Double left = null;
			Double right = null;
			Path2D path = new Path2D.Double();
			double[] pointsX = getXPoints();
			double[] pointsY = getYPoints();
	    	for(int index = 0; index < pointsX.length; index++){ 		
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
	    	}
	    	
			double shapeWidth = right - left;
			double shapeHeight = bottom - top;
	    	float scaleFactorX = 1.0f;
	    	float scaleFactorY = 1.0f;
    		Integer width = getWidth();
    		Integer height = getHeight();
    		if (width != null){
    			scaleFactorX = new Float(width) / new Float(shapeWidth);
    		}
    		if (height != null){
    			scaleFactorY = new Float(height) / new Float(shapeHeight);
    		}
	    	
	    	
	    	if (pointsX.length > 1){
		    	path.moveTo(pointsX[0] * scaleFactorX, pointsY[0] * scaleFactorY);
		    	for(int i = 1; i < pointsX.length; i++){
		    		path.lineTo(pointsX[i] * scaleFactorX, pointsY[i] * scaleFactorY);
		    	}
	    	}
	    	return path;
    	} catch(Exception ex){
    		ex.printStackTrace();
    	}
		return null;
    }

    protected double[] getXPoints(){
    	return pointsX;
    }
    
    protected double[] getYPoints(){
    	return pointsY;
    }
}
