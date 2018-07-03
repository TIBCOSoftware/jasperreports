/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2018 TIBCO Software Inc. All rights reserved.
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
import net.sf.jasperreports.renderers.AbstractRenderer;



/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @deprecated Replaced by {@link AbstractRenderer}.
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
	

	@Override
	public String getId()
	{
		return id;
	}

	
	@Override
	public RenderableTypeEnum getTypeValue()
	{
		return RenderableTypeEnum.getByValue(getType());
	}


	@Override
	public ImageTypeEnum getImageTypeValue()
	{
		return ImageTypeEnum.getByValue(getImageType());
	}


	@Override
	public Dimension2D getDimension(JasperReportsContext jasperReportsContext) throws JRException
	{
		return getDimension();
	}


	@Override
	public byte[] getImageData(JasperReportsContext jasperReportsContext) throws JRException
	{
		return getImageData();
	}


	@Override
	public void render(JasperReportsContext jasperReportsContext, Graphics2D grx, Rectangle2D rectangle) throws JRException
	{
		render(grx, rectangle);
	}
}
