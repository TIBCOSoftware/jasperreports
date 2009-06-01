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

import net.sf.jasperreports.engine.JRComponentElement;
import net.sf.jasperreports.engine.JRDefaultStyleProvider;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRRenderable;
import net.sf.jasperreports.engine.util.JRExpressionUtil;

/**
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public class BarcodeDesignEvaluator extends AbstractBarcodeEvaluator
{

	public BarcodeDesignEvaluator(JRComponentElement componentElement,
			JRDefaultStyleProvider defaultStyleProvider)
	{
		super(componentElement, defaultStyleProvider);
	}
	
	public JRRenderable evaluateImage()
	{
		evaluateBarcode();
		
		BarcodeImageProducer imageProducer = BarcodeUtils.getImageProducer(
				componentElement);
		JRRenderable barcodeImage = imageProducer.createImage(
				componentElement, 
				barcode, message, barcodeComponent.getOrientation());
		return barcodeImage;
	}
	
	protected void evaluateBaseBarcode(BarcodeComponent barcodeComponent, 
			String defaultMessage)
	{
		message = evaluateStringExpression(barcodeComponent.getCodeExpression(), 
				defaultMessage);
		
		String pattern = evaluateStringExpression(
				barcodeComponent.getPatternExpression(), null);
		if (pattern != null) 
		{
			barcode.setPattern(pattern);
		}
	}

	protected String evaluateStringExpression(JRExpression expression, String defaultValue)
	{
		String value = JRExpressionUtil.getSimpleExpressionText(expression);
		if (value == null)
		{
			value = defaultValue;
		}
		return value;
	}
	
	protected void evaluateCodabar(CodabarComponent codabar)
	{
		//FIXME default code depends on checksum mode?
		evaluateBaseBarcode(codabar, "0123456789");
	}

	protected void evaluateCode128(Code128Component code128)
	{
		evaluateBaseBarcode(code128, "0123456789");
	}

	protected void evaluateDataMatrix(DataMatrixComponent dataMatrix)
	{
		evaluateBaseBarcode(dataMatrix, "0123456789");
	}

	protected void evaluateEANCode128(EAN128Component ean128)
	{
		//FIXME default code depends on checksum mode?
		evaluateBaseBarcode(ean128, "0101234567890123");
	}

	protected void evaluateCode39(Code39Component code39)
	{
		evaluateBaseBarcode(code39, "0123456789");
	}

	protected void evaluateUPCA(UPCAComponent upcA)
	{
		evaluateBaseBarcode(upcA, "0123456789");
	}

	protected void evaluateUPCE(UPCEComponent upcE)
	{
		evaluateBaseBarcode(upcE, "0123456789");
	}

	protected void evaluateEAN13(EAN13Component ean13)
	{
		evaluateBaseBarcode(ean13, "0123456789");
	}

	protected void evaluateEAN8(EAN8Component ean8)
	{
		evaluateBaseBarcode(ean8, "0123456789");
	}

	protected void evaluateInterleaved2Of5(Interleaved2Of5Component interleaved2Of5)
	{
		evaluateBaseBarcode(interleaved2Of5, "0123456789");
	}

	protected void evaluateRoyalMailCustomer(
			RoyalMailCustomerComponent royalMailCustomer)
	{
		evaluateBaseBarcode(royalMailCustomer, "01234");
	}

	protected void evaluateUSPSIntelligentMail(
			USPSIntelligentMailComponent intelligentMail)
	{
		evaluateBaseBarcode(intelligentMail, 
				"00040123456200800001987654321");
	}

	protected void evaluatePOSTNET(POSTNETComponent intelligentMail)
	{
		evaluateBaseBarcode(intelligentMail, "01234");
	}

}
