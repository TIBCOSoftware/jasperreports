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
import net.sf.jasperreports.engine.util.ImageUtil;
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

		int imageWidth = availableImageWidth;
		int imageHeight = availableImageHeight;

		int xoffset = 0;
		int yoffset = 0;
		
		double cropTop = 0;
		double cropLeft = 0;
		double cropBottom = 0;
		double cropRight = 0;
		
		double angle = 0;

		switch (imageElement.getScaleImageValue())
		{
			case FILL_FRAME :
			{
				switch (imageElement.getRotation())
				{
					case LEFT:
						imageWidth = availableImageHeight;
						imageHeight = availableImageWidth;
						xoffset = 0;
						yoffset = availableImageHeight;
						angle = Math.PI / 2;
						break;
					case RIGHT:
						imageWidth = availableImageHeight;
						imageHeight = availableImageWidth;
						xoffset = availableImageWidth;
						yoffset = 0;
						angle = - Math.PI / 2;
						break;
					case UPSIDE_DOWN:
						imageWidth = availableImageWidth;
						imageHeight = availableImageHeight;
						xoffset = availableImageWidth;
						yoffset = availableImageHeight;
						angle = Math.PI;
						break;
					case NONE:
					default:
						imageWidth = availableImageWidth;
						imageHeight = availableImageHeight;
						xoffset = 0;
						yoffset = 0;
						angle = 0;
						break;
				}
				
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

				switch (imageElement.getRotation())
				{
					case LEFT:
						imageWidth = availableImageHeight;
						imageHeight = availableImageWidth;
						switch (imageElement.getHorizontalImageAlign())
						{
							case RIGHT :
								cropLeft = normalWidth - availableImageHeight;
								cropRight = 0;
								yoffset = imageWidth;
								break;
							case CENTER :
								cropLeft = (normalWidth - availableImageHeight) / 2;
								cropRight = cropLeft;
								yoffset = availableImageHeight - (availableImageHeight - imageWidth) / 2;
								break;
							case LEFT :
							default :
								cropLeft = 0;
								cropRight = normalWidth - availableImageHeight;
								yoffset = availableImageHeight;
								break;
						}
						switch (imageElement.getVerticalImageAlign())
						{
							case TOP :
								cropTop = 0;
								cropBottom = normalHeight - availableImageWidth;
								xoffset = 0;
								break;
							case MIDDLE :
								cropTop = (normalHeight - availableImageWidth) / 2;
								cropBottom = cropTop;
								xoffset = (availableImageWidth - imageHeight) / 2;
								break;
							case BOTTOM :
							default :
								cropTop = normalHeight - availableImageWidth;
								cropBottom = 0;
								xoffset = availableImageWidth - imageHeight;
								break;
						}
						angle = Math.PI / 2;
						break;
					case RIGHT:
						imageWidth = availableImageHeight;
						imageHeight = availableImageWidth;
						switch (imageElement.getHorizontalImageAlign())
						{
							case RIGHT :
								cropLeft = normalWidth - availableImageHeight;
								cropRight = 0;
								yoffset = availableImageHeight - imageWidth;
								break;
							case CENTER :
								cropLeft = (normalWidth - availableImageHeight) / 2;
								cropRight = cropLeft;
								yoffset = (availableImageHeight - imageWidth) / 2;
								break;
							case LEFT :
							default :
								cropLeft = 0;
								cropRight = normalWidth - availableImageHeight;
								yoffset = 0;
								break;
						}
						switch (imageElement.getVerticalImageAlign())
						{
							case TOP :
								cropTop = 0;
								cropBottom = normalHeight - availableImageWidth;
								xoffset = availableImageWidth;
								break;
							case MIDDLE :
								cropTop = (normalHeight - availableImageWidth) / 2;
								cropBottom = cropTop;
								xoffset = availableImageWidth - (availableImageWidth - imageHeight) / 2;
								break;
							case BOTTOM :
							default :
								cropTop = normalHeight - availableImageWidth;
								cropBottom = 0;
								xoffset = imageHeight;
								break;
						}
						angle = - Math.PI / 2;
						break;
					case UPSIDE_DOWN:
						imageWidth = (int)Math.min(normalWidth, availableImageWidth);
						imageHeight = (int)Math.min(normalHeight, availableImageHeight);
						switch (imageElement.getHorizontalImageAlign())
						{
							case RIGHT :
								cropLeft = normalWidth - availableImageWidth;
								cropRight = 0;
								xoffset = imageWidth;
								break;
							case CENTER :
								cropLeft = (normalWidth - availableImageWidth) / 2;
								cropRight = cropLeft;
								xoffset = availableImageWidth - (availableImageWidth - imageWidth) / 2;
								break;
							case LEFT :
							default :
								cropLeft = 0;
								cropRight = normalWidth - availableImageWidth;
								xoffset = availableImageWidth;
								break;
						}
						switch (imageElement.getVerticalImageAlign())
						{
							case TOP :
								cropTop = 0;
								cropBottom = normalHeight - availableImageHeight;
								yoffset = availableImageHeight;
								break;
							case MIDDLE :
								cropTop = (normalHeight - availableImageHeight) / 2;
								cropBottom = cropTop;
								yoffset = availableImageHeight - (availableImageHeight - imageHeight) / 2;
								break;
							case BOTTOM :
							default :
								cropTop = normalHeight - availableImageHeight;
								cropBottom = 0;
								yoffset = imageHeight;
								break;
						}
						angle = Math.PI;
						break;
					case NONE:
					default:
						imageWidth = availableImageWidth;
						imageHeight = availableImageHeight;
						switch (imageElement.getHorizontalImageAlign())
						{
							case RIGHT :
								cropLeft = normalWidth - availableImageWidth;
								cropRight = 0;
								xoffset = availableImageWidth - imageWidth;
								break;
							case CENTER :
								cropLeft = (normalWidth - availableImageWidth) / 2;
								cropRight = cropLeft;
								xoffset = (availableImageWidth - imageWidth) / 2;
								break;
							case LEFT :
							default :
								cropLeft = 0;
								cropRight = normalWidth - availableImageWidth;
								xoffset = 0;
								break;
						}
						switch (imageElement.getVerticalImageAlign())
						{
							case TOP :
								cropTop = 0;
								cropBottom = normalHeight - availableImageHeight;
								yoffset = 0;
								break;
							case MIDDLE :
								cropTop = (normalHeight - availableImageHeight) / 2;
								cropBottom = cropTop;
								yoffset = (availableImageHeight - imageHeight) / 2;
								break;
							case BOTTOM :
							default :
								cropTop = normalHeight - availableImageHeight;
								cropBottom = 0;
								yoffset = availableImageHeight - imageHeight;
								break;
						}
						angle = 0;
						break;
				}
								
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

				double ratioX = 1f;
				double ratioY = 1f;

				switch (imageElement.getRotation())
				{
					case LEFT:
						imageWidth = availableImageHeight;
						imageHeight = availableImageWidth;
						ratioX = availableImageWidth / normalHeight;
						ratioY = availableImageHeight / normalWidth;
						ratioX = ratioX < ratioY ? ratioX : ratioY;
						ratioY = ratioX;
						xoffset = 0;
						yoffset = availableImageHeight;
						cropLeft = (int)(ImageUtil.getXAlignFactor(imageElement) * (normalWidth - availableImageHeight / ratioX));
						cropRight = (int)((1f - ImageUtil.getXAlignFactor(imageElement)) * (normalWidth - availableImageHeight / ratioX));
						cropTop = (int)(ImageUtil.getYAlignFactor(imageElement) * (normalHeight - availableImageWidth / ratioY));
						cropBottom = (int)((1f - ImageUtil.getYAlignFactor(imageElement)) * (normalHeight - availableImageWidth / ratioY));
						angle = Math.PI / 2;
						break;
					case RIGHT:
						imageWidth = availableImageHeight;
						imageHeight = availableImageWidth;
						ratioX = availableImageWidth / normalHeight;
						ratioY = availableImageHeight / normalWidth;
						ratioX = ratioX < ratioY ? ratioX : ratioY;
						ratioY = ratioX;
						xoffset = availableImageWidth;
						yoffset = 0;
						cropLeft = (int)(ImageUtil.getXAlignFactor(imageElement) * (normalWidth - availableImageHeight / ratioX));
						cropRight = (int)((1f - ImageUtil.getXAlignFactor(imageElement)) * (normalWidth - availableImageHeight / ratioX));
						cropTop = (int)(ImageUtil.getYAlignFactor(imageElement) * (normalHeight - availableImageWidth / ratioY));
						cropBottom = (int)((1f - ImageUtil.getYAlignFactor(imageElement)) * (normalHeight - availableImageWidth / ratioY));
						angle = - Math.PI / 2;
						break;
					case UPSIDE_DOWN:
						imageWidth = availableImageWidth;
						imageHeight = availableImageHeight;
						ratioX = availableImageWidth / normalWidth;
						ratioY = availableImageHeight / normalHeight;
						ratioX = ratioX < ratioY ? ratioX : ratioY;
						ratioY = ratioX;
						xoffset = availableImageWidth;
						yoffset = availableImageHeight;
						cropLeft = (int)(ImageUtil.getXAlignFactor(imageElement) * (normalWidth - availableImageWidth / ratioX));
						cropRight = (int)((1f - ImageUtil.getXAlignFactor(imageElement)) * (normalWidth - availableImageWidth / ratioX));
						cropTop = (int)(ImageUtil.getYAlignFactor(imageElement) * (normalHeight - availableImageHeight / ratioY));
						cropBottom = (int)((1f - ImageUtil.getYAlignFactor(imageElement)) * (normalHeight - availableImageHeight / ratioY));
						angle = Math.PI;
						break;
					case NONE:
					default:
						imageWidth = availableImageWidth;
						imageHeight = availableImageHeight;
						ratioX = availableImageWidth / normalWidth;
						ratioY = availableImageHeight / normalHeight;
						ratioX = ratioX < ratioY ? ratioX : ratioY;
						ratioY = ratioX;
						xoffset = 0;
						yoffset = 0;
						cropLeft = (int)(ImageUtil.getXAlignFactor(imageElement) * (normalWidth - availableImageWidth / ratioX));
						cropRight = (int)((1f - ImageUtil.getXAlignFactor(imageElement)) * (normalWidth - availableImageWidth / ratioX));
						cropTop = (int)(ImageUtil.getYAlignFactor(imageElement) * (normalHeight - availableImageHeight / ratioY));
						cropBottom = (int)((1f - ImageUtil.getYAlignFactor(imageElement)) * (normalHeight - availableImageHeight / ratioY));
						angle = 0;
						break;
				}
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
				imageWidth,
				imageHeight,
				xoffset,
				yoffset,
				cropTop,
				cropLeft,
				cropBottom,
				cropRight,
				angle
				);
	}
}
