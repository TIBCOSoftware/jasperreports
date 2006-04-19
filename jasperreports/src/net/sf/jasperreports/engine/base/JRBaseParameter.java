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
package net.sf.jasperreports.engine.base;

import java.io.Serializable;

import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.util.JRClassLoader;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JRBaseParameter implements JRParameter, Serializable
{


	/**
	 *
	 */
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

	/**
	 *
	 */
	protected String name = null;
	protected String description = null;
	protected String valueClassName = java.lang.String.class.getName();
	protected boolean isSystemDefined = false;
	protected boolean isForPrompting = true;

	protected transient Class valueClass = null;

	/**
	 *
	 */
	protected JRExpression defaultValueExpression = null;


	/**
	 *
	 */
	protected JRBaseParameter()
	{
	}
	
	
	/**
	 *
	 */
	protected JRBaseParameter(JRParameter parameter, JRBaseObjectFactory factory)
	{
		factory.put(parameter, this);
		
		name = parameter.getName();
		description = parameter.getDescription();
		valueClassName = parameter.getValueClassName();
		isSystemDefined = parameter.isSystemDefined();
		isForPrompting = parameter.isForPrompting();

		defaultValueExpression = factory.getExpression(parameter.getDefaultValueExpression());
	}
		

	/**
	 *
	 */
	public String getName()
	{
		return this.name;
	}
		
	/**
	 *
	 */
	public String getDescription()
	{
		return this.description;
	}
		
	/**
	 *
	 */
	public void setDescription(String description)
	{
		this.description = description;
	}
	
	/**
	 *
	 */
	public Class getValueClass()
	{
		if (valueClass == null)
		{
			if (valueClassName != null)
			{
				try
				{
					valueClass = JRClassLoader.loadClassForName(valueClassName);
				}
				catch(ClassNotFoundException e)
				{
					throw new JRRuntimeException(e);
				}
			}
		}
		
		return valueClass;
	}

	/**
	 *
	 */
	public String getValueClassName()
	{
		return valueClassName;
	}

	/**
	 *
	 */
	public boolean isSystemDefined()
	{
		return this.isSystemDefined;
	}

	/**
	 *
	 */
	public boolean isForPrompting()
	{
		return this.isForPrompting;
	}

	/**
	 *
	 */
	public JRExpression getDefaultValueExpression()
	{
		return this.defaultValueExpression;
	}


}
