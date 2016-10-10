/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2016 TIBCO Software Inc. All rights reserved.
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

import java.awt.Dimension;
import java.awt.geom.Dimension2D;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.URLEncoder;
import java.text.AttributedCharacterIterator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.sf.jasperreports.engine.DefaultJasperReportsContext;
import net.sf.jasperreports.engine.JRAbstractExporter;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRGenericElementType;
import net.sf.jasperreports.engine.JRGenericPrintElement;
import net.sf.jasperreports.engine.JRLineBox;
import net.sf.jasperreports.engine.JRPen;
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
import net.sf.jasperreports.engine.JRPropertiesUtil;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.JRStyle;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.base.JRBasePen;
import net.sf.jasperreports.engine.export.GenericElementHandlerEnviroment;
import net.sf.jasperreports.engine.export.HyperlinkUtil;
import net.sf.jasperreports.engine.export.JRExportProgressMonitor;
import net.sf.jasperreports.engine.export.JRHyperlinkProducer;
import net.sf.jasperreports.engine.export.JRXmlExporter;
import net.sf.jasperreports.engine.export.LengthUtil;
import net.sf.jasperreports.engine.export.zip.ExportZipEntry;
import net.sf.jasperreports.engine.export.zip.FileBufferedZipEntry;
import net.sf.jasperreports.engine.type.LineDirectionEnum;
import net.sf.jasperreports.engine.type.LineStyleEnum;
import net.sf.jasperreports.engine.type.ModeEnum;
import net.sf.jasperreports.engine.type.ScaleImageEnum;
import net.sf.jasperreports.engine.util.JRColorUtil;
import net.sf.jasperreports.engine.util.JRStyledText;
import net.sf.jasperreports.engine.util.JRTypeSniffer;
import net.sf.jasperreports.export.ExportInterruptedException;
import net.sf.jasperreports.export.ExporterInputItem;
import net.sf.jasperreports.export.OutputStreamExporterOutput;
import net.sf.jasperreports.export.PptxExporterConfiguration;
import net.sf.jasperreports.export.PptxReportConfiguration;
import net.sf.jasperreports.renderers.DataRenderable;
import net.sf.jasperreports.renderers.DimensionRenderable;
import net.sf.jasperreports.renderers.Renderable;
import net.sf.jasperreports.renderers.RenderersCache;
import net.sf.jasperreports.renderers.ResourceRenderer;


/**
 * Exports a JasperReports document to Microsoft PowerPoint 2007 format (PPTX).
 * <p/>
 * This exporter uses an absolute positioned layout and currently there is a single special 
 * configuration that can be applied to a PPTX
 * exporter instance (see {@link net.sf.jasperreports.export.PptxReportConfiguration})
 * to configure its behavior: one can ignore hyperlinks in generated documents if they are 
 * not intended for the PPTX output format. This can be customized using either the 
 * {@link net.sf.jasperreports.export.PptxReportConfiguration#isIgnoreHyperlink() isIgnoreHyperlink()} 
 * exporter configuration flag, or its corresponding exporter hint called
 * {@link net.sf.jasperreports.export.PptxReportConfiguration#PROPERTY_IGNORE_HYPERLINK net.sf.jasperreports.export.pptx.ignore.hyperlink}.
 * <p/>
 * It supports font mappings, batch mode exporting, and filtering
 * out content using exporter filters.
 * <p/>
 * Documents produced using this exporter can be generated in the great majority of the
 * samples shipped with the JasperReports project source files, where the <code>pptx</code> Ant task is
 * defined.
 * 
 * @see net.sf.jasperreports.export.PptxReportConfiguration
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public class JRPptxExporter extends JRAbstractExporter<PptxReportConfiguration, PptxExporterConfiguration, OutputStreamExporterOutput, JRPptxExporterContext>
{
	private static final Log log = LogFactory.getLog(JRPptxExporter.class);
	
	/**
	 * The exporter key, as used in
	 * {@link GenericElementHandlerEnviroment#getElementHandler(JRGenericElementType, String)}.
	 */
	public static final String PPTX_EXPORTER_KEY = JRPropertiesUtil.PROPERTY_PREFIX + "pptx";
	
	protected static final String PPTX_EXPORTER_PROPERTIES_PREFIX = JRPropertiesUtil.PROPERTY_PREFIX + "export.pptx.";

	/**
	 * @deprecated Replaced by {@link PptxReportConfiguration#PROPERTY_IGNORE_HYPERLINK}.
	 */
	public static final String PROPERTY_IGNORE_HYPERLINK = PptxReportConfiguration.PROPERTY_IGNORE_HYPERLINK;

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
	protected PropsAppHelper appHelper;
	protected PropsCoreHelper coreHelper;
	protected PptxSlideHelper slideHelper;
	protected PptxSlideRelsHelper slideRelsHelper;
	protected Writer presentationWriter;

	protected Map<String, String> rendererToImagePathMap;
	protected RenderersCache renderersCache;
