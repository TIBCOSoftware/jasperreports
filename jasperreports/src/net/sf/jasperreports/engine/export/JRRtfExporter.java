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

/*
 * Contributors:
 * Matt Thompson - mthomp1234@users.sourceforge.net
 */
package net.sf.jasperreports.engine.export;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.font.TextAttribute;
import java.awt.geom.Dimension2D;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.text.AttributedCharacterIterator;
import java.text.AttributedCharacterIterator.Attribute;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import net.sf.jasperreports.engine.DefaultJasperReportsContext;
import net.sf.jasperreports.engine.JRAbstractExporter;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRFont;
import net.sf.jasperreports.engine.JRGenericElementType;
import net.sf.jasperreports.engine.JRGenericPrintElement;
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
import net.sf.jasperreports.engine.JRPropertiesUtil;
import net.sf.jasperreports.engine.JRWrappingSvgRenderer;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.Renderable;
import net.sf.jasperreports.engine.RenderableUtil;
import net.sf.jasperreports.engine.TabStop;
import net.sf.jasperreports.engine.base.JRBaseFont;
import net.sf.jasperreports.engine.base.JRBasePrintText;
import net.sf.jasperreports.engine.fonts.FontFamily;
import net.sf.jasperreports.engine.fonts.FontInfo;
import net.sf.jasperreports.engine.fonts.FontUtil;
import net.sf.jasperreports.engine.type.ImageTypeEnum;
import net.sf.jasperreports.engine.type.LineDirectionEnum;
import net.sf.jasperreports.engine.type.ModeEnum;
import net.sf.jasperreports.engine.type.OrientationEnum;
import net.sf.jasperreports.engine.type.RenderableTypeEnum;
import net.sf.jasperreports.engine.type.RunDirectionEnum;
import net.sf.jasperreports.engine.util.FileBufferedWriter;
import net.sf.jasperreports.engine.util.JRStyledText;
import net.sf.jasperreports.export.ExportInterruptedException;
import net.sf.jasperreports.export.ExporterInputItem;
import net.sf.jasperreports.export.RtfExporterConfiguration;
import net.sf.jasperreports.export.RtfReportConfiguration;
import net.sf.jasperreports.export.WriterExporterOutput;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Exports a JasperReports document to RTF format. 
 * <p/>
 * The {@link net.sf.jasperreports.engine.export.JRRtfExporter} implementation helps
 * to export JasperPrint documents in RTF format using RTF Specification 1.6. This
 * means that the RTF files produced by this exporter are compatible with Microsoft Word
 * 6.0, 2003 and XP.
 * <p/>
 * However, users might experience some problems when opening those RTF files with
 * OpenOffice or StarOffice, as these products are not perfectly compatible with the RTF
 * specifications from Microsoft.
 * <p/>
 * RTF is a character-based file format that supports absolute positioning of elements,
 * which means that this exporter produces output very similar to that of the <code>Graphics2D</code>
 * and PDF exporters. The {@link net.sf.jasperreports.export.RtfReportConfiguration} provides special 
 * configuration settings for this exporter.
 * <p/>
 * Almost all the provided samples show how to export to RTF.
 * 
 * @see net.sf.jasperreports.export.RtfReportConfiguration
 * @author Flavius Sana (flavius_sana@users.sourceforge.net)
 */
public class JRRtfExporter extends JRAbstractExporter<RtfReportConfiguration, RtfExporterConfiguration, WriterExporterOutput, JRRtfExporterContext>
{
	private static final Log log = LogFactory.getLog(JRRtfExporter.class);
	
	private static final String RTF_EXPORTER_PROPERTIES_PREFIX = JRPropertiesUtil.PROPERTY_PREFIX + "export.rtf.";
	
	public static final String EXCEPTION_MESSAGE_KEY_INVALID_TEXT_HEIGHT = "export.rtf.invalid.text.height";

	private static final int LINE_SPACING_FACTOR = 240; //(int)(240 * 2/3f);

