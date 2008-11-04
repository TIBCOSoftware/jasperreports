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
package net.sf.jasperreports.engine.xml;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.component.Component;
import net.sf.jasperreports.engine.component.ComponentKey;
import net.sf.jasperreports.engine.design.JRDesignComponentElement;

import org.apache.commons.digester.Rule;

/**
 * A digester rule that links a {@link Component} object with its parent
 * {@link JRDesignComponentElement}.
 * 
 * <p>
 * This rules also sets the {@link ComponentKey component type key} on the
 * component element via
 * {@link JRDesignComponentElement#setComponentKey(ComponentKey)}.
 * The component type key is created based on information from the XML
 * component node; the node namespace is used as component type namespace
 * and the node name is used as component name.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id: JRCrosstab.java 1741 2007-06-08 10:53:33Z lucianc $
 */
public class JRComponentRule extends Rule
{
	
	public static JRComponentRule newInstance()
	{
		return new JRComponentRule();
	}
	
	public void end(String namespace, String name) throws JRException
	{
		Object top = getDigester().peek();
		if (!(top instanceof Component))
		{
			throw new JRException("Object of type " + top.getClass().getName() + " is not a "
					+ Component.class.getName() + " instance");
		}

		Component component = (Component) top;
		JRDesignComponentElement componentElement = (JRDesignComponentElement) getDigester().peek(1);
		String namespacePrefix = ((JRXmlDigester) getDigester()).getLastNamespacePrefix();
		ComponentKey componentKey = new ComponentKey(namespace, namespacePrefix, name);
		componentElement.setComponentKey(componentKey);
		componentElement.setComponent(component);
	}
	
}
