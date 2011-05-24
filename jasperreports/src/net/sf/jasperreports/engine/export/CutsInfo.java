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
 * @version $Id$
 */
public class CutsInfo
{
	public static final int USAGE_NOT_EMPTY = 1;
	public static final int USAGE_SPANNED = 2;
	public static final int USAGE_BREAK = 4;
	
	private final List<Integer> cuts = new ArrayList<Integer>();
	private int[] cutUsage;
	
	public CutsInfo()
	{
		addCut(Integer.valueOf(0));
	}
	
	public CutsInfo(int lastCut)
	{
		this();
		addCut(Integer.valueOf(lastCut));
	}
	
	public List<Integer> getCuts()
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
		return cuts.get(index).intValue();
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
	
	protected boolean addCut(int cut)
	{
		return addCut(Integer.valueOf(cut));
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
		return indexOfCut(Integer.valueOf(cut));
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
	 * @return <code>true</code> if and only if the cut is not empty
	 */
	public boolean isCutNotEmpty(int index)
	{
		return ((cutUsage[index] & USAGE_NOT_EMPTY) > 0);
	}

	/**
	 * Decides whether a cut is occupied by spanning cells or not.
	 * 
	 * @param index the cut index
	 * @return <code>true</code> if and only if the cut is not empty
	 */
	public boolean isCutSpanned(int index)
	{
		return ((cutUsage[index] & USAGE_SPANNED) > 0);
	}

	/**
	 * 
	 */
	public boolean isBreak(int index)
	{
		return ((cutUsage[index] & USAGE_BREAK) > 0);
	}
	
	public boolean hasCuts()
	{
		return !cuts.isEmpty();
	}
	
	public int getFirstCut()
	{
		return getCut(0);
	}
	
	public int getLastCut()
	{
		return getCut(size() - 1);
	}
	
	public int getTotalLength()
	{
		return hasCuts() ? getLastCut() - getFirstCut() : 0;
	}
}
