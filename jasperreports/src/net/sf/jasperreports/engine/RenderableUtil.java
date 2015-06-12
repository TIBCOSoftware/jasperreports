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
import net.sf.jasperreports.repo.RepositoryUtil;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
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
	 *
	 */
	public Renderable getRenderable(String imageLocation) throws JRException
	{
		return getRenderable(imageLocation, OnErrorTypeEnum.ERROR, true);
	}


	/**
	 * 
	 */
	public Renderable getRenderable(String imageLocation, OnErrorTypeEnum onErrorType) throws JRException
	{
		return getRenderable(imageLocation, onErrorType, true);
	}


	/**
	 * 
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
	 *
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
	 *
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
	 *
	public Renderable getOnErrorRendererForImage(Renderable renderer, OnErrorTypeEnum onErrorType) throws JRException
	{
		try
		{
			renderer.getImage();
			return renderer;
		}
		catch (JRException e)
		{
			return getOnErrorRenderer(onErrorType, e); 
		}
	}
	*/

	protected Renderable handleImageError(Exception error, OnErrorTypeEnum onErrorType) throws JRException
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
}
