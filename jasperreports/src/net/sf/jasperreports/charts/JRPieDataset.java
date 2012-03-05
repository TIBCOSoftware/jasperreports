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

import net.sf.jasperreports.engine.JRChartDataset;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRHyperlink;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public interface JRPieDataset extends JRChartDataset
{
	
	/**
	 * 
	 */
	public Float getMinPercentage();

	/**
	 * 
	 */
	public void setMinPercentage(Float minPercentage);

	/**
	 * 
	 */
	public Integer getMaxCount();

	/**
	 * 
	 */
	public void setMaxCount(Integer maxCount);

	/**
	 * 
	 */
	public JRPieSeries[] getSeries();

	/**
	 * 
	 */
	public JRExpression getOtherKeyExpression();
	/**
	 * 
	 */
	public JRExpression getOtherLabelExpression();
	
	/**
	 * Returns the hyperlink specification for the special Other chart section, if present.
	 * 
	 * @return hyperlink specification for the Other chart section
	 */
	public JRHyperlink getOtherSectionHyperlink();

}
