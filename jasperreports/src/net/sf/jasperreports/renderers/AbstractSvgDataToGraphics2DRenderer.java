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
package net.sf.jasperreports.renderers;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Dimension2D;
import java.awt.geom.Rectangle2D;
import java.util.List;

import org.apache.batik.anim.dom.SAXSVGDocumentFactory;
import org.apache.batik.bridge.BridgeContext;
import org.apache.batik.bridge.GVTBuilder;
import org.apache.batik.bridge.UserAgent;
import org.apache.batik.bridge.ViewBox;
import org.apache.batik.dom.svg.SVGDocumentFactory;
import org.apache.batik.gvt.GraphicsNode;
import org.w3c.dom.svg.SVGDocument;
import org.w3c.dom.svg.SVGPreserveAspectRatio;

import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRPrintImageAreaHyperlink;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.JasperReportsContext;


/**
 * SVG renderer implementation based on <a href="http://xmlgraphics.apache.org/batik/">Batik</a>.
 *
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 */
public abstract class AbstractSvgDataToGraphics2DRenderer extends AbstractRenderToImageAwareRenderer implements DataRenderable, Graphics2DRenderable, DimensionRenderable, AreaHyperlinksRenderable
{
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

	private List<JRPrintImageAreaHyperlink> areaHyperlinks;

	private transient GraphicsNode rootNode;
	private transient Dimension2D documentSize;

	/**
	 * Creates a SVG renderer.
	 *
	 * @param areaHyperlinks a list of {@link JRPrintImageAreaHyperlink area hyperlinks}
	 */
	protected AbstractSvgDataToGraphics2DRenderer(List<JRPrintImageAreaHyperlink> areaHyperlinks)
	{
		this.areaHyperlinks = areaHyperlinks;
	}

	@Override
	public void render(JasperReportsContext jasperReportsContext, Graphics2D grx, Rectangle2D rectangle) throws JRException
	{
		ensureSvg(jasperReportsContext);

		AffineTransform transform = ViewBox.getPreserveAspectRatioTransform(
				new float[]{0, 0, (float) documentSize.getWidth(), (float) documentSize.getHeight()},
				SVGPreserveAspectRatio.SVG_PRESERVEASPECTRATIO_NONE, true,
				(float) rectangle.getWidth(), (float) rectangle.getHeight());
		Graphics2D graphics = (Graphics2D) grx.create();
		try
		{
			graphics.translate(rectangle.getX(), rectangle.getY());
			graphics.transform(transform);

			// CompositeGraphicsNode not thread safe
			synchronized (rootNode)
			{
				rootNode.paint(graphics);
			}
		}
		finally
		{
			graphics.dispose();
		}
	}

	@Override
	public Dimension2D getDimension(JasperReportsContext jasperReportsContext)
	{
		try
		{
			ensureSvg(jasperReportsContext);
			return documentSize;
		}
		catch (JRException e)
		{
			throw new JRRuntimeException(e);
		}
	}

	protected synchronized void ensureSvg(JasperReportsContext jasperReportsContext) throws JRException
	{
		if (rootNode != null)
		{
			// already loaded
			return;
		}

		UserAgent userAgent = new BatikUserAgent(jasperReportsContext);
		
		SVGDocumentFactory documentFactory =
			new SAXSVGDocumentFactory(userAgent.getXMLParserClassName(), true);
		documentFactory.setValidating(userAgent.isXMLParserValidating());

		SVGDocument document = getSvgDocument(jasperReportsContext, documentFactory);

		BridgeContext ctx = new BridgeContext(userAgent);
		ctx.setDynamic(true);
		GVTBuilder builder = new GVTBuilder();
		rootNode = builder.build(ctx, document);
		documentSize = ctx.getDocumentSize();
	}
	

	/**
	 * 
	 */
	protected abstract SVGDocument getSvgDocument(
		JasperReportsContext jasperReportsContext,
		SVGDocumentFactory documentFactory
		) throws JRException;

	
	@Override
	public List<JRPrintImageAreaHyperlink> getImageAreaHyperlinks(Rectangle2D renderingArea) throws JRException
	{
		return areaHyperlinks;
	}

	@Override
	public boolean hasImageAreaHyperlinks()
	{
		return areaHyperlinks != null && !areaHyperlinks.isEmpty();
	}
}
