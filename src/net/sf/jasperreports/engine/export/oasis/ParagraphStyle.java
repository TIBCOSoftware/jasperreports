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
package net.sf.jasperreports.engine.export.oasis;

import java.io.IOException;
import java.io.Writer;

import net.sf.jasperreports.engine.JRPrintText;
import net.sf.jasperreports.engine.JRTextElement;
import net.sf.jasperreports.engine.type.HorizontalAlignEnum;
import net.sf.jasperreports.engine.type.VerticalAlignEnum;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class ParagraphStyle extends Style
{
	/**
	 *
	 */
	protected static final String HORIZONTAL_ALIGN_LEFT = "start";
	protected static final String HORIZONTAL_ALIGN_RIGHT = "end";
	protected static final String HORIZONTAL_ALIGN_CENTER = "center";
	protected static final String HORIZONTAL_ALIGN_JUSTIFY = "justified";

	/**
	 *
	 */
	private static final String VERTICAL_ALIGN_TOP = "top";
	private static final String VERTICAL_ALIGN_MIDDLE = "middle";
	private static final String VERTICAL_ALIGN_BOTTOM = "bottom";

	/**
	*
	*/
	protected static final String ROTATION_ALIGN_NONE = "none";
	protected static final String ROTATION_ALIGN_TOP = "top";
	protected static final String ROTATION_ALIGN_CENTER = "center";
	protected static final String ROTATION_ALIGN_BOTTOM = "bottom";

	private String verticalAlignment = null;
	private String horizontalAlignment = null;
	private String runDirection = null;
	private String textRotation = "0";

	/**
	 *
	 */
	public ParagraphStyle(Writer styleWriter, JRPrintText text)
	{
		super(styleWriter);
		
		horizontalAlignment = getHorizontalAlignment(text.getHorizontalAlignmentValue(), text.getVerticalAlignmentValue(), text.getRotation());
		verticalAlignment = getVerticalAlignment(text.getHorizontalAlignmentValue(), text.getVerticalAlignmentValue(), text.getRotation());
		
		switch(text.getRotation())
		{
			case JRTextElement.ROTATION_LEFT:
			{
				textRotation = "90";
				break;
			}
			case JRTextElement.ROTATION_RIGHT:
			{
				textRotation = "270";
				break;
			}
			case JRTextElement.ROTATION_UPSIDE_DOWN://FIXMEODT possible?
			case JRTextElement.ROTATION_NONE:
			default:
			{
				textRotation = "0";
			}
		}

		runDirection = null;
		if (text.getRunDirection() == JRPrintText.RUN_DIRECTION_RTL)
		{
			runDirection = "rl";
		}
	}
	
	/**
	 *
	 */
	public static String getVerticalAlignment(
		HorizontalAlignEnum horizontalAlignment, 
		VerticalAlignEnum verticalAlignment, 
		byte rotation
		)
	{
		switch(rotation)
		{
			case JRTextElement.ROTATION_LEFT:
			{
				switch (horizontalAlignment)
				{
					case RIGHT :
						return VERTICAL_ALIGN_TOP;
					case CENTER :
						return VERTICAL_ALIGN_MIDDLE;
					case JUSTIFIED :
						return HORIZONTAL_ALIGN_JUSTIFY;//FIXMEODT ?????????????????
					case LEFT :
					default :
						return VERTICAL_ALIGN_BOTTOM;
				}
			}
			case JRTextElement.ROTATION_RIGHT:
			{
				switch (horizontalAlignment)
				{
					case RIGHT :
						return VERTICAL_ALIGN_BOTTOM;
					case CENTER :
						return VERTICAL_ALIGN_MIDDLE;
					case JUSTIFIED :
						return HORIZONTAL_ALIGN_JUSTIFY;//?????????????????
					case LEFT :
					default :
						return VERTICAL_ALIGN_TOP;
				}
			}
			case JRTextElement.ROTATION_UPSIDE_DOWN://FIXMEODT possible?
			case JRTextElement.ROTATION_NONE:
			default:
			{
				switch (verticalAlignment)
				{
					case BOTTOM :
						return VERTICAL_ALIGN_BOTTOM;
					case MIDDLE :
						return VERTICAL_ALIGN_MIDDLE;
					case TOP :
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
		HorizontalAlignEnum horizontalAlignment, 
		VerticalAlignEnum verticalAlignment, 
		byte rotation
		)
	{
		switch(rotation)
		{
			case JRTextElement.ROTATION_LEFT:
			{
				switch (verticalAlignment)
				{
					case BOTTOM :
						return HORIZONTAL_ALIGN_RIGHT;
					case MIDDLE :
						return HORIZONTAL_ALIGN_CENTER;
					case TOP :
					default :
						return HORIZONTAL_ALIGN_LEFT;
				}
			}
			case JRTextElement.ROTATION_RIGHT:
			{
				switch (verticalAlignment)
				{
					case BOTTOM :
						return HORIZONTAL_ALIGN_LEFT;
					case MIDDLE :
						return HORIZONTAL_ALIGN_CENTER;
					case TOP :
					default :
						return HORIZONTAL_ALIGN_RIGHT;
				}
			}
			case JRTextElement.ROTATION_UPSIDE_DOWN://FIXMEODT possible?
			case JRTextElement.ROTATION_NONE:
			default:
			{
				switch (horizontalAlignment)
				{
					case RIGHT :
						return HORIZONTAL_ALIGN_RIGHT;
					case CENTER :
						return HORIZONTAL_ALIGN_CENTER;
					case JUSTIFIED :
						return HORIZONTAL_ALIGN_JUSTIFY;
					case LEFT :
					default :
						return HORIZONTAL_ALIGN_LEFT;
				}
			}
		}
	}
	
	/**
	 *
	 */
	public String getId()
	{
		return verticalAlignment + "|" + horizontalAlignment + "|" + runDirection + "|" + textRotation;
	}

	/**
	 *
	 */
	public void write(String paragraphStyleName) throws IOException
	{
		styleWriter.write("<style:style style:name=\"" + paragraphStyleName + "\"");
		styleWriter.write(" style:family=\"paragraph\">\n");
		styleWriter.write("<style:paragraph-properties");
//		styleWriter.write(" fo:line-height=\"" + pLineHeight + "\"");
//		styleWriter.write(" style:line-spacing=\"" + pLineSpacing + "\"");
		styleWriter.write(" fo:text-align=\"" + horizontalAlignment + "\"");

//		styleWriter.write(" fo:keep-together=\"" + pKeepTogether + "\"");
//		styleWriter.write(" fo:margin-left=\"" + pMarginLeft + "\"");
//		styleWriter.write(" fo:margin-right=\"" + pMarginRight + "\"");
//		styleWriter.write(" fo:margin-top=\"" + pMarginTop + "\"");
//		styleWriter.write(" fo:margin-bottom=\"" + pMarginBottom + "\"");
//		styleWriter.write(" fo:background-color=\"#" + pBackGroundColor + "\"");
		styleWriter.write(" style:vertical-align=\"" + verticalAlignment + "\"");
		if (runDirection != null)
		{
			styleWriter.write(" style:writing-mode=\"" + runDirection + "\"");
		}
		styleWriter.write(">\n");
		styleWriter.write("</style:paragraph-properties>\n");
		styleWriter.write("<style:text-properties");
		styleWriter.write(" style:text-rotation-angle=\"" + textRotation + "\"");
		styleWriter.write(">\n");
		styleWriter.write("</style:text-properties>\n");
		
//        styleWriter.write("<style:properties");
//        styleWriter.write(" style:rotation-align=\"" + rotationAlignment + "\"");
//        styleWriter.write(">\n");
//        styleWriter.write("</style:properties>\n");
//

		styleWriter.write("</style:style>\n");
	}

}

