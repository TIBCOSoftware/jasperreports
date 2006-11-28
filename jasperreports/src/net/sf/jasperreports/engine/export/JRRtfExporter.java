/*
 * ============================================================================
 * GNU Lesser General Public License
 * ============================================================================
 *
 * JasperReports - Free Java report-generating library.
 * Copyright (C) 2001-2006 JasperSoft Corporation http://www.jaspersoft.com
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307, USA.
 * 
 * JasperSoft Corporation
 * 303 Second Street, Suite 450 North
 * San Francisco, CA 94107
 * http://www.jaspersoft.com
 */

/*
 * Contributors:
 * Matt Thompson - mthomp1234@users.sourceforge.net
 */
package net.sf.jasperreports.engine.export;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.font.TextAttribute;
import java.awt.geom.Dimension2D;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.text.AttributedCharacterIterator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRAbstractExporter;
import net.sf.jasperreports.engine.JRAlignment;
import net.sf.jasperreports.engine.JRBox;
import net.sf.jasperreports.engine.JRElement;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JRFont;
import net.sf.jasperreports.engine.JRGraphicElement;
import net.sf.jasperreports.engine.JRImage;
import net.sf.jasperreports.engine.JRLine;
import net.sf.jasperreports.engine.JRPrintElement;
import net.sf.jasperreports.engine.JRPrintEllipse;
import net.sf.jasperreports.engine.JRPrintFrame;
import net.sf.jasperreports.engine.JRPrintGraphicElement;
import net.sf.jasperreports.engine.JRPrintImage;
import net.sf.jasperreports.engine.JRPrintLine;
import net.sf.jasperreports.engine.JRPrintPage;
import net.sf.jasperreports.engine.JRPrintRectangle;
import net.sf.jasperreports.engine.JRPrintText;
import net.sf.jasperreports.engine.JRRenderable;
import net.sf.jasperreports.engine.JRReport;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.base.JRBaseFont;
import net.sf.jasperreports.engine.base.JRBasePrintText;
import net.sf.jasperreports.engine.util.JRImageLoader;
import net.sf.jasperreports.engine.util.JRStyledText;

/**
 * Exports a JasperReports document to RTF format. It has binary output type and exports the document to
 * a free-form layout. It uses the RTF Specification 1.6 (compatible with MS Word 6.0, 2003 and XP).
 * <p>
 * Since classic AWT fonts can be sometimes very different from system fonts (which are used by RTF viewers),
 * a font mapping feature was added. By using the {@link JRExporterParameter#FONT_MAP} parameter, a logical
 * font like "sansserif" can be mapped to a system specific font, like "Comic Sans MS". Both map keys and values are strings.
 * @author Flavius Sana (flavius_sana@users.sourceforge.net)
 * @version $Id$
 */
public class JRRtfExporter extends JRAbstractExporter
{
	protected JRExportProgressMonitor progressMonitor = null;
	
	protected Writer writer = null;
	protected File destFile = null;
	
	protected int reportIndex = 0;
	
	// temporaray list of fonts and colors to be
	// added to the header or the document
	private StringBuffer colorBuffer = null;
	private StringBuffer fontBuffer = null;
	protected List colors = null;
	protected List fonts = null;

	// z order of the graphical objects in .rtf file
	private int zorder = 1;
	
	// indicate that report containts Unicode characters with code > 127
	private boolean isUnicode = false;
	
