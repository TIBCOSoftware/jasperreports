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

import java.util.HashMap;
import java.util.Map;

import net.sf.jasperreports.engine.JRComponentElement;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRImage;
import net.sf.jasperreports.engine.JRPrintElement;
import net.sf.jasperreports.engine.JRRenderable;
import net.sf.jasperreports.engine.JRStyle;
import net.sf.jasperreports.engine.component.BaseFillComponent;
import net.sf.jasperreports.engine.component.FillPrepareResult;
import net.sf.jasperreports.engine.fill.JRTemplateImage;
import net.sf.jasperreports.engine.fill.JRTemplatePrintImage;

import org.krysalis.barcode4j.impl.AbstractBarcodeBean;

/**
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public class BarcodeFillComponent extends BaseFillComponent
{

	private final BarcodeComponent barcodeComponent;
	
	private final Map printTemplates = new HashMap();
	private AbstractBarcodeBean barcode;
	private String message;
	
	
	public BarcodeFillComponent(BarcodeComponent barcodeComponent)
	{
		this.barcodeComponent = barcodeComponent;
	}

	public BarcodeFillComponent(BarcodeFillComponent barcode)
	{
		this.barcodeComponent = barcode.barcodeComponent;
	}
	
	public void evaluate(byte evaluation) throws JRException
	{
		if (isEvaluateNow())
		{
			evaluateBarcode(evaluation);
		}
	}
	
	protected boolean isEvaluateNow()
	{
		return barcodeComponent.getEvaluationTime() 
				== JRExpression.EVALUATION_TIME_NOW;
	}

	protected void evaluateBarcode(byte evaluation)
	{
		BarcodeEvaluator evaluator = new BarcodeEvaluator(fillContext, evaluation);
		evaluator.evaluateBarcode();
		barcode = evaluator.getBarcode();
		message = evaluator.getMessage();
	}
	
	public FillPrepareResult prepare(int availableHeight)
	{
		if (isEvaluateNow() && message == null)
		{
			return FillPrepareResult.NO_PRINT_NO_OVERFLOW;
		}
		
		return FillPrepareResult.PRINT_NO_STRETCH;
	}

	public JRPrintElement fill()
	{
		JRTemplateImage templateImage = getTemplateImage();
		
		JRTemplatePrintImage image = new JRTemplatePrintImage(templateImage);
		JRComponentElement element = fillContext.getComponentElement();
		image.setX(element.getX());
		image.setY(fillContext.getElementPrintY());
		image.setWidth(element.getWidth());
		image.setHeight(element.getHeight());
		image.setScaleImage(JRImage.SCALE_IMAGE_CLIP);
		
		if (isEvaluateNow())
		{
			setBarcodeImage(image);
		}
		else
		{
			fillContext.registerDelayedEvaluation(image, 
					barcodeComponent.getEvaluationTime(), 
					barcodeComponent.getEvaluationGroup());
		}
		
		return image;
	}
	
	protected JRTemplateImage getTemplateImage()
	{
		JRStyle elementStyle = fillContext.getElementStyle();
		JRTemplateImage templateImage = (JRTemplateImage) printTemplates.get(elementStyle);
		if (templateImage == null)
		{
			templateImage = new JRTemplateImage(
					fillContext.getElementOrigin(), 
					fillContext.getDefaultStyleProvider());
			templateImage.setStyle(elementStyle);
			
			printTemplates.put(elementStyle, templateImage);
		}
		return templateImage;
	}
	
	protected void setBarcodeImage(JRTemplatePrintImage image)
	{
		if (message != null)
		{
			BarcodeImageProducer imageProducer = BarcodeUtils.getImageProducer(
					fillContext.getComponentElement());
			JRRenderable barcodeImage = imageProducer.createImage(
					fillContext, barcode, message, barcodeComponent.getOrientation());
			image.setRenderer(barcodeImage);
		}
	}

	public void evaluateDelayedElement(JRPrintElement element, byte evaluation)
			throws JRException
	{
		evaluateBarcode(evaluation);
		setBarcodeImage((JRTemplatePrintImage) element);
	}

}
