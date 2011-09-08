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

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;

import net.sf.jasperreports.engine.JRComponentElement;
import net.sf.jasperreports.engine.JRImageRenderer;
import net.sf.jasperreports.engine.JRRenderable;
import net.sf.jasperreports.engine.JRRuntimeException;
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
	
	public JRRenderable createImage(JRComponentElement componentElement, 
			BarcodeGenerator barcode, String message, int orientation)
	{
		try
		{
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			
			int resolution = JRProperties.getIntegerProperty(
					componentElement, PROPERTY_RESOLUTION, 300);
			boolean gray = JRProperties.getBooleanProperty(
					componentElement, PROPERTY_GRAY, true);
			boolean antiAlias = JRProperties.getBooleanProperty(
					componentElement, PROPERTY_ANTIALIAS, true);
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
