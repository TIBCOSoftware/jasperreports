/*
 * ============================================================================
 * GNU Lesser General Public License
 * ============================================================================
 *
 * JasperReports - Free Java report-generating library.
 * Copyright (C) 2001-2006 JasperSoft Corporation http://www.jaspersoft.com
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
package net.sf.jasperreports.engine.fonts;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.IOException;
import java.io.InputStream;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.util.JRLoader;



/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id: JRChart.java 2469 2008-11-19 15:12:30Z shertage $
 */
public class SimpleFontFace implements FontFace
{

	/**
	 * 
	 */
	private String file = null;
	private Font font = null;
	
	/**
	 * 
	 */
	public static SimpleFontFace createInstance(String file)
	{
		SimpleFontFace fontFace = null;
		if (file != null)
		{
			fontFace = new SimpleFontFace(file);
		}
		return fontFace;
	}
	
	/**
	 * 
	 */
	private SimpleFontFace(String file)
	{
		this.file = file;

		InputStream is = null;
		try
		{
			is = JRLoader.getLocationInputStream(file);
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
