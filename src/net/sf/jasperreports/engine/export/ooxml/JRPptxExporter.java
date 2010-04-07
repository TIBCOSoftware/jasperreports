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
import java.awt.geom.Dimension2D;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.text.AttributedCharacterIterator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import net.sf.jasperreports.engine.JRAbstractExporter;
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
import net.sf.jasperreports.engine.JRPrintHyperlink;
import net.sf.jasperreports.engine.JRPrintImage;
import net.sf.jasperreports.engine.JRPrintPage;
import net.sf.jasperreports.engine.JRPrintRectangle;
import net.sf.jasperreports.engine.JRPrintText;
import net.sf.jasperreports.engine.JRRenderable;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.JRStyle;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.base.JRBaseLineBox;
import net.sf.jasperreports.engine.export.ExporterFilter;
import net.sf.jasperreports.engine.export.ExporterNature;
import net.sf.jasperreports.engine.export.GenericElementHandlerEnviroment;
import net.sf.jasperreports.engine.export.JRExportProgressMonitor;
import net.sf.jasperreports.engine.export.JRExporterGridCell;
import net.sf.jasperreports.engine.export.JRHyperlinkProducer;
import net.sf.jasperreports.engine.export.zip.ExportZipEntry;
import net.sf.jasperreports.engine.type.ModeEnum;
import net.sf.jasperreports.engine.util.JRColorUtil;
import net.sf.jasperreports.engine.util.JRProperties;
import net.sf.jasperreports.engine.util.JRStyledText;

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
	protected PptxZip pptxZip = null;
	protected PptxPresentationHelper presentationHelper = null;
	protected PptxRelsHelper relsHelper = null;
	protected PptxContentTypesHelper ctHelper = null;
	protected PptxSlideHelper slideHelper = null;
//	protected DocxDocumentHelper docHelper = null;
	protected Writer presentationWriter = null;

	protected JRExportProgressMonitor progressMonitor = null;
	protected Map rendererToImagePathMap = null;
	protected Map imageMaps;
	protected List imagesToProcess = null;
