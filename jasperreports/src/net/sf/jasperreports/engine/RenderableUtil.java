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
package net.sf.jasperreports.engine;

import java.awt.Image;
import java.awt.Transparency;
import java.awt.image.ColorModel;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.InputStream;
import java.net.URL;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.sf.jasperreports.engine.type.ImageTypeEnum;
import net.sf.jasperreports.engine.type.OnErrorTypeEnum;
import net.sf.jasperreports.engine.util.JRImageLoader;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.renderers.DataRenderable;
import net.sf.jasperreports.renderers.DimensionRenderable;
import net.sf.jasperreports.renderers.Graphics2DRenderable;
import net.sf.jasperreports.renderers.RenderersCache;
import net.sf.jasperreports.renderers.ResourceRenderer;
import net.sf.jasperreports.renderers.WrappingDataToGraphics2DRenderer;
import net.sf.jasperreports.renderers.WrappingDeprecatedRenderable;
import net.sf.jasperreports.renderers.WrappingRenderToImageDataRenderer;
import net.sf.jasperreports.renderers.util.RendererUtil;
import net.sf.jasperreports.repo.RepositoryUtil;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @deprecated Replaced by {@link RendererUtil}.
 */
public class RenderableUtil
{
	
	private static final Log log = LogFactory.getLog(RenderableUtil.class);
	
	public static final String EXCEPTION_MESSAGE_KEY_IMAGE_ERROR = "engine.renderable.util.image.error";
	
	/**
	 *
	 */
	private JasperReportsContext jasperReportsContext;

	/**
	 *
	 */
	private RenderableUtil(JasperReportsContext jasperReportsContext)
	{
		this.jasperReportsContext = jasperReportsContext;
	}


	/**
	 *
	 */
	public static RenderableUtil getInstance(JasperReportsContext jasperReportsContext)
	{
		return new RenderableUtil(jasperReportsContext);
	}


	/**
	 *
	 */
	public Renderable getRenderable(byte[] imageData)
	{
		return new JRImageRenderer(imageData);
	}


	/**
	 * @deprecated Replaced by {@link ResourceRenderer#getInstance(String, boolean)}.
	 */
	public Renderable getRenderable(String imageLocation) throws JRException
	{
		return getRenderable(imageLocation, OnErrorTypeEnum.ERROR, true);
	}


	/**
	 * @deprecated Replaced by {@link ResourceRenderer#getInstance(String, boolean)}.
	 */
	public Renderable getRenderable(String imageLocation, OnErrorTypeEnum onErrorType) throws JRException
	{
		return getRenderable(imageLocation, onErrorType, true);
	}


	/**
	 * @deprecated Replaced by {@link ResourceRenderer#getInstance(String, boolean)} and {@link RendererUtil#getNonLazyRenderable(String, OnErrorTypeEnum)}.
	 */
	public Renderable getRenderable(String imageLocation, OnErrorTypeEnum onErrorType, boolean isLazy) throws JRException
	{
		if (imageLocation == null)
		{
			return null;
		}

		if (isLazy)
		{
			return new JRImageRenderer(imageLocation);
		}

		Renderable result;
		try
		{
			byte[] data = RepositoryUtil.getInstance(jasperReportsContext).getBytesFromLocation(imageLocation);
			result = new JRImageRenderer(data);
		}
		catch (Exception e)
		{
			result = handleImageError(e, onErrorType);
			
			if (log.isDebugEnabled())
			{
				log.debug("handled image error with type " + onErrorType + " for location " + imageLocation, e);
			}
		}
		return result;
	}

	
	/**
	 *
	 */
	public Renderable getRenderable(Image img, OnErrorTypeEnum onErrorType) throws JRException
	{
		ImageTypeEnum type = ImageTypeEnum.JPEG;
		if (img instanceof RenderedImage)
		{
			ColorModel colorModel = ((RenderedImage) img).getColorModel();
			//if the image has transparency, encode as PNG
			if (colorModel.hasAlpha() 
					&& colorModel.getTransparency() != Transparency.OPAQUE)
			{
				type = ImageTypeEnum.PNG;
			}
		}
		
		return getRenderable(img, type, onErrorType);
	}


