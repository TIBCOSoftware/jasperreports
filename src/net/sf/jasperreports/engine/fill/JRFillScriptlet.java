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
package net.sf.jasperreports.engine.fill;

import net.sf.jasperreports.engine.JRAbstractScriptlet;
import net.sf.jasperreports.engine.JRPropertiesHolder;
import net.sf.jasperreports.engine.JRPropertiesMap;
import net.sf.jasperreports.engine.JRScriptlet;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id: JRFillParameter.java 2004 2007-12-05 14:18:45Z teodord $
 */
public class JRFillScriptlet implements JRScriptlet
{


	/**
	 *
	 */
	protected JRScriptlet parent = null;

	/**
	 *
	 */
	private JRAbstractScriptlet scriptlet = null;


	/**
	 *
	 */
	protected JRFillScriptlet(
		JRScriptlet parent, 
		JRFillObjectFactory factory
		)
	{
		factory.put(parent, this);

		this.parent = parent;
	}


	/**
	 *
	 */
	public String getName()
	{
		return parent.getName();
	}
		
	/**
	 *
	 */
	public String getDescription()
	{
		return parent.getDescription();
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
	public JRAbstractScriptlet getScriptlet()
	{
		return scriptlet;
	}
		
	/**
	 *
	 */
	public void setScriptlet(JRAbstractScriptlet scriptlet)
	{
		this.scriptlet = scriptlet;
	}

	
	public boolean hasProperties()
	{
		return parent.hasProperties();
	}


	public JRPropertiesMap getPropertiesMap()
	{
		return parent.getPropertiesMap();
	}

	
	public JRPropertiesHolder getParentProperties()
	{
		return null;
	}
		

	/**
	 *
	 */
	public Object clone() 
	{
		return null;
	}

}
