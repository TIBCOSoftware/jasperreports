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
package net.sf.jasperreports.engine.export.oasis;

import java.awt.Dimension;
import java.awt.geom.Dimension2D;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRPrintImage;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.export.JRExporterGridCell;
import net.sf.jasperreports.engine.type.ModeEnum;
import net.sf.jasperreports.renderers.DimensionRenderable;
import net.sf.jasperreports.renderers.Renderable;
import net.sf.jasperreports.renderers.RenderersCache;
import net.sf.jasperreports.renderers.ResourceRenderer;
import net.sf.jasperreports.renderers.util.RendererUtil;

/**
 * @author Sanda Zaharia (shertage@users.sourceforge.net)
 */
public class InternalImageProcessor 
{
	private final JRPrintImage imageElement;
	private final RenderersCache imageRenderersCache;
	private final JRExporterGridCell cell;
	private final int availableImageWidth;
	private final int availableImageHeight;
	private final DocumentBuilder documentBuilder;
	private final JasperReportsContext jasperReportsContext;
	
	public InternalImageProcessor(
		JRPrintImage imageElement,
		JRExporterGridCell cell,
		int availableImageWidth,
		int availableImageHeight,
		DocumentBuilder documentBuilder,
		JasperReportsContext jasperReportsContext
		)
	{
		this.imageElement = imageElement;
		this.imageRenderersCache = imageElement.isUsingCache() ? documentBuilder.getRenderersCache() 
				: new RenderersCache(jasperReportsContext);
		this.cell = cell;
		this.availableImageWidth = availableImageWidth;
		this.availableImageHeight = availableImageHeight;
		this.documentBuilder = documentBuilder;
		this.jasperReportsContext = jasperReportsContext;
	}
	
	public InternalImageProcessorResult process(Renderable renderer) throws JRException
	{
		boolean isLazy = RendererUtil.isLazy(renderer);

		if (!isLazy)
		{
			if (renderer instanceof ResourceRenderer)
			{
				renderer = imageRenderersCache.getLoadedRenderer((ResourceRenderer)renderer);
			}
		}

		// check dimension first, to avoid caching renderers that might not be used eventually, due to their dimension errors 

		int width = availableImageWidth;
		int height = availableImageHeight;

		int xoffset = 0;
		int yoffset = 0;
		
		double cropTop = 0;
		double cropLeft = 0;
		double cropBottom = 0;
		double cropRight = 0;


		switch (imageElement.getScaleImageValue())
		{
			case FILL_FRAME :
			{
				width = availableImageWidth;
				height = availableImageHeight;
//				xoffset = 0;
//				yoffset = 0;
				break;
			}
			case CLIP :
			{
				double normalWidth = availableImageWidth;
				double normalHeight = availableImageHeight;

				DimensionRenderable dimensionRenderer = imageRenderersCache.getDimensionRenderable(renderer);
				Dimension2D dimension = dimensionRenderer == null ? null :  dimensionRenderer.getDimension(jasperReportsContext);
				if (dimension != null)
				{
					normalWidth = dimension.getWidth();
					normalHeight = dimension.getHeight();
				}

				if (normalWidth > availableImageWidth)
				{
					switch (imageElement.getHorizontalImageAlign())
					{
						case RIGHT :
						{
							cropLeft = normalWidth - availableImageWidth;
							cropRight = 0;
							break;
						}
						case CENTER :
						{
							cropLeft = (normalWidth - availableImageWidth) / 2;
							cropRight = cropLeft;
							break;
						}
						case LEFT :
						default :
						{
							cropLeft = 0;
							cropRight = normalWidth - availableImageWidth;
							break;
						}
					}
					width = (int)normalWidth - (int)cropLeft - (int)cropRight;
				}
				else
				{
					width = (int)normalWidth;
				}

				if (normalHeight > availableImageHeight)
				{
					switch (imageElement.getVerticalImageAlign())
					{
						case TOP :
						{
							cropTop = 0;
							cropBottom = normalHeight - availableImageHeight;
							break;
						}
						case MIDDLE :
						{
							cropTop = (normalHeight - availableImageHeight) / 2;
							cropBottom = cropTop;
							break;
						}
						case BOTTOM :
						default :
						{
							cropTop = normalHeight - availableImageHeight;
							cropBottom = 0;
							break;
						}
					}
					height = (int)normalHeight - (int)cropTop - (int)cropBottom;
				}
				else
				{
					height = (int)normalHeight;
				}
				
//				xoffset = (int)(ImageUtil.getXAlignFactor(imageElement) * ((int)normalWidth - availableImageWidth));
//				yoffset = (int)(ImageUtil.getYAlignFactor(imageElement) * ((int)normalHeight - availableImageHeight));

				break;
			}
			case RETAIN_SHAPE :
			default :
			{
				double normalWidth = availableImageWidth;
				double normalHeight = availableImageHeight;

				if (!isLazy)
				{
					DimensionRenderable dimensionRenderer = imageRenderersCache.getDimensionRenderable(renderer);
					Dimension2D dimension = dimensionRenderer == null ? null :  dimensionRenderer.getDimension(jasperReportsContext);
					if (dimension != null)
					{
						normalWidth = dimension.getWidth();
						normalHeight = dimension.getHeight();
					}
				}

				double ratio = normalWidth / normalHeight;

				if( ratio > availableImageWidth / (double)availableImageHeight )
				{
					width = availableImageWidth;
					height = (int)(width/ratio);

				}
				else
				{
					height = availableImageHeight;
					width = (int)(ratio * height);
				}

//				xoffset = (int)(ImageUtil.getXAlignFactor(imageElement) * (availableImageWidth - width));
//				yoffset = (int)(ImageUtil.getYAlignFactor(imageElement) * (availableImageHeight - height));
			}
		}


		String imagePath = 
				documentBuilder.getImagePath(
					renderer, 
					new Dimension(availableImageWidth, availableImageHeight),
					ModeEnum.OPAQUE == imageElement.getModeValue() ? imageElement.getBackcolor() : null,
					cell,
					isLazy,
					imageRenderersCache
					);

		return 
			new InternalImageProcessorResult(
				imagePath, 
				width,
				height,
				xoffset,
				yoffset,
				cropTop,
				cropLeft,
				cropBottom,
				cropRight
				);
	}
}
