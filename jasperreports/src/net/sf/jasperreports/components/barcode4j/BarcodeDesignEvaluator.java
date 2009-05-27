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
		evaluateBaseBarcode(ean128, "0101234567890123");
	}

}
