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
import net.sf.jasperreports.engine.JRImage;
import net.sf.jasperreports.engine.JRPrintElement;
import net.sf.jasperreports.engine.JRRenderable;
import net.sf.jasperreports.engine.base.JRBasePrintImage;
import net.sf.jasperreports.engine.component.ComponentDesignConverter;
import net.sf.jasperreports.engine.convert.ReportConverter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.krysalis.barcode4j.impl.AbstractBarcodeBean;

/**
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public class BarcodeDesignConverter implements ComponentDesignConverter
{

	private static final Log log = LogFactory.getLog(BarcodeDesignConverter.class);
	
	public JRPrintElement convert(ReportConverter reportConverter,
			JRComponentElement element)
	{
		JRBasePrintImage printImage = new JRBasePrintImage(
				reportConverter.getDefaultStyleProvider());
		reportConverter.copyBaseAttributes(element, printImage);
		printImage.setScaleImage(JRImage.SCALE_IMAGE_CLIP);
		
		JRRenderable barcodeImage = evaluateBarcode(reportConverter, element);
		printImage.setRenderer(barcodeImage);
		
		return printImage;
	}

	protected JRRenderable evaluateBarcode(ReportConverter reportConverter,
			JRComponentElement element)
	{
		try
		{
			BarcodeComponent barcodeComponent = (BarcodeComponent) element.getComponent();

			BarcodeDesignEvaluator evaluator = new BarcodeDesignEvaluator(element, 
					reportConverter.getDefaultStyleProvider());
			evaluator.evaluateBarcode();
			
			AbstractBarcodeBean barcode = evaluator.getBarcode();
			String message = evaluator.getMessage();
			
			BarcodeImageProducer imageProducer = BarcodeUtils.getImageProducer(element);
			JRRenderable barcodeImage = imageProducer.createImage(
					element, barcode, message, barcodeComponent.getOrientation());
			return barcodeImage;
		}
		catch (Exception e)
		{
			if (log.isWarnEnabled())
			{
				log.warn("Failed to create barcode preview", e);
			}
			
			return null;
		}
	}

}
