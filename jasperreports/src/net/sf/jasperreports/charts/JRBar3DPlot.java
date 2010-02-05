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
package net.sf.jasperreports.charts;

import net.sf.jasperreports.engine.JRChartPlot;
import net.sf.jasperreports.engine.JRExpression;


/**
 * @author Flavius Sana (flavius_sana@users.sourceforge.net)
 * @version $Id$ 
 */
public interface JRBar3DPlot extends JRChartPlot, JRCategoryAxisFormat, JRValueAxisFormat {
	
	/**
	 * 
	 */
	public JRExpression getCategoryAxisLabelExpression();

	/**
	 * 
	 */
	public JRExpression getValueAxisLabelExpression();

	/**
	 * 
	 */
	public JRExpression getRangeAxisMinValueExpression();

	/**
	 * 
	 */
	public JRExpression getRangeAxisMaxValueExpression();

	/**
	 * 
	 */
	public JRExpression getDomainAxisMinValueExpression();

	/**
	 * 
	 */
	public JRExpression getDomainAxisMaxValueExpression();

	/**
	 * @deprecated Replaced by {@link #getXOffsetDouble()}
	 */
	public double getXOffset();
	
	/**
	 * 
	 */
	public Double getXOffsetDouble();
	
	/**
	 * 
	 */
	public JRItemLabel getItemLabel();
	
	/**
	 * @deprecated Replaced by {@link #setXOffset(Double)} 
	 */
	public void setXOffset(double xOffset);
	
	/**
	 * 
	 */
	public void setXOffset(Double xOffset);
	
	/**
	 * @deprecated Replaced by {@link #getYOffsetDouble()} 
	 */
	public double getYOffset();
	
	/**
	 * 
	 */
	public Double getYOffsetDouble();
	
	/**
	 * @deprecated Replaced by {@link #setYOffset(Double)}
	 */ 
	public void setYOffset(double yOffset);
	
	/**
	 * 
	 */
	public void setYOffset(Double yOffset);
	
	/**
	 * @deprecated Replaced by {@link #getShowLabels()} 
	 */
	public boolean isShowLabels();

	/**
	 * 
	 */
	public Boolean getShowLabels();

	/**
	 * @deprecated Replaced by {@link #setShowLabels(Boolean)} 
	 */
	public void setShowLabels(boolean isShowLabels);

	/**
	 * 
	 */
	public void setShowLabels(Boolean isShowLabels);

}
