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

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.text.AttributedCharacterIterator.Attribute;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import com.lowagie.text.BadElementException;
import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.SplitCharacter;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfFormField;
import com.lowagie.text.pdf.PdfOutline;
import com.lowagie.text.pdf.PdfTemplate;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.pdf.RadioCheckField;
import com.lowagie.text.pdf.TextField;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRPrintImage;
import net.sf.jasperreports.engine.JRPrintText;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.PrintPageFormat;
import net.sf.jasperreports.engine.export.AbstractPdfTextRenderer;
import net.sf.jasperreports.engine.export.PdfTextRenderer;
import net.sf.jasperreports.engine.export.SimplePdfTextRenderer;
import net.sf.jasperreports.engine.export.type.PdfFieldTypeEnum;
import net.sf.jasperreports.engine.type.ModeEnum;
import net.sf.jasperreports.engine.util.BreakIteratorSplitCharacter;
import net.sf.jasperreports.engine.util.JRStyledText;
import net.sf.jasperreports.engine.util.NullOutputStream;
import net.sf.jasperreports.export.pdf.PdfChunk;
import net.sf.jasperreports.export.pdf.PdfContent;
import net.sf.jasperreports.export.pdf.PdfDocument;
import net.sf.jasperreports.export.pdf.PdfDocumentWriter;
import net.sf.jasperreports.export.pdf.PdfImage;
import net.sf.jasperreports.export.pdf.PdfOutlineEntry;
import net.sf.jasperreports.export.pdf.PdfPhrase;
import net.sf.jasperreports.export.pdf.PdfProducer;
import net.sf.jasperreports.export.pdf.PdfProducerContext;
import net.sf.jasperreports.export.pdf.PdfRadioCheck;
import net.sf.jasperreports.export.pdf.PdfStructure;
import net.sf.jasperreports.export.pdf.PdfTextChunk;
import net.sf.jasperreports.export.pdf.PdfTextField;
import net.sf.jasperreports.renderers.Graphics2DRenderable;

/**
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 */
public class ClassicPdfProducer implements PdfProducer
{
	
	private PdfProducerContext context;
	
	private ClassicPdfStructure pdfStructure;
	
	private ClassicDocument document;
	private ClassicPdfWriter writer;

	private Document imageTesterDocument;
	private PdfContentByte imageTesterPdfContentByte;
	
	private SplitCharacter splitCharacter;
	private GlyphRendering glyphRendering;
	
	private ClassicPdfContent pdfContent;
	
	private Map<String, RadioCheckField> radioFieldFactories;
	private Map<String, PdfFormField> radioGroups;

	public ClassicPdfProducer(PdfProducerContext context)
	{
		this.context = context;
		this.glyphRendering = new GlyphRendering(this);
	}

	@Override
	public PdfProducerContext getContext()
	{
		return context;
	}

	@Override
	public PdfDocument createDocument(PrintPageFormat pageFormat)
	{
		Document pdfDocument =
				new Document(
					new Rectangle(
						pageFormat.getPageWidth(),
						pageFormat.getPageHeight()
					)
				);
			
		imageTesterDocument =
				new Document(
					new Rectangle(
						10, //jasperPrint.getPageWidth(),
						10 //jasperPrint.getPageHeight()
					)
				);
		
		document = new ClassicDocument(pdfDocument);
		return document;
	}

	@Override
	public PdfDocumentWriter createWriter(OutputStream os) throws JRException
	{
		try
		{
			PdfWriter pdfWriter = PdfWriter.getInstance(document.getDocument(), os);
			pdfWriter.setCloseStream(false);
			
			PdfWriter imageTesterPdfWriter =
				PdfWriter.getInstance(
					imageTesterDocument,
					new NullOutputStream() // discard the output
					);
			imageTesterDocument.open();
			imageTesterDocument.newPage();
			imageTesterPdfContentByte = imageTesterPdfWriter.getDirectContent();
			imageTesterPdfContentByte.setLiteral("\n");
			
			writer = new ClassicPdfWriter(this, pdfWriter);
			return writer;
		}
		catch (DocumentException e)
		{
			throw context.handleDocumentException(e);
		}
	}
	
	public PdfWriter getPdfWriter()
	{
		return writer.getPdfWriter();
	}

	@Override
	public void setTagged()
	{
		writer.getPdfWriter().setTagged();
	}

	@Override
	public PdfContent createPdfContent()
	{
		pdfContent = new ClassicPdfContent(writer.getPdfWriter());
		return pdfContent;
	}

	@Override
	public PdfContent getPdfContent()
	{
		return pdfContent;
	}

	public PdfContentByte getPdfContentByte()
	{
		return pdfContent.getPdfContentByte();
	}

	@Override
	public void initReport()
	{
		glyphRendering.initGlyphRenderer();
	}

	@Override
	public void setForceLineBreakPolicy(boolean forceLineBreakPolicy)
	{
		splitCharacter = forceLineBreakPolicy ? new BreakIteratorSplitCharacter() : null;
	}
	
