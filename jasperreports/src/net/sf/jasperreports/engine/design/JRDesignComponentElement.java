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

import net.sf.jasperreports.engine.JRComponentElement;
import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRDefaultStyleProvider;
import net.sf.jasperreports.engine.JRExpressionCollector;
import net.sf.jasperreports.engine.JRVisitor;
import net.sf.jasperreports.engine.component.Component;
import net.sf.jasperreports.engine.component.ComponentKey;
import net.sf.jasperreports.engine.component.ComponentManager;
import net.sf.jasperreports.engine.component.ComponentsEnvironment;
import net.sf.jasperreports.engine.design.events.JRPropertyChangeSupport;

/**
 * A {@link JRComponentElement} implementation which is to be used at report
 * design time.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id: JRCrosstab.java 1741 2007-06-08 10:53:33Z lucianc $
 */
public class JRDesignComponentElement extends JRDesignElement implements JRComponentElement
{

	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

	public static final String PROPERTY_COMPONENT = "component";
	public static final String PROPERTY_COMPONENT_KEY = "componentKey";
	
	private ComponentKey componentKey;
	private Component component;
	
	private transient JRPropertyChangeSupport eventSupport;

	/**
	 * Creates an empty component element.
	 */
	public JRDesignComponentElement()
	{
		super(null);
	}

	/**
	 * Creates an empty component element.
	 * 
	 * @param defaultStyleProvider the default style provide to use for this
	 * element
	 */
	public JRDesignComponentElement(JRDefaultStyleProvider defaultStyleProvider)
	{
		super(defaultStyleProvider);
	}
	
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
	
	public void collectExpressions(JRExpressionCollector collector)
	{
		ComponentManager manager = ComponentsEnvironment.getComponentsRegistry().getComponentManager(componentKey);
		manager.getComponentCompiler().collectExpressions(component, collector);
	}

	public void visit(JRVisitor visitor)
	{
		visitor.visitComponentElement(this);
	}

	public Component getComponent()
	{
		return component;
	}

	/**
	 * Sets the component instance wrapped by this element.
	 * 
	 * @param component the component instance
	 * @see #getComponent()
	 */
	public void setComponent(Component component)
	{
		Object old = this.component;
		this.component = component;
		getEventSupport().firePropertyChange(PROPERTY_COMPONENT, old, this.component);
	}

	public ComponentKey getComponentKey()
	{
		return componentKey;
	}

	/**
	 * Sets the component type key that corresponds to the component instance.
	 * 
	 * @param componentKey the component type key
	 * @see #getComponentKey()
	 */
	public void setComponentKey(ComponentKey componentKey)
	{
		Object old = this.componentKey;
		this.componentKey = componentKey;
		getEventSupport().firePropertyChange(PROPERTY_COMPONENT_KEY, old, this.componentKey);
	}

}