//	protected Map hyperlinksMap = null;

	protected int reportIndex = 0;
	protected int pageIndex = 0;
	protected int tableIndex = 0;
	protected boolean startPage;
	protected int elementIndex = 0;

	/**
	 * used for counting the total number of sheets
	 */
	protected int slideIndex = 0;
	
	/**
	 *
	 */
	protected boolean isWrapBreakWord = false;

	/**
	 * @deprecated
	 */
	protected Map fontMap = null;

	protected LinkedList backcolorStack;
	protected Color backcolor;

	private PptxRunHelper runHelper = null;

	protected ExporterNature nature = null;

	protected boolean deepGrid;

	protected boolean flexibleRowHeight;
	

	protected class ExporterContext extends BaseExporterContext implements JRDocxExporterContext
	{
		DocxTableHelper tableHelper = null;
		
		public ExporterContext(DocxTableHelper tableHelper)
		{
			this.tableHelper = tableHelper;
		}
		
		public DocxTableHelper getTableHelper()
		{
			return tableHelper;
		}

		public String getExportPropertiesPrefix()
		{
			return PPTX_EXPORTER_PROPERTIES_PREFIX;
		}
	}
	
	
	public JRPptxExporter()
	{
		backcolorStack = new LinkedList();
		backcolor = null;
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

			nature = getExporterNature(filter);

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
		
		relsHelper = new PptxRelsHelper(pptxZip.getRelsEntry().getWriter());
		relsHelper.exportHeader();
		
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

//		if ((imagesToProcess != null && imagesToProcess.size() > 0))
//		{
//			for(Iterator it = imagesToProcess.iterator(); it.hasNext();)
//			{
//				JRPrintElementIndex imageIndex = (JRPrintElementIndex)it.next();
//
//				JRPrintImage image = getImage(jasperPrintList, imageIndex);
//				JRRenderable renderer = image.getRenderer();
//				if (renderer.getType() == JRRenderable.TYPE_SVG)
//				{
//					renderer =
//						new JRWrappingSvgRenderer(
//							renderer,
//							new Dimension(image.getWidth(), image.getHeight()),
//							ModeEnum.OPAQUE == image.getModeValue() ? image.getBackcolor() : null
//							);
//				}
//
//				String mimeType = JRTypeSniffer.getImageMimeType(renderer.getImageType());
//				if (mimeType == null)
//				{
//					mimeType = JRRenderable.MIME_TYPE_JPEG;
//				}
//				String extension = mimeType.substring(mimeType.lastIndexOf('/') + 1);
//				
//				String imageName = getImageName(imageIndex);
//				
//				pptxZip.addEntry(//FIXMEPPTX optimize with a different implementation of entry
//					new FileBufferedZipEntry(
//						"word/media/" + imageName + "." + extension,
//						renderer.getImageData()
//						)
//					);
//				
//				relsHelper.exportImage(imageName, extension);
//			}
//		}

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

		relsHelper.exportFooter();

		relsHelper.close();

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
		relsHelper.exportSlide(slideIndex + 1);

		pptxZip.addEntry("ppt/slides/_rels/slide" + (slideIndex + 1) + ".xml.rels", "net/sf/jasperreports/engine/export/ooxml/pptx/ppt/slides/_rels/slide1.xml.rels");
		
		ExportZipEntry slideEntry = pptxZip.addSlide(slideIndex + 1);
		Writer slideWriter = slideEntry.getWriter();
		slideHelper = new PptxSlideHelper(slideWriter);
		
//		cellHelper = new XlsxCellHelper(sheetWriter, styleHelper);
//		
		runHelper = new PptxRunHelper(slideWriter, fontMap, null);//FIXMEXLSX check this null
		
		slideHelper.exportHeader();
		
	}


	protected void closeSlide()
	{
		if (slideHelper != null)
		{
			slideHelper.exportFooter();
			
			slideHelper.close();
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
//					if (element instanceof JRPrintLine)
//					{
//						exportLine((JRPrintLine)element);
//					}
//					else if (element instanceof JRPrintRectangle)
//					{
//						exportRectangle((JRPrintRectangle)element);
//					}
//					else if (element instanceof JRPrintEllipse)
//					{
//						//exportEllipse((JRPrintEllipse)element);
//						exportRectangle((JRPrintEllipse)element);
//					}
//					else if (element instanceof JRPrintImage)
//					{
//						exportImage((JRPrintImage)element);
//					}
//					else if (element instanceof JRPrintText)
					if (element instanceof JRPrintText)
					{
						exportText((JRPrintText)element);
					}
//					else if (element instanceof JRPrintFrame)
//					{
//						exportFrame((JRPrintFrame) element);
//					}
//					else if (element instanceof JRGenericPrintElement)
//					{
//						exportGenericElement((JRGenericPrintElement) element);
//					}
				}
			}
		}
	}


	/**
	 *
	 *
	protected void exportLine(JRPrintLine line)
	{
		JRLineBox box = new JRBaseLineBox(null);
		JRPen pen = null;
		float ratio = line.getWidth() / line.getHeight();
		if (ratio > 1)
		{
			if (line.getDirectionValue() == LineDirectionEnum.TOP_DOWN)
			{
				pen = box.getTopPen();
			}
			else
			{
				pen = box.getBottomPen();
			}
		}
		else
		{
			if (line.getDirectionValue() == LineDirectionEnum.TOP_DOWN)
			{
				pen = box.getLeftPen();
			}
			else
			{
				pen = box.getRightPen();
			}
		}
		pen.setLineColor(line.getLinePen().getLineColor());
		pen.setLineStyle(line.getLinePen().getLineStyleValue());
		pen.setLineWidth(line.getLinePen().getLineWidth());

		gridCell.setBox(box);//CAUTION: only some exporters set the cell box
		
		tableHelper.getCellHelper().exportHeader(line, gridCell);
		tableHelper.getParagraphHelper().exportEmptyParagraph();
		tableHelper.getCellHelper().exportFooter();
	}


	/**
	 *
	 */
	protected void exportRectangle(DocxTableHelper tableHelper, JRPrintRectangle rectangle, JRExporterGridCell gridCell)
	{
		JRLineBox box = new JRBaseLineBox(null);
		JRPen pen = box.getPen();
		pen.setLineColor(rectangle.getLinePen().getLineColor());
		pen.setLineStyle(rectangle.getLinePen().getLineStyleValue());
		pen.setLineWidth(rectangle.getLinePen().getLineWidth());

		gridCell.setBox(box);//CAUTION: only some exporters set the cell box
		
		tableHelper.getCellHelper().exportHeader(rectangle, gridCell);
		tableHelper.getParagraphHelper().exportEmptyParagraph();
		tableHelper.getCellHelper().exportFooter();
	}


	/**
	 *
	 */
	protected void exportEllipse(DocxTableHelper tableHelper, JRPrintEllipse ellipse, JRExporterGridCell gridCell)
	{
		JRLineBox box = new JRBaseLineBox(null);
		JRPen pen = box.getPen();
		pen.setLineColor(ellipse.getLinePen().getLineColor());
		pen.setLineStyle(ellipse.getLinePen().getLineStyleValue());
		pen.setLineWidth(ellipse.getLinePen().getLineWidth());

		gridCell.setBox(box);//CAUTION: only some exporters set the cell box
		
		tableHelper.getCellHelper().exportHeader(ellipse, gridCell);
		tableHelper.getParagraphHelper().exportEmptyParagraph();
		tableHelper.getCellHelper().exportFooter();
	}


	/**
	 *
	 */
	public void exportText(JRPrintText text)
	{
		slideHelper.write("<p:sp>\n");
		slideHelper.write("  <p:nvSpPr>\n");
		slideHelper.write("    <p:cNvPr id=\"" + (elementIndex + 4) + "\" name=\"Title 1\"/>\n");
		slideHelper.write("    <p:cNvSpPr>\n");
		slideHelper.write("      <a:spLocks noGrp=\"1\"/>\n");
		slideHelper.write("    </p:cNvSpPr>\n");
		slideHelper.write("    <p:nvPr>\n");
		slideHelper.write("      <p:ph type=\"ctrTitle\"/>\n");
		slideHelper.write("    </p:nvPr>\n");
		slideHelper.write("  </p:nvSpPr>\n");
		slideHelper.write("  <p:spPr>\n");
		slideHelper.write("    <a:xfrm>\n");
		slideHelper.write("      <a:off x=\"" + Utility.emu(text.getX()) + "\" y=\"" + Utility.emu(text.getY()) + "\"/>\n");
		slideHelper.write("      <a:ext cx=\"" + Utility.emu(text.getWidth()) + "\" cy=\"" + Utility.emu(text.getHeight()) + "\"/>\n");
		slideHelper.write("    </a:xfrm><a:prstGeom prst=\"rect\"><a:avLst/></a:prstGeom>\n");
		if (text.getModeValue() == ModeEnum.OPAQUE && text.getBackcolor() != null)
		{
			slideHelper.write("<a:solidFill><a:srgbClr val=\"" + JRColorUtil.getColorHexa(text.getBackcolor()) + "\"/></a:solidFill>\n");
		}
		slideHelper.write("  </p:spPr>\n");
		slideHelper.write("  <p:txBody>\n");
		slideHelper.write("    <a:bodyPr wrap=\"square\" lIns=\"0\" tIns=\"0\" rIns=\"0\" bIns=\"0\" rtlCol=\"0\"/>\n");
		slideHelper.write("    <a:lstStyle/>\n");

		JRStyledText styledText = getStyledText(text);

		int textLength = 0;

		if (styledText != null)
		{
			textLength = styledText.length();
		}

//		if (isWrapBreakWord)
//		{
//			styleBuffer.append("width: " + gridCell.width + "; ");
//			styleBuffer.append("word-wrap: break-word; ");
//		}

//		if (text.getLineSpacing() != JRTextElement.LINE_SPACING_SINGLE)
//		{
//			styleBuffer.append("line-height: " + text.getLineSpacingFactor() + "; ");
//		}

//		if (styleBuffer.length() > 0)
//		{
//			writer.write(" style=\"");
//			writer.write(styleBuffer.toString());
//			writer.write("\"");
//		}
//
//		writer.write(">");
		slideHelper.write("    <a:p>\n");

		slideHelper.write("<a:pPr>\n");
		runHelper.exportProps(text, getTextLocale(text));
		slideHelper.write("</a:pPr>\n");
		
//		insertPageAnchor();
//		if (text.getAnchorName() != null)
//		{
//			tempBodyWriter.write("<text:bookmark text:name=\"");
//			tempBodyWriter.write(text.getAnchorName());
//			tempBodyWriter.write("\"/>");
//		}

		boolean startedHyperlink = startHyperlink(text, true);

		if (textLength > 0)
		{
			exportStyledText(text.getStyle(), styledText, getTextLocale(text));
		}

		if (startedHyperlink)
		{
			endHyperlink(true);
		}

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
	protected void exportImage(DocxTableHelper tableHelper, JRPrintImage image, JRExporterGridCell gridCell) throws JRException
	{
		int leftPadding = image.getLineBox().getLeftPadding().intValue();
		int topPadding = image.getLineBox().getTopPadding().intValue();//FIXMEDOCX maybe consider border thickness
		int rightPadding = image.getLineBox().getRightPadding().intValue();
		int bottomPadding = image.getLineBox().getBottomPadding().intValue();

		int availableImageWidth = image.getWidth() - leftPadding - rightPadding;
		availableImageWidth = availableImageWidth < 0 ? 0 : availableImageWidth;

		int availableImageHeight = image.getHeight() - topPadding - bottomPadding;
		availableImageHeight = availableImageHeight < 0 ? 0 : availableImageHeight;

		tableHelper.getCellHelper().exportHeader(image, gridCell);

//		docHelper.write("<w:p>");

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
					break;
				}
				case CLIP :
				{
					if (normalWidth > availableImageWidth)
					{
						switch (image.getHorizontalAlignmentValue())
						{
							case RIGHT :
							{
								cropLeft = 65536 * (normalWidth - availableImageWidth) / normalWidth;
								cropRight = 0;
								break;
							}
							case CENTER :
							{
								cropLeft = 65536 * (- availableImageWidth + normalWidth) / normalWidth / 2;
								cropRight = cropLeft;
								break;
							}
							case LEFT :
							default :
							{
								cropLeft = 0;
								cropRight = 65536 * (normalWidth - availableImageWidth) / normalWidth;
								break;
							}
						}
						width = availableImageWidth;
						cropLeft = cropLeft / 0.75d;
						cropRight = cropRight / 0.75d;
					}
					else
					{
						width = (int)normalWidth;
					}

					if (normalHeight > availableImageHeight)
					{
						switch (image.getVerticalAlignmentValue())
						{
							case TOP :
							{
								cropTop = 0;
								cropBottom = 65536 * (normalHeight - availableImageHeight) / normalHeight;
								break;
							}
							case MIDDLE :
							{
								cropTop = 65536 * (normalHeight - availableImageHeight) / normalHeight / 2;
								cropBottom = cropTop;
								break;
							}
							case BOTTOM :
							default :
							{
								cropTop = 65536 * (normalHeight - availableImageHeight) / normalHeight;
								cropBottom = 0;
								break;
							}
						}
						height = availableImageHeight;
						cropTop = cropTop / 0.75d;
						cropBottom = cropBottom / 0.75d;
					}
					else
					{
						height = (int)normalHeight;
					}

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

						}
						else
						{
							height = availableImageHeight;
							width = (int)(ratio * height);
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


			boolean startedHyperlink = startHyperlink(image,false);

//			docHelper.write("<w:r>\n"); 
//			docHelper.write("<w:drawing>\n");
//			docHelper.write("<wp:anchor distT=\"0\" distB=\"0\" distL=\"0\" distR=\"0\" simplePos=\"0\" relativeHeight=\"0\" behindDoc=\"0\" locked=\"1\" layoutInCell=\"1\" allowOverlap=\"1\">");
//			docHelper.write("<wp:simplePos x=\"0\" y=\"0\"/>");
//			docHelper.write("<wp:positionH relativeFrom=\"column\"><wp:align>" + DocxParagraphHelper.getHorizontalAlignment(image.getHorizontalAlignmentValue()) + "</wp:align></wp:positionH>");
//			docHelper.write("<wp:positionV relativeFrom=\"line\"><wp:posOffset>0</wp:posOffset></wp:positionV>");
////			docHelper.write("<wp:positionV relativeFrom=\"line\"><wp:align>" + CellHelper.getVerticalAlignment(new Byte(image.getVerticalAlignment())) + "</wp:align></wp:positionV>");
//			
//			docHelper.write("<wp:extent cx=\"" + Utility.emu(width) + "\" cy=\"" + Utility.emu(height) + "\"/>\n");
//			docHelper.write("<wp:wrapNone/>");
//			docHelper.write("<wp:docPr id=\"" + image.hashCode() + "\" name=\"Picture\"/>\n");
//			docHelper.write("<a:graphic>\n");
//			docHelper.write("<a:graphicData uri=\"http://schemas.openxmlformats.org/drawingml/2006/picture\">\n");
//			docHelper.write("<pic:pic>\n");
//			docHelper.write("<pic:nvPicPr><pic:cNvPr id=\"" + image.hashCode() + "\" name=\"Picture\"/><pic:cNvPicPr/></pic:nvPicPr>\n");
//			docHelper.write("<pic:blipFill>\n");
//			docHelper.write("<a:blip r:embed=\"" + getImagePath(renderer, image.isLazy(), gridCell) + "\"/>");
//			docHelper.write("<a:srcRect");
//			if (cropLeft > 0)
//			{
//				docHelper.write(" l=\"" + (int)cropLeft + "\"");
//			}
//			if (cropTop > 0)
//			{
//				docHelper.write(" t=\"" + (int)cropTop + "\"");
//			}
//			if (cropRight > 0)
//			{
//				docHelper.write(" r=\"" + (int)cropRight + "\"");
//			}
//			if (cropBottom > 0)
//			{
//				docHelper.write(" b=\"" + (int)cropBottom + "\"");
//			}
//			docHelper.write("/>");
//			docHelper.write("<a:stretch><a:fillRect/></a:stretch>\n");
//			docHelper.write("</pic:blipFill>\n");
//			docHelper.write("<pic:spPr><a:xfrm><a:off x=\"0\" y=\"0\"/><a:ext cx=\"" + Utility.emu(width) + "\" cy=\"" + Utility.emu(height) + "\"/>");
//			docHelper.write("</a:xfrm><a:prstGeom prst=\"rect\"></a:prstGeom></pic:spPr>\n");
//			docHelper.write("</pic:pic>\n");
//			docHelper.write("</a:graphicData>\n");
//			docHelper.write("</a:graphic>\n");
//			docHelper.write("</wp:anchor>\n");
//			docHelper.write("</w:drawing>\n");
//			docHelper.write("</w:r>"); 

			if(startedHyperlink)
			{
				endHyperlink(false);
			}
		}

//		docHelper.write("</w:p>");

		tableHelper.getCellHelper().exportFooter();
	}


	/**
	 *
	 */
	protected String getImagePath(JRRenderable renderer, boolean isLazy, JRExporterGridCell gridCell)
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
					JRPrintElementIndex imageIndex = getElementIndex(gridCell);
					imagesToProcess.add(imageIndex);

					String imageName = getImageName(imageIndex);
					imagePath = imageName;
					//imagePath = "Pictures/" + imageName;
