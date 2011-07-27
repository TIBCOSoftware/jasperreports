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
package net.sf.jasperreports.components.sort;


/**
 * @author Narcis Marcu (narcism@users.sourceforge.net)
 * @version $Id: SortElement.java 75 2011-06-23 15:04:59Z narcism $
 */
public interface SortElement {
	
	String PARAMETER_SORT_COLUMN_NAME = "sortColumnName";
	String PARAMETER_SORT_COLUMN_TYPE = "sortColumnType";
	String PARAMETER_SORT_HANDLER_COLOR = "sortHandlerColor";
	String PARAMETER_SORT_HANDLER_FONT_SIZE = "sortHandlerFontSize";
	String PARAMETER_SORT_HANDLER_VERTICAL_ALIGN = "sortHandlerVerticalAlign";
	String PARAMETER_SORT_HANDLER_HORIZONTAL_ALIGN = "sortHandlerHorizontalAlign";
	
	/**
	 * 
	 */
	String PARAMETER_DYNAMIC_TABLE_BINDING = "controlParameter";
	
	/**
	 * 
	 */
	public static final String REQUEST_PARAMETER_FILTER_FIELD = "jr.fltrFld";
	public static final String REQUEST_PARAMETER_FILTER_VALUE = "jr.fltrVl";
	
	public static final String REQUEST_PARAMETER_SORT_DATA = "jr.sort";
	public static final String REQUEST_PARAMETER_DATASET_RUN = "jr.dsr";//FIXMEJIVE this parameter cannot uniquely identify a dataset run that repeats; check it
	//public static final String PARAMETER_SORT_FIELDS = "_sortFields";

}