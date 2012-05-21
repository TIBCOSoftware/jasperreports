/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2011 Jaspersoft Corporation. All rights reserved.
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
import net.sf.jasperreports.engine.JRVariable;


/**
 * @author sanda zaharia (shertage@users.sourceforge.net)
 * @version $Id$
 */
public enum IncrementTypeEnum implements JREnum
{
	/**
	 * The variable is initialized only once, at the beginning of the report filling process, with the value returned by
	 * the variable's initial value expression.
	 */
	REPORT((byte)1, "Report"),
	
	/**
	 * The variable is reinitialized at the beginning of each new page.
	 */
	PAGE((byte)2, "Page"),
	
	/**
	 * The variable is reinitialized at the beginning of each new column.
	 */
	COLUMN((byte)3, "Column"),
	
	/**
	 * The variable is reinitialized every time the group specified by the {@link JRVariable#getResetGroup()} method breaks.
	 */
	GROUP((byte)4, "Group"),
	
	/**
	 * The variable will never be initialized using its initial value expression and will only contain values obtained by
	 * evaluating the variable's expression.
	 */
	NONE((byte)5, "None");

	/**
	 *
	 */
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;
	private final transient byte value;
	private final transient String name;

	private IncrementTypeEnum(byte value, String name)
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
	public static IncrementTypeEnum getByName(String name)
	{
		return (IncrementTypeEnum)EnumUtil.getByName(values(), name);
	}
	
	/**
	 *
	 */
	public static IncrementTypeEnum getByValue(Byte value)
	{
		return (IncrementTypeEnum)EnumUtil.getByValue(values(), value);
	}
	
	/**
	 *
	 */
	public static IncrementTypeEnum getByValue(byte value)
	{
		return getByValue(new Byte(value));
	}

}
