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
package net.sf.jasperreports.data.xls;

import java.util.ArrayList;
import java.util.List;

import net.sf.jasperreports.data.AbstractDataAdapter;

/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class XlsDataAdapterImpl extends AbstractDataAdapter implements XlsDataAdapter
{
	private boolean useFirstRowAsHeader = false;
	private String datePattern = null;
	private String numberPattern = null;
	private String fileName;
	private List<String> columnNames = new ArrayList<String>();
	private List<Integer> columnIndexes = new ArrayList<Integer>();
	private boolean queryExecuterMode = false;
	
	public String getDatePattern() {
		return datePattern;
	}

	public String getNumberPattern() {
		return numberPattern;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String filename) {
		this.fileName = filename;
	}
	
	public boolean isUseFirstRowAsHeader() {
		return useFirstRowAsHeader;
	}
	
	public List<String> getColumnNames() {
		return columnNames;
	}

	public List<Integer> getColumnIndexes() {
		return columnIndexes;
	}
	
	public void setColumnNames(List<String> columnNames) {
		this.columnNames = columnNames;
	}

	public void setColumnIndexes(List<Integer> columnIndexes) {
		this.columnIndexes = columnIndexes;
	}

	public void setUseFirstRowAsHeader(boolean useFirstRowAsHeader) {
		this.useFirstRowAsHeader = useFirstRowAsHeader;
	}

	public void setDatePattern(String datePattern) {
		this.datePattern = datePattern;
	}

	public void setNumberPattern(String numberPattern) {
		this.numberPattern = numberPattern;
	}
	
	public boolean isQueryExecuterMode() {
		return queryExecuterMode;
	}

	public void setQueryExecuterMode(boolean queryExecuterMode) {
		this.queryExecuterMode = queryExecuterMode;
	}
}
