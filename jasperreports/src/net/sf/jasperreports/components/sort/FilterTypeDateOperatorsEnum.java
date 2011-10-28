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
public enum FilterTypeDateOperatorsEnum implements JREnum
{
	EQUALS((byte)1, "Equals"),
	
	IS_NOT_EQUAL_TO((byte)2, "Is not Equal to"),

	IS_BETWEEN((byte)3, "Is Between"),

	IS_NOT_BETWEEN((byte)4, "Is not between"),
	
	IS_ON_OR_BEFORE((byte)5, "Is On or Before"),
	
	IS_BEFORE((byte)6, "Is Before"),
	
	IS_ON_OR_AFTER((byte)7, "Is On or After"),
	
	IS_AFTER((byte)8, "Is After");

	/**
	 *
	 */
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;
	private final transient byte value;
	private final transient String name;

	private FilterTypeDateOperatorsEnum(byte value, String name)
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
	public static FilterTypeDateOperatorsEnum getByName(String name)
	{
		return (FilterTypeDateOperatorsEnum)EnumUtil.getByName(values(), name);
	}
	
	/**
	 *
	 */
	public static FilterTypeDateOperatorsEnum getByEnumConstantName(String name)
	{
		return (FilterTypeDateOperatorsEnum)EnumUtil.getByEnumConstantName(values(), name);
	}
	
	/**
	 *
	 */
	public static FilterTypeDateOperatorsEnum getByValue(Byte value)
	{
		return (FilterTypeDateOperatorsEnum)EnumUtil.getByValue(values(), value);
	}
	
	/**
	 *
	 */
	public static FilterTypeDateOperatorsEnum getByValue(byte value)
	{
		return getByValue(new Byte(value));
	}
	
}
