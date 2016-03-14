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
package net.sf.jasperreports.renderers.util;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Transparency;
import java.awt.image.ColorModel;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.InputStream;
import java.net.URL;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.type.ImageTypeEnum;
import net.sf.jasperreports.engine.type.OnErrorTypeEnum;
import net.sf.jasperreports.engine.util.JRImageLoader;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.engine.util.JRTypeSniffer;
import net.sf.jasperreports.renderers.DataRenderable;
import net.sf.jasperreports.renderers.Graphics2DRenderable;
import net.sf.jasperreports.renderers.Renderable;
import net.sf.jasperreports.renderers.RenderersCache;
import net.sf.jasperreports.renderers.ResourceRenderer;
import net.sf.jasperreports.renderers.SimpleDataRenderer;
import net.sf.jasperreports.renderers.WrappingRenderToImageDataRenderer;
import net.sf.jasperreports.repo.RepositoryUtil;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public class RendererUtil
{
	
	private static final Log log = LogFactory.getLog(RendererUtil.class);
	
	public static final String EXCEPTION_MESSAGE_KEY_IMAGE_ERROR = "engine.renderable.util.image.error";
	public static final String EXCEPTION_MESSAGE_KEY_RENDERABLE_MUST_IMPLEMENT_INTERFACE = "engine.renderable.must.implement.interface";
	
	public static final Renderable NO_IMAGE_RENDERER = ResourceRenderer.getInstance(JRImageLoader.NO_IMAGE_RESOURCE, false);//FIXMEIMAGE consider moving constant from loader to here 

	public static final String SVG_MIME_TYPE = "image/svg+xml";
	public static final String SVG_FILE_EXTENSION = "svg";

	/**
	 *
	 */
	private final JasperReportsContext jasperReportsContext;
	private SvgDataSniffer svgDataSniffer;

	/**
	 *
	 */
	private RendererUtil(JasperReportsContext jasperReportsContext)
	{
		this.jasperReportsContext = jasperReportsContext;
	}


	/**
	 *
	 */
	public static RendererUtil getInstance(JasperReportsContext jasperReportsContext)
	{
		return new RendererUtil(jasperReportsContext);
	}


	/**
	 *
	 */
	public boolean isSvgData(byte[] data)
	{
		if (JRTypeSniffer.getImageTypeValue(data) == ImageTypeEnum.UNKNOWN)
		{
			if (XmlDataSniffer.isXmlData(data))
			{
				return getSvgDataSniffer().isSvgData(data);
			}
		}
		
		return false;
	}


	/**
	 *
	 */
	public boolean isSvgData(DataRenderable dataRenderable) throws JRException
	{
		return isSvgData(dataRenderable.getData(jasperReportsContext));
	}


	/**
	 *
	 */
	public SvgDataSniffer getSvgDataSniffer()
	{
		if (svgDataSniffer == null)
		{
			svgDataSniffer = SvgDataSniffer.getInstance(jasperReportsContext);
		}
		return svgDataSniffer;
	}


	/**
	 *
	 */
	public Renderable getNonLazyRenderable(String resourceLocation, OnErrorTypeEnum onErrorType) throws JRException
	{
		byte[] data;

		try
		{
			data = RepositoryUtil.getInstance(jasperReportsContext).getBytesFromLocation(resourceLocation);
		}
		catch (Exception e)
		{
			if (log.isDebugEnabled())
			{
				log.debug("handled image error with type " + onErrorType + " for location " + resourceLocation, e);
			}
			
			return handleImageError(e, onErrorType); 
		}
		
		return SimpleDataRenderer.getInstance(data);
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
		byte[] data = null;
		try
		{
			data = JRImageLoader.getInstance(jasperReportsContext).loadBytesFromAwtImage(image, imageType);
		}
		catch (Exception e)
		{
			if (log.isDebugEnabled())
			{
				log.debug("handled image error with type " + onErrorType, e);
			}

			return handleImageError(e, onErrorType);
		}
		return SimpleDataRenderer.getInstance(data);
	}


	/**
	 *
	 */
	public Renderable getRenderable(InputStream is, OnErrorTypeEnum onErrorType) throws JRException
	{
		byte[] data = null;
		try
		{
			data = JRLoader.loadBytes(is);
		}
		catch (Exception e)
		{
			if (log.isDebugEnabled())
			{
				log.debug("handled image error with type " + onErrorType, e);
			}

			return handleImageError(e, onErrorType); 
		}
		return SimpleDataRenderer.getInstance(data);
	}


	/**
	 *
	 */
	public Renderable getRenderable(URL url, OnErrorTypeEnum onErrorType) throws JRException
	{
		byte[] data = null;
		try
		{
			data = JRLoader.loadBytes(url);
		}
		catch (Exception e)
		{
			if (log.isDebugEnabled())
			{
				log.debug("handled image error with type " + onErrorType + " for URL " + url, e);
			}

			return handleImageError(e, onErrorType); 
		}
		return SimpleDataRenderer.getInstance(data);
	}


	/**
	 *
	 */
	public Renderable getRenderable(File file, OnErrorTypeEnum onErrorType) throws JRException
	{
		byte[] data = null;
		try
		{
			data = JRLoader.loadBytes(file);
		}
		catch (Exception e)
		{
			if (log.isDebugEnabled())
			{
				log.debug("handled image error with type " + onErrorType + " for file " + file, e);
			}

			return handleImageError(e, onErrorType); 
		}
		return SimpleDataRenderer.getInstance(data);
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
					error
					);
		}
		return errorRenderable;
	}

	/**
	 *
	 */
	public Renderable getOnErrorRenderer(OnErrorTypeEnum onErrorType, JRException e) throws JRException
	{
		Renderable renderer = null;
		
		switch (onErrorType)
		{
			case ICON :
			{
				renderer = NO_IMAGE_RENDERER;
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
	 *
	 */
	public Renderable getOnErrorRenderer(OnErrorTypeEnum onErrorType, JRRuntimeException e) throws JRRuntimeException
	{
		Renderable renderer = null;
		
		switch (onErrorType)
		{
			case ICON :
			{
				renderer = NO_IMAGE_RENDERER;
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
	 *
	 */
	public static boolean isLazy(Renderable renderer)
	{
		boolean isLazy = false;

		if (renderer instanceof ResourceRenderer)
		{
			isLazy = ((ResourceRenderer)renderer).isLazy();
		}
		else if (renderer != null)
		{
			@SuppressWarnings("deprecation")
			Class<?> depClass = net.sf.jasperreports.engine.JRImageRenderer.class;
			if (depClass.equals(renderer.getClass()))
			{
				@SuppressWarnings("deprecation")
				net.sf.jasperreports.engine.JRImageRenderer depRenderer = (net.sf.jasperreports.engine.JRImageRenderer)renderer;
				@SuppressWarnings("deprecation")
				String depImageLocation = depRenderer.getImageLocation();
				isLazy = depImageLocation != null;
			}
		}
		
		return isLazy;
	}


	/**
	 *
	 */
	public static String getResourceLocation(Renderable renderer)
	{
		String resourceLocation = null;

		if (renderer instanceof ResourceRenderer)
		{
			resourceLocation = ((ResourceRenderer)renderer).getResourceLocation();
		}
		else if (renderer != null)
		{
			@SuppressWarnings("deprecation")
			Class<?> depClass = net.sf.jasperreports.engine.JRImageRenderer.class;
			if (depClass.equals(renderer.getClass()))
			{
				@SuppressWarnings("deprecation")
				net.sf.jasperreports.engine.JRImageRenderer depRenderer = (net.sf.jasperreports.engine.JRImageRenderer)renderer;
				@SuppressWarnings("deprecation")
				String depImageLocation = depRenderer.getImageLocation();
				resourceLocation = depImageLocation;
			}
		}
		
		return resourceLocation;
	}

	
	/**
	 * 
	 */
	public DataRenderable getDataRenderable(
		Renderable renderer, 
		Dimension dimension, 
		Color backcolor
		) throws JRException
	{
		DataRenderable imageRenderer = null;
		
		if (renderer != null)
		{
			if (renderer instanceof DataRenderable)
			{
				imageRenderer = (DataRenderable)renderer;
			}
			else
			{
				Graphics2DRenderable grxRenderer = null;
				
				if (renderer instanceof Graphics2DRenderable)
				{
					grxRenderer = (Graphics2DRenderable)renderer;
				}
				else
				{
					throw 
						new JRException(
							EXCEPTION_MESSAGE_KEY_RENDERABLE_MUST_IMPLEMENT_INTERFACE,
							new Object[]{
								renderer.getClass().getName(),
								DataRenderable.class.getName() 
									+ " or " + Graphics2DRenderable.class.getName() 
								}
							);
				}

				imageRenderer =
					new WrappingRenderToImageDataRenderer(
						grxRenderer,
						dimension,
						backcolor
						);
			}
		}
			
		return imageRenderer;
	}

	
	/**
	 * 
	 */
	public DataRenderable getImageDataRenderable(
		RenderersCache renderersCache,
		Renderable renderer, 
		Dimension dimension, 
		Color backcolor
		) throws JRException
	{
		DataRenderable imageRenderer = null;
		
		if (renderer != null)
		{
			if (renderer instanceof DataRenderable)
			{
				boolean isSvgData = isSvgData((DataRenderable)renderer);
				if (isSvgData)
				{
					imageRenderer =
						new WrappingRenderToImageDataRenderer(
							(Graphics2DRenderable)renderersCache.getWrappingRenderable(renderer.getId(), (DataRenderable)renderer), 
							dimension, 
							backcolor
							);
				}
				else
				{
					imageRenderer = (DataRenderable)renderer;
				}
			}
			else
			{
				Graphics2DRenderable grxRenderer = null;
				
				if (renderer instanceof Graphics2DRenderable)
				{
					grxRenderer = (Graphics2DRenderable)renderer;
				}
				else
				{
					throw 
						new JRException(
							EXCEPTION_MESSAGE_KEY_RENDERABLE_MUST_IMPLEMENT_INTERFACE,
							new Object[]{
								renderer.getClass().getName(),
								DataRenderable.class.getName() 
									+ " or " + Graphics2DRenderable.class.getName() 
								}
							);
				}

				imageRenderer =
					new WrappingRenderToImageDataRenderer(
						grxRenderer,
						dimension,
						backcolor
						);
			}
		}
			
		return imageRenderer;
	}
}
