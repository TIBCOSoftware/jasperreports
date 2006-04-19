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
package net.sf.jasperreports.engine.fill;


/**
 * A result returned by {@link net.sf.jasperreports.engine.fill.JRSubreportRunner#start() JRSubreportRunner.start()}
 * or {@link net.sf.jasperreports.engine.fill.JRSubreportRunner#resume() JRSubreportRunner.resume()}.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public class JRSubreportRunResult
{
	private final boolean finished;
	private final Throwable exception;
	
	public JRSubreportRunResult(boolean finished, Throwable exception)
	{
		this.finished = finished;
		this.exception = exception;
	}

	/**
	 * Decides whether the fill has resulted in an error.
	 * 
	 * @return whether the fill has resulted in an error
	 */
	public boolean isError()
	{
		return exception != null;
	}

	/**
	 * Returns the exception thrown by the subreport fill.
	 * 
	 * @return the exception thrown by the subreport fill
	 */
	public Throwable getException()
	{
		return exception;
	}
	
	/**
	 * Decides whether the subreport fill has finished (the subreport does not need
	 * to continue on a new page).
	 * @return whether the subreport fill has finished
	 */
	public boolean hasFinished()
	{
		return finished;
	}
}
