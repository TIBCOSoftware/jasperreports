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
package net.sf.jasperreports.engine.part;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.fill.PartReportFiller;

/**
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 */
public class FillPrintPartQueue
{
	public static final String EXCEPTION_MESSAGE_KEY_CANNOT_REMOVE_HEAD_PART = "engine.part.queue.cannot.remove.head.part";
	public static final String EXCEPTION_MESSAGE_KEY_CANNOT_REPLACE_HEAD_PART = "engine.part.queue.cannot.replace.head.part";

	private FillPrintPart head;
	private FillPrintPart tail;
	
	public FillPrintPartQueue(PartPrintOutput output)
	{
		this.head = this.tail = new FilledPrintPart(output);
	}

	public FillPrintPart head()
	{
		return head;
	}
	
	public FillPrintPart tail()
	{
		return tail;
	}

	public boolean isCollapsed()
	{
		return head == tail;
	}

	public FilledPrintPart appendOutput(PartPrintOutput output)
	{
		FilledPrintPart filledPart = new FilledPrintPart(output);
		append(filledPart);
		return filledPart;
	}

	public DelayedPrintPart appendDelayed(FillPart fillPart)
	{
		DelayedPrintPart delayedPart = new DelayedPrintPart(fillPart);
		append(delayedPart);
		return delayedPart;
	}
	
	protected void append(FillPrintPart part)
	{
		part.setPreviousPart(tail);
		tail.setNextPart(part);
		tail = part;
	}

	public void fillDelayed(DelayedPrintPart part, PartReportFiller filler, byte evaluation) throws JRException
	{
		PartPrintOutput appendOutput = part.previousPart().getOutput();
		if (appendOutput != null)
		{
			part.getFillPart().fill(evaluation, appendOutput);
			remove(part);
			collapse(part.previousPart());
		}
		else
		{
			FillPartPrintOutput localOutput = new FillPartPrintOutput(filler);
			part.getFillPart().fill(evaluation, localOutput);
			FilledPrintPart filledPart = new FilledPrintPart(localOutput);
			replace(part, filledPart);
			collapse(filledPart);
		}
	}
	
	protected void remove(DelayedPrintPart part)
	{
		if (part == head)
		{
			throw 
				new JRRuntimeException(
					EXCEPTION_MESSAGE_KEY_CANNOT_REMOVE_HEAD_PART,
					(Object[])null);
		}
		
		part.previousPart().setNextPart(part.nextPart());
		
		if (part == tail)
		{
			tail = part.previousPart();
		}
		else
		{
			part.nextPart().setPreviousPart(part.previousPart());
		}
		
	}

	protected void replace(FillPrintPart originalPart, FillPrintPart newPart)
	{
		if (originalPart == head)
		{
			throw 
				new JRRuntimeException(
					EXCEPTION_MESSAGE_KEY_CANNOT_REPLACE_HEAD_PART,
					(Object[])null);
		}
		
		newPart.setPreviousPart(originalPart.previousPart());
		newPart.setNextPart(originalPart.nextPart());
		
		originalPart.previousPart().setNextPart(newPart);
		
		if (originalPart == tail)
		{
			tail = newPart;
		}
		else
		{
			originalPart.nextPart().setPreviousPart(newPart);
		}
	}
	
	protected void collapse(FillPrintPart part)
	{
		PartPrintOutput output = part.getOutput();
		FillPrintPart next = part.nextPart();
		while (next != null && next.getOutput() != null)
		{
			FillPartPrintOutput nextOutput = (FillPartPrintOutput) next.getOutput();
			output.append(nextOutput);
			nextOutput.getDelayedActions().dispose();
			
			next = next.nextPart();
		}
		
		part.setNextPart(next);
		if (next == null)
		{
			tail = part;
		}
		else
		{
			next.setPreviousPart(part);
		}
	}
}
