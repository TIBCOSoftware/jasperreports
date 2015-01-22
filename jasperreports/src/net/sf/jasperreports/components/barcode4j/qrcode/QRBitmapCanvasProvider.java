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

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;

import org.krysalis.barcode4j.output.bitmap.BitmapCanvasProvider;
import org.krysalis.barcode4j.output.bitmap.BitmapEncoderRegistry;

/**
 * This class is used to generate a bitmap image for the QRCode component. 
 * 
 * @author sanda zaharia (shertage@users.sourceforge.net)
 */
public class QRBitmapCanvasProvider extends BitmapCanvasProvider {

	private BufferedImage qrImage;
	private OutputStream qrOut;
	private String qrMime;
	private int qrResolution;
	
	public QRBitmapCanvasProvider(int resolution, 
			int imageType,
			boolean antiAlias, 
			int orientation) {
		this(null, null, resolution, imageType, antiAlias, orientation);
	}

	public QRBitmapCanvasProvider(OutputStream out, 
			String mime,
			int resolution, 
			int imageType, 
			boolean antiAlias, 
			int orientation) {
		super(out, mime, resolution, imageType, antiAlias, orientation);
		this.qrOut = out;
		this.qrMime = mime;
		this.qrResolution = resolution;
	}

	@Override
	public BufferedImage getBufferedImage() {
		return qrImage;
	}

	/**
	 * @param bImage the bImage to set
	 */
	public void setBufferedImage(BufferedImage bImage) {
		this.qrImage = bImage;
	}
	
	@Override
	public void finish() throws IOException {
		if(qrImage != null) {
			qrImage.flush();
		}
        if (qrOut != null) {
            BitmapEncoderRegistry.getInstance(qrMime).encode(qrImage, qrOut, qrMime, qrResolution);
        }
    }

}
