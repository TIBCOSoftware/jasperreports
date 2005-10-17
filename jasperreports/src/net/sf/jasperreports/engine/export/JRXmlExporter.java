/*
 * ============================================================================
 * GNU Lesser General Public License
 * ============================================================================
 *
 * JasperReports - Free Java report-generating library.
 * Copyright (C) 2001-2005 JasperSoft Corporation http://www.jaspersoft.com
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
 * 185, Berry Street, Suite 6200
 * San Francisco CA 94107
 * http://www.jaspersoft.com
 */

/*
 * Contributors:
 * Joakim Sandström - sanjoa@users.sourceforge.net
 */
package net.sf.jasperreports.engine.export;

import java.awt.Color;
import java.awt.Dimension;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRAbstractExporter;
import net.sf.jasperreports.engine.JRAlignment;
import net.sf.jasperreports.engine.JRAnchor;
import net.sf.jasperreports.engine.JRBox;
import net.sf.jasperreports.engine.JRElement;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JRFont;
import net.sf.jasperreports.engine.JRGraphicElement;
import net.sf.jasperreports.engine.JRHyperlink;
import net.sf.jasperreports.engine.JRImage;
import net.sf.jasperreports.engine.JRImageRenderer;
import net.sf.jasperreports.engine.JRLine;
import net.sf.jasperreports.engine.JRPrintElement;
import net.sf.jasperreports.engine.JRPrintEllipse;
import net.sf.jasperreports.engine.JRPrintGraphicElement;
import net.sf.jasperreports.engine.JRPrintImage;
import net.sf.jasperreports.engine.JRPrintLine;
import net.sf.jasperreports.engine.JRPrintPage;
import net.sf.jasperreports.engine.JRPrintRectangle;
import net.sf.jasperreports.engine.JRPrintText;
import net.sf.jasperreports.engine.JRRenderable;
import net.sf.jasperreports.engine.JRReport;
import net.sf.jasperreports.engine.JRReportFont;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.JRStyle;
import net.sf.jasperreports.engine.JRTextElement;
import net.sf.jasperreports.engine.JRWrappingSvgRenderer;
import net.sf.jasperreports.engine.fill.JRPrintFrame;
import net.sf.jasperreports.engine.util.XmlWriter;
import net.sf.jasperreports.engine.xml.JRXmlConstants;

import org.w3c.tools.codec.Base64Encoder;


