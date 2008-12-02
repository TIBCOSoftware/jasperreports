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
	public static Map getAttributes(Map attributes, JRFont font)
	{
		//Font awtFont = getAwtFont(font);//FIXMEFONT optimize so that we don't load the AWT font for all exporters.
		Font awtFont = 
			getAwtFontFromBundles(
				font.getFontName(), 
				((font.isBold()?Font.BOLD:Font.PLAIN)|(font.isItalic()?Font.ITALIC:Font.PLAIN)), 
				font.getFontSize()
				);
		if (awtFont != null)
		{
			attributes.put(TextAttribute.FONT, awtFont); 
		}
		
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
	 * @return a font info object
	 */
	public static FontInfo getFontInfo(String name)
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
	 *
	 */
	public static Font getAwtFontFromBundles(String name, int style, int size)
	{
		Font awtFont = null;
		FontInfo fontInfo = getFontInfo(name);
		
		if (fontInfo != null)
		{
			String ttf = null;
			int faceStyle = Font.PLAIN;
			FontFace face = fontInfo.getFontFace();
			if (face == null)
			{
				FontFamily family = fontInfo.getFontFamily();
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
					throw new JRRuntimeException("Font family '" + name + "' does not have the normal font face.");
				}
			}
			else
			{
				faceStyle = fontInfo.getStyle();
			}

			ttf = face.getFile();

			if (ttf == null)
			{
				//FIXMEFONT throw something
			}
			
			try
			{
				awtFont = //FIXMEFONT do some cache here
					Font.createFont(
						Font.TRUETYPE_FONT, 
						JRLoader.getLocationInputStream(ttf)//FIXMEFONT close stream
						);
			}
			catch(Exception e)
			{
				throw new JRRuntimeException(e);
			}

			awtFont = awtFont.deriveFont((float)size);
			
			String javaVersion = System.getProperty("java.version");
			if (javaVersion.startsWith("1.4"))
			{
				Map attrs = new HashMap();
				attrs.putAll(awtFont.getAttributes());
				attrs.put(TextAttribute.FAMILY, name);
				if ((style & Font.BOLD) > 0)
				{
					attrs.put(TextAttribute.WEIGHT, TextAttribute.WEIGHT_BOLD);
				}

				if ((style & Font.ITALIC) > 0)
				{
					attrs.put(TextAttribute.POSTURE, TextAttribute.POSTURE_OBLIQUE);
				}
				
				awtFont = new Font(attrs);
			}
			else
			{
				awtFont = awtFont.deriveFont(style | faceStyle);//FIXMEFONT this is not good
			}
			
		}
		
		return awtFont;
	}

	
	/**
	 *
	 */
	public static Font getAwtFont(JRFont font)
	{
		if (font == null)
		{
			return null;
		}
		
		Font awtFont = 
			getAwtFontFromBundles(
				font.getFontName(), 
				((font.isBold()?Font.BOLD:Font.PLAIN)|(font.isItalic()?Font.ITALIC:Font.PLAIN)), 
				font.getFontSize()
				);
		
		if (awtFont == null)
		{
			awtFont = new Font(getAttributes(new HashMap(), font));//FIXMEFONT this is not working in 1.6?
		}
		
		return awtFont;
	}
	
	
}
