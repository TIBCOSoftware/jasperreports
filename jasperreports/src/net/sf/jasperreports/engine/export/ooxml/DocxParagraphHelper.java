/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2011 Jaspersoft Corporation. All rights reserved.
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

import java.io.Writer;

import net.sf.jasperreports.engine.JRParagraph;
import net.sf.jasperreports.engine.JRPrintText;
import net.sf.jasperreports.engine.JRStyle;
import net.sf.jasperreports.engine.TabStop;
import net.sf.jasperreports.engine.export.LengthUtil;
import net.sf.jasperreports.engine.type.HorizontalAlignEnum;
import net.sf.jasperreports.engine.type.TabStopAlignEnum;


/**
 * @author sanda zaharia (shertage@users.sourceforge.net)
 * @version $Id$
 */
public class DocxParagraphHelper extends BaseHelper
{
	/**
	 *
	 */
	private static final String HORIZONTAL_ALIGN_LEFT = "left";
	private static final String HORIZONTAL_ALIGN_RIGHT = "right";
	private static final String HORIZONTAL_ALIGN_CENTER = "center";
	private static final String HORIZONTAL_ALIGN_BOTH = "both";

	/**
	 *
	 */
	private static final String TAB_STOP_ALIGN_LEFT = "left";
	private static final String TAB_STOP_ALIGN_RIGHT = "right";
	private static final String TAB_STOP_ALIGN_CENTER = "center";
	
	protected static final int LINE_SPACING_FACTOR = 240; //(int)(240 * 2/3f);

	/**
	 *
	 */
	private boolean pageBreak;

	/**
	 *
	 */
	public DocxParagraphHelper(Writer writer, boolean pageBreak)
	{
		super(writer);
		
		this.pageBreak = pageBreak;
	}
	
	/**
	 *
	 */
	public void exportProps(JRStyle style)
	{
		exportPropsHeader(null, style.getParagraph());

		exportAlignment(
			getHorizontalAlignment(
				style.getOwnHorizontalAlignmentValue() 
				)
			);

		exportTabStops(
			style.getParagraph()
			);

		exportLineSpacing(
			style.getParagraph() 
			);

		exportPropsFooter();
	}
	
	/**
	 *
	 */
	public void exportProps(JRPrintText text)
	{
		exportPropsHeader(text.getStyle() == null ? null : text.getStyle().getName(), text.getParagraph());//FIXMEDOCX why getStyleNameReference is not working?

		exportAlignment(
			getHorizontalAlignment(
				text.getOwnHorizontalAlignmentValue()
				)
			);
		
		exportTabStops(
			text.getParagraph() 
			);

		exportLineSpacing(
			text.getParagraph() 
			);

//		exportRunDirection(text.getRunDirection() == JRPrintText.RUN_DIRECTION_RTL ? "rl" : null);

		exportPropsFooter();
	}
	
	
	/**
	 *
	 */
	private void exportPropsHeader(String styleNameReference, JRParagraph paragraph)
	{
		write("      <w:pPr>\n");
		if (styleNameReference != null)
		{
			write("        <w:pStyle w:val=\"" + styleNameReference + "\"/>\n");
		}
		write("      <w:ind");
		if (paragraph.getOwnFirstLineIndent() != null)
		{
			write(" w:firstLine=\"" + LengthUtil.twip(paragraph.getOwnFirstLineIndent().intValue()) + "\"");
		}
		if (paragraph.getOwnLeftIndent() != null)
		{
			write(" w:left=\"" + LengthUtil.twip(paragraph.getOwnLeftIndent().intValue()) + "\"");
		}
		if (paragraph.getOwnRightIndent() != null)
		{
			write(" w:right=\"" + LengthUtil.twip(paragraph.getOwnRightIndent().intValue()) + "\"");
		}
		write("/>\n");
		if (pageBreak)
		{
			write("        <w:pageBreakBefore/>\n");
			pageBreak = false;
		}
	}
	
	/**
	 *
	 */
	private void exportAlignment(String horizontalAlignment)
	{
		if (horizontalAlignment != null)
		{
			write("   <w:jc w:val=\"" + horizontalAlignment + "\" />\n");
		}
	}
	
