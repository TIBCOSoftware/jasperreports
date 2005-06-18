/*
 * ============================================================================
 *                   GNU Lesser General Public License
 * ============================================================================
 *
 * JasperReports - Free Java report-generating library.
 * Copyright (C) 2001-2005 JasperSoft Corporation http://www.jaspersoft.com
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
 * 185, Berry Street, Suite 6200
 * San Francisco CA 94107
 * http://www.jaspersoft.com
 */
package net.sf.jasperreports.engine.export;

import net.sf.jasperreports.engine.JRExporterParameter;


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
	protected JRHtmlExporterParameter(String name)
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
	 * specified, IMAGES_DIR or IMAGES_DIR_FILE must also be specified and point to a valid directory.
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
	 * separates pages by two empty lines, but this behaviour can be overriden by this parameter.
	 */
	public static final JRHtmlExporterParameter BETWEEN_PAGES_HTML = new JRHtmlExporterParameter("Between Pages HTML");


	/**
	 * A string representing HTML code that will be inserted after the generated report. By default, JasperReports closes
	 * the usual HTML tags that were opened in HTML_HEADER. If the default HTML_HEADER was overriden, it is recommended that
	 * this parameter is overriden too, in order to ensure proper construction of HTML page.
	 */
	public static final JRHtmlExporterParameter HTML_FOOTER = new JRHtmlExporterParameter("HTML Footer");



	/**
	 * A boolean value specifying whether the blank lines, that sometimes appear between rows, should be deleted. Sometimes page
	 * break occurs before the entire page is filled with data (i.e. having a group with the <i>isStartNewPage</i> attribute set to true).
	 * All the remaining empty space could be removed by setting this parameter to true.
	 */
	public static final JRHtmlExporterParameter IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS = new JRHtmlExporterParameter("Is Remove Empty Space Between Rows");


	/**
	 * A boolean value specifying whether the report background should be white. If this parameter is not set, the default
	 * background will appear, depending on the selected CSS styles.
	 */
	public static final JRHtmlExporterParameter IS_WHITE_PAGE_BACKGROUND = new JRHtmlExporterParameter("Is White Page Background");


	/**
	 * A boolean value specifying whether the export engine should use small images for aligning. This is useful when you don't have
	 * images in your report anyway and you don't want to have to handle images at all.
	 */
	public static final JRHtmlExporterParameter IS_USING_IMAGES_TO_ALIGN = new JRHtmlExporterParameter("Is Using Images To Align");


}
