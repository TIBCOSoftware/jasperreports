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
package net.sf.jasperreports.charts.util;

import java.awt.geom.Rectangle2D;
import java.io.StringWriter;
import java.util.List;

import net.sf.jasperreports.engine.JRRenderable;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.renderers.BatikRenderer;

import org.apache.batik.dom.GenericDOMImplementation;
import org.apache.batik.svggen.SVGGraphics2D;
import org.apache.batik.svggen.SVGGraphics2DIOException;
import org.jfree.chart.JFreeChart;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id: JRPieChartImageMapRenderer.java 1364 2006-08-31 15:13:20Z lucianc $
 */
public class SvgChartRendererFactory implements ChartRendererFactory
{
	
	public JRRenderable getRenderer(
		JFreeChart chart, 
		ChartHyperlinkProvider chartHyperlinkProvider,
		Rectangle2D rectangle
		)
	{
		DOMImplementation domImpl = 
		    GenericDOMImplementation.getDOMImplementation();
		Document document = 
		    domImpl.createDocument(null, "svg", null);
		SVGGraphics2D grx = 
		    new SVGGraphics2D(document);
		
		grx.setSVGCanvasSize(rectangle.getBounds().getSize());

		List areaHyperlinks = null;

		if (chartHyperlinkProvider != null && chartHyperlinkProvider.hasHyperlinks())
		{
			areaHyperlinks = ChartUtil.getImageAreaHyperlinks(chart, chartHyperlinkProvider, grx, rectangle);
		}
		else
		{
			chart.draw(grx, rectangle);
		}

		try
		{
			StringWriter swriter = new StringWriter();
			grx.stream(swriter);
			return new BatikRenderer(swriter.getBuffer().toString(), areaHyperlinks);
		}
		catch (SVGGraphics2DIOException e)
		{
			throw new JRRuntimeException(e);
		}
	}

}
