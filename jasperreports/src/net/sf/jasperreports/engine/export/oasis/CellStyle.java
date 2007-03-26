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
import net.sf.jasperreports.engine.JRElement;
import net.sf.jasperreports.engine.JRPrintElement;
import net.sf.jasperreports.engine.util.JRColorUtil;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class CellStyle extends BorderStyle
{
	/**
	 *
	 */
	private static final String VERTICAL_ALIGN_TOP = "top";
	private static final String VERTICAL_ALIGN_MIDDLE = "middle";
	private static final String VERTICAL_ALIGN_BOTTOM = "bottom";
	
	//private String fill = null;
	private String backcolor = null;
	
	private String verticalAlignment = null;

	
	/**
	 *
	 */
	public CellStyle(Writer styleWriter, JRPrintElement element)
	{
		super(styleWriter, element);
		
		if (element.getMode() == JRElement.MODE_OPAQUE)
		{
			//fill = "solid";
			backcolor = JRColorUtil.getColorHexa(element.getBackcolor());
		}
		else
		{
			//fill = "none";
		}

		if (element instanceof JRAlignment)
		{
			switch (((JRAlignment)element).getVerticalAlignment())
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
		}
	}
	
	/**
	 *
	 */
	public String getId()
	{
		return backcolor + super.getId() + (verticalAlignment != null ? "" : "|" + verticalAlignment); 
	}

	/**
	 *
	 */
	public void write(String cellStyleName) throws IOException
	{
		styleWriter.write("<style:style style:name=\"");
		styleWriter.write(cellStyleName);
		styleWriter.write("\"");
		styleWriter.write(" style:family=\"table-cell\">\n");
		styleWriter.write(" <style:table-cell-properties");		
		styleWriter.write(" fo:wrap-option=\"wrap\"");
		styleWriter.write(" style:shrink-to-fit=\"false\"");
		if (backcolor != null)
		{
			styleWriter.write(" fo:background-color=\"#");
			styleWriter.write(backcolor);
			styleWriter.write("\"");
		}
		
		writeBorder(TOP_BORDER);
		writeBorder(LEFT_BORDER);
		writeBorder(BOTTOM_BORDER);
		writeBorder(RIGHT_BORDER);
		
		if (verticalAlignment != null)
		{
			styleWriter.write(" style:vertical-align=\"");
			styleWriter.write(verticalAlignment);
			styleWriter.write("\"");
		}

		styleWriter.write("/>\n");
		styleWriter.write("</style:style>\n");
	}

}

