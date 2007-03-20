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

import java.awt.Color;
import java.io.IOException;
import java.io.Writer;

import net.sf.jasperreports.engine.JRBox;
import net.sf.jasperreports.engine.JRElement;
import net.sf.jasperreports.engine.JRGraphicElement;
import net.sf.jasperreports.engine.JRPrintElement;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id: JRHtmlExporter.java 1600 2007-02-23 15:12:16Z shertage $
 */
public class FrameStyle extends Style
{
	/**
	 *
	 */
	private static final String[] border = new String[]{"top", "left", "bottom", "right"};
	private static final int TOP_BORDER = 0;
	private static final int LEFT_BORDER = 1;
	private static final int BOTTOM_BORDER = 2;
	private static final int RIGHT_BORDER = 3;
	protected static final int colorMask = Integer.parseInt("FFFFFF", 16);
	
	private JRPrintElement element = null;
	
	private String fill = null;
	private String backcolor = null;
	
	private String[] borderColor = new String[4];
	private String[] borderWidth = new String[4];
	private String[] borderStyle = new String[4];
	private String[] borderPadding = new String[4];

	/**
	 *
	 */
	public FrameStyle(Writer styleWriter, JRPrintElement element)
	{
		super(styleWriter);
		
		this.element = element;
		
		if (element.getMode() == JRElement.MODE_OPAQUE)
		{
			fill = "solid";
			backcolor = Integer.toHexString(element.getBackcolor().getRGB() & colorMask).toUpperCase();
			backcolor = ("000000" + backcolor).substring(backcolor.length());
		}
		else
		{
			fill = "none";
		}
	}
	
	/**
	 *
	 */
	public void setBox(JRBox box) throws IOException
	{
		if (box != null)
		{
			appendBorder(
				box.getTopBorder(),
				box.getTopBorderColor() == null ? element.getForecolor() : box.getTopBorderColor(),
				box.getTopPadding(),
				TOP_BORDER
				);
			appendBorder(
				box.getLeftBorder(),
				box.getLeftBorderColor() == null ? element.getForecolor() : box.getLeftBorderColor(),
				box.getLeftPadding(),
				LEFT_BORDER
				);
			appendBorder(
				box.getBottomBorder(),
				box.getBottomBorderColor() == null ? element.getForecolor() : box.getBottomBorderColor(),
				box.getBottomPadding(),
				BOTTOM_BORDER
				);
			appendBorder(
				box.getRightBorder(),
				box.getRightBorderColor() == null ? element.getForecolor() : box.getRightBorderColor(),
				box.getRightPadding(),
				RIGHT_BORDER
				);
		}
	}

	/**
	 *
	 */
	public String getId()
	{
		return fill + "|" + backcolor
			+ "|" + borderWidth[TOP_BORDER] + "|" + borderColor[TOP_BORDER] + "|" + borderStyle[TOP_BORDER] + "|" + borderPadding[TOP_BORDER]
			+ "|" + borderWidth[LEFT_BORDER] + "|" + borderColor[LEFT_BORDER] + "|" + borderStyle[LEFT_BORDER] + "|" + borderPadding[LEFT_BORDER]
			+ "|" + borderWidth[BOTTOM_BORDER] + "|" + borderColor[BOTTOM_BORDER] + "|" + borderStyle[BOTTOM_BORDER] + "|" + borderPadding[BOTTOM_BORDER]
			+ "|" + borderWidth[RIGHT_BORDER] + "|" + borderColor[RIGHT_BORDER] + "|" + borderStyle[RIGHT_BORDER] + "|" + borderPadding[RIGHT_BORDER]; 
	}

	/**
	 *
	 */
	public void write(String frameStyleName) throws IOException
	{
		styleWriter.write("<style:style style:name=\"" + frameStyleName + "\" style:family=\"graphic\" " +
				//" style:parent-style-name=\"Frame\"" +
				">\n");
		styleWriter.write("  <style:graphic-properties " +
			//"style:run-through=\"foreground\" " +
			//"style:wrap=\"run-through\" " +
			//"style:number-wrapped-paragraphs=\"no-limit\" " +
			//"style:wrap-contour=\"false\" " + 
			"style:vertical-pos=\"from-top\" " +
			"style:vertical-rel=\"page\" " +
			"style:horizontal-pos=\"from-left\" " +
			"style:horizontal-rel=\"page\"");
		styleWriter.write(" draw:fill=\"" + fill + "\" draw:fill-color=\"#" + backcolor + "\"");

		writeBorder(TOP_BORDER);
		writeBorder(LEFT_BORDER);
		writeBorder(BOTTOM_BORDER);
		writeBorder(RIGHT_BORDER);
		
		styleWriter.write("/>\n");
		styleWriter.write("</style:style>\n");
	}

	/**
	 *
	 */
	public void writeBorder(int side) throws IOException
	{
		if (borderWidth[side] != null)
		{
			styleWriter.write(" fo:border-");
			styleWriter.write(border[side]);
			styleWriter.write("=\"");
			styleWriter.write(borderWidth[side]);
			styleWriter.write("in ");
			styleWriter.write(borderStyle[side]); 
			styleWriter.write(" #");
			styleWriter.write(borderColor[side]);
			styleWriter.write("\"");
		}

		if (borderPadding[side] != null)
		{
			styleWriter.write(" fo:padding-");
			styleWriter.write(border[side]);
			styleWriter.write("=\"");
			styleWriter.write(borderPadding[side]);
			styleWriter.write("\"");
		}
	}

	/**
	 *
	 */
	private void appendBorder(byte pen, Color color, int padding, int side) throws IOException
	{
		String style = null;
		int width = 0;

		switch (pen)
		{
			case JRGraphicElement.PEN_DOTTED :
			{
				style = "dashed";
				width = 1;
				break;
			}
			case JRGraphicElement.PEN_4_POINT :
			{
				style = "solid";
				width = 4;
				break;
			}
			case JRGraphicElement.PEN_2_POINT :
			{
				style = "solid";
				width = 2;
				break;
			}
			case JRGraphicElement.PEN_THIN :
			{
				style = "solid";
				width = 1;//FIXMEODT can do better
				break;
			}
			case JRGraphicElement.PEN_NONE :
			{
				style = "none";
				break;
			}
			case JRGraphicElement.PEN_1_POINT :
			default :
			{
				style = "solid";
				width = 1;
				break;
			}
		}

		borderStyle[side] = style;
		borderWidth[side] = String.valueOf(Utility.translatePixelsToInchesWithNoRoundOff(width));

		String hexa = Integer.toHexString(color.getRGB() & colorMask).toUpperCase();
		hexa = ("000000" + hexa).substring(hexa.length());
		borderColor[side] = hexa;

		borderPadding[side] = String.valueOf(Utility.translatePixelsToInchesWithNoRoundOff(padding));
	}

}

