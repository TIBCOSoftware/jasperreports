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
package net.sf.jasperreports.data.xls;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import net.sf.jasperreports.data.AbstractDataAdapter;
import net.sf.jasperreports.data.DataFile;
import net.sf.jasperreports.data.RepositoryDataLocation;
import net.sf.jasperreports.data.StandardRepositoryDataLocation;

/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public class XlsDataAdapterImpl extends AbstractDataAdapter implements XlsDataAdapter
{
	private boolean useFirstRowAsHeader = false;
	private Locale locale;
	private TimeZone timeZone;
	private String datePattern = null;
	private String numberPattern = null;
	private DataFile dataFile;
	private List<String> columnNames = new ArrayList<String>();
	private List<Integer> columnIndexes = new ArrayList<Integer>();
	private boolean queryExecuterMode = false;
	private String sheetSelection;
	
	public String getDatePattern() {
		return datePattern;
	}

	public String getNumberPattern() {
		return numberPattern;
	}

	/**
	 * @deprecated replaced by {@link #getDataFile()}
	 */
	@Deprecated
	public String getFileName() {
		if (dataFile instanceof RepositoryDataLocation) {
			return ((RepositoryDataLocation) dataFile).getLocation();
		}
		return null;
	}

	/**
	 * @deprecated replaced by {@link #setDataFile(net.sf.jasperreports.data.DataFile)} and {@link StandardRepositoryDataLocation}
	 */
	@Deprecated
	public void setFileName(String filename) {
		if (filename != null) {
			StandardRepositoryDataLocation repositoryDataFile = new StandardRepositoryDataLocation(filename);
			setDataFile(repositoryDataFile);
		}
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
	
	public String getSheetSelection() {
		return sheetSelection;
	}

	public void setSheetSelection(String sheetSelection) {
		this.sheetSelection = sheetSelection;
	}
	
	public DataFile getDataFile()
	{
		return dataFile;
	}

	public void setDataFile(DataFile dataFile)
	{
		this.dataFile = dataFile;
	}
	
	public Locale getLocale() {
		return locale;
	}

	public void setLocale(Locale locale) {
		this.locale = locale;
	}

	public TimeZone getTimeZone() {
		return timeZone;
	}

	public void setTimeZone(TimeZone timeZone) {
		this.timeZone = timeZone;
	}

}
