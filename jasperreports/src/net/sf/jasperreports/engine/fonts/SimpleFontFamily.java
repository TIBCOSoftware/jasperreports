/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2011 Jaspersoft Corporation. All rights reserved.
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
package net.sf.jasperreports.engine.fonts;

import java.awt.Font;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import net.sf.jasperreports.engine.JRFont;
import net.sf.jasperreports.engine.util.JRDataUtils;
import net.sf.jasperreports.engine.util.JRFontUtil;
import net.sf.jasperreports.engine.util.JRProperties;
import net.sf.jasperreports.engine.util.JRStyledText;



/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class SimpleFontFamily implements FontFamily
{

	/**
	 * 
	 */
	private String name;
	private FontFace normalFace;
	private FontFace boldFace;
	private FontFace italicFace;
	private FontFace boldItalicFace;
	private String normalPdfFont;
	private String boldPdfFont;
	private String italicPdfFont;
	private String boldItalicPdfFont;
	private String pdfEncoding;
	private Boolean isPdfEmbedded;
	private String defaultExportFont;
	private Map<String,String> exportFonts;
	private Set<String> locales;
	
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
	public void setNormal(String normal)
	{
		normalFace = createFontFace(normal);
	}
	
	/**
	 * 
	 */
	public void setBold(String bold)
	{
		boldFace = createFontFace(bold);
	}
	
	/**
	 * 
	 */
	public void setItalic(String italic)
	{
		italicFace = createFontFace(italic);
	}
	
	/**
	 * 
	 */
	public void setBoldItalic(String boldItalic)
	{
		boldItalicFace = createFontFace(boldItalic);
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
	public FontFace getBoldFace()
	{
		return boldFace;
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
	public FontFace getBoldItalicFace()
	{
		return boldItalicFace;
	}
	
	/**
	 * 
	 */
	public String getNormalPdfFont()
	{
		return normalPdfFont;
	}
	
	/**
	 * 
	 */
	public void setNormalPdfFont(String normalPdfFont)
	{
		this.normalPdfFont = normalPdfFont;
	}
	
	/**
	 * 
	 */
	public String getBoldPdfFont()
	{
		return boldPdfFont;
	}
	
	/**
	 * 
	 */
	public void setBoldPdfFont(String boldPdfFont)
	{
		this.boldPdfFont = boldPdfFont;
	}
	
	/**
	 * 
	 */
	public String getItalicPdfFont()
	{
		return italicPdfFont;
	}
	
	/**
	 * 
	 */
	public void setItalicPdfFont(String italicPdfFont)
	{
		this.italicPdfFont = italicPdfFont;
	}
	
	/**
	 * 
	 */
	public String getBoldItalicPdfFont()
	{
		return boldItalicPdfFont;
	}
	
	/**
	 * 
	 */
	public void setBoldItalicPdfFont(String boldItalicPdfFont)
	{
		this.boldItalicPdfFont = boldItalicPdfFont;
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
	public Boolean isPdfEmbedded()
	{
		return isPdfEmbedded;
	}
	
	/**
	 * 
	 */
	public void setPdfEmbedded(Boolean isPdfEmbedded)
	{
		this.isPdfEmbedded = isPdfEmbedded;
	}
	
	/**
	 * 
	 */
	public String getDefaultExportFont()
	{
		return defaultExportFont;
	}
	
	/**
	 * 
	 */
	public void setDefaultExportFont(String defaultExportFont)
	{
		this.defaultExportFont = defaultExportFont;
	}
	
	/**
	 * 
	 */
	public Map<String,String> getExportFonts()
	{
		return exportFonts;
	}
	
	/**
	 * 
	 */
	public void setExportFonts(Map<String,String> exportFonts)
	{
		this.exportFonts = exportFonts;
	}
	
	/**
	 * 
	 */
	public String getExportFont(String key)
	{
		String exportFont = exportFonts == null ? null : (String)exportFonts.get(key);
		return exportFont == null ? defaultExportFont : exportFont;
	}
	
	/**
	 * 
	 */
	public Set<String> getLocales()
	{
		return locales;
	}
	
	/**
	 * 
	 */
	public void setLocales(Set<String> locales)
	{
		this.locales = locales;
	}
	
	/**
	 * 
	 */
	public boolean supportsLocale(Locale locale)
	{
		return locales == null || locales.contains(JRDataUtils.getLocaleCode(locale));
	}
	
	/**
	 * 
	 */
	private static SimpleFontFace createFontFace(String value)
	{
		SimpleFontFace fontFace = null;

		if (value != null)
		{
			if (value.trim().toUpperCase().endsWith(".TTF"))
			{
				fontFace = new SimpleFontFace(value);
			}
			else
			{
				JRFontUtil.checkAwtFont(value, JRProperties.getBooleanProperty(JRStyledText.PROPERTY_AWT_IGNORE_MISSING_FONT));
				
				fontFace = new SimpleFontFace(new Font(value, Font.PLAIN, JRProperties.getIntegerProperty(JRFont.DEFAULT_FONT_SIZE)));
			}
		}
		
		return fontFace;
	}
	
}
