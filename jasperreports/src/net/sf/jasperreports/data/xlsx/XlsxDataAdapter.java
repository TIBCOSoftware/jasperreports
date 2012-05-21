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
package net.sf.jasperreports.data.xlsx;

import java.util.List;

import net.sf.jasperreports.data.DataAdapter;

/**
 * @author sanda zaharia(shertage@users.sourceforge.net)
 * @version $Id$
 */
public interface XlsxDataAdapter extends DataAdapter 
{
	public String getDatePattern();

	public String getNumberPattern();

	public String getFileName();

	public void setFileName(String filename);
	
	public boolean isUseFirstRowAsHeader();
	
	public List<String> getColumnNames();

	public List<Integer> getColumnIndexes();
	
	public void setColumnNames(List<String> columnNames);

	public void setColumnIndexes(List<Integer> columnIndexes);

	public void setUseFirstRowAsHeader(boolean useFirstRowAsHeader);

	public void setDatePattern(String datePattern);

	public void setNumberPattern(String numberPattern);
	
	public boolean isQueryExecuterMode();

	public void setQueryExecuterMode(boolean queryExecuterMode);
}