	/**
	 * @deprecated Replaced by {@link RtfReportConfiguration#PROPERTY_IGNORE_HYPERLINK}.
	 */
	public static final String PROPERTY_IGNORE_HYPERLINK = RtfReportConfiguration.PROPERTY_IGNORE_HYPERLINK;

	/**
	 * The exporter key, as used in
	 * {@link GenericElementHandlerEnviroment#getElementHandler(JRGenericElementType, String)}.
	 */
	public static final String RTF_EXPORTER_KEY = JRPropertiesUtil.PROPERTY_PREFIX + "rtf";
	
	/**
	 *
	 */
	protected static final String JR_PAGE_ANCHOR_PREFIX = "JR_PAGE_ANCHOR_";

	protected FileBufferedWriter colorWriter;
	protected FileBufferedWriter fontWriter;
	protected FileBufferedWriter contentWriter;
	protected File destFile;

	protected int reportIndex;

	protected List<Color> colors;
	protected List<String> fonts;

	// z order of the graphical objects in .rtf file
	private int zorder = 1;

	protected class ExporterContext extends BaseExporterContext implements JRRtfExporterContext
	{
	}

	
	/**
	 * @see #JRRtfExporter(JasperReportsContext)
	 */
	public JRRtfExporter()
	{
		this(DefaultJasperReportsContext.getInstance());
	}

	
	/**
	 *
	 */
	public JRRtfExporter(JasperReportsContext jasperReportsContext)
	{
		super(jasperReportsContext);
		
		exporterContext = new ExporterContext();
	}


	/**
	 *
	 */
	protected Class<RtfExporterConfiguration> getConfigurationInterface()
	{
		return RtfExporterConfiguration.class;
	}


	/**
	 *
	 */
	protected Class<RtfReportConfiguration> getItemConfigurationInterface()
	{
		return RtfReportConfiguration.class;
	}
	

	/**
	 *
	 */
	@SuppressWarnings("deprecation")
	protected void ensureOutput()
	{
		if (exporterOutput == null)
		{
			exporterOutput = 
				new net.sf.jasperreports.export.parameters.ParametersWriterExporterOutput(
					getJasperReportsContext(),
					getParameters(),
					getCurrentJasperPrint()
					);
		}
	}
	

	/**
	 * Export report in .rtf format
	 */
	public void exportReport() throws JRException
	{
		/*   */
		ensureJasperReportsContext();
		ensureInput();

		fonts = new ArrayList<String>();
		colors = new ArrayList<Color>();
		colors.add(null);

		initExport();
		
		ensureOutput();
		
		Writer writer = getExporterOutput().getWriter();

		try
		{
			exportReportToWriter(writer);
		}
		catch (IOException e)
		{
			throw 
				new JRException(
					EXCEPTION_MESSAGE_KEY_OUTPUT_WRITER_ERROR,
					new Object[]{jasperPrint.getName()}, 
					e);
		}
		finally
		{
			getExporterOutput().close();
			resetExportContext();
		}
	}


	@Override
	protected void initExport()
	{
		super.initExport();
	}


	@Override
	protected void initReport()
	{
		super.initReport();
	}
	

