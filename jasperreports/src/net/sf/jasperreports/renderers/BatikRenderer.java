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
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Dimension2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.URL;
import java.util.List;

import net.sf.jasperreports.engine.DefaultJasperReportsContext;
import net.sf.jasperreports.engine.ImageMapRenderable;
import net.sf.jasperreports.engine.JRAbstractSvgRenderer;
import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRPrintImageAreaHyperlink;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.repo.RepositoryUtil;

import org.apache.batik.bridge.BridgeContext;
import org.apache.batik.bridge.GVTBuilder;
import org.apache.batik.bridge.UserAgent;
import org.apache.batik.bridge.UserAgentAdapter;
import org.apache.batik.bridge.ViewBox;
import org.apache.batik.dom.svg.SAXSVGDocumentFactory;
import org.apache.batik.dom.svg.SVGDocumentFactory;
import org.apache.batik.ext.awt.image.GraphicsUtil;
import org.apache.batik.gvt.GraphicsNode;
import org.w3c.dom.svg.SVGDocument;
import org.w3c.dom.svg.SVGPreserveAspectRatio;


/**
 * SVG renderer implementation based on <a href="http://xmlgraphics.apache.org/batik/">Batik</a>.
 *
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 */
public class BatikRenderer extends JRAbstractSvgRenderer implements ImageMapRenderable
{
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

	protected static class JRUserAgent extends UserAgentAdapter
	{
		public float getPixelUnitToMillimeter()
		{
			// JR works at 72dpi
			return 0.35277777777777777777777777777778f;
		}
	}
	
	private String svgText;
	private byte[] svgData;
	private String svgDataLocation;
	private List<JRPrintImageAreaHyperlink> areaHyperlinks;

	private int minDPI;
	private boolean antiAlias;
	
	
	private transient GraphicsNode rootNode;
	private transient Dimension2D documentSize;

	protected BatikRenderer(List<JRPrintImageAreaHyperlink> areaHyperlinks)
	{
		this.areaHyperlinks = areaHyperlinks;
	}

	/**
	 * Creates a SVG renderer.
	 *
	 * @param svgText the SVG text
	 * @param areaHyperlinks a list of {@link JRPrintImageAreaHyperlink area hyperlinks}
	 */
	public BatikRenderer(String svgText, List<JRPrintImageAreaHyperlink> areaHyperlinks)
	{
		this.svgText = svgText;
		this.areaHyperlinks = areaHyperlinks;
	}

	/**
	 * Creates a SVG renderer.
	 *
	 * @param svgData the SVG (binary) data
	 * @param areaHyperlinks a list of {@link JRPrintImageAreaHyperlink area hyperlinks}
	 */
	public BatikRenderer(byte[] svgData, List<JRPrintImageAreaHyperlink> areaHyperlinks)
	{
		this.svgData = svgData;
		this.areaHyperlinks = areaHyperlinks;
	}

	/**
	 * @deprecated Replaced by {@link #render(JasperReportsContext, Graphics2D, Rectangle2D)}.
	 */
	public void render(Graphics2D grx, Rectangle2D rectangle) throws JRException
	{
		render(DefaultJasperReportsContext.getInstance(), grx, rectangle);
	}
	
	public void render(JasperReportsContext jasperReportsContext, Graphics2D grx, Rectangle2D rectangle) throws JRException
	{
		ensureSvg(jasperReportsContext);

		AffineTransform transform = ViewBox.getPreserveAspectRatioTransform(
				new float[]{0, 0, (float) documentSize.getWidth(), (float) documentSize.getHeight()},
				SVGPreserveAspectRatio.SVG_PRESERVEASPECTRATIO_NONE, true,
				(float) rectangle.getWidth(), (float) rectangle.getHeight());
		Graphics2D graphics = (Graphics2D) grx.create();
		graphics.translate(rectangle.getX(), rectangle.getY());
		graphics.transform(transform);

		// CompositeGraphicsNode not thread safe
		synchronized (rootNode)
		{
			rootNode.paint(graphics);
		}
	}

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

	/**
	 * @deprecated Replaced by {@link #getDimension(JasperReportsContext)}.
	 */
	public Dimension2D getDimension()
	{
		return getDimension(DefaultJasperReportsContext.getInstance());
	}

	protected synchronized void ensureData(JasperReportsContext jasperReportsContext) throws JRException
	{
		if (svgText == null
				&& svgData == null && svgDataLocation != null)
		{
			svgData = RepositoryUtil.getInstance(jasperReportsContext).getBytesFromLocation(svgDataLocation);
		}
	}

	/**
	 * @deprecated Replaced by {@link #ensureData(JasperReportsContext)}.
	 */
	protected synchronized void ensureData() throws JRException
	{
		ensureData(DefaultJasperReportsContext.getInstance());
	}

	protected synchronized void ensureSvg(JasperReportsContext jasperReportsContext) throws JRException
	{
		if (rootNode != null)
		{
			// already loaded
			return;
		}

		ensureData(jasperReportsContext);
		
		try
		{
			UserAgent userAgent = new JRUserAgent();
			
			SVGDocumentFactory documentFactory =
				new SAXSVGDocumentFactory(userAgent.getXMLParserClassName(), true);
			documentFactory.setValidating(userAgent.isXMLParserValidating());

			SVGDocument document;
			if (svgText != null)
			{
				document = documentFactory.createSVGDocument(null,
						new StringReader(svgText));
			}
			else
			{
				document = documentFactory.createSVGDocument(null,
						new ByteArrayInputStream(svgData));
			}

			BridgeContext ctx = new BridgeContext(userAgent);
			ctx.setDynamic(true);
			GVTBuilder builder = new GVTBuilder();
			rootNode = builder.build(ctx, document);
			documentSize = ctx.getDocumentSize();
		}
		catch (IOException e)
		{
			throw new JRRuntimeException(e);
		}
	}
	

