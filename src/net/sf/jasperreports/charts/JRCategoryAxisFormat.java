/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2011 Jaspersoft Corporation. All rights reserved.
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
package net.sf.jasperreports.charts;

import java.awt.Color;

import net.sf.jasperreports.engine.JRFont;

/**
 * @author Teodor Danciu (teodord@users.sourceforge.net) 
 * @version $Id$
 */
public interface JRCategoryAxisFormat
{

	public static final String PROPERTY_CATEGORY_AXIS_TICK_LABEL_ROTATION = "categoryAxisTickLabelRotation";
	
	/**
	 * 
	 */
	public JRFont getCategoryAxisLabelFont();
	
	/**
	 * 
	 */
	public Color getCategoryAxisLabelColor();
		
	/**
	 * 
	 */
	public Color getOwnCategoryAxisLabelColor();
		
	/**
	 * 
	 */
	public JRFont getCategoryAxisTickLabelFont();
	
	/**
	 * 
	 */
	public Color getCategoryAxisTickLabelColor();
	
	/**
	 * 
	 */
	public Color getOwnCategoryAxisTickLabelColor();
	
	/**
	 * 
	 */
	public String getCategoryAxisTickLabelMask();
	
	/**
	 * 
	 */
	public Boolean getCategoryAxisVerticalTickLabels();
	
	/**
	 * 
	 */
	public Color getCategoryAxisLineColor();
	
	/**
	 * 
	 */
	public Color getOwnCategoryAxisLineColor();
	
	/**
	 * Gets the angle in degrees to rotate the data axis labels.  The range is -360 to 360.  A positive value angles
	 * the label so it reads downwards wile a negative value angles the label so it reads upwards.  Only charts that
	 * use a category based axis (such as line or bar charts) support label rotation.
	 */
	public Double getCategoryAxisTickLabelRotation();
	
	/**
	 * Sets the angle in degrees to rotate the data axis labels.  The range is -360 to 360.  A positive value angles
	 * the label so it reads downwards wile a negative value angles the label so it reads upwards.  Only charts that
	 * use a category based axis (such as line or bar charts) support label rotation.
	 */
	public void setCategoryAxisTickLabelRotation(Double labelRotation);
	
}
