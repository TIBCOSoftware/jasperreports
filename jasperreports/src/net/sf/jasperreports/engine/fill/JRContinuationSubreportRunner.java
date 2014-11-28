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


import org.apache.commons.javaflow.Continuation;


/**
 * Implemetation of {@link net.sf.jasperreports.engine.fill.JRSubreportRunner JRSubreportRunner}
 * using <a href="http://jakarta.apache.org/commons/sandbox/javaflow/">Javaflow</a> continuations.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 */
public class JRContinuationSubreportRunner extends JRSubreportRunnable implements JRSubreportRunner
{
	private Continuation continuation;

	public JRContinuationSubreportRunner(JRFillSubreport fillSubreport)
	{
		super(fillSubreport);
	}

	public boolean isFilling()
	{
		return continuation != null;
	}

	public JRSubreportRunResult start()
	{
		continuation = Continuation.startWith(this);
		return runResult();
	}

	public JRSubreportRunResult resume()
	{
		continuation = Continuation.continueWith(continuation);
		return runResult();
	}

	public void reset()
	{
		continuation = null;
	}

	public void cancel()
	{
	}

	public void suspend()
	{
		Continuation.suspend();
	}
}
