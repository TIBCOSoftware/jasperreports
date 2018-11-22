/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2018 TIBCO Software Inc. All rights reserved.
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
import net.sf.jasperreports.engine.type.NamedValueEnum;

/**
 * @author Sanda Zaharia (shertage@users.sourceforge.net)
 */
public enum PptxDateTimeEnum implements NamedValueEnum<String>
{

	/**
	 *
	 */
	DATETIME("datetime","datetime"),
	
	DATETIME1("MM/dd/yyyy","datetime1"),
	
	DATETIME2("EEEE, MMMM dd, yyyy","datetime2"),
	
	DATETIME3("dd MMMM yyyy","datetime3"),
	
	DATETIME4("MMMM dd, yyyy","datetime4"),
	
	DATETIME5("dd-MMM-yy","datetime5"),
	
	DATETIME6("MMMM yy","datetime6"),
	
	DATETIME7("MMM-yy","datetime7"),
	
	DATETIME8("MM/dd/yyyy hh:mm a","datetime8"),
	
	DATETIME9("MM/dd/yyyy hh:mm:ss a","datetime9"),
	
	DATETIME10("HH:mm","datetime10"),
	
	DATETIME11("HH:mm:ss","datetime11"),
	
	DATETIME12("hh:mm a","datetime12"),
	
	DATETIME13("hh:mm:ss a","datetime13");

	/**
	 *
	 */
	private final transient String name;
	private final transient String pptxValue;

	private PptxDateTimeEnum(String name, String pptxValue)
	{
		this.name = name;
		this.pptxValue = pptxValue;
	}
	
	@Override
	public String getName()
	{
		return name;
	}
	
	@Override
	public String getValue()
	{
		return pptxValue;
	}
	
	/**
	 *
	 */
	public static PptxDateTimeEnum getByName(String name)
	{
		return EnumUtil.getEnumByName(values(), name);
	}
}
