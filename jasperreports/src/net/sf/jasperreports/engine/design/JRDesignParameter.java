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

import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.base.JRBaseParameter;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JRDesignParameter extends JRBaseParameter
{
	/**
	 *
	 */
	private static final long serialVersionUID = 10001;

	/** Mechanism for firing property change events. */
	private PropertyChangeSupport propSupport = new PropertyChangeSupport(this);

	/** Bean property name for the parameter's name. */
	public static final String NAME_PROPERTY = "name";

	/** Bean property name for the value class name. */
	public static final String VALUE_CLASS_PROPERTY = "valueClassName";

	/** Bean property name for system defined parameter. */
	public static final String SYSTEM_DEFINED_PROPERTY = "systemDefined";

	/** Bean property name for prompting. */
	public static final String PROMPTING_PROPERTY = "forPrompting";

	/** Bean property name for default value expression. */
	public static final String DEFAULT_VALUE_EXPRESSION = "defaultValueExpression";


	/**
	 *
	 */
	public void setName(String name)
	{
		Object oldValue = this.name;
		this.name = name;
		getPropertyChangeSupport().firePropertyChange(NAME_PROPERTY, oldValue, this.name);
	}
	
	/**
	 *
	 */
	public void setValueClass(Class clazz)
	{
		setValueClassName(clazz.getName());
	}

	/**
	 *
	 */
	public void setValueClassName(String className)
	{
		Object oldValue = this.valueClassName;
		valueClassName = className;
		valueClass = null;
		getPropertyChangeSupport().firePropertyChange(VALUE_CLASS_PROPERTY, oldValue, this.valueClassName);
	}

	/**
	 *
	 */
	public void setSystemDefined(boolean isSystemDefined)
	{
		boolean oldValue = this.isSystemDefined;
		this.isSystemDefined = isSystemDefined;
		getPropertyChangeSupport().firePropertyChange(SYSTEM_DEFINED_PROPERTY, oldValue,
				this.isSystemDefined);
	}

	/**
	 *
	 */
	public void setForPrompting(boolean isForPrompting)
	{
		boolean oldValue = this.isForPrompting;
		this.isForPrompting = isForPrompting;
		getPropertyChangeSupport().firePropertyChange(PROMPTING_PROPERTY, oldValue, this.isForPrompting);
	}

	/**
	 *
	 */
	public void setDefaultValueExpression(JRExpression expression)
	{
		Object oldValue = this.defaultValueExpression;
		this.defaultValueExpression = expression;
		getPropertyChangeSupport().firePropertyChange(DEFAULT_VALUE_EXPRESSION, oldValue,
				this.defaultValueExpression);
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
