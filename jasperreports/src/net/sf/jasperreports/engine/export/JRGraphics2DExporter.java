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
 * Eugene D - eugenedruy@users.sourceforge.net 
 * Adrian Jackson - iapetus@users.sourceforge.net
 * David Taylor - exodussystems@users.sourceforge.net
 * Lars Kristensen - llk@users.sourceforge.net
 */
package net.sf.jasperreports.engine.export;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.util.List;

import net.sf.jasperreports.engine.DefaultJasperReportsContext;
import net.sf.jasperreports.engine.JRAbstractExporter;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRGenericElementType;
import net.sf.jasperreports.engine.JRPrintPage;
import net.sf.jasperreports.engine.JRPropertiesUtil;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.export.draw.FrameDrawer;
import net.sf.jasperreports.engine.util.JRGraphEnvInitializer;
import net.sf.jasperreports.export.Graphics2DExporterConfiguration;
import net.sf.jasperreports.export.Graphics2DExporterOutput;
import net.sf.jasperreports.export.Graphics2DReportConfiguration;
import net.sf.jasperreports.export.ReportExportConfiguration;


/**
 * Exports a JasperReports document to a <tt>Graphics2D</tt> object. Since all font measurement and layout
 * calculation during report filling is done using AWT, this is considered the perfect exporter, unlike the others,
 * which are only approximations of the initial document.
 * <p>
 * As its name indicates, this exporter is special because it does not produce files or does not send character
 * or binary data to an output stream.
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JRGraphics2DExporter extends JRAbstractExporter<Graphics2DReportConfiguration, Graphics2DExporterConfiguration, Graphics2DExporterOutput, JRGraphics2DExporterContext>
{
	private static final float DEFAULT_ZOOM = 1f;

	/**
	 * @deprecated Replaced by {@link Graphics2DReportConfiguration#MINIMIZE_PRINTER_JOB_SIZE}.
	 */
	public static final String MINIMIZE_PRINTER_JOB_SIZE = Graphics2DReportConfiguration.MINIMIZE_PRINTER_JOB_SIZE;

	private static final String GRAPHICS2D_EXPORTER_PROPERTIES_PREFIX = JRPropertiesUtil.PROPERTY_PREFIX + "export.graphics2d.";

	/**
	 * The exporter key, as used in
	 * {@link GenericElementHandlerEnviroment#getElementHandler(JRGenericElementType, String)}.
	 */
	public static final String GRAPHICS2D_EXPORTER_KEY = JRPropertiesUtil.PROPERTY_PREFIX + "graphics2d";
	
	/**
	 *
	 */
	protected AwtTextRenderer textRenderer;
	protected FrameDrawer frameDrawer;

	protected class ExporterContext extends BaseExporterContext implements JRGraphics2DExporterContext
	{
	}
	
	/**
	 * @see #JRGraphics2DExporter(JasperReportsContext)
	 */
	public JRGraphics2DExporter() throws JRException
	{
		this(DefaultJasperReportsContext.getInstance());
	}
	

	/**
	 *
	 */
	public JRGraphics2DExporter(JasperReportsContext jasperReportsContext) throws JRException
	{
		super(jasperReportsContext);
		
		JRGraphEnvInitializer.initializeGraphEnv();

		exporterContext = new ExporterContext();
	}


	/**
	 *
	 */
	protected Class<Graphics2DExporterConfiguration> getConfigurationInterface()
	{
		return Graphics2DExporterConfiguration.class;
	}


	/**
	 *
	 */
	protected Class<Graphics2DReportConfiguration> getItemConfigurationInterface()
	{
		return Graphics2DReportConfiguration.class;
	}
	

	/**
	 *
	 */
	@SuppressWarnings("deprecation")
	protected void ensureOutput()
	{
		if (exporterOutput == null)
		{
			exporterOutput = new net.sf.jasperreports.export.parameters.ParametersGraphics2DExporterOutput(parameters);
		}
	}
	

	/**
	 *
	 */
	public void exportReport() throws JRException
	{
		/*   */
		ensureJasperReportsContext();
		ensureInput();

		initExport();
		
		ensureOutput();

		Graphics2D grx = getExporterOutput().getGraphics2D();
		
		try
		{
			exportReportToGraphics2D(grx);
		}
		finally
		{
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
		
		setOffset(false);

		Graphics2DReportConfiguration configuration = getCurrentItemConfiguration();
		
		Boolean isMinimizePrinterJobSize = configuration.isMinimizePrinterJobSize();
		Boolean isIgnoreMissingFont = configuration.isIgnoreMissingFont();
		
		textRenderer = 
			new AwtTextRenderer(
				jasperReportsContext,
				isMinimizePrinterJobSize == null ? Boolean.TRUE : isMinimizePrinterJobSize,
				isIgnoreMissingFont == null ? Boolean.FALSE : isIgnoreMissingFont
				);

		frameDrawer = new FrameDrawer(exporterContext, filter, textRenderer);
	}

	
	/**
	 *
	 */
	public void exportReportToGraphics2D(Graphics2D grx) throws JRException
	{
		grx.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		//grx.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
		grx.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
		grx.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);

		setCurrentExporterInputItem(exporterInput.getItems().get(0));
		
		ReportExportConfiguration configuration = getCurrentItemConfiguration();
		
		AffineTransform atrans = new AffineTransform();
		atrans.translate(
			configuration.getOffsetX() == null ? 0 : configuration.getOffsetX(), 
			configuration.getOffsetY() == null ? 0 : configuration.getOffsetY()
			);
		float zoom = getZoom();
		atrans.scale(zoom, zoom);
		grx.transform(atrans);

		List<JRPrintPage> pages = jasperPrint.getPages();
		if (pages != null)
		{
			PageRange pageRange = getPageRange();
			int startPageIndex = (pageRange == null || pageRange.getStartPageIndex() == null) ? 0 : pageRange.getStartPageIndex();

			Shape oldClipShape = grx.getClip();
	
			grx.clip(new Rectangle(0, 0, jasperPrint.getPageWidth(), jasperPrint.getPageHeight()));
	
			try
			{
				JRPrintPage page = pages.get(startPageIndex);
				exportPage(grx, page);
			}
			finally
			{
				grx.setClip(oldClipShape);
			}
		}
	}
	

	/**
	 *
	 */
	protected void exportPage(Graphics2D grx, JRPrintPage page) throws JRException
	{
		grx.setColor(Color.white);
		grx.fillRect(
			0, 
			0, 
			jasperPrint.getPageWidth(), 
			jasperPrint.getPageHeight()
			);

		grx.setColor(Color.black);
		grx.setStroke(new BasicStroke(1));

		/*   */
		frameDrawer.draw(grx, page.getElements(), getOffsetX(), getOffsetY());
		
		JRExportProgressMonitor progressMonitor = getCurrentItemConfiguration().getProgressMonitor();
		if (progressMonitor != null)
		{
			progressMonitor.afterPageExport();
		}
	}

	/**
	 *
	 */
	public String getExporterKey()
	{
		return GRAPHICS2D_EXPORTER_KEY;
	}

	/**
	 * 
	 */
	public String getExporterPropertiesPrefix()
	{
		return GRAPHICS2D_EXPORTER_PROPERTIES_PREFIX;
	}


	/**
	 * @return the frameDrawer
	 */
	public FrameDrawer getFrameDrawer()
	{
		return this.frameDrawer;
	}


	private float getZoom()//FIXMEEXPORT
	{
		float zoom = DEFAULT_ZOOM;
		
		Float zoomRatio = getCurrentItemConfiguration().getZoomRatio();
		if (zoomRatio != null)
		{
			zoom = zoomRatio.floatValue();
			if (zoom <= 0)
			{
				throw new JRRuntimeException("Invalid zoom ratio : " + zoom);
			}
		}

		return zoom;
	}
}
