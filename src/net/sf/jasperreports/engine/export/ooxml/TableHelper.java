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

/*
 * Special thanks to Google 'Summer of Code 2005' program for supporting this development
 * 
 * Contributors:
 * Majid Ali Khan - majidkk@users.sourceforge.net
 * Frank Schönheit - Frank.Schoenheit@Sun.COM
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
	private CellHelper cellHelper = null;
	private ParagraphHelper paragraphHelper = null;
	private RunHelper runHelper = null;


	/**
	 * 
	 */
	protected TableHelper(
		Writer writer,
		RunHelper runHelper,
		boolean pageBreak
		) 
	{
		super(writer);

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
	public void exportHeader(CutsInfo xCuts) throws IOException 
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
	
	public void exportFooter() throws IOException 
	{
		writer.write("  </w:tbl>\n");
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