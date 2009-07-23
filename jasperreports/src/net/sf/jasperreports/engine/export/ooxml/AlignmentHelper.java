/*
 * ============================================================================
 * GNU Lesser General Public License
 * ============================================================================
 *
 * JasperReports - Free Java report-generating library.
 * Copyright (C) 2001-2009 JasperSoft Corporation http://www.jaspersoft.com
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
 * 539 Bryant Street, Suite 100
 * San Francisco, CA 94107
 * http://www.jaspersoft.com
 */
package net.sf.jasperreports.engine.export.ooxml;

import net.sf.jasperreports.engine.JRAlignment;
import net.sf.jasperreports.engine.JRTextElement;


/**
 * @author sanda zaharia (shertage@users.sourceforge.net)
 * @version $Id: ParagraphHelper.java 2912 2009-07-21 19:32:19Z teodord $
 */
public class AlignmentHelper
{
	/**
	 *
	 */
	protected static final String HORIZONTAL_ALIGN_LEFT = "left";
	protected static final String HORIZONTAL_ALIGN_RIGHT = "right";
	protected static final String HORIZONTAL_ALIGN_CENTER = "center";
	protected static final String HORIZONTAL_ALIGN_JUSTIFY = "both";

	/**
	 *
	 */
	private static final String VERTICAL_ALIGN_TOP = "top";
	private static final String VERTICAL_ALIGN_MIDDLE = "center";
	private static final String VERTICAL_ALIGN_BOTTOM = "bottom";

	/**
	*
	*/
	protected static final String ROTATION_ALIGN_NONE = "none";
	protected static final String ROTATION_ALIGN_TOP = "top";
	protected static final String ROTATION_ALIGN_CENTER = "center";
	protected static final String ROTATION_ALIGN_BOTTOM = "bottom";

	/**
	 *
	 */
	public static String getVerticalAlignment(
		byte horizontalAlignment, 
		byte verticalAlignment, 
		byte rotation
		)
	{
		switch(rotation)
		{
			case JRTextElement.ROTATION_LEFT:
			{
				switch (horizontalAlignment)
				{
					case JRAlignment.HORIZONTAL_ALIGN_RIGHT :
						return VERTICAL_ALIGN_TOP;
					case JRAlignment.HORIZONTAL_ALIGN_CENTER :
						return VERTICAL_ALIGN_MIDDLE;
					case JRAlignment.HORIZONTAL_ALIGN_JUSTIFIED :
						return HORIZONTAL_ALIGN_JUSTIFY;//FIXMEDOCX ?????????????????
					case JRAlignment.HORIZONTAL_ALIGN_LEFT :
					default :
						return VERTICAL_ALIGN_BOTTOM;
				}
			}
			case JRTextElement.ROTATION_RIGHT:
			{
				switch (horizontalAlignment)
				{
					case JRAlignment.HORIZONTAL_ALIGN_RIGHT :
						return VERTICAL_ALIGN_BOTTOM;
					case JRAlignment.HORIZONTAL_ALIGN_CENTER :
						return VERTICAL_ALIGN_MIDDLE;
					case JRAlignment.HORIZONTAL_ALIGN_JUSTIFIED :
						return HORIZONTAL_ALIGN_JUSTIFY;//?????????????????
					case JRAlignment.HORIZONTAL_ALIGN_LEFT :
					default :
						return VERTICAL_ALIGN_TOP;
				}
			}
			case JRTextElement.ROTATION_UPSIDE_DOWN://FIXMEDOCX possible?
			case JRTextElement.ROTATION_NONE:
			default:
			{
				switch (verticalAlignment)
				{
					case JRAlignment.VERTICAL_ALIGN_BOTTOM :
						return VERTICAL_ALIGN_BOTTOM;
					case JRAlignment.VERTICAL_ALIGN_MIDDLE :
						return VERTICAL_ALIGN_MIDDLE;
					case JRAlignment.VERTICAL_ALIGN_TOP :
					default :
						return VERTICAL_ALIGN_TOP;
				}
			}
		}
	}
	
	/**
	 *
	 */
	public static String getHorizontalAlignment(
		byte horizontalAlignment, 
		byte verticalAlignment, 
		byte rotation
		)
	{
		switch(rotation)
		{
			case JRTextElement.ROTATION_LEFT:
			{
				switch (verticalAlignment)
				{
					case JRAlignment.VERTICAL_ALIGN_BOTTOM :
						return HORIZONTAL_ALIGN_RIGHT;
					case JRAlignment.VERTICAL_ALIGN_MIDDLE :
						return HORIZONTAL_ALIGN_CENTER;
					case JRAlignment.VERTICAL_ALIGN_TOP :
					default :
						return HORIZONTAL_ALIGN_LEFT;
				}
			}
			case JRTextElement.ROTATION_RIGHT:
			{
				switch (verticalAlignment)
				{
					case JRAlignment.VERTICAL_ALIGN_BOTTOM :
						return HORIZONTAL_ALIGN_LEFT;
					case JRAlignment.VERTICAL_ALIGN_MIDDLE :
						return HORIZONTAL_ALIGN_CENTER;
					case JRAlignment.VERTICAL_ALIGN_TOP :
					default :
						return HORIZONTAL_ALIGN_RIGHT;
				}
			}
			case JRTextElement.ROTATION_UPSIDE_DOWN://FIXMEDOCX possible?
			case JRTextElement.ROTATION_NONE:
			default:
			{
				switch (horizontalAlignment)
				{
					case JRAlignment.HORIZONTAL_ALIGN_RIGHT :
						return HORIZONTAL_ALIGN_RIGHT;
					case JRAlignment.HORIZONTAL_ALIGN_CENTER :
						return HORIZONTAL_ALIGN_CENTER;
					case JRAlignment.HORIZONTAL_ALIGN_JUSTIFIED :
						return HORIZONTAL_ALIGN_JUSTIFY;
					case JRAlignment.HORIZONTAL_ALIGN_LEFT :
					default :
						return HORIZONTAL_ALIGN_LEFT;
				}
			}
		}
	}
	
}