	/**
	 * Export report in .rtf format to a stream
	 * @throws JRException
	 * @throws IOException
	 */
	protected void exportReportToWriter(Writer writer) throws JRException, IOException 
	{
		colorWriter = new FileBufferedWriter();
		fontWriter = new FileBufferedWriter();
		contentWriter = new FileBufferedWriter();

		List<ExporterInputItem> items = exporterInput.getItems();

		for(reportIndex = 0; reportIndex < items.size(); reportIndex++ )
		{
			ExporterInputItem item = items.get(reportIndex);

			setCurrentExporterInputItem(item);
			
			List<JRPrintPage> pages = jasperPrint.getPages();
			if (pages != null && pages.size() > 0)
			{
				PageRange pageRange = getPageRange();
				int startPageIndex = (pageRange == null || pageRange.getStartPageIndex() == null) ? 0 : pageRange.getStartPageIndex();
				int endPageIndex = (pageRange == null || pageRange.getEndPageIndex() == null) ? (pages.size() - 1) : pageRange.getEndPageIndex();

				contentWriter.write("{\\info{\\nofpages");
				contentWriter.write(String.valueOf(pages.size()));
				contentWriter.write("}}\n");

				contentWriter.write("\\viewkind1\\paperw");
				contentWriter.write(String.valueOf(LengthUtil.twip(jasperPrint.getPageWidth())));//FIXMEPART rtf does not work in batch mode
				contentWriter.write("\\paperh");
				contentWriter.write(String.valueOf(LengthUtil.twip(jasperPrint.getPageHeight())));

				contentWriter.write("\\marglsxn");
				contentWriter.write(String.valueOf(LengthUtil.twip(jasperPrint.getLeftMargin() == null ? 0 : jasperPrint.getLeftMargin())));
				contentWriter.write("\\margrsxn");
				contentWriter.write(String.valueOf(LengthUtil.twip(jasperPrint.getRightMargin() == null ? 0 : jasperPrint.getRightMargin())));
				contentWriter.write("\\margtsxn");
				contentWriter.write(String.valueOf(LengthUtil.twip(jasperPrint.getTopMargin() == null ? 0 : jasperPrint.getTopMargin())));
				contentWriter.write("\\margbsxn");
				contentWriter.write(String.valueOf(LengthUtil.twip(jasperPrint.getBottomMargin() == null ? 0 : jasperPrint.getBottomMargin())));
				contentWriter.write("\\deftab");
				contentWriter.write(String.valueOf(LengthUtil.twip(new JRBasePrintText(jasperPrint.getDefaultStyleProvider()).getParagraph().getTabStopWidth())));

				if (jasperPrint.getOrientationValue() == OrientationEnum.LANDSCAPE) {
					contentWriter.write("\\lndscpsxn");
				}


				for (int pageIndex = startPageIndex; pageIndex <= endPageIndex; pageIndex++) 
				{
					if (Thread.interrupted())
					{
						throw 
							new ExportInterruptedException();
					}

					JRPrintPage page = pages.get(pageIndex);

					contentWriter.write("\n");

					writeAnchor(JR_PAGE_ANCHOR_PREFIX + reportIndex + "_" + (pageIndex + 1));

					boolean lastPageFlag = false;
					if(pageIndex == endPageIndex && reportIndex == (items.size() - 1)){
						lastPageFlag = true;
					}
					exportPage(page, lastPageFlag);
				}
			}
		}
		contentWriter.write("}\n");

		contentWriter.close();
		colorWriter.close();
		fontWriter.close();
		
		// create the header of the rtf file
		writer.write("{\\rtf1\\ansi\\deff0\n");
		// create font and color tables
		writer.write("{\\fonttbl ");
		fontWriter.writeData(writer);
		writer.write("}\n");

		writer.write("{\\colortbl ;");
		colorWriter.writeData(writer);
		writer.write("}\n");

		contentWriter.writeData(writer);

		writer.flush();

		contentWriter.dispose();
		colorWriter.dispose();
		fontWriter.dispose();
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

		FontInfo fontInfo = FontUtil.getInstance(jasperReportsContext).getFontInfo(fontName, locale);
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
			contentWriter.write("\\page\n");
		}
		
