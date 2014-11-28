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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.JasperReportsContext;

/**
 * Base class used to perform report filling asychronously.
 * <p>
 * An instance of this type can be used as a handle to an asychronous fill process.
 * The main benefit of this method is that the filling process can be cancelled.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 */
public abstract class BaseFillHandle implements FillHandle
{	
	protected final JasperReportsContext jasperReportsContext;
	protected final JasperReport jasperReport;
	protected final Map<String,Object> parameters;
	protected final JRDataSource dataSource;
	protected final Connection conn;
	protected final ReportFiller filler;
	protected final List<AsynchronousFilllListener> listeners;
	protected boolean started;
	protected boolean running;
	protected boolean cancelled;
	protected final Object lock;
	
	protected BaseFillHandle (
			JasperReportsContext jasperReportsContext,
			JasperReport jasperReport,
			Map<String,Object> parameters,
			JRDataSource dataSource,
			Connection conn
			) throws JRException
	{
		this.jasperReportsContext = jasperReportsContext;
		this.jasperReport = jasperReport;
		this.parameters = parameters;
		this.dataSource = dataSource;
		this.conn = conn;
		this.filler = JRFiller.createReportFiller(jasperReportsContext, jasperReport);
		this.listeners = new ArrayList<AsynchronousFilllListener>();
		lock = this;
	}

	
	public void addListener(AsynchronousFilllListener listener)
	{
		listeners.add(listener);
	}

	public void addFillListener(FillListener listener)
	{
		filler.addFillListener(listener);
	}


	public boolean removeListener(AsynchronousFilllListener listener)
	{
		return listeners.remove(listener);
	}

	
	protected class ReportFill implements Runnable
	{
		public void run()
		{
			synchronized (lock)
			{
				running = true;
			}
			
			try
			{
				JasperPrint print;
				if (conn != null)
				{
					print = filler.fill(parameters, conn);
				}
				else if (dataSource != null)
				{
					print = filler.fill(parameters, dataSource);
				}
				else
				{
					print = filler.fill(parameters);
				}
				
				notifyFinish(print);
			}
			catch (Throwable e) //NOPMD
			{
				synchronized (lock)
				{
					if (cancelled)
					{
						notifyCancel();
					}
					else
					{
						notifyError(e);
					}
				}
			}
			finally
			{
				synchronized (lock)
				{
					running = false;
				}
			}
		}
	}
	
	
	public void startFill()
	{
		synchronized (lock)
		{
			if (started)
			{
				throw new IllegalStateException("Fill already started.");
			}

			started = true;
		}
		
		ReportFill reportFiller = new ReportFill();
		
		Executor executor = getReportExecutor();
		executor.execute(reportFiller);
	}
	
	protected abstract Executor getReportExecutor();
	
	
	public void cancellFill() throws JRException
	{
		synchronized (lock)
		{
			if (!running)
			{
				throw new IllegalStateException("Fill not running.");
			}
			
			filler.cancelFill();
			cancelled = true;
		}
	}
	
	
	protected void notifyFinish(JasperPrint print)
	{
		for (Iterator<AsynchronousFilllListener> i = listeners.iterator(); i.hasNext();)
		{
			AsynchronousFilllListener listener = i.next();
			listener.reportFinished(print);
		}
	}
	
	
	protected void notifyCancel()
	{
		for (Iterator<AsynchronousFilllListener> i = listeners.iterator(); i.hasNext();)
		{
			AsynchronousFilllListener listener =  i.next();
			listener.reportCancelled();
		}
	}
	
	
	protected void notifyError(Throwable e)
	{
		for (Iterator<AsynchronousFilllListener> i = listeners.iterator(); i.hasNext();)
		{
			AsynchronousFilllListener listener = i.next();
			listener.reportFillError(e);
		}
	}
	
	public boolean isPageFinal(int pageIdx)
	{
		return filler.isPageFinal(pageIdx);
	}
}
