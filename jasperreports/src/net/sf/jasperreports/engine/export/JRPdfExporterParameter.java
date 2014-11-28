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
import net.sf.jasperreports.engine.JRPropertiesUtil;
import net.sf.jasperreports.export.PdfExporterConfiguration;
import net.sf.jasperreports.export.PdfReportConfiguration;
import net.sf.jasperreports.export.type.PdfPrintScalingEnum;
import net.sf.jasperreports.export.type.PdfVersionEnum;
import net.sf.jasperreports.export.type.PdfaConformanceEnum;


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
public class JRPdfExporterParameter extends JRExporterParameter
{
	/**
	 * @deprecated Replaced by {@link PdfVersionEnum#VERSION_1_2}.
	 */
	public static final Character PDF_VERSION_1_2 = PdfVersionEnum.VERSION_1_2.getName().charAt(0);
	/**
	 * @deprecated Replaced by {@link PdfVersionEnum#VERSION_1_3}.
	 */
	public static final Character PDF_VERSION_1_3 = PdfVersionEnum.VERSION_1_3.getName().charAt(0);
	/**
	 * @deprecated Replaced by {@link PdfVersionEnum#VERSION_1_4}.
	 */
	public static final Character PDF_VERSION_1_4 = PdfVersionEnum.VERSION_1_4.getName().charAt(0);
	/**
	 * @deprecated Replaced by {@link PdfVersionEnum#VERSION_1_5}.
	 */
	public static final Character PDF_VERSION_1_5 = PdfVersionEnum.VERSION_1_5.getName().charAt(0);
	/**
	 * @deprecated Replaced by {@link PdfVersionEnum#VERSION_1_6}.
	 */
	public static final Character PDF_VERSION_1_6 = PdfVersionEnum.VERSION_1_6.getName().charAt(0);
	/**
	 * @deprecated Replaced by {@link PdfVersionEnum#VERSION_1_7}.
	 */
	public static final Character PDF_VERSION_1_7 = PdfVersionEnum.VERSION_1_7.getName().charAt(0);

	/**
	 * @deprecated Replaced by {@link PdfPrintScalingEnum#DEFAULT}.
	 */
	public static final String PRINT_SCALING_DEFAULT = PdfPrintScalingEnum.DEFAULT.getName();
	/**
	 * @deprecated Replaced by {@link PdfPrintScalingEnum#NONE}.
	 */
	public static final String PRINT_SCALING_NONE = PdfPrintScalingEnum.NONE.getName();
	
