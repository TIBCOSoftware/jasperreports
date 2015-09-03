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

import java.awt.Font;
import java.awt.Shape;
import java.awt.font.GlyphVector;
import java.awt.font.TextAttribute;
import java.text.AttributedCharacterIterator.Attribute;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfGraphics2D;

/**
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 */
public class PdfGlyphGraphics2D extends PdfGraphics2D
{

	private boolean initialized;
	private PdfContentByte pdfContentByte;
	private JRPdfExporter pdfExporter;
	private Locale locale;

	public PdfGlyphGraphics2D(PdfContentByte pdfContentByte, JRPdfExporter pdfExporter, Locale locale)
	{
		super(pdfContentByte, 
				pdfExporter.getCurrentPageFormat().getPageWidth(), pdfExporter.getCurrentPageFormat().getPageHeight(), 
				null, true, false, 0);		
		this.initialized = true;
		this.pdfContentByte = pdfContentByte;
		this.pdfExporter = pdfExporter;
		this.locale = locale;
	}
	
	@Override
	public void clip(Shape s)
	{
		if (!initialized)
		{
			//skipping the initial clip called from the PdfGraphics2D constructor
			return;
		}
		
		super.clip(s);
	}

	@Override
	public void drawGlyphVector(GlyphVector glyphVector, float x, float y)
	{
		Font awtFont = glyphVector.getFont();
		Map<Attribute, Object> fontAttrs = new HashMap<Attribute, Object>();
		Map<TextAttribute, ?> awtFontAttributes = awtFont.getAttributes();
		fontAttrs.putAll(awtFontAttributes);
		
		com.lowagie.text.Font currentFont = pdfExporter.getFont(fontAttrs, locale, false);
        
        PdfContentByte text = pdfContentByte.getDuplicate();
        text.beginText();
        
        float[] originalCoords = new float[]{x, y};
        float[] transformedCoors = new float[2];
        getTransform().transform(originalCoords, 0, transformedCoors, 0, 1);
        text.setTextMatrix(transformedCoors[0], pdfExporter.getCurrentPageFormat().getPageHeight() - transformedCoors[1]);
        
        double scaleX = awtFont.getTransform().getScaleX();
        double scaleY = awtFont.getTransform().getScaleY();
        double minScale = Math.min(scaleX, scaleY);
        text.setFontAndSize(currentFont.getBaseFont(), (float) (minScale * awtFont.getSize2D()));
        
		text.setColorFill(getColor());
		//FIXME find a way to determine the characters that correspond to this glyph vector
		// so that we can map the font glyphs that do not directly map to a character
		text.showText(glyphVector);
		text.resetRGBColorFill();
        
        text.endText();
        pdfContentByte.add(text);
	}

}
