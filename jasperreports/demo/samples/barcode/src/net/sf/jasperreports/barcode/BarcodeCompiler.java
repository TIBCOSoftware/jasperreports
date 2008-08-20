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

import net.sf.jasperreports.engine.component.Component;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRExpressionCollector;
import net.sf.jasperreports.engine.base.JRBaseObjectFactory;
import net.sf.jasperreports.engine.component.ComponentCompiler;
import net.sf.jasperreports.engine.design.JRVerifier;

/**
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public class BarcodeCompiler implements ComponentCompiler
{

	public void collectExpressions(Component component, JRExpressionCollector collector)
	{
		BarcodeComponent barcode = (BarcodeComponent) component;
		collector.addExpression(barcode.getCodeExpression());
	}

	public Component toCompiledComponent(Component component,
			JRBaseObjectFactory baseFactory)
	{
		BarcodeComponent barcode = (BarcodeComponent) component;
		BarcodeComponent compiledBarcode = new BarcodeComponent();
		JRExpression compiledCodeExpression = baseFactory.getExpression(barcode.getCodeExpression());
		compiledBarcode.setCodeExpression(compiledCodeExpression);
		return compiledBarcode;
	}

	public void verify(Component component, JRVerifier verifier)
	{
		BarcodeComponent barcode = (BarcodeComponent) component;
		
		JRExpression codeExpression = barcode.getCodeExpression();
		if (codeExpression == null)
		{
			verifier.addBrokenRule("Barcode expression is null", barcode);
		}
		else
		{
			String valueClass = codeExpression.getValueClassName();
			if (valueClass == null)
			{
				verifier.addBrokenRule("Barcode expression value class not set", codeExpression);
			}
			else if (!"java.lang.String".equals(valueClass))
			{
				verifier.addBrokenRule("Class " + valueClass 
						+ " not supported for barcode expression. Use java.lang.String instead.",
						codeExpression);
			}
		}
	}

}
