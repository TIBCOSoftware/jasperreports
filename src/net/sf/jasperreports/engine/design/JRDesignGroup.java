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

import net.sf.jasperreports.engine.JRBand;
import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JROrigin;
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
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;
	
	public static final String PROPERTY_COUNT_VARIABLE = "countVariable";
	
	public static final String PROPERTY_EXPRESSION = "expression";
	
	public static final String PROPERTY_GROUP_FOOTER = "groupFooter";
	
	public static final String PROPERTY_GROUP_HEADER = "groupHeader";
	
	public static final String PROPERTY_NAME = "name";

	/**
	 *
	 */
	public void setName(String name)
	{
		Object old = this.name;
		this.name = name;
		updateBandOrigins();
		getEventSupport().firePropertyChange(PROPERTY_NAME, old, this.name);
	}

	/**
	 *
	 */
	public void setExpression(JRExpression expression)
	{
		Object old = this.expression;
		this.expression = expression;
		getEventSupport().firePropertyChange(PROPERTY_EXPRESSION, old, this.expression);
	}
	
	/**
	 *
	 */
	public void setGroupHeader(JRBand groupHeader)
	{
		Object old = this.groupHeader;
		this.groupHeader = groupHeader;
		setBandOrigin(this.groupHeader, JROrigin.GROUP_HEADER);
		getEventSupport().firePropertyChange(PROPERTY_GROUP_HEADER, old, this.groupHeader);
	}
		
	/**
	 *
	 */
	public void setGroupFooter(JRBand groupFooter)
	{
		Object old = this.groupFooter;
		this.groupFooter = groupFooter;
		setBandOrigin(this.groupFooter, JROrigin.GROUP_FOOTER);
		getEventSupport().firePropertyChange(PROPERTY_GROUP_FOOTER, old, this.groupFooter);
	}

	/**
	 *
	 */
	public void setCountVariable(JRVariable countVariable)
	{
		Object old = this.countVariable;
		this.countVariable = countVariable;
		getEventSupport().firePropertyChange(PROPERTY_COUNT_VARIABLE, old, this.countVariable);
	}

	protected void setBandOrigin(JRBand band, byte type)
	{
		if (band instanceof JRDesignBand)
		{
			JROrigin origin = new JROrigin(null, getName(), type);
			((JRDesignBand) band).setOrigin(origin);
		}
	}
	
	protected void updateBandOrigins()
	{
		setBandOrigin(getGroupHeader(), JROrigin.GROUP_HEADER);
		setBandOrigin(getGroupFooter(), JROrigin.GROUP_FOOTER);
	}
}