	/**
	 * Creates and returns an instance of the JRImageRenderer class after encoding the image object using an image
	 * encoder that supports the supplied image type.
	 * 
	 * @param image the java.awt.Image object to wrap into a JRImageRenderer instance
	 * @param imageType the type of the image as specified by one of the constants defined in the JRRenderable interface
	 * @param onErrorType one of the error type constants defined in the {@link OnErrorTypeEnum}.
	 * @return the image renderer instance
	 */
	public Renderable getRenderable(Image image, ImageTypeEnum imageType, OnErrorTypeEnum onErrorType) throws JRException
	{
		Renderable result;
		try
		{
			result = new JRImageRenderer(JRImageLoader.getInstance(jasperReportsContext).loadBytesFromAwtImage(image, imageType));
		}
		catch (Exception e)
		{
			result = handleImageError(e, onErrorType);
			
			if (log.isDebugEnabled())
			{
				log.debug("handled image error with type " + onErrorType, e);
			}
		}
		return result;
	}


	/**
	 *
	 */
	public Renderable getRenderable(InputStream is, OnErrorTypeEnum onErrorType) throws JRException
	{
		Renderable result;
		try
		{
			result = new JRImageRenderer(JRLoader.loadBytes(is));
		}
		catch (Exception e)
		{
			result = handleImageError(e, onErrorType); 
			
			if (log.isDebugEnabled())
			{
				log.debug("handled image error with type " + onErrorType, e);
			}
		}
		return result;
	}


	/**
	 *
	 */
	public Renderable getRenderable(URL url, OnErrorTypeEnum onErrorType) throws JRException
	{
		Renderable result;
		try
		{
			result = new JRImageRenderer(JRLoader.loadBytes(url));
		}
		catch (Exception e)
		{
			result = handleImageError(e, onErrorType); 
			
			if (log.isDebugEnabled())
			{
				log.debug("handled image error with type " + onErrorType + " for URL " + url, e);
			}
		}
		return result;
	}


	/**
	 *
	 */
	public Renderable getRenderable(File file, OnErrorTypeEnum onErrorType) throws JRException
	{
		Renderable result;
		try
		{
			result = new JRImageRenderer(JRLoader.loadBytes(file));
		}
		catch (Exception e)
		{
			result = handleImageError(e, onErrorType); 
			
			if (log.isDebugEnabled())
			{
				log.debug("handled image error with type " + onErrorType + " for file " + file, e);
			}
		}
		return result;
	}


	/**
	 * @deprecated To be removed.
	 */
	public Renderable getOnErrorRendererForDimension(Renderable renderer, OnErrorTypeEnum onErrorType) throws JRException
	{
		Renderable result;
		try
		{
			renderer.getDimension(jasperReportsContext);
			result = renderer;
		}
		catch (Exception e)
		{
			result = handleImageError(e, onErrorType);
			
			if (log.isDebugEnabled())
			{
				log.debug("handled image error with type " + onErrorType, e);
			}
		}
		return result;
	}

	/**
	 * @deprecated To be removed.
	 */
	public net.sf.jasperreports.renderers.Renderable getOnErrorRendererForDimension(
		RenderersCache renderersCache,
		net.sf.jasperreports.renderers.Renderable renderer, 
		OnErrorTypeEnum onErrorType
		) throws JRException
	{
		net.sf.jasperreports.renderers.Renderable result = null;
		
		DimensionRenderable dimensionRenderer = renderersCache.getDimensionRenderable(renderer);
		
		if (dimensionRenderer != null)
		{
			try
			{
				dimensionRenderer.getDimension(jasperReportsContext);
				result = (net.sf.jasperreports.renderers.Renderable)dimensionRenderer;
			}
			catch (Exception e)
			{
				result = handleImageError(e, onErrorType);
				
				if (log.isDebugEnabled())
				{
					log.debug("handled image error with type " + onErrorType, e);
				}
			}
		}
		return result;
	}

	/**
	 * @deprecated To be removed.
	 */
	public Renderable getOnErrorRendererForImageData(Renderable renderer, OnErrorTypeEnum onErrorType) throws JRException
	{
		Renderable result;
		try
		{
			renderer.getImageData(jasperReportsContext);
			result = renderer;
		}
		catch (Exception e)
		{
			result = handleImageError(e, onErrorType); 
			
			if (log.isDebugEnabled())
			{
				log.debug("handled image error with type " + onErrorType, e);
			}
		}
		return result;
	}


	/**
	 * 
	 */
	public Renderable handleImageError(Exception error, OnErrorTypeEnum onErrorType) throws JRException
	{
		Renderable errorRenderable;
		if (error instanceof JRException)
		{
			errorRenderable = getOnErrorRenderer(onErrorType, (JRException) error);
		}
		else if (error instanceof JRRuntimeException)
		{
			errorRenderable = getOnErrorRenderer(onErrorType, (JRRuntimeException) error);
		}
		else if (error instanceof RuntimeException)
		{
			throw (RuntimeException) error;
		}
		else
		{
			// we shouldn't get here normally
			if (log.isDebugEnabled())
			{
				log.debug("got unexpected image exception of type " + error.getClass().getName(), error);
			}
			
			throw 
				new JRRuntimeException(
					EXCEPTION_MESSAGE_KEY_IMAGE_ERROR,
					(Object[])null,
					error);
		}
		return errorRenderable;
	}

