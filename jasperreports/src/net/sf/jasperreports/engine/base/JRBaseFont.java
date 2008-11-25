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
import net.sf.jasperreports.engine.JRStyleContainer;
import net.sf.jasperreports.engine.design.events.JRChangeEventsSupport;
import net.sf.jasperreports.engine.design.events.JRPropertyChangeSupport;
import net.sf.jasperreports.engine.util.JRStyleResolver;
import net.sf.jasperreports.engine.util.JRTextAttribute;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JRBaseFont implements JRFont, Serializable, JRChangeEventsSupport
{


	/**
	 *
	 */
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;
	
	public static final String PROPERTY_BOLD = "bold";
	
	public static final String PROPERTY_FONT_NAME = "fontName";
	
	public static final String PROPERTY_FONT_SIZE = "fontSize";
	
	public static final String PROPERTY_ITALIC = "italic";
	
	public static final String PROPERTY_PDF_EMBEDDED = "pdfEmbedded";
	
	public static final String PROPERTY_PDF_ENCODING = "pdfEncoding";
	
	public static final String PROPERTY_PDF_FONT_NAME = "pdfFontName";
	
	public static final String PROPERTY_REPORT_FONT = "reportFont";
	
	public static final String PROPERTY_STRIKE_THROUGH = "strikeThrough";
	
	public static final String PROPERTY_UNDERLINE = "underline";

	/**
	 *
	 */
	protected JRDefaultFontProvider defaultFontProvider = null;

	/**
	 *
	 */
	protected JRReportFont reportFont = null;

	/**
	 *
	 */
	protected JRStyleContainer styleContainer = null;

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
		String fontNameAttr = (String)attributes.get(JRTextAttribute.FONT_NAME);
		if (fontNameAttr == null)
		{
			fontNameAttr = (String)attributes.get(TextAttribute.FAMILY);
			if (fontNameAttr != null)
			{
				setFontName(fontNameAttr);
			}
		}
		else
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
		this(
			defaultFontProvider, 
			reportFont,
			null,
			font
			);
	}
		

	/**
	 *
	 */
	public JRBaseFont(
		JRDefaultFontProvider defaultFontProvider, 
		JRReportFont reportFont,
		JRStyleContainer styleContainer,
		JRFont font
		)
	{
		this.defaultFontProvider = defaultFontProvider;
		
		this.reportFont = reportFont;

		this.styleContainer = styleContainer;
		
		if (font != null)
		{
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
		return styleContainer == null ? null : styleContainer.getDefaultStyleProvider();
	}

	/**
	 *
	 */
	public JRStyle getStyle()
	{
		return styleContainer == null ? null : styleContainer.getStyle();
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
		Object old = this.reportFont;
		this.reportFont = reportFont;
		getEventSupport().firePropertyChange(PROPERTY_REPORT_FONT, old, this.reportFont);
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
		Object old = this.fontName;
		this.fontName = fontName;
		getEventSupport().firePropertyChange(PROPERTY_FONT_NAME, old, this.fontName);
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
		Object old = this.isBold;
		this.isBold = isBold;
		getEventSupport().firePropertyChange(PROPERTY_BOLD, old, this.isBold);
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
		Object old = this.isItalic;
		this.isItalic = isItalic;
		getEventSupport().firePropertyChange(PROPERTY_ITALIC, old, this.isItalic);
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
		Object old = this.isUnderline;
		this.isUnderline = isUnderline;
		getEventSupport().firePropertyChange(PROPERTY_UNDERLINE, old, this.isUnderline);
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
		Object old = this.isStrikeThrough;
		this.isStrikeThrough = isStrikeThrough;
		getEventSupport().firePropertyChange(PROPERTY_STRIKE_THROUGH, old, this.isStrikeThrough);
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
		Object old = this.fontSize;
		this.fontSize = fontSize;
		getEventSupport().firePropertyChange(PROPERTY_FONT_SIZE, old, this.fontSize);
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
		Object old = this.pdfFontName;
		this.pdfFontName = pdfFontName;
		getEventSupport().firePropertyChange(PROPERTY_PDF_FONT_NAME, old, this.pdfFontName);
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
		Object old = this.pdfEncoding;
		this.pdfEncoding = pdfEncoding;
		getEventSupport().firePropertyChange(PROPERTY_PDF_ENCODING, old, this.pdfEncoding);
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
		Object old = this.isPdfEmbedded;
		this.isPdfEmbedded = isPdfEmbedded;
		getEventSupport().firePropertyChange(PROPERTY_PDF_EMBEDDED, old, this.isPdfEmbedded);
	}


	public String getStyleNameReference()
	{
		return styleContainer == null ? null : styleContainer.getStyleNameReference();
	}

	
	private transient JRPropertyChangeSupport eventSupport;
	
	public JRPropertyChangeSupport getEventSupport()
	{
		synchronized (this)
		{
			if (eventSupport == null)
			{
				eventSupport = new JRPropertyChangeSupport(this);
			}
		}
		
		return eventSupport;
	}
	
}
