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
package net.sf.jasperreports.customvisualization.export;

import net.sf.jasperreports.customvisualization.CVConstants;
import net.sf.jasperreports.customvisualization.CVPrintElement;
import net.sf.jasperreports.customvisualization.CVUtils;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.base.JRBasePrintImage;
import net.sf.jasperreports.engine.type.HorizontalImageAlignEnum;
import net.sf.jasperreports.engine.type.OnErrorTypeEnum;
import net.sf.jasperreports.engine.type.ScaleImageEnum;
import net.sf.jasperreports.engine.type.VerticalImageAlignEnum;
import net.sf.jasperreports.renderers.Renderable;
import net.sf.jasperreports.renderers.SimpleDataRenderer;
import net.sf.jasperreports.renderers.SimpleRenderToImageAwareDataRenderer;
import net.sf.jasperreports.renderers.util.RendererUtil;
import net.sf.jasperreports.repo.RepositoryContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author Giulio Toffoli (gtoffoli@tibco.com)
 */
public class CVElementImageProvider
{
	private static final Log log = LogFactory.getLog(CVElementImageProvider.class);

	private static final CVElementImageProvider INSTANCE = new CVElementImageProvider();

	private CVElementImageDataProvider cvElementImageDataProvider = new CVElementDefaultImageDataProvider();

	public static CVElementImageProvider getInstance()
	{
		return INSTANCE;
	}

	/**
	 * Produce an image for the element.
	 * 
	 * This method is invoked by JasperReports all the times the report needs to
	 * be rendered. It is called even if the element itself is not yet ready to
	 * be painted (in example when the viewer is displaying the first page
	 * containing this element, but this element has evaluation time report).
	 * 
	 * The method return a JRPrintImage with a null renderer until the final image
	 * is not complete, in which case we look in the element for a cached renderer
	 * save as parameter with key CVPrintElement.PARAMETER_CACHE_RENDERER inside the element.
	 * If the renderer is not available (because this is the first time we try to draw this
	 * element after the image has been produced), the new renderer will be created.
	 *
	 * The ability to set a null renderer works starting from 6.2.2.
	 * 
	 * @param repositoryContext
	 * @param element
	 * @return
	 * @throws JRException
	 */
	public JRPrintImage getImage(
		RepositoryContext repositoryContext, 
		JRGenericPrintElement element) throws JRException
	{
		JRPrintImage printImage = createPrintImage(element);
		Renderable renderable = createRenderable(element, repositoryContext);
		printImage.setRenderer(renderable);

		return printImage;
	}

	public JRPrintImage createPrintImage(JRGenericPrintElement element)
	{
		JRBasePrintImage printImage = new JRBasePrintImage(element.getDefaultStyleProvider());

		printImage.setUUID(element.getUUID());
		printImage.setX(element.getX());
		printImage.setY(element.getY());
		printImage.setWidth(element.getWidth());
		printImage.setHeight(element.getHeight());
		printImage.setStyle(element.getStyle());
		printImage.setMode(element.getModeValue());
		printImage.setBackcolor(element.getBackcolor());
		printImage.setForecolor(element.getForecolor());

		printImage.setScaleImage(ScaleImageEnum.RETAIN_SHAPE);
		printImage.setHorizontalImageAlign(HorizontalImageAlignEnum.LEFT);
		printImage.setVerticalImageAlign(VerticalImageAlignEnum.TOP);

		//printImage.setOnErrorType(OnErrorTypeEnum.BLANK);

		return printImage;
	}

	public Renderable createRenderable(
			JRGenericPrintElement element,
			RepositoryContext repositoryContext) throws JRException
	{
		Renderable cacheRenderer = null;

		if (element.getParameterValue(CVPrintElement.CONFIGURATION) != null)
		{
			JasperReportsContext jasperReportsContext = repositoryContext.getJasperReportsContext();
			JRPropertiesUtil propUtil = JRPropertiesUtil.getInstance(jasperReportsContext);

			boolean renderAsPng = CVUtils.isRenderAsPng(element);
			String cacheKey = !renderAsPng ? CVPrintElement.PARAMETER_SVG_CACHE_RENDERER : CVPrintElement.PARAMETER_PNG_CACHE_RENDERER;

			cacheRenderer = (Renderable) element.getParameterValue(cacheKey);
			if (cacheRenderer == null)
			{
				try
				{
					byte[] imageData = cvElementImageDataProvider.getImageData(repositoryContext, element);

					if (renderAsPng)
					{
						cacheRenderer = new SimpleDataRenderer(imageData, null);
					}
					else
					{
						SimpleRenderToImageAwareDataRenderer renderer =
								new SimpleRenderToImageAwareDataRenderer(imageData, null);


						renderer.setMinDPI(propUtil.getIntegerProperty(element, CVConstants.CV_PNG_MIN_DPI, CVConstants.CV_PNG_MIN_DPI_DEFAULT_VALUE));
						renderer.setAntiAlias(propUtil.getBooleanProperty(element, CVConstants.CV_PNG_ANTIALIAS, CVConstants.CV_PNG_ANTIALIAS_DEFAULT_VALUE));
						cacheRenderer = renderer;
					}

				}
				catch (Exception e)
				{
					if (log.isErrorEnabled())
					{
						log.error("Generating image for Custom Visualization element " + CVUtils.getElementId(element) + " failed.", e);
					}

					cacheRenderer = RendererUtil.getInstance(jasperReportsContext).handleImageError(e,
							element.getParameterValue(CVPrintElement.PARAMETER_ON_ERROR_TYPE) == null
									? CVPrintElement.DEFAULT_ON_ERROR_TYPE
									: OnErrorTypeEnum.getByName((String) element.getParameterValue(CVPrintElement.PARAMETER_ON_ERROR_TYPE))
					);
				}

				element.setParameterValue(cacheKey, cacheRenderer);
			}
		}

		return cacheRenderer;
	}

}
