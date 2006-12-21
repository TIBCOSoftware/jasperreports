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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Vector;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.engine.JRRuntimeException;


/**
 * This datasource implementation reads a CSV stream. Datasource rows are separated by a record delimiter string and
 * fields inside a row are separated by a field delimiter character. Fields containing delimiter characters can be
 * placed inside quotes. If fields contain quotes themselves, these are duplicated (example: <i>"John ""Doe"""<i> will be
 * displayed as <i>John "Doe"</i>).
 * <p>
 * Since CSV does not specify column names, the default naming convention is to name report fields COLUMN_x and map each
 * column with the field found at index x in each row (these indices start with 0). To avoid this situation, users can
 * either specify a collection of column names or set a flag to read the column names from the first row of the CSV file.
 *
 * @author Ionut Nedelcu (ionutned@users.sourceforge.net)
 * @version $Id$
 */
public class JRCsvDataSource implements JRDataSource
{
	private DateFormat dateFormat = new SimpleDateFormat();
	private NumberFormat numberFormat = new DecimalFormat();
	private char fieldDelimiter = ',';
	private String recordDelimiter = "\n";
	private HashMap columnNames = new HashMap();
	private boolean useFirstRowAsHeader;

	private Vector fields;
	private Reader reader;
	private char buffer[] = new char[1024];
	private int position;
	private int bufSize;
	private boolean processingStarted;


	/**
	 * Creates a datasource instance from a CSV data input stream, using the default encoding.
	 * @param stream an input stream containing CSV data
	 */
	public JRCsvDataSource(InputStream stream)
	{
		this(new BufferedReader(new InputStreamReader(stream)));
	}


	/**
	 * Creates a datasource instance from a CSV data input stream, using the specified encoding.
	 * @param stream an input stream containing CSV data
	 * @param charsetName the encoding to use
	 */
	public JRCsvDataSource(InputStream stream, String charsetName) throws UnsupportedEncodingException
	{
		this(new BufferedReader(new InputStreamReader(stream, charsetName)));
	}


	/**
	 * Creates a datasource instance from a CSV file, using the default encoding.
	 * @param file a file containing CSV data
	 */
	public JRCsvDataSource(File file) throws FileNotFoundException
	{
		this(new FileInputStream(file));
	}


	/**
	 * Creates a datasource instance from a CSV file, using the specified encoding.
	 * @param file a file containing CSV data
	 * @param charsetName the encoding to use
	 */
	public JRCsvDataSource(File file, String charsetName) throws FileNotFoundException, UnsupportedEncodingException
	{
		this(new FileInputStream(file), charsetName);
	}


	/**
	 * Creates a datasource instance from a CSV data reader.
	 * @param reader a <tt>Reader</tt> instance, for reading the stream
	 */
	public JRCsvDataSource(Reader reader)
	{
		this.reader = reader;
	}


	/**
	 *
	 */
	public boolean next() throws JRException
	{
		try {
			if (!processingStarted) {
				if (useFirstRowAsHeader) {
					parseRow();
					for (int i = 0; i < fields.size(); i++) {
						String name = (String) fields.get(i);
						this.columnNames.put(name, new Integer(i));
					}
				}
				processingStarted = true;
			}

			return parseRow();
		} catch (IOException e) {
			throw new JRException(e);
		}
	}


