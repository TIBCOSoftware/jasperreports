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
package net.sf.jasperreports.engine.fill;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

/**
 * Class used to perform report filling asychronously.
 * <p>
 * An instance of this type can be used as a handle to an asychronous fill process.
 * The main benefit of this method is that the filling process can be cancelled.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public class AsynchronousFillHandle
{	
	protected final JasperReport jasperReport;
	protected final Map<String,Object> parameters;
	protected final JRDataSource dataSource;
	protected final Connection conn;
	protected final JRBaseFiller filler;
	protected final List<AsynchronousFilllListener> listeners;
	protected Thread fillThread;
	protected boolean started;
	protected boolean running;
	protected boolean cancelled;
	protected final Object lock;
	
	protected Integer priority;
	
	protected String threadName;
	
	protected AsynchronousFillHandle (
			JasperReport jasperReport,
			Map<String,Object> parameters,
			JRDataSource dataSource
			) throws JRException
	{
		this(jasperReport, parameters, dataSource, null);
	}
	
	protected AsynchronousFillHandle (
			JasperReport jasperReport,
			Map<String,Object> parameters,
			Connection conn
			) throws JRException
	{
		this(jasperReport, parameters, null, conn);
	}
	
	protected AsynchronousFillHandle (
			JasperReport jasperReport,
			Map<String,Object> parameters
			) throws JRException
	{
		this(jasperReport, parameters, null, null);
	}
	
	protected AsynchronousFillHandle (
			JasperReport jasperReport,
			Map<String,Object> parameters,
			JRDataSource dataSource,
			Connection conn
			) throws JRException
	{
		this.jasperReport = jasperReport;
		this.parameters = parameters;
		this.dataSource = dataSource;
		this.conn = conn;
		this.filler = JRFiller.createFiller(jasperReport);
		this.listeners = new ArrayList<AsynchronousFilllListener>();
		lock = this;
	}

	
	/**
	 * Adds a listener to the filling process.
	 * 
	 * @param listener the listener to be added
	 */
	public void addListener(AsynchronousFilllListener listener)
	{
		listeners.add(listener);
	}


	/**
	 * Removes a listener from the filling process.
	 * 
	 * @param listener the listener to be removed
	 * @return <tt>true</tt> if the listener was found and removed
	 */
	public boolean removeListener(AsynchronousFilllListener listener)
	{
		return listeners.remove(listener);
	}

	
	protected class ReportFiller implements Runnable
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
			catch (Exception e)
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
	
	
	/**
	 * Starts the filling process asychronously.
	 * <p>
	 * The filling is launched on a new thread and the method exits
	 * after the thread is started.
	 * <p>
	 * When the filling finishes either in success or failure, the listeners
	 * are notified.  
	 */
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
		
		ReportFiller reportFiller = new ReportFiller();
		
		if (threadName == null)
		{
			fillThread = new Thread(reportFiller);
		}
		else
		{
			fillThread = new Thread(reportFiller, threadName);
		}
		
		if (priority != null)
		{
			fillThread.setPriority(priority.intValue());
		}
		
		fillThread.start();
	}

	
	/**
	 * Cancels the fill started by the handle.
	 * <p>
	 * The method sends a cancel signal to the filling process.
	 * When the filling process will end, the listeners will be notified 
	 * that the filling has been cancelled.
	 * 
	 * @throws JRException
	 */
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


	/**
	 * Creates an asychronous filling handle.
	 * 
	 * @param jasperReport the report
	 * @param parameters the parameter map
	 * @param dataSource the data source
	 * @return the handle
	 * @throws JRException
	 */
	public static AsynchronousFillHandle createHandle(
		JasperReport jasperReport,
		Map<String,Object> parameters,
		JRDataSource dataSource
		) throws JRException
	{
		AsynchronousFillHandle filler = new AsynchronousFillHandle(jasperReport, parameters, dataSource);
		
		return filler;
	}


	/**
	 * Creates an asychronous filling handle.
	 * 
	 * @param jasperReport the report
	 * @param parameters the parameter map
	 * @param conn the connection
	 * @return the handle
	 * @throws JRException
	 */
	public static AsynchronousFillHandle createHandle(
		JasperReport jasperReport,
		Map<String,Object> parameters,
		Connection conn
		) throws JRException
	{
		AsynchronousFillHandle filler = new AsynchronousFillHandle(jasperReport, parameters, conn);
		
		return filler;
	}


	/**
	 * Creates an asychronous filling handle.
	 * 
	 * @param jasperReport the report
	 * @param parameters the parameter map
	 * @return the handle
	 * @throws JRException
	 */
	public static AsynchronousFillHandle createHandle(
		JasperReport jasperReport,
		Map<String,Object> parameters
		) throws JRException
	{
		AsynchronousFillHandle filler = new AsynchronousFillHandle(jasperReport, parameters);
		
		return filler;
	}
	
	
	/**
	 * Sets the priority of the filler thread.
	 * 
	 * @param priority the filler thread priority.
	 * @see Thread#setPriority(int)
	 */
	public void setPriority (int priority)
	{
		synchronized (lock)
		{
			this.priority = Integer.valueOf(priority);
			if (fillThread != null)
			{
				fillThread.setPriority(priority);
			}
		}
	}
	
	
	/**
	 * Sets the name of the filler thread.
	 * 
	 * @param name the filler thread name.
	 * @see Thread#setName(java.lang.String)
	 */
	public void setThreadName (String name)
	{
		synchronized (lock)
		{
			this.threadName = name;
			if (fillThread != null)
			{
				fillThread.setName(name);
			}
		}
	}
}
