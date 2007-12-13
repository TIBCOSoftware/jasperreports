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
package net.sf.jasperreports.engine.export.oasis;

import java.io.IOException;
import java.io.Writer;

import net.sf.jasperreports.engine.JRAlignment;
import net.sf.jasperreports.engine.JRPrintText;
import net.sf.jasperreports.engine.JRTextElement;


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
		
		verticalAlignment = VERTICAL_ALIGN_TOP;
		switch (text.getVerticalAlignment())
		{
			case JRAlignment.VERTICAL_ALIGN_BOTTOM :
			{
				verticalAlignment = VERTICAL_ALIGN_BOTTOM;
				break;
			}
			case JRAlignment.VERTICAL_ALIGN_MIDDLE :
			{
				verticalAlignment = VERTICAL_ALIGN_MIDDLE;
				break;
			}
			case JRAlignment.VERTICAL_ALIGN_TOP :
			default :
			{
				verticalAlignment = VERTICAL_ALIGN_TOP;
			}
		}

		horizontalAlignment = HORIZONTAL_ALIGN_LEFT;
		switch (text.getHorizontalAlignment())
		{
			case JRAlignment.HORIZONTAL_ALIGN_RIGHT :
			{
				horizontalAlignment = HORIZONTAL_ALIGN_RIGHT;
				break;
			}
			case JRAlignment.HORIZONTAL_ALIGN_CENTER :
			{
				horizontalAlignment = HORIZONTAL_ALIGN_CENTER;
				break;
			}
			case JRAlignment.HORIZONTAL_ALIGN_JUSTIFIED :
			{
				horizontalAlignment = HORIZONTAL_ALIGN_JUSTIFY;
				break;
			}
			case JRAlignment.HORIZONTAL_ALIGN_LEFT :
			default :
			{
				horizontalAlignment = HORIZONTAL_ALIGN_LEFT;
			}
		}

		runDirection = null;
		if (text.getRunDirection() == JRPrintText.RUN_DIRECTION_RTL)
		{
			runDirection = "rl";
		}
		
		switch(text.getRotation())
		{
			case JRTextElement.ROTATION_LEFT:
				textRotation = "90";
				break;
			case JRTextElement.ROTATION_RIGHT:
				textRotation = "270";
				break;
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
		styleWriter.write("> \r\n");
		styleWriter.write("</style:paragraph-properties>\n");
		styleWriter.write("<style:text-properties");
		styleWriter.write(" style:text-rotation-angle=\"" + textRotation + "\"");
		styleWriter.write("> \r\n");
		styleWriter.write("</style:text-properties>\n");
		styleWriter.write("</style:style>\n");
	}

}

