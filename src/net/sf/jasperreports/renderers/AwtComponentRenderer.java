/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2009 Jaspersoft Corporation. All rights reserved.
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

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.Dimension2D;
import java.awt.geom.Rectangle2D;

import net.sf.jasperreports.engine.JRAbstractSvgRenderer;
import net.sf.jasperreports.engine.JRRuntimeException;


/**
 * 
 * @author Narcis Marcu (narcism@users.sourceforge.net)
 * @version $Id$
 */
public class AwtComponentRenderer extends JRAbstractSvgRenderer
{

	private static final long serialVersionUID = 1L;
	
	private Component component;


	public AwtComponentRenderer(Component component) 
	{
		this.component = component;
	}
	
	public Dimension2D getDimension()
	{
		return component.getSize();
	}

	public void render(Graphics2D grx, Rectangle2D rectangle) 
	{
		try
		{
			Dimension size = component.getSize();

			grx.translate(rectangle.getX(), rectangle.getY());
			if (rectangle.getWidth() != size.getWidth() 
					|| rectangle.getHeight() != size.getHeight())
			{
				grx.scale(rectangle.getWidth() / size.getWidth(), 
						rectangle.getHeight() / size.getHeight());
			}
			component.paint(grx);
		}
		catch (Exception e)
		{
			throw new JRRuntimeException(e);
		}
	}
	
}
