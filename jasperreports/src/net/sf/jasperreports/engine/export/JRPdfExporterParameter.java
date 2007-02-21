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
package net.sf.jasperreports.engine.export;

import net.sf.jasperreports.engine.JRExporterParameter;


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


	public static Character PDF_VERSION_1_2 =  new Character('2');  // Not using iText constants in order not to depend on the library version
	public static Character PDF_VERSION_1_3 = new Character('3');
	public static Character PDF_VERSION_1_4 = new Character('4');
	public static Character PDF_VERSION_1_5 = new Character('5');
	public static Character PDF_VERSION_1_6 = new Character('6');

	
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
	 * A boolean value specifying whether the PDF document should be compressed.
	 */
	public static final JRPdfExporterParameter IS_COMPRESSED = new JRPdfExporterParameter("Is Compressed");

	/**
	 * A boolean value specifying whether the final PDF document should be encrypted.
	 */
	public static final JRPdfExporterParameter IS_ENCRYPTED = new JRPdfExporterParameter("Is Encrypted");

	/**
	 * A boolean value specifying whether the encryption key is 128 bits.
	 */
	public static final JRPdfExporterParameter IS_128_BIT_KEY = new JRPdfExporterParameter("Is 128 Bit Key");


	/**
	 * The user password needed to open the document, if it is encrypted.
	 */
	public static final JRPdfExporterParameter USER_PASSWORD = new JRPdfExporterParameter("User Password");


	/**
	 * The password belonging to the owner of the document, if it is encrypted. If the password is null, it will be replaced
	 * by a random string.
	 */
	public static final JRPdfExporterParameter OWNER_PASSWORD = new JRPdfExporterParameter("Owner Password");


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
     * {@link net.sf.jasperreports.engine.util.JRProperties#PDF_FORCE_LINEBREAK_POLICY PDF_FORCE_LINEBREAK_POLICY} property.
     * This export parameter overrides the property value.
     * 
     * @see net.sf.jasperreports.engine.util.JRProperties#PDF_FORCE_LINEBREAK_POLICY
     * @see net.sf.jasperreports.engine.util.BreakIteratorSplitCharacter
     */
    public static final JRPdfExporterParameter FORCE_LINEBREAK_POLICY = new JRPdfExporterParameter("Force linebreak policy");
    

    /**
     * Flag to force the rendering of SVG images using shapes, on the PDF Graphics2D context.
     * 
     * This allows rendering fonts as shapes and avoid font mapping issues that might cause Unicode 
     * text not to show up properly, but has the disadvantage of producing larger PDF files.
     * By default, the flag is set to true, mainly due to backward compatibility reasons.
     * 
     * To reduce PDF file size for documents containing SVG images such as charts, the flag should be 
     * turned to false, and the PDF exporter font mappings should be correctly configured using the 
     * {@link JRExporterParameter#FONT_MAP FONT_MAP} exporter parameter to ensure proper rendering of 
     * text in the SVG.
     * 
     * This flag can be set system-wide using the
     * {@link net.sf.jasperreports.engine.export.JRPdfExporter#PDF_FORCE_SVG_SHAPES PDF_FORCE_SVG_SHAPES} property.
     * This export parameter overrides the property value.
     * 
     * @see net.sf.jasperreports.engine.export.JRPdfExporter#PDF_FORCE_SVG_SHAPES
     * @see net.sf.jasperreports.engine.JRExporterParameter#FONT_MAP
     */
    public static final JRPdfExporterParameter FORCE_SVG_SHAPES = new JRPdfExporterParameter("Force SVG Shapes");
    

	/**
	 * The user defined JavaScript piece of code to be inserted in the generated PDF document 
	 */
	public static final JRPdfExporterParameter PDF_JAVASCRIPT = new JRPdfExporterParameter("PDF JavaScript");
}
