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
package net.sf.jasperreports.engine;

import net.sf.jasperreports.engine.type.OrientationEnum;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id: JasperPrint.java 6841 2014-01-28 14:51:00Z teodord $
 */
public interface PrintPageFormat
{

	/**
	 * @return Returns the page width
	 */
	public Integer getPageWidth();
		
	/**
	 * @return Returns the page height.
	 */
	public Integer getPageHeight();
		
	/**
	 * @return Returns the top page margin
	 */
	public Integer getTopMargin();
		
	/**
	 * @return Returns the left page margin
	 */
	public Integer getLeftMargin();
		
	/**
	 * @return Returns the bottom page margin
	 */
	public Integer getBottomMargin();
		
	/**
	 * @return Returns the right page margin
	 */
	public Integer getRightMargin();
		
	/**
	 * Returns the page orientation.
	 */
	public OrientationEnum getOrientation();

}
