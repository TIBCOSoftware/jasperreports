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

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.component.FillContext;
import net.sf.jasperreports.engine.util.JRStringUtil;

/**
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public class BarcodeEvaluator extends AbstractBarcodeEvaluator
{

	private final FillContext fillContext;
	private final byte evaluationType;
	
	public BarcodeEvaluator(FillContext fillContext, byte evaluationType)
	{
		super(fillContext.getComponentElement(), 
				fillContext.getDefaultStyleProvider());
		
		this.fillContext = fillContext;
		this.evaluationType = evaluationType;
	}
	
	protected void evaluateBaseBarcode(BarcodeComponent barcodeComponent)
	{
		message = JRStringUtil.getString(evaluateExpression(barcodeComponent.getCodeExpression()));
		
		String pattern = JRStringUtil.getString(evaluateExpression(barcodeComponent.getPatternExpression()));
		if (pattern != null) 
		{
			barcode.setPattern(pattern);
		}
	}
	
	protected Object evaluateExpression(JRExpression expression)
	{
		try
		{
			return fillContext.evaluate(expression, evaluationType);
		}
		catch (JRException e)
		{
			throw new JRRuntimeException(e);
		}
	}

	protected void evaluateCodabar(CodabarComponent codabar)
	{
		evaluateBaseBarcode(codabar);
	}

	protected void evaluateCode128(Code128Component code128)
	{
		evaluateBaseBarcode(code128);
	}

	protected void evaluateDataMatrix(DataMatrixComponent dataMatrix)
	{
		evaluateBaseBarcode(dataMatrix);
	}

	protected void evaluateEANCode128(EAN128Component ean128)
	{
		evaluateBaseBarcode(ean128);
	}

	protected void evaluateCode39(Code39Component code39)
	{
		evaluateBaseBarcode(code39);
	}

	protected void evaluateUPCA(UPCAComponent upcA)
	{
		evaluateBaseBarcode(upcA);
	}

	protected void evaluateUPCE(UPCEComponent upcE)
	{
		evaluateBaseBarcode(upcE);
	}

	protected void evaluateEAN13(EAN13Component ean13)
	{
		evaluateBaseBarcode(ean13);
	}

	protected void evaluateEAN8(EAN8Component ean8)
	{
		evaluateBaseBarcode(ean8);
	}

	protected void evaluateInterleaved2Of5(Interleaved2Of5Component interleaved2Of5)
	{
		evaluateBaseBarcode(interleaved2Of5);
	}

	protected void evaluateRoyalMailCustomer(
			RoyalMailCustomerComponent royalMailCustomer)
	{
		evaluateBaseBarcode(royalMailCustomer);
	}

	protected void evaluateUSPSIntelligentMail(
			USPSIntelligentMailComponent intelligentMail)
	{
		evaluateBaseBarcode(intelligentMail);
	}

	protected void evaluatePOSTNET(POSTNETComponent intelligentMail)
	{
		evaluateBaseBarcode(intelligentMail);
	}

	protected void evaluatePDF417(PDF417Component pdf417)
	{
		evaluateBaseBarcode(pdf417);
	}

}
