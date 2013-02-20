/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2013 Jaspersoft Corporation. All rights reserved.
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
 * Alex Parfenov - aparfeno@users.sourceforge.net
 * Adrian Jackson - iapetus@users.sourceforge.net
 * David Taylor - exodussystems@users.sourceforge.net
 * Lars Kristensen - llk@users.sourceforge.net
 */

package net.sf.jasperreports.engine.export;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.font.TextAttribute;
import java.awt.geom.Dimension2D;
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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;
import java.util.Map;

import net.sf.jasperreports.engine.DefaultJasperReportsContext;
import net.sf.jasperreports.engine.ImageMapRenderable;
import net.sf.jasperreports.engine.JRAbstractExporter;
import net.sf.jasperreports.engine.JRBoxContainer;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JRGenericPrintElement;
import net.sf.jasperreports.engine.JRImageRenderer;
import net.sf.jasperreports.engine.JRLineBox;
import net.sf.jasperreports.engine.JRPen;
import net.sf.jasperreports.engine.JRPrintElement;
import net.sf.jasperreports.engine.JRPrintElementIndex;
import net.sf.jasperreports.engine.JRPrintEllipse;
import net.sf.jasperreports.engine.JRPrintFrame;
import net.sf.jasperreports.engine.JRPrintGraphicElement;
import net.sf.jasperreports.engine.JRPrintHyperlink;
import net.sf.jasperreports.engine.JRPrintHyperlinkParameter;
import net.sf.jasperreports.engine.JRPrintImage;
import net.sf.jasperreports.engine.JRPrintImageArea;
import net.sf.jasperreports.engine.JRPrintImageAreaHyperlink;
import net.sf.jasperreports.engine.JRPrintLine;
import net.sf.jasperreports.engine.JRPrintPage;
import net.sf.jasperreports.engine.JRPrintRectangle;
import net.sf.jasperreports.engine.JRPrintText;
import net.sf.jasperreports.engine.JRPropertiesUtil;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.JRWrappingSvgRenderer;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.Renderable;
import net.sf.jasperreports.engine.RenderableUtil;
import net.sf.jasperreports.engine.base.JRBasePrintText;
import net.sf.jasperreports.engine.fonts.FontFamily;
import net.sf.jasperreports.engine.fonts.FontInfo;
import net.sf.jasperreports.engine.fonts.FontUtil;
import net.sf.jasperreports.engine.type.HyperlinkTypeEnum;
import net.sf.jasperreports.engine.type.LineDirectionEnum;
import net.sf.jasperreports.engine.type.LineSpacingEnum;
import net.sf.jasperreports.engine.type.LineStyleEnum;
import net.sf.jasperreports.engine.type.ModeEnum;
import net.sf.jasperreports.engine.type.RenderableTypeEnum;
import net.sf.jasperreports.engine.type.RotationEnum;
import net.sf.jasperreports.engine.type.RunDirectionEnum;
import net.sf.jasperreports.engine.type.ScaleImageEnum;
import net.sf.jasperreports.engine.util.JRBoxUtil;
import net.sf.jasperreports.engine.util.JRColorUtil;
import net.sf.jasperreports.engine.util.JRStringUtil;
import net.sf.jasperreports.engine.util.JRStyledText;
import net.sf.jasperreports.engine.util.JRTextAttribute;
import net.sf.jasperreports.engine.util.Pair;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * Exports a JasperReports document to XHTML format.

 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JRXhtmlExporter extends JRAbstractExporter
{
	private static final Log log = LogFactory.getLog(JRXhtmlExporter.class);
	
	private static final String XHTML_EXPORTER_PROPERTIES_PREFIX = JRPropertiesUtil.PROPERTY_PREFIX + "export.xhtml.";

	public static final String PROPERTY_XHTML_HYPERLINK_VISIBLE = XHTML_EXPORTER_PROPERTIES_PREFIX + JRPrintHyperlink.PROPERTY_HYPERLINK_VISIBLE_SUFFIX;

	/**
	 * The exporter key, as used in
	 * {@link GenericElementHandlerEnviroment#getHandler(net.sf.jasperreports.engine.JRGenericElementType, String)}.
	 */
	public static final String XHTML_EXPORTER_KEY = JRPropertiesUtil.PROPERTY_PREFIX + "xhtml";
	
	/**
	 *
	 */
	protected static final String JR_PAGE_ANCHOR_PREFIX = "JR_PAGE_ANCHOR_";

	protected static final float DEFAULT_ZOOM = 1f;

	/**
	 *
	 */
	protected static final String CSS_TEXT_ALIGN_LEFT = "left";
	protected static final String CSS_TEXT_ALIGN_RIGHT = "right";
	protected static final String CSS_TEXT_ALIGN_CENTER = "center";
	protected static final String CSS_TEXT_ALIGN_JUSTIFY = "justify";

	/**
	 *
	 */
	protected static final String HTML_VERTICAL_ALIGN_TOP = "top";
	protected static final String HTML_VERTICAL_ALIGN_MIDDLE = "middle";
	protected static final String HTML_VERTICAL_ALIGN_BOTTOM = "bottom";
	
	public static final String IMAGE_NAME_PREFIX = "img_";
	protected static final int IMAGE_NAME_PREFIX_LEGTH = IMAGE_NAME_PREFIX.length();

	protected class ExporterContext extends BaseExporterContext implements JRHtmlExporterContext
	{
		public String getExportPropertiesPrefix()
		{
			return XHTML_EXPORTER_PROPERTIES_PREFIX;
		}

		public String getHyperlinkURL(JRPrintHyperlink link)
		{
			return JRXhtmlExporter.this.getHyperlinkURL(link);
		}
	}

	/**
	 *
	 */
	protected Writer writer;
	protected JRExportProgressMonitor progressMonitor;
	protected Map<String,String> rendererToImagePathMap;
	protected Map<Pair<String,Rectangle>,String> imageMaps;
	protected Map<String,byte[]> imageNameToImageDataMap;
	protected List<JRPrintElementIndex> imagesToProcess;
	
	protected int reportIndex;
	protected int pageIndex;
	protected List<FrameInfo> frameInfoStack;
	protected int elementIndex;
	protected int topLimit;
	protected int leftLimit;
	protected int rightLimit;
	protected int bottomLimit;

	/**
	 *
	 */
	protected File imagesDir;
	protected String imagesURI;
	protected boolean isOutputImagesToDir;
	protected boolean isWhitePageBackground;
	protected String encoding;
	protected String sizeUnit;
	protected float zoom = DEFAULT_ZOOM;
	protected boolean isWrapBreakWord;
	protected boolean isIgnorePageMargins;

	/**
	 *
	 */
	protected String htmlHeader;
	protected String betweenPagesHtml;
	protected String htmlFooter;


	protected JRHyperlinkTargetProducerFactory targetProducerFactory;		

	protected boolean hyperlinkStarted;	
	
	protected JRHtmlExporterContext exporterContext = new ExporterContext();

	/**
	 * @see #JRXhtmlExporter(JasperReportsContext)
	 */
	public JRXhtmlExporter()
	{
		this(DefaultJasperReportsContext.getInstance());
	}

	
	/**
	 *
	 */
	public JRXhtmlExporter(JasperReportsContext jasperReportsContext)
	{
		super(jasperReportsContext);

		targetProducerFactory = new DefaultHyperlinkTargetProducerFactory(jasperReportsContext);		
	}
	

	/**
	 *
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
				filter = createFilter(XHTML_EXPORTER_PROPERTIES_PREFIX);
			}

			/*   */
			if (!isModeBatch)
			{
				setPageRange();
			}
	
			htmlHeader = (String)parameters.get(JRHtmlExporterParameter.HTML_HEADER);
			betweenPagesHtml = (String)parameters.get(JRHtmlExporterParameter.BETWEEN_PAGES_HTML);
			htmlFooter = (String)parameters.get(JRHtmlExporterParameter.HTML_FOOTER);
	
			imagesDir = (File)parameters.get(JRHtmlExporterParameter.IMAGES_DIR);
			if (imagesDir == null)
			{
				String dir = (String)parameters.get(JRHtmlExporterParameter.IMAGES_DIR_NAME);
				if (dir != null)
				{
					imagesDir = new File(dir);
				}
			}
	
			isWhitePageBackground = 
				getBooleanParameter(
					JRHtmlExporterParameter.IS_WHITE_PAGE_BACKGROUND,
					JRHtmlExporterParameter.PROPERTY_WHITE_PAGE_BACKGROUND,
					true
					);
	
			Boolean isOutputImagesToDirParameter = (Boolean)parameters.get(JRHtmlExporterParameter.IS_OUTPUT_IMAGES_TO_DIR);
			if (isOutputImagesToDirParameter != null)
			{
				isOutputImagesToDir = isOutputImagesToDirParameter.booleanValue();
			}
	
			String uri = (String)parameters.get(JRHtmlExporterParameter.IMAGES_URI);
			if (uri != null)
			{
				imagesURI = uri;
			}
	
			encoding = 
				getStringParameterOrDefault(
					JRExporterParameter.CHARACTER_ENCODING, 
					JRExporterParameter.PROPERTY_CHARACTER_ENCODING
					);

			rendererToImagePathMap = new HashMap<String,String>();
			imageMaps = new HashMap<Pair<String,Rectangle>,String>();
			imagesToProcess = new ArrayList<JRPrintElementIndex>();
	
			//backward compatibility with the IMAGE_MAP parameter
			imageNameToImageDataMap = (Map<String,byte[]>)parameters.get(JRHtmlExporterParameter.IMAGES_MAP);
	//		if (imageNameToImageDataMap == null)
	//		{
	//			imageNameToImageDataMap = new HashMap();
	//		}
			//END - backward compatibility with the IMAGE_MAP parameter

			isWrapBreakWord = 
				getBooleanParameter(
					JRHtmlExporterParameter.IS_WRAP_BREAK_WORD,
					JRHtmlExporterParameter.PROPERTY_WRAP_BREAK_WORD,
					false
					);
	
			sizeUnit = 
				getStringParameterOrDefault(
					JRHtmlExporterParameter.SIZE_UNIT,
					JRHtmlExporterParameter.PROPERTY_SIZE_UNIT
					);
			
			Float zoomRatio = (Float)parameters.get(JRHtmlExporterParameter.ZOOM_RATIO);
			if (zoomRatio != null)
			{
				zoom = zoomRatio.floatValue();
				if (zoom <= 0)
				{
					throw new JRException("Invalid zoom ratio : " + zoom);
				}
			}
			else
			{
				zoom = DEFAULT_ZOOM;
			}
	
			isIgnorePageMargins = 
				getBooleanParameter(
					JRExporterParameter.IGNORE_PAGE_MARGINS,
					JRExporterParameter.PROPERTY_IGNORE_PAGE_MARGINS,
					false
					);
			
			setFontMap();
						
			setHyperlinkProducerFactory();
			
			StringBuffer sb = (StringBuffer)parameters.get(JRExporterParameter.OUTPUT_STRING_BUFFER);
			if (sb != null)
			{
				try
				{
					writer = new StringWriter();
					exportReportToWriter();
					sb.append(writer.toString());
				}
				catch (IOException e)
				{
					throw new JRException("Error writing to StringBuffer writer : " + jasperPrint.getName(), e);
				}
				finally
				{
					if (writer != null)
					{
						try
						{
							writer.close();
						}
						catch(IOException e)
						{
						}
					}
				}
			}
			else
			{
				writer = (Writer)parameters.get(JRExporterParameter.OUTPUT_WRITER);
				if (writer != null)
				{
					try
					{
						exportReportToWriter();
					}
					catch (IOException e)
					{
						throw new JRException("Error writing to writer : " + jasperPrint.getName(), e);
					}
				}
				else
				{
					OutputStream os = (OutputStream)parameters.get(JRExporterParameter.OUTPUT_STREAM);
					if (os != null)
					{
						try
						{
							writer = new OutputStreamWriter(os, encoding);
							exportReportToWriter();
						}
						catch (IOException e)
						{
							throw new JRException("Error writing to OutputStream writer : " + jasperPrint.getName(), e);
						}
					}
					else
					{
						File destFile = (File)parameters.get(JRExporterParameter.OUTPUT_FILE);
						if (destFile == null)
						{
							String fileName = (String)parameters.get(JRExporterParameter.OUTPUT_FILE_NAME);
							if (fileName != null)
							{
								destFile = new File(fileName);
							}
							else
							{
								throw new JRException("No output specified for the exporter.");
							}
						}
	
						try
						{
							os = new FileOutputStream(destFile);
							writer = new OutputStreamWriter(os, encoding);
						}
						catch (IOException e)
						{
							throw new JRException("Error creating to file writer : " + jasperPrint.getName(), e);
						}
	
						if (imagesDir == null)
						{
							imagesDir = new File(destFile.getParent(), destFile.getName() + "_files");
						}
	
						if (isOutputImagesToDirParameter == null)
						{
							isOutputImagesToDir = true;
						}
	
						if (imagesURI == null)
						{
							imagesURI = imagesDir.getName() + "/";
						}
	
						try
						{
							exportReportToWriter();
						}
						catch (IOException e)
						{
							throw new JRException("Error writing to file writer : " + jasperPrint.getName(), e);
						}
						finally
						{
							if (writer != null)
							{
								try
								{
									writer.close();
								}
								catch(IOException e)
								{
								}
							}
						}
					}
				}
			}
	
			if (isOutputImagesToDir)
			{
				if (imagesDir == null)
				{
					throw new JRException("The images directory was not specified for the exporter.");
				}
	
				if (imagesToProcess != null && imagesToProcess.size() > 0)
				{
					if (!imagesDir.exists())
					{
						imagesDir.mkdir();
					}
	
					for(Iterator<JRPrintElementIndex> it = imagesToProcess.iterator(); it.hasNext();)
					{
						JRPrintElementIndex imageIndex = it.next();
	
						JRPrintImage image = getImage(jasperPrintList, imageIndex);
						Renderable renderer = image.getRenderable();
						if (renderer.getTypeValue() == RenderableTypeEnum.SVG)
						{
							renderer =
								new JRWrappingSvgRenderer(
									renderer,
									new Dimension(image.getWidth(), image.getHeight()),
									ModeEnum.OPAQUE == image.getModeValue() ? image.getBackcolor() : null
									);
						}
	
						byte[] imageData = renderer.getImageData(jasperReportsContext);
	
						File imageFile = new File(imagesDir, getImageName(imageIndex));
						FileOutputStream fos = null;
	
						try
						{
							fos = new FileOutputStream(imageFile);
							fos.write(imageData, 0, imageData.length);
						}
						catch (IOException e)
						{
							throw new JRException("Error writing to image file : " + imageFile, e);
						}
						finally
						{
							if (fos != null)
							{
								try
								{
									fos.close();
								}
								catch(IOException e)
								{
								}
							}
						}
					}
				}
			}
		}
		finally
		{
			resetExportContext();
		}
	}


	public static JRPrintImage getImage(List<JasperPrint> jasperPrintList, String imageName)
	{
		return getImage(jasperPrintList, getPrintElementIndex(imageName));
	}


	public static JRPrintImage getImage(List<JasperPrint> jasperPrintList, JRPrintElementIndex imageIndex)
	{
		JasperPrint report = jasperPrintList.get(imageIndex.getReportIndex());
		JRPrintPage page = report.getPages().get(imageIndex.getPageIndex());

		Integer[] elementIndexes = imageIndex.getAddressArray();
		Object element = page.getElements().get(elementIndexes[0].intValue());

		for (int i = 1; i < elementIndexes.length; ++i)
		{
			JRPrintFrame frame = (JRPrintFrame) element;
			element = frame.getElements().get(elementIndexes[i].intValue());
		}

		return (JRPrintImage) element;
	}


	/**
	 *
	 */
	protected void exportReportToWriter() throws JRException, IOException
	{
		if (htmlHeader == null)
		{
			writer.write("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n");
			writer.write("<html>\n");
			writer.write("<head>\n");
			writer.write("  <title></title>\n");
			writer.write("  <meta http-equiv=\"Content-Type\" content=\"text/html; charset=" + encoding + "\"/>\n");
			writer.write("  <style type=\"text/css\">\n");
			writer.write("    a {text-decoration: none}\n");
			writer.write("  </style>\n");
			writer.write("</head>\n");
			writer.write("<body text=\"#000000\" link=\"#000000\" alink=\"#000000\" vlink=\"#000000\">\n");
			writer.write("<table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\n");
			writer.write("<tr><td width=\"50%\">&nbsp;</td><td align=\"center\">\n");
			writer.write("\n");
		}
		else
		{
			writer.write(htmlHeader);
		}

		for(reportIndex = 0; reportIndex < jasperPrintList.size(); reportIndex++)
		{
			setJasperPrint(jasperPrintList.get(reportIndex));

			List<JRPrintPage> pages = jasperPrint.getPages();
			if (pages != null && pages.size() > 0)
			{
				if (isModeBatch)
				{
					startPageIndex = 0;
					endPageIndex = pages.size() - 1;
				}

				JRPrintPage page = null;
				for(pageIndex = startPageIndex; pageIndex <= endPageIndex; pageIndex++)
				{
					if (Thread.interrupted())
					{
						throw new JRException("Current thread interrupted.");
					}

					page = pages.get(pageIndex);

					writer.write("<a name=\"" + JR_PAGE_ANCHOR_PREFIX + reportIndex + "_" + (pageIndex + 1) + "\"></a>\n");

					/*   */
					exportPage(page);

					if (reportIndex < jasperPrintList.size() - 1 || pageIndex < endPageIndex)
					{
						if (betweenPagesHtml == null)
						{
							writer.write("<br/>\n<br/>\n");
						}
						else
						{
							writer.write(betweenPagesHtml);
						}
					}

					writer.write("\n");
				}
			}
		}

		if (htmlFooter == null)
		{
			writer.write("</td><td width=\"50%\">&nbsp;</td></tr>\n");
			writer.write("</table>\n");
			writer.write("</body>\n");
			writer.write("</html>\n");
		}
		else
		{
			writer.write(htmlFooter);
		}

		writer.flush();
	}


	/**
	 *
	 */
	protected void exportPage(JRPrintPage page) throws JRException, IOException
	{
		topLimit = jasperPrint.getPageHeight();
		leftLimit = jasperPrint.getPageWidth();
		rightLimit = 0;
		bottomLimit = 0;
		
		setPageLimits(page.getElements());

		if (!isIgnorePageMargins)
		{
			topLimit = 0;
		}
		if (!isIgnorePageMargins)
		{
			leftLimit = 0;
		}
		if (jasperPrint.getPageWidth() > rightLimit && !isIgnorePageMargins)
		{
			rightLimit = jasperPrint.getPageWidth();
		}
		if (jasperPrint.getPageHeight() > bottomLimit && !isIgnorePageMargins)
		{
			bottomLimit = jasperPrint.getPageHeight();
		}
		
		if (topLimit > bottomLimit)//these can occur only when empty page and page margins are ignored
		{
			topLimit = bottomLimit;
		}
		if (leftLimit > rightLimit)
		{
			leftLimit = rightLimit;
		}
		
		writer.write(
			"<div class=\"jrPage\" style=\"" + (isWhitePageBackground ? "background-color: #FFFFFF;" : "") 
			+ "position:relative;width:" + toSizeUnit(rightLimit - leftLimit) 
			+ ";height:" + toSizeUnit(bottomLimit - topLimit) + ";\">\n"
			);

		frameInfoStack = new ArrayList<FrameInfo>();
		
		exportElements(page.getElements());

		writer.write("</div>");
		
		if (progressMonitor != null)
		{
			progressMonitor.afterPageExport();
		}
	}
	

	/**
	 *
	 */
	protected void exportElements(List<JRPrintElement> elements) throws IOException, JRException
	{
		if (elements != null && elements.size() > 0)
		{
			JRPrintElement element;
			for(int i = 0; i < elements.size(); i++)
			{
				elementIndex = i;
				
				element = elements.get(i);
				
				if (filter == null || filter.isToExport(element))
				{
					if (element instanceof JRPrintLine)
					{
						exportLine((JRPrintLine)element);
					}
					else if (element instanceof JRPrintRectangle)
					{
						exportRectangle((JRPrintRectangle)element);
					}
					else if (element instanceof JRPrintEllipse)
					{
						//exportEllipse((JRPrintEllipse)element);
						exportRectangle((JRPrintEllipse)element);
					}
					else if (element instanceof JRPrintImage)
					{
						exportImage((JRPrintImage)element);
					}
					else if (element instanceof JRPrintText)
					{
						exportText((JRPrintText)element);
					}
					else if (element instanceof JRPrintFrame)
					{
						exportFrame((JRPrintFrame) element);
					}
					else if (element instanceof JRGenericPrintElement)
					{
						exportGenericElement((JRGenericPrintElement) element);
					}
				}
			}
		}
	}

	
	/**
	 *
	 */
	protected void exportLine(JRPrintLine line) throws IOException
	{
		writer.write("<span");

		appendId(line);

		StringBuffer styleBuffer = new StringBuffer();

		appendPositionStyle(line, line.getLinePen(), styleBuffer);
		appendSizeStyle(line, line.getLinePen(), styleBuffer);
		appendBackcolorStyle(line, styleBuffer);
		
		String side = null;
		float ratio = line.getWidth() / line.getHeight();
		if (ratio > 1)
		{
			if (line.getDirectionValue() == LineDirectionEnum.TOP_DOWN)
			{
				side = "top";
			}
			else
			{
				side = "bottom";
			}
		}
		else
		{
			if (line.getDirectionValue() == LineDirectionEnum.TOP_DOWN)
			{
				side = "left";
			}
			else
			{
				side = "right";
			}
		}

		appendPen(
			styleBuffer,
			line.getLinePen(),
			side
			);

		if (styleBuffer.length() > 0)
		{
			writer.write(" style=\"");
			writer.write(styleBuffer.toString());
			writer.write("\"");
		}

		writer.write("></span>\n");
	}


	/**
	 *
	 */
	protected void exportRectangle(JRPrintGraphicElement element) throws IOException
	{
		writer.write("<span");

		appendId(element);

		StringBuffer styleBuffer = new StringBuffer();

		appendPositionStyle(element, element.getLinePen(), styleBuffer);
		appendSizeStyle(element, element.getLinePen(), styleBuffer);
		appendBackcolorStyle(element, styleBuffer);
		
		appendPen(
			styleBuffer,
			element.getLinePen(),
			null
			);

		if (styleBuffer.length() > 0)
		{
			writer.write(" style=\"");
			writer.write(styleBuffer.toString());
			writer.write("\"");
		}

		writer.write("></span>\n");
	}


	/**
	 *
	 */
	protected void exportStyledText(JRStyledText styledText, Locale locale) throws IOException
	{
		exportStyledText(styledText, null, locale, null, null);
	}
	
	protected void exportStyledText(JRStyledText styledText, String tooltip, Locale locale, LineSpacingEnum lineSpacing, Float lineSpacingSize) throws IOException
	{
		String text = styledText.getText();

		int runLimit = 0;

		AttributedCharacterIterator iterator = styledText.getAttributedString().getIterator();

		boolean first = true;
		boolean startedSpan = false;
		while(runLimit < styledText.length() && (runLimit = iterator.getRunLimit()) <= styledText.length())
		{
			//if there are several text runs, write the tooltip into a parent <span>
			if (first && runLimit < styledText.length() && tooltip != null)
			{
				startedSpan = true;
				writer.write("<span title=\"");
				writer.write(JRStringUtil.xmlEncode(tooltip));
				writer.write("\">");
				//reset the tooltip so that inner <span>s to not use it
				tooltip = null;
			}
			first = false;
			
			exportStyledTextRun(
				iterator.getAttributes(), 
				text.substring(iterator.getIndex(), runLimit),
				tooltip,
				locale,
				lineSpacing,
				lineSpacingSize
				);

			iterator.setIndex(runLimit);
		}
		
		if (startedSpan)
		{
			writer.write("</span>");
		}
	}


	/**
	 *
	 */
	protected void exportStyledTextRun(Map<Attribute,Object> attributes, String text, Locale locale) throws IOException
	{
		exportStyledTextRun(attributes, text, null, locale, null, null);
	}
	
	protected void exportStyledTextRun(
		Map<Attribute,Object> attributes, 
		String text,
		String tooltip,
		Locale locale,
		LineSpacingEnum lineSpacing,
		Float lineSpacingSize
		) throws IOException
	{
		String fontFamilyAttr = (String)attributes.get(TextAttribute.FAMILY);//FIXMENOW reuse this font lookup code everywhere
		String fontFamily = fontFamilyAttr;
		if (fontMap != null && fontMap.containsKey(fontFamilyAttr))
		{
			fontFamily = fontMap.get(fontFamilyAttr);
		}
		else
		{
			FontInfo fontInfo = FontUtil.getInstance(jasperReportsContext).getFontInfo(fontFamilyAttr, locale);
			if (fontInfo != null)
			{
				//fontName found in font extensions
				FontFamily family = fontInfo.getFontFamily();
				String exportFont = family.getExportFont(getExporterKey());
				if (exportFont != null)
				{
					fontFamily = exportFont;
				}
			}
		}

		boolean localHyperlink = false;
		JRPrintHyperlink hyperlink = (JRPrintHyperlink)attributes.get(JRTextAttribute.HYPERLINK);
		if (!hyperlinkStarted && hyperlink != null)
		{
			startHyperlink(hyperlink);
			localHyperlink = true;
		}
		
		writer.write("<span style=\"font-family: ");
		writer.write(fontFamily);
		writer.write("; ");

		Color forecolor = (Color)attributes.get(TextAttribute.FOREGROUND);
		if (!hyperlinkStarted || !Color.black.equals(forecolor))
		{
			writer.write("color: #");
			writer.write(JRColorUtil.getColorHexa(forecolor));
			writer.write("; ");
		}

		Color runBackcolor = (Color)attributes.get(TextAttribute.BACKGROUND);
		if (runBackcolor != null)
		{
			writer.write("background-color: #");
			writer.write(JRColorUtil.getColorHexa(runBackcolor));
			writer.write("; ");
		}

		writer.write("font-size: ");
		writer.write(toSizeUnit(((Float)attributes.get(TextAttribute.SIZE)).intValue()));
		writer.write(";");
		
		switch (lineSpacing)
		{
			case SINGLE:
			default:
			{
				writer.write(" line-height: 1; *line-height: normal;");
				break;
			}
			case ONE_AND_HALF:
			{
				writer.write(" line-height: 1.5;");
				break;
			}
			case DOUBLE:
			{
				writer.write(" line-height: 2.0;");
				break;
			}
			case PROPORTIONAL:
			{
				if (lineSpacingSize != null) {
					writer.write(" line-height: " + lineSpacingSize.floatValue() + ";");
				}
				break;
			}
			case AT_LEAST:
			case FIXED:
			{
				if (lineSpacingSize != null) {
					writer.write(" line-height: " + lineSpacingSize.floatValue() + "px;");
				}
				break;
			}
		}

		/*
		if (!horizontalAlignment.equals(CSS_TEXT_ALIGN_LEFT))
		{
			writer.write(" text-align: ");
			writer.write(horizontalAlignment);
			writer.write(";");
		}
		*/

		if (TextAttribute.WEIGHT_BOLD.equals(attributes.get(TextAttribute.WEIGHT)))
		{
			writer.write(" font-weight: bold;");
		}
		if (TextAttribute.POSTURE_OBLIQUE.equals(attributes.get(TextAttribute.POSTURE)))
		{
			writer.write(" font-style: italic;");
		}
		if (TextAttribute.UNDERLINE_ON.equals(attributes.get(TextAttribute.UNDERLINE)))
		{
			writer.write(" text-decoration: underline;");
		}
		if (TextAttribute.STRIKETHROUGH_ON.equals(attributes.get(TextAttribute.STRIKETHROUGH)))
		{
			writer.write(" text-decoration: line-through;");
		}

		if (TextAttribute.SUPERSCRIPT_SUPER.equals(attributes.get(TextAttribute.SUPERSCRIPT)))
		{
			writer.write(" vertical-align: super;");
		}
		else if (TextAttribute.SUPERSCRIPT_SUB.equals(attributes.get(TextAttribute.SUPERSCRIPT)))
		{
			writer.write(" vertical-align: sub;");
		}
		
		writer.write("\"");

		if (tooltip != null)
		{
			writer.write(" title=\"");
			writer.write(JRStringUtil.xmlEncode(tooltip));
			writer.write("\"");
		}
		
		writer.write(">");

		writer.write(
			JRStringUtil.htmlEncode(text)
			);

		writer.write("</span>");

		if (localHyperlink)
		{
			endHyperlink();
		}
	}


	/**
	 *
	 */
	protected void exportText(JRPrintText text) throws IOException
	{
		JRStyledText styledText = getStyledText(text);

		int textLength = 0;

		if (styledText != null)
		{
			textLength = styledText.length();
		}

		if (text.getAnchorName() != null)
		{
			writer.write("<a name=\"");
			writer.write(text.getAnchorName());
			writer.write("\"></a>");
		}

		writer.write("<div");//FIXME why dealing with cell style if no text to print (textLength == 0)?

		appendId(text);

		if (text.getRunDirectionValue() == RunDirectionEnum.RTL)
		{
			writer.write(" dir=\"rtl\"");
		}

		StringBuffer styleBuffer = new StringBuffer();
		StringBuffer divStyleBuffer = new StringBuffer();
		String rotationValue = null;

		if (text.getRotationValue() == RotationEnum.NONE)
		{
			appendPositionStyle(text, text, divStyleBuffer);
			appendSizeStyle(text, text, divStyleBuffer);
			appendBorderStyle(text.getLineBox(), divStyleBuffer);

			appendPositionStyle(
				text.getLineBox().getLeftPadding() - getInsideBorderOffset(text.getLineBox().getLeftPen().getLineWidth(), false),
				text.getLineBox().getTopPadding() - getInsideBorderOffset(text.getLineBox().getTopPen().getLineWidth(), false), 
				styleBuffer
				);
			appendSizeStyle(
				text.getWidth() - text.getLineBox().getLeftPadding() - text.getLineBox().getRightPadding(), 
				text.getHeight() - text.getLineBox().getTopPadding() - text.getLineBox().getBottomPadding(), 
				styleBuffer
				);
//			styleBuffer.append("width: 100%; height: 100%;");
		}
		else
		{
			JRBasePrintText rotatedText = new JRBasePrintText(text.getDefaultStyleProvider());
			rotatedText.setUUID(text.getUUID());
			rotatedText.setX(text.getX());
			rotatedText.setY(text.getY());
			rotatedText.setWidth(text.getWidth());
			rotatedText.setHeight(text.getHeight());
			rotatedText.copyBox(text.getLineBox());
			
			JRBoxUtil.rotate(rotatedText.getLineBox(), text.getRotationValue());
			
			int rotationIE = 0;
			int rotationAngle = 0;
			int translateX = 0;
			int translateY = 0;
			switch (text.getRotationValue())
			{
				case LEFT : 
				{
					translateX = 
						- (text.getHeight()
							- text.getLineBox().getTopPadding()
							- text.getLineBox().getBottomPadding()
						- (text.getWidth() 
							- text.getLineBox().getLeftPadding()
							- text.getLineBox().getRightPadding()
							)) / 2;
					translateY = 
						(text.getHeight() 
							- text.getLineBox().getTopPadding()
							- text.getLineBox().getBottomPadding()
						- (text.getWidth() 
							- text.getLineBox().getLeftPadding()
							- text.getLineBox().getRightPadding()
							)) / 2;
					rotatedText.setWidth(text.getHeight());
					rotatedText.setHeight(text.getWidth());
					rotationIE = 3;
					rotationAngle = -90;
					rotationValue = "left";
					break;
				}
				case RIGHT : 
				{
					translateX = 
						- (text.getHeight() 
							- text.getLineBox().getTopPadding()
							- text.getLineBox().getBottomPadding()
						- (text.getWidth() 
							- text.getLineBox().getLeftPadding()
							- text.getLineBox().getRightPadding()
							)) / 2;
					translateY = 
						(text.getHeight() 
							- text.getLineBox().getTopPadding()
							- text.getLineBox().getBottomPadding()
						- (text.getWidth() 
							- text.getLineBox().getLeftPadding()
							- text.getLineBox().getRightPadding()
							)) / 2;
					rotatedText.setWidth(text.getHeight());
					rotatedText.setHeight(text.getWidth());
					rotationIE = 1;
					rotationAngle = 90;
					rotationValue = "right";
					break;
				}
				case UPSIDE_DOWN : 
				{
					rotationIE = 2;
					rotationAngle = 180;
					rotationValue = "upsideDown";
					break;
				}
				case NONE :
				default :
				{
				}
			}
			
			appendPositionStyle(text, text, divStyleBuffer);
			appendSizeStyle(text, text, divStyleBuffer);
			appendBorderStyle(text.getLineBox(), divStyleBuffer);

			appendPositionStyle(
				text.getLineBox().getLeftPadding() - getInsideBorderOffset(text.getLineBox().getLeftPen().getLineWidth(), false), 
				text.getLineBox().getTopPadding() - getInsideBorderOffset(text.getLineBox().getTopPen().getLineWidth(), false), 
				styleBuffer
				);
			appendSizeStyle(
				rotatedText.getWidth() - rotatedText.getLineBox().getLeftPadding() - rotatedText.getLineBox().getRightPadding(), 
				rotatedText.getHeight() - rotatedText.getLineBox().getTopPadding() - rotatedText.getLineBox().getBottomPadding(), 
				styleBuffer
				);
//			appendSizeStyle(rotatedText, rotatedText, styleBuffer);
			
			styleBuffer.append("-webkit-transform: translate(" + translateX + "px," + translateY + "px) ");
			styleBuffer.append("rotate(" + rotationAngle + "deg); ");
			styleBuffer.append("-moz-transform: translate(" + translateX + "px," + translateY + "px) ");
			styleBuffer.append("rotate(" + rotationAngle + "deg); ");
			styleBuffer.append("filter: progid:DXImageTransform.Microsoft.BasicImage(rotation=" + rotationIE + "); ");
		}

		appendBackcolorStyle(text, divStyleBuffer);

		String verticalAlignment = HTML_VERTICAL_ALIGN_TOP;

		switch (text.getVerticalAlignmentValue())
		{
			case BOTTOM :
			{
				verticalAlignment = HTML_VERTICAL_ALIGN_BOTTOM;
				break;
			}
			case MIDDLE :
			{
				verticalAlignment = HTML_VERTICAL_ALIGN_MIDDLE;
				break;
			}
			case TOP :
			default :
			{
				verticalAlignment = HTML_VERTICAL_ALIGN_TOP;
			}
		}

		String horizontalAlignment = CSS_TEXT_ALIGN_LEFT;

		if (textLength > 0)
		{
			switch (text.getHorizontalAlignmentValue())
			{
				case RIGHT :
				{
					horizontalAlignment = CSS_TEXT_ALIGN_RIGHT;
					break;
				}
				case CENTER :
				{
					horizontalAlignment = CSS_TEXT_ALIGN_CENTER;
					break;
				}
				case JUSTIFIED :
				{
					horizontalAlignment = CSS_TEXT_ALIGN_JUSTIFY;
					break;
				}
				case LEFT :
				default :
				{
					horizontalAlignment = CSS_TEXT_ALIGN_LEFT;
				}
			}

//			if (
//				(text.getRunDirection() == JRPrintText.RUN_DIRECTION_LTR
//				 && !horizontalAlignment.equals(CSS_TEXT_ALIGN_LEFT))
//				|| (text.getRunDirection() == JRPrintText.RUN_DIRECTION_RTL
//					&& !horizontalAlignment.equals(CSS_TEXT_ALIGN_RIGHT))
//				)
//			{
				styleBuffer.append("text-align: ");
				styleBuffer.append(horizontalAlignment);
				styleBuffer.append(";");
//			}
//
//			if (!verticalAlignment.equals(HTML_VERTICAL_ALIGN_TOP))
//			{
				styleBuffer.append(" display:table;");
//			}
		}

		if (isWrapBreakWord)
		{
			//styleBuffer.append("width: " + toSizeUnit(text.getWidth()) + "; ");
			styleBuffer.append("word-wrap: break-word; ");
		}
		
		styleBuffer.append("text-indent: " + text.getParagraph().getFirstLineIndent().intValue() + "px; ");
//		styleBuffer.append("margin-left: " + text.getParagraph().getLeftIndent().intValue() + "px; ");
//		styleBuffer.append("margin-right: " + text.getParagraph().getRightIndent().intValue() + "px; ");
//		styleBuffer.append("margin-top: " + text.getParagraph().getSpacingBefore().intValue() + "px; ");
//		styleBuffer.append("margin-bottom: " + text.getParagraph().getSpacingAfter().intValue() + "px; ");

		if (text.getLineBreakOffsets() != null)
		{
			//if we have line breaks saved in the text, set nowrap so that
			//the text only wraps at the explicit positions
			styleBuffer.append("white-space: nowrap; ");
		}
		
		divStyleBuffer.append("overflow: hidden;");
		
		if (divStyleBuffer.length() > 0) 
		{
			writer.write(" style=\"");
			writer.write(divStyleBuffer.toString());
			writer.write("\"");
		}
		writer.write("><span");
		
		if (rotationValue != null) 
		{
			writer.write(" class=\"rotated\" data-rotation=\"" + rotationValue + "\"");
		}
		
		if (styleBuffer.length() > 0)
		{
			writer.write(" style=\"");
			writer.write(styleBuffer.toString());
			writer.write("\"");
		}
		
		writer.write(">");
		
//		if (!verticalAlignment.equals(HTML_VERTICAL_ALIGN_TOP))
//		{
			writer.write("<span style=\"display:table-cell;vertical-align:"); //display:table-cell conflicts with overflow: hidden;
			writer.write(verticalAlignment);
			writer.write(";\">");
//		}

		startHyperlink(text);

		if (textLength > 0)
		{
			//only use text tooltip when no hyperlink present
			String textTooltip = hyperlinkStarted ? null : text.getHyperlinkTooltip();
			exportStyledText(styledText, textTooltip, getTextLocale(text), text.getParagraph().getLineSpacing(), text.getParagraph().getLineSpacingSize());
		}
//		else
//		{
//			//writer.write(emptyCellStringProvider.getStringForEmptyTD(imagesURI));
//		}

		endHyperlink();

//		if (!verticalAlignment.equals(HTML_VERTICAL_ALIGN_TOP))
//		{
			writer.write("</span>");//FIXMENOW move tooltip span here
//		}

		writer.write("</span></div>\n");
	}


	protected boolean startHyperlink(JRPrintHyperlink link) throws IOException
	{
		String href = getHyperlinkURL(link);

		if (href != null)
		{
			writer.write("<a href=\"");
			writer.write(href);
			writer.write("\"");

			String target = getHyperlinkTarget(link);
			if (target != null)
			{
				writer.write(" target=\"");
				writer.write(target);
				writer.write("\"");
			}

			if (link.getHyperlinkTooltip() != null)
			{
				writer.write(" title=\"");
				writer.write(JRStringUtil.xmlEncode(link.getHyperlinkTooltip()));
				writer.write("\"");
			}
			
			writer.write(">");
		}
		
		hyperlinkStarted = href != null;
		
		return hyperlinkStarted;
	}


	protected String getHyperlinkTarget(JRPrintHyperlink link)
	{
		String target = null;
		JRHyperlinkTargetProducer producer = targetProducerFactory.getHyperlinkTargetProducer(link.getLinkTarget());		
		if (producer == null)
		{
			switch(link.getHyperlinkTargetValue())
			{
				case BLANK :
				{
					target = "_blank";
					break;
				}
				case PARENT :
				{
					target = "_parent";
					break;
				}
				case TOP :
				{
					target = "_top";
					break;
				}
				case CUSTOM :
				{
					boolean paramFound = false;
					List<JRPrintHyperlinkParameter> parameters = link.getHyperlinkParameters() == null ? null : link.getHyperlinkParameters().getParameters();
					if (parameters != null)
					{
						for(Iterator<JRPrintHyperlinkParameter> it = parameters.iterator(); it.hasNext();)
						{
							JRPrintHyperlinkParameter parameter = it.next();
							if (link.getLinkTarget().equals(parameter.getName()))
							{
								target = parameter.getValue() == null ? null : parameter.getValue().toString();
								paramFound = true;
								break;
							}
						}
					}
					if (!paramFound)
					{
						target = link.getLinkTarget();
					}
					break;
				}
				case SELF :
				default :
				{
				}
			}
		}
		else
		{
			target = producer.getHyperlinkTarget(link);
		}

		return target;
	}


	protected String getHyperlinkURL(JRPrintHyperlink link)
	{
		String href = null;
		
		Boolean hyperlinkVisible = HyperlinkUtil.getHyperlinkVisible(PROPERTY_XHTML_HYPERLINK_VISIBLE, link);
		if (hyperlinkVisible == null)
		{
			hyperlinkVisible = JRPropertiesUtil.getInstance(jasperReportsContext).getBooleanProperty(jasperPrint, PROPERTY_XHTML_HYPERLINK_VISIBLE, true);
		}

		if (hyperlinkVisible)
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
							href = link.getHyperlinkReference();
						}
						break;
					}
					case LOCAL_ANCHOR :
					{
						if (link.getHyperlinkAnchor() != null)
						{
							href = "#" + link.getHyperlinkAnchor();
						}
						break;
					}
					case LOCAL_PAGE :
					{
						if (link.getHyperlinkPage() != null)
						{
							href = "#" + JR_PAGE_ANCHOR_PREFIX + reportIndex + "_" + link.getHyperlinkPage().toString();
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
							href = link.getHyperlinkReference() + "#" + link.getHyperlinkAnchor();
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
							href = link.getHyperlinkReference() + "#" + JR_PAGE_ANCHOR_PREFIX + "0_" + link.getHyperlinkPage().toString();
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
				href = customHandler.getHyperlink(link);
			}
		}
		
		return href;
	}


	protected void endHyperlink() throws IOException
	{
		if (hyperlinkStarted)
		{
			writer.write("</a>");
		}
		hyperlinkStarted = false;
	}


	protected boolean appendBorderStyle(JRLineBox box, StringBuffer styleBuffer)
	{
		boolean addedToStyle = false;
		
		if (box != null)
		{
			LineStyleEnum tps = box.getTopPen().getLineStyleValue();
			LineStyleEnum lps = box.getLeftPen().getLineStyleValue();
			LineStyleEnum bps = box.getBottomPen().getLineStyleValue();
			LineStyleEnum rps = box.getRightPen().getLineStyleValue();
			
			float tpw = box.getTopPen().getLineWidth().floatValue();
			float lpw = box.getLeftPen().getLineWidth().floatValue();
			float bpw = box.getBottomPen().getLineWidth().floatValue();
			float rpw = box.getRightPen().getLineWidth().floatValue();
			
			if (0f < tpw && tpw < 1f) {
				tpw = 1f;
			}
			if (0f < lpw && lpw < 1f) {
				lpw = 1f;
			}
			if (0f < bpw && bpw < 1f) {
				bpw = 1f;
			}
			if (0f < rpw && rpw < 1f) {
				rpw = 1f;
			}
			
			Color tpc = box.getTopPen().getLineColor();
			
			// try to compact all borders into one css property
			if (tps == lps &&												// same line style
					tps == bps &&
					tps == rps &&
					tpw == lpw &&											// same line width
					tpw == bpw &&
					tpw == rpw &&
					tpc.equals(box.getLeftPen().getLineColor()) &&			// same line color
					tpc.equals(box.getBottomPen().getLineColor()) &&
					tpc.equals(box.getRightPen().getLineColor())) 
			{
				addedToStyle |= appendPen(
						styleBuffer,
						box.getTopPen(),
						null
						);
			} else {
				addedToStyle |= appendPen(
					styleBuffer,
					box.getTopPen(),
					"top"
					);
				addedToStyle |= appendPen(
					styleBuffer,
					box.getLeftPen(),
					"left"
					);
				addedToStyle |= appendPen(
					styleBuffer,
					box.getBottomPen(),
					"bottom"
					);
				addedToStyle |= appendPen(
					styleBuffer,
					box.getRightPen(),
					"right"
					);
			}
			
			Integer tp = box.getTopPadding();
			Integer lp = box.getLeftPadding();
			Integer bp = box.getBottomPadding();
			Integer rp = box.getRightPadding();
			
			// try to compact all paddings into one css property
			if (tp == lp && tp == bp && tp == rp)
			{
				addedToStyle |= appendPadding(
						styleBuffer,
						tp,
						null
						);
			} else 
			{
				addedToStyle |= appendPadding(
						styleBuffer,
						box.getTopPadding(),
						"top"
						);
				addedToStyle |= appendPadding(
						styleBuffer,
						box.getLeftPadding(),
						"left"
						);
				addedToStyle |= appendPadding(
						styleBuffer,
						box.getBottomPadding(),
						"bottom"
						);
				addedToStyle |= appendPadding(
						styleBuffer,
						box.getRightPadding(),
						"right"
						);
			}
		}
		
		return addedToStyle;
	}


	protected int getInsideBorderOffset(float borderWidth, boolean small)
	{
		int intBorderWidth = (int)borderWidth;
		if (0f < borderWidth && borderWidth < 1f)
		{
			intBorderWidth = 1;
		}
		return intBorderWidth / 2 + (small ? 0 : intBorderWidth % 2);
	}
		
	
	protected void appendSizeStyle(JRPrintElement element, JRBoxContainer boxContainer, StringBuffer styleBuffer)
	{
		int widthDiff = 0;
		int heightDiff = 0;

		JRLineBox box = boxContainer == null ? null :  boxContainer.getLineBox();
		if (box != null)
		{
			widthDiff = 
				box.getLeftPadding().intValue() + box.getRightPadding().intValue()
				+ getInsideBorderOffset(box.getLeftPen().getLineWidth().floatValue(), false)
				+ getInsideBorderOffset(box.getRightPen().getLineWidth().floatValue(), true);
			heightDiff =
				box.getTopPadding().intValue() + box.getBottomPadding().intValue()
				+ getInsideBorderOffset(box.getTopPen().getLineWidth().floatValue(), false)
				+ getInsideBorderOffset(box.getBottomPen().getLineWidth().floatValue(), true);
		}
		
		int width = element.getWidth() - widthDiff; 
		int height = element.getHeight() - heightDiff; 
		
		appendSizeStyle(
			width < 0 ? 0 : width,
			height < 0 ? 0 : height,
			styleBuffer
			);
	}


	protected void appendSizeStyle(JRPrintElement element, JRPen pen, StringBuffer styleBuffer)
	{
		int diff = 0;

		if (pen != null)
		{
			diff = 
				getInsideBorderOffset(pen.getLineWidth().floatValue(), false) 
				+ getInsideBorderOffset(pen.getLineWidth().floatValue(), true);
		}
		
		appendSizeStyle(
			element.getWidth() - diff,
			element.getHeight() - diff,
			styleBuffer
			);
	}


	protected void appendSizeStyle(int width, int height, StringBuffer styleBuffer)
	{
		styleBuffer.append("width:");
		styleBuffer.append(toSizeUnit(width));
		styleBuffer.append(";");

		styleBuffer.append("height:");
		styleBuffer.append(toSizeUnit(height));
		styleBuffer.append(";");
	}


	protected void appendPositionStyle(JRPrintElement element, JRBoxContainer boxContainer, StringBuffer styleBuffer)
	{
		int leftOffset = 0;
		int topOffset = 0;

		JRLineBox box = boxContainer == null ? null :  boxContainer.getLineBox();
		if (box != null)
		{
			leftOffset = 
				getInsideBorderOffset(box.getLeftPen().getLineWidth().floatValue(), true);
			topOffset = 
				getInsideBorderOffset(box.getTopPen().getLineWidth().floatValue(), true);
		}
		
		FrameInfo frameInfo = frameInfoStack.size() == 0 ? null : frameInfoStack.get(frameInfoStack.size() - 1); 
		
		appendPositionStyle(
			element.getX() - leftOffset - (frameInfo == null ? leftLimit : frameInfo.leftInsideBorderOffset),
			element.getY() - topOffset - (frameInfo == null ? topLimit : frameInfo.topInsideBorderOffset),
			styleBuffer
			);
	}


	protected void appendPositionStyle(JRPrintElement element, JRPen pen, StringBuffer styleBuffer)
	{
		int offset = 0;

		if (pen != null)
		{
			offset = 
				getInsideBorderOffset(pen.getLineWidth().floatValue(), true);
		}
		
		FrameInfo frameInfo = frameInfoStack.size() == 0 ? null : frameInfoStack.get(frameInfoStack.size() - 1); 
		
		appendPositionStyle(
			element.getX() - offset - (frameInfo == null ? leftLimit : frameInfo.leftInsideBorderOffset),
			element.getY() - offset - (frameInfo == null ? topLimit : frameInfo.topInsideBorderOffset),
			styleBuffer
			);
	}


	protected void appendPositionStyle(int x, int y, StringBuffer styleBuffer)
	{
		styleBuffer.append("position:absolute;");
		styleBuffer.append("left:");
		styleBuffer.append(toSizeUnit(x));
		styleBuffer.append(";");
		styleBuffer.append("top:");
		styleBuffer.append(toSizeUnit(y));
		styleBuffer.append(";");
	}


	protected void appendBackcolorStyle(JRPrintElement element, StringBuffer styleBuffer)
	{
		if (element.getModeValue() == ModeEnum.OPAQUE)
		{
			styleBuffer.append("background-color: #");
			styleBuffer.append(JRColorUtil.getColorHexa(element.getBackcolor()));
			styleBuffer.append("; ");
		}
	}


	/**
	 *
	 */
	protected void exportImage(JRPrintImage image) throws JRException, IOException
	{
		writer.write("<span");

		appendId(image);

		float xAlignFactor = 0f;

		switch (image.getHorizontalAlignmentValue())
		{
			case RIGHT :
			{
				xAlignFactor = 1f;
				break;
			}
			case CENTER :
			{
				xAlignFactor = 0.5f;
				break;
			}
			case LEFT :
			default :
			{
				xAlignFactor = 0f;
			}
		}

		float yAlignFactor = 0f;

		switch (image.getVerticalAlignmentValue())
		{
			case BOTTOM :
			{
				yAlignFactor = 1f;
				break;
			}
			case MIDDLE :
			{
				yAlignFactor = 0.5f;
				break;
			}
			case TOP :
			default :
			{
				yAlignFactor = 0f;
			}
		}

		StringBuffer styleBuffer = new StringBuffer();
		appendPositionStyle(image, image, styleBuffer);
		appendSizeStyle(image, image, styleBuffer);
		appendBackcolorStyle(image, styleBuffer);
		
		boolean addedToStyle = appendBorderStyle(image.getLineBox(), styleBuffer);
		if (!addedToStyle)
		{
			appendPen(
				styleBuffer,
				image.getLinePen(),
				null
				);
		}

		if (styleBuffer.length() > 0)
		{
			writer.write(" style=\"");
			writer.write(styleBuffer.toString());
			writer.write("\"");
		}

		writer.write(">");

		if (image.getAnchorName() != null)
		{
			writer.write("<a name=\"");
			writer.write(image.getAnchorName());
			writer.write("\"></a>");
		}
		
		Renderable renderer = image.getRenderable();
		Renderable originalRenderer = renderer;
		boolean imageMapRenderer = renderer != null 
				&& renderer instanceof ImageMapRenderable
				&& ((ImageMapRenderable) renderer).hasImageAreaHyperlinks();

		boolean hasHyperlinks = false;

		if(renderer != null)
		{
			if (imageMapRenderer)
			{
				hasHyperlinks = true;
				hyperlinkStarted = false;
			}
			else
			{
				hasHyperlinks = startHyperlink(image);
			}
			
			writer.write("<img");
			String imagePath = null;
			String imageMapName = null;
			List<JRPrintImageAreaHyperlink> imageMapAreas = null;
	
			ScaleImageEnum scaleImage = image.getScaleImageValue();
			
			if (renderer != null)
			{
				if (renderer.getTypeValue() == RenderableTypeEnum.IMAGE && rendererToImagePathMap.containsKey(renderer.getId()))
				{
					imagePath = rendererToImagePathMap.get(renderer.getId());
				}
				else
				{
					if (image.isLazy())
					{
						imagePath = ((JRImageRenderer)renderer).getImageLocation();
					}
					else
					{
						JRPrintElementIndex imageIndex = getElementIndex();
						imagesToProcess.add(imageIndex);
	
						String imageName = getImageName(imageIndex);
						imagePath = imagesURI + imageName;

						//backward compatibility with the IMAGE_MAP parameter
						if (imageNameToImageDataMap != null)
						{
							if (renderer.getTypeValue() == RenderableTypeEnum.SVG)
							{
								renderer =
									new JRWrappingSvgRenderer(
										renderer,
										new Dimension(image.getWidth(), image.getHeight()),
										ModeEnum.OPAQUE == image.getModeValue() ? image.getBackcolor() : null
										);
							}
							imageNameToImageDataMap.put(imageName, renderer.getImageData(jasperReportsContext));
						}
						//END - backward compatibility with the IMAGE_MAP parameter
					}
	
					rendererToImagePathMap.put(renderer.getId(), imagePath);
				}
				
				if (imageMapRenderer)
				{
					Rectangle renderingArea = new Rectangle(image.getWidth(), image.getHeight());
					
					if (renderer.getTypeValue() == RenderableTypeEnum.IMAGE)
					{
						imageMapName = imageMaps.get(new Pair<String,Rectangle>(renderer.getId(), renderingArea));
					}
	
					if (imageMapName == null)
					{
						imageMapName = "map_" + getElementIndex().toString();
						imageMapAreas = ((ImageMapRenderable) originalRenderer).getImageAreaHyperlinks(renderingArea);//FIXMECHART
						
						if (renderer.getTypeValue() == RenderableTypeEnum.IMAGE)
						{
							imageMaps.put(new Pair<String,Rectangle>(renderer.getId(), renderingArea), imageMapName);
						}
					}
				}
			}
	
			writer.write(" src=\"");
			if (imagePath != null)
			{
				writer.write(imagePath);
			}
			writer.write("\"");
		
			int availableImageWidth = image.getWidth() - image.getLineBox().getLeftPadding().intValue() - image.getLineBox().getRightPadding().intValue();
			if (availableImageWidth < 0)
			{
				availableImageWidth = 0;
			}
		
			int availableImageHeight = image.getHeight() - image.getLineBox().getTopPadding().intValue() - image.getLineBox().getBottomPadding().intValue();
			if (availableImageHeight < 0)
			{
				availableImageHeight = 0;
			}
		
			switch (scaleImage)
			{
				case FILL_FRAME :
				{
					int leftDiff = 0;
					int topDiff = 0;
					int widthDiff = 0;
					int heightDiff = 0;

					JRLineBox box = image.getLineBox();
					if (box != null)
					{
						leftDiff = box.getLeftPadding().intValue();
						topDiff = box.getTopPadding().intValue();
						widthDiff = 
							getInsideBorderOffset(box.getLeftPen().getLineWidth().floatValue(), false)
							+ getInsideBorderOffset(box.getRightPen().getLineWidth().floatValue(), true);
						heightDiff =
							getInsideBorderOffset(box.getTopPen().getLineWidth().floatValue(), false)
							+ getInsideBorderOffset(box.getBottomPen().getLineWidth().floatValue(), true);
					}
					
					writer.write(" style=\"position:absolute;left:");
					writer.write(toSizeUnit(leftDiff));
					writer.write(";top:");
					writer.write(toSizeUnit(topDiff));
					writer.write(";width:");
					writer.write(toSizeUnit(availableImageWidth - widthDiff));
					writer.write(";height:");
					writer.write(toSizeUnit(availableImageHeight - heightDiff));
					writer.write("\"");
		
					break;
				}
				case CLIP :
				{
					double normalWidth = availableImageWidth;
					double normalHeight = availableImageHeight;
		
					if (!image.isLazy())
					{
						// Image load might fail. 
						Renderable tmpRenderer = 
							RenderableUtil.getInstance(jasperReportsContext).getOnErrorRendererForDimension(renderer, image.getOnErrorTypeValue());
						Dimension2D dimension = tmpRenderer == null ? null : tmpRenderer.getDimension(jasperReportsContext);
						// If renderer was replaced, ignore image dimension.
						if (tmpRenderer == renderer && dimension != null)
						{
							normalWidth = dimension.getWidth();
							normalHeight = dimension.getHeight();
						}
					}

					int leftDiff = 0;
					int topDiff = 0;
					int widthDiff = 0;
					int heightDiff = 0;

					JRLineBox box = image.getLineBox();
					if (box != null)
					{
						leftDiff = box.getLeftPadding().intValue();
						topDiff = box.getTopPadding().intValue();
						widthDiff = 
							getInsideBorderOffset(box.getLeftPen().getLineWidth().floatValue(), false)
							+ getInsideBorderOffset(box.getRightPen().getLineWidth().floatValue(), true);
						heightDiff =
							getInsideBorderOffset(box.getTopPen().getLineWidth().floatValue(), false)
							+ getInsideBorderOffset(box.getBottomPen().getLineWidth().floatValue(), true);
					}
					
					writer.write(" style=\"position:absolute;left:");
					writer.write(toSizeUnit((int)(leftDiff + xAlignFactor * (availableImageWidth - widthDiff - normalWidth))));
					writer.write(";top:");
					writer.write(toSizeUnit((int)(topDiff + yAlignFactor * (availableImageHeight - heightDiff - normalHeight))));
					writer.write(";width:");
					writer.write(toSizeUnit((int)normalWidth));
					writer.write(";height:");
					writer.write(toSizeUnit((int)normalHeight));
					writer.write(";clip:rect(");
					writer.write(toSizeUnit((int)(yAlignFactor * (normalHeight - availableImageHeight + heightDiff))));
					writer.write(",");
					writer.write(toSizeUnit((int)(xAlignFactor * normalWidth + (1 - xAlignFactor) * (availableImageWidth - widthDiff))));
					writer.write(",");
					writer.write(toSizeUnit((int)(yAlignFactor * normalHeight + (1 - yAlignFactor) * (availableImageHeight - heightDiff))));
					writer.write(",");
					writer.write(toSizeUnit((int)(xAlignFactor * (normalWidth - availableImageWidth + widthDiff))));
					writer.write(")\"");

					break;
				}
				case RETAIN_SHAPE :
				default :
				{
					double normalWidth = availableImageWidth;
					double normalHeight = availableImageHeight;
		
					if (!image.isLazy())
					{
						// Image load might fail. 
						Renderable tmpRenderer = 
							RenderableUtil.getInstance(jasperReportsContext).getOnErrorRendererForDimension(renderer, image.getOnErrorTypeValue());
						Dimension2D dimension = tmpRenderer == null ? null : tmpRenderer.getDimension(jasperReportsContext);
						// If renderer was replaced, ignore image dimension.
						if (tmpRenderer == renderer && dimension != null)
						{
							normalWidth = dimension.getWidth();
							normalHeight = dimension.getHeight();
						}
					}
		
					int leftDiff = 0;
					int topDiff = 0;
					int widthDiff = 0;
					int heightDiff = 0;

					JRLineBox box = image.getLineBox();
					if (box != null)
					{
						leftDiff = box.getLeftPadding().intValue();
						topDiff = box.getTopPadding().intValue();
						widthDiff = 
							getInsideBorderOffset(box.getLeftPen().getLineWidth().floatValue(), false)
							+ getInsideBorderOffset(box.getRightPen().getLineWidth().floatValue(), true);
						heightDiff =
							getInsideBorderOffset(box.getTopPen().getLineWidth().floatValue(), false)
							+ getInsideBorderOffset(box.getBottomPen().getLineWidth().floatValue(), true);
					}
					
					if (availableImageHeight > 0)
					{
						double ratio = normalWidth / normalHeight;
		
						if( ratio > (double)availableImageWidth / (double)availableImageHeight )
						{
							writer.write(" style=\"position:absolute;left:");
							writer.write(toSizeUnit(leftDiff));
							writer.write(";top:");
							writer.write(toSizeUnit((int)(topDiff + yAlignFactor * (availableImageHeight - heightDiff - (availableImageWidth - widthDiff) / ratio))));
							writer.write(";width:");
							writer.write(toSizeUnit(availableImageWidth - widthDiff));
							writer.write("\"");
						}
						else
						{
							writer.write(" style=\"position:absolute;left:");
							//writer.write(String.valueOf(leftDiff));
							writer.write(toSizeUnit((int)(leftDiff + xAlignFactor * (availableImageWidth - widthDiff - ratio * (availableImageHeight - heightDiff)))));
							writer.write(";top:");
							writer.write(toSizeUnit(topDiff));
							writer.write(";height:");
							writer.write(toSizeUnit(availableImageHeight - heightDiff));
							writer.write("\"");
						}
					}
				}
			}
			
			if (imageMapName != null)
			{
				writer.write(" usemap=\"#" + imageMapName + "\"");
			}
			
			writer.write(" alt=\"\"");
			
			if (hasHyperlinks)
			{
				writer.write(" border=\"0\"");
			}
			
			if (image.getHyperlinkTooltip() != null)
			{
				writer.write(" title=\"");
				writer.write(JRStringUtil.xmlEncode(image.getHyperlinkTooltip()));
				writer.write("\"");
			}
			
			writer.write("/>");

			endHyperlink();
			
			if (imageMapAreas != null)
			{
				writer.write("\n");
				writeImageMap(imageMapName, image, imageMapAreas);
			}
		}
		writer.write("</span>\n");
	}


	protected JRPrintElementIndex getElementIndex()
	{
		StringBuffer sbuffer = new StringBuffer();
		for (int i = 0; i < frameInfoStack.size(); i++)
		{
			FrameInfo frameInfo = frameInfoStack.get(i);
			Integer frameIndex = frameInfo.elementIndex;

			sbuffer.append(frameIndex).append("_");
		}
		
		JRPrintElementIndex imageIndex =
			new JRPrintElementIndex(
					reportIndex,
					pageIndex,
					sbuffer.append(elementIndex).toString()
					);
		return imageIndex;
	}


	protected void writeImageMap(String imageMapName, JRPrintImage image, List<JRPrintImageAreaHyperlink> imageMapAreas) throws IOException
	{
		writer.write("<map name=\"" + imageMapName + "\">\n");

		for (ListIterator<JRPrintImageAreaHyperlink> it = imageMapAreas.listIterator(imageMapAreas.size()); it.hasPrevious();)
		{
			JRPrintImageAreaHyperlink areaHyperlink = it.previous();
			JRPrintImageArea area = areaHyperlink.getArea();

			writer.write("  <area shape=\"" + JRPrintImageArea.getHtmlShape(area.getShape()) + "\"");
			writeImageAreaCoordinates(area.getCoordinates());			
			writeImageAreaHyperlink(areaHyperlink.getHyperlink());
			writer.write("/>\n");
		}
		
		if (image.getHyperlinkTypeValue() != HyperlinkTypeEnum.NONE)
		{
			writer.write("  <area shape=\"default\"");
			writeImageAreaCoordinates(new int[]{0, 0, image.getWidth(), image.getHeight()});//for IE
			writeImageAreaHyperlink(image);
			writer.write("/>\n");
		}
		
		writer.write("</map>\n");
	}

	
	protected void writeImageAreaCoordinates(int[] coords) throws IOException
	{
		if (coords != null && coords.length > 0)
		{
			StringBuffer coordsEnum = new StringBuffer(coords.length * 4);
			coordsEnum.append(toZoom(coords[0]));
			for (int i = 1; i < coords.length; i++)
			{
				coordsEnum.append(',');
				coordsEnum.append(toZoom(coords[i]));
			}
			writer.write(" coords=\"" + coordsEnum + "\"");
		}		
	}


	protected void writeImageAreaHyperlink(JRPrintHyperlink hyperlink) throws IOException
	{
		String href = getHyperlinkURL(hyperlink);
		if (href == null)
		{
			writer.write(" nohref=\"nohref\"");
		}
		else
		{
			writer.write(" href=\"" + href + "\"");
			
			String target = getHyperlinkTarget(hyperlink);
			if (target != null)
			{
				writer.write(" target=\"");
				writer.write(target);
				writer.write("\"");
			}
		}

		if (hyperlink.getHyperlinkTooltip() != null)
		{
			writer.write(" title=\"");
			writer.write(JRStringUtil.xmlEncode(hyperlink.getHyperlinkTooltip()));
			writer.write("\"");
		}
	}


	/**
	 *
	 */
	private boolean appendPadding(StringBuffer sb, Integer padding, String side)
	{
		boolean addedToStyle = false;
		
		if (padding.intValue() > 0)
		{
			sb.append("padding");
			if (side != null)
			{
				sb.append("-");
				sb.append(side);
			}
			sb.append(": ");
			sb.append(toSizeUnit(padding.intValue()));
			sb.append("; ");

			addedToStyle = true;
		}
		
		return addedToStyle;
	}


	/**
	 *
	 */
	private boolean appendPen(StringBuffer sb, JRPen pen, String side)
	{
		boolean addedToStyle = false;
		
		float borderWidth = pen.getLineWidth().floatValue();
		if (0f < borderWidth && borderWidth < 1f)
		{
			borderWidth = 1f;
		}

		String borderStyle = null;
		switch (pen.getLineStyleValue())
		{
			case DOUBLE :
			{
				borderStyle = "double";
				break;
			}
			case DOTTED :
			{
				borderStyle = "dotted";
				break;
			}
			case DASHED :
			{
				borderStyle = "dashed";
				break;
			}
			case SOLID :
			default :
			{
				borderStyle = "solid";
				break;
			}
		}

		if (borderWidth > 0f)
		{
			sb.append("border");
			if (side != null)
			{
				sb.append("-");
				sb.append(side);
			}
			
			sb.append(": ");
			sb.append(toSizeUnit((int)borderWidth));
			
			sb.append(" ");
			sb.append(borderStyle);

			sb.append(" #");
			sb.append(JRColorUtil.getColorHexa(pen.getLineColor()));
			sb.append("; ");

			addedToStyle = true;
		}

		return addedToStyle;
	}


	/**
	 *
	 */
	private void appendId(JRPrintElement element) throws IOException
	{
		String id = getPropertiesUtil().getProperty(element, JRHtmlExporter.PROPERTY_HTML_ID);
		if (id != null)
		{
			writer.write(" id=\"" + id + "\"");
		}
		String clazz = getPropertiesUtil().getProperty(element, JRHtmlExporter.PROPERTY_HTML_CLASS);
		if (clazz != null)
		{
			writer.write(" class=\"" + clazz + "\"");
		}
		String popupId = getPropertiesUtil().getProperty(element, JRHtmlExporter.PROPERTY_HTML_POPUP_ID);
		if (popupId != null)
		{
			writer.write(" data-popupId=\"" + popupId + "\"");
		}
		String popupColumn = getPropertiesUtil().getProperty(element, JRHtmlExporter.PROPERTY_HTML_POPUP_COLUMN);
		if (popupColumn != null)
		{
			writer.write(" data-popupColumn=\"" + popupColumn + "\"");
		}
	}
	
		
	/**
	 *
	 */
	private void setPageLimits(List<JRPrintElement> elements) throws IOException, JRException
	{
		if (elements != null && elements.size() > 0)
		{
			JRPrintElement element;
			for(int i = 0; i < elements.size(); i++)
			{
				elementIndex = i;
				
				element = elements.get(i);
				
				if (filter == null || filter.isToExport(element))
				{
					topLimit = element.getY() < topLimit ? element.getY() : topLimit;
					leftLimit = element.getX() < leftLimit ? element.getX() : leftLimit;
					rightLimit = (element.getX() + element.getWidth()) > rightLimit ? element.getX() + element.getWidth() : rightLimit;
					bottomLimit = (element.getY() + element.getHeight()) > bottomLimit ? element.getY() + element.getHeight() : bottomLimit;
				}
			}
		}
	}

	
	/**
	 *
	 */
	public static String getImageName(JRPrintElementIndex printElementIndex)
	{
		return IMAGE_NAME_PREFIX + printElementIndex.toString();
	}


	/**
	 *
	 */
	public static JRPrintElementIndex getPrintElementIndex(String imageName)
	{
		if (!imageName.startsWith(IMAGE_NAME_PREFIX))
		{
			throw new JRRuntimeException("Invalid image name: " + imageName);
		}

		return JRPrintElementIndex.parsePrintElementIndex(imageName.substring(IMAGE_NAME_PREFIX_LEGTH));
	}


	protected void exportFrame(JRPrintFrame frame) throws IOException, JRException
	{
		writer.write("<div");
		
		appendId(frame);

		StringBuffer styleBuffer = new StringBuffer();
		appendPositionStyle(frame, frame, styleBuffer);
		appendSizeStyle(frame, frame, styleBuffer);
		appendBackcolorStyle(frame, styleBuffer);
		appendBorderStyle(frame.getLineBox(), styleBuffer);

		if (styleBuffer.length() > 0)
		{
			writer.write(" style=\"");//overflow:hidden;");
			writer.write(styleBuffer.toString());
			writer.write("\"");
		}
		
		if (frame.getPropertiesMap() != null && frame.getPropertiesMap().containsProperty(JRHtmlExporter.PROPERTY_HTML_UUID)) {
			writer.write(" data-uuid=\"");
			writer.write(frame.getPropertiesMap().getProperty(JRHtmlExporter.PROPERTY_HTML_UUID));
			writer.write("\"");
			writer.write(" class=\"jrtableframe\"");
		}


		writer.write(">\n");
		
		FrameInfo frameInfo = new FrameInfo();
		frameInfo.elementIndex = elementIndex;
		JRLineBox box = frame.getLineBox();
		if (box != null)
		{
			frameInfo.leftInsideBorderOffset =
				- box.getLeftPadding() + getInsideBorderOffset(box.getLeftPen().getLineWidth().floatValue(), false);
			frameInfo.topInsideBorderOffset =
				- box.getTopPadding() + getInsideBorderOffset(box.getTopPen().getLineWidth().floatValue(), false);
		}
		frameInfoStack.add(frameInfo);

		exportElements(frame.getElements());

		frameInfoStack.remove(frameInfoStack.size() - 1);

		writer.write("</div>\n");
	}


	protected void exportGenericElement(JRGenericPrintElement element) throws IOException
	{
		GenericElementHtmlHandler handler = (GenericElementHtmlHandler) 
				GenericElementHandlerEnviroment.getInstance(getJasperReportsContext()).getElementHandler(
						element.getGenericType(), XHTML_EXPORTER_KEY);
		
		if (handler == null)
		{
			if (log.isDebugEnabled())
			{
				log.debug("No XHTML generic element handler for " 
						+ element.getGenericType());
			}
		}
		else if (handler.toExport(element))
		{
//			writer.write("<div");
//
//			StringBuffer styleBuffer = new StringBuffer();
//
//			appendPositionStyle(element, styleBuffer);
//			appendSizeStyle(element, (JRBoxContainer)null, styleBuffer);
//			appendBackcolorStyle(element, styleBuffer);
//			
//			if (styleBuffer.length() > 0)
//			{
//				writer.write(" style=\"");
//				writer.write(styleBuffer.toString());
//				writer.write("\"");
//			}
//
//			writer.write(">");

			String htmlFragment = handler.getHtmlFragment(exporterContext, element);
			if (htmlFragment != null)
			{
				writer.write(htmlFragment);
			}

//			writer.write("</div>\n");
		}
	}

	public Map<JRExporterParameter,Object> getExportParameters()
	{
		return parameters;
	}

	public String getExportPropertiesPrefix()
	{
		return XHTML_EXPORTER_PROPERTIES_PREFIX;
	}

	public JasperPrint getExportedReport()
	{
		return jasperPrint;
	}


	public String toSizeUnit(int size)
	{
		return String.valueOf(toZoom(size)) + sizeUnit;
	}

	public int toZoom(int size)
	{
		return (int)(zoom * size);
	}


	protected JRStyledText getStyledText(JRPrintText textElement,
			boolean setBackcolor)
	{
		JRStyledText styledText = super.getStyledText(textElement, setBackcolor);
		
		if (styledText != null)
		{
			short[] lineBreakOffsets = textElement.getLineBreakOffsets();
			if (lineBreakOffsets != null && lineBreakOffsets.length > 0)
			{
				//insert new lines at the line break positions saved at fill time
				//cloning the text first
				styledText = styledText.cloneText();
				styledText.insert("\n", lineBreakOffsets);
			}
		}
		
		return styledText;
	}
	
	/**
	 *
	 */
	protected String getExporterKey()
	{
		return XHTML_EXPORTER_KEY;
	}
	
	private static class FrameInfo
	{
		protected int elementIndex;
		protected int leftInsideBorderOffset;
		protected int topInsideBorderOffset;
	}
}

