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

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;

import net.sf.jasperreports.engine.JRComponentElement;
import net.sf.jasperreports.engine.JRImageRenderer;
import net.sf.jasperreports.engine.JRRenderable;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.component.FillContext;
import net.sf.jasperreports.engine.util.JRProperties;

import org.krysalis.barcode4j.BarcodeGenerator;
import org.krysalis.barcode4j.output.bitmap.BitmapCanvasProvider;

/**
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public class BarcodeRasterizedImageProducer implements BarcodeImageProducer
{
	
	public static final String PROPERTY_RESOLUTION = 
		BarcodeComponent.PROPERTY_PREFIX + "image.resolution";
	
	public static final String PROPERTY_GRAY = 
		BarcodeComponent.PROPERTY_PREFIX + "image.gray";
	
	public static final String PROPERTY_ANTIALIAS = 
		BarcodeComponent.PROPERTY_PREFIX + "image.antiAlias";
	
	public JRRenderable createImage(FillContext fillContext, 
			BarcodeGenerator barcode, String message, int orientation)
	{
		try
		{
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			
			JRComponentElement element = fillContext.getComponentElement();
			int resolution = JRProperties.getIntegerProperty(
					element, PROPERTY_RESOLUTION, 300);
			boolean gray = JRProperties.getBooleanProperty(
					element, PROPERTY_GRAY, true);
			boolean antiAlias = JRProperties.getBooleanProperty(
					element, PROPERTY_ANTIALIAS, true);
			int imageType = gray ? BufferedImage.TYPE_BYTE_GRAY 
					: BufferedImage.TYPE_BYTE_BINARY;
			
			BitmapCanvasProvider provider = new BitmapCanvasProvider(
			    out, "image/x-png", resolution, imageType, antiAlias, orientation);
			barcode.generateBarcode(provider, message);
			provider.finish();
			
			byte[] imageData = out.toByteArray();
			return JRImageRenderer.getInstance(imageData);
		}
		catch (Exception e)
		{
			throw new JRRuntimeException(e);
		}
	}

}
