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
package net.sf.jasperreports.engine.export;

import net.sf.jasperreports.engine.JRGenericElementType;

/**
 * A bundle of {@link GenericElementHandler generic element handlers} that share
 * the same namespace.
 * 
 * <p>
 * Such a bundle can contain handlers for several generic element types, each
 * having a unique name.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 * @see JRGenericElementType
 */
public interface GenericElementHandlerBundle
{

	/**
	 * Returns the namespace of this bundle.
	 * 
	 * @return the bundle namespace
	 */
	String getNamespace();
	
	/**
	 * Returns a generic element handler for a element type name and an
	 * exporter key.
	 * 
	 * @param elementName the element type name
	 * @param exporterKey the exporter key
	 * @return a generic element handler for the combination
	 */
	GenericElementHandler getHandler(String elementName, 
			String exporterKey);

}
