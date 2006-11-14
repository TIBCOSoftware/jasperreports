/*
 * ============================================================================
 * GNU Lesser General Public License
 * ============================================================================
 *
 * JasperReports - Free Java report-generating library.
 * Copyright (C) 2001-2006 JasperSoft Corporation http://www.jaspersoft.com
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307, USA.
 * 
 * JasperSoft Corporation
 * 303 Second Street, Suite 450 North
 * San Francisco, CA 94107
 * http://www.jaspersoft.com
 */
package net.sf.jasperreports.engine.data;

import java.text.DateFormat;
import java.io.InputStream;
import java.io.File;
import java.io.Reader;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.FileReader;

import net.sf.jasperreports.engine.JRDataSourceProvider;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

/**
 * @author Ionut Nedelcu (ionutned@users.sourceforge.net)
 * @version $Id$
 */
public class JRCsvDataSourceProvider implements JRDataSourceProvider
{
	private Reader reader;

	private DateFormat dateFormat;
	private char fieldDelimiter;
	private String recordDelimiter;
	private String[] columnNames;


	/**
	 * @param stream an input stream containing CSV data
	 */
	public JRCsvDataSourceProvider(InputStream stream)
	{
		this(new InputStreamReader(stream));
	}


	/**
	 * Builds a datasource instance.
	 * @param file a file containing CSV data
	 */
	public JRCsvDataSourceProvider(File file) throws FileNotFoundException
	{
		this(new FileReader(file));
	}


	/**
	 * Builds a datasource instance.
	 * @param reader a <tt>Reader</tt> instance, for reading the stream
	 */
	public JRCsvDataSourceProvider(Reader reader)
	{
		this.reader = reader;
	}

	/**
	 *
	 */
	public boolean supportsGetFieldsOperation()
	{
		return false;
	}


	/**
	 *
	 */
	public JRField[] getFields(JasperReport report) throws JRException, UnsupportedOperationException
	{
		return null;
	}


	/**
	 *
	 */
	public JRDataSource create(JasperReport report) throws JRException
	{
		JRCsvDataSource ds;
		if (reader != null)
			 ds = new JRCsvDataSource(reader);
		else {
			throw new JRException("Cannot find a source to read the data from");
		}

		ds.setDateFormat(dateFormat);
		ds.setFieldDelimiter(fieldDelimiter);
		ds.setRecordDelimiter(recordDelimiter);
		ds.setColumnNames(columnNames);

		return ds;
	}


	/**
	 *
	 */
	public void dispose(JRDataSource dataSource) throws JRException
	{
	}

	public String[] getColumnNames()
	{
		return columnNames;
	}

	public void setColumnNames(String[] columnNames)
	{
		this.columnNames = columnNames;
	}

	public DateFormat getDateFormat()
	{
		return dateFormat;
	}

	public void setDateFormat(DateFormat dateFormat)
	{
		this.dateFormat = dateFormat;
	}

	public char getFieldDelimiter()
	{
		return fieldDelimiter;
	}

	public void setFieldDelimiter(char fieldDelimiter)
	{
		this.fieldDelimiter = fieldDelimiter;
	}

	public String getRecordDelimiter()
	{
		return recordDelimiter;
	}

	public void setRecordDelimiter(String recordDelimiter)
	{
		this.recordDelimiter = recordDelimiter;
	}
}