	private Map fontMap = null;
	
	
	/**
	 * Export report in .rtf format
	 */
	public void exportReport() throws JRException
	{
		progressMonitor = (JRExportProgressMonitor)parameters.get(JRExporterParameter.PROGRESS_MONITOR);
		

		try
		{
			/*   */
			setExportContext();
	
			/*   */
			setInput();

			if (!isModeBatch) {
				setPageRange();
			}

			fonts = new ArrayList();
			fontBuffer = new StringBuffer();
			colors = new ArrayList();
			colors.add(null);
			colorBuffer = new StringBuffer(";");
			
			fontMap = (Map) parameters.get(JRExporterParameter.FONT_MAP);
			
			StringBuffer sb = (StringBuffer)parameters.get(JRExporterParameter.OUTPUT_STRING_BUFFER);
			if (sb != null) {
				StringBuffer buffer = exportReportToBuffer();
				sb.append(buffer.toString());
			}
			else {
				Writer outWriter = (Writer)parameters.get(JRExporterParameter.OUTPUT_WRITER);
				if (outWriter != null) {
					try {
						writer = outWriter;
						
						// export report
						exportReportToStream();
					}
					catch (IOException ex) {
						throw new JRException("Error writing to writer : " + jasperPrint.getName(), ex);
					}
				}
				else {
					OutputStream os = (OutputStream)parameters.get(JRExporterParameter.OUTPUT_STREAM);
					if(os != null) {
						try {
							writer = new OutputStreamWriter(os);
							
							// export report
							exportReportToStream();
						}
						catch (Exception ex) {
							throw new JRException("Error writing to output stream : " + jasperPrint.getName(), ex);
						}
					}
					else {
						destFile = (File)parameters.get(JRExporterParameter.OUTPUT_FILE);
						if (destFile == null) {
							String fileName = (String)parameters.get(JRExporterParameter.OUTPUT_FILE_NAME);
							if (fileName != null) {
								destFile = new File(fileName);
							}
							else {
								throw new JRException("No output specified for the exporter");
							}
						}
						
						// export report
						exportReportToFile();
					}
				}
			}
		}
		finally
		{
			resetExportContext();
		}
	}
	
	
	/**
	 * Export report in .rtf format
	 * @return report in .rtf format in a StringBuffer object
	 */
	protected StringBuffer exportReportToBuffer() throws JRException{
		StringWriter buffer = new StringWriter();
		writer = buffer;
		try {
			exportReportToStream();
		}
		catch (IOException ex) {
			throw new JRException("Error while exporting report to the buffer");
		}
		
		return buffer.getBuffer();
	}
	
	
	/**
	 * Export report in .rtf format to a stream
	 * @throws JRException
	 * @throws IOException
	 */
	protected void exportReportToStream() throws JRException, IOException {
		
		// create the header of the rtf file 
		writer.write("{\\rtf1\\ansi\\deff0\n");
		// create font and color tables
		createColorAndFontEntries();
		writer.write("{\\fonttbl ");
		writer.write(fontBuffer.toString()); 
		writer.write("}\n");
		
		writer.write("{\\colortbl ");
		writer.write(colorBuffer.toString());
		writer.write("}\n");
		
		
		for(reportIndex = 0; reportIndex < jasperPrintList.size(); reportIndex++ ){
			jasperPrint = (JasperPrint)jasperPrintList.get(reportIndex);
			
			List pages = jasperPrint.getPages();
			if (pages != null && pages.size() > 0){
				if (isModeBatch)
				{
					startPageIndex = 0;
					endPageIndex = pages.size() - 1;
				}
				JRPrintPage page = null;
				
				writer.write("{\\info{\\nofpages");
				writer.write(String.valueOf(pages.size()));
				writer.write("}}\n");
				
				writer.write("\\viewkind1\\paperw");
				writer.write(String.valueOf(twip(jasperPrint.getPageWidth())));
				writer.write("\\paperh");
				writer.write(String.valueOf(twip(jasperPrint.getPageHeight())));
				
				writer.write("\\marglsxn0\\margrsxn0\\margtsxn0\\margbsxn0");
				
				if (jasperPrint.getOrientation() == JRReport.ORIENTATION_LANDSCAPE) {
					writer.write("\\lndscpsxn");
				}
				
				
				for (int pageIndex = startPageIndex; pageIndex <= endPageIndex; pageIndex++) {
					writer.write("\n");
					if(Thread.currentThread().isInterrupted()){
						throw new JRException("Current thread intrerrupted");
					}
					
					page = (JRPrintPage)pages.get(pageIndex);
					
					boolean lastPageFlag = false;
					if(pageIndex == endPageIndex && reportIndex == (jasperPrintList.size() - 1)){
						lastPageFlag = true;
					}
					exportPage(page, lastPageFlag);
				}
			}	
		}
		writer.write("}\n");
		writer.flush();
	}
	
	
	/**
	 * Export report to a file in the .rtf format 
	 */
	protected void exportReportToFile() throws JRException {
		try {
			OutputStream fileOutputStream = new FileOutputStream(destFile);
			writer = new BufferedWriter(new OutputStreamWriter(fileOutputStream));
			exportReportToStream();
		}
		catch (IOException ex) {
			throw new JRException("Error writing to the file : " + destFile, ex);
		}
		finally {
			if(writer != null) {
				try {
					writer.close();
				}
				catch(IOException ex) {
					
				}
			}
		}
	}
	
	
	/**
	 * Create color and font entries for the header of .rtf file. 
	 * Each color is represented by values of the red, 
	 * green and blue components. 
	 * @throws JRException
	 */
	protected void createColorAndFontEntries() throws JRException {
		for(reportIndex = 0; reportIndex < jasperPrintList.size(); reportIndex++ ){
			jasperPrint = (JasperPrint)jasperPrintList.get(reportIndex);

			getFontIndex(new JRBasePrintText(jasperPrint.getDefaultStyleProvider()));
			
			List pages = jasperPrint.getPages();
			if (pages != null && pages.size() > 0) {
				if (isModeBatch) {
					startPageIndex = 0;
					endPageIndex = pages.size() - 1;
				}

				for (int pageIndex = startPageIndex; pageIndex <= endPageIndex; pageIndex++) {
					if (Thread.currentThread().isInterrupted()) {
						throw new JRException("Current thread interrupted");
					}
					JRPrintPage page = (JRPrintPage)pages.get(pageIndex);
					JRPrintElement element = null;
					Collection elements = page.getElements();
					if (elements != null && elements.size() > 0)
					{
						for (Iterator it = elements.iterator(); it.hasNext();)
						{
							element = (JRPrintElement) it.next();
							getColorIndex(element.getForecolor());
							getColorIndex(element.getBackcolor());

							if (element instanceof JRPrintText)
							{
								JRPrintText text = (JRPrintText)element;

								// create color indices for box border color
								getColorIndex(text.getBorderColor());
								getColorIndex(text.getTopBorderColor());
								getColorIndex(text.getBottomBorderColor());
								getColorIndex(text.getLeftBorderColor());
								getColorIndex(text.getRightBorderColor());

								for(int i = 0; i < text.getText().length(); i++ ){
									if((text.getText().charAt(i)) > 127){
										isUnicode = true;
										break;
									}
								}

								int runLimit = 0;
								JRStyledText styledText = getStyledText((JRPrintText)element);
								AttributedCharacterIterator iterator = styledText.getAttributedString().getIterator();
								while (runLimit < styledText.length()
										&& (runLimit = iterator.getRunLimit()) <= styledText.length())
								{
									Map styledTextAttributes = iterator.getAttributes();

									getFontIndex(new JRBaseFont(styledTextAttributes));

									getColorIndex((Color)styledTextAttributes.get(TextAttribute.FOREGROUND));
									getColorIndex((Color)styledTextAttributes.get(TextAttribute.BACKGROUND));

									iterator.setIndex(runLimit);
								}

								getFontIndex((JRPrintText)element);
							}
						}
					}
				}
			}
		}
	}
	
	
	/**
	 * Return color index from header of the .rtf file. If a color is not 
	 * found is automatically added to the header of the rtf file. The
	 * method is called first when the header of the .rtf file is constructed
	 * and when a componenent needs a color for foreground or background
	 * @param color Color for which the index is required. 
	 * @return index of the color from .rtf file header
	 */
	private int getColorIndex(Color color)
	{
		int colorNdx = colors.indexOf(color);
		if (colorNdx < 0)
		{
			colorNdx = colors.size();
			colors.add(color);
			colorBuffer.append("\\red").append(color.getRed())
					.append("\\green").append(color.getGreen())
					.append("\\blue").append(color.getBlue())
					.append(";");
		}
		return colorNdx;
	}
	
	
	/**
	 * Return font index from the header of the .rtf file. The method is
	 * called first when the header of the .rtf document is constructed and when a
	 * text component needs font informations.
	 * @param font the font for which the index is required
	 * @return index of the font from .rtf file header
	 */
	private int getFontIndex(JRFont font) 
	{
		String fontName = font.getFontName(); 
		if(fontMap != null && fontMap.containsKey(fontName)){
			fontName = (String)fontMap.get(fontName);
		}
		
		int fontIndex = fonts.indexOf(fontName);
		
		if(fontIndex < 0) {
			fontIndex = fonts.size();
			fonts.add(fontName);
			fontBuffer.append("{\\f").append(fontIndex).append("\\fnil ").append(fontName).append(";}");
		}
		
		return fontIndex;
	}
	
