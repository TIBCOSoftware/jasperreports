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
package net.sf.jasperreports.components.barcode4j.qrcode;

import net.sf.jasperreports.components.barcode4j.BarcodeComponent;
import net.sf.jasperreports.components.barcode4j.BarcodeVisitor;
import net.sf.jasperreports.engine.JRConstants;

/**
 * Contains the main settings for the QRCode component
 * 
 * @author sanda zaharia (shertage@users.sourceforge.net)
 */
public class QRCodeComponent extends BarcodeComponent
{

	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

	public static final String PROPERTY_ERROR_CORRECTION_LEVEL = "errorCorrectionLevel";
	
	public static final Integer PROPERTY_DEFAULT_QUIET_ZONE = 4;
	public static final String PROPERTY_DEFAULT_ENCODING = "UTF-8";
	public static final String PROPERTY_DEFAULT_ERROR_CORRECTION_LEVEL = "L";
	
	private String errorCorrectionLevel;
	public void receive(BarcodeVisitor visitor)
	{
		visitor.visitQRCode(this);
	}
	public String getErrorCorrectionLevel()
	{
		return errorCorrectionLevel;
	}
	
	public void setErrorCorrectionLevel(String errorCorrectionLevel)
	{
		Object old = this.errorCorrectionLevel;
		this.errorCorrectionLevel = errorCorrectionLevel;
		getEventSupport().firePropertyChange(PROPERTY_ERROR_CORRECTION_LEVEL, 
				old, this.errorCorrectionLevel);
	}
}
