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

import net.sf.jasperreports.chartcustomizers.utils.ChartCustomizerUtils;
import net.sf.jasperreports.engine.JRChart;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.Marker;
import org.jfree.chart.plot.XYPlot;

/**
 * This customizer plots a line chart as spline.
 * 
 * @author Giulio Toffoli (gt78@users.sourceforge.net)
 */
public class RangeIntervalMarkerCustomizer extends AbstractIntervalMarkerCustomizer {

    @Override
    public void customize(JFreeChart jfc, JRChart jrc) {
        
            Marker m = this.createMarker();
            
            if (jfc.getPlot() instanceof XYPlot)
            {
                jfc.getXYPlot().addRangeMarker(m, ChartCustomizerUtils.getLayer(configuration));
            }
            else if (jfc.getPlot() instanceof CategoryPlot)
            {
                jfc.getCategoryPlot().addRangeMarker(m, ChartCustomizerUtils.getLayer(configuration));
            }
        
    }

}
