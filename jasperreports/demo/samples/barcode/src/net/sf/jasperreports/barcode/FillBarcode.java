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

import net.sf.jasperreports.engine.JRComponentElement;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRImage;
import net.sf.jasperreports.engine.JRPrintElement;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.component.BaseFillComponent;
import net.sf.jasperreports.engine.component.FillPrepareResult;
import net.sf.jasperreports.engine.fill.JRTemplateImage;
import net.sf.jasperreports.engine.fill.JRTemplatePrintImage;
import net.sourceforge.barbecue.Barcode;
import net.sourceforge.barbecue.BarcodeException;
import net.sourceforge.barbecue.BarcodeFactory;

/**
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public class FillBarcode extends BaseFillComponent
{

	private final BarcodeComponent barcodeComponent;
	private String code;
	
	public FillBarcode(BarcodeComponent barcode)
	{
		this.barcodeComponent = barcode;
	}
	
	protected BarcodeComponent getBarcode()
	{
		return barcodeComponent;
	}
	
	public void evaluate(byte evaluation) throws JRException
	{
		code = (String) fillContext.evaluate(
				barcodeComponent.getCodeExpression(), evaluation);
	}

	public FillPrepareResult prepare(int availableHeight)
	{
		return code == null ? FillPrepareResult.NO_PRINT_NO_OVERFLOW
				: FillPrepareResult.PRINT_NO_STRETCH;
	}

	public JRPrintElement fill()
	{
		try
		{
			Barcode barcode = BarcodeFactory.createCode128B(code);
			BarbecueRenderer renderer = new BarbecueRenderer(barcode);
			
			JRComponentElement element = fillContext.getComponentElement();
			JRTemplateImage templateImage = new JRTemplateImage(fillContext.getElementOrigin(), 
					fillContext.getDefaultStyleProvider());
			templateImage.setStyle(fillContext.getElementStyle());
			
			JRTemplatePrintImage image = new JRTemplatePrintImage(templateImage);
			image.setX(element.getX());
			image.setY(fillContext.getElementPrintY());
			image.setWidth(element.getWidth());
			image.setHeight(element.getHeight());
			image.setScaleImage(JRImage.SCALE_IMAGE_RETAIN_SHAPE);
			image.setRenderer(renderer);
			
			return image;
		}
		catch (BarcodeException e)
		{
			throw new JRRuntimeException(e);
		}
	}

}
