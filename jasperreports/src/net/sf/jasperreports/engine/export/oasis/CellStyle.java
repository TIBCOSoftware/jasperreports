/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2011 Jaspersoft Corporation. All rights reserved.
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
package net.sf.jasperreports.engine.export.oasis;

import java.io.IOException;
import java.io.Writer;

import net.sf.jasperreports.engine.JRAlignment;
import net.sf.jasperreports.engine.JRPrintElement;
import net.sf.jasperreports.engine.JRPrintText;
import net.sf.jasperreports.engine.export.JRExporterGridCell;
import net.sf.jasperreports.engine.type.HorizontalAlignEnum;
import net.sf.jasperreports.engine.type.ModeEnum;
import net.sf.jasperreports.engine.type.RotationEnum;
import net.sf.jasperreports.engine.type.VerticalAlignEnum;
import net.sf.jasperreports.engine.util.JRColorUtil;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class CellStyle extends BorderStyle
{
	//private String fill;
	private String backcolor;
	
	private final String horizontalAlignment;
	private final String verticalAlignment;

	
	/**
	 *
	 */
	public CellStyle(Writer styleWriter, JRExporterGridCell gridCell)
	{
		super(styleWriter);

		JRPrintElement element = gridCell.getElement();
		
		if (element != null && element.getModeValue() == ModeEnum.OPAQUE)
		{
			//fill = "solid";
			backcolor = JRColorUtil.getColorHexa(element.getBackcolor());
		}
		else
		{
			//fill = "none";
			if (gridCell.getBackcolor() != null)
			{
				backcolor = JRColorUtil.getColorHexa(gridCell.getBackcolor());
			}
		}

		RotationEnum rotation = element instanceof JRPrintText ? ((JRPrintText)element).getRotationValue() : RotationEnum.NONE;
		VerticalAlignEnum vAlign = VerticalAlignEnum.TOP;
		HorizontalAlignEnum hAlign = HorizontalAlignEnum.LEFT;

		JRAlignment alignment = element instanceof JRAlignment ? (JRAlignment)element : null;
		if (alignment != null)
		{
			vAlign = alignment.getVerticalAlignmentValue();
			hAlign = alignment.getHorizontalAlignmentValue();
		}
		
		horizontalAlignment = ParagraphStyle.getHorizontalAlignment(hAlign, vAlign, rotation);
		verticalAlignment = ParagraphStyle.getVerticalAlignment(hAlign, vAlign, rotation);
		
		setBox(gridCell.getBox());
	}
	
	/**
	 *
	 */
	public String getId()
	{
		return backcolor + super.getId() + "|" + horizontalAlignment + "|" + verticalAlignment; 
	}

	/**
	 *
	 */
	public void write(String cellStyleName) throws IOException
	{
		styleWriter.write("<style:style style:name=\"");
		styleWriter.write(cellStyleName);
		styleWriter.write("\"");
		styleWriter.write(" style:family=\"table-cell\">\n");
		styleWriter.write(" <style:table-cell-properties");		
		styleWriter.write(" fo:wrap-option=\"wrap\"");
		styleWriter.write(" style:shrink-to-fit=\"false\"");
		if (backcolor != null)
		{
			styleWriter.write(" fo:background-color=\"#");
			styleWriter.write(backcolor);
			styleWriter.write("\"");
		}
		
		writeBorder(TOP_BORDER);
		writeBorder(LEFT_BORDER);
		writeBorder(BOTTOM_BORDER);
		writeBorder(RIGHT_BORDER);
		
		if (verticalAlignment != null)
		{
			styleWriter.write(" style:vertical-align=\"");
			styleWriter.write(verticalAlignment);
			styleWriter.write("\"");
		}

		styleWriter.write("/>\n");

		if (horizontalAlignment != null)
		{
			styleWriter.write(" <style:paragraph-properties");		
			styleWriter.write(" fo:text-align=\"");
			styleWriter.write(horizontalAlignment);
			styleWriter.write("\"");
			styleWriter.write("/>\n");
		}

		styleWriter.write("</style:style>\n");
	}

}

