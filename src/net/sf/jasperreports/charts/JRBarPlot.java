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
 *(at your option) any later version.
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
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public interface JRBarPlot extends JRChartPlot, JRCategoryAxisFormat, JRValueAxisFormat
{

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
	public JRExpression getDomainAxisMinValueExpression();

	/**
	 * 
	 */
	public JRExpression getDomainAxisMaxValueExpression();

	/**
	 * 
	 */
	public JRExpression getRangeAxisMinValueExpression();

	/**
	 * 
	 */
	public JRExpression getRangeAxisMaxValueExpression();

	/**
	 * @deprecated Replaced by {@link #getShowTickMarks()}  
	 */
	public boolean isShowTickMarks();

	/**
	 * 
	 */
	public Boolean getShowTickMarks();

	/**
	 * @deprecated Replaced by {@link #setShowTickMarks(Boolean)}  
	 */
	public void setShowTickMarks(boolean isShowTickMarks);
		
	/**
	 *
	 */
	public void setShowTickMarks(Boolean isShowTickMarks);
		
	/**
	 * @deprecated Replaced by {@link #getShowTickLabels()}
	 */
	public boolean isShowTickLabels();
	
	/**
	 * 
	 */
	public Boolean getShowTickLabels();
	
	/**
	 * 
	 */
	public JRItemLabel getItemLabel();
	
	/**
	 * @deprecated Replaced by {@link #setShowTickLabels(Boolean)}
	 */
	public void setShowTickLabels(boolean isShowTickLabels);

	/**
	 *
	 */
	public void setShowTickLabels(Boolean isShowTickLabels);

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
	public void setShowLabels( boolean isShowLabels );

	/**
	 *
	 */
	public void setShowLabels( Boolean isShowLabels );

}
