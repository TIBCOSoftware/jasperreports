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
package net.sf.jasperreports.engine.design;

import net.sf.jasperreports.engine.base.JRBaseSubreportReturnValue;

/**
 * Implementation of {@link net.sf.jasperreports.engine.JRSubreportReturnValue JRSubreportReturnValue}
 * to be used for report desing purposes.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public class JRDesignSubreportReturnValue extends JRBaseSubreportReturnValue
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 10002;

	/**
	 * Sets the subreport variable name.
	 * 
	 * @param name the variable name
	 * @see net.sf.jasperreports.engine.JRSubreportReturnValue#getSubreportVariable()
	 */
	public void setSubreportVariable(String name)
	{
		this.subreportVariable = name;
	}

	/**
	 * Sets the master variable name.
	 * 
	 * @param name the variable name
	 * @see net.sf.jasperreports.engine.JRSubreportReturnValue#getToVariable()
	 */
	public void setToVariable(String name)
	{
		this.toVariable = name;
	}

	/**
	 * Sets the calculation type.
	 * 
	 * @param calculation the calculation type
	 * @see net.sf.jasperreports.engine.JRSubreportReturnValue#getCalculation()
	 */
	public void setCalculation(byte calculation)
	{
		this.calculation = calculation;
	}
	
	/**
	 * Sets the incrementer factory class name.
	 * 
	 * @param incrementerFactoryClassName the name of the incrementer factory class
	 * @see net.sf.jasperreports.engine.JRSubreportReturnValue#getIncrementerFactoryClassName()
	 */
	public void setIncrementerFactoryClassName(String incrementerFactoryClassName)
	{
		this.incrementerFactoryClassName = incrementerFactoryClassName;
	}
}
