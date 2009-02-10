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
import java.util.Map;

import net.sf.jasperreports.engine.JRAlignment;
import net.sf.jasperreports.engine.JRElement;
import net.sf.jasperreports.engine.JRFont;
import net.sf.jasperreports.engine.JRPrintElement;
import net.sf.jasperreports.engine.JRPrintText;
import net.sf.jasperreports.engine.JRStyle;
import net.sf.jasperreports.engine.JRTextElement;
import net.sf.jasperreports.engine.util.JRColorUtil;


/**
 * @author sanda zaharia (shertage@users.sourceforge.net)
 * @version $Id$
 */
public class XmlssCellStyle extends XmlssBorderStyle
{

	private static final String ALIGNMENT_LEFT = "Left";
	private static final String ALIGNMENT_RIGHT = "Right";
	private static final String ALIGNMENT_CENTER = "Center";
	private static final String ALIGNMENT_TOP = "Top";
	private static final String ALIGNMENT_BOTTOM = "Bottom";
	private static final String READING_ORDER_LTR = "LeftToRight";
	private static final String READING_ORDER_RTL = "RightToLeft";
	private static final String ROTATE_NONE = "0";
	private static final String ROTATE_LEFT = "90";
	private static final String ROTATE_RIGHT = "-90";
	private static final String STYLE_AUTOMATIC = "Automatic";
	private static final String STYLE_NONE = "None";
	private static final String UNDERLINE_STYLE_SINGLE = "Single";
	
	//private String fill = null;
	private final String id;

	private String backcolor = XmlssCellStyle.STYLE_AUTOMATIC;
	
	private String horizontalAlignment = XmlssCellStyle.ALIGNMENT_LEFT;
	private String verticalAlignment = XmlssCellStyle.ALIGNMENT_TOP;
	private String readingOrder = XmlssCellStyle.READING_ORDER_LTR;
	private String rotate = XmlssCellStyle.ROTATE_NONE;
	private String shrinkToFit;
	private String wrapText = "1";
	