		JRExportProgressMonitor progressMonitor = getCurrentItemConfiguration().getProgressMonitor();
		if (progressMonitor != null)
		{
			progressMonitor.afterPageExport();
		}
	}

	/**
	 *
	 */
	private void startElement(JRPrintElement element) throws IOException 
	{
		contentWriter.write("{\\shp\\shpbxpage\\shpbypage\\shpwr5\\shpfhdr0\\shpfblwtxt0\\shpz");
		contentWriter.write(String.valueOf(zorder++));
		contentWriter.write("\\shpleft");
		contentWriter.write(String.valueOf(LengthUtil.twip(element.getX() + getOffsetX())));
		contentWriter.write("\\shpright");
		contentWriter.write(String.valueOf(LengthUtil.twip(element.getX() + getOffsetX() + element.getWidth())));
		contentWriter.write("\\shptop");
		contentWriter.write(String.valueOf(LengthUtil.twip(element.getY() + getOffsetY())));
		contentWriter.write("\\shpbottom");
		contentWriter.write(String.valueOf(LengthUtil.twip(element.getY() + getOffsetY() + element.getHeight())));

		Color bgcolor = element.getBackcolor();

		if (element.getModeValue() == ModeEnum.OPAQUE)
		{
			contentWriter.write("{\\sp{\\sn fFilled}{\\sv 1}}");
			contentWriter.write("{\\sp{\\sn fillColor}{\\sv ");
			contentWriter.write(String.valueOf(getColorRGB(bgcolor)));
			contentWriter.write("}}");
		}
		else
		{
			contentWriter.write("{\\sp{\\sn fFilled}{\\sv 0}}");
		}
		
		contentWriter.write("{\\shpinst");
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
		contentWriter.write("}}\n");
	}

	/**
	 *
	 */
	private void exportPen(JRPen pen) throws IOException 
	{
		contentWriter.write("{\\sp{\\sn lineColor}{\\sv ");
		contentWriter.write(String.valueOf(getColorRGB(pen.getLineColor())));
		contentWriter.write("}}");

		float lineWidth = pen.getLineWidth().floatValue();
		
		if (lineWidth == 0f)
		{
			contentWriter.write("{\\sp{\\sn fLine}{\\sv 0}}");
		}

		switch (pen.getLineStyleValue())
		{
			case DOUBLE :
			{
				contentWriter.write("{\\sp{\\sn lineStyle}{\\sv 1}}");
				break;
			}
			case DOTTED :
			{
				contentWriter.write("{\\sp{\\sn lineDashing}{\\sv 2}}");
				break;
			}
			case DASHED :
			{
				contentWriter.write("{\\sp{\\sn lineDashing}{\\sv 1}}");
				break;
			}
		}

		contentWriter.write("{\\sp{\\sn lineWidth}{\\sv ");
		contentWriter.write(String.valueOf(LengthUtil.emu(lineWidth)));
		contentWriter.write("}}");
	}


	/**
	 *
	 */
	private void exportPen(Color color) throws IOException 
	{
		contentWriter.write("{\\sp{\\sn lineColor}{\\sv ");
		contentWriter.write(String.valueOf(getColorRGB(color)));
		contentWriter.write("}}");
		contentWriter.write("{\\sp{\\sn fLine}{\\sv 0}}");
		contentWriter.write("{\\sp{\\sn lineWidth}{\\sv 0}}");
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

		contentWriter.write("{\\shp\\shpbxpage\\shpbypage\\shpwr5\\shpfhdr0\\shpz");
		contentWriter.write(String.valueOf(zorder++));
		contentWriter.write("\\shpleft");
		contentWriter.write(String.valueOf(LengthUtil.twip(x)));
		contentWriter.write("\\shpright");
		contentWriter.write(String.valueOf(LengthUtil.twip(x + width)));
		contentWriter.write("\\shptop");
		contentWriter.write(String.valueOf(LengthUtil.twip(y)));
		contentWriter.write("\\shpbottom");
		contentWriter.write(String.valueOf(LengthUtil.twip(y + height)));

		contentWriter.write("{\\shpinst");
		
		contentWriter.write("{\\sp{\\sn shapeType}{\\sv 20}}");
		
		exportPen(line.getLinePen());
		
		if (line.getDirectionValue() == LineDirectionEnum.TOP_DOWN)
		{
			contentWriter.write("{\\sp{\\sn fFlipV}{\\sv 0}}");
		}
		else
		{
			contentWriter.write("{\\sp{\\sn fFlipV}{\\sv 1}}");
		}

		contentWriter.write("}}\n");
	}


	/**
	 *
	 */
	private void exportBorder(JRPen pen, float x, float y, float width, float height) throws IOException 
	{
		contentWriter.write("{\\shp\\shpbxpage\\shpbypage\\shpwr5\\shpfhdr0\\shpz");
		contentWriter.write(String.valueOf(zorder++));
		contentWriter.write("\\shpleft");
		contentWriter.write(String.valueOf(LengthUtil.twip(x)));//FIXMEBORDER starting point of borders seem to have CAP_SQUARE-like appearence at least for Thin
		contentWriter.write("\\shpright");
		contentWriter.write(String.valueOf(LengthUtil.twip(x + width)));
		contentWriter.write("\\shptop");
		contentWriter.write(String.valueOf(LengthUtil.twip(y)));
		contentWriter.write("\\shpbottom");
		contentWriter.write(String.valueOf(LengthUtil.twip(y + height)));

		contentWriter.write("{\\shpinst");
		
		contentWriter.write("{\\sp{\\sn shapeType}{\\sv 20}}");
		
		exportPen(pen);
		
		contentWriter.write("}}\n");
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
			contentWriter.write("{\\sp{\\sn shapeType}{\\sv 1}}");
		}
		else
		{
			contentWriter.write("{\\sp{\\sn shapeType}{\\sv 2}}");
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
		
		contentWriter.write("{\\sp{\\sn shapeType}{\\sv 3}}");

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
				throw 
					new JRException(
						EXCEPTION_MESSAGE_KEY_INVALID_TEXT_HEIGHT,  
						(Object[])null 
						);
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
				switch (text.getVerticalTextAlign())
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
				switch (text.getVerticalTextAlign())
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
				switch (text.getVerticalTextAlign())
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
				switch (text.getVerticalTextAlign())
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

		contentWriter.write(rotation);
		contentWriter.write("{\\sp{\\sn dyTextTop}{\\sv ");
		contentWriter.write(String.valueOf(LengthUtil.emu(topPadding)));
		contentWriter.write("}}");
		contentWriter.write("{\\sp{\\sn dxTextLeft}{\\sv ");
		contentWriter.write(String.valueOf(LengthUtil.emu(leftPadding)));
		contentWriter.write("}}");
		contentWriter.write("{\\sp{\\sn dyTextBottom}{\\sv ");
		contentWriter.write(String.valueOf(LengthUtil.emu(bottomPadding)));
		contentWriter.write("}}");
		contentWriter.write("{\\sp{\\sn dxTextRight}{\\sv ");
		contentWriter.write(String.valueOf(LengthUtil.emu(rightPadding)));
		contentWriter.write("}}");
		contentWriter.write("{\\sp{\\sn fLine}{\\sv 0}}");
		contentWriter.write("{\\shptxt{\\pard ");

		contentWriter.write("\\fi" + LengthUtil.twip(text.getParagraph().getFirstLineIndent().intValue()) + " ");
		contentWriter.write("\\li" + LengthUtil.twip(text.getParagraph().getLeftIndent().intValue()) + " ");
		contentWriter.write("\\ri" + LengthUtil.twip(text.getParagraph().getRightIndent().intValue()) + " ");
		contentWriter.write("\\sb" + LengthUtil.twip(text.getParagraph().getSpacingBefore().intValue()) + " ");
		contentWriter.write("\\sa" + LengthUtil.twip(text.getParagraph().getSpacingAfter().intValue()) + " ");

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

				contentWriter.write(tabStopAlign + "\\tx" + LengthUtil.twip(tabStop.getPosition()) + " ");
			}
		}