/**
 * Exports a JasperReports document to an XML file that contains the same data as a {@link net.sf.jasperreports.engine.JasperPrint}
 * object, but in XML format, instead of a serialized class. Such XML files can be parsed back into <tt>JasperPrint</tt>
 * object using the {@link net.sf.jasperreports.engine.xml.JRPrintXmlLoader} utility class. Their structure is validated
 * against an internal DTD file called jasperprint.dtd
 * 
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JRXmlExporter extends JRAbstractExporter
{

	/**
	 *
	 */
	protected XmlWriter xmlWriter = null;
	protected String encoding = null;
	
	protected JRExportProgressMonitor progressMonitor = null;
	protected Map rendererToImagePathMap = null;
	protected Map imageNameToImageDataMap = null;
	protected Map fontsMap = new HashMap();
	protected Map stylesMap = new HashMap();

	/**
	 *
	 */
	protected String dtdLocation = null;
	protected boolean isEmbeddingImages = true;
	protected File destFile = null;
	protected File imagesDir = null;

	/**
	 *
	 */
	protected static final int colorMask = Integer.parseInt("FFFFFF", 16);
	
	/**
	 * 
	 */
	private static int imageId = 0;


	/**
	 *
	 */
	public void exportReport() throws JRException
	{
		progressMonitor = (JRExportProgressMonitor)parameters.get(JRExporterParameter.PROGRESS_MONITOR);
		
		/*   */
		setOffset();

		/*   */
		setClassLoader();

		/*   */
		setInput();

		/*   */
		setPageRange();

		dtdLocation = (String)parameters.get(JRXmlExporterParameter.DTD_LOCATION);
		if (dtdLocation == null)
		{
			dtdLocation = "http://jasperreports.sourceforge.net/dtds/jasperprint.dtd";
		}
		
		encoding = (String)parameters.get(JRExporterParameter.CHARACTER_ENCODING);
		if (encoding == null)
		{
			encoding = "UTF-8";
		}
		
		StringBuffer sb = (StringBuffer)parameters.get(JRExporterParameter.OUTPUT_STRING_BUFFER);
		if (sb != null)
		{
			StringBuffer buffer = exportReportToBuffer();
			sb.append(buffer.toString());
		}
		else
		{
			Writer outWriter = (Writer)parameters.get(JRExporterParameter.OUTPUT_WRITER);
			if (outWriter != null)
			{
				try
				{
					exportReportToStream(outWriter);
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
						exportReportToStream(new OutputStreamWriter(os, encoding));
					}
					catch (Exception e)
					{
						throw new JRException("Error writing to OutputStream : " + jasperPrint.getName(), e);
					}
				}
				else
				{
					destFile = (File)parameters.get(JRExporterParameter.OUTPUT_FILE);
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
					
					imagesDir = new File(destFile.getParent(), destFile.getName() + "_files");
					
					Boolean isEmbeddingImagesParameter = (Boolean)parameters.get(JRXmlExporterParameter.IS_EMBEDDING_IMAGES);
					if (isEmbeddingImagesParameter == null)
					{
						isEmbeddingImagesParameter = Boolean.TRUE;
					}
					isEmbeddingImages = isEmbeddingImagesParameter.booleanValue();
					
					exportReportToFile();
				}
			}
		}

		/*   */
		resetClassLoader();
	}


	/**
	 *
	 */
	protected void exportReportToFile() throws JRException
	{
		//if (!isEmbeddingImages)
		{
			rendererToImagePathMap = new HashMap();
			imageNameToImageDataMap = new HashMap();
		}
				
		Writer writer = null;
		try
		{
			OutputStream fileOutputStream = new FileOutputStream(destFile);
			writer = new BufferedWriter(new OutputStreamWriter(fileOutputStream, encoding));
			exportReportToStream(writer);
		}
		catch (IOException e)
		{
			throw new JRException("Error writing to file : " + destFile, e);
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
		
		if (!isEmbeddingImages)
		{
			Collection imageNames = imageNameToImageDataMap.keySet();
			if (imageNames != null && imageNames.size() > 0)
			{
				if (!imagesDir.exists())
				{
					imagesDir.mkdir();
				}
	
				for(Iterator it = imageNames.iterator(); it.hasNext();)
				{
					String imageName = (String)it.next();
					byte[] imageData = (byte[])imageNameToImageDataMap.get(imageName);

					File imageFile = new File(imagesDir, imageName);

					OutputStream fos = null;
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
	
	
	/**
	 *
	 */
	protected StringBuffer exportReportToBuffer() throws JRException
	{
		StringWriter buffer = new StringWriter();
		try
		{
			exportReportToStream(buffer);
		}
		catch (IOException e)
		{
			throw new JRException("Error while exporting report to buffer", e);
		}
		return buffer.getBuffer();
	}


	protected void exportReportToStream(Writer writer) throws JRException, IOException
	{
		xmlWriter = new XmlWriter(writer);
		
		xmlWriter.writeProlog(encoding);
		xmlWriter.writePublicDoctype("jasperPrint", "-//JasperReports//DTD Report Design//EN", dtdLocation);

		xmlWriter.startElement("jasperPrint");
		xmlWriter.addAttribute("name", jasperPrint.getName());
		xmlWriter.addAttribute("pageWidth", jasperPrint.getPageWidth());
		xmlWriter.addAttribute("pageHeight", jasperPrint.getPageHeight());
		xmlWriter.addAttribute("orientation", jasperPrint.getOrientation(), JRXmlConstants.getOrientationMap(), JRReport.ORIENTATION_PORTRAIT);
		
		JRReportFont[] fonts = jasperPrint.getFonts();
		if (fonts != null && fonts.length > 0)
		{
			for(int i = 0; i < fonts.length; i++)
			{
				fontsMap.put(fonts[i].getName(), fonts[i]);
				exportReportFont(fonts[i]);
			}
		}
		
		JRStyle[] styles = jasperPrint.getStyles();
		if (styles != null && styles.length > 0)
		{
			for(int i = 0; i < styles.length; i++)
			{
				stylesMap.put(styles[i].getName(), styles[i]);
				exportStyle(styles[i]);
			}
		}


		List pages = jasperPrint.getPages();
		if (pages != null && pages.size() > 0)
		{
			JRPrintPage page = null;
			for(int i = startPageIndex; i <= endPageIndex; i++)
			{
				if (Thread.currentThread().isInterrupted())
				{
					throw new JRException("Current thread interrupted.");
				}
				
				page = (JRPrintPage)pages.get(i);
	
				/*   */
				exportPage(page);
			}
		}

		xmlWriter.closeElement();
		
		writer.flush();
	}


	/**
	 * @throws IOException 
	 *
	 */
	protected void exportReportFont(JRReportFont font) throws IOException
	{
		xmlWriter.startElement("reportFont");
		xmlWriter.addAttribute("name", font.getName());
		xmlWriter.addAttribute("isDefault", font.isDefault());
		xmlWriter.addAttribute("fontName", font.getFontName());
		xmlWriter.addAttribute("size", font.getFontSize());
		xmlWriter.addAttribute("isBold", font.isBold());
		xmlWriter.addAttribute("isItalic", font.isItalic());
		xmlWriter.addAttribute("isUnderline", font.isUnderline());
		xmlWriter.addAttribute("isStrikeThrough", font.isStrikeThrough());
		xmlWriter.addAttribute("pdfFontName", font.getPdfFontName());
		xmlWriter.addAttribute("pdfEncoding", font.getPdfEncoding());
		xmlWriter.addAttribute("isPdfEmbedded", font.isPdfEmbedded());
		xmlWriter.closeElement();
	}


	/**
	 * @throws IOException 
	 */
	protected void exportStyle(JRStyle style) throws IOException
	{
		//FIXME STYLE
		xmlWriter.startElement("style");
		xmlWriter.addAttribute("name", style.getName());
		xmlWriter.addAttribute("isDefault", style.isDefault());
		xmlWriter.addAttribute("fontName", style.getFontName());
		xmlWriter.addAttribute("fontSize", style.getFontSize());
		xmlWriter.addAttribute("isBold", style.isBold());
		xmlWriter.addAttribute("isItalic", style.isItalic());
		xmlWriter.addAttribute("isUnderline", style.isUnderline());
		xmlWriter.addAttribute("isStrikeThrough", style.isStrikeThrough());
		xmlWriter.addAttribute("pdfFontName", style.getPdfFontName());
		xmlWriter.addAttribute("pdfEncoding", style.getPdfEncoding());
		xmlWriter.addAttribute("isPdfEmbedded", style.isPdfEmbedded());
		xmlWriter.closeElement();
	}


	/**
	 * @throws IOException 
	 *
	 */
	protected void exportPage(JRPrintPage page) throws JRException, IOException
	{
		xmlWriter.startElement("page");

		Collection elements = page.getElements();
		exportElements(elements);

		xmlWriter.closeElement();
		
		if (progressMonitor != null)
		{
			progressMonitor.afterPageExport();
		}
	}


	protected void exportElements(Collection elements) throws IOException, JRException
	{
		if (elements != null && elements.size() > 0)
		{
			JRPrintElement element;
			for(Iterator it = elements.iterator(); it.hasNext();)
			{
				element = (JRPrintElement)it.next();
				
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
					exportEllipse((JRPrintEllipse)element);
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
			}
		}
	}


	/**
	 * @throws IOException 
	 * @throws IOException 
	 *
	 */
	protected void exportLine(JRPrintLine line) throws IOException
	{
		xmlWriter.startElement("line");
		xmlWriter.addAttribute("direction", line.getDirection(), JRXmlConstants.getDirectionMap(), JRLine.DIRECTION_TOP_DOWN);

		exportReportElement(line);
		exportGraphicElement(line);
		
		xmlWriter.closeElement();
	}


	/**
	 * @throws IOException 
	 * @throws IOException 
	 *
	 */
	protected void exportReportElement(JRPrintElement element) throws IOException
	{
		xmlWriter.startElement("reportElement");

		if (
			(element instanceof JRPrintLine && element.getMode() != JRElement.MODE_OPAQUE) ||
			(element instanceof JRPrintRectangle && element.getMode() != JRElement.MODE_OPAQUE) ||
			(element instanceof JRPrintEllipse && element.getMode() != JRElement.MODE_OPAQUE) ||
			(element instanceof JRPrintImage && element.getMode() != JRElement.MODE_TRANSPARENT) ||
			(element instanceof JRPrintText && element.getMode() != JRElement.MODE_TRANSPARENT) ||
			(element instanceof JRPrintFrame && element.getMode() != JRElement.MODE_TRANSPARENT)
			)
		{
			xmlWriter.addAttribute("mode", element.getMode(), JRXmlConstants.getModeMap());
		}

		xmlWriter.addAttribute("x", element.getX() + getOffsetX());
		xmlWriter.addAttribute("y", element.getY() + getOffsetY());
		xmlWriter.addAttribute("width", element.getWidth());
		xmlWriter.addAttribute("height", element.getHeight());
		xmlWriter.addAttribute("forecolor", element.getForecolor(), Color.black);
		xmlWriter.addAttribute("backcolor", element.getBackcolor(), Color.white);

		xmlWriter.closeElement();
	}


	/**
	 * @throws IOException 
	 * @throws IOException 
	 *
	 */
	protected void exportGraphicElement(JRPrintGraphicElement element) throws IOException
	{
		xmlWriter.startElement("graphicElement");
		
		if (
			(element instanceof JRPrintLine && element.getPen() != JRGraphicElement.PEN_1_POINT) ||
			(element instanceof JRPrintRectangle && element.getPen() != JRGraphicElement.PEN_1_POINT) ||
			(element instanceof JRPrintEllipse && element.getPen() != JRGraphicElement.PEN_1_POINT) ||
			(element instanceof JRPrintImage && element.getPen() != JRGraphicElement.PEN_NONE)
			)
		{
			xmlWriter.addAttribute("pen", element.getPen(), JRXmlConstants.getPenMap());
		}

		xmlWriter.addAttribute("fill", element.getFill(), JRXmlConstants.getFillMap(), JRGraphicElement.FILL_SOLID);

		xmlWriter.closeElement();
	}


	/**
	 * @throws IOException 
	 * @throws IOException 
	 *
	 */
	protected void exportRectangle(JRPrintRectangle rectangle) throws IOException
	{
		xmlWriter.startElement("rectangle");
		xmlWriter.addAttribute("radius", rectangle.getRadius(), 0);

		exportReportElement(rectangle);
		exportGraphicElement(rectangle);
		
		xmlWriter.closeElement();
	}


	/**
	 * @throws IOException 
	 * @throws IOException 
	 *
	 */
	protected void exportEllipse(JRPrintEllipse ellipse) throws IOException
	{
		xmlWriter.startElement("ellipse");

		exportReportElement(ellipse);
		exportGraphicElement(ellipse);
		
		xmlWriter.closeElement();
	}


	/**
	 * @throws JRException 
	 * @throws IOException 
	 *
	 */
	protected void exportImage(JRPrintImage image) throws JRException, IOException
	{
		xmlWriter.startElement("image");
		xmlWriter.addAttribute("scaleImage", image.getScaleImage(), JRXmlConstants.getScaleImageMap(), JRImage.SCALE_IMAGE_RETAIN_SHAPE);
		xmlWriter.addAttribute("hAlign", image.getHorizontalAlignment(), JRXmlConstants.getHorizontalAlignMap(), JRAlignment.HORIZONTAL_ALIGN_LEFT);
		xmlWriter.addAttribute("vAlign", image.getVerticalAlignment(), JRXmlConstants.getVerticalAlignMap(), JRAlignment.VERTICAL_ALIGN_TOP);
		xmlWriter.addAttribute("isLazy", image.isLazy(), false);
		xmlWriter.addAttribute("onErrorType", image.getOnErrorType(), JRXmlConstants.getOnErrorTypeMap(), JRImage.ON_ERROR_TYPE_ERROR);
		xmlWriter.addAttribute("hyperlinkType", image.getHyperlinkType(), JRXmlConstants.getHyperlinkTypeMap(), JRHyperlink.HYPERLINK_TYPE_NONE);
		xmlWriter.addAttribute("hyperlinkTarget", image.getHyperlinkTarget(), JRXmlConstants.getHyperlinkTargetMap(), JRHyperlink.HYPERLINK_TARGET_SELF);
		xmlWriter.addAttribute("anchorName", image.getAnchorName());
		xmlWriter.addAttribute("hyperlinkReference", image.getHyperlinkReference());
		xmlWriter.addAttribute("hyperlinkAnchor", image.getHyperlinkAnchor());
		xmlWriter.addAttribute("hyperlinkPage", image.getHyperlinkPage());
		xmlWriter.addAttribute("bookmarkLevel", image.getBookmarkLevel(), JRAnchor.NO_BOOKMARK);

		exportReportElement(image);
		exportBox(image);
		exportGraphicElement(image);
		

		JRRenderable renderer = image.getRenderer();
		if (renderer != null)
		{
			xmlWriter.startElement("imageSource");
			xmlWriter.addAttribute("isEmbedded", isEmbeddingImages && !image.isLazy(), false);
	
			String imageSource = "";
			
			if (renderer.getType() == JRRenderable.TYPE_SVG)
			{
				renderer = 
					new JRWrappingSvgRenderer(
						renderer, 
						new Dimension(image.getWidth(), image.getHeight()),
						image.getBackcolor()
						);
			}
				
			if (isEmbeddingImages && !image.isLazy())
			{
				try
				{
					ByteArrayInputStream bais = new ByteArrayInputStream(renderer.getImageData());
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					
					Base64Encoder encoder = new Base64Encoder(bais, baos);
					encoder.process();
					
					imageSource = new String(baos.toByteArray(), "UTF-8");
				}
				catch (IOException e)
				{
					throw new JRException("Error embedding image into XML.", e);
				}
			}
			else
			{
				if (renderer.getType() == JRRenderable.TYPE_IMAGE && rendererToImagePathMap.containsKey(renderer))
				{
					imageSource = (String)rendererToImagePathMap.get(renderer);
				}
				else
				{
					if (image.isLazy())
					{
						imageSource = ((JRImageRenderer)renderer).getImageLocation();
					}
					else
					{
						imageSource = "img_" + getNextImageId();
						imageNameToImageDataMap.put(imageSource, renderer.getImageData());
						
						imageSource = new File(imagesDir, imageSource).getPath();
					}

					rendererToImagePathMap.put(renderer, imageSource);
				}
			}
			
			xmlWriter.writeCDATA(imageSource);
			xmlWriter.closeElement();
		}
		
		xmlWriter.closeElement();
	}


	/**
	 * @throws IOException 
	 *
	 */
	protected void exportText(JRPrintText text) throws IOException
	{
		xmlWriter.startElement("text");
		xmlWriter.addAttribute("textAlignment", text.getHorizontalAlignment(), JRXmlConstants.getHorizontalAlignMap(), JRAlignment.HORIZONTAL_ALIGN_LEFT);

		if (text.getVerticalAlignment() != JRAlignment.VERTICAL_ALIGN_TOP)
		{
			xmlWriter.addAttribute("verticalAlignment", text.getVerticalAlignment(), JRXmlConstants.getVerticalAlignMap());
			xmlWriter.addAttribute("textHeight", text.getTextHeight());
		}

		xmlWriter.addAttribute("rotation", text.getRotation(), JRXmlConstants.getRotationMap(), JRTextElement.ROTATION_NONE);
		xmlWriter.addAttribute("runDirection", text.getRunDirection(), JRXmlConstants.getRunDirectionMap(), JRPrintText.RUN_DIRECTION_LTR);
		xmlWriter.addAttribute("lineSpacing", text.getLineSpacing(), JRXmlConstants.getLineSpacingMap(), JRTextElement.LINE_SPACING_SINGLE);
		xmlWriter.addAttribute("isStyledText", text.isStyledText(), false);
		xmlWriter.addAttribute("lineSpacingFactor", text.getLineSpacingFactor());
		xmlWriter.addAttribute("leadingOffset", text.getLeadingOffset());
		xmlWriter.addAttribute("hyperlinkType", text.getHyperlinkType(), JRXmlConstants.getHyperlinkTypeMap(), JRHyperlink.HYPERLINK_TYPE_NONE);
		xmlWriter.addAttribute("hyperlinkTarget", text.getHyperlinkTarget(), JRXmlConstants.getHyperlinkTargetMap(), JRHyperlink.HYPERLINK_TARGET_SELF);
		xmlWriter.addAttribute("anchorName", text.getAnchorName());
		xmlWriter.addAttribute("hyperlinkReference", text.getHyperlinkReference());
		xmlWriter.addAttribute("hyperlinkAnchor", text.getHyperlinkAnchor());
		xmlWriter.addAttribute("hyperlinkPage", text.getHyperlinkPage());
		xmlWriter.addAttribute("bookmarkLevel", text.getBookmarkLevel(), JRAnchor.NO_BOOKMARK);
		
		exportReportElement(text);
		exportBox(text);

		exportFont(text);//FIXME STYLE test minimum XML output

		if (text.getText() != null)
		{
			xmlWriter.writeCDATAElement("textContent", text.getText());
		}

		xmlWriter.closeElement();
	}


	/**
	 * @throws IOException 
	 * @throws IOException 
	 *
	 */
	private void exportBox(JRBox box) throws IOException
	{
		if (box != null)
		{
			xmlWriter.startElement("box");

			xmlWriter.addAttribute("border", box.getBorder(), JRXmlConstants.getPenMap(), JRGraphicElement.PEN_NONE);
			xmlWriter.addAttribute("borderColor", box.getBorderColor());			
			xmlWriter.addAttributePositive("padding", box.getPadding());
			
			xmlWriter.addAttribute("topBorder", box.getOwnTopBorder(), JRXmlConstants.getPenMap());
			xmlWriter.addAttribute("topBorderColor", box.getOwnTopBorderColor());
			xmlWriter.addAttribute("topPadding", box.getOwnTopPadding());
			
			xmlWriter.addAttribute("leftBorder", box.getOwnLeftBorder(), JRXmlConstants.getPenMap());
			xmlWriter.addAttribute("leftBorderColor", box.getOwnLeftBorderColor());
			xmlWriter.addAttribute("leftPadding", box.getOwnLeftPadding());
			
			xmlWriter.addAttribute("bottomBorder", box.getOwnBottomBorder(), JRXmlConstants.getPenMap());
			xmlWriter.addAttribute("bottomBorderColor", box.getOwnBottomBorderColor());
			xmlWriter.addAttribute("bottomPadding", box.getOwnBottomPadding());

			
			xmlWriter.addAttribute("rightBorder", box.getOwnRightBorder(), JRXmlConstants.getPenMap());
			xmlWriter.addAttribute("rightBorderColor", box.getOwnRightBorderColor());
			xmlWriter.addAttribute("rightPadding", box.getOwnRightPadding());
			
			xmlWriter.closeElement(true);
		}
	}


	/**
	 *
	 */
	protected void exportFont(JRFont font) throws IOException
	{
		if (font != null)
		{
			xmlWriter.startElement("font");

			if(font.getReportFont() != null)
			{
				JRFont baseFont = 
					(JRFont)fontsMap.get(
						font.getReportFont().getName()
						);
				if(baseFont != null)
				{
					xmlWriter.addAttribute("reportFont", font.getReportFont().getName());
				}
				else
				{
					throw 
						new JRRuntimeException(
							"Referenced report font not found : " 
							+ font.getReportFont().getName()
							);
				}
			}
		
			xmlWriter.addAttribute("fontName", font.getOwnFontName());
			xmlWriter.addAttribute("size", font.getOwnFontSize());
			xmlWriter.addAttribute("isBold", font.isOwnBold());
			xmlWriter.addAttribute("isItalic", font.isOwnItalic());
			xmlWriter.addAttribute("isUnderline", font.isOwnUnderline());
			xmlWriter.addAttribute("isStrikeThrough", font.isOwnStrikeThrough());
			xmlWriter.addAttribute("pdfFontName", font.getOwnPdfFontName());
			xmlWriter.addAttribute("pdfEncoding", font.getOwnPdfEncoding());
			xmlWriter.addAttribute("isPdfEmbedded", font.isOwnPdfEmbedded());
			xmlWriter.closeElement(true);
		}
	}
	
	
	protected void exportFrame(JRPrintFrame frame) throws IOException, JRException
	{
		xmlWriter.startElement("frame");
		
		setFrameElementsOffset(frame, true);
		try
		{
			exportReportElement(frame);
			exportBox(frame.getBox());
			exportElements(frame.getElements());

			xmlWriter.closeElement();
		}
		finally
		{
			restoreElementOffsets();
		}
	}
	
	
	/**
	 * 
	 */
	private static synchronized int getNextImageId(){
		return imageId++;
	}
}
