package net.sf.jasperreports.renderers;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.io.StringReader;
import java.util.List;

import net.sf.jasperreports.engine.JRAbstractSvgRenderer;
import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRImageMapRenderer;
import net.sf.jasperreports.engine.JRRuntimeException;

import org.apache.batik.bridge.BridgeContext;
import org.apache.batik.bridge.DocumentLoader;
import org.apache.batik.bridge.GVTBuilder;
import org.apache.batik.bridge.UserAgent;
import org.apache.batik.bridge.UserAgentAdapter;
import org.apache.batik.bridge.ViewBox;
import org.apache.batik.dom.svg.SAXSVGDocumentFactory;
import org.apache.batik.dom.svg.SVGDocumentFactory;
import org.apache.batik.gvt.GraphicsNode;
import org.w3c.dom.svg.SVGDocument;


/**
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id: JRAbstractChartImageMapRenderer.java 1364 2006-08-31 15:13:20Z lucianc $
 */
public class BatikRenderer extends JRAbstractSvgRenderer implements JRImageMapRenderer
{
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;
	
	private String svgText;
	private List areaHyperlinks;
	
	public BatikRenderer(String svgText, List areaHyperlinks)
	{
		this.svgText = svgText;
		this.areaHyperlinks = areaHyperlinks;
	}
	
	public void render(Graphics2D grx, Rectangle2D rectangle) throws JRException
	{
		try
		{
			UserAgent userAgent = new UserAgentAdapter();
			DocumentLoader loader = new DocumentLoader(userAgent);
			SVGDocumentFactory documentFactory = 
				new SAXSVGDocumentFactory(userAgent.getXMLParserClassName(), true);
			documentFactory.setValidating(userAgent.isXMLParserValidating());
    		
    		SVGDocument document = 
	        	documentFactory.createSVGDocument(
	        		"", 
	        		new StringReader(svgText)
	        		);

			BridgeContext ctx = new BridgeContext(userAgent, loader);
			ctx.setDynamic(true);
			GVTBuilder builder = new GVTBuilder();
			GraphicsNode graphicsNode = builder.build(ctx, document);
			
			AffineTransform transform = ViewBox.getViewTransform(null, document.getRootElement(), 
					(float) rectangle.getWidth(), (float) rectangle.getHeight(), ctx);
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
}
