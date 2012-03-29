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
package net.sf.jasperreports.components.sort.actions;



/**
 * @author Narcis Marcu (narcism@users.sourceforge.net)
 * @version $Id: FieldDateComparator.java 4707 2011-10-18 08:58:14Z narcism $
 */
public class SortData {
	
	private String uuid;
	private String sortColumnName;
	private String sortColumnType;
	private String sortOrder;
	
	public SortData() {
	}
	
	public SortData(String uuid, String sortColumnName, String sortColumnType, String sortOrder) 
	{
		this.uuid = uuid;
		this.sortColumnName = sortColumnName;
		this.sortColumnType = sortColumnType;
		this.sortOrder = sortOrder;
	}

	public String getUuid() {
		return uuid;
	}
	
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getSortColumnName() {
		return sortColumnName;
	}

	public void setSortColumnName(String sortColumnName) {
		this.sortColumnName = sortColumnName;
	}

	public String getSortColumnType() {
		return sortColumnType;
	}

	public void setSortColumnType(String sortColumnType) {
		this.sortColumnType = sortColumnType;
	}

	public String getSortOrder() {
		return sortOrder;
	}

	public void setSortOrder(String sortOrder) {
		this.sortOrder = sortOrder;
	}

	@Override
	public String toString() {
		return "sortColumnName: " + sortColumnName + "; sortColumnType: " + sortColumnType + "; sortOrder: " + sortOrder;
	}
}
