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

/*
 * Contributors:
 * Eugene D - eugenedruy@users.sourceforge.net 
 * Adrian Jackson - iapetus@users.sourceforge.net
 * David Taylor - exodussystems@users.sourceforge.net
 * Lars Kristensen - llk@users.sourceforge.net
 */
package net.sf.jasperreports.engine.convert;

import net.sf.jasperreports.engine.JRElement;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRImage;
import net.sf.jasperreports.engine.JRImageRenderer;
import net.sf.jasperreports.engine.JRPrintElement;
import net.sf.jasperreports.engine.JRPrintImage;
import net.sf.jasperreports.engine.JRRenderable;
import net.sf.jasperreports.engine.base.JRBasePrintImage;
import net.sf.jasperreports.engine.type.OnErrorTypeEnum;
import net.sf.jasperreports.engine.type.ScaleImageEnum;
import net.sf.jasperreports.engine.util.JRExpressionUtil;
import net.sf.jasperreports.engine.util.JRImageLoader;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public final class ImageConverter extends ElementConverter
{
	private static final Log log = LogFactory.getLog(ImageConverter.class);

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
		printImage.setHorizontalAlignment(image.getOwnHorizontalAlignmentValue());
		printImage.setLazy(image.isLazy());
		printImage.setLinkType(image.getLinkType());
		printImage.setOnErrorType(OnErrorTypeEnum.ICON);
		printImage.setVerticalAlignment(image.getOwnVerticalAlignmentValue());
		printImage.setRenderer(getRenderer(image, printImage));
		printImage.setScaleImage(image.getOwnScaleImageValue());
		
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
				return JRImageRenderer.getInstance(location);
				/*
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
				*/
			}
			catch (JRException e)
			{
				if (log.isDebugEnabled())
				{
					log.debug("Creating location renderer for converted image failed.", e);
				}
			}
		}
		
		try
		{
			printImage.setScaleImage(ScaleImageEnum.CLIP);
			return 
				JRImageRenderer.getInstance(
					JRImageLoader.NO_IMAGE_RESOURCE, 
					imageElement.getOnErrorTypeValue()
					);
		}
		catch (JRException e)
		{
			if (log.isDebugEnabled())
			{
				log.debug("Creating icon renderer for converted image failed.", e);
			}
		}
		
		return null;
	}

}
