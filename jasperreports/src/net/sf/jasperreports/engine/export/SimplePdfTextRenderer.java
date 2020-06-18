/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2019 TIBCO Software Inc. All rights reserved.
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

import java.text.AttributedCharacterIterator;
import java.text.AttributedString;

import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.pdf.ColumnText;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfWriter;

import net.sf.jasperreports.engine.JRPrintText;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.type.RunDirectionEnum;
import net.sf.jasperreports.engine.util.JRStyledText;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public class SimplePdfTextRenderer extends AbstractPdfTextRenderer
{
	private float yLine = 0;
	
	/**
	 * @deprecated Replaced by {@link #SimplePdfTextRenderer(JasperReportsContext, boolean, boolean, boolean)}.
	 */
	public SimplePdfTextRenderer(
		JasperReportsContext jasperReportsContext, 
		boolean ignoreMissingFont
		)
	{
		this(jasperReportsContext, ignoreMissingFont, true, false);
	}
	
	
	/**
	 * 
	 */
	public SimplePdfTextRenderer(
		JasperReportsContext jasperReportsContext, 
		boolean ignoreMissingFont,
		boolean defaultIndentFirstLine,
		boolean defaultJustifyLastLine
		)
	{
		super(
			jasperReportsContext, 
			ignoreMissingFont,
			defaultIndentFirstLine,
			defaultJustifyLastLine
			);
	}

	
	@Override
	public void initialize(
		JRPdfExporter pdfExporter, 
		PdfContentByte pdfContentByte,
		JRPrintText text, 
		JRStyledText styledText, 
		int offsetX,
		int offsetY
		)
	{
		super.initialize(
			pdfExporter, 
			pdfContentByte,
			text, 
			styledText, 
			offsetX,
			offsetY
			);
		
		yLine = 
			pdfExporter.getCurrentPageFormat().getPageHeight()
				- y
				- topPadding
				- verticalAlignOffset
				- text.getLeadingOffset();
	}

	
	@Override
	public void render()
	{
		super.render();
	}

	
	@Override
	protected void renderParagraph(
		AttributedCharacterIterator allParagraphs,
		int paragraphStart,
		String paragraphText
		)
	{
		AttributedString paragraph = null;
		
		if (paragraphText == null)
		{
			paragraphText = " ";
			paragraph = 
				new AttributedString(
					paragraphText,
					new AttributedString(
						allParagraphs, 
						paragraphStart, 
						paragraphStart + paragraphText.length()
						).getIterator().getAttributes()
					);
		}
		else
		{
			paragraph = 
				new AttributedString(
					allParagraphs, 
					paragraphStart, 
					paragraphStart + paragraphText.length()
					);
		}
		
		ColumnText colText = new ColumnText(pdfContentByte);
		colText.setSimpleColumn(
			pdfExporter.getPhrase(paragraph, paragraphText, text),
			x + leftPadding,
			yLine,
			x + width - rightPadding,
			pdfExporter.getCurrentPageFormat().getPageHeight()
				- y
				- height
				+ bottomPadding,
			0,//text.getLineSpacingFactor(),// * text.getFont().getSize(),
			horizontalAlignment == Element.ALIGN_JUSTIFIED && (isLastParagraph && justifyLastLine) 
				? Element.ALIGN_JUSTIFIED_ALL : horizontalAlignment
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
		
		yLine = colText.getYLine();
	}


	@Override
	protected AttributedString getAttributedString()
	{
		return styledText.getAttributedString();
	}


	@Override
	public void draw()
	{
		//nothing to do
	}
}