	@Override
	public void newPage()
	{
		document.getDocument().newPage();
		pdfContent.refreshContent();
	}
	
	@Override
	public void setPageSize(PrintPageFormat pageFormat, int pageWidth, int pageHeight)
	{
		Rectangle pageSize;
		switch (pageFormat.getOrientation())
		{
		case LANDSCAPE:
			// using rotate to indicate landscape page
			pageSize = new Rectangle(pageHeight, pageWidth).rotate();
			break;
		default:
			pageSize = new Rectangle(pageWidth, pageHeight);
			break;
		}
		document.getDocument().setPageSize(pageSize);		
	}

	@Override
	public void endPage()
	{
		if (radioGroups != null)
		{
			for (PdfFormField radioGroup : radioGroups.values())
			{
				getPdfWriter().addAnnotation(radioGroup);
			}
			radioGroups = null;
			radioFieldFactories = null; // radio groups that overflow unto next page don't seem to work; reset everything as it does not make sense to keep them
		}
	}

	@Override
	public void close()
	{
		document.getDocument().close();
		imageTesterDocument.close();
	}

	@Override
	public AbstractPdfTextRenderer getTextRenderer(
			JRPrintText text, JRStyledText styledText, Locale textLocale,
			boolean awtIgnoreMissingFont, boolean defaultIndentFirstLine, boolean defaultJustifyLastLine)
	{
		AbstractPdfTextRenderer textRenderer = glyphRendering.getGlyphTextRenderer(text, styledText, textLocale,
				awtIgnoreMissingFont, defaultIndentFirstLine, defaultJustifyLastLine);
		if (textRenderer == null)
		{
			if (text.getLeadingOffset() == 0)
			{
				// leading offset is non-zero only for multiline texts that have at least one tab character or some paragraph indent (first, left or right)
				textRenderer = 
					new PdfTextRenderer(
						context.getJasperReportsContext(), 
						awtIgnoreMissingFont, 
						defaultIndentFirstLine,
						defaultJustifyLastLine
						);//FIXMENOW make some reusable instances here and below
			}
			else
			{
				textRenderer = 
					new SimplePdfTextRenderer(
						context.getJasperReportsContext(), 
						awtIgnoreMissingFont, 
						defaultIndentFirstLine,
						defaultJustifyLastLine
						);//FIXMETAB optimize this
			}
		}
		return textRenderer;
	}

	@Override
	public PdfImage createImage(byte[] data, boolean verify) throws IOException, JRException
	{
		try
		{
			Image image = Image.getInstance(data);
			
			if (verify)
			{
				imageTesterPdfContentByte.addImage(image, 10, 0, 0, 10, 0, 0);
			}
			
			return new ClassicImage(image);
		}
		catch (DocumentException e)
		{
			throw context.handleDocumentException(e);
		}
	}
	
	@Override
	public PdfImage createImage(BufferedImage bi, int angle) throws IOException
	{
		try
		{
			Image image = Image.getInstance(bi, null);
			image.setRotationDegrees(angle);
			
			return new ClassicImage(image);
		}
		catch (BadElementException e)
		{
			//TODO message
			throw new JRRuntimeException(e);
		}
	}
	
	@Override
	public void drawImage(JRPrintImage image, Graphics2DRenderable renderer, boolean forceSvgShapes, 
			double templateWidth, double templateHeight,
			int translateX, int translateY, double angle, 
			double renderWidth, double renderHeight, 
			float ratioX, float ratioY, float x, float y) throws JRException, IOException
	{
		PdfContentByte pdfContentByte = getPdfContentByte();
		PdfTemplate template = pdfContentByte.createTemplate(
				(float) templateWidth, (float) templateHeight);

		Graphics2D g = forceSvgShapes
			? template.createGraphicsShapes((float) templateWidth, (float) templateHeight)
			: template.createGraphics((float) templateWidth, (float) templateHeight, 
					new ClassicPdfFontMapper(this));

		try
		{
			g.translate(translateX, translateY);

			if (angle != 0)
			{
				g.rotate(angle);
			}
			
			if (image.getModeValue() == ModeEnum.OPAQUE)
			{
				g.setColor(image.getBackcolor());
				g.fillRect(0, 0, (int) renderWidth, (int) renderHeight);
			}

			renderer.render(context.getJasperReportsContext(), g, 
					new Rectangle2D.Double(0, 0, renderWidth, renderHeight));
		}
		finally
		{
			g.dispose();
		}

		pdfContentByte.saveState();
		pdfContentByte.addTemplate(
			template,
			ratioX, 0f, 0f, ratioY, x, y);
		pdfContentByte.restoreState();
		
		getPdfWriter().releaseTemplate(template);
	}
	
	public Font getFont(Map<Attribute,Object> attributes, Locale locale)
	{
		ClassicFontRecipient fontRecipient = new ClassicFontRecipient();
		context.setFont(attributes, locale, false, fontRecipient);
		Font font = fontRecipient.getFont();
		return font;
	}
	
