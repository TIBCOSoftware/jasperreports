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


/**
 * An abstract representation of a data source field. Each row in a dataset consists of one or more fields with unique
 * names. These names can be used in report expressions.
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public interface JRField extends JRPropertiesHolder, JRCloneable
{


	/**
	 * Gets the field unique name.
	 */
	public String getName();
		
	/**
	 * Gets the field optional description.
	 */
	public String getDescription();
		
	/**
	 * Sets the field description.
	 */
	public void setDescription(String description);
		
	/**
	 * Gets the field value class. Field types cannot be primitives.
	 */
	public Class<?> getValueClass();
		
	/**
	 * Gets the field value class name.
	 */
	public String getValueClassName();
		

}
