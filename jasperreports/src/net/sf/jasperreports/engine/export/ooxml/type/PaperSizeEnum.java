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
package net.sf.jasperreports.engine.export.ooxml.type;

import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.type.EnumUtil;
import net.sf.jasperreports.engine.type.JREnum;


/**
 * @author sanda zaharia (shertage@users.sourceforge.net)
 */
public enum PaperSizeEnum implements JREnum
{
	/**
	 * Specifies an undefined paper size.
	 */ 
	UNDEFINED((byte)-1, "Undefined"),
	
	/**
	 * Specifies the Letter paper size.
	 */ 
	LETTER((byte)1, "Letter"),
	
	/**
	 * Specifies the Legal paper size.
	 */ 
	LEGAL((byte)5, "Legal"),
	
	/**
	 * Specifies the Executive paper size.
	 */ 
	EXECUTIVE((byte)7, "Executive"),
	
	/**
	 * Specifies the A3 paper size.
	 */ 
	A3((byte)8, "A3"),
	
	/**
	 * Specifies the A4 paper size.
	 */ 
	A4((byte)9, "A4"),
	
	/**
	 * Specifies the A5 paper size.
	 */ 
	A5((byte)11, "A5"),
	
	/**
	 * Specifies the DL Envelope paper size.
	 */ 
	ENVELOPE_DL((byte)27, "Envelope_DL");
	
	/**
	 *
	 */
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;
	private final transient byte value;
	private final transient String name;

	private PaperSizeEnum(byte value, String name)
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
	public static PaperSizeEnum getByName(String name)
	{
		return (PaperSizeEnum)EnumUtil.getByName(values(), name);
	}
	
	/**
	 *
	 */
	public static PaperSizeEnum getByValue(Byte value)
	{
		return (PaperSizeEnum)EnumUtil.getByValue(values(), value);
	}
	
	/**
	 *
	 */
	public static PaperSizeEnum getByValue(byte value)
	{
		return getByValue(new Byte(value));
	}
	
}
