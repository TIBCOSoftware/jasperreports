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
package net.sf.jasperreports.engine.fonts;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.IOException;
import java.io.InputStream;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.repo.RepositoryUtil;



/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class SimpleFontFace implements FontFace
{

	/**
	 * 
	 */
	private String file;
	private Font font;
	
	/**
	 * 
	 */
	public SimpleFontFace(String file)
	{
		this.file = file;

		InputStream is = null;
		try
		{
			is = RepositoryUtil.getInputStream(file);
		}
		catch(JRException e)
		{
			throw new JRRuntimeException(e);
		}
		
		try
		{
			font = Font.createFont(Font.TRUETYPE_FONT, is);
		}
		catch(FontFormatException e)
		{
			throw new JRRuntimeException(e);
		}
		catch(IOException e)
		{
			throw new JRRuntimeException(e);
		}
		finally
		{
			try
			{
				is.close();
			}
			catch (IOException e)
			{
			}
		}
	}
	
	/**
	 * 
	 */
	public SimpleFontFace(Font font)
	{
		this.font = font;
	}
	
	/**
	 * 
	 */
	public String getName()
	{
		//(String)font.getAttributes().get(TextAttribute.FAMILY);
		return font.getName();
	}
	
	/**
	 * 
	 */
	public String getFile()
	{
		return file;
	}
	
	/**
	 * 
	 */
	public Font getFont()
	{
		return font;
	}
	
}
