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

/*
 * Contributors:
 * Eugene D - eugenedruy@users.sourceforge.net 
 * Adrian Jackson - iapetus@users.sourceforge.net
 * David Taylor - exodussystems@users.sourceforge.net
 * Lars Kristensen - llk@users.sourceforge.net
 */
package net.sf.jasperreports.engine.convert;

import java.awt.Image;

import net.sf.jasperreports.engine.JRElement;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRImage;
import net.sf.jasperreports.engine.JRImageRenderer;
import net.sf.jasperreports.engine.JRPrintElement;
import net.sf.jasperreports.engine.JRPrintImage;
import net.sf.jasperreports.engine.JRRenderable;
import net.sf.jasperreports.engine.base.JRBasePrintImage;
import net.sf.jasperreports.engine.util.JRExpressionUtil;
import net.sf.jasperreports.engine.util.JRImageLoader;
import net.sf.jasperreports.engine.util.JRLoader;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id: JRGraphics2DExporter.java 1811 2007-08-13 19:52:07Z teodord $
 */
public class ImageConverter extends ElementConverter
{

	/**
	 *
	 */
	private final static ImageConverter INSTANCE = new ImageConverter();
	
	/**
	 *
	 */
	private ImageConverter()
	{
	}

	/**
	 *
	 */
	public static ImageConverter getInstance()
	{
		return INSTANCE;
	}
	
	/**
	 *
	 */
	public JRPrintElement convert(ReportConverter reportConverter, JRElement element)
	{
		JRBasePrintImage printImage = new JRBasePrintImage(reportConverter.getDefaultStyleProvider());
		JRImage image = (JRImage)element;

		copyGraphicElement(reportConverter, image, printImage);

		printImage.copyBox(image.getLineBox());
		
		printImage.setAnchorName(JRExpressionUtil.getExpressionText(image.getAnchorNameExpression()));
		printImage.setBookmarkLevel(image.getBookmarkLevel());
		printImage.setHorizontalAlignment(image.getOwnHorizontalAlignment());
		printImage.setLazy(image.isLazy());
		printImage.setLinkType(image.getLinkType());
		printImage.setOnErrorType(image.getOnErrorType());
		printImage.setVerticalAlignment(image.getOwnVerticalAlignment());
		printImage.setRenderer(getRenderer(image, printImage));
		printImage.setScaleImage(image.getOwnScaleImage());
		
		return printImage;
	}

	/**
	 * 
	 */
	private JRRenderable getRenderer(JRImage imageElement, JRPrintImage printImage)
	{
		String location = JRExpressionUtil.getSimpleExpressionText(imageElement.getExpression());
		if(location != null)
		{
			try
			{
				byte[] imageData = JRLoader.loadBytesFromLocation(location); 
				Image awtImage = JRImageLoader.loadImage(imageData);
				if (awtImage == null)
				{
					printImage.setScaleImage(JRImage.SCALE_IMAGE_CLIP);
					return 
						JRImageRenderer.getInstance(
							JRImageLoader.NO_IMAGE_RESOURCE, 
							imageElement.getOnErrorType()
							);
				}
				return JRImageRenderer.getInstance(imageData);
			}
			catch (JRException e)
			{
				e.printStackTrace();
			}
		}
		
		try
		{
			printImage.setScaleImage(JRImage.SCALE_IMAGE_CLIP);
			return 
				JRImageRenderer.getInstance(
					JRImageLoader.NO_IMAGE_RESOURCE, 
					imageElement.getOnErrorType()
					);
		}
		catch (JRException e)
		{
			e.printStackTrace();//FIXMECONVERT use logging
		}
		
		return null;
	}

}
