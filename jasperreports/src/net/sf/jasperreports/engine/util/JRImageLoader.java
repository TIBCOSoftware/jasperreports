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
package net.sf.jasperreports.engine.util;

import java.awt.Image;

import net.sf.jasperreports.engine.DefaultJasperReportsContext;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRPropertiesUtil;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.type.ImageTypeEnum;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public final class JRImageLoader
{


	/**
	 * Configuration property specifying the name of the class implementing the {@link JRImageReader} interface
	 * to be used by the engine. If not set, the engine will try to an image reader implementation that corresponds to the JVM version.
	 */
	public static final String PROPERTY_IMAGE_READER = JRPropertiesUtil.PROPERTY_PREFIX + "image.reader";

	/**
	 * Configuration property specifying the name of the class implementing the {@link JRImageEncoder} interface
	 * to be used by the engine. If not set, the engine will try to an image encoder implementation that corresponds to the JVM version.
	 */
	public static final String PROPERTY_IMAGE_ENCODER = JRPropertiesUtil.PROPERTY_PREFIX + "image.encoder";

	public static final String NO_IMAGE_RESOURCE = "net/sf/jasperreports/engine/images/image-16.png";
	public static final String SUBREPORT_IMAGE_RESOURCE = "net/sf/jasperreports/engine/images/subreport-16.png";
	public static final String CHART_IMAGE_RESOURCE = "net/sf/jasperreports/engine/images/chart-16.png";
	public static final String CROSSTAB_IMAGE_RESOURCE = "net/sf/jasperreports/engine/images/crosstab-16.png";
	public static final String COMPONENT_IMAGE_RESOURCE = "net/sf/jasperreports/engine/images/component-16.png";

	/**
	 *
	 */
	private JRImageReader imageReader;
	private JRImageEncoder imageEncoder;
	private JasperReportsContext jasperReportsContext;


	/**
	 *
	 */
	private JRImageLoader(JasperReportsContext jasperReportsContext)
	{
		this.jasperReportsContext = jasperReportsContext;
		
		init();
	}
	
	
	/**
	 *
	 */
	private static JRImageLoader getDefaultInstance()
	{
		return new JRImageLoader(DefaultJasperReportsContext.getInstance());
	}
	
	
	/**
	 *
	 */
	public static JRImageLoader getInstance(JasperReportsContext jasperReportsContext)
	{
		return new JRImageLoader(jasperReportsContext);
	}
	
	
	private void init()
	{
		String readerClassName = JRPropertiesUtil.getInstance(jasperReportsContext).getProperty(PROPERTY_IMAGE_READER);
		if (readerClassName == null)
		{
			imageReader = new JRJdk14ImageReader();
		}
		else
		{
			try 
			{
				Class<?> clazz = JRClassLoader.loadClassForRealName(readerClassName);	
				imageReader = (JRImageReader) clazz.newInstance();
			}
			catch (Exception e)
			{
				throw new JRRuntimeException(e);
			}
		}


		String encoderClassName = JRPropertiesUtil.getInstance(jasperReportsContext).getProperty(PROPERTY_IMAGE_ENCODER);
		if (encoderClassName == null)
		{
			imageEncoder = new JRJdk14ImageEncoder();
		}
		else
		{
			try 
			{
				Class<?> clazz = JRClassLoader.loadClassForRealName(encoderClassName);	
				imageEncoder = (JRImageEncoder) clazz.newInstance();
			}
			catch (Exception e)
			{
				throw new JRRuntimeException(e);
			}
		}
	}


	/**
	 * @deprecated Replaced by {@link #loadBytesFromAwtImage(Image, ImageTypeEnum)}.
	 */
	public byte[] loadBytesFromAwtImage(Image image, byte imageType) throws JRException
	{
		return loadBytesFromAwtImage(image, ImageTypeEnum.getByValue(imageType));
	}


	/**
	 * Encoding the image object using an image encoder that supports the supplied image type.
	 * 
	 * @param image the java.awt.Image object to encode
	 * @param imageType the type of the image as specified by one of the constants defined in the JRRenderable interface
	 * @return the encoded image data
	 */
	public byte[] loadBytesFromAwtImage(Image image, ImageTypeEnum imageType) throws JRException
	{
		return imageEncoder.encode(image, imageType);
	}


	/**
	 *
	 */
	public Image loadAwtImageFromBytes(byte[] bytes) throws JRException
	{
		return imageReader.readImage(bytes);
	}


	/**
	 * @deprecated Replaced by {@link #loadBytesFromAwtImage(Image, byte)}.
	 */
	public static byte[] loadImageDataFromAWTImage(Image image, byte imageType) throws JRException
	{
		return getDefaultInstance().loadBytesFromAwtImage(image, imageType);
	}


	/**
	 * @deprecated Replaced by {@link #loadAwtImageFromBytes(byte[])}.
	 */
	public static Image loadImage(byte[] bytes) throws JRException
	{
		return getDefaultInstance().loadAwtImageFromBytes(bytes);
	}
}
