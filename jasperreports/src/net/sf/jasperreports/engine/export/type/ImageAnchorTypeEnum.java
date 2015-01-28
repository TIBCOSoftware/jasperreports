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
package net.sf.jasperreports.engine.export.type;

import net.sf.jasperreports.engine.type.EnumUtil;
import net.sf.jasperreports.engine.type.NamedEnum;


/**
 * @author sanda zaharia (shertage@users.sourceforge.net)
 */
public enum ImageAnchorTypeEnum implements NamedEnum
{
	/**
	 * Constant useful for specifying the <code>Move and size with cells</code> anchor type in Excel.
	 */
	MOVE_SIZE((byte)0, "MoveSize"),

	/**
	 * Constant useful for specifying the <code>Move but don't size with cells</code> anchor type in Excel.
	 */
	MOVE_NO_SIZE((byte)2, "MoveNoSize"),
	
	/**
	 * Constant useful for specifying the <code>Don't move or size with cells</code> anchor type in Excel.
	 */
	NO_MOVE_NO_SIZE((byte)3, "NoMoveNoSize");
	
	
	/**
	 *
	 */
	private final transient byte value;
	private final transient String name;

	private ImageAnchorTypeEnum(byte value, String name)
	{
		this.value = value;
		this.name = name;
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
	public static ImageAnchorTypeEnum getByName(String name)
	{
		return EnumUtil.getEnumByName(values(), name);
	}
}
