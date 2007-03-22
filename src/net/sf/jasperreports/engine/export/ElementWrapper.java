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

/*
 * Contributors:
 * Greg Hilton 
 */

package net.sf.jasperreports.engine.export;

import net.sf.jasperreports.engine.JRPrintElement;

/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id: JRGridLayout.java 1632 2007-03-14 12:29:52Z teodord $
 */
public class ElementWrapper
{
	private JRPrintElement element = null;
	private String address = null;
	private ElementWrapper[] wrappers = null;
	
	public ElementWrapper(
		JRPrintElement element, 
		String address,
		ElementWrapper[] wrappers
		)
	{
		this.element = element;
		this.address = address;
		this.wrappers = wrappers;
	}
	
	public JRPrintElement getElement()
	{
		return element;
	}
	
	public String getAddress()
	{
		return address;
	}
	
	public ElementWrapper[] getWrappers()
	{
		return wrappers;
	}
}
