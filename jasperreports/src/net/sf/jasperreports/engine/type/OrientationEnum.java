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
public enum OrientationEnum implements JREnum
{
	/**
	 * Specifies a portrait orientation. This is used mostly to inform printers of page layouts.
	 */
	PORTRAIT((byte)1, "Portrait"),

	/**
	 * Specifies a landscape orientation. This is used mostly to inform printers of page layouts.
	 */
	LANDSCAPE((byte)2, "Landscape");
	
	/**
	 *
	 */
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;
	private final transient byte value;
	private final transient String name;

	private OrientationEnum(byte value, String name)
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
	public static OrientationEnum getByName(String name)
	{
		return (OrientationEnum)EnumUtil.getByName(values(), name);
	}
	
	/**
	 *
	 */
	public static OrientationEnum getByValue(Byte value)
	{
		return (OrientationEnum)EnumUtil.getByValue(values(), value);
	}
	
	/**
	 *
	 */
	public static OrientationEnum getByValue(byte value)
	{
		return getByValue(new Byte(value));
	}

}
