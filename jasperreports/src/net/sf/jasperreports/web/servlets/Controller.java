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
package net.sf.jasperreports.web.servlets;

import net.sf.jasperreports.data.cache.ColumnDataCacheHandler;
import net.sf.jasperreports.data.cache.DataCacheHandler;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.fill.AsynchronousFillHandle;
import net.sf.jasperreports.repo.RepositoryUtil;
import net.sf.jasperreports.web.WebReportContext;
import net.sf.jasperreports.web.actions.Action;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id: ReportServlet.java 4928 2012-01-23 10:24:41Z teodord $
 */
public class Controller
{
	private static final Log log = LogFactory.getLog(Controller.class);

	public static final String REQUEST_PARAMETER_REPORT_URI = "jr.uri";
	public static final String REQUEST_PARAMETER_ASYNC = "jr.async";

	/**
	 *
	 */
	private JasperReportsContext jasperReportsContext;

	
	/**
	 *
	 */
	public Controller(JasperReportsContext jasperReportsContext)
	{
		this.jasperReportsContext = jasperReportsContext;
	}
	
	
	/**
	 *
	 */
	public void runReport(
		WebReportContext webReportContext,
		Action action
		) throws JRException
	{
		String reportUri = (String)webReportContext.getParameterValue(REQUEST_PARAMETER_REPORT_URI);

		setDataCache(webReportContext);
		
		JasperReport jasperReport = null; 
		
		if (reportUri != null && reportUri.trim().length() > 0)
		{
			reportUri = reportUri.trim();

			if (action != null) 
			{
				action.run();
			}

			jasperReport = RepositoryUtil.getInstance(jasperReportsContext).getReport(webReportContext, reportUri);
		}
		
		if (jasperReport == null)
		{
			throw new JRException("Report not found at : " + reportUri);
		}
		
		Boolean async = (Boolean)webReportContext.getParameterValue(REQUEST_PARAMETER_ASYNC);
		if (async == null)
		{
			async = Boolean.FALSE;
		}
		webReportContext.setParameterValue(REQUEST_PARAMETER_ASYNC, async);
		
		runReport(webReportContext, jasperReport, async.booleanValue());
	}


	protected void setDataCache(WebReportContext webReportContext)
	{
		DataCacheHandler dataCacheHandler = (DataCacheHandler) webReportContext.getParameterValue(
				DataCacheHandler.PARAMETER_DATA_CACHE_HANDLER);
		if (dataCacheHandler != null && !dataCacheHandler.isSnapshotPopulated())
		{
			// if we have an old cache handler which is not yet final, create a new one
			// TODO lucianc also check for final but disabled caches
			
			if (log.isDebugEnabled())
			{
				log.debug("Data cache handler not final " + dataCacheHandler);
			}
			
			dataCacheHandler = null;
		}
		
		if (dataCacheHandler == null)
		{
			//initialize the data cache handler
			dataCacheHandler = new ColumnDataCacheHandler();
			
			if (log.isDebugEnabled())
			{
				log.debug("Created data cache handler " + dataCacheHandler);
			}
			
			webReportContext.setParameterValue(
					DataCacheHandler.PARAMETER_DATA_CACHE_HANDLER, dataCacheHandler);
		}
	}


	/**
	 *
	 */
	protected void runReport(
		WebReportContext webReportContext,
		JasperReport jasperReport, 
		boolean async
		) throws JRException
	{
		JasperPrintAccessor accessor;
		if (async)
		{
			AsynchronousFillHandle fillHandle = 
				AsynchronousFillHandle.createHandle(
					jasperReportsContext,
					jasperReport, 
					webReportContext.getParameterValues()
					);
			AsyncJasperPrintAccessor asyncAccessor = new AsyncJasperPrintAccessor(fillHandle);
			
			fillHandle.startFill();
			
			accessor = asyncAccessor;
		}
		else
		{
			JasperPrint jasperPrint = 
					JasperFillManager.getInstance(jasperReportsContext).fill(
						jasperReport, 
						webReportContext.getParameterValues()
						);
			accessor = new SimpleJasperPrintAccessor(jasperPrint);
		}
		
		webReportContext.setParameterValue(WebReportContext.REPORT_CONTEXT_PARAMETER_JASPER_PRINT_ACCESSOR, accessor);
	}
}