	/**
	 * Convert a int value from points to twips (multiply with 20)
	 * @param points value that needs to be converted
	 * @return converted value in twips
	 */
	private int twip(int points) {
		return points * 20;
	}
	
	
	/**
	 * Convert a float value to twips (multiply with 20)
	 * @param points value that need to be converted
	 * @return converted value in twips
	 */
	private int twip(float points) {
		return (int)(points * 20);
	}
	
	
	/**
	 * Exports a report page
	 * @param page Page that will be exported
	 * @throws JRException
	 */
	protected void exportPage(JRPrintPage page, boolean lastPage) throws JRException, IOException
	{
		exportElements(page.getElements());
		
		if(lastPage == false){
			if(isUnicode){
				writer.write("{\\pard\\pagebb\\par}\n" );
			}
			else {
				writer.write("\\page\n");
			}
			
		}
	}
	
	/**
	 * Add a graphic element to the .rtf document
	 * @param type Type of the graphic element
	 * @param x x axis position of the graphic element
	 * @param y y axis position of the graphic 
	 * @param w width of the graphic element
	 * @param h height of the graphic element
	 * @throws IOException
	 */
	private void startGraphic(String type, int x, int y, int w, int h) throws IOException {  
		writer.write("{\\*\\do\\dobxpage\\dobypage\\dodhgt");
		writer.write(String.valueOf(zorder++));
		
		writer.write("\\");
		writer.write(type);
		
		writer.write("\\dpx");
		writer.write(String.valueOf(x));
		writer.write("\\dpy");
		writer.write(String.valueOf(y));
		
		writer.write("\\dpxsize");
		writer.write(String.valueOf(w));
		writer.write("\\dpysize");
		writer.write(String.valueOf(h));
	}
	
