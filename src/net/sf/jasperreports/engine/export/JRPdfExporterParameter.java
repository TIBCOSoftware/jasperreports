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
import com.lowagie.text.pdf.*;


/**
 * Contains parameters useful for export in PDF format.
 * <p>
 * The HTML exporter can send data to an output stream or a file on disk. The engine looks among the export parameters in
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
}
