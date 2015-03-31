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
package net.sf.jasperreports.engine.fill;

import java.sql.Connection;
import java.util.Map;

import net.sf.jasperreports.engine.DefaultJasperReportsContext;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JRPropertiesUtil;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.type.SectionTypeEnum;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public final class JRFiller
{

	public static final String EXCEPTION_MESSAGE_KEY_THREAD_INTERRUPTED = "fill.common.filler.thread.interrupted";
	public static final String EXCEPTION_MESSAGE_KEY_UNKNOWN_REPORT_SECTION_TYPE = "fill.common.filler.unknown.report.section.type";
	
	/**
	 * The default locale used to fill reports.
	 * 
	 * <p>
	 * The property is overridden by the value of the {@link JRParameter#REPORT_LOCALE} parameter.
	 * By default, the system/JVM locale is used.
	 * </p>
	 * 
	 * @see JRParameter#REPORT_LOCALE
	 */
	public static final String PROPERTY_DEFAULT_LOCALE = JRPropertiesUtil.PROPERTY_PREFIX + "default.locale";
	

	/**
	 * The default time zone used to fill reports.
	 * 
	 * <p>
	 * The property is overridden by the value of the {@link JRParameter#REPORT_TIME_ZONE} parameter.
	 * By default, the system/JVM time zone is used.
	 * </p>
	 * 
	 * @see JRParameter#REPORT_TIME_ZONE
	 */
	public static final String PROPERTY_DEFAULT_TIMEZONE = JRPropertiesUtil.PROPERTY_PREFIX + "default.timezone";

	/**
	 *
	 */
	public static JasperPrint fill(
		JasperReportsContext jasperReportsContext,
		JasperReport jasperReport,
		Map<String,Object> parameters,
		Connection conn
		) throws JRException
	{
		ReportFiller filler = createReportFiller(jasperReportsContext, jasperReport);
		
		JasperPrint jasperPrint = null;
		
		try
		{
			jasperPrint = filler.fill(parameters, conn);
		}
		catch(JRFillInterruptedException e)
		{
			throw 
				new JRException(
					EXCEPTION_MESSAGE_KEY_THREAD_INTERRUPTED,
					null,
					e);
		}
		
		return jasperPrint;
	}


	/**
	 *
	 */
	public static JasperPrint fill(
		JasperReportsContext jasperReportsContext,
		JasperReport jasperReport,
		Map<String,Object> parameters,
		JRDataSource dataSource
		) throws JRException
	{
		ReportFiller filler = createReportFiller(jasperReportsContext, jasperReport);
		
		JasperPrint jasperPrint = null;
		
		try
		{
			jasperPrint = filler.fill(parameters, dataSource);
		}
		catch(JRFillInterruptedException e)
		{
			throw 
				new JRException(
					EXCEPTION_MESSAGE_KEY_THREAD_INTERRUPTED,
					null,
					e);
		}
		
		return jasperPrint;
	}
	

	/**
	 * Fills a report.
	 * <p/>
	 * The data source used to fill the report is determined in the following way:
	 * <ul>
	 * 	<li>If a non-null value of the {@link net.sf.jasperreports.engine.JRParameter#REPORT_DATA_SOURCE REPORT_DATA_SOURCE}
	 * has been specified, it will be used as data source.</li>
	 * 	<li>Otherwise, if the report has a query then a data source will be created based on the query and connection
	 * parameter values.</li>
	 * 	<li>Otherwise, the report will be filled without a data source.</li>
	 * </ul>
	 * 
	 * @param jasperReport the report
	 * @param parameters the fill parameters
	 * @return the filled report
	 * @throws JRException
	 */
	public static JasperPrint fill(
		JasperReportsContext jasperReportsContext,
		JasperReport jasperReport, 
		Map<String,Object> parameters
		) throws JRException
	{
		ReportFiller filler = createReportFiller(jasperReportsContext, jasperReport);

		try
		{
			JasperPrint jasperPrint = filler.fill(parameters);

			return jasperPrint;
		}
		catch (JRFillInterruptedException e)
		{
			throw 
				new JRException(
					EXCEPTION_MESSAGE_KEY_THREAD_INTERRUPTED,
					null,
					e);
		}
	}


	/**
	 *
	 */
	//FIXMEBOOK deprecate?
	public static JRBaseFiller createFiller(JasperReportsContext jasperReportsContext, JasperReport jasperReport) throws JRException
	{
		return createBandReportFiller(jasperReportsContext, jasperReport);
	}

	protected static JRBaseFiller createBandReportFiller(JasperReportsContext jasperReportsContext, JasperReport jasperReport) throws JRException
	{
		JRBaseFiller filler = null;

		switch (jasperReport.getPrintOrderValue())
		{
			case HORIZONTAL :
			{
				filler = new JRHorizontalFiller(jasperReportsContext, jasperReport);
				break;
			}
			case VERTICAL :
			{
				filler = new JRVerticalFiller(jasperReportsContext, jasperReport);
				break;
			}
		}
		return filler;
	}
	
	public static ReportFiller createReportFiller(JasperReportsContext jasperReportsContext, JasperReport jasperReport) throws JRException
	{
		ReportFiller filler;
		SectionTypeEnum sectionType = jasperReport.getSectionType();
		sectionType = sectionType == null ? SectionTypeEnum.BAND : sectionType;
		switch (sectionType)
		{
		case BAND:
			filler = createBandReportFiller(jasperReportsContext, jasperReport);
			break;
		case PART:
		{
			filler = new PartReportFiller(jasperReportsContext, jasperReport);
			break;
		}
		default:
			throw 
				new JRRuntimeException(
					EXCEPTION_MESSAGE_KEY_UNKNOWN_REPORT_SECTION_TYPE,  
					new Object[]{jasperReport.getSectionType()} 
					);
		}
		return filler;
	}
	
	
	/**
	 * @deprecated Replaced by {@link #fill(JasperReportsContext, JasperReport, Map, Connection)}.
	 */
	public static JasperPrint fillReport(
		JasperReport jasperReport,
		Map<String,Object> parameters,
		Connection conn
		) throws JRException
	{
		return fill(DefaultJasperReportsContext.getInstance(), jasperReport, parameters, conn);
	}


	/**
	 * @deprecated Replaced by {@link #fill(JasperReportsContext, JasperReport, Map, JRDataSource)}.
	 */
	public static JasperPrint fillReport(
		JasperReport jasperReport,
		Map<String,Object> parameters,
		JRDataSource dataSource
		) throws JRException
	{
		return fill(DefaultJasperReportsContext.getInstance(), jasperReport, parameters, dataSource);
	}
	

	/**
	 * @deprecated Replaced by {@link #fill(JasperReportsContext, JasperReport, Map)}.
	 */
	public static JasperPrint fillReport(JasperReport jasperReport, Map<String,Object> parameters) throws JRException
	{
		return fill(DefaultJasperReportsContext.getInstance(), jasperReport, parameters);
	}


	/**
	 * @deprecated Replaced by {@link #createFiller(JasperReportsContext, JasperReport)}.
	 */
	public static JRBaseFiller createFiller(JasperReport jasperReport) throws JRException
	{
		return createFiller(DefaultJasperReportsContext.getInstance(), jasperReport);
	}
	
	
	private JRFiller()
	{
	}
}
