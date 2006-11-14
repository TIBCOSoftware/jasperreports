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
package net.sf.jasperreports.engine;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JRPrintElementIndex
{


	/**
	 *
	 */
	private int reportIndex = 0;
	private int pageIndex = 0;
	private List elementIndexes = null;


	/**
	 *
	 */
	public JRPrintElementIndex(
		int reportIndex,
		int pageIndex,
		Integer[] elementIndexes
		)
	{
		this.reportIndex = reportIndex;
		this.pageIndex = pageIndex;
		this.elementIndexes = Arrays.asList(elementIndexes);
	}


	/**
	 *
	 */
	public int getReportIndex()
	{
		return this.reportIndex;
	}
		

	/**
	 *
	 */
	public int getPageIndex()
	{
		return this.pageIndex;
	}
		

	/**
	 *
	 */
	public Integer[] getElementIndexes()
	{
		return (Integer[]) elementIndexes.toArray(new Integer[elementIndexes.size()]);
	}


	/**
	 * Returns a String representation of this element index.
	 * <p>
	 * The representation is obtained by appending all the indexes that compose this instance.
	 * The result is compatible with {@link #parsePrintElementIndex(String) parsePrintElementIndex(String)},
	 * which can be used to recreate the elemetn index instance from a String representation.
	 * </p>
	 */
	public String toString()
	{
		StringBuffer str = new StringBuffer();
		str.append(reportIndex);
		str.append('_');
		str.append(pageIndex);
		for (Iterator it = elementIndexes.iterator(); it.hasNext();)
		{
			Integer idx = (Integer) it.next();
			str.append('_');
			str.append(idx);			
		}
		return str.toString();
	}
	
	
	/**
	 * Parses a String representation as obtained by {@link #toString() toString()} back
	 * into an element index instance.
	 * 
	 * @param indexStr the String representation of an element index
	 * @return an element index instance corresponding to the String representation
	 */
	public static JRPrintElementIndex parsePrintElementIndex(String indexStr)
	{
		StringTokenizer tkzer = new StringTokenizer(indexStr, "_");
		
		int reportIndex = Integer.parseInt(tkzer.nextToken());
		int pageIndex = Integer.parseInt(tkzer.nextToken());

		Integer[] elementIndexes = new Integer[tkzer.countTokens()];
		int c = 0;
		while (tkzer.hasMoreTokens())
		{
			elementIndexes[c++] = Integer.valueOf(tkzer.nextToken());
		}

		return
			new JRPrintElementIndex(
				reportIndex,
				pageIndex,
				elementIndexes
				);
	}
}
