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
package net.sf.jasperreports.charts.type;

import net.sf.jasperreports.engine.type.EnumUtil;
import net.sf.jasperreports.engine.type.JREnum;


/**
 * @author sanda zaharia (shertage@users.sourceforge.net)
 */
public enum MeterShapeEnum implements JREnum
{
	/**
	 * The portion of the circle described by the Meter that is not occupied by the
	 * Meter is drawn as a chord.  (A straight line connects the ends.)
	 */
	CHORD((byte)0, "chord"),
	
	/**
	 * The portion of the circle described by the Meter that is not occupied by the
	 * Meter is drawn as a circle.
	 */
	CIRCLE((byte)1, "circle"),
	
	/**
	 * The portion of the circle described by the Meter that is not occupied by the
	 * Meter is not drawn.
	 */
	PIE((byte)2, "pie"),
	
	/**
	 * The portion of the circle described by the Meter that is not occupied by the
	 * Meter is drawn as a circle, and handled with specific dial objects.
	 */
	DIAL((byte)3, "dial");
	
	/**
	 *
	 */
	private final transient byte value;
	private final transient String name;

	private MeterShapeEnum(byte value, String name)
	{
		this.value = value;
		this.name = name;
	}

	/**
	 * @deprecated Used only by deprecated serialized fields.
	 */
	public Byte getValueByte()
	{
		return new Byte(value);
	}
	
	/**
	 * @deprecated Used only by deprecated serialized fields.
	 */
	public final byte getValue()
	{
		return value;
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
	public static MeterShapeEnum getByName(String name)
	{
		return EnumUtil.getEnumByName(values(), name);
	}
	
	/**
	 * @deprecated Used only by deprecated serialized fields.
	 */
	public static MeterShapeEnum getByValue(Byte value)
	{
		return (MeterShapeEnum)EnumUtil.getByValue(values(), value);
	}
	
	/**
	 * @deprecated Used only by deprecated serialized fields.
	 */
	public static MeterShapeEnum getByValue(byte value)
	{
		return getByValue(new Byte(value));
	}
}
