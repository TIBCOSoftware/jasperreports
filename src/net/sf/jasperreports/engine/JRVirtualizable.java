/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2005 - 2009 Works, Inc. All rights reserved.
 * http://www.works.com
 *
 * Unless you have purchased a commercial license agreement from Jaspersoft,
 * the following license terms apply:
 *
 * This program is part of JasperReports.
 *
 * JasperReports is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 *(at your option) any later version.
 *
 * JasperReports is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with JasperReports. If not, see <http://www.gnu.org/licenses/>.
 */

/*
 * Licensed to Jaspersoft Corporation under a Contributer Agreement
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
	 * Called by the virtualizer after the object's data is externalized, but before
	 * the virtual data is {@link #removeVirtualData() removed}.
	 */
	void afterExternalization();
	
	
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
