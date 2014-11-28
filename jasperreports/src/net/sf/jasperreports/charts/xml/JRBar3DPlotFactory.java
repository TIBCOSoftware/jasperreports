/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2014 TIBCO Software Inc. All rights reserved.
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
package net.sf.jasperreports.charts.xml;

import net.sf.jasperreports.charts.design.JRDesignBar3DPlot;
import net.sf.jasperreports.engine.JRChart;
import net.sf.jasperreports.engine.xml.JRBaseFactory;

import org.xml.sax.Attributes;

/**
 * @author Flavius Sana (flavius_sana@users.sourceforge.net)
 */
public class JRBar3DPlotFactory extends JRBaseFactory {
	private static final String ATTRIBUTE_xOffset = "xOffset";
	private static final String ATTRIBUTE_yOffset = "yOffset";
	private static final String ATTRIBUTE_isShowLabels = "isShowLabels";

	public Object createObject(Attributes atts)
	{
		JRChart chart = (JRChart) digester.peek();
		JRDesignBar3DPlot plot = (JRDesignBar3DPlot)chart.getPlot();

		String xOffset = atts.getValue(ATTRIBUTE_xOffset);
		if (xOffset != null && xOffset.length() > 0) {
			plot.setXOffset(Double.valueOf(xOffset));
		}
 
		String yOffset = atts.getValue(ATTRIBUTE_yOffset);
		if (yOffset != null && yOffset.length() > 0) {
			plot.setYOffset(Double.valueOf(yOffset));
		}
		
		String isShowLabels = atts.getValue( ATTRIBUTE_isShowLabels );
		if( isShowLabels != null && isShowLabels.length() > 0 ){
			plot.setShowLabels(Boolean.valueOf(isShowLabels));
		}

		return plot;
	}

}