	/**
	 * Add document control words that marks the end of a graphic element
	 * @param element Graphic element
	 * @throws IOException
	 */
	private void finishGraphic(JRPrintGraphicElement element) throws IOException {
		int mode = 0;
		if(element.getMode() == JRElement.MODE_OPAQUE) {
			mode = 1;
		}
		
		finishGraphic( element.getPen(),
					element.getForecolor(),
					element.getBackcolor(),
					mode );
	}
	
	/**
	 * Add document control words that marks the end of a graphic element
	 * @param pen pen dimension
	 * @param fg foreground color
	 * @param bg background color
	 * @param fillPattern fill pattern
	 * @throws IOException
	 */
	private void finishGraphic(byte pen, Color fg, Color bg, int fillPattern) throws IOException {
		switch(pen) {
			case JRGraphicElement.PEN_THIN:
				writer.write("\\dplinew10");
				break;
			case JRGraphicElement.PEN_1_POINT:
				writer.write("\\dplinew20");
				break;
			case JRGraphicElement.PEN_2_POINT:
				writer.write("\\dplinew40");
				break;
			case JRGraphicElement.PEN_4_POINT:
				writer.write("\\dplinew80");
				break;
			case JRGraphicElement.PEN_DOTTED:
				writer.write("\\dplinedash");
				break;
			case JRGraphicElement.PEN_NONE:
				writer.write("\\dplinehollow");
				break;
			default:
				writer.write("\\dplinew20");
				break;
		}
		
		writer.write("\\dplinecor");
		writer.write(String.valueOf(fg.getRed()));
		writer.write("\\dplinecob");
		writer.write(String.valueOf(fg.getBlue()));
		writer.write("\\dplinecog");
		writer.write(String.valueOf(fg.getGreen()));
		
		writer.write("\\dpfillfgcr");
		writer.write(String.valueOf(fg.getRed()));
		writer.write("\\dplinefgcb");
		writer.write(String.valueOf(fg.getBlue()));
		writer.write("\\dpfillfgcg");
		writer.write(String.valueOf(fg.getGreen()));
		
		writer.write("\\dpfillbgcr");
		writer.write(String.valueOf(bg.getRed()));
		writer.write("\\dpfillbgcg");
		writer.write(String.valueOf(bg.getGreen()));
		writer.write("\\dpfillbgcb");
		writer.write(String.valueOf(bg.getBlue()));
		
		writer.write("\\dpfillpat");
		writer.write(String.valueOf(fillPattern));
		writer.write("}\n");
	}

	
	
