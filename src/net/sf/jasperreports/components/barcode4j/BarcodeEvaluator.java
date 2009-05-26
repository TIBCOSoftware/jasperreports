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

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.JRStyle;
import net.sf.jasperreports.engine.component.FillContext;

import org.krysalis.barcode4j.ChecksumMode;
import org.krysalis.barcode4j.HumanReadablePlacement;
import org.krysalis.barcode4j.impl.AbstractBarcodeBean;
import org.krysalis.barcode4j.impl.codabar.CodabarBean;
import org.krysalis.barcode4j.impl.code128.Code128Bean;
import org.krysalis.barcode4j.impl.code128.EAN128Bean;
import org.krysalis.barcode4j.impl.datamatrix.DataMatrixBean;
import org.krysalis.barcode4j.impl.datamatrix.SymbolShapeHint;
import org.krysalis.barcode4j.tools.UnitConv;

/**
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public class BarcodeEvaluator implements BarcodeVisitor
{

	private final FillContext fillContext;
	private final byte evaluationType;

	private String message;
	private AbstractBarcodeBean barcode;
	
	public BarcodeEvaluator(FillContext fillContext, byte evaluationType)
	{
		this.fillContext = fillContext;
		this.evaluationType = evaluationType;
	}

	public void evaluateBarcode()
	{
		BarcodeComponent barcodeComponent = (BarcodeComponent) fillContext
				.getComponentElement().getComponent();
		barcodeComponent.receive(this);
	}
	
	public String getMessage()
	{
		return message;
	}
	
	public AbstractBarcodeBean getBarcode()
	{
		return barcode;
	}
	
	protected void evaluateBaseBarcode(BarcodeComponent barcodeComponent)
	{
		message = (String) evaluateExpression(
				barcodeComponent.getCodeExpression());
		
		String pattern = (String) evaluateExpression(
				barcodeComponent.getPatternExpression());
		if (pattern != null) 
		{
			barcode.setPattern(pattern);
		}

		// FIXME DataMatrix?
		double barcodeHeight;
		if (BarcodeUtils.isVertical(barcodeComponent))
		{
			barcodeHeight = UnitConv.pt2mm(
					fillContext.getComponentElement().getWidth());
		}
		else
		{
			barcodeHeight = UnitConv.pt2mm(
					fillContext.getComponentElement().getHeight());
		}
		barcode.setHeight(barcodeHeight);
		
		JRStyle style = getElementStyle();
		if (style != null)
		{
			String fontName = style.getFontName();
			if (fontName != null)
			{
				barcode.setFontName(fontName);
			}
			
			Integer fontSize = style.getFontSize();
			if (fontSize != null)
			{
				double barFontSize = UnitConv.pt2mm(fontSize.intValue());
				barcode.setFontSize(barFontSize);
			}
		}
		
		Double moduleWidth = barcodeComponent.getModuleWidth();
		if (moduleWidth != null)
		{
			barcode.setModuleWidth(UnitConv.pt2mm(moduleWidth.doubleValue()));
		}
		
		String textPlacement = barcodeComponent.getTextPosition();
		if (textPlacement != null)
		{
			barcode.setMsgPosition(
					HumanReadablePlacement.byName(textPlacement));
		}
	}

	protected JRStyle getElementStyle()
	{
		JRStyle style = fillContext.getElementStyle();
		if (style == null)
		{
			style = fillContext.getDefaultStyleProvider().getDefaultStyle();
		}
		return style;
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
	
	public void visitCodabar(CodabarComponent codabar)
	{
		CodabarBean codabarBean = new CodabarBean();
		barcode = codabarBean;
		evaluateBaseBarcode(codabar);
		if (codabar.getChecksumMode() != null)
		{
			codabarBean.setChecksumMode(ChecksumMode.byName(codabar.getChecksumMode()));
		}
		if (codabar.getWideFactor() != null)
		{
			codabarBean.setWideFactor(codabar.getWideFactor().doubleValue());
		}
	}

	public void visitCode128(Code128Component code128)
	{
		barcode = new Code128Bean();
		evaluateBaseBarcode(code128);
	}

	public void visitDataMatrix(DataMatrixComponent dataMatrix)
	{
		DataMatrixBean dataMatrixBean = new DataMatrixBean();
		barcode = dataMatrixBean;
		evaluateBaseBarcode(dataMatrix);
		if (dataMatrix.getShape() != null)
		{
			dataMatrixBean.setShape(SymbolShapeHint.byName(dataMatrix.getShape()));
		}
	}

	public void visitEANCode128(EAN128Component ean128)
	{
		EAN128Bean ean128Bean = new EAN128Bean();
		barcode = ean128Bean;
		evaluateBaseBarcode(ean128);
		if (ean128.getChecksumMode() != null)
		{
			ean128Bean.setChecksumMode(ChecksumMode.byName(ean128.getChecksumMode()));
		}
	}

}
