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
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public enum EvaluationTimeEnum implements JREnum
{
	/**
	 * A constant specifying that an expression should be evaluated at the exact moment in the filling process
	 * when it is encountered.
	 */
	NOW((byte)1, "Now"),

	/**
	 * A constant specifying that an expression should be evaluated at the end of the filling process.
	 */
	REPORT((byte)2, "Report"),

	/**
	 * A constant specifying that an expression should be evaluated after each page is filled.
	 */
	PAGE((byte)3, "Page"),

	/**
	 * A constant specifying that an expression should be evaluated after each column is filled.
	 */
	COLUMN((byte)4, "Column"),

	/**
	 * A constant specifying that an expression should be evaluated after each group break.
	 */
	GROUP((byte)5, "Group"),

	/**
	 * The element will be evaluated at band end.
	 */
	BAND((byte)6, "Band"),
	
	/**
	 * Evaluation time indicating that each variable participating in the expression
	 * should be evaluated at a time decided by the engine.
	 * <p/>
	 * Variables will be evaluated at a time corresponding to their reset type.
	 * Fields are evaluated "now", i.e. at the time the band the element lies on gets filled.
	 * <p/>
	 * This evaluation type should be used when report elements having expressions that combine 
	 * values evaluated at different times are required (e.g. percentage out of a total).
	 * <p/>
	 * NB: avoid using this evaluation type when other types suffice as it can lead
	 * to performance loss.
	 */
	AUTO((byte)7, "Auto"),
	
	/**
	 * Used for elements that are evaluated at the moment the master report ends.
	 * 
	 * @see JRVariable#MASTER_CURRENT_PAGE
	 * @see JRVariable#MASTER_TOTAL_PAGES
	 */
	MASTER((byte) 8, "Master");

	/**
	 *
	 */
	private final transient byte value;
	private final transient String name;

	private EvaluationTimeEnum(byte value, String name)
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
	public static EvaluationTimeEnum getByName(String name)
	{
		return EnumUtil.getEnumByName(values(), name);
	}
	
	/**
	 * @deprecated Used only by deprecated serialized fields.
	 */
	public static EvaluationTimeEnum getByValue(Byte value)
	{
		return (EvaluationTimeEnum)EnumUtil.getByValue(values(), value);
	}
	
	/**
	 * @deprecated Used only by deprecated serialized fields.
	 */
	public static EvaluationTimeEnum getByValue(byte value)
	{
		return getByValue(new Byte(value));
	}
}
