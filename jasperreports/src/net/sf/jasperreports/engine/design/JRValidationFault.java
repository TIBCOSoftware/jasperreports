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
package net.sf.jasperreports.engine.design;

import java.io.Serializable;

import net.sf.jasperreports.engine.JRConstants;


/**
 * A report validation fault.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 * @see JRVerifier
 */
public class JRValidationFault implements Serializable
{
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;
	
	private String message;
	private Object source;
	
	/**
	 * Creates an empty fault.
	 */
	public JRValidationFault()
	{
	}

	/**
	 * Returns the fault message/description.
	 * 
	 * @return the fault description
	 */
	public String getMessage()
	{
		return message;
	}
	
	/**
	 * Sets the fault message/description.
	 * 
	 * @param message the description
	 */
	public void setMessage(String message)
	{
		this.message = message;
	}
	
	/**
	 * Returns the fault source object.
	 * <p/>
	 * The source is the report object that dispalayed the fault.
	 * It can be, for instance, a (@link JRDesignVariable variable}, an {@link JRDesignExpression expression},
	 * or a {@link JRDesignElement report element}. 
	 * 
	 * @return the fault source
	 */
	public Object getSource()
	{
		return source;
	}
	
	/**
	 * Set the fault source.
	 * 
	 * @param source the fault source
	 */
	public void setSource(Object source)
	{
		this.source = source;
	}
	
}