	/**
	 *
	 */
	private void exportTabStops(JRParagraph paragraph)
	{
		TabStop[] tabStops = paragraph.getTabStops();
		if (tabStops != null && tabStops.length > 0)
		{
			write("   <w:tabs>\n");
			for (int i = 0; i < tabStops.length; i++)
			{
				TabStop tabStop = tabStops[i];
				write("   <w:tab w:pos=\"" + LengthUtil.twip(tabStop.getPosition()) + "\" w:val=\"" + getTabStopAlignment(tabStop.getAlignment()) + "\"/>\n");
			}
			write("   </w:tabs>\n");
		}
	}
	
	/**
	 *
	 */
	private void exportLineSpacing(JRParagraph paragraph)
	{
		if (
			paragraph.getOwnLineSpacing() != null
			|| paragraph.getOwnLineSpacingSize() != null
			|| paragraph.getOwnSpacingBefore() != null
			|| paragraph.getOwnSpacingAfter() != null
			)
		{
			String lineRule; 
			String lineSpacing; 

			switch (paragraph.getLineSpacing())
			{
				case AT_LEAST :
				{
					lineRule = "atLeast";
					lineSpacing = String.valueOf(LengthUtil.twip(paragraph.getLineSpacingSize().floatValue())); 
					break;
				}
				case FIXED :
				{
					lineRule = "exact";
					lineSpacing = String.valueOf(LengthUtil.twip(paragraph.getLineSpacingSize().floatValue())); 
					break;
				}
				case PROPORTIONAL :
				{
					lineRule = "auto";
					lineSpacing = String.valueOf((int)(paragraph.getLineSpacingSize().floatValue() * LINE_SPACING_FACTOR)); 
					break;
				}
				case DOUBLE :
				{
					lineRule = "auto";
					lineSpacing = String.valueOf((int)(2f * LINE_SPACING_FACTOR)); 
					break;
				}
				case ONE_AND_HALF :
				{
					lineRule = "auto";
					lineSpacing = String.valueOf((int)(1.5f * LINE_SPACING_FACTOR)); 
					break;
				}
				case SINGLE :
				default :
				{
					lineRule = "auto";
					lineSpacing = String.valueOf((int)(1f * LINE_SPACING_FACTOR));
				}
			}
			
			write("   <w:spacing w:lineRule=\"" + lineRule + "\" w:line=\"" + lineSpacing + "\"");
			write(" w:after=\"" + LengthUtil.twip(paragraph.getSpacingAfter().intValue()) + "\"");
			write(" w:before=\"" + LengthUtil.twip(paragraph.getSpacingBefore().intValue()) + "\"/>\n");
		}
	}
	
	/**
	 *
	 */
	private void exportPropsFooter()
	{
		write("      </w:pPr>\n");
	}
	
	/**
	 *
	 */
	public void exportEmptyParagraph()
	{
		write("     <w:p><w:pPr><w:pStyle w:val=\"EMPTY_CELL_STYLE\"/>\n");
		if (pageBreak)
		{
			write("        <w:pageBreakBefore/>\n");
			pageBreak = false;
		}
		write("     </w:pPr></w:p>\n");
	}

	/**
	 *
	 */
	public static String getHorizontalAlignment(HorizontalAlignEnum horizontalAlignment)
	{
		if (horizontalAlignment != null)
		{
			switch (horizontalAlignment)
			{
				case RIGHT :
					return HORIZONTAL_ALIGN_RIGHT;
				case CENTER :
					return HORIZONTAL_ALIGN_CENTER;
				case JUSTIFIED :
					return HORIZONTAL_ALIGN_BOTH;
				case LEFT :
				default :
					return HORIZONTAL_ALIGN_LEFT;
			}
		}
		return null;
	}


	/**
	 *
	 */
	public static String getTabStopAlignment(TabStopAlignEnum alignment)
	{
		if (alignment != null)
		{
			switch (alignment)
			{
				case RIGHT :
					return TAB_STOP_ALIGN_RIGHT;
				case CENTER :
					return TAB_STOP_ALIGN_CENTER;
				case LEFT :
				default :
					return TAB_STOP_ALIGN_LEFT;
			}
		}
		return null;
	}
}
