/*
 * ============================================================================
 * GNU Lesser General Public License
 * ============================================================================
 *
 * JasperReports - Free Java report-generating library.
 * Copyright (C) 2001-2006 JasperSoft Corporation http://www.jaspersoft.com
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
 * 303 Second Street, Suite 450 North
 * San Francisco, CA 94107
 * http://www.jaspersoft.com
 */
package net.sf.jasperreports.barcode;

import java.util.Map;

import net.sourceforge.barbecue.Barcode;
import net.sourceforge.barbecue.BarcodeException;
import net.sourceforge.barbecue.BarcodeFactory;

/**
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public class BarcodeProviders
{

	private Map providers;

	public Map getProviders()
	{
		return providers;
	}

	public void setProviders(Map providers)
	{
		this.providers = providers;
	}
	
	public boolean isTypeSupported(String type)
	{
		return providers.containsKey(type);
	}
	
	public Barcode createBarcode(String type, String code)
	{
		BarcodeProvider provider = (BarcodeProvider) providers.get(type);
		if (provider == null)
		{
			throw new RuntimeException("No barcode provider for type " + type);
		}
		try
		{
			return provider.createBarcode(code);
		}
		catch (BarcodeException e)
		{
			throw new RuntimeException("Error creating barcode", e);
		}
	}
	
	public static class UPCAProvider implements BarcodeProvider
	{
		public Barcode createBarcode(String code) throws BarcodeException
		{
			return BarcodeFactory.createUPCA(code);
		}
	}
	
	public static class Code128Provider implements BarcodeProvider
	{
		public Barcode createBarcode(String code) throws BarcodeException
		{
			return BarcodeFactory.createCode128(code);
		}
	}
	
	public static class CodabarProvider implements BarcodeProvider
	{
		public Barcode createBarcode(String code) throws BarcodeException
		{
			return BarcodeFactory.createCodabar(code);
		}
	}
	
	public static class Std2of5Provider implements BarcodeProvider
	{
		public Barcode createBarcode(String code) throws BarcodeException
		{
			return BarcodeFactory.createStd2of5(code);
		}
	}
	
	public static class Int2of5Provider implements BarcodeProvider
	{
		public Barcode createBarcode(String code) throws BarcodeException
		{
			return BarcodeFactory.createInt2of5(code);
		}
	}
	
	public static class EAN13Provider implements BarcodeProvider
	{
		public Barcode createBarcode(String code) throws BarcodeException
		{
			return BarcodeFactory.createEAN13(code);
		}
	}
}
