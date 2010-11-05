/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2009 Jaspersoft Corporation. All rights reserved.
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
package net.sf.jasperreports.engine.export.ooxml;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.geom.Dimension2D;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.text.AttributedCharacterIterator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import net.sf.jasperreports.engine.JRAbstractExporter;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JRGenericPrintElement;
import net.sf.jasperreports.engine.JRImageRenderer;
import net.sf.jasperreports.engine.JRPrintElement;
import net.sf.jasperreports.engine.JRPrintElementIndex;
import net.sf.jasperreports.engine.JRPrintEllipse;
import net.sf.jasperreports.engine.JRPrintFrame;
import net.sf.jasperreports.engine.JRPrintHyperlink;
import net.sf.jasperreports.engine.JRPrintImage;
import net.sf.jasperreports.engine.JRPrintLine;
import net.sf.jasperreports.engine.JRPrintPage;
import net.sf.jasperreports.engine.JRPrintRectangle;
import net.sf.jasperreports.engine.JRPrintText;
import net.sf.jasperreports.engine.JRRenderable;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.JRStyle;
import net.sf.jasperreports.engine.JRWrappingSvgRenderer;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.GenericElementHandlerEnviroment;
import net.sf.jasperreports.engine.export.JRExportProgressMonitor;
import net.sf.jasperreports.engine.export.JRHyperlinkProducer;
import net.sf.jasperreports.engine.export.zip.ExportZipEntry;
import net.sf.jasperreports.engine.export.zip.FileBufferedZipEntry;
import net.sf.jasperreports.engine.type.LineDirectionEnum;
import net.sf.jasperreports.engine.type.ModeEnum;
import net.sf.jasperreports.engine.util.JRColorUtil;
import net.sf.jasperreports.engine.util.JRProperties;
import net.sf.jasperreports.engine.util.JRStyledText;
import net.sf.jasperreports.engine.util.JRTypeSniffer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * Exports a JasperReports document to PPTX format.
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id: JRDocxExporter.java 3502 2010-03-04 11:05:10Z teodord $
 */
public class JRPptxExporter extends JRAbstractExporter
{
	private static final Log log = LogFactory.getLog(JRPptxExporter.class);
	
	/**
	 * The exporter key, as used in
	 * {@link GenericElementHandlerEnviroment#getHandler(net.sf.jasperreports.engine.JRGenericElementType, String)}.
	 */
	public static final String PPTX_EXPORTER_KEY = JRProperties.PROPERTY_PREFIX + "pptx";
	
	protected static final String PPTX_EXPORTER_PROPERTIES_PREFIX = JRProperties.PROPERTY_PREFIX + "export.pptx.";

	/**
	 *
	 */
	protected static final String JR_PAGE_ANCHOR_PREFIX = "JR_PAGE_ANCHOR_";

	/**
	 *
	 */
	public static final String IMAGE_NAME_PREFIX = "img_";
	protected static final int IMAGE_NAME_PREFIX_LEGTH = IMAGE_NAME_PREFIX.length();

	/**
	 *
	 */
	protected PptxZip pptxZip;
	protected PptxPresentationHelper presentationHelper;
	protected PptxPresentationRelsHelper presentationRelsHelper;
	protected PptxContentTypesHelper ctHelper;
	protected PptxSlideHelper slideHelper;
	protected PptxSlideRelsHelper slideRelsHelper;
	protected Writer presentationWriter;

	protected JRExportProgressMonitor progressMonitor;
	protected Map rendererToImagePathMap;
	protected Map imageMaps;
	protected List imagesToProcess;
//	protected Map hyperlinksMap;

	protected int reportIndex;
	protected int pageIndex;
	protected List frameIndexStack;
	protected int elementIndex;
	protected boolean startPage;

	/**
	 * used for counting the total number of sheets
	 */
	protected int slideIndex;
	
	/**
	 * @deprecated
	 */
	protected Map fontMap;

	private PptxRunHelper runHelper;


	protected class ExporterContext extends BaseExporterContext implements JRPptxExporterContext
	{
		public ExporterContext()
		{
		}
		
		public PptxSlideHelper getSlideHelper()
		{
			return slideHelper;
		}

		public String getExportPropertiesPrefix()
		{
			return PPTX_EXPORTER_PROPERTIES_PREFIX;
		}
	}
	
	
	public JRPptxExporter()
	{
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
				filter = createFilter(getExporterPropertiesPrefix());
			}

			/*   */
			if (!isModeBatch)
			{
				setPageRange();
			}

			rendererToImagePathMap = new HashMap();
			imageMaps = new HashMap();
			imagesToProcess = new ArrayList();
//			hyperlinksMap = new HashMap();

			fontMap = (Map) parameters.get(JRExporterParameter.FONT_MAP);

			setHyperlinkProducerFactory();

