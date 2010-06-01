/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2009 Jaspersoft Corporation. All rights reserved.
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
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id: JasperCompileManager.java 3033 2009-08-27 11:46:22Z teodord $
 */
public enum WhenNoDataTypeEnum implements JREnum
{
	/**
	 * Specifies that in case of empty datasources, there will be an empty report.
	 */
	NO_PAGES((byte)1, "NoPages"),

	/**
	 * Specifies that in case of empty datasources, there will be a report with just one blank page.
	 */
	BLANK_PAGE((byte)2, "BlankPage"),

	/**
	 * Specifies that in case of empty datasources, all sections except detail will displayed.
	 */
	ALL_SECTIONS_NO_DETAIL((byte)3, "AllSectionsNoDetail"),

	/**
	 * Specifies that in case of empty datasources, the NoData section will be displayed.
	 */
	NO_DATA_SECTION((byte)4, "NoDataSection");

	/**
	 *
	 */
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;
	private final transient byte value;
	private final transient String name;

	private WhenNoDataTypeEnum(byte value, String name)
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
	public static WhenNoDataTypeEnum getByName(String name)
	{
		return (WhenNoDataTypeEnum)EnumUtil.getByName(values(), name);
	}
	
	/**
	 *
	 */
	public static WhenNoDataTypeEnum getByValue(Byte value)
	{
		return (WhenNoDataTypeEnum)EnumUtil.getByValue(values(), value);
	}
	
	/**
	 *
	 */
	public static WhenNoDataTypeEnum getByValue(byte value)
	{
		return getByValue(new Byte(value));
	}
	
}
