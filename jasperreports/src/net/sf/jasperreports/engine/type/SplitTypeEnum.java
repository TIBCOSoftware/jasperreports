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
public enum SplitTypeEnum implements JREnum
{
	/**
	 * The band is allowed to split, but never within its declared height. 
	 * This means the band splits only when its content stretches.
	 */
	STRETCH((byte)1, "Stretch"),

	/**
	 * Prevents the band from splitting on first break attempt. 
	 * On subsequent pages/columns, the band is allowed to split, to avoid infinite loops.
	 */
	PREVENT((byte)2, "Prevent"),

	/**
	 * The band is allowed to split anywhere, as early as needed, 
	 * but not before at least one element being printed on the current page/column.
	 */
	IMMEDIATE((byte)3, "Immediate");

	/**
	 *
	 */
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;
	private final transient byte value;
	private final transient String name;

	private SplitTypeEnum(byte value, String name)
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
	public static SplitTypeEnum getByName(String name)
	{
		return (SplitTypeEnum)EnumUtil.getByName(values(), name);
	}
	
	/**
	 *
	 */
	public static SplitTypeEnum getByValue(Byte value)
	{
		return (SplitTypeEnum)EnumUtil.getByValue(values(), value);
	}
	
	/**
	 *
	 */
	public static SplitTypeEnum getByValue(byte value)
	{
		return getByValue(new Byte(value));
	}
	
}
