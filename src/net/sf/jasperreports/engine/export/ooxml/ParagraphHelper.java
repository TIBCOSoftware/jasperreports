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

import java.io.IOException;
import java.io.Writer;

import net.sf.jasperreports.engine.JRAlignment;
import net.sf.jasperreports.engine.JRPrintText;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.JRStyle;
import net.sf.jasperreports.engine.JRTextElement;


/**
 * @author sanda zaharia (shertage@users.sourceforge.net)
 * @version $Id$
 */
public class ParagraphHelper extends StyleHelper
{
	/**
	 *
	 */
	protected static final String HORIZONTAL_ALIGN_LEFT = "left";
	protected static final String HORIZONTAL_ALIGN_RIGHT = "right";
	protected static final String HORIZONTAL_ALIGN_CENTER = "center";
	protected static final String HORIZONTAL_ALIGN_JUSTIFY = "justified";

	/**
	 *
	 */
	private static final String VERTICAL_ALIGN_TOP = "top";
	private static final String VERTICAL_ALIGN_MIDDLE = "middle";
	private static final String VERTICAL_ALIGN_BOTTOM = "bottom";

	/**
	*
	*/
	protected static final String ROTATION_ALIGN_NONE = "none";
	protected static final String ROTATION_ALIGN_TOP = "top";
	protected static final String ROTATION_ALIGN_CENTER = "center";
	protected static final String ROTATION_ALIGN_BOTTOM = "bottom";

	private String styleNameReference = null;
	private String verticalAlignment = null;
	private String horizontalAlignment = null;
	private String textRotation = null;
	private String runDirection = null;

	/**
	 *
	 */
	public ParagraphHelper(Writer styleWriter, JRStyle style)
	{
		super(styleWriter);
		
		if (style.getStyle() != null)
		{
			styleNameReference = style.getStyle().getName();//FIXMEDOCX why getStyleNameReference is not working?
		}

		setAlignmentAndRotation(
			style.getOwnHorizontalAlignment(), 
			style.getOwnVerticalAlignment(), 
			style.getOwnRotation(), 
			style.getHorizontalAlignment() == null ? JRAlignment.HORIZONTAL_ALIGN_LEFT : style.getHorizontalAlignment().byteValue(),//FIXMEDOCX how to get rid of these conditional expressions? 
			style.getVerticalAlignment() == null ? JRAlignment.VERTICAL_ALIGN_TOP : style.getVerticalAlignment().byteValue(), 
			style.getRotation() == null ? JRTextElement.ROTATION_NONE : style.getRotation().byteValue()
			);
	}
	
	/**
	 *
	 */
	public ParagraphHelper(Writer styleWriter, JRPrintText text)
	{
		super(styleWriter);
		
		if (text.getStyle() != null)
		{
			styleNameReference = text.getStyle().getName();//FIXMEDOCX why getStyleNameReference is not working?
		}

		setAlignmentAndRotation(
			text.getOwnHorizontalAlignment(), 
			text.getOwnVerticalAlignment(), 
			text.getOwnRotation(), 
			text.getHorizontalAlignment(), 
			text.getVerticalAlignment(), 
			text.getRotation()
			);
		
		runDirection = null;
		if (text.getRunDirection() == JRPrintText.RUN_DIRECTION_RTL)
		{
			runDirection = "rl";
		}
	}
	
