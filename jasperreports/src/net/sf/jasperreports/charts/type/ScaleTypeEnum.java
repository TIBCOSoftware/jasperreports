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
package net.sf.jasperreports.charts.type;

import net.sf.jasperreports.engine.JRConstants;

import org.jfree.chart.renderer.xy.XYBubbleRenderer;


/**
 * @author sanda zaharia (shertage@users.sourceforge.net)
 * @version $Id: PlotOrientationEnum.java 3551 2010-03-09 16:24:05Z shertage $
 */
public enum ScaleTypeEnum
{
	/**
	 *
	 */
	ON_BOTH_AXES(XYBubbleRenderer.SCALE_ON_BOTH_AXES, "BothAxes"),

	/**
	 *
	 */
	ON_DOMAIN_AXIS(XYBubbleRenderer.SCALE_ON_DOMAIN_AXIS, "DomainAxis"),

	/**
	 *
	 */
	ON_RANGE_AXIS(XYBubbleRenderer.SCALE_ON_RANGE_AXIS, "RangeAxis");



	/**
	 *
	 */
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;
	private final transient int value;
	private final transient String name;

	private ScaleTypeEnum(int value, String name)
	{
		this.value = value;
		this.name = name;
	}

	/**
	 *
	 */
	public final int getValue()
	{
		return this.value;
	}
	
	/**
	 *
	 */
	public final Integer getValueInteger()
	{
		return this.value;
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
	public static ScaleTypeEnum getByName(String name)
	{
		ScaleTypeEnum[] values = values();
		if (values != null && name != null)
		{
			for(ScaleTypeEnum e:values)
			{
				if (name.equals(e.getName()))
				{
					return e;
				}
			}
		}
		return null;
	}
	
	/**
	 *
	 */
	public static ScaleTypeEnum getByValue(Integer scaleType)
	{
		ScaleTypeEnum[] values = values();
		if (values != null && scaleType != null)
		{
			for(ScaleTypeEnum e:values)
			{
				if (scaleType.equals(e.getValue()))
				{
					return e;
				}
			}
		}
		return null;
	}
	
	/**
	 *
	 */
	public static ScaleTypeEnum getByValue(int value)
	{
		return getByValue(new Integer(value));
	}
}
