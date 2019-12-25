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
package net.sf.jasperreports.chartthemes.simple.handlers;

import org.exolab.castor.mapping.GeneralizedFieldHandler;
import org.jfree.ui.Align;


/**
 * @author Sanda Zaharia (shertage@users.sourceforge.net)
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
	
	@Override
	public Object convertUponGet(Object value)
	{
		if (value == null)
		{
			return null;
		}
		return 
			value.equals(Align.BOTTOM) 
			? "Align.BOTTOM"
			: value.equals(Align.BOTTOM_LEFT)
			? "Align.BOTTOM_LEFT"
			: value.equals(Align.BOTTOM_RIGHT)
			? "Align.BOTTOM_RIGHT"
			: value.equals(Align.CENTER)
			? "Align.CENTER"
			: value.equals(Align.FIT)
			? "Align.FIT"
			: value.equals(Align.FIT_HORIZONTAL)
			? "Align.FIT_HORIZONTAL"
			: value.equals(Align.FIT_VERTICAL)
			? "Align.FIT_VERTICAL"
			: value.equals(Align.LEFT)
			? "Align.LEFT"
			: value.equals(Align.RIGHT)
			? "Align.RIGHT"
			: value.equals(Align.TOP)
			? "Align.TOP"
			: value.equals(Align.TOP_LEFT)
			? "Align.TOP_LEFT"
			: value.equals(Align.TOP_RIGHT)
			? "Align.TOP_RIGHT" : null;
	}

	@Override
	public Object convertUponSet(Object value)
	{
		if (value == null)
		{
			return null;
		}
		return 
			"Align.BOTTOM".equals(value) 
			? Align.BOTTOM
			: "Align.BOTTOM_LEFT".equals(value)
			? Align.BOTTOM_LEFT
			: "Align.BOTTOM_RIGHT".equals(value)
			? Align.BOTTOM_RIGHT
			: "Align.CENTER".equals(value)
			? Align.CENTER
			: "Align.FIT".equals(value)
			? Align.FIT
			: "Align.FIT_HORIZONTAL".equals(value)
			? Align.FIT_HORIZONTAL
			: "Align.FIT_VERTICAL".equals(value)
			? Align.FIT_VERTICAL
			: "Align.LEFT".equals(value)
			? Align.LEFT
			: "Align.RIGHT".equals(value)
			? Align.RIGHT
			: "Align.TOP".equals(value)
			? Align.TOP
			: "Align.TOP_LEFT".equals(value)
			? Align.TOP_LEFT
			: "Align.TOP_RIGHT".equals(value)
			? Align.TOP_RIGHT : null;
	}
	
	@Override
	public Class<?> getFieldType()
	{
		return Integer.class;
	}

	@Override
	public Object newInstance(Object parent) throws IllegalStateException
	{
		//-- Since it's marked as a string...just return null,
		//-- it's not needed.
		return null;
	}
}
