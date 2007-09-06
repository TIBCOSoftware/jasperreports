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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sf.jasperreports.engine.JRPrintElement;

/**
 * Utility class used by grid exporters to create a grid for page layout.
 * 
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id: JRGridLayout.java 1806 2007-08-06 12:36:33Z teodord $
 */
public class CutsInfo
{
	public static final int USAGE_NOT_EMPTY = 1;
	public static final int USAGE_SPANNED = 2;
	
	private final List cuts = new ArrayList();
	private int[] cutUsage;
	
	public CutsInfo()
	{
		addCut(new Integer(0));
	}
	
	public CutsInfo(int lastCut)
	{
		this();
		addCut(new Integer(lastCut));
	}
	
	public List getCuts()
	{
		return cuts;
	}
	
	public int size()
	{
		return cuts.size();
	}
	
	public void use()
	{
		if (cutUsage == null)
		{
			cutUsage = new int[cuts.size()];
		}
	}
	
	public int getCut(int index)
	{
		return ((Integer)cuts.get(index)).intValue();
	}
	
	public void addXCuts(JRPrintElement element, int offset)
	{
		addCut(element.getX() + offset);
		addCut(element.getX() + element.getWidth() + offset);
	}
	
	public void addYCuts(JRPrintElement element, int offset)
	{
		addCut(element.getY() + offset);
		addCut(element.getY() + element.getHeight() + offset);
	}
	
	private boolean addCut(int cut)
	{
		return addCut(new Integer(cut));
	}
	
	public void addUsage(int index, int usage)
	{
		cutUsage[index] |= usage;
	}
	
	private boolean addCut(Integer cut)
	{
		int idx = Collections.binarySearch(cuts, cut);
		
		if (idx >= 0)
		{
			return false;
		}
		
		cuts.add(-idx - 1, cut);
		return true;
	}
	
	public int indexOfCut(int cut)
	{
		return indexOfCut(new Integer(cut));
	}
	
	private int indexOfCut(Integer cut)
	{
		int idx = Collections.binarySearch(cuts, cut);
		
		if (idx < 0)
		{
			idx = -1;
		}
		
		return idx;
	}

	/**
	 * Decides whether a cut is empty or not.
	 * 
	 * @param index the cut index
	 * @return <code>true</code> iff the cut is not empty
	 */
	public boolean isCutNotEmpty(int index)
	{
		return ((cutUsage[index] & USAGE_NOT_EMPTY) > 0);
	}

	/**
	 * Decides whether a cut is occupied by spanning cells or not.
	 * 
	 * @param index the cut index
	 * @return <code>true</code> iff the cut is not empty
	 */
	public boolean isCutSpanned(int index)
	{
		return ((cutUsage[index] & USAGE_SPANNED) > 0);
	}


}