	/**
	 * Get border adjustment for graphic elements depending on pen width used
	 * @param pen
	 */
	protected int getAdjustment(byte pen)
	{
		switch (pen)
		{
			case JRGraphicElement.PEN_THIN:
				return 0;
			case JRGraphicElement.PEN_1_POINT:
				return 10;
			case JRGraphicElement.PEN_2_POINT:
				return 20;
			case JRGraphicElement.PEN_4_POINT:
				return 40;
			case JRGraphicElement.PEN_DOTTED:
				return 0;
			case JRGraphicElement.PEN_NONE:
				return 0;
			default:
				return 0;
		}
	}
	
	
	/**
	 * Draw a line object
	 * @param line JasperReports line object - JRPrintLine
	 * @throws IOException
	 */
	protected void exportLine(JRPrintLine line) throws IOException {
		int x = twip(line.getX());
		x += getOffsetX();
		int y = twip(line.getY());
		y+= getOffsetY();
		int h = twip(line.getHeight());
		int w = twip(line.getWidth());

		if (w <= 20 || h <= 20)
		{
			if (w > 20)
			{
				h = 0;
			}
			else
			{
				w = 0;
			}
		}

		startGraphic("dpline", x, y, w, h);

		if (line.getDirection() == JRLine.DIRECTION_TOP_DOWN)
		{
			writer.write("\\dpptx");
			writer.write(String.valueOf(x));
			writer.write("\\dppty");
			writer.write(String.valueOf(y));
			writer.write("\\dpptx");
			writer.write(String.valueOf(x + w));
			writer.write("\\dppty");
			writer.write(String.valueOf(y + h));
		}
		else
		{
			writer.write("\\dpptx");
			writer.write(String.valueOf(x));
			writer.write("\\dppty");
			writer.write(String.valueOf(y + h));
			writer.write("\\dpptx");
			writer.write(String.valueOf(x + w));
			writer.write("\\dppty");
			writer.write(String.valueOf(y));
		}

		finishGraphic(line);
	}
	
	
	/**
	 * Draw a rectangle
	 * @param rect JasperReports rectangle object (JRPrintRectangle)
	 */
	protected void exportRectangle(JRPrintRectangle rect) throws IOException {
		startGraphic("dprect" + (rect.getRadius() > 0 ? "\\dproundr" : ""),
				twip(rect.getX() + getOffsetX()), 
				twip(rect.getY() + getOffsetY()), 
				twip(rect.getWidth()),
				twip(rect.getHeight())
				);
		finishGraphic(rect);
	}
	
	
	/**
	 * Draw a ellipse object
	 * @param ellipse JasperReports ellipse object (JRPrintElipse)
	 */
	protected void exportEllipse(JRPrintEllipse ellipse) throws IOException {
		startGraphic(
			"dpellipse", 
			twip(ellipse.getX() + getOffsetX()), 
			twip(ellipse.getY() + getOffsetY()),
			twip(ellipse.getWidth()), 
			twip(ellipse.getHeight())
		);
		finishGraphic(ellipse);
	}

	
	/**
	 * Draw a text box
	 * @param text JasperReports text object (JRPrintText)
	 * @throws JRException 
	 */
	protected void exportText(JRPrintText text) throws IOException, JRException {
		
		
		// use styled text
		JRStyledText styledText = getStyledText(text);
		if (styledText == null)
		{
			return;
		}

		int x = twip(text.getX() + getOffsetX());
		int y = twip(text.getY() + getOffsetY());

		int width = twip(text.getWidth());
		int height = twip(text.getHeight());

		int textBoxAdjustment = 20;
		
		int textHeight = twip(text.getTextHeight());
		
		if(textHeight <= 0) {
			if(height <= 0 ){
				throw new JRException("Invalid text height");
			}
			textHeight = height;
		}
		
		// padding for the text
		int topPadding = twip(text.getTopPadding());
		int leftPadding = twip(text.getLeftPadding());
		int bottomPadding = twip(text.getBottomPadding());
		int rightPadding = twip(text.getRightPadding());

		Color bgcolor = text.getBackcolor();
		
		if (text.getMode() == JRElement.MODE_OPAQUE)
		{
			startGraphic("dprect", x, y, width, height);
			finishGraphic(JRGraphicElement.PEN_NONE, text.getForecolor(), bgcolor, 1);
		}
		
		int verticalAdjustment = topPadding;
		switch (text.getVerticalAlignment())
		{
			case JRAlignment.VERTICAL_ALIGN_TOP:
				verticalAdjustment = 0;
				break;
			case JRAlignment.VERTICAL_ALIGN_MIDDLE:
				verticalAdjustment = (height - topPadding - bottomPadding - twip(text.getTextHeight())) / 2;
				break;
			case JRAlignment.VERTICAL_ALIGN_BOTTOM:
				verticalAdjustment = (height - topPadding - bottomPadding - twip(text.getTextHeight()));
				break;
		}
		
		/* 
		 rtf text box does not allow unicode characters
		 representation so if the report contains
		 unicode characters above 127 the text box
		 is replaced by paragraphs
		 */
		if(isUnicode) {
			writer.write("{\\pard\\absw");
			writer.write(String.valueOf(width));
			writer.write("\\absh");
			writer.write(String.valueOf(textHeight));
			writer.write("\\phpg\\posx");
			writer.write(String.valueOf(x));
			writer.write("\\pvpg\\posy");
			writer.write(String.valueOf(y + verticalAdjustment + topPadding));
		}
		else {
			writer.write("{\\*\\do\\dobxpage\\dobypage\\dodhgt");
			writer.write(String.valueOf(zorder++));
			writer.write("\\dptxbx\\dpx");
			writer.write(String.valueOf(x + textBoxAdjustment));
			writer.write("\\dpxsize");
			writer.write(String.valueOf(width - textBoxAdjustment));
			writer.write("\\dpy");
			writer.write(String.valueOf(y + verticalAdjustment + topPadding + textBoxAdjustment));
			writer.write("\\dpysize");
			writer.write(String.valueOf(textHeight + bottomPadding - textBoxAdjustment));
			writer.write("\\dpfillpat0\\dplinehollow{\\dptxbxtext {\\pard");
		}
		
		JRFont font = text;

		writer.write("\\f");
		writer.write(String.valueOf(getFontIndex(font)));
		writer.write("\\cf");
		writer.write(String.valueOf(getColorIndex(text.getForecolor())));
		writer.write("\\cb");
		writer.write(String.valueOf(getColorIndex(bgcolor)));
		writer.write(" ");

		if (leftPadding > 0)
		{
			writer.write("\\li");
			writer.write(String.valueOf(leftPadding));
		}

		if (rightPadding > 0)
		{
			writer.write("\\ri");
			writer.write(String.valueOf(rightPadding));
		}

		if (font.isBold())
			writer.write("\\b");
		if (font.isItalic())
			writer.write("\\i");
		if (font.isStrikeThrough())
			writer.write("\\strike");
		if (font.isUnderline())
			writer.write("\\ul");
		writer.write("\\fs");
		writer.write(String.valueOf(font.getFontSize() * 2));

		switch (text.getHorizontalAlignment())
		{
			case JRAlignment.HORIZONTAL_ALIGN_LEFT:
				writer.write("\\ql");
				break;
			case JRAlignment.HORIZONTAL_ALIGN_CENTER:
				writer.write("\\qc");
				break;
			case JRAlignment.HORIZONTAL_ALIGN_RIGHT:
				writer.write("\\qr");
				break;
			case JRAlignment.HORIZONTAL_ALIGN_JUSTIFIED:
				writer.write("\\qj");
				break;
			default:
				writer.write("\\ql");
				break;
		}

		writer.write("\\sl");
		writer.write(String.valueOf(twip(text.getLineSpacingFactor() * font.getFontSize())));
		writer.write(" ");

		// add parameters in case of styled text element
		String plainText = styledText.getText();
		int runLimit = 0;

		AttributedCharacterIterator iterator = styledText.getAttributedString().getIterator();
		while (
			runLimit < styledText.length()
			&& (runLimit = iterator.getRunLimit()) <= styledText.length()
			)
		{

			Map styledTextAttributes = iterator.getAttributes();
			JRFont styleFont = new JRBaseFont(styledTextAttributes);
			Color styleForeground = (Color) styledTextAttributes.get(TextAttribute.FOREGROUND);
			Color styleBackground = (Color) styledTextAttributes.get(TextAttribute.BACKGROUND);
			
			writer.write("\\f");
			writer.write(String.valueOf(getFontIndex(styleFont)));
			writer.write("\\fs");
			writer.write(String.valueOf(2 * styleFont.getFontSize()));

			if (styleFont.isBold())
			{
				writer.write("\\b");
			}
			if (styleFont.isItalic())
			{
				writer.write("\\i");
			}
			if (styleFont.isUnderline())
			{
				writer.write("\\ul");
			}
			if (styleFont.isStrikeThrough())
			{
				writer.write("\\strike");
			}

			if (TextAttribute.SUPERSCRIPT_SUPER.equals(styledTextAttributes.get(TextAttribute.SUPERSCRIPT)))
			{
				writer.write("\\super");
			}
			else if (TextAttribute.SUPERSCRIPT_SUB.equals(styledTextAttributes.get(TextAttribute.SUPERSCRIPT)))
			{
				writer.write("\\sub");
			}
			
			if(!(null == styleBackground || styleBackground.equals(bgcolor))){
				writer.write("\\highlight");
				writer.write(String.valueOf(getColorIndex(styleBackground)));
			}
			writer.write("\\cf");
			writer.write(String.valueOf(getColorIndex(styleForeground)));
			writer.write(" ");

			int s = 0;
			int e = 0;
			String str = plainText.substring(iterator.getIndex(), runLimit);
			
			String pattern = "\n";
			String replace = "\\line ";
			StringBuffer result = new StringBuffer();

			while ((e = str.indexOf(pattern, s)) >= 0)
			{
				result.append(str.substring(s, e));
				result.append(replace);
				s = e + pattern.length();
			}
			result.append(str.substring(s));
			
			writer.write(handleUnicodeText(result, text.getRunDirection() == JRPrintText.RUN_DIRECTION_RTL));
			
			// reset all styles in the paragraph
			writer.write("\\plain");

			iterator.setIndex(runLimit);
		}
		
		if(isUnicode) {
			writer.write("\\par}\n");
		}
		else {
			writer.write("\\par}}}\n");
		}

		exportBox(text, x, y, width, height, text.getForecolor(), bgcolor);
	}
	
	
	/**
	 * Replace Unicode characters with RTF Unicode control words
	 * @param source source text
	 * @return text with Unicode characters replaced 
	 */
	private String handleUnicodeText(StringBuffer source, boolean isRightToLeft)
	{
		StringBuffer resultBuffer = new StringBuffer();
		StringBuffer leftToRightBuffer = new StringBuffer();
		
		for(int i = 0; i < source.length(); i++ )
		{
			long ch = source.charAt(i);
			if(ch > 127)
			{
				if(isRightToLeft)
				{
					resultBuffer.insert(0, leftToRightBuffer.toString());
					leftToRightBuffer = new StringBuffer();
					
					resultBuffer.insert(0, "\\u" + ch + '?');
				}
				else 
				{
					leftToRightBuffer.append("\\u" + ch + '?');
				}
			}
			else 
			{
				leftToRightBuffer.append((char)ch);
			}
		}
		
		if(leftToRightBuffer != null && leftToRightBuffer.length() > 0)
		{
			if(isRightToLeft)
			{
				resultBuffer.insert(0, leftToRightBuffer.toString());
			}
			else 
			{
				resultBuffer.append(leftToRightBuffer.toString());
			}
		}
		
		return resultBuffer.toString();
	}
	
	
	/**
	 * Export a image object 
	 * @param printImage JasperReports image object (JRPrintImage)
	 * @throws JRException
	 * @throws IOException
	 */
	protected void exportImage(JRPrintImage printImage) throws JRException, IOException
	{
		int x = twip(printImage.getX() + globalOffsetX);
		int y = twip(printImage.getY() + globalOffsetY);
		int width = twip(printImage.getWidth());
		int height = twip(printImage.getHeight());

		if (printImage.getMode() == JRElement.MODE_OPAQUE)
		{
			startGraphic("dprect", x, y, width, height);
			finishGraphic(JRGraphicElement.PEN_NONE, printImage.getForecolor(),
					printImage.getBackcolor(), 1);
		}

		int leftPadding = printImage.getLeftPadding();
		int topPadding = printImage.getTopPadding();
		int rightPadding = printImage.getRightPadding();
		int bottomPadding = printImage.getBottomPadding();

		int availableImageWidth = printImage.getWidth() - leftPadding - rightPadding;
		availableImageWidth = availableImageWidth < 0 ? 0 : availableImageWidth;

		int availableImageHeight = printImage.getHeight() - topPadding - bottomPadding;
		availableImageHeight = availableImageHeight < 0 ? 0 : availableImageHeight;

		JRRenderable renderer = printImage.getRenderer();

		if (availableImageWidth > 0 && availableImageHeight > 0 && renderer != null)
		{
			int normalWidth = availableImageWidth;
			int normalHeight = availableImageHeight;

			Dimension2D dimension = renderer.getDimension();
			if (dimension != null)
			{
				normalWidth = (int) dimension.getWidth();
				normalHeight = (int) dimension.getHeight();
			}

			float xalignFactor = 0f;
			switch (printImage.getHorizontalAlignment())
			{
				case JRAlignment.HORIZONTAL_ALIGN_RIGHT:
				{
					xalignFactor = 1f;
					break;
				}
				case JRAlignment.HORIZONTAL_ALIGN_CENTER:
				{
					xalignFactor = 0.5f;
					break;
				}
				case JRAlignment.HORIZONTAL_ALIGN_LEFT:
				default:
				{
					xalignFactor = 0f;
					break;
				}
			}

			float yalignFactor = 0f;
			switch (printImage.getVerticalAlignment())
			{
				case JRAlignment.VERTICAL_ALIGN_BOTTOM:
				{
					yalignFactor = 1f;
					break;
				}
				case JRAlignment.VERTICAL_ALIGN_MIDDLE:
				{
					yalignFactor = 0.5f;
					break;
				}
				case JRAlignment.VERTICAL_ALIGN_TOP:
				default:
				{
					yalignFactor = 0f;
					break;
				}
			}

			BufferedImage bi = new BufferedImage(availableImageWidth, availableImageHeight, BufferedImage.TYPE_INT_RGB);
			Graphics2D grx = bi.createGraphics();
			grx.setColor(printImage.getBackcolor());//FIXMEIMAGE ignore mode? what about alpha channel above?
			grx.fillRect(0, 0, availableImageWidth, availableImageHeight);

			switch (printImage.getScaleImage())
			{
				case JRImage.SCALE_IMAGE_CLIP:
				{
					int xoffset = (int) (xalignFactor * (availableImageWidth - normalWidth));
					int yoffset = (int) (yalignFactor * (availableImageHeight - normalHeight));

					renderer.render(grx, new Rectangle(xoffset, yoffset,
							normalWidth, normalHeight));

					break;
				}
				case JRImage.SCALE_IMAGE_FILL_FRAME:
				{
					renderer.render(grx, new Rectangle(0, 0,
							availableImageWidth, availableImageHeight));

					break;
				}
				case JRImage.SCALE_IMAGE_RETAIN_SHAPE:
				default:
				{
					if (printImage.getHeight() > 0)
					{
						double ratio = (double) normalWidth / (double) normalHeight;

						if (ratio > (double) availableImageWidth / (double) availableImageHeight)
						{
							normalWidth = availableImageWidth;
							normalHeight = (int) (availableImageWidth / ratio);
						}
						else
						{
							normalWidth = (int) (availableImageHeight * ratio);
							normalHeight = availableImageHeight;
						}

						int xoffset = (int) (xalignFactor * (availableImageWidth - normalWidth));
						int yoffset = (int) (yalignFactor * (availableImageHeight - normalHeight));

						renderer.render(grx, new Rectangle(xoffset, yoffset,
								normalWidth, normalHeight));
					}

					break;
				}
			}
			
			writer.write("{\\*\\do\\dobxpage\\dobypage\\dodhgt");
			writer.write(String.valueOf(zorder++));
			writer.write("\\dptxbx\\dpx");
			writer.write(String.valueOf(twip(printImage.getX() + leftPadding + globalOffsetX + getOffsetX())));
			writer.write("\\dpxsize");
			writer.write(String.valueOf(twip(availableImageWidth)));
			writer.write("\\dpy");
			writer.write(String.valueOf(twip(printImage.getY() + topPadding + globalOffsetY + getOffsetY())));
			writer.write("\\dpysize");
			writer.write(String.valueOf(twip(availableImageHeight)));
			writer.write("\\dpfillpat0\\dplinehollow{\\dptxbxtext {\\pict\\jpegblip\\picwgoal");
			writer.write(String.valueOf(twip(availableImageWidth)));
			writer.write("\\pichgoal");
			writer.write(String.valueOf(twip(availableImageHeight)));
			writer.write("\n");
			
			ByteArrayInputStream bais = new ByteArrayInputStream(JRImageLoader.loadImageDataFromAWTImage(bi, JRRenderable.IMAGE_TYPE_JPEG));

			int count = 0;
			int current = 0;
			while ((current = bais.read()) != -1)
			{
				String helperStr = Integer.toHexString(current);
				if (helperStr.length() < 2)
				{
					helperStr = "0" + helperStr;
				}
				writer.write(helperStr);
				count++;
				if (count == 64)
				{
					writer.write("\n");
					count = 0;
				}
			}

			writer.write("\n}}}\n");
		}

		if (
			printImage.getTopBorder() == JRGraphicElement.PEN_NONE &&
			printImage.getLeftBorder() == JRGraphicElement.PEN_NONE &&
			printImage.getBottomBorder() == JRGraphicElement.PEN_NONE &&
			printImage.getRightBorder() == JRGraphicElement.PEN_NONE
			)
		{
			if (printImage.getPen() != JRGraphicElement.PEN_NONE)
			{
				startGraphic("dprect", x, y, width, height);
				finishGraphic(printImage);
			}
		}
		else
		{
			exportBox(printImage, x, y, width, height, printImage.getForecolor(), printImage.getBackcolor());
		}
	}
	
