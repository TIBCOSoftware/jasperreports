/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2011 Jaspersoft Corporation. All rights reserved.
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

/**
 * A default {@link ComponentManager component manager} implementation.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public class DefaultComponentManager implements ComponentManager
{

	private ComponentCompiler componentCompiler;
	private ComponentXmlWriter componentXmlWriter;
	private ComponentFillFactory componentFillFactory;
	private ComponentDesignConverter componentDesignConverter;

	public ComponentFillFactory getComponentFillFactory()
	{
		return componentFillFactory;
	}

	/**
	 * Sets the fill component factory implementation.
	 * 
	 * @param fillFactory the fill component factory 
	 * @see #getComponentFillFactory()
	 */
	public void setComponentFillFactory(ComponentFillFactory fillFactory)
	{
		this.componentFillFactory = fillFactory;
	}

	
	public ComponentCompiler getComponentCompiler()
	{
		return componentCompiler;
	}

	/**
	 * Sets the component compiler implementation.
	 * 
	 * @param componentCompiler the component compiler
	 * @see #getComponentCompiler()
	 */
	public void setComponentCompiler(ComponentCompiler componentCompiler)
	{
		this.componentCompiler = componentCompiler;
	}
	
	public ComponentXmlWriter getComponentXmlWriter()
	{
		return componentXmlWriter;
	}

	/**
	 * Sets the component XML writer implementation.
	 * 
	 * @param componentXmlWriter the component XML writer
	 * @see #getComponentXmlWriter()
	 */
	public void setComponentXmlWriter(ComponentXmlWriter componentXmlWriter)
	{
		this.componentXmlWriter = componentXmlWriter;
	}

	public ComponentDesignConverter getDesignConverter()
	{
		return componentDesignConverter;
	}

	/**
	 * Sets the design component preview converter.
	 * 
	 * @param designConverter the design component preview converter
	 */
	public void setDesignConverter(
			ComponentDesignConverter designConverter)
	{
		this.componentDesignConverter = designConverter;
	}

}
