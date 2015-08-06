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

import net.sf.jasperreports.engine.JRVariable;


/**
 * @author sanda zaharia (shertage@users.sourceforge.net)
 */
public enum IncrementTypeEnum implements JREnum
{
	/**
	 * The variable never gets incremented during the report-filling process.
	 */
	REPORT((byte)1, "Report"),
	
	/**
	 * The variable is incremented with each new page.
	 */
	PAGE((byte)2, "Page"),
	
	/**
	 * The variable is incremented with each new column.
	 */
	COLUMN((byte)3, "Column"),
	
	/**
	 * The variable is incremented every time the group specified by the {@link JRVariable#getIncrementGroup()} method breaks.
	 */
	GROUP((byte)4, "Group"),
	
	/**
	 * The variable is incremented with every record during the iteration through the data source.
	 */
	NONE((byte)5, "None");

	/**
	 *
	 */
	private final transient byte value;
	private final transient String name;

	private IncrementTypeEnum(byte value, String name)
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
	 *
	 */
	@SuppressWarnings("deprecation")
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
	public static IncrementTypeEnum getByName(String name)
	{
		return EnumUtil.getEnumByName(values(), name);
	}
	
	/**
	 * @deprecated Used only by deprecated serialized fields.
	 */
	public static IncrementTypeEnum getByValue(Byte value)
	{
		return (IncrementTypeEnum)EnumUtil.getByValue(values(), value);
	}
	
	/**
	 * @deprecated Used only by deprecated serialized fields.
	 */
	public static IncrementTypeEnum getByValue(byte value)
	{
		return getByValue(new Byte(value));
	}
}
