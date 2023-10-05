/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2023 Cloud Software Group, Inc. All rights reserved.
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
package net.sf.jasperreports.engine.query;

import net.sf.jasperreports.annotations.properties.Property;
import net.sf.jasperreports.annotations.properties.PropertyScope;
import net.sf.jasperreports.engine.JRPropertiesUtil;
import net.sf.jasperreports.properties.PropertyConstants;

/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public interface EjbqlConstants
{
	public static final String QUERY_EXECUTER_NAME_EJBQL = "net.sf.jasperreports.query.executer:EJBQL";

	/**
	 * EJBQL query language.
	 */
	public static final String QUERY_LANGUAGE_EJBQL = "ejbql";

	/**
	 * Built-in parameter holding the value of the <code>javax.persistence.EntityManager</code> to be used for creating the query.
	 */
	public static final String PARAMETER_JPA_ENTITY_MANAGER = "JPA_ENTITY_MANAGER";	

	/**
	 * Built-in parameter (optional) holding the value of the query hints map.
	 * Each named/value pair will be set as query hint against the query.
	 */
	public static final String PARAMETER_JPA_QUERY_HINTS_MAP = "JPA_QUERY_HINTS_MAP";	
	
	
	/**
	 * Property specifying the number of result rows to be retrieved at once.
	 * <p/>
	 * Result pagination is implemented by <code>javax.persistence.Query.setFirstResult()</code> and <code>javax.persistence.Query.setMaxResults()</code>.
	 * <p/>
	 * By default, all the rows are retrieved (no result pagination is performed).
	 */
	@Property(
			category = PropertyConstants.CATEGORY_DATA_SOURCE,
			scopes = {PropertyScope.CONTEXT, PropertyScope.DATASET},
			scopeQualifications = {QUERY_EXECUTER_NAME_EJBQL},
			sinceVersion = PropertyConstants.VERSION_1_2_3,
			valueType = Integer.class
			)
	public static final String PROPERTY_JPA_QUERY_PAGE_SIZE = JRPropertiesUtil.PROPERTY_PREFIX + "ejbql.query.page.size";

	/**
	 * Property specifying the prefix for EJBQL query hints.
	 */
	@Property(
			name = "net.sf.jasperreports.ejbql.query.hint.{hint}",
			category = PropertyConstants.CATEGORY_DATA_SOURCE,
			scopes = {PropertyScope.DATASET},
			scopeQualifications = {QUERY_EXECUTER_NAME_EJBQL},
			sinceVersion = PropertyConstants.VERSION_1_2_3
			)
	public static final String PROPERTY_JPA_QUERY_HINT_PREFIX = JRPropertiesUtil.PROPERTY_PREFIX + "ejbql.query.hint.";
}
