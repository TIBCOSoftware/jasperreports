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

import org.krysalis.barcode4j.ChecksumMode;
import org.krysalis.barcode4j.HumanReadablePlacement;
import org.krysalis.barcode4j.impl.datamatrix.SymbolShapeHint;

import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.design.JRVerifier;

/**
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public class BarcodeVerifier implements BarcodeVisitor
{

	private final JRVerifier verifier;
	
	public BarcodeVerifier(JRVerifier verifier)
	{
		this.verifier = verifier;
	}

	protected void verifyBarcode(BarcodeComponent barcode)
	{
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
		
		JRExpression patternExpression = barcode.getPatternExpression();
		if (patternExpression != null)
		{
			String valueClass = patternExpression.getValueClassName();
			if (valueClass == null)
			{
				verifier.addBrokenRule("Barcode pattern expression value class not set", 
						patternExpression);
			}
			else if (!"java.lang.String".equals(valueClass))
			{
				verifier.addBrokenRule("Class " + valueClass 
						+ " not supported for barcode pattern expression. Use java.lang.String instead.",
						patternExpression);
			}
		}
		
		verifyTextPosition(barcode);
		verifyOrientation(barcode);
		
		byte evaluationTime = barcode.getEvaluationTime();
		if (evaluationTime == JRExpression.EVALUATION_TIME_AUTO)
		{
			verifier.addBrokenRule("Auto evaluation time is not supported for barcodes", barcode);
		}
		else if (evaluationTime == JRExpression.EVALUATION_TIME_GROUP)
		{
			String evaluationGroup = barcode.getEvaluationGroup();
			if (evaluationGroup == null || evaluationGroup.length() == 0)
			{
				verifier.addBrokenRule("No evaluation group set for barcode", barcode);
			}
			else if (!verifier.getReportDesign().getGroupsMap().containsKey(evaluationGroup))
			{
				verifier.addBrokenRule("Barcode evalution group \"" 
						+ evaluationGroup + " not found", barcode);
			}
		}
	}

	protected void verifyChecksumMode(String checksumMode, BarcodeComponent barcode)
	{
		try
		{
			if (checksumMode != null)
			{
				ChecksumMode.byName(checksumMode);
			}
		}
		catch (Exception e)
		{
			verifier.addBrokenRule(e, barcode);
		}
	}

	protected void verifyTextPosition(BarcodeComponent barcode)
	{
		try
		{
			String position = barcode.getTextPosition();
			if (position != null)
			{
				HumanReadablePlacement.byName(position);
			}
		}
		catch (Exception e)
		{
			verifier.addBrokenRule(e, barcode);
		}
	}

	protected void verifyOrientation(BarcodeComponent barcode)
	{
		int orientation = barcode.getOrientation();
		if (!(orientation == BarcodeComponent.ORIENTATION_UP 
				|| orientation == BarcodeComponent.ORIENTATION_LEFT 
				|| orientation == BarcodeComponent.ORIENTATION_DOWN 
				|| orientation == BarcodeComponent.ORIENTATION_RIGHT))
		{
			verifier.addBrokenRule("Invalid barcode orientation, supported values are 0, 90, 180, 270", 
					barcode);
		}
	}

	public void visitCodabar(CodabarComponent codabar)
	{
		verifyBarcode(codabar);
		verifyChecksumMode(codabar.getChecksumMode(), codabar);
	}

	public void visitCode128(Code128Component code128)
	{
		verifyBarcode(code128);
	}

	public void visitEANCode128(EAN128Component ean128)
	{
		verifyBarcode(ean128);
		verifyChecksumMode(ean128.getChecksumMode(), ean128);
	}

	public void visitDataMatrix(DataMatrixComponent dataMatrix)
	{
		verifyBarcode(dataMatrix);
		
		try
		{
			String shape = dataMatrix.getShape();
			if (shape != null)
			{
				SymbolShapeHint.byName(shape);
			}
		}
		catch (Exception e)
		{
			verifier.addBrokenRule(e, dataMatrix);
		}
	}

	public void visitRoyalMailCustomer(
			RoyalMailCustomerComponent royalMailCustomer)
	{
		verifyBarcode(royalMailCustomer);
		verifyChecksumMode(royalMailCustomer.getChecksumMode(), royalMailCustomer);
	}

	public void visitUSPSIntelligentMail(
			USPSIntelligentMailComponent intelligentMail)
	{
		verifyBarcode(intelligentMail);
		verifyChecksumMode(intelligentMail.getChecksumMode(), intelligentMail);
	}

}
