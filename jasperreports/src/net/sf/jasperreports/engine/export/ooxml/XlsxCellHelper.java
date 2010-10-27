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
import java.util.Locale;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.export.JRExporterGridCell;
import net.sf.jasperreports.engine.export.data.BooleanTextValue;
import net.sf.jasperreports.engine.export.data.DateTextValue;
import net.sf.jasperreports.engine.export.data.NumberTextValue;
import net.sf.jasperreports.engine.export.data.StringTextValue;
import net.sf.jasperreports.engine.export.data.TextValue;
import net.sf.jasperreports.engine.export.data.TextValueHandler;
import net.sf.jasperreports.engine.type.VerticalAlignEnum;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id: CellHelper.java 3033 2009-08-27 11:46:22Z teodord $
 */
public class XlsxCellHelper extends BaseHelper
{
	/**
	 *
	 */
	private static final String VERTICAL_ALIGN_TOP = "top";
	private static final String VERTICAL_ALIGN_MIDDLE = "center";
	private static final String VERTICAL_ALIGN_BOTTOM = "bottom";
	
	/**
	 *
	 */
	private XlsxStyleHelper styleHelper;
//	private XlsxBorderHelper borderHelper;
	
	/**
	 *
	 */
	public XlsxCellHelper(
		Writer writer,
		XlsxStyleHelper styleHelper
		)
	{
		super(writer);
		
		this.styleHelper = styleHelper;
//		borderHelper = new XlsxBorderHelper(writer);
	}

	/**
	 * 
	 *
	public XlsxBorderHelper getBorderHelper() 
	{
		return borderHelper;
	}

	/**
	 *
	 */
	public void exportHeader(
		JRExporterGridCell gridCell,
		int rowIndex,
		int colIndex 
		) 
	{
		exportHeader(gridCell, rowIndex, colIndex, null, null, null, true, false, false);
	}

	/**
	 *
	 */
	public void exportHeader(
		JRExporterGridCell gridCell,
		int rowIndex,
		int colIndex, 
		TextValue textValue,
		String pattern,
		Locale locale,
		boolean isWrapText,
		boolean isHidden,
		boolean isLocked
		) 
	{
		TypeTextValueHandler handler = TypeTextValueHandler.getInstance();
		
		try
		{
			if (textValue != null)
			{
				textValue.handle(handler);
			}
			else
			{
				handler.handle((StringTextValue)null);
			}
		}
		catch (JRException e)
		{
			throw new JRRuntimeException(e);
		}

		String type = handler.getType();
		
		write("  <c r=\"" + getColumIndexLetter(colIndex) + (rowIndex + 1) + "\" s=\"" + styleHelper.getCellStyle(gridCell, pattern, locale, isWrapText, isHidden, isLocked) + "\"");
		if (type != null)
		{
			write(" t=\"" + type + "\"");
		}
		write(">");
		
//		exportPropsHeader();
//
//		if (gridCell.getColSpan() > 1)
//		{
//			write("      <w:gridSpan w:val=\"" + gridCell.getColSpan() +"\" />\n");
//		}
//		if (gridCell.getRowSpan() > 1)
//		{
//			write("      <w:vMerge w:val=\"restart\" />\n");
//		}
//		
//		exportProps(element, gridCell);
//		
//		exportPropsFooter();
	}

	/**
	 *
	 */
	public void exportFooter() 
	{
		write("</c>");
	}


