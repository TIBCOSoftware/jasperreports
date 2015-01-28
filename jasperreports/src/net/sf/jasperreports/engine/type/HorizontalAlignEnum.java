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


/**
 * @deprecated Replaced by {@link HorizontalTextAlignEnum} and {@link HorizontalImageAlignEnum}.
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public enum HorizontalAlignEnum implements JREnum
{
	/**
	 *
	 */ 
	LEFT((byte)1, "Left"),
	
	/**
	 *
	 */ 
	CENTER((byte)2, "Center"),
	
	/**
	 *
	 */ 
	RIGHT((byte)3, "Right"),
	
	/**
	 *
	 */ 
	JUSTIFIED((byte)4, "Justified");
	
	/**
	 *
	 */
	private final transient byte value;
	private final transient String name;

	private HorizontalAlignEnum(byte value, String name)
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
	public static HorizontalAlignEnum getByName(String name)
	{
		return (HorizontalAlignEnum)EnumUtil.getByName(values(), name);
	}
	
	/**
	 *
	 */
	public static HorizontalAlignEnum getByValue(Byte value)
	{
		return (HorizontalAlignEnum)EnumUtil.getByValue(values(), value);
	}
	
	/**
	 *
	 */
	public static HorizontalAlignEnum getByValue(byte value)
	{
		return getByValue(new Byte(value));
	}
	
	/**
	 *
	 */
	public static HorizontalTextAlignEnum getHorizontalTextAlignEnum(HorizontalAlignEnum horizontalAlignment)
	{
		return horizontalAlignment == null ? null : HorizontalTextAlignEnum.getByName(horizontalAlignment.getName());
	}
	
	/**
	 *
	 */
	public static HorizontalImageAlignEnum getHorizontalImageAlignEnum(HorizontalAlignEnum horizontalAlignment)
	{
		if (horizontalAlignment == JUSTIFIED)
		{
			return HorizontalImageAlignEnum.LEFT;
		}
		return horizontalAlignment == null ? null : HorizontalImageAlignEnum.getByName(horizontalAlignment.getName());
	}
	
	/**
	 *
	 */
	public static HorizontalAlignEnum getHorizontalAlignEnum(HorizontalTextAlignEnum horizontalTextAlign)
	{
		return horizontalTextAlign == null ? null : HorizontalAlignEnum.getByName(horizontalTextAlign.getName());
	}

	/**
	 *
	 */
	public static HorizontalAlignEnum getHorizontalAlignEnum(HorizontalImageAlignEnum horizontalImageAlign)
	{
		return horizontalImageAlign == null ? null : HorizontalAlignEnum.getByName(horizontalImageAlign.getName());
	}
	
}