	private JRStyle style;
	private String verticalPosition = XmlssCellStyle.STYLE_NONE;
	private String pattern;
	private String forecolor = XmlssCellStyle.STYLE_AUTOMATIC;
	private JRFont defaultFont;
	private String excelFontName;
	/**
	 *
	 */
	public XmlssCellStyle(
			Writer styleWriter, 
			JRPrintElement element,
			Color cellBackground,
			String pattern,
			boolean isShrinkToFit,
			JRFont defaultFont,
			Map fontMap)
	{
		super(styleWriter, element);

		this.style = element.getStyle() != null ? element.getStyle() : element.getDefaultStyleProvider().getDefaultStyle();
		this.defaultFont = defaultFont;
		this.pattern = pattern;
		this.shrinkToFit = String.valueOf(getBitValue(isShrinkToFit));
		
		switch (element.getMode())
		{
			case JRElement.MODE_OPAQUE: 
				backcolor = "#" + JRColorUtil.getColorHexa(element.getBackcolor());
				break;
			case JRElement.MODE_TRANSPARENT:
			default:
				if(cellBackground != null)
					backcolor = "#" + JRColorUtil.getColorHexa(cellBackground);
		}
		
		if(element.getForecolor() != null)
		{
			forecolor = "#" + JRColorUtil.getColorHexa(element.getForecolor());
		}
		
		byte rotation = element instanceof JRPrintText ? ((JRPrintText)element).getRotation() : JRTextElement.ROTATION_NONE;
		rotate = getRotation(rotation);
		
		
		if(element instanceof JRPrintText && ((JRPrintText)element).getRunDirection() == JRPrintText.RUN_DIRECTION_RTL)
			readingOrder = XmlssCellStyle.READING_ORDER_RTL;
			
		JRAlignment alignment = element instanceof JRAlignment ? (JRAlignment)element : null;
		if (alignment != null)
		{
			horizontalAlignment = getHorizontalAlignment(alignment.getHorizontalAlignment(), alignment.getVerticalAlignment(), rotation);
			verticalAlignment = getVerticalAlignment(alignment.getHorizontalAlignment(), alignment.getVerticalAlignment(), rotation);
		}
		
		if(style!= null)
		{
			String fontName = style.getFontName();
			excelFontName = (fontMap != null && fontMap.containsKey(fontName)) 
				? (String) fontMap.get(fontName) 
				: fontName;
			
			id =horizontalAlignment + "|" +
				verticalAlignment + "|" +
				readingOrder + "|" +
				rotate + "|" +
				shrinkToFit + "|" +
				super.getId() + "|" +
				excelFontName + "|" +
				style.getFontSize() + "|" +
				forecolor + "|" +
				style.isItalic() + "|" +
				style.isBold() + "|" +
				style.isStrikeThrough() + "|" +
				style.isUnderline() + "|" +
				verticalPosition + "|" +
				backcolor + "|" +
				this.pattern;
		}
		else
		{
			String fontName = defaultFont.getFontName();
			excelFontName = (fontMap != null && fontMap.containsKey(fontName)) 
				? (String) fontMap.get(fontName) 
				: fontName;
			id =horizontalAlignment + "|" +
				verticalAlignment + "|" +
				readingOrder + "|" +
				rotate + "|" +
				shrinkToFit + "|" +
				super.getId() + "|" +
				excelFontName + "|" +
				defaultFont.getFontSize() + "|" +
				forecolor + "|" +
				defaultFont.isItalic() + "|" +
				defaultFont.isBold() + "|" +
				defaultFont.isStrikeThrough() + "|" +
				defaultFont.isUnderline() + "|" +
				verticalPosition + "|" +
				backcolor + "|" +
				this.pattern;
		}
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
	public void write(String cellStyleName) throws IOException
	{
		styleWriter.write("<ss:Style ss:ID=\"");
		styleWriter.write(cellStyleName);
		styleWriter.write("\">\n");
		
		styleWriter.write(" <ss:Alignment");
		styleWriter.write(" ss:Horizontal=\"" + horizontalAlignment + "\"");
		styleWriter.write(" ss:Vertical=\"" + verticalAlignment + "\"");
		styleWriter.write(" ss:ReadingOrder=\"" + readingOrder + "\"");
		styleWriter.write(" ss:Rotate=\"" + rotate + "\"");
		styleWriter.write(" ss:ShrinkToFit=\"" + shrinkToFit + "\"");
		styleWriter.write(" ss:WrapText=\"" + wrapText + "\"");
				
		styleWriter.write("/>\n");

		styleWriter.write(" <ss:Borders>");
		writeBorder(TOP_BORDER);
		writeBorder(LEFT_BORDER);
		writeBorder(BOTTOM_BORDER);
		writeBorder(RIGHT_BORDER);
		styleWriter.write(" </ss:Borders>\n");
		
		styleWriter.write(" <ss:Font");
		styleWriter.write(" ss:FontName=\"" + excelFontName + "\"");
		if(style != null)
		{
			styleWriter.write(" ss:Size=\"" + style.getFontSize() + "\"");
			styleWriter.write(" ss:Bold=\"" + getBitValue(style.isBold().booleanValue()) + "\"");
			styleWriter.write(" ss:Italic=\"" + getBitValue(style.isItalic().booleanValue()) + "\"");
			styleWriter.write(" ss:StrikeThrough=\"" + getBitValue(style.isStrikeThrough().booleanValue()) + "\"");
			styleWriter.write(" ss:Underline=\"" + getUnderlineStyle(style.isUnderline().booleanValue()) + "\"");
		}
		else if(defaultFont != null)
		{
			styleWriter.write(" ss:Size=\"" + defaultFont.getFontSize() + "\"");
			styleWriter.write(" ss:Bold=\"" + getBitValue(defaultFont.isBold()) + "\"");
			styleWriter.write(" ss:Italic=\"" + getBitValue(defaultFont.isItalic()) + "\"");
			styleWriter.write(" ss:StrikeThrough=\"" + getBitValue(defaultFont.isStrikeThrough()) + "\"");
			styleWriter.write(" ss:Underline=\"" + getUnderlineStyle(defaultFont.isUnderline()) + "\"");
		}
		styleWriter.write(" ss:Color=\"" + forecolor + "\"");
		styleWriter.write("/>\n");
		
		styleWriter.write(" <ss:Interior");
		styleWriter.write(" ss:Color=\"" + backcolor + "\"");
		styleWriter.write(" ss:Pattern=\"Solid\"");
		styleWriter.write("/>\n");
		
		styleWriter.write(" <ss:NumberFormat");
		styleWriter.write(" ss:Format=\"" + pattern + "\"");
		styleWriter.write("/>\n");
		
		styleWriter.write(" <ss:Protection/>\n");
		
		styleWriter.write("</ss:Style>\n");
		
	}

	/**
	 *
	 */
	public static String getVerticalAlignment(
		byte horizontalAlignment, 
		byte verticalAlignment, 
		byte rotation
		)
	{
		switch(rotation)
		{
			case JRTextElement.ROTATION_LEFT:
			{
				switch (horizontalAlignment)
				{
					case JRAlignment.HORIZONTAL_ALIGN_RIGHT :
						return XmlssCellStyle.ALIGNMENT_TOP;
					case JRAlignment.HORIZONTAL_ALIGN_CENTER :
						return XmlssCellStyle.ALIGNMENT_CENTER;
					case JRAlignment.HORIZONTAL_ALIGN_JUSTIFIED :
					case JRAlignment.HORIZONTAL_ALIGN_LEFT :
					default :
						return XmlssCellStyle.ALIGNMENT_BOTTOM;
				}
			}
			case JRTextElement.ROTATION_RIGHT:
			{
				switch (horizontalAlignment)
				{
					case JRAlignment.HORIZONTAL_ALIGN_RIGHT :
						return XmlssCellStyle.ALIGNMENT_BOTTOM;
					case JRAlignment.HORIZONTAL_ALIGN_CENTER :
						return XmlssCellStyle.ALIGNMENT_CENTER;
					case JRAlignment.HORIZONTAL_ALIGN_JUSTIFIED :
					case JRAlignment.HORIZONTAL_ALIGN_LEFT :
					default :
						return XmlssCellStyle.ALIGNMENT_TOP;
				}
			}
			case JRTextElement.ROTATION_UPSIDE_DOWN:
			case JRTextElement.ROTATION_NONE:
			default:
			{
				switch (verticalAlignment)
				{
					case JRAlignment.VERTICAL_ALIGN_BOTTOM :
						return XmlssCellStyle.ALIGNMENT_BOTTOM;
					case JRAlignment.VERTICAL_ALIGN_MIDDLE :
						return XmlssCellStyle.ALIGNMENT_CENTER;
					case JRAlignment.VERTICAL_ALIGN_TOP :
					default :
						return XmlssCellStyle.ALIGNMENT_TOP;
				}
			}
		}
	}
	
	/**
	 *
	 */
	public static String getHorizontalAlignment(
		byte horizontalAlignment, 
		byte verticalAlignment, 
		byte rotation
		)
	{
		switch(rotation)
		{
			case JRTextElement.ROTATION_LEFT:
			{
				switch (verticalAlignment)
				{
					case JRAlignment.VERTICAL_ALIGN_BOTTOM :
						return XmlssCellStyle.ALIGNMENT_RIGHT;
					case JRAlignment.VERTICAL_ALIGN_MIDDLE :
						return XmlssCellStyle.ALIGNMENT_CENTER;
					case JRAlignment.VERTICAL_ALIGN_TOP :
					default :
						return XmlssCellStyle.ALIGNMENT_LEFT;
				}
			}
			case JRTextElement.ROTATION_RIGHT:
			{
				switch (verticalAlignment)
				{
					case JRAlignment.VERTICAL_ALIGN_BOTTOM :
						return XmlssCellStyle.ALIGNMENT_LEFT;
					case JRAlignment.VERTICAL_ALIGN_MIDDLE :
						return XmlssCellStyle.ALIGNMENT_CENTER;
					case JRAlignment.VERTICAL_ALIGN_TOP :
					default :
						return XmlssCellStyle.ALIGNMENT_RIGHT;
				}
			}
			case JRTextElement.ROTATION_UPSIDE_DOWN:
			case JRTextElement.ROTATION_NONE:
			default:
			{
				switch (horizontalAlignment)
				{
					case JRAlignment.HORIZONTAL_ALIGN_RIGHT :
						return XmlssCellStyle.ALIGNMENT_RIGHT;
					case JRAlignment.HORIZONTAL_ALIGN_CENTER :
						return XmlssCellStyle.ALIGNMENT_CENTER;
					case JRAlignment.HORIZONTAL_ALIGN_JUSTIFIED :
					case JRAlignment.HORIZONTAL_ALIGN_LEFT :
					default :
						return XmlssCellStyle.ALIGNMENT_LEFT;
				}
			}
		}
	}
	
	
	private String getRotation(byte rotation)
	{
		switch (rotation)
		{
			case JRTextElement.ROTATION_LEFT:
				return XmlssCellStyle.ROTATE_LEFT;
			case JRTextElement.ROTATION_RIGHT:
				return XmlssCellStyle.ROTATE_RIGHT;
			case JRTextElement.ROTATION_NONE:
			default:
				return XmlssCellStyle.ROTATE_NONE;
		}
	}
	
	private byte getBitValue(boolean isTrue)
	{
			return isTrue ? (byte)1 : 0;
	}

	private String getUnderlineStyle(boolean isUnderline)
	{
		if(isUnderline)
			return XmlssCellStyle.UNDERLINE_STYLE_SINGLE;
		return XmlssCellStyle.STYLE_NONE;
	}
	
}

