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
package net.sf.jasperreports.engine.export;

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
 * @version $Id$
 */
public class PdfTextRenderer extends AbstractTextRenderer
{
	/**
	 * 
	 */
	private JRPdfExporter pdfExporter;
	private PdfContentByte pdfContentByte;
	private int horizontalAlignment;
	private float leftOffsetFactor;
	private float rightOffsetFactor;

	
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
		leftOffsetFactor = 0f;
		rightOffsetFactor = 0f;
		
		//FIXMETAB 0.2f was a fair approximation
		switch (text.getHorizontalAlignmentValue())
		{
			case JUSTIFIED :
			{
				horizontalAlignment = Element.ALIGN_JUSTIFIED_ALL;
				leftOffsetFactor = 0f;
				rightOffsetFactor = 0f;
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
				leftOffsetFactor = -0.2f;
				rightOffsetFactor = 0f;
				break;
			}
			case CENTER :
			{
				horizontalAlignment = Element.ALIGN_CENTER;
				leftOffsetFactor = -0.1f;
				rightOffsetFactor = 0.1f;
				break;
			}
			case LEFT :
			default :
			{
				if (text.getRunDirectionValue() == RunDirectionEnum.LTR)
				{
					horizontalAlignment = Element.ALIGN_LEFT;
				}
				else
				{
					horizontalAlignment = Element.ALIGN_RIGHT;
				}
				leftOffsetFactor = 0f;
				rightOffsetFactor = 0.2f;
				break;
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
		
		float advance = segment.layout.getAdvance();
		
		ColumnText colText = new ColumnText(pdfContentByte);
		colText.setSimpleColumn(
			pdfExporter.getPhrase(segment.as, segment.text, text),
			x + drawPosX + leftOffsetFactor * advance,// + leftPadding
			pdfExporter.exporterContext.getExportedReport().getPageHeight()
				- y
				- topPadding
				- verticalAlignOffset
				//- text.getLeadingOffset()
				+ lineHeight
				- drawPosY,
			x + drawPosX  + segment.layout.getAdvance() + rightOffsetFactor * advance,// + leftPadding
			pdfExporter.exporterContext.getExportedReport().getPageHeight()
				- y
				- topPadding
				- verticalAlignOffset
				//- text.getLeadingOffset()
				-400//+ lineHeight//FIXMETAB
				- drawPosY,
			0,//text.getLineSpacingFactor(),// * text.getFont().getSize(),
			horizontalAlignment
			);

		//colText.setLeading(0, text.getLineSpacingFactor());// * text.getFont().getSize());
		colText.setLeading(lineHeight);
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
	

}
