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
public class SimpleFontEntry implements FontEntry
{

	/**
	 * 
	 */
	private String name = null;
	private String normal = null;
	private String bold = null;
	private String italic = null;
	private String boldItalic = null;
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
		return normal;
	}
	
	/**
	 * 
	 */
	public void setNormal(String normal)
	{
		this.normal = normal;
	}
	
	/**
	 * 
	 */
	public String getBold()
	{
		return bold;
	}
	
	/**
	 * 
	 */
	public void setBold(String bold)
	{
		this.bold = bold;
	}
	
	/**
	 * 
	 */
	public String getItalic()
	{
		return italic;
	}
	
	/**
	 * 
	 */
	public void setItalic(String italic)
	{
		this.italic = italic;
	}
	
	/**
	 * 
	 */
	public String getBoldItalic()
	{
		return boldItalic;
	}
	
	/**
	 * 
	 */
	public void setBoldItalic(String boldItalic)
	{
		this.boldItalic = boldItalic;
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
