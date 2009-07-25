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
import java.io.IOException;
import java.io.Writer;

import net.sf.jasperreports.engine.JRAlignment;
import net.sf.jasperreports.engine.JRCommonGraphicElement;
import net.sf.jasperreports.engine.JRElement;
import net.sf.jasperreports.engine.JRPrintElement;
import net.sf.jasperreports.engine.JRPrintText;
import net.sf.jasperreports.engine.JRStyle;
import net.sf.jasperreports.engine.JRTextElement;
import net.sf.jasperreports.engine.export.JRExporterGridCell;
import net.sf.jasperreports.engine.util.JRColorUtil;


/**
 * @author sanda zaharia (shertage@users.sourceforge.net)
 * @version $Id$
 */
public class CellHelper extends BaseHelper
{
	
	/**
	 *
	 */
	private BorderHelper borderHelper = null;
	
	/**
	 *
	 */
	public CellHelper(Writer writer)
	{
		super(writer);
		
		borderHelper = new BorderHelper(writer);
	}
		
	/**
	 * 
	 */
	public BorderHelper getBorderHelper() 
	{
		return borderHelper;
	}

	/**
	 *
	 */
	public void exportHeader(JRPrintElement element, JRExporterGridCell gridCell) throws IOException 
	{
		writer.write("    <w:tc> \r\n");
		writer.write("     <w:tcPr> \r\n");
//		writer.write("      <w:tcW w:w=\"" + Utility.translatePointsToTwips(emptyCellWidth) + "\" w:type=\"dxa\" /> \r\n");
		if (gridCell.getColSpan() > 1)
		{
			writer.write("      <w:gridSpan w:val=\"" + gridCell.getColSpan() +"\" /> \r\n");
		}
		if (gridCell.getRowSpan() > 1)
		{
			writer.write("      <w:vMerge w:val=\"restart\" /> \r\n");
		}
		
		exportProps(element, gridCell);
		
		writer.write("     </w:tcPr> \r\n");
	}

	/**
	 *
	 */
	public void exportFooter() throws IOException 
	{
		writer.write("    </w:tc> \r\n");
	}


	/**
	 *
	 */
	public void exportProps(JRStyle style) throws IOException
	{
		exportBackcolor(style.getMode() == null ? JRElement.MODE_OPAQUE : style.getMode().byteValue(), style.getOwnBackcolor());

//		if (element instanceof JRBoxContainer)
			borderHelper.export(style.getLineBox());
			
		//FIXMEDOCS invert this or use constructor with to parameters for priority management
//		if (element instanceof JRCommonGraphicElement)
			borderHelper.export(style.getLinePen());
		
		//export(null);
	}


	/**
	 *
	 */
	public void exportProps(JRPrintElement element, JRExporterGridCell gridCell) throws IOException
	{
		exportBackcolor(element.getMode(), element.getOwnBackcolor());
		
		borderHelper.export(gridCell.getBox());

		if (element instanceof JRCommonGraphicElement)
			borderHelper.export(((JRCommonGraphicElement)element).getLinePen());
		
		//export(element);
	}


	/**
	 *
	 */
	public void exportProps(JRExporterGridCell gridCell) throws IOException
	{
		exportBackcolor(JRElement.MODE_OPAQUE, gridCell.getBackcolor());//FIXMEDOCX check this
		
		borderHelper.export(gridCell.getBox());

		//export(null);
	}

	
	/**
	 *
	 */
	public void exportBackcolor(byte mode, Color backcolor) throws IOException
	{
		if (mode == JRElement.MODE_OPAQUE && backcolor != null)
		{
			writer.write("      <w:shd w:val=\"clear\" w:color=\"auto\"	w:fill=\"" + JRColorUtil.getColorHexa(backcolor) + "\" /> \r\n");
		}
	}

	/**
	 *
	 */
	public void export(JRPrintElement element) throws IOException
	{
		
		byte rotation = element instanceof JRPrintText ? ((JRPrintText)element).getRotation() : JRTextElement.ROTATION_NONE;
		byte vAlign = JRAlignment.VERTICAL_ALIGN_TOP;
		byte hAlign = JRAlignment.HORIZONTAL_ALIGN_LEFT;

		JRAlignment alignment = element instanceof JRAlignment ? (JRAlignment)element : null;
		if (alignment != null)
		{
			vAlign = alignment.getVerticalAlignment();
			hAlign = alignment.getHorizontalAlignment();
		}
		
		String verticalAlignment = AlignmentHelper.getVerticalAlignment(hAlign, vAlign, rotation);

		if (verticalAlignment != null)
		{
			writer.write("      <w:vAlign w:val=\"" + verticalAlignment +"\" /> \r\n");
		}
	}

}
