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
import net.sf.jasperreports.engine.JRHyperlink;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id: JasperCompileManager.java 3033 2009-08-27 11:46:22Z teodord $
 */
public enum HyperlinkTargetEnum implements JREnum
{
	/**
	 * Target not defined.
	 */
	NONE((byte)0, "None"),

	/**
	 * Constant useful for specifying that the hyperlink will be opened in the same window.
	 */
	SELF((byte)1, "Self"),

	/**
	 * Constant useful for specifying that the hyperlink will be opened in a new window.
	 */
	BLANK((byte)2, "Blank"),

	/**
	 * Constant useful for specifying that the hyperlink will be opened in the parent frame.
	 */
	PARENT((byte)3, "Parent"),

	/**
	 * Constant useful for specifying that the hyperlink will be opened in the top frame.
	 */
	TOP((byte)4, "Top"),

	/**
	 * Custom hyperlink target name.
	 * <p>
	 * The specific target name is determined by {@link JRHyperlink#getLinkTarget() getLinkTarget()}.
	 * </p>
	 */
	CUSTOM((byte)5, "Custom");

	/**
	 *
	 */
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;
	private final transient byte value;
	private final transient String name;

	private HyperlinkTargetEnum(byte value, String name)
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
	public static HyperlinkTargetEnum getByName(String name)
	{
		return (HyperlinkTargetEnum)EnumUtil.getByName(values(), name);
	}
	
	/**
	 *
	 */
	public static HyperlinkTargetEnum getByValue(Byte value)
	{
		return (HyperlinkTargetEnum)EnumUtil.getByValue(values(), value);
	}
	
	/**
	 *
	 */
	public static HyperlinkTargetEnum getByValue(byte value)
	{
		return getByValue(new Byte(value));
	}
	
}
