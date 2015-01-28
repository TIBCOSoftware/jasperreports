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
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public enum RenderableTypeEnum implements JREnum
{
	/**
	 * Specifies that the renderable is rendering an image.
	 */ 
	IMAGE((byte)0, "Image"),
	
	/**
	 * Specifies that the renderable is rendering a SVG.
	 */ 
	SVG((byte)1, "SVG");

	/**
	 *
	 */
	private final transient byte value;
	private final transient String name;

	private RenderableTypeEnum(byte value, String name)
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
	public static RenderableTypeEnum getByName(String name)
	{
		return EnumUtil.getEnumByName(values(), name);
	}
	
	/**
	 * @deprecated Used only by deprecated serialized fields.
	 */
	public static RenderableTypeEnum getByValue(Byte value)
	{
		return (RenderableTypeEnum)EnumUtil.getByValue(values(), value);
	}
	
	/**
	 * @deprecated Used only by deprecated serialized fields.
	 */
	public static RenderableTypeEnum getByValue(byte value)
	{
		return getByValue(new Byte(value));
	}
}
