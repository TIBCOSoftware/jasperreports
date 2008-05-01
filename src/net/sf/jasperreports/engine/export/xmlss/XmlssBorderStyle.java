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
package net.sf.jasperreports.engine.export.xmlss;

import java.io.IOException;
import java.io.Writer;

import net.sf.jasperreports.engine.JRLineBox;
import net.sf.jasperreports.engine.JRPen;
import net.sf.jasperreports.engine.JRPrintElement;
import net.sf.jasperreports.engine.util.JRColorUtil;


/**
 * @author sanda zaharia (shertage@users.sourceforge.net)
 * @version $Id$
 */
public abstract class XmlssBorderStyle extends XmlssStyle
{
	/**
	 *
	 */
	private static final String[] border = new String[]{"Top", "Left", "Bottom", "Right"};
	protected static final int TOP_BORDER = 0;
	protected static final int LEFT_BORDER = 1;
	protected static final int BOTTOM_BORDER = 2;
	protected static final int RIGHT_BORDER = 3;
	
	protected JRPrintElement element = null;
	
	private String[] borderColor = new String[4];
	private String[] borderWidth = new String[4];
	private String[] borderStyle = new String[4];
	private String[] borderPadding = new String[4];

	/**
	 *
	 */
	public XmlssBorderStyle(Writer styleWriter, JRPrintElement element)
	{
		super(styleWriter);
		
		this.element = element;
	}
	
	/**
	 *
	 */
	public void setBox(JRLineBox box) throws IOException
	{
		appendBorder(box.getTopPen(), TOP_BORDER);
//		borderPadding[TOP_BORDER] = String.valueOf(box.getTopPadding());
		appendBorder(box.getLeftPen(), LEFT_BORDER);
//		borderPadding[LEFT_BORDER] = String.valueOf(box.getLeftPadding());
		appendBorder(box.getBottomPen(), BOTTOM_BORDER);
//		borderPadding[BOTTOM_BORDER] = String.valueOf(box.getBottomPadding());
		appendBorder(box.getRightPen(), RIGHT_BORDER);
//		borderPadding[RIGHT_BORDER] = String.valueOf(box.getRightPadding());
	}

	/**
	 *
	 */
	public void setPen(JRPen pen) throws IOException
	{
		if (
			borderWidth[TOP_BORDER] == null
			&& borderWidth[LEFT_BORDER] == null
			&& borderWidth[BOTTOM_BORDER] == null
			&& borderWidth[RIGHT_BORDER] == null
			)
		{
			appendBorder(pen, TOP_BORDER);
			appendBorder(pen, LEFT_BORDER);
			appendBorder(pen, BOTTOM_BORDER);
			appendBorder(pen, RIGHT_BORDER);
		}
	}

	/**
	 *
	 */
	public String getId()
	{
		return 
			borderWidth[TOP_BORDER] + "|" + borderColor[TOP_BORDER] + "|" + borderStyle[TOP_BORDER] + "|" + borderPadding[TOP_BORDER]
			+ "|" + borderWidth[LEFT_BORDER] + "|" + borderColor[LEFT_BORDER] + "|" + borderStyle[LEFT_BORDER] + "|" + borderPadding[LEFT_BORDER]
			+ "|" + borderWidth[BOTTOM_BORDER] + "|" + borderColor[BOTTOM_BORDER] + "|" + borderStyle[BOTTOM_BORDER] + "|" + borderPadding[BOTTOM_BORDER]
			+ "|" + borderWidth[RIGHT_BORDER] + "|" + borderColor[RIGHT_BORDER] + "|" + borderStyle[RIGHT_BORDER] + "|" + borderPadding[RIGHT_BORDER]; 
	}

	/**
	 *
	 */
	protected void writeBorder(int side) throws IOException
	{
		if (borderWidth[side] != null)
		{
			styleWriter.write("  <ss:Border ");
			styleWriter.write("ss:Position=\"" + border[side]+"\" ");
			styleWriter.write("ss:LineStyle=\"" + borderStyle[side] + "\" ");
			styleWriter.write("ss:Weight=\"" + borderWidth[side] + "\" ");
			styleWriter.write("ss:Color=\"#" + borderColor[side] + "\"/>");
		}

		//FIXME: to set the padding
		
	}

	/**
	 *
	 */
	private void appendBorder(JRPen pen, int side) throws IOException
	{
		float width = pen.getLineWidth().floatValue();
		
		String style = null;

		if (width > 0f)
		{
			switch (pen.getLineStyle().byteValue())//FIXMEBORDER is this working? deal with double border too.
			{
				case JRPen.LINE_STYLE_DOTTED :
				{
					style = "Dot";
					break;
				}
				case JRPen.LINE_STYLE_DASHED :
				{
					style = "Dash";
					break;
				}
				case JRPen.LINE_STYLE_SOLID :
				default :
				{
					style = "Continuous";
					break;
				}
			}
			
			if(0f < width && 0.6f > width)
			{
				borderWidth[side] = "1";	//Thin
			}
			else if(0.6f <= width && 1.5f > width)
			{
				borderWidth[side] = "2";	//Medium
			}
			else
			{
				borderWidth[side] = "3";	//Thick
			}
		}
		else
		{
			style = "None";
		}

		borderStyle[side] = style;
		borderColor[side] = JRColorUtil.getColorHexa(pen.getLineColor());
	}

}

