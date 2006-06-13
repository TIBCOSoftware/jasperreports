/*
 * ============================================================================
 * GNU Lesser General Public License
 * ============================================================================
 *
 * JasperReports - Free Java report-generating library.
 * Copyright (C) 2005 Works, Inc.  http://www.works.com/
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
 * Works, Inc.
 * 6034 West Courtyard Drive
 * Suite 210
 * Austin, TX 78730-5032
 * USA
 * http://www.works.com/
 */

/*
 * Licensed to JasperSoft Corporation under a Contributer Agreement
 */
package net.sf.jasperreports.engine;

import net.sf.jasperreports.engine.fill.JRVirtualizationContext;

/**
 * @author John Bindel
 * @version $Id$
 */
public interface JRVirtualizable {
	/**
	 * Used by the virtualizer to identify the data for this object.
	 */
	String getUID();

	/**
	 * Used by the virtualizer to set data.
	 */
	void setVirtualData(Object o);

	/**
	 * Used by the virtualizer to get data.
	 */
	Object getVirtualData();

	/**
	 * Used by the virtualizer to remove the data from the object in memory so
	 * that it may be garbage collected.
	 */
	void removeVirtualData();

	/**
	 * Used by the virtualizer to set identity data.
	 */
	void setIdentityData(Object id);

	/**
	 * Used by the virtualizer to get identity data.
	 */
	Object getIdentityData();
	
	
	/**
	 * Called by the virtualizer before the object's data is externalized.
	 */
	void beforeExternalization();
	
	
	/**
	 * Called by the virtualizer after the object's data was made available to the object.
	 */
	void afterInternalization();
	
	/**
	 * Returns the virtualization context this object belongs to.
	 * 
	 * @return the virtualization context this object belongs to
	 */
	//FIXME use a more generic context type, JRVirtualizationContext has print page-specific methods
	//issue: changing JRVirtualizationContext hierarchy would impact serialization
	JRVirtualizationContext getContext(); 
}
