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

/*
 * Contributors:
 * Greg Hilton 
 */

package net.sf.jasperreports.engine.export;

import net.sf.jasperreports.engine.JRPrintElement;
import net.sf.jasperreports.engine.JRPrintImage;
import net.sf.jasperreports.engine.JRPrintText;

/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JRXlsExporterNature implements ExporterNature
{
	
	private ExporterFilter filter = null;
	private boolean isIgnoreGraphics = false;

	/**
	 * 
	 */
	protected JRXlsExporterNature(ExporterFilter filter, boolean isIgnoreGraphics)
	{
		this.filter = filter;
		this.isIgnoreGraphics = isIgnoreGraphics;
	}
	
	/**
	 * 
	 */
	public boolean isToExport(JRPrintElement element)
	{
		return 
			(!(isIgnoreGraphics || (element instanceof JRPrintImage)) || (element instanceof JRPrintText))
			&& (filter == null || filter.isToExport(element));
			
	}
	
	/**
	 * 
	 */
	public boolean isDeep()
	{
		return true;
	}
	
	/**
	 * 
	 */
	public boolean isSplitSharedRowSpan()
	{
		return false;
	}

	/**
	 * 
	 */
	public boolean isSpanCells()
	{
		return true;
	}
	
	/**
	 * 
	 */
	public boolean isIgnoreLastRow()
	{
		return false;
	}

	/**
	 * 
	 */
	public boolean isHorizontallyMergeEmptyCells()
	{
		return false;
	}
		
}
