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
package net.sf.jasperreports.engine.export.ooxml;

import java.awt.Color;
import java.awt.font.TextAttribute;
import java.io.Writer;
import java.text.AttributedCharacterIterator.Attribute;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.StringTokenizer;

import net.sf.jasperreports.engine.JRPrintText;
import net.sf.jasperreports.engine.JRStyle;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.base.JRBasePrintText;
import net.sf.jasperreports.engine.fonts.FontFamily;
import net.sf.jasperreports.engine.fonts.FontInfo;
import net.sf.jasperreports.engine.fonts.FontUtil;
import net.sf.jasperreports.engine.type.ColorEnum;
import net.sf.jasperreports.engine.type.ModeEnum;
import net.sf.jasperreports.engine.util.JRColorUtil;
import net.sf.jasperreports.engine.util.JRStringUtil;
import net.sf.jasperreports.engine.util.JRStyleResolver;


/**
 * @author sanda zaharia (shertage@users.sourceforge.net)
 */
public class DocxRunHelper extends BaseHelper
{
	/**
	 *
	 */
	private String exporterKey;
	
	/**
	 *
	 */
	public DocxRunHelper(JasperReportsContext jasperReportsContext, Writer writer, String exporterKey)
	{
		super(jasperReportsContext, writer);
		this.exporterKey = exporterKey;
	}


	/**
	 *
	 */
	public void export(
			JRStyle style, 
			Map<Attribute,Object> attributes, 
			String text, 
			Locale locale, 
			boolean hiddenText, 
			String invalidCharReplacement, 
			Color backcolor, 
			boolean isNewLineAsParagraph)
	{
		if (text != null)
		{
			write("      <w:r>\n");
			boolean highlightText = backcolor == null || !backcolor.equals(attributes.get(TextAttribute.BACKGROUND));
			exportProps(
					getAttributes(style), 
					attributes, 
					locale, 
					hiddenText, 
					highlightText 
				);
			
			StringTokenizer tkzer = new StringTokenizer(text, "\n", true);
			while(tkzer.hasMoreTokens())
			{
				String token = tkzer.nextToken();
				if ("\n".equals(token))
				{
					if(isNewLineAsParagraph)
					{
						write("<w:t xml:space=\"preserve\"><w:p/></w:t>\n");
					}
					else
					{
						write("<w:br/>");
					}
				}
				else
				{
					write("<w:t xml:space=\"preserve\">");
					write(JRStringUtil.xmlEncode(token, invalidCharReplacement));//FIXMEODT try something nicer for replace
					write("</w:t>\n");
				}
			}
			write("      </w:r>\n");
		}
	}

	/**
	 *
	 */
	public void exportProps(JRStyle style, Locale locale)
	{
		JRStyle baseStyle = JRStyleResolver.getBaseStyle(style);
		exportProps(
			getAttributes(baseStyle), 
			getAttributes(style), 
			locale, 
			false, 
			false
			);
	}

