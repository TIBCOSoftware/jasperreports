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
import java.io.Serializable;

import net.sf.jasperreports.engine.type.ImageTypeEnum;
import net.sf.jasperreports.engine.type.RenderableTypeEnum;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @deprecated Replaced by {@link Renderable}.
 */
public interface JRRenderable extends Serializable
{
	/**
	 * @deprecated Replaced by {@link Renderable#PROPERTY_IMAGE_DPI}.
	 */
	public static final String PROPERTY_IMAGE_DPI = Renderable.PROPERTY_IMAGE_DPI;


	/**
	 * @deprecated Replaced by {@link RenderableTypeEnum#IMAGE}.
	 */
	public static final byte TYPE_IMAGE = 0;

	/**
	 * @deprecated Replaced by {@link RenderableTypeEnum#SVG}.
	 */
	public static final byte TYPE_SVG = 1;

	/**
	 * A constant used for specifying that the image is of unknown type
	 * @deprecated Replaced by {@link ImageTypeEnum#UNKNOWN}.
	 */
	public static final byte IMAGE_TYPE_UNKNOWN = 0;
	
	/**
	 * A constant used for specifying that the image is of GIF type
	 * @deprecated Replaced by {@link ImageTypeEnum#GIF}.
	 */
	public static final byte IMAGE_TYPE_GIF = 1;
	
	/**
	 * A constant used for specifying that the image is of the JPEG type
	 * @deprecated Replaced by {@link ImageTypeEnum#JPEG}.
	 */
	public static final byte IMAGE_TYPE_JPEG = 2;
	
	/**
	 * A constant used for specifying that the image is of the PNG type
	 * @deprecated Replaced by {@link ImageTypeEnum#PNG}.
	 */
	public static final byte IMAGE_TYPE_PNG = 3;
	
	/**
	 * A constant used for specifying that the image is of the TIFF type
	 * @deprecated Replaced by {@link ImageTypeEnum#TIFF}.
	 */
	public static final byte IMAGE_TYPE_TIFF = 4; 

	/**
	 * @deprecated Replaced by {@link ImageTypeEnum#GIF}.
	 */
	public static final String MIME_TYPE_GIF  = "image/gif";
	/**
	 * @deprecated Replaced by {@link ImageTypeEnum#JPEG}.
	 */
	public static final String MIME_TYPE_JPEG = "image/jpeg";
	/**
	 * @deprecated Replaced by {@link ImageTypeEnum#PNG}.
	 */
	public static final String MIME_TYPE_PNG  = "image/png";
	/**
	 * @deprecated Replaced by {@link ImageTypeEnum#TIFF}.
	 */
	public static final String MIME_TYPE_TIFF = "image/tiff";
	

	/**
	 * @deprecated Replaced by {@link Renderable#getId()}.
	 */
	public String getId();

	/**
	 * @deprecated Replaced by {@link Renderable#getTypeValue()}.
	 */
	public byte getType();

	/**
	 * @deprecated Replaced by {@link Renderable#getImageTypeValue()}.
	 */
	public byte getImageType();

	/**
	 * @deprecated Replaced by {@link Renderable#getDimension(JasperReportsContext)}.
	 */
	public Dimension2D getDimension() throws JRException;


	/**
	 * @deprecated Replaced by {@link Renderable#getImageData(JasperReportsContext)}.
	 */
	public byte[] getImageData() throws JRException;


	/**
	 * @deprecated Replaced by {@link Renderable#render(JasperReportsContext, Graphics2D, Rectangle2D)}.
	 */
	public void render(Graphics2D grx, Rectangle2D rectangle) throws JRException;


}
