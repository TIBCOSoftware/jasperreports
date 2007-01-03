/*
 * ============================================================================
 * GNU Lesser General Public License
 * ============================================================================
 *
 * JasperReports - Free Java report-generating library.
 * Copyright (C) 2001-2006 JasperSoft Corporation http://www.jaspersoft.com
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

import net.sf.jasperreports.charts.design.JRDesignHighLowPlot;
import net.sf.jasperreports.engine.JRChart;
import net.sf.jasperreports.engine.xml.JRBaseFactory;

import org.xml.sax.Attributes;


/**
 * @author Ionut Nedelcu (ionutned@users.sourceforge.net)
 * @version $Id$
 */
public class JRHighLowPlotFactory extends JRBaseFactory
{

	private static final String ATTRIBUTE_isShowOpenTicks = "isShowOpenTicks";
	private static final String ATTRIBUTE_isShowCloseTicks = "isShowCloseTicks";


	/**
	 *
	 */
	public Object createObject(Attributes atts)
	{
		JRChart chart = (JRChart) digester.peek();
		JRDesignHighLowPlot plot = (JRDesignHighLowPlot)chart.getPlot();

		String isShowOpenTicks = atts.getValue(ATTRIBUTE_isShowOpenTicks);
		if (isShowOpenTicks != null && isShowOpenTicks.length() > 0) {
			plot.setShowOpenTicks(Boolean.valueOf(isShowOpenTicks).booleanValue());
		}

		String isShowCloseTicks = atts.getValue(ATTRIBUTE_isShowCloseTicks);
		if (isShowCloseTicks != null && isShowCloseTicks.length() > 0) {
			plot.setShowCloseTicks(Boolean.valueOf(isShowCloseTicks).booleanValue());
		}

		return plot;
	}
}
