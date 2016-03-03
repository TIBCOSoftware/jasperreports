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
package net.sf.jasperreports.renderers;

import java.awt.Graphics2D;
import java.awt.geom.Dimension2D;
import java.awt.geom.Rectangle2D;

import net.sf.jasperreports.engine.DimensionRenderable;
import net.sf.jasperreports.engine.Graphics2DRenderable;
import net.sf.jasperreports.engine.ImageRenderable;
import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.SvgRenderable;
import net.sf.jasperreports.engine.type.ImageTypeEnum;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public class ResourceRenderer extends AbstractRenderer
{
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

	private final String resourceLocation;
	private final boolean isLazy;
	
	/**
	 *
	 */
	protected ResourceRenderer(
		String resourceLocation,
		boolean isLazy
		)
	{
		this.resourceLocation = resourceLocation;
		this.isLazy = isLazy;
	}

	/**
	 *
	 */
	public static ResourceRenderer getInstance(String resourceLocation, boolean isLazy)
	{
		if (resourceLocation == null)
		{
			return null;
		}
		
		return new ResourceRenderer(resourceLocation, isLazy);
	}

	/**
	 *
	 */
	public String getResourceLocation()
	{
		return resourceLocation;
	}

	/**
	 *
	 */
	public boolean isLazy()
	{
		return isLazy;
	}

	/**
	 * @deprecated Replaced by {@link ImageRenderable}, {@link SvgRenderable} and {@link Graphics2DRenderable}.
	 */
	@Override
	public net.sf.jasperreports.engine.type.RenderableTypeEnum getTypeValue() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @deprecated Replaced by {@link ImageRenderable#getImageType()}.
	 */
	@Override
	public ImageTypeEnum getImageTypeValue() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @deprecated Replaced by {@link DimensionRenderable#getDimension(JasperReportsContext)}.
	 */
	@Override
	public Dimension2D getDimension(JasperReportsContext jasperReportsContext) throws JRException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @deprecated Replaced by {@link ImageRenderable#getImageData(JasperReportsContext)}.
	 */
	@Override
	public byte[] getImageData(JasperReportsContext jasperReportsContext) throws JRException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @deprecated Replaced by {@link Graphics2DRenderable#render(JasperReportsContext, Graphics2D, Rectangle2D)}.
	 */
	@Override
	public void render(JasperReportsContext jasperReportsContext, Graphics2D grx, Rectangle2D rectangle) throws JRException {
		throw new UnsupportedOperationException();
	}
}
