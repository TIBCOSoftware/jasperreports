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
package net.sf.jasperreports.charts.type;

import net.sf.jasperreports.engine.type.EnumUtil;
import net.sf.jasperreports.engine.type.NamedEnum;

import org.jfree.data.time.Day;
import org.jfree.data.time.Hour;
import org.jfree.data.time.Millisecond;
import org.jfree.data.time.Minute;
import org.jfree.data.time.Month;
import org.jfree.data.time.Quarter;
import org.jfree.data.time.Second;
import org.jfree.data.time.Week;
import org.jfree.data.time.Year;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public enum TimePeriodEnum implements NamedEnum
{
	/**
	 *
	 */
	YEAR(Year.class, "Year"),

	/**
	 *
	 */
	QUARTER(Quarter.class, "Quarter"),
	
	/**
	 *
	 */
	MONTH(Month.class, "Month"),
	
	/**
	 *
	 */
	WEEK(Week.class, "Week"),
	
	/**
	 *
	 */
	DAY(Day.class, "Day"),
	
	/**
	 *
	 */
	HOUR(Hour.class, "Hour"),
	
	/**
	 *
	 */
	MINUTE(Minute.class, "Minute"),
	
	/**
	 *
	 */
	SECOND(Second.class, "Second"),
	
	/**
	 *
	 */
	MILLISECOND(Millisecond.class, "Milisecond");//FIXMENOW should we fix this spelling error?


	/**
	 *
	 */
	private final transient Class<?> value;
	private final transient String name;

	private TimePeriodEnum(Class<?> clazz, String name)
	{
		this.value = clazz;
		this.name = name;
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
	public final Class<?> getTimePeriod()
	{
		return this.value;
	}
	
	/**
	 *
	 */
	public static TimePeriodEnum getByName(String name)
	{
		return EnumUtil.getEnumByName(values(), name);
	}
	
	/**
	 *
	 */
	public static TimePeriodEnum getByValue(Class<?> clazz)
	{
		TimePeriodEnum[] values = values();
		if (values != null && clazz != null)
		{
			for(TimePeriodEnum e:values)
			{
				if (clazz.getName().equals(e.getTimePeriod().getName()))
				{
					return e;
				}
			}
		}
		return null;
	}
	
}
