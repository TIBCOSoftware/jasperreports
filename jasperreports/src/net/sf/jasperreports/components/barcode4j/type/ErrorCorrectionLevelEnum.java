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

import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;


/**
 * @author sanda zaharia (shertage@users.sourceforge.net)
 */
public enum ErrorCorrectionLevelEnum {
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
	private final transient ErrorCorrectionLevel errorCorrectionLevel;
	private final transient String name;

	private ErrorCorrectionLevelEnum(ErrorCorrectionLevel errorCorrectionLevel, String name) {
		this.errorCorrectionLevel = errorCorrectionLevel;
		this.name = name;
	}

	/**
	 *
	 */
	public final ErrorCorrectionLevel getErrorCorrectionLevel() {
		return errorCorrectionLevel;
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
	public static ErrorCorrectionLevelEnum getByName(String name) {
		ErrorCorrectionLevelEnum[] values = values();
		if (values != null && name != null) {
			for(ErrorCorrectionLevelEnum e:values) {
				if (name.equals(e.getName())) {
					return e;
				}
			}
		}
		return null;
	}
	
	/**
	 *
	 */
	public static ErrorCorrectionLevelEnum getByErrorCorrectionLevel(ErrorCorrectionLevel errorCorrectionLevel) {
		ErrorCorrectionLevelEnum[] values = values();
		if (values != null && errorCorrectionLevel != null) {
			for(ErrorCorrectionLevelEnum e:values) {
				if (errorCorrectionLevel.equals(e.getErrorCorrectionLevel())) {
					return e;
				}
			}
		}
		return null;
	}
}
