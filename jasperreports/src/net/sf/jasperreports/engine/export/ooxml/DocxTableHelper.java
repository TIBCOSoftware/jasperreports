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
package net.sf.jasperreports.engine.export.ooxml;

import java.io.Writer;

import net.sf.jasperreports.engine.export.CutsInfo;
import net.sf.jasperreports.engine.export.JRExporterGridCell;
import net.sf.jasperreports.engine.export.LengthUtil;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class DocxTableHelper extends BaseHelper
{
	/**
	 * 
	 */
	private CutsInfo xCuts;
	private DocxCellHelper cellHelper;
	private DocxParagraphHelper paragraphHelper;


	/**
	 * 
	 */
	protected DocxTableHelper(
		Writer writer,
		CutsInfo xCuts,
		boolean pageBreak
		) 
	{
		super(writer);

		this.xCuts = xCuts;
		this.cellHelper = new DocxCellHelper(writer);
		this.paragraphHelper = new DocxParagraphHelper(writer, pageBreak);
	}


	/**
	 * 
	 */
	public DocxCellHelper getCellHelper() 
	{
		return cellHelper;
	}
	

	/**
	 * 
	 */
	public DocxParagraphHelper getParagraphHelper() 
	{
		return paragraphHelper;
	}
	

	/**
	 * 
	 */
	public void exportHeader() 
	{
		write("  <w:tbl>\n");
		write("   <w:tblPr>\n");
		write("    <w:tblLayout w:type=\"fixed\"/>\n");
		write("   </w:tblPr>\n");
		write("   <w:tblGrid>\n");
		for(int col = 1; col < xCuts.size(); col++)
		{
			write("    <w:gridCol w:w=\"" + LengthUtil.twip(xCuts.getCutOffset(col) - xCuts.getCutOffset(col - 1)) + "\"/>\n");
		}
		write("   </w:tblGrid>\n");
	}
	
	public void exportFooter(boolean lastPage, int pageWidth, int pageHeight) 
	{
		write("  </w:tbl>\n");
		if (lastPage)
		{
			write("    <w:p>\n");
			write("    <w:pPr>\n");
			write("  <w:sectPr>\n");
			write("   <w:pgSz w:w=\"" + LengthUtil.twip(pageWidth) + "\" w:h=\"" + LengthUtil.twip(pageHeight) + "\" />\n");
			write("   <w:pgMar w:top=\"0\" w:right=\"0\" w:bottom=\"0\" w:left=\"0\" w:header=\"0\" w:footer=\"0\" w:gutter=\"0\" />\n");
			write("   <w:docGrid w:linePitch=\"360\" />\n");
			write("  </w:sectPr>\n");
			write("    </w:pPr>\n");
			write("    </w:p>\n");
		}
	}
	
	public void exportRowHeader(int rowHeight, boolean allowRowResize) 
	{
		write("   <w:tr>\n");
		write("    <w:trPr>\n");
		write("     <w:trHeight w:hRule=\"" + (allowRowResize ? "atLeast" : "exact")  + "\" w:val=\"" +  + LengthUtil.twip(rowHeight) + "\" />\n");
		write("    </w:trPr>\n");
	}
	
	public void exportRowFooter() 
	{
		write("   </w:tr>\n");
	}
	
	public void exportEmptyCell(JRExporterGridCell gridCell, int emptyCellColSpan)
	{
		write("    <w:tc>\n");
		write("     <w:tcPr>\n");
		if (emptyCellColSpan > 1)
		{
			write("      <w:gridSpan w:val=\"" + emptyCellColSpan +"\" />\n");
		}
		
		if (gridCell != null)
		{
			cellHelper.exportProps(gridCell);
		}
		
		write("     </w:tcPr>\n");
		
		paragraphHelper.exportEmptyParagraph();

		write("    </w:tc>\n");
	}

	public void exportOccupiedCells(JRExporterGridCell gridCell)
	{
		write("    <w:tc>\n");
		write("     <w:tcPr>\n");
		if (gridCell.getColSpan() > 1)
		{
			write("      <w:gridSpan w:val=\"" + gridCell.getColSpan() +"\" />\n");
		}
		write("      <w:vMerge w:val=\"continue\" />\n");
		
		cellHelper.exportProps(gridCell.getElement(), gridCell);
		
		write("     </w:tcPr>\n");
		
		paragraphHelper.exportEmptyParagraph();

		cellHelper.exportFooter();
	}

}