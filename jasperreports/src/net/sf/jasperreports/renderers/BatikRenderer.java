package net.sf.jasperreports.renderers;

import java.awt.Graphics2D;
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
import java.net.URLStreamHandlerFactory;
import java.util.List;

import net.sf.jasperreports.engine.JRAbstractSvgRenderer;
import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRImageMapRenderer;
import net.sf.jasperreports.engine.JRPrintImageAreaHyperlink;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.util.FileResolver;
import net.sf.jasperreports.engine.util.JRLoader;

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
 * @version $Id: JRAbstractChartImageMapRenderer.java 1364 2006-08-31 15:13:20Z lucianc $
 */
public class BatikRenderer extends JRAbstractSvgRenderer implements JRImageMapRenderer
{
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

	private String svgText;
	private byte[] svgData;
	private String svgDataLocation;
	private List areaHyperlinks;

	protected BatikRenderer(List areaHyperlinks)
	{
		this.areaHyperlinks = areaHyperlinks;
	}

	/**
	 * Creates a SVG renderer.
	 *
	 * @param svgText the SVG text
	 * @param areaHyperlinks a list of {@link JRPrintImageAreaHyperlink area hyperlinks}
	 */
	public BatikRenderer(String svgText, List areaHyperlinks)
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
	public BatikRenderer(byte[] svgData, List areaHyperlinks)
	{
		this.svgData = svgData;
		this.areaHyperlinks = areaHyperlinks;
	}

	public void render(Graphics2D grx, Rectangle2D rectangle) throws JRException
	{
		ensureData();

		try
		{
			UserAgent userAgent = new UserAgentAdapter();
			SVGDocumentFactory documentFactory =
				new SAXSVGDocumentFactory(userAgent.getXMLParserClassName(), true);
			documentFactory.setValidating(userAgent.isXMLParserValidating());

			SVGDocument document;
			if (svgText != null)
			{
				document = documentFactory.createSVGDocument("",
						new StringReader(svgText));
			}
			else
			{
				document = documentFactory.createSVGDocument("",
						new ByteArrayInputStream(svgData));
			}

			BridgeContext ctx = new BridgeContext(userAgent);
			ctx.setDynamic(true);
			GVTBuilder builder = new GVTBuilder();
			GraphicsNode graphicsNode = builder.build(ctx, document);

			Dimension2D docSize = ctx.getDocumentSize();
			AffineTransform transform = ViewBox.getPreserveAspectRatioTransform(
					new float[]{0, 0, (float) docSize.getWidth(), (float) docSize.getHeight()},
					SVGPreserveAspectRatio.SVG_PRESERVEASPECTRATIO_NONE, true,
					(float) rectangle.getWidth(), (float) rectangle.getHeight());
			Graphics2D graphics = (Graphics2D) grx.create();
			graphics.translate(rectangle.getX(), rectangle.getY());
			graphics.transform(transform);

			graphicsNode.paint(graphics);
		}
		catch (IOException e)
		{
			throw new JRRuntimeException(e);
		}
	}

	protected synchronized void ensureData() throws JRException
	{
		if (svgText == null
				&& svgData == null && svgDataLocation != null)
		{
			svgData = JRLoader.loadBytesFromLocation(svgDataLocation);
		}
	}

	public List renderWithHyperlinks(Graphics2D grx, Rectangle2D rectangle) throws JRException
	{
		render(grx, rectangle);

		return areaHyperlinks;
	}

	/**
	 * @deprecated Replaced by {@link #renderWithHyperlinks(Graphics2D, Rectangle2D)}
	 */
	public List getImageAreaHyperlinks(Rectangle2D renderingArea) throws JRException
	{
		return areaHyperlinks;
	}

	public boolean hasImageAreaHyperlinks()
	{
		return areaHyperlinks != null && !areaHyperlinks.isEmpty();
	}

	protected Graphics2D createGraphics(BufferedImage bi)
	{
		return GraphicsUtil.createGraphics(bi);
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
	 * @see JRLoader#loadBytesFromLocation(String)
	 */
	public static BatikRenderer getInstanceFromLocation(String location) throws JRException
	{
		byte[] data = JRLoader.loadBytesFromLocation(location);
		return new BatikRenderer(data, null);
	}

	/**
	 * Creates a SVG renderer by loading data from a generic location.
	 *
	 * @param location the location
	 * @param classLoader the classloader to be used to resolve resources
	 * @param urlHandlerFactory the URL handler factory used to resolve URLs
	 * @param fileResolver the file resolver
	 * @return a SVG renderer
	 * @throws JRException
	 * @see JRLoader#loadBytesFromLocation(String, ClassLoader, URLStreamHandlerFactory, FileResolver)
	 */
	public static BatikRenderer getInstanceFromLocation(String location,
			ClassLoader classLoader,
			URLStreamHandlerFactory urlHandlerFactory,
			FileResolver fileResolver) throws JRException
	{
		byte[] data = JRLoader.loadBytesFromLocation(location);
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
}
