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
 * 185, Berry Street, Suite 6200
 * San Francisco CA 94107
 * http://www.jaspersoft.com
 */
package net.sf.jasperreports.engine.base;

import java.awt.font.TextAttribute;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import net.sf.jasperreports.engine.JRDefaultFontProvider;
import net.sf.jasperreports.engine.JRFont;
import net.sf.jasperreports.engine.JRReportFont;
import net.sf.jasperreports.engine.util.JRTextAttribute;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JRBaseFont implements JRFont, Serializable
{


	/**
	 *
	 */
	private static final long serialVersionUID = 608;

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
	public JRBaseFont(Map attributes)
	{
		Object bold = attributes.get(TextAttribute.WEIGHT);
		if (bold != null)
		{
			setBold(TextAttribute.WEIGHT_BOLD.equals(bold));
		}

		Object italic = attributes.get(TextAttribute.POSTURE);
		if (italic != null)
		{
			setItalic(TextAttribute.POSTURE_OBLIQUE.equals(italic));
		}

		Float size = (Float)attributes.get(TextAttribute.SIZE);
		if (size != null)
		{
			setSize(size.intValue());
		}
		
		Object underline = attributes.get(TextAttribute.UNDERLINE);
		if (underline != null)
		{
			setUnderline(TextAttribute.UNDERLINE_ON.equals(underline));
		}

		Object strikeThrough = attributes.get(TextAttribute.STRIKETHROUGH);
		if (strikeThrough != null)
		{
			setStrikeThrough(TextAttribute.STRIKETHROUGH_ON.equals(strikeThrough));
		}

		String pdfFontName = (String)attributes.get(JRTextAttribute.PDF_FONT_NAME);
		if (pdfFontName != null)
		{
			setPdfFontName(pdfFontName);
		}
		
		String pdfEncoding = (String)attributes.get(JRTextAttribute.PDF_ENCODING);
		if (pdfEncoding != null)
		{
			setPdfEncoding(pdfEncoding);
		}
		
		Boolean isPdfEmbedded = (Boolean)attributes.get(JRTextAttribute.IS_PDF_EMBEDDED);
		if (isPdfEmbedded != null)
		{
			setPdfEmbedded(isPdfEmbedded.booleanValue());
		}
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

			attributes.put(TextAttribute.FAMILY, getFontName());
			attributes.put(TextAttribute.SIZE, new Float(getSize()));

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
		
			attributes.put(JRTextAttribute.PDF_FONT_NAME, getPdfFontName());
			attributes.put(JRTextAttribute.PDF_ENCODING, getPdfEncoding());

			if (isPdfEmbedded())
			{
				attributes.put(JRTextAttribute.IS_PDF_EMBEDDED, Boolean.TRUE);
			}
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
