/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2011 Jaspersoft Corporation. All rights reserved.
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
package net.sf.jasperreports;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.Map;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xhtmlrenderer.extend.NamespaceHandler;
import org.xhtmlrenderer.extend.UserInterface;
import org.xhtmlrenderer.layout.BoxBuilder;
import org.xhtmlrenderer.layout.LayoutContext;
import org.xhtmlrenderer.layout.SharedContext;
import org.xhtmlrenderer.render.BlockBox;
import org.xhtmlrenderer.render.Box;
import org.xhtmlrenderer.render.RenderingContext;
import org.xhtmlrenderer.render.ViewportBox;
import org.xhtmlrenderer.simple.extend.XhtmlNamespaceHandler;
import org.xhtmlrenderer.swing.Java2DFontContext;
import org.xhtmlrenderer.swing.Java2DOutputDevice;
import org.xhtmlrenderer.swing.Java2DRenderer;
import org.xhtmlrenderer.util.Configuration;
import org.xhtmlrenderer.util.ImageUtil;

/**
 * @author Narcis Marcu (narcism@users.sourceforge.net)
 * @version $Id$
 */
public class CustomJava2DRenderer  extends Java2DRenderer{
	private static final int DEFAULT_HEIGHT = 1000;
	private static final int DEFAULT_DOTS_PER_POINT = 1;

	private SharedContext sharedContext;
	private BufferedImage outputImage;
	private Java2DOutputDevice outputDevice;

	private Document doc;
	private Box root;


	/**
	 * Whether we've completed rendering; image will only be rendered once.
	 */
	private int width;
	private Map renderingHints;


	/**
	 * Creates a new instance pointing to the given Document. Does not render until {@link #getImage(int)} is called for
	 * the first time.
	 *
	 * @param doc The document to be rendered.
	 * @param width Target width, in pixels, for the image; required to provide horizontal bounds for the layout.
	 * @param height Target height, in pixels, for the image.
	 */
	public CustomJava2DRenderer(Document doc, int width, int height) {
		super(doc, width, height);
		this.doc = doc;
		this.width = width;
		prepareLayout();
	}

	/**
	 * Sets the rendering hints to apply to the Graphics2D instance used by the renderer; see
	 * {@link java.awt.Graphics2D#setRenderingHints(java.util.Map)}. The Map need not specify values for all
	 * properties; any settings in this map will be applied as override to the default settings, and will
	 * not replace the entire Map for the Graphics2D instance.
	 *
	 * @param hints values to override in default rendering hints for Graphics2D we are rendering to
	 */
	public void setRenderingHints(Map hints) {
		renderingHints = hints;
		super.setRenderingHints(hints);
	}

	private void prepareLayout() {
		outputImage = ImageUtil.createCompatibleBufferedImage(DEFAULT_DOTS_PER_POINT, DEFAULT_DOTS_PER_POINT);
		outputDevice = new Java2DOutputDevice(outputImage);
		sharedContext = getSharedContext();
	
		setDocument(doc, null, new XhtmlNamespaceHandler());
		layout(this.width);
	}
	
	public int getComputedHeight(){
		return root.getHeight();
	}
	
	public void paint(Graphics2D newG) {
		outputDevice = new Java2DOutputDevice(newG);
		if ( renderingHints != null ) {
			newG.getRenderingHints().putAll(renderingHints);
		}
		
		RenderingContext rc = sharedContext.newRenderingContextInstance();
		rc.setFontContext(new Java2DFontContext(newG));
		rc.setOutputDevice(outputDevice);
		sharedContext.getTextRenderer().setup(rc.getFontContext());
		root.getLayer().paint(rc);
	}
	

	private void setDocument(Document doc, String url, NamespaceHandler nsh) {
		sharedContext.reset();
		if (Configuration.isTrue("xr.cache.stylesheets", true)) {
			sharedContext.getCss().flushStyleSheets();
		} else {
			sharedContext.getCss().flushAllStyleSheets();
		}
		sharedContext.setBaseURL(url);
		sharedContext.setNamespaceHandler(nsh);
		sharedContext.getCss().setDocumentContext(
				sharedContext,
				sharedContext.getNamespaceHandler(),
				doc,
				new NullUserInterface()
		);
	}

	private void layout(int width) {
		Rectangle rect = new Rectangle(0, 0, width, DEFAULT_HEIGHT);
		sharedContext.set_TempCanvas(rect);
		LayoutContext c = newLayoutContext();
		BlockBox root = BoxBuilder.createRootBox(c, doc);
		root.setContainingBlock(new ViewportBox(rect));
		root.layout(c);
		this.root = root;
	}

	private LayoutContext newLayoutContext() {
		LayoutContext result = sharedContext.newLayoutContextInstance();
		result.setFontContext(new Java2DFontContext(outputDevice.getGraphics()));

		sharedContext.getTextRenderer().setup(result.getFontContext());

		return result;
	}

	private static final class NullUserInterface implements UserInterface {

		public boolean isHover(Element e) {
			return false;
		}

		public boolean isActive(Element e) {
			return false;
		}

		public boolean isFocus(Element e) {
			return false;
		}
	}
}