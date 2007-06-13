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
package net.sf.jasperreports.engine.query;

import net.sf.jasperreports.engine.JRValueParameter;


/**
 * A query clause handling context, as seen from a {@link JRClauseFunction clause function}.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public interface JRQueryClauseContext
{

	/**
	 * Returns the query text buffer.
	 * 
	 * @return the query text buffer
	 */
	StringBuffer queryBuffer();
	
	/**
	 * Return a value parameter from the report parameters map.
	 * 
	 * @param parameterName the parameter name
	 * @return the parameter
	 */
	JRValueParameter getValueParameter(String parameterName);
	
	/**
	 * Records a query parameter.
	 * 
	 * @param parameterName the parameter name
	 */
	void addQueryParameter(String parameterName);
	
	/**
	 * Records a multi-valued query parameter.
	 * 
	 * @param parameterName the parameter name
	 * @param count the value count
	 */
	void addQueryMultiParameters(String parameterName, int count);

}
