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
package net.sf.jasperreports.components.spiderchart.type;

import net.sf.jasperreports.engine.type.EnumUtil;
import net.sf.jasperreports.engine.type.NamedEnum;

import org.jfree.util.Rotation;


/**
 * @author sanda zaharia (shertage@users.sourceforge.net)
 */
public enum SpiderRotationEnum implements NamedEnum
{
	/**
	 *
	 */
	CLOCKWISE(Rotation.CLOCKWISE, "Clockwise"),

	/**
	 *
	 */
	ANTICLOCKWISE(Rotation.ANTICLOCKWISE, "Anticlockwise");


	/**
	 *
	 */
	private final transient Rotation value;
	private final transient String name;

	private SpiderRotationEnum(Rotation rotation, String name)
	{
		this.value = rotation;
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
	public final Rotation getRotation()
	{
		return this.value;
	}
	
	/**
	 *
	 */
	public static SpiderRotationEnum getByName(String name)
	{
		return EnumUtil.getEnumByName(values(), name);
	}
}