//	protected Map imageMaps;
//	protected Map hyperlinksMap;

	protected int reportIndex;
	protected int pageIndex;
	protected List<Integer> frameIndexStack;
	protected int elementIndex;
	protected boolean startPage;
	protected String invalidCharReplacement;

	/**
	 * used for counting the total number of sheets
	 */
	protected int slideIndex;
	
	private PptxRunHelper runHelper;

	protected class ExporterContext extends BaseExporterContext implements JRPptxExporterContext
	{
		public ExporterContext()
		{
		}
		
		@Override
		public PptxSlideHelper getSlideHelper()
		{
			return slideHelper;
		}
	}
	
	
	/**
	 * @see #JRPptxExporter(JasperReportsContext)
	 */
	public JRPptxExporter()
	{
		this(DefaultJasperReportsContext.getInstance());
	}

	
	/**
	 *
	 */
	public JRPptxExporter(JasperReportsContext jasperReportsContext)
	{
		super(jasperReportsContext);

		exporterContext = new ExporterContext();
	}


	@Override
	protected Class<PptxExporterConfiguration> getConfigurationInterface()
	{
		return PptxExporterConfiguration.class;
	}


	@Override
	protected Class<PptxReportConfiguration> getItemConfigurationInterface()
	{
		return PptxReportConfiguration.class;
	}
	

	@Override
	@SuppressWarnings("deprecation")
	protected void ensureOutput()
	{
		if (exporterOutput == null)
		{
			exporterOutput = 
				new net.sf.jasperreports.export.parameters.ParametersOutputStreamExporterOutput(
					getJasperReportsContext(),
					getParameters(),
					getCurrentJasperPrint()
					);
		}
	}
	

	@Override
	public void exportReport() throws JRException
	{
		/*   */
		ensureJasperReportsContext();
		ensureInput();

		rendererToImagePathMap = new HashMap<String,String>();
//		imageMaps = new HashMap();
//		hyperlinksMap = new HashMap();
		
		initExport();

		ensureOutput();
		
		OutputStream outputStream = getExporterOutput().getOutputStream();

		try
		{
			exportReportToStream(outputStream);
		}
		catch (IOException e)
		{
			throw new JRRuntimeException(e);
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
		
		if (jasperPrint.hasProperties() && jasperPrint.getPropertiesMap().containsProperty(JRXmlExporter.PROPERTY_REPLACE_INVALID_CHARS))
		{
			// allows null values for the property
			invalidCharReplacement = jasperPrint.getProperty(JRXmlExporter.PROPERTY_REPLACE_INVALID_CHARS);
		}
		else
		{
			invalidCharReplacement = getPropertiesUtil().getProperty(JRXmlExporter.PROPERTY_REPLACE_INVALID_CHARS, jasperPrint);
		}

		renderersCache = new RenderersCache(getJasperReportsContext());
	}

	
	/**
	 *
	 */
	protected void exportReportToStream(OutputStream os) throws JRException, IOException
	{
		pptxZip = new PptxZip();

		presentationWriter = pptxZip.getPresentationEntry().getWriter();
		
		presentationHelper = new PptxPresentationHelper(jasperReportsContext, presentationWriter);
		presentationHelper.exportHeader();
		
		presentationRelsHelper = new PptxPresentationRelsHelper(jasperReportsContext, pptxZip.getRelsEntry().getWriter());
		presentationRelsHelper.exportHeader();
		
		ctHelper = new PptxContentTypesHelper(jasperReportsContext, pptxZip.getContentTypesEntry().getWriter());
		ctHelper.exportHeader();

		appHelper = new PropsAppHelper(jasperReportsContext, pptxZip.getAppEntry().getWriter());
		coreHelper = new PropsCoreHelper(jasperReportsContext, pptxZip.getCoreEntry().getWriter());

		appHelper.exportHeader();
		
		PptxExporterConfiguration configuration = getCurrentConfiguration();

		String application = configuration.getMetadataApplication();
		if( application == null )
		{
			application = "JasperReports Library version " + Package.getPackage("net.sf.jasperreports.engine").getImplementationVersion();
		}
		appHelper.exportProperty(PropsAppHelper.PROPERTY_APPLICATION, application);

		coreHelper.exportHeader();
		
		String title = configuration.getMetadataTitle();
		if (title != null)
		{
			coreHelper.exportProperty(PropsCoreHelper.PROPERTY_TITLE, title);
		}
		String subject = configuration.getMetadataSubject();
		if (subject != null)
		{
			coreHelper.exportProperty(PropsCoreHelper.PROPERTY_SUBJECT, subject);
		}
		String author = configuration.getMetadataAuthor();
		if (author != null)
		{
			coreHelper.exportProperty(PropsCoreHelper.PROPERTY_CREATOR, author);
		}
		String keywords = configuration.getMetadataKeywords();
		if (keywords != null)
		{
			coreHelper.exportProperty(PropsCoreHelper.PROPERTY_KEYWORDS, keywords);
		}

//		DocxStyleHelper styleHelper = 
//			new DocxStyleHelper(
//				null,//pptxZip.getStylesEntry().getWriter(), 
//				fontMap, 
//				getExporterKey()
//				);
//		styleHelper.export(jasperPrintList);
//		styleHelper.close();

		List<ExporterInputItem> items = exporterInput.getItems();

		for(reportIndex = 0; reportIndex < items.size(); reportIndex++)
		{
			ExporterInputItem item = items.get(reportIndex);
			
			setCurrentExporterInputItem(item);
			
			List<JRPrintPage> pages = jasperPrint.getPages();
			if (pages != null && pages.size() > 0)
			{
				PageRange pageRange = getPageRange();
				int startPageIndex = (pageRange == null || pageRange.getStartPageIndex() == null) ? 0 : pageRange.getStartPageIndex();
				int endPageIndex = (pageRange == null || pageRange.getEndPageIndex() == null) ? (pages.size() - 1) : pageRange.getEndPageIndex();

				JRPrintPage page = null;
				for(pageIndex = startPageIndex; pageIndex <= endPageIndex; pageIndex++)
				{
					if (Thread.interrupted())
					{
						throw new ExportInterruptedException();
					}

					page = pages.get(pageIndex);

					createSlide(null);//FIXMEPPTX
					
					slideIndex++;

					exportPage(page);
				}
			}
		}
		
		closeSlide();

		presentationHelper.exportFooter(jasperPrint);
		presentationHelper.close();

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

		appHelper.exportFooter();
		appHelper.close();

		coreHelper.exportFooter();
		coreHelper.close();

		pptxZip.zipEntries(os);

		pptxZip.dispose();
	}


	/**
	 *
	 */
	protected void exportPage(JRPrintPage page) throws JRException
	{
		frameIndexStack = new ArrayList<Integer>();

		exportElements(page.getElements());
		
		JRExportProgressMonitor progressMonitor = getCurrentItemConfiguration().getProgressMonitor();
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
		slideRelsHelper = new PptxSlideRelsHelper(jasperReportsContext, slideRelsWriter);
		
		ExportZipEntry slideEntry = pptxZip.addSlide(slideIndex + 1);
		Writer slideWriter = slideEntry.getWriter();
		slideHelper = new PptxSlideHelper(jasperReportsContext, slideWriter, slideRelsHelper);

//		cellHelper = new XlsxCellHelper(sheetWriter, styleHelper);
//		
		runHelper = new PptxRunHelper(jasperReportsContext, slideWriter, null);//FIXMEXLSX check this null
		
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
	protected void exportElements(List<JRPrintElement> elements) throws JRException
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
		slideHelper.write("    <p:cNvPr id=\"" + toOOXMLId(line) + "\" name=\"Line\"/>\n");
		slideHelper.write("    <p:cNvSpPr>\n");
		slideHelper.write("      <a:spLocks noGrp=\"1\"/>\n");
		slideHelper.write("    </p:cNvSpPr>\n");
		slideHelper.write("    <p:nvPr/>\n");
		slideHelper.write("  </p:nvSpPr>\n");
		slideHelper.write("  <p:spPr>\n");
		slideHelper.write("    <a:xfrm" + (line.getDirectionValue() == LineDirectionEnum.BOTTOM_UP ? " flipV=\"1\"" : "") + ">\n");
		slideHelper.write("      <a:off x=\"" + LengthUtil.emu(x) + "\" y=\"" + LengthUtil.emu(y) + "\"/>\n");
		slideHelper.write("      <a:ext cx=\"" + LengthUtil.emu(width) + "\" cy=\"" + LengthUtil.emu(height) + "\"/>\n");
		slideHelper.write("    </a:xfrm><a:prstGeom prst=\"line\"><a:avLst/></a:prstGeom>\n");
		if (line.getModeValue() == ModeEnum.OPAQUE && line.getBackcolor() != null)
		{
			slideHelper.write("<a:solidFill><a:srgbClr val=\"" + JRColorUtil.getColorHexa(line.getBackcolor()) + "\"/></a:solidFill>\n");
		}
		if (line.getLinePen().getLineWidth() > 0)
		{
			slideHelper.write("  <a:ln w=\"" + LengthUtil.emu(line.getLinePen().getLineWidth()) + "\"");
			if(LineStyleEnum.DOUBLE.equals(line.getLinePen().getLineStyleValue()))
			{
				slideHelper.write(" cmpd=\"dbl\"");
			}
			slideHelper.write(">\n");
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
		slideHelper.write("    <p:cNvPr id=\"" + toOOXMLId(rectangle) + "\" name=\"Rectangle\"/>\n");
		slideHelper.write("    <p:cNvSpPr>\n");
		slideHelper.write("      <a:spLocks noGrp=\"1\"/>\n");
		slideHelper.write("    </p:cNvSpPr>\n");
		slideHelper.write("    <p:nvPr/>\n");
		slideHelper.write("  </p:nvSpPr>\n");
		slideHelper.write("  <p:spPr>\n");
		slideHelper.write("    <a:xfrm>\n");
		slideHelper.write("      <a:off x=\"" + LengthUtil.emu(rectangle.getX() + getOffsetX()) + "\" y=\"" + LengthUtil.emu(rectangle.getY() + getOffsetY()) + "\"/>\n");
		slideHelper.write("      <a:ext cx=\"" + LengthUtil.emu(rectangle.getWidth()) + "\" cy=\"" + LengthUtil.emu(rectangle.getHeight()) + "\"/>\n");
		slideHelper.write("    </a:xfrm><a:prstGeom prst=\"" + (rectangle.getRadius() == 0 ? "rect" : "roundRect") + "\">");
		if(rectangle.getRadius() > 0)
		{
			// a rounded rectangle radius cannot exceed 1/2 of its lower side;
			int size = Math.min(50000, (rectangle.getRadius() * 100000)/Math.min(rectangle.getHeight(), rectangle.getWidth()));
			slideHelper.write("<a:avLst><a:gd name=\"adj\" fmla=\"val "+ size +"\"/></a:avLst></a:prstGeom>\n");
		}
		else
		{
			slideHelper.write("<a:avLst/></a:prstGeom>\n");
		}
		if (rectangle.getModeValue() == ModeEnum.OPAQUE && rectangle.getBackcolor() != null)
		{
			slideHelper.write("<a:solidFill><a:srgbClr val=\"" + JRColorUtil.getColorHexa(rectangle.getBackcolor()) + "\"/></a:solidFill>\n");
		}
		if (rectangle.getLinePen().getLineWidth() > 0)
		{
			slideHelper.write("  <a:ln w=\"" + LengthUtil.emu(rectangle.getLinePen().getLineWidth()) + "\"");
			if(LineStyleEnum.DOUBLE.equals(rectangle.getLinePen().getLineStyleValue()))
			{
				slideHelper.write(" cmpd=\"dbl\"");
			}
			slideHelper.write(">\n");
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
		slideHelper.write("    <p:cNvPr id=\"" + toOOXMLId(ellipse) + "\" name=\"Ellipse\"/>\n");
		slideHelper.write("    <p:cNvSpPr>\n");
		slideHelper.write("      <a:spLocks noGrp=\"1\"/>\n");
		slideHelper.write("    </p:cNvSpPr>\n");
		slideHelper.write("    <p:nvPr/>\n");
		slideHelper.write("  </p:nvSpPr>\n");
		slideHelper.write("  <p:spPr>\n");
		slideHelper.write("    <a:xfrm>\n");
		slideHelper.write("      <a:off x=\"" + LengthUtil.emu(ellipse.getX() + getOffsetX()) + "\" y=\"" + LengthUtil.emu(ellipse.getY() + getOffsetY()) + "\"/>\n");
		slideHelper.write("      <a:ext cx=\"" + LengthUtil.emu(ellipse.getWidth()) + "\" cy=\"" + LengthUtil.emu(ellipse.getHeight()) + "\"/>\n");
		slideHelper.write("    </a:xfrm><a:prstGeom prst=\"ellipse\"><a:avLst/></a:prstGeom>\n");
		if (ellipse.getModeValue() == ModeEnum.OPAQUE && ellipse.getBackcolor() != null)
		{
			slideHelper.write("<a:solidFill><a:srgbClr val=\"" + JRColorUtil.getColorHexa(ellipse.getBackcolor()) + "\"/></a:solidFill>\n");
		}
		if (ellipse.getLinePen().getLineWidth() > 0)
		{
			slideHelper.write("  <a:ln w=\"" + LengthUtil.emu(ellipse.getLinePen().getLineWidth()) + "\"");
			if(LineStyleEnum.DOUBLE.equals(ellipse.getLinePen().getLineStyleValue()))
			{
				slideHelper.write(" cmpd=\"dbl\"");
			}
			slideHelper.write(">\n");
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
		slideHelper.write("    <p:cNvPr id=\"" + toOOXMLId(text) + "\" name=\"Text\">\n");
		
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
		slideHelper.write("      <a:off x=\"" + LengthUtil.emu(x) + "\" y=\"" + LengthUtil.emu(y) + "\"/>\n");
		slideHelper.write("      <a:ext cx=\"" + LengthUtil.emu(width) + "\" cy=\"" + LengthUtil.emu(height) + "\"/>\n");
		slideHelper.write("    </a:xfrm><a:prstGeom prst=\"rect\"><a:avLst/></a:prstGeom>\n");
		if (text.getModeValue() == ModeEnum.OPAQUE && text.getBackcolor() != null)
		{
			slideHelper.write("<a:solidFill><a:srgbClr val=\"" + JRColorUtil.getColorHexa(text.getBackcolor()) + "\"/></a:solidFill>\n");
		}
		JRPen pen = getPptxPen(text.getLineBox());
		if (pen != null)
		{
			slideHelper.write("  <a:ln w=\"" + LengthUtil.emu(pen.getLineWidth()) + "\"");
			if(LineStyleEnum.DOUBLE.equals(pen.getLineStyleValue()))
			{
				slideHelper.write(" cmpd=\"dbl\"");
			}
			slideHelper.write(">\n");
			slideHelper.write("<a:solidFill><a:srgbClr val=\"" + JRColorUtil.getColorHexa(pen.getLineColor()) + "\"/></a:solidFill>\n");
			slideHelper.write("<a:prstDash val=\"");
			switch (pen.getLineStyleValue())
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
				LengthUtil.emu(leftPadding) +
				"\" tIns=\"" +
				LengthUtil.emu(topPadding) +
				"\" rIns=\"" +
				LengthUtil.emu(rightPadding) +
				"\" bIns=\"" +
				LengthUtil.emu(bottomPadding) +
				"\" rtlCol=\"0\" anchor=\"");
		switch (text.getVerticalTextAlign())
		{
			case BOTTOM:
				slideHelper.write("b");
				break;
			case MIDDLE:
				slideHelper.write("ctr");
				break;
			case TOP:
			case JUSTIFIED:
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
		switch (text.getHorizontalTextAlign())
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
		switch (text.getParagraph().getLineSpacing())
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
				locale,
				invalidCharReplacement
				);

			iterator.setIndex(runLimit);
		}
	}


	/**
	 *
	 */
	public void exportImage(JRPrintImage image) throws JRException
	{
		int leftPadding = image.getLineBox().getLeftPadding().intValue();
		int topPadding = image.getLineBox().getTopPadding().intValue();//FIXMEDOCX maybe consider border thickness
		int rightPadding = image.getLineBox().getRightPadding().intValue();
		int bottomPadding = image.getLineBox().getBottomPadding().intValue();

		int availableImageWidth = image.getWidth() - leftPadding - rightPadding;
		availableImageWidth = availableImageWidth < 0 ? 0 : availableImageWidth;

		int availableImageHeight = image.getHeight() - topPadding - bottomPadding;
		availableImageHeight = availableImageHeight < 0 ? 0 : availableImageHeight;

		Renderable renderer = image.getRenderer();

		if (
			renderer != null
			&& availableImageWidth > 0 
			&& availableImageHeight > 0
			)
		{
			InternalImageProcessor imageProcessor = 
				new InternalImageProcessor(
					image, 
					image.getScaleImageValue() != ScaleImageEnum.FILL_FRAME,
					availableImageWidth,
					availableImageHeight
					);
				
			InternalImageProcessorResult imageProcessorResult = null;
			
			try
			{
				imageProcessorResult = imageProcessor.process(renderer);
			}
			catch (Exception e)
			{
				Renderable onErrorRenderer = getRendererUtil().handleImageError(e, image.getOnErrorTypeValue());
				if (onErrorRenderer != null)
				{
					imageProcessorResult = imageProcessor.process(onErrorRenderer);
				}
			}
			
			if (imageProcessorResult != null)//FIXMEPPTX render background for null images, like other exporters do 
			{
				int width = availableImageWidth;
				int height = availableImageHeight;

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
//						cropTop = 100000 * topPadding / availableImageHeight;
//						cropLeft = 100000 * leftPadding / availableImageWidth;
//						cropBottom = 100000 * bottomPadding / availableImageHeight;
//						cropRight = 100000 * rightPadding / availableImageWidth;
	 					break;
					}
					case CLIP :
					{
						double normalWidth = availableImageWidth;
						double normalHeight = availableImageHeight;

						Dimension2D dimension = imageProcessorResult.dimension;
						if (dimension != null)
						{
							normalWidth = dimension.getWidth();
							normalHeight = dimension.getHeight();
						}

//						if (normalWidth > availableImageWidth)
//						{
							switch (image.getHorizontalImageAlign())
							{
								case RIGHT :
								{
									cropLeft = 100000 * (availableImageWidth - normalWidth) / availableImageWidth;
									cropRight = 0;
//									cropRight = 100000 * rightPadding / availableImageWidth;
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
//									cropLeft = 100000 * leftPadding / availableImageWidth;
									cropLeft = 0;
									cropRight = 100000 * (availableImageWidth - normalWidth) / availableImageWidth;
									break;
								}
							}
//							width = availableImageWidth;
////							cropLeft = cropLeft / 0.75d;
////							cropRight = cropRight / 0.75d;
//						}
//						else
//						{
//							width = (int)normalWidth;
//						}

//						if (normalHeight > availableImageHeight)
//						{
							switch (image.getVerticalImageAlign())
							{
								case TOP :
								{
//									cropTop = 100000 * topPadding / availableImageHeight;
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
//									cropBottom = 100000 * bottomPadding / availableImageHeight;
									break;
								}
							}
//							height = availableImageHeight;
//							cropTop = cropTop / 0.75d;
//							cropBottom = cropBottom / 0.75d;
//						}
//						else
//						{
//							height = (int)normalHeight;
//						}

						break;
					}
					case RETAIN_SHAPE :
					default :
					{
						double normalWidth = availableImageWidth;
						double normalHeight = availableImageHeight;

						Dimension2D dimension = imageProcessorResult.dimension;
						if (dimension != null)
						{
							normalWidth = dimension.getWidth();
							normalHeight = dimension.getHeight();
						}

						double ratio = normalWidth / normalHeight;

						if( ratio > availableImageWidth / (double)availableImageHeight )
						{
							width = availableImageWidth;
							height = (int)(width/ratio);

							switch (image.getVerticalImageAlign())
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

							switch (image.getHorizontalImageAlign())
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

//				insertPageAnchor();
//				if (image.getAnchorName() != null)
//				{
//					tempBodyWriter.write("<text:bookmark text:name=\"");
//					tempBodyWriter.write(image.getAnchorName());
//					tempBodyWriter.write("\"/>");
//				}


//				boolean startedHyperlink = startHyperlink(image,false);

				slideRelsHelper.exportImage(imageProcessorResult.imagePath);

				slideHelper.write("<p:pic>\n");
				slideHelper.write("  <p:nvPicPr>\n");
				slideHelper.write("    <p:cNvPr id=\"" + toOOXMLId(image) + "\" name=\"Picture\">\n");

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
				slideHelper.write("<a:blip r:embed=\"" + imageProcessorResult.imagePath + "\"/>");
				slideHelper.write("<a:srcRect");
////				if (cropLeft > 0)
////				{
//					slideHelper.write(" l=\"" + (int)(100000 * leftPadding / image.getWidth()) + "\"");
////				}
////				if (cropTop > 0)
////				{
//					slideHelper.write(" t=\"" + (int)cropTop + "\"");
////				}
////				if (cropRight > 0)
////				{
//					slideHelper.write(" r=\"" + (int)cropRight + "\"");
////				}
////				if (cropBottom > 0)
////				{
//					slideHelper.write(" b=\"" + (int)cropBottom + "\"");
////				}
				slideHelper.write("/>");
				slideHelper.write("<a:stretch><a:fillRect");
//				if (cropLeft > 0)
//				{
					slideHelper.write(" l=\"" + (int)cropLeft + "\"");
//				}
//				if (cropTop > 0)
//				{
					slideHelper.write(" t=\"" + (int)cropTop + "\"");
//				}
//				if (cropRight > 0)
//				{
					slideHelper.write(" r=\"" + (int)cropRight + "\"");
//				}
//				if (cropBottom > 0)
//				{
					slideHelper.write(" b=\"" + (int)cropBottom + "\"");
//				}
				slideHelper.write("/></a:stretch>\n");
				slideHelper.write("</p:blipFill>\n");
				slideHelper.write("  <p:spPr>\n");
				slideHelper.write("    <a:xfrm>\n");
				slideHelper.write("      <a:off x=\"" + LengthUtil.emu(image.getX() + getOffsetX() + leftPadding) + "\" y=\"" + LengthUtil.emu(image.getY() + getOffsetY() + topPadding) + "\"/>\n");
				slideHelper.write("      <a:ext cx=\"" + LengthUtil.emu(availableImageWidth) + "\" cy=\"" + LengthUtil.emu(availableImageHeight) + "\"/>\n");
				slideHelper.write("    </a:xfrm><a:prstGeom prst=\"rect\"><a:avLst/></a:prstGeom>\n");
				if (image.getModeValue() == ModeEnum.OPAQUE && image.getBackcolor() != null)
				{
					slideHelper.write("<a:solidFill><a:srgbClr val=\"" + JRColorUtil.getColorHexa(image.getBackcolor()) + "\"/></a:solidFill>\n");
				}
				JRPen pen = getPptxPen(image.getLineBox());
				if (pen != null)
				{
					slideHelper.write("  <a:ln w=\"" + LengthUtil.emu(pen.getLineWidth()) + "\"");
					if(LineStyleEnum.DOUBLE.equals(pen.getLineStyleValue()))
					{
						slideHelper.write(" cmpd=\"dbl\"");
					}
					slideHelper.write(">\n");
					slideHelper.write("<a:solidFill><a:srgbClr val=\"" + JRColorUtil.getColorHexa(pen.getLineColor()) + "\"/></a:solidFill>\n");
					slideHelper.write("<a:prstDash val=\"");
					switch (pen.getLineStyleValue())
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

//				if(startedHyperlink)
//				{
//					endHyperlink(false);
//				}
			}
		}

//		docHelper.write("</w:p>");
	}

	private class InternalImageProcessor
	{
		private final JRPrintElement imageElement;
		private final boolean needDimension; 
		private final int availableImageWidth;
		private final int availableImageHeight;

		protected InternalImageProcessor(
			JRPrintElement imageElement,
			boolean needDimension,
			int availableImageWidth,
			int availableImageHeight
			)
		{
			this.imageElement = imageElement;
			this.needDimension = needDimension;
			this.availableImageWidth = availableImageWidth;
			this.availableImageHeight = availableImageHeight;
		}
		
		private InternalImageProcessorResult process(Renderable renderer) throws JRException
		{
			if (renderer instanceof ResourceRenderer)
			{
				renderer = renderersCache.getLoadedRenderer((ResourceRenderer)renderer);
			}
			
			// check dimension first, to avoid caching renderers that might not be used eventually, due to their dimension errors 
			Dimension2D dimension = null;
			if (needDimension)
			{
				DimensionRenderable dimensionRenderer = renderersCache.getDimensionRenderable(renderer);
				dimension = dimensionRenderer == null ? null :  dimensionRenderer.getDimension(jasperReportsContext);
			}
			
			
			String imagePath = null;

//			if (image.isLazy()) //FIXMEPPTX learn how to link images
//			{
//
//			}
//			else
//			{
				if (
					renderer instanceof DataRenderable //we do not cache imagePath for non-data renderers because they render width different width/height each time
					&& rendererToImagePathMap.containsKey(renderer.getId())
					)
				{
					imagePath = rendererToImagePathMap.get(renderer.getId());
				}
				else
				{
					JRPrintElementIndex imageIndex = getElementIndex();

					DataRenderable imageRenderer = 
							getRendererUtil().getImageDataRenderable(
								renderersCache,
								renderer,
								new Dimension(availableImageWidth, availableImageHeight),
								ModeEnum.OPAQUE == imageElement.getModeValue() ? imageElement.getBackcolor() : null
								);

					byte[] imageData = imageRenderer.getData(jasperReportsContext);
					String fileExtension = JRTypeSniffer.getImageTypeValue(imageData).getFileExtension();
					String imageName = IMAGE_NAME_PREFIX + imageIndex.toString() + (fileExtension == null ? "" : ("." + fileExtension));

					pptxZip.addEntry(//FIXMEPPTX optimize with a different implementation of entry
						new FileBufferedZipEntry(
							"ppt/media/" + imageName,
							imageData
							)
						);
					
					//presentationRelsHelper.exportImage(imageName, extension);
					
					imagePath = imageName;
					//imagePath = "Pictures/" + imageName;

					if (imageRenderer == renderer)
					{
						//cache imagePath only for true ImageRenderable instances because the wrapping ones render with different width/height each time
						rendererToImagePathMap.put(renderer.getId(), imagePath);
					}
				}
//			}
			
			return new InternalImageProcessorResult(imagePath, dimension);
		}
	}

	private class InternalImageProcessorResult
	{
		protected final String imagePath;
		protected final Dimension2D dimension;
		
		protected InternalImageProcessorResult(String imagePath, Dimension2D dimension)
		{
			this.imagePath = imagePath;
			this.dimension = dimension;
		}
	}


	protected JRPrintElementIndex getElementIndex()
	{
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < frameIndexStack.size(); i++)
		{
			Integer frameIndex = frameIndexStack.get(i);

			sb.append(frameIndex).append("_");
		}
		
		JRPrintElementIndex imageIndex =
			new JRPrintElementIndex(
					reportIndex,
					pageIndex,
					sb.append(elementIndex).toString()
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
			StringBuilder coordsEnum = new StringBuilder(coords.length * 4);
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
			throw 
				new JRRuntimeException(
					EXCEPTION_MESSAGE_KEY_INVALID_IMAGE_NAME,
					new Object[]{imageName});
		}

		return JRPrintElementIndex.parsePrintElementIndex(imageName.substring(IMAGE_NAME_PREFIX_LEGTH));
	}


	/**
	 *
	 */
	public void exportFrame(JRPrintFrame frame) throws JRException
	{
		slideHelper.write("<p:sp>\n");
		slideHelper.write("  <p:nvSpPr>\n");
		slideHelper.write("    <p:cNvPr id=\"" + toOOXMLId(frame) + "\" name=\"Frame\"/>\n");
		slideHelper.write("    <p:cNvSpPr>\n");
		slideHelper.write("      <a:spLocks noGrp=\"1\"/>\n");
		slideHelper.write("    </p:cNvSpPr>\n");
		slideHelper.write("    <p:nvPr/>\n");
		slideHelper.write("  </p:nvSpPr>\n");
		slideHelper.write("  <p:spPr>\n");
		slideHelper.write("    <a:xfrm>\n");
		slideHelper.write("      <a:off x=\"" + LengthUtil.emu(frame.getX() + getOffsetX()) + "\" y=\"" + LengthUtil.emu(frame.getY() + getOffsetY()) + "\"/>\n");
		slideHelper.write("      <a:ext cx=\"" + LengthUtil.emu(frame.getWidth()) + "\" cy=\"" + LengthUtil.emu(frame.getHeight()) + "\"/>\n");
		slideHelper.write("    </a:xfrm><a:prstGeom prst=\"rect\"><a:avLst/></a:prstGeom>\n");
		if (frame.getModeValue() == ModeEnum.OPAQUE && frame.getBackcolor() != null)
		{
			slideHelper.write("<a:solidFill><a:srgbClr val=\"" + JRColorUtil.getColorHexa(frame.getBackcolor()) + "\"/></a:solidFill>\n");
		}
		JRPen pen = getPptxPen(frame.getLineBox());
		if (pen != null)
		{
			slideHelper.write("  <a:ln w=\"" + LengthUtil.emu(pen.getLineWidth()) + "\"");
			if(LineStyleEnum.DOUBLE.equals(pen.getLineStyleValue()))
			{
				slideHelper.write(" cmpd=\"dbl\"");
			}
			slideHelper.write(">\n");
			slideHelper.write("<a:solidFill><a:srgbClr val=\"" + JRColorUtil.getColorHexa(pen.getLineColor()) + "\"/></a:solidFill>\n");
			slideHelper.write("<a:prstDash val=\"");
			switch (pen.getLineStyleValue())
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
		GenericElementHandlerEnviroment.getInstance(getJasperReportsContext()).getElementHandler(
				element.getGenericType(), PPTX_EXPORTER_KEY);

		if (handler != null)
		{
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

		Boolean ignoreHyperlink = HyperlinkUtil.getIgnoreHyperlink(PptxReportConfiguration.PROPERTY_IGNORE_HYPERLINK, link);
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
							try 
							{
								href = link.getHyperlinkReference().replaceAll("\\s", URLEncoder.encode(" ","UTF-8"));
							} 
							catch (UnsupportedEncodingException e) 
							{
								href = link.getHyperlinkReference();
							}
							
						}
						break;
					}
					case LOCAL_ANCHOR :
					{
//						if (link.getHyperlinkAnchor() != null)
//						{
//							href = "#" + link.getHyperlinkAnchor();
//						}
						break;
					}
					case LOCAL_PAGE :
					{
//						if (link.getHyperlinkPage() != null)
//						{
//							href = "#" + JR_PAGE_ANCHOR_PREFIX + reportIndex + "_" + link.getHyperlinkPage().toString();
//						}
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
//						if (
//							link.getHyperlinkReference() != null &&
//							link.getHyperlinkPage() != null
//							)
//						{
//							href = link.getHyperlinkReference() + "#" + JR_PAGE_ANCHOR_PREFIX + "0_" + link.getHyperlinkPage().toString();
//						}
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
	
	@Override
	public String getExporterKey()
	{
		return PPTX_EXPORTER_KEY;
	}

	@Override
	public String getExporterPropertiesPrefix()
	{
		return PPTX_EXPORTER_PROPERTIES_PREFIX;
	}

	protected String toOOXMLId(JRPrintElement element)
	{
		// using hashCode() for now, though in theory there is a risk of collisions
		// we could use something based on getSourceElementId() and getPrintElementId()
		// or even a counter since we do not have any references to Ids
		int hashCode = element.hashCode();
		// OOXML object ids are xsd:unsignedInt 
		return Long.toString(hashCode & 0xFFFFFFFFL); 
	}
	
	protected JRPen getPptxPen(JRLineBox box)
	{
		JRBasePen pen = null;
		Float lineWidth = box.getPen().getLineWidth();
		if(lineWidth == 0)
		{
			// PPTX does not support side borders
			// in case side borders are defined for the report element, ensure that all 4 are declared and all of them come with the same settings
			if(
				((JRBasePen)box.getTopPen()).isIdentical(box.getLeftPen())
				&& ((JRBasePen)box.getTopPen()).isIdentical(box.getBottomPen())
				&& ((JRBasePen)box.getTopPen()).isIdentical(box.getRightPen())
				&& box.getTopPen().getLineWidth() > 0
				)
			{
				pen = new JRBasePen(box);
				pen.setLineWidth(box.getTopPen().getLineWidth());
				pen.setLineColor(box.getTopPen().getLineColor());
				pen.setLineStyle(box.getTopPen().getLineStyleValue());
			}
		}
		else
		{
			pen = new JRBasePen(box);
			pen.setLineWidth(lineWidth);
			pen.setLineColor(box.getPen().getLineColor());
			pen.setLineStyle(box.getPen().getLineStyleValue());
		}
		return pen;
	}
}

