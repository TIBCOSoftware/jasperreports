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

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

import net.sf.jasperreports.engine.JRException;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JRSaver
{


	/**
	 *
	 */
	public static void saveObject(
		Object obj, 
		String fileName
		) throws JRException
	{
		saveObject( obj, new File(fileName) );
	}


	/**
	 *
	 */
	public static void saveObject(
		Object obj, 
		File file
		) throws JRException
	{
		FileOutputStream fos = null;
		ObjectOutputStream oos = null;

		try
		{
			fos = new FileOutputStream(file);
			BufferedOutputStream bos = new BufferedOutputStream(fos);
			oos = new ObjectOutputStream(bos);
			oos.writeObject(obj);
			oos.flush();
			bos.flush();
			fos.flush();
		}
		catch (IOException e)
		{
			throw new JRException("Error saving file : " + file, e);
		}
		finally
		{
			if (oos != null)
			{
				try
				{
					oos.close();
				}
				catch(IOException e)
				{
				}
			}

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
		}
	}


	/**
	 *
	 */
	public static void saveObject(
		Object obj, 
		OutputStream os
		) throws JRException
	{
		ObjectOutputStream oos = null;

		try
		{
			oos = new ObjectOutputStream(os);
			oos.writeObject(obj);
			oos.flush();
		}
		catch (IOException e)
		{
			throw new JRException("Error saving object to OutputStream", e);
		}
		finally
		{
			//FIXME should not close the stream
			if (oos != null)
			{
				try
				{
					oos.close();
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
	public static void saveClassSource(
		String source, 
		File file
		) throws JRException
	{
		FileWriter fwriter = null;

		try
		{
			fwriter = new FileWriter(file);
			BufferedWriter bufferedWriter = new BufferedWriter(fwriter);
			bufferedWriter.write(source);
			bufferedWriter.flush();
			fwriter.flush();
		}
		catch (IOException e)
		{
			throw new JRException("Error saving expressions class file : " + file, e);
		}
		finally
		{
			if (fwriter != null)
			{
				try
				{
					fwriter.close();
				}
				catch(IOException e)
				{
				}
			}
		}
	}


}
