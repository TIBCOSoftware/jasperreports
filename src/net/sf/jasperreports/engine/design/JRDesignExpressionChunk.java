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

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import net.sf.jasperreports.engine.base.JRBaseExpressionChunk;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JRDesignExpressionChunk extends JRBaseExpressionChunk
{
	/** Mechanism for firing property change events. */
	private transient PropertyChangeSupport propSupport;

	/** Bean property name for text. */
	public static final String TEXT_PROPERTY = "text";

	/** Bean property name for type. */
	public static final String TYPE_PROPERTY = "type";

	/**
	 *
	 */
	private static final long serialVersionUID = 10003;

	/**
	 *
	 */
	public void setType(byte type)
	{
		Byte oldValue = new Byte(this.type);
		this.type = type;
		getPropertyChangeSupport().firePropertyChange(TYPE_PROPERTY,
				oldValue, new Byte(this.type));
	}
		
	/**
	 *
	 */
	public void setText(String text)
	{
		Object oldValue = this.text;
		this.text = text;
		getPropertyChangeSupport().firePropertyChange(TEXT_PROPERTY,
				oldValue, this.text);
	}

	/**
	 * Get the property change support object for this class.  Because the
	 * property change support object has to be transient, it may need to be
	 * created.
	 * @return The property change support object.
	 */
	protected PropertyChangeSupport getPropertyChangeSupport() {
		if (propSupport == null) {
			propSupport = new PropertyChangeSupport(this);
		}
		return propSupport;
	}

	/**
	 * Add a property listener to listen to all properties of this class.
	 * @param l The property listener to add.
	 */
	public void addPropertyChangeListener(PropertyChangeListener l) {
		getPropertyChangeSupport().addPropertyChangeListener(l);
	}

	/**
	 * Add a property listener to receive property change events for only one specific
	 * property.
	 * @param propName The property to listen to.
	 * @param l The property listener to add.
	 */
	public void addPropertyChangeListener(String propName, PropertyChangeListener l) {
		getPropertyChangeSupport().addPropertyChangeListener(propName, l);
	}

	/**
	 * Remove a property change listener.  This will remove any listener that was added
	 * through either of the addPropertyListener methods.
	 * @param l The listener to remove.
	 */
	public void removePropertyChangeListener(PropertyChangeListener l) {
		getPropertyChangeSupport().removePropertyChangeListener(l);
	}

}
