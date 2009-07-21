/*
 * ============================================================================
 * GNU Lesser General Public License
 * ============================================================================
 *
 * JasperReports - Free Java report-generating library.
 * Copyright (C) 2001-2009 JasperSoft Corporation http://www.jaspersoft.com
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307, USA.
 * 
 * JasperSoft Corporation
 * 539 Bryant Street, Suite 100
 * San Francisco, CA 94107
 * http://www.jaspersoft.com
 */
package net.sf.jasperreports.components.barcode4j;

import net.sf.jasperreports.engine.JRConstants;

import org.krysalis.barcode4j.ChecksumMode;

/**
 * 
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id: Code128Component.java 2810 2009-05-26 17:30:50Z lucianc $
 */
public class UPCEComponent extends BarcodeComponent
{

	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

	public static final String PROPERTY_CHECKSUM_MODE = "checksumMode";

	private String checksumMode;

	public String getChecksumMode()
	{
		return checksumMode;
	}

	public void setChecksumMode(String checksumMode)
	{
		Object old = this.checksumMode;
		this.checksumMode = checksumMode;
		getEventSupport().firePropertyChange(PROPERTY_CHECKSUM_MODE, old, this.checksumMode);
	}

	public void setChecksumMode(ChecksumMode checksumMode)
	{
		setChecksumMode(checksumMode == null ? null : checksumMode.getName());
	}

	public void receive(BarcodeVisitor visitor)
	{
		visitor.visitUPCE(this);
	}

}
