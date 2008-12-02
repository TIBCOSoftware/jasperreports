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
public interface FontFamily
{

	/**
	 * 
	 */
	public String getName();
	
	/**
	 * 
	 */
	public void setName(String name);
	
	/**
	 * 
	 */
	public String getNormal();
	
	/**
	 * 
	 */
	public void setNormal(String normal);
	
	/**
	 * 
	 */
	public String getBold();
	
	/**
	 * 
	 */
	public void setBold(String bold);
	
	/**
	 * 
	 */
	public String getItalic();
	
	/**
	 * 
	 */
	public void setItalic(String italic);
	
	/**
	 * 
	 */
	public String getBoldItalic();
	
	/**
	 * 
	 */
	public void setBoldItalic(String boldItalic);
	
	/**
	 * 
	 */
	public FontFace getNormalFace();
	
	/**
	 * 
	 */
	public void setNormalFace(FontFace normal);
	
	/**
	 * 
	 */
	public FontFace getBoldFace();
	
	/**
	 * 
	 */
	public void setBoldFace(FontFace bold);
	
	/**
	 * 
	 */
	public FontFace getItalicFace();
	
	/**
	 * 
	 */
	public void setItalicFace(FontFace italic);
	
	/**
	 * 
	 */
	public FontFace getBoldItalicFace();
	
	/**
	 * 
	 */
	public void setBoldItalicFace(FontFace boldItalic);
	
	/**
	 * 
	 */
	public boolean isSimulatedBold();
	
	/**
	 * 
	 */
	public void setSimulatedBold(boolean isSimulatedBold);
	
	/**
	 * 
	 */
	public boolean isSimulatedItalic();
	
	/**
	 * 
	 */
	public void setSimulatedItalic(boolean isSimulatedItalic);
	
	/**
	 * 
	 */
	public String getPdfEncoding();
	
	/**
	 * 
	 */
	public void setPdfEncoding(String pdfEncoding);
	
	/**
	 * 
	 */
	public boolean isPdfEmbedded();
	
	/**
	 * 
	 */
	public void setPdfEmbedded(boolean isPdfEmbedded);
	
}
