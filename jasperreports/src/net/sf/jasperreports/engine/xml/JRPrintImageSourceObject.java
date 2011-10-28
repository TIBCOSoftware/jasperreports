/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2011 Jaspersoft Corporation. All rights reserved.
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

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRImageRenderer;
import net.sf.jasperreports.engine.JRPrintImage;

import org.w3c.tools.codec.Base64Decoder;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JRPrintImageSourceObject
{


	/**
	 *
	 */
	private JRPrintImage printImage;

	/**
	 *
	 */
	private boolean isEmbedded;


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
		if (isEmbedded)
		{
			try
			{
				ByteArrayInputStream bais = new ByteArrayInputStream(imageSource.getBytes("UTF-8"));//FIXMENOW other encodings ?
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				
				Base64Decoder decoder = new Base64Decoder(bais, baos);
				decoder.process();
				
				printImage.setRenderer(JRImageRenderer.getInstance(baos.toByteArray()));//, JRImage.ON_ERROR_TYPE_ERROR));
			}
			catch (Exception e)
			{
				throw new JRException("Error decoding embedded image.", e);
			}
		}
		else
		{
			printImage.setRenderer(JRImageRenderer.getInstance(imageSource));
		}
	}
	

}
