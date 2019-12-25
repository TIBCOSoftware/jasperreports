/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2019 TIBCO Software Inc. All rights reserved.
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

import net.sf.jasperreports.engine.JRElementDataset;

/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public enum DatasetResetTypeEnum implements JREnum
{
	/**
	 * The dataset is initialized only once, at the beginning of the report filling process.
	 */
	REPORT((byte)1, "Report"),
	
	/**
	 * The dataset is reinitialized at the beginning of each new page.
	 */
	PAGE((byte)2, "Page"),
	
	/**
	 * The dataset is reinitialized at the beginning of each new column.
	 */
	COLUMN((byte)3, "Column"),
	
	/**
	 * The dataset is reinitialized every time the group specified by the {@link JRElementDataset#getResetGroup()} method breaks.
	 */
	GROUP((byte)4, "Group"),
	
	/**
	 * The dataset will never be initialized.
	 */
	NONE((byte)5, "None");
	
	/**
	 *
	 */
	private final transient byte value;
	private final transient String name;

	private DatasetResetTypeEnum(byte value, String name)
	{
		this.value = value;
		this.name = name;
	}

	/**
	 * @deprecated Used only by deprecated serialized fields.
	 */
	@Override
	public Byte getValueByte()
	{
		return value;
	}
	
	/**
	 * @deprecated Used only by deprecated serialized fields.
	 */
	@Override
	public final byte getValue()
	{
		return value;
	}
	
	@Override
	public String getName()
	{
		return name;
	}
	
	/**
	 *
	 */
	public static DatasetResetTypeEnum getByName(String name)
	{
		return EnumUtil.getEnumByName(values(), name);
	}
	
	/**
	 * @deprecated Used only by deprecated serialized fields.
	 */
	public static DatasetResetTypeEnum getByValue(Byte value)
	{
		return (DatasetResetTypeEnum)EnumUtil.getByValue(values(), value);
	}
	
	/**
	 *
	 */
	public static DatasetResetTypeEnum getByValue(byte value)
	{
		return getByValue((Byte)value);
	}
}
