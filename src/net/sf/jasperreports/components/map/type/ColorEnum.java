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
package net.sf.jasperreports.components.map.type;

import net.sf.jasperreports.engine.JRConstants;


/**
 * @author Sanda Zaharia (shertage@users.sourceforge.net)
 * @version $Id$
 */
public enum ColorEnum
{
	/**
	 *
	 */
	BLACK("000000", "black"),

	/**
	 *
	 */
	BLUE("0000FF", "blue"),

	/**
	 *
	 */
	GRAY("808080", "gray"),

	/**
	 *
	 */
	GREEN("00FF00", "green"),

	/**
	 *
	 */
	BROWN("A52A2A", "brown"),

	/**
	 *
	 */
	ORANGE("FFA500", "orange"),

	/**
	 *
	 */
	PURPLE("800080", "purple"),

	/**
	 *
	 */
	RED("FF0000", "red"),

	/**
	 *
	 */
	YELLOW("FFFF00", "yellow"),

	/**
	 *
	 */
	WHITE("FFFFFF", "white");

	/**
	 *
	 */
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;
	private final transient String hexColor;
	private final transient String name;

	private ColorEnum(String hexColor, String name)
	{
		this.hexColor = hexColor;
		this.name = name;
	}

	/**
	 *
	 */
	public final String getHexColor()
	{
		return hexColor;
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
	public static ColorEnum getByName(String name)
	{
		ColorEnum[] values = values();
		if (values != null && name != null)
		{
			for(ColorEnum e:values)
			{
				if (name.equals(e.getName()))
				{
					return e;
				}
			}
		}
		return null;
	}
	
	/**
	 *
	 */
	public static ColorEnum getByHexColor(String hexColor)
	{
		ColorEnum[] values = values();
		if (values != null && hexColor != null)
		{
			for(ColorEnum e:values)
			{
				if (hexColor.equals(e.getHexColor()))
				{
					return e;
				}
			}
		}
		return null;
	}
	
}
