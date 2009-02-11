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
package net.sf.jasperreports.engine.export.xmlss;

import java.awt.Color;
import java.io.IOException;
import java.io.Writer;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.sf.jasperreports.engine.JRBoxContainer;
import net.sf.jasperreports.engine.JRCommonGraphicElement;
import net.sf.jasperreports.engine.JRFont;
import net.sf.jasperreports.engine.JRPrintElement;


/**
 * @author sanda zaharia (shertage@users.sourceforge.net)
 * @version $Id$
 */
public class XmlssStyleCache
{
	/**
	 *
	 */
	private Writer styleWriter = null;
	private Map fontMap = null;
	private Set fontFaces = new HashSet();

	/**
	 *
	 */
//	private Map frameStyles = new HashMap();//FIXMEODT soft cache?
//	private int frameStylesCounter = 0;
	private Map cellStyles = new HashMap();
	private int cellStylesCounter = 0;
//	private Map graphicStyles = new HashMap();
//	private int graphicStylesCounter = 0;
//	private Map paragraphStyles = new HashMap();
//	private int paragraphStylesCounter = 0;
//	private Map textSpanStyles = new HashMap();
//	private int textSpanStylesCounter = 0;


	/**
	 *
	 */
	public XmlssStyleCache(Writer styleWriter, Map fontMap)
	{
		this.styleWriter = styleWriter;
		this.fontMap = fontMap;
	}


	/**
	 *
	 */
	public Collection getFontFaces()
	{
		return fontFaces;
	}


	/**
	 *
	 */
//	public String getFrameStyle(JRPrintText text) throws IOException
//	{
//		XmlssFrameStyle xmlssFrameStyle  = new XmlssFrameStyle(styleWriter, text);
//		xmlssFrameStyle.setBox(text.getLineBox());
//		
//		String frameStyleId = xmlssFrameStyle.getId();
//		String frameStyleName = (String)frameStyles.get(frameStyleId);
//		
//		if (frameStyleName == null)
//		{
//			frameStyleName = "F" + frameStylesCounter++;
//			frameStyles.put(frameStyleId, frameStyleName);
//			
//			xmlssFrameStyle.write(frameStyleName);
//		}
//		
//		return frameStyleName;
//	}


	/**
	 *
	 */
//	public String getFrameStyle(JRPrintElement element) throws IOException
//	{
//		XmlssFrameStyle xmlssFrameStyle  = new XmlssFrameStyle(styleWriter, element);
//		
//		String frameStyleId = xmlssFrameStyle.getId();
//		String frameStyleName = (String)frameStyles.get(frameStyleId);
//		
//		if (frameStyleName == null)
//		{
//			frameStyleName = "F" + frameStylesCounter++;
//			frameStyles.put(frameStyleId, frameStyleName);
//			
//			xmlssFrameStyle.write(frameStyleName);
//		}
//		
//		return frameStyleName;
//	}


	/**
	 *
	 */
//	public String getGraphicStyle(JRPrintGraphicElement element) throws IOException
//	{
//		XmlssGraphicStyle xmlssGraphicStyle  = new XmlssGraphicStyle(styleWriter, element);
//		
//		String graphicStyleId = xmlssGraphicStyle.getId();
//		String graphicStyleName = (String)cellStyles.get(graphicStyleId);
//		
//		if (graphicStyleName == null)
//		{
//			graphicStyleName = "G" + graphicStylesCounter++;
//			graphicStyles.put(graphicStyleId, graphicStyleName);
//			
//			xmlssGraphicStyle.write(graphicStyleName);
//		}
//		
//		return graphicStyleName;
//	}


	/**
	 *
	 */
	public String getCellStyle(JRPrintElement element, Color cellBackground, String pattern, boolean isShrinkToFit, JRFont defaultFont, Map fontMap) throws IOException
	{
		XmlssCellStyle xmlssCellStyle  = new XmlssCellStyle(styleWriter, element, cellBackground, pattern, isShrinkToFit, defaultFont, fontMap);

		if (element instanceof JRBoxContainer)
			xmlssCellStyle.setBox(((JRBoxContainer)element).getLineBox());
		if (element instanceof JRCommonGraphicElement)
			xmlssCellStyle.setPen(((JRCommonGraphicElement)element).getLinePen());
		
		String cellStyleId = xmlssCellStyle.getId();
		String cellStyleName = (String)cellStyles.get(cellStyleId);
		
		if (cellStyleName == null) 
		{
			cellStyleName = "C" + cellStylesCounter++;
			cellStyles.put(cellStyleId, cellStyleName);
			
			xmlssCellStyle.write(cellStyleName);
		}
		
		return cellStyleName;
	}


	/**
	 *
	 */
//	public String getParagraphStyle(JRPrintText text) throws IOException
//	{
//		XmlssParagraphStyle xmlssParagraphStyle  = new XmlssParagraphStyle(styleWriter, text);
//		
//		String paragraphStyleId = xmlssParagraphStyle.getId();
//		String paragraphStyleName = (String)paragraphStyles.get(paragraphStyleId);
//		
//		if (paragraphStyleName == null)
//		{
//			paragraphStyleName = "P" + paragraphStylesCounter++;
//			paragraphStyles.put(paragraphStyleId, paragraphStyleName);
//			
//			xmlssParagraphStyle.write(paragraphStyleName);
//		}
//		
//		return paragraphStyleName;
//	}


