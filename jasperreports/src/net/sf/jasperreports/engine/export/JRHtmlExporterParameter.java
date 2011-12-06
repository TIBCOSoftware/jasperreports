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
import net.sf.jasperreports.engine.JRPrintFrame;
import net.sf.jasperreports.engine.JRTextElement;
import net.sf.jasperreports.engine.util.JRProperties;


/**
 * Contains parameters useful for export in HTML format.
 * <p>
 * The HTML exporter can send data to a string buffer, output stream, character stream or file on disk. The engine looks
 * among the export parameters in order to find the selected output type in this order: OUTPUT_STRING_BUFFER, OUTPUT_WRITER,
 * OUTPUT_STREAM, OUTPUT_FILE, OUTPUT_FILE_NAME.
 * <p>
 * An important issue is images. The HTML format stores images as separate files, so the exporter needs to know
 * where these images will be stored. If they are stored on disk, the IMAGES_URI parameter will be initialized with a string
 * containing the file name on disk. If they remain in memory, IMAGES_URI must point to a resource that is able to send the images
 * to the browser (such as an image servlet, as shown in the <i>webapp</i> example).
 *
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JRHtmlExporterParameter extends JRExporterParameter
{


	/**
	 *
	 */
	public JRHtmlExporterParameter(String name)
	{
		super(name);
	}


	/**
	 * A map containing all the images that were used for generating the report. The JasperReports engine stores all the
	 * images in this map, and uses the map keys for referencing images throughout the export process.
	 */
	public static final JRHtmlExporterParameter IMAGES_MAP = new JRHtmlExporterParameter("Images Map Object");


	/**
	 * A <tt>java.io.File</tt> instance representing an absolute path to a folder on a local disk, where all the images are stored.
	 */
	public static final JRHtmlExporterParameter IMAGES_DIR = new JRHtmlExporterParameter("Images Directory");


	/**
	 * An absolute path to a folder on a local disk, where all the images are stored. This is an alternative to IMAGES_DIR
	 */
	public static final JRHtmlExporterParameter IMAGES_DIR_NAME = new JRHtmlExporterParameter("Images Directory Name");


	/**
	 * A boolean value specifying whether the images should be stored on disk. The default value is true. If this parameter is
	 * specified, IMAGES_DIR or IMAGES_DIR_NAME must also be specified and point to a valid directory.
	 */
	public static final JRHtmlExporterParameter IS_OUTPUT_IMAGES_TO_DIR = new JRHtmlExporterParameter("Is Output Images to Directory Flag");


	/**
	 * An URI to the folder where all the images are stored. It could also point to a different resource, such as an image servlet.
	 * It is used in the generated HTML to point to the actual location of the image in the <img> tag (as a file on disk or a
	 * web resource).
	 */
	public static final JRHtmlExporterParameter IMAGES_URI = new JRHtmlExporterParameter("Images URI");


	/**
	 * A string representing HTML code that will be inserted in front of the generated report. The JasperReports engine places
	 * a piece of HTML code at the top of the page but users can define their own headers and stylesheet links.
	 */
	public static final JRHtmlExporterParameter HTML_HEADER = new JRHtmlExporterParameter("HTML Header");


	/**
	 * A string representing HTML code that will be inserted between pages of the generated report. By default, JasperReports
	 * separates pages by two empty lines, but this behavior can be overridden by this parameter.
	 */
	public static final JRHtmlExporterParameter BETWEEN_PAGES_HTML = new JRHtmlExporterParameter("Between Pages HTML");


	/**
	 * A string representing HTML code that will be inserted after the generated report. By default, JasperReports closes
	 * the usual HTML tags that were opened in HTML_HEADER. If the default HTML_HEADER was overridden, it is recommended that
	 * this parameter is overridden too, in order to ensure proper construction of HTML page.
	 */
	public static final JRHtmlExporterParameter HTML_FOOTER = new JRHtmlExporterParameter("HTML Footer");



	/**
	 * A boolean value specifying whether the blank lines, that sometimes appear between rows, should be deleted. Sometimes page
	 * break occurs before the entire page is filled with data (i.e. having a group with the <i>isStartNewPage</i> attribute set to true).
	 * All the remaining empty space could be removed by setting this parameter to true.
	 */
	public static final JRHtmlExporterParameter IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS = new JRHtmlExporterParameter("Is Remove Empty Space Between Rows");


	/**
	 * Property whose value is used as default state of the {@link #IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS} export flag.
	 * <p/>
	 * This property is by default not set (<code>false</code>).
	 * 
	 * @see JRProperties
	 */
	public static final String PROPERTY_REMOVE_EMPTY_SPACE_BETWEEN_ROWS = JRProperties.PROPERTY_PREFIX + "export.html.remove.emtpy.space.between.rows";


	/**
	 * A boolean value specifying whether the report background should be white. If this parameter is not set, the default
	 * background will appear, depending on the selected CSS styles.
	 */
	public static final JRHtmlExporterParameter IS_WHITE_PAGE_BACKGROUND = new JRHtmlExporterParameter("Is White Page Background");


	/**
	 * Property whose value is used as default state of the {@link #IS_WHITE_PAGE_BACKGROUND IS_WHITE_PAGE_BACKGROUND} export flag.
	 * <p/>
	 * This property is set by default (<code>true</code>).
	 * 
	 * @see JRProperties
	 */
	public static final String PROPERTY_WHITE_PAGE_BACKGROUND = JRProperties.PROPERTY_PREFIX + "export.xls.white.page.background";


	/**
	 * A boolean value specifying whether the export engine should use small images for aligning. This is useful when you don't have
	 * images in your report anyway and you don't want to have to handle images at all.
	 */
	public static final JRHtmlExporterParameter IS_USING_IMAGES_TO_ALIGN = new JRHtmlExporterParameter("Is Using Images To Align");


	/**
	 * Property whose value is used as default state of the {@link #IS_USING_IMAGES_TO_ALIGN IS_USING_IMAGES_TO_ALIGN} export flag.
	 * <p/>
	 * This property is set by default (<code>true</code>).
	 * 
	 * @see JRProperties
	 */
	public static final String PROPERTY_USING_IMAGES_TO_ALIGN = JRProperties.PROPERTY_PREFIX + "export.html.using.images.to.align";


	/**
	 * A boolean value specifying whether the export engine should use force wrapping by breaking words (CSS <code>word-wrap: break-word</code>).
	 * 
	 * <p>
	 * Note that this CSS property is not currently supported by all browsers.
	 * An alternative approach for forcing word breaks in HTML is to save the
	 * line breaks at fill time via the {@link JRTextElement#PROPERTY_SAVE_LINE_BREAKS}
	 * property.
	 */
	public static final JRHtmlExporterParameter IS_WRAP_BREAK_WORD = new JRHtmlExporterParameter("Is Wrap Break Word");


	/**
	 * Property whose value is used as default state of the {@link #IS_WRAP_BREAK_WORD IS_WRAP_BREAK_WORD} export flag.
	 * <p/>
	 * This property is by default not set (<code>false</code>).
	 * 
	 * @see JRProperties
	 */
	public static final String PROPERTY_WRAP_BREAK_WORD = JRProperties.PROPERTY_PREFIX + "export.html.wrap.break.word";


	/**
	 * A String value specifying the unit to use when measuring lengths or font size. 
	 * This can be one of the supported size units from the CSS specifications like "px" for pixels
	 * or "pt" for points. The default value is "px", meaning that lengths and font sizes are specified in pixels. 
	 */
	public static final JRHtmlExporterParameter SIZE_UNIT = new JRHtmlExporterParameter("Size Unit");

	/**
	 * Property whose value is used as default for the {@link #SIZE_UNIT SIZE_UNIT} export parameter.
	 * 
	 * @see JRProperties
	 */
	public static final String PROPERTY_SIZE_UNIT = JRProperties.PROPERTY_PREFIX + "export.html.size.unit";


	/**
	 * Possible value for the {@link JRHtmlExporterParameter#SIZE_UNIT} parameter indicated that measurements are made in pixels. 
	 */
	public static final String SIZE_UNIT_PIXEL = "px";

	/**
	 * Possible value for the {@link JRHtmlExporterParameter#SIZE_UNIT} parameter indicated that measurements are made in points. 
	 */
	public static final String SIZE_UNIT_POINT = "pt";

	
	/**
	 * The zoom ratio used for the export. The default value is 1.
	 */
	public static final JRHtmlExporterParameter ZOOM_RATIO = new JRHtmlExporterParameter("Zoom Ratio");

	/**
	 * Indicates whether {@link JRPrintFrame frames} are to be exported as nested HTML tables.
	 * <p>
	 * The type of the parameter is <code>java.lang.Boolean</code>.
	 * </p>
	 * <p>
	 * Is set to <code>false</code>, the frame contents will be integrated into the master/page HTML table.
	 * This can be useful when exporting frames as nested tables causes output misalignments.
	 * </p>
	 * @see #PROPERTY_FRAMES_AS_NESTED_TABLES
	 */
	public static final JRHtmlExporterParameter FRAMES_AS_NESTED_TABLES = new JRHtmlExporterParameter("Export Frames as Nested Tables");
	

	/**
	 * This property serves as default value for the {@link #FRAMES_AS_NESTED_TABLES FRAMES_AS_NESTED_TABLES}
	 * export parameter.
	 * <p>
	 * The propery itself defaults to <code>true</code>.
	 * </p>
	 * @see JRProperties
	 */
	public static final String PROPERTY_FRAMES_AS_NESTED_TABLES = JRProperties.PROPERTY_PREFIX + "export.html.frames.as.nested.tables";
	

	/**
	 * A parameter that determines whether the HTML exporter should flush the
	 * output stream after writing the HTML content to it.
	 * 
	 * <p>
	 * The default value is given by the {@link #PROPERTY_FLUSH_OUTPUT} property.
	 * </p>
	 */
	public static final JRHtmlExporterParameter FLUSH_OUTPUT = 
		new JRHtmlExporterParameter("Flush Output");
	
	/**
	 * Property that provides the default value for the {@link #FLUSH_OUTPUT}
	 * parameter.
	 * 
	 * <p>
	 * The property can be set at report level or globally.
	 * By default, the HTML exporter performs a flush on the output stream
	 * after export.
	 * </p>
	 */
	public static final String PROPERTY_FLUSH_OUTPUT = 
		JRProperties.PROPERTY_PREFIX + "export.html.flush.output";
}
