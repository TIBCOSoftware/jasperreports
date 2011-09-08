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
public enum LineSpacingEnum implements JREnum
{
	/**
	 * Constant for setting normal spacing between lines.
	 */
	SINGLE((byte)0, "Single"),

	/**
	 * Constant for setting spacing between lines to 50% more than normal.
	 */
	ONE_AND_HALF((byte)1, "1_1_2"),
	
	/**
	 * Constant for setting spacing between lines to double size.
	 */
	DOUBLE((byte)2, "Double"),
	
	/**
	 * Constant for setting spacing between lines to at least a specified size.
	 */
	AT_LEAST((byte)3, "AtLeast"),
	
	/**
	 * Constant for setting spacing between lines to a specified size.
	 */
	FIXED((byte)4, "Fixed"),
	
	/**
	 * Constant for setting spacing between lines to a specified proportion of the normal line spacing.
	 */
	PROPORTIONAL((byte)5, "Proportional");
	
	
	/**
	 *
	 */
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;
	private final transient byte value;
	private final transient String name;

	private LineSpacingEnum(byte value, String name)
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
	public static LineSpacingEnum getByName(String name)
	{
		return (LineSpacingEnum)EnumUtil.getByName(values(), name);
	}
	
	/**
	 *
	 */
	public static LineSpacingEnum getByValue(Byte value)
	{
		return (LineSpacingEnum)EnumUtil.getByValue(values(), value);
	}
	
	/**
	 *
	 */
	public static LineSpacingEnum getByValue(byte value)
	{
		return getByValue(new Byte(value));
	}

}
