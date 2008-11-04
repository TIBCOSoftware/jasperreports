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

import net.sf.jasperreports.engine.JRAbstractExporter;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JRPrintPage;
import net.sf.jasperreports.engine.export.draw.FrameDrawer;
import net.sf.jasperreports.engine.export.legacy.BorderOffset;
import net.sf.jasperreports.engine.util.JRGraphEnvInitializer;
import net.sf.jasperreports.engine.util.JRProperties;


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
public class JRGraphics2DExporter extends JRAbstractExporter
{

	private static final float DEFAULT_ZOOM = 1f;

	/**
	 * Property that provides a default value for the 
	 * {@link net.sf.jasperreports.engine.export.JRGraphics2DExporterParameter#MINIMIZE_PRINTER_JOB_SIZE JRGraphics2DExporterParameter.MINIMIZE_PRINTER_JOB_SIZE}
	 * Graphics2D exporter parameter.
	 * 
	 * @see net.sf.jasperreports.engine.export.JRGraphics2DExporterParameter#MINIMIZE_PRINTER_JOB_SIZE
	 */
	public static final String MINIMIZE_PRINTER_JOB_SIZE = JRProperties.PROPERTY_PREFIX + "export.graphics2d.min.job.size";

	private static final String GRAPHICS2D_EXPORTER_PROPERTIES_PREFIX = JRProperties.PROPERTY_PREFIX + "export.graphics2d.";

	/**
	 *
	 */
	protected Graphics2D grx = null;
	protected JRExportProgressMonitor progressMonitor = null;
	protected float zoom = DEFAULT_ZOOM;

	protected TextRenderer textRenderer = null;
	protected FrameDrawer frameDrawer = null;

	/**
	 *
	 */
	public JRGraphics2DExporter() throws JRException
	{
		JRGraphEnvInitializer.initializeGraphEnv();
	}
	

	/**
	 *
	 */
	public void exportReport() throws JRException
	{
		progressMonitor = (JRExportProgressMonitor)parameters.get(JRExporterParameter.PROGRESS_MONITOR);
		
		/*   */
		setOffset(false);

		try
		{
			/*   */
			setExportContext();
	
			/*   */
			setInput();
			
			if (!parameters.containsKey(JRExporterParameter.FILTER))
			{
				filter = createFilter(GRAPHICS2D_EXPORTER_PROPERTIES_PREFIX);
			}

			/*   */
			setPageRange();
	
			/*   */
			setTextRenderer();
			
			grx = (Graphics2D)parameters.get(JRGraphics2DExporterParameter.GRAPHICS_2D);
			if (grx == null)
			{
				throw new JRException("No output specified for the exporter. java.awt.Graphics2D object expected.");
			}
			
			BorderOffset.setLegacy(
				JRProperties.getBooleanProperty(jasperPrint, BorderOffset.PROPERTY_LEGACY_BORDER_OFFSET, false)
				);

			/*   */
			setDrawers();

			Float zoomRatio = (Float)parameters.get(JRGraphics2DExporterParameter.ZOOM_RATIO);
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
	
			exportReportToGraphics2D();
		}
		finally
		{
			resetExportContext();
		}
	}
		

	protected void setTextRenderer()
	{
		boolean isMinimizePrinterJobSize = true;
		Boolean isMinimizePrinterJobSizeParam = (Boolean) parameters.get(JRGraphics2DExporterParameter.MINIMIZE_PRINTER_JOB_SIZE);
		if (isMinimizePrinterJobSizeParam == null)
		{
			isMinimizePrinterJobSize = JRProperties.getBooleanProperty(MINIMIZE_PRINTER_JOB_SIZE);
		}
		else
		{
			isMinimizePrinterJobSize = isMinimizePrinterJobSizeParam.booleanValue();
		}
		
		textRenderer = new TextRenderer(isMinimizePrinterJobSize);
	}

	
	protected void setDrawers()
	{
		frameDrawer = new FrameDrawer(filter, textRenderer);
	}

	
	/**
	 *
	 */
	public void exportReportToGraphics2D() throws JRException
	{
		grx.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		//grx.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
		grx.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
		grx.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);

		AffineTransform atrans = new AffineTransform();
		atrans.translate(globalOffsetX, globalOffsetY);
		atrans.scale(zoom, zoom);
		grx.transform(atrans);

		java.util.List pages = jasperPrint.getPages();
		if (pages != null)
		{
			Shape oldClipShape = grx.getClip();
	
			grx.clip(new Rectangle(0, 0, jasperPrint.getPageWidth(), jasperPrint.getPageHeight()));
	
			try
			{
				JRPrintPage page = (JRPrintPage)pages.get(startPageIndex);
				exportPage(page);
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
	protected void exportPage(JRPrintPage page) throws JRException
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
		
		if (progressMonitor != null)
		{
			progressMonitor.afterPageExport();
		}
	}

}