	public Renderable getOnErrorRenderer(OnErrorTypeEnum onErrorType, JRException e) throws JRException
	{
		Renderable renderer = null;
		
		switch (onErrorType)
		{
			case ICON :
			{
				renderer = new JRImageRenderer(JRImageLoader.NO_IMAGE_RESOURCE);
				//FIXME cache these renderers
				break;
			}
			case BLANK :
			{
				break;
			}
			case ERROR :
			default :
			{
				throw e;
			}
		}

		return renderer;
	}

	public Renderable getOnErrorRenderer(OnErrorTypeEnum onErrorType, JRRuntimeException e) throws JRRuntimeException
	{
		Renderable renderer = null;
		
		switch (onErrorType)
		{
			case ICON :
			{
				renderer = new JRImageRenderer(JRImageLoader.NO_IMAGE_RESOURCE);
				//FIXME cache these renderers
				break;
			}
			case BLANK :
			{
				break;
			}
			case ERROR :
			default :
			{
				throw e;
			}
		}

		return renderer;
	}


	/**
	 * @deprecated To be removed.
	 */
	public static Renderable getWrappingRenderable(JRRenderable deprecatedRenderer)
	{
		Renderable renderable = null;
		if (deprecatedRenderer != null)
		{
			renderable = deprecatedRenderer instanceof Renderable ? (Renderable)deprecatedRenderer : null;
			if (renderable == null)
			{
				renderable = new net.sf.jasperreports.engine.util.WrappingRenderable(deprecatedRenderer);
			}
		}
		return renderable;
	}


	/**
	 * @deprecated To be removed.
	 */
	public static Renderable getWrappingRenderable(JRImageMapRenderer deprecatedRenderer)
	{
		Renderable renderable = null;
		if (deprecatedRenderer != null)
		{
			renderable = deprecatedRenderer instanceof Renderable ? (Renderable)deprecatedRenderer : null;
			if (renderable == null)
			{
				renderable = new net.sf.jasperreports.engine.util.WrappingImageMapRenderable(deprecatedRenderer);
			}
		}
		return renderable;
	}


	/**
	 * @deprecated To be removed.
	 */
	public static Renderable getWrappingRenderable(net.sf.jasperreports.renderers.Renderable renderable)
	{
		Renderable deprecatedRenderable = null;
		if (renderable != null)
		{
			deprecatedRenderable = renderable instanceof Renderable ? (Renderable)renderable : null;
			if (deprecatedRenderable == null)
			{
				Graphics2DRenderable grxRenderable = null;
				DataRenderable dataRenderable = null;

				DimensionRenderable dimensionRenderable = 
					renderable instanceof DimensionRenderable 
					? (DimensionRenderable)renderable
					: null;
					
				if (renderable instanceof Graphics2DRenderable)
				{
					if (renderable instanceof DataRenderable)
					{
						grxRenderable = (Graphics2DRenderable)renderable;
						dataRenderable = (DataRenderable)renderable;
					}
					else
					{
						grxRenderable = (Graphics2DRenderable)renderable;
						dataRenderable = 
							new WrappingRenderToImageDataRenderer(
								grxRenderable,
								dimensionRenderable, 
								null
								);
					}
				}
				else
				{
					if (renderable instanceof DataRenderable)
					{
						dataRenderable = (DataRenderable)renderable;
						grxRenderable = new WrappingDataToGraphics2DRenderer((DataRenderable)renderable);
					}
					else
					{
						throw 
							new JRRuntimeException(
								RendererUtil.EXCEPTION_MESSAGE_KEY_RENDERABLE_MUST_IMPLEMENT_INTERFACE,
								new Object[]{
									renderable.getClass().getName(),
									DataRenderable.class.getName() 
										+ " or " + Graphics2DRenderable.class.getName() 
									}
								);
					}
				}
				
				if (dimensionRenderable == null)
				{
					dimensionRenderable = 
						renderable instanceof DimensionRenderable 
						? (DimensionRenderable)grxRenderable
						: null;
				}
				
				deprecatedRenderable = 
					new WrappingDeprecatedRenderable(
						renderable.getId(),
						grxRenderable, 
						dataRenderable, 
						dimensionRenderable
						);
			}
		}
		return deprecatedRenderable;
	}
}
