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
package net.sf.jasperreports.engine;

import net.sf.jasperreports.engine.type.SplitTypeEnum;
import net.sf.jasperreports.engine.util.JRProperties;


/**
 * Implementations of this interface represent various bands in the report template. A report can contain the following
 * bands: background, title, summary, page header, page footer, last page footer, column header and column footer.
 * @see JRSection
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public interface JRBand extends JRElementGroup
{
	

	/**
	 * 
	 */
	public static final String PROPERTY_SPLIT_TYPE = JRProperties.PROPERTY_PREFIX + "band.split.type";


	/**
	 *
	 */
	public int getHeight();

	/**
	 * Specifies the band split behavior.
	 */
	public SplitTypeEnum getSplitTypeValue();

	/**
	 *
	 */
	public void setSplitType(SplitTypeEnum splitType);

	/**
	 * Returns the boolean expression that specifies if the band will be displayed.
	 */
	public JRExpression getPrintWhenExpression();

		
}
