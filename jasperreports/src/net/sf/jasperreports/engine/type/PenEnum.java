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
package net.sf.jasperreports.engine.type;

import net.sf.jasperreports.engine.JRConstants;


/**
 * @author sanda zaharia (shertage@users.sourceforge.net)
 * @version $Id$
 */
public enum PenEnum implements JREnum
{
	/**
	 * Constant useful for specifying that the element border will not be drawn.
	 */
	NONE((byte)0, "None"),

	/**
	 * Constant useful for specifying that an element border of 1 pixel.
	 */
	ONE_POINT((byte)1, "1Point"),
	
	/**
	 * Constant useful for specifying that an element border of 2 pixels.
	 */
	TWO_POINT((byte)2, "2Point"),
	
	/**
	 * Constant useful for specifying that an element border of 4 pixels.
	 */
	FOUR_POINT((byte)3, "4Point"),
	
	/**
	 * Constant useful for specifying that an element has a dotted border.
	 */
	DOTTED((byte)4, "Dotted"),
	
	/**
	 * Constant useful for specifying that an element has a thin border (0.5 pixels)
	 */
	THIN((byte)5, "Thin");

	/**
	 *
	 */
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;
	private final transient byte value;
	private final transient String name;

	private PenEnum(byte value, String name)
	{
		this.value = value;
		this.name = name;
	}

	/**
	 *
	 */
	public Byte getValueByte()
	{
		return new Byte(value);
	}
	
	/**
	 *
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
	public static PenEnum getByName(String name)
	{
		return (PenEnum)EnumUtil.getByName(values(), name);
	}
	
	/**
	 *
	 */
	public static PenEnum getByValue(Byte value)
	{
		return (PenEnum)EnumUtil.getByValue(values(), value);
	}
	
	/**
	 *
	 */
	public static PenEnum getByValue(byte value)
	{
		return getByValue(new Byte(value));
	}

}
