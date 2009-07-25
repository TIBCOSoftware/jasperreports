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

import java.awt.Color;
import java.io.IOException;
import java.io.Writer;

import net.sf.jasperreports.engine.JRLineBox;
import net.sf.jasperreports.engine.JRPen;
import net.sf.jasperreports.engine.util.JRColorUtil;


/**
 * @author sanda zaharia (shertage@users.sourceforge.net)
 * @version $Id$
 */
public class BorderHelper extends BaseHelper
{

	/**
	 *
	 */
	public BorderHelper(Writer writer)
	{
		super(writer);
	}
	
	/**
	 *
	 */
	public void export(JRLineBox box) throws IOException
	{
		if (box != null)
		{
			export(new BorderInfo(box));
		}
	}

	/**
	 *
	 */
	public void export(JRPen pen) throws IOException
	{
		if (pen != null)
		{
			export(new BorderInfo(pen));
		}
	}

	/**
	 *
	 */
	private void export(BorderInfo info) throws IOException
	{
		if(info.hasBorder())
		{
			writer.write("      <w:tcBorders> \r\n");
			exportBorder(info, BorderInfo.TOP_BORDER);
			exportBorder(info, BorderInfo.LEFT_BORDER);
			exportBorder(info, BorderInfo.BOTTOM_BORDER);
			exportBorder(info, BorderInfo.RIGHT_BORDER);
			writer.write("      </w:tcBorders> \r\n");
		}
		
		writer.write("      <w:tcMar> \r\n");
		exportPadding(info, BorderInfo.TOP_BORDER);
		exportPadding(info, BorderInfo.LEFT_BORDER);
		exportPadding(info, BorderInfo.BOTTOM_BORDER);
		exportPadding(info, BorderInfo.RIGHT_BORDER);
		writer.write("      </w:tcMar> \r\n");
	}

	/**
	 *
	 */
	private void exportBorder(BorderInfo info, int side) throws IOException
	{
		if (info.borderWidth[side] != null)
		{
			writer.write("<w:" + BorderInfo.BORDER[side] +" w:val=\"" + info.borderStyle[side] + "\" w:sz=\"" + info.borderWidth[side] + "\" w:space=\"0\"");
			if (info.borderColor[side] != null)//FIXMEDOCX check this; use default color?
			{
				writer.write(" w:color=\"" + JRColorUtil.getColorHexa(info.borderColor[side]) + "\"");
			}
			writer.write(" /> \r\n");
		}
	}
	
	/**
	 *
	 */
	private void exportPadding(BorderInfo info, int side) throws IOException
	{
		if (info.borderPadding[side] != null)
		{
			writer.write("       <w:" + BorderInfo.BORDER[side] +" w:w=\"" + info.borderPadding[side] + "\" w:type=\"dxa\" /> \r\n");
		}
	}

}

class BorderInfo
{
	/**
	 *
	 */
	protected static final String[] BORDER = new String[]{"top", "left", "bottom", "right"};
	protected static final int TOP_BORDER = 0;
	protected static final int LEFT_BORDER = 1;
	protected static final int BOTTOM_BORDER = 2;
	protected static final int RIGHT_BORDER = 3;
	
	protected Color[] borderColor = new Color[4];
	protected String[] borderWidth = new String[4];
	protected String[] borderStyle = new String[4];
	protected String[] borderPadding = new String[4];

	/**
	 *
	 */
	public BorderInfo(JRLineBox box)
	{
		setBorder(box.getTopPen(), TOP_BORDER);
		borderPadding[TOP_BORDER] = String.valueOf(Utility.twip(box.getTopPadding().intValue()));
		setBorder(box.getLeftPen(), LEFT_BORDER);
		borderPadding[LEFT_BORDER] = String.valueOf(Utility.twip(box.getLeftPadding().intValue()));
		setBorder(box.getBottomPen(), BOTTOM_BORDER);
		borderPadding[BOTTOM_BORDER] = String.valueOf(Utility.twip(box.getBottomPadding().intValue()));
		setBorder(box.getRightPen(), RIGHT_BORDER);
		borderPadding[RIGHT_BORDER] = String.valueOf(Utility.twip(box.getRightPadding().intValue()));
	}
	
	/**
	 *
	 */
	public BorderInfo(JRPen pen)
	{
		if (
			borderWidth[TOP_BORDER] == null
			&& borderWidth[LEFT_BORDER] == null
			&& borderWidth[BOTTOM_BORDER] == null
			&& borderWidth[RIGHT_BORDER] == null
			)
		{
			setBorder(pen, TOP_BORDER);
			setBorder(pen, LEFT_BORDER);
			setBorder(pen, BOTTOM_BORDER);
			setBorder(pen, RIGHT_BORDER);
		}
	}

	/**
	 *
	 */
	protected boolean hasBorder() 
	{
		return	
			borderWidth[TOP_BORDER] != null
			|| borderWidth[LEFT_BORDER] != null
			|| borderWidth[BOTTOM_BORDER] != null
			|| borderWidth[RIGHT_BORDER] != null;
	}

	/**
	 *
	 */
	private void setBorder(JRPen pen, int side)
	{
		float width = pen.getLineWidth() == null ? 0 : pen.getLineWidth().floatValue();
		String style = null;

		if (width > 0f)
		{
			switch (pen.getLineStyle().byteValue())//FIXMEBORDER is this working? deal with double border too.
			{
				case JRPen.LINE_STYLE_DOTTED :
				{
					style = "dotted";
					break;
				}
				case JRPen.LINE_STYLE_DASHED :
				{
					style = "dashSmallGap";
					break;
				}
				case JRPen.LINE_STYLE_SOLID :
				default :
				{
					style = "single";
					break;
				}
			}

			borderWidth[side] = String.valueOf(Utility.halfPoint(width));
		}
		else
		{
			style = "none";
		}

		borderStyle[side] = style;
		borderColor[side] = pen.getLineColor();
	}

}
