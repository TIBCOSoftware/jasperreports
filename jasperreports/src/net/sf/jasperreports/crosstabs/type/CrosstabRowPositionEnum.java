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
package net.sf.jasperreports.crosstabs.type;

import net.sf.jasperreports.engine.type.EnumUtil;
import net.sf.jasperreports.engine.type.JREnum;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public enum CrosstabRowPositionEnum implements JREnum
{
	/**
	 * Vertical stretch position indicating that the contents will be rendered on the top.
	 */
	TOP((byte)1, "Top"),
	
	/**
	 * Vertical stretch position indicating that the contents will be rendered on the middle.
	 */
	MIDDLE((byte)2, "Middle"),
	
	/**
	 * Vertical stretch position indicating that the contents will be rendered on the bottom.
	 */
	BOTTOM((byte)3, "Bottom"),
	
	/**
	 * Vertical stretch position indicating that the contents will be rendered vertically stretched.
	 */
	STRETCH((byte)4, "Stretch");

	/**
	 *
	 */
	private final transient byte value;
	private final transient String name;

	private CrosstabRowPositionEnum(byte value, String name)
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
	 * @deprecated Used only by deprecated serialized fields.
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
	public static CrosstabRowPositionEnum getByName(String name)
	{
		return EnumUtil.getEnumByName(values(), name);
	}
	
	/**
	 * @deprecated Used only by deprecated serialized fields.
	 */
	public static CrosstabRowPositionEnum getByValue(Byte value)
	{
		return (CrosstabRowPositionEnum)EnumUtil.getByValue(values(), value);
	}
	
	/**
	 * @deprecated Used only by deprecated serialized fields.
	 */
	public static CrosstabRowPositionEnum getByValue(byte value)
	{
		return getByValue(new Byte(value));
	}
}
