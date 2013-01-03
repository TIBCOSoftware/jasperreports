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
package net.sf.jasperreports.engine.fill;

import java.io.Serializable;

import net.sf.jasperreports.engine.JRConstants;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class SimpleTextFormat implements TextFormat, Serializable
{
	/**
	 *
	 */
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

	/**
	 *
	 */
	private String valueClassName;
	private String pattern;
	private String formatFactoryClass;
	private String localeCode;
	private String timeZoneId;
	
	
	/**
	 *
	 */
	public String getValueClassName()
	{
		return valueClassName;
	}

	/**
	 *
	 */
	public void setValueClassName(String valueClassName)
	{
		this.valueClassName = valueClassName;
	}

	/**
	 *
	 */
	public String getPattern()
	{
		return pattern;
	}

	/**
	 *
	 */
	public void setPattern(String pattern)
	{
		this.pattern = pattern;
	}

	/**
	 *
	 */
	public String getFormatFactoryClass()
	{
		return formatFactoryClass;
	}

	/**
	 *
	 */
	public void setFormatFactoryClass(String formatFactoryClass)
	{
		this.formatFactoryClass = formatFactoryClass;
	}

	/**
	 *
	 */
	public String getLocaleCode()
	{
		return localeCode;
	}

	/**
	 *
	 */
	public void setLocaleCode(String localeCode)
	{
		this.localeCode = localeCode;
	}

	/**
	 *
	 */
	public String getTimeZoneId()
	{
		return timeZoneId;
	}

	/**
	 *
	 */
	public void setTimeZoneId(String timeZoneId)
	{
		this.timeZoneId = timeZoneId;
	}
}
