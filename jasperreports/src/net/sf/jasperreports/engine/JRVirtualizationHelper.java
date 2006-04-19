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
package net.sf.jasperreports.engine;

/**
 * Virtualization helper class.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public class JRVirtualizationHelper
{
	private static final ThreadLocal threadVirtualizer = new ThreadLocal();

	
	/**
	 * Sets a virtualizer to be used for the current thread.
	 * <p>
	 * The current thread's virtualizer is used when a report obtained by virtualization
	 * is deserialized.
	 * 
	 * @param virtualizer
	 */
	public static void setThreadVirtualizer(JRVirtualizer virtualizer)
	{
		threadVirtualizer.set(virtualizer);
	}

	
	/**
	 * Clears the virtualizer associated to the current thread.
	 */
	public static void clearThreadVirtualizer()
	{
		threadVirtualizer.set(null);
	}

	
	/**
	 * Returns the virtualizer associated to the current thread.
	 * <p>
	 * This method is used by {@link net.sf.jasperreports.engine.base.JRVirtualPrintPage JRVirtualPrintPage}
	 * on deserialization.
	 * 
	 * @return the virtualizer associated to the current thread
	 */
	public static JRVirtualizer getThreadVirtualizer()
	{
		return (JRVirtualizer) threadVirtualizer.get();
	}
}
