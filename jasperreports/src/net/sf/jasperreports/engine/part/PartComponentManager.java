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
package net.sf.jasperreports.engine.part;

import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.component.ComponentKey;
import net.sf.jasperreports.engine.component.ComponentsBundle;
import net.sf.jasperreports.engine.component.ComponentsEnvironment;

/**
 * A part component manager is the entry point through which the handlers for a
 * single part component type can be accessed.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id: ComponentManager.java 6929 2014-02-25 15:46:46Z teodord $
 * @see ComponentsEnvironment#getManager(ComponentKey)
 * @see ComponentsBundle#getComponentManager(String)
 */
public interface PartComponentManager
{

	/**
	 * Returns the component compiler.
	 * 
	 * @return the component compiler
	 */
	PartComponentCompiler getComponentCompiler(JasperReportsContext jasperReportsContext);

	/**
	 * Returns the component XML writer.
	 * 
	 * @return the component XML writer
	 */
	PartComponentXmlWriter getComponentXmlWriter(JasperReportsContext jasperReportsContext);
	
	/**
	 * Returns the factory of fill component instances.
	 * 
	 * @return the factory of fill component instances
	 */
	PartComponentFillFactory getComponentFillFactory(JasperReportsContext jasperReportsContext);
	
}
