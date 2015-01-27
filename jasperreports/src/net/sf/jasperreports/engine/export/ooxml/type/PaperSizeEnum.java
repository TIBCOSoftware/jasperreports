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

import net.sf.jasperreports.engine.type.EnumUtil;
import net.sf.jasperreports.engine.type.NamedEnum;


/**
 * @author sanda zaharia (shertage@users.sourceforge.net)
 */
public enum PaperSizeEnum implements NamedEnum
{
	/**
	 * Specifies an undefined paper size.
	 */ 
	UNDEFINED((byte)-1, "Undefined", -1, -1),
	
	/**
	 * Specifies the Letter paper size.
	 * ANSI X3.151-1987 - "Letter" (216 x 279 mm)
	 */ 
	LETTER((byte)1, "Letter", 216, 279),
	
	/**
	 * Specifies the Legal paper size.
	 * ANSI X3.151-1987 - "Legal" (216 x 356 mm)
	 */ 
	LEGAL((byte)5, "Legal", 216, 356),
	
	/**
	 * Specifies the Executive paper size.
	 * ANSI X3.151-1987 - "Executive" (190 x 254 mm)
	 */ 
	EXECUTIVE((byte)7, "Executive", 190, 254),
	
	/**
	 * Specifies the A3 paper size.
	 * ISO 216 - "A3" (297 x 420 mm)
	 */ 
	A3((byte)8, "A3", 297, 420),
	
	/**
	 * Specifies the A4 paper size.
	 * ISO 216 - "A4" (210 x 297 mm)
	 */ 
	A4((byte)9, "A4", 210, 297),
	
	/**
	 * Specifies the A5 paper size.
	 * ISO 216 - "A5" (148 x 210 mm)
	 */ 
	A5((byte)11, "A5", 148, 210),

//	/**
//	 * Specifies the A5 paper size.
//	 * ANSI X3.151-1987 - "Ledger/Tabloid" (279 x 432 mm)
//	 */ 
//	LEDGER_TABLOID((byte)xx, "Ledger/Tabloid", 279, 432),
		
	/**
	 * Specifies the DL Envelope paper size.
	 * ISO 269 - "Envelope DL" (110 x 220 mm)
	 */ 
	ENVELOPE_DL((byte)27, "Envelope_DL", 110, 220);
	
	/**
	 *
	 */
	private final transient byte ooxmlValue;
	private final transient String name;
	private final transient int width;
	private final transient int height;

	private PaperSizeEnum(
		byte ooxmlValue, 
		String name,
		int width,
		int height
		)
	{
		this.ooxmlValue = ooxmlValue;
		this.name = name;
		this.width = width;
		this.height = height;
	}

	/**
	 *
	 */
	public final byte getOoxmlValue()
	{
		return ooxmlValue;
	}
	
	/**
	 *
	 */
	public final int getWidth()
	{
		return width;
	}
	
	/**
	 *
	 */
	public final int getHeight()
	{
		return height;
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
		return EnumUtil.getEnumByName(values(), name);
	}
}
