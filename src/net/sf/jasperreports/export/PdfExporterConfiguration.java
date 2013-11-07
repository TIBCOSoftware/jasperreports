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

import net.sf.jasperreports.engine.JRPrintHyperlink;
import net.sf.jasperreports.engine.JRPropertiesUtil;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.export.annotations.ExporterParameter;
import net.sf.jasperreports.export.annotations.ExporterProperty;
import net.sf.jasperreports.export.type.PdfPrintScalingEnum;
import net.sf.jasperreports.export.type.PdfVersionEnum;
import net.sf.jasperreports.export.type.PdfaConformanceEnum;


/**
 * Interface containing settings used by the PDF exporter.
 *
 * @see JRPdfExporter
 * 
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public interface PdfExporterConfiguration extends ExporterConfiguration
{
	/**
	 * Property whose value is used as default state of the {@link #isCreatingBatchModeBookmarks()} export configuration flag.
	 * <p/>
	 * This property is by default not set (<code>false</code>).
	 * 
	 * @see JRPropertiesUtil
	 */
	public static final String PROPERTY_CREATE_BATCH_MODE_BOOKMARKS = JRPropertiesUtil.PROPERTY_PREFIX + "export.pdf.create.batch.mode.bookmarks";
	
	/**
	 * Property that provides a default value for the {@link #isForceSvgShapes()} PDF exporter configuration flag.
	 */
	public static final String PROPERTY_FORCE_SVG_SHAPES = JRPropertiesUtil.PROPERTY_PREFIX + "export.pdf.force.svg.shapes";

	/**
	 * Property whose value is used as default state of the {@link #isCompressed()} export configuration flag.
	 * <p/>
	 * This property is by default not set (<code>false</code>).
	 * 
	 * @see JRPropertiesUtil
	 */
	public static final String PROPERTY_COMPRESSED = JRPropertiesUtil.PROPERTY_PREFIX + "export.pdf.compressed";

	/**
	 * Property whose value is used as default state of the {@link #isEncrypted()} export configuration flag.
	 * <p/>
	 * This property is by default not set (<code>false</code>).
	 * 
	 * @see JRPropertiesUtil
	 */
	public static final String PROPERTY_ENCRYPTED = JRPropertiesUtil.PROPERTY_PREFIX + "export.pdf.encrypted";

	/**
	 * Property whose value is used as default state of the {@link #is128BitKey()} export configuration flag.
	 * <p/>
	 * This property is by default not set (<code>false</code>).
	 * 
	 * @see JRPropertiesUtil
	 */
	public static final String PROPERTY_128_BIT_KEY = JRPropertiesUtil.PROPERTY_PREFIX + "export.pdf.128.bit.key";

	/**
	 * Property whose value is used as default for the {@link #getUserPassword()} export configuration setting.
	 * 
	 * @see JRPropertiesUtil
	 */
	public static final String PROPERTY_USER_PASSWORD = JRPropertiesUtil.PROPERTY_PREFIX + "export.pdf.user.password";

	/**
	 * Property whose value is used as default for the {@link #getOwnerPassword()} export configuration setting.
	 * 
	 * @see JRPropertiesUtil
	 */
	public static final String PROPERTY_OWNER_PASSWORD = JRPropertiesUtil.PROPERTY_PREFIX + "export.pdf.owner.password";

	/**
	 * Property whose value is used as default for the {@link #getPdfVersion()} export configuration setting.
	 * Possible values of the this property are 2, 3, 4, 5, 6 and 7.
	 * 
	 * @see JRPropertiesUtil
	 */
	public static final String PROPERTY_PDF_VERSION = JRPropertiesUtil.PROPERTY_PREFIX + "export.pdf.version";

	/**
	 * Property whose value is used as default for the {@link #getPdfJavaScript()} export configuration setting.
	 * 
	 * @see JRPropertiesUtil
	 */
	public static final String PROPERTY_PDF_JAVASCRIPT = JRPropertiesUtil.PROPERTY_PREFIX + "export.pdf.javascript";

	/**
	 * Property whose value is used as default state of the {@link #getPrintScaling()} export configuration setting.
	 * <p/>
	 * By default, this property is set to {@link #PRINT_SCALING_DEFAULT}.
	 *
	 * @see JRPropertiesUtil
	 */
	public static final String PROPERTY_PRINT_SCALING = JRPropertiesUtil.PROPERTY_PREFIX + "export.pdf.print.scaling";

	/**
	 * Property that provides a default for the {@link #isCollapseMissingBookmarkLevels()} export configuration flag.
	 * 
	 * <p>
	 * The property can be set globally and at report level.
	 * By default, the property is set to <code>false</code>.
	 * </p>
	 * 
	 * @see net.sf.jasperreports.engine.JRAnchor#getBookmarkLevel()
	 * @since 3.7.3
	 */
	public static final String PROPERTY_COLLAPSE_MISSING_BOOKMARK_LEVELS = 
		JRPropertiesUtil.PROPERTY_PREFIX + "export.pdf.collapse.missing.bookmark.levels";

	/**
	 * Property whose value is used as default for the {@link #isTagged()} export configuration flag.
	 * 
	 * @see JRPropertiesUtil
	 */
	public static final String PROPERTY_TAGGED = JRPropertiesUtil.PROPERTY_PREFIX + "export.pdf.tagged";

	/**
	 * Property whose value is used as default for the {@link #getTagLanguage()} export configuration setting.
	 * 
	 * @see JRPropertiesUtil
	 */
	public static final String PROPERTY_TAG_LANGUAGE = JRPropertiesUtil.PROPERTY_PREFIX + "export.pdf.tag.language";

	/**
	 * Property which specifies a default for the {@link #isSizePageToContent()} export configuration flag.
	 * 
	 * @see JRPropertiesUtil
	 */
	public static final String PROPERTY_SIZE_PAGE_TO_CONTENT = JRPropertiesUtil.PROPERTY_PREFIX + "export.pdf.size.page.to.content";

	/**
	 * Property whose value is used as default for the {@link #getPdfaConformance()} export configuration setting.
	 * <p/>
	 * By default, this property is set to {@link PdfaConformanceEnum#NONE}.
	 * 
	 * @see JRPropertiesUtil
	 */
	public static final String PROPERTY_PDFA_CONFORMANCE = JRPropertiesUtil.PROPERTY_PREFIX + "export.pdfa.conformance";

	/**
	 * Property whose value is used as default for the {@link #getIccProfilePath} export configuration setting.
	 */
	public static final String PROPERTY_PDFA_ICC_PROFILE_PATH = JRPropertiesUtil.PROPERTY_PREFIX + "export.pdfa.icc.profile.path";

	/**
	 * Property that provides a default for the {@link #isIgnoreHyperlink()} export configuration flag.
	 */
	public static final String PROPERTY_IGNORE_HYPERLINK = JRPdfExporter.PDF_EXPORTER_PROPERTIES_PREFIX + JRPrintHyperlink.PROPERTY_IGNORE_HYPERLINK_SUFFIX;

	/**
	 * Returns a boolean value specifying  whether the PDF document should contain an outline section.
	 * @see #PROPERTY_CREATE_BATCH_MODE_BOOKMARKS
	 */
	@SuppressWarnings("deprecation")
	@ExporterParameter(
		parameterClass=net.sf.jasperreports.engine.export.JRPdfExporterParameter.class, 
		parameterName="IS_CREATING_BATCH_MODE_BOOKMARKS"
		)
	@ExporterProperty(
		value=PROPERTY_CREATE_BATCH_MODE_BOOKMARKS, 
		booleanDefault=false
		)
	public Boolean isCreatingBatchModeBookmarks();
	
	/**
	 * Flag to force the rendering of SVG images using shapes, on the PDF Graphics2D context.
	 *
	 * This allows rendering fonts as shapes and avoid font mapping issues that might cause Unicode
	 * text not to show up properly, but has the disadvantage of producing larger PDF files.
	 * By default, the flag is set to true, mainly due to backward compatibility reasons.
	 *
	 * To reduce PDF file size for documents containing SVG images such as charts, the flag should be
	 * turned to false, and the PDF exporter font mappings should be correctly configured using the
	 * font extension support, to ensure proper rendering of text in the SVG.
	 *
	 * This flag can be set system-wide using the {@link #PROPERTY_FORCE_SVG_SHAPES PROPERTY_FORCE_SVG_SHAPES} 
	 * property.
	 *
	 * @see #PROPERTY_FORCE_SVG_SHAPES
	 */
	@SuppressWarnings("deprecation")
	@ExporterParameter(
		parameterClass=net.sf.jasperreports.engine.export.JRPdfExporterParameter.class, 
		parameterName="FORCE_SVG_SHAPES"
		)
	@ExporterProperty(
		value=PROPERTY_FORCE_SVG_SHAPES, 
		booleanDefault=false
		)
	public Boolean isForceSvgShapes();
	
	/**
	 * Returns a boolean value specifying whether the PDF document should be compressed.
	 * @see #PROPERTY_COMPRESSED
	 */
	@SuppressWarnings("deprecation")
	@ExporterParameter(
		parameterClass=net.sf.jasperreports.engine.export.JRPdfExporterParameter.class, 
		parameterName="IS_COMPRESSED"
		)
	@ExporterProperty(
		value=PROPERTY_COMPRESSED, 
		booleanDefault=false
		)
	public Boolean isCompressed();
	
	/**
	 * Returns a boolean value specifying whether the final PDF document should be encrypted.
	 * @see PdfExporterConfiguration#PROPERTY_ENCRYPTED
	 */
	@SuppressWarnings("deprecation")
	@ExporterParameter(
		parameterClass=net.sf.jasperreports.engine.export.JRPdfExporterParameter.class, 
		parameterName="IS_ENCRYPTED"
		)
	@ExporterProperty(
		value=PROPERTY_ENCRYPTED, 
		booleanDefault=false
		)
	public Boolean isEncrypted();
	
	/**
	 * Returns a boolean value specifying whether the encryption key is 128 bits.
	 * @see #PROPERTY_128_BIT_KEY
	 */
	@SuppressWarnings("deprecation")
	@ExporterParameter(
		parameterClass=net.sf.jasperreports.engine.export.JRPdfExporterParameter.class, 
		parameterName="IS_128_BIT_KEY"
		)
	@ExporterProperty(
		value=PROPERTY_128_BIT_KEY, 
		booleanDefault=false
		)
	public Boolean is128BitKey();
	
	/**
	 * The user password needed to open the document, if it is encrypted.
	 * @see #PROPERTY_USER_PASSWORD
	 */
	@SuppressWarnings("deprecation")
	@ExporterParameter(
		parameterClass=net.sf.jasperreports.engine.export.JRPdfExporterParameter.class, 
		parameterName="USER_PASSWORD"
		)
	@ExporterProperty(PROPERTY_USER_PASSWORD)
	public String getUserPassword();
	
	/**
	 * The password belonging to the owner of the document, if it is encrypted. If the password is null, it will be replaced
	 * by a random string.
	 * @see #PROPERTY_OWNER_PASSWORD
	 */
	@SuppressWarnings("deprecation")
	@ExporterParameter(
		parameterClass=net.sf.jasperreports.engine.export.JRPdfExporterParameter.class, 
		parameterName="OWNER_PASSWORD"
		)
	@ExporterProperty(PROPERTY_OWNER_PASSWORD)
	public String getOwnerPassword();
	
	/**
	 * Returns a <tt>Character</tt> instance representing the version of the generated PDF. This class contains predefined constants
	 * that can be passed as parameters directly.
	 * @see #PROPERTY_PDF_VERSION
	 */
	@SuppressWarnings("deprecation")
	@ExporterParameter(
		parameterClass=net.sf.jasperreports.engine.export.JRPdfExporterParameter.class, 
		parameterName="PDF_VERSION"
		)
	@ExporterProperty(PROPERTY_PDF_VERSION)
	public PdfVersionEnum getPdfVersion();
	
	/**
	 * The user defined JavaScript piece of code to be inserted in the generated PDF document.
	 * @see #PROPERTY_PDF_JAVASCRIPT
	 */
	@SuppressWarnings("deprecation")
	@ExporterParameter(
		parameterClass=net.sf.jasperreports.engine.export.JRPdfExporterParameter.class, 
		parameterName="PDF_JAVASCRIPT"
		)
	@ExporterProperty(PROPERTY_PDF_JAVASCRIPT)
	public String getPdfJavaScript();
	
	/**
	 * Setting specifying the print scaling preference in the PDF print dialog.
	 */
	@SuppressWarnings("deprecation")
	@ExporterParameter(
		parameterClass=net.sf.jasperreports.engine.export.JRPdfExporterParameter.class, 
		parameterName="PRINT_SCALING"
		)
	@ExporterProperty(PROPERTY_PRINT_SCALING)
	public PdfPrintScalingEnum getPrintScaling();
	
	/**
	 * Flag that determines if missing bookmark levels are collapsed, or if 
	 * empty bookmarks are created for the missing levels.
	 * 
	 * <p>
	 * The flag dictates what happens when a filled report contains an anchor
	 * having a bookmark level that is not the immediate successor of its parent
	 * (e.g. an anchor with bookmark level 3 follows immediately after a bookmark
	 * of level 1).
	 * If the flag is not set, an empty bookmark is created for the missing
	 * level(s) in order to preserve the original level of the bookmark.
	 * When the property is set, the level of the bookmark will be collapsed and
	 * the bookmark will be created as a direct descendant of its nearest parent.
	 * </p>
	 * @see #PROPERTY_COLLAPSE_MISSING_BOOKMARK_LEVELS
	 */
	@ExporterProperty(
		value=PROPERTY_COLLAPSE_MISSING_BOOKMARK_LEVELS,
		booleanDefault=false
		)
	public Boolean isCollapseMissingBookmarkLevels();
	
	/**
	 * Specifies whether the exporter should put structure tags in the generated PDF.
	 * @see #PROPERTY_TAGGED
	 */
	@SuppressWarnings("deprecation")
	@ExporterParameter(
		parameterClass=net.sf.jasperreports.engine.export.JRPdfExporterParameter.class, 
		parameterName="IS_TAGGED"
		)
	@ExporterProperty(
		value=PROPERTY_TAGGED, 
		booleanDefault=false
		)
	public Boolean isTagged();
	
	/**
	 * Specifies the language that the exporter should put in the language tag of the generated PDF.
	 * @see #PROPERTY_TAG_LANGUAGE
	 */
	@SuppressWarnings("deprecation")
	@ExporterParameter(
		parameterClass=net.sf.jasperreports.engine.export.JRPdfExporterParameter.class, 
		parameterName="TAG_LANGUAGE"
		)
	@ExporterProperty(PROPERTY_TAG_LANGUAGE)
	public String getTagLanguage();
	
	/**
	 * Flag which specifies if the size of each page should be increased to accommodate its content.
	 * @see #PROPERTY_SIZE_PAGE_TO_CONTENT
	 */
	@ExporterProperty(
		value=PROPERTY_SIZE_PAGE_TO_CONTENT, 
		booleanDefault=false
		)
	public Boolean isSizePageToContent();
	
	/**
	 * The Conformance level of the PDF/A document.
	 * @see #PROPERTY_PDFA_CONFORMANCE
	 */
	@SuppressWarnings("deprecation")
	@ExporterParameter(
		parameterClass=net.sf.jasperreports.engine.export.JRPdfExporterParameter.class, 
		parameterName="PDFA_CONFORMANCE"
		)
	@ExporterProperty(PROPERTY_PDFA_CONFORMANCE)
	public PdfaConformanceEnum getPdfaConformance();
	
	/**
	 * The path to the ICC profile file for the PDF/A compliant document.
	 * @see #PROPERTY_PDFA_ICC_PROFILE_PATH
	 */
	@SuppressWarnings("deprecation")
	@ExporterParameter(
		parameterClass=net.sf.jasperreports.engine.export.JRPdfExporterParameter.class, 
		parameterName="PDFA_ICC_PROFILE_PATH"
		)
	@ExporterProperty(PROPERTY_PDFA_ICC_PROFILE_PATH)
	public String getIccProfilePath();
	
	/**
	 * @see #PROPERTY_IGNORE_HYPERLINK
	 */
	@ExporterProperty(
		value=PROPERTY_IGNORE_HYPERLINK, 
		booleanDefault=false
		)
	public Boolean isIgnoreHyperlink();
}
