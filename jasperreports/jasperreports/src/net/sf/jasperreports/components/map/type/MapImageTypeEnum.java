/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2013 Jaspersoft Corporation. All rights reserved.
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
package net.sf.jasperreports.components.map.type;

import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.type.EnumUtil;
import net.sf.jasperreports.engine.type.JREnum;


/**
 * @author sanda zaharia (shertage@users.sourceforge.net)
 * @version $Id$
 */
public enum MapImageTypeEnum implements JREnum
{
	/**
	 * The 8-bit PNG format (the same as PNG_8)
	 */
	PNG((byte)0, "png"),

	/**
	 * The 8-bit PNG format
	 */
	PNG_8((byte)1, "png8"),
	
	/**
	 * The 32-bit PNG format
	 */
	PNG_32((byte)2, "png32"),
	
	/**
	 * The GIF format
	 */
	GIF((byte)3, "gif"),
	
	/**
	 * The JPEG compression format
	 */
	JPG((byte)4, "jpg"),
	
	/**
	 * The non-progressive JPEG compression format
	 */
	JPG_BASELINE((byte)5, "jpg-baseline");

	/**
	 *
	 */
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;
	private final transient byte value;
	private final transient String name;

	private MapImageTypeEnum(byte value, String name)
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
	public static MapImageTypeEnum getByName(String name)
	{
		return (MapImageTypeEnum)EnumUtil.getByName(values(), name);
	}
	
	/**
	 *
	 */
	public static MapImageTypeEnum getByValue(Byte value)
	{
		return (MapImageTypeEnum)EnumUtil.getByValue(values(), value);
	}
	
	/**
	 *
	 */
	public static MapImageTypeEnum getByValue(byte value)
	{
		return getByValue(new Byte(value));
	}
	
}
