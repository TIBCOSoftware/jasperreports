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
package net.sf.jasperreports.engine.export;

import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.export.TextExporterConfiguration;
import net.sf.jasperreports.export.TextReportConfiguration;


/**
 * Contains parameters useful for export in plain text format.
 * <p>
 * The text exporter can send data to a string buffer, output stream, character stream or file on disk. The engine looks
 * among the export parameters in order to find the selected output type in this order: OUTPUT_STRING_BUFFER, OUTPUT_WRITER,
 * OUTPUT_STREAM, OUTPUT_FILE, OUTPUT_FILE_NAME.
 *
 * @deprecated Replaced by {@link TextExporterConfiguration}.
 * @author Ionut Nedelcu (ionutned@users.sourceforge.net)
 */
public class JRTextExporterParameter extends JRExporterParameter
{
	/**
	 *
	 */
	public JRTextExporterParameter(String name)
	{
		super(name);
	}


	/**
	 * @deprecated Replaced by {@link TextReportConfiguration#getCharWidth()}.
	 */
	public static final JRTextExporterParameter CHARACTER_WIDTH = new JRTextExporterParameter("Character Width");

	
	/**
	 * @deprecated Replaced by {@link TextReportConfiguration#PROPERTY_CHARACTER_WIDTH}.
	 */
	public static final String PROPERTY_CHARACTER_WIDTH = TextReportConfiguration.PROPERTY_CHARACTER_WIDTH;


	/**
	 * @deprecated Replaced by {@link TextReportConfiguration#getCharHeight()}.
	 */
	public static final JRTextExporterParameter CHARACTER_HEIGHT = new JRTextExporterParameter("Character Height");


	/**
	 * @deprecated Replaced by {@link TextReportConfiguration#PROPERTY_CHARACTER_HEIGHT}.
	 */
	public static final String PROPERTY_CHARACTER_HEIGHT = TextReportConfiguration.PROPERTY_CHARACTER_HEIGHT;


	/**
	 * @deprecated Replaced by {@link TextReportConfiguration#getPageWidthInChars()}.
	 */
	public static final JRTextExporterParameter PAGE_WIDTH = new JRTextExporterParameter("Page Width");


	/**
	 * @deprecated Replaced by {@link TextReportConfiguration#PROPERTY_PAGE_WIDTH}.
	 */
	public static final String PROPERTY_PAGE_WIDTH = TextReportConfiguration.PROPERTY_PAGE_WIDTH;


	/**
	 * @deprecated Replaced by {@link TextReportConfiguration#getPageHeightInChars()}.
	 */
	public static final JRTextExporterParameter PAGE_HEIGHT = new JRTextExporterParameter("Page Height");


	/**
	 * @deprecated Replaced by {@link TextReportConfiguration#PROPERTY_PAGE_HEIGHT}.
	 */
	public static final String PROPERTY_PAGE_HEIGHT = TextReportConfiguration.PROPERTY_PAGE_HEIGHT;


	/**
	 * @deprecated Replaced by {@link TextExporterConfiguration#getPageSeparator()}.
	 */
	public static final JRTextExporterParameter BETWEEN_PAGES_TEXT = new JRTextExporterParameter("Between Pages Text");


	/**
	 * @deprecated Replaced by {@link TextExporterConfiguration#getLineSeparator()}.
	 */
	public static final JRTextExporterParameter LINE_SEPARATOR = new JRTextExporterParameter("Line Separator");

}
