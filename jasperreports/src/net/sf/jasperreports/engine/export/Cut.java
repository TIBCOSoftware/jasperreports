/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2011 Jaspersoft Corporation. All rights reserved.
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

import java.util.SortedMap;


/**
 * Utility class used by grid exporters to create a grid for page layout.
 * 
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id: CutsInfo.java 4595 2011-09-08 15:55:10Z teodord $
 */
public class Cut
{
	public static final int USAGE_NOT_EMPTY = 1;
	public static final int USAGE_SPANNED = 2;
	public static final int USAGE_BREAK = 4;
	
	private int position;
	private int usage;
	
	//TODO: to extend the Cut class in order to add attributes to be used in particular output formats only; 
	private Boolean autoFit;
	private Integer customWidth;
	private SortedMap<String, Boolean> rowLevelMap;//FIXME move this in special Cut implementation for rows/y
	
	public Cut()
	{
	}
	
	public int getPosition()
	{
		return position;
	}
	
	public void setPosition(int position)
	{
		this.position = position;
	}
	
	public int getUsage()
	{
		return usage;
	}
	
	public void setUsage(int usage)
	{
		this.usage = usage;
	}

	/**
	 * Decides whether a cut is empty or not.
	 * 
	 * @param index the cut index
	 * @return <code>true</code> if and only if the cut is not empty
	 */
	public boolean isCutNotEmpty()
	{
		return ((getUsage() & Cut.USAGE_NOT_EMPTY) > 0);
	}

	/**
	 * Decides whether a cut is occupied by spanning cells or not.
	 * 
	 * @param index the cut index
	 * @return <code>true</code> if and only if the cut is not empty
	 */
	public boolean isCutSpanned()
	{
		return ((getUsage() & Cut.USAGE_SPANNED) > 0);
	}

	public boolean isAutoFit()
	{
		return autoFit == null ? false : autoFit.booleanValue();
	}
	
	public void setAutoFit(boolean autoFit)
	{
		if (this.autoFit == null)
		{
			this.autoFit = autoFit;
		}
		else
		{
			this.autoFit = this.autoFit && autoFit;
		}
	}
	
	public Integer getCustomWidth() {
		return this.customWidth;
	}

	public void setCustomWidth(Integer customWidth) {
		this.customWidth = customWidth;
	}

	public SortedMap<String, Boolean> getRowLevelMap()
	{
		return this.rowLevelMap;
	}

	public void setRowLevelMap(SortedMap<String, Boolean> rowLevelMap)
	{
		this.rowLevelMap = rowLevelMap;
	}

}
