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
package net.sf.jasperreports.chartthemes.simple.handlers;

import org.exolab.castor.mapping.GeneralizedFieldHandler;
import org.jfree.ui.Align;
import org.jfree.ui.HorizontalAlignment;


/**
 * @author sanda zaharia (shertage@users.sourceforge.net)
 * @version $Id$
 */
public class BackgoundImageAlignmentFieldHandler extends GeneralizedFieldHandler
{
	/**
	 *
	 */
	public BackgoundImageAlignmentFieldHandler()
	{
		super();
	}
	
	/**
	 *
	 */
	public Object convertUponGet(Object value)
	{
		if (value == null)
		{
			return null;
		}
		return ((Integer)value).toString();
	}

	/**
	 *
	 */
	public Object convertUponSet(Object value)
	{
		if (value == null)
		{
			return null;
		}
		return 
			String.valueOf(Align.BOTTOM).equals(value) 
			? new Integer(Align.BOTTOM) 
			: String.valueOf(Align.BOTTOM_LEFT).equals(value)
			? new Integer(Align.BOTTOM_LEFT)
			: String.valueOf(Align.BOTTOM_RIGHT).equals(value)
			? new Integer(Align.BOTTOM_RIGHT)
			: String.valueOf(Align.CENTER).equals(value)
			? new Integer(Align.CENTER)
			: String.valueOf(Align.EAST).equals(value)
			? new Integer(Align.EAST)
			: String.valueOf(Align.FIT).equals(value)
			? new Integer(Align.FIT)
			: String.valueOf(Align.FIT_HORIZONTAL).equals(value)
			? new Integer(Align.FIT_HORIZONTAL)
			: String.valueOf(Align.FIT_VERTICAL).equals(value)
			? new Integer(Align.FIT_VERTICAL)
			: String.valueOf(Align.LEFT).equals(value)
			? new Integer(Align.LEFT)
			: String.valueOf(Align.NORTH).equals(value)
			? new Integer(Align.NORTH)
			: String.valueOf(Align.NORTH_EAST).equals(value)
			? new Integer(Align.NORTH_EAST)
			: String.valueOf(Align.NORTH_WEST).equals(value)
			? new Integer(Align.NORTH_WEST)
			: String.valueOf(Align.RIGHT).equals(value)
			? new Integer(Align.RIGHT)
			: String.valueOf(Align.SOUTH).equals(value)
			? new Integer(Align.SOUTH)
			: String.valueOf(Align.SOUTH_EAST).equals(value)
			? new Integer(Align.SOUTH_EAST)
			: String.valueOf(Align.SOUTH_WEST).equals(value)
			? new Integer(Align.SOUTH_WEST)
			: String.valueOf(Align.TOP).equals(value)
			? new Integer(Align.TOP)
			: String.valueOf(Align.TOP_LEFT).equals(value)
			? new Integer(Align.TOP_LEFT)
			: String.valueOf(Align.TOP_RIGHT).equals(value)
			? new Integer(Align.TOP_RIGHT)
			: String.valueOf(Align.WEST).equals(value)
			? new Integer(Align.WEST) : null;
	}
	
	/**
	 *
	 */
	public Class getFieldType()
	{
		return Integer.class;
	}

	/**
	 *
	 */
	public Object newInstance(Object parent) throws IllegalStateException
	{
		//-- Since it's marked as a string...just return null,
		//-- it's not needed.
		return null;
	}
}
