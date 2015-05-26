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
package net.sf.jasperreports.engine.export;

import java.awt.Font;
import java.util.Locale;

import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.fonts.FontFace;
import net.sf.jasperreports.engine.fonts.FontFamily;
import net.sf.jasperreports.engine.fonts.FontInfo;
import net.sf.jasperreports.engine.fonts.FontUtil;
import net.sf.jasperreports.engine.util.JRDataUtils;

/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public class HtmlFont
{
	private static final int IE_FONT_NAME_MAX_LENGTH = 31;
	
	private Locale locale;
	private String fontName;
	private String ttf;
	private String eot;
	private String svg;
	private String woff;
	private int style;
	
	private String id;
	private String shortId;
	
	/**
	 * 
	 */
	private HtmlFont(Locale locale, FontFace fontFace, int style)
	{
		this.locale = locale;
		this.fontName = fontFace.getName();
		this.ttf = fontFace.getTtf();
		this.eot = fontFace.getEot();
		this.svg = fontFace.getSvg();
		this.woff = fontFace.getWoff();
		this.style = style;
		
		createIds();
	}
	
	/**
	 * 
	 */
	public static HtmlFont getInstance(Locale locale, FontInfo fontInfo, boolean isBold, boolean isItalic)
	{
		HtmlFont htmlFont = null;
		
		if (fontInfo != null)
		{
			FontFamily family = fontInfo.getFontFamily();
			FontFace fontFace = fontInfo.getFontFace();
			if (fontFace != null)
			{
				htmlFont = HtmlFont.getInstance(locale, fontFace, fontInfo.getStyle());
			}
			
			if (htmlFont == null && isBold && isItalic)
			{
				fontFace = family.getBoldItalicFace();
				if (fontFace != null)
				{
					htmlFont = HtmlFont.getInstance(locale, fontFace, Font.BOLD | Font.ITALIC);
				}
			}
			
			if (htmlFont == null && isBold)
			{
				fontFace = family.getBoldFace();
				if (fontFace != null)
				{
					htmlFont = HtmlFont.getInstance(locale, fontFace, Font.BOLD);
				}
			}

			if (htmlFont == null && isItalic)
			{
				fontFace = family.getItalicFace();
				if (fontFace != null)
				{
					htmlFont = HtmlFont.getInstance(locale, fontFace, Font.ITALIC);
				}
			}

			if (htmlFont == null)
			{
				fontFace = family.getNormalFace();
				if (fontFace != null)
				{
					htmlFont = HtmlFont.getInstance(locale, fontFace, Font.PLAIN);
				}
			}
		}
		
		return htmlFont;
	}
	
	/**
	 * 
	 */
	public static HtmlFont getInstance(JasperReportsContext jasperReportsContext, String htmlFontId)
	{
		int localeSeparatorPos = htmlFontId.lastIndexOf("-");
		String faceName = htmlFontId.substring(0, localeSeparatorPos);
		boolean isBold = false;
		boolean isItalic = false;
		if (faceName.endsWith("-Italic"))
		{
			faceName = faceName.substring(0, faceName.length() - "-Italic".length());
			isItalic = true;
		}
		if (faceName.endsWith("-Bold"))
		{
			faceName = faceName.substring(0, faceName.length() - "-Bold".length());
			isBold = true;
		}
		String localeCode = htmlFontId.substring(localeSeparatorPos + 1);
		Locale locale = JRDataUtils.getLocale(localeCode);
		
		FontInfo fontInfo = FontUtil.getInstance(jasperReportsContext).getFontInfo(faceName, locale);

		return getInstance(locale, fontInfo, isBold, isItalic);
	}
	
	/**
	 * 
	 */
	private static HtmlFont getInstance(Locale locale, FontFace fontFace, int style)
	{
		HtmlFont htmlFont = null;

		if (
			fontFace.getTtf() != null
			|| fontFace.getEot() != null
			|| fontFace.getSvg() != null
			|| fontFace.getWoff() != null
			)
		{
			htmlFont = new HtmlFont(locale, fontFace, style);
		}

		return htmlFont;
	}
	
	/**
	 * 
	 */
	private void createIds()
	{
		String prefix = fontName;
		String suffix =
			(((style & Font.BOLD) > 0 || (style & Font.ITALIC) > 0) ? "-" : "")
			+ ((style & Font.BOLD) > 0 ? "Bold" : "")
			+ ((style & Font.ITALIC) > 0 ? "Italic" : "")
			+ (locale == null ? "" : ("-" + JRDataUtils.getLocaleCode(locale)));
		
		id = prefix + suffix;
		
		if (prefix.length() + suffix.length() > IE_FONT_NAME_MAX_LENGTH)
		{
			prefix = prefix.replaceAll("\\s", "");
		}
		if (prefix.length() + suffix.length() > IE_FONT_NAME_MAX_LENGTH)
		{
			suffix =
				(((style & Font.BOLD) > 0 || (style & Font.ITALIC) > 0) ? "-" : "")
				+ ((style & Font.BOLD) > 0 ? "B" : "")
				+ ((style & Font.ITALIC) > 0 ? "I" : "")
				+ (locale == null ? "" : ("-" + JRDataUtils.getLocaleCode(locale)));
		}
		if (prefix.length() + suffix.length() > IE_FONT_NAME_MAX_LENGTH)
		{
			prefix = prefix.substring(0, 1) + prefix.substring(1).replaceAll("[AaEeIiOoUu]", "");
		}
		if (prefix.length() + suffix.length() > IE_FONT_NAME_MAX_LENGTH)
		{
			prefix = prefix.substring(0, IE_FONT_NAME_MAX_LENGTH - suffix.length());
		}
		
		shortId = prefix + suffix;
	}
	
	/**
	 * 
	 */
	public String getId()
	{
		return id;
	}
	
	/**
	 * 
	 */
	public String getShortId()
	{
		return shortId;
	}
	
	public Locale getLocale()
	{
		return locale;
	}
	
	public String getFontName()
	{
		return fontName;
	}
	
	public String getTtf()
	{
		return ttf;
	}
	
	public String getEot()
	{
		return eot;
	}
	
	public String getSvg()
	{
		return svg;
	}
	
	public String getWoff()
	{
		return woff;
	}
	
	public int getStyle()
	{
		return style;
	}
}