	/**
	 *
	 */
	public Object getFieldValue(JRField jrField) throws JRException
	{
		String fieldName = jrField.getName();

		Integer columnIndex = (Integer) columnNames.get(fieldName);
		if (columnIndex == null && fieldName.startsWith("COLUMN_")) {
			columnIndex = Integer.valueOf(fieldName.substring(7));
		}
		if (columnIndex == null)
			throw new JRException("Unknown column name : " + fieldName);

		if (fields.size() > columnIndex.intValue()) {
			String fieldValue = (String) fields.get(columnIndex.intValue());
			Class valueClass = jrField.getValueClass();
			
			if (valueClass.equals(String.class)) 
				return fieldValue;

			fieldValue = fieldValue.trim();
			
			if (fieldValue.length() == 0)
				return null;
			
			try {
				if (valueClass.equals(Boolean.class)) {
					return fieldValue.equalsIgnoreCase("true") ? Boolean.TRUE : Boolean.FALSE;
				}
				else if (valueClass.equals(Byte.class)) {
					return new Byte((numberFormat.parse(fieldValue)).byteValue());
				}
				else if (valueClass.equals(Integer.class)) {
					return new Integer((numberFormat.parse(fieldValue)).intValue());
				}
				else if (valueClass.equals(Long.class)) {
					return new Long((numberFormat.parse(fieldValue)).longValue());
				}
				else if (valueClass.equals(Short.class)) {
					return new Short((numberFormat.parse(fieldValue)).shortValue());
				}
				else if (valueClass.equals(Double.class)) {
					return new Double((numberFormat.parse(fieldValue)).doubleValue());
				}
				else if (valueClass.equals(Float.class)) {
					return new Float((numberFormat.parse(fieldValue)).floatValue());
				}
				else if (valueClass.equals(BigDecimal.class)) {
					return new BigDecimal((numberFormat.parse(fieldValue)).toString());
				}
				else if (valueClass.equals(BigInteger.class)) {
					return new BigInteger(String.valueOf(numberFormat.parse(fieldValue).longValue()));
				}
				else if(valueClass.equals(java.lang.Number.class)) {
					return numberFormat.parse(fieldValue);
				}
				else if (valueClass.equals(java.util.Date.class)) {
					return dateFormat.parse(fieldValue);
				}
				else if (valueClass.equals(java.sql.Timestamp.class)) {
					return new java.sql.Timestamp(dateFormat.parse(fieldValue).getTime());
				}
				else if (valueClass.equals(java.sql.Time.class)) {
					return new java.sql.Time(dateFormat.parse(fieldValue).getTime());
				}
				else
					throw new JRException("Field '" + jrField.getName() + "' is of class '" + valueClass.getName() + "' and can not be converted");
			} catch (Exception e) {
				throw new JRException("Unable to get value for field '" + jrField.getName() + "' of class '" + valueClass.getName() + "'", e);
			}
		}

		throw new JRException("Unknown column name : " + fieldName);
	}


	/**
	 * Parses a row of CSV data and extracts the fields it contains
	 */
	private boolean parseRow() throws IOException
	{
		int pos = 0;
		int startFieldPos = 0;
		boolean insideQuotes = false;
		boolean hadQuotes = false;
		boolean misplacedQuote = false;
		char c;
		fields = new Vector();

		String row = getRow();
		if (row == null || row.length() == 0)
			return false;

		while (pos < row.length()) {
			c = row.charAt(pos);

			if (c == '"') {
				// already inside a text containing quotes
				if (!insideQuotes) {
					if (!hadQuotes) {
						insideQuotes = true;
						hadQuotes = true;
					}
					else // the field contains a bad string, like "fo"o", instead of "fo""o"
						misplacedQuote = true;
				}
				// found a quote when already inside quotes, expecting two consecutive quotes, otherwise it means
				// it's a closing quote
				else {
					if (pos+1 < row.length() && row.charAt(pos+1) == '"')
						pos++;
					else
						insideQuotes = false;
				}
			}
			// field delimiter found, copy the field contents to the field array
			if (c == fieldDelimiter && !insideQuotes) {
				String field = row.substring(startFieldPos, pos);
				// if an illegal quote was found, the entire field is considered illegal
				if (misplacedQuote) {
					misplacedQuote = false;
					hadQuotes = false;
					field = "";
				}
				// if the field was between quotes, remove them and turn any escaped quotes inside the text into normal quotes
				else if (hadQuotes) {
					field = field.trim();
					if (field.startsWith("\"") && field.endsWith("\"")) {
						field = field.substring(1, field.length() - 1);
						field = replaceAll(field, "\"\"", "\"");
					}
					else
						field = "";
					hadQuotes = false;
				}

				fields.add(field);
				startFieldPos = pos + 1;
			}

			pos++;
			// if the record delimiter was found inside a quoted field, it is not an actual record delimiter,
			// so another line should be read
			if ((pos == row.length()) && insideQuotes) {
				row = row + recordDelimiter + getRow();
			}
		}

		// end of row was reached, so the final characters form the last field in the record
		String field = row.substring(startFieldPos, pos);
		if (field == null)
			return true;

		if (misplacedQuote)
			field = "";
		else if (hadQuotes) {
			field = field.trim();
			if (field.startsWith("\"") && field.endsWith("\"")) {
				field = field.substring(1, field.length() - 1);
				field = replaceAll(field, "\"\"", "\"");
			}
			else
				field = "";
		}
		fields.add(field);

		return true;
	}


