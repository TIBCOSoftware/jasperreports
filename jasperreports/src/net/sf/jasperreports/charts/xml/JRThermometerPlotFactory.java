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

import net.sf.jasperreports.charts.design.JRDesignThermometerPlot;
import net.sf.jasperreports.engine.JRChart;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.xml.JRBaseFactory;
import net.sf.jasperreports.engine.xml.JRXmlConstants;

import org.xml.sax.Attributes;


/**
 * @author Barry Klawans (bklawans@users.sourceforge.net)
 * @version $Id$
 */
public class JRThermometerPlotFactory extends JRBaseFactory
{
	public static final String ELEMENT_meterPlot = "meterPlot";
	public static final String ELEMENT_lowRange = "lowRange";
	public static final String ELEMENT_mediumRange = "mediumRange";
	public static final String ELEMENT_highRange = "highRange";

	public static final String ATTRIBUTE_showValueLines = "isShowValueLines";
	public static final String ATTRIBUTE_valueLocation = "valueLocation";
	public static final String ATTRIBUTE_mercuryColor = "mercuryColor";

	/**
	 *
	 */
	public Object createObject(Attributes atts) throws JRException
	{
		JRChart chart = (JRChart)digester.peek();
		JRDesignThermometerPlot thermometerPlot = (JRDesignThermometerPlot)chart.getPlot();

	   String showValueLines = atts.getValue(ATTRIBUTE_showValueLines);
		if (showValueLines != null && showValueLines.length() > 0)
		{
			thermometerPlot.setShowValueLines(Boolean.valueOf(showValueLines).booleanValue());
		}

		String location = atts.getValue(ATTRIBUTE_valueLocation);
		Byte loc = (Byte)JRXmlConstants.getThermometerValueLocationMap().get(location);
		if (loc == null)
		{
			throw new JRException("Invalid thermometer value location: " + location);
		}
		else
		{
			thermometerPlot.setValueLocation(loc.byteValue());
		}

		String mercuryColor = atts.getValue(ATTRIBUTE_mercuryColor);
		if (mercuryColor != null && mercuryColor.length() > 0)
		{
			thermometerPlot.setMercuryColor(JRXmlConstants.getColor(mercuryColor, null));
		}

		return thermometerPlot;
	}
}
