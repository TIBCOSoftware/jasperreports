/*
 * ============================================================================
 *                   The JasperReports License, Version 1.0
 * ============================================================================
 * 
 * Copyright (C) 2001-2004 Teodor Danciu (teodord@users.sourceforge.net). All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 
 * 3. The end-user documentation included with the redistribution, if any, must
 *    include the following acknowledgment: "This product includes software
 *    developed by Teodor Danciu (http://jasperreports.sourceforge.net)."
 *    Alternately, this acknowledgment may appear in the software itself, if
 *    and wherever such third-party acknowledgments normally appear.
 * 
 * 4. The name "JasperReports" must not be used to endorse or promote products 
 *    derived from this software without prior written permission. For written 
 *    permission, please contact teodord@users.sourceforge.net.
 * 
 * 5. Products derived from this software may not be called "JasperReports", nor 
 *    may "JasperReports" appear in their name, without prior written permission
 *    of Teodor Danciu.
 * 
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS  FOR A PARTICULAR  PURPOSE ARE  DISCLAIMED.  IN NO  EVENT SHALL  THE
 * APACHE SOFTWARE  FOUNDATION  OR ITS CONTRIBUTORS  BE LIABLE FOR  ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL,  EXEMPLARY, OR CONSEQUENTIAL  DAMAGES (INCLU-
 * DING, BUT NOT LIMITED TO, PROCUREMENT  OF SUBSTITUTE GOODS OR SERVICES; LOSS
 * OF USE, DATA, OR  PROFITS; OR BUSINESS  INTERRUPTION)  HOWEVER CAUSED AND ON
 * ANY  THEORY OF LIABILITY,  WHETHER  IN CONTRACT,  STRICT LIABILITY,  OR TORT
 * (INCLUDING  NEGLIGENCE OR  OTHERWISE) ARISING IN  ANY WAY OUT OF THE  USE OF
 * THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

/*
 * ============================================================================
 *                   GNU Lesser General Public License
 * ============================================================================
 *
 * JasperReports - Free Java report-generating library.
 * Copyright (C) 2001-2004 Teodor Danciu teodord@users.sourceforge.net
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
 * Teodor Danciu
 * 173, Calea Calarasilor, Bl. 42, Sc. 1, Ap. 18
 * Postal code 030615, Sector 3
 * Bucharest, ROMANIA
 * Email: teodord@users.sourceforge.net
 */
package dori.jasper.engine.base;

import java.awt.font.TextAttribute;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import dori.jasper.engine.JRFont;
import dori.jasper.engine.JRReportFont;


/**
 *
 */
public class JRBaseFont implements JRFont, Serializable
{


	/**
	 *
	 */
	private static final long serialVersionUID = 501;

	/**
	 *
	 */
	private static final String DEFAULT_FONT_NAME = "sansserif";
	private static final String DEFAULT_PDF_FONT_NAME = "Helvetica";
	private static final String DEFAULT_PDF_ENCODING = "Cp1252";

	/**
	 *
	 */
	protected JRReportFont reportFont = null;
	protected String fontName = null;
	protected Boolean isBold = null;
	protected Boolean isItalic = null;
	protected Boolean isUnderline = null;
	protected Boolean isStrikeThrough = null;
	protected Integer size = null;
	protected String pdfFontName = null;
	protected String pdfEncoding = null;
	protected Boolean isPdfEmbedded = null;

	protected Map attributes = null;


	/**
	 *
	 */
	protected JRBaseFont()
	{
	}
		

	/**
	 *
	 */
	protected JRBaseFont(JRFont font, Map baseObjectsMap)
	{
		baseObjectsMap.put(font, this);

		fontName = font.getOwnFontName();
		isBold = font.isOwnBold();
		isItalic = font.isOwnItalic();
		isUnderline = font.isOwnUnderline();
		isStrikeThrough = font.isOwnStrikeThrough();
		size = font.getOwnSize();
		pdfFontName = font.getOwnPdfFontName();
		pdfEncoding = font.getOwnPdfEncoding();
		isPdfEmbedded = font.isOwnPdfEmbedded();

		reportFont = JRBaseObjectFactory.getReportFont(font.getReportFont(), baseObjectsMap);
	}
		

	/**
	 *
	 */
	public JRReportFont getReportFont()
	{
		return this.reportFont;
	}
	

	/**
	 *
	 */
	public String getFontName()
	{
		String ret = null;

		if (this.fontName != null)
		{
			ret = this.fontName;
		}
		else
		{
			if (this.reportFont != null)
			{
				ret = this.reportFont.getFontName();
			}
			else
			{
				ret = DEFAULT_FONT_NAME;
			}
		}
		
		return ret;
	}
	
	/**
	 *
	 */
	public String getOwnFontName()
	{
		return this.fontName;
	}
	
	/**
	 *
	 */
	public void setFontName(String fontName)
	{
		this.fontName = fontName;
		this.attributes = null;
	}
	

	/**
	 *
	 */
	public boolean isBold()
	{
		boolean ret = false;

		if (this.isBold != null)
		{
			ret = this.isBold.booleanValue();
		}
		else
		{
			if (this.reportFont != null)
			{
				ret = this.reportFont.isBold();
			}
			else
			{
				ret = false;
			}
		}
		
		return ret;
	}
	
	/**
	 *
	 */
	public Boolean isOwnBold()
	{
		return this.isBold;
	}
	
