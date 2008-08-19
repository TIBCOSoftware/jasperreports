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
package net.sf.jasperreports.engine.component;

import java.util.Collection;

/**
 * A registry of component bundles.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id: JRCrosstab.java 1741 2007-06-08 10:53:33Z lucianc $
 */
public interface ComponentsRegistry
{

	/**
	 * Returns the set of all component bundles present in the registry. 
	 * 
	 * @return the set of component bundles
	 */
	Collection getComponentBundles();

	/**
	 * Returns a component bundle that corresponds to a namespace.
	 * 
	 * @param namespace a component bundle namespace
	 * @return the corresponding component bundle
	 * @throws JRRuntimeException if no bundle corresponding to the namespace
	 * is found in the registry
	 */
	ComponentsBundle getComponentsBundle(String namespace);

	/**
	 * Returns a component manager that corresponds to a particular component
	 * type key.
	 * 
	 * @param componentKey the component type key
	 * @return the manager for the component type
	 * @throws JRRuntimeException if the registry does not contain the specified
	 * component type
	 */
	ComponentManager getComponentManager(ComponentKey componentKey);

}
