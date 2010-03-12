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
package net.sf.jasperreports.components.table;

import java.awt.Color;

import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRDefaultStyleProvider;
import net.sf.jasperreports.engine.JRLineBox;
import net.sf.jasperreports.engine.JRStyle;
import net.sf.jasperreports.engine.base.JRBaseLineBox;
import net.sf.jasperreports.engine.design.DesignStyleContainer;
import net.sf.jasperreports.engine.design.JRDesignElementGroup;

/**
 * 
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public class DesignCell extends JRDesignElementGroup implements Cell, DesignStyleContainer
{

	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

	private JRDefaultStyleProvider defaultStyleProvider;
	private JRStyle style;
	private String styleNameReference;
	private JRLineBox box;
	private Integer width;
	private Integer height;

	public DesignCell()
	{
		this.box = new JRBaseLineBox(this);
	}
	
	public Integer getHeight()
	{
		return height;
	}

	public Integer getWidth()
	{
		return width;
	}

	public Color getDefaultLineColor()
	{
		return Color.BLACK;
	}

	public JRLineBox getLineBox()
	{
		return box;
	}

	public JRDefaultStyleProvider getDefaultStyleProvider()
	{
		return defaultStyleProvider;
	}

	public JRStyle getStyle()
	{
		return style;
	}

	public String getStyleNameReference()
	{
		return styleNameReference;
	}

	public void setDefaultStyleProvider(
			JRDefaultStyleProvider defaultStyleProvider)
	{
		this.defaultStyleProvider = defaultStyleProvider;
	}

	public void setStyle(JRStyle style)
	{
		this.style = style;
	}

	public void setStyleNameReference(String styleName)
	{
		this.styleNameReference = styleName;
	}

	public void setWidth(Integer width)
	{
		this.width = width;
	}

	public void setHeight(Integer height)
	{
		this.height = height;
	}

}
