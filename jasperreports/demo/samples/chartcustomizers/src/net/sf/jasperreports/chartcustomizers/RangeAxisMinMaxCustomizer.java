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

import java.util.Map;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.XYPlot;

import net.sf.jasperreports.chartcustomizers.utils.ChartCustomizerUtils;
import net.sf.jasperreports.engine.JRChart;
import net.sf.jasperreports.engine.JRChartCustomizer;

/**
 * Customizer to define the minimum and maximum value of the range axis, works for both 
 * XY and Category plot
 */
public class RangeAxisMinMaxCustomizer implements JRChartCustomizer, ConfigurableChartCustomizer {

	public static final String RANGE_MIN_KEY = "rangeMin";
	
	public static final String RANGE_MAX_KEY = "rangeMax";
	
	protected Map<String, String> configuration = null;
	
    @Override
    public void customize(JFreeChart jfc, JRChart jrc) {
        
            if ((jfc.getPlot() instanceof XYPlot)){
	            XYPlot plot = jfc.getXYPlot();
	            Double rangeMin = ChartCustomizerUtils.asDouble(configuration.get(RANGE_MIN_KEY));
	            if (rangeMin == null) plot.getRangeAxis().getRange().getLowerBound();
	            Double rangeMax = ChartCustomizerUtils.asDouble(configuration.get(RANGE_MAX_KEY));
	            if (rangeMax == null) plot.getRangeAxis().getRange().getUpperBound();
	            if (rangeMax != null && rangeMin != null){
	            	plot.getRangeAxis().setRange(rangeMin, rangeMax);
	            }
            } else if (jfc.getPlot() instanceof CategoryPlot){
            	CategoryPlot plot = jfc.getCategoryPlot();
	            Double rangeMin = ChartCustomizerUtils.asDouble(configuration.get(RANGE_MIN_KEY));
	            if (rangeMin == null) plot.getRangeAxis().getRange().getLowerBound();
	            Double rangeMax = ChartCustomizerUtils.asDouble(configuration.get(RANGE_MAX_KEY));
	            if (rangeMax == null) plot.getRangeAxis().getRange().getUpperBound();
	            if (rangeMax != null && rangeMin != null){
	            	plot.getRangeAxis().setRange(rangeMin, rangeMax);
	            }
            }
    }

	@Override
	public void setConfiguration(Map<String, String> properties) {
		this.configuration = properties;
	}
}
