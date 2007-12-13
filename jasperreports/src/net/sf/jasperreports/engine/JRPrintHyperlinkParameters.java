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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * A set of parameters associated with a print element.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public class JRPrintHyperlinkParameters implements Serializable
{
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;
	
	private List parameters;
	
	
	/**
	 * Creates an empty set of parameters.
	 */
	public JRPrintHyperlinkParameters()
	{
		parameters = new ArrayList();
	}


	/**
	 * Returns the list of {@link JRPrintHyperlinkParameter parameters}.
	 * 
	 * @return the list of parameters
	 */
	public List getParameters()
	{
		return parameters;
	}
	
	
	/**
	 * Adds a new parameter to the set.
	 * 
	 * @param parameter the parameter to add
	 */
	public void addParameter(JRPrintHyperlinkParameter parameter)
	{
		parameters.add(parameter);
	}
}
