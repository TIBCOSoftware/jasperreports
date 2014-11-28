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
package net.sf.jasperreports.export;

import net.sf.jasperreports.engine.JRPropertiesUtil;
import net.sf.jasperreports.engine.export.JRXlsAbstractExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsMetadataExporter;
import net.sf.jasperreports.engine.export.oasis.JROdsExporter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.annotations.ExporterParameter;
import net.sf.jasperreports.export.annotations.ExporterProperty;


/**
 * Interface containing settings used by the Excel exporters.
 *
 * @see JRXlsExporter
 * @see JRXlsxExporter
 * @see JROdsExporter
 * @see JRXlsMetadataExporter
 * 
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public interface XlsExporterConfiguration extends ExporterConfiguration
{
	/**
	 * Property whose value is used as default state of the {@link #isCreateCustomPalette()} export configuration flag.
	 * <p/>
	 * This property is by default not set (<code>false</code>).
	 * 
	 * @see JRPropertiesUtil
	 */
	public static final String PROPERTY_CREATE_CUSTOM_PALETTE = JRPropertiesUtil.PROPERTY_PREFIX + "export.xls.create.custom.palette";

	/**
	 * Property that provides a default value for the {@link #getWorkbookTemplate()} export configuration setting.
	 * 
	 * @see JRPropertiesUtil
	 * @since 4.5.1
	 */
	public static final String PROPERTY_WORKBOOK_TEMPLATE = JRXlsAbstractExporter.XLS_EXPORTER_PROPERTIES_PREFIX + "workbook.template";

	/**
	 * Property that provides a default value for the {@link #isKeepWorkbookTemplateSheets()} export configuration flag.
	 * <p>
	 * This property is used in conjunction with {@link #PROPERTY_WORKBOOK_TEMPLATE}.
	 * <p>
	 * Default value is <code>false</code>.
	 * 
	 * @see JRPropertiesUtil
	 * @since 4.5.1
	 */
	public static final String PROPERTY_WORKBOOK_TEMPLATE_KEEP_SHEETS = JRXlsAbstractExporter.XLS_EXPORTER_PROPERTIES_PREFIX + "workbook.template.keep.sheets";

	/**
	 * Returns a boolean value specifying whether the standard color palette should be customized
	 * so that the XLS result uses the original report colors.
	 * <p/>
	 * The default state of this flag is given by the
	 * {@link #PROPERTY_CREATE_CUSTOM_PALETTE net.sf.jasperreports.export.xls.create.custom.palette} property.
	 * <p/>
	 * The colors used in the result XLS are determined in the following manner:
	 * <ol>
	 * 	<li>
	 * If this flag is not set, the nearest color from the standard XLS palette is chosen
	 * for a report color.
	 * 	</li>
	 * 	<li>
	 * If the flag is set, the nearest not yet modified color from the palette is chosen
	 * and modified to exactly match the report color.  If all the colors from the palette
	 * are modified (the palette has a fixed size), the nearest color from the palette is
	 * chosen for further report colors.
	 * 	</li>
	 * </ol> 
	 * 
	 * @see #PROPERTY_CREATE_CUSTOM_PALETTE
	 */
	@SuppressWarnings("deprecation")
	@ExporterParameter(
		type=net.sf.jasperreports.engine.export.JRXlsAbstractExporterParameter.class,
		name="CREATE_CUSTOM_PALETTE"
		)
	@ExporterProperty(
		value=PROPERTY_CREATE_CUSTOM_PALETTE, 
		booleanDefault=false
		)
	public Boolean isCreateCustomPalette();

	/**
	 * Setting that specifies the location of an existing workbook template. The content of an existing workbook document 
	 * or template can be embedded into exported document if the template location is known. In this case the content of 
	 * the template will be exported first and the content of the exported report will be appended to this one. Macros and 
	 * other settings in the existing template will be also preserved in the generated document. Templates can be loaded from 
	 * Excel template files (*.xlt) as well as from valid Excel documents (*.xls).
	 * <p>
	 * This setting is used in Excel exporters based either on Apache POI APIs ({@link JRXlsExporter}) or on JExcelApi library 
	 * ({@link net.sf.jasperreports.engine.export.JExcelApiExporter JExcelApiExporter}). There's no similar property for the {@link JRXlsxExporter}.
	 * @see #PROPERTY_WORKBOOK_TEMPLATE
	 */
	@ExporterProperty(PROPERTY_WORKBOOK_TEMPLATE)
	public String getWorkbookTemplate();
	
	/**
	 * Flag that specifies whether to keep the sheets of the existing template into generated document. Sometimes 
	 * is important to embed in a generated document only macros and/or other global settings from an existing template, but 
	 * without keeping the own sheets of the template document. If set to false, this property prevent the template sheets 
	 * to be exported.
	 * <p>
	 * This setting is used in conjunction with {@link #getWorkbookTemplate()}.
	 * 
	 * @see #PROPERTY_WORKBOOK_TEMPLATE_KEEP_SHEETS
	 */
	@ExporterProperty(
		value=PROPERTY_WORKBOOK_TEMPLATE_KEEP_SHEETS, 
		booleanDefault=false
		)
	public Boolean isKeepWorkbookTemplateSheets();
}
