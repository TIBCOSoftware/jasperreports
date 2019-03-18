/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2019 TIBCO Software Inc. All rights reserved.
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

import net.sf.jasperreports.export.PdfExporterConfiguration;
import net.sf.jasperreports.export.PdfReportConfiguration;


/**
 * Contains parameters useful for export in PDF format.
 * <p>
 * The PDF exporter can send data to an output stream or a file on disk. The engine looks among the export parameters in
 * order to find the selected output type in this order: OUTPUT_STREAM, OUTPUT_FILE, OUTPUT_FILE_NAME.
 * <p>
 *
 * @deprecated Replaced by {@link PdfExporterConfiguration}.
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public class JRPdfExporterParameter extends net.sf.jasperreports.engine.JRExporterParameter
{
	/**
	 *
	 */
	protected JRPdfExporterParameter(String name)
	{
		super(name);
	}

	
	/**
	 * @deprecated Replaced by {@link PdfExporterConfiguration#isCreatingBatchModeBookmarks()}.
	 */
	public static final JRPdfExporterParameter IS_CREATING_BATCH_MODE_BOOKMARKS = new JRPdfExporterParameter("Is Creating Batch Mode Bookmarks");

	
	/**
	 * @deprecated Replaced by {@link PdfExporterConfiguration#isCompressed()}.
	 */
	public static final JRPdfExporterParameter IS_COMPRESSED = new JRPdfExporterParameter("Is Compressed");


	/**
	 * @deprecated Replaced by {@link PdfExporterConfiguration#isEncrypted()}.
	 */
	public static final JRPdfExporterParameter IS_ENCRYPTED = new JRPdfExporterParameter("Is Encrypted");


	/**
	 * @deprecated Replaced by {@link PdfExporterConfiguration#is128BitKey()}.
	 */
	public static final JRPdfExporterParameter IS_128_BIT_KEY = new JRPdfExporterParameter("Is 128 Bit Key");


	/**
	 * @deprecated Replaced by {@link PdfExporterConfiguration#getUserPassword()}.
	 */
	public static final JRPdfExporterParameter USER_PASSWORD = new JRPdfExporterParameter("User Password");


	/**
	 * @deprecated Replaced by {@link PdfExporterConfiguration#getOwnerPassword()}.
	 */
	public static final JRPdfExporterParameter OWNER_PASSWORD = new JRPdfExporterParameter("Owner Password");


	/**
	 * @deprecated Replaced by {@link PdfExporterConfiguration#getPermissions()}.
	 */
	public static final JRPdfExporterParameter PERMISSIONS = new JRPdfExporterParameter("Permissions");


	/**
	 * @deprecated Replaced by {@link PdfExporterConfiguration#getPdfVersion()}.
	 */
	public static final JRPdfExporterParameter PDF_VERSION = new JRPdfExporterParameter("PDF Version");


	/**
	 * @deprecated Replaced by {@link PdfExporterConfiguration#getMetadataTitle()}.
	 */
	public static final JRPdfExporterParameter METADATA_TITLE = new JRPdfExporterParameter("Metadata Title");

	/**
	 * @deprecated Replaced by {@link PdfExporterConfiguration#getMetadataAuthor()}.
	 */
	public static final JRPdfExporterParameter METADATA_AUTHOR = new JRPdfExporterParameter("Metadata Author");

	/**
	 * @deprecated Replaced by {@link PdfExporterConfiguration#getMetadataSubject()}.
	 */
	public static final JRPdfExporterParameter METADATA_SUBJECT = new JRPdfExporterParameter("Metadata Subject");

	/**
	 * @deprecated Replaced by {@link PdfExporterConfiguration#getMetadataKeywords()}.
	 */
	public static final JRPdfExporterParameter METADATA_KEYWORDS = new JRPdfExporterParameter("Metadata Keywords");

	/**
	 * @deprecated Replaced by {@link PdfExporterConfiguration#getMetadataCreator()}.
	 */
	public static final JRPdfExporterParameter METADATA_CREATOR = new JRPdfExporterParameter("Metadata Creator");


	/**
	 * @deprecated Replaced by {@link PdfReportConfiguration#isForceLineBreakPolicy()}.
	 */
	public static final JRPdfExporterParameter FORCE_LINEBREAK_POLICY = new JRPdfExporterParameter("Force linebreak policy");


	/**
	 * @deprecated Replaced by {@link PdfReportConfiguration#isForceSvgShapes()}.
	 */
	public static final JRPdfExporterParameter FORCE_SVG_SHAPES = new JRPdfExporterParameter("Force SVG Shapes");


	/**
	 * @deprecated Replaced by {@link PdfExporterConfiguration#getPdfJavaScript()}.
	 */
	public static final JRPdfExporterParameter PDF_JAVASCRIPT = new JRPdfExporterParameter("PDF JavaScript");
	
	
	/**
	 * @deprecated Replaced by {@link PdfExporterConfiguration#getPrintScaling()}.
	 */
	public static final JRPdfExporterParameter PRINT_SCALING = new JRPdfExporterParameter("Print Scaling");
	 
	
	/**
	 * @deprecated Replaced by {@link PdfExporterConfiguration#isTagged()}.
	 */
	public static final JRPdfExporterParameter IS_TAGGED = new JRPdfExporterParameter("Is Tagged");
	
	
	/**
	 * @deprecated Replaced by {@link PdfExporterConfiguration#getTagLanguage()}.
	 */
	public static final JRPdfExporterParameter TAG_LANGUAGE = new JRPdfExporterParameter("Tag Language");
	
	
	/**
	 * @deprecated Replaced by {@link PdfExporterConfiguration#getPdfaConformance()}.
	 */
	public static final JRPdfExporterParameter PDFA_CONFORMANCE = new JRPdfExporterParameter("PDF/A Conformance");
	
	
	/**
	 * @deprecated Replaced by {@link PdfExporterConfiguration#getIccProfilePath()}.
	 */
	public static final JRPdfExporterParameter PDFA_ICC_PROFILE_PATH = new JRPdfExporterParameter("PDF/A sRGB profile");
	
}
