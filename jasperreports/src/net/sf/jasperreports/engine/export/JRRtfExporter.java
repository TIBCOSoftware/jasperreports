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

/*
 * Contributors:
 * Matt Thompson - mthomp1234@users.sourceforge.net
 */
package net.sf.jasperreports.engine.export;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.font.TextAttribute;
import java.awt.geom.Dimension2D;
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
import java.text.AttributedCharacterIterator.Attribute;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import net.sf.jasperreports.engine.JRAbstractExporter;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JRFont;
import net.sf.jasperreports.engine.JRGenericPrintElement;
import net.sf.jasperreports.engine.JRImageRenderer;
import net.sf.jasperreports.engine.JRLineBox;
import net.sf.jasperreports.engine.JRPen;
import net.sf.jasperreports.engine.JRPrintElement;
import net.sf.jasperreports.engine.JRPrintEllipse;
import net.sf.jasperreports.engine.JRPrintFrame;
import net.sf.jasperreports.engine.JRPrintHyperlink;
import net.sf.jasperreports.engine.JRPrintImage;
import net.sf.jasperreports.engine.JRPrintLine;
import net.sf.jasperreports.engine.JRPrintPage;
import net.sf.jasperreports.engine.JRPrintRectangle;
import net.sf.jasperreports.engine.JRPrintText;
import net.sf.jasperreports.engine.JRRenderable;
import net.sf.jasperreports.engine.JRWrappingSvgRenderer;
import net.sf.jasperreports.engine.TabStop;
import net.sf.jasperreports.engine.base.JRBaseFont;
import net.sf.jasperreports.engine.base.JRBasePrintText;
import net.sf.jasperreports.engine.fonts.FontFamily;
import net.sf.jasperreports.engine.fonts.FontInfo;
import net.sf.jasperreports.engine.type.LineDirectionEnum;
import net.sf.jasperreports.engine.type.ModeEnum;
import net.sf.jasperreports.engine.type.OrientationEnum;
import net.sf.jasperreports.engine.type.RunDirectionEnum;
import net.sf.jasperreports.engine.util.FileBufferedWriter;
import net.sf.jasperreports.engine.util.JRFontUtil;
import net.sf.jasperreports.engine.util.JRProperties;
import net.sf.jasperreports.engine.util.JRStyledText;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Exports a JasperReports document to RTF format. It has binary output type and exports the document to
 * a free-form layout. It uses the RTF Specification 1.6 (compatible with MS Word 6.0, 2003 and XP).
 * 
 * @author Flavius Sana (flavius_sana@users.sourceforge.net)
 * @version $Id$
 */
public class JRRtfExporter extends JRAbstractExporter
{
	private static final Log log = LogFactory.getLog(JRRtfExporter.class);
	
	private static final String RTF_EXPORTER_PROPERTIES_PREFIX = JRProperties.PROPERTY_PREFIX + "export.rtf.";
	
	private static final int LINE_SPACING_FACTOR = 240; //(int)(240 * 2/3f);

	/**
	 * The exporter key, as used in
	 * {@link GenericElementHandlerEnviroment#getHandler(net.sf.jasperreports.engine.JRGenericElementType, String)}.
	 */
	public static final String RTF_EXPORTER_KEY = JRProperties.PROPERTY_PREFIX + "rtf";
	
	/**
	 *
	 */
	protected static final String JR_PAGE_ANCHOR_PREFIX = "JR_PAGE_ANCHOR_";
	protected JRExportProgressMonitor progressMonitor;

	protected FileBufferedWriter colorWriter;
	protected FileBufferedWriter fontWriter;
	protected FileBufferedWriter writer;
	protected Writer rtfWriter;
	protected File destFile;

	protected int reportIndex;

	protected List<Color> colors;
	protected List<String> fonts;

	// z order of the graphical objects in .rtf file
	private int zorder = 1;

	/**
	 * @deprecated
	 */
	private Map<String,String> fontMap;

	protected class ExporterContext extends BaseExporterContext implements JRRtfExporterContext
	{
		public String getExportPropertiesPrefix()
		{
			return RTF_EXPORTER_PROPERTIES_PREFIX;
		}
	}
	
