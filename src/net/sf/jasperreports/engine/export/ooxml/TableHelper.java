/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2009 Jaspersoft Corporation. All rights reserved.
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
 *(at your option) any later version.
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

import java.io.IOException;
import java.io.Writer;

import net.sf.jasperreports.engine.export.CutsInfo;
import net.sf.jasperreports.engine.export.JRExporterGridCell;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class TableHelper extends BaseHelper
{
	/**
	 * 
	 */
	private CutsInfo xCuts = null;
	private CellHelper cellHelper = null;
	private ParagraphHelper paragraphHelper = null;
	private RunHelper runHelper = null;


	/**
	 * 
	 */
	protected TableHelper(
		Writer writer,
		CutsInfo xCuts,
		RunHelper runHelper,
		boolean pageBreak
		) 
	{
		super(writer);

		this.xCuts = xCuts;
		this.cellHelper = new CellHelper(writer);
		this.paragraphHelper = new ParagraphHelper(writer, pageBreak);
		this.runHelper = runHelper;
	}


	/**
	 * 
	 */
	public CellHelper getCellHelper() 
	{
		return cellHelper;
	}
	

	/**
	 * 
	 */
	public ParagraphHelper getParagraphHelper() 
	{
		return paragraphHelper;
	}
	

	/**
	 * 
	 */
	public void exportHeader() throws IOException 
	{
		writer.write("  <w:tbl>\n");
		writer.write("   <w:tblPr>\n");
		writer.write("    <w:tblLayout w:type=\"fixed\"/>\n");
		writer.write("   </w:tblPr>\n");
		writer.write("   <w:tblGrid>\n");
		for(int col = 1; col < xCuts.size(); col++)
		{
			writer.write("    <w:gridCol w:w=\"" + Utility.twip(xCuts.getCut(col) - xCuts.getCut(col - 1)) + "\"/>\n");
		}
		writer.write("   </w:tblGrid>\n");
	}
	
	public void exportFooter(boolean lastPage, int pageWidth, int pageHeight) throws IOException 
	{
		writer.write("  </w:tbl>\n");
		if (lastPage)
		{
			writer.write("    <w:p>\n");
			writer.write("    <w:pPr>\n");
			writer.write("  <w:sectPr>\n");
			writer.write("   <w:pgSz w:w=\"" + Utility.twip(pageWidth) + "\" w:h=\"" + Utility.twip(pageHeight) + "\" />\n");
			writer.write("   <w:pgMar w:top=\"0\" w:right=\"0\" w:bottom=\"0\" w:left=\"0\" w:header=\"0\" w:footer=\"0\" w:gutter=\"0\" />\n");
			writer.write("   <w:docGrid w:linePitch=\"360\" />\n");
			writer.write("  </w:sectPr>\n");
			writer.write("    </w:pPr>\n");
			writer.write("    </w:p>\n");
		}
	}
	
	public void exportRowHeader(int rowHeight) throws IOException 
	{
		writer.write("   <w:tr>\n");
		writer.write("    <w:trPr>\n");
		writer.write("     <w:trHeight w:hRule=\"exact\" w:val=\"" +  + Utility.twip(rowHeight) + "\" />\n");
		writer.write("    </w:trPr>\n");
	}
	
	public void exportRowFooter() throws IOException 
	{
		writer.write("   </w:tr>\n");
	}
	
	public void exportEmptyCell(JRExporterGridCell gridCell, int emptyCellColSpan) throws IOException
	{
		writer.write("    <w:tc>\n");
		writer.write("     <w:tcPr>\n");
		if (emptyCellColSpan > 1)
		{
			writer.write("      <w:gridSpan w:val=\"" + emptyCellColSpan +"\" />\n");
		}
		
		if (gridCell != null)
		{
			cellHelper.exportProps(gridCell);
		}
		
		writer.write("     </w:tcPr>\n");
		
		paragraphHelper.exportEmptyParagraph();

		writer.write("    </w:tc>\n");
	}

	public void exportOccupiedCells(JRExporterGridCell gridCell) throws IOException
	{
		writer.write("    <w:tc>\n");
		writer.write("     <w:tcPr>\n");
		if (gridCell.getColSpan() > 1)
		{
			writer.write("      <w:gridSpan w:val=\"" + gridCell.getColSpan() +"\" />\n");
		}
		writer.write("      <w:vMerge w:val=\"continue\" />\n");
		
		cellHelper.exportProps(gridCell.getElement(), gridCell);
		
		writer.write("     </w:tcPr>\n");
		
		paragraphHelper.exportEmptyParagraph();

		cellHelper.exportFooter();
	}

}