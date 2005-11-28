/*
 * ============================================================================
 * GNU Lesser General Public License
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
 * 303 Second Street, Suite 450 North
 * San Francisco, CA 94107
 * http://www.jaspersoft.com
 */
package net.sf.jasperreports.charts.xml;

import net.sf.jasperreports.charts.design.JRDesignBarPlot;
import net.sf.jasperreports.engine.JRChart;
import net.sf.jasperreports.engine.xml.JRBaseFactory;

import org.xml.sax.Attributes;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JRBarPlotFactory extends JRBaseFactory
{
	private static final String ATTRIBUTE_isShowTickMarks = "isShowTickMarks";
	private static final String ATTRIBUTE_isShowTickLabels = "isShowTickLabels";
	private static final String ATTRIBUTE_isShowLabels = "isShowLabels";

	public Object createObject(Attributes atts)
	{
		JRChart chart = (JRChart) digester.peek();
		JRDesignBarPlot plot = (JRDesignBarPlot)chart.getPlot();

		String isShowTickMarks = atts.getValue(ATTRIBUTE_isShowTickMarks);
		if (isShowTickMarks != null && isShowTickMarks.length() > 0) {
			plot.setShowTickMarks(Boolean.valueOf(isShowTickMarks).booleanValue());
		}

		String isShowTickLabels = atts.getValue(ATTRIBUTE_isShowTickLabels);
		if (isShowTickLabels != null && isShowTickLabels.length() > 0) {
			plot.setShowTickLabels(Boolean.valueOf(isShowTickLabels).booleanValue());
		}
		
		String isShowLabels = atts.getValue( ATTRIBUTE_isShowLabels );
		if( isShowLabels != null && isShowLabels.length() > 0 ){
			plot.setShowLabels(Boolean.valueOf(isShowLabels).booleanValue());
		}

		return plot;
	}
}
