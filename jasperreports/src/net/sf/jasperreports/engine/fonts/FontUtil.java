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
package net.sf.jasperreports.engine.fonts;

import java.awt.Font;
import java.awt.font.TextAttribute;
import java.text.AttributedCharacterIterator.Attribute;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import net.sf.jasperreports.engine.JRFont;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.util.JRFontNotFoundException;
import net.sf.jasperreports.engine.util.JRGraphEnvInitializer;
import net.sf.jasperreports.engine.util.JRTextAttribute;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public final class FontUtil
{
	private static final Log log = LogFactory.getLog(FontUtil.class);
	public static final String EXCEPTION_MESSAGE_KEY_NULL_FONT = "engine.fonts.null.font";

	private JasperReportsContext jasperReportsContext;


	/**
	 *
	 */
	private FontUtil(JasperReportsContext jasperReportsContext)
	{
		this.jasperReportsContext = jasperReportsContext;
	}
	
	
	/**
	 *
	 */
	public static FontUtil getInstance(JasperReportsContext jasperReportsContext)
	{
		return new FontUtil(jasperReportsContext);
	}
	
	
	/**
	 *.
	 */ //FIXMECONTEXT this should no longer be a thread local
	private static final InheritableThreadLocal<Set<String>> threadMissingFontsCache = new InheritableThreadLocal<Set<String>>()
	{
		@Override
		protected Set<String> initialValue() {
			return new HashSet<String>();
		}
	};
	
	/**
	 *
	 */
	public static void copyNonNullOwnProperties(JRFont srcFont, JRFont destFont)
	{
		if(srcFont != null && destFont != null)
		{
			if (srcFont.getOwnFontName() != null)
			{
				destFont.setFontName(srcFont.getOwnFontName());
			}
			if (srcFont.isOwnBold() != null)
			{
				destFont.setBold(srcFont.isOwnBold());
			}
			if (srcFont.isOwnItalic() != null)
			{
				destFont.setItalic(srcFont.isOwnItalic());
			}
			if (srcFont.isOwnUnderline() != null)
			{
				destFont.setUnderline(srcFont.isOwnUnderline());
			}
			if (srcFont.isOwnStrikeThrough() != null)
			{
				destFont.setStrikeThrough(srcFont.isOwnStrikeThrough());
			}
			if (srcFont.getOwnFontsize() != null)
			{
				destFont.setFontSize(srcFont.getOwnFontsize());
			}
			if (srcFont.getOwnPdfFontName() != null)
			{
				destFont.setPdfFontName(srcFont.getOwnPdfFontName());
			}
			if (srcFont.getOwnPdfEncoding() != null)
			{
				destFont.setPdfEncoding(srcFont.getOwnPdfEncoding());
			}
			if (srcFont.isOwnPdfEmbedded() != null)
			{
				destFont.setPdfEmbedded(srcFont.isOwnPdfEmbedded());
			}
		}
	}
	

	/**
	 *
	 */
	public Map<Attribute,Object> getAttributesWithoutAwtFont(Map<Attribute,Object> attributes, JRFont font)
	{
		attributes.put(TextAttribute.FAMILY, font.getFontName());

		attributes.put(TextAttribute.SIZE, font.getFontsize());

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
	public FontInfo getFontInfo(String name, Locale locale)
	{
		//FIXMEFONT do some cache
		List<FontFamily> families = jasperReportsContext.getExtensions(FontFamily.class);
		for (Iterator<FontFamily> itf = families.iterator(); itf.hasNext();)
		{
			FontFamily family = itf.next();
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
		//throw new JRRuntimeException("Font family/face named '" + name + "' not found.");
		return null;
	}


	/**
	 * Returns the font family names available through extensions, in alphabetical order.
	 */
	public Collection<String> getFontFamilyNames()
	{
		TreeSet<String> familyNames = new TreeSet<String>();//FIXMEFONT use collator for order?
		//FIXMEFONT do some cache
		List<FontFamily> families = jasperReportsContext.getExtensions(FontFamily.class);
		for (Iterator<FontFamily> itf = families.iterator(); itf.hasNext();)
		{
			FontFamily family = itf.next();
			if (family.isVisible())
			{
				familyNames.add(family.getName());
			}
		}
		return familyNames;
	}


	/**
	 * @deprecated Replaced by {@link #getAwtFontFromBundles(String, int, float, Locale, boolean)}.
	 */
	public Font getAwtFontFromBundles(String name, int style, int size, Locale locale, boolean ignoreMissingFont)
	{
		return getAwtFontFromBundles(name, style, (float)size, locale, ignoreMissingFont);
	}


	/**
	 *
	 */
	public Font getAwtFontFromBundles(String name, int style, float size, Locale locale, boolean ignoreMissingFont)
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
				
				if ((face == null || face.getFont() == null) && ((style & Font.BOLD) > 0))
				{
					face = family.getBoldFace();
					faceStyle = Font.BOLD;
				}
				
				if ((face == null || face.getFont() == null) && ((style & Font.ITALIC) > 0))
				{
					face = family.getItalicFace();
					faceStyle = Font.ITALIC;
				}
				
				if (face == null || face.getFont() == null)
				{
					face = family.getNormalFace();
					faceStyle = Font.PLAIN;
				}
					
//				if (face == null)
//				{
//					throw new JRRuntimeException("Font family '" + family.getName() + "' does not have the normal font face.");
//				}
			}
			else
			{
				faceStyle = fontInfo.getStyle();
			}

			if (face == null || face.getFont() == null)
			{
				// None of the family's font faces was found to match, neither by name, nor by style and the font family does not even specify a normal face font.
				// In such case, we take the family name and consider it as JVM available font name.
				checkAwtFont(family.getName(), ignoreMissingFont);
				
				awtFont = new Font(family.getName(), style, (int)size);
				awtFont = awtFont.deriveFont(size);
			}
			else
			{
				awtFont = face.getFont();
				if (awtFont == null)
				{
					throw 
						new JRRuntimeException(
							EXCEPTION_MESSAGE_KEY_NULL_FONT,
							new Object[]{face.getName(), family.getName()});
				}

				awtFont = awtFont.deriveFont(size);
				
				awtFont = awtFont.deriveFont(style & ~faceStyle);
			}
		}
		
		return awtFont;
	}

	
	/**
	 *
	 */ //FIXMECONTEXT check how to make this cache effective again
	public void resetThreadMissingFontsCache()
	{
		threadMissingFontsCache.set(new HashSet<String>());
	}
	
	
	/**
	 *
	 */
	public void checkAwtFont(String name, boolean ignoreMissingFont)
	{
		if (!JRGraphEnvInitializer.isAwtFontAvailable(name))
		{
			if (ignoreMissingFont)
			{
				Set<String> missingFontNames = threadMissingFontsCache.get();
				if (!missingFontNames.contains(name))
				{
					missingFontNames.add(name);
					if (log.isWarnEnabled())
					{
						log.warn("Font '" + name + "' is not available to the JVM. For more details, see http://jasperreports.sourceforge.net/api/net/sf/jasperreports/engine/util/JRFontNotFoundException.html");
					}
				}
			}
			else
			{
				throw new JRFontNotFoundException(name);
			}
		}
	}

	
	/**
	 * Returns a java.awt.Font instance by converting a JRFont instance.
	 * Mostly used in combination with third-party visualization packages such as JFreeChart (for chart themes).
	 * Unless the font parameter is null, this method always returns a non-null AWT font, regardless whether it was
	 * found in the font extensions or not. This is because we do need a font to draw with and there is no point
	 * in raising a font missing exception here, as it is not JasperReports who does the drawing. 
	 */
	public Font getAwtFont(JRFont font, Locale locale)
	{
		if (font == null)
		{
			return null;
		}
		
		// ignoring missing font as explained in the Javadoc
		Font awtFont = 
			getAwtFontFromBundles(
				font.getFontName(), 
				((font.isBold()?Font.BOLD:Font.PLAIN)|(font.isItalic()?Font.ITALIC:Font.PLAIN)), 
				font.getFontsize(),
				locale,
				true
				);
		
		if (awtFont == null)
		{
			awtFont = new Font(getAttributesWithoutAwtFont(new HashMap<Attribute,Object>(), font));
		}
		else
		{
			// add underline and strikethrough attributes since these are set at
			// style/font level
			Map<Attribute, Object> attributes = new HashMap<Attribute, Object>();
			if (font.isUnderline())
			{
				attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
			}
			if (font.isStrikeThrough())
			{
				attributes.put(TextAttribute.STRIKETHROUGH, TextAttribute.STRIKETHROUGH_ON);
			}
			
			if (!attributes.isEmpty())
			{
				awtFont = awtFont.deriveFont(attributes);
			}
		}
		
		return awtFont;
	}
	
	
	private FontUtil()
	{
	}
}
