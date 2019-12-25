/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2019 TIBCO Software Inc. All rights reserved.
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
import java.awt.geom.AffineTransform;
import java.awt.geom.Dimension2D;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRPrintImage;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.type.ModeEnum;
import net.sf.jasperreports.engine.util.ImageUtil;
import net.sf.jasperreports.renderers.DimensionRenderable;
import net.sf.jasperreports.renderers.Graphics2DRenderable;
import net.sf.jasperreports.renderers.Renderable;
import net.sf.jasperreports.renderers.RenderersCache;
import net.sf.jasperreports.renderers.ResourceRenderer;
import net.sf.jasperreports.renderers.util.RendererUtil;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public class ImageDrawer extends ElementDrawer<JRPrintImage>
{
	private final RenderersCache renderersCache;

	/**
	 * @deprecated Replaced by {@link #ImageDrawer(JasperReportsContext, RenderersCache)}.
	 */
	public ImageDrawer(JasperReportsContext jasperReportsContext)
	{
		this(
			jasperReportsContext,
			new RenderersCache(jasperReportsContext)
			);
	}
	
	/**
	 *
	 */
	public ImageDrawer(
		JasperReportsContext jasperReportsContext,
		RenderersCache renderersCache
		)
	{
		super(jasperReportsContext);
		
		this.renderersCache = renderersCache;
	}
	
	
	@Override
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
		
		Renderable renderer = printImage.getRenderer();
		
		if (
			renderer != null 
			&& internalImageDrawer.availableImageWidth > 0 
			&& internalImageDrawer.availableImageHeight > 0
			)
		{
			try
			{
				internalImageDrawer.draw(grx, renderer);
			}
			catch (Exception e)
			{
				Renderable onErrorRenderer = RendererUtil.getInstance(getJasperReportsContext()).handleImageError(e, printImage.getOnErrorTypeValue());
				if (onErrorRenderer != null)
				{
					internalImageDrawer.draw(grx, onErrorRenderer);
				}
			}
		}

		if (
			printImage.getLineBox().getTopPen().getLineWidth() <= 0f &&
			printImage.getLineBox().getLeftPen().getLineWidth() <= 0f &&
			printImage.getLineBox().getBottomPen().getLineWidth() <= 0f &&
			printImage.getLineBox().getRightPen().getLineWidth() <= 0f
			)
		{
			if (printImage.getLinePen().getLineWidth() != 0)
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

			topPadding = printImage.getLineBox().getTopPadding();
			leftPadding = printImage.getLineBox().getLeftPadding();
			bottomPadding = printImage.getLineBox().getBottomPadding();
			rightPadding = printImage.getLineBox().getRightPadding();
			
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
			if (renderer instanceof ResourceRenderer)
			{
				renderer = renderersCache.getLoadedRenderer((ResourceRenderer)renderer);
			}
			
			Graphics2DRenderable grxRenderer = renderersCache.getGraphics2DRenderable(renderer);//FIXME honor isUsingCache
			
			switch (printImage.getScaleImageValue())
			{
				case CLIP :
				{
					drawClip(grx, grxRenderer);
					break;
				}
				case FILL_FRAME :
				{
					drawFillFrame(grx, grxRenderer);
					break;
				}
				case RETAIN_SHAPE :
				default :
				{
					drawRetainShape(grx, grxRenderer);
				}
			}
		}

		private void drawClip(
			Graphics2D grx,
			Graphics2DRenderable renderer
			) throws JRException
		{
			int translateX = 0;
			int translateY = 0;
			int renderWidth = 0;
			int renderHeight = 0;
			double angle = 0;
			
			Dimension2D dimension = 
				renderer instanceof DimensionRenderable 
				? ((DimensionRenderable)renderer).getDimension(getJasperReportsContext())
				: null;
			
			if (dimension != null)
			{
				renderWidth = (int)dimension.getWidth();
				renderHeight = (int)dimension.getHeight();
			}

			switch (printImage.getRotation())
			{
				case LEFT :
				{
					if (dimension == null)
					{
						renderWidth = availableImageHeight;
						renderHeight = availableImageWidth;
					}
					translateX = (int)(ImageUtil.getYAlignFactor(printImage) * (availableImageWidth - renderHeight));
					translateY = availableImageHeight - (int)(ImageUtil.getXAlignFactor(printImage) * (availableImageHeight - renderWidth));
					angle = - Math.PI / 2;
					break;
				}
				case RIGHT :
				{
					if (dimension == null)
					{
						renderWidth = availableImageHeight;
						renderHeight = availableImageWidth;
					}
					translateX = availableImageWidth - (int)(ImageUtil.getYAlignFactor(printImage) * (availableImageWidth - renderHeight));
					translateY = (int)(ImageUtil.getXAlignFactor(printImage) * (availableImageHeight - renderWidth));
					angle = Math.PI / 2;
					break;
				}
				case UPSIDE_DOWN :
				{
					if (dimension == null)
					{
						renderWidth = availableImageWidth;
						renderHeight = availableImageHeight;
					}
					translateX = availableImageWidth - (int)(ImageUtil.getXAlignFactor(printImage) * (availableImageWidth - renderWidth));
					translateY = availableImageHeight - (int)(ImageUtil.getYAlignFactor(printImage) * (availableImageHeight - renderHeight));
					angle = Math.PI;
					break;
				}
				case NONE :
				default :
				{
					if (dimension == null)
					{
						renderWidth = availableImageWidth;
						renderHeight = availableImageHeight;
					}
					translateX = (int)(ImageUtil.getXAlignFactor(printImage) * (availableImageWidth - renderWidth));
					translateY = (int)(ImageUtil.getYAlignFactor(printImage) * (availableImageHeight - renderHeight));
					angle = 0;
				}
			}
			
			Shape oldClipShape = grx.getClip();

			grx.clip(
				new Rectangle(
					printImage.getX() + leftPadding + offsetX, 
					printImage.getY() + topPadding + offsetY, 
					availableImageWidth, 
					availableImageHeight
					)
				);
			
			
			AffineTransform oldTransform = grx.getTransform();

			grx.translate(
				printImage.getX() + leftPadding + offsetX + translateX, 
				printImage.getY() + topPadding + offsetY + translateY
				);
			grx.rotate(angle);

			try
			{
				renderer.render(
					getJasperReportsContext(),
					grx, 
					new Rectangle(
						0, 
						0, 
						renderWidth, 
						renderHeight
						) 
					);
			}
			finally
			{
				grx.setTransform(oldTransform);
				grx.setClip(oldClipShape);
			}
		}
		
		
		/**
		 *
		 */
		private void drawFillFrame(
			Graphics2D grx,
			Graphics2DRenderable renderer
			) throws JRException
		{
			int translateX = 0;
			int translateY = 0;
			int renderWidth = availableImageWidth;
			int renderHeight = availableImageHeight;
			double angle = 0;
			
			switch (printImage.getRotation())
			{
				case LEFT :
				{
					translateX = 0;
					translateY = availableImageHeight;
					renderWidth = availableImageHeight;
					renderHeight = availableImageWidth;
					angle = - Math.PI / 2;
					break;
				}
				case RIGHT :
				{
					translateX = availableImageWidth;
					translateY = 0;
					renderWidth = availableImageHeight;
					renderHeight = availableImageWidth;
					angle = Math.PI / 2;
					break;
				}
				case UPSIDE_DOWN :
				{
					translateX = availableImageWidth;
					translateY = availableImageHeight;
					renderWidth = availableImageWidth;
					renderHeight = availableImageHeight;
					angle = Math.PI;
					break;
				}
				case NONE :
				default :
				{
				}
			}
			
			AffineTransform oldTransform = grx.getTransform();

			grx.translate(
				printImage.getX() + leftPadding + offsetX + translateX, 
				printImage.getY() + topPadding + offsetY + translateY
				);
			grx.rotate(angle);
			
			try
			{
				renderer.render(
					getJasperReportsContext(),
					grx,
					new Rectangle(
						0, 
						0, 
						renderWidth,
						renderHeight 
						) 
					);
			}
			finally
			{
				grx.setTransform(oldTransform);
			}
		}
		
		
		/**
		 *
		 */
		private void drawRetainShape(
			Graphics2D grx,
			Graphics2DRenderable renderer
			) throws JRException
		{
			float normalWidth = 0;
			float normalHeight = 0;

			float ratioX = 1f;
			float ratioY = 1f;
			
			int translateX = 0;
			int translateY = 0;
			float renderWidth = 0;
			float renderHeight = 0;
			double angle = 0;
			
			Dimension2D dimension = 
				renderer instanceof DimensionRenderable 
				? ((DimensionRenderable)renderer).getDimension(getJasperReportsContext())
				: null;
			if (dimension != null)
			{
				normalWidth = (int)dimension.getWidth();
				normalHeight = (int)dimension.getHeight();
			}

			switch (printImage.getRotation())
			{
				case LEFT :
				{
					if (dimension == null)
					{
						normalWidth = availableImageHeight;
						normalHeight = availableImageWidth;
					}
					ratioX = availableImageWidth / normalHeight;
					ratioY = availableImageHeight / normalWidth;
					ratioX = ratioX < ratioY ? ratioX : ratioY;
					ratioY = ratioX;
					renderWidth = normalWidth * ratioX;
					renderHeight = normalHeight * ratioY;
					translateX = (int)(ImageUtil.getYAlignFactor(printImage) * (availableImageWidth - renderHeight));
					translateY = availableImageHeight - (int)(ImageUtil.getXAlignFactor(printImage) * (availableImageHeight - renderWidth));
					angle = - Math.PI / 2;
					break;
				}
				case RIGHT :
				{
					if (dimension == null)
					{
						normalWidth = availableImageHeight;
						normalHeight = availableImageWidth;
					}
					ratioX = availableImageWidth / normalHeight;
					ratioY = availableImageHeight / normalWidth;
					ratioX = ratioX < ratioY ? ratioX : ratioY;
					ratioY = ratioX;
					renderWidth = normalWidth * ratioX;
					renderHeight = normalHeight * ratioY;
					translateX = availableImageWidth - (int)(ImageUtil.getYAlignFactor(printImage) * (availableImageWidth - renderHeight));
					translateY = (int)(ImageUtil.getXAlignFactor(printImage) * (availableImageHeight - renderWidth));
					angle = Math.PI / 2;
					break;
				}
				case UPSIDE_DOWN :
				{
					if (dimension == null)
					{
						normalWidth = availableImageWidth;
						normalHeight = availableImageHeight;
					}
					ratioX = availableImageWidth / normalWidth;
					ratioY = availableImageHeight / normalHeight;
					ratioX = ratioX < ratioY ? ratioX : ratioY;
					ratioY = ratioX;
					renderWidth = normalWidth * ratioX;
					renderHeight = normalHeight * ratioY;
					translateX = availableImageWidth - (int)(ImageUtil.getXAlignFactor(printImage) * (availableImageWidth - renderWidth));
					translateY = availableImageHeight - (int)(ImageUtil.getYAlignFactor(printImage) * (availableImageHeight - renderHeight));
					angle = Math.PI;
					break;
				}
				case NONE :
				default :
				{
					if (dimension == null)
					{
						normalWidth = availableImageWidth;
						normalHeight = availableImageHeight;
					}
					ratioX = availableImageWidth / normalWidth;
					ratioY = availableImageHeight / normalHeight;
					ratioX = ratioX < ratioY ? ratioX : ratioY;
					ratioY = ratioX;
					renderWidth = normalWidth * ratioX;
					renderHeight = normalHeight * ratioY;
					translateX = (int)(ImageUtil.getXAlignFactor(printImage) * (availableImageWidth - renderWidth));
					translateY = (int)(ImageUtil.getYAlignFactor(printImage) * (availableImageHeight - renderHeight));
					angle = 0;
				}
			}
			
			AffineTransform oldTransform = grx.getTransform();

			grx.translate(
				printImage.getX() + leftPadding + offsetX + translateX, 
				printImage.getY() + topPadding + offsetY + translateY
				);
			grx.rotate(angle);
			
			try
			{
				renderer.render(
					getJasperReportsContext(),
					grx,
					new Rectangle(
						0, 
						0, 
						(int)renderWidth,
						(int)renderHeight 
						) 
					);
			}
			finally
			{
				grx.setTransform(oldTransform);
			}
		}
	}
}
