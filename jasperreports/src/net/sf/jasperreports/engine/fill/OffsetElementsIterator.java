/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2019 TIBCO Software Inc. All rights reserved.
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

import java.util.Iterator;
import java.util.List;

import net.sf.jasperreports.engine.JRPrintElement;

/**
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 */
public class OffsetElementsIterator implements Iterator<JRPrintElement>
{
	private final Iterator<Object> iterator;
	
	private OffsetElements subElements;
	private Iterator<? extends JRPrintElement> subIterator;

	public OffsetElementsIterator(List<Object> elements)
	{
		this.iterator = elements.iterator();
	}

	@Override
	public boolean hasNext()
	{
		return (subIterator != null && subIterator.hasNext()) || iterator.hasNext();
	}

	@Override
	public JRPrintElement next()
	{
		JRPrintElement element = null;
		// try to get the element from the sublist
		if (subIterator != null)
		{
			if (subIterator.hasNext())
			{
				element = subIterator.next();
				// remove the element from the sublist.  this helps with virtualized subreport pages 
				// by releasing the external data and allowing the master page to reuse the storage.
				subIterator.remove();
				
				// apply the offsets
				setSubOffsets(element);
			}
			else
			{
				subIterator = null;
				subElements = null;
			}
		}
		
		// if no sublist element, get from the main list
		if (element == null)
		{
			Object next = iterator.next();
			if (next instanceof JRPrintElement)
			{
				element = (JRPrintElement) next;
			}
			else
			{
				subElements = (OffsetElements) next;
				subIterator = subElements.getElements().iterator();

				// we're only adding non empty sublists, hence not checking hasNext()
				element = subIterator.next();
				subIterator.remove();
				
				// apply the offsets
				setSubOffsets(element);
			}
		}
		
		return element;
	}

	protected void setSubOffsets(JRPrintElement element)
	{
		element.setX(subElements.getOffsetX() + element.getX());
		element.setY(subElements.getOffsetY() + element.getY());
	}

	@Override
	public void remove()
	{
		throw new UnsupportedOperationException();
	}

}
