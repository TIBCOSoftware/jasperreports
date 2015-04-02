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
package net.sf.jasperreports.components.barcode4j;

import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.type.EnumUtil;
import net.sf.jasperreports.engine.type.NamedEnum;

import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;


/**
 * @author sanda zaharia (shertage@users.sourceforge.net)
 */
public enum ErrorCorrectionLevelEnum implements NamedEnum
{
	/**
	 *
	 */
	L("L"),

	/**
	 *
	 */
	M("M"),

	/**
	 *
	 */
	Q("Q"),

	/**
	 *
	 */
	H("H");

	/**
	 *
	 */
	private final transient String name;
	public static final String EXCEPTION_MESSAGE_KEY_UNKNOWN_NAME = "components.barcode4j.error.correction.level.unknown.name";

	private ErrorCorrectionLevelEnum(String name) 
	{
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
	public final ErrorCorrectionLevel getErrorCorrectionLevel()
	{
		// not storing this as instance field as we don't want to force an iText dependency
		ErrorCorrectionLevel level;
		if (name.equals("L"))
		{
			level = ErrorCorrectionLevel.L;
		}
		else if (name.equalsIgnoreCase("M"))
		{
			level = ErrorCorrectionLevel.M;
		}
		else if (name.equalsIgnoreCase("Q"))
		{
			level = ErrorCorrectionLevel.Q;
		}
		else if (name.equalsIgnoreCase("H"))
		{
			level = ErrorCorrectionLevel.H;
		}
		else
		{
			// should not happen
			throw 
				new JRRuntimeException(
					EXCEPTION_MESSAGE_KEY_UNKNOWN_NAME,
					new Object[]{name});
		}
		return level;
	}

	/**
	 *
	 */
	public static ErrorCorrectionLevelEnum getByName(String name)
	{
		return EnumUtil.getEnumByName(values(), name);
	}
}
