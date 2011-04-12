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
package net.sf.jasperreports.engine.export;

import java.awt.font.FontRenderContext;
import java.text.AttributedString;
import java.util.Locale;

import net.sf.jasperreports.engine.JRPrintText;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.type.RunDirectionEnum;
import net.sf.jasperreports.engine.util.JRProperties;
import net.sf.jasperreports.engine.util.JRStyledText;

import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.pdf.ColumnText;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfWriter;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id: TextRenderer.java 4280 2011-04-11 10:36:24Z teodord $
 */
public class PdfTextRenderer extends AbstractTextRenderer
{
	/**
	 * 
	 */
	private JRPdfExporter pdfExporter;
	private PdfContentByte pdfContentByte;
	private int horizontalAlignment;

	
	/**
	 * 
	 */
	public static PdfTextRenderer getInstance()
	{
		return 
			new PdfTextRenderer(
				JRProperties.getBooleanProperty(JRStyledText.PROPERTY_AWT_IGNORE_MISSING_FONT)
				);
	}
	
	
	/**
	 * 
	 */
	public PdfTextRenderer(boolean ignoreMissingFont)
	{
		super(false, ignoreMissingFont);
	}
	
	
	/**
	 * 
	 */
	public void initialize(
		JRPdfExporter pdfExporter, 
		PdfContentByte pdfContentByte,
		JRPrintText text,
		int offsetX,
		int offsetY
		)
	{
		this.pdfExporter = pdfExporter;
		this.pdfContentByte = pdfContentByte;
		
		horizontalAlignment = Element.ALIGN_LEFT;
		switch (text.getHorizontalAlignmentValue())
		{
			case LEFT :
			{
				if (text.getRunDirectionValue() == RunDirectionEnum.LTR)
				{
					horizontalAlignment = Element.ALIGN_LEFT;
				}
				else
				{
					horizontalAlignment = Element.ALIGN_RIGHT;
				}
				break;
			}
			case CENTER :
			{
				horizontalAlignment = Element.ALIGN_CENTER;
				break;
			}
			case RIGHT :
			{
				if (text.getRunDirectionValue() == RunDirectionEnum.LTR)
				{
					horizontalAlignment = Element.ALIGN_RIGHT;
				}
				else
				{
					horizontalAlignment = Element.ALIGN_LEFT;
				}
				break;
			}
			case JUSTIFIED :
			{
				horizontalAlignment = Element.ALIGN_JUSTIFIED_ALL;
				break;
			}
			default :
			{
				horizontalAlignment = Element.ALIGN_LEFT;
			}
		}

		super.initialize(text, offsetX, offsetY);
	}
	
	
	/**
	 * 
	 */
	public void draw()
	{
		TabSegment segment = segments.get(segmentIndex);
		
		ColumnText colText = new ColumnText(pdfContentByte);
		colText.setSimpleColumn(
			pdfExporter.getPhrase(segment.as, segment.text, Locale.ENGLISH, text),//FIXMETAB  hardcoded locale
			x + leftPadding + drawPosX,
			pdfExporter.exporterContext.getExportedReport().getPageHeight()
				- y
				- topPadding
				- verticalOffset
				- text.getLeadingOffset()
				+ lineHeight//FIXMETAB check this; should not be in leading offset?
				- drawPosY,
			x + segment.layout.getAdvance() + drawPosX,
			pdfExporter.exporterContext.getExportedReport().getPageHeight()
				- y
				- topPadding
				- verticalOffset
				- text.getLeadingOffset()
				+ lineHeight
				- drawPosY
				- 400,//FIXMETAB hardcoded value
			0,//text.getLineSpacingFactor(),// * text.getFont().getSize(),
			horizontalAlignment
			);

		colText.setLeading(0, text.getLineSpacingFactor());// * text.getFont().getSize());
		colText.setRunDirection(
			text.getRunDirectionValue() == RunDirectionEnum.LTR
			? PdfWriter.RUN_DIRECTION_LTR : PdfWriter.RUN_DIRECTION_RTL
			);

		try
		{
			colText.go();
		}
		catch (DocumentException e)
		{
			throw new JRRuntimeException(e);
		}

//		ColumnText colText = new ColumnText(pdfContentByte);
//		colText.setSimpleColumn(
//			getPhrase(styledText, text),
//			x + leftPadding,
//			jasperPrint.getPageHeight()
//				- y
//				- topPadding
//				- verticalOffset
//				- text.getLeadingOffset(),
//				//+ text.getLineSpacingFactor() * text.getFont().getSize(),
//			x + width - rightPadding,
//			jasperPrint.getPageHeight()
//				- y
//				- height
//				+ bottomPadding,
//			0,//text.getLineSpacingFactor(),// * text.getFont().getSize(),
//			horizontalAlignment
//			);
//
//		colText.setLeading(0, text.getLineSpacingFactor());// * text.getFont().getSize());
//		colText.setRunDirection(
//			text.getRunDirectionValue() == RunDirectionEnum.LTR
//			? PdfWriter.RUN_DIRECTION_LTR : PdfWriter.RUN_DIRECTION_RTL
//			);
	}
	

	/**
	 * 
	 */
	public FontRenderContext getFontRenderContext()
	{
		return LINE_BREAK_FONT_RENDER_CONTEXT;
	}
	
}
