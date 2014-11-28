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
package net.sf.jasperreports.chartthemes.simple.handlers;

import org.exolab.castor.mapping.GeneralizedFieldHandler;
import org.jfree.ui.Align;


/**
 * @author sanda zaharia (shertage@users.sourceforge.net)
 */
public class ImageAlignmentFieldHandler extends GeneralizedFieldHandler
{
	/**
	 *
	 */
	public ImageAlignmentFieldHandler()
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
		return 
			new Integer(Align.BOTTOM).equals(value) 
			? "Align.BOTTOM"
			: new Integer(Align.BOTTOM_LEFT).equals(value)
			? "Align.BOTTOM_LEFT"
			: new Integer(Align.BOTTOM_RIGHT).equals(value)
			? "Align.BOTTOM_RIGHT"
			: new Integer(Align.CENTER).equals(value)
			? "Align.CENTER"
			: new Integer(Align.FIT).equals(value)
			? "Align.FIT"
			: new Integer(Align.FIT_HORIZONTAL).equals(value)
			? "Align.FIT_HORIZONTAL"
			: new Integer(Align.FIT_VERTICAL).equals(value)
			? "Align.FIT_VERTICAL"
			: new Integer(Align.LEFT).equals(value)
			? "Align.LEFT"
			: new Integer(Align.RIGHT).equals(value)
			? "Align.RIGHT"
			: new Integer(Align.TOP).equals(value)
			? "Align.TOP"
			: new Integer(Align.TOP_LEFT).equals(value)
			? "Align.TOP_LEFT"
			: new Integer(Align.TOP_RIGHT).equals(value)
			? "Align.TOP_RIGHT" : null;
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
			"Align.BOTTOM".equals(value) 
			? new Integer(Align.BOTTOM) 
			: "Align.BOTTOM_LEFT".equals(value)
			? new Integer(Align.BOTTOM_LEFT)
			: "Align.BOTTOM_RIGHT".equals(value)
			? new Integer(Align.BOTTOM_RIGHT)
			: "Align.CENTER".equals(value)
			? new Integer(Align.CENTER)
			: "Align.FIT".equals(value)
			? new Integer(Align.FIT)
			: "Align.FIT_HORIZONTAL".equals(value)
			? new Integer(Align.FIT_HORIZONTAL)
			: "Align.FIT_VERTICAL".equals(value)
			? new Integer(Align.FIT_VERTICAL)
			: "Align.LEFT".equals(value)
			? new Integer(Align.LEFT)
			: "Align.RIGHT".equals(value)
			? new Integer(Align.RIGHT)
			: "Align.TOP".equals(value)
			? new Integer(Align.TOP)
			: "Align.TOP_LEFT".equals(value)
			? new Integer(Align.TOP_LEFT)
			: "Align.TOP_RIGHT".equals(value)
			? new Integer(Align.TOP_RIGHT) : null;
	}
	
	/**
	 *
	 */
	public Class<?> getFieldType()
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
