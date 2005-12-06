package net.sf.jasperreports.engine.data;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Vector;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

import org.apache.commons.beanutils.ConvertUtils;


/**
 * This datasource implementation reads a CSV stream. Datasource rows are separated by a record delimiter string and
 * fields inside a row are separated by a field delimiter character. Fields containing delimiter characters can be
 * placed inside quotes. If fields contain quotes themselves, these are duplicated (example: <i>"John ""Doe"""<i> will be
 * displayed as <i>John "Doe"</i>).
 * @author Ionut Nedelcu (ionutned@users.sourceforge.net)
 * @version $Id
 */
public class JRCsvDataSource implements JRDataSource
{
	private Vector fields;
	private char fieldDelimiter = ',';
	private String recordDelimiter;

	private Reader reader;
	private char buffer[] = new char[1024];
	private int position;
	private int bufSize;


	/**
	 * @param stream an input stream containing CSV data
	 * @param fieldDelimiter a character representing the field delimiter (usually comma)
	 * @param recordDelimiter a string representing the record delimiter (usually \r\n or \n, depending on the operating
	 * system)
	 */
	public JRCsvDataSource(InputStream stream, char fieldDelimiter, String recordDelimiter)
	{
		this(new InputStreamReader(stream), fieldDelimiter, recordDelimiter);
	}

	/**
	 * Builds a datasource instance.
	 * @param file a file containing CSV data
	 * @param fieldDelimiter a character representing the field delimiter (usually comma)
	 * @param recordDelimiter a string representing the record delimiter (usually \r\n or \n, depending on the operating
	 * system)
	 */
	public JRCsvDataSource(File file, char fieldDelimiter, String recordDelimiter) throws FileNotFoundException
	{
		this(new FileReader(file), fieldDelimiter, recordDelimiter);
	}


	/**
	 * Builds a datasource instance.
	 * @param reader a <tt>Reader</tt> instance, for reading the stream
	 * @param fieldDelimiter a character representing the field delimiter (usually comma)
	 * @param recordDelimiter a string representing the record delimiter (usually \r\n or \n, depending on the operating
	 * system)
	 */
	public JRCsvDataSource(Reader reader, char fieldDelimiter, String recordDelimiter)
	{
		this.fieldDelimiter = fieldDelimiter;
		this.recordDelimiter = recordDelimiter;
		this.reader = reader;
	}


	/**
	 *
	 */
	public boolean next() throws JRException
	{
		try {
			return parseRow();
		} catch (IOException e) {
			throw new JRException(e.getMessage());
		}
	}


	/**
	 *
	 */
	public Object getFieldValue(JRField jrField) throws JRException
	{
		String fieldName = jrField.getName();
		if (fieldName.startsWith("COLUMN_")) {
			int columnIndex = Integer.parseInt(fieldName.substring(7));
			if (fields.size() > columnIndex) {
				String fieldValue = (String) fields.get(columnIndex);
				if (Number.class.isAssignableFrom(jrField.getValueClass()))
					fieldValue = fieldValue.trim();
				return ConvertUtils.convert(fieldValue, jrField.getValueClass());
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
				if (!insideQuotes) {
					if (!hadQuotes) {  // if hadQuotes is true, the field contains a bad string, like "fo"o"
						insideQuotes = true;
						hadQuotes = true;
					}
					else
						misplacedQuote = true;
				}
				else {
					if (pos+1 < row.length() && row.charAt(pos+1) == '"')
						pos++;
					else
						insideQuotes = false;
				}
			}

			if (c == fieldDelimiter && !insideQuotes) {
				String field = row.substring(startFieldPos, pos);
				if (misplacedQuote) {
					misplacedQuote = false;
					field = "";
				}
				else if (hadQuotes) {
					field = field.trim();
					if (field.startsWith("\"") && field.endsWith("\"")) {
						field = field.substring(1, field.length() - 1);
						field = field.replaceAll("\"\"", "\"");
					}
					else
						field = "";
					hadQuotes = false;
				}

				fields.add(field);
				startFieldPos = pos + 1;
			}

			pos++;
			if ((pos == row.length()) && insideQuotes) {
				row = row + recordDelimiter + getRow();
			}
		}

		String field = row.substring(startFieldPos, pos);
		if (field == null || field.length() == 0)
			return true;

		if (misplacedQuote)
			field = "";
		else if (hadQuotes) {
			field = field.trim();
			if (field.startsWith("\"") && field.endsWith("\"")) {
				field = field.substring(1, field.length() - 1);
				field = field.replaceAll("\"\"", "\"");
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

				if (c == recordDelimiter.charAt(0)) {
					int i;
					char[] temp = new char[recordDelimiter.length()];
					temp[0] = c;
					boolean isDelimiter = true;
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
}


