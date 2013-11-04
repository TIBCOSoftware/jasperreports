/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2013 Jaspersoft Corporation. All rights reserved.
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
package net.sf.jasperreports.export;

import net.sf.jasperreports.engine.JRPropertiesUtil;
import net.sf.jasperreports.engine.JRTextElement;
import net.sf.jasperreports.export.annotations.ExporterParameter;
import net.sf.jasperreports.export.annotations.ExporterProperty;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public interface HtmlExporterConfiguration extends ExporterConfiguration
{
	/**
	 * Property whose value is used as default state of the {@link #isWhitePageBackground()} export configuration flag.
	 * <p/>
	 * This property is set by default (<code>true</code>).
	 * 
	 * @see JRPropertiesUtil
	 */
	public static final String PROPERTY_WHITE_PAGE_BACKGROUND = JRPropertiesUtil.PROPERTY_PREFIX + "export.html.white.page.background";


	/**
	 * Property whose value is used as default state of the {@link #isRemoveEmptySpaceBetweenRows()} export configuration flag.
	 * <p/>
	 * This property is by default not set (<code>false</code>).
	 * 
	 * @see JRPropertiesUtil
	 */
	public static final String PROPERTY_REMOVE_EMPTY_SPACE_BETWEEN_ROWS = JRPropertiesUtil.PROPERTY_PREFIX + "export.html.remove.emtpy.space.between.rows";


	/**
	 * Property that provides the default value for the {@link #isFlushOutput()} export configuration setting.
	 * 
	 * <p>
	 * The property can be set at report level or globally.
	 * By default, the HTML exporter performs a flush on the output stream
	 * after export.
	 * </p>
	 */
	public static final String PROPERTY_FLUSH_OUTPUT = JRPropertiesUtil.PROPERTY_PREFIX + "export.html.flush.output";


	/**
	 * Property whose value is used as default state of the {@link #isWrapBreakWord()} export configuration flag.
	 * <p/>
	 * This property is by default not set (<code>false</code>).
	 * 
	 * @see JRPropertiesUtil
	 */
	public static final String PROPERTY_WRAP_BREAK_WORD = JRPropertiesUtil.PROPERTY_PREFIX + "export.html.wrap.break.word";


	/**
	 * Property whose value is used as default for the {@link #getSizeUnit()} export configuration setting.
	 * 
	 * @see JRPropertiesUtil
	 */
	public static final String PROPERTY_SIZE_UNIT = JRPropertiesUtil.PROPERTY_PREFIX + "export.html.size.unit";

	
	/**
	 * Property that provides the default value for the {@link #getBorderCollapse()} export configuration setting.
	 * <p>
	 * The property can be set globally and at report level.  It defaults to <code>collapse</code>.
	 * </p>
	 */
	public static final String PROPERTY_BORDER_COLLAPSE = JRPropertiesUtil.PROPERTY_PREFIX + "export.html.border.collapse";


	/**
	 * Property that provides a default value for the {@link #isAccessibleHtml()} exporter configuration setting.
	 */
	public static final String PROPERTY_ACCESSIBLE = JRPropertiesUtil.PROPERTY_PREFIX + "export.html.accessible";

	
	/**
	 * Returns a string representing HTML code that will be inserted in front of the generated report. The JasperReports engine places
	 * a piece of HTML code at the top of the page but users can define their own headers and stylesheet links.
	 */
	@SuppressWarnings("deprecation")
	@ExporterParameter(
		parameterClass=net.sf.jasperreports.engine.export.JRHtmlExporterParameter.class, 
		parameterName="HTML_HEADER"
		)
	public String getHtmlHeader();
	

	/**
	 * Returns a string representing HTML code that will be inserted after the generated report. By default, JasperReports closes
	 * the usual HTML tags that were opened in {@link #getHtmlHeader()}. If a custom HTML header was provided using {@link #getHtmlHeader()}, 
	 * it is recommended that a value for this setting is provided too, in order to ensure proper construction of HTML page.
	 */
	@SuppressWarnings("deprecation")
	@ExporterParameter(
		parameterClass=net.sf.jasperreports.engine.export.JRHtmlExporterParameter.class, 
		parameterName="HTML_FOOTER"
		)
	public String getHtmlFooter();
	

	/**
	 * Returns a string representing HTML code that will be inserted between pages of the generated report. By default, JasperReports
	 * separates pages by two empty lines, but this behavior can be overridden by this parameter.
	 */
	@SuppressWarnings("deprecation")
	@ExporterParameter(
		parameterClass=net.sf.jasperreports.engine.export.JRHtmlExporterParameter.class, 
		parameterName="BETWEEN_PAGES_HTML"
		)
	public String getBetweenPagesHtml();
	
	
	/**
	 * Returns a boolean value specifying whether the blank lines, that sometimes appear between rows, should be deleted. Sometimes page
	 * break occurs before the entire page is filled with data (i.e. having a group with the <i>isStartNewPage</i> attribute set to true).
	 * All the remaining empty space could be removed by setting this parameter to true.
	 * @see #PROPERTY_REMOVE_EMPTY_SPACE_BETWEEN_ROWS
	 */
	@SuppressWarnings("deprecation")
	@ExporterParameter(
		parameterClass=net.sf.jasperreports.engine.export.JRHtmlExporterParameter.class, 
		parameterName="IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS"
		)
	@ExporterProperty(
		value=PROPERTY_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, 
		booleanDefault=false
		)
	public Boolean isRemoveEmptySpaceBetweenRows();

	
	/**
	 * Returns a boolean value specifying whether the report background should be white. If this parameter is not set, the default
	 * background will appear, depending on the selected CSS styles.
	 * @see #PROPERTY_WHITE_PAGE_BACKGROUND
	 */
	@SuppressWarnings("deprecation")
	@ExporterParameter(
		parameterClass=net.sf.jasperreports.engine.export.JRHtmlExporterParameter.class, 
		parameterName="IS_WHITE_PAGE_BACKGROUND"
		)
	@ExporterProperty(
		value=PROPERTY_WHITE_PAGE_BACKGROUND, 
		booleanDefault=true
		)
	public Boolean isWhitePageBackground();
	

	/**
	 * Returns a boolean value specifying whether the export engine should use force wrapping by breaking words (CSS <code>word-wrap: break-word</code>).
	 * <p>
	 * Note that this CSS property is not currently supported by all browsers.
	 * An alternative approach for forcing word breaks in HTML is to save the
	 * line breaks at fill time via the {@link JRTextElement#PROPERTY_SAVE_LINE_BREAKS}
	 * property.
	 * @see #PROPERTY_WRAP_BREAK_WORD
	 */
	@SuppressWarnings("deprecation")
	@ExporterParameter(
		parameterClass=net.sf.jasperreports.engine.export.JRHtmlExporterParameter.class,
		parameterName="IS_WRAP_BREAK_WORD"
		)
	@ExporterProperty(
		value=PROPERTY_WRAP_BREAK_WORD,
		booleanDefault=false
		)
	public Boolean isWrapBreakWord();
	

	/**
	 * Returns a String value specifying the unit to use when measuring lengths or font size. 
	 * This can be one of the supported size units from the CSS specifications like "px" for pixels
	 * or "pt" for points. The default value is "px", meaning that lengths and font sizes are specified in pixels.
	 * @see #PROPERTY_SIZE_UNIT 
	 */
	@SuppressWarnings("deprecation")
	@ExporterParameter(
		parameterClass=net.sf.jasperreports.engine.export.JRHtmlExporterParameter.class, 
		parameterName="SIZE_UNIT",
		acceptNull=false
		)
	@ExporterProperty(PROPERTY_SIZE_UNIT)
	public String getSizeUnit();
	
	
	/**
	 * Provides the value for the <code>border-collapse</code> CSS property to be applied
	 * to the table generated for the report.
	 * @see #PROPERTY_BORDER_COLLAPSE
	 */
	@ExporterProperty(PROPERTY_BORDER_COLLAPSE)
	public String getBorderCollapse();
	
	
	/**
	 * Indicates whether page margins should be ignored when the report is exported using a grid-based exporter
	 * <p>
	 * If set to <code>true</code>, any page in the document will be exported without taking into account its margins.
	 * </p>
	 * @see #PROPERTY_IGNORE_PAGE_MARGINS
	 */
	@SuppressWarnings("deprecation")
	@ExporterParameter(
		parameterClass=net.sf.jasperreports.engine.JRExporterParameter.class,
		parameterName="IGNORE_PAGE_MARGINS"
		)
	@ExporterProperty(
		value=PROPERTY_IGNORE_PAGE_MARGINS, 
		booleanDefault=false
		)
	public Boolean isIgnorePageMargins();
	
	
	/**
	 * Configuration setting that determines the exporter to produce accessible HTML.
	 * @see HtmlExporterConfiguration#PROPERTY_ACCESSIBLE
	 */
	@ExporterProperty(
		value=PROPERTY_ACCESSIBLE,
		booleanDefault=false
		)
	public Boolean isAccessibleHtml();
	
	
	/**
	 * The zoom ratio used for the export. The default value is 1.
	 */
	@SuppressWarnings("deprecation")
	@ExporterParameter(
		parameterClass=net.sf.jasperreports.engine.export.JRHtmlExporterParameter.class, 
		parameterName="ZOOM_RATIO"
		)
	public Float getZoomRatio();
	
	
	/**
	 * A flag that determines whether the HTML exporter should flush the
	 * output stream after writing the HTML content to it.
	 * @see #PROPERTY_FLUSH_OUTPUT
	 */
	@SuppressWarnings("deprecation")
	@ExporterParameter(
		parameterClass=net.sf.jasperreports.engine.export.JRHtmlExporterParameter.class, 
		parameterName="FLUSH_OUTPUT"
		)
	@ExporterProperty(
		value=PROPERTY_FLUSH_OUTPUT, 
		booleanDefault=true
		)
	public Boolean isFlushOutput();
}
