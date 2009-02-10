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
package net.sf.jasperreports.engine.util;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.sf.jasperreports.crosstabs.JRCellContents;
import net.sf.jasperreports.crosstabs.JRCrosstab;
import net.sf.jasperreports.crosstabs.JRCrosstabCell;
import net.sf.jasperreports.crosstabs.JRCrosstabColumnGroup;
import net.sf.jasperreports.crosstabs.JRCrosstabRowGroup;
import net.sf.jasperreports.crosstabs.design.JRDesignCrosstab;
import net.sf.jasperreports.engine.JRBand;
import net.sf.jasperreports.engine.JRChild;
import net.sf.jasperreports.engine.JRElementGroup;
import net.sf.jasperreports.engine.JRFrame;
import net.sf.jasperreports.engine.JRGroup;
import net.sf.jasperreports.engine.JRReport;
import net.sf.jasperreports.engine.JRVisitor;


/**
 * Report elements visitor.
 * 
 * This class can be used to recursively visit all the elements of a report.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public class JRElementsVisitor extends JRDelegationVisitor
{

	/**
	 * Visits all the elements of a report.
	 * 
	 * @param report the report
	 * @param visitor the element visitor
	 */
	public static void visitReport(JRReport report, JRVisitor visitor)
	{
		JRElementsVisitor reportVisitor = new JRElementsVisitor(visitor);
		reportVisitor.visitReport(report);
	}
	
	/**
	 * Creates a report visitor.
	 * 
	 * @param visitor the elements visitor
	 */
	public JRElementsVisitor(JRVisitor visitor)
	{
		super(visitor);
	}

	/**
	 * Visits all the elements of a report.
	 * 
	 * @param report the report
	 */
	public void visitReport(JRReport report)
	{
		visitBand(report.getBackground());
		visitBand(report.getTitle());
		visitBand(report.getPageHeader());
		visitBand(report.getColumnHeader());
		visitBand(report.getDetail());
		visitBand(report.getColumnFooter());
		visitBand(report.getPageFooter());
		visitBand(report.getLastPageFooter());
		visitBand(report.getSummary());
		visitBand(report.getNoData());
		
		JRGroup[] groups = report.getGroups();
		if (groups != null)
		{
			for(int i = 0; i < groups.length; i++)
			{
				JRGroup group = groups[i];
				visitBand(group.getGroupHeader());
				visitBand(group.getGroupFooter());
			}
		}
	}
	
	protected void visitBand(JRBand band)
	{
		if (band != null)
		{
			band.visit(this);
		}
	}

	protected void visitElements(List elements)
	{
		if (elements != null)
		{
			for (Iterator it = elements.iterator(); it.hasNext();)
			{
				JRChild child = (JRChild) it.next();
				child.visit(this);
			}
		}
	}
	
	/**
	 * Visits the element group and all its children.
	 */
	public void visitElementGroup(JRElementGroup elementGroup)
	{
		super.visitElementGroup(elementGroup);
		visitElements(elementGroup.getChildren());
	}

	/**
	 * Visits the frame and all its children. 
	 */
	public void visitFrame(JRFrame frame)
	{
		super.visitFrame(frame);
		visitElements(frame.getChildren());
	}

	/**
	 * Visits the crosstab and the elements in all its cells.
	 */
	public void visitCrosstab(JRCrosstab crosstab)
	{
		super.visitCrosstab(crosstab);
		
		visitCrosstabCell(crosstab.getWhenNoDataCell());
		visitCrosstabCell(crosstab.getHeaderCell());
		
		JRCrosstabRowGroup[] rowGroups = crosstab.getRowGroups();
		for (int i = 0; i < rowGroups.length; i++)
		{
			JRCrosstabRowGroup rowGroup = rowGroups[i];
			visitCrosstabCell(rowGroup.getHeader());
			visitCrosstabCell(rowGroup.getTotalHeader());
		}
		
		JRCrosstabColumnGroup[] columnGroups = crosstab.getColumnGroups();
		for (int i = 0; i < columnGroups.length; i++)
		{
			JRCrosstabColumnGroup columnGroup = columnGroups[i];
			visitCrosstabCell(columnGroup.getHeader());
			visitCrosstabCell(columnGroup.getTotalHeader());
		}
		
		if (crosstab instanceof JRDesignCrosstab)
		{
			List cells = ((JRDesignCrosstab) crosstab).getCellsList();
			for (Iterator it = cells.iterator(); it.hasNext();)
			{
				JRCrosstabCell cell = (JRCrosstabCell) it.next();
				visitCrosstabCell(cell.getContents());
			}
		}
		else
		{
			JRCrosstabCell[][] cells = crosstab.getCells();
			if (cells != null)
			{
				Set cellContents = new HashSet();
				for (int i = 0; i < cells.length; i++)
				{
					for (int j = 0; j < cells[i].length; j++)
					{
						JRCrosstabCell cell = cells[i][j];
						if (cell != null && cell.getContents() != null
								&& cellContents.add(cell.getContents()))
						{
							visitCrosstabCell(cell.getContents());
						}
					}
				}
			}
		}
	}
	
	protected void visitCrosstabCell(JRCellContents cell)
	{
		if (cell != null)
		{
			visitElements(cell.getChildren());
		}
	}
}
