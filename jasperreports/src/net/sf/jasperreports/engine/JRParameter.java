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
package net.sf.jasperreports.engine;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public interface JRParameter extends JRPropertiesHolder
{


	/**
	 * A <tt>Map</tt> containing report parameters passed by users at fill time.
	 */
	public static final String REPORT_PARAMETERS_MAP = "REPORT_PARAMETERS_MAP";


	/**
	 * A <tt>java.sql.Connection</tt> needed to run the default report query.
	 */
	public static final String REPORT_CONNECTION = "REPORT_CONNECTION";


	/**
	 * An integer allowing users to limit the datasource size.
	 */
	public static final String REPORT_MAX_COUNT = "REPORT_MAX_COUNT";


	/**
	 * A {@link JRDataSource} instance representing the report data source. JasperReports defines some convenience implementations
	 * of <tt>JRDataSource</tt>, but users may create their own data sources for specific needs.
	 */
	public static final String REPORT_DATA_SOURCE = "REPORT_DATA_SOURCE";


	/**
	 * A {@link JRAbstractScriptlet} containing an instance of the report scriptlet provided by the user.
	 */
	public static final String REPORT_SCRIPTLET = "REPORT_SCRIPTLET";


	/**
	 * A <tt>java.util.Locale</tt> instance containing the resource bundle desired locale. This parameter should be used in
	 * conjunction with REPORT_RESOURCE_BUNDLE.
	 */
	public static final String REPORT_LOCALE = "REPORT_LOCALE";


	/**
	 * The <tt>java.util.ResourceBundle</tt> containing localized messages. If the resource bundle base name is specified at
	 * design time, the engine will try to load the resource bundle using specified name and locale.
	 */
	public static final String REPORT_RESOURCE_BUNDLE = "REPORT_RESOURCE_BUNDLE";
	

	/**
	 * A <tt>java.util.TimeZone</tt> instance to use for date formatting.
	 */
	public static final String REPORT_TIME_ZONE = "REPORT_TIME_ZONE";


	/**
	 * The {@link JRVirtualizer JRVirtualizer} to be used for page virtualization.  This parameter is optional.
	 */
	public static final String REPORT_VIRTUALIZER = "REPORT_VIRTUALIZER";

	
	/**
	 * A <tt>java.lang.ClassLoader</tt> instance to be used during the report filling process to load resources such 
	 * as images, fonts and subreport templates.
	 */
	public static final String REPORT_CLASS_LOADER = "REPORT_CLASS_LOADER";

	
	/**
	 * A <tt>java.net.URLStreamHandlerFactory</tt> instance to be used during the report filling process to 
	 * handler custom URL protocols for loading resources such as images, fonts and subreport templates.
	 */
	public static final String REPORT_URL_HANDLER_FACTORY = "REPORT_URL_HANDLER_FACTORY";


	/**
	 * A {@link net.sf.jasperreports.engine.util.FormatFactory FormatFactory} instance to be used 
	 * during the report filling process to create instances of <tt>java.text.DateFormat<tt> to format date text
	 * fields and instances of <tt>java.text.NumberFormat<tt> to format numeric text fields.
	 */
	public static final String REPORT_FORMAT_FACTORY = "REPORT_FORMAT_FACTORY";

	
	/**
	 * Whether to use pagination.
	 * <b>
	 * If set to <code>true</code> the report will be generated on one long page.
	 */
	public static final String IS_IGNORE_PAGINATION = "IS_IGNORE_PAGINATION";

	
	/**
	 * A {@link java.util.Collection collection} of {@link JRTemplate templates} passed to the
	 * report at fill time.
	 * <p/>
	 * These templates add to the ones specified in the report (see {@link JRReport#getTemplates()}).
	 * In the final templates list they are placed after the report templates; therefore styles from
	 * these templates can use and override styles in the report templates.
	 * They are, however, placed before the report styles hence report styles can use and override
	 * styles from these templates.
	 */
	public static final String REPORT_TEMPLATES = "REPORT_TEMPLATES";

	/**
	 *
	 */
	public String getName();
		
	/**
	 *
	 */
	public String getDescription();
		
	/**
	 *
	 */
	public void setDescription(String description);
		
	/**
	 *
	 */
	public Class getValueClass();

	/**
	 *
	 */
	public String getValueClassName();

	/**
	 *
	 */
	public boolean isSystemDefined();

	/**
	 *
	 */
	public boolean isForPrompting();

	/**
	 *
	 */
	public JRExpression getDefaultValueExpression();


}
