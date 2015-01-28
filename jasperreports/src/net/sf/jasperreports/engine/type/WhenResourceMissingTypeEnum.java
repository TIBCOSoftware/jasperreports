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
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public enum WhenResourceMissingTypeEnum implements JREnum
{
	/**
	 * Return NULL when a resource is missing.
	 */
	NULL((byte)1, "Null"),

	/**
	 * Return empty string when a resource is missing.
	 */
	EMPTY((byte)2, "Empty"),

	/**
	 * Return the key when a resource is missing.
	 */
	KEY((byte)3, "Key"),

	/**
	 * Throw an exception when a resource is missing.
	 */
	ERROR((byte)4, "Error");

	/**
	 *
	 */
	private final transient byte value;
	private final transient String name;

	private WhenResourceMissingTypeEnum(byte value, String name)
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
	public static WhenResourceMissingTypeEnum getByName(String name)
	{
		return EnumUtil.getEnumByName(values(), name);
	}
	
	/**
	 * @deprecated Used only by deprecated serialized fields.
	 */
	public static WhenResourceMissingTypeEnum getByValue(Byte value)
	{
		return (WhenResourceMissingTypeEnum)EnumUtil.getByValue(values(), value);
	}
	
	/**
	 * @deprecated Used only by deprecated serialized fields.
	 */
	public static WhenResourceMissingTypeEnum getByValue(byte value)
	{
		return getByValue(new Byte(value));
	}
}
