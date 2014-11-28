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
import java.awt.geom.Dimension2D;
import java.awt.geom.Rectangle2D;

import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRRenderable;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.Renderable;
import net.sf.jasperreports.engine.type.ImageTypeEnum;
import net.sf.jasperreports.engine.type.RenderableTypeEnum;

/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @deprecated To be removed.
 */
public class WrappingRenderable implements Renderable
{
	/**
	 *
	 */
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

	/**
	 *
	 */
	private JRRenderable renderable;
	
	
	/**
	 *
	 */
	public WrappingRenderable(JRRenderable renderable)
	{
		this.renderable = renderable;
	}


	public String getId() {
		return renderable.getId();
	}


	public byte getType() {
		return renderable.getType();
	}


	public byte getImageType() {
		return renderable.getImageType();
	}


	public Dimension2D getDimension() throws JRException {
		return renderable.getDimension();
	}


	public byte[] getImageData() throws JRException {
		return renderable.getImageData();
	}


	public void render(Graphics2D grx, Rectangle2D rectangle)
			throws JRException {
		renderable.render(grx, rectangle);
	}


	public RenderableTypeEnum getTypeValue() {
		return RenderableTypeEnum.getByValue(renderable.getType());
	}


	public ImageTypeEnum getImageTypeValue() {
		return ImageTypeEnum.getByValue(renderable.getImageType());
	}


	public Dimension2D getDimension(JasperReportsContext jasperReportsContext)
			throws JRException {
		return renderable.getDimension();
	}


	public byte[] getImageData(JasperReportsContext jasperReportsContext)
			throws JRException {
		return renderable.getImageData();
	}


	public void render(JasperReportsContext jasperReportsContext,
			Graphics2D grx, Rectangle2D rectangle) throws JRException {
		renderable.render(grx, rectangle);
	}

}
