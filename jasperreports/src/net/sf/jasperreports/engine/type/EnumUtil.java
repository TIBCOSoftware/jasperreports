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




/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id: JasperCompileManager.java 3033 2009-08-27 11:46:22Z teodord $
 */
public final class EnumUtil
{
	
	/**
	 *
	 */
	public static JREnum getByValue(JREnum[] values, Integer value)
	{
		if (values != null && value != null)
		{
			return getByValue(values, new Byte(value.byteValue()));
		}
		return null;
	}

	/**
	 *
	 */
	public static JREnum getByValue(JREnum[] values, Byte value)
	{
		if (values != null && value != null)
		{
			for(JREnum e:values)
			{
				if (value.equals(e.getValueByte()))
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
	public static JREnum getByName(JREnum[] values, String name)
	{
		if (values != null && name != null)
		{
			for(JREnum e:values)
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
	private EnumUtil()
	{
	}
}