			OutputStream os = (OutputStream)parameters.get(JRExporterParameter.OUTPUT_STREAM);
			if (os != null)
			{
				try
				{
					exportReportToStream(os);
				}
				catch (IOException e)
				{
					throw new JRException("Error trying to export to output stream : " + jasperPrint.getName(), e);
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
					exportReportToStream(os);
				}
				catch (IOException e)
				{
					throw new JRException("Error trying to export to file : " + destFile, e);
				}
				finally
				{
					if (os != null)
					{
						try
						{
							os.close();
						}
						catch(IOException e)
						{
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


	/**
	 *
	 */
	public static JRPrintImage getImage(List jasperPrintList, String imageName)
	{
		return getImage(jasperPrintList, getPrintElementIndex(imageName));
	}


	public static JRPrintImage getImage(List jasperPrintList, JRPrintElementIndex imageIndex)
	{
		JasperPrint report = (JasperPrint)jasperPrintList.get(imageIndex.getReportIndex());
		JRPrintPage page = (JRPrintPage)report.getPages().get(imageIndex.getPageIndex());

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
	protected void exportReportToStream(OutputStream os) throws JRException, IOException
	{
		pptxZip = new PptxZip();

		presentationWriter = pptxZip.getPresentationEntry().getWriter();
		
		presentationHelper = new PptxPresentationHelper(presentationWriter);
		presentationHelper.exportHeader();
		
		presentationRelsHelper = new PptxPresentationRelsHelper(pptxZip.getRelsEntry().getWriter());
		presentationRelsHelper.exportHeader();
		
		ctHelper = new PptxContentTypesHelper(pptxZip.getContentTypesEntry().getWriter());
		ctHelper.exportHeader();

//		DocxStyleHelper styleHelper = 
//			new DocxStyleHelper(
//				null,//pptxZip.getStylesEntry().getWriter(), 
//				fontMap, 
//				getExporterKey()
//				);
//		styleHelper.export(jasperPrintList);
//		styleHelper.close();

		for(reportIndex = 0; reportIndex < jasperPrintList.size(); reportIndex++)
		{
			setJasperPrint((JasperPrint)jasperPrintList.get(reportIndex));

			List pages = jasperPrint.getPages();
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

					page = (JRPrintPage)pages.get(pageIndex);

					createSlide(null);//FIXMEPPTX
					
					slideIndex++;

					exportPage(page);
				}
			}
		}
		
		closeSlide();

		presentationHelper.exportFooter(jasperPrint);
		presentationHelper.close();

		if ((imagesToProcess != null && imagesToProcess.size() > 0))
		{
			for(Iterator it = imagesToProcess.iterator(); it.hasNext();)
			{
				JRPrintElementIndex imageIndex = (JRPrintElementIndex)it.next();

				JRPrintImage image = getImage(jasperPrintList, imageIndex);
				JRRenderable renderer = image.getRenderer();
				if (renderer.getType() == JRRenderable.TYPE_SVG)
				{
					renderer =
						new JRWrappingSvgRenderer(
							renderer,
							new Dimension(image.getWidth(), image.getHeight()),
							ModeEnum.OPAQUE == image.getModeValue() ? image.getBackcolor() : null
							);
				}

				String mimeType = JRTypeSniffer.getImageMimeType(renderer.getImageType());
				if (mimeType == null)
				{
					mimeType = JRRenderable.MIME_TYPE_JPEG;
				}
				String extension = mimeType.substring(mimeType.lastIndexOf('/') + 1);
				
				String imageName = IMAGE_NAME_PREFIX + imageIndex.toString() + "." + extension;
				
				pptxZip.addEntry(//FIXMEPPTX optimize with a different implementation of entry
					new FileBufferedZipEntry(
						"ppt/media/" + imageName,
						renderer.getImageData()
						)
					);
				
				//presentationRelsHelper.exportImage(imageName, extension);
			}
		}

//		if ((hyperlinksMap != null && hyperlinksMap.size() > 0))
//		{
//			for(Iterator it = hyperlinksMap.keySet().iterator(); it.hasNext();)
//			{
//				String href = (String)it.next();
//				String id = (String)hyperlinksMap.get(href);
//
//				relsHelper.exportHyperlink(id, href);
//			}
//		}

		presentationRelsHelper.exportFooter();

		presentationRelsHelper.close();

		ctHelper.exportFooter();
		
		ctHelper.close();

		pptxZip.zipEntries(os);

		pptxZip.dispose();
	}


	/**
	 *
	 */
	protected void exportPage(JRPrintPage page) throws JRException
	{
		frameIndexStack = new ArrayList();

		exportElements(page.getElements());
		
		if (progressMonitor != null)
		{
			progressMonitor.afterPageExport();
		}
	}


	protected void createSlide(String name)
	{
		closeSlide();
		
		presentationHelper.exportSlide(slideIndex + 1);
		ctHelper.exportSlide(slideIndex + 1);
		presentationRelsHelper.exportSlide(slideIndex + 1);

//		pptxZip.addEntry("ppt/slides/_rels/slide" + (slideIndex + 1) + ".xml.rels", "net/sf/jasperreports/engine/export/ooxml/pptx/ppt/slides/_rels/slide1.xml.rels");
		
		ExportZipEntry slideRelsEntry = pptxZip.addSlideRels(slideIndex + 1);
		Writer slideRelsWriter = slideRelsEntry.getWriter();
		slideRelsHelper = new PptxSlideRelsHelper(slideRelsWriter);
		
		ExportZipEntry slideEntry = pptxZip.addSlide(slideIndex + 1);
		Writer slideWriter = slideEntry.getWriter();
		slideHelper = new PptxSlideHelper(slideWriter, slideRelsHelper);

//		cellHelper = new XlsxCellHelper(sheetWriter, styleHelper);
//		
		runHelper = new PptxRunHelper(slideWriter, fontMap, null);//FIXMEXLSX check this null
		
		slideHelper.exportHeader();
		slideRelsHelper.exportHeader();
		
	}


	protected void closeSlide()
	{
		if (slideHelper != null)
		{
			slideHelper.exportFooter();
			
			slideHelper.close();

			slideRelsHelper.exportFooter();
			
			slideRelsHelper.close();
		}
	}
	

	/**
	 *
	 */
	protected void exportElements(List elements) throws JRException
	{
		if (elements != null && elements.size() > 0)
		{
			JRPrintElement element;
			for(int i = 0; i < elements.size(); i++)
			{
				elementIndex = i;
				
				element = (JRPrintElement)elements.get(i);
				
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
						exportFrame((JRPrintFrame)element);
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
	protected void exportLine(JRPrintLine line)
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

		slideHelper.write("<p:sp>\n");
		slideHelper.write("  <p:nvSpPr>\n");
		slideHelper.write("    <p:cNvPr id=\"" + line.hashCode() + "\" name=\"Line\"/>\n");
		slideHelper.write("    <p:cNvSpPr>\n");
		slideHelper.write("      <a:spLocks noGrp=\"1\"/>\n");
		slideHelper.write("    </p:cNvSpPr>\n");
		slideHelper.write("    <p:nvPr/>\n");
		slideHelper.write("  </p:nvSpPr>\n");
		slideHelper.write("  <p:spPr>\n");
		slideHelper.write("    <a:xfrm" + (line.getDirectionValue() == LineDirectionEnum.TOP_DOWN ? " flipV=\"1\"" : "") + ">\n");
		slideHelper.write("      <a:off x=\"" + Utility.emu(x) + "\" y=\"" + Utility.emu(y) + "\"/>\n");
		slideHelper.write("      <a:ext cx=\"" + Utility.emu(width) + "\" cy=\"" + Utility.emu(height) + "\"/>\n");
		slideHelper.write("    </a:xfrm><a:prstGeom prst=\"line\"><a:avLst/></a:prstGeom>\n");
		if (line.getModeValue() == ModeEnum.OPAQUE && line.getBackcolor() != null)
		{
			slideHelper.write("<a:solidFill><a:srgbClr val=\"" + JRColorUtil.getColorHexa(line.getBackcolor()) + "\"/></a:solidFill>\n");
		}
		if (line.getLinePen().getLineWidth() > 0)
		{
			slideHelper.write("  <a:ln w=\"" + Utility.emu(line.getLinePen().getLineWidth()) + "\">\n");
			slideHelper.write("<a:solidFill><a:srgbClr val=\"" + JRColorUtil.getColorHexa(line.getLinePen().getLineColor()) + "\"/></a:solidFill>\n");
			slideHelper.write("<a:prstDash val=\"");
			switch (line.getLinePen().getLineStyleValue())
			{
				case DASHED :
				{
					slideHelper.write("dash");
					break;
				}
				case DOTTED :
				{
					slideHelper.write("dot");
					break;
				}
				case DOUBLE :
				case SOLID :
				default :
				{
					slideHelper.write("solid");
					break;
				}
			}
			slideHelper.write("\"/>\n");
			slideHelper.write("  </a:ln>\n");
		}
		slideHelper.write("  </p:spPr>\n");
		slideHelper.write("  <p:txBody>\n");
		slideHelper.write("    <a:bodyPr rtlCol=\"0\" anchor=\"ctr\"/>\n");
		slideHelper.write("    <a:lstStyle/>\n");
		slideHelper.write("    <a:p>\n");
		slideHelper.write("<a:pPr algn=\"ctr\"/>\n");
		slideHelper.write("    </a:p>\n");
		slideHelper.write("  </p:txBody>\n");
		slideHelper.write("</p:sp>\n");
	}


	/**
	 *
	 */
	protected void exportRectangle(JRPrintRectangle rectangle)
	{
		slideHelper.write("<p:sp>\n");
		slideHelper.write("  <p:nvSpPr>\n");
		slideHelper.write("    <p:cNvPr id=\"" + rectangle.hashCode() + "\" name=\"Rectangle\"/>\n");
		slideHelper.write("    <p:cNvSpPr>\n");
		slideHelper.write("      <a:spLocks noGrp=\"1\"/>\n");
		slideHelper.write("    </p:cNvSpPr>\n");
		slideHelper.write("    <p:nvPr/>\n");
		slideHelper.write("  </p:nvSpPr>\n");
		slideHelper.write("  <p:spPr>\n");
		slideHelper.write("    <a:xfrm>\n");
		slideHelper.write("      <a:off x=\"" + Utility.emu(rectangle.getX() + getOffsetX()) + "\" y=\"" + Utility.emu(rectangle.getY() + getOffsetY()) + "\"/>\n");
		slideHelper.write("      <a:ext cx=\"" + Utility.emu(rectangle.getWidth()) + "\" cy=\"" + Utility.emu(rectangle.getHeight()) + "\"/>\n");
		slideHelper.write("    </a:xfrm><a:prstGeom prst=\"" + (rectangle.getRadius() == 0 ? "rect" : "roundRect") + "\"><a:avLst/></a:prstGeom>\n"); //FIXMEPPTX radius
		if (rectangle.getModeValue() == ModeEnum.OPAQUE && rectangle.getBackcolor() != null)
		{
			slideHelper.write("<a:solidFill><a:srgbClr val=\"" + JRColorUtil.getColorHexa(rectangle.getBackcolor()) + "\"/></a:solidFill>\n");
		}
		if (rectangle.getLinePen().getLineWidth() > 0)
		{
			slideHelper.write("  <a:ln w=\"" + Utility.emu(rectangle.getLinePen().getLineWidth()) + "\">\n");
			slideHelper.write("<a:solidFill><a:srgbClr val=\"" + JRColorUtil.getColorHexa(rectangle.getLinePen().getLineColor()) + "\"/></a:solidFill>\n");
			slideHelper.write("<a:prstDash val=\"");
			switch (rectangle.getLinePen().getLineStyleValue())
			{
				case DASHED :
				{
					slideHelper.write("dash");
					break;
				}
				case DOTTED :
				{
					slideHelper.write("dot");
					break;
				}
				case DOUBLE :
				case SOLID :
				default :
				{
					slideHelper.write("solid");
					break;
				}
			}
			slideHelper.write("\"/>\n");
			slideHelper.write("  </a:ln>\n");
		}
		slideHelper.write("  </p:spPr>\n");
		slideHelper.write("  <p:txBody>\n");
		slideHelper.write("    <a:bodyPr rtlCol=\"0\" anchor=\"ctr\"/>\n");
		slideHelper.write("    <a:lstStyle/>\n");
		slideHelper.write("    <a:p>\n");
		slideHelper.write("<a:pPr algn=\"ctr\"/>\n");
		slideHelper.write("    </a:p>\n");
		slideHelper.write("  </p:txBody>\n");
		slideHelper.write("</p:sp>\n");
	}


	/**
	 *
	 */
	protected void exportEllipse(JRPrintEllipse ellipse)
	{
		slideHelper.write("<p:sp>\n");
		slideHelper.write("  <p:nvSpPr>\n");
		slideHelper.write("    <p:cNvPr id=\"" + ellipse.hashCode() + "\" name=\"Ellipse\"/>\n");
		slideHelper.write("    <p:cNvSpPr>\n");
		slideHelper.write("      <a:spLocks noGrp=\"1\"/>\n");
		slideHelper.write("    </p:cNvSpPr>\n");
		slideHelper.write("    <p:nvPr/>\n");
		slideHelper.write("  </p:nvSpPr>\n");
		slideHelper.write("  <p:spPr>\n");
		slideHelper.write("    <a:xfrm>\n");
		slideHelper.write("      <a:off x=\"" + Utility.emu(ellipse.getX() + getOffsetX()) + "\" y=\"" + Utility.emu(ellipse.getY() + getOffsetY()) + "\"/>\n");
		slideHelper.write("      <a:ext cx=\"" + Utility.emu(ellipse.getWidth()) + "\" cy=\"" + Utility.emu(ellipse.getHeight()) + "\"/>\n");
		slideHelper.write("    </a:xfrm><a:prstGeom prst=\"ellipse\"><a:avLst/></a:prstGeom>\n");
		if (ellipse.getModeValue() == ModeEnum.OPAQUE && ellipse.getBackcolor() != null)
		{
			slideHelper.write("<a:solidFill><a:srgbClr val=\"" + JRColorUtil.getColorHexa(ellipse.getBackcolor()) + "\"/></a:solidFill>\n");
		}
		if (ellipse.getLinePen().getLineWidth() > 0)
		{
			slideHelper.write("  <a:ln w=\"" + Utility.emu(ellipse.getLinePen().getLineWidth()) + "\">\n");
			slideHelper.write("<a:solidFill><a:srgbClr val=\"" + JRColorUtil.getColorHexa(ellipse.getLinePen().getLineColor()) + "\"/></a:solidFill>\n");
			slideHelper.write("<a:prstDash val=\"");
			switch (ellipse.getLinePen().getLineStyleValue())
			{
				case DASHED :
				{
					slideHelper.write("dash");
					break;
				}
				case DOTTED :
				{
					slideHelper.write("dot");
					break;
				}
				case DOUBLE :
				case SOLID :
				default :
				{
					slideHelper.write("solid");
					break;
				}
			}
			slideHelper.write("\"/>\n");
			slideHelper.write("  </a:ln>\n");
		}
		slideHelper.write("  </p:spPr>\n");
		slideHelper.write("  <p:txBody>\n");
		slideHelper.write("    <a:bodyPr rtlCol=\"0\" anchor=\"ctr\"/>\n");
		slideHelper.write("    <a:lstStyle/>\n");
		slideHelper.write("    <a:p>\n");
		slideHelper.write("<a:pPr algn=\"ctr\"/>\n");
		slideHelper.write("    </a:p>\n");
		slideHelper.write("  </p:txBody>\n");
		slideHelper.write("</p:sp>\n");
	}


	/**
	 *
	 */
	public void exportText(JRPrintText text)
	{
		JRStyledText styledText = getStyledText(text);

		int textLength = 0;

		if (styledText != null)
		{
			textLength = styledText.length();
		}
	
		int x = 0;
		int y = 0;
		int width = 0;
		int height = 0;
		int rotation = 0;
		
		int leftPadding = text.getLineBox().getLeftPadding();
		int topPadding = text.getLineBox().getTopPadding();
		int rightPadding = text.getLineBox().getRightPadding();
		int bottomPadding = text.getLineBox().getBottomPadding();

		switch (text.getRotationValue())
		{
			case LEFT:
			{
				rotation = -5400000;
				x = text.getX() + getOffsetX() - (text.getHeight() - text.getWidth()) / 2;
				y = text.getY() + getOffsetY() + (text.getHeight() - text.getWidth()) / 2;
				width = text.getHeight();
				height = text.getWidth();
				int tmpPadding = topPadding;
				topPadding = leftPadding;
				leftPadding = bottomPadding;
				bottomPadding = rightPadding;
				rightPadding = tmpPadding;
				break;
			}
			case RIGHT:
			{
				rotation = 5400000;
				x = text.getX() + getOffsetX() - (text.getHeight() - text.getWidth()) / 2;
				y = text.getY() + getOffsetY() + (text.getHeight() - text.getWidth()) / 2;
				width = text.getHeight();
				height = text.getWidth();
				int tmpPadding = topPadding;
				topPadding = rightPadding;
				rightPadding = bottomPadding;
				bottomPadding = leftPadding;
				leftPadding = tmpPadding;
				break;
			}
			case UPSIDE_DOWN:
			{
				rotation = 10800000;
				x = text.getX() + getOffsetX();
				y = text.getY() + getOffsetY();
				width = text.getWidth();
				height = text.getHeight();
				int tmpPadding = topPadding;
				topPadding = bottomPadding;
				bottomPadding = tmpPadding;
				tmpPadding = leftPadding;
				leftPadding = rightPadding;
				rightPadding = tmpPadding;
				break;
			}
			case NONE:
			default:
			{
				rotation = 0;
				x = text.getX() + getOffsetX();
				y = text.getY() + getOffsetY();
				width = text.getWidth();
				height = text.getHeight();
				break;
			}
		}
		
		slideHelper.write("<p:sp>\n");
		slideHelper.write("  <p:nvSpPr>\n");
		slideHelper.write("    <p:cNvPr id=\"" + text.hashCode() + "\" name=\"Text\">\n");
		
		String href = getHyperlinkURL(text);
		if (href != null)
		{
			slideHelper.exportHyperlink(href);
		}

		slideHelper.write("    </p:cNvPr>\n");
		slideHelper.write("    <p:cNvSpPr>\n");
		slideHelper.write("      <a:spLocks noGrp=\"1\"/>\n");
		slideHelper.write("    </p:cNvSpPr>\n");
		slideHelper.write("    <p:nvPr/>\n");
		slideHelper.write("  </p:nvSpPr>\n");
		slideHelper.write("  <p:spPr>\n");
		slideHelper.write("    <a:xfrm rot=\"" + rotation + "\">\n");
		slideHelper.write("      <a:off x=\"" + Utility.emu(x) + "\" y=\"" + Utility.emu(y) + "\"/>\n");
		slideHelper.write("      <a:ext cx=\"" + Utility.emu(width) + "\" cy=\"" + Utility.emu(height) + "\"/>\n");
		slideHelper.write("    </a:xfrm><a:prstGeom prst=\"rect\"><a:avLst/></a:prstGeom>\n");
		if (text.getModeValue() == ModeEnum.OPAQUE && text.getBackcolor() != null)
		{
			slideHelper.write("<a:solidFill><a:srgbClr val=\"" + JRColorUtil.getColorHexa(text.getBackcolor()) + "\"/></a:solidFill>\n");
		}
		if (text.getLineBox().getPen().getLineWidth() > 0)
		{
			slideHelper.write("  <a:ln w=\"" + Utility.emu(text.getLineBox().getPen().getLineWidth()) + "\">\n");
			slideHelper.write("<a:solidFill><a:srgbClr val=\"" + JRColorUtil.getColorHexa(text.getLineBox().getPen().getLineColor()) + "\"/></a:solidFill>\n");
			slideHelper.write("<a:prstDash val=\"");
			switch (text.getLineBox().getPen().getLineStyleValue())
			{
				case DASHED :
				{
					slideHelper.write("dash");
					break;
				}
				case DOTTED :
				{
					slideHelper.write("dot");
					break;
				}
				case DOUBLE :
				case SOLID :
				default :
				{
					slideHelper.write("solid");
					break;
				}
			}
			slideHelper.write("\"/>\n");
			slideHelper.write("  </a:ln>\n");
		}
		slideHelper.write("  </p:spPr>\n");
		slideHelper.write("  <p:txBody>\n");
		slideHelper.write("    <a:bodyPr wrap=\"square\" lIns=\"" +
				Utility.emu(leftPadding) +
				"\" tIns=\"" +
				Utility.emu(topPadding) +
				"\" rIns=\"" +
				Utility.emu(rightPadding) +
				"\" bIns=\"" +
				Utility.emu(bottomPadding) +
				"\" rtlCol=\"0\" anchor=\"");
		switch (text.getVerticalAlignmentValue())
		{
			case TOP:
				slideHelper.write("t");
				break;
			case MIDDLE:
				slideHelper.write("ctr");
				break;
			case BOTTOM:
				slideHelper.write("b");
				break;
			default:
				slideHelper.write("t");
				break;
		}
		slideHelper.write("\"/>\n");
		slideHelper.write("    <a:lstStyle/>\n");

//		if (styleBuffer.length() > 0)
//		{
//			writer.write(" style=\"");
//			writer.write(styleBuffer.toString());
//			writer.write("\"");
//		}
//
//		writer.write(">");
		slideHelper.write("    <a:p>\n");

		slideHelper.write("<a:pPr");
		slideHelper.write(" algn=\"");
		switch (text.getHorizontalAlignmentValue())
		{
			case LEFT:
				slideHelper.write("l");
				break;
			case CENTER:
				slideHelper.write("ctr");
				break;
			case RIGHT:
				slideHelper.write("r");
				break;
			case JUSTIFIED:
				slideHelper.write("just");
				break;
			default:
				slideHelper.write("l");
				break;
		}
		slideHelper.write("\">\n");
		slideHelper.write("<a:lnSpc><a:spcPct");
		slideHelper.write(" val=\"");
		switch (text.getLineSpacingValue())
		{
			case DOUBLE:
				slideHelper.write("200");
				break;
			case ONE_AND_HALF:
				slideHelper.write("150");
				break;
			case SINGLE:
			default:
				slideHelper.write("100");
				break;
		}
		slideHelper.write("%\"/></a:lnSpc>\n");
		runHelper.exportProps(text, getTextLocale(text));
		slideHelper.write("</a:pPr>\n");
		
//		insertPageAnchor();
//		if (text.getAnchorName() != null)
//		{
//			tempBodyWriter.write("<text:bookmark text:name=\"");
//			tempBodyWriter.write(text.getAnchorName());
//			tempBodyWriter.write("\"/>");
//		}

//		boolean startedHyperlink = startHyperlink(text, true);

		if (textLength > 0)
		{
			exportStyledText(text.getStyle(), styledText, getTextLocale(text));
		}

//		if (startedHyperlink)
//		{
//			endHyperlink(true);
//		}

		slideHelper.write("    </a:p>\n");
//		docHelper.write("     </w:p>\n");
//		docHelper.flush();

		slideHelper.write("  </p:txBody>\n");
		slideHelper.write("</p:sp>\n");
	}


	/**
	 *
	 */
	protected void exportStyledText(JRStyle style, JRStyledText styledText, Locale locale)
	{
		String text = styledText.getText();

		int runLimit = 0;

		AttributedCharacterIterator iterator = styledText.getAttributedString().getIterator();

		while(runLimit < styledText.length() && (runLimit = iterator.getRunLimit()) <= styledText.length())
		{
			runHelper.export(
				style, 
				iterator.getAttributes(), 
				text.substring(iterator.getIndex(), runLimit),
				locale
				);

			iterator.setIndex(runLimit);
		}
	}


	/**
	 *
	 */
	protected void exportImage(JRPrintImage image) throws JRException
	{
		int leftPadding = image.getLineBox().getLeftPadding().intValue();
		int topPadding = image.getLineBox().getTopPadding().intValue();//FIXMEDOCX maybe consider border thickness
		int rightPadding = image.getLineBox().getRightPadding().intValue();
		int bottomPadding = image.getLineBox().getBottomPadding().intValue();

		int availableImageWidth = image.getWidth() - leftPadding - rightPadding;
		availableImageWidth = availableImageWidth < 0 ? 0 : availableImageWidth;

		int availableImageHeight = image.getHeight() - topPadding - bottomPadding;
		availableImageHeight = availableImageHeight < 0 ? 0 : availableImageHeight;

		JRRenderable renderer = image.getRenderer();

		if (
			renderer != null &&
			availableImageWidth > 0 &&
			availableImageHeight > 0
			)
		{
			if (renderer.getType() == JRRenderable.TYPE_IMAGE)
			{
				// Non-lazy image renderers are all asked for their image data at some point.
				// Better to test and replace the renderer now, in case of lazy load error.
				renderer = JRImageRenderer.getOnErrorRendererForImageData(renderer, image.getOnErrorTypeValue());
			}
		}
		else
		{
			renderer = null;
		}

		if (renderer != null)
		{
			int width = availableImageWidth;
			int height = availableImageHeight;

			double normalWidth = availableImageWidth;
			double normalHeight = availableImageHeight;

			// Image load might fail.
			JRRenderable tmpRenderer =
				JRImageRenderer.getOnErrorRendererForDimension(renderer, image.getOnErrorTypeValue());
			Dimension2D dimension = tmpRenderer == null ? null : tmpRenderer.getDimension();
			// If renderer was replaced, ignore image dimension.
			if (tmpRenderer == renderer && dimension != null)
			{
				normalWidth = dimension.getWidth();
				normalHeight = dimension.getHeight();
			}

			double cropTop = 0;
			double cropLeft = 0;
			double cropBottom = 0;
			double cropRight = 0;
			
			switch (image.getScaleImageValue())
			{
				case FILL_FRAME :
				{
					width = availableImageWidth;
					height = availableImageHeight;
//					cropTop = 100000 * topPadding / availableImageHeight;
//					cropLeft = 100000 * leftPadding / availableImageWidth;
//					cropBottom = 100000 * bottomPadding / availableImageHeight;
//					cropRight = 100000 * rightPadding / availableImageWidth;
 					break;
				}
				case CLIP :
				{
//					if (normalWidth > availableImageWidth)
//					{
						switch (image.getHorizontalAlignmentValue())
						{
							case RIGHT :
							{
								cropLeft = 100000 * (availableImageWidth - normalWidth) / availableImageWidth;
								cropRight = 0;
//								cropRight = 100000 * rightPadding / availableImageWidth;
								break;
							}
							case CENTER :
							{
								cropLeft = 100000 * (availableImageWidth - normalWidth) / availableImageWidth / 2;
								cropRight = cropLeft;
								break;
							}
							case LEFT :
							default :
							{
//								cropLeft = 100000 * leftPadding / availableImageWidth;
								cropLeft = 0;
								cropRight = 100000 * (availableImageWidth - normalWidth) / availableImageWidth;
								break;
							}
						}
//						width = availableImageWidth;
////						cropLeft = cropLeft / 0.75d;
////						cropRight = cropRight / 0.75d;
//					}
//					else
//					{
//						width = (int)normalWidth;
//					}

//					if (normalHeight > availableImageHeight)
//					{
						switch (image.getVerticalAlignmentValue())
						{
							case TOP :
							{
//								cropTop = 100000 * topPadding / availableImageHeight;
								cropTop = 0;
								cropBottom = 100000 * (availableImageHeight - normalHeight) / availableImageHeight;
								break;
							}
							case MIDDLE :
							{
								cropTop = 100000 * (availableImageHeight - normalHeight) / availableImageHeight / 2;
								cropBottom = cropTop;
								break;
							}
							case BOTTOM :
							default :
							{
								cropTop = 100000 * (availableImageHeight - normalHeight) / availableImageHeight;
								cropBottom = 0;
//								cropBottom = 100000 * bottomPadding / availableImageHeight;
								break;
							}
						}
//						height = availableImageHeight;
//						cropTop = cropTop / 0.75d;
//						cropBottom = cropBottom / 0.75d;
//					}
//					else
//					{
//						height = (int)normalHeight;
//					}

					break;
				}
				case RETAIN_SHAPE :
				default :
				{
					if (availableImageHeight > 0)
					{
						double ratio = normalWidth / normalHeight;

						if( ratio > availableImageWidth / (double)availableImageHeight )
						{
							width = availableImageWidth;
							height = (int)(width/ratio);

							switch (image.getVerticalAlignmentValue())
							{
								case TOP :
								{
									cropTop = 0;
									cropBottom = 100000 * (availableImageHeight - height) / availableImageHeight;
									break;
								}
								case MIDDLE :
								{
									cropTop = 100000 * (availableImageHeight - height) / availableImageHeight / 2;
									cropBottom = cropTop;
									break;
								}
								case BOTTOM :
								default :
								{
									cropTop = 100000 * (availableImageHeight - height) / availableImageHeight;
									cropBottom = 0;
									break;
								}
							}
						}
						else
						{
							height = availableImageHeight;
							width = (int)(ratio * height);

							switch (image.getHorizontalAlignmentValue())
							{
								case RIGHT :
								{
									cropLeft = 100000 * (availableImageWidth - width) / availableImageWidth;
									cropRight = 0;
									break;
								}
								case CENTER :
								{
									cropLeft = 100000 * (availableImageWidth - width) / availableImageWidth / 2;
									cropRight = cropLeft;
									break;
								}
								case LEFT :
								default :
								{
									cropLeft = 0;
									cropRight = 100000 * (availableImageWidth - width) / availableImageWidth;
									break;
								}
							}
						}
					}
				}
			}

//			insertPageAnchor();
//			if (image.getAnchorName() != null)
//			{
//				tempBodyWriter.write("<text:bookmark text:name=\"");
//				tempBodyWriter.write(image.getAnchorName());
//				tempBodyWriter.write("\"/>");
//			}


//			boolean startedHyperlink = startHyperlink(image,false);

			String imageName = getImagePath(renderer, image.isLazy());
			slideRelsHelper.exportImage(imageName);

			slideHelper.write("<p:pic>\n");
			slideHelper.write("  <p:nvPicPr>\n");
			slideHelper.write("    <p:cNvPr id=\"" + (image.hashCode() > 0 ? image.hashCode() : -image.hashCode()) + "\" name=\"Picture\">\n");

			String href = getHyperlinkURL(image);
			if (href != null)
			{
				slideHelper.exportHyperlink(href);
			}
			
			slideHelper.write("    </p:cNvPr>\n");
			slideHelper.write("    <p:cNvPicPr>\n");
			slideHelper.write("      <a:picLocks noChangeAspect=\"1\"/>\n");
			slideHelper.write("    </p:cNvPicPr>\n");
			slideHelper.write("    <p:nvPr/>\n");
			slideHelper.write("  </p:nvPicPr>\n");
			slideHelper.write("<p:blipFill>\n");
			slideHelper.write("<a:blip r:embed=\"" + imageName + "\"/>");
			slideHelper.write("<a:srcRect");
////			if (cropLeft > 0)
////			{
//				slideHelper.write(" l=\"" + (int)(100000 * leftPadding / image.getWidth()) + "\"");
////			}
////			if (cropTop > 0)
////			{
//				slideHelper.write(" t=\"" + (int)cropTop + "\"");
////			}
////			if (cropRight > 0)
////			{
//				slideHelper.write(" r=\"" + (int)cropRight + "\"");
////			}
////			if (cropBottom > 0)
////			{
//				slideHelper.write(" b=\"" + (int)cropBottom + "\"");
////			}
			slideHelper.write("/>");
			slideHelper.write("<a:stretch><a:fillRect");
//			if (cropLeft > 0)
//			{
				slideHelper.write(" l=\"" + (int)cropLeft + "\"");
//			}
//			if (cropTop > 0)
//			{
				slideHelper.write(" t=\"" + (int)cropTop + "\"");
//			}
//			if (cropRight > 0)
//			{
				slideHelper.write(" r=\"" + (int)cropRight + "\"");
//			}
//			if (cropBottom > 0)
//			{
				slideHelper.write(" b=\"" + (int)cropBottom + "\"");
//			}
			slideHelper.write("/></a:stretch>\n");
			slideHelper.write("</p:blipFill>\n");
			slideHelper.write("  <p:spPr>\n");
			slideHelper.write("    <a:xfrm>\n");
			slideHelper.write("      <a:off x=\"" + Utility.emu(image.getX() + getOffsetX()) + "\" y=\"" + Utility.emu(image.getY() + getOffsetY()) + "\"/>\n");
			slideHelper.write("      <a:ext cx=\"" + Utility.emu(image.getWidth()) + "\" cy=\"" + Utility.emu(image.getHeight()) + "\"/>\n");
			slideHelper.write("    </a:xfrm><a:prstGeom prst=\"rect\"><a:avLst/></a:prstGeom>\n");
			if (image.getModeValue() == ModeEnum.OPAQUE && image.getBackcolor() != null)
			{
				slideHelper.write("<a:solidFill><a:srgbClr val=\"" + JRColorUtil.getColorHexa(image.getBackcolor()) + "\"/></a:solidFill>\n");
			}
			if (image.getLineBox().getPen().getLineWidth() > 0)
			{
				slideHelper.write("  <a:ln w=\"" + Utility.emu(image.getLineBox().getPen().getLineWidth()) + "\">\n");
				slideHelper.write("<a:solidFill><a:srgbClr val=\"" + JRColorUtil.getColorHexa(image.getLineBox().getPen().getLineColor()) + "\"/></a:solidFill>\n");
				slideHelper.write("<a:prstDash val=\"");
				switch (image.getLineBox().getPen().getLineStyleValue())
				{
					case DASHED :
					{
						slideHelper.write("dash");
						break;
					}
					case DOTTED :
					{
						slideHelper.write("dot");
						break;
					}
					case DOUBLE :
					case SOLID :
					default :
					{
						slideHelper.write("solid");
						break;
					}
				}
				slideHelper.write("\"/>\n");
				slideHelper.write("  </a:ln>\n");
			}
			slideHelper.write("  </p:spPr>\n");
			slideHelper.write("  </p:pic>\n");

//			if(startedHyperlink)
//			{
//				endHyperlink(false);
//			}
		}

//		docHelper.write("</w:p>");
	}


	/**
	 *
	 */
	protected String getImagePath(JRRenderable renderer, boolean isLazy)
	{
		String imagePath = null;

		if (renderer != null)
		{
			if (renderer.getType() == JRRenderable.TYPE_IMAGE && rendererToImagePathMap.containsKey(renderer.getId()))
			{
				imagePath = (String)rendererToImagePathMap.get(renderer.getId());
			}
			else
			{
//				if (isLazy)//FIXMEDOCX learn how to link images
//				{
//					imagePath = ((JRImageRenderer)renderer).getImageLocation();
//				}
//				else
//				{
					JRPrintElementIndex imageIndex = getElementIndex();
					imagesToProcess.add(imageIndex);

					String mimeType = JRTypeSniffer.getImageMimeType(renderer.getImageType());//FIXMEPPTX this code for file extension is duplicated
					if (mimeType == null)
					{
						mimeType = JRRenderable.MIME_TYPE_JPEG;
					}
					String extension = mimeType.substring(mimeType.lastIndexOf('/') + 1);

					String imageName = IMAGE_NAME_PREFIX + imageIndex.toString() + "." + extension;
					imagePath = imageName;
					//imagePath = "Pictures/" + imageName;
//				}

				rendererToImagePathMap.put(renderer.getId(), imagePath);
			}
		}

		return imagePath;
	}


	protected JRPrintElementIndex getElementIndex()
	{
		StringBuffer sbuffer = new StringBuffer();
		for (int i = 0; i < frameIndexStack.size(); i++)
		{
			Integer frameIndex = (Integer)frameIndexStack.get(i);

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


	/**
	 *
	 *
	protected void writeImageMap(String imageMapName, JRPrintHyperlink mainHyperlink, List imageMapAreas)
	{
		writer.write("<map name=\"" + imageMapName + "\">\n");

		for (Iterator it = imageMapAreas.iterator(); it.hasNext();)
		{
			JRPrintImageAreaHyperlink areaHyperlink = (JRPrintImageAreaHyperlink) it.next();
			JRPrintImageArea area = areaHyperlink.getArea();

			writer.write("  <area shape=\"" + JRPrintImageArea.getHtmlShape(area.getShape()) + "\"");
			writeImageAreaCoordinates(area);
			writeImageAreaHyperlink(areaHyperlink.getHyperlink());
			writer.write("/>\n");
		}

		if (mainHyperlink.getHyperlinkTypeValue() != NONE)
		{
			writer.write("  <area shape=\"default\"");
			writeImageAreaHyperlink(mainHyperlink);
			writer.write("/>\n");
		}

		writer.write("</map>\n");
	}


	protected void writeImageAreaCoordinates(JRPrintImageArea area)
	{
		int[] coords = area.getCoordinates();
		if (coords != null && coords.length > 0)
		{
			StringBuffer coordsEnum = new StringBuffer(coords.length * 4);
			coordsEnum.append(coords[0]);
			for (int i = 1; i < coords.length; i++)
			{
				coordsEnum.append(',');
				coordsEnum.append(coords[i]);
			}

			writer.write(" coords=\"" + coordsEnum + "\"");
		}
	}


	protected void writeImageAreaHyperlink(JRPrintHyperlink hyperlink)
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
	public static JRPrintElementIndex getPrintElementIndex(String imageName)
	{
		if (!imageName.startsWith(IMAGE_NAME_PREFIX))
		{
			throw new JRRuntimeException("Invalid image name: " + imageName);
		}

		return JRPrintElementIndex.parsePrintElementIndex(imageName.substring(IMAGE_NAME_PREFIX_LEGTH));
	}


	/**
	 *
	 */
	protected void exportFrame(JRPrintFrame frame) throws JRException
	{
		slideHelper.write("<p:sp>\n");
		slideHelper.write("  <p:nvSpPr>\n");
		slideHelper.write("    <p:cNvPr id=\"" + frame.hashCode() + "\" name=\"Frame\"/>\n");
		slideHelper.write("    <p:cNvSpPr>\n");
		slideHelper.write("      <a:spLocks noGrp=\"1\"/>\n");
		slideHelper.write("    </p:cNvSpPr>\n");
		slideHelper.write("    <p:nvPr/>\n");
		slideHelper.write("  </p:nvSpPr>\n");
		slideHelper.write("  <p:spPr>\n");
		slideHelper.write("    <a:xfrm>\n");
		slideHelper.write("      <a:off x=\"" + Utility.emu(frame.getX() + getOffsetX()) + "\" y=\"" + Utility.emu(frame.getY() + getOffsetY()) + "\"/>\n");
		slideHelper.write("      <a:ext cx=\"" + Utility.emu(frame.getWidth()) + "\" cy=\"" + Utility.emu(frame.getHeight()) + "\"/>\n");
		slideHelper.write("    </a:xfrm><a:prstGeom prst=\"rect\"><a:avLst/></a:prstGeom>\n");
		if (frame.getModeValue() == ModeEnum.OPAQUE && frame.getBackcolor() != null)
		{
			slideHelper.write("<a:solidFill><a:srgbClr val=\"" + JRColorUtil.getColorHexa(frame.getBackcolor()) + "\"/></a:solidFill>\n");
		}
		if (frame.getLineBox().getPen().getLineWidth() > 0)
		{
			slideHelper.write("  <a:ln w=\"" + Utility.emu(frame.getLineBox().getPen().getLineWidth()) + "\">\n");
			slideHelper.write("<a:solidFill><a:srgbClr val=\"" + JRColorUtil.getColorHexa(frame.getLineBox().getPen().getLineColor()) + "\"/></a:solidFill>\n");
			slideHelper.write("<a:prstDash val=\"");
			switch (frame.getLineBox().getPen().getLineStyleValue())
			{
				case DASHED :
				{
					slideHelper.write("dash");
					break;
				}
				case DOTTED :
				{
					slideHelper.write("dot");
					break;
				}
				case DOUBLE :
				case SOLID :
				default :
				{
					slideHelper.write("solid");
					break;
				}
			}
			slideHelper.write("\"/>\n");
			slideHelper.write("  </a:ln>\n");
		}
		slideHelper.write("  </p:spPr>\n");
		slideHelper.write("  <p:txBody>\n");
		slideHelper.write("    <a:bodyPr rtlCol=\"0\" anchor=\"ctr\"/>\n");
		slideHelper.write("    <a:lstStyle/>\n");
		slideHelper.write("    <a:p>\n");
		slideHelper.write("<a:pPr algn=\"ctr\"/>\n");
		slideHelper.write("    </a:p>\n");
		slideHelper.write("  </p:txBody>\n");
		slideHelper.write("</p:sp>\n");

		setFrameElementsOffset(frame, false);

		frameIndexStack.add(Integer.valueOf(elementIndex));

		exportElements(frame.getElements());

		frameIndexStack.remove(frameIndexStack.size() - 1);
		
		restoreElementOffsets();
	}


	/**
	 *
	 */
	protected void exportGenericElement(JRGenericPrintElement element)
	{
		GenericElementPptxHandler handler = (GenericElementPptxHandler) 
		GenericElementHandlerEnviroment.getHandler(
				element.getGenericType(), PPTX_EXPORTER_KEY);

		if (handler != null)
		{
			JRPptxExporterContext exporterContext = new ExporterContext();

			handler.exportElement(exporterContext, element);
		}
		else
		{
			if (log.isDebugEnabled())
			{
				log.debug("No PPTX generic element handler for " 
						+ element.getGenericType());
			}
		}
	}


//	private float getXAlignFactor(JRPrintImage image)
//	{
//		float xalignFactor = 0f;
//		switch (image.getHorizontalAlignmentValue())
//		{
//			case RIGHT :
//			{
//				xalignFactor = 1f;
//				break;
//			}
//			case CENTER :
//			{
//				xalignFactor = 0.5f;
//				break;
//			}
//			case LEFT :
//			default :
//			{
//				xalignFactor = 0f;
//				break;
//			}
//		}
//		return xalignFactor;
//	}


//	private float getYAlignFactor(JRPrintImage image)
//	{
//		float yalignFactor = 0f;
//		switch (image.getVerticalAlignmentValue())
//		{
//			case BOTTOM :
//			{
//				yalignFactor = 1f;
//				break;
//			}
//			case MIDDLE :
//			{
//				yalignFactor = 0.5f;
//				break;
//			}
//			case TOP :
//			default :
//			{
//				yalignFactor = 0f;
//				break;
//			}
//		}
//		return yalignFactor;
//	}

//	protected boolean startHyperlink(JRPrintHyperlink link, boolean isText)
//	{
//		String href = getHyperlinkURL(link);
//
////		if (href != null)
////		{
////			String id = (String)hyperlinksMap.get(href);
////			if (id == null)
////			{
////				id = "link" + hyperlinksMap.size();
////				hyperlinksMap.put(href, id);
////			}
////			
////			docHelper.write("<w:hyperlink r:id=\"" + id + "\"");
////
////			String target = getHyperlinkTarget(link);//FIXMETARGET
////			if (target != null)
////			{
////				docHelper.write(" tgtFrame=\"" + target + "\"");
////			}
////
////			docHelper.write(">\n");
//
////			docHelper.write("<w:r><w:fldChar w:fldCharType=\"begin\"/></w:r>\n");
////			docHelper.write("<w:r><w:instrText xml:space=\"preserve\"> HYPERLINK \"" + JRStringUtil.xmlEncode(href) + "\"");
////
////			String target = getHyperlinkTarget(link);//FIXMETARGET
////			if (target != null)
////			{
////				docHelper.write(" \\t \"" + target + "\"");
////			}
////
////			String tooltip = link.getHyperlinkTooltip(); 
////			if (tooltip != null)
////			{
////				docHelper.write(" \\o \"" + JRStringUtil.xmlEncode(tooltip) + "\"");
////			}
////
////			docHelper.write(" </w:instrText></w:r>\n");
////			docHelper.write("<w:r><w:fldChar w:fldCharType=\"separate\"/></w:r>\n");
////		}
//
//		return href != null;
//	}


	protected String getHyperlinkTarget(JRPrintHyperlink link)
	{
		String target = null;
		switch(link.getHyperlinkTargetValue())
		{
			case SELF :
			{
				target = "_self";
				break;
			}
			case BLANK :
			default :
			{
				target = "_blank";
				break;
			}
		}
		return target;
	}


	protected String getHyperlinkURL(JRPrintHyperlink link)
	{
		String href = null;
		JRHyperlinkProducer customHandler = getCustomHandler(link);
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
//					if (link.getHyperlinkAnchor() != null)
//					{
//						href = "#" + link.getHyperlinkAnchor();
//					}
					break;
				}
				case LOCAL_PAGE :
				{
//					if (link.getHyperlinkPage() != null)
//					{
//						href = "#" + JR_PAGE_ANCHOR_PREFIX + reportIndex + "_" + link.getHyperlinkPage().toString();
//					}
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
//					if (
//						link.getHyperlinkReference() != null &&
//						link.getHyperlinkPage() != null
//						)
//					{
//						href = link.getHyperlinkReference() + "#" + JR_PAGE_ANCHOR_PREFIX + "0_" + link.getHyperlinkPage().toString();
//					}
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

		return href;
	}


//	protected void endHyperlink(boolean isText)
//	{
////		docHelper.write("</w:hyperlink>\n");
////		docHelper.write("<w:r><w:fldChar w:fldCharType=\"end\"/></w:r>\n");
//	}

//	protected void insertPageAnchor()
//	{
//		if(startPage)
//		{
//			tempBodyWriter.write("<text:bookmark text:name=\"");
//			tempBodyWriter.write(JR_PAGE_ANCHOR_PREFIX + reportIndex + "_" + (pageIndex + 1));
//			tempBodyWriter.write("\"/>\n");
//			startPage = false;
//		}
//	}
	
	/**
	 *
	 */
	protected String getExporterPropertiesPrefix()//FIXMEPPTX move this to abstract exporter
	{
		return PPTX_EXPORTER_PROPERTIES_PREFIX;
	}

	/**
	 *
	 */
	protected String getExporterKey()
	{
		return PPTX_EXPORTER_KEY;
	}

}

