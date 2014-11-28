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
package net.sf.jasperreports.engine.util;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.List;

import net.sf.jasperreports.engine.ImageMapRenderable;
import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRImageMapRenderer;
import net.sf.jasperreports.engine.JRPrintImageAreaHyperlink;

/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @deprecated To be removed.
 */
public class WrappingImageMapRenderable extends WrappingRenderable implements ImageMapRenderable
{
	/**
	 *
	 */
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

	/**
	 *
	 */
	private JRImageMapRenderer imageMapRenderer;
	
	
	/**
	 *
	 */
	public WrappingImageMapRenderable(JRImageMapRenderer imageMapRenderer)
	{
		super(imageMapRenderer);
		this.imageMapRenderer = imageMapRenderer;
	}


	public List<JRPrintImageAreaHyperlink> renderWithHyperlinks(Graphics2D grx,
			Rectangle2D rectangle) throws JRException {
		return imageMapRenderer.renderWithHyperlinks(grx, rectangle);
	}


	public List<JRPrintImageAreaHyperlink> getImageAreaHyperlinks(
			Rectangle2D renderingArea) throws JRException {
		return imageMapRenderer.getImageAreaHyperlinks(renderingArea);
	}


	public boolean hasImageAreaHyperlinks() {
		return imageMapRenderer.hasImageAreaHyperlinks();
	}


}
