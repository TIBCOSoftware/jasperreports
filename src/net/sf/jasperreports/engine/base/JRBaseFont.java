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

import dori.jasper.engine.JRDefaultFontProvider;
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
	private static final boolean DEFAULT_FONT_BOLD = false;
	private static final boolean DEFAULT_FONT_ITALIC = false;
	private static final boolean DEFAULT_FONT_UNDERLINE = false;
	private static final boolean DEFAULT_FONT_STRIKETHROUGH = false;
	private static final int DEFAULT_FONT_SIZE = 10;
	private static final String DEFAULT_PDF_FONT_NAME = "Helvetica";
	private static final String DEFAULT_PDF_ENCODING = "Cp1252";
	private static final boolean DEFAULT_PDF_EMBEDDED = false;

	private static JRFont defaultFont = null;

	/**
	 *
	 */
	protected JRDefaultFontProvider defaultFontProvider = null;

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

	protected boolean isCachingAttributes = false;
	protected transient Map attributes = null;


	/**
	 *
	 */
	public JRBaseFont()
	{
	}
		

	/**
	 *
	 */
	protected JRBaseFont(JRDefaultFontProvider defaultFontProvider)
	{
		this.defaultFontProvider = defaultFontProvider;
	}
		

	/**
	 *
	 */
	public JRBaseFont(
		JRDefaultFontProvider defaultFontProvider, 
		JRReportFont reportFont,
		JRFont font
		)
	{
		this.defaultFontProvider = defaultFontProvider;
		
		this.reportFont = reportFont;
		
		fontName = font.getOwnFontName();
		isBold = font.isOwnBold();
		isItalic = font.isOwnItalic();
		isUnderline = font.isOwnUnderline();
		isStrikeThrough = font.isOwnStrikeThrough();
		size = font.getOwnSize();
		pdfFontName = font.getOwnPdfFontName();
		pdfEncoding = font.getOwnPdfEncoding();
		isPdfEmbedded = font.isOwnPdfEmbedded();
		
		isCachingAttributes = font.isCachingAttributes();
	}
		

	/**
	 *
	 */
	public JRDefaultFontProvider getDefaultFontProvider()
	{
		return defaultFontProvider;
	}
	

	/**
	 *
	 */
	public JRReportFont getReportFont()
	{
		return reportFont;
	}
	

	/**
	 *
	 */
	public String getFontName()
	{
		if (fontName == null)
		{
			return getBaseFont().getFontName();
		}
		else
		{
			return fontName;
		}
	}
	
	/**
	 *
	 */
	public String getOwnFontName()
	{
		return fontName;
	}
	
	/**
	 *
	 */
	public void setFontName(String fontName)
	{
		this.fontName = fontName;
		attributes = null;
	}
	

	/**
	 *
	 */
	public boolean isBold()
	{
		if (isBold == null)
		{
			return getBaseFont().isBold();
		}
		else
		{
			return isBold.booleanValue();
		}
	}
	
	/**
	 *
	 */
	public Boolean isOwnBold()
	{
		return isBold;
	}
	
	/**
	 *
	 */
	public void setBold(boolean isBold)
	{
		setBold(isBold ? Boolean.TRUE : Boolean.FALSE);
	}

	/**
	 * Alternative setBold method which allows also to reset
	 * the "own" isBold property.
	 */
	public void setBold(Boolean isBold)
	{
		this.isBold = isBold;
		this.attributes = null;
	}

	
	/**
	 *
	 */
	public boolean isItalic()
	{
		if (isItalic == null)
		{
			return getBaseFont().isItalic();
		}
		else
		{
			return isItalic.booleanValue();
		}
	}
	
	/**
	 *
	 */
	public Boolean isOwnItalic()
	{
		return isItalic;
	}
	
	/**
	 *
	 */
	public void setItalic(boolean isItalic)
	{
		setItalic(isItalic ? Boolean.TRUE : Boolean.FALSE);
	}
	
	/**
	 * Alternative setItalic method which allows also to reset
	 * the "own" isItalic property.
	 */
	public void setItalic(Boolean isItalic) 
	{
		this.isItalic = isItalic;
		this.attributes = null;
	}
	
	/**
	 *
	 */
	public boolean isUnderline()
	{
		if (isUnderline == null)
		{
			return getBaseFont().isUnderline();
		}
		else
		{
			return isUnderline.booleanValue();
		}
	}
	
	/**
	 *
	 */
	public Boolean isOwnUnderline()
	{
		return isUnderline;
	}
	
	/**
	 *
	 */
	public void setUnderline(boolean isUnderline)
	{
		setUnderline(isUnderline ? Boolean.TRUE : Boolean.FALSE);
	}
	
	/**
	 * Alternative setUnderline method which allows also to reset
	 * the "own" isUnderline property.
	 */
	public void setUnderline(Boolean isUnderline) 
	{
		this.isUnderline = isUnderline;
		this.attributes = null;
	}

	/**
	 *
	 */
	public boolean isStrikeThrough()
	{
		if (isStrikeThrough == null)
		{
			return getBaseFont().isStrikeThrough();
		}
		else
		{
			return isStrikeThrough.booleanValue();
		}
	}
	
	/**
	 *
	 */
	public Boolean isOwnStrikeThrough()
	{
		return isStrikeThrough;
	}
	
	/**
	 *
	 */
	public void setStrikeThrough(boolean isStrikeThrough)
	{
		setStrikeThrough(isStrikeThrough ? Boolean.TRUE : Boolean.FALSE);
	}

	/**
	 * Alternative setStrikeThrough method which allows also to reset
	 * the "own" isStrikeThrough property.
	 */
	public void setStrikeThrough(Boolean isStrikeThrough) 
	{
		this.isStrikeThrough = isStrikeThrough;
		this.attributes = null;
	}

	/**
	 *
	 */
	public int getSize()
	{
		if (size == null)
		{
			return getBaseFont().getSize();
		}
		else
		{
			return size.intValue();
		}
	}
	
	/**
	 *
	 */
	public Integer getOwnSize()
	{
		return size;
	}
	
	/**
	 *
	 */
	public void setSize(int size)
	{
		setSize(new Integer(size));
	}

	/**
	 * Alternative setSize method which allows also to reset
	 * the "own" size property.
	 */
	public void setSize(Integer size) 
	{
		this.size = size;
		this.attributes = null;
	}

	/**
	 *
	 */
	public String getPdfFontName()
	{
		if (pdfFontName == null)
		{
			return getBaseFont().getPdfFontName();
		}
		else
		{
			return pdfFontName;
		}
	}

	/**
	 *
	 */
	public String getOwnPdfFontName()
	{
		return pdfFontName;
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
		if (pdfEncoding == null)
		{
			return getBaseFont().getPdfEncoding();
		}
		else
		{
			return pdfEncoding;
		}
	}
	
	/**
	 *
	 */
	public String getOwnPdfEncoding()
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
		if (isPdfEmbedded == null)
		{
			return getBaseFont().isPdfEmbedded();
		}
		else
		{
			return isPdfEmbedded.booleanValue();
		}
	}

	/**
	 *
	 */
	public Boolean isOwnPdfEmbedded()
	{
		return isPdfEmbedded;
	}
	
	/**
	 *
	 */
	public void setPdfEmbedded(boolean isPdfEmbedded)
	{
		setPdfEmbedded(isPdfEmbedded ? Boolean.TRUE : Boolean.FALSE);
	}
	
	/**
	 * Alternative setPdfEmbedded method which allows also to reset
	 * the "own" isPdfEmbedded property.
	 */
	public void setPdfEmbedded(Boolean isPdfEmbedded) 
	{
		this.isPdfEmbedded = isPdfEmbedded;
		this.attributes = null;
	}

	/**
	 *
	 */
	public boolean isCachingAttributes()
	{
		return isCachingAttributes;
	}

	/**
	 *
	 */
	public void setCachingAttributes(boolean isCachingAttributes)
	{
		this.isCachingAttributes = isCachingAttributes;
		attributes = null;
	}
	

	/**
	 *
	 */
	public Map getAttributes()
	{
		if (attributes == null || !isCachingAttributes)
		{
			attributes = new HashMap();

			if (isBold())
			{
				attributes.put(TextAttribute.WEIGHT, TextAttribute.WEIGHT_BOLD);
			}
			if (isItalic())
			{
				attributes.put(TextAttribute.POSTURE, TextAttribute.POSTURE_OBLIQUE);
			}
			if (isUnderline())
			{
				attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
			}
			if (isStrikeThrough())
			{
				attributes.put(TextAttribute.STRIKETHROUGH, TextAttribute.STRIKETHROUGH_ON);
			}
		
			attributes.put(TextAttribute.SIZE, new Float(getSize()));
			attributes.put(TextAttribute.FAMILY, getFontName());
		}
		
		return attributes;
	}
	

	/**
	 *
	 */
	private JRFont getBaseFont()
	{
		JRFont baseFont = null;

		if (reportFont != null)
		{
			baseFont = reportFont;
		}
		else if (
			defaultFontProvider != null 
			&& defaultFontProvider.getDefaultFont() != null
			)
		{
			baseFont = defaultFontProvider.getDefaultFont();
		}
		else
		{
			baseFont = getDefaultFont();
		}
		
		return baseFont;
	}
	

	/**
	 *
	 */
	private static JRFont getDefaultFont()
	{
		if (defaultFont == null)
		{
			defaultFont = new JRBaseFont();
			defaultFont.setFontName(DEFAULT_FONT_NAME);
			defaultFont.setBold(DEFAULT_FONT_BOLD);
			defaultFont.setItalic(DEFAULT_FONT_ITALIC);
			defaultFont.setUnderline(DEFAULT_FONT_UNDERLINE);
			defaultFont.setStrikeThrough(DEFAULT_FONT_STRIKETHROUGH);
			defaultFont.setSize(DEFAULT_FONT_SIZE);
			defaultFont.setPdfFontName(DEFAULT_PDF_FONT_NAME);
			defaultFont.setPdfEncoding(DEFAULT_PDF_ENCODING);
			defaultFont.setPdfEmbedded(DEFAULT_PDF_EMBEDDED);
		}
		
		return defaultFont;
	}
	

}
