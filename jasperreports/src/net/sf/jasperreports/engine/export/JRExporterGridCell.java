/*
 * ============================================================================
 * GNU Lesser General Public License
 * ============================================================================
 *
 * JasperReports - Free Java report-generating library.
 * Copyright (C) 2001-2005 JasperSoft Corporation http://www.jaspersoft.com
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
 * 185, Berry Street, Suite 6200
 * San Francisco CA 94107
 * http://www.jaspersoft.com
 */
package net.sf.jasperreports.engine.export;

import net.sf.jasperreports.engine.JRPrintElement;
import net.sf.jasperreports.engine.base.JRBasePrintElement;
	
	
/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JRExporterGridCell
{


	/**
	 *
	 */
	public static final JRExporterGridCell OCCUPIED_CELL = 
		new JRExporterGridCell(
			new JRBasePrintElement(),
			null,
			0,
			0,
			1,
			1
		); 


	/**
	 *
	 */
	public JRPrintElement element = null; 
	public Integer[] elementIndex = null;
	public int width = 0;
	public int height = 0;
	public int colSpan = 0;
	public int rowSpan = 0;


	/**
	 * @deprecated
	 */
	public JRExporterGridCell
	(
		JRPrintElement element, 
		int width, 
		int height,
		int colSpan, 
		int rowSpan
	)
	{
		this(
			element,
			null,
			width,
			height,
			colSpan,
			rowSpan
			);
	}


	/**
	 *
	 */
	public JRExporterGridCell
	(
		JRPrintElement element, 
		Integer[] elementIndex,
		int width, 
		int height,
		int colSpan, 
		int rowSpan
	)
	{
		this.element = element;
		this.elementIndex = elementIndex;
		this.width = width;
		this.height = height;
		this.colSpan = colSpan;
		this.rowSpan = rowSpan;
	}


}
