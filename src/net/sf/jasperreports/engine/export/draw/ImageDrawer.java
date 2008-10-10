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
package net.sf.jasperreports.engine.export.draw;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Dimension2D;

import net.sf.jasperreports.engine.JRAlignment;
import net.sf.jasperreports.engine.JRElement;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRImage;
import net.sf.jasperreports.engine.JRImageRenderer;
import net.sf.jasperreports.engine.JRPrintElement;
import net.sf.jasperreports.engine.JRPrintImage;
import net.sf.jasperreports.engine.JRRenderable;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id: JRGraphics2DExporter.java 1811 2007-08-13 19:52:07Z teodord $
 */
public class ImageDrawer extends ElementDrawer
{

	/**
	 *
	 */
	public void draw(Graphics2D grx, JRPrintElement element, int offsetX, int offsetY) throws JRException
	{
		JRPrintImage printImage = (JRPrintImage)element;
		
		grx = //FIXME use this for all Graphics2D operations
			(Graphics2D) grx.create(
				printImage.getX() + offsetX, 
				printImage.getY() + offsetY, 
				printImage.getWidth(),
				printImage.getHeight()
				);

		if (printImage.getMode() == JRElement.MODE_OPAQUE)
		{
			grx.setColor(printImage.getBackcolor());

			grx.fillRect(
				0, 
				0, 
				printImage.getWidth(),
				printImage.getHeight()
				);
		}

		int topPadding = printImage.getLineBox().getTopPadding().intValue();
		int leftPadding = printImage.getLineBox().getLeftPadding().intValue();
		int bottomPadding = printImage.getLineBox().getBottomPadding().intValue();
		int rightPadding = printImage.getLineBox().getRightPadding().intValue();
		
		int availableImageWidth = printImage.getWidth() - leftPadding - rightPadding;
		availableImageWidth = (availableImageWidth < 0)?0:availableImageWidth;

		int availableImageHeight = printImage.getHeight() - topPadding - bottomPadding;
		availableImageHeight = (availableImageHeight < 0)?0:availableImageHeight;
		
		JRRenderable renderer = printImage.getRenderer();
		
		if (
			renderer != null &&
			availableImageWidth > 0 &&
			availableImageHeight > 0
			)
		{
			if (renderer.getType() == JRRenderable.TYPE_IMAGE)
			{
				// Image renderers are all asked for their image data and dimension at some point. 
				// Better to test and replace the renderer now, in case of lazy load error.
				renderer = JRImageRenderer.getOnErrorRendererForImageData(renderer, printImage.getOnErrorType());
				if (renderer != null)
				{
					renderer = JRImageRenderer.getOnErrorRendererForDimension(renderer, printImage.getOnErrorType());
				}
			}
		}
		else
		{
			renderer = null;
		}

		if (renderer != null)
		{
			int normalWidth = availableImageWidth;
			int normalHeight = availableImageHeight;

			Dimension2D dimension = renderer.getDimension();
			if (dimension != null)
			{
				normalWidth = (int)dimension.getWidth();
				normalHeight = (int)dimension.getHeight();
			}
	
			float xalignFactor = 0f;
			switch (printImage.getHorizontalAlignment())
			{
				case JRAlignment.HORIZONTAL_ALIGN_RIGHT :
				{
					xalignFactor = 1f;
					break;
				}
				case JRAlignment.HORIZONTAL_ALIGN_CENTER :
				{
					xalignFactor = 0.5f;
					break;
				}
				case JRAlignment.HORIZONTAL_ALIGN_LEFT :
				default :
				{
					xalignFactor = 0f;
					break;
				}
			}

			float yalignFactor = 0f;
			switch (printImage.getVerticalAlignment())
			{
				case JRAlignment.VERTICAL_ALIGN_BOTTOM :
				{
					yalignFactor = 1f;
					break;
				}
				case JRAlignment.VERTICAL_ALIGN_MIDDLE :
				{
					yalignFactor = 0.5f;
					break;
				}
				case JRAlignment.VERTICAL_ALIGN_TOP :
				default :
				{
					yalignFactor = 0f;
					break;
				}
			}

			switch (printImage.getScaleImage())// FIXME maybe put this in JRFiller
			{
				case JRImage.SCALE_IMAGE_CLIP :
				{
					int xoffset = (int)(xalignFactor * (availableImageWidth - normalWidth));
					int yoffset = (int)(yalignFactor * (availableImageHeight - normalHeight));

//					Shape oldClipShape = grx.getClip();

					grx.clip(
						new Rectangle(
							leftPadding, 
							topPadding, 
							availableImageWidth, 
							availableImageHeight
							)
						);
					
//					try
//					{
						renderer.render(
							grx, 
							new Rectangle(
								leftPadding + xoffset, 
								topPadding + yoffset, 
								normalWidth, 
								normalHeight
								) 
							);
//					}
//					finally
//					{
//						grx.setClip(oldClipShape);
//					}
	
					break;
				}
				case JRImage.SCALE_IMAGE_FILL_FRAME :
				{
					renderer.render(
						grx,
						new Rectangle(
							leftPadding, 
							topPadding, 
							availableImageWidth, 
							availableImageHeight
							)
						);
	
					break;
				}
				case JRImage.SCALE_IMAGE_RETAIN_SHAPE :
				default :
				{
					if (printImage.getHeight() > 0)
					{
						double ratio = (double)normalWidth / (double)normalHeight;
						
						if( ratio > (double)availableImageWidth / (double)availableImageHeight )
						{
							normalWidth = availableImageWidth; 
							normalHeight = (int)(availableImageWidth / ratio); 
						}
						else
						{
							normalWidth = (int)(availableImageHeight * ratio); 
							normalHeight = availableImageHeight; 
						}

						int xoffset = (int)(xalignFactor * (availableImageWidth - normalWidth));
						int yoffset = (int)(yalignFactor * (availableImageHeight - normalHeight));

						renderer.render(
							grx,
							new Rectangle(
								leftPadding + xoffset, 
								topPadding + yoffset, 
								normalWidth, 
								normalHeight
								) 
							);
					}
					
					break;
				}
			}
		}

		if (
			printImage.getLineBox().getTopPen().getLineWidth().floatValue() <= 0f &&
			printImage.getLineBox().getLeftPen().getLineWidth().floatValue() <= 0f &&
			printImage.getLineBox().getBottomPen().getLineWidth().floatValue() <= 0f &&
			printImage.getLineBox().getRightPen().getLineWidth().floatValue() <= 0f
			)
		{
			if (printImage.getLinePen().getLineWidth().floatValue() != 0)
			{
				drawPen(
					grx, 
					printImage.getLinePen(), 
					printImage, 
					offsetX, 
					offsetY
					);
			}
		}
		else
		{
			drawBox(grx, printImage.getLineBox(), printImage, offsetX, offsetY);
		}
	}

}
