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
import net.sf.jasperreports.engine.JRStyle;

import org.krysalis.barcode4j.ChecksumMode;
import org.krysalis.barcode4j.HumanReadablePlacement;
import org.krysalis.barcode4j.impl.AbstractBarcodeBean;
import org.krysalis.barcode4j.impl.codabar.CodabarBean;
import org.krysalis.barcode4j.impl.code128.Code128Bean;
import org.krysalis.barcode4j.impl.code128.EAN128Bean;
import org.krysalis.barcode4j.impl.code39.Code39Bean;
import org.krysalis.barcode4j.impl.datamatrix.DataMatrixBean;
import org.krysalis.barcode4j.impl.datamatrix.SymbolShapeHint;
import org.krysalis.barcode4j.impl.fourstate.AbstractFourStateBean;
import org.krysalis.barcode4j.impl.fourstate.RoyalMailCBCBean;
import org.krysalis.barcode4j.impl.fourstate.USPSIntelligentMailBean;
import org.krysalis.barcode4j.tools.UnitConv;

/**
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public abstract class AbstractBarcodeEvaluator implements BarcodeVisitor
{
	
	protected final JRComponentElement componentElement;
	protected final BarcodeComponent barcodeComponent;
	protected final JRDefaultStyleProvider defaultStyleProvider;

	protected String message;
	protected AbstractBarcodeBean barcode;
	
	protected AbstractBarcodeEvaluator(JRComponentElement componentElement, 
			JRDefaultStyleProvider defaultStyleProvider)
	{
		this.componentElement = componentElement;
		this.barcodeComponent = (BarcodeComponent) componentElement.getComponent();
		this.defaultStyleProvider = defaultStyleProvider;
	}

	public void evaluateBarcode()
	{
		BarcodeComponent barcodeComponent = 
			(BarcodeComponent) componentElement.getComponent();
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

	protected void setBaseAttributes(BarcodeComponent barcodeComponent)
	{
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
		
		Double quietZone = barcodeComponent.getQuietZone();
		if (quietZone != null)
		{
			barcode.doQuietZone(true);
			barcode.setQuietZone(UnitConv.pt2mm(quietZone.doubleValue()));
		}
		
		Double vQuietZone = barcodeComponent.getVerticalQuietZone();
		if (vQuietZone != null)
		{
			barcode.setVerticalQuietZone(UnitConv.pt2mm(vQuietZone.doubleValue()));
		}

		// FIXME DataMatrix?
		double barcodeHeight;
		if (BarcodeUtils.isVertical(barcodeComponent))
		{
			barcodeHeight = UnitConv.pt2mm(componentElement.getWidth());
		}
		else
		{
			barcodeHeight = UnitConv.pt2mm(componentElement.getHeight());
		}
		barcode.setHeight(barcodeHeight);
		
	}

	protected JRStyle getElementStyle()
	{
		JRStyle style = componentElement.getStyle();
		if (style == null)
		{
			style = defaultStyleProvider.getDefaultStyle();
		}
		return style;
	}
	
	public void visitCodabar(CodabarComponent codabar)
	{
		CodabarBean codabarBean = new CodabarBean();
		barcode = codabarBean;
		evaluateCodabar(codabar);
		setBaseAttributes(codabar);
		if (codabar.getChecksumMode() != null)
		{
			codabarBean.setChecksumMode(ChecksumMode.byName(codabar.getChecksumMode()));
		}
		if (codabar.getWideFactor() != null)
		{
			codabarBean.setWideFactor(codabar.getWideFactor().doubleValue());
		}
	}

	protected abstract void evaluateCodabar(CodabarComponent codabar);

	public void visitCode128(Code128Component code128)
	{
		barcode = new Code128Bean();
		evaluateCode128(code128);
		setBaseAttributes(code128);
	}

	protected abstract void evaluateCode128(Code128Component code128);

	public void visitDataMatrix(DataMatrixComponent dataMatrix)
	{
		DataMatrixBean dataMatrixBean = new DataMatrixBean();
		barcode = dataMatrixBean;
		evaluateDataMatrix(dataMatrix);
		setBaseAttributes(dataMatrix);
		if (dataMatrix.getShape() != null)
		{
			dataMatrixBean.setShape(SymbolShapeHint.byName(dataMatrix.getShape()));
		}
	}

	protected abstract void evaluateDataMatrix(DataMatrixComponent dataMatrix);

	public void visitEANCode128(EAN128Component ean128)
	{
		EAN128Bean ean128Bean = new EAN128Bean();
		barcode = ean128Bean;
		evaluateEANCode128(ean128);
		setBaseAttributes(ean128);
		if (ean128.getChecksumMode() != null)
		{
			ean128Bean.setChecksumMode(ChecksumMode.byName(ean128.getChecksumMode()));
		}
	}

	protected abstract void evaluateEANCode128(EAN128Component ean128);

	public void visitCode39(Code39Component code39)
	{
		Code39Bean code39Bean = new Code39Bean();
		barcode = code39Bean;
		evaluateCode39(code39);
		setBaseAttributes(code39);
		if (code39.getChecksumMode() != null)
		{
			code39Bean.setChecksumMode(ChecksumMode.byName(code39.getChecksumMode()));
		}
		if (code39.isDisplayChecksum() != null)
		{
			code39Bean.setDisplayChecksum(code39.isDisplayChecksum().booleanValue());
		}
		if (code39.isDisplayStartStop() != null)
		{
			code39Bean.setDisplayStartStop(code39.isDisplayStartStop().booleanValue());
		}
		if (code39.isExtendedCharSetEnabled() != null)
		{
			code39Bean.setExtendedCharSetEnabled(code39.isExtendedCharSetEnabled().booleanValue());
		}
		if (code39.getIntercharGapWidth() != null)
		{
			code39Bean.setIntercharGapWidth(code39.getIntercharGapWidth().doubleValue());
		}
		if (code39.getWideFactor() != null)
		{
			code39Bean.setWideFactor(code39.getWideFactor().doubleValue());
		}
	}

	protected abstract void evaluateCode39(Code39Component code39);

	public void visitRoyalMailCustomer(
			RoyalMailCustomerComponent royalMailCustomer)
	{
		RoyalMailCBCBean mailBean = new RoyalMailCBCBean();
		barcode = mailBean;
		evaluateRoyalMailCustomer(royalMailCustomer);
		setBaseAttributes(royalMailCustomer);
		setFourStateAttributes(royalMailCustomer, mailBean);
	}

	protected void setFourStateAttributes(
			FourStateBarcodeComponent barcodeComponent, 
			AbstractFourStateBean barcodeBean)
	{
		if (barcodeComponent.getChecksumMode() != null)
		{
			barcodeBean.setChecksumMode(
					ChecksumMode.byName(barcodeComponent.getChecksumMode()));
		}
		
		if (barcodeComponent.getAscenderHeight() != null)
		{
			barcodeBean.setAscenderHeight(
					UnitConv.pt2mm(barcodeComponent.getAscenderHeight().doubleValue()));
		}
		
		if (barcodeComponent.getIntercharGapWidth() != null)
		{
			barcodeBean.setIntercharGapWidth(
					UnitConv.pt2mm(barcodeComponent.getIntercharGapWidth().doubleValue()));
		}
		
		if (barcodeComponent.getTrackHeight() != null)
		{
			barcodeBean.setTrackHeight(
					UnitConv.pt2mm(barcodeComponent.getTrackHeight().doubleValue()));
		}
	}

	protected abstract void evaluateRoyalMailCustomer(
			RoyalMailCustomerComponent royalMailCustomer);

	public void visitUSPSIntelligentMail(
			USPSIntelligentMailComponent intelligentMail)
	{
		USPSIntelligentMailBean mailBean = new USPSIntelligentMailBean();
		barcode = mailBean;
		evaluateUSPSIntelligentMail(intelligentMail);
		setBaseAttributes(intelligentMail);
		setFourStateAttributes(intelligentMail, mailBean);
	}

	protected abstract void evaluateUSPSIntelligentMail(
			USPSIntelligentMailComponent intelligentMail);

}
