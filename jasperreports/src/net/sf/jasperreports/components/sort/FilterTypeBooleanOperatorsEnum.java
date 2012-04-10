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
package net.sf.jasperreports.components.sort;

import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.type.EnumUtil;
import net.sf.jasperreports.engine.type.JREnum;


/**
 * @author Narcis Marcu (narcism@users.sourceforge.net)
 * @version $Id$
 */
public enum FilterTypeBooleanOperatorsEnum implements JREnum
{
	IS_TRUE((byte)1, "Is true"),
	
	IS_NOT_TRUE((byte)2, "Is not true"),

	IS_FALSE((byte)3, "Is false"),

	IS_NOT_FALSE((byte)4, "Is not false");
	
	/**
	 *
	 */
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;
	private final transient byte value;
	private final transient String name;

	private FilterTypeBooleanOperatorsEnum(byte value, String name)
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
	public static FilterTypeBooleanOperatorsEnum getByName(String name)
	{
		return (FilterTypeBooleanOperatorsEnum)EnumUtil.getByName(values(), name);
	}
	
	/**
	 *
	 */
	public static FilterTypeBooleanOperatorsEnum getByEnumConstantName(String name)
	{
		return (FilterTypeBooleanOperatorsEnum)EnumUtil.getByEnumConstantName(values(), name);
	}
	
	/**
	 *
	 */
	public static FilterTypeBooleanOperatorsEnum getByValue(Byte value)
	{
		return (FilterTypeBooleanOperatorsEnum)EnumUtil.getByValue(values(), value);
	}
	
	/**
	 *
	 */
	public static FilterTypeBooleanOperatorsEnum getByValue(byte value)
	{
		return getByValue(new Byte(value));
	}
	
}
