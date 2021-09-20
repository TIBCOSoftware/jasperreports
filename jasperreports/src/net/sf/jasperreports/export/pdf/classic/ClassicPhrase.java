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
package net.sf.jasperreports.export.pdf.classic;

import com.lowagie.text.DocumentException;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.ColumnText;
import com.lowagie.text.pdf.PdfWriter;

import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.export.pdf.PdfChunk;
import net.sf.jasperreports.export.pdf.PdfPhrase;
import net.sf.jasperreports.export.pdf.PdfTextAlignment;
import net.sf.jasperreports.export.pdf.TextDirection;

/**
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 */
public class ClassicPhrase implements PdfPhrase
{

	private ClassicPdfProducer pdfProducer;
	private Phrase phrase;

	public ClassicPhrase(ClassicPdfProducer pdfProducer, Phrase phrase)
	{
		this.pdfProducer = pdfProducer;
		this.phrase = phrase;
	}

	@Override
	public void add(PdfChunk chunk)
	{
		phrase.add(((ClassicChunk) chunk).getChunk());
	}

	@Override
	public float go(float llx, float lly, float urx, float ury, 
			float fixedLeading, float multipliedLeading, 
			PdfTextAlignment alignment, TextDirection runDirection)
	{
		ColumnText colText = new ColumnText(pdfProducer.getPdfContentByte());
		colText.setSimpleColumn(phrase, 
				llx, lly, urx, ury, 
				fixedLeading, 
				ClassicPdfUtils.toITextAlignment(alignment));
		if (multipliedLeading != 0f)
		{
			colText.setLeading(fixedLeading, multipliedLeading);
		}
		colText.setRunDirection(toITextRunDirection(runDirection));
		try
		{
			colText.go();
		}
		catch (DocumentException e)
		{
			throw new JRRuntimeException(e);
		}
		return colText.getYLine();
	}
	
	protected static int toITextRunDirection(TextDirection direction)
	{
		int iTextDirection;
		switch (direction)
		{
		case DEFAULT:
			iTextDirection = PdfWriter.RUN_DIRECTION_DEFAULT;
			break;
		case LTR:
			iTextDirection = PdfWriter.RUN_DIRECTION_LTR;
			break;
		case RTL:
			iTextDirection = PdfWriter.RUN_DIRECTION_RTL;
			break;
		default:
			throw new JRRuntimeException("Unknown text direction " + direction);
		}
		return iTextDirection;
	}

}
