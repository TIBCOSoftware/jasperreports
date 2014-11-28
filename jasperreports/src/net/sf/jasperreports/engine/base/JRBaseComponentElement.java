/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2014 TIBCO Software Inc. All rights reserved.
 * http://www.jaspersoft.com
 *
 * Unless you have purchased a commercial license agreement from Jaspersoft,
 * the following license terms apply:
 *
 * This program is part of JasperReports.
 *
 * JasperReports is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * JasperReports is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with JasperReports. If not, see <http://www.gnu.org/licenses/>.
 */
package net.sf.jasperreports.engine.base;

import net.sf.jasperreports.engine.DefaultJasperReportsContext;
import net.sf.jasperreports.engine.JRComponentElement;
import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRExpressionCollector;
import net.sf.jasperreports.engine.JRVisitable;
import net.sf.jasperreports.engine.JRVisitor;
import net.sf.jasperreports.engine.component.Component;
import net.sf.jasperreports.engine.component.ComponentKey;
import net.sf.jasperreports.engine.component.ComponentManager;
import net.sf.jasperreports.engine.component.ComponentsEnvironment;

/**
 * A read-only {@link JRComponentElement} implementation which is included
 * in compiled reports.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 */
public class JRBaseComponentElement extends JRBaseElement implements
		JRComponentElement
{

	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

	private ComponentKey componentKey;
	private Component component;
	
	public JRBaseComponentElement(JRComponentElement element, JRBaseObjectFactory factory)
	{
		super(element, factory);
		
		componentKey = element.getComponentKey();
		
		ComponentManager manager = ComponentsEnvironment.getInstance(DefaultJasperReportsContext.getInstance()).getManager(componentKey);
		component = manager.getComponentCompiler(DefaultJasperReportsContext.getInstance()).toCompiledComponent(
				element.getComponent(), factory);
	}

	public Component getComponent()
	{
		return component;
	}

	public ComponentKey getComponentKey()
	{
		return componentKey;
	}

	public void collectExpressions(JRExpressionCollector collector)
	{
		ComponentManager manager = ComponentsEnvironment.getInstance(DefaultJasperReportsContext.getInstance()).getManager(componentKey);
		manager.getComponentCompiler(DefaultJasperReportsContext.getInstance()).collectExpressions(component, collector);
	}

	public void visit(JRVisitor visitor)
	{
		visitor.visitComponentElement(this);
		
		if (component instanceof JRVisitable)
		{
			((JRVisitable) component).visit(visitor);
		}
	}

}
