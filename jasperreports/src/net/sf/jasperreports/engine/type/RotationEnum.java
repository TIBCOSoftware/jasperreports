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
package net.sf.jasperreports.engine.type;


/**
 * @author sanda zaharia (shertage@users.sourceforge.net)
 */
public enum RotationEnum implements JREnum
{
	/**
	 * Constant useful for displaying the text without rotating it
	 */
	NONE((byte)0, "None"),

	/**
	 * Constant useful for rotating the text 90 degrees counter clockwise.
	 */
	LEFT((byte)1, "Left"),
	
	/**
	 * Constant useful for rotating the text 90 degrees clockwise.
	 */
	RIGHT((byte)2, "Right"),
	
	/**
	 * Constant useful for rotating the text 180 degrees.
	 */
	UPSIDE_DOWN((byte)3, "UpsideDown");
	
	/**
	 *
	 */
	private final transient byte value;
	private final transient String name;

	private RotationEnum(byte value, String name)
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
	public static RotationEnum getByName(String name)
	{
		return EnumUtil.getEnumByName(values(), name);
	}
	
	/**
	 * @deprecated Used only by deprecated serialized fields.
	 */
	public static RotationEnum getByValue(Byte value)
	{
		return (RotationEnum)EnumUtil.getByValue(values(), value);
	}
	
	/**
	 * @deprecated Used only by deprecated serialized fields.
	 */
	public static RotationEnum getByValue(byte value)
	{
		return getByValue(new Byte(value));
	}
}
