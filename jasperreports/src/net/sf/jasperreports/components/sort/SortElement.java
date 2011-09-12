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
package net.sf.jasperreports.components.sort;

import net.sf.jasperreports.engine.JRGenericElementType;
import net.sf.jasperreports.engine.util.JRProperties;
import net.sf.jasperreports.engine.xml.JRXmlConstants;


/**
 * @author Narcis Marcu (narcism@users.sourceforge.net)
 * @version $Id$
 */
public interface SortElement {
	
	public static final String SORT_ELEMENT_NAME = "sort";
	public static final JRGenericElementType SORT_ELEMENT_TYPE = new JRGenericElementType(JRXmlConstants.JASPERREPORTS_NAMESPACE, SORT_ELEMENT_NAME);

	public static final String SORT_ELEMENT_TYPE_FIELD = "Field";
	public static final String SORT_ELEMENT_TYPE_VARIABLE = "Variable";
	public static final String SORT_ORDER_ASC = "Asc";
	public static final String SORT_ORDER_DESC = "Dsc";
	public static final String SORT_COLUMN_TOKEN_SEPARATOR = ":";
	
//	public static final String PARAMETER_SORT_COLUMN = "sortColumn";
	public static final String PARAMETER_SORT_COLUMN_NAME = "sortColumnName";
	public static final String PARAMETER_SORT_COLUMN_TYPE = "sortColumnType";
	public static final String PARAMETER_SORT_HANDLER_COLOR = "sortHandlerColor";
	public static final String PARAMETER_SORT_HANDLER_FONT_SIZE = "sortHandlerFontSize";
	public static final String PARAMETER_SORT_HANDLER_VERTICAL_ALIGN = "sortHandlerVerticalAlign";
	public static final String PARAMETER_SORT_HANDLER_HORIZONTAL_ALIGN = "sortHandlerHorizontalAlign";
	
	/**
	 * Property used to distinguish the generic elements added by user, from the auto-generated ones 
	 */
	public static final String PROPERTY_DYNAMIC_TABLE_BINDING = "controlParameter";
	
	/**
	 * 
	 */
	public static final String REQUEST_PARAMETER_FILTER_FIELD = "jr.fltrFld";
	public static final String REQUEST_PARAMETER_FILTER_VALUE = "jr.fltrVl";
	
	public static final String REQUEST_PARAMETER_SORT_DATA = "jr.sort";
	public static final String REQUEST_PARAMETER_DATASET_RUN = "jr.dsr";//FIXMEJIVE this parameter cannot uniquely identify a dataset run that repeats; check it
	
	public static final String PROPERTY_DATASET_RUN = JRProperties.PROPERTY_PREFIX + "export." + SortElement.REQUEST_PARAMETER_DATASET_RUN;
	public static final String PROPERTY_IS_FILTERABLE = JRProperties.PROPERTY_PREFIX + "export.jr.filterable";//FIXMEJIVE why we need jr token in name?

}