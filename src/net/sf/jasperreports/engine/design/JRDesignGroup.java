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
package net.sf.jasperreports.engine.design;

import net.sf.jasperreports.engine.JRBand;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRVariable;
import net.sf.jasperreports.engine.base.JRBaseGroup;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JRDesignGroup extends JRBaseGroup
{


	/**
	 *
	 */
	private static final long serialVersionUID = 604;

	/**
	 *
	 */
	public void setName(String name)
	{
		this.name = name;
	}
		
	/**
	 *
	 */
	public void setExpression(JRExpression expression)
	{
		this.expression = expression;
	}
	
	/**
	 *
	 */
	public void setGroupHeader(JRBand groupHeader)
	{
		this.groupHeader = groupHeader;
	}
		
	/**
	 *
	 */
	public void setGroupFooter(JRBand groupFooter)
	{
		this.groupFooter = groupFooter;
	}

	/**
	 *
	 */
	public void setCountVariable(JRVariable countVariable)
	{
		this.countVariable = countVariable;
	}


}
