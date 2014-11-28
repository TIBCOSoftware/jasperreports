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
package net.sf.jasperreports.charts;



/**
 * Type of plot 
 * used to render Bar 3D and the Stacked Bar 3D charts. 
 * <br/>
 * Like the {@link net.sf.jasperreports.charts.JRBarPlot JRBarPlot}, the JRBar3DPlot 
 * allows customization of the labels for both of its axes and the display of the item labels
 * 
 * @author Flavius Sana (flavius_sana@users.sourceforge.net)
 */
public interface JRBar3DPlot extends JRCategoryPlot
{

	/**
	 * @return the x offset
	 */
	public Double getXOffsetDouble();
	
	/**
	 * @return a {@link net.sf.jasperreports.charts.JRItemLabel JRItemLabel} object 
	 * representing the item label
	 */
	public JRItemLabel getItemLabel();
	
	/**
	 * Sets the x offset
	 * @param xOffset the x offset
	 */
	public void setXOffset(Double xOffset);
	
	/**
	 * @return the y offset
	 */
	public Double getYOffsetDouble();
	
	/**
	 * Sets the y offset
	 * @param yOffset the y offset
	 */
	public void setYOffset(Double yOffset);
	
	/**
	 * @return a flag that specifies whether the labels are to be shown or not
	 */
	public Boolean getShowLabels();

	/**
	 * Sets a flag that specifies whether the labels are to be shown or not
	 * @param isShowLabels the show labels flag
	 */
	public void setShowLabels(Boolean isShowLabels);

}
