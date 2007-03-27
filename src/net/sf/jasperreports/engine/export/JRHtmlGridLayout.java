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

import java.util.List;

import net.sf.jasperreports.engine.JRPrintElement;

/**
 * Utility class used by grid exporters to create a grid for page layout.
 * 
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JRHtmlGridLayout extends JRGridLayout
{
	
	/**
	 * 
	 */
	public JRHtmlGridLayout(
		List elements, 
		int width, 
		int height, 
		int offsetX, 
		int offsetY 
		)
	{
		super(
			elements,
			width, 
			height, 
			offsetX, 
			offsetY
			);
	}
	
	/**
	 * 
	 */
	public JRHtmlGridLayout(
		List elements, 
		int width, 
		int height, 
		int offsetX, 
		int offsetY, 
		List xCuts
		)
	{
		super(
			elements,
			width, 
			height, 
			offsetX, 
			offsetY,
			xCuts
			);
	}

	/**
	 * 
	 */
	private JRHtmlGridLayout(
		ElementWrapper[] wrappers, 
		int width, 
		int height, 
		int offsetX, 
		int offsetY, 
		String address
		)
	{
		super(
			wrappers, 
			width, 
			height, 
			offsetX,
			offsetY,
			address
			);
	}


	/**
	 * 
	 */
	public boolean isToExport(JRPrintElement element)
	{
		return true;
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
		return false;
	}
	
	/**
	 * 
	 */
	public JRGridLayout newInstance(
		ElementWrapper[] wrappers, 
		int width, 
		int height, 
		int offsetX,
		int offsetY,
		String address
		)
	{
		return 
			new JRHtmlGridLayout(
				wrappers, 
				width, 
				height, 
				offsetX,
				offsetY,
				address
				);
	}
		
}
