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
package net.sf.jasperreports.data.json;

import java.util.Locale;
import java.util.TimeZone;

import net.sf.jasperreports.data.AbstractDataAdapter;
import net.sf.jasperreports.data.DataFile;

/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public class JsonDataAdapterImpl extends AbstractDataAdapter implements
		JsonDataAdapter {
	private String fileName;
	private DataFile dataFile;
	private String datePattern = null;
	private String numberPattern = null;
	private String selectExpression;
	private Locale locale;
	private TimeZone timeZone;
	private boolean useConnection = false;

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		if (fileName != null) {
			this.dataFile = null;
		}
		this.fileName = fileName;
	}

	public String getDatePattern() {
		return datePattern;
	}

	public void setDatePattern(String datePattern) {
		this.datePattern = datePattern;
	}

	public String getNumberPattern() {
		return numberPattern;
	}

	public void setNumberPattern(String numberPattern) {
		this.numberPattern = numberPattern;
	}

	public String getSelectExpression() {
		return selectExpression;
	}

	public void setSelectExpression(String selectExpression) {
		this.selectExpression = selectExpression;
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

	public boolean isUseConnection() {
		return useConnection;
	}

	public void setUseConnection(boolean useConnection) {
		this.useConnection = useConnection;
	}
	
	public DataFile getDataFile()
	{
		return dataFile;
	}

	public void setDataFile(DataFile dataFile)
	{
		if (dataFile != null)
		{
			this.fileName = null;
		}
		this.dataFile = dataFile;
	}
}
