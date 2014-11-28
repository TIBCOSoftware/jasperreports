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

import java.util.concurrent.atomic.AtomicInteger;

import net.sf.jasperreports.engine.JRPrintElement;
import net.sf.jasperreports.engine.JRRuntimeException;

/**
 * Default {@link PrintElementOriginator} implementation.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 */
public class DefaultPrintElementOriginator implements PrintElementOriginator
{

	private final int sourceElementId;
	private final AtomicInteger printIdCounter;
	
	public DefaultPrintElementOriginator(int sourceElementId)
	{
		this.sourceElementId = sourceElementId;
		this.printIdCounter = new AtomicInteger(JRPrintElement.UNSET_PRINT_ELEMENT_ID);
	}

	@Override
	public int getSourceElementId()
	{
		return sourceElementId;
	}

	@Override
	public int generatePrintElementId()
	{
		int id = printIdCounter.incrementAndGet();
		// testing for overflow; this limits the number of elements to 2^32 - 1
		if (id == JRPrintElement.UNSET_PRINT_ELEMENT_ID)
		{
			throw new JRRuntimeException("Maximum number of elements for " + sourceElementId + " reached");
		}
		return id;
	}

}
