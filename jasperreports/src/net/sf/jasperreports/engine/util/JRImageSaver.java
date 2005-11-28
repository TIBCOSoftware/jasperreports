/*
 * ============================================================================
 * GNU Lesser General Public License
 * ============================================================================
 *
 * JasperReports - Free Java report-generating library.
 * Copyright (C) 2001-2005 JasperSoft Corporation http://www.jaspersoft.com
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307, USA.
 * 
 * JasperSoft Corporation
 * 303 Second Street, Suite 450 North
 * San Francisco, CA 94107
 * http://www.jaspersoft.com
 */
package net.sf.jasperreports.engine.util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import net.sf.jasperreports.engine.JRException;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JRImageSaver
{


	/**
	 *
	 */
	public static void saveImageDataToFile(byte[] imageData, File file) throws JRException
	{
		FileOutputStream fos = null;
		ByteArrayInputStream bais = null;

		try
		{
			fos = new FileOutputStream(file);
			bais = new ByteArrayInputStream(imageData);
			
			byte[] bytes = new byte[10000];
			int ln = 0;
			while ((ln = bais.read(bytes)) > 0)
			{
				fos.write(bytes, 0, ln);
			}
			
			fos.flush();
		}
		catch (IOException e)
		{
			throw new JRException("Error saving image data : " + file, e);
		}
		finally
		{
			if (fos != null)
			{
				try
				{
					fos.close();
				}
				catch(IOException e)
				{
				}
			}

			if (bais != null)
			{
				try
				{
					bais.close();
				}
				catch(IOException e)
				{
				}
			}
		}
	}


	/**
	 *
	 */
	public static void saveImageDataToOutputStream(byte[] imageData, OutputStream os) throws JRException
	{
		ByteArrayInputStream bais = null;

		try
		{
			bais = new ByteArrayInputStream(imageData);
			
			byte[] bytes = new byte[10000];
			int ln = 0;
			while ((ln = bais.read(bytes)) > 0)
			{
				os.write(bytes, 0, ln);
			}

			os.flush();
		}
		catch (IOException e)
		{
			throw new JRException("Error saving image data to output stream.", e);
		}
		finally
		{
			if (bais != null)
			{
				try
				{
					bais.close();
				}
				catch(IOException e)
				{
				}
			}
		}
	}


}
