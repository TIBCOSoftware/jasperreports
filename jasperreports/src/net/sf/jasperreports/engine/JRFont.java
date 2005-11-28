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
package net.sf.jasperreports.engine;


/**
 * An abstract representation of a font. Fonts in JasperReports are very complex because of the library portability
 * across operating systems and export formats. This interface provides basic font functionality methods for
 * managing font attributes and special PDF font attributes.
 * <p>
 * Users can define report level fonts that can be referenced by name in text elements. Their default properties
 * can be overriden in each element (for example, a text element can use a report level font and just change its
 * "underline" attribute). All the "own" methods in this class actually return the override values of font properties.
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public interface JRFont extends JRStyleContainer
{

	public static final String DEFAULT_FONT_NAME = "sansserif";
	public static final boolean DEFAULT_FONT_BOLD = false;
	public static final boolean DEFAULT_FONT_ITALIC = false;
	public static final boolean DEFAULT_FONT_UNDERLINE = false;
	public static final boolean DEFAULT_FONT_STRIKETHROUGH = false;
	public static final int DEFAULT_FONT_SIZE = 10;
	public static final String DEFAULT_PDF_FONT_NAME = "Helvetica";
	public static final String DEFAULT_PDF_ENCODING = "Cp1252";
	public static final boolean DEFAULT_PDF_EMBEDDED = false;

	/**
	 *
	 */
	public JRReportFont getReportFont();
	
	/**
	 *
	 */
	public void setReportFont(JRReportFont reportFont);

	/**
	 *
	 */
	public String getFontName();
	
	/**
	 *
	 */
	public String getOwnFontName();
	
	/**
	 *
	 */
	public void setFontName(String fontName);
	
	/**
	 *
	 */
	public boolean isBold();
	
	/**
	 *
	 */
	public Boolean isOwnBold();
	
	/**
	 *
	 */
	public void setBold(boolean isBold);
	
	/**
	 *
	 */
	public void setBold(Boolean isBold);
	
	/**
	 *
	 */
	public boolean isItalic();
	
	/**
	 *
	 */
	public Boolean isOwnItalic();
	
	/**
	 *
	 */
	public void setItalic(boolean isItalic);
	
	/**
	 *
	 */
	public void setItalic(Boolean isItalic);
	
	/**
	 *
	 */
	public boolean isUnderline();
	
	/**
	 *
	 */
	public Boolean isOwnUnderline();
	
	/**
	 *
	 */
	public void setUnderline(boolean isUnderline);
	
	/**
	 *
	 */
	public void setUnderline(Boolean isUnderline);
	
	/**
	 *
	 */
	public boolean isStrikeThrough();
	
	/**
	 *
	 */
	public Boolean isOwnStrikeThrough();
	
	/**
	 *
	 */
	public void setStrikeThrough(boolean isStrikeThrough);

	/**
	 *
	 */
	public void setStrikeThrough(Boolean isStrikeThrough);

	/**
	 * @deprecated Replaced by {@link #getFontSize()}.
	 */
	public int getSize();
	
	/**
	 * @deprecated Replaced by {@link #getOwnFontSize()}.
	 */
	public Integer getOwnSize();
	
	/**
	 * @deprecated Replaced by {@link #setFontSize(int)}.
	 */
	public void setSize(int size);

	/**
	 * @deprecated Replaced by {@link #setFontSize(Integer)}.
	 */
	public void setSize(Integer size);

	/**
	 *
	 */
	public int getFontSize();
	
	/**
	 *
	 */
	public Integer getOwnFontSize();
	
	/**
	 *
	 */
	public void setFontSize(int fontSize);

	/**
	 *
	 */
	public void setFontSize(Integer fontSize);

	/**
	 *
	 */
	public String getPdfFontName();
	
	/**
	 *
	 */
	public String getOwnPdfFontName();
	
	/**
	 *
	 */
	public void setPdfFontName(String pdfFontName);

	/**
	 *
	 */
	public String getPdfEncoding();
	
	/**
	 *
	 */
	public String getOwnPdfEncoding();
	
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
	public Boolean isOwnPdfEmbedded();

	/**
	 *
	 */
	public void setPdfEmbedded(boolean isPdfEmbedded);
	
	/**
	 *
	 */
	public void setPdfEmbedded(Boolean isPdfEmbedded);

}