	/**
	 * @deprecated Replaced by {@link #ensureSvg(JasperReportsContext)}.
	 */
	protected synchronized void ensureSvg() throws JRException
	{
		ensureSvg(DefaultJasperReportsContext.getInstance());
	}
	
	/**
	 * @deprecated To be removed.
	 */
	public List<JRPrintImageAreaHyperlink> renderWithHyperlinks(Graphics2D grx, Rectangle2D rectangle) throws JRException
	{
		render(grx, rectangle);

		return areaHyperlinks;
	}

	/**
	 *
	 */
	public List<JRPrintImageAreaHyperlink> getImageAreaHyperlinks(Rectangle2D renderingArea) throws JRException
	{
		return areaHyperlinks;
	}

	public boolean hasImageAreaHyperlinks()
	{
		return areaHyperlinks != null && !areaHyperlinks.isEmpty();
	}

	protected Graphics2D createGraphics(BufferedImage bi)
	{
		Graphics2D graphics = GraphicsUtil.createGraphics(bi);
		if (antiAlias)
		{
			graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			//FIXME use JPG instead of PNG for smaller size?
		}
		return graphics;
	}

	protected void setSvgDataLocation(String svgDataLocation)
	{
		this.svgDataLocation = svgDataLocation;
	}

	/**
	 * Creates a SVG renderer from binary data.
	 *
	 * @param svgData the SVG (binary) data
	 * @return a SVG renderer
	 */
	public static BatikRenderer getInstance(byte[] svgData)
	{
		return new BatikRenderer(svgData, null);
	}

	/**
	 * Creates a SVG renderer from a data stream.
	 *
	 * <p>
	 * Note: the data stream is exhausted, but not closed.
	 * </p>
	 *
	 * @param svgDataStream the SVG binary data stream
	 * @return a SVG renderer
	 * @throws JRException
	 */
	public static BatikRenderer getInstance(InputStream svgDataStream) throws JRException
	{
		byte[] data = JRLoader.loadBytes(svgDataStream);
		return new BatikRenderer(data, null);
	}

	/**
	 * Creates a SVG renderer from a file.
	 *
	 * @param svgFile the SVG file to read
	 * @return a SVG renderer
	 * @throws JRException
	 */
	public static BatikRenderer getInstance(File svgFile) throws JRException
	{
		byte[] data = JRLoader.loadBytes(svgFile);
		return new BatikRenderer(data, null);
	}

	/**
	 * Creates a SVG renderer from a {@link URL}.
	 *
	 * @param svgURL the SVG URL
	 * @return a SVG renderer
	 * @throws JRException
	 */
	public static BatikRenderer getInstance(URL svgURL) throws JRException
	{
		byte[] data = JRLoader.loadBytes(svgURL);
		return new BatikRenderer(data, null);
	}

	/**
	 * Creates a SVG renderer from SVG text.
	 *
	 * @param svgText the SVG text
	 * @return a SVG renderer
	 * @throws JRException
	 */
	public static BatikRenderer getInstanceFromText(String svgText) throws JRException
	{
		return new BatikRenderer(svgText, null);
	}

	/**
	 * Creates a SVG renderer by loading data from a generic location.
	 *
	 * @param location the location
	 * @return a SVG renderer
	 * @throws JRException
	 * @deprecated Replaced by {@link #getInstanceFromLocation(JasperReportsContext, String)}.
	 */
	public static BatikRenderer getInstanceFromLocation(String location) throws JRException
	{
		return getInstanceFromLocation(DefaultJasperReportsContext.getInstance(), location);
	}

	/**
	 * Creates a SVG renderer by loading data from a generic location.
	 *
	 * @param location the location
	 * @return a SVG renderer
	 * @throws JRException
	 * @see RepositoryUtil#getBytesFromLocation(String)
	 */
	public static BatikRenderer getInstanceFromLocation(JasperReportsContext jasperReportsContext, String location) throws JRException
	{
		byte[] data = RepositoryUtil.getInstance(jasperReportsContext).getBytesFromLocation(location);
		return new BatikRenderer(data, null);
	}

	/**
	 * Creates a lazily loaded SVG renderer for a location.
	 *
	 * <p>
	 * The returned renderer loads the SVG data lazily, i.e. only when the data
	 * is actually required (which is at the first
	 * {@link #render(Graphics2D, Rectangle2D)}}.
	 * </p>
	 *
	 * @param location the SVG location
	 * @throws JRException
	 */
	public static BatikRenderer getLocationInstance(String location) throws JRException
	{
		BatikRenderer renderer = new BatikRenderer(null);
		renderer.setSvgDataLocation(location);
		return renderer;
	}

	@Override
	protected int getImageDataDPI(JasperReportsContext jasperReportsContext)
	{
		int dpi = super.getImageDataDPI(jasperReportsContext);
		if (minDPI > 0 && dpi < minDPI)
		{
			dpi = minDPI;
		}
		return dpi;
	}

	public int getMinDPI()
	{
		return minDPI;
	}

	public void setMinDPI(int minDPI)
	{
		this.minDPI = minDPI;
	}

	public boolean isAntiAlias()
	{
		return antiAlias;
	}

	public void setAntiAlias(boolean antiAlias)
	{
		this.antiAlias = antiAlias;
	}
}
