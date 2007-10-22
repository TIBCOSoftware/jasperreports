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

import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRElementGroup;
import net.sf.jasperreports.engine.base.JRBaseElementGroup;
import net.sf.jasperreports.engine.design.events.JRPropertyChangeSupport;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JRDesignElementGroup extends JRBaseElementGroup
{


	/**
	 *
	 */
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;


	public static final String PROPERTY_ELEMENT_GROUP = "elementGroup";

	/**
	 *
	 */
	public void setElementGroup(JRElementGroup elementGroup)
	{
		Object old = this.elementGroup;
		this.elementGroup = elementGroup;
		getEventSupport().firePropertyChange(PROPERTY_ELEMENT_GROUP, old, this.elementGroup);
	}

	/**
	 *
	 */
	public void addElement(JRDesignElement element)
	{
		element.setElementGroup(this);
		
		this.children.add(element);
	}

	/**
	 *
	 */
	public JRDesignElement removeElement(JRDesignElement element)
	{
		element.setElementGroup(null);

		this.children.remove(element);
		
		return element;
	}

	/**
	 *
	 */
	public void addElementGroup(JRDesignElementGroup elemGrp)
	{
		elemGrp.setElementGroup(this);
		
		this.children.add(elemGrp);
	}

	/**
	 *
	 */
	public JRDesignElementGroup removeElementGroup(JRDesignElementGroup elemGrp)
	{
		elemGrp.setElementGroup(null);

		this.children.remove(elemGrp);
		
		return elemGrp;
	}

	
	private transient JRPropertyChangeSupport eventSupport;
	
	public JRPropertyChangeSupport getEventSupport()
	{
		synchronized (this)
		{
			if (eventSupport == null)
			{
				eventSupport = new JRPropertyChangeSupport(this);
			}
		}
		
		return eventSupport;
	}

}
