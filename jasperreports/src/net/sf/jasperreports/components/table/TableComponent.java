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
package net.sf.jasperreports.components.table;

import java.util.List;

import net.sf.jasperreports.engine.JRCloneable;
import net.sf.jasperreports.engine.JRDatasetRun;
import net.sf.jasperreports.engine.JRPropertiesUtil;
import net.sf.jasperreports.engine.JRVisitable;
import net.sf.jasperreports.engine.component.Component;


/**
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 */
public interface TableComponent extends Component, JRCloneable, JRVisitable
{
	/**
	 * Property that specifies a default value for the <code>whenNoDataType</code> attribute of table components.
	 */
	public static final String PROPERTY_WHEN_NO_DATA_TYPE = JRPropertiesUtil.PROPERTY_PREFIX + "components.table.when.no.data.type";

	JRDatasetRun getDatasetRun();

	List<BaseColumn> getColumns();
	
	WhenNoDataTypeTableEnum getWhenNoDataType();
	
}
