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
package net.sf.jasperreports.web.servlets;

import net.sf.jasperreports.data.cache.ColumnDataCacheHandler;
import net.sf.jasperreports.data.cache.DataCacheHandler;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.fill.AsynchronousFillHandle;
import net.sf.jasperreports.repo.RepositoryUtil;
import net.sf.jasperreports.web.JRInteractiveException;
import net.sf.jasperreports.web.WebReportContext;
import net.sf.jasperreports.web.actions.AbstractAction;
import net.sf.jasperreports.web.actions.Action;
import net.sf.jasperreports.web.commands.CommandStack;
import net.sf.jasperreports.web.util.WebUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class Controller
{
	private static final Log log = LogFactory.getLog(Controller.class);

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
	 * @throws JRInteractiveException 
	 *
	 */
	public void runReport(
		WebReportContext webReportContext,
		Action action
		) throws JRException, JRInteractiveException
	{
		String reportUri = (String)webReportContext.getParameterValue(WebUtil.REQUEST_PARAMETER_REPORT_URI);
		int initialStackSize = 0;
		CommandStack commandStack = (CommandStack)webReportContext.getParameterValue(AbstractAction.PARAM_COMMAND_STACK);
		if (commandStack != null) {
			initialStackSize = commandStack.getExecutionStackSize();
		}

		setDataCache(webReportContext);
		
		JasperReport jasperReport = null;
		
		
		if (reportUri != null && reportUri.trim().length() > 0)
		{
			reportUri = reportUri.trim();

			if (action != null) 
			{
				action.run();
				if (log.isDebugEnabled()) {
					log.debug("action requires refill: " + action.requiresRefill());
				}
				if (!action.requiresRefill()) { // stop here because this action does not modify jasperDesign
					return;
				}
			}

			jasperReport = RepositoryUtil.getInstance(jasperReportsContext).getReport(webReportContext, reportUri);
		}


		if (jasperReport == null)
		{
			throw new JRException("Report not found at : " + reportUri);
		}
		
		Boolean async = (Boolean)webReportContext.getParameterValue(WebUtil.REQUEST_PARAMETER_ASYNC_REPORT);
		if (async == null)
		{
			async = Boolean.FALSE;
		}
		webReportContext.setParameterValue(WebUtil.REQUEST_PARAMETER_ASYNC_REPORT, async);
		
		try {
			runReport(webReportContext, jasperReport, async.booleanValue());
		} catch (JRException e) {
			undoAction(webReportContext, initialStackSize);
			throw e;
		} catch (JRRuntimeException e) {
			undoAction(webReportContext, initialStackSize);
			throw e;
		}
	}
	
	private void undoAction(WebReportContext webReportContext, int initialStackSize) {
		CommandStack commandStack = (CommandStack)webReportContext.getParameterValue(AbstractAction.PARAM_COMMAND_STACK);
		if (commandStack != null) {
			for (int i = 0; i < (commandStack.getExecutionStackSize() - initialStackSize); i++) {
				commandStack.undo();
			}
		}
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
