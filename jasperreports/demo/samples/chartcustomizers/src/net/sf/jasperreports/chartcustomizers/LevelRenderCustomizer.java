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

import net.sf.jasperreports.chartcustomizers.utils.ChartCustomizerUtils;
import net.sf.jasperreports.engine.JRChart;
import net.sf.jasperreports.engine.JRChartCustomizer;

public class LevelRenderCustomizer implements JRChartCustomizer, ConfigurableChartCustomizer {

    private Map<String, String> configuration = null;
    
    public static final String ITEM_MARIGN = "itemMargin";
    
    public static final String MAX_ITEM_WIDTH = "maximumItemWidth";
    
    public static final String SERIES_INDEX = "seriesIndex";
    
    @Override
    public void customize(JFreeChart jfc, JRChart jrc) {
        
		if (!(jfc.getPlot() instanceof CategoryPlot))
			return;

		CategoryPlot plot = (CategoryPlot) jfc.getPlot();
		
		Integer seriesIndex = ChartCustomizerUtils.asInteger(configuration.get(SERIES_INDEX));
		
		if (seriesIndex != null){
			plot.setRenderer(seriesIndex, ChartCustomizerUtils.getCategoryLevelRender(configuration));
		}
    }

    @Override
    public void setConfiguration(Map<String, String> properties) {
        this.configuration = properties;
    }
    
}