	/**
	 * 
	 * @param frame
	 * @throws JRException
	 */
	protected void exportFrame(JRPrintFrame frame) throws JRException, IOException {
		int x = twip(frame.getX() + getOffsetX());
		int y = twip(frame.getY() + getOffsetY());
		int width = twip(frame.getWidth());
		int height = twip(frame.getHeight());
		
		if (frame.getMode() == JRElement.MODE_OPAQUE)
		{
			startGraphic("dprect", x, y, width, height);
			finishGraphic(JRGraphicElement.PEN_NONE, frame.getForecolor(),
					frame.getBackcolor(), 1);
		}
		
		setFrameElementsOffset(frame, false);
		exportElements(frame.getElements());
		restoreElementOffsets();
		
		exportBox(frame, x, y, width, height, frame.getForecolor(), frame.getBackcolor());		
	}
	
	
	protected void exportElements(Collection elements) throws JRException, IOException {
		if (elements != null && elements.size() > 0) {
			for (Iterator it = elements.iterator(); it.hasNext();) {
				JRPrintElement element = (JRPrintElement)it.next();
				if (element instanceof JRPrintLine) {
					exportLine((JRPrintLine)element);
				}
				else if (element instanceof JRPrintRectangle) {
					exportRectangle((JRPrintRectangle)element);
				}
				else if (element instanceof JRPrintEllipse) {
					exportEllipse((JRPrintEllipse)element);
				}
				else if (element instanceof JRPrintImage) {
					exportImage((JRPrintImage)element);
				}
				else if (element instanceof JRPrintText) {
					exportText((JRPrintText)element);
				}
				else if (element instanceof JRPrintFrame) {
					exportFrame((JRPrintFrame)element);
				}
			}
		}
	}
	
