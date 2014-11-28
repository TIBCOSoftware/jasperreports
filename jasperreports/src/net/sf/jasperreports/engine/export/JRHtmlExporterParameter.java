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
import net.sf.jasperreports.export.ExporterInput;
import net.sf.jasperreports.export.HtmlExporterConfiguration;
import net.sf.jasperreports.export.HtmlExporterOutput;
import net.sf.jasperreports.export.HtmlReportConfiguration;
import net.sf.jasperreports.export.type.HtmlSizeUnitEnum;


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
 * @deprecated Replaced by {@link ExporterInput}, {@link HtmlExporterConfiguration} and {@link HtmlExporterOutput}.
 * @author Teodor Danciu (teodord@users.sourceforge.net)
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
	 * @deprecated Replaced by {@link HtmlExporterOutput#getImageHandler()}.
	 */
	public static final JRHtmlExporterParameter IMAGES_MAP = new JRHtmlExporterParameter("Images Map Object");


	/**
	 * A <tt>java.io.File</tt> instance representing an absolute path to a folder on a local disk, where all the images are stored.
	 * @deprecated Replaced by {@link HtmlExporterOutput#getImageHandler()}.
	 */
	public static final JRHtmlExporterParameter IMAGES_DIR = new JRHtmlExporterParameter("Images Directory");


	/**
	 * An absolute path to a folder on a local disk, where all the images are stored. This is an alternative to IMAGES_DIR
	 * @deprecated Replaced by {@link HtmlExporterOutput#getImageHandler()}.
	 */
	public static final JRHtmlExporterParameter IMAGES_DIR_NAME = new JRHtmlExporterParameter("Images Directory Name");


	/**
	 * A boolean value specifying whether the images should be stored on disk. The default value is true. If this parameter is
	 * specified, IMAGES_DIR or IMAGES_DIR_NAME must also be specified and point to a valid directory.
	 * @deprecated Replaced by {@link HtmlExporterOutput#getImageHandler()}.
	 */
	public static final JRHtmlExporterParameter IS_OUTPUT_IMAGES_TO_DIR = new JRHtmlExporterParameter("Is Output Images to Directory Flag");


	/**
	 * An URI to the folder where all the images are stored. It could also point to a different resource, such as an image servlet.
	 * It is used in the generated HTML to point to the actual location of the image in the <img> tag (as a file on disk or a
	 * web resource).
	 * @deprecated Replaced by {@link HtmlExporterOutput#getImageHandler()}.
	 */
	public static final JRHtmlExporterParameter IMAGES_URI = new JRHtmlExporterParameter("Images URI");


	/**
	 * @deprecated Replaced by {@link HtmlExporterConfiguration#getHtmlHeader()}.
	 */
	public static final JRHtmlExporterParameter HTML_HEADER = new JRHtmlExporterParameter("HTML Header");


	/**
	 * @deprecated Replaced by {@link HtmlExporterConfiguration#getBetweenPagesHtml()}.
	 */
	public static final JRHtmlExporterParameter BETWEEN_PAGES_HTML = new JRHtmlExporterParameter("Between Pages HTML");


	/**
	 * @deprecated Replaced by {@link HtmlExporterConfiguration#getHtmlFooter()}.
	 */
	public static final JRHtmlExporterParameter HTML_FOOTER = new JRHtmlExporterParameter("HTML Footer");



	/**
	 * @deprecated Replaced by {@link HtmlReportConfiguration#isRemoveEmptySpaceBetweenRows()}.
	 */
	public static final JRHtmlExporterParameter IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS = new JRHtmlExporterParameter("Is Remove Empty Space Between Rows");


	/**
	 * @deprecated Replaced by {@link HtmlReportConfiguration#PROPERTY_REMOVE_EMPTY_SPACE_BETWEEN_ROWS}.
	 */
	public static final String PROPERTY_REMOVE_EMPTY_SPACE_BETWEEN_ROWS = HtmlReportConfiguration.PROPERTY_REMOVE_EMPTY_SPACE_BETWEEN_ROWS;


	/**
	 * @deprecated Replaced by {@link HtmlReportConfiguration#isWhitePageBackground()}.
	 */
	public static final JRHtmlExporterParameter IS_WHITE_PAGE_BACKGROUND = new JRHtmlExporterParameter("Is White Page Background");


	/**
	 * @deprecated Replaced by {@link HtmlReportConfiguration#PROPERTY_WHITE_PAGE_BACKGROUND}.
	 */
	public static final String PROPERTY_WHITE_PAGE_BACKGROUND = HtmlReportConfiguration.PROPERTY_WHITE_PAGE_BACKGROUND;


	/**
	 * @deprecated Replaced by {@link JRHtmlExporterConfiguration#isUsingImagesToAlign()}.
	 */
	public static final JRHtmlExporterParameter IS_USING_IMAGES_TO_ALIGN = new JRHtmlExporterParameter("Is Using Images To Align");


	/**
	 * @deprecated Replaced by {@link JRHtmlExporterConfiguration#PROPERTY_USING_IMAGES_TO_ALIGN}.
	 */
	public static final String PROPERTY_USING_IMAGES_TO_ALIGN = JRHtmlExporterConfiguration.PROPERTY_USING_IMAGES_TO_ALIGN;


	/**
	 * @deprecated Replaced by {@link HtmlReportConfiguration#isWrapBreakWord()}.
	 */
	public static final JRHtmlExporterParameter IS_WRAP_BREAK_WORD = new JRHtmlExporterParameter("Is Wrap Break Word");


	/**
	 * @deprecated Replaced by {@link HtmlReportConfiguration#PROPERTY_WRAP_BREAK_WORD}.
	 */
	public static final String PROPERTY_WRAP_BREAK_WORD = HtmlReportConfiguration.PROPERTY_WRAP_BREAK_WORD;


	/**
	 * @deprecated Replaced by {@link HtmlReportConfiguration#getSizeUnit()}. 
	 */
	public static final JRHtmlExporterParameter SIZE_UNIT = new JRHtmlExporterParameter("Size Unit");

	/**
	 * @deprecated Replaced by {@link HtmlReportConfiguration#PROPERTY_SIZE_UNIT}.
	 */
	public static final String PROPERTY_SIZE_UNIT = HtmlReportConfiguration.PROPERTY_SIZE_UNIT;


	/**
	 * @deprecated Replaced by {@link HtmlSizeUnitEnum#PIXEL}. 
	 */
	public static final String SIZE_UNIT_PIXEL = HtmlSizeUnitEnum.PIXEL.getName();

	/**
	 * @deprecated Replaced by {@link HtmlSizeUnitEnum#POINT}. 
	 */
	public static final String SIZE_UNIT_POINT = HtmlSizeUnitEnum.POINT.getName();

	
	/**
	 * @deprecated Replaced by {@link HtmlReportConfiguration#getZoomRatio()}.
	 */
	public static final JRHtmlExporterParameter ZOOM_RATIO = new JRHtmlExporterParameter("Zoom Ratio");

	/**
	 * @deprecated Replaced by {@link JRHtmlReportConfiguration#isFramesAsNestedTables()}.
	 */
	public static final JRHtmlExporterParameter FRAMES_AS_NESTED_TABLES = new JRHtmlExporterParameter("Export Frames as Nested Tables");
	

	/**
	 * @deprecated Replaced by {@link JRHtmlReportConfiguration#PROPERTY_FRAMES_AS_NESTED_TABLES}.
	 */
	public static final String PROPERTY_FRAMES_AS_NESTED_TABLES = JRHtmlReportConfiguration.PROPERTY_FRAMES_AS_NESTED_TABLES;
	

	/**
	 * @deprecated Replaced by {@link HtmlExporterConfiguration#isFlushOutput()}.
	 */
	public static final JRHtmlExporterParameter FLUSH_OUTPUT = new JRHtmlExporterParameter("Flush Output");
	
	/**
	 * @deprecated Replaced by {@link HtmlExporterConfiguration#PROPERTY_FLUSH_OUTPUT}.
	 */
	public static final String PROPERTY_FLUSH_OUTPUT = HtmlExporterConfiguration.PROPERTY_FLUSH_OUTPUT;
}
