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

import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.base.JRBaseParameter;

import java.beans.PropertyChangeSupport;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JRDesignParameter extends JRBaseParameter
{
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
	private static final long serialVersionUID = 606;

	/**
	 *
	 */
	public void setName(String name)
	{
        Object oldValue = this.name;
		this.name = name;
        propSupport.firePropertyChange(NAME_PROPERTY, oldValue, this.name);
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
        propSupport.firePropertyChange(VALUE_CLASS_PROPERTY, oldValue, this.valueClassName);
	}

	/**
	 *
	 */
	public void setSystemDefined(boolean isSystemDefined)
	{
        boolean oldValue = this.isSystemDefined;
		this.isSystemDefined = isSystemDefined;
        propSupport.firePropertyChange(SYSTEM_DEFINED_PROPERTY, oldValue,
                this.isSystemDefined);
	}

	/**
	 *
	 */
	public void setForPrompting(boolean isForPrompting)
	{
        boolean oldValue = this.isForPrompting;
		this.isForPrompting = isForPrompting;
        propSupport.firePropertyChange(PROMPTING_PROPERTY, oldValue, this.isForPrompting);
	}

	/**
	 *
	 */
	public void setDefaultValueExpression(JRExpression expression)
	{
        Object oldValue = this.defaultValueExpression;
		this.defaultValueExpression = expression;
        propSupport.firePropertyChange(DEFAULT_VALUE_EXPRESSION, oldValue,
                this.defaultValueExpression);
	}


}
