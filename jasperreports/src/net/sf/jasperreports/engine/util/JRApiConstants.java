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
package net.sf.jasperreports.engine.util;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import net.sf.jasperreports.charts.JRChartAxis;
import net.sf.jasperreports.charts.JRThermometerPlot;



/**
 * @author sanda zaharia (shertage@users.sourceforge.net)
 * @version $Id$
 */
public class JRApiConstants
{
	private static Map axisPositionMap = null;

	public static Map getAxisPositionMap()
	{
		 if (axisPositionMap == null)
		 {
			Map map = new HashMap(6);
			map.put(new Byte(JRChartAxis.POSITION_LEFT_OR_TOP), 	"JRChartAxis.POSITION_LEFT_OR_TOP");
			map.put(new Byte(JRChartAxis.POSITION_RIGHT_OR_BOTTOM), "JRChartAxis.POSITION_RIGHT_OR_BOTTOM");
			axisPositionMap = Collections.unmodifiableMap(map);
		 }

		 return axisPositionMap;
	}

	public static String getAxisPosition(Byte key)
	{
		return (String)getAxisPositionMap().get(key);
	}
	
	
}
