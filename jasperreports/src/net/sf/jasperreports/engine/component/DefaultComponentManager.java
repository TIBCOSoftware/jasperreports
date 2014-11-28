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
package net.sf.jasperreports.engine.component;

import net.sf.jasperreports.engine.JasperReportsContext;

/**
 * A default {@link ComponentManager component manager} implementation.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 */
public class DefaultComponentManager implements ComponentManager
{

	private ComponentCompiler componentCompiler;
	private ComponentXmlWriter componentXmlWriter;
	private ComponentFillFactory componentFillFactory;
	private ComponentDesignConverter componentDesignConverter;

	public ComponentFillFactory getComponentFillFactory(JasperReportsContext jasperReportsContext)
	{
		return componentFillFactory;
	}

	/**
	 * Sets the fill component factory implementation.
	 * 
	 * @param fillFactory the fill component factory 
	 * @see #getComponentFillFactory(JasperReportsContext)
	 */
	public void setComponentFillFactory(ComponentFillFactory fillFactory)
	{
		this.componentFillFactory = fillFactory;
	}

	
	public ComponentCompiler getComponentCompiler(JasperReportsContext jasperReportsContext)
	{
		return componentCompiler;
	}

	/**
	 * Sets the component compiler implementation.
	 * 
	 * @param componentCompiler the component compiler
	 * @see #getComponentCompiler(JasperReportsContext)
	 */
	public void setComponentCompiler(ComponentCompiler componentCompiler)
	{
		this.componentCompiler = componentCompiler;
	}
	
	public ComponentXmlWriter getComponentXmlWriter(JasperReportsContext jasperReportsContext)
	{
		return componentXmlWriter;
	}

	/**
	 * Sets the component XML writer implementation.
	 * 
	 * @param componentXmlWriter the component XML writer
	 * @see #getComponentXmlWriter(JasperReportsContext)
	 */
	public void setComponentXmlWriter(ComponentXmlWriter componentXmlWriter)
	{
		this.componentXmlWriter = componentXmlWriter;
	}

	public ComponentDesignConverter getDesignConverter(JasperReportsContext jasperReportsContext)
	{
		return componentDesignConverter;
	}

	/**
	 * Sets the design component preview converter.
	 * 
	 * @param designConverter the design component preview converter
	 */
	public void setDesignConverter(ComponentDesignConverter designConverter)
	{
		this.componentDesignConverter = designConverter;
	}

}
