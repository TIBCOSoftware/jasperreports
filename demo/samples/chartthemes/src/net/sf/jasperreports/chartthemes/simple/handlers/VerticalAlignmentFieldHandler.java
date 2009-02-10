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
import org.jfree.ui.VerticalAlignment;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id: GenericChartTheme.java 2535 2009-01-16 14:06:40Z teodord $
 */
public class VerticalAlignmentFieldHandler extends GeneralizedFieldHandler
{
	/**
	 *
	 */
	public VerticalAlignmentFieldHandler()
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
		return ((VerticalAlignment)value).toString();
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
			VerticalAlignment.TOP.toString().equals(value) 
			? VerticalAlignment.TOP 
			: VerticalAlignment.CENTER.toString().equals(value)
			? VerticalAlignment.CENTER
			: VerticalAlignment.BOTTOM.toString().equals(value)
			? VerticalAlignment.BOTTOM : null;
	}
	
	/**
	 *
	 */
	public Class getFieldType()
	{
		return VerticalAlignment.class;
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
