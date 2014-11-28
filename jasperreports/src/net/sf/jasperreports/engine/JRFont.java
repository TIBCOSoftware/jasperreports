/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2014 TIBCO Software Inc. All rights reserved.
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
package net.sf.jasperreports.engine;



/**
 * An abstract representation of a font. Fonts in JasperReports are very complex because of the library portability
 * across operating systems and export formats. This interface provides basic font functionality methods for
 * managing font attributes and special PDF font attributes.
 * <p>
 * Users can define report level fonts that can be referenced by name in text elements. Their default properties
 * can be overridden in each element (for example, a text element can use a report level font and just change its
 * "underline" attribute). All the "own" methods in this class actually return the override values of font properties.
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public interface JRFont extends JRStyleContainer
{

	public static final String DEFAULT_FONT_NAME = JRPropertiesUtil.PROPERTY_PREFIX + "default.font.name";
	public static final String DEFAULT_FONT_SIZE = JRPropertiesUtil.PROPERTY_PREFIX + "default.font.size";
	public static final String DEFAULT_PDF_FONT_NAME = JRPropertiesUtil.PROPERTY_PREFIX + "default.pdf.font.name";
	public static final String DEFAULT_PDF_ENCODING = JRPropertiesUtil.PROPERTY_PREFIX + "default.pdf.encoding";
	public static final String DEFAULT_PDF_EMBEDDED = JRPropertiesUtil.PROPERTY_PREFIX + "default.pdf.embedded";

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
	 *
	 */
	public float getFontsize();
	
	/**
	 *
	 */
	public Float getOwnFontsize();

	/**
	 *
	 */
	public void setFontSize(Float size);

	/**
	 * @deprecated Replaced by {@link #getFontsize()}.
	 */
	public int getFontSize();
	
	/**
	 * @deprecated Replaced by {@link #getOwnFontsize()}.
	 */
	public Integer getOwnFontSize();
	
	/**
	 * @deprecated Replaced by {@link #setFontSize(Float)}.
	 */
	public void setFontSize(int fontSize);

	/**
	 * @deprecated Replaced by {@link #setFontSize(Float)}.
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
