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
 * Contains parameters useful for export in PDF format.
 * <p>
 * The PDF exporter can send data to an output stream or a file on disk. The engine looks among the export parameters in
 * order to find the selected output type in this order: OUTPUT_STREAM, OUTPUT_FILE, OUTPUT_FILE_NAME.
 * <p>
 *
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JRPdfExporterParameter extends JRExporterParameter
{


	public static final Character PDF_VERSION_1_2 = new Character('2');  // Not using iText constants in order not to depend on the library version
	public static final Character PDF_VERSION_1_3 = new Character('3');
	public static final Character PDF_VERSION_1_4 = new Character('4');
	public static final Character PDF_VERSION_1_5 = new Character('5');
	public static final Character PDF_VERSION_1_6 = new Character('6');
	public static final Character PDF_VERSION_1_7 = new Character('7');

	public static final String PRINT_SCALING_DEFAULT = "default";
	public static final String PRINT_SCALING_NONE = "none";
	
	public static final String PDFA_CONFORMANCE_1A = "pdfa1a";
	public static final String PDFA_CONFORMANCE_1B = "pdfa1b";
	public static final String PDFA_CONFORMANCE_NONE = "none";

	/**
	 *
	 */
	protected JRPdfExporterParameter(String name)
	{
		super(name);
	}

	
	/**
	 * A boolean value specifying  whether the PDF document should contain an outline section
	 */
	public static final JRPdfExporterParameter IS_CREATING_BATCH_MODE_BOOKMARKS = new JRPdfExporterParameter("Is Creating Batch Mode Bookmarks");

	
	/**
	 * Property whose value is used as default state of the {@link #IS_CREATING_BATCH_MODE_BOOKMARKS IS_CREATING_BATCH_MODE_BOOKMARKS} export flag.
	 * <p/>
	 * This property is by default not set (<code>false</code>).
	 * 
	 * @see JRProperties
	 */
	public static final String PROPERTY_CREATE_BATCH_MODE_BOOKMARKS = JRProperties.PROPERTY_PREFIX + "export.pdf.create.batch.mode.bookmarks";

	
	/**
	 * A boolean value specifying whether the PDF document should be compressed.
	 */
	public static final JRPdfExporterParameter IS_COMPRESSED = new JRPdfExporterParameter("Is Compressed");

	
	/**
	 * Property whose value is used as default state of the {@link #IS_COMPRESSED IS_COMPRESSED} export flag.
	 * <p/>
	 * This property is by default not set (<code>false</code>).
	 * 
	 * @see JRProperties
	 */
	public static final String PROPERTY_COMPRESSED = JRProperties.PROPERTY_PREFIX + "export.pdf.compressed";


	/**
	 * A boolean value specifying whether the final PDF document should be encrypted.
	 */
	public static final JRPdfExporterParameter IS_ENCRYPTED = new JRPdfExporterParameter("Is Encrypted");

	
	/**
	 * Property whose value is used as default state of the {@link #IS_ENCRYPTED IS_ENCRYPTED} export flag.
	 * <p/>
	 * This property is by default not set (<code>false</code>).
	 * 
	 * @see JRProperties
	 */
	public static final String PROPERTY_ENCRYPTED = JRProperties.PROPERTY_PREFIX + "export.pdf.encrypted";


	/**
	 * A boolean value specifying whether the encryption key is 128 bits.
	 */
	public static final JRPdfExporterParameter IS_128_BIT_KEY = new JRPdfExporterParameter("Is 128 Bit Key");


	/**
	 * Property whose value is used as default state of the {@link #IS_128_BIT_KEY IS_128_BIT_KEY} export flag.
	 * <p/>
	 * This property is by default not set (<code>false</code>).
	 * 
	 * @see JRProperties
	 */
	public static final String PROPERTY_128_BIT_KEY = JRProperties.PROPERTY_PREFIX + "export.pdf.128.bit.key";


	/**
	 * The user password needed to open the document, if it is encrypted.
	 */
	public static final JRPdfExporterParameter USER_PASSWORD = new JRPdfExporterParameter("User Password");


	/**
	 * Property whose value is used as default for the {@link #USER_PASSWORD USER_PASSWORD} export parameter.
	 * 
	 * @see JRProperties
	 */
	public static final String PROPERTY_USER_PASSWORD = JRProperties.PROPERTY_PREFIX + "export.pdf.user.password";


	/**
	 * The password belonging to the owner of the document, if it is encrypted. If the password is null, it will be replaced
	 * by a random string.
	 */
	public static final JRPdfExporterParameter OWNER_PASSWORD = new JRPdfExporterParameter("Owner Password");


	/**
	 * Property whose value is used as default for the {@link #OWNER_PASSWORD OWNER_PASSWORD} export parameter.
	 * 
	 * @see JRProperties
	 */
	public static final String PROPERTY_OWNER_PASSWORD = JRProperties.PROPERTY_PREFIX + "export.pdf.owner.password";


	/**
	 * An integer value representing the PDF permissions for the generated document. The open permissions for the document
	 * can be AllowPrinting, AllowModifyContents, AllowCopy, AllowModifyAnnotations, AllowFillIn, AllowScreenReaders,
	 * AllowAssembly and AllowDegradedPrinting (these can all be found in the PdfWriter class of iText library). The
	 * permissions can be combined by applying bitwise OR to them.
	 */
	public static final JRPdfExporterParameter PERMISSIONS = new JRPdfExporterParameter("Permissions");


	/**
	 * A <tt>Character</tt> instance representing the version of the generated PDF. This class contains predefined constants
	 * that can be passed as parameters directly.
	 */
	public static final JRPdfExporterParameter PDF_VERSION = new JRPdfExporterParameter("PDF Version");


	/**
	 * Property whose value is used as default for the {@link #PDF_VERSION PDF_VERSION} export parameter.
	 * Possible values of the this property are 2, 3, 4, 5, 6 and 7.
	 * 
	 * @see JRProperties
	 */
	public static final String PROPERTY_PDF_VERSION = JRProperties.PROPERTY_PREFIX + "export.pdf.version";


	/**
	 * The Title of the PDF document, as String.
	 */
	public static final JRPdfExporterParameter METADATA_TITLE = new JRPdfExporterParameter("Metadata Title");

	/**
	 * The Author of the PDF document, as String.
	 */
	public static final JRPdfExporterParameter METADATA_AUTHOR = new JRPdfExporterParameter("Metadata Author");

	/**
	 * The Subject of the PDF document, as String.
	 */
	public static final JRPdfExporterParameter METADATA_SUBJECT = new JRPdfExporterParameter("Metadata Subject");

	/**
	 * The Keywords of the PDF document, as comma-separated String.
	 */
	public static final JRPdfExporterParameter METADATA_KEYWORDS = new JRPdfExporterParameter("Metadata Keywords");

	/**
	 * The Creator or Application for the PDF document, as String, defaults to JasperReports.
	 */
	public static final JRPdfExporterParameter METADATA_CREATOR = new JRPdfExporterParameter("Metadata Creator");


	/**
	 * Flag that decides whether the PDF exporter should use a {@link com.lowagie.text.SplitCharacter SplitCharacter}
	 * implementation which ensures that report texts are broken into lines by iText in the same manner as done by the
	 * fill process.
	 * <p>
	 * The default line-breaking logic differs from AWT (which is used during the report fill) to iText (used by the PDF
	 * exporter).  By setting this flag, the logic used by AWT is imposed to iText.  The drawback is that the PDF export
	 * performance would drop.  Because of this, the flag is not set by default.
	 * <p>
	 * This flag can be set system-wide using the
	 * {@link #PROPERTY_FORCE_LINEBREAK_POLICY PROPERTY_FORCE_LINEBREAK_POLICY} property.
	 * This export parameter overrides the property value.
	 *
	 * @see #PROPERTY_FORCE_LINEBREAK_POLICY
	 * @see net.sf.jasperreports.engine.util.BreakIteratorSplitCharacter
	 * @deprecated No longer used.
	 */
	public static final JRPdfExporterParameter FORCE_LINEBREAK_POLICY = new JRPdfExporterParameter("Force linebreak policy");


	/**
	 * Property that provides a default value for the {@link #FORCE_LINEBREAK_POLICY FORCE_LINEBREAK_POLICY}
	 * PDF exporter parameter.
	 * 
	 * @see #FORCE_LINEBREAK_POLICY
	 * @deprecated No longer used.
	 */
	public static final String PROPERTY_FORCE_LINEBREAK_POLICY = JRProperties.PROPERTY_PREFIX + "export.pdf.force.linebreak.policy";
	
	
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
	 * property. This export parameter overrides the property value.
	 *
	 * @see #PROPERTY_FORCE_SVG_SHAPES
	 */
	public static final JRPdfExporterParameter FORCE_SVG_SHAPES = new JRPdfExporterParameter("Force SVG Shapes");


	/**
	 * Property that provides a default value for the {@link #FORCE_SVG_SHAPES FORCE_SVG_SHAPES}
	 * PDF exporter parameter.
	 */
	public static final String PROPERTY_FORCE_SVG_SHAPES = JRProperties.PROPERTY_PREFIX + "export.pdf.force.svg.shapes";

	
	/**
	 * The user defined JavaScript piece of code to be inserted in the generated PDF document
	 */
	public static final JRPdfExporterParameter PDF_JAVASCRIPT = new JRPdfExporterParameter("PDF JavaScript");
	
	
	/**
	 * Property whose value is used as default for the {@link #PDF_JAVASCRIPT PDF_JAVASCRIPT} export parameter.
	 * 
	 * @see JRProperties
	 */
	public static final String PROPERTY_PDF_JAVASCRIPT = JRProperties.PROPERTY_PREFIX + "export.pdf.javascript";


	/**
	 * Parameter specifying the print scaling preference in the PDF print dialog.
	 */
	public static final JRPdfExporterParameter PRINT_SCALING = new JRPdfExporterParameter("Print Scaling");
	 
	
	/**
	 * Property whose value is used as default state of the {@link #PRINT_SCALING PRINT_SCALING} export parameter.
	 * <p/>
	 * By default, this property is set to {@link #PRINT_SCALING_DEFAULT}.
	 *
	 * @see JRProperties
	 */
	public static final String PROPERTY_PRINT_SCALING = JRProperties.PROPERTY_PREFIX + "export.pdf.print.scaling";

	
	/**
	 * Specifies whether the exporter should put structure tags in the generated PDF.
	 */
	public static final JRPdfExporterParameter IS_TAGGED = new JRPdfExporterParameter("Is Tagged");
	
	
	/**
	 * Property whose value is used as default for the {@link #IS_TAGGED} export parameter.
	 * 
	 * @see JRProperties
	 */
	public static final String PROPERTY_TAGGED = JRProperties.PROPERTY_PREFIX + "export.pdf.tagged";


	/**
	 * Specifies the language that the exporter should put in the language tag of the generated PDF.
	 */
	public static final JRPdfExporterParameter TAG_LANGUAGE = new JRPdfExporterParameter("Tag Language");
	
	
	/**
	 * Property whose value is used as default for the {@link #TAG_LANGUAGE} export parameter.
	 * 
	 * @see JRProperties
	 */
	public static final String PROPERTY_TAG_LANGUAGE = JRProperties.PROPERTY_PREFIX + "export.pdf.tag.language";


	/**
	 * Property that determines if missing bookmark levels are collapsed, or if 
	 * empty bookmarks are created for the missing levels.
	 * 
	 * <p>
	 * The property dictates what happens when a filled report contains an anchor
	 * having a bookmark level that is not the immediate successor of its parent
	 * (e.g. an anchor with bookmark level 3 follows immediately after a bookmark
	 * of level 1).
	 * If the property is not set, an empty bookmark is created for the missing
	 * level(s) in order to preserve the original level of the bookmark.
	 * When the property is set, the level of the bookmark will be collapsed and
	 * the bookmark will be created as a direct descendant of its nearest parent.
	 * </p>
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
		JRProperties.PROPERTY_PREFIX + "export.pdf.collapse.missing.bookmark.levels";
	
	
	/**
	 * The Conformance level of the PDF/A document, as java.lang.String.
	 * <p/>
	 * Possible values are:
	 * <ul>
	 * 	<li>{@link #PDFA_CONFORMANCE_NONE}</li>
	 * 	<li>{@link #PDFA_CONFORMANCE_1A}</li>
	 * 	<li>{@link #PDFA_CONFORMANCE_1B}</li>
	 * </ul>
	 */
	public static final JRPdfExporterParameter PDFA_CONFORMANCE = new JRPdfExporterParameter("PDF/A Conformance");
	
	
	/**
	 * Property whose value is used as default for the {@link #PDFA_CONFORMANCE} export parameter.
	 * <p/>
	 * By default, this property is set to {@link #PDFA_CONFORMANCE_NONE}.
	 * 
	 * @see JRProperties
	 */
	public static final String PROPERTY_PDFA_CONFORMANCE = JRProperties.PROPERTY_PREFIX + "export.pdfa.conformance";
	
	
	/**
	 * The path to the ICC profile file for the PDF/A compliant document
	 */
	public static final JRPdfExporterParameter PDFA_ICC_PROFILE_PATH = new JRPdfExporterParameter("PDF/A sRGB profile");
	
	
	/**
	 * Property whose value is used as default for the {@link #PDFA_ICC_PROFILE_PATH} export parameter.
	 */
	public static final String PROPERTY_PDFA_ICC_PROFILE_PATH = JRProperties.PROPERTY_PREFIX + "export.pdfa.icc.profile.path";
	
}