	/**
	 *
	 */
//	public String getTextSpanStyle(Map attributes, String text) throws IOException
//	{
//		String fontFamily;
//		String fontFamilyAttr = (String)attributes.get(TextAttribute.FAMILY);
//		if (fontMap != null && fontMap.containsKey(fontFamilyAttr))
//		{
//			fontFamily = (String) fontMap.get(fontFamilyAttr);
//		}
//		else
//		{
//			fontFamily = fontFamilyAttr;
//		}
//		fontFaces.add(fontFamily);
//		
//		StringBuffer textSpanStyleIdBuffer = new StringBuffer();
//		textSpanStyleIdBuffer.append(fontFamily);
//
//		String forecolorHexa = null;
//		Color forecolor = (Color)attributes.get(TextAttribute.FOREGROUND);
//		if (!Color.black.equals(forecolor))
//		{
//			forecolorHexa = JRColorUtil.getColorHexa(forecolor);
//			textSpanStyleIdBuffer.append(forecolorHexa);
//		}
//
//		String backcolorHexa = null;
//		Color runBackcolor = (Color)attributes.get(TextAttribute.BACKGROUND);
//		if (runBackcolor != null)
//		{
//			backcolorHexa = JRColorUtil.getColorHexa(runBackcolor);
//			textSpanStyleIdBuffer.append(backcolorHexa);
//		}
//
//		String size = String.valueOf(attributes.get(TextAttribute.SIZE));
//		textSpanStyleIdBuffer.append(size);
//
//		String weight = null;
//		if (TextAttribute.WEIGHT_BOLD.equals(attributes.get(TextAttribute.WEIGHT)))
//		{
//			weight = "bold";
//			textSpanStyleIdBuffer.append(weight);
//		}
//		String posture = null;
//		if (TextAttribute.POSTURE_OBLIQUE.equals(attributes.get(TextAttribute.POSTURE)))
//		{
//			posture = "italic";
//			textSpanStyleIdBuffer.append(posture);
//		}
//		String underline = null;
//		if (TextAttribute.UNDERLINE_ON.equals(attributes.get(TextAttribute.UNDERLINE)))
//		{
//			underline = "single";
//			textSpanStyleIdBuffer.append(underline);
//		}
//		String strikeThrough = null;
//		if (TextAttribute.STRIKETHROUGH_ON.equals(attributes.get(TextAttribute.STRIKETHROUGH)))
//		{
//			strikeThrough = "single";
//			textSpanStyleIdBuffer.append(strikeThrough);
//		}
//
////		if (TextAttribute.SUPERSCRIPT_SUPER.equals(attributes.get(TextAttribute.SUPERSCRIPT)))
////		{
////			textSpanStyleIdBuffer.append(" vertical-align: super;");
////		}
////		else if (TextAttribute.SUPERSCRIPT_SUB.equals(attributes.get(TextAttribute.SUPERSCRIPT)))
////		{
////			textSpanStyleIdBuffer.append(" vertical-align: sub;");
////		}
//
//		String textSpanStyleId = textSpanStyleIdBuffer.toString();
//		String textSpanStyleName = (String)textSpanStyles.get(textSpanStyleId);
//		
//		if (textSpanStyleName == null)
//		{
//			textSpanStyleName = "T" + textSpanStylesCounter++;
//			textSpanStyles.put(textSpanStyleId, textSpanStyleName);
//			
//			styleWriter.write("<style:style style:name=\"" + textSpanStyleName + "\"");
//			styleWriter.write(" style:family=\"text\">\n");
//			styleWriter.write("<style:text-properties");
//			if (forecolorHexa != null)
//			{
//				styleWriter.write(" fo:color=\"#" + forecolorHexa+ "\"");
//			}
//			styleWriter.write(" style:font-name=\"" + fontFamily + "\"");
//			styleWriter.write(" fo:font-size=\"" + size + "pt\"");
//			if (posture != null)
//			{
//				styleWriter.write(" fo:font-style=\"" + posture + "\"");
//			}
//			if (weight != null)
//			{
//				styleWriter.write(" fo:font-weight=\"" + weight + "\"");
//			}
//			if (backcolorHexa != null)
//			{
//				styleWriter.write(" fo:background-color=\"#" + backcolorHexa + "\"");
//			}
////			styleWriter.write(" style:text-rotation-angle=\"" + textRotationAngle + "\"");
////			styleWriter.write(" style:text-rotation-scale=\"" + textRotationScale + "\"");
//			if (underline != null)
//			{
//				styleWriter.write(" style:text-underline-type=\"" + underline + "\"");
//			}
//			if (strikeThrough != null)
//			{
//				styleWriter.write(" style:text-line-through-type=\"" + strikeThrough + "\"");
//			}
//			styleWriter.write(">\n");
//			styleWriter.write("</style:text-properties>\n");	
//			styleWriter.write("</style:style>\n");
//		}
//		
//		return textSpanStyleName;
//	}


}

