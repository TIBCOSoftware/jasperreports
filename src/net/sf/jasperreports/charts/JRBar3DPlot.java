/*
 * ============================================================================
 * GNU Lesser General Public License
 * ============================================================================
 *
 * JasperReports - Free Java report-generating library.
 * Copyright (C) 2001-2006 JasperSoft Corporation http://www.jaspersoft.com
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307, USA.
 * 
 * JasperSoft Corporation
 * 303 Second Street, Suite 450 North
 * San Francisco, CA 94107
 * http://www.jaspersoft.com
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
	 * @deprecated Replaced by {@link #getXOffsetDouble()}
	 */
	public double getXOffset();
	
	/**
	 * 
	 */
	public Double getXOffsetDouble();
	
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
