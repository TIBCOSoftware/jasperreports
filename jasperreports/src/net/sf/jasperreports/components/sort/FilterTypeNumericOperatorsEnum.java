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
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public enum FilterTypeNumericOperatorsEnum implements JREnum
{
	EQUALS((byte)1, "Equals"),
	
	DOES_NOT_EQUAL((byte)2, "Does Not Equal"),

	GREATER_THAN((byte)3, "Greater Than"),

	GREATER_THAN_EQUAL_TO((byte)4, "Greater Than or Equal to"),
	
	LESS_THAN((byte)5, "Less Than"),
	
	LESS_THAN_EQUAL_TO((byte)6, "Less Than or Equal to"),
	
	IS_BETWEEN((byte)7, "Is Between"),
	
	IS_NOT_BETWEEN((byte)8, "Is not between");

	/**
	 *
	 */
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;
	private final transient byte value;
	private final transient String name;

	private FilterTypeNumericOperatorsEnum(byte value, String name)
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
	public static FilterTypeNumericOperatorsEnum getByName(String name)
	{
		return (FilterTypeNumericOperatorsEnum)EnumUtil.getByName(values(), name);
	}
	
	/**
	 *
	 */
	public static FilterTypeNumericOperatorsEnum getByEnumConstantName(String name)
	{
		return (FilterTypeNumericOperatorsEnum)EnumUtil.getByEnumConstantName(values(), name);
	}
	
	/**
	 *
	 */
	public static FilterTypeNumericOperatorsEnum getByValue(Byte value)
	{
		return (FilterTypeNumericOperatorsEnum)EnumUtil.getByValue(values(), value);
	}
	
	/**
	 *
	 */
	public static FilterTypeNumericOperatorsEnum getByValue(byte value)
	{
		return getByValue(new Byte(value));
	}
	
}
