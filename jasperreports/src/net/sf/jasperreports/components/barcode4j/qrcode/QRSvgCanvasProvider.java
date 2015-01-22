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
package net.sf.jasperreports.components.barcode4j.qrcode;

import java.awt.Color;

import net.sf.jasperreports.engine.util.JRColorUtil;

import org.krysalis.barcode4j.output.BarcodeCanvasSetupException;
import org.krysalis.barcode4j.output.svg.SVGCanvasProvider;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.google.zxing.common.BitMatrix;

/**
 * This class is used to generate a SVG image for the QRCode component. 
 * 
 * @author sanda zaharia (shertage@users.sourceforge.net)
 */
public class QRSvgCanvasProvider extends SVGCanvasProvider {

	public QRSvgCanvasProvider(int orientation)
			throws BarcodeCanvasSetupException {
		super(orientation);
	}

	public QRSvgCanvasProvider(String namespacePrefix, int orientation)
			throws BarcodeCanvasSetupException {
		super(namespacePrefix, orientation);
	}

	public QRSvgCanvasProvider(boolean useNamespace, int orientation)
			throws BarcodeCanvasSetupException {
		super(useNamespace, orientation);
	}

	public QRSvgCanvasProvider(DOMImplementation domImpl,
			String namespacePrefix, int orientation)
			throws BarcodeCanvasSetupException {
		super(domImpl, namespacePrefix, orientation);
	}

	public QRSvgCanvasProvider(DOMImplementation domImpl, boolean useNamespace,
			int orientation) throws BarcodeCanvasSetupException {
		super(domImpl, useNamespace, orientation);
	}
	
	public void generateSvg(BitMatrix matrix, Color onColor, Color offColor)
	{
		Document dom = getDOM();
		Element svg = dom.getDocumentElement();
		int width = matrix.getWidth();
		int height = matrix.getHeight();
		svg.setAttribute("width", String.valueOf(width));
		svg.setAttribute("height",String.valueOf(height));
		svg.setAttribute("viewBox", "0 0 " + width + " " + height);

		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				Element element = isNamespaceEnabled() 
						? dom.createElementNS(SVG_NAMESPACE, getQualifiedName("rect"))
						: dom.createElement("rect");
				element.setAttribute("x", String.valueOf(x));
				element.setAttribute("y", String.valueOf(y));
				element.setAttribute("width", "1");
				element.setAttribute("height", "1");
				if (matrix.get(x,y)) {
					element.setAttribute("fill", "#" + JRColorUtil.getColorHexa(onColor));
				} else {
					element.setAttribute("fill", "#" + JRColorUtil.getColorHexa(offColor));
				}
				dom.getFirstChild().appendChild(element);
			}
		}        
	}
}
