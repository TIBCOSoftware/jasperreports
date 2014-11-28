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

/*
 * Contributors:
 * Adrian Jackson - iapetus@users.sourceforge.net
 * David Taylor - exodussystems@users.sourceforge.net
 * Lars Kristensen - llk@users.sourceforge.net
 */
package net.sf.jasperreports.engine;

import java.awt.Graphics2D;
import java.awt.geom.Dimension2D;
import java.awt.geom.Rectangle2D;

import net.sf.jasperreports.engine.type.ImageTypeEnum;
import net.sf.jasperreports.engine.type.RenderableTypeEnum;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
@SuppressWarnings("deprecation")
public interface Renderable extends JRRenderable
{
	/**
	 * Specifies the image resolution in dots-per-inch, for the images created by the engine when rasterizing SVGs or when clipping other renderers.
	 */
	public static final String PROPERTY_IMAGE_DPI = JRPropertiesUtil.PROPERTY_PREFIX + "image.dpi";

	/**
	 *
	 */
	public String getId();

	/**
	 *
	 */
	public RenderableTypeEnum getTypeValue();

	/**
	 *
	 */
	public ImageTypeEnum getImageTypeValue();

	/**
	 *
	 */
	public Dimension2D getDimension(JasperReportsContext jasperReportsContext) throws JRException;


	/**
	 *
	 */
	public byte[] getImageData(JasperReportsContext jasperReportsContext) throws JRException;


	/**
	 *
	 */
	public void render(JasperReportsContext jasperReportsContext, Graphics2D grx, Rectangle2D rectangle) throws JRException;
}
