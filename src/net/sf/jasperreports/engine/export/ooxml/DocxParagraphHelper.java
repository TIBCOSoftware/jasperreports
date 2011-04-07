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

import java.io.Writer;

import net.sf.jasperreports.engine.JRPrintText;
import net.sf.jasperreports.engine.JRStyle;
import net.sf.jasperreports.engine.export.LengthUtil;
import net.sf.jasperreports.engine.type.HorizontalAlignEnum;
import net.sf.jasperreports.engine.type.LineSpacingEnum;


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
		exportPropsHeader(null);

		exportAlignment(
			getHorizontalAlignment(
				style.getOwnHorizontalAlignmentValue() 
				)
			);

		exportTabStop(
			style.getOwnTabStop(),
			getTabStopAlignment(
				style.getOwnHorizontalAlignmentValue() 
				)
			);

		exportLineSpacing(
			getLineSpacing(
				style.getOwnLineSpacingValue() 
				)
			);

		exportPropsFooter();
	}
	
	/**
	 *
	 */
	public void exportProps(JRPrintText text)
	{
		exportPropsHeader(text.getStyle() == null ? null : text.getStyle().getName());//FIXMEDOCX why getStyleNameReference is not working?

		exportAlignment(
			getHorizontalAlignment(
				text.getOwnHorizontalAlignmentValue()
				)
			);
		
		exportTabStop(
				text.getTabStop(),//FIXMETAB use defaulttabStop in settings.xml and do the same for ODT if possible 
				getTabStopAlignment(
					text.getHorizontalAlignmentValue()//FIXMETAB own
					)
				);

		exportLineSpacing(
			getLineSpacing(
				text.getOwnLineSpacingValue() 
				)
			);

//		exportRunDirection(text.getRunDirection() == JRPrintText.RUN_DIRECTION_RTL ? "rl" : null);

		exportPropsFooter();
	}
	
	
	/**
	 *
	 */
	private void exportPropsHeader(String styleNameReference)
	{
		write("      <w:pPr>\n");
		if (styleNameReference != null)
		{
			write("        <w:pStyle w:val=\"" + styleNameReference + "\"/>\n");
		}
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
	private void exportTabStop(Integer tabStop, String tabStopAlignment)
	{
		if (tabStop != null && tabStop > 0)
		{
			write("   <w:tabs>\n");
			for (int i = 0; i < 10; i++)
			{
				write("   <w:tab w:pos=\"" + LengthUtil.twip((i + 1) * tabStop) + "\" w:val=\"" + tabStopAlignment + "\"/>\n");
			}
			write("   </w:tabs>\n");
		}
	}
	
	/**
	 *
	 */
	private void exportLineSpacing(String lineSpacing)
	{
		if (lineSpacing != null)
		{
			write("   <w:spacing w:line=\"" + lineSpacing + "\" w:after=\"0\" w:before=\"0\"/>\n");
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
	public static String getTabStopAlignment(HorizontalAlignEnum horizontalAlignment)
	{
		if (horizontalAlignment != null)
		{
			switch (horizontalAlignment)
			{
				case RIGHT :
					return TAB_STOP_ALIGN_RIGHT;
				case CENTER :
					return TAB_STOP_ALIGN_CENTER;
				case JUSTIFIED :
				case LEFT :
				default :
					return TAB_STOP_ALIGN_LEFT;
			}
		}
		return null;
	}
	/**
	 *
	 */
	public static String getLineSpacing(LineSpacingEnum lineSpacing)
	{
		if (lineSpacing != null)
		{
			float lnsp = 0;
			switch (lineSpacing)
			{
				case DOUBLE :
					lnsp = 2;
					break;
				case ONE_AND_HALF :
					lnsp = 1.5f;
					break;
				case SINGLE :
				default :
					lnsp = 1;
			}
			
			return String.valueOf((int)(lnsp * LINE_SPACING_FACTOR)); 
		}
		return null;
	}
}
