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
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.sf.jasperreports.engine.JRBoxContainer;
import net.sf.jasperreports.engine.JRCommonGraphicElement;
import net.sf.jasperreports.engine.JRPrintElement;


/**
 * @author sanda zaharia (shertage@users.sourceforge.net)
 * @version $Id: StyleCache.java 2908 2009-07-21 14:32:01Z teodord $
 */
public class TableCellHelper
{
	/**
	 *
	 */
	private Writer styleWriter = null;//FIXMEDOCX rename this to docs writer
	private Map fontMap = null;
	private Set fontFaces = new HashSet();

	/**
	 *
	 */
	private Map cellStyles = new HashMap();
	private int cellStylesCounter = 0;


	/**
	 *
	 */
	public TableCellHelper(Writer styleWriter, Map fontMap)
	{
		this.styleWriter = styleWriter;
		this.fontMap = fontMap;
	}


	/**
	 *
	 */
	public Collection getFontFaces()
	{
		return fontFaces;
	}


	/**
	 *
	 */
	public String getCellStyle(JRPrintElement element) throws IOException
	{
		CellHelper cellStyle  = new CellHelper(styleWriter, element);
		
		if (element instanceof JRBoxContainer)
			cellStyle.setBox(((JRBoxContainer)element).getLineBox());
		if (element instanceof JRCommonGraphicElement)
			cellStyle.setPen(((JRCommonGraphicElement)element).getLinePen());
		
		String cellStyleId = cellStyle.getId();
		String cellStyleName = null;//(String)cellStyles.get(cellStyleId);
		
		if (cellStyleName == null)
		{
			cellStyleName = "C" + cellStylesCounter++;
			cellStyles.put(cellStyleId, cellStyleName);
			
			cellStyle.export(cellStyleName);
		}
		
		return cellStyleName;
	}


}

