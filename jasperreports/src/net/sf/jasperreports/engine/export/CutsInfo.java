/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2014 TIBCO Software Inc. All rights reserved.
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

import java.util.HashMap;
import java.util.Map;

import net.sf.jasperreports.engine.util.SortedIntList;

/**
 * Utility class used by grid exporters to create a grid for page layout.
 * 
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public class CutsInfo
{
//	public static final int USAGE_NOT_EMPTY = 1;
//	public static final int USAGE_SPANNED = 2;
//	public static final int USAGE_BREAK = 4;
	
	private final SortedIntList cutOffsets = new SortedIntList();
//	private int[] cutUsage;
	private Cut[] cuts;
	
	private Map<String, Object> propertiesMap;
	
	public CutsInfo()
	{
		propertiesMap = new HashMap<String, Object>();
		addCutOffset(Integer.valueOf(0));
	}
	
	public CutsInfo(Integer lastCutOffset)
	{
		this();
		addCutOffset(lastCutOffset);
	}
	
	public int size()
	{
		return cutOffsets.size();
	}
	
	public void use()//FIXMEXLS maybe remove this
	{
		if (cuts == null)
		{
			cuts = new Cut[cutOffsets.size()];
		}
	}
	
	public int getCutOffset(int index)
	{
		return cutOffsets.get(index);
	}
	
	public Cut getCut(int index)
	{
		Cut cut = cuts[index];
		if (cut == null)
		{
			cut = new Cut();
			cuts[index] = cut;
		}
		return cut;
	}
	
	public void addUsage(int index, int usage)
	{
		Cut cut = getCut(index);
		int tmpUsage = cut.getUsage();
		tmpUsage |= usage;
		cut.setUsage(tmpUsage);
	}
	
	public boolean addCutOffset(int cutOffset)
	{
		return cutOffsets.add(cutOffset);
	}

	public boolean removeCutOffset(int cutOffset)
	{
		return cutOffsets.remove(cutOffset);
	}
	
	public int indexOfCutOffset(Integer cutOffset)
	{
		return cutOffsets.indexOf(cutOffset);
	}

	/**
	 * Decides whether a cut is empty or not.
	 * 
	 * @param index the cut index
	 * @return <code>true</code> if and only if the cut is not empty
	 */
	public boolean isCutNotEmpty(int index)//FIXMEXLS do this in Cut
	{
		return getCut(index).isCutNotEmpty();
	}

	/**
	 * Decides whether a cut is occupied by spanning cells or not.
	 * 
	 * @param index the cut index
	 * @return <code>true</code> if and only if the cut is not empty
	 */
	public boolean isCutSpanned(int index)
	{
		return getCut(index).isCutSpanned();
	}

	/**
	 * 
	 */
	public boolean isBreak(int index)
	{
		Cut cut = getCut(index);
		return ((cut.getUsage() & Cut.USAGE_BREAK) > 0);
	}
	
	public boolean hasCuts()
	{
		return !cutOffsets.isEmpty();
	}
	
	public int getFirstCutOffset()
	{
		return getCutOffset(0);
	}
	
	public int getLastCutOffset()
	{
		return getCutOffset(size() - 1);
	}
	
	public int getTotalLength()
	{
		return hasCuts() ? getLastCutOffset() - getFirstCutOffset() : 0;
	}

	public Map<String, Object> getPropertiesMap() 
	{
		return propertiesMap;
	}

}
