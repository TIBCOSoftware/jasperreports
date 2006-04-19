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
package net.sf.jasperreports.engine.base;

import java.awt.font.TextAttribute;
import java.io.Serializable;
import java.util.Map;

import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRDefaultFontProvider;
import net.sf.jasperreports.engine.JRDefaultStyleProvider;
import net.sf.jasperreports.engine.JRFont;
import net.sf.jasperreports.engine.JRReportFont;
import net.sf.jasperreports.engine.JRStyle;
import net.sf.jasperreports.engine.util.JRStyleResolver;
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
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

	/**
	 *
	 */
	protected JRDefaultFontProvider defaultFontProvider = null;

	/**
	 *
	 */
	protected JRReportFont reportFont = null;

	protected String fontName = null;
	protected Boolean isBold = null;
	protected Boolean isItalic = null;
	protected Boolean isUnderline = null;
	protected Boolean isStrikeThrough = null;
	protected Integer fontSize = null;
	protected String pdfFontName = null;
	protected String pdfEncoding = null;
	protected Boolean isPdfEmbedded = null;


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
		String fontNameAttr = (String)attributes.get(TextAttribute.FAMILY);
		if (fontNameAttr != null)
		{
			setFontName(fontNameAttr);
		}
		
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

		Float sizeAttr = (Float)attributes.get(TextAttribute.SIZE);
		if (sizeAttr != null)
		{
			setFontSize(sizeAttr.intValue());
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

		String pdfFontNameAttr = (String)attributes.get(JRTextAttribute.PDF_FONT_NAME);
		if (pdfFontNameAttr != null)
		{
			setPdfFontName(pdfFontNameAttr);
		}
		
		String pdfEncodingAttr = (String)attributes.get(JRTextAttribute.PDF_ENCODING);
		if (pdfEncodingAttr != null)
		{
			setPdfEncoding(pdfEncodingAttr);
		}
		
		Boolean isPdfEmbeddedAttr = (Boolean)attributes.get(JRTextAttribute.IS_PDF_EMBEDDED);
		if (isPdfEmbeddedAttr != null)
		{
			setPdfEmbedded(isPdfEmbeddedAttr);
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
		fontSize = font.getOwnFontSize();
		pdfFontName = font.getOwnPdfFontName();
		pdfEncoding = font.getOwnPdfEncoding();
		isPdfEmbedded = font.isOwnPdfEmbedded();
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
	public JRDefaultStyleProvider getDefaultStyleProvider()
	{
		return null;
	}

	/**
	 *
	 */
	public JRStyle getStyle()
	{
		return null;
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
	public void setReportFont(JRReportFont reportFont)
	{
		this.reportFont = reportFont;
	}

	/**
	 *
	 */
	public String getFontName()
	{
		return JRStyleResolver.getFontName(this);
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
	}
	

	/**
	 *
	 */
	public boolean isBold()
	{
		return JRStyleResolver.isBold(this);
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
	}

	
	/**
	 *
	 */
	public boolean isItalic()
	{
		return JRStyleResolver.isItalic(this);
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
	}
	
	/**
	 *
	 */
	public boolean isUnderline()
	{
		return JRStyleResolver.isUnderline(this);
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
	}

	/**
	 *
	 */
	public boolean isStrikeThrough()
	{
		return JRStyleResolver.isStrikeThrough(this);
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
	}

	/**
	 *
	 */
	public int getFontSize()
	{
		return JRStyleResolver.getFontSize(this);
	}
	
	/**
	 *
	 */
	public Integer getOwnFontSize()
	{
		return fontSize;
	}
	
	/**
	 *
	 */
	public void setFontSize(int fontSize)
	{
		setFontSize(new Integer(fontSize));
	}

	/**
	 * Alternative setSize method which allows also to reset
	 * the "own" size property.
	 */
	public void setFontSize(Integer fontSize) 
	{
		this.fontSize = fontSize;
	}

	/**
	 * @deprecated Replaced by {@link #getFontSize()}.
	 */
	public int getSize()
	{
		return getFontSize();
	}
	
	/**
	 * @deprecated Replaced by {@link #getOwnFontSize()}.
	 */
	public Integer getOwnSize()
	{
		return getOwnFontSize();
	}
	
	/**
	 * @deprecated Replaced by {@link #setFontSize(int)}.
	 */
	public void setSize(int size)
	{
		setFontSize(size);
	}

	/**
	 * @deprecated Replaced by {@link #setFontSize(Integer)}.
	 */
	public void setSize(Integer size) 
	{
		setFontSize(size)
;	}

	/**
	 *
	 */
	public String getPdfFontName()
	{
		return JRStyleResolver.getPdfFontName(this);
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
		return JRStyleResolver.getPdfEncoding(this);
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
		return JRStyleResolver.isPdfEmbedded(this);
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
	}

	
}
