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
package net.sf.jasperreports.engine.util;

import java.awt.Font;
import java.awt.font.TextAttribute;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import net.sf.jasperreports.engine.JRFont;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.fonts.FontBundle;
import net.sf.jasperreports.engine.fonts.FontFace;
import net.sf.jasperreports.engine.fonts.FontFamily;
import net.sf.jasperreports.engine.fonts.FontInfo;
import net.sf.jasperreports.extensions.ExtensionsEnvironment;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JRFontUtil
{
	/**
	 *
	 */
	public static void copyNonNullOwnProperties(JRFont srcFont, JRFont destFont)
	{
		if(srcFont != null && destFont != null)
		{
			if (srcFont.getOwnFontName() != null)
				destFont.setFontName(srcFont.getOwnFontName());
			if (srcFont.isOwnBold() != null)
				destFont.setBold(srcFont.isOwnBold());
			if (srcFont.isOwnItalic() != null)
				destFont.setItalic(srcFont.isOwnItalic());
			if (srcFont.isOwnUnderline() != null)
				destFont.setUnderline(srcFont.isOwnUnderline());
			if (srcFont.isOwnStrikeThrough() != null)
				destFont.setStrikeThrough(srcFont.isOwnStrikeThrough());
			if (srcFont.getOwnFontSize() != null)
				destFont.setFontSize(srcFont.getOwnFontSize());
			if (srcFont.getOwnPdfFontName() != null)
				destFont.setPdfFontName(srcFont.getOwnPdfFontName());
			if (srcFont.getOwnPdfEncoding() != null)
				destFont.setPdfEncoding(srcFont.getOwnPdfEncoding());
			if (srcFont.isOwnPdfEmbedded() != null)
				destFont.setPdfEmbedded(srcFont.isOwnPdfEmbedded());
		}
	}
	

	/**
	 *
	 */
	private static final AwtFontDeriver FONT_DERIVER 
		= System.getProperty("java.version").startsWith("1.4")
			?AwtFontDeriver.JDK14_AWT_FONT_DERIVER
			:AwtFontDeriver.DEFAULT_AWT_FONT_DERIVER;
	

	/**
	 *
	 */
	public static Map getAttributes(Map attributes, JRFont font, Locale locale)
	{
		//Font awtFont = getAwtFont(font);//FIXMEFONT optimize so that we don't load the AWT font for all exporters.
		Font awtFont = 
			getAwtFontFromBundles(
				font.getFontName(), 
				((font.isBold()?Font.BOLD:Font.PLAIN)|(font.isItalic()?Font.ITALIC:Font.PLAIN)), 
				font.getFontSize(),
				locale
				);
		if (awtFont != null)
		{
			attributes.put(TextAttribute.FONT, awtFont); 
		}
		
		getAttributesWithoutAwtFont(attributes, font);

		return attributes;
	}


	/**
	 *
	 */
	private static Map getAttributesWithoutAwtFont(Map attributes, JRFont font)
	{
		attributes.put(TextAttribute.FAMILY, font.getFontName());

		attributes.put(TextAttribute.SIZE, new Float(font.getFontSize()));

		if (font.isBold())
		{
			attributes.put(TextAttribute.WEIGHT, TextAttribute.WEIGHT_BOLD);
		}
		if (font.isItalic())
		{
			attributes.put(TextAttribute.POSTURE, TextAttribute.POSTURE_OBLIQUE);
		}
		if (font.isUnderline())
		{
			attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
		}
		if (font.isStrikeThrough())
		{
			attributes.put(TextAttribute.STRIKETHROUGH, TextAttribute.STRIKETHROUGH_ON);
		}
		
		attributes.put(JRTextAttribute.PDF_FONT_NAME, font.getPdfFontName());
		attributes.put(JRTextAttribute.PDF_ENCODING, font.getPdfEncoding());

		if (font.isPdfEmbedded())
		{
			attributes.put(JRTextAttribute.IS_PDF_EMBEDDED, Boolean.TRUE);
		}

		return attributes;
	}


	/**
	 * Returns font information containing the font family, font face and font style.
	 * 
	 * @param name the font family or font face name
	 * @param locale the locale
	 * @return a font info object
	 */
	public static FontInfo getFontInfo(String name, Locale locale)
	{
		//FIXMEFONT do some cache
		List bundles = ExtensionsEnvironment.getExtensionsRegistry().getExtensions(FontBundle.class);
		for (Iterator itb = bundles.iterator(); itb.hasNext();)
		{
			FontBundle bundle = (FontBundle)itb.next();
			List families = bundle.getFontFamilies();
			for (Iterator itf = families.iterator(); itf.hasNext();)
			{
				FontFamily family = (FontFamily)itf.next();
				if (locale == null || family.supportsLocale(locale))
				{
					if (name.equals(family.getName()))
					{
						return new FontInfo(family, null, Font.PLAIN);
					}
					FontFace face = family.getNormalFace();
					if (face != null && name.equals(face.getName()))
					{
						return new FontInfo(family, face, Font.PLAIN);
					}
					face = family.getBoldFace();
					if (face != null && name.equals(face.getName()))
					{
						return new FontInfo(family, face, Font.BOLD);
					}
					face = family.getItalicFace();
					if (face != null && name.equals(face.getName()))
					{
						return new FontInfo(family, face, Font.ITALIC);
					}
					face = family.getBoldItalicFace();
					if (face != null && name.equals(face.getName()))
					{
						return new FontInfo(family, face, Font.BOLD | Font.ITALIC);
					}
				}
			}
		}
		//throw new JRRuntimeException("Font family/face named '" + name + "' not found.");
		return null;
	}


	/**
	 *
	 */
	public static Font getAwtFontFromBundles(String name, int style, int size, Locale locale)
	{
		Font awtFont = null;
		FontInfo fontInfo = getFontInfo(name, locale);
		
		if (fontInfo != null)
		{
			int faceStyle = Font.PLAIN;
			FontFamily family = fontInfo.getFontFamily();
			FontFace face = fontInfo.getFontFace();
			if (face == null)
			{
				if (((style & Font.BOLD) > 0) && ((style & Font.ITALIC) > 0))
				{
					face = family.getBoldItalicFace();
					faceStyle = Font.BOLD | Font.ITALIC;
				}
				
				if (face == null && ((style & Font.BOLD) > 0))
				{
					face = family.getBoldFace();
					faceStyle = Font.BOLD;
				}
				
				if (face == null && ((style & Font.ITALIC) > 0))
				{
					face = family.getItalicFace();
					faceStyle = Font.ITALIC;
				}
				
				if (face == null)
				{
					face = family.getNormalFace();
					faceStyle = Font.PLAIN;
				}
					
				if (face == null)
				{
					throw new JRRuntimeException("Font family '" + family.getName() + "' does not have the normal font face.");
				}
			}
			else
			{
				faceStyle = fontInfo.getStyle();
			}

			awtFont = face.getFont();
			if (awtFont == null)
			{
				throw new JRRuntimeException("The '" + face.getName() + "' font face in family '" + family.getName() + "' returns a null font.");
			}

			awtFont = awtFont.deriveFont((float)size);
			
			awtFont = FONT_DERIVER.deriveFont(awtFont, name, style, faceStyle);
		}
		
		return awtFont;
	}

	
	/**
	 *
	 */
	public static Font getAwtFont(JRFont font, Locale locale)
	{
		if (font == null)
		{
			return null;
		}
		
		Font awtFont = 
			getAwtFontFromBundles(
				font.getFontName(), 
				((font.isBold()?Font.BOLD:Font.PLAIN)|(font.isItalic()?Font.ITALIC:Font.PLAIN)), 
				font.getFontSize(),
				locale
				);
		
		if (awtFont == null)
		{
			awtFont = new Font(getAttributesWithoutAwtFont(new HashMap(), font));//FIXMEFONT this is not working in 1.6?
		}
		
		return awtFont;
	}
	
	
}

interface AwtFontDeriver
{
	public Font deriveFont(Font font, String name, int style, int faceStyle);
	
	public static final AwtFontDeriver JDK14_AWT_FONT_DERIVER = 
		new AwtFontDeriver()
		{
			public Font deriveFont(Font font, String name, int style, int faceStyle)
			{
				Map attrs = new HashMap();
				attrs.putAll(font.getAttributes());
				attrs.put(TextAttribute.FAMILY, name);
				if ((style & Font.BOLD) > 0)
				{
					attrs.put(TextAttribute.WEIGHT, TextAttribute.WEIGHT_BOLD);
				}

				if ((style & Font.ITALIC) > 0)
				{
					attrs.put(TextAttribute.POSTURE, TextAttribute.POSTURE_OBLIQUE);
				}
				
				return new Font(attrs);
			}
		};

	public static final AwtFontDeriver DEFAULT_AWT_FONT_DERIVER = 
		new AwtFontDeriver()
		{
			public Font deriveFont(Font font, String name, int style, int faceStyle)
			{
				return font.deriveFont(style & ~faceStyle);
			}
		};
}