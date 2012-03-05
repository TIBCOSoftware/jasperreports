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
package net.sf.jasperreports.engine.export;

import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.util.JRProperties;

/**
 * Contains parameters useful for export in plain text format.
 * <p>
 * The text exporter can send data to a string buffer, output stream, character stream or file on disk. The engine looks
 * among the export parameters in order to find the selected output type in this order: OUTPUT_STRING_BUFFER, OUTPUT_WRITER,
 * OUTPUT_STREAM, OUTPUT_FILE, OUTPUT_FILE_NAME.
 *
 * @author Ionut Nedelcu (ionutned@users.sourceforge.net)
 * @version $Id$
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
	 * An integer representing the pixel/character horizontal ratio.
	 */
	public static final JRTextExporterParameter CHARACTER_WIDTH = new JRTextExporterParameter("Character Width");

	
	/**
	 * Property whose value is used as default state of the {@link #CHARACTER_WIDTH CHARACTER_WIDTH} export parameter.
	 * <p/>
	 * This property is not set by default.
	 * 
	 * @see JRProperties
	 */
	public static final String PROPERTY_CHARACTER_WIDTH = JRProperties.PROPERTY_PREFIX + "export.text.character.width";


	/**
	 * An integer representing the pixel/character horizontal ratio.
	 */
	public static final JRTextExporterParameter CHARACTER_HEIGHT = new JRTextExporterParameter("Character Height");


	/**
	 * Property whose value is used as default state of the {@link #CHARACTER_HEIGHT CHARACTER_HEIGHT} export parameter.
	 * <p/>
	 * This property is not set by default.
	 * 
	 * @see JRProperties
	 */
	public static final String PROPERTY_CHARACTER_HEIGHT = JRProperties.PROPERTY_PREFIX + "export.text.character.height";


	/**
	 * An integer representing the page width in characters.
	 */
	public static final JRTextExporterParameter PAGE_WIDTH = new JRTextExporterParameter("Page Width");


	/**
	 * Property whose value is used as default state of the {@link #PAGE_WIDTH PAGE_WIDTH} export parameter.
	 * <p/>
	 * This property is not set by default.
	 * 
	 * @see JRProperties
	 */
	public static final String PROPERTY_PAGE_WIDTH = JRProperties.PROPERTY_PREFIX + "export.text.page.width";


	/**
	 * An integer representing the page height in characters.
	 */
	public static final JRTextExporterParameter PAGE_HEIGHT = new JRTextExporterParameter("Page Height");


	/**
	 * Property whose value is used as default state of the {@link #PAGE_HEIGHT PAGE_HEIGHT} export parameter.
	 * <p/>
	 * This property is not set by default.
	 * 
	 * @see JRProperties
	 */
	public static final String PROPERTY_PAGE_HEIGHT = JRProperties.PROPERTY_PREFIX + "export.text.page.height";


	/**
	 * A string representing text that will be inserted between pages of the generated report. By default, JasperReports
	 * separates pages by two empty lines, but this behavior can be overridden by this parameter.
	 */
	public static final JRTextExporterParameter BETWEEN_PAGES_TEXT = new JRTextExporterParameter("Between Pages Text");


	/**
	 * A string representing the separator between two lines of text. This parameter is useful since line separators can
	 * vary from one operating system to another. The default value is the system "line.separator" property.
	 */
	public static final JRTextExporterParameter LINE_SEPARATOR = new JRTextExporterParameter("Line Separator");

}