	/**
	 *
	 *
	public void exportProps(JRPrintElement element, JRExporterGridCell gridCell)
	{
		exportBackcolor(element.getModeValue(), element.getBackcolor());
		
		borderHelper.export(gridCell.getBox());

//		if (element instanceof JRCommonGraphicElement)
//			borderHelper.export(((JRCommonGraphicElement)element).getLinePen());
		
		JRAlignment align = element instanceof JRAlignment ? (JRAlignment)element : null;
		if (align != null)
		{
			JRPrintText text = element instanceof JRPrintText ? (JRPrintText)element : null;
			Byte ownRotation = text == null ? null : text.getOwnRotation();
			
			String verticalAlignment = 
				getVerticalAlignment(
					align.getOwnVerticalAlignment() 
					);
			String textRotation = getTextDirection(ownRotation);

			exportAlignmentAndRotation(verticalAlignment, textRotation);
		}
	}


	/**
	 *
	 *
	public void exportProps(JRExporterGridCell gridCell)
	{
		exportBackcolor(ModeEnum.OPAQUE, gridCell.getBackcolor());//FIXMEDOCX check this
		
		borderHelper.export(gridCell.getBox());
	}

	
//	/**
//	 *
//	 */
//	private void exportBackcolor(ModeEnum mode, Color backcolor)
//	{
//		if (mode == ModeEnum.OPAQUE && backcolor != null)
//		{
//			write("      <w:shd w:val=\"clear\" w:color=\"auto\"	w:fill=\"" + JRColorUtil.getColorHexa(backcolor) + "\" />\n");
//		}
//	}

//	/**
//	 *
//	 */
//	private void exportPropsHeader()
//	{
//		write("      <w:tcPr>\n");
//	}
	
//	/**
//	 *
//	 */
//	private void exportAlignmentAndRotation(String verticalAlignment, String textRotation)
//	{
//		if (verticalAlignment != null)
//		{
//			write("      <w:vAlign w:val=\"" + verticalAlignment +"\" />\n");
//		}
//		if (textRotation != null)
//		{
//			write("   <w:textDirection w:val=\"" + textRotation + "\" />\n");
//		}
//	}
	
//	/**
//	 *
//	 */
//	private void exportPropsFooter()
//	{
//		write("      </w:tcPr>\n");
//	}
	
//	/**
//	 *
//	 */
//	private static String getTextDirection(Byte rotation)
//	{
//		String textDirection = null;
//		
//		if (rotation != null)
//		{
//			switch(rotation.byteValue())
//			{
//				case JRTextElement.ROTATION_LEFT:
//				{
//					textDirection = "btLr";
//					break;
//				}
//				case JRTextElement.ROTATION_RIGHT:
//				{
//					textDirection = "tbRl";
//					break;
//				}
//				case JRTextElement.ROTATION_UPSIDE_DOWN://FIXMEDOCX possible?
//				case JRTextElement.ROTATION_NONE:
//				default:
//				{
//				}
//			}
//		}
//
//		return textDirection;
//	}

	/**
	 *
	 */
	public static String getVerticalAlignment(VerticalAlignEnum verticalAlignment)
	{
		if (verticalAlignment != null)
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
		return null;
	}
	
	
	/**
	 *
	 */
	public static String getColumIndexLetter(int colIndex)
	{
		int intFirstLetter = ((colIndex) / 676) + 64;
		int intSecondLetter = ((colIndex % 676) / 26) + 64;
		int intThirdLetter = (colIndex % 26) + 65;
		
		char firstLetter = (intFirstLetter > 64) ? (char)intFirstLetter : ' ';
		char secondLetter = (intSecondLetter > 64) ? (char)intSecondLetter : ' ';
		char thirdLetter = (char)intThirdLetter;
		
		return ("" + firstLetter + secondLetter + thirdLetter).trim();
	}


}

class TypeTextValueHandler implements TextValueHandler 
{
	private static final TypeTextValueHandler INSTANCE = new TypeTextValueHandler();

	private String type;
	
	private TypeTextValueHandler(){
	}
	
	public static TypeTextValueHandler getInstance(){
		return INSTANCE;
	}
	
	public void handle(BooleanTextValue textValue) throws JRException {
		type = "b";
	}
	
	public void handle(DateTextValue textValue) throws JRException {
		type = "d";
	}
	
	public void handle(NumberTextValue textValue) throws JRException {
		type = "n";
	}
	
	public void handle(StringTextValue textValue) throws JRException {
		type = "inlineStr";
	}
	
	public String getType()
	{
		return type;
	}
}
