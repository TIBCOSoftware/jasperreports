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
package net.sf.jasperreports.components.barcode4j;

import org.krysalis.barcode4j.ChecksumMode;

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
		evaluateBaseBarcode(ean128, "0101234567890128");
	}

	protected void evaluateCode39(Code39Component code39)
	{
		evaluateBaseBarcode(code39, "01234567892");
	}

	protected void evaluateUPCA(UPCAComponent upcA)
	{
		String defaultMessage;
		String checksumMode = upcA.getChecksumMode();
		if (checksumMode != null 
				&& checksumMode.equals(ChecksumMode.CP_ADD.getName()))
		{
			defaultMessage = "01234567890";
		}
		else
		{
			defaultMessage = "012345678905";
		}
			
		evaluateBaseBarcode(upcA, defaultMessage);
	}

	protected void evaluateUPCE(UPCEComponent upcE)
	{
		String defaultMessage;
		String checksumMode = upcE.getChecksumMode();
		if (checksumMode != null 
				&& checksumMode.equals(ChecksumMode.CP_ADD.getName()))
		{
			defaultMessage = "0123413";
		}
		else
		{
			defaultMessage = "01234133";
		}
			
		evaluateBaseBarcode(upcE, defaultMessage);
	}

	protected void evaluateEAN13(EAN13Component ean13)
	{
		String defaultMessage;
		String checksumMode = ean13.getChecksumMode();
		if (checksumMode != null 
				&& checksumMode.equals(ChecksumMode.CP_ADD.getName()))
		{
			defaultMessage = "012345678901";
		}
		else
		{
			defaultMessage = "0123456789012";
		}
			
		evaluateBaseBarcode(ean13, defaultMessage);
	}

	protected void evaluateEAN8(EAN8Component ean8)
	{
		String defaultMessage;
		String checksumMode = ean8.getChecksumMode();
		if (checksumMode != null 
				&& checksumMode.equals(ChecksumMode.CP_ADD.getName()))
		{
			defaultMessage = "0123456";
		}
		else
		{
			defaultMessage = "01234565";
		}
			
		evaluateBaseBarcode(ean8, defaultMessage);
	}

	protected void evaluateInterleaved2Of5(Interleaved2Of5Component interleaved2Of5)
	{
		evaluateBaseBarcode(interleaved2Of5, "0123456784");
	}

	protected void evaluateRoyalMailCustomer(
			RoyalMailCustomerComponent royalMailCustomer)
	{
		String defaultMessage;
		String checksumMode = royalMailCustomer.getChecksumMode();
		if (checksumMode != null 
				&& checksumMode.equals(ChecksumMode.CP_CHECK.getName()))
		{
			defaultMessage = "01234Q";
		}
		else
		{
			defaultMessage = "01234";
		}
			
		evaluateBaseBarcode(royalMailCustomer, defaultMessage);
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

	protected void evaluatePDF417(PDF417Component pdf417)
	{
		evaluateBaseBarcode(pdf417, "01234");
	}

}
