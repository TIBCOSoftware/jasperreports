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
package net.sf.jasperreports.engine;

import java.awt.Graphics2D;
import java.awt.geom.Dimension2D;
import java.awt.geom.Rectangle2D;

import net.sf.jasperreports.engine.type.ImageTypeEnum;
import net.sf.jasperreports.engine.type.RenderableTypeEnum;



/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public abstract class JRAbstractRenderer implements Renderable
{

	/**
	 *
	 */
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

	/**
	 *
	 */
	private String id;
	
	
	/**
	 *
	 */
	public JRAbstractRenderer()
	{
		id = System.currentTimeMillis() + "-" + Math.random();
	}
	

	/**
	 *
	 */
	public String getId()
	{
		return id;
	}

	
	/**
	 *
	 */
	@SuppressWarnings("deprecation")
	public RenderableTypeEnum getTypeValue()
	{
		return RenderableTypeEnum.getByValue(getType());
	}


	/**
	 *
	 */
	@SuppressWarnings("deprecation")
	public ImageTypeEnum getImageTypeValue()
	{
		return ImageTypeEnum.getByValue(getImageType());
	}


	/**
	 *
	 */
	@SuppressWarnings("deprecation")
	public Dimension2D getDimension(JasperReportsContext jasperReportsContext) throws JRException
	{
		return getDimension();
	}


	/**
	 *
	 */
	@SuppressWarnings("deprecation")
	public byte[] getImageData(JasperReportsContext jasperReportsContext) throws JRException
	{
		return getImageData();
	}


	/**
	 *
	 */
	@SuppressWarnings("deprecation")
	public void render(JasperReportsContext jasperReportsContext, Graphics2D grx, Rectangle2D rectangle) throws JRException
	{
		render(grx, rectangle);
	}
}