	@Override
	public PdfTextChunk createChunk(String text, Map<Attribute,Object> attributes, Locale locale)
	{
		Font font = getFont(attributes, locale);
		Chunk chunk = new Chunk(text, font);

		if (splitCharacter != null)
		{
			//TODO use line break offsets if available?
			chunk.setSplitCharacter(splitCharacter);
		}
		
		return new ClassicTextChunk(this, chunk, font);
	}

	@Override
	public PdfChunk createChunk(PdfImage imageContainer)
	{
		Image image = ((ClassicImage) imageContainer).getImage();
		Chunk chunk = new Chunk(image, 0, 0);
		return new ClassicChunk(this, chunk);
	}
	
	@Override
	public PdfPhrase createPhrase()
	{
		Phrase phrase = new Phrase();
		return new ClassicPhrase(this, phrase);
	}

	@Override
	public PdfPhrase createPhrase(PdfChunk chunk)
	{
		Phrase phrase = new Phrase(((ClassicChunk) chunk).getChunk());
		return new ClassicPhrase(this, phrase);
	}

	@Override
	public PdfTextField createTextField(float llx, float lly, float urx, float ury, String fieldName)
	{
		TextField textField = createTextFormField(llx, lly, urx, ury, fieldName);
		return new ClassicPdfTextField(this, textField, PdfFieldTypeEnum.TEXT);
	}

	protected TextField createTextFormField(float llx, float lly, float urx, float ury, String fieldName)
	{
		Rectangle rectangle = new Rectangle(llx, lly, urx, ury);
		TextField textField = new TextField(writer.getPdfWriter(), rectangle, fieldName);
		return textField;
	}

	@Override
	public PdfTextField createComboField(float llx, float lly, float urx, float ury, String fieldName, 
			String value, String[] choices)
	{
		TextField textField = createTextFormField(llx, lly, urx, ury, fieldName);		
		setFieldChoices(textField, value, choices);
		return new ClassicPdfTextField(this, textField, PdfFieldTypeEnum.COMBO);
	}

	protected void setFieldChoices(TextField textField, String value, String[] choices)
	{
		if (choices != null)
		{
			textField.setChoices(choices);
			
			if (value != null)
			{
				int i = 0;
				for (String choice : choices)
				{
					if (value.equals(choice))
					{
						textField.setChoiceSelection(i);
						break;
					}
					i++;
				}
			}
		}
	}

	@Override
	public PdfTextField createListField(float llx, float lly, float urx, float ury, String fieldName, 
			String value, String[] choices)
	{
		TextField textField = createTextFormField(llx, lly, urx, ury, fieldName);		
		setFieldChoices(textField, value, choices);
		return new ClassicPdfTextField(this, textField, PdfFieldTypeEnum.LIST);
	}

	@Override
	public PdfRadioCheck createCheckField(float llx, float lly, float urx, float ury, String fieldName, 
			String onValue)
	{
		Rectangle rectangle = new Rectangle(llx, lly, urx, ury);
		RadioCheckField radioField = new RadioCheckField(writer.getPdfWriter(), rectangle, fieldName, onValue);
		return new ClassicRadioCheck(this, radioField);
	}

	@Override
	public PdfRadioCheck getRadioField(float llx, float lly, float urx, float ury, String fieldName, 
			String onValue)
	{
		Rectangle rectangle = new Rectangle(llx, lly, urx, ury);
		//TODO does this make sense?
		RadioCheckField radioField = radioFieldFactories == null ? null : radioFieldFactories.get(fieldName);
		if (radioField == null)
		{
			radioField = new RadioCheckField(writer.getPdfWriter(), rectangle, fieldName, onValue);
			if (radioFieldFactories == null)
			{
				radioFieldFactories = new HashMap<>();
			}
			radioFieldFactories.put(fieldName, radioField);
		}
		
		radioField.setBox(rectangle);
		
		return new ClassicRadioCheck(this, radioField);
	}
	
	protected PdfFormField getRadioGroup(RadioCheckField radioCheckField)
	{
		String fieldName = radioCheckField.getFieldName();
		PdfFormField radioGroup = radioGroups == null ? null : radioGroups.get(fieldName);
		if (radioGroup == null)
		{
			if (radioGroups == null)
			{
				radioGroups = new HashMap<>();
			}
			
			radioGroup = radioCheckField.getRadioGroup(true, false);
			radioGroups.put(fieldName, radioGroup);
		}
		return radioGroup;
	}

	@Override
	public PdfOutlineEntry getRootOutline()
	{
		PdfOutline rootOutline = pdfContent.getPdfContentByte().getRootOutline();
		return new ClassicPdfOutline(rootOutline);
	}

	@Override
	public PdfStructure getPdfStructure()
	{
		if (pdfStructure == null)
		{
			pdfStructure = new ClassicPdfStructure(this);
		}
		return pdfStructure;
	}
	
}
