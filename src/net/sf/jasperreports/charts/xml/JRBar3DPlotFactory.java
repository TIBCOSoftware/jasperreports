/*
 * ============================================================================
 *                   GNU Lesser General Public License
 * ============================================================================
 *
 * JasperReports - Free Java report-generating library.
 * Copyright (C) 2001-2005 JasperSoft Corporation http://www.jaspersoft.com
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307, USA.
 * 
 * JasperSoft Corporation
 * 185, Berry Street, Suite 6200
 * San Francisco CA 94107
 * http://www.jaspersoft.com
 */

package net.sf.jasperreports.charts.xml;

import net.sf.jasperreports.charts.design.JRDesignBar3DPlot;
import net.sf.jasperreports.engine.JRChart;
import net.sf.jasperreports.engine.xml.JRBaseFactory;

import org.xml.sax.Attributes;

/**
 * @author User
 * @version JRBar3DPlotFactory.java Jun 20, 2005 7:14:40 PM User 
 */
public class JRBar3DPlotFactory extends JRBaseFactory {
    private static final String ATTRIBUTE_xOffset = "xOffset";
	private static final String ATTRIBUTE_yOffset = "yOffset";

	public Object createObject(Attributes atts)
	{
		JRChart chart = (JRChart) digester.peek();
		JRDesignBar3DPlot plot = (JRDesignBar3DPlot)chart.getPlot();

		String xOffset = atts.getValue(ATTRIBUTE_xOffset);
		if (xOffset != null && xOffset.length() > 0) {
            plot.setXOffset(new Integer(xOffset).intValue());
		}
 
		String yOffset = atts.getValue(ATTRIBUTE_yOffset);
		if (yOffset != null && yOffset.length() > 0) {
            plot.setYOffset(new Integer( yOffset).intValue());
		}

		return plot;
	}

}
