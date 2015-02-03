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
package net.sf.jasperreports.components.barcode4j;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import net.sf.jasperreports.engine.JRComponentElement;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.Renderable;
import net.sf.jasperreports.engine.util.JRColorUtil;
import net.sf.jasperreports.renderers.BatikRenderer;

import org.krysalis.barcode4j.output.BarcodeCanvasSetupException;
import org.krysalis.barcode4j.output.svg.SVGCanvasProvider;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.itextpdf.text.pdf.qrcode.ByteMatrix;
import com.itextpdf.text.pdf.qrcode.EncodeHintType;
import com.itextpdf.text.pdf.qrcode.QRCodeWriter;
import com.itextpdf.text.pdf.qrcode.WriterException;

/**
 * 
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public class QRCodeSVGImageProducer implements QRCodeImageProducer
{

	public Renderable createImage(
		JasperReportsContext jasperReportsContext,
		JRComponentElement componentElement,
		QRCodeBean qrCodeBean, 
		String message
		)
	{
		QRCodeWriter writer = new QRCodeWriter();

		Map<EncodeHintType,Object> hints = new HashMap<EncodeHintType,Object>();
		hints.put(EncodeHintType.CHARACTER_SET, QRCodeComponent.PROPERTY_DEFAULT_ENCODING);
		hints.put(EncodeHintType.ERROR_CORRECTION, qrCodeBean.getErrorCorrectionLevel().getErrorCorrectionLevel());

		ByteMatrix matrix = null;
		SVGCanvasProvider provider = null;
		int margin = qrCodeBean.getMargin() == null ? 0 : qrCodeBean.getMargin();
		try
		{
			matrix = writer.encode(
					message, 
					componentElement.getWidth() - margin, 
					componentElement.getHeight() - margin, 
					hints);
			provider = new SVGCanvasProvider(false, OrientationEnum.UP.getValue());
		}
		catch (WriterException e)
		{
			throw new JRRuntimeException(e);
		}
		catch (BarcodeCanvasSetupException e)
		{
			throw new JRRuntimeException(e);
		}

		Document svgDoc = provider.getDOM();
		Element svg = svgDoc.getDocumentElement();
		int width = matrix.getWidth();
		int height = matrix.getHeight();
		svg.setAttribute("width", String.valueOf(width));
		svg.setAttribute("height",String.valueOf(height));
		svg.setAttribute("viewBox", "0 0 " + width + " " + height);

		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				Element element = svgDoc.createElement("rect");
				element.setAttribute("x", String.valueOf(x));
				element.setAttribute("y", String.valueOf(y));
				element.setAttribute("width", "1");
				element.setAttribute("height", "1");
				if (matrix.get(x,y) > -1) {
					element.setAttribute("fill", "#" + JRColorUtil.getColorHexa(componentElement.getForecolor()));
				} else {
					element.setAttribute("fill", "#" + JRColorUtil.getColorHexa(componentElement.getBackcolor()));
				}
				svgDoc.getFirstChild().appendChild(element);
			}
		}        

		Source source = new DOMSource(svgDoc);
		StringWriter outWriter = new StringWriter();
		Result output = new StreamResult(outWriter);

		try
		{
			Transformer transformer = TransformerFactory.newInstance()
					.newTransformer();
			transformer.transform(source, output);

		}
		catch (TransformerException e)
		{
			throw new JRRuntimeException(e);
		}

		String svgString = outWriter.toString();
		return new BatikRenderer(svgString, null);
	}

}