	/**
	 * Reads a row from the stream. A row is a sequence of characters separated by the record delimiter.
	 */
	private String getRow() throws IOException
	{
		StringBuffer row = new StringBuffer();
		char c;

		while (true) {
			try {
				c = getChar();

				// searches for the first character of the record delimiter
				if (c == recordDelimiter.charAt(0)) {
					int i;
					char[] temp = new char[recordDelimiter.length()];
					temp[0] = c;
					boolean isDelimiter = true;
					// checks if the following characters in the stream form the record delimiter
					for (i = 1; i < recordDelimiter.length() && isDelimiter; i++) {
						temp[i] = getChar();
						if (temp[i] != recordDelimiter.charAt(i))
							isDelimiter = false;
					}

					if (isDelimiter)
						return row.toString();

					row.append(temp, 0, i);
				}

				row.append(c);
			} catch (JRException e) {
				return row.toString();
			}

		} // end while
	}


	/**
	 * Reads a character from the stream.
	 * @throws IOException if any I/O error occurs
	 * @throws JRException if end of stream has been reached
	 */
	private char getChar() throws IOException, JRException
	{
		// end of buffer, fill a new buffer
		if (position + 1 > bufSize) {
			bufSize = reader.read(buffer);
			position = 0;
			if (bufSize == -1)
				throw new JRException("No more chars");
		}

		return buffer[position++];
	}


	/**
	 * Gets the date format that will be used to parse date fields
	 */
	public DateFormat getDateFormat()
	{
		return dateFormat;
	}


	/**
	 * Sets the desired date format to be used for parsing date fields
	 */
	public void setDateFormat(DateFormat dateFormat)
	{
		if (processingStarted)
			throw new JRRuntimeException("Cannot modify data source properties after data reading has started");
		this.dateFormat = dateFormat;
	}


	/**
	 * Returns the field delimiter character.
	 */
	public char getFieldDelimiter()
	{
		return fieldDelimiter;
	}


	/**
	 * Sets the field delimiter character. The default is comma. If characters such as comma or quotes are specified,
	 * the results can be unpredictable.
	 * @param fieldDelimiter
	 */
	public void setFieldDelimiter(char fieldDelimiter)
	{
		if (processingStarted)
			throw new JRRuntimeException("Cannot modify data source properties after data reading has started");
		this.fieldDelimiter = fieldDelimiter;
	}


	/**
	 * Returns the record delimiter string.
	 */
	public String getRecordDelimiter()
	{
		return recordDelimiter;
	}


	/**
	 * Sets the record delimiter string. The default is line feed (\n).
	 * @param recordDelimiter
	 */
	public void setRecordDelimiter(String recordDelimiter)
	{
		if (processingStarted)
			throw new JRRuntimeException("Cannot modify data source properties after data reading has started");
		this.recordDelimiter = recordDelimiter;
	}


	/**
	 * Specifies an array of strings representing column names matching field names in the report template
	 */
	public void setColumnNames(String[] columnNames)
	{
		if (processingStarted)
			throw new JRRuntimeException("Cannot modify data source properties after data reading has started");
		for (int i = 0; i < columnNames.length; i++)
			this.columnNames.put(columnNames[i], new Integer(i));
	}


	/**
	 * Specifies whether the first line of the CSV file should be considered a table
	 * header, containing column names matching field names in the report template
	 */
	public void setUseFirstRowAsHeader(boolean useFirstRowAsHeader)
	{
		if (processingStarted)
			throw new JRRuntimeException("Cannot modify data source properties after data reading has started");
		this.useFirstRowAsHeader = useFirstRowAsHeader;
	}


	/**
	 * Closes the reader. Users of this data source should close it after usage.
	 */
	public void close()
	{
		try
		{
			reader.close();
		}
		catch(IOException e)
		{
			//nothing to do
		}
	}


	private String replaceAll(String string, String substring, String replacement)
	{
		StringBuffer result = new StringBuffer();
		int index = string.indexOf(substring);
		int oldIndex = 0;
		while (index >= 0) {
			result.append(string.substring(oldIndex, index));
			result.append(replacement);
			index += substring.length();
			oldIndex = index;

			index = string.indexOf(substring, index);
		}

		if (oldIndex <  string.length())
			result.append(string.substring(oldIndex, string.length()));

		return result.toString();
	}


	public NumberFormat getNumberFormat() {
		return numberFormat;
	}


	public void setNumberFormat(NumberFormat numberFormat) {
		this.numberFormat = numberFormat;
	}
}