//				}

				rendererToImagePathMap.put(renderer.getId(), imagePath);
			}
		}

		return imagePath;
	}


	protected JRPrintElementIndex getElementIndex(JRExporterGridCell gridCell)
	{
		JRPrintElementIndex imageIndex =
			new JRPrintElementIndex(
					reportIndex,
					pageIndex,
					gridCell.getWrapper().getAddress()
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


	/**
	 * In deep grids, this is called only for empty frames.
	 *
	protected void exportFrame(DocxTableHelper tableHelper, JRPrintFrame frame, JRExporterGridCell gridCell) throws JRException
	{
		tableHelper.getCellHelper().exportHeader(frame, gridCell);
//		tableHelper.getCellHelper().exportProps(gridCell);

		boolean appendBackcolor =
			frame.getModeValue() == ModeEnum.OPAQUE
			&& (backcolor == null || frame.getBackcolor().getRGB() != backcolor.getRGB());

		if (appendBackcolor)
		{
			setBackcolor(frame.getBackcolor());
		}

		try
		{
			JRGridLayout layout = gridCell.getLayout();
			JRPrintElementIndex frameIndex =
				new JRPrintElementIndex(
						reportIndex,
						pageIndex,
						gridCell.getWrapper().getAddress()
						);
			exportGrid(layout, frameIndex);
		}
		finally
		{
			if (appendBackcolor)
			{
				restoreBackcolor();
			}
		}
		
		tableHelper.getParagraphHelper().exportEmptyParagraph();
		tableHelper.getCellHelper().exportFooter();
	}


	/**
	 *
	 */
	protected void exportGenericElement(DocxTableHelper tableHelper, JRGenericPrintElement element, JRExporterGridCell gridCell)
	{
		GenericElementDocxHandler handler = (GenericElementDocxHandler) 
		GenericElementHandlerEnviroment.getHandler(
				element.getGenericType(), PPTX_EXPORTER_KEY);

		if (handler != null)
		{
			JRDocxExporterContext exporterContext = new ExporterContext(tableHelper);

			handler.exportElement(exporterContext, element, gridCell);
		}
		else
		{
			if (log.isDebugEnabled())
			{
				log.debug("No DOCX generic element handler for " 
						+ element.getGenericType());
			}
		}
	}


	/**
	 *
	 */
	protected void setBackcolor(Color color)
	{
		backcolorStack.addLast(backcolor);

		backcolor = color;
	}


	protected void restoreBackcolor()
	{
		backcolor = (Color) backcolorStack.removeLast();
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

	protected boolean startHyperlink(JRPrintHyperlink link, boolean isText)
	{
		String href = getHyperlinkURL(link);

//		if (href != null)
//		{
//			String id = (String)hyperlinksMap.get(href);
//			if (id == null)
//			{
//				id = "link" + hyperlinksMap.size();
//				hyperlinksMap.put(href, id);
//			}
//			
//			docHelper.write("<w:hyperlink r:id=\"" + id + "\"");
//
//			String target = getHyperlinkTarget(link);//FIXMETARGET
//			if (target != null)
//			{
//				docHelper.write(" tgtFrame=\"" + target + "\"");
//			}
//
//			docHelper.write(">\n");

//			docHelper.write("<w:r><w:fldChar w:fldCharType=\"begin\"/></w:r>\n");
//			docHelper.write("<w:r><w:instrText xml:space=\"preserve\"> HYPERLINK \"" + JRStringUtil.xmlEncode(href) + "\"");
//
//			String target = getHyperlinkTarget(link);//FIXMETARGET
//			if (target != null)
//			{
//				docHelper.write(" \\t \"" + target + "\"");
//			}
//
//			String tooltip = link.getHyperlinkTooltip(); 
//			if (tooltip != null)
//			{
//				docHelper.write(" \\o \"" + JRStringUtil.xmlEncode(tooltip) + "\"");
//			}
//
//			docHelper.write(" </w:instrText></w:r>\n");
//			docHelper.write("<w:r><w:fldChar w:fldCharType=\"separate\"/></w:r>\n");
//		}

		return href != null;
	}


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


	protected void endHyperlink(boolean isText)
	{
//		docHelper.write("</w:hyperlink>\n");
//		docHelper.write("<w:r><w:fldChar w:fldCharType=\"end\"/></w:r>\n");
	}

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
	protected void setInput() throws JRException
	{
		super.setInput();

		deepGrid = 
			!getBooleanParameter(
				JRDocxExporterParameter.FRAMES_AS_NESTED_TABLES,
				JRDocxExporterParameter.PROPERTY_FRAMES_AS_NESTED_TABLES,
				true
				);

		flexibleRowHeight = 
			getBooleanParameter(
				JRDocxExporterParameter.FLEXIBLE_ROW_HEIGHT,
				JRDocxExporterParameter.PROPERTY_FLEXIBLE_ROW_HEIGHT,
				false
				);
	}

	/**
	 *
	 */
	protected ExporterNature getExporterNature(ExporterFilter filter) 
	{
		return new JRDocxExporterNature(filter, deepGrid);
	}

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

