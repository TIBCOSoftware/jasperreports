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
 * Contributors:
 * Greg Hilton 
 */

package net.sf.jasperreports.engine.export.ooxml2;

import net.sf.jasperreports.engine.JRGenericPrintElement;
import net.sf.jasperreports.engine.JRPrintElement;
import net.sf.jasperreports.engine.export.ExporterFilter;
import net.sf.jasperreports.engine.export.ExporterNature;

/**
 * @author sanda zaharia (shertage@users.sourceforge.net)
 * @version $Id: JROfficeOpenXmlExporterNature.java 2913 2009-07-22 08:48:14Z teodord $
 */
public abstract class JROfficeOpenXmlExporterNature implements ExporterNature
{
	
	public static final byte ODT_NATURE = 1;//FIXMEDOCX
	public static final byte ODS_NATURE = 2;
	
	protected ExporterFilter filter = null;

	/**
	 * 
	 */
	public JROfficeOpenXmlExporterNature(ExporterFilter filter)
	{
		this.filter = filter;
	}
	
	/**
	 * 
	 */
	public boolean isToExport(JRPrintElement element)
	{
		return !(element instanceof JRGenericPrintElement)
			&& (filter == null || filter.isToExport(element));
	}
	
	/**
	 * 
	 */
	public boolean isDeep()
	{
		return false;
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
		return true;
	}

	public boolean isHorizontallyMergeEmptyCells()
	{
		return false;
	}

	/**
	 * Specifies whether empty page margins should be ignored
	 */
	public boolean isIgnorePageMargins()
	{
		return false;
	}
	
	/**
	 *
	 */
	public boolean isBreakBeforeRow(JRPrintElement element)
	{
		return false;
	}
	
	/**
	 *
	 */
	public boolean isBreakAfterRow(JRPrintElement element)
	{
		return false;
	}
	
	protected abstract byte getOfficeOpenXmlNature();
}
