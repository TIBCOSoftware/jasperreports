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
package net.sf.jasperreports.engine.xml;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import org.w3c.tools.codec.Base64Decoder;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRPrintImage;
import net.sf.jasperreports.renderers.SimpleDataRenderer;
import net.sf.jasperreports.renderers.Renderable;
import net.sf.jasperreports.renderers.ResourceRenderer;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public class JRPrintImageSourceObject
{
	public static final String EXCEPTION_MESSAGE_KEY_DECODING_ERROR = "xml.print.image.decoding.error";


	/**
	 *
	 */
	private JRPrintImage printImage;

	/**
	 *
	 */
	private final boolean isLazy;
	private boolean isEmbedded;


	/**
	 * @deprecated Replaced by {@link #JRPrintImageSourceObject(boolean)}.
	 */
	public JRPrintImageSourceObject()
	{
		this(false);
	}
	

	/**
	 *
	 */
	public JRPrintImageSourceObject(boolean isLazy)
	{
		this.isLazy = isLazy;
	}
	

	/**
	 *
	 */
	public void setPrintImage(JRPrintImage printImage)
	{
		this.printImage = printImage;
	}
	

	/**
	 *
	 */
	public void setEmbedded(boolean isEmbedded)
	{
		this.isEmbedded = isEmbedded;
	}
	

	/**
	 *
	 */
	public void setImageSource(String imageSource) throws JRException
	{
		Renderable renderable = null;
		
		if (isLazy)
		{
			renderable = ResourceRenderer.getInstance(imageSource, true);
		}
		else
		{
			if (isEmbedded)
			{
				try
				{
					ByteArrayInputStream bais = new ByteArrayInputStream(imageSource.getBytes("UTF-8"));//FIXMENOW other encodings ?
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					
					Base64Decoder decoder = new Base64Decoder(bais, baos);
					decoder.process();
					
					renderable = SimpleDataRenderer.getInstance(baos.toByteArray());//, JRImage.ON_ERROR_TYPE_ERROR));
				}
				catch (Exception e)
				{
					throw 
						new JRException(
							EXCEPTION_MESSAGE_KEY_DECODING_ERROR,
							null,
							e);
				}
			}
			else
			{
				renderable = ResourceRenderer.getInstance(imageSource, false);
			}
		}

		printImage.setRenderer(renderable);
	}
	

}