	/**
	 * @deprecated Replaced by {@link PdfaConformanceEnum#PDFA_1A}.
	 */
	public static final String PDFA_CONFORMANCE_1A = PdfaConformanceEnum.PDFA_1A.getName();
	/**
	 * @deprecated Replaced by {@link PdfaConformanceEnum#PDFA_1B}.
	 */
	public static final String PDFA_CONFORMANCE_1B = PdfaConformanceEnum.PDFA_1B.getName();
	/**
	 * @deprecated Replaced by {@link PdfaConformanceEnum#NONE}.
	 */
	public static final String PDFA_CONFORMANCE_NONE = PdfaConformanceEnum.NONE.getName();

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
	 * @deprecated Replaced by {@link PdfExporterConfiguration#PROPERTY_CREATE_BATCH_MODE_BOOKMARKS}.
	 */
	public static final String PROPERTY_CREATE_BATCH_MODE_BOOKMARKS = PdfExporterConfiguration.PROPERTY_CREATE_BATCH_MODE_BOOKMARKS;

	
	/**
	 * @deprecated Replaced by {@link PdfExporterConfiguration#isCompressed()}.
	 */
	public static final JRPdfExporterParameter IS_COMPRESSED = new JRPdfExporterParameter("Is Compressed");

	
	/**
	 * @deprecated Replaced by {@link PdfExporterConfiguration#PROPERTY_COMPRESSED}.
	 */
	public static final String PROPERTY_COMPRESSED = PdfExporterConfiguration.PROPERTY_COMPRESSED;


	/**
	 * @deprecated Replaced by {@link PdfExporterConfiguration#isEncrypted()}.
	 */
	public static final JRPdfExporterParameter IS_ENCRYPTED = new JRPdfExporterParameter("Is Encrypted");

	
	/**
	 * @deprecated Replaced by {@link PdfExporterConfiguration#PROPERTY_ENCRYPTED}.
	 */
	public static final String PROPERTY_ENCRYPTED = PdfExporterConfiguration.PROPERTY_ENCRYPTED;


	/**
	 * @deprecated Replaced by {@link PdfExporterConfiguration#is128BitKey()}.
	 */
	public static final JRPdfExporterParameter IS_128_BIT_KEY = new JRPdfExporterParameter("Is 128 Bit Key");


	/**
	 * @deprecated Replaced by {@link PdfExporterConfiguration#PROPERTY_128_BIT_KEY}.
	 */
	public static final String PROPERTY_128_BIT_KEY = PdfExporterConfiguration.PROPERTY_128_BIT_KEY;


	/**
	 * @deprecated Replaced by {@link PdfExporterConfiguration#getUserPassword()}.
	 */
	public static final JRPdfExporterParameter USER_PASSWORD = new JRPdfExporterParameter("User Password");


	/**
	 * @deprecated Replaced by {@link PdfExporterConfiguration#PROPERTY_USER_PASSWORD}.
	 */
	public static final String PROPERTY_USER_PASSWORD = PdfExporterConfiguration.PROPERTY_USER_PASSWORD;


	/**
	 * @deprecated Replaced by {@link PdfExporterConfiguration#getOwnerPassword()}.
	 */
	public static final JRPdfExporterParameter OWNER_PASSWORD = new JRPdfExporterParameter("Owner Password");


	/**
	 * @deprecated Replaced by {@link PdfExporterConfiguration#PROPERTY_OWNER_PASSWORD}.
	 */
	public static final String PROPERTY_OWNER_PASSWORD = PdfExporterConfiguration.PROPERTY_OWNER_PASSWORD;


	/**
	 * @deprecated Replaced by {@link PdfExporterConfiguration#getPermissions()}.
	 */
	public static final JRPdfExporterParameter PERMISSIONS = new JRPdfExporterParameter("Permissions");


	/**
	 * @deprecated Replaced by {@link PdfExporterConfiguration#getPdfVersion()}.
	 */
	public static final JRPdfExporterParameter PDF_VERSION = new JRPdfExporterParameter("PDF Version");


	/**
	 * @deprecated Replaced by {@link PdfExporterConfiguration#PROPERTY_PDF_VERSION}.
	 */
	public static final String PROPERTY_PDF_VERSION = PdfExporterConfiguration.PROPERTY_PDF_VERSION;


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
	 * @deprecated Replaced by {@link PdfReportConfiguration#PROPERTY_FORCE_LINEBREAK_POLICY}.
	 */
	public static final String PROPERTY_FORCE_LINEBREAK_POLICY = JRPropertiesUtil.PROPERTY_PREFIX + "export.pdf.force.linebreak.policy";
	
	
	/**
	 * @deprecated Replaced by {@link PdfReportConfiguration#isForceSvgShapes()}.
	 */
	public static final JRPdfExporterParameter FORCE_SVG_SHAPES = new JRPdfExporterParameter("Force SVG Shapes");


	/**
	 * @deprecated Replaced by {@link PdfReportConfiguration#PROPERTY_FORCE_SVG_SHAPES}.
	 */
	public static final String PROPERTY_FORCE_SVG_SHAPES = PdfReportConfiguration.PROPERTY_FORCE_SVG_SHAPES;

	
	/**
	 * @deprecated Replaced by {@link PdfExporterConfiguration#getPdfJavaScript()}.
	 */
	public static final JRPdfExporterParameter PDF_JAVASCRIPT = new JRPdfExporterParameter("PDF JavaScript");
	
	
	/**
	 *@deprecated Replaced by {@link PdfExporterConfiguration#PROPERTY_PDF_JAVASCRIPT}.
	 */
	public static final String PROPERTY_PDF_JAVASCRIPT = PdfExporterConfiguration.PROPERTY_PDF_JAVASCRIPT;


	/**
	 * @deprecated Replaced by {@link PdfExporterConfiguration#getPrintScaling()}.
	 */
	public static final JRPdfExporterParameter PRINT_SCALING = new JRPdfExporterParameter("Print Scaling");
	 
	
	/**
	 * @deprecated Replaced by {@link PdfExporterConfiguration#PROPERTY_PRINT_SCALING}.
	 */
	public static final String PROPERTY_PRINT_SCALING = PdfExporterConfiguration.PROPERTY_PRINT_SCALING;

	
	/**
	 * @deprecated Replaced by {@link PdfExporterConfiguration#isTagged()}.
	 */
	public static final JRPdfExporterParameter IS_TAGGED = new JRPdfExporterParameter("Is Tagged");
	
	
	/**
	 * @deprecated Replaced by {@link PdfExporterConfiguration#PROPERTY_TAGGED}.
	 */
	public static final String PROPERTY_TAGGED = PdfExporterConfiguration.PROPERTY_TAGGED;


	/**
	 * @deprecated Replaced by {@link PdfExporterConfiguration#getTagLanguage()}.
	 */
	public static final JRPdfExporterParameter TAG_LANGUAGE = new JRPdfExporterParameter("Tag Language");
	
	
	/**
	 * @deprecated Replaced by {@link PdfExporterConfiguration#PROPERTY_TAG_LANGUAGE}.
	 */
	public static final String PROPERTY_TAG_LANGUAGE = PdfExporterConfiguration.PROPERTY_TAG_LANGUAGE;


	/**
	 * @deprecated Replaced by {@link PdfReportConfiguration#PROPERTY_COLLAPSE_MISSING_BOOKMARK_LEVELS}.
	 */
	public static final String PROPERTY_COLLAPSE_MISSING_BOOKMARK_LEVELS = PdfReportConfiguration.PROPERTY_COLLAPSE_MISSING_BOOKMARK_LEVELS; 
	
	
	/**
	 * @deprecated Replaced by {@link PdfReportConfiguration#PROPERTY_SIZE_PAGE_TO_CONTENT}.
	 */
	public static final String PROPERTY_SIZE_PAGE_TO_CONTENT = PdfReportConfiguration.PROPERTY_SIZE_PAGE_TO_CONTENT;

	
	/**
	 * @deprecated Replaced by {@link PdfExporterConfiguration#getPdfaConformance()}.
	 */
	public static final JRPdfExporterParameter PDFA_CONFORMANCE = new JRPdfExporterParameter("PDF/A Conformance");
	
	
	/**
	 * @deprecated Replaced by {@link PdfExporterConfiguration#PROPERTY_PDFA_CONFORMANCE}.
	 */
	public static final String PROPERTY_PDFA_CONFORMANCE = PdfExporterConfiguration.PROPERTY_PDFA_CONFORMANCE;
	
	
	/**
	 * @deprecated Replaced by {@link PdfExporterConfiguration#getIccProfilePath()}.
	 */
	public static final JRPdfExporterParameter PDFA_ICC_PROFILE_PATH = new JRPdfExporterParameter("PDF/A sRGB profile");
	
	
	/**
	 * @deprecated Replaced by {@link PdfExporterConfiguration#PROPERTY_PDFA_ICC_PROFILE_PATH}.
	 */
	public static final String PROPERTY_PDFA_ICC_PROFILE_PATH = PdfExporterConfiguration.PROPERTY_PDFA_ICC_PROFILE_PATH;
	
}