	/**
	 *
	 */
	public void exportProps(Map<Attribute,Object> parentAttrs,  Map<Attribute,Object> attrs, Locale locale, boolean hiddenText, boolean highlightText)
	{
		write("       <w:rPr>\n");
		
		Object value = attrs.get(TextAttribute.FAMILY);
		Object oldValue = parentAttrs.get(TextAttribute.FAMILY);
		
		if (value != null && !value.equals(oldValue))//FIXMEDOCX the text locale might be different from the report locale, resulting in different export font
		{
			String fontFamilyAttr = (String)value;
			String fontFamily = fontFamilyAttr;
			FontInfo fontInfo = FontUtil.getInstance(jasperReportsContext).getFontInfo(fontFamilyAttr, locale);
			if (fontInfo != null)
			{
				//fontName found in font extensions
				FontFamily family = fontInfo.getFontFamily();
				String exportFont = family.getExportFont(exporterKey);
				if (exportFont != null)
				{
					fontFamily = exportFont;
				}
			}
			write("        <w:rFonts w:ascii=\"" + fontFamily + "\" w:hAnsi=\"" + fontFamily + "\" w:eastAsia=\"" + fontFamily + "\" w:cs=\"" + fontFamily + "\" />\n");
		}
		
		value = attrs.get(TextAttribute.FOREGROUND);
		oldValue = parentAttrs.get(TextAttribute.FOREGROUND);
		
		if (value != null && !value.equals(oldValue))
		{
			write("        <w:color w:val=\"" + JRColorUtil.getColorHexa((Color)value) + "\" />\n");
		}
		
		if(highlightText)
		{
			value = attrs.get(TextAttribute.BACKGROUND);

			if (value != null && ColorEnum.getByColor((Color)value) != null)
			{
				//the highlight does not accept the color hexadecimal expression, but only few color names
				write("        <w:highlight w:val=\"" + ColorEnum.getByColor((Color)value).getName() + "\" />\n");
			}
		}
		
		value = attrs.get(TextAttribute.SIZE);
		oldValue = parentAttrs.get(TextAttribute.SIZE);
		
		if (value != null && !value.equals(oldValue))
		{
			float fontSize = ((Float)value).floatValue();
			fontSize = fontSize == 0 ? 0.5f : fontSize;// only the special EMPTY_CELL_STYLE would have font size zero
			write("        <w:sz w:val=\"" + (int)(2 * fontSize) + "\" />\n");
		}
		
		value = attrs.get(TextAttribute.WEIGHT);
		oldValue = parentAttrs.get(TextAttribute.WEIGHT);
		
		if (value != null && !value.equals(oldValue))
		{
			write("        <w:b w:val=\"" + value.equals(TextAttribute.WEIGHT_BOLD) + "\"/>\n");
		}
		
		value = attrs.get(TextAttribute.POSTURE);
		oldValue = parentAttrs.get(TextAttribute.POSTURE);
		
		if (value != null && !value.equals(oldValue))
		{
			write("        <w:i w:val=\"" + value.equals(TextAttribute.POSTURE_OBLIQUE) + "\"/>\n");
		}
		
		
		value = attrs.get(TextAttribute.UNDERLINE);
		oldValue = parentAttrs.get(TextAttribute.UNDERLINE);
		
		if (
				(value == null && oldValue != null)
				|| (value != null && !value.equals(oldValue))
				)
		{
			write("        <w:u w:val=\"" + (value == null ? "none" : "single") + "\"/>\n");
		}
		
		value = attrs.get(TextAttribute.STRIKETHROUGH);
		oldValue = parentAttrs.get(TextAttribute.STRIKETHROUGH);
		
		if (
				(value == null && oldValue != null)
				|| (value != null && !value.equals(oldValue))
				)
		{
			write("        <w:strike w:val=\"" + (value != null) + "\"/>\n");
		}
		
		value = attrs.get(TextAttribute.SUPERSCRIPT);
		
		if (TextAttribute.SUPERSCRIPT_SUPER.equals(value))
		{
			write("        <w:vertAlign w:val=\"superscript\" />\n");
		}
		else if (TextAttribute.SUPERSCRIPT_SUB.equals(value))
		{
			write("        <w:vertAlign w:val=\"subscript\" />\n");
		}
		
		if (hiddenText)
		{
			write("        <w:vanish/>\n");
		}
		
		write("       </w:rPr>\n");
	}
	
	
	/**
	 *
	 */
	private Map<Attribute,Object> getAttributes(JRStyle style)//FIXMEDOCX put this in util?
	{
		Map<Attribute,Object> styledTextAttributes = new HashMap<Attribute,Object>(); 

		if (style != null)
		{
			JRPrintText text = new JRBasePrintText(null);
			text.setStyle(style);
			
			//JRFontUtil.getAttributes(styledTextAttributes, text, (Locale)null);//FIXMEDOCX getLocale());
			FontUtil.getInstance(jasperReportsContext).getAttributesWithoutAwtFont(styledTextAttributes, text);
			styledTextAttributes.put(TextAttribute.FOREGROUND, text.getForecolor());
			if (text.getModeValue() == ModeEnum.OPAQUE)
			{
				styledTextAttributes.put(TextAttribute.BACKGROUND, text.getBackcolor());
			}
		}

		return styledTextAttributes;
	}

}

