/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2013 Jaspersoft Corporation. All rights reserved.
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
package net.sf.jasperreports.export.type;

import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.type.EnumUtil;
import net.sf.jasperreports.engine.type.JREnum;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public enum PdfVersionEnum implements JREnum
{
	/**
	 * 
	 */
	VERSION_1_2((byte)102, "2"),

	/**
	 * 
	 */
	VERSION_1_3((byte)103, "3"),

	/**
	 * 
	 */
	VERSION_1_4((byte)104, "4"),

	/**
	 * 
	 */
	VERSION_1_5((byte)105, "5"),

	/**
	 * 
	 */
	VERSION_1_6((byte)106, "6"),

	/**
	 * 
	 */
	VERSION_1_7((byte)107, "7");
	
	/**
	 *
	 */
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;
	private final transient byte value;
	private final transient String name;

	private PdfVersionEnum(byte value, String name)
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
	public static PdfVersionEnum getByName(String name)
	{
		return (PdfVersionEnum)EnumUtil.getByName(values(), name);
	}
	
	/**
	 *
	 */
	public static PdfVersionEnum getByValue(Byte value)
	{
		return (PdfVersionEnum)EnumUtil.getByValue(values(), value);
	}
	
	/**
	 *
	 */
	public static PdfVersionEnum getByValue(byte value)
	{
		return getByValue(new Byte(value));
	}

}
