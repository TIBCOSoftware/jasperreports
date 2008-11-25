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
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRFont;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.fonts.FontBundle;
import net.sf.jasperreports.engine.fonts.FontEntry;
import net.sf.jasperreports.engine.fonts.LiteFont;
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
		attributes.put(JRTextAttribute.FONT_NAME, font.getFontName());

		//Font awtFont = getAwtFont(font);//FIXMEFONT optimize so that we don't load the AWT font for all exporters.
		Font awtFont = 
			getAwtFont(
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
	 * Returns a font entry by name.
	 * 
	 * @param name the font entry name
	 * @return a font entry
	 */
	public static FontEntry getFontEntry(String name)
	{
		//FIXMEFONT do some cache
		List bundles = ExtensionsEnvironment.getExtensionsRegistry().getExtensions(FontBundle.class);
		for (Iterator it = bundles.iterator(); it.hasNext();)
		{
			FontBundle bundle = (FontBundle)it.next();
			FontEntry fontEntry = bundle.getFontEntry(name);
			if (fontEntry != null)
			{
				return fontEntry;
			}
		}
		//throw new JRRuntimeException("Font entry named '" + name + "' not found.");
		return null;
	}


	/**
	 *
	 */
	public static Font getAwtFont(String name, int style, int size)
	{
		Font awtFont = null;
		FontEntry fontEntry = getFontEntry(name);
		
		if (fontEntry != null)
		{
			boolean isSimulatedBold = false;
			boolean isSimulatedItalic = false;
			 
			String ttf = null;
			
			if (((style & Font.BOLD) > 0) && ((style & Font.ITALIC) > 0))
			{
				ttf = fontEntry.getBoldItalic();
				if (ttf == null)
				{
					isSimulatedBold = true;
					isSimulatedItalic = true;
				}
			}
			
			if (ttf == null && ((style & Font.BOLD) > 0))
			{
				ttf = fontEntry.getBold();
				if (ttf == null)
				{
					isSimulatedBold = true;
				}
			}
			
			if (ttf == null && ((style & Font.ITALIC) > 0))
			{
				ttf = fontEntry.getItalic();
				if (ttf == null)
				{
					isSimulatedItalic = true;
				}
			}
			
			if (ttf == null)
			{
				ttf = fontEntry.getNormal();
			}
				
			if (ttf == null)
			{
				throw new JRRuntimeException("Font entry '" + name + "' does not have the normal font set.");
			}

			try
			{
				awtFont = //FIXMEFONT do some cache here
					Font.createFont(
						Font.TRUETYPE_FONT, 
						JRLoader.getResourceInputStream(ttf)
						);
			}
			catch(Exception e)
			{
				throw new JRRuntimeException(e);
			}

			awtFont = awtFont.deriveFont((float)size);

//			if ((style & Font.BOLD) > 0)
//			//if (isSimulatedBold)
//			{
//				awtFont = awtFont.deriveFont(Font.BOLD);
//			}
//			if ((style & Font.ITALIC) > 0)
//			//if (isSimulatedItalic)
//			{
//				awtFont = awtFont.deriveFont(Font.ITALIC);
//			}
			awtFont = awtFont.deriveFont(style);
		}
		
		return awtFont;
	}

	
	/**
	 *
	 */
	public static LiteFont getLiteFont(String name, int style, int size)
	{
		Font awtFont = getAwtFont(name, style, size);
		
		if (awtFont != null)
		{
			return new LiteFont(name, awtFont.getAttributes());
		}
		
		return null;
	}
	
	
	/**
	 *
	 */
	public static LiteFont getLiteFont(JRFont font)
	{
		return 
			getLiteFont(
				font.getFontName(), 
				((font.isBold()?Font.BOLD:Font.PLAIN)|(font.isItalic()?Font.ITALIC:Font.PLAIN)), 
				font.getFontSize()
				);
	}
	
	
	/**
	 *
	 *
	public static Font getAwtFont(JRFont font)
	{
		Font awtFont = null;
		FontEntry fontEntry = getFontEntry(font.getFontName());
		
		if (fontEntry != null)
		{
			boolean isSimulatedBold = false;
			boolean isSimulatedItalic = false;
			 
			try
			{
				String ttf = null;
				
				if (font.isBold() && font.isItalic())
				{
					ttf = fontEntry.getBoldItalic();
					if (ttf == null)
					{
						isSimulatedBold = true;
						isSimulatedItalic = true;
					}
				}
				
				if (ttf == null && font.isBold())
				{
					ttf = fontEntry.getBold();
					if (ttf == null)
					{
						isSimulatedBold = true;
					}
				}
				
				if (ttf == null && font.isItalic())
				{
					ttf = fontEntry.getItalic();
					if (ttf == null)
					{
						isSimulatedItalic = true;
					}
				}
				
				if (ttf == null)
				{
					ttf = fontEntry.getNormal();
				}
					
				awtFont = //FIXMEFONT do some cache here
					Font.createFont(
						Font.TRUETYPE_FONT, 
						JRLoader.getResourceInputStream(ttf)
						);
			}
			catch(Exception e)
			{
				throw new JRRuntimeException(e);
			}

//			Map attributes = new HashMap();
//			attributes.putAll(awtFont.getAttributes());
//			attributes.put(JRTextAttribute.FONT_NAME, font.getFontName());//FIXMEFONT maybe create only one getLiteFont method to be used everywhere
//			awtFont = new Font(attributes);

			awtFont = awtFont.deriveFont((float)font.getFontSize());

			if (isSimulatedBold)
			{
				awtFont = awtFont.deriveFont(Font.BOLD);
			}
			if (isSimulatedItalic)
			{
				awtFont = awtFont.deriveFont(Font.ITALIC);
			}
		}
		
		return awtFont;
	}

	
	/**
	 *
	 *
	public static LiteFont getLiteFont(JRFont font)
	{
		Font awtFont = getAwtFont(font);
		
		if (awtFont != null)
		{
			return new LiteFont(awtFont.getAttributes());
		}
		
		return null;
	}
	*/

	
}
