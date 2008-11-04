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
package net.sf.jasperreports.engine.design.events;

import java.beans.PropertyChangeSupport;

import net.sf.jasperreports.engine.JRConstants;


/**
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public class JRPropertyChangeSupport extends PropertyChangeSupport
{
	
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;
	
	private Object sourceBean;
	
	public JRPropertyChangeSupport(Object sourceBean)
	{
		super(sourceBean);
		
		this.sourceBean = sourceBean;
	}

	public void fireCollectionElementAddedEvent(String propertyName, Object addedValue, int addedIndex)
	{
		firePropertyChange(new CollectionElementAddedEvent(sourceBean, propertyName, 
				addedValue, addedIndex));
	}

	public void fireCollectionElementRemovedEvent(String propertyName, Object removedValue, int removedIndex)
	{
		firePropertyChange(new CollectionElementRemovedEvent(sourceBean, propertyName, 
				removedValue, removedIndex));
	}
	
	public void firePropertyChange(String propertyName, float oldValue, float newValue)
	{
		if (oldValue == newValue)
		{
			return;
		}
		
		firePropertyChange(propertyName, new Float(oldValue), new Float(newValue));
	}
	
	public void firePropertyChange(String propertyName, double oldValue, double newValue)
	{
		if (oldValue == newValue)
		{
			return;
		}
		
		firePropertyChange(propertyName, new Double(oldValue), new Double(newValue));
	}
	
}
