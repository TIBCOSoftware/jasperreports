/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2011 Jaspersoft Corporation. All rights reserved.
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
package net.sf.jasperreports.charts.type;

import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.type.EnumUtil;
import net.sf.jasperreports.engine.type.JREnum;

import org.jfree.chart.plot.PlotOrientation;


/**
 * @author sanda zaharia (shertage@users.sourceforge.net)
 * @version $Id$
 */
public enum PlotOrientationEnum implements JREnum
{
	/**
	 *
	 */
	HORIZONTAL(PlotOrientation.HORIZONTAL, "Horizontal"),

	/**
	 *
	 */
	VERTICAL(PlotOrientation.VERTICAL, "Vertical");


	/**
	 *
	 */
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;
	private final transient PlotOrientation value;
	private final transient String name;

	private PlotOrientationEnum(PlotOrientation orientation, String name)
	{
		this.value = orientation;
		this.name = name;
	}

	/**
	 *
	 */
	public Byte getValueByte()
	{
		return new Byte(getValue());
	}
	
	/**
	 *
	 */
	public final byte getValue()
	{
		return (byte)-1;
	}
	
	/**
	 *
	 */
	public String getName()
	{
		return name;
	}
	
	/**
	 *
	 */
	public final PlotOrientation getOrientation()
	{
		return this.value;
	}
	
	/**
	 *
	 */
	public static PlotOrientationEnum getByName(String name)
	{
		return (PlotOrientationEnum)EnumUtil.getByName(values(), name);
	}
	
	/**
	 *
	 */
	public static PlotOrientationEnum getByValue(PlotOrientation orientation)
	{
		PlotOrientationEnum[] values = values();
		if (values != null && orientation != null)
		{
			for(PlotOrientationEnum e:values)
			{
				if (orientation.equals(e.getOrientation()))
				{
					return e;
				}
			}
		}
		return null;
	}
	
}
