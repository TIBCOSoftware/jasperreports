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
package net.sf.jasperreports.components.barcode4j;

import java.io.StringWriter;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import net.sf.jasperreports.engine.JRComponentElement;
import net.sf.jasperreports.engine.JRRenderable;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.renderers.BatikRenderer;

import org.krysalis.barcode4j.BarcodeGenerator;
import org.krysalis.barcode4j.output.svg.SVGCanvasProvider;
import org.w3c.dom.Document;

/**
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public class BarcodeSVGImageProducer implements BarcodeImageProducer
{

	public JRRenderable createImage(JRComponentElement componentElement,
			BarcodeGenerator barcode, String message, int orientation)
	{
		try
		{
			SVGCanvasProvider provider = new SVGCanvasProvider(false, orientation);
			barcode.generateBarcode(provider, message);
			Document svgDoc = provider.getDOM();

			Source source = new DOMSource(svgDoc);
			StringWriter outWriter = new StringWriter();
			Result output = new StreamResult(outWriter);
			Transformer transformer = TransformerFactory.newInstance()
					.newTransformer();
			transformer.transform(source, output);

			String svgString = outWriter.toString();
			return new BatikRenderer(svgString, null);
		}
		catch (Exception e)
		{
			throw new JRRuntimeException(e);
		}
	}

}
