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

import java.awt.font.GlyphVector;
import java.util.Locale;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfDictionary;
import com.lowagie.text.pdf.PdfName;
import com.lowagie.text.pdf.PdfObject;
import com.lowagie.text.pdf.PdfString;

import net.sf.jasperreports.engine.JRPrintText;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.util.JRStyledText;
import net.sf.jasperreports.export.pdf.PdfProducer;
import net.sf.jasperreports.export.pdf.classic.ClassicPdfProducer;


/**
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 */
public class PdfGlyphRenderer extends AbstractPdfTextRenderer
{
	private static final Log log = LogFactory.getLog(PdfGlyphRenderer.class);
	
	private static final boolean PATCHED_ITEXT;
	static
	{
		PATCHED_ITEXT = determinePatchedItext();
	}

	private static boolean determinePatchedItext()
	{
		try
		{
			PdfContentByte.class.getMethod("showText", GlyphVector.class);
			return true;
		} 
		catch (NoSuchMethodException e) 
		{
			log.warn("Unpatched iText found, cannot use glyph rendering");
			return false;
		} 
		catch (SecurityException e) 
		{
			throw new JRRuntimeException(e);
		}
	}
	
	public static boolean supported()
	{
		return PATCHED_ITEXT;
	}

	private ClassicPdfProducer itextPdfProducer;
	private PdfContentByte pdfContentByte;
	
	private boolean addActualText;	
	private PdfGlyphGraphics2D pdfGraphics2D;
	
	/**
	 * @deprecated Replaced by {@link #PdfGlyphRenderer(JasperReportsContext, boolean, boolean, boolean, boolean)}.
	 */
	public PdfGlyphRenderer(
		JasperReportsContext jasperReportsContext, 
		boolean ignoreMissingFont, 
		boolean addActualText
		)
	{
		this(
			jasperReportsContext, 
			ignoreMissingFont, 
			true,
			false,
			addActualText
			);
	}

	public PdfGlyphRenderer(
		JasperReportsContext jasperReportsContext, 
		boolean ignoreMissingFont, 
		boolean defaultIndentFirstLine,
		boolean defaultJustifyLastLine,
		boolean addActualText
		)
	{
		super(
			jasperReportsContext, 
			ignoreMissingFont,
			defaultIndentFirstLine,
			defaultJustifyLastLine
			);
		this.addActualText = addActualText;
	}

	@Override
	public void initialize(JRPdfExporter pdfExporter, PdfProducer pdfProducer, JRPrintText text,
			JRStyledText styledText, int offsetX, int offsetY)
	{
		if (!(pdfProducer instanceof ClassicPdfProducer))
		{
			throw new IllegalArgumentException("Only ClassicPdfProducer is supported");
		}
		super.initialize(pdfExporter, pdfProducer, text, styledText, offsetX, offsetY);
		
		itextPdfProducer = (ClassicPdfProducer) pdfProducer;
		pdfContentByte = itextPdfProducer.getPdfContentByte();
	}

	@Override
	public void render()
	{
		Locale locale = pdfExporter.getTextLocale(text);
		pdfGraphics2D = new PdfGlyphGraphics2D(pdfContentByte, pdfExporter, itextPdfProducer, locale);
		super.render();
		pdfGraphics2D.dispose();
	}
	
	
	@Override
	public void draw()
	{
		boolean addText = addActualText && !itextPdfProducer.getContext().isTagged();
		if (addText)
		{
			PdfDictionary markedContentProps = new PdfDictionary();
			markedContentProps.put(PdfName.ACTUALTEXT, new PdfString(allText, PdfObject.TEXT_UNICODE));
			pdfContentByte.beginMarkedContentSequence(PdfName.SPAN, markedContentProps, true);
		}
		
		TabSegment segment = segments.get(segmentIndex);
		segment.layout.draw(
				pdfGraphics2D,
				x + drawPosX,// + leftPadding,
				y + topPadding + verticalAlignOffset + drawPosY
				);
		
		if (addText)
		{
			pdfContentByte.endMarkedContentSequence();
		}
		
		return;
	}

	@Override
	public boolean addActualText()
	{
		return addActualText;
	}
	

}
