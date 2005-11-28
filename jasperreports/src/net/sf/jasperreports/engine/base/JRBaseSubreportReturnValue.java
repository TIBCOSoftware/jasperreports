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
 * 303 Second Street, Suite 450 North
 * San Francisco, CA 94107
 * http://www.jaspersoft.com
 */
package net.sf.jasperreports.engine.base;

import java.io.Serializable;

import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRSubreportReturnValue;
import net.sf.jasperreports.engine.JRVariable;

/**
 * Base implementation of {@link net.sf.jasperreports.engine.JRSubreportReturnValue JRSubreportReturnValue}.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public class JRBaseSubreportReturnValue implements JRSubreportReturnValue, Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

	/**
	 * The name of the subreport variable to be copied.
	 */
	protected String subreportVariable = null;

	/**
	 * The name of the master variable where the value should be copied.
	 */
	protected String toVariable = null;
	
	/**
	 * The calculation type.
	 */
	protected byte calculation = JRVariable.CALCULATION_NOTHING;
	
	/**
	 * The incrementer factory class name.
	 */
	protected String incrementerFactoryClassName = null;

	
	protected JRBaseSubreportReturnValue()
	{
	}

	
	protected JRBaseSubreportReturnValue(JRSubreportReturnValue returnValue, JRBaseObjectFactory factory)
	{
		factory.put(returnValue, this);

		subreportVariable = returnValue.getSubreportVariable();
		toVariable = returnValue.getToVariable();
		calculation = returnValue.getCalculation();
		incrementerFactoryClassName = returnValue.getIncrementerFactoryClassName();
	}

	/**
	 * Returns the name of the subreport variable whose value should be copied.
	 * 
	 * @return the name of the subreport variable whose value should be copied.
	 */
	public String getSubreportVariable()
	{
		return this.subreportVariable;
	}

	/**
	 * Returns the name of the master report variable where the value should be copied.
	 * 
	 * @return the name of the master report variable where the value should be copied.
	 */
	public String getToVariable()
	{
		return this.toVariable;
	}

	/**
	 * Returns the calculation type.
	 * <p>
	 * When copying the value from the subreport, a formula can be applied such that sum,
	 * maximum, average and so on can be computed.
	 * 
	 * @return the calculation type.
	 */
	public byte getCalculation()
	{
		return calculation;
	}

	/**
	 * Returns the incrementer factory class name.
	 * <p>
	 * The factory will be used to increment the value of the master report variable
	 * with the value from the subreport.
	 * 
	 * @return the incrementer factory class name.
	 */
	public String getIncrementerFactoryClassName()
	{
		return incrementerFactoryClassName;
	}

}
