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


/**
 * @author John Bindel
 * @version $Id$
 * @see net.sf.jasperreports.engine.JRVirtualizationHelper
 */
public interface JRVirtualizer
{
	/**
	 * Lets this virtualizer know that it must track the object.<p>
	 *
	 * All virtualizable object must register with their virtualizer
	 * upon construction.
	 */
 	void registerObject(JRVirtualizable o);

	/**
	 * Lets this virtualizer know that it no longer must track the
	 * object.
	 */
 	void deregisterObject(JRVirtualizable o);

	/**
	 * Lets the virtualizer know that this object is still being used.
	 * This should be called to help the virtualizer determine which
	 * objects to keep in its cache, and which objects to page-out
	 * when it must do some paging-out.<p>
	 *
	 * The virtualizer gets to decide what type of caching strategy
	 * it will use.
	 */
	void touch(JRVirtualizable o);

	/**
	 * Called when the virtual object must be paged-in.
	 * <p>
	 * If the object's virtual data is not paged-out, the object will only be
	 * {@link #touch(JRVirtualizable) touched}.
	 */
	void requestData(JRVirtualizable o);

	/**
	 * Called when the virtual object paged-out data should be freed.
	 * <p>
	 * If the object's virtual data is not paged-out, the object will only be
	 * {@link #touch(JRVirtualizable) touched}.
	 */
	void clearData(JRVirtualizable o);

	/**
	 * Called when the virtual object should be paged-out.
	 */
	void virtualizeData(JRVirtualizable o);
	
	/**
	 * Called when we are done with the virtualizer and wish to
	 * cleanup any resources it has.
	 */
	void cleanup();
	
}
