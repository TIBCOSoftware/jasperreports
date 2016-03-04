/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2014 TIBCO Software Inc. All rights reserved.
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
package net.sf.jasperreports.renderers;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.type.ImageTypeEnum;
import net.sf.jasperreports.engine.util.JRTypeSniffer;


/**
 * Provides functionality for image rendering.
 * <p/>
 * The content of an image element can come either directly from an image file like a JPG,
 * GIF, PNG, or can be a Scalable Vector Graphics (SVG) file that is rendered using some
 * business logic or a special graphics API like a charting or a barcode library. Either way,
 * JasperReports treats images in a very transparent way because it relies on a special
 * interface called {@link net.sf.jasperreports.renderers.Renderable} to offer a common
 * way to render images.
 * <p/>
 * The {@link net.sf.jasperreports.renderers.Graphics2DRenderable} interface has a method called 
 * {@link Graphics2DRenderable#render(JasperReportsContext, Graphics2D, Rectangle2D)},
 * which gets called by the engine each time it needs to draw the image
 * on a given device or graphic context. This approach provides the best quality for the
 * SVG images when they must be drawn on unknown devices or zoomed into without
 * losing sharpness.
 * <p/>
 * Other methods specified in this interface can be used to obtain the native size of the
 * actual image that the renderer wraps or the binary data for an image that must be stored
 * in a separate file during export.
 * <p/>
 * The library comes with a default implementation for the 
 * {@link net.sf.jasperreports.renderers.Renderable} interface that
 * wraps images that come from files or binary image data in JPG, GIF, or PNG format.
 * This is the {@link net.sf.jasperreports.renderers.ImageRenderer} class, which is actually a container
 * for the binary image data, used to load a <code>java.awt.Image</code> object from it.
 * Then it draws the loaded image on the supplied <code>java.awt.Graphics2D</code> context when the engine
 * requires it.
 * <p/>
 * Image renderers are serializable because inside the generated document for each image is
 * a renderer object kept as reference, which is serialized along with the whole
 * JasperPrint object.
 * <p/>
 * When a {@link net.sf.jasperreports.renderers.ImageRenderer} instance is serialized, 
 * so is the binary image data it contains.
 * <p/>
 * However, if the image element must be lazy loaded (see the <code>isLazy</code> image attribute), then the
 * engine will not load the binary image data at report-filling time. Rather, it stores inside
 * the renderer only the <code>java.lang.String</code> location of the image. The actual image data
 * is loaded only when needed for rendering at report-export or view time.
 * <p/>
 * To simplify the implementation of SVG image renderers, JasperReports ships with an
 * abstract rendered {@link net.sf.jasperreports.renderers.AbstractRenderToImageRenderer}. This
 * implementation contains the code to produce binary image data from the SVG graphic in
 * PNG format. This is needed when the image must be stored in separate files on disk or
 * delivered in binary format to a consumer (like a web browser).
 * 
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public class ImageRenderer extends AbstractRenderer implements ImageRenderable
{
	/**
	 *
	 */
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;
	
	/**
	 *
	 */
	private byte[] imageData;
	private ImageTypeEnum imageType = ImageTypeEnum.UNKNOWN;

	/**
	 *
	 */
	protected ImageRenderer(byte[] imageData)
	{
		this.imageData = imageData;
		
		if (imageData != null) 
		{
			imageType = JRTypeSniffer.getImageTypeValue(imageData);
		}
			
	}


	/**
	 *
	 */
	public static ImageRenderer getInstance(byte[] imageData)
	{
		if (imageData != null) 
		{
			return new ImageRenderer(imageData);
		}
		return null;
	}


	@Override
	public ImageTypeEnum getImageType()
	{
		return imageType;
	}
	

	@Override
	public byte[] getImageData(JasperReportsContext jasperReportsContext) throws JRException
	{
		return imageData;
	}
}
