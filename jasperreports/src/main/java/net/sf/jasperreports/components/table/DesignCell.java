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
package net.sf.jasperreports.components.table;

import java.awt.Color;

import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRDefaultStyleProvider;
import net.sf.jasperreports.engine.JRLineBox;
import net.sf.jasperreports.engine.JRPropertiesHolder;
import net.sf.jasperreports.engine.JRPropertiesMap;
import net.sf.jasperreports.engine.JRStyle;
import net.sf.jasperreports.engine.base.JRBaseLineBox;
import net.sf.jasperreports.engine.design.DesignStyleContainer;
import net.sf.jasperreports.engine.design.JRDesignElementGroup;

/**
 * 
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 */
public class DesignCell extends JRDesignElementGroup implements Cell, DesignStyleContainer
{

	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

	public static final String PROPERTY_DEFAULT_STYLE_PROVIDER = "defaultStyleProvider";
	public static final String PROPERTY_STYLE = "style";
	public static final String PROPERTY_STYLE_NAME_REFERENCE = "styleNameReference";
	public static final String PROPERTY_ROW_SPAN = "rowSpan";
	public static final String PROPERTY_HEIGHT = "height";
	
	private JRDefaultStyleProvider defaultStyleProvider;
	private JRStyle style;
	private String styleNameReference;
	private JRLineBox box;
	private Integer rowSpan;
	private Integer height;
	
	private JRPropertiesMap propertiesMap;

	public DesignCell()
	{
		this.box = new JRBaseLineBox(this);
	}
	
	@Override
	public Integer getHeight()
	{
		return height;
	}

	@Override
	public Color getDefaultLineColor()
	{
		return Color.BLACK;
	}

	@Override
	public JRLineBox getLineBox()
	{
		return box;
	}

	@Override
	public JRDefaultStyleProvider getDefaultStyleProvider()
	{
		return defaultStyleProvider;
	}

	@Override
	public JRStyle getStyle()
	{
		return style;
	}

	@Override
	public String getStyleNameReference()
	{
		return styleNameReference;
	}

	@Override
	public void setDefaultStyleProvider(
			JRDefaultStyleProvider defaultStyleProvider)
	{
		Object old = this.defaultStyleProvider;
		this.defaultStyleProvider = defaultStyleProvider;
		getEventSupport().firePropertyChange(PROPERTY_DEFAULT_STYLE_PROVIDER, 
				old, this.defaultStyleProvider);
	}

	@Override
	public void setStyle(JRStyle style)
	{
		Object old = this.style;
		this.style = style;
		getEventSupport().firePropertyChange(PROPERTY_STYLE, 
				old, this.style);
	}

	@Override
	public void setStyleNameReference(String styleName)
	{
		Object old = this.styleNameReference;
		this.styleNameReference = styleName;
		getEventSupport().firePropertyChange(PROPERTY_STYLE_NAME_REFERENCE, 
				old, this.styleNameReference);
	}

	public void setHeight(Integer height)
	{
		Object old = this.height;
		this.height = height;
		getEventSupport().firePropertyChange(PROPERTY_HEIGHT, 
				old, this.height);
	}

	@Override
	public Integer getRowSpan()
	{
		return rowSpan;
	}

	public void setRowSpan(Integer rowSpan)
	{
		Object old = this.rowSpan;
		this.rowSpan = rowSpan;
		getEventSupport().firePropertyChange(PROPERTY_ROW_SPAN, 
				old, this.rowSpan);
	}

	@Override
	public boolean hasProperties()
	{
		return propertiesMap != null && propertiesMap.hasProperties();
	}

	@Override
	public JRPropertiesMap getPropertiesMap()
	{
		if (propertiesMap == null)
		{
			propertiesMap = new JRPropertiesMap();
		}
		return propertiesMap;
	}

	@Override
	public JRPropertiesHolder getParentProperties()
	{
		return null;
	}
	
	@Override
	public Object clone() 
	{
		DesignCell clone = (DesignCell) super.clone();
		clone.propertiesMap = JRPropertiesMap.getPropertiesClone(this);
		return clone;
	}

}
