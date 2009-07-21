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
import net.sf.jasperreports.engine.JRElement;
import net.sf.jasperreports.engine.JRPrintElement;
import net.sf.jasperreports.engine.JRPrintText;
import net.sf.jasperreports.engine.JRTextElement;
import net.sf.jasperreports.engine.util.JRColorUtil;


/**
 * @author sanda zaharia (shertage@users.sourceforge.net)
 * @version $Id$
 */
public class CellStyle extends BorderStyle
{
	//private String fill = null;
	private String backcolor = null;
	
	private final String verticalAlignment;

	
	/**
	 *
	 */
	public CellStyle(Writer styleWriter, JRPrintElement element)
	{
		super(styleWriter, element);
		
		if (element.getMode() == JRElement.MODE_OPAQUE)
		{
			//fill = "solid";
			backcolor = JRColorUtil.getColorHexa(element.getBackcolor());
		}
		else
		{
			//fill = "none";
		}

		byte rotation = element instanceof JRPrintText ? ((JRPrintText)element).getRotation() : JRTextElement.ROTATION_NONE;
		byte vAlign = JRAlignment.VERTICAL_ALIGN_TOP;
		byte hAlign = JRAlignment.HORIZONTAL_ALIGN_LEFT;

		JRAlignment alignment = element instanceof JRAlignment ? (JRAlignment)element : null;
		if (alignment != null)
		{
			vAlign = alignment.getVerticalAlignment();
			hAlign = alignment.getHorizontalAlignment();
		}
		
		verticalAlignment = ParagraphStyle.getVerticalAlignment(hAlign, vAlign, rotation);
	}
	
	/**
	 *
	 */
	public String getId()
	{
		return backcolor + super.getId() + "|" + verticalAlignment; 
	}

//	/**
//	 *
//	 */
//	public void write(String cellStyleName) throws IOException
//	{
//		styleWriter.write("<style:style style:name=\"");
//		styleWriter.write(cellStyleName);
//		styleWriter.write("\"");
//		styleWriter.write(" style:family=\"table-cell\">\n");
//		styleWriter.write(" <style:table-cell-properties");		
//		styleWriter.write(" fo:wrap-option=\"wrap\"");
//		styleWriter.write(" style:shrink-to-fit=\"false\"");
//		if (backcolor != null)
//		{
//			styleWriter.write(" fo:background-color=\"#");
//			styleWriter.write(backcolor);
//			styleWriter.write("\"");
//		}
//		
//		writeBorder(TOP_BORDER);
//		writeBorder(LEFT_BORDER);
//		writeBorder(BOTTOM_BORDER);
//		writeBorder(RIGHT_BORDER);
//		
//		if (verticalAlignment != null)
//		{
//			styleWriter.write(" style:vertical-align=\"");
//			styleWriter.write(verticalAlignment);
//			styleWriter.write("\"");
//		}
//
//		styleWriter.write("/>\n");
//		styleWriter.write("</style:style>\n");
//	}

	/**
	 *
	 */
	public void write(String cellStyleName) throws IOException
	{
//		bodyWriter.write("     <w:tcPr> \r\n");
//		bodyWriter.write("      <w:tcW w:w=\"" + width + "\" w:type=\"dxa\" /> \r\n");
//		if (gridspan > 0)
//		{
//			bodyWriter.write("      <w:gridSpan w:val=\"" + gridspan + "\" /> \r\n");
//		}
		
		if(hasBorder())
		{
			styleWriter.write("      <w:tcBorders> \r\n");
			writeBorder(TOP_BORDER);
			writeBorder(LEFT_BORDER);
			writeBorder(BOTTOM_BORDER);
			writeBorder(RIGHT_BORDER);
			styleWriter.write("      </w:tcBorders> \r\n");
		}
		
		if (backcolor != null)
		{
			styleWriter.write("      <w:shd w:val=\"clear\" w:color=\"auto\"	w:fill=\"" + backcolor + "\" /> \r\n");
		}
		
		styleWriter.write("      <w:tcMar> \r\n");
		writePadding(TOP_BORDER);
		writePadding(LEFT_BORDER);
		writePadding(BOTTOM_BORDER);
		writePadding(RIGHT_BORDER);
		styleWriter.write("      </w:tcMar> \r\n");
		
		if (verticalAlignment != null)
		{
			styleWriter.write("      <w:vAlign w:val=\"" + verticalAlignment +"\" /> \r\n");
		}
//		bodyWriter.write("     </w:tcPr> \r\n");
//		bodyWriter.flush();//FIXMEDOCX check all flush
	}

}

