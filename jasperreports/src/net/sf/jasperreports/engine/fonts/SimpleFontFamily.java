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



/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id: JRChart.java 2469 2008-11-19 15:12:30Z shertage $
 */
public class SimpleFontFamily implements FontFamily
{

	/**
	 * 
	 */
	private String name = null;
	private FontFace normalFace = null;
	private FontFace boldFace = null;
	private FontFace italicFace = null;
	private FontFace boldItalicFace = null;
	private String pdfEncoding = null;
	private boolean isPdfEmbedded = false;
	private boolean isSimulatedBold = false;
	private boolean isSimulatedItalic = false;
	
	/**
	 * 
	 */
	public String getName()
	{
		return name;
	}
	
	/**
	 * 
	 */
	public void setName(String name)
	{
		this.name = name;
	}
	
	/**
	 * 
	 */
	public String getNormal()
	{
		return normalFace == null ? null : normalFace.getFile();
	}
	
	/**
	 * 
	 */
	public void setNormal(String normal)
	{
		normalFace = SimpleFontFace.createInstance(normal);
	}
	
	/**
	 * 
	 */
	public String getBold()
	{
		return boldFace == null ? null : boldFace.getFile();
	}
	
	/**
	 * 
	 */
	public void setBold(String bold)
	{
		boldFace = SimpleFontFace.createInstance(bold);
	}
	
	/**
	 * 
	 */
	public String getItalic()
	{
		return italicFace == null ? null : italicFace.getFile();
	}
	
	/**
	 * 
	 */
	public void setItalic(String italic)
	{
		italicFace = SimpleFontFace.createInstance(italic);
	}
	
	/**
	 * 
	 */
	public String getBoldItalic()
	{
		return boldItalicFace == null ? null : boldItalicFace.getFile();
	}
	
	/**
	 * 
	 */
	public void setBoldItalic(String boldItalic)
	{
		boldItalicFace = SimpleFontFace.createInstance(boldItalic);
	}

	/**
	 * 
	 */
	public FontFace getNormalFace()
	{
		return normalFace;
	}
	
	/**
	 * 
	 */
	public void setNormalFace(FontFace normalFace)
	{
		this.normalFace = normalFace;
	}
	
	/**
	 * 
	 */
	public FontFace getBoldFace()
	{
		return boldFace;
	}
	
	/**
	 * 
	 */
	public void setBoldFace(FontFace boldFace)
	{
		this.boldFace = boldFace;
	}
	
	/**
	 * 
	 */
	public FontFace getItalicFace()
	{
		return italicFace;
	}
	
	/**
	 * 
	 */
	public void setItalicFace(FontFace italicFace)
	{
		this.italicFace = italicFace;
	}
	
	/**
	 * 
	 */
	public FontFace getBoldItalicFace()
	{
		return boldItalicFace;
	}
	
	/**
	 * 
	 */
	public void setBoldItalicFace(FontFace boldItalicFace)
	{
		this.boldItalicFace = boldItalicFace;
	}
	
	/**
	 * 
	 */
	public boolean isSimulatedBold()
	{
		return isSimulatedBold;
	}
	
	/**
	 * 
	 */
	public void setSimulatedBold(boolean isSimulatedBold)
	{
		this.isSimulatedBold = isSimulatedBold;
	}
	
	/**
	 * 
	 */
	public boolean isSimulatedItalic()
	{
		return isSimulatedItalic;
	}
	
	/**
	 * 
	 */
	public void setSimulatedItalic(boolean isSimulatedItalic)
	{
		this.isSimulatedItalic = isSimulatedItalic;
	}
	
	/**
	 * 
	 */
	public String getPdfEncoding()
	{
		return pdfEncoding;
	}
	
	/**
	 * 
	 */
	public void setPdfEncoding(String pdfEncoding)
	{
		this.pdfEncoding = pdfEncoding;
	}
	
	/**
	 * 
	 */
	public boolean isPdfEmbedded()
	{
		return isPdfEmbedded;
	}
	
	/**
	 * 
	 */
	public void setPdfEmbedded(boolean isPdfEmbedded)
	{
		this.isPdfEmbedded = isPdfEmbedded;
	}
	
}
