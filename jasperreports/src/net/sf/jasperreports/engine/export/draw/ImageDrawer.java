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
import java.awt.Shape;
import java.awt.geom.Dimension2D;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRPrintImage;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.Renderable;
import net.sf.jasperreports.engine.RenderableUtil;
import net.sf.jasperreports.engine.type.ModeEnum;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public class ImageDrawer extends ElementDrawer<JRPrintImage>
{
	/**
	 *
	 */
	public ImageDrawer(JasperReportsContext jasperReportsContext)
	{
		super(jasperReportsContext);
	}
	
	
	/**
	 *
	 */
	public void draw(Graphics2D grx, JRPrintImage printImage, int offsetX, int offsetY) throws JRException
	{
		if (printImage.getModeValue() == ModeEnum.OPAQUE)
		{
			grx.setColor(printImage.getBackcolor());

			grx.fillRect(
				printImage.getX() + offsetX, 
				printImage.getY() + offsetY, 
				printImage.getWidth(),
				printImage.getHeight()
				);
		}

		InternalImageDrawer internalImageDrawer = 
			new InternalImageDrawer(
				printImage,
				offsetX,
				offsetY
				);
		
		Renderable renderer = printImage.getRenderable();
		
		if (
			renderer != null &&
			internalImageDrawer.availableImageWidth > 0 &&
			internalImageDrawer.availableImageHeight > 0
			)
		{
			try
			{
				internalImageDrawer.draw(grx, renderer);
			}
			catch (Exception e)
			{
				Renderable onErrorRenderer = RenderableUtil.getInstance(getJasperReportsContext()).handleImageError(e, printImage.getOnErrorTypeValue());
				if (onErrorRenderer != null)
				{
					internalImageDrawer.draw(grx, onErrorRenderer);
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
	
	
	private class InternalImageDrawer
	{
		private final JRPrintImage printImage;
		private final int offsetX;
		private final int offsetY;
		private final int availableImageWidth;
		private final int availableImageHeight;
		private final int topPadding;
		private final int leftPadding;
		private final int bottomPadding;
		private final int rightPadding;

		protected InternalImageDrawer(
			JRPrintImage printImage,
			int offsetX,
			int offsetY
			)
		{
			this.printImage = printImage;
			this.offsetX = offsetX;
			this.offsetY = offsetY;

			topPadding = printImage.getLineBox().getTopPadding().intValue();
			leftPadding = printImage.getLineBox().getLeftPadding().intValue();
			bottomPadding = printImage.getLineBox().getBottomPadding().intValue();
			rightPadding = printImage.getLineBox().getRightPadding().intValue();
			
			int tmpAvailableImageWidth = printImage.getWidth() - leftPadding - rightPadding;
			availableImageWidth = tmpAvailableImageWidth < 0 ? 0 : tmpAvailableImageWidth;

			int tmpAvailableImageHeight = printImage.getHeight() - topPadding - bottomPadding;
			availableImageHeight = tmpAvailableImageHeight < 0 ? 0 : tmpAvailableImageHeight;
		}
		
		protected void draw(
			Graphics2D grx,
			Renderable renderer
			) throws JRException
		{
			switch (printImage.getScaleImageValue())
			{
				case CLIP :
				{
					drawClip(grx, renderer);
					break;
				}
				case FILL_FRAME :
				{
					drawFillFrame(grx, renderer);
					break;
				}
				case RETAIN_SHAPE :
				default :
				{
					drawRetainShape(grx, renderer);
				}
			}
		}

		private void drawClip(
			Graphics2D grx,
			Renderable renderer
			) throws JRException
		{
			int normalWidth = availableImageWidth;
			int normalHeight = availableImageHeight;

			Dimension2D dimension = renderer.getDimension(getJasperReportsContext());
			if (dimension != null)
			{
				normalWidth = (int)dimension.getWidth();
				normalHeight = (int)dimension.getHeight();
			}

			int xoffset = (int)(getXAlignFactor(printImage) * (availableImageWidth - normalWidth));
			int yoffset = (int)(getYAlignFactor(printImage) * (availableImageHeight - normalHeight));

			Shape oldClipShape = grx.getClip();

			grx.clip(
				new Rectangle(
					printImage.getX() + leftPadding + offsetX, 
					printImage.getY() + topPadding + offsetY, 
					availableImageWidth, 
					availableImageHeight
					)
				);
			
			try
			{
				renderer.render(
					getJasperReportsContext(),
					grx, 
					new Rectangle(
						printImage.getX() + leftPadding + offsetX + xoffset, 
						printImage.getY() + topPadding + offsetY + yoffset, 
						normalWidth, 
						normalHeight
						) 
					);
			}
			finally
			{
				grx.setClip(oldClipShape);
			}
		}
		
		
		/**
		 *
		 */
		private void drawFillFrame(
			Graphics2D grx,
			Renderable renderer
			) throws JRException
		{
			renderer.render(
				getJasperReportsContext(),
				grx,
				new Rectangle(
					printImage.getX() + leftPadding + offsetX, 
					printImage.getY() + topPadding + offsetY, 
					availableImageWidth, 
					availableImageHeight
					)
				);
		}
		
		
		/**
		 *
		 */
		private void drawRetainShape(
			Graphics2D grx,
			Renderable renderer
			) throws JRException
		{
			if (printImage.getHeight() > 0)
			{
				int normalWidth = availableImageWidth;
				int normalHeight = availableImageHeight;

				Dimension2D dimension = renderer.getDimension(getJasperReportsContext());
				if (dimension != null)
				{
					normalWidth = (int)dimension.getWidth();
					normalHeight = (int)dimension.getHeight();
				}
		
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

				int xoffset = (int)(getXAlignFactor(printImage) * (availableImageWidth - normalWidth));
				int yoffset = (int)(getYAlignFactor(printImage) * (availableImageHeight - normalHeight));

				renderer.render(
					getJasperReportsContext(),
					grx,
					new Rectangle(
						printImage.getX() + leftPadding + offsetX + xoffset, 
						printImage.getY() + topPadding + offsetY + yoffset, 
						normalWidth, 
						normalHeight
						) 
					);
			}
		}
	}

	private float getXAlignFactor(JRPrintImage printImage)
	{
		float xalignFactor = 0f;
		switch (printImage.getHorizontalImageAlign())
		{
			case RIGHT :
			{
				xalignFactor = 1f;
				break;
			}
			case CENTER :
			{
				xalignFactor = 0.5f;
				break;
			}
			case LEFT :
			default :
			{
				xalignFactor = 0f;
				break;
			}
		}
		return xalignFactor;
	}

	private float getYAlignFactor(JRPrintImage printImage)
	{
		float yalignFactor = 0f;
		switch (printImage.getVerticalImageAlign())
		{
			case BOTTOM :
			{
				yalignFactor = 1f;
				break;
			}
			case MIDDLE :
			{
				yalignFactor = 0.5f;
				break;
			}
			case TOP :
			default :
			{
				yalignFactor = 0f;
				break;
			}
		}
		return yalignFactor;
	}
}