	/**
	 * Exports a JRBox that represents the border of a JasperReports object
	 * @param box
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @param fg
	 * @param bg
	 * @throws IOException
	 */
	private void exportBox(JRBox box, int x, int y, int width, int height, Color fg, Color bg) throws IOException{
		
		if (box.getTopBorder() != JRGraphicElement.PEN_NONE) {
			Color bc = box.getTopBorderColor();
			byte pen = box.getTopBorder();
			
			int a = getAdjustment(box.getTopBorder());
			if (bc == null) {
				bc = fg;
			}
			startGraphic("dpline", x, y + a, width, 0);
			finishGraphic(pen, bc, bg, 1);
			
		}
		if (box.getLeftBorder() != JRGraphicElement.PEN_NONE)
		{
			Color bc = box.getLeftBorderColor();
			byte pen = box.getLeftBorder();
			int a = getAdjustment(pen);
			if (bc == null)
				bc = fg;
			startGraphic("dpline", x + a, y, 0, height);
			finishGraphic(pen, bc, bg, 1);

		}

		if (box.getBottomBorder() != JRGraphicElement.PEN_NONE)
		{
			Color bc = box.getBottomBorderColor();
			byte pen = box.getBottomBorder();
			int a = getAdjustment(pen);
			if (bc == null)
				bc = fg;
			startGraphic("dpline", x, y + height - a, width, 0);
			finishGraphic(pen, bc, bg, 1);
		}

		if (box.getRightBorder() != JRGraphicElement.PEN_NONE)
		{
			Color bc = box.getRightBorderColor();
			byte pen = box.getRightBorder();
			int a = getAdjustment(pen);
			if (bc == null)
				bc = fg;
			startGraphic("dpline", x + width - a, y, 0, height);
			finishGraphic(pen, bc, bg, 1);
		}
	}
}
