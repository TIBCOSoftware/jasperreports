/*
 * ============================================================================
 * GNU Lesser General Public License
 * ============================================================================
 *
 * JasperReports - Free	Java report-generating library.
 * Copyright (C) 2001-2009 JasperSoft Corporation http://www.jaspersoft.com
 *
 * This	library	is free	software; you can redistribute it and/or
 * modify it under the terms of	the	GNU	Lesser General Public
 * License as published	by the Free	Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This	library	is distributed in the hope that	it will	be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A	PARTICULAR PURPOSE.	 See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received	a copy of the GNU Lesser General Public
 * License along with this library;	if not,	write to the Free Software
 * Foundation, Inc., 59	Temple Place, Suite	330, Boston, MA	 02111-1307, USA.
 *
 * JasperSoft Corporation
 * 303 Second Street, Suite	450	North
 * San Francisco, CA 94107
 * http://www.jaspersoft.com
 */
package	net.sf.jasperreports.charts.xml;

import net.sf.jasperreports.charts.design.JRDesignChartAxis;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.design.JRDesignChart;
import net.sf.jasperreports.engine.xml.JRBaseFactory;
import net.sf.jasperreports.engine.xml.JRXmlConstants;

import org.xml.sax.Attributes;


/**
 * @author Barry Klawans (bklawans@users.sourceforge.net)
 * @version	$Id: JRChartAxisFactory.java 1578 2007-02-09 16:27:52Z shertage	$
 */
public class JRChartAxisFactory	extends	JRBaseFactory
{
	public static final	String ELEMENT_axis	= "axis";
	public static final	String ATTRIBUTE_position =	"position";

	/**
	 *
	 */
	public Object createObject(Attributes atts)	throws JRException
	{
		JRDesignChart parentChart =	(JRDesignChart)digester.peek(1);
		JRDesignChartAxis axis = new JRDesignChartAxis(parentChart);

		String posAttr = atts.getValue(ATTRIBUTE_position);
		Byte position =	(Byte)JRXmlConstants.getAxisPositionMap().get(posAttr);
//		if (position ==	null)
//		{
//			throw new JRException("Invalid axis	position: "	+ posAttr);
//		}
//		else
//		{
			axis.setPosition(position);
//		}

		return axis;
	}
}
