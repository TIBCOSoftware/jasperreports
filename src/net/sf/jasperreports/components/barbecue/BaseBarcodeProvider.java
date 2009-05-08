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
package net.sf.jasperreports.components.barbecue;

import net.sourceforge.barbecue.Barcode;
import net.sourceforge.barbecue.BarcodeException;

/**
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public abstract class BaseBarcodeProvider implements BarcodeProvider
{

	public Barcode createBarcode(BarcodeInfo barcodeInfo)
			throws BarcodeException
	{
		Barcode barcode = createBaseBarcode(barcodeInfo);
		barcode.setDrawingText(barcodeInfo.isDrawText());
		if (barcodeInfo.getBarWidth() != null)
		{
			barcode.setBarWidth(barcodeInfo.getBarWidth().intValue());
		}
		if (barcodeInfo.getBarHeight() != null)
		{
			barcode.setBarHeight(barcodeInfo.getBarHeight().intValue());
		}
		return barcode;
	}
	
	protected abstract Barcode createBaseBarcode(BarcodeInfo barcodeInfo)
			throws BarcodeException;

}
