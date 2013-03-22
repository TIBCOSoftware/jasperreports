/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2013 Jaspersoft Corporation. All rights reserved.
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

import net.sf.jasperreports.engine.type.BreakTypeEnum;


/**
 * An abstract representation of a break element.
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public interface JRBreak extends JRElement
{

	/**
	 * Property that determines how page breaks are to be handled in reports that are not paginated.
	 * 
	 * <p>
	 * The property value can be one of
	 * <ul>
	 * <li>{@link #PAGE_BREAK_NO_PAGINATION_IGNORE}</li>
	 * <li>{@link #PAGE_BREAK_NO_PAGINATION_APPLY}</li>
	 * </ul>
	 * </p>
	 * 
	 * <p>
	 * The property can be set globally, at report level or at element level.
	 * The default behaviour is to ignore page breaks in non paginated reports.
	 * </p>
	 */
	String PROPERTY_PAGE_BREAK_NO_PAGINATION = JRPropertiesUtil.PROPERTY_PREFIX + "page.break.no.pagination";
	
	/**
	 * Value for {@link #PROPERTY_PAGE_BREAK_NO_PAGINATION} that results in page breaks 
	 * being ignored in non paginated reports.
	 */
	String PAGE_BREAK_NO_PAGINATION_IGNORE = "ignore";
	
	/**
	 * Value for {@link #PROPERTY_PAGE_BREAK_NO_PAGINATION} that results in page breaks 
	 * being honoured in non paginated reports.
	 */
	String PAGE_BREAK_NO_PAGINATION_APPLY = "apply";

	/**
	 * Gets the break type.
	 * @return a value representing one of the break type constants in {@link BreakTypeEnum}
	 */
	public BreakTypeEnum getTypeValue();
	
	/**
	 * Sets the break type.
	 * @param breakTypeEnum a value representing one of the break type constants in {@link BreakTypeEnum}
	 */
	public void setType(BreakTypeEnum breakTypeEnum);
	

}
