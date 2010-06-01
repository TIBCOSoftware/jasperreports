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
package net.sf.jasperreports.crosstabs.type;

import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.type.EnumUtil;
import net.sf.jasperreports.engine.type.JREnum;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id: JasperCompileManager.java 3033 2009-08-27 11:46:22Z teodord $
 */
public enum CrosstabColumnPositionEnum implements JREnum
{
	/**
	 * Horizontal stretch position indicating that the contents will be rendered on the left side.
	 */
	LEFT((byte)1, "Left"),
	
	/**
	 * Horizontal stretch position indicating that the contents will be rendered on the center.
	 */
	CENTER((byte)2, "Center"),
	
	/**
	 * Horizontal stretch position indicating that the contents will be rendered on the right side.
	 */
	RIGHT((byte)3, "Right"),
	
	/**
	 * Horizontal stretch position indicating that the contents will be horizontally stretched.
	 */
	STRETCH((byte)4 ,"Stretch");

	/**
	 *
	 */
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;
	private final transient byte value;
	private final transient String name;

	private CrosstabColumnPositionEnum(byte value, String name)
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
	public static CrosstabColumnPositionEnum getByName(String name)
	{
		return (CrosstabColumnPositionEnum)EnumUtil.getByName(values(), name);
	}
	
	/**
	 *
	 */
	public static CrosstabColumnPositionEnum getByValue(Byte value)
	{
		return (CrosstabColumnPositionEnum)EnumUtil.getByValue(values(), value);
	}
	
	/**
	 *
	 */
	public static CrosstabColumnPositionEnum getByValue(byte value)
	{
		return getByValue(new Byte(value));
	}
	
}
