/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2019 TIBCO Software Inc. All rights reserved.
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
package net.sf.jasperreports.engine.util;

/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public class StyledTextListInfo
{
	private final boolean ordered;
	public final String type;
	public Integer start;
	public final boolean hasParentLi;
	
	private int itemCount = 0;
	private boolean insideLi = false;
	
	public StyledTextListInfo(
		boolean ordered,
		String type,
		Integer start,
		boolean hasParentLi
		)
	{
		this.ordered = ordered;
		this.type = type;
		this.start = start;
		this.hasParentLi = hasParentLi;
	}

	public boolean ordered()
	{
		return ordered;
	}

	public String getType()
	{
		return type;
	}

	public Integer getStart()
	{
		return start;
	}

	public void setStart(int start)
	{
		this.start = start;
	}

	public boolean hasParentLi()
	{
		return hasParentLi;
	}

	public int getItemCount()
	{
		return itemCount;
	}

	public void setItemCount(int itemCount)
	{
		this.itemCount = itemCount;
	}

	public boolean insideLi()
	{
		return insideLi;
	}

	public void setInsideLi(boolean insideLi)
	{
		this.insideLi = insideLi;
	}
}
