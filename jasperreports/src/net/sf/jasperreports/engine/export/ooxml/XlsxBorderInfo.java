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
package net.sf.jasperreports.engine.export.ooxml;

import net.sf.jasperreports.engine.JRLineBox;
import net.sf.jasperreports.engine.JRPen;
import net.sf.jasperreports.engine.util.JRColorUtil;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id: BorderHelper.java 3135 2009-10-22 14:20:23Z teodord $
 */
public class XlsxBorderInfo
{
	/**
	 *
	 */
	protected static final String[] BORDER = new String[]{"top", "left", "bottom", "right"};
	protected static final int TOP_BORDER = 0;
	protected static final int LEFT_BORDER = 1;
	protected static final int BOTTOM_BORDER = 2;
	protected static final int RIGHT_BORDER = 3;
	
	protected String[] borderColor = new String[4];
	protected String[] borderStyle = new String[4];
	protected String[] borderPadding = new String[4];

	/**
	 *
	 */
	public XlsxBorderInfo(JRLineBox box)
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
	public XlsxBorderInfo(JRPen pen)
	{
		setBorder(pen, TOP_BORDER);
		setBorder(pen, LEFT_BORDER);
		setBorder(pen, BOTTOM_BORDER);
		setBorder(pen, RIGHT_BORDER);
	}

	/**
	 *
	 */
	public String getId() 
	{
		return	
			borderStyle[TOP_BORDER] + "|" + borderColor[TOP_BORDER] 
			+ "|" + borderStyle[LEFT_BORDER] + "|" + borderColor[LEFT_BORDER]
			+ "|" + borderStyle[BOTTOM_BORDER] + "|" + borderColor[BOTTOM_BORDER]
			+ "|" + borderStyle[RIGHT_BORDER] + "|" + borderColor[RIGHT_BORDER];
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
			switch (pen.getLineStyleValue())
			{
				case DOUBLE :
				{
					style = "double";
					break;
				}
				case DOTTED :
				{
					style = "dotted";
					break;
				}
				case DASHED :
				{
					if (width >= 1f)
					{
						style = "mediumDashed";
					}
					else
					{
						style = "dashed";
					}
					break;
				}
				case SOLID :
				default :
				{
					if (width >= 2f)
					{
						style = "thick";
					}
					else if (width >= 1f)
					{
						style = "medium";
					}
					else if (width >= 0.5f)
					{
						style = "thin";
					}
					else
					{
						style = "hair";
					}
					break;
				}
			}
		}

		borderStyle[side] = style;
		borderColor[side] = JRColorUtil.getColorHexa(pen.getLineColor());
	}

}