	protected JRRtfExporterContext exporterContext = new ExporterContext();

	
	/**
	 * Export report in .rtf format
	 */
	public void exportReport() throws JRException
	{
		progressMonitor = (JRExportProgressMonitor)parameters.get(JRExporterParameter.PROGRESS_MONITOR);

		/*   */
		setOffset();

		try
		{
			/*   */
			setExportContext();

			/*   */
			setInput();
			
			if (!parameters.containsKey(JRExporterParameter.FILTER))
			{
				filter = createFilter(RTF_EXPORTER_PROPERTIES_PREFIX);
			}

			if (!isModeBatch) {
				setPageRange();
			}

			fonts = new ArrayList<String>();
			colors = new ArrayList<Color>();
			colors.add(null);

			fontMap = (Map<String,String>) parameters.get(JRExporterParameter.FONT_MAP);
			setHyperlinkProducerFactory();
			StringBuffer sb = (StringBuffer)parameters.get(JRExporterParameter.OUTPUT_STRING_BUFFER);
			if (sb != null) {
				StringBuffer buffer = exportReportToBuffer();
				sb.append(buffer.toString());
			}
			else {
				Writer outWriter = (Writer)parameters.get(JRExporterParameter.OUTPUT_WRITER);
				if (outWriter != null) {
					try {
						rtfWriter = outWriter;

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
							rtfWriter = new OutputStreamWriter(os);

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
		rtfWriter = buffer;
		try {
			exportReportToStream();
		}
		catch (IOException ex) {
			throw new JRException("Error while exporting report to the buffer", ex);
		}

		return buffer.getBuffer();
	}


	/**
	 * Export report in .rtf format to a stream
	 * @throws JRException
	 * @throws IOException
	 */
	protected void exportReportToStream() throws JRException, IOException 
	{
		colorWriter = new FileBufferedWriter();
		fontWriter = new FileBufferedWriter();
		writer = new FileBufferedWriter();

		for(reportIndex = 0; reportIndex < jasperPrintList.size(); reportIndex++ ){
			setJasperPrint(jasperPrintList.get(reportIndex));

			List<JRPrintPage> pages = jasperPrint.getPages();
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
				writer.write(String.valueOf(LengthUtil.twip(jasperPrint.getPageWidth())));
				writer.write("\\paperh");
				writer.write(String.valueOf(LengthUtil.twip(jasperPrint.getPageHeight())));

				writer.write("\\marglsxn");
				writer.write(String.valueOf(LengthUtil.twip(jasperPrint.getLeftMargin() == null ? 0 : jasperPrint.getLeftMargin())));
				writer.write("\\margrsxn");
				writer.write(String.valueOf(LengthUtil.twip(jasperPrint.getRightMargin() == null ? 0 : jasperPrint.getRightMargin())));
				writer.write("\\margtsxn");
				writer.write(String.valueOf(LengthUtil.twip(jasperPrint.getTopMargin() == null ? 0 : jasperPrint.getTopMargin())));
				writer.write("\\margbsxn");
				writer.write(String.valueOf(LengthUtil.twip(jasperPrint.getBottomMargin() == null ? 0 : jasperPrint.getBottomMargin())));
				writer.write("\\deftab");
				writer.write(String.valueOf(LengthUtil.twip(new JRBasePrintText(jasperPrint.getDefaultStyleProvider()).getParagraph().getTabStopWidth())));

				if (jasperPrint.getOrientationValue() == OrientationEnum.LANDSCAPE) {
					writer.write("\\lndscpsxn");
				}


				for (int pageIndex = startPageIndex; pageIndex <= endPageIndex; pageIndex++) {
					writer.write("\n");
					if(Thread.interrupted()){
						throw new JRException("Current thread interrupted");
					}

					page = pages.get(pageIndex);
					writeAnchor(JR_PAGE_ANCHOR_PREFIX + reportIndex + "_" + (pageIndex + 1));

					boolean lastPageFlag = false;
					if(pageIndex == endPageIndex && reportIndex == (jasperPrintList.size() - 1)){
						lastPageFlag = true;
					}
					exportPage(page, lastPageFlag);
				}
			}
		}
		writer.write("}\n");

		writer.close();
		colorWriter.close();
		fontWriter.close();
		
		// create the header of the rtf file
		rtfWriter.write("{\\rtf1\\ansi\\deff0\n");
		// create font and color tables
		rtfWriter.write("{\\fonttbl ");
		fontWriter.writeData(rtfWriter);
		rtfWriter.write("}\n");

		rtfWriter.write("{\\colortbl ;");
		colorWriter.writeData(rtfWriter);
		rtfWriter.write("}\n");

		writer.writeData(rtfWriter);

		rtfWriter.flush();

		writer.dispose();
		colorWriter.dispose();
		fontWriter.dispose();
	}


	/**
	 * Export report to a file in the .rtf format
	 */
	protected void exportReportToFile() throws JRException {
		try {
			OutputStream fileOutputStream = new FileOutputStream(destFile);
			rtfWriter = new BufferedWriter(new OutputStreamWriter(fileOutputStream));
			exportReportToStream();
		}
		catch (IOException ex) {
			throw new JRException("Error writing to the file : " + destFile, ex);
		}
		finally {
			if(rtfWriter != null) {
				try {
					rtfWriter.close();
				}
				catch(IOException ex) {

				}
			}
		}
	}


	/**
	 * Return color index from header of the .rtf file. If a color is not
	 * found is automatically added to the header of the rtf file. The
	 * method is called first when the header of the .rtf file is constructed
	 * and when a component needs a color for foreground or background
	 * @param color Color for which the index is required.
	 * @return index of the color from .rtf file header
	 */
	private int getColorIndex(Color color) throws IOException
	{
		int colorNdx = colors.indexOf(color);
		if (colorNdx < 0)
		{
			colorNdx = colors.size();
			colors.add(color);
			colorWriter.write(
				"\\red" + color.getRed() 
				+ "\\green" + color.getGreen() 
				+ "\\blue" + color.getBlue() + ";"
				);
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
	private int getFontIndex(JRFont font, Locale locale) throws IOException
	{
		String fontName = font.getFontName();
		if(fontMap != null && fontMap.containsKey(fontName))
		{
			fontName = fontMap.get(fontName);
		}
		else
		{
			FontInfo fontInfo = JRFontUtil.getFontInfo(fontName, locale);
			if (fontInfo != null)
			{
				//fontName found in font extensions
				FontFamily family = fontInfo.getFontFamily();
				String exportFont = family.getExportFont(getExporterKey());
				if (exportFont != null)
				{
					fontName = exportFont;
				}
			}
		}

		int fontIndex = fonts.indexOf(fontName);

		if(fontIndex < 0) {
			fontIndex = fonts.size();
			fonts.add(fontName);
			fontWriter.write("{\\f"  + fontIndex + "\\fnil " + fontName + ";}");
		}

		return fontIndex;
	}


	/**
	 * Exports a report page
	 * @param page Page that will be exported
	 * @throws JRException
	 */
	protected void exportPage(JRPrintPage page, boolean lastPage) throws JRException, IOException
	{
		exportElements(page.getElements());

		if(!lastPage)
		{
			writer.write("\\page\n");
		}
	}

	/**
	 *
	 */
	private void startElement(JRPrintElement element) throws IOException 
	{
		writer.write("{\\shp\\shpbxpage\\shpbypage\\shpwr5\\shpfhdr0\\shpfblwtxt0\\shpz");
		writer.write(String.valueOf(zorder++));
		writer.write("\\shpleft");
		writer.write(String.valueOf(LengthUtil.twip(element.getX() + getOffsetX())));
		writer.write("\\shpright");
		writer.write(String.valueOf(LengthUtil.twip(element.getX() + getOffsetX() + element.getWidth())));
		writer.write("\\shptop");
		writer.write(String.valueOf(LengthUtil.twip(element.getY() + getOffsetY())));
		writer.write("\\shpbottom");
		writer.write(String.valueOf(LengthUtil.twip(element.getY() + getOffsetY() + element.getHeight())));

		Color bgcolor = element.getBackcolor();

		if (element.getModeValue() == ModeEnum.OPAQUE)
		{
			writer.write("{\\sp{\\sn fFilled}{\\sv 1}}");
			writer.write("{\\sp{\\sn fillColor}{\\sv ");
			writer.write(String.valueOf(getColorRGB(bgcolor)));
			writer.write("}}");
		}
		else
		{
			writer.write("{\\sp{\\sn fFilled}{\\sv 0}}");
		}
		
		writer.write("{\\shpinst");
	}

	/**
	 *
	 */
	private int getColorRGB(Color color) 
	{
		return color.getRed() + 256 * color.getGreen() + 65536 * color.getBlue();
	}

	/**
	 *
	 */
	private void finishElement() throws IOException 
	{
		writer.write("}}\n");
	}

	/**
	 *
	 */
	private void exportPen(JRPen pen) throws IOException 
	{
		writer.write("{\\sp{\\sn lineColor}{\\sv ");
		writer.write(String.valueOf(getColorRGB(pen.getLineColor())));
		writer.write("}}");

		float lineWidth = pen.getLineWidth().floatValue();
		
		if (lineWidth == 0f)
		{
			writer.write("{\\sp{\\sn fLine}{\\sv 0}}");
		}

		switch (pen.getLineStyleValue())
		{
			case DOUBLE :
			{
				writer.write("{\\sp{\\sn lineStyle}{\\sv 1}}");
				break;
			}
			case DOTTED :
			{
				writer.write("{\\sp{\\sn lineDashing}{\\sv 2}}");
				break;
			}
			case DASHED :
			{
				writer.write("{\\sp{\\sn lineDashing}{\\sv 1}}");
				break;
			}
		}

		writer.write("{\\sp{\\sn lineWidth}{\\sv ");
		writer.write(String.valueOf(LengthUtil.emu(lineWidth)));
		writer.write("}}");
	}


	/**
	 *
	 */
	private void exportPen(Color color) throws IOException 
	{
		writer.write("{\\sp{\\sn lineColor}{\\sv ");
		writer.write(String.valueOf(getColorRGB(color)));
		writer.write("}}");
		writer.write("{\\sp{\\sn fLine}{\\sv 0}}");
		writer.write("{\\sp{\\sn lineWidth}{\\sv 0}}");
	}


	/**
	 * Draw a line object
	 * @param line JasperReports line object - JRPrintLine
	 * @throws IOException
	 */
	protected void exportLine(JRPrintLine line) throws IOException 
	{
		int x = line.getX() + getOffsetX();
		int y = line.getY() + getOffsetY();
		int height = line.getHeight();
		int width = line.getWidth();

		if (width <= 1 || height <= 1)
		{
			if (width > 1)
			{
				height = 0;
			}
			else
			{
				width = 0;
			}
		}

		writer.write("{\\shp\\shpbxpage\\shpbypage\\shpwr5\\shpfhdr0\\shpz");
		writer.write(String.valueOf(zorder++));
		writer.write("\\shpleft");
		writer.write(String.valueOf(LengthUtil.twip(x)));
		writer.write("\\shpright");
		writer.write(String.valueOf(LengthUtil.twip(x + width)));
		writer.write("\\shptop");
		writer.write(String.valueOf(LengthUtil.twip(y)));
		writer.write("\\shpbottom");
		writer.write(String.valueOf(LengthUtil.twip(y + height)));

		writer.write("{\\shpinst");
		
		writer.write("{\\sp{\\sn shapeType}{\\sv 20}}");
		
		exportPen(line.getLinePen());
		
		if (line.getDirectionValue() == LineDirectionEnum.TOP_DOWN)
		{
			writer.write("{\\sp{\\sn fFlipV}{\\sv 0}}");
		}
		else
		{
			writer.write("{\\sp{\\sn fFlipV}{\\sv 1}}");
		}

		writer.write("}}\n");
	}


	/**
	 *
	 */
	private void exportBorder(JRPen pen, float x, float y, float width, float height) throws IOException 
	{
		writer.write("{\\shp\\shpbxpage\\shpbypage\\shpwr5\\shpfhdr0\\shpz");
		writer.write(String.valueOf(zorder++));
		writer.write("\\shpleft");
		writer.write(String.valueOf(LengthUtil.twip(x)));//FIXMEBORDER starting point of borders seem to have CAP_SQUARE-like appearence at least for Thin
		writer.write("\\shpright");
		writer.write(String.valueOf(LengthUtil.twip(x + width)));
		writer.write("\\shptop");
		writer.write(String.valueOf(LengthUtil.twip(y)));
		writer.write("\\shpbottom");
		writer.write(String.valueOf(LengthUtil.twip(y + height)));

		writer.write("{\\shpinst");
		
		writer.write("{\\sp{\\sn shapeType}{\\sv 20}}");
		
		exportPen(pen);
		
		writer.write("}}\n");
	}


	/**
	 * Draw a rectangle
	 * @param rectangle JasperReports rectangle object (JRPrintRectangle)
	 */
	protected void exportRectangle(JRPrintRectangle rectangle) throws IOException 
	{
		startElement(rectangle);
		
		if (rectangle.getRadius() == 0)
		{
			writer.write("{\\sp{\\sn shapeType}{\\sv 1}}");
		}
		else
		{
			writer.write("{\\sp{\\sn shapeType}{\\sv 2}}");
		}

		exportPen(rectangle.getLinePen());
		
		finishElement();
	}


	/**
	 * Draw a ellipse object
	 * @param ellipse JasperReports ellipse object (JRPrintElipse)
	 */
	protected void exportEllipse(JRPrintEllipse ellipse) throws IOException 
	{
		startElement(ellipse);
		
		writer.write("{\\sp{\\sn shapeType}{\\sv 3}}");

		exportPen(ellipse.getLinePen());
		
		finishElement();
	}


	/**
	 * Draw a text box
	 * @param text JasperReports text object (JRPrintText)
	 * @throws JRException
	 */
	public void exportText(JRPrintText text) throws IOException, JRException {


		// use styled text
		JRStyledText styledText = getStyledText(text);
		if (styledText == null)
		{
			return;
		}

		int width = text.getWidth();
		int height = text.getHeight();

		int textHeight = (int)text.getTextHeight();

		if(textHeight <= 0) {
			if(height <= 0 ){
				throw new JRException("Invalid text height");
			}
			textHeight = height;
		}

		/*   */
		startElement(text);

		// padding for the text
		int topPadding = text.getLineBox().getTopPadding().intValue();
		int leftPadding = text.getLineBox().getLeftPadding().intValue();
		int bottomPadding = text.getLineBox().getBottomPadding().intValue();
		int rightPadding = text.getLineBox().getRightPadding().intValue();

		String rotation = null;

		switch (text.getRotationValue())
		{
			case LEFT :
			{
				switch (text.getVerticalAlignmentValue())
				{
					case TOP:
					{
						break;
					}
					case MIDDLE:
					{
						leftPadding = Math.max(leftPadding, (width - rightPadding - textHeight) / 2);
						break;
					}
					case BOTTOM:
					{
						leftPadding = Math.max(leftPadding, width - rightPadding - textHeight);
						break;
					}
				}
				rotation = "{\\sp{\\sn txflTextFlow}{\\sv 2}}";
				break;
			}
			case RIGHT :
			{
				switch (text.getVerticalAlignmentValue())
				{
					case TOP:
					{
						break;
					}
					case MIDDLE:
					{
						rightPadding = Math.max(rightPadding, (width - leftPadding - textHeight) / 2);
						break;
					}
					case BOTTOM:
					{
						rightPadding = Math.max(rightPadding, width - leftPadding - textHeight);
						break;
					}
				}
				rotation = "{\\sp{\\sn txflTextFlow}{\\sv 3}}";
				break;
			}
			case UPSIDE_DOWN :
			{
				switch (text.getVerticalAlignmentValue())
				{
					case TOP:
					{
						topPadding = Math.max(topPadding, height - bottomPadding - textHeight);
						break;
					}
					case MIDDLE:
					{
						topPadding = Math.max(topPadding, (height - bottomPadding - textHeight) / 2);
						break;
					}
					case BOTTOM:
					{
						break;
					}
				}
				rotation = "";
				break;
			}
			case NONE :
			default :
			{
				switch (text.getVerticalAlignmentValue())
				{
					case TOP:
					{
						break;
					}
					case MIDDLE:
					{
						topPadding = Math.max(topPadding, (height - bottomPadding - textHeight) / 2);
						break;
					}
					case BOTTOM:
					{
						topPadding = Math.max(topPadding, height - bottomPadding - textHeight);
						break;
					}
				}
				rotation = "";
			}
		}

		writer.write(rotation);
		writer.write("{\\sp{\\sn dyTextTop}{\\sv ");
		writer.write(String.valueOf(LengthUtil.emu(topPadding)));
		writer.write("}}");
		writer.write("{\\sp{\\sn dxTextLeft}{\\sv ");
		writer.write(String.valueOf(LengthUtil.emu(leftPadding)));
		writer.write("}}");
		writer.write("{\\sp{\\sn dyTextBottom}{\\sv ");
		writer.write(String.valueOf(LengthUtil.emu(bottomPadding)));
		writer.write("}}");
		writer.write("{\\sp{\\sn dxTextRight}{\\sv ");
		writer.write(String.valueOf(LengthUtil.emu(rightPadding)));
		writer.write("}}");
		writer.write("{\\sp{\\sn fLine}{\\sv 0}}");
		writer.write("{\\shptxt{\\pard ");

		writer.write("\\fi" + LengthUtil.twip(text.getParagraph().getFirstLineIndent().intValue()) + " ");
		writer.write("\\li" + LengthUtil.twip(text.getParagraph().getLeftIndent().intValue()) + " ");
		writer.write("\\ri" + LengthUtil.twip(text.getParagraph().getRightIndent().intValue()) + " ");
		writer.write("\\sb" + LengthUtil.twip(text.getParagraph().getSpacingBefore().intValue()) + " ");
		writer.write("\\sa" + LengthUtil.twip(text.getParagraph().getSpacingAfter().intValue()) + " ");

		TabStop[] tabStops = text.getParagraph().getTabStops();
		if (tabStops != null && tabStops.length > 0)
		{
			for (int i = 0; i < tabStops.length; i++)
			{
				TabStop tabStop = tabStops[i];

				String tabStopAlign = "";
				
				switch (tabStop.getAlignment())
				{
					case CENTER:
						tabStopAlign = "\\tqc";
						break;
					case RIGHT:
						tabStopAlign = "\\tqr";
						break;
					case LEFT:
					default:
						tabStopAlign = "";
						break;
				}

				writer.write(tabStopAlign + "\\tx" + LengthUtil.twip(tabStop.getPosition()) + " ");
			}
		}

//		JRFont font = text;
		if (text.getRunDirectionValue() == RunDirectionEnum.RTL)
		{
			writer.write("\\rtlch");
		}
//		writer.write("\\f");
//		writer.write(String.valueOf(getFontIndex(font)));
//		writer.write("\\cf");
//		writer.write(String.valueOf(getColorIndex(text.getForecolor())));
		writer.write("\\cb");
		writer.write(String.valueOf(getColorIndex(text.getBackcolor())));
		writer.write(" ");

//		if (font.isBold())
//			writer.write("\\b");
//		if (font.isItalic())
//			writer.write("\\i");
//		if (font.isStrikeThrough())
//			writer.write("\\strike");
//		if (font.isUnderline())
//			writer.write("\\ul");
//		writer.write("\\fs");
//		writer.write(String.valueOf(font.getFontSize() * 2));

		switch (text.getHorizontalAlignmentValue())
		{
			case LEFT:
				writer.write("\\ql");
				break;
			case CENTER:
				writer.write("\\qc");
				break;
			case RIGHT:
				writer.write("\\qr");
				break;
			case JUSTIFIED:
				writer.write("\\qj");
				break;
			default:
				writer.write("\\ql");
				break;
		}

		switch (text.getParagraph().getLineSpacing())
		{
			case AT_LEAST:
			{
				writer.write("\\sl" + LengthUtil.twip(text.getParagraph().getLineSpacingSize().floatValue()));
				writer.write(" \\slmult0 ");
				break;
			}
			case FIXED:
			{
				writer.write("\\sl-" + LengthUtil.twip(text.getParagraph().getLineSpacingSize().floatValue()));
				writer.write(" \\slmult0 ");
				break;
			}
			case PROPORTIONAL:
			{
				writer.write("\\sl" + (int)(text.getParagraph().getLineSpacingSize().floatValue() * LINE_SPACING_FACTOR));
				writer.write(" \\slmult1 ");
				break;
			}
			case DOUBLE:
			{
				writer.write("\\sl" + (int)(2f * LINE_SPACING_FACTOR));
				writer.write(" \\slmult1 ");
				break;
			}
			case ONE_AND_HALF:
			{
				writer.write("\\sl" + (int)(1.5f * LINE_SPACING_FACTOR));
				writer.write(" \\slmult1 ");
				break;
			}
			case SINGLE:
			default:
			{
				writer.write("\\sl" + (int)(1f * LINE_SPACING_FACTOR));
				writer.write(" \\slmult1 ");
				break;
			}
		}

		if (text.getAnchorName() != null)
		{
			writeAnchor(text.getAnchorName());
		}

		exportHyperlink(text);

		// add parameters in case of styled text element
		String plainText = styledText.getText();
		int runLimit = 0;

		AttributedCharacterIterator iterator = styledText.getAttributedString().getIterator();
		while (
			runLimit < styledText.length()
			&& (runLimit = iterator.getRunLimit()) <= styledText.length()
			)
		{

			Map<Attribute,Object> styledTextAttributes = iterator.getAttributes();
			JRFont styleFont = new JRBaseFont(styledTextAttributes);
			Color styleForeground = (Color) styledTextAttributes.get(TextAttribute.FOREGROUND);
			Color styleBackground = (Color) styledTextAttributes.get(TextAttribute.BACKGROUND);

			writer.write("\\f");
			writer.write(String.valueOf(getFontIndex(styleFont, getTextLocale(text))));
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

			if(!(null == styleBackground || styleBackground.equals(text.getBackcolor()))){
				writer.write("\\highlight");
				writer.write(String.valueOf(getColorIndex(styleBackground)));
			}
			writer.write("\\cf");
			writer.write(String.valueOf(getColorIndex(styleForeground)));
			writer.write(" ");

			writer.write(
				handleUnicodeText(
					plainText.substring(iterator.getIndex(), runLimit)					
					)
				);

			// reset all styles in the paragraph
			writer.write("\\plain");

			iterator.setIndex(runLimit);
		}
//		if (startedHyperlink)
//		{
//			endHyperlink();
//		}

		writer.write("\\par}}");
		
		/*   */
		finishElement();

		exportBox(text.getLineBox(), text.getX() + getOffsetX(), text.getY() + getOffsetY(), width, height);
	}


	/**
	 * Replace Unicode characters with RTF Unicode control words
	 * @param source source text
	 * @return text with Unicode characters replaced
	 */
	private String handleUnicodeText(String sourceText)
	{
		StringBuffer unicodeText = new StringBuffer();
		
		for(int i = 0; i < sourceText.length(); i++ )
		{
			long ch = sourceText.charAt(i);
			if(ch > 127)
			{
				unicodeText.append("\\u" + ch + '?');
			}
			else if(ch == '\n')
			{
				unicodeText.append("\\line ");
			}
			else if(ch == '\\' || ch =='{' || ch =='}')
			{
				unicodeText.append('\\').append((char)ch);
			}
			else
			{
				unicodeText.append((char)ch);
			}
		}

		return unicodeText.toString();
	}


	/**
	 * Export a image object
	 * @param printImage JasperReports image object (JRPrintImage)
	 * @throws JRException
	 * @throws IOException
	 */
	public void exportImage(JRPrintImage printImage) throws JRException, IOException
	{
		int leftPadding = printImage.getLineBox().getLeftPadding().intValue();
		int topPadding = printImage.getLineBox().getTopPadding().intValue();
		int rightPadding = printImage.getLineBox().getRightPadding().intValue();
		int bottomPadding = printImage.getLineBox().getBottomPadding().intValue();

		int availableImageWidth = printImage.getWidth() - leftPadding - rightPadding;
		availableImageWidth = availableImageWidth < 0 ? 0 : availableImageWidth;

		int availableImageHeight = printImage.getHeight() - topPadding - bottomPadding;
		availableImageHeight = availableImageHeight < 0 ? 0 : availableImageHeight;

		JRRenderable renderer = printImage.getRenderer();

		if (
			renderer != null &&
			availableImageWidth > 0 &&
			availableImageHeight > 0
			)
		{
			if (renderer.getType() == JRRenderable.TYPE_IMAGE)
			{
				// Image renderers are all asked for their image data at some point.
				// Better to test and replace the renderer now, in case of lazy load error.
				renderer = JRImageRenderer.getOnErrorRendererForImageData(renderer, printImage.getOnErrorTypeValue());
			}
		}
		else
		{
			renderer = null;
		}

		if (renderer != null)
		{
			if (renderer.getType() == JRRenderable.TYPE_SVG)
			{
				renderer =
					new JRWrappingSvgRenderer(
						renderer,
						new Dimension(printImage.getWidth(), printImage.getHeight()),
						ModeEnum.OPAQUE == printImage.getModeValue() ? printImage.getBackcolor() : null
						);
			}

			int normalWidth = availableImageWidth;
			int normalHeight = availableImageHeight;

			// Image load might fail.
			JRRenderable tmpRenderer =
				JRImageRenderer.getOnErrorRendererForDimension(renderer, printImage.getOnErrorTypeValue());
			Dimension2D dimension = tmpRenderer == null ? null : tmpRenderer.getDimension();
			// If renderer was replaced, ignore image dimension.
			if (tmpRenderer == renderer && dimension != null)
			{
				normalWidth = (int) dimension.getWidth();
				normalHeight = (int) dimension.getHeight();
			}

			int imageWidth = 0;
			int imageHeight = 0;
			int xoffset = 0;
			int yoffset = 0;
			int cropTop = 0;
			int cropLeft = 0;
			int cropBottom = 0;
			int cropRight = 0;

			switch (printImage.getScaleImageValue())
			{
				case CLIP:
				{
					switch (printImage.getHorizontalAlignmentValue())
					{
						case RIGHT :
						{
							cropLeft = 65536 * (- availableImageWidth + normalWidth) / availableImageWidth;
							cropRight = 0;
							break;
						}
						case CENTER :
						{
							cropLeft = 65536 * (- availableImageWidth + normalWidth) / availableImageWidth / 2;
							cropRight = cropLeft;
							break;
						}
						case LEFT :
						default :
						{
							cropLeft = 0;
							cropRight = 65536 * (- availableImageWidth + normalWidth) / availableImageWidth;
							break;
						}
					}
					switch (printImage.getVerticalAlignmentValue())
					{
						case TOP :
						{
							cropTop = 0;
							cropBottom = 65536 * (- availableImageHeight + normalHeight) / normalHeight;
							break;
						}
						case MIDDLE :
						{
							cropTop = 65536 * (- availableImageHeight + normalHeight) / normalHeight / 2;
							cropBottom = cropTop;
							break;
						}
						case BOTTOM :
						default :
						{
							cropTop = 65536 * (- availableImageHeight + normalHeight) / normalHeight;
							cropBottom = 0;
							break;
						}
					}
					imageWidth = availableImageWidth;
					imageHeight = availableImageHeight;
					break;
				}
				case FILL_FRAME:
				{
					normalWidth = availableImageWidth;
					normalHeight = availableImageHeight;
					imageWidth = availableImageWidth;
					imageHeight = availableImageHeight;
					break;
				}
				case RETAIN_SHAPE:
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

						xoffset = (int) (getXAlignFactor(printImage) * (availableImageWidth - normalWidth));
						yoffset = (int) (getYAlignFactor(printImage) * (availableImageHeight - normalHeight));
						imageWidth = normalWidth;
						imageHeight = normalHeight;
					}

					break;
				}
			}

			startElement(printImage);
			exportPen(printImage.getForecolor());//FIXMEBORDER should we have lineColor here, if at all needed?
			finishElement();
			
			writer.write("{\\shp{\\*\\shpinst\\shpbxpage\\shpbypage\\shpwr5\\shpfhdr0\\shpfblwtxt0\\shpz");
			writer.write(String.valueOf(zorder++));
			writer.write("\\shpleft");
			writer.write(String.valueOf(LengthUtil.twip(printImage.getX() + leftPadding + xoffset + getOffsetX())));
			writer.write("\\shpright");
			writer.write(String.valueOf(LengthUtil.twip(printImage.getX() + leftPadding + xoffset + getOffsetX() + imageWidth)));
			writer.write("\\shptop");
			writer.write(String.valueOf(LengthUtil.twip(printImage.getY() + topPadding + yoffset + getOffsetY())));
			writer.write("\\shpbottom");
			writer.write(String.valueOf(LengthUtil.twip(printImage.getY() + topPadding + yoffset + getOffsetY() + imageHeight)));
			writer.write("{\\sp{\\sn shapeType}{\\sv 75}}");
			writer.write("{\\sp{\\sn fFilled}{\\sv 0}}");
			writer.write("{\\sp{\\sn fLockAspectRatio}{\\sv 0}}");

			writer.write("{\\sp{\\sn cropFromTop}{\\sv ");
			writer.write(String.valueOf(cropTop));
			writer.write("}}");
			writer.write("{\\sp{\\sn cropFromLeft}{\\sv ");
			writer.write(String.valueOf(cropLeft));
			writer.write("}}");
			writer.write("{\\sp{\\sn cropFromBottom}{\\sv ");
			writer.write(String.valueOf(cropBottom));
			writer.write("}}");
			writer.write("{\\sp{\\sn cropFromRight}{\\sv ");
			writer.write(String.valueOf(cropRight));
			writer.write("}}");

			if(printImage.getAnchorName() != null)
			{
				writeAnchor(printImage.getAnchorName());
			}
			
			exportHyperlink(printImage);
			
			writer.write("{\\sp{\\sn pib}{\\sv {\\pict");
			if (renderer.getImageType() == JRRenderable.IMAGE_TYPE_JPEG)
			{
				writer.write("\\jpegblip");
			}
			else
			{
				writer.write("\\pngblip");
			}
			writer.write("\n");

			ByteArrayInputStream bais = new ByteArrayInputStream(renderer.getImageData());

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

			writer.write("\n}}}");
			writer.write("}}\n");
		}

		int x = printImage.getX() + getOffsetX();
		int y = printImage.getY() + getOffsetY();
		int width = printImage.getWidth();
		int height = printImage.getHeight();

		if (
			printImage.getLineBox().getTopPen().getLineWidth().floatValue() <= 0f &&
			printImage.getLineBox().getLeftPen().getLineWidth().floatValue() <= 0f &&
			printImage.getLineBox().getBottomPen().getLineWidth().floatValue() <= 0f &&
			printImage.getLineBox().getRightPen().getLineWidth().floatValue() <= 0f
			)
		{
			if (printImage.getLinePen().getLineWidth().floatValue() > 0f)
			{
				exportPen(printImage.getLinePen(), x, y, width, height);
			}
		}
		else
		{
			exportBox(printImage.getLineBox(), x, y, width, height);
		}
	}

	/**
	 *
	 * @param frame
	 * @throws JRException
	 */
	protected void exportFrame(JRPrintFrame frame) throws JRException, IOException {
		int x = frame.getX() + getOffsetX();
		int y = frame.getY() + getOffsetY();
		int width = frame.getWidth();
		int height = frame.getHeight();

		startElement(frame);
		
		exportPen(frame.getForecolor());
		
		finishElement();

		setFrameElementsOffset(frame, false);
		exportElements(frame.getElements());
		restoreElementOffsets();

		exportBox(frame.getLineBox(), x, y, width, height);
	}


	protected void exportElements(Collection<JRPrintElement> elements) throws JRException, IOException {
		if (elements != null && elements.size() > 0) {
			for (Iterator<JRPrintElement> it = elements.iterator(); it.hasNext();) {
				JRPrintElement element = it.next();
				if (filter == null || filter.isToExport(element)) {
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
					else if (element instanceof JRGenericPrintElement) {
						exportGenericElement((JRGenericPrintElement)element);
					}
				}
			}
		}
	}

	/**
	 *
	 */
	private void exportBox(JRLineBox box, int x, int y, int width, int height) throws IOException
	{
		exportTopPen(box.getTopPen(), box.getLeftPen(), box.getRightPen(), x, y, width, height);
		exportLeftPen(box.getTopPen(), box.getLeftPen(), box.getBottomPen(), x, y, width, height);
		exportBottomPen(box.getLeftPen(), box.getBottomPen(), box.getRightPen(), x, y, width, height);
		exportRightPen(box.getTopPen(), box.getBottomPen(), box.getRightPen(), x, y, width, height);
	}

	/**
	 *
	 */
	private void exportPen(JRPen pen, int x, int y, int width, int height) throws IOException
	{
		exportTopPen(pen, pen, pen, x, y, width, height);
		exportLeftPen(pen, pen, pen, x, y, width, height);
		exportBottomPen(pen, pen, pen, x, y, width, height);
		exportRightPen(pen, pen, pen, x, y, width, height);
	}

	/**
	 *
	 */
	private void exportTopPen(
		JRPen topPen, 
		JRPen leftPen, 
		JRPen rightPen, 
		int x, 
		int y, 
		int width, 
		int height
		) throws IOException
	{
		if (topPen.getLineWidth().floatValue() > 0f) 
		{
			exportBorder(
				topPen, 
				x - leftPen.getLineWidth().floatValue() / 2, 
				y, 
				width + (leftPen.getLineWidth().floatValue() + rightPen.getLineWidth().floatValue()) / 2, 
				0
				);
			//exportBorder(topPen, x, y + getAdjustment(topPen), width, 0);
		}
	}

	/**
	 *
	 */
	private void exportLeftPen(
		JRPen topPen, 
		JRPen leftPen, 
		JRPen bottomPen, 
		int x, 
		int y, 
		int width, 
		int height
		) throws IOException
	{
		if (leftPen.getLineWidth().floatValue() > 0f) 
		{
			exportBorder(
				leftPen, 
				x, 
				y - topPen.getLineWidth().floatValue() / 2, 
				0, 
				height + (topPen.getLineWidth().floatValue() + bottomPen.getLineWidth().floatValue()) / 2
				);
			//exportBorder(leftPen, x + getAdjustment(leftPen), y, 0, height);
		}
	}

	/**
	 *
	 */
	private void exportBottomPen(
		JRPen leftPen, 
		JRPen bottomPen, 
		JRPen rightPen, 
		int x, 
		int y, 
		int width, 
		int height
		) throws IOException
	{
		if (bottomPen.getLineWidth().floatValue() > 0f) 
		{
			exportBorder(
				bottomPen, 
				x - leftPen.getLineWidth().floatValue() / 2, 
				y + height, 
				width + (leftPen.getLineWidth().floatValue() + rightPen.getLineWidth().floatValue()) / 2, 
				0
				);
			//exportBorder(bottomPen, x, y + height - getAdjustment(bottomPen), width, 0);
		}
	}

	/**
	 *
	 */
	private void exportRightPen(
		JRPen topPen, 
		JRPen bottomPen, 
		JRPen rightPen, 
		int x, 
		int y, 
		int width, 
		int height
		) throws IOException
	{
		if (rightPen.getLineWidth().floatValue() > 0f) 
		{
			exportBorder(
				rightPen, 
				x + width, 
				y - topPen.getLineWidth().floatValue() / 2, 
				0, 
				height + (topPen.getLineWidth().floatValue() + bottomPen.getLineWidth().floatValue()) / 2
				);
			//exportBorder(rightPen, x + width - getAdjustment(rightPen), y, 0, height);
		}
	}


	protected void exportGenericElement(JRGenericPrintElement element)
	{
		GenericElementRtfHandler handler = (GenericElementRtfHandler) 
				GenericElementHandlerEnviroment.getHandler(
						element.getGenericType(), RTF_EXPORTER_KEY);
		
		if (handler != null)
		{
			handler.exportElement(exporterContext, element);
		}
		else
		{
			if (log.isDebugEnabled())
			{
				log.debug("No RTF generic element handler for " 
						+ element.getGenericType());
			}
		}
	}

	
	protected void exportHyperlink(JRPrintHyperlink link) throws IOException
	{
		String hlloc = null;
		String hlfr = null;
		String hlsrc = null;
		
		JRHyperlinkProducer customHandler = getCustomHandler(link);
		if (customHandler == null)
		{
			switch(link.getHyperlinkTypeValue())
			{
				case REFERENCE :
				{
					if (link.getHyperlinkReference() != null)
					{
						hlsrc = link.getHyperlinkReference();
						hlfr = hlsrc;
					}
					break;
				}
				case LOCAL_ANCHOR :
				{
					if (link.getHyperlinkAnchor() != null)
					{
						hlloc = link.getHyperlinkAnchor();
						hlfr = hlloc;
					}
					break;
				}
				case LOCAL_PAGE :
				{
					if (link.getHyperlinkPage() != null)
					{
						hlloc = JR_PAGE_ANCHOR_PREFIX + reportIndex + "_" + link.getHyperlinkPage().toString();
						hlfr = hlloc;
					}
					break;
				}
				case REMOTE_ANCHOR :
				{
					if (
						link.getHyperlinkReference() != null &&
						link.getHyperlinkAnchor() != null
						)
					{
						hlsrc = link.getHyperlinkReference() + "#" + link.getHyperlinkAnchor();
						hlfr = hlsrc;
					}
					break;
				}
				case REMOTE_PAGE :
				{
					if (
						link.getHyperlinkReference() != null &&
						link.getHyperlinkPage() != null
						)
					{
						hlsrc = link.getHyperlinkReference() + "#" + JR_PAGE_ANCHOR_PREFIX + "0_" + link.getHyperlinkPage().toString();
						hlfr = hlsrc;
					}
					break;
				}
				case NONE :
				default :
				{
					break;
				}
			}
		}
		else
		{
			hlsrc = customHandler.getHyperlink(link);
			hlfr = hlsrc;
		}

		if (hlfr != null)
		{
			writer.write("{\\sp{\\sn fIsButton}{\\sv 1}}");
			writer.write("{\\sp{\\sn pihlShape}{\\sv {\\*\\hl");
			writer.write("{\\hlfr ");
			writer.write(hlfr);
			writer.write(" }");
			if (hlloc != null)
			{
				writer.write("{\\hlloc ");
				writer.write(hlloc);
				writer.write(" }");
			}
			if (hlsrc != null)
			{
				writer.write("{\\hlsrc ");
				writer.write(hlsrc);
				writer.write(" }");
			}
			writer.write("}}}");
		}
	}


	protected void writeAnchor(String anchorName) throws IOException
	{
		writer.write("{\\*\\bkmkstart ");
		writer.write(anchorName);
		writer.write("}{\\*\\bkmkend ");
		writer.write(anchorName);
		writer.write("}");
	}

	private float getXAlignFactor(JRPrintImage image)
	{
		float xalignFactor = 0f;
		switch (image.getHorizontalAlignmentValue())
		{
			case RIGHT :
			{
				xalignFactor = 1f;
				break;
			}
			case CENTER :
			{
				xalignFactor = 0.5f;
				break;
			}
			case LEFT :
			default :
			{
				xalignFactor = 0f;
				break;
			}
		}
		return xalignFactor;
	}

	private float getYAlignFactor(JRPrintImage image)
	{
		float yalignFactor = 0f;
		switch (image.getVerticalAlignmentValue())
		{
			case BOTTOM :
			{
				yalignFactor = 1f;
				break;
			}
			case MIDDLE :
			{
				yalignFactor = 0.5f;
				break;
			}
			case TOP :
			default :
			{
				yalignFactor = 0f;
				break;
			}
		}
		return yalignFactor;
	}

	/**
	 *
	 */
	protected String getExporterKey()
	{
		return RTF_EXPORTER_KEY;
	}
}
