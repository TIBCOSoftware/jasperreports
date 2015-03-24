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

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRPrintElement;
import net.sf.jasperreports.engine.JRRuntimeException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 */
public class FillerSubreportParent implements BandReportFillerParent
{
	
	private static final Log log = LogFactory.getLog(FillerSubreportParent.class);
	public static final String EXCEPTION_MESSAGE_KEY_UNSUPPORTED_OVERFLOW = "fill.subreport.parent.unsupported.overflow";
	public static final String EXCEPTION_MESSAGE_KEY_NO_SUBREPORT_RUNNER = "fill.subreport.parent.no.subreport.runner";

	private final JRFillSubreport parentElement;
	private final JRBaseFiller parentFiller;
	private final DatasetExpressionEvaluator evaluator;

	private JRSubreportRunner subreportRunner;

	private int currentPageStretchHeight;
	
	public FillerSubreportParent(JRFillSubreport parentElement, DatasetExpressionEvaluator evaluator)
	{
		this.parentElement = parentElement;
		this.parentFiller = parentElement.filler;
		this.evaluator = evaluator;
	}

	@Override
	public BaseReportFiller getFiller()
	{
		return parentFiller;
	}

	@Override
	public void registerSubfiller(JRBaseFiller filler)
	{
		parentFiller.registerSubfiller(filler);
	}

	@Override
	public void unregisterSubfiller(JRBaseFiller filler)
	{
		parentFiller.unregisterSubfiller(filler);
	}

	@Override
	public boolean isRunToBottom()
	{
		return parentElement.isRunToBottom() != null && parentElement.isRunToBottom();
	}

	@Override
	public boolean isParentPagination()
	{
		return true;
	}

	@Override
	public boolean isPageBreakInhibited()
	{
		return parentElement.getBand().isPageBreakInhibited();
	}

	@Override
	public DatasetExpressionEvaluator getCachedEvaluator()
	{
		return evaluator;
	}

	public void setSubreportRunner(JRSubreportRunner subreportRunner)
	{
		this.subreportRunner = subreportRunner;
	}

	@Override
	public void addPage(FillerPageAddedEvent pageAdded) throws JRException
	{
		currentPageStretchHeight = pageAdded.getPageStretchHeight();
		
		if (!pageAdded.hasReportEnded())
		{
			if (!parentFiller.isBandOverFlowAllowed())
			{
				throw 
					new JRRuntimeException(
						EXCEPTION_MESSAGE_KEY_UNSUPPORTED_OVERFLOW,  
						(Object[])null 
						);
			}

			suspendSubreportRunner(pageAdded);
		}
	}

	protected void suspendSubreportRunner(FillerPageAddedEvent pageAdded) throws JRException
	{
		if (subreportRunner == null)
		{
			throw 
				new JRRuntimeException(
					EXCEPTION_MESSAGE_KEY_NO_SUBREPORT_RUNNER,  
					(Object[])null 
					);
		}

		if (log.isDebugEnabled())
		{
			log.debug("Fill " + pageAdded.getFiller().fillerId + ": suspeding subreport runner");
		}

		subreportRunner.suspend();
	}

	public int getCurrentPageStretchHeight()
	{
		return currentPageStretchHeight;
	}

	@Override
	public void updateBookmark(JRPrintElement element)
	{
		parentFiller.updateBookmark(element);
	}

}
