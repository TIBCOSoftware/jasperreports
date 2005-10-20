/*
 * ============================================================================
 * GNU Lesser General Public License
 * ============================================================================
 *
 * JasperReports - Free Java report-generating library.
 * Copyright (C) 2001-2005 JasperSoft Corporation http://www.jaspersoft.com
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
 * 185, Berry Street, Suite 6200
 * San Francisco CA 94107
 * http://www.jaspersoft.com
 */
package net.sf.jasperreports.engine;

import java.util.List;

/**
 * Print element container interface.
 * <p>
 * An instance of this type is used by {@link net.sf.jasperreports.engine.fill.JRFillElementContainer JRFillElementContainer}
 * to collect the generated print elements.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public interface JRPrintElementContainer
{
	/**
	 * Returns the height of the container.
	 * 
	 * @return the height of the container
	 */
	public int getHeight();
	
	
	/**
	 * Sets the height of the container.
	 * 
	 * @param height the height
	 */
	public void setHeight(int height);

	
	/**
	 * Returns the list of {@link JRPrintElement elements} of the container.
	 * 
	 * @return the list of elements
	 */
	public List getElements();
	
	
	/**
	 * Adds an element to the container.
	 * 
	 * @param element the element to add
	 */
	public void addElement(JRPrintElement element);
}
