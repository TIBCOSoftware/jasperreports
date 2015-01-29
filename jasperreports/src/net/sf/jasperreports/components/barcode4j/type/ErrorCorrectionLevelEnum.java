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
package net.sf.jasperreports.components.barcode4j.type;

import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.type.EnumUtil;
import net.sf.jasperreports.engine.type.NamedValueEnum;

import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;


/**
 * @author sanda zaharia (shertage@users.sourceforge.net)
 */
public enum ErrorCorrectionLevelEnum  implements NamedValueEnum<ErrorCorrectionLevel> {
	/**
	 *
	 */
	L(ErrorCorrectionLevel.L, "L"),
	
	/**
	 *
	 */
	M(ErrorCorrectionLevel.M, "M"),
	
	/**
	 *
	 */
	Q(ErrorCorrectionLevel.Q, "Q"),
	
	/**
	 *
	 */
	H(ErrorCorrectionLevel.H, "H");
	
	/**
	 *
	 */
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;
	private final transient ErrorCorrectionLevel value;
	private final transient String name;

	private ErrorCorrectionLevelEnum(ErrorCorrectionLevel value, String name) {
		this.value = value;
		this.name = name;
	}

	/**
	 *
	 */
	public final ErrorCorrectionLevel getValue() {
		return value;
	}
	
	/**
	 *
	 */
	public String getName() {
		return name;
	}
	
	/**
	 *
	 */
	public static ErrorCorrectionLevelEnum getByName(String name)
	{
		return EnumUtil.getEnumByName(values(), name);
	}
	
	/**
	 *
	 */
	public static ErrorCorrectionLevelEnum getByValue(ErrorCorrectionLevel value)
	{
		return EnumUtil.getByValue(values(), value);
	}
}
