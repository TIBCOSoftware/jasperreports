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
package net.sf.jasperreports.components.spiderchart.type;

import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.type.EnumUtil;
import net.sf.jasperreports.engine.type.JREnum;

import org.jfree.util.TableOrder;


/**
 * @author sanda zaharia (shertage@users.sourceforge.net)
 * @version $Id: TableOrderEnum.java 3874 2010-07-13 14:58:41Z shertage $
 */
public enum TableOrderEnum implements JREnum
{
	/**
	 *
	 */
	BY_ROW(TableOrder.BY_ROW, "Row"),

	/**
	 *
	 */
	BY_COLUMN(TableOrder.BY_COLUMN, "Column");


	/**
	 *
	 */
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;
	private final transient TableOrder value;
	private final transient String name;

	private TableOrderEnum(TableOrder order, String name)
	{
		this.value = order;
		this.name = name;
	}

	/**
	 *
	 */
	public Byte getValueByte()
	{
		return new Byte(getValue());
	}
	
	/**
	 *
	 */
	public final byte getValue()
	{
		return (byte)-1;
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
	public final TableOrder getOrder()
	{
		return this.value;
	}
	
	/**
	 *
	 */
	public static TableOrderEnum getByName(String name)
	{
		return (TableOrderEnum)EnumUtil.getByName(values(), name);
	}
	
	/**
	 *
	 */
	public static TableOrderEnum getByValue(TableOrder order)
	{
		TableOrderEnum[] values = values();
		if (values != null && order != null)
		{
			for(TableOrderEnum e:values)
			{
				if (order.equals(e.getOrder()))
				{
					return e;
				}
			}
		}
		return null;
	}
	
}