//		JRFont font = text;
		if (text.getRunDirectionValue() == RunDirectionEnum.RTL)
		{
			contentWriter.write("\\rtlch");
		}
//		writer.write("\\f");
//		writer.write(String.valueOf(getFontIndex(font)));
//		writer.write("\\cf");
//		writer.write(String.valueOf(getColorIndex(text.getForecolor())));
		contentWriter.write("\\cb");
		contentWriter.write(String.valueOf(getColorIndex(text.getBackcolor())));
		contentWriter.write(" ");

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

		switch (text.getHorizontalTextAlign())
		{
			case LEFT:
				contentWriter.write("\\ql");
				break;
			case CENTER:
				contentWriter.write("\\qc");
				break;
			case RIGHT:
				contentWriter.write("\\qr");
				break;
			case JUSTIFIED:
				contentWriter.write("\\qj");
				break;
			default:
				contentWriter.write("\\ql");
				break;
		}

		switch (text.getParagraph().getLineSpacing())
		{
			case AT_LEAST:
			{
				contentWriter.write("\\sl" + LengthUtil.twip(text.getParagraph().getLineSpacingSize().floatValue()));
				contentWriter.write(" \\slmult0 ");
				break;
			}
			case FIXED:
			{
				contentWriter.write("\\sl-" + LengthUtil.twip(text.getParagraph().getLineSpacingSize().floatValue()));
				contentWriter.write(" \\slmult0 ");
				break;
			}
			case PROPORTIONAL:
			{
				contentWriter.write("\\sl" + (int)(text.getParagraph().getLineSpacingSize().floatValue() * LINE_SPACING_FACTOR));
				contentWriter.write(" \\slmult1 ");
				break;
			}
			case DOUBLE:
			{
				contentWriter.write("\\sl" + (int)(2f * LINE_SPACING_FACTOR));
				contentWriter.write(" \\slmult1 ");
				break;
			}
			case ONE_AND_HALF:
			{
				contentWriter.write("\\sl" + (int)(1.5f * LINE_SPACING_FACTOR));
				contentWriter.write(" \\slmult1 ");
				break;
			}
			case SINGLE:
			default:
			{
				contentWriter.write("\\sl" + (int)(1f * LINE_SPACING_FACTOR));
				contentWriter.write(" \\slmult1 ");
				break;
			}
		}

		if (text.getAnchorName() != null)
		{
			writeAnchor(text.getAnchorName());
		}

		boolean startedHyperlink = exportHyperlink(text);

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

			contentWriter.write("\\f");
			contentWriter.write(String.valueOf(getFontIndex(styleFont, getTextLocale(text))));
			contentWriter.write("\\fs");
			contentWriter.write(String.valueOf((int)(2 * styleFont.getFontsize())));

			if (styleFont.isBold())
			{
				contentWriter.write("\\b");
			}
			if (styleFont.isItalic())
			{
				contentWriter.write("\\i");
			}
			if (styleFont.isUnderline())
			{
				contentWriter.write("\\ul");
			}
			if (styleFont.isStrikeThrough())
			{
				contentWriter.write("\\strike");
			}

			if (TextAttribute.SUPERSCRIPT_SUPER.equals(styledTextAttributes.get(TextAttribute.SUPERSCRIPT)))
			{
				contentWriter.write("\\super");
			}
			else if (TextAttribute.SUPERSCRIPT_SUB.equals(styledTextAttributes.get(TextAttribute.SUPERSCRIPT)))
			{
				contentWriter.write("\\sub");
			}

			if(!(null == styleBackground || styleBackground.equals(text.getBackcolor()))){
				contentWriter.write("\\highlight");
				contentWriter.write(String.valueOf(getColorIndex(styleBackground)));
			}
			contentWriter.write("\\cf");
			contentWriter.write(String.valueOf(getColorIndex(styleForeground)));
			contentWriter.write(" ");

			contentWriter.write(
				handleUnicodeText(
					plainText.substring(iterator.getIndex(), runLimit)					
					)
				);

			// reset all styles in the paragraph
			contentWriter.write("\\plain");

			iterator.setIndex(runLimit);
		}
		
		endHyperlink(startedHyperlink);

		contentWriter.write("\\par}}");
		
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

		Renderable renderer = printImage.getRenderable();

		if (
			renderer != null &&
			availableImageWidth > 0 &&
			availableImageHeight > 0
			)
		{
			if (renderer.getTypeValue() == RenderableTypeEnum.IMAGE)
			{
				// Image renderers are all asked for their image data at some point.
				// Better to test and replace the renderer now, in case of lazy load error.
				renderer = RenderableUtil.getInstance(jasperReportsContext).getOnErrorRendererForImageData(renderer, printImage.getOnErrorTypeValue());
			}
		}
		else
		{
			renderer = null;
		}

		if (renderer != null)
		{
			if (renderer.getTypeValue() == RenderableTypeEnum.SVG)
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
			Renderable tmpRenderer =
				RenderableUtil.getInstance(jasperReportsContext).getOnErrorRendererForDimension(renderer, printImage.getOnErrorTypeValue());
			Dimension2D dimension = tmpRenderer == null ? null : tmpRenderer.getDimension(jasperReportsContext);
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
					switch (printImage.getHorizontalImageAlign())
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
					switch (printImage.getVerticalImageAlign())
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
			boolean startedHyperlink = exportHyperlink(printImage);

			contentWriter.write("{\\shp{\\*\\shpinst\\shpbxpage\\shpbypage\\shpwr5\\shpfhdr0\\shpfblwtxt0\\shpz");
			contentWriter.write(String.valueOf(zorder++));
			contentWriter.write("\\shpleft");
			contentWriter.write(String.valueOf(LengthUtil.twip(printImage.getX() + leftPadding + xoffset + getOffsetX())));
			contentWriter.write("\\shpright");
			contentWriter.write(String.valueOf(LengthUtil.twip(printImage.getX() + leftPadding + xoffset + getOffsetX() + imageWidth)));
			contentWriter.write("\\shptop");
			contentWriter.write(String.valueOf(LengthUtil.twip(printImage.getY() + topPadding + yoffset + getOffsetY())));
			contentWriter.write("\\shpbottom");
			contentWriter.write(String.valueOf(LengthUtil.twip(printImage.getY() + topPadding + yoffset + getOffsetY() + imageHeight)));
			contentWriter.write("{\\sp{\\sn shapeType}{\\sv 75}}");
			contentWriter.write("{\\sp{\\sn fFilled}{\\sv 0}}");
			contentWriter.write("{\\sp{\\sn fLockAspectRatio}{\\sv 0}}");

			contentWriter.write("{\\sp{\\sn cropFromTop}{\\sv ");
			contentWriter.write(String.valueOf(cropTop));
			contentWriter.write("}}");
			contentWriter.write("{\\sp{\\sn cropFromLeft}{\\sv ");
			contentWriter.write(String.valueOf(cropLeft));
			contentWriter.write("}}");
			contentWriter.write("{\\sp{\\sn cropFromBottom}{\\sv ");
			contentWriter.write(String.valueOf(cropBottom));
			contentWriter.write("}}");
			contentWriter.write("{\\sp{\\sn cropFromRight}{\\sv ");
			contentWriter.write(String.valueOf(cropRight));
			contentWriter.write("}}");
			
			writeShapeHyperlink(printImage);

			if(printImage.getAnchorName() != null)
			{
				writeAnchor(printImage.getAnchorName());
			}
			
			contentWriter.write("{\\sp{\\sn pib}{\\sv {\\pict");
			if (renderer.getImageTypeValue() == ImageTypeEnum.JPEG)
			{
				contentWriter.write("\\jpegblip");
			}
			else
			{
				contentWriter.write("\\pngblip");
			}
			contentWriter.write("\n");

			ByteArrayInputStream bais = new ByteArrayInputStream(renderer.getImageData(jasperReportsContext));

			int count = 0;
			int current = 0;
			while ((current = bais.read()) != -1)
			{
				String helperStr = Integer.toHexString(current);
				if (helperStr.length() < 2)
				{
					helperStr = "0" + helperStr;
				}
				contentWriter.write(helperStr);
				count++;
				if (count == 64)
				{
					contentWriter.write("\n");
					count = 0;
				}
			}

			contentWriter.write("\n}}}");
			contentWriter.write("}}\n");
			endHyperlink(startedHyperlink);
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
	public void exportFrame(JRPrintFrame frame) throws JRException, IOException {
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
				GenericElementHandlerEnviroment.getInstance(getJasperReportsContext()).getElementHandler(
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

	
	protected boolean exportHyperlink(JRPrintHyperlink link) throws IOException
	{
		String hl = null;
		String local ="";
		boolean result = false;
		
		Boolean ignoreHyperlink = HyperlinkUtil.getIgnoreHyperlink(RtfReportConfiguration.PROPERTY_IGNORE_HYPERLINK, link);
		if (ignoreHyperlink == null)
		{
			ignoreHyperlink = getCurrentItemConfiguration().isIgnoreHyperlink();
		}

		if (!ignoreHyperlink)
		{
			JRHyperlinkProducer customHandler = getHyperlinkProducer(link);
			if (customHandler == null)
			{
				switch(link.getHyperlinkTypeValue())
				{
				case REFERENCE :
				{
					if (link.getHyperlinkReference() != null)
					{
						hl = link.getHyperlinkReference();
					}
					break;
				}
				case LOCAL_ANCHOR :
				{
					if (link.getHyperlinkAnchor() != null)
					{
						hl = link.getHyperlinkAnchor();
						local = "\\\\l ";
					}
					break;
				}
				case LOCAL_PAGE :
				{
					if (link.getHyperlinkPage() != null)
					{
						hl = JR_PAGE_ANCHOR_PREFIX + reportIndex + "_" + link.getHyperlinkPage().toString();
						local = "\\\\l ";
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
						hl = link.getHyperlinkReference() + "#" + link.getHyperlinkAnchor();
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
						hl = link.getHyperlinkReference() + "#" + JR_PAGE_ANCHOR_PREFIX + "0_" + link.getHyperlinkPage().toString();
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
				hl = customHandler.getHyperlink(link);
			}
		}
		
		if (hl != null)
		{
			contentWriter.write("{\\field{\\*\\fldinst HYPERLINK " + local + "\"" + hl + "\"}{\\fldrslt ");
			result = true;
		}
		return result;
	}
	
	protected void writeShapeHyperlink (JRPrintHyperlink link) throws IOException
	{
		String hlloc = null;
		String hlfr = null;
		String hlsrc = null;
		
		Boolean ignoreHyperlink = HyperlinkUtil.getIgnoreHyperlink(RtfReportConfiguration.PROPERTY_IGNORE_HYPERLINK, link);
		if (ignoreHyperlink == null)
		{
			ignoreHyperlink = getCurrentItemConfiguration().isIgnoreHyperlink();
		}

		if (!ignoreHyperlink)
		{
			JRHyperlinkProducer customHandler = getHyperlinkProducer(link);
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
		}

		if (hlfr != null)
		{
			contentWriter.write("{\\sp{\\sn fIsButton}{\\sv 1}}");
			contentWriter.write("{\\sp{\\sn pihlShape}{\\sv {\\*\\hl");
			contentWriter.write("{\\hlfr ");
			contentWriter.write(hlfr);
			contentWriter.write(" }");
			if (hlloc != null)
			{
				contentWriter.write("{\\hlloc ");
				contentWriter.write(hlloc);
				contentWriter.write(" }");
			}
			if (hlsrc != null)
			{
				contentWriter.write("{\\hlsrc ");
				contentWriter.write(hlsrc);
				contentWriter.write(" }");
			}
			contentWriter.write("}}}");
		}
	}


	protected void endHyperlink(boolean startedHyperlink) throws IOException
	{
		if(startedHyperlink)
		{
			contentWriter.write("}}");
		}
	}
	
	protected void writeAnchor(String anchorName) throws IOException
	{
		contentWriter.write("{\\*\\bkmkstart ");
		contentWriter.write(anchorName);
		contentWriter.write("}{\\*\\bkmkend ");
		contentWriter.write(anchorName);
		contentWriter.write("}");
	}

	private float getXAlignFactor(JRPrintImage image)
	{
		float xalignFactor = 0f;
		switch (image.getHorizontalImageAlign())
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
		switch (image.getVerticalImageAlign())
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
	public String getExporterKey()
	{
		return RTF_EXPORTER_KEY;
	}
	
	/**
	 * 
	 */
	public String getExporterPropertiesPrefix()
	{
		return RTF_EXPORTER_PROPERTIES_PREFIX;
	}
}
