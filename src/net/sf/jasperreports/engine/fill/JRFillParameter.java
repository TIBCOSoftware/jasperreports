/*
 * ============================================================================
 *                   GNU Lesser General Public License
 * ============================================================================
 *
 * JasperReports - Free Java report-generating library.
 * Copyright (C) 2001-2005 Teodor Danciu teodord@users.sourceforge.net
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
 * Teodor Danciu
 * 173, Calea Calarasilor, Bl. 42, Sc. 1, Ap. 18
 * Postal code 030615, Sector 3
 * Bucharest, ROMANIA
 * Email: teodord@users.sourceforge.net
 */
package net.sf.jasperreports.engine.fill;

import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRParameter;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JRFillParameter implements JRParameter
{


	/**
	 *
	 */
	protected JRParameter parent = null;

	/**
	 *
	 */
	private Object value = null;


	/**
	 *
	 */
	protected JRFillParameter(
		JRParameter parameter, 
		JRFillObjectFactory factory
		)
	{
		factory.put(parameter, this);

		parent = parameter;
	}


	/**
	 *
	 */
	public String getName()
	{
		return this.parent.getName();
	}
		
	/**
	 *
	 */
	public String getDescription()
	{
		return this.parent.getDescription();
	}
		
	/**
	 *
	 */
	public void setDescription(String description)
	{
	}
	
	/**
	 *
	 */
	public Class getValueClass()
	{
		return parent.getValueClass();
	}
	
	/**
	 *
	 */
	public String getValueClassName()
	{
		return parent.getValueClassName();
	}
	
	/**
	 *
	 */
	public boolean isSystemDefined()
	{
		return this.parent.isSystemDefined();
	}
	
	/**
	 *
	 */
	public boolean isForPrompting()
	{
		return this.parent.isForPrompting();
	}
	
	/**
	 *
	 */
	public JRExpression getDefaultValueExpression()
	{
		return this.parent.getDefaultValueExpression();
	}
		
	/**
	 *
	 */
	public Object getValue()
	{
		return this.value;
	}
		
	/**
	 *
	 */
	public void setValue(Object value)
	{
		this.value = value;
	}
		

}
