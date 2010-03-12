/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2009 Jaspersoft Corporation. All rights reserved.
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
package	net.sf.jasperreports.charts.xml;

import net.sf.jasperreports.charts.design.JRDesignChartAxis;
import net.sf.jasperreports.charts.type.AxisPositionEnum;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.design.JRDesignChart;
import net.sf.jasperreports.engine.xml.JRBaseFactory;

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

		AxisPositionEnum position = AxisPositionEnum.getByName(atts.getValue(ATTRIBUTE_position));
		
		if (position !=	null)
		{
			axis.setPosition(position);
		}

		return axis;
	}
}