	/**
	 *
	 */
	public void setBold(boolean isBold)
	{
		this.isBold = new Boolean(isBold);
		this.attributes = null;
	}

	
	/**
	 *
	 */
	public boolean isItalic()
	{
		boolean ret = false;

		if (this.isItalic != null)
		{
			ret = this.isItalic.booleanValue();
		}
		else
		{
			if (this.reportFont != null)
			{
				ret = this.reportFont.isItalic();
			}
			else
			{
				ret = false;
			}
		}
		
		return ret;
	}
	
	/**
	 *
	 */
	public Boolean isOwnItalic()
	{
		return this.isItalic;
	}
	
	/**
	 *
	 */
	public void setItalic(boolean isItalic)
	{
		this.isItalic = new Boolean(isItalic);
		this.attributes = null;
	}
	

	/**
	 *
	 */
	public boolean isUnderline()
	{
		boolean ret = false;

		if (this.isUnderline != null)
		{
			ret = this.isUnderline.booleanValue();
		}
		else
		{
			if (this.reportFont != null)
			{
				ret = this.reportFont.isUnderline();
			}
			else
			{
				ret = false;
			}
		}
		
		return ret;
	}
	
	/**
	 *
	 */
	public Boolean isOwnUnderline()
	{
		return this.isUnderline;
	}
	
	/**
	 *
	 */
	public void setUnderline(boolean isUnderline)
	{
		this.isUnderline = new Boolean(isUnderline);
		this.attributes = null;
	}
	

	/**
	 *
	 */
	public boolean isStrikeThrough()
	{
		boolean ret = false;

		if (this.isStrikeThrough != null)
		{
			ret = this.isStrikeThrough.booleanValue();
		}
		else
		{
			if (this.reportFont != null)
			{
				ret = this.reportFont.isStrikeThrough();
			}
			else
			{
				ret = false;
			}
		}
		
		return ret;
	}
	
	/**
	 *
	 */
	public Boolean isOwnStrikeThrough()
	{
		return this.isStrikeThrough;
	}
	
	/**
	 *
	 */
	public void setStrikeThrough(boolean isStrikeThrough)
	{
		this.isStrikeThrough = new Boolean(isStrikeThrough);
		this.attributes = null;
	}


	/**
	 *
	 */
	public int getSize()
	{
		int ret = 0;

		if (this.size != null)
		{
			ret = this.size.intValue();
		}
		else
		{
			if (this.reportFont != null)
			{
				ret = this.reportFont.getSize();
			}
			else
			{
				ret = 10;
			}
		}
		
		return ret;
	}
	
	/**
	 *
	 */
	public Integer getOwnSize()
	{
		return this.size;
	}
	
	/**
	 *
	 */
	public void setSize(int size)
	{
		this.size = new Integer(size);
		this.attributes = null;
	}


	/**
	 *
	 */
	public String getPdfFontName()
	{
		String ret = null;

		if (this.pdfFontName != null)
		{
			ret = this.pdfFontName;
		}
		else
		{
			if (this.reportFont != null)
			{
				ret = this.reportFont.getPdfFontName();
			}
			else
			{
				ret = DEFAULT_PDF_FONT_NAME;
			}
		}
		
		return ret;
	}

	/**
	 *
	 */
	public String getOwnPdfFontName()
	{
		return this.pdfFontName;
	}
	
	/**
	 *
	 */
	public void setPdfFontName(String pdfFontName)
	{
		this.pdfFontName = pdfFontName;
	}

	
	/**
	 *
	 */
	public String getPdfEncoding()
	{
		String ret = null;

		if (this.pdfEncoding != null)
		{
			ret = this.pdfEncoding;
		}
		else
		{
			if (this.reportFont != null)
			{
				ret = this.reportFont.getPdfEncoding();
			}
			else
			{
				ret = DEFAULT_PDF_ENCODING;
			}
		}
		
		return ret;
	}
	
	/**
	 *
	 */
	public String getOwnPdfEncoding()
	{
		return this.pdfEncoding;
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
		boolean ret = false;

		if (this.isPdfEmbedded != null)
		{
			ret = this.isPdfEmbedded.booleanValue();
		}
		else
		{
			if (this.reportFont != null)
			{
				ret = this.reportFont.isPdfEmbedded();
			}
			else
			{
				ret = false;
			}
		}
		
		return ret;
	}

	/**
	 *
	 */
	public Boolean isOwnPdfEmbedded()
	{
		return this.isPdfEmbedded;
	}
	
	/**
	 *
	 */
	public void setPdfEmbedded(boolean isPdfEmbedded)
	{
		this.isPdfEmbedded = new Boolean(isPdfEmbedded);
	}
	

	/**
	 *
	 */
	public Map getAttributes()
	{
		if (attributes == null)
		{
			attributes = new HashMap();

			if (this.isBold())
			{
				attributes.put(TextAttribute.WEIGHT, TextAttribute.WEIGHT_BOLD);
			}
			if (this.isItalic())
			{
				attributes.put(TextAttribute.POSTURE, TextAttribute.POSTURE_OBLIQUE);
			}
			if (this.isUnderline())
			{
				attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
			}
			if (this.isStrikeThrough())
			{
				attributes.put(TextAttribute.STRIKETHROUGH, TextAttribute.STRIKETHROUGH_ON);
			}
			
			attributes.put(TextAttribute.SIZE, new Float(this.getSize()));
			attributes.put(TextAttribute.FAMILY, this.getFontName());
		}
		
		return attributes;
	}
	

}
