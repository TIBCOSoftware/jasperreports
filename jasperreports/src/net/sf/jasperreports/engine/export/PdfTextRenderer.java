/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2014 TIBCO Software Inc. All rights reserved.
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

import net.sf.jasperreports.engine.DefaultJasperReportsContext;
import net.sf.jasperreports.engine.JRPropertiesUtil;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.type.RunDirectionEnum;
import net.sf.jasperreports.engine.util.JRStyledText;

import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.ColumnText;
import com.lowagie.text.pdf.PdfWriter;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public class PdfTextRenderer extends AbstractPdfTextRenderer
{
	/**
	 * @deprecated Replaced by {@link #PdfTextRenderer(JasperReportsContext, boolean)}.
	 */
	public static PdfTextRenderer getInstance()
	{
		return 
			new PdfTextRenderer(
				DefaultJasperReportsContext.getInstance(),
				JRPropertiesUtil.getInstance(DefaultJasperReportsContext.getInstance()).getBooleanProperty(JRStyledText.PROPERTY_AWT_IGNORE_MISSING_FONT)
				);
	}
	
	
	/**
	 * @deprecated Replaced by {@link #PdfTextRenderer(JasperReportsContext, boolean)}. 
	 */
	public PdfTextRenderer(boolean ignoreMissingFont)
	{
		this(DefaultJasperReportsContext.getInstance(), ignoreMissingFont);
	}
	
	
	/**
	 * 
	 */
	public PdfTextRenderer(JasperReportsContext jasperReportsContext, boolean ignoreMissingFont)
	{
		super(jasperReportsContext, ignoreMissingFont);
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
			pdfExporter.getCurrentPageFormat().getPageHeight()
				- y
				- topPadding
				- verticalAlignOffset
				//- text.getLeadingOffset()
				+ lineHeight
				- drawPosY,
			x + drawPosX  + segment.layout.getAdvance() + rightOffsetFactor * advance,// + leftPadding
			pdfExporter.getCurrentPageFormat().getPageHeight()
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
	}
	

}
