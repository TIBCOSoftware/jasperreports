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
package net.sf.jasperreports.export.pdf;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.text.AttributedCharacterIterator.Attribute;
import java.util.Locale;
import java.util.Map;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRPrintText;
import net.sf.jasperreports.engine.PrintPageFormat;
import net.sf.jasperreports.engine.export.AbstractPdfTextRenderer;
import net.sf.jasperreports.engine.util.JRStyledText;

/**
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 */
public interface PdfProducer
{

	PdfProducerContext getContext();
	
	PdfDocument createDocument(PrintPageFormat pageFormat);

	PdfDocumentWriter createWriter(OutputStream os) throws JRException;

	void setTagged();
	
	PdfStructure getPdfStructure();

	PdfContent createPdfContent();

	PdfContent getPdfContent();

	void initReport();

	void setForceLineBreakPolicy(boolean forceLineBreakPolicy);

	void newPage();

	void setPageSize(PrintPageFormat pageFormat, int pageWidth, int pageHeight);

	AbstractPdfTextRenderer getCustomTextRenderer(
			JRPrintText text, JRStyledText styledText, Locale textLocale, 
			boolean awtIgnoreMissingFont, boolean defaultIndentFirstLine, boolean defaultJustifyLastLine);
	
	PdfImage createImage(byte[] loadBytesFromResource, boolean verify) throws IOException, JRException;

	PdfImage createImage(BufferedImage bi, int angle) throws IOException;
	
	PdfTextChunk createChunk(String text, Map<Attribute, Object> attributes, Locale locale);

	PdfChunk createChunk(PdfImage imageContainer);

	PdfPhrase createPhrase();

	PdfPhrase createPhrase(PdfChunk chunk);
	
	PdfImageTemplate createImageTemplate(float templateWidth, float templateHeight);

	PdfTextField createTextField(float llx, float lly, float urx, float ury, String fieldName);

	PdfRadioCheck createRadioField(float llx, float lly, float urx, float ury, String fieldName, 
			String onValue);
	
	PdfOutlineEntry getRootOutline();
	
	void close();
	
}