	/**
	 *
	 */
	public void setAlignmentAndRotation(
		Byte ownHAlign, 
		Byte ownVAlign, 
		Byte ownRotation,
		byte hAlign, 
		byte vAlign, 
		byte rotation
		)
	{
		if (
			ownHAlign != null
			|| ownVAlign != null
			|| ownRotation != null
			)
		{
			horizontalAlignment = getHorizontalAlignment(hAlign, vAlign, rotation);
			verticalAlignment = getVerticalAlignment(hAlign, vAlign, rotation);
			
			switch(rotation)
			{
				case JRTextElement.ROTATION_LEFT:
				{
					textRotation = "90";
					break;
				}
				case JRTextElement.ROTATION_RIGHT:
				{
					textRotation = "270";
					break;
				}
				case JRTextElement.ROTATION_UPSIDE_DOWN://FIXMEDOCX possible?
				case JRTextElement.ROTATION_NONE:
				default:
				{
					textRotation = "0";
				}
			}
		}
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
						return VERTICAL_ALIGN_TOP;
					case JRAlignment.HORIZONTAL_ALIGN_CENTER :
						return VERTICAL_ALIGN_MIDDLE;
					case JRAlignment.HORIZONTAL_ALIGN_JUSTIFIED :
						return HORIZONTAL_ALIGN_JUSTIFY;//FIXMEDOCX ?????????????????
					case JRAlignment.HORIZONTAL_ALIGN_LEFT :
					default :
						return VERTICAL_ALIGN_BOTTOM;
				}
			}
			case JRTextElement.ROTATION_RIGHT:
			{
				switch (horizontalAlignment)
				{
					case JRAlignment.HORIZONTAL_ALIGN_RIGHT :
						return VERTICAL_ALIGN_BOTTOM;
					case JRAlignment.HORIZONTAL_ALIGN_CENTER :
						return VERTICAL_ALIGN_MIDDLE;
					case JRAlignment.HORIZONTAL_ALIGN_JUSTIFIED :
						return HORIZONTAL_ALIGN_JUSTIFY;//?????????????????
					case JRAlignment.HORIZONTAL_ALIGN_LEFT :
					default :
						return VERTICAL_ALIGN_TOP;
				}
			}
			case JRTextElement.ROTATION_UPSIDE_DOWN://FIXMEDOCX possible?
			case JRTextElement.ROTATION_NONE:
			default:
			{
				switch (verticalAlignment)
				{
					case JRAlignment.VERTICAL_ALIGN_BOTTOM :
						return VERTICAL_ALIGN_BOTTOM;
					case JRAlignment.VERTICAL_ALIGN_MIDDLE :
						return VERTICAL_ALIGN_MIDDLE;
					case JRAlignment.VERTICAL_ALIGN_TOP :
					default :
						return VERTICAL_ALIGN_TOP;
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
						return HORIZONTAL_ALIGN_RIGHT;
					case JRAlignment.VERTICAL_ALIGN_MIDDLE :
						return HORIZONTAL_ALIGN_CENTER;
					case JRAlignment.VERTICAL_ALIGN_TOP :
					default :
						return HORIZONTAL_ALIGN_LEFT;
				}
			}
			case JRTextElement.ROTATION_RIGHT:
			{
				switch (verticalAlignment)
				{
					case JRAlignment.VERTICAL_ALIGN_BOTTOM :
						return HORIZONTAL_ALIGN_LEFT;
					case JRAlignment.VERTICAL_ALIGN_MIDDLE :
						return HORIZONTAL_ALIGN_CENTER;
					case JRAlignment.VERTICAL_ALIGN_TOP :
					default :
						return HORIZONTAL_ALIGN_RIGHT;
				}
			}
			case JRTextElement.ROTATION_UPSIDE_DOWN://FIXMEDOCX possible?
			case JRTextElement.ROTATION_NONE:
			default:
			{
				switch (horizontalAlignment)
				{
					case JRAlignment.HORIZONTAL_ALIGN_RIGHT :
						return HORIZONTAL_ALIGN_RIGHT;
					case JRAlignment.HORIZONTAL_ALIGN_CENTER :
						return HORIZONTAL_ALIGN_CENTER;
					case JRAlignment.HORIZONTAL_ALIGN_JUSTIFIED :
						return HORIZONTAL_ALIGN_JUSTIFY;
					case JRAlignment.HORIZONTAL_ALIGN_LEFT :
					default :
						return HORIZONTAL_ALIGN_LEFT;
				}
			}
		}
	}
	
	/**
	 *
	 */
	public String getId()
	{
		return verticalAlignment + "|" + horizontalAlignment + "|" + runDirection + "|" + textRotation;
	}

//	/**
//	 *
//	 */
//	public void write(String paragraphStyleName) throws IOException
//	{
//		styleWriter.write("<style:style style:name=\"" + paragraphStyleName + "\"");
//		styleWriter.write(" style:family=\"paragraph\">\n");
//		styleWriter.write("<style:paragraph-properties");
////		styleWriter.write(" fo:line-height=\"" + pLineHeight + "\"");
////		styleWriter.write(" style:line-spacing=\"" + pLineSpacing + "\"");
//		styleWriter.write(" fo:text-align=\"" + horizontalAlignment + "\"");
//
////		styleWriter.write(" fo:keep-together=\"" + pKeepTogether + "\"");
////		styleWriter.write(" fo:margin-left=\"" + pMarginLeft + "\"");
////		styleWriter.write(" fo:margin-right=\"" + pMarginRight + "\"");
////		styleWriter.write(" fo:margin-top=\"" + pMarginTop + "\"");
////		styleWriter.write(" fo:margin-bottom=\"" + pMarginBottom + "\"");
////		styleWriter.write(" fo:background-color=\"#" + pBackGroundColor + "\"");
//		styleWriter.write(" style:vertical-align=\"" + verticalAlignment + "\"");
//		if (runDirection != null)
//		{
//			styleWriter.write(" style:writing-mode=\"" + runDirection + "\"");
//		}
//		styleWriter.write("> \r\n");
//		styleWriter.write("</style:paragraph-properties>\n");
//		styleWriter.write("<style:text-properties");
//		styleWriter.write(" style:text-rotation-angle=\"" + textRotation + "\"");
//		styleWriter.write("> \r\n");
//		styleWriter.write("</style:text-properties>\n");
//		
////        styleWriter.write("<style:properties");
////        styleWriter.write(" style:rotation-align=\"" + rotationAlignment + "\"");
////        styleWriter.write("> \r\n");
////        styleWriter.write("</style:properties>\n");
////
//
//		styleWriter.write("</style:style>\n");
//	}

	/**
	 *
	 */
	public void export(boolean pageBreakInserted) throws IOException
	{
		styleWriter.write("      <w:pPr> \r\n");
		if (styleNameReference != null)
		{
			styleWriter.write("        <w:pStyle w:val=\"" + styleNameReference + "\"/> \r\n");
		}
		if (!pageBreakInserted)
		{
			styleWriter.write("        <w:pageBreakBefore/> \r\n");
		}
		if (horizontalAlignment != null)
		{
			styleWriter.write("   <w:jc w:val=\"" + horizontalAlignment + "\" /> \r\n");
		}
		//FIXME: textRotation???
		styleWriter.write("      </w:pPr> \r\n");
	}

	/**
	 *
	 */
	public void export(String styleNameRef) throws IOException
	{
		throw new JRRuntimeException("FIXMEDOCX why should we implement this?");
	}
}

