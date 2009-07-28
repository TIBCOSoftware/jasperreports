/*
 * ============================================================================
 * GNU Lesser General Public License
 * ============================================================================
 *
 * JasperReports - Free Java report-generating library.
 * Copyright (C) 2001-2009 JasperSoft Corporation http://www.jaspersoft.com
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
 * 539 Bryant Street, Suite 100
 * San Francisco, CA 94107
 * http://www.jaspersoft.com
 */
package net.sf.jasperreports.engine.export.ooxml;

import java.awt.Color;
import java.awt.font.TextAttribute;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import net.sf.jasperreports.engine.JRElement;
import net.sf.jasperreports.engine.JRPrintText;
import net.sf.jasperreports.engine.JRStyle;
import net.sf.jasperreports.engine.base.JRBasePrintText;
import net.sf.jasperreports.engine.util.JRColorUtil;
import net.sf.jasperreports.engine.util.JRFontUtil;
import net.sf.jasperreports.engine.util.JRStringUtil;


/**
 * @author sanda zaharia (shertage@users.sourceforge.net)
 * @version $Id: StyleCache.java 2908 2009-07-21 14:32:01Z teodord $
 */
public class RunHelper extends BaseHelper
{
	/**
	 *
	 */
	private Map fontMap = null;


	/**
	 *
	 */
	public RunHelper(Writer writer, Map fontMap)
	{
		super(writer);
		this.fontMap = fontMap;
	}


	/**
	 *
	 */
	public void export(JRStyle style, Map attributes, String text) throws IOException
	{
		writer.write("      <w:r>\n");
		
		exportProps(getAttributes(style), attributes);
		
		writer.write("<w:t xml:space=\"preserve\">");
		if (text != null)
		{
			writer.write(JRStringUtil.xmlEncode(text));//FIXMEODT try something nicer for replace
		}
		writer.write("</w:t>\n");
		writer.write("      </w:r>\n");
	}

	/**
	 *
	 */
	public void exportProps(JRStyle style) throws IOException
	{
		JRPrintText text = new JRBasePrintText(null);
		text.setStyle(style);
		Map styledTextAttributes = new HashMap(); 
		JRFontUtil.getAttributes(styledTextAttributes, text, (Locale)null);//FIXMEDOCX getLocale());
		styledTextAttributes.put(TextAttribute.FOREGROUND, text.getForecolor());
		if (style.getMode() == null || style.getMode().byteValue() == JRElement.MODE_OPAQUE)
		{
			styledTextAttributes.put(TextAttribute.BACKGROUND, style.getBackcolor());
		}

		exportProps(getAttributes(style.getStyle()), getAttributes(style));
	}

	/**
	 *
	 */
	public void exportProps(Map parentAttrs,  Map attrs) throws IOException
	{
		writer.write("       <w:rPr>\n");

		Object value = attrs.get(TextAttribute.FAMILY);
		Object oldValue = parentAttrs.get(TextAttribute.FAMILY);
		
		if (value != null && !value.equals(oldValue))
		{
			String fontFamily;
			String fontFamilyAttr = (String)value;
			if (fontMap != null && fontMap.containsKey(fontFamilyAttr))
			{
				fontFamily = (String) fontMap.get(fontFamilyAttr);
			}
			else
			{
				fontFamily = fontFamilyAttr;
			}
			writer.write("        <w:rFonts w:ascii=\"" + fontFamily + "\" />\n");
		}
		
		value = attrs.get(TextAttribute.FOREGROUND);
		oldValue = parentAttrs.get(TextAttribute.FOREGROUND);
		
		if (value != null && !value.equals(oldValue))
		{
			writer.write("        <w:color w:val=\"" + JRColorUtil.getColorHexa((Color)value) + "\" />\n");
		}

		value = attrs.get(TextAttribute.BACKGROUND);
		oldValue = parentAttrs.get(TextAttribute.BACKGROUND);
		
		if (value != null && !value.equals(oldValue))
		{
			//FIXME: the highlight does not accept the color hexadecimal expression, but only few color names
//			writer.write("        <w:highlight w:val=\"" + JRColorUtil.getColorHexa((Color)value) + "\" />\n");
		}

		value = attrs.get(TextAttribute.SIZE);
		oldValue = parentAttrs.get(TextAttribute.SIZE);

		if (value != null && !value.equals(oldValue))
		{
			writer.write("        <w:sz w:val=\"" + (2 * ((Float)value).floatValue()) + "\" />\n");
		}
		
		value = attrs.get(TextAttribute.WEIGHT);
		oldValue = parentAttrs.get(TextAttribute.WEIGHT);

		if (value != null && !value.equals(oldValue))
		{
			writer.write("        <w:b w:val=\"" + value.equals(TextAttribute.WEIGHT_BOLD) + "\"/>\n");
		}

		value = attrs.get(TextAttribute.POSTURE);
		oldValue = parentAttrs.get(TextAttribute.POSTURE);

		if (value != null && !value.equals(oldValue))
		{
			writer.write("        <w:i w:val=\"" + value.equals(TextAttribute.POSTURE_OBLIQUE) + "\"/>\n");
		}


		value = attrs.get(TextAttribute.UNDERLINE);
		oldValue = parentAttrs.get(TextAttribute.UNDERLINE);

		if (
			(value == null && oldValue != null)
			|| (value != null && !value.equals(oldValue))
			)
		{
			writer.write("        <w:u w:val=\"" + (value == null ? "none" : "single") + "\"/>\n");
		}
		
		value = attrs.get(TextAttribute.STRIKETHROUGH);
		oldValue = parentAttrs.get(TextAttribute.STRIKETHROUGH);

		if (
			(value == null && oldValue != null)
			|| (value != null && !value.equals(oldValue))
			)
		{
			writer.write("        <w:strike w:val=\"" + (value != null) + "\"/>\n");
		}

		value = attrs.get(TextAttribute.SUPERSCRIPT);

		if (TextAttribute.SUPERSCRIPT_SUPER.equals(value))
		{
			writer.write("        <w:vertAlign w:val=\"superscript\" />\n");
		}
		else if (TextAttribute.SUPERSCRIPT_SUB.equals(value))
		{
			writer.write("        <w:vertAlign w:val=\"subscript\" />\n");
		}

		writer.write("       </w:rPr>\n");
	}


	/**
	 *
	 */
	private Map getAttributes(JRStyle style) throws IOException//FIXMEDOCX put this in util?
	{
		JRPrintText text = new JRBasePrintText(null);
		text.setStyle(style);
		
		Map styledTextAttributes = new HashMap(); 
		JRFontUtil.getAttributes(styledTextAttributes, text, (Locale)null);//FIXMEDOCX getLocale());
		styledTextAttributes.put(TextAttribute.FOREGROUND, text.getForecolor());
		if (text.getMode() == JRElement.MODE_OPAQUE)
		{
			styledTextAttributes.put(TextAttribute.BACKGROUND, text.getBackcolor());
		}

		return styledTextAttributes;
	}

}

