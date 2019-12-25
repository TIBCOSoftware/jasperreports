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


/**
 * @deprecated Replaced by {@link VerticalTextAlignEnum} and {@link VerticalImageAlignEnum}.
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public enum VerticalAlignEnum implements JREnum
{
	/**
	 *
	 */ 
	TOP((byte)1, "Top"),
	
	/**
	 *
	 */ 
	MIDDLE((byte)2, "Middle"),
	
	/**
	 *
	 */ 
	BOTTOM((byte)3, "Bottom"),
	
	/**
	 *
	 */ 
	JUSTIFIED((byte)4, "Justified");

	/**
	 *
	 */
	private final transient byte value;
	private final transient String name;

	private VerticalAlignEnum(byte value, String name)
	{
		this.value = value;
		this.name = name;
	}

	@Override
	public Byte getValueByte()
	{
		return value;
	}
	
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
	public static VerticalAlignEnum getByName(String name)
	{
		return (VerticalAlignEnum)EnumUtil.getByName(values(), name);
	}
	
	/**
	 *
	 */
	public static VerticalAlignEnum getByValue(Byte value)
	{
		return (VerticalAlignEnum)EnumUtil.getByValue(values(), value);
	}
	
	/**
	 *
	 */
	public static VerticalAlignEnum getByValue(byte value)
	{
		return getByValue((Byte)value);
	}
	
	/**
	 *
	 */
	public static VerticalTextAlignEnum getVerticalTextAlignEnum(VerticalAlignEnum verticalAlignment)
	{
		return verticalAlignment == null ? null : VerticalTextAlignEnum.getByName(verticalAlignment.getName());
	}
	
	/**
	 *
	 */
	public static VerticalImageAlignEnum getVerticalImageAlignEnum(VerticalAlignEnum verticalAlignment)
	{
		if (verticalAlignment == JUSTIFIED)
		{
			return VerticalImageAlignEnum.TOP;
		}
		return verticalAlignment == null ? null : VerticalImageAlignEnum.getByName(verticalAlignment.getName());
	}
	
	/**
	 *
	 */
	public static VerticalAlignEnum getVerticalAlignEnum(VerticalTextAlignEnum verticalTextAlign)
	{
		return verticalTextAlign == null ? null : VerticalAlignEnum.getByName(verticalTextAlign.getName());
	}
	
	/**
	 *
	 */
	public static VerticalAlignEnum getVerticalAlignEnum(VerticalImageAlignEnum verticalImageAlign)
	{
		return verticalImageAlign == null ? null : VerticalAlignEnum.getByName(verticalImageAlign.getName());
	}
}
