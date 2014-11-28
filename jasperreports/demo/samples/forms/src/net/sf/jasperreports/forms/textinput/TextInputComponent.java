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
package net.sf.jasperreports.forms.textinput;

import java.io.Serializable;

import net.sf.jasperreports.engine.JRDefaultStyleProvider;
import net.sf.jasperreports.engine.JRTextField;
import net.sf.jasperreports.engine.base.JRBaseObjectFactory;
import net.sf.jasperreports.engine.component.BaseComponentContext;
import net.sf.jasperreports.engine.component.ComponentContext;
import net.sf.jasperreports.engine.component.ContextAwareComponent;
import net.sf.jasperreports.engine.design.JRDesignTextField;
import net.sf.jasperreports.engine.design.events.JRChangeEventsSupport;
import net.sf.jasperreports.engine.design.events.JRPropertyChangeSupport;

/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public class TextInputComponent implements ContextAwareComponent, Serializable, JRChangeEventsSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static final String PROPERTY_MULTI_LINE = "multiLine";

	private JRTextField textField;
	private boolean multiLine;

	private ComponentContext context;

	private transient JRPropertyChangeSupport eventSupport;
	
	public TextInputComponent(JRDefaultStyleProvider defaultStyleProvider) 
	{
		textField = new JRDesignTextField(defaultStyleProvider);
	}
	
	public TextInputComponent(TextInputComponent component, JRBaseObjectFactory objectFactory) 
	{
		this.textField = (JRTextField)objectFactory.getVisitResult(component.textField);
		this.multiLine = component.isMultiLine();

		this.context = new BaseComponentContext(component.getContext(), objectFactory);
	}

	public void setContext(ComponentContext context)
	{
		this.context = context;
	}
	
	public ComponentContext getContext()
	{
		return context;
	}
	
	/**
	 *
	 */
	public JRTextField getTextField() 
	{
		return textField;
	}

	/**
	 *
	 */
	public boolean isMultiLine() 
	{
		return multiLine;
	}

	/**
	 *
	 */
	public void setMultiLine(boolean multiLine) 
	{
		boolean old = this.multiLine;
		this.multiLine = multiLine;
		getEventSupport().firePropertyChange(PROPERTY_MULTI_LINE, old, this.multiLine);
	}

	/**
	 *
	 */
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
