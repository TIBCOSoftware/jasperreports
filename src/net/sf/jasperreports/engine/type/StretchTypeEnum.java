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
public enum StretchTypeEnum implements JREnum
{
	/**
	 * A constant indicating that the element preserves its original specified height.
	 */
	NO_STRETCH((byte)0, "NoStretch"),//FIXMEENUM check all 0 constants for initialization

	/**
	 * A constant indicating that users have the possibility to group the elements of a report section 
	 * in multiple nested groups. The only reason one might have for grouping your report elements 
	 * is to be able to stretch them to fit the tallest object.
	 */
	RELATIVE_TO_TALLEST_OBJECT((byte)1, "RelativeToTallestObject"),
	
	/**
	 * A constant used for specifying that the graphic element will adapt its height to match the new 
	 * height of the report section it placed on, which has been affected by stretch
	 */
	RELATIVE_TO_BAND_HEIGHT((byte)2, "RelativeToBandHeight");
	
	
	/**
	 *
	 */
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;
	private final transient byte value;
	private final transient String name;

	private StretchTypeEnum(byte value, String name)
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
	public static StretchTypeEnum getByName(String name)
	{
		return (StretchTypeEnum)EnumUtil.getByName(values(), name);
	}
	
	/**
	 *
	 */
	public static StretchTypeEnum getByValue(Byte value)
	{
		return (StretchTypeEnum)EnumUtil.getByValue(values(), value);
	}
	
	/**
	 *
	 */
	public static StretchTypeEnum getByValue(byte value)
	{
		return getByValue(new Byte(value));
	}

}
