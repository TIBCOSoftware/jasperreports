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
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

import net.sf.jasperreports.components.barcode4j.type.ErrorCorrectionLevelEnum;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.util.JRColorUtil;

import org.krysalis.barcode4j.impl.AbstractBarcodeBean;
import org.krysalis.barcode4j.output.CanvasProvider;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.client.j2se.MatrixToImageConfig;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

/**
 * This class is used to generate QRCode component barcode logic. 
 * 
 * @author sanda zaharia (shertage@users.sourceforge.net)
 */
public class QRCodeBean extends AbstractBarcodeBean {
	
	private Color onColor;
	private Color offColor;
	private Integer customWidth;
	private Integer customHeight;
	private String encoding;
	private String errorCorrectionLevel;
  
	public QRCodeBean() {
	}
	
	public QRCodeBean(
			Integer customWidth,
			Integer customHeight,
			Color onColor,
			Color offColor,
			String encoding,
			String errorCorrectionLevel
			) {
		this.customWidth = customWidth;
		this.customHeight = customHeight;
		this.onColor = onColor;
		this.offColor = offColor;
		this.encoding = encoding;
		this.errorCorrectionLevel = errorCorrectionLevel;
	}
  
	public void generateBarcode(CanvasProvider canvas, String msg) {
		if ((msg == null) || (msg.length() == 0)) {
			throw new NullPointerException("Parameter msg must not be empty");
		}
		if(!(canvas instanceof QRBitmapCanvasProvider || canvas instanceof QRSvgCanvasProvider)) {
			throw new JRRuntimeException("Unsupported type of CanvasProvider");
		}
		
		QRCodeWriter writer = new QRCodeWriter();
		Map<EncodeHintType,Object> hints = new HashMap<EncodeHintType,Object>();
		hints.put(EncodeHintType.CHARACTER_SET, encoding==null? QRCodeComponent.PROPERTY_DEFAULT_ENCODING : encoding);
		String level = errorCorrectionLevel == null ? QRCodeComponent.PROPERTY_DEFAULT_ERROR_CORRECTION_LEVEL : errorCorrectionLevel;
		hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevelEnum.getByName(level).getValue());
		hints.put(EncodeHintType.MARGIN, (int)quietZone);
		
		try {
			BitMatrix matrix = writer.encode(msg, BarcodeFormat.QR_CODE, customWidth, customHeight, hints);
			if(canvas instanceof QRBitmapCanvasProvider) {
				int on = JRColorUtil.getOpaqueArgb(onColor, Color.BLACK);
				int off = JRColorUtil.getOpaqueArgb(offColor, Color.WHITE);
				MatrixToImageConfig config = new MatrixToImageConfig(on, off);
				BufferedImage image = MatrixToImageWriter.toBufferedImage(matrix, config);
				((QRBitmapCanvasProvider)canvas).setBufferedImage(image);
			} else if(canvas instanceof QRSvgCanvasProvider) {
				((QRSvgCanvasProvider)canvas).generateSvg(matrix, 
						onColor == null ? Color.BLACK : onColor, 
						offColor == null ? Color.WHITE : offColor);
			}
		} catch (Exception e) {
			throw new JRRuntimeException(e);
		}
	}
	
	@Override
	public double getBarWidth(int width) {
		return 0d;
	}
  
	public Color getOnColor() {
		return onColor;
	}

	public void setOnColor(Color onColor) {
		this.onColor = onColor;
	}

	public Color getOffColor() {
		return offColor;
	}

	public void setOffColor(Color offColor) {
		this.offColor = offColor;
	}

	public String getEncoding() {
		return encoding;
	}

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	public String getErrorCorrectionLevel() {
		return errorCorrectionLevel;
	}

	public void setErrorCorrectionLevel(String errorCorrectionLevel) {
		this.errorCorrectionLevel = errorCorrectionLevel;
	}

	public Integer getCustomWidth() {
		return customWidth;
	}

	public void setCustomWidth(Integer customWidth) {
		this.customWidth = customWidth;
	}

	public Integer getCustomHeight() {
		return customHeight;
	}

	public void setCustomHeight(Integer customHeight) {
		this.customHeight = customHeight;
	}
	
}

