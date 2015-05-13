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

import java.awt.Color;
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

import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.google.zxing.qrcode.encoder.ByteMatrix;
import com.google.zxing.qrcode.encoder.Encoder;
import com.google.zxing.qrcode.encoder.QRCode;


/**
 * 
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public class QRCodeSVGImageProducer implements QRCodeImageProducer
{

	public static final int DEFAULT_MARGIN = 4;//same as zxing's QRCodeWriter.QUIET_ZONE_SIZE
	
	public Renderable createImage(
		JasperReportsContext jasperReportsContext,
		JRComponentElement componentElement,
		QRCodeBean qrCodeBean, 
		String message
		)
	{
		Map<EncodeHintType,Object> hints = new HashMap<EncodeHintType,Object>();
		hints.put(EncodeHintType.CHARACTER_SET, QRCodeComponent.PROPERTY_DEFAULT_ENCODING);
		ErrorCorrectionLevel errorCorrectionLevel = qrCodeBean.getErrorCorrectionLevel().getErrorCorrectionLevel();
		hints.put(EncodeHintType.ERROR_CORRECTION, errorCorrectionLevel);

		ByteMatrix matrix = null;
		SVGCanvasProvider provider = null;
		try
		{
			QRCode qrCode = Encoder.encode(message, errorCorrectionLevel, hints);
			matrix = qrCode.getMatrix();
			
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
		
		int margin = qrCodeBean.getMargin() == null ? DEFAULT_MARGIN : qrCodeBean.getMargin();
		int svgWidth = width + 2 * margin;
		int svgHeight = height + 2 * margin;
		
		svg.setAttribute("width", String.valueOf(svgWidth));
		svg.setAttribute("height",String.valueOf(svgHeight));
		svg.setAttribute("viewBox", "0 0 " + svgWidth + " " + svgHeight);

		Color color = componentElement.getForecolor();
		String fill = "#" + JRColorUtil.getColorHexa(color);
		String fillOpacity = color.getAlpha() < 255 ? Float.toString(((float) color.getAlpha()) / 255) : null;
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				if (matrix.get(x,y) == 1) {
					Element element = svgDoc.createElement("rect");
					element.setAttribute("x", String.valueOf(x + margin));
					element.setAttribute("y", String.valueOf(y + margin));
					element.setAttribute("width", "1");
					element.setAttribute("height", "1");
					element.setAttribute("fill", fill);
					if (fillOpacity != null) {
						element.setAttribute("fill-opacity", fillOpacity);
					}
					svgDoc.getFirstChild().appendChild(element);
				}
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
