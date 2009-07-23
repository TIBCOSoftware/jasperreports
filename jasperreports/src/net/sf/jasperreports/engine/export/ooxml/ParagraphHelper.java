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
package net.sf.jasperreports.engine.export.ooxml2;

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
public class ParagraphHelper extends BaseHelper
{
	/**
	 *
	 */
	private boolean pageBreak = false;

	/**
	 *
	 */
	public ParagraphHelper(Writer writer, boolean pageBreak)
	{
		super(writer);
		
		this.pageBreak = pageBreak;
	}
	
	/**
	 *
	 */
	public void exportProps(JRStyle style) throws IOException
	{
		exportPropsHeader(style.getStyle() == null ? null : style.getStyle().getName());//FIXMEDOCX why getStyleNameReference is not working?

		String horizontalAlignment = 
			getHorizontalAlignment(
				style.getOwnHorizontalAlignment(), 
				style.getOwnVerticalAlignment(), 
				style.getOwnRotation(), 
				style.getHorizontalAlignment() == null ? JRAlignment.HORIZONTAL_ALIGN_LEFT : style.getHorizontalAlignment().byteValue(),//FIXMEDOCX how to get rid of these conditional expressions? 
				style.getVerticalAlignment() == null ? JRAlignment.VERTICAL_ALIGN_TOP : style.getVerticalAlignment().byteValue(), 
				style.getRotation() == null ? JRTextElement.ROTATION_NONE : style.getRotation().byteValue()
				);
		String textRotation = getRotation(style.getOwnRotation());

		exportAlignmentAndRotation(horizontalAlignment, textRotation);

		exportPropsFooter();
	}
	
	/**
	 *
	 */
	public void exportProps(JRPrintText text) throws IOException
	{
		exportPropsHeader(text.getStyle() == null ? null : text.getStyle().getName());//FIXMEDOCX why getStyleNameReference is not working?

		String horizontalAlignment = 
			getHorizontalAlignment(
				text.getOwnHorizontalAlignment(), 
				text.getOwnVerticalAlignment(), 
				text.getOwnRotation(), 
				text.getHorizontalAlignment(), 
				text.getVerticalAlignment(), 
				text.getRotation()
				);
		String textRotation = getRotation(text.getOwnRotation());

		exportAlignmentAndRotation(horizontalAlignment, textRotation);
		
//		exportRunDirection(text.getRunDirection() == JRPrintText.RUN_DIRECTION_RTL ? "rl" : null);

		exportPropsFooter();
	}
	
	
	/**
	 *
	 */
	private void exportPropsHeader(String styleNameReference) throws IOException
	{
		writer.write("      <w:pPr> \r\n");
		if (styleNameReference != null)
		{
			writer.write("        <w:pStyle w:val=\"" + styleNameReference + "\"/> \r\n");
		}
		if (pageBreak)
		{
			writer.write("        <w:pageBreakBefore/> \r\n");
			pageBreak = false;
		}
	}
	
	/**
	 *
	 */
	private void exportAlignmentAndRotation(String horizontalAlignment, String textRotation) throws IOException
	{
		if (horizontalAlignment != null)
		{
			writer.write("   <w:jc w:val=\"" + horizontalAlignment + "\" /> \r\n");
		}
		//FIXME: textRotation???
	}
	
	/**
	 *
	 */
	private void exportPropsFooter() throws IOException
	{
		writer.write("      </w:pPr> \r\n");
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
	public void export(String styleNameRef) throws IOException
	{
		throw new JRRuntimeException("FIXMEDOCX why should we implement this?");
	}

	public void exportEmptyParagraph() throws IOException
	{
		writer.write("     <w:p><w:pPr> \r\n");
		if (pageBreak)
		{
			writer.write("        <w:pageBreakBefore/> \r\n");
			pageBreak = false;
		}
		writer.write("     </w:pPr></w:p> \r\n");
	}

	/**
	 *
	 */
	private String getHorizontalAlignment(
		Byte ownHAlign, 
		Byte ownVAlign, 
		Byte ownRotation,
		byte hAlign, 
		byte vAlign, 
		byte rotation
		)
	{
		String horizontalAlignment = null;
		
		if (
			ownHAlign != null
			|| ownVAlign != null
			|| ownRotation != null
			)
		{
			horizontalAlignment = AlignmentHelper.getHorizontalAlignment(hAlign, vAlign, rotation);
		}
		
		return horizontalAlignment;
	}

	/**
	 *
	 */
	private String getRotation(Byte rotation)
	{
		String textRotation = null;
		
		if (rotation != null)
		{
			switch(rotation.byteValue())
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

		return textRotation;
	}
}
