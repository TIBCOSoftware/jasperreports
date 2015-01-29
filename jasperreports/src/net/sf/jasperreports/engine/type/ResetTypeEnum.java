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

import net.sf.jasperreports.engine.JRVariable;


/**
 * @author sanda zaharia (shertage@users.sourceforge.net)
 */
public enum ResetTypeEnum implements JREnum
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
	NONE((byte)5, "None"),
	
	/**
	 * Used internally by the master report page variables to allow the variables to be used in
	 * text fields with {@link EvaluationTimeEnum#AUTO Auto} evaluation time.
	 * 
	 * @see JRVariable#MASTER_CURRENT_PAGE
	 * @see JRVariable#MASTER_TOTAL_PAGES
	 */
	MASTER((byte) 6, "Master");

	/**
	 *
	 */
	private final transient byte value;
	private final transient String name;

	private ResetTypeEnum(byte value, String name)
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
	public static ResetTypeEnum getByName(String name)
	{
		return EnumUtil.getEnumByName(values(), name);
	}
	
	/**
	 * @deprecated Used only by deprecated serialized fields.
	 */
	public static ResetTypeEnum getByValue(Byte value)
	{
		return (ResetTypeEnum)EnumUtil.getByValue(values(), value);
	}
	
	/**
	 *
	 */
	public static ResetTypeEnum getByValue(byte value)
	{
		return getByValue(new Byte(value));
	}
}
